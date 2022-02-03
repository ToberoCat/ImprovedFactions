package io.github.toberocat.core.utility.factions.rank;

import io.github.toberocat.core.utility.language.LangMessage;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.entity.Player;

public class NewMemberRank extends Rank {
    public static final String registry = "NewMember";
    public NewMemberRank() {
        super("New member", registry, false);
    }

    @Override
    public String description(Player player) {
        return Language.getMessage(LangMessage.RANK_NEWMEMBER_DESCRIPTION, player);
    }
}
