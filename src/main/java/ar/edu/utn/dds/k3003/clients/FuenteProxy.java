package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.FachadaProcesadorPdI;
import ar.edu.utn.dds.k3003.facades.dtos.ColeccionDTO;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.facades.dtos.PdIDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.HttpStatus;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.List;
import java.util.NoSuchElementException;

@Profile("deploy")
@Service
public class FuenteProxy implements FachadaFuente {

    private final String endpoint;
    private final FuenteRetrofitClient service;

    public FuenteProxy(ObjectMapper objectMapper) {
        var env = System.getenv();
        this.endpoint = env.getOrDefault("URL_FUENTE", "https://fuentegrupo10-1.onrender.com/"); // url de Fuente TODO http://localhost:8082/

        var retrofit =
                new Retrofit.Builder()
                        .baseUrl(this.endpoint)
                        .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                        .build();

        this.service = retrofit.create(FuenteRetrofitClient.class);
    }

    @SneakyThrows
    @Override
    public HechoDTO buscarHechoXId(String hechoId) throws NoSuchElementException {
        Response<HechoDTO> response = service.get(hechoId).execute();

        if (response.isSuccessful()) {
            return response.body();
        }
        if (response.code() == HttpStatus.NOT_FOUND.getCode()) {
            throw new NoSuchElementException("No se encontró el hecho " + hechoId);
        }
        throw new RuntimeException("Error conectándose con el componente Fuente");
    }

    @SneakyThrows
    @Override
    public void censurarHecho(String hechoId) {
        Response<Void> response = service.censurar(hechoId).execute();

        if (!response.isSuccessful()) {
            throw new RuntimeException("Error censurando hecho en Fuente: " + response.code());
        }
    }


    // Métodos de FachadaFuente  no usados
    @Override
    public ColeccionDTO agregar(ColeccionDTO coleccionDTO) {
        throw new UnsupportedOperationException("No implementado en Solicitudes");
    }

    @Override
    public ColeccionDTO buscarColeccionXId(String s) throws NoSuchElementException {
        throw new UnsupportedOperationException("No implementado en Solicitudes");
    }

    @Override
    public HechoDTO agregar(HechoDTO hechoDTO) {
        throw new UnsupportedOperationException("No implementado en Solicitudes");
    }

    @Override
    public List<HechoDTO> buscarHechosXColeccion(String s) throws NoSuchElementException {
        throw new UnsupportedOperationException("No implementado en Solicitudes");
    }

    @Override
    public void setProcesadorPdI(FachadaProcesadorPdI fachadaProcesadorPdI) {
        // No se usa en Solicitudes
    }

    @Override
    public PdIDTO agregar(PdIDTO pdIDTO) throws IllegalStateException {
        throw new UnsupportedOperationException("No implementado en Solicitudes");
    }

    @Override
    public List<ColeccionDTO> colecciones() {
        throw new UnsupportedOperationException("No implementado en Solicitudes");
    }
}
