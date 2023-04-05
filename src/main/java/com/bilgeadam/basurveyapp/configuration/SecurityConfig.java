package com.bilgeadam.basurveyapp.configuration;

import com.bilgeadam.basurveyapp.configuration.jwt.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * This class is used to configure the security settings of the application.
 * It enables web security and provides a bean of type {@link SecurityFilterChain} to configure the HttpSecurity to handle requests.
 *
 * @author Eralp Nitelik
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    /**
     * @author Eralp Nitelik
     */

    private static final String[] WHITELIST = {
            "/auth/**",
            "/test",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/bus/v3/api-docs/**",
            "/swagger-resources/**",
            "/configuration/security",
            "/swagger-resources/configuration/ui",
            "/swagger-resources/configuration/security",
            "/webjars/**",
            "/v2/api-docs/**",
            "/v3/api-docs/**",
            "/survey/response/**",
            "/question/getsurveyquestions/**",
//            "/role/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                // requests below do not need authentication.
                .authorizeHttpRequests()
                .requestMatchers(WHITELIST)
                .permitAll()
                // all other requests require authentication.
                .anyRequest()
                .authenticated()
                .and()
                // authentication should not be stored thus stateless session.
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // show spring which authentication provider to use.
                .authenticationProvider(authenticationProvider)
                // to execute our custom filter before UsernamePasswordAuthenticationFilter, this allows us to set securityContext with our filter.
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
