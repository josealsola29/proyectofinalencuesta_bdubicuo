package com.example.proyectofinalencuesta_bdubicuo.data.remote.networkEntities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AsignacionAlterna {
    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("rolId")
    @Expose
    public String rol;

    @SerializedName("region")
    @Expose
    public String region;

    @SerializedName("zona")
    @Expose
    public String zona;

    @SerializedName("numeracion")
    @Expose
    public String numeracion;

    @SerializedName("codigo")
    @Expose
    public String codigo;

    @SerializedName("userName")
    @Expose
    public String userName;

    @SerializedName("superiorId")
    @Expose
    public int superiorId;

    @SerializedName("nombre")
    @Expose
    public String nombre;

    @SerializedName("fechaCreacion")
    @Expose
    public String fechaCreacion;

    @SerializedName("fechaUltimaConexion")
    @Expose
    public String fechaUltimaConexion;

    public AsignacionAlterna(int id, String rol, String region, String zona, String numeracion, String codigo, String userName, int superiorId, String nombre, String fechaCreacion, String fechaUltimaConexion) {
        this.id = id;
        this.rol = rol;
        this.region = region;
        this.zona = zona;
        this.numeracion = numeracion;
        this.codigo = codigo;
        this.userName = userName;
        this.superiorId = superiorId;
        this.nombre = nombre;
        this.fechaCreacion = fechaCreacion;
        this.fechaUltimaConexion = fechaUltimaConexion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getNumeracion() {
        return numeracion;
    }

    public void setNumeracion(String numeracion) {
        this.numeracion = numeracion;
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

    public int getSuperiorId() {
        return superiorId;
    }

    public void setSuperiorId(int superiorId) {
        this.superiorId = superiorId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getFechaUltimaConexion() {
        return fechaUltimaConexion;
    }

    public void setFechaUltimaConexion(String fechaUltimaConexion) {
        this.fechaUltimaConexion = fechaUltimaConexion;
    }
}
