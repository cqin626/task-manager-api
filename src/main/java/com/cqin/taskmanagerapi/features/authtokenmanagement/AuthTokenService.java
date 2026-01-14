package com.cqin.taskmanagerapi.features.authtokenmanagement;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Date;
import java.util.HexFormat;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cqin.taskmanagerapi.features.authtokenmanagement.dtos.TokenResponse;
import com.cqin.taskmanagerapi.features.usermanagement.User;

import io.jsonwebtoken.Jwts;

@Service
public class AuthTokenService {
   private final RSAPrivateKey privateKey;
   private final RSAPublicKey publicKey;
   private final AuthTokenRepo authTokenRepo;

   public AuthTokenService(RSAPrivateKey privateKey, RSAPublicKey publicKey, AuthTokenRepo authTokenRepo) {
      this.privateKey = privateKey;
      this.publicKey = publicKey;
      this.authTokenRepo = authTokenRepo;
   }

   private String getTokenHash(String token) {
      MessageDigest digest;
      try {
         digest = MessageDigest.getInstance("SHA-256");
         byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
         return HexFormat.of().formatHex(hash);
      } catch (NoSuchAlgorithmException e) {
         throw new RuntimeException("SHA-256 algorithm not available", e);
      }
   }

   public TokenResponse generateAccessToken(Map<String, Object> claims) {
      long currentTimestamp = System.currentTimeMillis();
      Date issuedAt = new Date(currentTimestamp);
      Date expiresAt = new Date(currentTimestamp + 1000 * 60 * 15);

      String accessToken = Jwts.builder()
            .claims(claims)
            .issuedAt(issuedAt)
            .expiration(expiresAt)
            .signWith(this.privateKey, Jwts.SIG.RS256)
            .compact();

      return new TokenResponse(accessToken, expiresAt.toInstant());
   }

   public TokenResponse generateRefreshToken(Map<String, Object> claims, User user) {
      long currentTimestamp = System.currentTimeMillis();
      Date issuedAt = new Date(currentTimestamp);
      Date expiresAt = new Date(currentTimestamp + 1000 * 60 * 60 * 24 * 7);
      String refreshToken = Jwts.builder()
            .claims(claims)
            .issuedAt(issuedAt)
            .expiration(expiresAt)
            .signWith(this.privateKey, Jwts.SIG.RS256)
            .compact();
      String refreshTokenHash = this.getTokenHash(refreshToken);

      this.authTokenRepo.save(new RefreshToken(refreshTokenHash, issuedAt.toInstant(), expiresAt.toInstant(), user));

      return new TokenResponse(refreshToken, expiresAt.toInstant());
   }

   public Instant getTokenExpiration(String token) {
      return Jwts.parser()
            .verifyWith(this.publicKey)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getExpiration()
            .toInstant();
   }
}
