package cl.duoc.auth_server.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Map;
import com.nimbusds.jose.jwk.JWKSet;

@RestController 
public class JwkSetController {

  private final JWKSet publicJwkSet;

  public JwkSetController(JWKSet publicJwkSet) {
    this.publicJwkSet = publicJwkSet;
  }

  // Endpoint to expose the public JWK set
  @GetMapping("/.well-known/jwks.json")
  public Map<String, Object> getPublicKeys() {
    return publicJwkSet.toJSONObject();
  }



}
