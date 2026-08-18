package com.solgas.solgascmsapi.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException() {
        super("Producto no encontrado");
    }
}
