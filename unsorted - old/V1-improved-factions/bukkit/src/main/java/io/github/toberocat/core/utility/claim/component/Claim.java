package io.github.toberocat.core.utility.claim.component;

public class Claim {
    public static final String UNKNOWN_CLAIM_REGISTRY = "__glb:unknown__";

    private int x, y;
    private String registry;
    private String world;

    public Claim() {
        registry = UNKNOWN_CLAIM_REGISTRY;
    }

    public Claim(int x, int y, String registry, String world) {
        this.x = x;
        this.y = y;
        this.registry = registry;
        this.world = world;
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

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }
}
