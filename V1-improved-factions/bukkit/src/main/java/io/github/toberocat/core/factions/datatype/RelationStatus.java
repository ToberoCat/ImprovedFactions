package io.github.toberocat.core.factions.datatype;

public enum RelationStatus {
    ALLY(0),
    NEUTRAL(1),
    ENEMY(2);

    int id;

    RelationStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
