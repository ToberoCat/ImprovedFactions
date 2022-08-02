package io.github.toberocat.core.utility.data.access;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.data.Table;
import io.github.toberocat.core.utility.jackson.JsonUtility;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class FileAccess extends AbstractAccess<FileAccess> {
    @Override
    public boolean register() {
        create("Factions");
        create("History");
        create("History/Territory");
        create("Chunks");
        create("Players");
        create("Messages");

        return true;
    }

    @Override
    public @NotNull <R> AccessPipeline<FileAccess> write(@NotNull Table table, @NotNull String byKey, @NotNull R instance) {
        String filePath = MainIF.getIF().getDataFolder().getPath() + "/Data/" + table.getFile() + "/" + byKey + ".json";
        File file = new File(filePath);

        JsonUtility.saveObject(file, instance);
        return this;
    }

    @Override
    public @NotNull AccessPipeline<FileAccess> delete(@NotNull Table table, @NotNull String byKey) {
        String filePath = MainIF.getIF().getDataFolder().getPath() + "/Data/" + table.getFile() + "/" + byKey + ".json";
        new File(filePath).delete();
        return this;
    }

    @Override
    public @NotNull AccessPipeline<FileAccess> restoreDefault() {
        clear("Factions");
        clear("Chunks");
        clear("History");
        clear("Players");

        return this;
    }

    @Override
    public @NotNull Stream<String> listInTableStream(@NotNull Table table) {
        String filePath = MainIF.getIF().getDataFolder().getPath() + "/Data/" + table.getFile();
        File file = new File(filePath);

        File[] listed = file.listFiles();
        if (listed == null) return Stream.empty();

        return Arrays.stream(listed)
                .map(File::getName);
    }

    @Override
    public @NotNull List<String> listInTable(@NotNull Table table) {
        return listInTableStream(table).toList();
    }

    @Override
    public boolean has(@NotNull Table table, @NotNull String byKey) {
        String filePath = MainIF.getIF().getDataFolder().getPath() + "/Data/"
                + table.getTable() + "/" + byKey + ".json";
        return new File(filePath).exists();
    }

    @Override
    public <R> R read(@NotNull Table table, @NotNull String byKey) {
        if (table.getFileClass() == null || table.getFile() == null) return null;

        String filePath = MainIF.getIF().getDataFolder().getPath() + "/Data/" + table.getFile() + "/" + byKey + ".json";
        File file = new File(filePath);
        if (!file.exists()) return null;

        try {
            return (R) JsonUtility.readObject(file, table.getFileClass());
        } catch (IOException e) {
            Utility.except(e);
            return null;
        }
    }

    public @NotNull AccessPipeline<FileAccess> create(@NotNull String folder) {
        String defPath = MainIF.getIF().getDataFolder().getPath() + "/";
        Utility.mkdir(defPath + "Data/" + folder);
        return this;
    }

    public @NotNull AccessPipeline<FileAccess> clear(String folder) {
        File dat = new File(MainIF.getIF().getDataFolder().getPath() + "/Data/" + folder);
        File[] listed = dat.listFiles();
        if (listed == null) return this;

        for (File file : listed) file.delete();
        return this;
    }
}
