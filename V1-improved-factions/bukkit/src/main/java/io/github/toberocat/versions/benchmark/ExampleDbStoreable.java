package io.github.toberocat.versions.benchmark;

import io.github.toberocat.core.utility.data.annotation.DatabaseField;

public class ExampleDbStoreable {

    @DatabaseField
    private String carName;

    @DatabaseField
    private final int buildYear;
    public String marke;

    public ExampleDbStoreable() {
        this.carName = "Ylo";
        this.buildYear = 1945;
        this.marke = "BMW";
    }

    public String getMarke() {
        return marke;
    }
}
