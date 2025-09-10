package ar.edu.utn.dds.k3003.controller;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    private final Counter erroresCounter;

    public ControllerExceptionHandler(MeterRegistry registry) {
        this.erroresCounter = Counter.builder("solicitudes_errores_total")
                .description("Cantidad de errores en los endpoints de solicitudes")
                .register(registry);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        erroresCounter.increment(); // sumamos 1 al contador
        return ResponseEntity.status(500).body("Error: " + e.getMessage());
    }
}

