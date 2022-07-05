package io.github.toberocat.core.utility.sql;

import org.jetbrains.annotations.NotNull;

/* Just an interface to make sure the data that's being saved has a real mysql implementation */
public interface MySqlData<T> {
    /**
     * Returns if it was a success. If not, saving to json
     * @param sql the database
     * @return If successfully saved
     */
    boolean save(@NotNull MySql sql);
    T read(@NotNull MySql sql);
}
