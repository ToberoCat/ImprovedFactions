package io.github.toberocat.core.utility.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.Utility;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;

public class JsonUtility {

    private static final ObjectMapper om = new ObjectMapper();

    public static boolean saveObject(File file, Object object) {
        try {
            om.writerWithDefaultPrettyPrinter().writeValue(file, object);
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
            return om.writeValueAsString(object);
        } catch (IOException e) {
            MainIF.logMessage(Level.WARNING, "Couldn't save file to string, because of " + e.getMessage());
            if (Boolean.TRUE.equals(MainIF.getConfigManager().getValue("general.printStacktrace"))) e.printStackTrace();
            return "";
        }
    }

    public static Object readObject(String object, Class clazz) {
        try {
            return om.readValue(object, clazz);
        } catch (IOException e) {
            MainIF.logMessage(Level.WARNING, "Couldn't read file " + object +" because of " + e.getMessage());
            if (Boolean.TRUE.equals(MainIF.getConfigManager().getValue("general.printStacktrace"))) e.printStackTrace();
        }
        return null;
    }

    public static Object readObject(File file, Class clazz) throws IOException {
        return om.readValue(file, clazz);
    }

    public static Object readObjectFromURL(URL url, Class clazz) {
        try {
            return om.readValue(url, clazz);
        } catch (IOException e) {
            MainIF.logMessage(Level.WARNING, "Couldn't read url because of " + e.getMessage());
            if (Boolean.TRUE.equals(MainIF.getConfigManager().getValue("general.printStacktrace"))) e.printStackTrace();
        }
        return null;
    }
}
