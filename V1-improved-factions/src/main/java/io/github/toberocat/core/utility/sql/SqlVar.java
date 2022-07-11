package io.github.toberocat.core.utility.sql;

import org.jetbrains.annotations.NotNull;

public abstract class SqlVar<T> {
    protected final T to;
    protected final String from;

    public SqlVar(@NotNull String from, T to) {
        this.to = to;
        this.from = from;
    }

    public static SqlVar<String> of(@NotNull String from, @NotNull String to) {
        return new SqlVar<>(from, to) {
            @Override
            public String to() {
                return "'" + to + "'";
            }
        };
    }

    public static SqlVar<Integer> of(@NotNull String from, int to) {
        return new SqlVar<>(from, to) {
            @Override
            public String to() {
                return String.valueOf(to);
            }
        };
    }

    public static SqlVar<Double> of(@NotNull String from, double to) {
        return new SqlVar<>(from, to) {
            @Override
            public String to() {
                return String.valueOf(to);
            }
        };
    }

    public static SqlVar<Boolean> of(@NotNull String from, boolean to) {
        return new SqlVar<>(from, to) {
            @Override
            public String to() {
                return String.valueOf(to);
            }
        };
    }

    public String from() {
        return from;
    }

    public abstract String to();


}
