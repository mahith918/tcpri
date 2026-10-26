package org.tcpri.workspace.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetService {
    private final UserRepository users; private final PasswordResetTokenRepository tokens; private final PasswordEncoder encoder;
    private final JavaMailSender mailSender; private final String from; private final String baseUrl;
    public PasswordResetService(UserRepository users, PasswordResetTokenRepository tokens, PasswordEncoder encoder, JavaMailSender mailSender,
                                @Value("${app.mail.from:}") String from, @Value("${app.base-url:http://localhost:8080}") String baseUrl) {
        this.users = users; this.tokens = tokens; this.encoder = encoder; this.mailSender = mailSender; this.from = from; this.baseUrl = baseUrl;
    }
    public void request(String email) {
        if (email == null || email.isBlank()) return;
        users.findByEmailIgnoreCase(email.trim()).filter(u -> u.getStatus() == AccountStatus.APPROVED).ifPresent(user -> {
            String value = UUID.randomUUID().toString().replace("-", "");
            send(user, tokens.save(new PasswordResetToken(value, user, LocalDateTime.now().plusMinutes(30))).getToken());
        });
    }
    public boolean reset(String value, String password) {
        if (value == null || password == null || password.length() < 12) return false;
        return tokens.findByToken(value).filter(t -> t.getUsedAt() == null && t.getExpiresAt().isAfter(LocalDateTime.now())).map(token -> {
            token.getUser().setPasswordHash(encoder.encode(password)); users.save(token.getUser()); token.markUsed(); tokens.save(token); return true;
        }).orElse(false);
    }
    private void send(AppUser user, String token) {
        if (from == null || from.isBlank()) return;
        SimpleMailMessage message = new SimpleMailMessage(); message.setFrom(from); message.setTo(user.getEmail());
        message.setSubject("Reset your TCPRI password"); message.setText("Hello " + user.getName() + ",\n\nReset your TCPRI password using this link (valid for 30 minutes):\n" + baseUrl + "/reset-password?token=" + token + "\n\nIf you did not request this, ignore this email.");
        try { mailSender.send(message); } catch (MailException ignored) { }
    }
}
