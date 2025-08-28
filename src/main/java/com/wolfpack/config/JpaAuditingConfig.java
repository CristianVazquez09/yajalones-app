// com/wolfpack/config/JpaAuditingConfig.java
package com.wolfpack.config;

import com.wolfpack.repo.IUsuarioRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
@RequiredArgsConstructor
public class JpaAuditingConfig {

    private final IUsuarioRepo usuarioRepo;

    @Bean
    AuditorAware<Integer> auditorAware() {
        return () -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()) return Optional.empty();

            // Con filtro propio, el principal suele ser UserDetails o String (username)
            String username = null;
            Object principal = auth.getPrincipal();
            if (principal instanceof UserDetails ud) {
                username = ud.getUsername();
            } else if (principal instanceof String s) {
                username = s;
            } else {
                username = auth.getName(); // fallback
            }
            if (username == null || username.isBlank()) return Optional.empty();

            return usuarioRepo.findIdByNombreUsuario(username); // Optional<Integer>
        };
    }
}
