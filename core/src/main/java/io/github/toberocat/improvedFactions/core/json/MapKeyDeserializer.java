package io.github.toberocat.improvedFactions.core.json;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import io.github.toberocat.improvedFactions.core.handler.ItemHandler;

import java.io.IOException;
import java.util.Base64;

public class MapKeyDeserializer extends KeyDeserializer {

    @Override
    public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
        return ItemHandler.api().deserializeBytes(Base64.getDecoder().decode(key));
    }
}
