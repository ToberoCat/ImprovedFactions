package io.github.toberocat.core.utility.jackson;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.jackson.deserializer.LocationDeserializer;
import io.github.toberocat.core.utility.jackson.serializer.LocationSerializer;
import org.bukkit.Location;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;

public class JsonUtility {

    public static final ObjectMapper OBJECT_MAPPER = createMapper();

    private static ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new SimpleModule()
                .addSerializer(Location.class, new LocationSerializer())
                .addDeserializer(Location.class, new LocationDeserializer()));

        mapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
        return mapper;
    }

    public static boolean saveObject(File file, Object object) {
        try {
            OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValue(file, object);
            return true;
        } catch (IOException e) {
            MainIF.logMessage(Level.WARNING, "Couldn't save file " + file.getName() + " to "
                    + file.getAbsolutePath() + ", because of " + e.getMessage());
            if (Boolean.TRUE.equals(MainIF.getConfigManager().getValue("general.printStacktrace"))) e.printStackTrace();
            return false;
        }
    }

    public static String saveObject(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (IOException e) {
            MainIF.logMessage(Level.WARNING, "Couldn't save file to string, because of " + e.getMessage());
            if (Boolean.TRUE.equals(MainIF.getConfigManager().getValue("general.printStacktrace"))) e.printStackTrace();
            return "";
        }
    }

    public static <T> T readObject(InputStream in, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(in, clazz);
        } catch (IOException e) {
            MainIF.logMessage(Level.WARNING, "Couldn't read file because of " + e.getMessage());
            if (Boolean.TRUE.equals(MainIF.getConfigManager().getValue("general.printStacktrace"))) e.printStackTrace();
        }
        return null;
    }

    public static Object readObject(String object, Class clazz) {
        try {
            return OBJECT_MAPPER.readValue(object, clazz);
        } catch (IOException e) {
            MainIF.logMessage(Level.WARNING, "Couldn't read file " + object + " because of " + e.getMessage());
            if (Boolean.TRUE.equals(MainIF.getConfigManager().getValue("general.printStacktrace"))) e.printStackTrace();
        }
        return null;
    }

    public static Object readObject(File file, Class clazz) throws IOException {
        return OBJECT_MAPPER.readValue(file, clazz);
    }

    public static Object readObjectFromURL(URL url, Class clazz) {
        try {
            return OBJECT_MAPPER.readValue(url, clazz);
        } catch (IOException e) {
            MainIF.logMessage(Level.WARNING, "Couldn't read url because of " + e.getMessage());
            if (Boolean.TRUE.equals(MainIF.getConfigManager().getValue("general.printStacktrace"))) e.printStackTrace();
        }
        return null;
    }
}
