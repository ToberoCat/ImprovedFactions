package io.github.toberocat.improvedfactions.data;

import io.github.toberocat.improvedfactions.mesh.HeightCache;

import java.util.LinkedList;

public class Line {
    private final LinkedList<PositionPair> locations;

    public Line(RenderLocation p1, RenderLocation p2) {
        this.locations = createPointsBetween(p1, p2);
    }

    public LinkedList<PositionPair> getLocations() {
        return locations;
    }

    private LinkedList<PositionPair> createPointsBetween(RenderLocation p1, RenderLocation p2) {
        LinkedList<PositionPair> pair = new LinkedList<>();

        p1 = min(p1, p2);
        p2 = max(p1, p2);

        if (p1.x() == p2.x()) for (int z = p1.z(); z <= p2.z(); z++)
            pair.add(new PositionPair(p1.x(), z, HeightCache.fromXZ(p1.world(), p1.x(), z)));
        else for (int x = p1.x(); x <= p2.x(); x++)
            pair.add(new PositionPair(x, p1.z(), HeightCache.fromXZ(p1.world(), x, p1.z())));

        return pair;
    }

    private RenderLocation min(RenderLocation p1, RenderLocation p2) {
        return new RenderLocation(Math.min(p1.x(), p2.x()), Math.min(p1.z(), p2.z()), p1.world());
    }


    private RenderLocation max(RenderLocation p1, RenderLocation p2) {
        return new RenderLocation(Math.max(p1.x(), p2.x()), Math.max(p1.z(), p2.z()), p1.world());
    }

    @Override
    public String toString() {
        return "Line";
    }
}
