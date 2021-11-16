package com.ktsnvt.ktsnvt.exception;

public class InvalidEmployeeTypeException extends BusinessException{
    public InvalidEmployeeTypeException(String pin) {
        super(String.format("PIN code: %s is not valid for this type of operation.", pin));
    }
}
