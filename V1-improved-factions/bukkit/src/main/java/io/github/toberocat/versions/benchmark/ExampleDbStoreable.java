package io.github.toberocat.versions.benchmark;

public class ExampleDbStoreable {

    private String carName;

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
