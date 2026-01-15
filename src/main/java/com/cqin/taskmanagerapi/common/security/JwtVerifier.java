package com.cqin.taskmanagerapi.common.security;

import java.security.interfaces.RSAPublicKey;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cqin.taskmanagerapi.common.exceptions.httpexceptions.UnauthorizedException;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Service
public class JwtVerifier {
   private final RSAPublicKey publicKey;

   public JwtVerifier(RSAPublicKey publicKey) {
      this.publicKey = publicKey;
   }

   public Map<String, Object> getVerifiedAndParsedToken(String token) {
      try {
         return Jwts.parser()
               .verifyWith(publicKey)
               .build()
               .parseSignedClaims(token)
               .getPayload();
      } catch (JwtException e) {
         throw new UnauthorizedException("Invalid or expired token");
      }
   }
}
