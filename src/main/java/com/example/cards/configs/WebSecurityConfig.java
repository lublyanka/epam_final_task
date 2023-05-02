package com.example.cards.configs;

import com.example.cards.filters.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
  @Autowired private JwtTokenFilter jwtTokenFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    /*
     * For these reasons,
     * the SecurityContextPersistenceFilter has been deprecated to be replaced with the SecurityContextHolderFilter.
     * In Spring Security 6, the default behavior is that the SecurityContextHolderFilter will only read the SecurityContext from SecurityContextRepository
     *  and populate it in the SecurityContextHolder. Users now must explicitly save the SecurityContext with the SecurityContextRepository if they
     *  want the SecurityContext to persist between requests. This removes ambiguity and improves performance by only requiring writing
     * to the SecurityContextRepository (i.e. HttpSession) when it is necessary.
     * */

    http.csrf()
        .disable()
        .sessionManagement(
            (session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            (requests) ->
                requests
                    // .anyRequest().hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                    // .requestMatchers( "/api/admin/**")
                    //  .hasAuthority("ROLE_ADMIN")
                    // .authenticated()
                    .requestMatchers(
                        "/*",
                        "/js/**",
                        "/images/*",
                        "/lang/*",
                        "/css/*",
                        "/api/auth/registration",
                        "/api/auth/login")
                    .permitAll()
                    .anyRequest()
                    .permitAll() // this should be changed to .anyRequest().authenticated()
            );
    http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();

    //    http.csrf().disable()
    //            .authorizeHttpRequests(
    //            (requests) ->
    //                requests
    //                    .requestMatchers("/*",
    //                            "/js/**",
    //                            "/images/*",
    //                            "/lang/*",
    //                            "/css/*",
    //                            "/api/auth/registration",
    //                            "/api/auth/login").permitAll()
    //                    // .anyRequest().hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
    //                        .requestMatchers("/api/admin/**")
    //                        .authenticated()
    //                        .anyRequest().permitAll() //this should be changed to
    // .anyRequest().authenticated()
    //                        .and()
    //                        .addFilterBefore(
    //                                jwtTokenFilter,
    //                                UsernamePasswordAuthenticationFilter.class))
    //    ;
    //    return http.build();
  }
}
