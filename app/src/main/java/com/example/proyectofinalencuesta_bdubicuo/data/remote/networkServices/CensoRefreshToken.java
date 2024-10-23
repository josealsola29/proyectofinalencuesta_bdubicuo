package com.example.proyectofinalencuesta_bdubicuo.data.remote.networkServices;

import com.example.proyectofinalencuesta_bdubicuo.data.remote.networkEntities.AuthResponseRefreshToken;
import com.example.proyectofinalencuesta_bdubicuo.data.remote.networkEntities.RefreshTokenRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CensoRefreshToken {

    @POST("auth/refresh")
    Call<AuthResponseRefreshToken> doRefresh(@Body RefreshTokenRequest refreshTokenRequest);
}
