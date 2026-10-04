package cl.duoc.auth_server.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import cl.duoc.auth_server.services.JwtTokenService;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component 
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

  private final JwtTokenService jwtTokenService;
  private final ObjectMapper objectMapper;

  public OAuth2LoginSuccessHandler(JwtTokenService jwtTokenService, ObjectMapper objectMapper) {
    this.jwtTokenService = jwtTokenService;
    this.objectMapper = objectMapper;
  }

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

    OAuth2User githubUser = oauthToken.getPrincipal();

    String githubLogin = githubUser.getAttribute("login");
    String email = githubUser.getAttribute("email");
    String name = githubUser.getAttribute("name");

    String jwt = jwtTokenService.generateToken(
        githubLogin,
        email,
        name);

    Map<String, Object> tokenResponse = new LinkedHashMap<>();
    tokenResponse.put("tokenType", "Bearer");
    tokenResponse.put("accessToken", jwt);
    tokenResponse.put("expiresIn", 3600);
    tokenResponse.put("githubUser", githubLogin);
    tokenResponse.put("email", email);

    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    objectMapper.writeValue(
        response.getOutputStream(),
        tokenResponse);

  }

}
