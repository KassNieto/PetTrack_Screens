package com.pettrack.pettrack.api;

import com.pettrack.pettrack.models.LoginRequest;
import com.pettrack.pettrack.models.LoginResponse;
import com.pettrack.pettrack.models.Mascota;
import com.pettrack.pettrack.models.User;
import com.pettrack.pettrack.models.historialmedico.HistorialMedicoUploader;
import com.pettrack.pettrack.models.signup.RegisterRequest;
import com.pettrack.pettrack.models.signup.ApiResponse;

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
    @POST("/usuarios/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @POST("/usuarios") // Endpoint para registro
    Call<ApiResponse> registerUser(@Body RegisterRequest registerRequest);

    @GET("usuarios/{id}")
    Call<User> getUserById(@Path("id") int userId); // Cambiado a int

    @GET("usuarios/{id}/mascotas")
    Call<List<Mascota>> getMascotasByUsuarioId(@Path("id") int usuarioId);

    @Multipart
    @POST("usuarios/{usuarioId}/mascotas/con-imagen")
    Call<Mascota> crearMascotaConImagen(
            @Path("usuarioId") int usuarioId,
            @Part("mascota") RequestBody mascota,
            @Part MultipartBody.Part foto
    );

    @GET("usuarios/{userId}/mascotas/{mascotaId}")
    Call<Mascota> getMascotaById(
            @Path("userId") int userId,
            @Path("mascotaId") int mascotaId
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