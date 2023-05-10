package com.example.cards.configs;

import com.example.cards.filters.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextRepository;

/** The Web security config. */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
  private final JwtTokenFilter jwtTokenFilter;

  /*
   * In Spring Security 6, the default behavior is that the SecurityContextHolderFilter will only read the SecurityContext from SecurityContextRepository
   *  and populate it in the SecurityContextHolder. Users now must explicitly save the SecurityContext with the SecurityContextRepository if they
   *  want the SecurityContext to persist between requests. This removes ambiguity and improves performance by only requiring writing
   * to the SecurityContextRepository (i.e. HttpSession) when it is necessary.
   * */

  @Qualifier("securityContextRepository")
  private final SecurityContextRepository securityContextRepository;

  /**
   * Security filter chain.
   *
   * @param http the http
   * @return the security filter chain
   * @throws Exception the exception
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http.csrf().disable();
    http.sessionManagement(
            (session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .securityContext(x -> x.securityContextRepository(securityContextRepository))
        .authorizeHttpRequests(
            (requests) ->
                requests
                    .requestMatchers("/api/admin/**")
                    .hasAuthority("ROLE_ADMIN")
                    .requestMatchers(
                        "/*",
                        "/js/**",
                        "/images/*",
                        "/lang/*",
                        "/css/*",
                        "/api/auth/registration",
                        "/api/auth/login",
                        "/account/*",
                        "/payment/*",
                        "/users/*",
                        "swagger-ui/**",
                        "/v3/api-docs",
                        "/v3/api-docs/**")
                    .permitAll()
                    .anyRequest()
                    .hasAnyAuthority("ROLE_ADMIN", "ROLE_USER"));
    http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    http.formLogin().disable();
    return http.build();
  }
}
