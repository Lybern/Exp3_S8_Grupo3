package cl.duoc.auth_server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;

import cl.duoc.auth_server.security.OAuth2LoginSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  private final OAuth2LoginSuccessHandler successHandler;

  public SecurityConfig(OAuth2LoginSuccessHandler successHandler) {
    this.successHandler = successHandler;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http)
      throws Exception {

    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(
                "/",
                "/api/auth/**",
                "/oauth2/**",
                "/login/**",
                "/.well-known/jwks.json",
                "/actuator/health",
                "/actuator/info")
            .permitAll()
            .anyRequest().authenticated())
        .oauth2Login(oauth2 -> oauth2
            .successHandler(successHandler))
        .logout(Customizer.withDefaults());

    return http.build();
  }

}
