package io.github.toberocat.core.utility.version;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.toberocat.core.utility.Utility;

import java.io.IOException;
import java.net.URL;

public class UpdateChecker {
    private final Version version;
    private Version newestVersion;

    public UpdateChecker(Version version, URL versionJson) {
        this.version = version;

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode _version = null;
        try {
            _version = mapper.readValue(versionJson, ObjectNode.class);
        } catch (IOException e) {
            Utility.except(e);
        }
        if (_version.has("version")) {
            newestVersion = Version.from(_version.get("version").toString().replace("\"", ""));
        }
    }

    public UpdateChecker(Version version, Version newestVersion) {
        this.version = version;
        this.newestVersion = newestVersion;
    }

    public Version getVersion() {
        return version;
    }

    public Version getNewestVersion() {
        return newestVersion;
    }

    public boolean isNewestVersion() {
        return version.versionToInteger() >= newestVersion.versionToInteger();
    }
}
