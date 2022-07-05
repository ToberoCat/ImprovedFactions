package io.github.toberocat.core.utility.data;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import io.github.toberocat.MainIF;
import io.github.toberocat.core.debug.Debugger;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.core.utility.config.ConfigManager;
import io.github.toberocat.core.utility.jackson.JsonUtility;
import io.github.toberocat.core.utility.sql.MySql;
import io.github.toberocat.core.utility.sql.MySqlData;

import java.io.File;
import java.io.IOException;
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

    public static AsyncTask<?> create(String table) {
        if (sql != null) {
            //ToDo: Add the method for creating tables in mySQL
            return null;
        } else {
            return AsyncTask.run(() -> {
                String defPath = MainIF.getIF().getDataFolder().getPath() + "/";
                Utility.mkdir(defPath + "Data/" + table);
            });
        }
    }

    public static String getRaw(String folder, String filename) {
        if (sql != null) {
            //ToDo: Add the method for storing the file in mySQL
            return "";
        } else {
            String filePath = MainIF.getIF().getDataFolder().getPath() + "/Data/" + folder + "/" + filename;
            File file = new File(filePath);

            try {
                return Files.asCharSource(file, Charsets.UTF_8).read();
            } catch (IOException e) {
                Utility.except(e);
                return null;
            }
        }
    }

    public static void clear(String folder) {
        if (sql != null) {
            //ToDo: Add the method for clearing the file in mySQL
        } else {
            File dat = new File(
                    MainIF.getIF().getDataFolder().getPath() + "/Data/" + folder);

            File[] listed = dat.listFiles();
            if (listed == null) return;

            for (File file : listed) {
                file.delete();
            }
        }
    }

    public static <T extends MySqlData<T>> T get(String folder, String filename, Class<T> clazz) {
        if (sql != null) {
            //ToDo: Add the method for storing the file in mySQL
            return null;
        } else {
            String filePath = MainIF.getIF().getDataFolder().getPath() + "/Data/" + folder + "/" + filename + ".json";
            File file = new File(filePath);
            if (!file.exists()) return null;

            try {
                return JsonUtility.readObject(file, clazz);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static <T extends MySqlData<T>> T getWithExceptions(String folder, String filename, Class<T> clazz) throws IOException {
        if (sql != null) {
            //ToDo: Add the method for storing the file in mySQL
            return null;
        } else {
            String filePath = MainIF.getIF().getDataFolder().getPath() + "/Data/" + folder + "/" + filename + ".json";
            File file = new File(filePath);
            if (!file.exists()) return null;

            return JsonUtility.readObject(file, clazz);
        }
    }

    public static void disable() {
        if (sql != null) {
            try {
                sql.disconnect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static <T extends MySqlData<T>> boolean write(String folder, String filename, T object) {
        if (sql != null) {
            //ToDo: Add the method for storing the file in mySQL
            return true;
        } else {
            String filePath = MainIF.getIF().getDataFolder().getPath() + "/Data/" + folder + "/" + filename + ".json";
            File file = new File(filePath);

            return JsonUtility.saveObject(file, object);
        }
    }

    public static boolean delete(String folder, String filename) {
        if (sql != null) {
            //ToDo: Remove file in table
            return true;
        } else {
            String filePath = MainIF.getIF().getDataFolder().getPath() + "/Data/" + folder + "/" + filename + ".json";
            File file = new File(filePath);
            return file.delete();
        }
    }

    /**
     * Raw file names. No .json removing
     */
    public static String[] listRaw(String folder) {
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
    public static String[] listFiles(String folder) {
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

    public static boolean exists(String folder, String filename) {
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
}
