package com.patitas.mascota.domain.exception;

public class DuplicateMascotaException extends RuntimeException {

    public DuplicateMascotaException(String nombre, Long duenoId) {
        super("El dueno con el id " + duenoId + " ya tiene una mascota llamada " + nombre);
    }
}
