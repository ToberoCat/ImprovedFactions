package io.github.toberocat.core.utility.exceptions;

public class DatabaseAccessException extends RuntimeException {
    public DatabaseAccessException(String errMessage) {
        super(errMessage);
    }

}
