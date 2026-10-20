package com.patitas.turno.adapter.in.web;

import com.patitas.turno.domain.exception.TurnoNotFoundException;
import com.patitas.turno.domain.exception.TurnoSuperpuestoException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class TurnoExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(TurnoNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(TurnoNotFoundException exception) {
        return problem(HttpStatus.NOT_FOUND, "Turno no encontrado", exception.getMessage());
    }

    @ExceptionHandler(TurnoSuperpuestoException.class)
    public ResponseEntity<ProblemDetail> handleConflict(TurnoSuperpuestoException exception) {
        return problem(HttpStatus.CONFLICT, "Conflicto de turno", exception.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        Map<String, String> errores = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                errores.putIfAbsent(error.getField(), error.getDefaultMessage()));
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "Revisa los campos de la solicitud");
        problem.setTitle("Datos invalidos");
        problem.setProperty("errores", errores);
        return handleExceptionInternal(exception, problem, headers, status, request);
    }

    private ResponseEntity<ProblemDetail> problem(HttpStatus status, String title, String detail) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setTitle(title);
        return ResponseEntity.status(status).body(problem);
    }
}
