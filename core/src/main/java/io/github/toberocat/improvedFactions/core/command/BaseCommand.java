package io.github.toberocat.improvedFactions.core.command;

import io.github.toberocat.improvedFactions.core.command.component.Command;
import io.github.toberocat.improvedFactions.core.command.component.CommandSettings;
import io.github.toberocat.improvedFactions.core.command.sub.admin.AdminRoot;
import io.github.toberocat.improvedFactions.core.command.sub.chunk.ClaimCommand;
import io.github.toberocat.improvedFactions.core.command.sub.chunk.UnclaimCommand;
import io.github.toberocat.improvedFactions.core.command.sub.chunk.zone.ZoneRootCommand;
import io.github.toberocat.improvedFactions.core.command.sub.faction.CreateFactionCommand;
import io.github.toberocat.improvedFactions.core.command.sub.faction.DeleteFactionCommand;
import io.github.toberocat.improvedFactions.core.command.sub.faction.SettingCommand;
import io.github.toberocat.improvedFactions.core.command.sub.gui.GuiRoot;
import io.github.toberocat.improvedFactions.core.command.sub.invite.AcceptInviteCommand;
import io.github.toberocat.improvedFactions.core.command.sub.invite.InviteCommand;
import io.github.toberocat.improvedFactions.core.command.sub.member.JoinFactionCommand;
import io.github.toberocat.improvedFactions.core.command.sub.member.LeaveFactionCommand;
import io.github.toberocat.improvedFactions.core.command.sub.utils.BakePermissionsCommand;
import io.github.toberocat.improvedFactions.core.command.sub.utils.ListFactionCommand;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.translator.layout.Translatable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class BaseCommand extends Command<SettingCommand.SettingPacket, Command.ConsoleCommandPacket> {

    private final SettingCommand settingCommand;

    public BaseCommand() {
        settingCommand = new SettingCommand();

        add(new CreateFactionCommand());
        add(new DeleteFactionCommand());
        add(new ListFactionCommand());
        add(new ClaimCommand());
        add(new UnclaimCommand());
        add(new JoinFactionCommand());
        add(new LeaveFactionCommand());
        add(new InviteCommand());
        add(new AcceptInviteCommand());
        add(new BakePermissionsCommand());
        add(new ZoneRootCommand());
        add(new AdminRoot());
        add(new GuiRoot());
        add(settingCommand);
    }

    @Override
    public boolean isAdmin() {
        return false;
    }

    @Override
    public @NotNull String label() {
        return "faction";
    }

    @Override
    public @NotNull CommandSettings createSettings() {
        return new BaseSettings(node);
    }

    @Override
    public @NotNull List<String> tabCompletePlayer(@NotNull FactionPlayer<?> player, @NotNull String[] args) {
        return new ArrayList<>(commands.keySet());
    }

    @Override
    public @NotNull List<String> tabCompleteConsole(@NotNull String[] args) {
        return new ArrayList<>(commands.keySet());
    }

    @Override
    public void run(@NotNull SettingCommand.SettingPacket packet) {
        settingCommand.run(packet);
    }

    @Override
    public void runConsole(@NotNull ConsoleCommandPacket packet) {

    }

    @Override
    public @Nullable SettingCommand.SettingPacket createFromArgs(@NotNull FactionPlayer<?> executor, @NotNull String[] args) {
        return settingCommand.createFromArgs(executor, args);
    }

    @Override
    public @Nullable Command.ConsoleCommandPacket createFromArgs(@NotNull String[] args) {
        return new ConsoleCommandPacket() {
        };
    }

    protected class BaseSettings extends CommandSettings {

        public BaseSettings(@NotNull Function<Translatable, Map<String, String>> node) {
            super(node);
        }

        @Override
        public boolean showTab(@NotNull FactionPlayer<?> player) {
            return settingCommand.settings().showTab(player);
        }

        @Override
        public boolean showTabConsole() {
            return settingCommand.settings().showTabConsole();
        }

        @Override
        public @NotNull SettingResult canExecute(@NotNull FactionPlayer<?> player) {
            return settingCommand.settings().canExecute(player);
        }
    }
}
