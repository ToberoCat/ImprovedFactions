package io.github.toberocat.core.factions.modules;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionUtility;
import io.github.toberocat.core.factions.permission.FactionPerm;
import io.github.toberocat.core.utility.async.AsyncTask;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class MessageModule extends FactionModule {
    public MessageModule(Faction faction) {
        super(faction);
    }

    public void broadcast(String message) {
        AsyncTask.run(() -> {
            String formatted = String.format("§7[§e%s§7] §r%s", faction.getDisplayName(), message.trim());
            faction.getFactionMemberManager().getOnlinePlayers().forEach(member -> {
                if (!faction.hasPermission(member, FactionPerm.MEMBER_RECEIVE_BROADCAST)) return;
                member.sendMessage(formatted);
            });
        });
    }

    public void broadcastAlly(String message) {
        AsyncTask.run(() -> {
            String formatted = String.format("§7[§e%s§7] §r%s", faction.getDisplayName(), message.trim());
            Stream.concat(faction.getRelationManager().getAllies().stream()
                                    .map(FactionUtility::getFactionByRegistry)
                                    .filter(Objects::nonNull)
                                    .flatMap(x -> x.getFactionMemberManager().getOnlinePlayers().stream()),
                            faction.getFactionMemberManager().getOnlinePlayers().stream())
                    .forEach(member -> {
                        if (!faction.hasPermission(member, FactionPerm.MEMBER_RECEIVE_BROADCAST)) return;
                        member.sendMessage(formatted);
                    });
        });
    }
}
