package com.vetSystem.vet_system.Exception;

public class CupoMascotasException extends RuntimeException {

    public CupoMascotasException(Long duenioId) {
        super("El dueño con id " + duenioId
                + " ya alcanzó el límite de 5 mascotas activas");
    }
}