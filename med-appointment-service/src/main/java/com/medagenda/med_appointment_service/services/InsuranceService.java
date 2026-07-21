package com.medagenda.med_appointment_service.services;

import com.medagenda.med_appointment_service.dtos.InsuranceRequestDTO;
import com.medagenda.med_appointment_service.dtos.InsuranceResponseDTO;
import com.medagenda.med_appointment_service.entities.Insurance;
import com.medagenda.med_appointment_service.repositories.InsuranceRepository;
import com.medagenda.med_commom.exceptions.BusinessException;
import org.springframework.stereotype.Service;

@Service
public class InsuranceService {

    private final InsuranceRepository insuranceRepository;

    public InsuranceService(InsuranceRepository insuranceRepository) {
        this.insuranceRepository = insuranceRepository;
    }

    public InsuranceResponseDTO createInsurance(InsuranceRequestDTO data) {
        if (insuranceRepository.findByNameIgnoreCase(data.name()).isPresent()) {
            throw new BusinessException("Insurance name already registered in the catalog", "APP_006");
        }

        Insurance insurance = new Insurance(
                data.name(),
                true
        );

        Insurance savedInsurance = insuranceRepository.save(insurance);

        return new InsuranceResponseDTO(
                savedInsurance.getId(),
                savedInsurance.getName(),
                savedInsurance.isActive()
        );
    }
}