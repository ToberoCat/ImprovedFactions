package io.github.toberocat.improvedfactions.factions;

import io.github.toberocat.improvedfactions.utility.configs.DataManager;

public abstract class FactionData {

    public abstract void Save(Faction faction, DataManager dataManager);
    public abstract void Load(Faction faction, DataManager dataManager);
}
