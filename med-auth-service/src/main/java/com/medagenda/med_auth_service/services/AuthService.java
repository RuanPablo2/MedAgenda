package com.medagenda.med_auth_service.services;

import com.medagenda.med_auth_service.dtos.LoginDTO;
import com.medagenda.med_auth_service.dtos.TokenResponseDTO;
import com.medagenda.med_auth_service.dtos.UserRegisterDTO;
import com.medagenda.med_auth_service.dtos.UserResponseDTO;
import com.medagenda.med_auth_service.entities.User;
import com.medagenda.med_auth_service.repositories.UserRepository;
import com.medagenda.med_commom.exceptions.BusinessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository, TokenService tokenService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    public TokenResponseDTO authenticate(LoginDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        String token = tokenService.generateToken((User) auth.getPrincipal());
        return new TokenResponseDTO(token);
    }

    public UserResponseDTO registerUser(UserRegisterDTO data) {
        if (this.userRepository.findByEmail(data.email()).isPresent()) {
            throw new BusinessException("Email already in use", "AUTH_001");
        }

        String encryptedPassword = passwordEncoder.encode(data.password());
        User newUser = new User(data.email(), encryptedPassword, data.role());

        User savedUser = this.userRepository.save(newUser);

        return new UserResponseDTO(savedUser.getId(), savedUser.getEmail(), savedUser.getRole());
    }
}