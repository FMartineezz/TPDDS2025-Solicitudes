package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.model.Solicitud;
import java.util.List;
import java.util.Optional;

public interface SolicitudRepository {
        List<Solicitud> findAll();
        Optional<List<Solicitud>> findByHechoId (String hechoId);
        Optional<Solicitud> findById(String id);
        Solicitud save(Solicitud solicitud);
        void deleteAll();
}
