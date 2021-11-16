package com.ktsnvt.ktsnvt.exception;

public class PinAlreadyExistsException extends BusinessException {
    public PinAlreadyExistsException(String pin) {
        super(String.format("PIN code: %s is already in use.", pin));
    }
}
