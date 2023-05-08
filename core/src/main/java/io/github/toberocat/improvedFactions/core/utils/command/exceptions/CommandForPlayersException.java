package io.github.toberocat.improvedFactions.core.utils.command.exceptions;

import java.util.HashMap;

public class CommandForPlayersException extends CommandException {
    public CommandForPlayersException() {
        super("exceptions.requires-player", HashMap::new);
    }
}
