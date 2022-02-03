package io.github.toberocat.core.utility.sql;

/**
 * This error gets thrown by the SQL class
 */
public class SQLValueError extends Exception {
    public SQLValueError(String message) {
        super(message);
    }
}
