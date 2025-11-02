package com.ncu.banking.exception;

public class DatabaseException extends RuntimeException {

    //  Constructor that accepts only a message
    public DatabaseException(String message) {
        super(message);
    }

    // Constructor that accepts a message and a cause (chained exception)
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
