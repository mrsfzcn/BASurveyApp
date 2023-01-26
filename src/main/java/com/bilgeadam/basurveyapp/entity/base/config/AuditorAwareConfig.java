package com.bilgeadam.basurveyapp.entity.base.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author Eralp Nitelik
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class AuditorAwareConfig {
    @Bean
    public AuditorAware<Long> auditorAware() {
        return new CustomAuditorAware();
    }
}
