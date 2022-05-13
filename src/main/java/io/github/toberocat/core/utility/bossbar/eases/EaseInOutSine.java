package io.github.toberocat.core.utility.bossbar.eases;

import io.github.toberocat.core.utility.bossbar.Ease;

import static java.lang.Math.cos;

public class EaseInOutSine implements Ease {

    /**
     * https://easings.net/en#easeInOutSine
     * */
    @Override
    public double evaluate(double time) {
        return -(cos(Math.PI * time) - 1) / 2;
    }
}
