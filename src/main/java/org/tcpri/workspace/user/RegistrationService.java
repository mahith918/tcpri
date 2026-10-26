package org.tcpri.workspace.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationService {
    private final UserRepository users;
    private final PasswordEncoder encoder;

    public RegistrationService(UserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    @Transactional
    public void register(RegistrationForm form) {
        String email = form.getEmail().trim().toLowerCase();
        if (users.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException("An account with this email already exists.");
        }
        users.save(new AppUser(form.getName().trim(), email, encoder.encode(form.getPassword())));
    }
}
