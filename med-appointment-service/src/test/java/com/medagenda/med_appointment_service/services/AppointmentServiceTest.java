package com.medagenda.med_appointment_service.services;

import com.medagenda.med_appointment_service.dtos.AppointmentRequestDTO;
import com.medagenda.med_appointment_service.dtos.AppointmentResponseDTO;
import com.medagenda.med_appointment_service.dtos.AppointmentStatusUpdateDTO;
import com.medagenda.med_appointment_service.dtos.UserResponseDTO;
import com.medagenda.med_appointment_service.entities.Appointment;
import com.medagenda.med_appointment_service.entities.Patient;
import com.medagenda.med_appointment_service.entities.enums.AppointmentStatus;
import com.medagenda.med_appointment_service.integration.AuthClient;
import com.medagenda.med_appointment_service.repositories.AppointmentRepository;
import com.medagenda.med_appointment_service.repositories.InsuranceRepository;
import com.medagenda.med_appointment_service.repositories.PatientRepository;
import com.medagenda.med_commom.enums.Role;
import com.medagenda.med_commom.exceptions.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private InsuranceRepository insuranceRepository;

    @Mock
    private AuthClient authClient;

    @InjectMocks
    private AppointmentService appointmentService;

    private Patient mockPatient;
    private UserResponseDTO mockDoctor;

    @BeforeEach
    void setUp() {
        mockPatient = new Patient("João da Silva", "12345678900", "999999999", LocalDate.of(1990, 1, 1));
        mockPatient.setId(1L);

        mockDoctor = new UserResponseDTO(2L, "Dr. Bruno", "bruno@medagenda.com", Role.DOCTOR);
    }

    @Test
    @DisplayName("Should successfully schedule a private appointment")
    void scheduleAppointment_Success_PrivatePrice() {
        AppointmentRequestDTO request = new AppointmentRequestDTO(
                2L, 1L, null, new BigDecimal("250.00"), LocalDateTime.now().plusDays(1)
        );

        when(patientRepository.findById(1L)).thenReturn(Optional.of(mockPatient));
        when(authClient.getUserById(2L)).thenReturn(mockDoctor);

        Appointment savedAppointment = new Appointment(
                2L, "Dr. Bruno", mockPatient, null, new BigDecimal("250.00"), request.scheduledAt()
        );
        savedAppointment.setId(10L);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(savedAppointment);

        AppointmentResponseDTO response = appointmentService.scheduleAppointment(request);

        assertNotNull(response);
        assertEquals(10L, response.id());
        assertEquals("Particular", response.insuranceName());
        assertEquals("Dr. Bruno", response.doctorName());

        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    @DisplayName("Should throw BusinessException APP_001 when no price and no insurance are provided")
    void scheduleAppointment_ThrowsException_NoPriceNoInsurance() {
        AppointmentRequestDTO request = new AppointmentRequestDTO(
                2L, 1L, null, null, LocalDateTime.now().plusDays(1) // Ambos null
        );

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            appointmentService.scheduleAppointment(request);
        });

        assertEquals("APP_001", exception.getErrorCode());

        verify(appointmentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw BusinessException APP_002 when both price and insurance are provided")
    void scheduleAppointment_ThrowsException_BothPriceAndInsurance() {
        AppointmentRequestDTO request = new AppointmentRequestDTO(
                2L, 1L, 3L, new BigDecimal("250.00"), LocalDateTime.now().plusDays(1) // Ambos preenchidos
        );

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            appointmentService.scheduleAppointment(request);
        });

        assertEquals("APP_002", exception.getErrorCode());
        verify(appointmentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should successfully update appointment status from SCHEDULED to WAITING")
    void updateAppointmentStatus_Success_ValidTransition() {
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        AppointmentStatusUpdateDTO updateDTO = new AppointmentStatusUpdateDTO(AppointmentStatus.WAITING);

        appointmentService.updateAppointmentStatus(1L, updateDTO);

        assertEquals(AppointmentStatus.WAITING, appointment.getStatus());
        verify(appointmentRepository, times(1)).save(appointment);
    }

    @Test
    @DisplayName("Should throw BusinessException APP_009 when transition is invalid (CANCELED to FINISHED)")
    void updateAppointmentStatus_ThrowsException_InvalidTransition() {
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setStatus(AppointmentStatus.CANCELED);

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        AppointmentStatusUpdateDTO updateDTO = new AppointmentStatusUpdateDTO(AppointmentStatus.FINISHED);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            appointmentService.updateAppointmentStatus(1L, updateDTO);
        });

        assertEquals("APP_009", exception.getErrorCode());
        assertTrue(exception.getMessage().contains("Invalid status transition"));

        verify(appointmentRepository, never()).save(any());
    }
}