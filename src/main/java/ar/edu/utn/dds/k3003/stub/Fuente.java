package ar.edu.utn.dds.k3003.stub;

import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.FachadaProcesadorPdI;
import ar.edu.utn.dds.k3003.facades.dtos.ColeccionDTO;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.facades.dtos.PdIDTO;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Profile("local")
public class Fuente implements FachadaFuente {
    @Override
    public ColeccionDTO agregar(ColeccionDTO coleccionDTO) {
        return null;
    }

    @Override
    public ColeccionDTO buscarColeccionXId(String s) throws NoSuchElementException {
        return null;
    }

    @Override
    public HechoDTO agregar(HechoDTO hechoDTO) {
        return null;
    }

    @Override
    public HechoDTO buscarHechoXId(String s) throws NoSuchElementException {
        // Caso especial -> simulamos que no existe
        if ("not-found".equals(s)) {
            throw new NoSuchElementException("El hecho con id " + s + " no existe.");
        }

        // Fake -> devolvemos un hecho válido siempre que se pase un id distinto a "not-found"
        return new HechoDTO(s, "coleccion-fake", "titulo fake para id " + s);
    }

    @Override
    public List<HechoDTO> buscarHechosXColeccion(String s) throws NoSuchElementException {
        return List.of();
    }

    @Override
    public void setProcesadorPdI(FachadaProcesadorPdI fachadaProcesadorPdI) {

    }

    @Override
    public PdIDTO agregar(PdIDTO pdIDTO) throws IllegalStateException {
        return null;
    }

    @Override
    public List<ColeccionDTO> colecciones() {
        return List.of();
    }

    @Override
    public void censurarHecho(String hechoId){

    }
}
