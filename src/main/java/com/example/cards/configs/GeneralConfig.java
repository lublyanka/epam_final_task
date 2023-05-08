package com.example.cards.configs;

import io.jsonwebtoken.security.Keys;
import java.security.Key;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.servlet.DispatcherServlet;

/** The General config. */
@Configuration
public class GeneralConfig {
  /**
   * Dispatcher registration servlet registration bean.
   *
   * @return the servlet registration bean
   */
  @Bean
  public ServletRegistrationBean<?> dispatcherRegistration() {
    return new ServletRegistrationBean<>(dispatcherServlet());
  }

  /**
   * Dispatcher servlet configuration. Log all requests and responses with exceptions in single
   * place them in console
   *
   * @return the dispatcher servlet
   */
  @Bean(name = DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_BEAN_NAME)
  public DispatcherServlet dispatcherServlet() {
    return new LoggableDispatcherServlet();
  }

  /**
   * Gets jwt key.
   *
   * @param jwtSecretString the jwt secret string
   * @return the jwt key
   */
  @Bean
  @Qualifier("jwtKey")
  public Key getJwtKey(@Value("${jwt.secret}") String jwtSecretString) {
    return Keys.hmacShaKeyFor(jwtSecretString.getBytes());
  }

  /**
   * Password encoder password encoder.
   *
   * @return the BCryptPasswordEncoder instance with 10 rounds of hashing
   */
  @Bean
  @Qualifier("passwordEncoder")
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(10);
  }

  /**
   * Security context repository attribute name string.
   *
   * @return the string
   */
  @Bean
  @Qualifier("securityContextRepositoryAttributeName")
  public String securityContextRepositoryAttributeName() {
    return RequestAttributeSecurityContextRepository.DEFAULT_REQUEST_ATTR_NAME;
  }

  /**
   * Security context repository security context repository. In Spring Security 6, the default
   * behavior is that the SecurityContextHolderFilter will only read the SecurityContext from
   * SecurityContextRepository and populate it in the SecurityContextHolder. Users now must
   * explicitly save the SecurityContext with the SecurityContextRepository if they want the
   * SecurityContext to persist between requests. This removes ambiguity and improves performance by
   * only requiring writing to the SecurityContextRepository (i.e. HttpSession) when it is
   * necessary.
   *
   * @param attributeName the attribute name
   * @return the security context repository
   */
  @Bean
  @Qualifier("securityContextRepository")
  public SecurityContextRepository securityContextRepository(
      @Qualifier("securityContextRepositoryAttributeName") String attributeName) {
    return new RequestAttributeSecurityContextRepository(attributeName);
  }
}
