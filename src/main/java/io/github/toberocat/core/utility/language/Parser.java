package io.github.toberocat.core.utility.language;

import org.bukkit.entity.Player;

import java.util.LinkedList;

public class Parser {
    private final LinkedList<Parseable> parseables;
    private final String key;

    private Parser(String key) {
        this.parseables = new LinkedList<>();
        this.key = key;
    }

    public static Parser run(String key) {
        return new Parser(key);
    }

    public void send(Player player) {
        Language.sendMessage(key, player, parseables.toArray(Parseable[]::new));
    }

    public Parser parse(String from, String to) {
        parseables.add(new Parseable(from, to));
        return this;
    }
}
