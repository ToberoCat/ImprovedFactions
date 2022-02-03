package io.github.toberocat.core.extensions;

import io.github.toberocat.MainIF;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class ExtensionDownloader {
    private static boolean isRedirected( Map<String, List<String>> header ) {
        for( String hv : header.get( null )) {
            if(   hv.contains( " 301 " )
                    || hv.contains( " 302 " )) return true;
        }
        return false;
    }

    public static void DownloadExtension(ExtensionObject extension, ExtensionDownloadCallback callback) throws IOException {
        callback.startDownload(extension);

        String link = extension.getDownloadLinks().get(MainIF.getVersion()).toString();
        String fileName = extension.getFileName() + ".jar";

        URL url = new URL(link);
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        Map<String, List<String>> header = http.getHeaderFields();

        while(isRedirected(header)) {
            link = header.get("Location").get( 0 );
            url = new URL( link );
            http = (HttpURLConnection)url.openConnection();
            header = http.getHeaderFields();
        }

        InputStream input  = http.getInputStream();
        byte[] buffer = new byte[4096];
        int n = -1;
        OutputStream output = new FileOutputStream(MainIF.getIF().getDataFolder() + "/" + fileName);
        while ((n = input.read(buffer)) != -1) {
            output.write( buffer, 0, n );
        }
        output.close();
        callback.finishedDownload(extension);
    }

}
