package com.example.cards.configs;

import com.example.cards.deserializers.CardTypeDeserializer;
import com.example.cards.entities.dict.CardType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class JacksonConfiguration {

  @Bean
  public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
    ObjectMapper objectMapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addDeserializer(CardType.class, new CardTypeDeserializer());
    objectMapper.registerModule(module);

    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
    converter.setObjectMapper(objectMapper);
    return converter;
  }

  /*@Bean
  public Module hibernate5Module() {
      return new Hibernate5Module();
  }

  @Bean
  public ObjectMapper objectMapper() {
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.registerModule(hibernate5Module());
      return objectMapper;
  }*/
}
