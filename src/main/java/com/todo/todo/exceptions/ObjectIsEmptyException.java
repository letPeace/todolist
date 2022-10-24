package com.todo.todo.exceptions;

public class ObjectIsEmptyException extends RuntimeException {
    public ObjectIsEmptyException() {
        super();
    }
    public ObjectIsEmptyException(String message) {
        super(message);
    }
}
