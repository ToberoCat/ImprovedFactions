package io.github.toberocat.core.utility.language;

/**
 * This class is used to replace a char from a .lang message with a string. E.g: {faction_name} => name of your faction
 */
public class Parseable {

    private String parse;
    private String to;

    /**
     * Create a new parseable
     *
     * @param parse The string sequence that needs to be replaced
     * @param to    The sequence the old should be replaced with
     */
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
