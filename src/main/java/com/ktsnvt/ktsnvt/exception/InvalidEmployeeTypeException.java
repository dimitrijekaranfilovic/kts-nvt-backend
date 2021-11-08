package com.ktsnvt.ktsnvt.exception;

public class InvalidEmployeeTypeException extends BusinessException{
    public InvalidEmployeeTypeException(String pin) {
        super("PIN code: " + pin + " is not valid for this type of operation.");
    }
}
