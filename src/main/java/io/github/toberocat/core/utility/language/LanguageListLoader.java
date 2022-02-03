package io.github.toberocat.core.utility.language;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.toberocat.core.utility.Utility;

import java.io.IOException;
import java.net.URL;

public class LanguageListLoader {

    public static String[] getLanguageList() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            LangContainer container = mapper.readValue(new URL("https://raw.githubusercontent.com/ToberoCat/ImprovedFaction/master/languages.json"),
                    LangContainer.class);
            return container.getLanguages().keySet().toArray(new String[0]);
        } catch (IOException e){
            Utility.except(e);
            return new String[] {""};
        }
    }

    public static void download(String langCode, String language) {

    }
}
