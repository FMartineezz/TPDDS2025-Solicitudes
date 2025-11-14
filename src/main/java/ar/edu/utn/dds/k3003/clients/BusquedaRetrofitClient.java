package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.facades.dtos.SolicitudDTO;
import org.springframework.context.annotation.Profile;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

import java.io.IOException;

@Profile("deploy")
public interface BusquedaRetrofitClient {

    @POST("events/hechoHide")
    Call<Void> hideHecho(@Body SolicitudDTO dto);
}
