
package com.pettrack.pettrack.api;

import com.pettrack.pettrack.models.LoginRequest;
import com.pettrack.pettrack.models.LoginResponse;
import com.pettrack.pettrack.models.Mascota;
import com.pettrack.pettrack.models.User;
import com.pettrack.pettrack.models.cartillavacunacion.Desparasitacion;
import com.pettrack.pettrack.models.signup.RegisterRequest;
import com.pettrack.pettrack.models.signup.ApiResponse;
import com.pettrack.pettrack.models.cartillavacunacion.Vacuna;
import com.pettrack.pettrack.models.historialmedico.HistorialMedicoUploader;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {

    // Autenticación
    @POST("usuarios/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    // Registro de usuario
    @POST("usuarios")
    Call<ApiResponse> registerUser(@Body RegisterRequest registerRequest);

    // Obtener usuario por id
    @GET("usuarios/{id}")
    Call<User> getUserById(@Path("id") int userId);

    // Mascotas del usuario
    @GET("usuarios/{id}/mascotas")
    Call<List<Mascota>> getMascotasByUsuarioId(@Path("id") int usuarioId);

    // Crear mascota con imagen
    @Multipart
    @POST("usuarios/{usuarioId}/mascotas/con-imagen")
    Call<Mascota> crearMascotaConImagen(
            @Path("usuarioId") int usuarioId,
            @Part("mascota") RequestBody mascota,
            @Part MultipartBody.Part foto
    );

    // Obtener mascota por id
    @GET("usuarios/{userId}/mascotas/{mascotaId}")
    Call<Mascota> getMascotaById(
            @Path("userId") int userId,
            @Path("mascotaId") int mascotaId
    );

    // Vacunas
    @GET("mascotas/{mascotaId}/vacunas")
    Call<List<Vacuna>> getVacunasByMascotaId(@Path("mascotaId") int mascotaId);

    @GET("mascotas/{mascotaId}/vacunas/{vacunaId}")
    Call<Vacuna> getVacunaById(@Path("mascotaId") int mascotaId, @Path("vacunaId") int vacunaId);

    @POST("mascotas/{mascotaId}/vacunas")
    Call<Vacuna> agregarVacuna(@Path("mascotaId") int mascotaId, @Body Vacuna vacuna);

    // Desparasitaciones
    @GET("mascotas/{mascotaId}/desparasitaciones")
    Call<List<Desparasitacion>> getDesparasitacionesByMascotaId(@Path("mascotaId") int mascotaId);

    @GET("desparasitaciones/{id}")
    Call<Desparasitacion> getDesparasitacionById(@Path("id") int desparasitacionId);

    @POST("mascotas/{mascotaId}/desparasitaciones")
    Call<Desparasitacion> agregarDesparasitacion(
            @Path("mascotaId") int mascotaId,
            @Body Desparasitacion desparasitacion
    );


    @Multipart
    @POST("mascotas/{id}/historial")
    Call<HistorialMedicoUploader> subirHistorialMedico(
            @Path("id") int mascotaId,
            @Part MultipartBody.Part archivo
    );

    // Obtener lista de archivos del historial médico de una mascota
    @GET("mascotas/{mascotaId}/historial-medico")
    Call<List<HistorialMedicoUploader>> getHistorialMedico(
            @Path("mascotaId") int mascotaId
    );

    // Obtener un archivo específico del historial médico
    @GET("mascotas/{mascotaId}/historial-medico/{id}")
    Call<HistorialMedicoUploader> getHistorialMedicoPorId(
            @Path("mascotaId") int mascotaId,
            @Path("id") int id
    );

    // Subir un archivo al historial médico
    @POST("mascotas/{mascotaId}/historial-medico")
    Call<Void> subirHistorialMedico(
            @Path("mascotaId") int mascotaId,
            @Body HistorialMedicoUploader historialMedicoUploader
    );

    @GET("mascotas/{mascotaId}/historial-medico")
    Call<List<HistorialMedicoUploader>> obtenerArchivos(@Path("mascotaId") int mascotaId);

    @Multipart
    @POST("mascotas/{mascotaId}/historial-medico")
    Call<ResponseBody> subirArchivos(
            @Path("mascotaId") int mascotaId,
            @Part List<MultipartBody.Part> archivos,
            @Part("historial") RequestBody historialJson
    );
}
