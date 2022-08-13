package io.github.toberocat.improvedFactions.faction.database.mysql.module;

import io.github.toberocat.core.factions.components.FactionModule;
import io.github.toberocat.core.factions.database.DatabaseFaction;
import io.github.toberocat.core.utility.data.database.sql.MySqlDatabase;
import org.jetbrains.annotations.NotNull;

public abstract class MySqlModule extends FactionModule<DatabaseFaction> {

    public MySqlModule(DatabaseFaction faction) {
        super(faction);
    }

    public abstract void loadAdditionalFromDb(@NotNull MySqlDatabase database);
    public abstract void writeAdditionalToDb(@NotNull MySqlDatabase database);

    /**
     * This is getting called when the module is getting removed, due to extension removal
     * @param database The database where the cleanup should happen
     */
    public abstract void cleanUp(@NotNull MySqlDatabase database);
}
