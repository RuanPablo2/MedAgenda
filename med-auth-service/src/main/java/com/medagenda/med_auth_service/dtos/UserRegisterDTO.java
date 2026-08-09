package com.medagenda.med_auth_service.dtos;

import com.medagenda.med_commom.enums.Role;

public record UserRegisterDTO(
        String name,
        String email,
        String password,
        Role role
) {}