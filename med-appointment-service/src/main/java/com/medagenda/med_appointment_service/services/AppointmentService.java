package com.medagenda.med_appointment_service.services;

import com.medagenda.med_appointment_service.dtos.AppointmentStatusUpdateDTO;
import com.medagenda.med_appointment_service.dtos.UserResponseDTO;
import com.medagenda.med_appointment_service.entities.enums.AppointmentStatus;
import com.medagenda.med_appointment_service.integration.AuthClient;
import com.medagenda.med_commom.exceptions.BusinessException;
import com.medagenda.med_appointment_service.dtos.AppointmentRequestDTO;
import com.medagenda.med_appointment_service.dtos.AppointmentResponseDTO;
import com.medagenda.med_appointment_service.entities.Appointment;
import com.medagenda.med_appointment_service.entities.Insurance;
import com.medagenda.med_appointment_service.entities.Patient;
import com.medagenda.med_appointment_service.repositories.AppointmentRepository;
import com.medagenda.med_appointment_service.repositories.InsuranceRepository;
import com.medagenda.med_appointment_service.repositories.PatientRepository;
import com.medagenda.med_commom.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final InsuranceRepository insuranceRepository;
    private final AuthClient authClient;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              PatientRepository patientRepository,
                              InsuranceRepository insuranceRepository,
                              AuthClient authClient) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.insuranceRepository = insuranceRepository;
        this.authClient = authClient;
    }

    public AppointmentResponseDTO scheduleAppointment(AppointmentRequestDTO data) {
        if (data.insuranceId() == null && data.price() == null) {
            throw new BusinessException("An appointment must have either an insurance or a private price", "APP_001");
        }
        if (data.insuranceId() != null && data.price() != null) {
            throw new BusinessException("An appointment cannot have both insurance and a private price simultaneously", "APP_002");
        }

        Patient patient = patientRepository.findById(data.patientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found", "APP_003"));

        Insurance insurance = null;
        if (data.insuranceId() != null) {
            insurance = insuranceRepository.findById(data.insuranceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Insurance not found", "APP_004"));
        }

        UserResponseDTO doctor = null;
        try {
            doctor = authClient.getUserById(data.doctorId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("Failed to retrieve doctor information. Auth service might be unavailable.", "APP_005");
        }

        Appointment appointment = new Appointment(
                data.doctorId(),
                doctor.name(),
                patient,
                insurance,
                data.price(),
                data.scheduledAt()
        );

        appointment = appointmentRepository.save(appointment);

        String insuranceName = (insurance != null) ? insurance.getName() : "Particular";

        return new AppointmentResponseDTO(
                appointment.getId(),
                appointment.getDoctorId(),
                appointment.getDoctorName(),
                appointment.getPatient().getId(),
                patient.getFullName(),
                insuranceName,
                appointment.getPrice(),
                appointment.getScheduledAt(),
                appointment.getStatus()
        );
    }

    public void updateAppointmentStatus(Long id, AppointmentStatusUpdateDTO data) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found", "APP_008"));

        AppointmentStatus currentStatus = appointment.getStatus();
        AppointmentStatus newStatus = data.status();

        if (currentStatus == newStatus) {
            return;
        }

        boolean isValidTransition = switch (currentStatus) {
            case SCHEDULED -> newStatus == AppointmentStatus.WAITING || newStatus == AppointmentStatus.CANCELED;
            case WAITING -> newStatus == AppointmentStatus.IN_PROGRESS || newStatus == AppointmentStatus.CANCELED;
            case IN_PROGRESS -> newStatus == AppointmentStatus.FINISHED || newStatus == AppointmentStatus.CANCELED;
            case FINISHED, CANCELED -> false;
        };

        if (!isValidTransition) {
            throw new BusinessException(
                    "Invalid status transition from " + currentStatus + " to " + newStatus,
                    "APP_009"
            );
        }

        appointment.setStatus(newStatus);
        appointmentRepository.save(appointment);
    }

    public List<AppointmentResponseDTO> getCalendar(LocalDateTime startDate, LocalDateTime endDate) {

        if (startDate.isAfter(endDate)) {
            throw new BusinessException("Start date cannot be after end date", "APP_010");
        }

        List<Appointment> appointments = appointmentRepository.findByScheduledAtBetween(startDate, endDate);

        return mapToDTOList(appointments);
    }

    public List<AppointmentResponseDTO> getTodayAgendaForDoctor(Long doctorId) {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);

        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndScheduledAtBetween(doctorId, startOfDay, endOfDay);

        return mapToDTOList(appointments);
    }

    private List<AppointmentResponseDTO> mapToDTOList(List<Appointment> appointments) {
        return appointments.stream().map(appointment -> {
            String insuranceName = (appointment.getInsurance() != null)
                    ? appointment.getInsurance().getName()
                    : "Particular";

            return new AppointmentResponseDTO(
                    appointment.getId(),
                    appointment.getDoctorId(),
                    appointment.getDoctorName(),
                    appointment.getPatient().getId(),
                    appointment.getPatient().getFullName(),
                    insuranceName,
                    appointment.getPrice(),
                    appointment.getScheduledAt(),
                    appointment.getStatus()
            );
        }).toList();
    }

    public AppointmentResponseDTO getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found", "APP_008"));

        String insuranceName = (appointment.getInsurance() != null)
                ? appointment.getInsurance().getName()
                : "Particular";

        return new AppointmentResponseDTO(
                appointment.getId(),
                appointment.getDoctorId(),
                appointment.getDoctorName(),
                appointment.getPatient().getId(),
                appointment.getPatient().getFullName(),
                insuranceName,
                appointment.getPrice(),
                appointment.getScheduledAt(),
                appointment.getStatus()
        );
    }
}