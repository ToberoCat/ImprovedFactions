package io.github.toberocat.core.commands.admin;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.local.FactionUtility;
import io.github.toberocat.core.utility.claim.ClaimManager;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.data.access.FileAccess;
import io.github.toberocat.core.utility.data.PersistentDataUtility;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class AdminRegenerateSubCommand extends SubCommand {
    public AdminRegenerateSubCommand() {
        super("regenerate", "admin.regenerate", "command.admin.regenerate.description", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setUseWhenFrozen(true);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        Language.sendRawMessage("Started regeneration of the data. This will repair corrupted registries for the worlds", player);

        Language.sendRawMessage("Loading all factions", player);
        for (String registry : FileAccess.listFilesFolder("Factions")) {
            FactionUtility.getFactionByRegistry(registry);
        }

        Language.sendRawMessage("Generation for all players", player);
        for (Player onP : Bukkit.getOnlinePlayers()) {
            String currentRegistry = PersistentDataUtility.read(PersistentDataUtility.PLAYER_FACTION_REGISTRY, PersistentDataType.STRING,
                    onP.getPersistentDataContainer());
            if (currentRegistry != null && (currentRegistry.equals(ClaimManager.SAFEZONE_REGISTRY) ||
                    currentRegistry.equals(ClaimManager.WARZONE_REGISTRY) ||
                    currentRegistry.equals(ClaimManager.UNCLAIMABLE_REGISTRY))) continue;

            PersistentDataUtility.remove(PersistentDataUtility.PLAYER_FACTION_REGISTRY,
                    onP.getPersistentDataContainer());
            Language.sendRawMessage("Resetted your data", onP);

            for (Faction faction : Faction.getLoadedFactions().values()) {
                if (faction.getFactionMemberManager().getMembers().contains(onP.getUniqueId())) {
                    PersistentDataUtility.write(PersistentDataUtility.PLAYER_FACTION_REGISTRY, PersistentDataType.STRING,
                            faction.getRegistryName(), onP.getPersistentDataContainer());
                    Language.sendRawMessage("Loaded data from storage", onP);
                    break;
                }
            }
        }

        Language.sendRawMessage("Regeneration of chunks. Starting garbage collection", player);
        for (World world : Bukkit.getWorlds()) {
            ArrayList<Chunk> chunks = new ArrayList<>(world.getForceLoadedChunks());
            chunks.addAll(List.of(world.getLoadedChunks()));

            for (Chunk chunk : chunks) {
                String registry = PersistentDataUtility.read(PersistentDataUtility.FACTION_CLAIMED_KEY,
                        PersistentDataType.STRING, chunk.getPersistentDataContainer());

                if (!FactionUtility.doesFactionExist(registry)) {
                    PersistentDataUtility.write(PersistentDataUtility.FACTION_CLAIMED_KEY,
                            PersistentDataType.STRING, ClaimManager.UNCLAIMED_CHUNK_REGISTRY,
                            chunk.getPersistentDataContainer());
                }

            }
        }

        Language.sendRawMessage("Finished regeneration. Restart the server to prevent RAM leaks. Currently every faction is loaded and uses max memory", player);
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
