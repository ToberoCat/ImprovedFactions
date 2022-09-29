package io.github.toberocat.improvedFactions.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.toberocat.improvedFactions.core.gui.ItemContainer;
import io.github.toberocat.improvedFactions.core.item.ItemStack;

import java.io.IOException;
import java.util.Base64;

public class MapKeySerializer extends JsonSerializer<ItemContainer> {

    @Override
    public void serialize(ItemContainer value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        if (value.stack() == null) {
            gen.writeFieldName(value.slot() + " null");
        } else {
            gen.writeFieldName(value.slot() + " " + value.stack().toBase64());
        }
    }
}
