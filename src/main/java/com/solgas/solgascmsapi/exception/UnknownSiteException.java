package com.solgas.solgascmsapi.exception;

public class UnknownSiteException extends RuntimeException {

    public UnknownSiteException(String slug) {
        super("Sitio no encontrado: " + slug);
    }
}
