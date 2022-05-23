package io.github.toberocat.core.extensions;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.core.utility.callbacks.ExceptionCallback;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

public class ExtensionDownloader {
    private static boolean isRedirected(Map<String, List<String>> header) {
        for (String hv : header.get(null)) {
            if (hv.contains(" 301 ")
                    || hv.contains(" 302 ")) return true;
        }
        return false;
    }

    public static String getSha256FromFile(File file) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        FileInputStream fis = new FileInputStream(file.getPath());
        byte[] dataBytes = new byte[1024];

        int nread = 0;
        while ((nread = fis.read(dataBytes)) != -1) {
            md.update(dataBytes, 0, nread);
        }

        byte[] mdbytes = md.digest();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mdbytes.length; i++) {
            sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    public static void downloadExtension(ExtensionObject extension, ExtensionDownloadCallback callback) {
        callback.startDownload(extension);

        AsyncTask.run((ExceptionCallback) () -> {
            for (int i = 0; i < 10; i++) {
                URL url = extension.getDownloadLinks().get(MainIF.getVersion().getVersion());
                if (url == null)
                    url = extension.getDownloadLinks().get(extension.getMinVersion());

                if (url == null) return;

                String fileName = "Extensions/" + extension.getFileName() + ".jar";

                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                Map<String, List<String>> header = http.getHeaderFields();

                String link = "";
                while (isRedirected(header)) {
                    link = header.get("Location").get(0);
                    url = new URL(link);
                    http = (HttpURLConnection) url.openConnection();
                    header = http.getHeaderFields();
                }

                InputStream input = http.getInputStream();
                byte[] buffer = new byte[4096];
                int n = -1;
                OutputStream output =
                        new FileOutputStream(MainIF.getIF().getDataFolder() + "/" + fileName);
                while ((n = input.read(buffer)) != -1) {
                    output.write(buffer, 0, n);
                }
                output.close();
                if (getSha256FromFile(new File(MainIF.getIF().getDataFolder() + "/" + fileName))
                        .equals(extension.getSha256())) {
                    callback.finishedDownload(extension);
                    return;
                }
            }
            callback.cancelDownload(extension);
        });
    }
}
