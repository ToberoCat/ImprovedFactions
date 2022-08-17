package io.github.toberocat.improvedFactions.core.faction.database.mysql.module;

import io.github.toberocat.improvedFactions.core.database.mysql.MySqlDatabase;
import io.github.toberocat.improvedFactions.core.faction.components.FactionModule;
import io.github.toberocat.improvedFactions.core.faction.database.mysql.MySqlFaction;
import org.jetbrains.annotations.NotNull;

public abstract class MySqlModule extends FactionModule<MySqlFaction> {

    public MySqlModule(MySqlFaction faction) {
        super(faction);
    }

    public abstract void loadAdditionalFromDb(@NotNull MySqlDatabase database);

    public abstract void writeAdditionalToDb(@NotNull MySqlDatabase database);

    /**
     * This is getting called when the module is getting removed, due to extension removal
     *
     * @param database The database where the cleanup should happen
     */
    public abstract void cleanUp(@NotNull MySqlDatabase database);
}
