package com.ktsnvt.ktsnvt.exception;

public class EmailAlreadyExistsException extends BusinessException {
    public EmailAlreadyExistsException(String email) {
        super("Email: " + email + " is already in use.");
    }
}
