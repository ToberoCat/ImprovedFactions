package io.github.toberocat.improvedFactions.core.oldcommand.component.quick;

import io.github.toberocat.improvedFactions.core.oldcommand.component.SubCommand;
import org.jetbrains.annotations.NotNull;


public interface QuickCommand<T extends QuickCommand<T>> {
    @NotNull T child(SubCommand command);
}
