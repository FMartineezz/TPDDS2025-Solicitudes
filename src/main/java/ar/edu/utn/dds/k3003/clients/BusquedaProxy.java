package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.busqueda.BusquedaPort;
import ar.edu.utn.dds.k3003.facades.dtos.SolicitudDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Profile;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;




@Profile("deploy")
public class BusquedaProxy implements BusquedaPort {
    private String endpoint;
    private BusquedaRetrofitClient service;

    public BusquedaProxy(ObjectMapper mapper) {
        var env = System.getenv();
        this.endpoint = env.getOrDefault("URL_BUSQUEDA", "https://busquedaservice.onrender.com/");

        var retrofit = new Retrofit.Builder().
                baseUrl(this.endpoint).
                addConverterFactory(JacksonConverterFactory.create(mapper)).build();
        this.service = retrofit.create(BusquedaRetrofitClient.class);
    }

    @SneakyThrows
    @Override
    public void hideHecho(SolicitudDTO dto){
        Response<Void> response = service.hideHecho(dto).execute();

        if (!response.isSuccessful()) {
            throw new RuntimeException("Error borrando solicitud de borrado" + response.code());
        }
    }

}