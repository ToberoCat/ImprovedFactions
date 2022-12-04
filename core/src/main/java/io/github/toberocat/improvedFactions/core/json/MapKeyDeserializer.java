package io.github.toberocat.improvedFactions.core.json;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import io.github.toberocat.improvedFactions.core.handler.ItemHandler;

import java.io.IOException;
import java.util.Objects;

public class MapKeyDeserializer extends KeyDeserializer { // ToDo: Fix Serilaizer

    @Override
    public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
        return null;
/*        String[] parts = key.split(" ");
        int slot = Integer.parseInt(parts[0]);

        String itemBase = parts[1];
        if (Objects.equals(itemBase, "null")) return new ItemContainer(slot,
                ItemHandler.api().createStack("minecraft:air", "Air", 0));
        return new ItemContainer(slot, ItemHandler.api().fromBase64(parts[1]));*/
    }
}
