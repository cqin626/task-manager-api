package com.cqin.taskmanagerapi.common.security;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class RSAKeyConfig {

   @Value("classpath:keys/public.pem")
   private Resource publicKeyResource;

   @Value("classpath:keys/private.pem")
   private Resource privateKeyResource;

   @Bean
   public RSAPublicKey publicKey() throws Exception {
      String key = new String(publicKeyResource.getInputStream().readAllBytes());
      String pem = key.replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
            .replaceAll("\\s", "");
      byte[] decoded = Base64.getDecoder().decode(pem);
      return (RSAPublicKey) KeyFactory.getInstance("RSA")
            .generatePublic(new X509EncodedKeySpec(decoded));
   }

   @Bean
   public RSAPrivateKey privateKey() throws Exception {
      String key = new String(privateKeyResource.getInputStream().readAllBytes());
      String pem = key.replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            .replaceAll("\\s", "");
      byte[] decoded = Base64.getDecoder().decode(pem);
      return (RSAPrivateKey) KeyFactory.getInstance("RSA")
            .generatePrivate(new PKCS8EncodedKeySpec(decoded));
   }
}
