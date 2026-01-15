package com.cqin.taskmanagerapi.common.logging;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LoggingFilter extends OncePerRequestFilter {

   private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

   @Override
   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
         throws ServletException, IOException {
      Map<String, String> headers = new HashMap<>();
      Enumeration<String> headerNames = request.getHeaderNames();

      while (headerNames.hasMoreElements()) {
         String name = headerNames.nextElement();
         if (name.equalsIgnoreCase("authorization") ||
               name.equalsIgnoreCase("cookie")) {
            headers.put(name, "*****");
         } else {
            headers.put(name, request.getHeader(name));
         }
      }

      logger.info("Incoming request: method={} uri={} headers={}",
            request.getMethod(), request.getRequestURI(), headers);

      filterChain.doFilter(request, response);

      logger.info("Outgoing response: status={}", response.getStatus());
   }
}
