package ar.edu.utn.dds.k3003.dto;

import ar.edu.utn.dds.k3003.facades.dtos.EstadoSolicitudBorradoEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SolicitudModificacionRequestDTO {
    private String descripcion;
    private EstadoSolicitudBorradoEnum estado;
}
