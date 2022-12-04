package io.github.toberocat.improvedFactions.core.player;

import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.faction.components.rank.Rank;
import io.github.toberocat.improvedFactions.core.persistent.component.PersistentWrapper;
import io.github.toberocat.improvedFactions.core.translator.Placeholder;
import io.github.toberocat.improvedFactions.core.translator.layout.Translatable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Function;

public interface OfflineFactionPlayer<P> {
    /* Faction */
    default @NotNull Rank getRank() throws FactionNotInStorage, PlayerHasNoFactionException {
        return getFaction().getPlayerRank(this);
    }

    @NotNull Faction<?> getFaction() throws PlayerHasNoFactionException, FactionNotInStorage;

    @Nullable String getFactionRegistry();


    boolean inFaction();

    /* Messages */

    /**
     * @deprecated Use {@link OfflineFactionPlayer#sendFancyMessage(Function, Placeholder...)} instead
     */
    @Deprecated
    void sendMessage(@NotNull String message);

    /**
     * @deprecated Use {@link OfflineFactionPlayer#sendFancyMessage(Function, Placeholder...)} instead
     */
    @Deprecated
    void sendTranslatable(@NotNull Function<Translatable, String> query,
                          Placeholder... placeholders);

    /**
     * @deprecated Use {@link OfflineFactionPlayer#sendFancyMessage(Function, Placeholder...)} instead
     */
    @Deprecated
    void sendClickableTranslatable(@NotNull Function<Translatable, String> query,@NotNull String command,
                                   Placeholder... placeholders);

    /**
     * Fancy message
     * An utility to make sending messages with
     * hovering and clickable text easier to use
     * and more available to end users
     * <p>
     * Example messages:
     * <ul>
     *     <li>
     *         {text:&6Sample colored text}
     *     </li>
     *     <li>
     *         {text:Hover over the message; hover:&cHello}
     *     </li>
     *     <li>
     *         {text:Click here to say hi in chat; command:Hi everyone!}
     *     </li>
     *     <li>
     *         {text:Click here to change your gamemode; command:/gamemode creative}
     *     </li>
     *     <li>
     *         {text:Click here to go to youtube; url:https://youtube.com/}
     *     </li>
     *     <li>
     *         {text:Multiple attributes; hover:Hovering; suggest_command:This is a command suggestion}
     *     </li>
     *     <li>
     *         {text:First hover; hover:First} {text:Second hover; hover:Second} {text:Broadcast; command:/broadcast Hello!}
     *     </li>
     * </ul>
     *
     * @author iDarkyy
     */
    void sendFancyMessage(@NotNull Function<Translatable, String> fancyMessage,
                          Placeholder... placeholders);

    /* Player */
    @Nullable FactionPlayer<?> getPlayer();

    @NotNull UUID getUniqueId();

    @NotNull String getName();

    long getLastPlayed();

    boolean isOnline();

    /* Persistent data */
    @NotNull PersistentWrapper getDataContainer();

    /* Raw instance */
    @NotNull P getRaw();
}
