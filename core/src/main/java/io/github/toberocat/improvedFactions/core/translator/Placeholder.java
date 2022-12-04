package io.github.toberocat.improvedFactions.core.translator;

import java.util.Objects;

public final class Placeholder {
    private final String from;
    private final String to;

    public Placeholder(String from, String to) {
        this.from = "{" + from + "}";
        this.to = to;
    }

    public String from() {
        return from;
    }

    public String to() {
        return to;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Placeholder) obj;
        return Objects.equals(this.from, that.from) &&
                Objects.equals(this.to, that.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    @Override
    public String toString() {
        return "Placeholder[" +
                "from=" + from + ", " +
                "to=" + to + ']';
    }


}
