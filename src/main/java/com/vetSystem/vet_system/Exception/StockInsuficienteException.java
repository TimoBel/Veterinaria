package com.vetSystem.vet_system.Exception;

public class StockInsuficienteException extends RuntimeException {

    public StockInsuficienteException(Long medicamentoId) {
        super("El medicamento con id " + medicamentoId
                + " no tiene stock disponible");
    }
}