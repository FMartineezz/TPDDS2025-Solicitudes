package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.model.Solicitud;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("deploy")
public interface JpaSolicitudesRepo extends JpaRepository<Solicitud, Long>, SolicitudRepository {
    // Aquí se corrige el tipo de la clave principal a Long.
    Optional<List<Solicitud>> findByHechoId(String hechoId);
}
