package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.model.Solicitud;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@Profile("deploy")
public interface JpaSolicitudesRepo extends JpaRepository<Solicitud, Long>, SolicitudRepository {

    Optional<List<Solicitud>> findByHechoId(String hechoId);

    @Query("SELECT s.hechoId FROM Solicitud s")
    HashSet<String> findAllHechosId();
}



