package cl.duoc.auth_server.services;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenService {

    private final JwtEncoder jwtEncoder;
    private final String issuer;
    private final long expirationSeconds;

    public JwtTokenService(
            JwtEncoder jwtEncoder,
            @Value("${ms.auth.jwt.issuer:http://localhost:8080}") String issuer,
            @Value("${ms.auth.jwt.expiration-seconds:3600}") long expirationSeconds) {

        this.jwtEncoder = jwtEncoder;
        this.issuer = issuer;
        this.expirationSeconds = expirationSeconds;
    }

    public String generateToken(String githubLogin, String email, String name) {
        Instant now = Instant.now();

        JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expirationSeconds))
                .subject(githubLogin)
                .claim("provider", "github")
                .claim("scope", "transacciones.read transacciones.write");

        if (email != null && !email.isEmpty()) {
            claimsBuilder.claim("email", email);
        }

        if (name != null && !name.isEmpty()) {
            claimsBuilder.claim("name", name);
        }

        JwtClaimsSet claims = claimsBuilder.build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }



}
