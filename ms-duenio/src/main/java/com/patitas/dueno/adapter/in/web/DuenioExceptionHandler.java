package com.patitas.dueno.adapter.in.web;

import com.patitas.dueno.domain.exception.DuenioNotFoundException;
import com.patitas.dueno.domain.exception.DuplicateEmailException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class DuenioExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DuenioNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDuenioNotFound(
            DuenioNotFoundException exception, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                crearError(HttpStatus.NOT_FOUND, exception.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEmail(
            DuplicateEmailException exception, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                crearError(HttpStatus.CONFLICT, exception.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedError(
            Exception exception, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                crearError(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Se produjo un error inesperado en el servidor", request.getRequestURI()));
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception exception,
            Object body,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        String mensaje = "La solicitud no es valida";
        if (exception instanceof MethodArgumentNotValidException validationException) {
            mensaje = validationException.getBindingResult().getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining("; "));
        }
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        ErrorResponse error = crearError(status, mensaje, path);
        return super.handleExceptionInternal(exception, error, headers, status, request);
    }

    private ErrorResponse crearError(HttpStatusCode status, String mensaje, String path) {
        return new ErrorResponse(
                LocalDateTime.now(), status.value(),
                HttpStatus.valueOf(status.value()).getReasonPhrase(), mensaje, path);
    }
}
