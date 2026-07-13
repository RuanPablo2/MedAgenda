package com.medagenda.med_auth_service.config;

import com.medagenda.med_auth_service.entities.User;
import com.medagenda.med_auth_service.repositories.UserRepository;
import com.medagenda.med_commom.enums.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            User admin = new User(
                    "admin@medagenda.com",
                    passwordEncoder.encode("admin123"),
                    Role.ADMIN
            );

            userRepository.save(admin);
            System.out.println("✅ [AUTH SERVICE] Default Admin user created: admin@medagenda.com / admin123");
        }
    }
}