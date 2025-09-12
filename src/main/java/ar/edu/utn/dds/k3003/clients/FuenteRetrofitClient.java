package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import org.springframework.context.annotation.Profile;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

@Profile("deploy")
public interface FuenteRetrofitClient {

    @GET("hecho/{nombre}")
    Call<HechoDTO> get(@Path("nombre") String nombre);

    @PATCH("hecho/{id}/estado")
    Call<Void> censurar(@Path("id") String id);
}
