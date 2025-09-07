package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.facades.dtos.EstadoSolicitudBorradoEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
public class Solicitud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    private Long id;
    @Setter
    private String descripcion;
    @Setter
    @Enumerated(EnumType.STRING)
    private EstadoSolicitudBorradoEnum estado;
    private String hechoId;

    public Solicitud() {
    }

    public Solicitud(String descripcion, EstadoSolicitudBorradoEnum estado, String hechoId) {
        this.descripcion = descripcion;
        this.estado = estado;
        this.hechoId = hechoId;
    }


}
