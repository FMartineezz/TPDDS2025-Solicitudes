package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.model.Solicitud;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@Profile("local")
public class InMemoryRepository implements SolicitudRepository {
    private List<Solicitud> solicitudes;
    private Long nextId = 1L;

    public InMemoryRepository() {
        this.solicitudes = new ArrayList<>();
    }

    @Override
    public List<Solicitud> findAll() {
        return new ArrayList<>(solicitudes);
    }

    @Override
    public Optional<List<Solicitud>> findByHechoId(String hechoId) {
        List<Solicitud> solicitudesFiltradas = this.solicitudes.stream()
                .filter(solicitud -> Objects.equals(solicitud.getHechoId(), hechoId))
                .collect(Collectors.toList());

        return Optional.of(solicitudesFiltradas);
    }

    @Override
    public Optional<Solicitud> findById(Long id) {
        return this.findAll().stream()
                .filter(solicitud -> Objects.equals(solicitud.getId(), id))
                .findFirst();
    }

    @Override
    public Solicitud save(Solicitud solicitud) {
        if(solicitud.getId()== null){
            solicitud.setId(nextId++);
        }
        this.solicitudes.add(solicitud);
        return solicitud;
    }

    @Override
    public void deleteAll() {
        solicitudes.clear();
    }

    @Override
    public HashSet<String> findAllHechosId(){
        HashSet<String> hechosIds = new HashSet<>();
        for(Solicitud solicitud : solicitudes ){
            hechosIds.add(solicitud.getHechoId());
        }
        return hechosIds;
    }
}
