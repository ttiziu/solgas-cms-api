package com.solgas.solgascmsapi.exception;

public class DuplicateProductKeyException extends RuntimeException {

    public DuplicateProductKeyException(String key) {
        super("Ya existe un producto con la clave: " + key);
    }
}
