package dev.luismartinez.reservaciones.application.config;

import dev.luismartinez.reservaciones.application.lasting.EMessage;
import dev.luismartinez.reservaciones.application.service.user.UserGenericService;
import dev.luismartinez.reservaciones.application.service.user.UserServiceMongo;
import dev.luismartinez.reservaciones.domain.repository.jpa.UserRepositoryJpa;
import dev.luismartinez.reservaciones.domain.repository.mongo.UserRepositoryMongo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

  private final UserRepositoryMongo userRepositoryMongo;
  private final UserRepositoryJpa userRepositoryJpa;
  private final UserGenericService userService;

  @Bean
  public UserDetailsService userDetailsService() {
    if (userService instanceof UserServiceMongo) {
      return username -> userRepositoryMongo.findUserByEmail(username)
              .orElseThrow(() -> new UsernameNotFoundException(
                      EMessage.USER_NOT_FOUND.getMessage()
              ));
    } else {
      return username -> userRepositoryJpa.findUserByEmail(username)
              .orElseThrow(() -> new UsernameNotFoundException(
                      EMessage.USER_NOT_FOUND.getMessage()
              ));
    }


  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authenticationProvider
      = new DaoAuthenticationProvider();
    // Indexar el UserDetailsService
    authenticationProvider.setUserDetailsService(
      userDetailsService()
    );
    // Indexar el cifrado de contraseñas
    authenticationProvider.setPasswordEncoder(
      passwordEncoder()
    );
    return authenticationProvider;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(
    AuthenticationConfiguration configuration
  ) throws Exception {
    return configuration.getAuthenticationManager();
  }

}
