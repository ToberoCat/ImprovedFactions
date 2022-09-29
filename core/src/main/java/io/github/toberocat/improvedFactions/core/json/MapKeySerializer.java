package io.github.toberocat.improvedFactions.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.toberocat.improvedFactions.core.item.ItemStack;

import java.io.IOException;
import java.util.Base64;

public class MapKeySerializer extends JsonSerializer<ItemStack> {

    @Override
    public void serialize(ItemStack value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        gen.writeFieldName(Base64.getEncoder().encodeToString(value.toBase64()));
    }
}
