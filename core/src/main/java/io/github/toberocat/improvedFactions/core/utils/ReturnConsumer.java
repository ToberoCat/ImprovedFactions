package io.github.toberocat.improvedFactions.core.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ReturnConsumer<T, R> {
    @Nullable R accept(@NotNull T t);
}
