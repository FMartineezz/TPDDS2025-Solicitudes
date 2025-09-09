package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.antiSpam.AntiSpamService;
import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.FachadaSolicitudes;
import ar.edu.utn.dds.k3003.facades.dtos.EstadoSolicitudBorradoEnum;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.facades.dtos.SolicitudDTO;
import ar.edu.utn.dds.k3003.model.Solicitud;
import ar.edu.utn.dds.k3003.repository.SolicitudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class Fachada implements FachadaSolicitudes {
    public AntiSpamService antiSpamService;
    public FachadaFuente fuente;
    public SolicitudRepository repository;

    public Fachada(AntiSpamService antiSpamService,SolicitudRepository repository, FachadaFuente fachadaFuente){
        this.antiSpamService = antiSpamService;
        this.repository = repository;
        this.fuente = fachadaFuente;
    }


    @Override
    public SolicitudDTO agregar(SolicitudDTO solicitudDTO) {
        //validar que este asociada a un echo
        if(solicitudDTO.hechoId()== null ){
            throw new NoSuchElementException("La solicitud con id " + solicitudDTO.id() + "no tiene hecho asociado.");
        }
        // validar existencia de hecho
        HechoDTO hecho = fuente.buscarHechoXId(solicitudDTO.hechoId());
        if (hecho == null) {
            throw new NoSuchElementException("El hecho con id " + solicitudDTO.hechoId() + " no existe.");
        }
        // validar duplicado
        List<SolicitudDTO> solicitudesPorHecho = buscarSolicitudXHecho(solicitudDTO.hechoId());
        for (SolicitudDTO existente : solicitudesPorHecho) {
            if (existente.descripcion().equalsIgnoreCase(solicitudDTO.descripcion())
                    && existente.estado().equals(solicitudDTO.estado())) {
                throw new KeyAlreadyExistsException("Ya existe una solicitud para ese hecho con la misma descripción y estado.");
            }
        }
        // validar antiSpam
        if (antiSpamService.esSpam(solicitudDTO.descripcion())) {
            throw new IllegalArgumentException("La descripción contiene spam.");
        }

        Solicitud solicitud = toDomain(solicitudDTO);
        solicitud = repository.save(solicitud);
        return toDto(solicitud);
    }

    @Override
    public SolicitudDTO modificar(String id , EstadoSolicitudBorradoEnum nuevoEstado,String nuevaDescripcion) throws NoSuchElementException {
        Long idLong = Long.parseLong(id); // Conversión
        Solicitud solicitud = extraerSolicitudDelRepositorio(idLong);

        solicitud.setDescripcion(nuevaDescripcion);
        solicitud.setEstado(nuevoEstado);

        if (nuevoEstado == EstadoSolicitudBorradoEnum.ACEPTADA) {
            fuente.censurarHecho(solicitud.getHechoId()); // Llamada al proxy Fuente TODO
        }

        repository.save(solicitud);
        return toDto(solicitud);
    }

    @Override
    public List<SolicitudDTO> buscarSolicitudXHecho(String hechoId) {
        Optional<List<Solicitud>> solicitudes = repository.findByHechoId(hechoId);

        return solicitudes.map(s -> s.stream()
                        .map(this::toDto)
                        .collect(Collectors.toList()))
                .orElseGet(List::of);
    }

    @Override
    public SolicitudDTO buscarSolicitudXId(String id) {
        Long idLong = Long.parseLong(id); // Conversión aquí
        Solicitud solicitud = extraerSolicitudDelRepositorio(idLong);
        return toDto(solicitud);
    }

    @Override
    public boolean estaActivo(String s) {
        return false;
    }

    @Override
    public void setFachadaFuente(FachadaFuente fachadaFuente) {
        this.fuente = fachadaFuente;
    }

    public void vaciarSolicitudes() {
        repository.deleteAll();
    }

    //METODOS PRIVADOS
    private Solicitud extraerSolicitudDelRepositorio(Long id){
       return repository.findById(id).
                orElseThrow(()->new NoSuchElementException("La solicitud " + id + " no se encuentra en el repositorio."));
    }

    private SolicitudDTO toDto (Solicitud dominio){
        return new SolicitudDTO(String.valueOf(dominio.getId()), dominio.getDescripcion(), dominio.getEstado(), dominio.getHechoId());
    }
    private Solicitud toDomain (SolicitudDTO dto){
        return new Solicitud(dto.descripcion(),dto.estado(),dto.hechoId());
    }
}
