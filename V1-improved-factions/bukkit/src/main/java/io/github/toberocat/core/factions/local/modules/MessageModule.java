package io.github.toberocat.core.factions.local.modules;

import io.github.toberocat.core.factions.local.LocalFaction;
import io.github.toberocat.core.factions.local.FactionUtility;
import io.github.toberocat.core.factions.local.permission.FactionPerm;
import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.language.Parseable;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.stream.Stream;

public class MessageModule extends FactionModule {
    public MessageModule(LocalFaction faction) {
        super(faction);
    }

    public void broadcast(@NotNull String message) {
        AsyncTask.run(() -> {
            String formatted = String.format("§7[§e%s§7] §r%s", faction.getDisplayName(), message.trim());
            faction.getFactionMemberManager().getOnlinePlayers().forEach(member -> {
                if (!faction.hasPermission(member, FactionPerm.MEMBER_RECEIVE_BROADCAST)) return;
                member.sendMessage(formatted);
            });
        });
    }

    public void broadcastTranslatable(@NotNull String translatable) {
        Parseable factionParser = new Parseable("{faction}", faction.getDisplayName());
        AsyncTask.run(() -> {
            faction.getFactionMemberManager().getOnlinePlayers().forEach(member -> {
                if (!faction.hasPermission(member, FactionPerm.MEMBER_RECEIVE_BROADCAST)) return;
                String message = Language.getMessage("faction.broadcast.prefix", member,
                        factionParser,
                        new Parseable("{message}", Language.getMessage(translatable, member)));
                member.sendMessage(message);
            });
        });
    }

    public void broadcastAlly(@NotNull String message) {
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
