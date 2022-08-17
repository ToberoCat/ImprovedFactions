package io.github.toberocat.improvedFactions.persistent;

import io.github.toberocat.improvedFactions.handler.ConfigHandler;
import io.github.toberocat.improvedFactions.persistent.database.mysql.MySqlData;
import io.github.toberocat.improvedFactions.persistent.local.LocalData;

public final class PersistentHandler {

    private static final PersistentData implementation = createImplementation();

    private static PersistentData createImplementation() {
        if (ConfigHandler.api().getBool("storage.use-mysql", false))
            return new MySqlData();
        else return new LocalData();
    }

    public static PersistentData api() {
        return implementation;
    }
}
