package io.github.toberocat.improvedfactions.utility;

import java.util.Arrays;

public class Vector2 {

    private float x;
    private float y;

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    @Override
    public String toString() {
        return (int)x + " " + (int)y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public static Vector2 FromString(String args) {
        String[] params = args.split(" ");
        return new Vector2(Integer.parseInt(params[0]), Integer.parseInt(params[1]));
    }
}
