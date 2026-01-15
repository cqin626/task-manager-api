package com.cqin.taskmanagerapi.common.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cqin.taskmanagerapi.common.responses.APIResponse;

import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class SecurityConfig {
   private final JwtAuthenticationFilter jwtAuthFilter;

   public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
      this.jwtAuthFilter = jwtAuthFilter;
   }

   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      ObjectMapper objectMapper = new ObjectMapper();
      http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                  .requestMatchers("/api/v1/users/**").hasRole("ADMIN")
                  .requestMatchers("/api/v1/auth/**").permitAll()
                  .anyRequest().authenticated())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(exceptions -> exceptions
                  .authenticationEntryPoint((req, res, ex) -> {
                     APIResponse<?> apiResponse = APIResponse.error("Unauthorized to access.");
                     res.setContentType("application/json;charset=UTF-8");
                     res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                     objectMapper.writeValue(res.getWriter(), apiResponse);
                     res.getWriter().flush();
                  })
                  .accessDeniedHandler((req, res, ex) -> {
                     APIResponse<?> apiResponse = APIResponse.error("Forbidden to access.");
                     res.setContentType("application/json;charset=UTF-8");
                     res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                     objectMapper.writeValue(res.getWriter(), apiResponse);
                     res.getWriter().flush();
                  }));

      return http.build();
   }
}
