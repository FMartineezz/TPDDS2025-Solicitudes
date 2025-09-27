package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.dto.SolicitudModificacionRequestDTO;
import ar.edu.utn.dds.k3003.facades.dtos.SolicitudDTO;
import ar.edu.utn.dds.k3003.app.Fachada;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
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
    private final Timer responseTimer;
    private final Timer responseAplicationTimer;

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
        this.responseTimer = Timer.builder("solicitudes_response_time")
                .description("Tiempo de respuesta que tarda entre que llega la query hasta tirar la response")
                .register(this.registry);
        this.responseAplicationTimer = Timer.builder("solicitudes_response_application_timer")
                .description("Tiempo que tarda en responder a la app que nos consume")
                .publishPercentiles(95)
                .register(this.registry);
    }

    @GetMapping
    public ResponseEntity<List<SolicitudDTO>> buscarPorHecho(@RequestParam("hecho") String hechoId) {
        return responseTimer.record(()-> {
        solicitudesGetCounter.increment();
        return ResponseEntity.ok(fachada.buscarSolicitudXHecho(hechoId));});
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudDTO> buscarPorId(@PathVariable String id) {
        return responseTimer.record(()->{
        solicitudesGetByIdCounter.increment();
        return ResponseEntity.ok(fachada.buscarSolicitudXId(id));});
    }

    @PostMapping
    public ResponseEntity<SolicitudDTO> agregar(@RequestBody SolicitudDTO dto) {
        return responseTimer.record(()->{
        solicitudesPostCounter.increment();
        return ResponseEntity.ok(fachada.agregar(dto));});
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SolicitudDTO> modificar(@PathVariable String id, @RequestBody SolicitudModificacionRequestDTO body) {
        return responseTimer.record(()->{
        solicitudesPatchCounter.increment();
        return ResponseEntity.ok(fachada.modificar(id, body.getEstado(), body.getDescripcion()));});
    }

    @DeleteMapping
    public ResponseEntity<Void> eliminarTodas() {
        fachada.vaciarSolicitudes();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/hecho/{hechoId}")
        public ResponseEntity<Boolean> estaActiva(@PathVariable String hechoId){
        return responseAplicationTimer.record(()->{
        return ResponseEntity.ok(fachada.estaActivo(hechoId));});
    }

    @GetMapping("/hechos")
        public ResponseEntity<HashSet<String>> hechosConSolicitudes (){
        return responseAplicationTimer.record(()->{
        return ResponseEntity.ok(fachada.todosLosHechosId());});
    }

}