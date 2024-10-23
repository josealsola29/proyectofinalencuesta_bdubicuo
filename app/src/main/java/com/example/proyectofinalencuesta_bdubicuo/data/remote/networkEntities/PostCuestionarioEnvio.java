package com.example.proyectofinalencuesta_bdubicuo.data.remote.networkEntities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostCuestionarioEnvio {
    @SerializedName("llave")
    @Expose
    private String llaveCuestionario;

    @SerializedName("codigoSegmento")
    @Expose
    private String codigoSegmento;
    @SerializedName("provinciaIdExp")
    @Expose
    private String provinciaIdExp;
    @SerializedName("distritoIdExp")
    @Expose
    private String distritoIdExp;

    @SerializedName("corregimientoIdExp")
    @Expose
    private String corregimientoIdExp;

    @SerializedName("productorAgro")
    @Expose
    private String productor;

    @SerializedName("cuestionarioNum")
    @Expose
    private String explotacionNum;

    @SerializedName("vivienda")
    @Expose
    private String vivienda;

    @SerializedName("notas")
    @Expose
    private String notas;

    @SerializedName("datos")
    @Expose
    private String datos;

    @SerializedName("datosJson")
    @Expose
    private String datosJson;

    @SerializedName("fechaCreacion")
    @Expose
    private String fechaCreacion;//Probar cambiar a dateTime

    @SerializedName("fechaModificacion")
    @Expose
    private String fechaModificacion;

    @SerializedName("estado")
    @Expose
    private Integer estado;

    public PostCuestionarioEnvio(String llaveCuestionario, String codigoSegmento, String provinciaIdExp, String distritoIdExp, String corregimientoIdExp, String productor, String explotacionNum, String vivienda, String notas, String datos, String datosJson, String fechaCreacion, String fechaModificacion, Integer estado) {
        this.llaveCuestionario = llaveCuestionario;
        this.codigoSegmento = codigoSegmento;
        this.provinciaIdExp = provinciaIdExp;
        this.distritoIdExp = distritoIdExp;
        this.corregimientoIdExp = corregimientoIdExp;
        this.productor = productor;
        this.explotacionNum = explotacionNum;
        this.vivienda = vivienda;
        this.notas = notas;
        this.datos = datos;
        this.datosJson = datosJson;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
        this.estado = estado;
    }

    public String getLlaveCuestionario() {
        return llaveCuestionario;
    }

    public void setLlaveCuestionario(String llaveCuestionario) {
        this.llaveCuestionario = llaveCuestionario;
    }

    public String getCodigoSegmento() {
        return codigoSegmento;
    }

    public void setCodigoSegmento(String codigoSegmento) {
        this.codigoSegmento = codigoSegmento;
    }

    public String getProvinciaIdExp() {
        return provinciaIdExp;
    }

    public void setProvinciaIdExp(String provinciaIdExp) {
        this.provinciaIdExp = provinciaIdExp;
    }

    public String getDistritoIdExp() {
        return distritoIdExp;
    }

    public void setDistritoIdExp(String distritoIdExp) {
        this.distritoIdExp = distritoIdExp;
    }

    public String getCorregimientoIdExp() {
        return corregimientoIdExp;
    }

    public void setCorregimientoIdExp(String corregimientoIdExp) {
        this.corregimientoIdExp = corregimientoIdExp;
    }

    public String getProductor() {
        return productor;
    }

    public void setProductor(String productor) {
        this.productor = productor;
    }

    public String getExplotacionNum() {
        return explotacionNum;
    }

    public void setExplotacionNum(String explotacionNum) {
        this.explotacionNum = explotacionNum;
    }

    public String getVivienda() {
        return vivienda;
    }

    public void setVivienda(String vivienda) {
        this.vivienda = vivienda;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public String getDatos() {
        return datos;
    }

    public void setDatos(String datos) {
        this.datos = datos;
    }

    public String getDatosJson() {
        return datosJson;
    }

    public void setDatosJson(String datosJson) {
        this.datosJson = datosJson;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(String fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }
}
