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


public class Test {
    public static void main(String[] args) throws IOException {
        String str = "1.0";
        str = str.replaceAll("[^0-9]", "");
        System.out.println(str);
    }
}
