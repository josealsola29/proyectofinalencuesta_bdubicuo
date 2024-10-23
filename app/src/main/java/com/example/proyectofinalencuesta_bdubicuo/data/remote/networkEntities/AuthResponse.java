package com.example.proyectofinalencuesta_bdubicuo.data.remote.networkEntities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthResponse {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("codigo")
    @Expose
    private String codigo;

    @SerializedName("username")
    @Expose
    private String userName;

    @SerializedName("accessToken")
    @Expose
    private String accessToken;

    @SerializedName("refreshToken")
    @Expose
    private String refreshToken;

    @SerializedName("region")
    @Expose
    private String region;

    @SerializedName("refreshTokenExpireAt")
    @Expose
    private String refreshTokenExpireAt;

    @SerializedName("rol")
    @Expose
    private String rol;

    @SerializedName("expireAt")
    @Expose
    private String expireAt;

    public AuthResponse(int id, String codigo, String userName, String accessToken, String refreshToken, String region, String refreshTokenExpireAt, String rol, String expireAt) {
        this.id = id;
        this.codigo = codigo;
        this.userName = userName;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.region = region;
        this.refreshTokenExpireAt = refreshTokenExpireAt;
        this.rol = rol;
        this.expireAt = expireAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRefreshTokenExpireAt() {
        return refreshTokenExpireAt;
    }

    public void setRefreshTokenExpireAt(String refreshTokenExpireAt) {
        this.refreshTokenExpireAt = refreshTokenExpireAt;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(String expireAt) {
        this.expireAt = expireAt;
    }
}
