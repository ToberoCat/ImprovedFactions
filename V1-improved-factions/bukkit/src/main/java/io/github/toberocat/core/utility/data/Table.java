package io.github.toberocat.core.utility.data;

import io.github.toberocat.core.factions.local.datatype.FactionBans;
import io.github.toberocat.core.factions.local.datatype.FactionDescriptions;
import io.github.toberocat.core.factions.local.datatype.FactionRelations;
import io.github.toberocat.core.factions.local.datatype.FactionSetting;
import io.github.toberocat.core.player.PlayerDataType;
import io.github.toberocat.core.player.PlayerSettingDataType;
import org.jetbrains.annotations.NotNull;

public enum Table {
    FACTIONS("factions", Database.class),
    FACTION_BANS("faction_bans", FactionBans.class),
    FACTION_DESCRIPTIONS("faction_descriptions", FactionDescriptions.class),
    FACTION_RELATIONS("faction_relations", FactionRelations.class),
    FACTION_SETTINGS("faction_settings", FactionSetting.class),

    PLAYERS("players", PlayerDataType.class),
    PLAYER_SETTINGS("player_settings", PlayerSettingDataType.class);

    String table;
    Class<?> dataBaseClazz;

    Table(@NotNull String table, Class<?> dataBaseClazz) {
        this.table = table;
        this.dataBaseClazz = dataBaseClazz;
    }

    public String getTable() {
        return table;
    }

    public Class<?> getDatabaseClass() {
        return dataBaseClazz;
    }

    @Override
    public String toString() {
        return getTable();
    }
}
