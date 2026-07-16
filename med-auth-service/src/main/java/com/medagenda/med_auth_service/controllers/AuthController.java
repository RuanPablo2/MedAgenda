package com.medagenda.med_auth_service.controllers;

import com.medagenda.med_auth_service.dtos.LoginDTO;
import com.medagenda.med_auth_service.dtos.TokenResponseDTO;
import com.medagenda.med_auth_service.dtos.UserRegisterDTO;
import com.medagenda.med_auth_service.dtos.UserResponseDTO;
import com.medagenda.med_auth_service.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody LoginDTO data) {
        TokenResponseDTO response = authService.authenticate(data);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/users")
    public ResponseEntity<UserResponseDTO> register(@RequestBody UserRegisterDTO data) {
        UserResponseDTO response = authService.registerUser(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}