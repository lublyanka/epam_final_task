package com.example.cards.configs;

import com.example.cards.LoggableDispatcherServlet;
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
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
public class GeneralConfig {
    @Bean
    public ServletRegistrationBean dispatcherRegistration() {
        return new ServletRegistrationBean(dispatcherServlet());
    }

    @Bean(name = DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_BEAN_NAME)
    public DispatcherServlet dispatcherServlet() {
        return new LoggableDispatcherServlet();
    }

    @Bean
    @Qualifier("jwtKey")
    public Key getJwtKey(@Value("${jwt.secret}") String jwtSecretString) {
        return Keys.hmacShaKeyFor(jwtSecretString.getBytes());
    }

    @Bean
    @Qualifier("passwordEncoder")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

}
