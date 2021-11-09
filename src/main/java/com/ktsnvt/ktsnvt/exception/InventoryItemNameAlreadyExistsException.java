package com.ktsnvt.ktsnvt.exception;

public class InventoryItemNameAlreadyExistsException extends BusinessException {

    public InventoryItemNameAlreadyExistsException(String name) {
        super("Inventory item name: " + name + " is already in use");
    }
}
