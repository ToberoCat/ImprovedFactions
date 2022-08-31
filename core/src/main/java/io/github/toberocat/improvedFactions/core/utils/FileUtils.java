package io.github.toberocat.improvedFactions.core.utils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public final class FileUtils {

    /**
     * Given a file and a path, return a new file that is the result of joining the path to the file.
     *
     * @param file The file to join with.
     * @param path The path to the file or directory.
     * @return A new File object with the path of the first file and the path of the second file.
     */
    public static File join(File file, String path) {
        return new File(file.getPath(), path);
    }

    /**
     * Moves a file or directory to a directory.
     *
     * @param from the file to move
     * @param to   where the file should be moved to
     * @return true if the file was moved successfully
     */
    public static boolean move(@NotNull File from, @NotNull File to) throws IOException {
        boolean success = true;
        File toParent = to.getParentFile();

        if (toParent != null) {
            success = toParent.mkdirs();
        }

        Path f = from.toPath();
        Path t = to.toPath();

        Files.move(f, t);
        return success;
    }
}
