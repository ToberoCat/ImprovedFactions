package io.github.toberocat.core.utility.language;

import io.github.toberocat.core.utility.Utility;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LangContainer {
    private Map<String, ArrayList<LangWeb>> languages;

    public LangContainer() {
        this.languages = new HashMap<>();
        languages.put("en_us", new ArrayList<>());
        Utility.run(() -> languages.get("en_us").add(new LangWeb("Tobero", "credits", "1.0",
                new URL("https://www.youtube.com/watch?v=Kgjkth6BRRY&list=RDMM&index=2"))));
    }

    public Map<String, ArrayList<LangWeb>> getLanguages() {
        return languages;
    }

    public void setLanguages(Map<String, ArrayList<LangWeb>> languages) {
        this.languages = languages;
    }

    public record LangWeb(String author, String credits, String version,
                          URL downloadUrl) {

        public String getAuthor() {
            return author;
        }

        public String getCredits() {
            return credits;
        }

        public String getVersion() {
            return version;
        }

        public URL getDownloadUrl() {
            return downloadUrl;
        }
    }
}
