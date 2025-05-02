package com.pettrack.pettrack.api;

import com.pettrack.pettrack.models.LoginRequest;
import com.pettrack.pettrack.models.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("usuarios/login") // Ajusta este endpoint según tu API
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);
}