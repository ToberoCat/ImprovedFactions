package io.github.toberocat.core.extensions.list;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.toberocat.core.extensions.ExtensionObject;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.core.utility.async.Promise;
import io.github.toberocat.core.utility.version.Version;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Optional;

public class ExtensionListLoader {

    public static ExtensionObject[] readListSync() {
        return loadList().await();
    }

    public static AsyncTask<ExtensionObject[]> readList() {
        return loadList();
    }

    private static AsyncTask<ExtensionObject[]> loadList() {
        return AsyncTask.run(() -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(new URL("https://raw.githubusercontent.com/ToberoCat/ImprovedFaction/master/v1_extensions.json"),
                        ExtensionObject[].class);
            } catch (IOException e) {
                Utility.except(e);
                return new ExtensionObject[0];
            }
        });
    }
}
