package io.github.toberocat.core.utility.data;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.debug.Debugger;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.core.utility.config.ConfigManager;
import io.github.toberocat.core.utility.jackson.JsonUtility;
import io.github.toberocat.core.utility.sql.MySql;
import io.github.toberocat.core.utility.sql.MySqlData;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Level;

public class DataAccess {
    private static MySql sql;

    public static boolean init() {
        if (Boolean.TRUE.equals(ConfigManager.getValue("general.useSQL"))) {
            String host = ConfigManager.getValue("sql.host", "localhost");
            int port = ConfigManager.getValue("sql.port", 3306);
            String user = ConfigManager.getValue("sql.user", "root");
            String password = ConfigManager.getValue("sql.password", "1234");

            sql = new MySql(host, port, "improved_factions");

            try {
                sql.login(user, password);
            } catch (SQLException | ClassNotFoundException e) {
                MainIF.getIF().saveShutdown("&cDatabase not connected. " +
                        "Please check if your login informations, host and port are right");
                return false;
            }

            if (!sql.isConnected()) {
                MainIF.getIF().saveShutdown("&cCouldn't connect to database");
                return false;
            }

            MainIF.logMessage(Level.INFO, "&aConnection established to &6mysql");
        }

        create("Factions");
        create("History");
        create("History/Territory");
        create("Chunks");
        create("Players");
        create("Messages");

        return true;
    }

    public static AsyncTask<?> create(String folder) {
        if (sql != null) return AsyncTask.run(() -> {
        });
        return AsyncTask.run(() -> {
            String defPath = MainIF.getIF().getDataFolder().getPath() + "/";
            Utility.mkdir(defPath + "Data/" + folder);
        });
    }

    public static void reset() {
        if (sql == null) {
            DataAccess.clearFolder("Factions");
            DataAccess.clearFolder("Chunks");
            DataAccess.clearFolder("History");
            DataAccess.clearFolder("Players");
        } else {
            clearTable("faction_bans");
            clearTable("faction_descriptions");
            clearTable("faction_relations");
            clearTable("faction_settings");
            clearTable("factions");
            clearTable("players");
        }
    }

    public static void clearFolder(String folder) {
        if (sql != null) return;
        File dat = new File(
                MainIF.getIF().getDataFolder().getPath() + "/Data/" + folder);

        File[] listed = dat.listFiles();
        if (listed == null) return;

        for (File file : listed) {
            file.delete();
        }
    }

    public static void clearTable(@NotNull String table) {
        if (sql == null || !sql.isConnected()) return;

        try {
            sql.eval("TRUNCATE %s", table);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static <T extends MySqlData<T>> T get(@NotNull String folder,
                                                 @NotNull String filename,
                                                 @NotNull Class<T> clazz) {
        if (sql != null) {
            try {
                T t = clazz.getConstructor().newInstance();
                return t.read(sql);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        } else {
            String filePath = MainIF.getIF().getDataFolder().getPath() + "/Data/" + folder + "/" + filename + ".json";
            File file = new File(filePath);
            if (!file.exists()) return null;

            try {
                return JsonUtility.readObject(file, clazz);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static <T extends MySqlData<T>> T getWithExceptions(@NotNull String folder,
                                                               @NotNull String filename,
                                                               @NotNull Class<T> clazz) throws IOException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (sql != null) {
            T t = clazz.getConstructor().newInstance();
            return t.read(sql);
        } else {
            String filePath = MainIF.getIF().getDataFolder().getPath() + "/Data/" + folder + "/" + filename + ".json";
            File file = new File(filePath);
            if (!file.exists()) return null;

            return JsonUtility.readObject(file, clazz);
        }
    }

    public static void disable() {
        if (sql == null) return;

        try {
            sql.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static <T extends MySqlData<T>> boolean write(String folder, String filename, T object) {
        if (sql != null) {
            return object.save(sql);
        } else {
            String filePath = MainIF.getIF().getDataFolder().getPath() + "/Data/" + folder + "/" + filename + ".json";
            File file = new File(filePath);

            return JsonUtility.saveObject(file, object);
        }
    }

    public static <T extends MySqlData<T>> boolean delete(@NotNull String folder,
                                                          @NotNull String filename,
                                                          T object) {
        if (sql != null) {
            return object.delete(sql);
        } else {
            String filePath = MainIF.getIF().getDataFolder().getPath() + "/Data/" + folder + "/" + filename + ".json";
            File file = new File(filePath);
            return file.delete();
        }
    }

    /**
     * Raw file names. No .json removing
     */
    public static String[] listRawFolder(String folder) {
        if (sql != null) {
            // ToDO: Add method for sql table listing
            return new String[]{""};
        } else {
            String filePath = MainIF.getIF().getDataFolder().getPath() + "/Data/" + folder;
            File file = new File(filePath);

            File[] listed = file.listFiles();
            if (listed == null) return new String[0];

            return Arrays.stream(listed).map(File::getName).toArray(String[]::new);
        }
    }

    /**
     * Raw file names. .json got removed
     */
    public static String[] listFilesFolder(String folder) {
        if (sql != null) {
            // ToDO: Add method for sql table listing
            return new String[0];
        } else {
            String filePath = MainIF.getIF().getDataFolder().getPath() + "/Data/" + folder;
            File file = new File(filePath);

            File[] listed = file.listFiles();
            if (listed == null) return new String[0];

            return Arrays.stream(listed).map(x -> x.getName().split("\\.")[0]).toArray(String[]::new);
        }
    }

    public static boolean existsFolder(@NotNull String folder, @NotNull String filename) {
        if (sql != null) {
            return false;
        } else {
            String filePath = MainIF.getIF().getDataFolder().getPath() + "/Data/"
                    + folder + "/" + filename + ".json";
            File file = new File(filePath);

            Debugger.log(file.exists() + "");

            return file.exists();
        }
    }

    public static boolean isSql() {
        return sql != null && sql.isConnected();
    }

    public static MySql getSql() {
        return sql;
    }
}
