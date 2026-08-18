package com.solgas.solgascmsapi.exception;

public class ImageNotFoundException extends RuntimeException {

    public ImageNotFoundException() {
        super("Imagen no encontrada");
    }
}
