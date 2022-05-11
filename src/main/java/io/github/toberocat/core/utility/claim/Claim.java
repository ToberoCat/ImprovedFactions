package io.github.toberocat.core.utility.claim;

public class Claim {
    public static final String UNKNOWN_CLAIM_REGISTRY = "__glb:unknown__";

    private int x, y;
    private String registry;

    public Claim() {
        registry = UNKNOWN_CLAIM_REGISTRY;
    }

    public Claim(int x, int y, String registry) {
        this.x = x;
        this.y = y;
        this.registry = registry;
    }

    /**
     * Used for migration from v1 pre release 2 to the current version
     */
    public void setWidth(int width) {
        this.x = width;
    }

    /**
     * Used for migration from v1 pre release 2 to the current version
     */
    public void setHeight(int height) {
        this.y = height;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getRegistry() {
        return registry;
    }

    public void setRegistry(String registry) {
        this.registry = registry;
    }
}
