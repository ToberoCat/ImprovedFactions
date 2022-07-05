package io.github.toberocat.core.utility.events;

import io.github.toberocat.core.utility.Result;
import io.github.toberocat.core.utility.config.Config;

public interface ConfigSaveEvent {
    default SaveType isSingleCall() {
        return SaveType.Config;
    }

    default Result Save(Config config) {
        if (isSingleCall() == SaveType.Config) return SaveConfig(config);
        return SaveDataAccess();
    }

    /**
     * @param configFile The config that needs to get saved
     * @return The result, where if success false, backup will be created
     */
    default Result SaveConfig(Config configFile) {
        return new Result<>(true);
    }

    /**
     * @return A result, where paired needs to be the value, that would got saved and machineError the path. E.g: Factions/registry.json @FactionType
     */
    default Result SaveDataAccess() {
        return new Result<>(true);
    }

    enum SaveType {Config, DataAccess}
}
