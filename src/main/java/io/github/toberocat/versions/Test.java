package io.github.toberocat.versions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.github.toberocat.core.extensions.ExtensionRegistry;
import io.github.toberocat.core.utility.jackson.YmlUtility;
import io.github.toberocat.core.utility.version.Version;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;


public class Test {
    public static void main(String[] args) throws IOException {

        ArrayList<UUID> arrayList = new ArrayList<>();

        for (int i = 0; i < 100; i++) arrayList.add(UUID.randomUUID());

        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            String[] array = arrayList.stream().map(x -> x.toString() + x.getMostSignificantBits()).toArray(String[]::new);
        }
    }
}
