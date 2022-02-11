package io.github.toberocat.core.utility.data;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.async.AsyncCore;
import io.github.toberocat.core.utility.callbacks.ResultCallback;
import io.github.toberocat.core.utility.json.JsonUtility;
import io.github.toberocat.core.debug.Debugger;
import io.github.toberocat.core.utility.sql.MySQL;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Level;

public class DataAccess {
    private static MySQL sql;

    public static boolean init() {
        if (MainIF.getConfigManager().getValue("general.useSQL")) {
            sql = new MySQL();

            try {
                sql.connect();
            } catch (SQLException | ClassNotFoundException e) {
                MainIF.getIF().SaveShutdown("&cDatabase not connected. Please check if your login information are right");
                return false;
            }

            if (!sql.isConnected()) return false;

            MainIF.LogMessage(Level.INFO, "&aSuccessfully &fconnected to database");
        }

        makeFolder("Factions");
        makeFolder("History");
        makeFolder("History/Territory");
        makeFolder("Chunks");
        makeFolder("Players");
        makeFolder("Messages");

        return true;
    }

    public static AsyncCore makeFolder(String folder) {
        if (sql != null) {
            //ToDo: Add the method for creating tables in mySQL
            return null;
        } else {
            return AsyncCore.Run(() -> {
                String defPath = MainIF.getIF().getDataFolder().getPath() + "/";
                Utility.mkdir(defPath + "Data/" + folder);
            });
        }
    }

    public static String getRawFile(String folder, String filename) {
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

    public static void clearFolder(String folder) {
        if (sql != null) {
            //ToDo: Add the method for clearing the file in mySQL
        } else {
            for (File file : new File(
                    MainIF.getIF().getDataFolder().getPath() + "/Data/" + folder).listFiles()) {
                file.delete();
            }
        }
    }

    public static <T> T getFile(String folder, String filename, Class clazz) {
        if (sql != null) {
            //ToDo: Add the method for storing the file in mySQL
            return null;
        } else {
            String filePath = MainIF.getIF().getDataFolder().getPath() + "/Data/" + folder + "/" + filename + ".json";
            File file = new File(filePath);

            try {
                return (T) JsonUtility.ReadObject(file, clazz);
            } catch (IOException e) {
                return null;
            }
        }
    }

    public static void disable() {
        if (sql != null) sql.disconnect();
    }

    public static <T> boolean addFile(String folder, String filename, T object) {
        if (sql != null) {
            //ToDo: Add the method for storing the file in mySQL
            return true;
        } else {
            String filePath = MainIF.getIF().getDataFolder().getPath() + "/Data/" + folder + "/" + filename + ".json";
            File file = new File(filePath);

            return JsonUtility.SaveObject(file, object);
        }
    }

    public static boolean removeFile(String folder, String filename) {
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
    public static String[] listRawFiles(String folder) {
        if (sql != null) {
            // ToDO: Add method for sql table listing
            return new String[] {""};
        } else {
            String filePath = MainIF.getIF().getDataFolder().getPath() + "/Data/" + folder;
            File file = new File(filePath);

            File[] listed = file.listFiles();
            return Arrays.stream(listed).map(File::getName).toArray(String[]::new);
        }
    }

    /**
     * Raw file names. .json got removed
     */
    public static String[] listFiles(String folder) {
        if (sql != null) {
            // ToDO: Add method for sql table listing
            return new String[] {""};
        } else {
            String filePath = MainIF.getIF().getDataFolder().getPath() + "/Data/" + folder;
            File file = new File(filePath);

            File[] listed = file.listFiles();
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

            Debugger.log(file.exists()+"");

            return file.exists();
        }
    }
}
