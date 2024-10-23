package com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "cuestionarios_backup",
        indices = {@Index("codigoViviendaHogares")})
public class CuestionariosBackup {
    @SerializedName("llave")
    @ColumnInfo(name = "llaveCuestionario")
    @PrimaryKey
    @NonNull
    @Expose
    private String llaveCuestionario;

    @SerializedName("codigoViviendaHogares")
    @ColumnInfo(name = "codigoViviendaHogares")
    @Expose
    private String codigoViviendaHogares;

    @SerializedName("codigoSegmento")
    @ColumnInfo(name = "codigoSegmento")
    @Expose
    private String codigoSegmento;

    @SerializedName("provinciaId")
    @ColumnInfo(name = "provinciaID")
    @Expose
    private String provinciaID;

    @SerializedName("distritoId")
    @ColumnInfo(name = "distritoID")
    @Expose
    private String distritoID;

    @SerializedName("corregimientoId")
    @ColumnInfo(name = "corregimientoID")
    @Expose
    private String corregimientoID;

    @SerializedName("provinciaIdExp")
    @ColumnInfo(name = "provinciaIdExp")
    @Expose
    private String provinciaIdExp;

    @SerializedName("provinciaIdExpDesc")
    @ColumnInfo(name = "provinciaIdExpDesc")
    @Expose
    private String provinciaIdExpDesc;

    @SerializedName("distritoIdExp")
    @ColumnInfo(name = "distritoIdExp")
    @Expose
    private String distritoIdExp;

    @SerializedName("distritoIdExpDesc")
    @ColumnInfo(name = "distritoIdExpDesc")
    @Expose
    private String distritoIdExpDesc;

    @SerializedName("corregimientoIdExp")
    @ColumnInfo(name = "corregimientoIdExp")
    @Expose
    private String corregimientoIdExp;

    @SerializedName("corregimientoIdExpDesc")
    @ColumnInfo(name = "corregimientoIdExpDesc")
    @Expose
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
    @Expose
    private String subzona;//Se añadió a la tabla SQL

    @SerializedName("segmento")
    @ColumnInfo(name = "segmento")
    @Expose
    private String segmento;

    @SerializedName("division")
    @ColumnInfo(name = "division")
    @Expose
    private String division;

    @SerializedName("empadronador")
    @ColumnInfo(name = "empadronador")
    @Expose
    private String empadronador;

    @SerializedName("numEmpadronador")
    @ColumnInfo(name = "numEmpadronador")
    @Expose
    private String numEmpadronador;

    @SerializedName("jefe")
    @ColumnInfo(name = "jefe")
    @Expose
    private String jefe;

    @SerializedName("vivienda")
    @ColumnInfo(name = "vivienda")
    @Expose
    private String vivienda;

/*    @SerializedName("hogar")
    @ColumnInfo(name = "hogar")
    @Expose
    private String hogar;*/

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

    @SerializedName("erroresEstructura")
    @ColumnInfo(name = "erroresEstructura")
    @Expose
    private String erroresEstructura;

    @SerializedName("estado")
    @ColumnInfo(name = "estado_cuestionario")
    @Expose
    private int estado;

    @SerializedName("fechaCreacion")
    @ColumnInfo(name = "fechaCreacion")
    @Expose
    private String fechaCreacion;//Probar cambiar a dateTime

    @SerializedName("fechaModificacion")
    @ColumnInfo(name = "fechaModificacion")
    @Expose
    private String fechaModificacion;

    @SerializedName("fechaRecepcion")
    @ColumnInfo(name = "fechaRecepcion")
    @Expose
    private String fechaRecepcion;

    @SerializedName("fechaEntrada")
    @ColumnInfo(name = "fechaEntrada")
    @Expose
    private String fechaEntrada;

    @ColumnInfo(name = "flagEnvio")
    @Expose
    private boolean flagEnvio;

    // @SerializedName("flagPrimerEnvio")
    @ColumnInfo(name = "flagPrimerEnvio")
    @Expose
    private boolean flagPrimerEnvio;

    @ColumnInfo(name = "cantExplotaciones")
    @Expose
    private Integer cantExplotaciones;

    public CuestionariosBackup(@NonNull String llaveCuestionario, String codigoViviendaHogares, String codigoSegmento, String provinciaID, String distritoID, String corregimientoID, String provinciaIdExp, String provinciaIdExpDesc, String distritoIdExp, String distritoIdExpDesc, String corregimientoIdExp, String corregimientoIdExpDesc, String productor, String explotacionNum, String subzona, String segmento, String division, String empadronador, String numEmpadronador, String jefe, String vivienda, /*String hogar, */String notas, String datos, String datosJson, String erroresEstructura, int estado, String fechaCreacion, String fechaModificacion, String fechaRecepcion, String fechaEntrada, boolean flagEnvio, boolean flagPrimerEnvio, Integer cantExplotaciones) {
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
    }

    public Integer getCantExplotaciones() {
        return cantExplotaciones;
    }

    public void setCantExplotaciones(Integer cantExplotaciones) {
        this.cantExplotaciones = cantExplotaciones;
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

/*    public String getHogar() {
        return hogar;
    }

    public void setHogar(String hogar) {
        this.hogar = hogar;
    }*/

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
}
