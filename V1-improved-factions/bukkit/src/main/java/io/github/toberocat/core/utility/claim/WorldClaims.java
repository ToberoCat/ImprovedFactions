package io.github.toberocat.core.utility.claim;

import org.jetbrains.annotations.NotNull;

public interface WorldClaims {
    void add(@NotNull Claim claim);
    void remove(@NotNull Claim claim);
}
