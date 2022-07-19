package io.github.toberocat.core.utility.data;

import io.github.toberocat.core.factions.local.LocalFaction;
import io.github.toberocat.core.factions.local.datatype.FactionBans;
import io.github.toberocat.core.factions.local.datatype.FactionDescriptions;
import io.github.toberocat.core.factions.local.datatype.FactionRelations;
import io.github.toberocat.core.factions.local.datatype.FactionSetting;
import io.github.toberocat.core.player.PlayerSettings;
import io.github.toberocat.core.utility.claim.Claim;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum Table {
    FACTIONS("factions", "Factions", LocalFaction.class),
    FACTION_BANS("faction_bans", null, null),
    FACTION_DESCRIPTIONS("faction_descriptions", null, null),
    FACTION_RELATIONS("faction_relations", null, null),
    FACTION_SETTINGS("faction_settings", null, null),

    PLAYERS("players", "Players", PlayerSettings.class),
    PLAYER_SETTINGS("player_settings", null, null),

    MESSAGES("messages", "Messages", null),
    CHUNKS("chunks", "Chunks", Claim.class);

    String table;
    String file;
    Class<?> fileClass;

    Table(@NotNull String table, @Nullable String file, @Nullable Class<?> fileClass) {
        this.table = table;
        this.file = file;
        this.fileClass = fileClass;
    }

    public String getTable() {
        return table;
    }

    public @Nullable String getFile() {
        return file;
    }

    public @Nullable Class<?> getFileClass() {
        return fileClass;
    }

    @Override
    public String toString() {
        return getTable();
    }
}
