package com.cqin.taskmanagerapi.common.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
   @Bean
   public Argon2PasswordEncoder passwordEncoder() {
      return new Argon2PasswordEncoder(16, 32, 1, 65536, 3);
   }

   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      // TODO: Reconfigure
      http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll()).csrf(csrf -> csrf.disable());
      return http.build();
   }
}
