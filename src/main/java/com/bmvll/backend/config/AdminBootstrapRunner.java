package com.bmvll.backend.config;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.bmvll.backend.model.User;
import com.bmvll.backend.model.UserRole;
import com.bmvll.backend.model.UserStatus;
import com.bmvll.backend.repository.UserRepository;

/**
 * Crea un ADMIN inicial si todavía no existe ninguno, usando BOOTSTRAP_ADMIN_EMAIL /
 * BOOTSTRAP_ADMIN_PASSWORD (.env). Solo para arrancar el sistema en dev/local — no hay endpoint
 * de auto-registro público (ver TODO.md: solo ADMIN puede crear cuentas vía /api/auth/register).
 */
@Component
@Profile("!test")
public class AdminBootstrapRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminBootstrapRunner.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String bootstrapEmail;
    private final String bootstrapPassword;

    public AdminBootstrapRunner(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.bootstrap-admin.email:}") String bootstrapEmail,
            @Value("${app.bootstrap-admin.password:}") String bootstrapPassword) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.bootstrapEmail = bootstrapEmail;
        this.bootstrapPassword = bootstrapPassword;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (bootstrapEmail.isBlank() || bootstrapPassword.isBlank()) {
            return;
        }
        if (userRepository.existsByRole(UserRole.ADMIN)) {
            return;
        }

        Instant now = Instant.now();
        User admin = User.builder()
                .firstName("Admin")
                .lastName("Bootstrap")
                .email(bootstrapEmail)
                .passwordHash(passwordEncoder.encode(bootstrapPassword))
                .role(UserRole.ADMIN)
                .status(UserStatus.ACTIVE)
                .createdAt(now)
                .updatedAt(now)
                .build();

        userRepository.save(admin);
        log.info("Admin inicial creado: {}", bootstrapEmail);
    }
}
