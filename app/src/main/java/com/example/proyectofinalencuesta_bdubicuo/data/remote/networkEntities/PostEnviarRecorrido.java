package com.example.proyectofinalencuesta_bdubicuo.data.remote.networkEntities;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostEnviarRecorrido {
    @SerializedName("llave")
    @Expose
    @Nullable
    private String llave;
    @Expose
    @SerializedName("segmento")
    private String segmento;
    @Expose
    @SerializedName("division")
    private String division;
    @Expose
    @SerializedName("vivienda")
    private String vivienda;
    @Expose
    @SerializedName("condicionId")
    private int condicionId;
    @Expose
    @SerializedName("pregA")
    private Boolean pregA;
    @Expose
    @SerializedName("pregB")
    private Boolean pregB;
    @Expose
    @SerializedName("pregC")
    private Boolean pregC;
    @Expose
    @SerializedName("pregD")
    private Boolean pregD;
    @Expose
    @SerializedName("pregE")
    private Boolean pregE;
    @Expose
    @SerializedName("pregF")
    private Boolean pregF;
    @Expose
    @SerializedName("pregG")
    private Boolean pregG;
    @Expose
    @SerializedName("cantProductoresAgro")
    private Integer cantProductoresAgro;
    @Expose
    @SerializedName("cantExplotacionesAgro")
    private Integer cantExplotacionesAgro;
    @Expose
    @SerializedName("cuestionario")
    private Boolean cuestionario;
    @Expose
    @SerializedName("observaciones")
    private String observaciones;
    @Expose
    @SerializedName("fechaCreacion")
    private String fechaCreacion;
    @Expose
    @SerializedName("fechaModificacion")
    private String fechaModificacion;

    public PostEnviarRecorrido(String llave, String segmento, String division, String vivienda, int condicionId, Boolean pregA, Boolean pregB, Boolean pregC, Boolean pregD, Boolean pregE, Boolean pregF, Boolean pregG, Integer cantProductoresAgro, Integer cantExplotacionesAgro, Boolean cuestionario, String observaciones, String fechaCreacion, String fechaModificacion) {
        this.segmento = segmento;
        this.division = division;
        this.llave = llave;
        this.vivienda = vivienda;
        this.condicionId = condicionId;
        this.pregA = pregA;
        this.pregB = pregB;
        this.pregC = pregC;
        this.pregD = pregD;
        this.pregE = pregE;
        this.pregF = pregF;
        this.pregG = pregG;
        this.cantProductoresAgro = cantProductoresAgro;
        this.cantExplotacionesAgro = cantExplotacionesAgro;
        this.cuestionario = cuestionario;
        this.observaciones = observaciones;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
    }

    public String getLlave() {
        return llave;
    }

    public void setLlave(String llave) {
        this.llave = llave;
    }

    public String getSegmento() {
        return segmento;
    }

    public void setSegmento(String segmento) {
        this.segmento = segmento;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getVivienda() {
        return vivienda;
    }

    public void setVivienda(String vivienda) {
        this.vivienda = vivienda;
    }

    public int getCondicionId() {
        return condicionId;
    }

    public void setCondicionId(int condicionId) {
        this.condicionId = condicionId;
    }

    public Boolean getPregA() {
        return pregA;
    }

    public void setPregA(Boolean pregA) {
        this.pregA = pregA;
    }

    public Boolean getPregB() {
        return pregB;
    }

    public void setPregB(Boolean pregB) {
        this.pregB = pregB;
    }

    public Boolean getPregC() {
        return pregC;
    }

    public void setPregC(Boolean pregC) {
        this.pregC = pregC;
    }

    public Boolean getPregD() {
        return pregD;
    }

    public void setPregD(Boolean pregD) {
        this.pregD = pregD;
    }

    public Boolean getPregE() {
        return pregE;
    }

    public void setPregE(Boolean pregE) {
        this.pregE = pregE;
    }

    public Boolean getPregF() {
        return pregF;
    }

    public void setPregF(Boolean pregF) {
        this.pregF = pregF;
    }

    public Boolean getPregG() {
        return pregG;
    }

    public void setPregG(Boolean pregG) {
        this.pregG = pregG;
    }

    public Integer getCantProductoresAgro() {
        return cantProductoresAgro;
    }

    public void setCantProductoresAgro(Integer cantProductoresAgro) {
        this.cantProductoresAgro = cantProductoresAgro;
    }

    public Integer getCantExplotacionesAgro() {
        return cantExplotacionesAgro;
    }

    public void setCantExplotacionesAgro(Integer cantExplotacionesAgro) {
        this.cantExplotacionesAgro = cantExplotacionesAgro;
    }

    public Boolean getCuestionario() {
        return cuestionario;
    }

    public void setCuestionario(Boolean cuestionario) {
        this.cuestionario = cuestionario;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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
}
