package io.github.toberocat.core.utility.data;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.datatype.FactionBans;
import io.github.toberocat.core.factions.datatype.FactionDescriptions;
import io.github.toberocat.core.factions.datatype.FactionRelations;
import io.github.toberocat.core.player.PlayerDataType;
import io.github.toberocat.core.factions.datatype.FactionSetting;
import io.github.toberocat.core.player.PlayerSettingDataType;
import org.jetbrains.annotations.NotNull;

public enum Table {
    FACTIONS("factions", Faction.class),
    FACTION_BANS("faction_bans", FactionBans.class),
    FACTION_DESCRIPTIONS("faction_descriptions", FactionDescriptions.class),
    FACTION_RELATIONS("faction_relations", FactionRelations.class),
    FACTION_SETTINGS("faction_settings", FactionSetting.class),

    PLAYERS("players", PlayerDataType.class),
    PLAYER_SETTINGS("player_settings", PlayerSettingDataType.class);

    String table;
    Class<?> clazz;
    Table(@NotNull String table, @NotNull Class<?> clazz) {
        this.table = table;
        this.clazz = clazz;
    }

    public String getTable() {
        return table;
    }

    public Class<?> getDefaultClass() {
        return clazz;
    }

    @Override
    public String toString() {
        return getTable();
    }
}
