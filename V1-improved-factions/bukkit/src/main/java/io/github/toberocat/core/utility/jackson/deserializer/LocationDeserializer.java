package io.github.toberocat.core.utility.jackson.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.IOException;

public class LocationDeserializer extends StdDeserializer<Location> {
    public LocationDeserializer() {
        this(null);
    }

    public LocationDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Location deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        String worldName = node.get("world").asText();
        double x = node.get("x").numberValue().doubleValue();
        double y = node.get("y").numberValue().doubleValue();
        double z = node.get("z").numberValue().doubleValue();
        float yaw = node.get("yaw").numberValue().floatValue();
        float pitch = node.get("pitch").numberValue().floatValue();

        return new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
    }
}
