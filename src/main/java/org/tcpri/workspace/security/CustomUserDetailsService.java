package org.tcpri.workspace.security;

import org.tcpri.workspace.user.AccountStatus;
import org.tcpri.workspace.user.AppUser;
import org.tcpri.workspace.user.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository users;

    public CustomUserDetailsService(UserRepository users) {
        this.users = users;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser user = users.findByEmailIgnoreCase(email)
            .filter(u -> u.getStatus() == AccountStatus.APPROVED)
            .orElseThrow(() -> new UsernameNotFoundException("Account not found or not approved"));
        return User.withUsername(user.getEmail())
            .password(user.getPasswordHash())
            .roles(user.getRole().name())
            .build();
    }
}
