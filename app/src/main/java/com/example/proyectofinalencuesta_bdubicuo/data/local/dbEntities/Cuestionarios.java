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

@Entity(tableName = "cuestionarios",
        foreignKeys = {
                @ForeignKey(onDelete = CASCADE, entity = ControlRecorrido.class,
                        parentColumns = "llaveRecorrido", childColumns = "codigoViviendaHogares")},
        indices = {@Index("codigoViviendaHogares")})
public class Cuestionarios {
    @SerializedName("llave")
    @ColumnInfo(name = "llaveCuestionario")
    @PrimaryKey
    @NonNull
    @Expose
    private String llaveCuestionario;

    @SerializedName("llaveRecorrido")
    @ColumnInfo(name = "codigoViviendaHogares")
    private String codigoViviendaHogares;

    @SerializedName("codigoSegmento")
    @ColumnInfo(name = "codigoSegmento")
    @Expose
    private String codigoSegmento;

    @SerializedName("provinciaId")
    @ColumnInfo(name = "provinciaID")
    private String provinciaID;

    @SerializedName("distritoId")
    @ColumnInfo(name = "distritoID")
    private String distritoID;

    @SerializedName("corregimientoId")
    @ColumnInfo(name = "corregimientoID")
    private String corregimientoID;

    @SerializedName("provinciaIdExp")
    @ColumnInfo(name = "provinciaIdExp")
    @Expose
    private String provinciaIdExp;

    @ColumnInfo(name = "provinciaIdExpDesc")
    private String provinciaIdExpDesc;

    @SerializedName("distritoIdExp")
    @ColumnInfo(name = "distritoIdExp")
    @Expose
    private String distritoIdExp;

    @SerializedName("distritoIdExpDesc")
    @ColumnInfo(name = "distritoIdExpDesc")
    private String distritoIdExpDesc;

    @SerializedName("corregimientoIdExp")
    @ColumnInfo(name = "corregimientoIdExp")
    @Expose
    private String corregimientoIdExp;

    @SerializedName("corregimientoIdExpDesc")
    @ColumnInfo(name = "corregimientoIdExpDesc")
    private String corregimientoIdExpDesc;

    @SerializedName("productorAgro")
    @ColumnInfo(name = "productor")
    @Expose
    private String productor;

    @SerializedName("cuestionarioNum")
    @ColumnInfo(name = "explotacionNum")
    @Expose
    private String explotacionNum;

    @SerializedName("subZona")
    @ColumnInfo(name = "subzona")
    private String subzona;//Se añadió a la tabla SQL

    @SerializedName("segmento")
    @ColumnInfo(name = "segmento")
    private String segmento;

    @SerializedName("division")
    @ColumnInfo(name = "division")
    private String division;

    @SerializedName("empadronador")
    @ColumnInfo(name = "empadronador")
    private String empadronador;

    @SerializedName("numEmpadronador")
    @ColumnInfo(name = "numEmpadronador")
    private String numEmpadronador;

    @SerializedName("jefe")
    @ColumnInfo(name = "jefe")
    private String jefe;

    @SerializedName("vivienda")
    @ColumnInfo(name = "vivienda")
    @Expose
    private String vivienda;

    @SerializedName("notas")
    @ColumnInfo(name = "notas")
    @Expose
    private String notas;

    @SerializedName("datos")
    @ColumnInfo(name = "datos")
    @Expose
    private String datos;

    @SerializedName("datosJson")
    @ColumnInfo(name = "datosJson")
    @Expose
    private String datosJson;

    @ColumnInfo(name = "erroresEstructura")
    private String erroresEstructura;

    @ColumnInfo(name = "estado_cuestionario")
    private int estado;

    @SerializedName("fechaCreacion")
    @ColumnInfo(name = "fechaCreacion")
    @Expose
    private String fechaCreacion;//Probar cambiar a dateTime

    @SerializedName("fechaModificacion")
    @ColumnInfo(name = "fechaModificacion")
    @Expose
    private String fechaModificacion;

    @ColumnInfo(name = "fechaRecepcion")
    private String fechaRecepcion;

    @ColumnInfo(name = "fechaEntrada")
    private String fechaEntrada;

    @ColumnInfo(name = "flagEnvio")
    private boolean flagEnvio;

    // @SerializedName("flagPrimerEnvio")
    @ColumnInfo(name = "flagPrimerEnvio")
    private boolean flagPrimerEnvio;

    @ColumnInfo(name = "cantExplotaciones")
    private Integer cantExplotaciones;

    @ColumnInfo(name = "p170maquinaria")
    private Integer p170maquinaria;

    public Cuestionarios(@NonNull String llaveCuestionario, String codigoViviendaHogares, String codigoSegmento, String provinciaID, String distritoID, String corregimientoID, String provinciaIdExp, String provinciaIdExpDesc, String distritoIdExp, String distritoIdExpDesc, String corregimientoIdExp, String corregimientoIdExpDesc, String productor, String explotacionNum, String subzona, String segmento, String division, String empadronador, String numEmpadronador, String jefe, String vivienda, /*String hogar, */String notas, String datos, String datosJson, String erroresEstructura, int estado, String fechaCreacion, String fechaModificacion, String fechaRecepcion, String fechaEntrada, boolean flagEnvio, boolean flagPrimerEnvio, Integer cantExplotaciones, Integer p170maquinaria) {
        this.llaveCuestionario = llaveCuestionario;
        this.codigoViviendaHogares = codigoViviendaHogares;
        this.codigoSegmento = codigoSegmento;
        this.provinciaID = provinciaID;
        this.distritoID = distritoID;
        this.corregimientoID = corregimientoID;
        this.provinciaIdExp = provinciaIdExp;
        this.provinciaIdExpDesc = provinciaIdExpDesc;
        this.distritoIdExp = distritoIdExp;
        this.distritoIdExpDesc = distritoIdExpDesc;
        this.corregimientoIdExp = corregimientoIdExp;
        this.corregimientoIdExpDesc = corregimientoIdExpDesc;
        this.productor = productor;
        this.explotacionNum = explotacionNum;
        this.subzona = subzona;
        this.segmento = segmento;
        this.division = division;
        this.empadronador = empadronador;
        this.numEmpadronador = numEmpadronador;
        this.jefe = jefe;
        this.vivienda = vivienda;
//        this.hogar = hogar;
        this.notas = notas;
        this.datos = datos;
        this.datosJson = datosJson;
        this.erroresEstructura = erroresEstructura;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
        this.fechaRecepcion = fechaRecepcion;
        this.fechaEntrada = fechaEntrada;
        this.flagEnvio = flagEnvio;
        this.flagPrimerEnvio = flagPrimerEnvio;
        this.cantExplotaciones = cantExplotaciones;
        this.p170maquinaria = p170maquinaria;
    }

    @NonNull
    public String getLlaveCuestionario() {
        return llaveCuestionario;
    }

    public void setLlaveCuestionario(@NonNull String llaveCuestionario) {
        this.llaveCuestionario = llaveCuestionario;
    }

    public String getCodigoViviendaHogares() {
        return codigoViviendaHogares;
    }

    public void setCodigoViviendaHogares(String codigoViviendaHogares) {
        this.codigoViviendaHogares = codigoViviendaHogares;
    }

    public String getCodigoSegmento() {
        return codigoSegmento;
    }

    public void setCodigoSegmento(String codigoSegmento) {
        this.codigoSegmento = codigoSegmento;
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

    public String getProvinciaIdExp() {
        return provinciaIdExp;
    }

    public void setProvinciaIdExp(String provinciaIdExp) {
        this.provinciaIdExp = provinciaIdExp;
    }

    public String getProvinciaIdExpDesc() {
        return provinciaIdExpDesc;
    }

    public void setProvinciaIdExpDesc(String provinciaIdExpDesc) {
        this.provinciaIdExpDesc = provinciaIdExpDesc;
    }

    public String getDistritoIdExp() {
        return distritoIdExp;
    }

    public void setDistritoIdExp(String distritoIdExp) {
        this.distritoIdExp = distritoIdExp;
    }

    public String getDistritoIdExpDesc() {
        return distritoIdExpDesc;
    }

    public void setDistritoIdExpDesc(String distritoIdExpDesc) {
        this.distritoIdExpDesc = distritoIdExpDesc;
    }

    public String getCorregimientoIdExp() {
        return corregimientoIdExp;
    }

    public void setCorregimientoIdExp(String corregimientoIdExp) {
        this.corregimientoIdExp = corregimientoIdExp;
    }

    public String getCorregimientoIdExpDesc() {
        return corregimientoIdExpDesc;
    }

    public void setCorregimientoIdExpDesc(String corregimientoIdExpDesc) {
        this.corregimientoIdExpDesc = corregimientoIdExpDesc;
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

    public String getSubzona() {
        return subzona;
    }

    public void setSubzona(String subzona) {
        this.subzona = subzona;
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

    public String getEmpadronador() {
        return empadronador;
    }

    public void setEmpadronador(String empadronador) {
        this.empadronador = empadronador;
    }

    public String getNumEmpadronador() {
        return numEmpadronador;
    }

    public void setNumEmpadronador(String numEmpadronador) {
        this.numEmpadronador = numEmpadronador;
    }

    public String getJefe() {
        return jefe;
    }

    public void setJefe(String jefe) {
        this.jefe = jefe;
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

    public String getErroresEstructura() {
        return erroresEstructura;
    }

    public void setErroresEstructura(String erroresEstructura) {
        this.erroresEstructura = erroresEstructura;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
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

    public String getFechaRecepcion() {
        return fechaRecepcion;
    }

    public void setFechaRecepcion(String fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }

    public String getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(String fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public boolean isFlagEnvio() {
        return flagEnvio;
    }

    public void setFlagEnvio(boolean flagEnvio) {
        this.flagEnvio = flagEnvio;
    }

    public boolean isFlagPrimerEnvio() {
        return flagPrimerEnvio;
    }

    public void setFlagPrimerEnvio(boolean flagPrimerEnvio) {
        this.flagPrimerEnvio = flagPrimerEnvio;
    }

    public Integer getCantExplotaciones() {
        return cantExplotaciones;
    }

    public void setCantExplotaciones(Integer cantExplotaciones) {
        this.cantExplotaciones = cantExplotaciones;
    }

    public Integer getP170maquinaria() {
        return p170maquinaria;
    }

    public void setP170maquinaria(Integer p170maquinaria) {
        this.p170maquinaria = p170maquinaria;
    }
}
