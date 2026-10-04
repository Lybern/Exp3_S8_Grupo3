package cl.duoc.auth_server.controllers;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @GetMapping("/login")
  public ResponseEntity<Map<String, String>> login() {
    return ResponseEntity.ok(
      Map.of(
        "message", "Para autenticar con GitHub debe ingresar a /oauth2/authorization/github",
        "authorizationUrl", "http://localhost:8080/oauth2/authorization/github"
      ));
  }

}
