package io.github.toberocat.improvedFactions.core.utils.command.options;

import io.github.toberocat.improvedFactions.core.handler.ConfigFile;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.utils.CooldownManager;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Options {
    public static final Set<OptionFactory> CONFIG_OPTION_FACTORIES = new HashSet<>();
    private final Set<Option> tab = new HashSet<>();
    private final Set<Option> command = new HashSet<>();

    static {
        addFactory((config, options) -> {
            CooldownOption cooldownManager = new CooldownOption(CooldownManager.createManager(
                    config.getEnum("cooldown-unit", TimeUnit.class).orElse(TimeUnit.SECONDS),
                    config.getInt("cooldown", 0)));
            options.cmdOpt(cooldownManager).tabOpt(cooldownManager);
        });
    }

    public static void addFactory(@NotNull OptionFactory factory) {
        CONFIG_OPTION_FACTORIES.add(factory);
    }

    public static @NotNull Options getFromConfig(@NotNull String path) {
        Options options = new Options();
        ConfigFile configFile = ImprovedFactions.api().getConfig("commands.yml").getSection(path);
        CONFIG_OPTION_FACTORIES.forEach(x -> x.create(configFile, options));
        return options;
    }

    public @NotNull Options opt(@NotNull Option option) {
        command.add(option);
        tab.add(option);
        return this;
    }

    public @NotNull Options cmdOpt(@NotNull Option option) {
        command.add(option);
        return this;
    }

    public @NotNull Options tabOpt(@NotNull Option option) {
        tab.add(option);
        return this;
    }

    public @NotNull Option[] getCommandOptions() {
        return command.toArray(Option[]::new);
    }

    public @NotNull Option[] getTabOptions() {
        return command.toArray(Option[]::new);
    }
}
