package ar.edu.utn.dds.k3003.facades;

import ar.edu.utn.dds.k3003.facades.dtos.ColeccionDTO;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.facades.dtos.PdIDTO;
import java.util.List;
import java.util.NoSuchElementException;

public interface FachadaFuente {
    ColeccionDTO agregar(ColeccionDTO var1);

    ColeccionDTO buscarColeccionXId(String var1) throws NoSuchElementException;

    HechoDTO agregar(HechoDTO var1);

    HechoDTO buscarHechoXId(String var1) throws NoSuchElementException;

    List<HechoDTO> buscarHechosXColeccion(String var1) throws NoSuchElementException;

    void setProcesadorPdI(FachadaProcesadorPdI var1);

    PdIDTO agregar(PdIDTO var1) throws IllegalStateException;

    List<ColeccionDTO> colecciones();

    void censurarHecho(String hechoId);
}