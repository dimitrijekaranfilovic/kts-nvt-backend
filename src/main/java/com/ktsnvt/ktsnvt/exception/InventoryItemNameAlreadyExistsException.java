package com.ktsnvt.ktsnvt.exception;

public class InventoryItemNameAlreadyExistsException extends BusinessException {

    public InventoryItemNameAlreadyExistsException(String name) {
        super(String.format("Inventory item name: %s is already in use.", name));
    }
}
