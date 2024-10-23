package com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "controlrecorrido",
        foreignKeys = {
                @ForeignKey(onDelete = CASCADE, entity = Segmentos.class,
                        parentColumns = "id", childColumns = "codigoSegmento")},
        indices = {@Index("codigoSegmento")})
public class ControlRecorrido {

    @PrimaryKey
    @SerializedName("llave")
    @ColumnInfo(name = "llaveRecorrido")
    @NonNull
    private String llaveRecorrido;

    @SerializedName("codigoSegmento")
    @ColumnInfo(name = "codigoSegmento")
    private String codigoSegmento;

    @SerializedName("subZonaId")
    @ColumnInfo(name = "subzona")
    private String subzona;

    @SerializedName("provinciaId")
    @ColumnInfo(name = "provinciaID")
    private String provinciaID;

    @SerializedName("distritoId")
    @ColumnInfo(name = "distritoID")
    private String distritoID;

    @SerializedName("corregimientoId")
    @ColumnInfo(name = "corregimientoID")
    private String corregimientoID;

    @SerializedName("segmento")
    @ColumnInfo(name = "segmentoId")
    private String segmentoID;

    @SerializedName("division")
    @ColumnInfo(name = "divisionId")
    private String divisionID;

    @SerializedName("empadronador")
    @ColumnInfo(name = "codigoEmpadronador")
    private String codigoEmpadronador;

    @SerializedName("numEmpadronador")
    @ColumnInfo(name = "numEmpadronador")
    private String numEmpadronador;

    @SerializedName("vivienda")
    @ColumnInfo(name = "vivienda")
    private String vivienda;

/*
    @SerializedName("hogar")
    @ColumnInfo(name = "hogar")
    private String hogar;
*/

    @SerializedName("observaciones")
    @ColumnInfo(name = "observaciones")
    private String observaciones;

    @SerializedName("condicionId")
    @ColumnInfo(name = "condicionID")
    private Integer condicionID;
//    @SerializedName("unSoloPresupuesto")
//    @ColumnInfo(name = "preg_amfy0506")
//    private Boolean preg_amfy0506_unSoloPresupuesto;

    @SerializedName("pregA")
    @ColumnInfo(name = "pregA")
    private Boolean pregA;

    @SerializedName("pregB")
    @ColumnInfo(name = "pregB")
    private Boolean pregB;

    @SerializedName("pregC")
    @ColumnInfo(name = "pregC")
    private Boolean pregC;

    @SerializedName("pregD")
    @ColumnInfo(name = "pregD")
    private Boolean pregD;

    @SerializedName("pregE")
    @ColumnInfo(name = "pregE")
    private Boolean pregE;

    @SerializedName("pregF")
    @ColumnInfo(name = "pregF")
    private Boolean pregF;

    @SerializedName("pregG")
    @ColumnInfo(name = "pregG")
    private Boolean pregG;

    @SerializedName("cantProductoresAgro")
    @ColumnInfo(name = "cantProductoresAgro")
    private Integer cantProductoresAgro;

    @SerializedName("cantExplotacionesAgro")
    @ColumnInfo(name = "cantExplotacionesAgro")
    private Integer cantExplotacionesAgro;

    @SerializedName("fechaCreacion")
    @ColumnInfo(name = "fechaCreacionCR")
    @Expose
    private String fechaCreacionCR;//Probar cambiar a dateTime

    @SerializedName("fechaModificacion")
    @ColumnInfo(name = "fechaModificacionCR")
    @Expose
    private String fechaModificacionCR;

    @SerializedName("fechaRecepcion")
    @ColumnInfo(name = "fechaRecepcionCR")
    @Expose
    private String fechaRecepcionCR;

    @ColumnInfo(name = "visivilidadLn")
    @Expose
    private Boolean visivilidadLn;
    @ColumnInfo(name = "habilitado")
    @Expose
    private Boolean habilitado;

    @ColumnInfo(name = "flagEnvio")
    @Expose
    private Boolean flagEnvio;

    @ColumnInfo(name = "flagPrimerEnvio")
    @Expose
    private Boolean flagPrimerEnvio;

    public ControlRecorrido(@NonNull String llaveRecorrido, String codigoSegmento, String subzona, String provinciaID, String distritoID, String corregimientoID, String segmentoID, String divisionID, String codigoEmpadronador, String numEmpadronador, String vivienda/*, String hogar*/, String observaciones, Integer condicionID, Boolean pregA, Boolean pregB, Boolean pregC, Boolean pregD, Boolean pregE, Boolean pregF, Boolean pregG, Integer cantProductoresAgro, Integer cantExplotacionesAgro, String fechaCreacionCR, String fechaModificacionCR, String fechaRecepcionCR, Boolean visivilidadLn, Boolean habilitado, Boolean flagEnvio, Boolean flagPrimerEnvio) {
        this.llaveRecorrido = llaveRecorrido;
        this.codigoSegmento = codigoSegmento;
        this.subzona = subzona;
        this.provinciaID = provinciaID;
        this.distritoID = distritoID;
        this.corregimientoID = corregimientoID;
        this.segmentoID = segmentoID;
        this.divisionID = divisionID;
        this.codigoEmpadronador = codigoEmpadronador;
        this.numEmpadronador = numEmpadronador;
        this.vivienda = vivienda;
//        this.hogar = hogar;
        this.observaciones = observaciones;
        this.condicionID = condicionID;
        this.pregA = pregA;
        this.pregB = pregB;
        this.pregC = pregC;
        this.pregD = pregD;
        this.pregE = pregE;
        this.pregF = pregF;
        this.pregG = pregG;
        this.cantProductoresAgro = cantProductoresAgro;
        this.cantExplotacionesAgro = cantExplotacionesAgro;
        this.fechaCreacionCR = fechaCreacionCR;
        this.fechaModificacionCR = fechaModificacionCR;
        this.fechaRecepcionCR = fechaRecepcionCR;
        this.visivilidadLn = visivilidadLn;
        this.habilitado = habilitado;
        this.flagEnvio = flagEnvio;
        this.flagPrimerEnvio = flagPrimerEnvio;
    }

    @NonNull
    public String getLlaveRecorrido() {
        return llaveRecorrido;
    }

    public void setLlaveRecorrido(@NonNull String llaveRecorrido) {
        this.llaveRecorrido = llaveRecorrido;
    }

    public String getCodigoSegmento() {
        return codigoSegmento;
    }

    public void setCodigoSegmento(String codigoSegmento) {
        this.codigoSegmento = codigoSegmento;
    }

    public String getSubzona() {
        return subzona;
    }

    public void setSubzona(String subzona) {
        this.subzona = subzona;
    }

    public String getProvinciaID() {
        return provinciaID;
    }

    public void setProvinciaID(String provinciaID) {
        this.provinciaID = provinciaID;
    }

    public String getDistritoID() {
        return distritoID;
    }

    public void setDistritoID(String distritoID) {
        this.distritoID = distritoID;
    }

    public String getCorregimientoID() {
        return corregimientoID;
    }

    public void setCorregimientoID(String corregimientoID) {
        this.corregimientoID = corregimientoID;
    }

    public String getSegmentoID() {
        return segmentoID;
    }

    public void setSegmentoID(String segmentoID) {
        this.segmentoID = segmentoID;
    }

    public String getDivisionID() {
        return divisionID;
    }

    public void setDivisionID(String divisionID) {
        this.divisionID = divisionID;
    }

    public String getCodigoEmpadronador() {
        return codigoEmpadronador;
    }

    public void setCodigoEmpadronador(String codigoEmpadronador) {
        this.codigoEmpadronador = codigoEmpadronador;
    }

    public String getNumEmpadronador() {
        return numEmpadronador;
    }

    public void setNumEmpadronador(String numEmpadronador) {
        this.numEmpadronador = numEmpadronador;
    }

    public String getVivienda() {
        return vivienda;
    }

    public void setVivienda(String vivienda) {
        this.vivienda = vivienda;
    }

//    public String getHogar() {
//        return hogar;
//    }
//
//    public void setHogar(String hogar) {
//        this.hogar = hogar;
//    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Integer getCondicionID() {
        return condicionID;
    }

    public void setCondicionID(Integer condicionID) {
        this.condicionID = condicionID;
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

    public String getFechaCreacionCR() {
        return fechaCreacionCR;
    }

    public void setFechaCreacionCR(String fechaCreacionCR) {
        this.fechaCreacionCR = fechaCreacionCR;
    }

    public String getFechaModificacionCR() {
        return fechaModificacionCR;
    }

    public void setFechaModificacionCR(String fechaModificacionCR) {
        this.fechaModificacionCR = fechaModificacionCR;
    }

    public String getFechaRecepcionCR() {
        return fechaRecepcionCR;
    }

    public void setFechaRecepcionCR(String fechaRecepcionCR) {
        this.fechaRecepcionCR = fechaRecepcionCR;
    }

    public Boolean getVisivilidadLn() {
        return visivilidadLn;
    }

    public void setVisivilidadLn(Boolean visivilidadLn) {
        this.visivilidadLn = visivilidadLn;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }

    public Boolean isFlagEnvio() {
        return flagEnvio;
    }

    public void setFlagEnvio(Boolean flagEnvio) {
        this.flagEnvio = flagEnvio;
    }

    public Boolean isFlagPrimerEnvio() {
        return flagPrimerEnvio;
    }

    public void setFlagPrimerEnvio(Boolean flagPrimerEnvio) {
        this.flagPrimerEnvio = flagPrimerEnvio;
    }
}
