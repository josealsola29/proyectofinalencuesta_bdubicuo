package com.example.proyectofinalencuesta_bdubicuo.data.remote.networkEntities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthResponseRefreshToken {
    @SerializedName("username")
    @Expose
    private String userName;

    @SerializedName("codigo")
    @Expose
    private String codigo;

    @SerializedName("accessToken")
    @Expose
    private String accessToken;

    @SerializedName("refreshToken")
    @Expose
    private String refreshToken;

    @SerializedName("rol")
    @Expose
    private String rol;

    @SerializedName("region")
    @Expose
    private String region;

    public AuthResponseRefreshToken(String userName, String codigo, String accessToken, String refreshToken, String rol, String region) {
        this.userName = userName;
        this.codigo = codigo;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.rol = rol;
        this.region = region;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
