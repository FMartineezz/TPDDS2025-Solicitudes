package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.dto.SolicitudModificacionRequestDTO;
import ar.edu.utn.dds.k3003.facades.dtos.SolicitudDTO;
import ar.edu.utn.dds.k3003.service.Fachada;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/solicitudes")
public class SolicitudController {

    private final Fachada fachada;

    @Autowired
    public SolicitudController(Fachada fachada) {
        this.fachada = fachada;
    }

    @GetMapping
    public ResponseEntity<List<SolicitudDTO>> buscarPorHecho(@RequestParam("hecho") String hechoId) {
        return ResponseEntity.ok(fachada.buscarSolicitudXHecho(hechoId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudDTO> buscarPorId(@PathVariable String id) {
            return ResponseEntity.ok(fachada.buscarSolicitudXId(id));
    }

    @PostMapping
    public ResponseEntity<SolicitudDTO> agregar(@RequestBody SolicitudDTO dto) {
        return ResponseEntity.ok(fachada.agregar(dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SolicitudDTO> modificar(@PathVariable String id, @RequestBody SolicitudModificacionRequestDTO body) {
        return ResponseEntity.ok(fachada.modificar(id, body.getEstado(), body.getDescripcion()));
    }

    @DeleteMapping
    public ResponseEntity<Void> eliminarTodas() {
        fachada.vaciarSolicitudes();
        return ResponseEntity.noContent().build();
    }


}