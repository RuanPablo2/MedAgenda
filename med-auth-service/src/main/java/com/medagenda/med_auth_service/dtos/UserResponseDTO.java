package com.medagenda.med_auth_service.dtos;

import com.medagenda.med_commom.enums.Role;

public record UserResponseDTO(
        Long id,
        String email,
        Role role
) {}