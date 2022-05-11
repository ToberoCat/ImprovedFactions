package io.github.toberocat.core.extensions;

import io.github.toberocat.core.utility.version.Version;

public record ExtensionRegistry(
        String main,
        String displayName,
        String registry,
        Version version,
        Version minVersion,
        Version[] testedVersions,
        String[] dependencies) {
}
