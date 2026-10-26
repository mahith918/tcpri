package org.tcpri.workspace.config;

import org.tcpri.workspace.user.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BootstrapAdmin {
    @Bean
    CommandLineRunner createInitialAdmin(
            UserRepository users, PasswordEncoder encoder,
            @Value("${app.bootstrap-admin.email:}") String email,
            @Value("${app.bootstrap-admin.password:}") String password,
            @Value("${app.bootstrap-admin.name:Initial Administrator}") String name) {
        return args -> {
            if (email == null || email.isBlank() || password == null || password.isBlank()) {
                System.out.println("Bootstrap admin not created: set BOOTSTRAP_ADMIN_EMAIL and BOOTSTRAP_ADMIN_PASSWORD.");
                return;
            }
            if (!users.existsByEmailIgnoreCase(email)) {
                AppUser admin = new AppUser(name, email, encoder.encode(password));
                admin.setRole(Role.ADMIN);
                admin.setStatus(AccountStatus.APPROVED);
                users.save(admin);
                System.out.println("Initial administrator account created.");
            }
        };
    }
}
