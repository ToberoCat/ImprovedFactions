package io.github.toberocat.core.utility;

/**
 * This class is used to return messages next to a success boolean
 */
public class Result<T> {

    private final boolean success;
    private String machineMessage;
    private String playerMessage;
    private T paired;

    public Result(boolean success) {
        this.success = success;
    }

    public T getPaired() {
        return paired;
    }

    public Result<T> setPaired(T paired) {
        this.paired = paired;
        return this;
    }

    public Result<T> setMessages(String machineMessage, String playerMessage) {
        this.machineMessage = machineMessage;
        this.playerMessage = playerMessage;
        return this;
    }

    public String getMachineMessage() {
        return machineMessage;
    }

    public Result setMachineMessage(String machineMessage) {
        this.machineMessage = machineMessage;
        return this;
    }

    public String getPlayerMessage() {
        return playerMessage;
    }

    public Result setPlayerMessage(String playerMessage) {
        this.playerMessage = playerMessage;
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public static <T> Result<T> success() {
        return new Result(true);
    }

    public static <T> Result<T> failure(String machineMessage, String playerMessage) {
        return new Result<T>(false).setMessages(machineMessage, playerMessage);
    }
}

