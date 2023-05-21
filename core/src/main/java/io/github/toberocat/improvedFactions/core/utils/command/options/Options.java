package io.github.toberocat.improvedFactions.core.utils.command.options;

import io.github.toberocat.improvedFactions.core.handler.ConfigFile;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.utils.CooldownManager;
import io.github.toberocat.improvedFactions.core.utils.command.SubCommand;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

public class Options {
    public static final Set<OptionFactory> CONFIG_OPTION_FACTORIES = new HashSet<>();
    private final List<Option> tab = new LinkedList<>();
    private final List<Option> command = new LinkedList<>();

    static {
        addFactory((config, options) -> {
            CooldownManager cooldownManager = CooldownManager.createManager(
                    config.getEnum("cooldown-unit", TimeUnit.class).orElse(TimeUnit.SECONDS),
                    config.getInt("cooldown", 0));
            options.cmdOpt(new CooldownOption(cooldownManager))
                    .tabOpt(new CooldownTabOption(cooldownManager, config.getBool("cooldown-hide", false)));
        });
    }

    public static void addFactory(@NotNull OptionFactory factory) {
        CONFIG_OPTION_FACTORIES.add(factory);
    }

    public static @NotNull Options getFromConfig(@NotNull String path) {
        return getFromConfig(path, null);
    }

    public static @NotNull Options getFromConfig(@NotNull String path,
                                                 @Nullable BiConsumer<Options, ConfigFile> loadExtra) {
        Options options = new Options();
        ConfigFile configFile = SubCommand.getConfig(path);
        CONFIG_OPTION_FACTORIES.forEach(x -> x.create(configFile, options));
        if (loadExtra != null)
            loadExtra.accept(options, configFile);
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
        return tab.toArray(Option[]::new);
    }
}
