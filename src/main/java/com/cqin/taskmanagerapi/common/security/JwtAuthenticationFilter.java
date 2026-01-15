package com.cqin.taskmanagerapi.common.security;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
   private final JwtVerifier jwtVerifier;

   public JwtAuthenticationFilter(JwtVerifier jwtVerifier) {
      this.jwtVerifier = jwtVerifier;
   }

   @Override
   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
         throws ServletException, IOException {
      String authHeader = request.getHeader("Authorization");

      if (authHeader != null && authHeader.startsWith("Bearer ")) {
         String accessToken = authHeader.substring(7);

         try {
            Map<String, Object> claims = this.jwtVerifier.getVerifiedAndParsedToken(accessToken);

            Object uidClaim = claims.get("uid");
            Long userId = uidClaim instanceof Integer ? ((Integer) uidClaim).longValue() : (Long) uidClaim;

            String userRole = (String) claims.get("role");

            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + userRole));

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                  userId,
                  null,
                  authorities);

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authToken);
         } catch (Exception e) {
            SecurityContextHolder.clearContext();
         }
      }

      filterChain.doFilter(request, response);
   }
}
