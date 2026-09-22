package com.vetSystem.vet_system.Exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> manejarValidacion(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {

        String mensaje = exception.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return construirRespuesta(HttpStatus.BAD_REQUEST, mensaje, request);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> manejarRecursoNoEncontrado(
            ResourceNotFoundException exception,
            HttpServletRequest request) {

        return construirRespuesta(HttpStatus.NOT_FOUND, exception.getMessage(), request);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> manejarRecursoDuplicado(
            DuplicateResourceException exception,
            HttpServletRequest request) {

        return construirRespuesta(HttpStatus.CONFLICT, exception.getMessage(), request);
    }

    @ExceptionHandler(TurnoSuperpuestoException.class)
    public ResponseEntity<ErrorResponse> manejarTurnoSuperpuesto(
            TurnoSuperpuestoException exception,
            HttpServletRequest request) {

        return construirRespuesta(HttpStatus.CONFLICT, exception.getMessage(), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejarErrorInesperado(
            Exception exception,
            HttpServletRequest request) {

        return construirRespuesta(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocurrió un error inesperado en el servidor",
                request);
    }

    @ExceptionHandler(StockInsuficienteException.class)
    public ResponseEntity<ErrorResponse> manejarStockInsuficiente(
            StockInsuficienteException exception,
            HttpServletRequest request) {

        return construirRespuesta(
                HttpStatus.UNPROCESSABLE_ENTITY,
                exception.getMessage(),
                request
        );
    }

    private ResponseEntity<ErrorResponse> construirRespuesta(
            HttpStatus status,
            String mensaje,
            HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now().toString(),
                status.value(),
                status.getReasonPhrase(),
                mensaje,
                request.getRequestURI());

        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(CupoMascotasException.class)
    public ResponseEntity<ErrorResponse> manejarCupoMascotas(
            CupoMascotasException exception,
            HttpServletRequest request) {

        return construirRespuesta(
                HttpStatus.UNPROCESSABLE_ENTITY,
                exception.getMessage(),
                request
        );
    }

}
