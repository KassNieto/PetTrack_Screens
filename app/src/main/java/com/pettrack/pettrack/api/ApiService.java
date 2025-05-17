package com.pettrack.pettrack.api;

import com.pettrack.pettrack.models.LoginRequest;
import com.pettrack.pettrack.models.LoginResponse;
import com.pettrack.pettrack.models.Mascota;
import com.pettrack.pettrack.models.User;
import com.pettrack.pettrack.models.signup.RegisterRequest;
import com.pettrack.pettrack.models.signup.ApiResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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

    @GET("mascotas/{id}")
    Call<Mascota> getMascotaById(@Path("id") int mascotaId);
}