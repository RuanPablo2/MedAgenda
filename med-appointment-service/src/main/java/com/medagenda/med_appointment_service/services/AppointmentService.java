package com.medagenda.med_appointment_service.services;

import com.medagenda.med_commom.exceptions.BusinessException;
import com.medagenda.med_appointment_service.dtos.AppointmentRequestDTO;
import com.medagenda.med_appointment_service.dtos.AppointmentResponseDTO;
import com.medagenda.med_appointment_service.entities.Appointment;
import com.medagenda.med_appointment_service.entities.Insurance;
import com.medagenda.med_appointment_service.entities.Patient;
import com.medagenda.med_appointment_service.repositories.AppointmentRepository;
import com.medagenda.med_appointment_service.repositories.InsuranceRepository;
import com.medagenda.med_appointment_service.repositories.PatientRepository;
import org.springframework.stereotype.Service;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final InsuranceRepository insuranceRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, PatientRepository patientRepository, InsuranceRepository insuranceRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.insuranceRepository = insuranceRepository;
    }

    public AppointmentResponseDTO scheduleAppointment(AppointmentRequestDTO data) {
        if (data.insuranceId() == null && data.price() == null) {
            throw new BusinessException("An appointment must have either an insurance or a private price", "APP_001");
        }
        if (data.insuranceId() != null && data.price() != null) {
            throw new BusinessException("An appointment cannot have both insurance and a private price simultaneously", "APP_002");
        }

        Patient patient = patientRepository.findById(data.patientId())
                .orElseThrow(() -> new BusinessException("Patient not found", "APP_003"));

        Insurance insurance = null;
        if (data.insuranceId() != null) {
            insurance = insuranceRepository.findById(data.insuranceId())
                    .orElseThrow(() -> new BusinessException("Insurance not found", "APP_004"));
        }

        Appointment appointment = new Appointment(
                data.doctorId(),
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
                patient.getFullName(),
                insuranceName,
                appointment.getPrice(),
                appointment.getScheduledAt(),
                appointment.getStatus()
        );
    }
}