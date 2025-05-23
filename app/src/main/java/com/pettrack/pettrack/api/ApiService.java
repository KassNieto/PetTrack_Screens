package com.pettrack.pettrack.api;

import com.pettrack.pettrack.models.LoginRequest;
import com.pettrack.pettrack.models.LoginResponse;
import com.pettrack.pettrack.models.Mascota;
import com.pettrack.pettrack.models.User;
import com.pettrack.pettrack.models.signup.RegisterRequest;
import com.pettrack.pettrack.models.signup.ApiResponse;
import com.pettrack.pettrack.models.cartillavacunacion.Vacuna;
import com.pettrack.pettrack.models.cartillavacunacion.Desparasitacion;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    // Usuarios
    @POST("/usuarios/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @POST("/usuarios")
    Call<ApiResponse> registerUser(@Body RegisterRequest registerRequest);

    @GET("/usuarios/{id}")
    Call<User> getUserById(@Path("id") int userId);

    // Mascotas
    @GET("/usuarios/{id}/mascotas")
    Call<List<Mascota>> getMascotasByUsuarioId(@Path("id") int usuarioId);

    // Vacunas
    @GET("/mascotas/{id}/vacunas")
    Call<List<Vacuna>> getVacunasByMascotaId(@Path("id") int mascotaId);

    @GET("/mascotas/{id}/vacunas/{vacunaId}")
    Call<Vacuna> getVacunaById(@Path("id") int mascotaId, @Path("vacunaId") int vacunaId);

    @POST("/mascotas/{id}/vacunas")
    Call<Vacuna> agregarVacuna(@Path("id") int mascotaId, @Body Vacuna vacuna);

    // Desparasitaciones
    @GET("/mascotas/{id}/desparasitaciones")
    Call<List<Desparasitacion>> getDesparasitacionesByMascotaId(@Path("id") int mascotaId);

    @GET("/desparasitaciones/{id}")
    Call<Desparasitacion> getDesparasitacionById(@Path("id") int desparasitacionId);

    @POST("/mascotas/{id}/desparasitaciones")
    Call<Desparasitacion> agregarDesparasitacion(@Path("id") int mascotaId, @Body Desparasitacion desparasitacion);
}
