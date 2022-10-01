package io.github.toberocat.improvedFactions.core.faction.local;

import io.github.toberocat.improvedFactions.core.faction.OpenType;
import io.github.toberocat.improvedFactions.core.faction.components.report.Report;
import io.github.toberocat.improvedFactions.core.faction.local.module.LocalFactionModule;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record LocalFactionDataType(@NotNull String registry,
                                   @NotNull String display,
                                   @NotNull String motd,
                                   @NotNull String tag,
                                   @NotNull String iconBase64,
                                   @NotNull OpenType openType,
                                   boolean frozen,
                                   boolean permanent,
                                   @NotNull String createdAt,
                                   @NotNull UUID owner,
                                   @NotNull List<String> description,
                                   @NotNull List<UUID> banned,
                                   @NotNull List<UUID> members,
                                   @NotNull List<String> sentInvites,
                                   @NotNull List<String> receivedInvites,
                                   @NotNull List<String> allies,
                                   @NotNull List<String> enemies,
                                   @NotNull List<Report> reports,
                                   @NotNull Map<UUID, String> ranks,
                                   @NotNull Map<String, LocalFactionModule> modules,
                                   @NotNull Map<String, String[]> permissions,
                                   @NotNull Map<String, String> settingValues) {
}
