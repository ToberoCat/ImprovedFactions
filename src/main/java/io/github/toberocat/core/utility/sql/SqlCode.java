package io.github.toberocat.core.utility.sql;

import io.github.toberocat.core.utility.callbacks.TryRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;

public class SqlCode {

    public static final String CREATE_FACTION_TABLE = readFileSql("sql/faction/create_faction.sql");
    public static final String CREATE_PLAYER_TABLE = readFileSql("sql/create_players.sql");

    public static TryRunnable<PreparedStatement> execute(@NotNull final MySql sql, @NotNull String code, final Object... placeholders) {
        // Replace all placeholders
        if (placeholders != null) {
            StringBuilder builder = new StringBuilder(code);

            int index = 0;
            int next = -1;
            while ((next = builder.indexOf("?", next + 1)) != -1) {
                if (index >= placeholders.length)
                    throw new IllegalArgumentException("More placeholders required than provided. " +
                            "Required at least: " + index + "; Provided: " + placeholders.length);

                builder.replace(next, next + 1, placeholders[index].toString());
                index++;
            }
            code = builder.toString();
        }

        return sql.evalTry(code);
    }
    
    public static String readFileSql(@NotNull String resPath) {
        InputStream is = SqlCode.class.getClassLoader().getResourceAsStream(resPath);
        if (is == null)
            throw new IllegalArgumentException("Couldn't resolve the local file path to the sql file");

            StringBuilder textBuilder = new StringBuilder();

        try (Reader reader = new BufferedReader(new InputStreamReader(is,
                Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return textBuilder.toString();
    }
}
