package io.github.toberocat.improvedfactions.particle;

import io.github.toberocat.core.utility.settings.type.SettingEnum;

public enum ParticlePerformance implements SettingEnum {
    FAST(4), NORMAL(2), FANCY(1), HIDDEN(-1);

    int density;
    ParticlePerformance(int density) {
        this.density = density;
    }

    @Override
    public String getDisplay() {
        return name();
    }
}
