package com.example.proyectofinalencuesta_bdubicuo.data.remote.networkServices;

import com.example.proyectofinalencuesta_bdubicuo.data.remote.networkEntities.AuthResponse;
import com.example.proyectofinalencuesta_bdubicuo.data.remote.networkEntities.PostLogin;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CensoAuthService {
    @POST("auth")
    Call<AuthResponse> doLogin(@Body PostLogin postLogin);
}
