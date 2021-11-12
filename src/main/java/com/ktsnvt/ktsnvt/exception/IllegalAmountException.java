package com.ktsnvt.ktsnvt.exception;

public class IllegalAmountException extends BusinessException{
    protected IllegalAmountException(String message) {
        super(message);
    }

    public IllegalAmountException(Integer providedAmount){
        super(String.format("Amount cannot be lower than or equal to 0; %d provided.", providedAmount));
    }
}

