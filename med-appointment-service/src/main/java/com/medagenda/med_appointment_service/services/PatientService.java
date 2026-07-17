package com.medagenda.med_appointment_service.services;

import com.medagenda.med_appointment_service.dtos.PatientRequestDTO;
import com.medagenda.med_appointment_service.dtos.PatientResponseDTO;
import com.medagenda.med_appointment_service.entities.Patient;
import com.medagenda.med_appointment_service.repositories.PatientRepository;
import com.medagenda.med_commom.exceptions.BusinessException;
import org.springframework.stereotype.Service;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public PatientResponseDTO createPatient(PatientRequestDTO data) {
        if (patientRepository.findByCpf(data.cpf()).isPresent()) {
            throw new BusinessException("CPF already registered in the system", "APP_005");
        }

        Patient patient = new Patient(
                data.fullName(),
                data.cpf(),
                data.phone(),
                data.birthDate()
        );

        Patient savedPatient = patientRepository.save(patient);

        return new PatientResponseDTO(
                savedPatient.getId(),
                savedPatient.getFullName(),
                savedPatient.getCpf(),
                savedPatient.getPhone(),
                savedPatient.getBirthDate()
        );
    }
}