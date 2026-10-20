package com.patitas.veterinario.domain.exception;

public class MatriculaDuplicadaException extends RuntimeException {

    public MatriculaDuplicadaException(String matricula) {
        super("Ya existe un veterinario con la matricula " + matricula);
    }
}
