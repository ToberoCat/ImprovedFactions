package io.github.toberocat.improvedfactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.net.URL;

public class UpdateChecker {
    private String version;
    private String newestVersion;

    public UpdateChecker(String version, URL versionJson) {
        this.version = version;

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode _version = null;
        try {
            _version = mapper.readValue(versionJson, ObjectNode.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (_version.has("version")) {
            newestVersion = _version.get("version").toString().replace("\"", "");
        }
    }

    public UpdateChecker(String version, String newestVersion) {
        this.version = version;
        this.newestVersion = newestVersion;
    }

    public String getVersion() {
        return version;
    }

    public String getNewestVersion() {
        return newestVersion;
    }

    public boolean isNewestVersion() {
        int currentV = versionStringToInt(version);
        int newestV = versionStringToInt(version);

        if (currentV < newestV) {
            return false;
        } else {
            return true;
        }
    }

    public static int versionStringToInt(String version) {
        if (version == null || version.length() == 0) {
            return 0;
        }
        return Integer.parseInt(version.replaceAll("[^0-9.]", "").replaceAll("\\.", ""));
    }
}
