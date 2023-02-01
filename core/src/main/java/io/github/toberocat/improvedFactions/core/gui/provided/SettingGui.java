package io.github.toberocat.improvedFactions.core.gui.provided;

import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.improvedFactions.core.faction.components.rank.members.FactionRank;
import io.github.toberocat.improvedFactions.core.gui.content.ItemState;
import io.github.toberocat.improvedFactions.core.gui.manager.AbstractGuiProvider;
import io.github.toberocat.improvedFactions.core.item.ItemStack;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;

public class SettingGui extends AbstractGuiProvider {

    public static final String GUI_ID = "manage-faction";
    public static final int FACTION_ICON_FLAG = 0;

    public SettingGui() {
        super(GUI_ID, new GuiSettings()
                .addFlag("Faction Icon", FACTION_ICON_FLAG)
                .addState(Arrays.stream(FactionRank.FACTION_RANKS)
                        .map(x -> x.toLowerCase() + "Rank")
                        .toArray(String[]::new)));
    }


    @Override
    public @NotNull ItemState getState(@NotNull FactionPlayer<?> viewer,
                                       @NotNull Map<String, ItemState> states) {
        try {
            String id = viewer.getRank().getRegistry().toLowerCase() + "Rank";
            return states.get(states.containsKey(id) ? id : "defaultState");
        } catch (FactionNotInStorage | PlayerHasNoFactionException e) {
            return states.get("defaultState");
        }
    }
}
