package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.dto.SolicitudModificacionRequestDTO;
import ar.edu.utn.dds.k3003.facades.dtos.SolicitudDTO;
import ar.edu.utn.dds.k3003.service.Fachada;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/solicitudes")
public class SolicitudController {

    private final Fachada fachada;
    private final MeterRegistry registry;

    // contadores finales
    private final Counter solicitudesGetCounter;
    private final Counter solicitudesGetByIdCounter;
    private final Counter solicitudesPostCounter;
    private final Counter solicitudesPatchCounter;


    @Autowired
    public SolicitudController(Fachada fachada, MeterRegistry registry) {
        this.fachada = fachada;
        this.registry = registry; // importante asignar el registry antes de usarlo

        // inicializo contadores usando this.registry
        this.solicitudesGetCounter = Counter.builder("solicitudes_get_total")
                .description("Cantidad de consultas por hecho")
                .register(this.registry);

        this.solicitudesGetByIdCounter = Counter.builder("solicitudes_get_by_id_total")
                .description("Cantidad de consultas por id")
                .register(this.registry);

        this.solicitudesPostCounter = Counter.builder("solicitudes_post_total")
                .description("Cantidad de solicitudes creadas")
                .register(this.registry);

        this.solicitudesPatchCounter = Counter.builder("solicitudes_patch_total")
                .description("Cantidad de modificaciones de solicitudes")
                .register(this.registry);
    }

    @GetMapping
    public ResponseEntity<List<SolicitudDTO>> buscarPorHecho(@RequestParam("hecho") String hechoId) {
        solicitudesGetCounter.increment();
        return ResponseEntity.ok(fachada.buscarSolicitudXHecho(hechoId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudDTO> buscarPorId(@PathVariable String id) {
        solicitudesGetByIdCounter.increment();
        return ResponseEntity.ok(fachada.buscarSolicitudXId(id));
    }

    @PostMapping
    public ResponseEntity<SolicitudDTO> agregar(@RequestBody SolicitudDTO dto) {
        solicitudesPostCounter.increment();
        return ResponseEntity.ok(fachada.agregar(dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SolicitudDTO> modificar(@PathVariable String id, @RequestBody SolicitudModificacionRequestDTO body) {
        solicitudesPatchCounter.increment();
        return ResponseEntity.ok(fachada.modificar(id, body.getEstado(), body.getDescripcion()));
    }

    @DeleteMapping
    public ResponseEntity<Void> eliminarTodas() {
        fachada.vaciarSolicitudes();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/hecho/{hechoId}")
    public ResponseEntity<Boolean> estaActiva(@PathVariable String hechoId){
        return ResponseEntity.ok(fachada.estaActivo(hechoId));
    }

}