package com.bilgeadam.basurveyapp.entity.base.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * @author Eralp Nitelik
 */
public class CustomAuditorAware implements AuditorAware<Long> {
    @Override
    @NonNull
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        if (!"anonymousUser".equals(authentication.getPrincipal())) {
            return Optional.of((Long) authentication.getCredentials());
        }
        // 0 if saved by unauthenticated person.
        return Optional.of(0L);
    }
}
