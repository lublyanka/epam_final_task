package com.example.cards.deserializers;

import com.example.cards.entities.dict.CardType;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;

public class CardTypeDeserializer extends JsonDeserializer<CardType> {
    @Override
    public CardType deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String code = node.get("code").asText();
        return new CardType(code);
    }
}