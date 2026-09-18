package com.bmvll.backend.security;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bmvll.backend.model.User;
import com.bmvll.backend.model.UserRole;
import com.bmvll.backend.model.UserStatus;
import com.bmvll.backend.repository.UserRepository;

/**
 * Solo ADMIN/LIBRARIAN pueden autenticarse en esta fase; MEMBER no tiene login (ver TODO.md).
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Credenciales inválidas"));

        if (user.getRole() != UserRole.ADMIN && user.getRole() != UserRole.LIBRARIAN) {
            throw new UsernameNotFoundException("Credenciales inválidas");
        }
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new UsernameNotFoundException("Credenciales inválidas");
        }
        if (user.getPasswordHash() == null || user.getPasswordHash().isBlank()) {
            throw new UsernameNotFoundException("Credenciales inválidas");
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPasswordHash())
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())))
                .build();
    }
}
