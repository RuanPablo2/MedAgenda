package com.medagenda.med_auth_service.services;

import com.medagenda.med_auth_service.dtos.LoginDTO;
import com.medagenda.med_auth_service.dtos.TokenResponseDTO;
import com.medagenda.med_auth_service.dtos.UserRegisterDTO;
import com.medagenda.med_auth_service.dtos.UserResponseDTO;
import com.medagenda.med_auth_service.entities.User;
import com.medagenda.med_auth_service.repositories.UserRepository;
import com.medagenda.med_commom.enums.Role;
import com.medagenda.med_commom.exceptions.BusinessException;
import com.medagenda.med_commom.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenService tokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User("Dr. Bruno", "bruno@medagenda.com", "hash-super-seguro", Role.DOCTOR);
        mockUser.setId(1L);
    }

    @Test
    @DisplayName("Should successfully register a new user and encrypt the password")
    void registerUser_Success() {
        UserRegisterDTO request = new UserRegisterDTO("Dr. Bruno", "bruno@medagenda.com", "senha123", Role.DOCTOR);

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.password())).thenReturn("hash-super-seguro");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        UserResponseDTO response = authService.registerUser(request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("bruno@medagenda.com", response.email());

        verify(passwordEncoder, times(1)).encode("senha123");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw BusinessException AUTH_001 when email is already in use")
    void registerUser_ThrowsException_EmailAlreadyInUse() {
        UserRegisterDTO request = new UserRegisterDTO("Dr. Bruno", "bruno@medagenda.com", "senha123", Role.DOCTOR);

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(mockUser));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            authService.registerUser(request);
        });

        assertEquals("AUTH_001", exception.getErrorCode());

        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should successfully authenticate user and return JWT token")
    void authenticate_Success() {
        LoginDTO loginDTO = new LoginDTO("bruno@medagenda.com", "senha123");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(mockUser);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(tokenService.generateToken(mockUser)).thenReturn("eyJhbGciOiJIUzI1NiIsInR5c.Falso.Token");

        TokenResponseDTO response = authService.authenticate(loginDTO);

        assertNotNull(response);
        assertEquals("eyJhbGciOiJIUzI1NiIsInR5c.Falso.Token", response.token());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService, times(1)).generateToken(mockUser);
    }

    @Test
    @DisplayName("Should return user details when ID exists")
    void getUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        UserResponseDTO response = authService.getUserById(1L);

        assertNotNull(response);
        assertEquals("Dr. Bruno", response.name());
        assertEquals(Role.DOCTOR, response.role());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException AUTH_002 when user ID does not exist")
    void getUserById_ThrowsException_NotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            authService.getUserById(99L);
        });

        assertEquals("AUTH_002", exception.getErrorCode());
    }
}