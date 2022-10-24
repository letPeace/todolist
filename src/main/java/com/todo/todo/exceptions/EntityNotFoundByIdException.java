package com.todo.todo.exceptions;

import java.util.NoSuchElementException;

public class EntityNotFoundByIdException extends NoSuchElementException {
    public EntityNotFoundByIdException() {
        super();
    }
    public EntityNotFoundByIdException(String message) {
        super(message);
    }
}
