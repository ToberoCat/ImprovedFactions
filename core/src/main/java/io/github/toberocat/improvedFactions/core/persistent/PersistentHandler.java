package io.github.toberocat.improvedFactions.core.persistent;

import io.github.toberocat.improvedFactions.core.persistent.local.LocalData;
import io.github.toberocat.improvedFactions.core.handler.ConfigFile;
import io.github.toberocat.improvedFactions.core.persistent.database.mysql.MySqlData;

public final class PersistentHandler {

    public static final String FACTION_KEY = "faction_registry";
    public static final String CLAIM_KEY = "claim_registry";
    public static final String RECEIVED_INVITE_KEY = "received_invite";
    public static final String SENT_INVITE_KEY = "received_invite";

    private static final PersistentData implementation = createImplementation();

    private static PersistentData createImplementation() {
        if (ConfigFile.api().getBool("storage.use-mysql", false))
            return new MySqlData();
        else return new LocalData();
    }

    public static PersistentData api() {
        return implementation;
    }
}
