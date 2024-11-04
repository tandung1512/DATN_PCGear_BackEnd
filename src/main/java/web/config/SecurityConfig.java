package web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF protection (use cautiously)
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/**").permitAll() 
                .requestMatchers("/**").permitAll() 
                .requestMatchers("/pcgearhub/**").permitAll() 
                .anyRequest().authenticated() // Other requests require authentication
            );

        return http.build(); // Build the SecurityFilterChain
    }
}
