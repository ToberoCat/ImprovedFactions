package io.github.toberocat.improvedfactions.utility.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.toberocat.improvedfactions.utility.Utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class JsonUtility {

    private static ObjectMapper om = new ObjectMapper();

    public static boolean SaveObject(File file, Object object) {
        try {
            om.writerWithDefaultPrettyPrinter().writeValue(file, object);
            return true;
        } catch (IOException e) {
            Utils.except(e);
            return false;
        }
    }

    public static String SaveObject(Object object) {
        try {
            return om.writeValueAsString(object);
        } catch (IOException e) {
            Utils.except(e);
            return "";
        }
    }

    public static Object ReadObject(String object, Class clazz) {
        try {
            return om.readValue(object, clazz);
        } catch (IOException e) {
            Utils.except(e);
        }
        return null;
    }
    public static Object ReadObject(File file, Class clazz) throws IOException {
        return om.readValue(file, clazz);
    }

    public static Object ReadObjectFromURL(URL url, Class clazz) {
        try {
            return om.readValue(url, clazz);
        } catch (IOException e) {
            Utils.except(e);
        }
        return null;
    }
}
