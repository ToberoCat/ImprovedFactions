package io.github.toberocat.core.utility.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.github.toberocat.core.utility.Utility;

import java.io.InputStream;
import java.util.concurrent.atomic.AtomicReference;

public class YmlUtility {

    private static final ObjectMapper om = new ObjectMapper(new YAMLFactory());

    public static <T> T loadYml(InputStream in, Class<T> clazz) {
        AtomicReference<T> t = new AtomicReference<>();
        Utility.run(() -> t.set(om.readValue(in, clazz)));

        return t.get();
    }
}
