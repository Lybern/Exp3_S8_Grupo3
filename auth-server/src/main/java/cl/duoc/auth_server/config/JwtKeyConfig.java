package cl.duoc.auth_server.config;

import java.security.interfaces.RSAPublicKey;
import java.util.UUID;
import java.security.interfaces.RSAPrivateKey;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import com.nimbusds.jose.proc.SecurityContext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;


@Configuration 
public class JwtKeyConfig {

  @Bean
  public RSAKey rsaKey() {
    KeyPair keyPair = generateKeyPair();

    RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
    RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

    return new RSAKey.Builder(publicKey)
        .privateKey(privateKey)
        .keyID(UUID.randomUUID().toString())
        .build();

  }

  @Bean
  public JWKSource<SecurityContext> jwkSource(RSAKey rsaKey) {
    JWKSet jwkSet = new JWKSet(rsaKey);
    return new ImmutableJWKSet<>(jwkSet);
  }

  @Bean
  public JWKSet publicJwkSet(RSAKey rsaKey) {
    return new JWKSet(rsaKey.toPublicJWK());
  }

  @Bean
  public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
    return new NimbusJwtEncoder(jwkSource);
  }  

  private KeyPair generateKeyPair() {
    try {
      KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
      keyPairGenerator.initialize(2048);
      return keyPairGenerator.generateKeyPair();
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }

  }

}
