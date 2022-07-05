package io.github.toberocat.improvedfactions.language;

public class Parseable {

    private String parse;
    private String to;

    public Parseable(String parse, String to) {
        this.parse = parse;
        this.to = to;
    }

    public String getParse() {
        return parse;
    }

    public void setParse(String parse) {
        this.parse = parse;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
