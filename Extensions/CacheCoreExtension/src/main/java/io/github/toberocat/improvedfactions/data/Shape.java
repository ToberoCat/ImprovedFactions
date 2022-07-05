package io.github.toberocat.improvedfactions.data;

import org.bukkit.util.BoundingBox;

import java.util.LinkedList;

public record Shape(LinkedList<Line> lines, BoundingBox box) {
}
