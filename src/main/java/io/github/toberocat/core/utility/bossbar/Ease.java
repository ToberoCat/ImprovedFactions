package io.github.toberocat.core.utility.bossbar;

public interface Ease {
    /**
     * Given a time, return the value of the function at that time.
     * Returned value needs to be normalised, speaking 0 - 1
     *
     * @param time The time at which you want to evaluate the function.
     * @return The value of the function at the given time.
     */
    double evaluate(double time);
}
