package io.github.toberocat.core.extensions;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.jackson.JsonUtility;
import io.github.toberocat.core.utility.jackson.YmlUtility;
import io.github.toberocat.core.utility.language.LangMessage;
import org.apache.commons.lang.SystemUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import static io.github.toberocat.MainIF.logMessage;

public class ExtensionInitializer {
    public static boolean loadExtensions(@NotNull String data) throws ClassNotFoundException {
        File extFolder = new File(data + "/Extensions");
        if (extFolder.mkdir() && !extFolder.exists()) return false;

        File[] extensions = extFolder.listFiles();
        if (extensions == null) return true;

        List<LangMessage> langMessages = new ArrayList<>();
        for (File jar : extensions) langMessages.addAll(enableExtension(jar, extFolder));

        for (LangMessage message : langMessages) LangMessage.addDefault(message);

        return true;
    }

    private static LinkedList<LangMessage> enableExtension(@NotNull File extensionJar,
                                                           @NotNull File extensionFolder)
            throws ClassNotFoundException {
        if (!extensionJar.getName().endsWith(".jar")) return new LinkedList<>();

        ExtensionRegistry registry = loadRegistry(extensionJar);
        if (registry == null || MainIF.LOADED_EXTENSIONS.containsKey(registry.registry()))
            return new LinkedList<>();

        LinkedList<LangMessage> messages = new LinkedList<>();
        LangMessage extensionLang = getExtensionLangFile(extensionJar);
        if (extensionLang != null) messages.add(extensionLang);

        // Load dependencies first
        for (String extensionDependency : registry.extensionDependencies()) {
            File jar = new File(extensionFolder, extensionDependency + ".jar");
            if (!jar.exists()) continue;

            messages.addAll(enableExtension(jar, extensionFolder));
        }

        Extension extension = ExtensionLoader.loadClass(extensionJar, registry.main(),
                Extension.class);
        extension.enable(registry, MainIF.getIF());

        if (!extension.isEnabled()) return new LinkedList<>();

        MainIF.LOADED_EXTENSIONS.put(extension.getRegistry().registry(), extension);

        return messages;
    }

    private static LangMessage getExtensionLangFile(File file) {
        String path;
        if (SystemUtils.IS_OS_LINUX)
            path = "jar:file:///" + file.getAbsolutePath() + "!/en_us.lang";
        else
            path = "jar:file:\\" + file.getAbsolutePath() + "!/en_us.lang";

        try {
            URL inputURL = new URL(path);
            if (SystemUtils.IS_OS_LINUX) inputURL = inputURL.toURI().toURL();

            JarURLConnection conn = (JarURLConnection) inputURL.openConnection();
            InputStream in = conn.getInputStream();
            return JsonUtility.readObject(in, LangMessage.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            logMessage(Level.SEVERE, "&6" + file.getName() +
                    " &ccouldn't loaded. Couldn't create file url to lang file");
        }

        return null;
    }

    private static ExtensionRegistry loadRegistry(File file) {
        String path;
        if (SystemUtils.IS_OS_LINUX)
            path = "jar:file:///" + file.getAbsolutePath() + "!/extension.yml";
        else
            path = "jar:file:\\" + file.getAbsolutePath() + "!/extension.yml";

        try {
            URL inputURL = new URL(path);
            if (SystemUtils.IS_OS_LINUX) inputURL = inputURL.toURI().toURL();

            JarURLConnection conn;
            conn = (JarURLConnection) inputURL.openConnection();
            InputStream in = conn.getInputStream();
            return YmlUtility.loadYml(in, ExtensionRegistry.class);
        } catch (IOException e) {
            e.printStackTrace();
            logMessage(Level.SEVERE, "&6" + file.getName() + " &ccouldn't get loaded. Please make sure this extension isn't for a beta version, else redownload it please");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            logMessage(Level.SEVERE, "&6" + file.getName() + " &ccouldn't loaded. Couldn't create file url");
        }

        return null;
    }
}
