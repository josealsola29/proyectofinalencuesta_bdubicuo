package com.example.proyectofinalencuesta_bdubicuo.data.remote.networkEntities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RefreshTokenRequest {
    @SerializedName("RefreshToken")
    @Expose
    private String refreshToken;

    @SerializedName("AccessToken")
    @Expose
    private String accessToken;

    public RefreshTokenRequest(String refreshToken, String accessToken) {
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
