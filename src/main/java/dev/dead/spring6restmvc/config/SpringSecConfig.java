package dev.dead.spring6restmvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {
        http.csrf(httpSecurityCsrfConfigurer ->
                httpSecurityCsrfConfigurer
                        .ignoringRequestMatchers("/api/**"));
        return http.build();

    }
}
