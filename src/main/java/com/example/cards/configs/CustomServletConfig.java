package com.example.cards.configs;

import org.springframework.boot.web.server.MimeMappings;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Configuration;

/** The Custom servlet configuration. */
@Configuration
public class CustomServletConfig
    implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {
  /**
   * allow usage of custom mime types (js, css)
   *
   * @param factory the web server factory to customize
   */
  @Override
  public void customize(ConfigurableServletWebServerFactory factory) {
    MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
    mappings.remove("js");
    mappings.add("js", "application/javascript;charset=utf-8");
    mappings.remove("css");
    mappings.add("css", "text/css;charset=utf-8");
    factory.setMimeMappings(mappings);
  }
}
