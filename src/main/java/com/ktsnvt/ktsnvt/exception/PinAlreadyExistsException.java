package com.ktsnvt.ktsnvt.exception;

public class PinAlreadyExistsException extends BusinessException {
    public PinAlreadyExistsException(String pin) {
        super("PIN code: " + pin + " is already in use");
    }
}
