package com.pausaparakdramas.PausaParaKdramas.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private FirebaseTokenFilter firebaseTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> {}) // usa la config de WebMvcConfigurer
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/public/**", "/index.html").permitAll()
                        .requestMatchers("/usuarios/me", "/usuarios/sync", "/usuarios/**").authenticated()
                        .requestMatchers("/api/upload").authenticated() // <--- agregar esto
                        .anyRequest().authenticated()
                )
                .addFilterBefore(firebaseTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
