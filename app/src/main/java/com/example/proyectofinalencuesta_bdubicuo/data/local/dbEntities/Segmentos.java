package com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.proyectofinalencuesta_bdubicuo.data.local.constants.TypeConverterCens;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "segmentos")
public class Segmentos {
    @PrimaryKey
    @SerializedName("id")
    @ColumnInfo(name = "id")
    @Expose
    @NonNull
    private String id;

    @SerializedName("regionId")
    @ColumnInfo(name = "regionId")
    @Expose
    private String regionID;

    @SerializedName("zonaId")
    @ColumnInfo(name = "zonaId")
    @Expose
    private String zonaID;

    @SerializedName("subZonaId")
    @ColumnInfo(name = "subZonaId")
    @Expose
    private String subZonaID;

    @SerializedName("provinciaId")
    @ColumnInfo(name = "provinciaId")
    @Expose
    private String provinciaID;

    @SerializedName("distritoId")
    @ColumnInfo(name = "distritoId")
    @Expose
    private String distritoID;

    @SerializedName("corregimientoId")
    @ColumnInfo(name = "corregimientoId")
    @Expose
    private String corregimientoID;

    @SerializedName("lugarPobladoId")
    @ColumnInfo(name = "lugarPobladoId")
    @Expose
    private String lugarPobladoID;

    @SerializedName("lugarPobladoDescripcion")
    @ColumnInfo(name = "lugarPobladoDescripcion")
    @Expose
    private String lugarPobladoDescripcion;

    @SerializedName("barrioId")
    @ColumnInfo(name = "barrioId")
    @Expose
    private String barrioID;

    @SerializedName("barrioDescripcion")
    @ColumnInfo(name = "barrioDescripcion")
    @Expose
    private String barrioDescripcion;

    @SerializedName("segmentoId")
    @ColumnInfo(name = "segmentoId")
    @Expose
    private String segmentoID;

    @SerializedName("divisionId")
    @ColumnInfo(name = "divisionId")
    @Expose
    private String divisionID;

    @SerializedName("estado")
    @ColumnInfo(name = "estado")
    @Expose
    private String estado;

    @SerializedName("cuestionarios")
    @ColumnInfo(name = "cuestionarios")
    @Expose
    private String cuestionarios;//FK

    @SerializedName("inconsistencias")
    @ColumnInfo(name = "inconsistencias")
    @Expose
    private boolean inconsistencias;// CONFIRMAR

    @SerializedName("empadronadorId")
    @ColumnInfo(name = "empadronadorId")
    @Expose
    private String empadronadorID;

    //cantidadPreliminar

    @SerializedName("fechaDeHabilitación")
    @ColumnInfo(name = "fechaDeHabilitación")
    @Expose
    private String fechaDeHabilitacion;//DATE TIME

    @SerializedName("fechaUltimaCarga")
    @ColumnInfo(name = "fechaUltimaCarga")
    @Expose
    private String fechaUltimaCarga;//DATE TIME

    @SerializedName("fechaDeRevisado")
    @ColumnInfo(name = "fechaDeRevisado")
    @Expose
    private String fechaDeRevisado;//DATE TIME

    @SerializedName("fechaUltimaDescarga")
    @ColumnInfo(name = "fechaUltimaDescarga")
    @Expose
    private String fechaUltimaDescarga;//DATE TIME

    @SerializedName("fechaPrimeraDescarga")
    @ColumnInfo(name = "fechaPrimeraDescarga")
    @Expose
    private String fechaPrimeraDescarga;//DATE TIME

    @SerializedName("asignacionPrincipal")
    @ColumnInfo(name = "asignacionPrincipal")
    @Expose
    private String asignacionPrincipal;

    @SerializedName("asignacionAlterna")
    @ColumnInfo(name = "asignacionAlterna")
    @Expose
    private int asignacionAlterna;//FK

    @ColumnInfo(name = "provNombre")
    @Expose
    private String provNombre;

    @ColumnInfo(name = "distNombre")
    @Expose
    private String distNombre;

    @ColumnInfo(name = "corregNombre")
    @Expose
    private String corregNombre;

    @ColumnInfo(name = "area")
    @Expose
    private int area;

    @ColumnInfo(name = "detalle")
    @Expose
    private String detalle;

    @ColumnInfo(name = "contingencia")
    @Expose
    private boolean contingencia;

    @ColumnInfo(name = "tipoEmp")
    @Expose
    private int tipoEmp;

    @ColumnInfo(name = "viviendas")
    @Expose
    @TypeConverters(TypeConverterCens.class)
    private List<ListadoFocalizados> viviendas;


    public Segmentos(@NonNull String id, String regionID, String zonaID, String subZonaID, String provinciaID, String distritoID, String corregimientoID, String lugarPobladoID, String lugarPobladoDescripcion, String barrioID, String barrioDescripcion, String segmentoID, String divisionID, String estado, String cuestionarios, boolean inconsistencias, String empadronadorID, String fechaDeHabilitacion, String fechaUltimaCarga, String fechaDeRevisado, String fechaUltimaDescarga, String fechaPrimeraDescarga, String asignacionPrincipal, int asignacionAlterna, String provNombre, String distNombre, String corregNombre, int area, String detalle, boolean contingencia, int tipoEmp, List<ListadoFocalizados> viviendas) {
        this.id = id;
        this.regionID = regionID;
        this.zonaID = zonaID;
        this.subZonaID = subZonaID;
        this.provinciaID = provinciaID;
        this.distritoID = distritoID;
        this.corregimientoID = corregimientoID;
        this.lugarPobladoID = lugarPobladoID;
        this.lugarPobladoDescripcion = lugarPobladoDescripcion;
        this.barrioID = barrioID;
        this.barrioDescripcion = barrioDescripcion;
        this.segmentoID = segmentoID;
        this.divisionID = divisionID;
        this.estado = estado;
        this.cuestionarios = cuestionarios;
        this.inconsistencias = inconsistencias;
        this.empadronadorID = empadronadorID;
        this.fechaDeHabilitacion = fechaDeHabilitacion;
        this.fechaUltimaCarga = fechaUltimaCarga;
        this.fechaDeRevisado = fechaDeRevisado;
        this.fechaUltimaDescarga = fechaUltimaDescarga;
        this.fechaPrimeraDescarga = fechaPrimeraDescarga;
        this.asignacionPrincipal = asignacionPrincipal;
        this.asignacionAlterna = asignacionAlterna;
        this.provNombre = provNombre;
        this.distNombre = distNombre;
        this.corregNombre = corregNombre;
        this.area = area;
        this.detalle = detalle;
        this.contingencia = contingencia;
        this.tipoEmp = tipoEmp;
        this.viviendas = viviendas;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getRegionID() {
        return regionID;
    }

    public void setRegionID(String regionID) {
        this.regionID = regionID;
    }

    public String getZonaID() {
        return zonaID;
    }

    public void setZonaID(String zonaID) {
        this.zonaID = zonaID;
    }

    public String getSubZonaID() {
        return subZonaID;
    }

    public void setSubZonaID(String subZonaID) {
        this.subZonaID = subZonaID;
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

    public String getLugarPobladoID() {
        return lugarPobladoID;
    }

    public void setLugarPobladoID(String lugarPobladoID) {
        this.lugarPobladoID = lugarPobladoID;
    }

    public String getLugarPobladoDescripcion() {
        return lugarPobladoDescripcion;
    }

    public void setLugarPobladoDescripcion(String lugarPobladoDescripcion) {
        this.lugarPobladoDescripcion = lugarPobladoDescripcion;
    }

    public String getBarrioID() {
        return barrioID;
    }

    public void setBarrioID(String barrioID) {
        this.barrioID = barrioID;
    }

    public String getBarrioDescripcion() {
        return barrioDescripcion;
    }

    public void setBarrioDescripcion(String barrioDescripcion) {
        this.barrioDescripcion = barrioDescripcion;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCuestionarios() {
        return cuestionarios;
    }

    public void setCuestionarios(String cuestionarios) {
        this.cuestionarios = cuestionarios;
    }

    public boolean isInconsistencias() {
        return inconsistencias;
    }

    public void setInconsistencias(boolean inconsistencias) {
        this.inconsistencias = inconsistencias;
    }

    public String getEmpadronadorID() {
        return empadronadorID;
    }

    public void setEmpadronadorID(String empadronadorID) {
        this.empadronadorID = empadronadorID;
    }

    public String getFechaDeHabilitacion() {
        return fechaDeHabilitacion;
    }

    public void setFechaDeHabilitacion(String fechaDeHabilitacion) {
        this.fechaDeHabilitacion = fechaDeHabilitacion;
    }

    public String getFechaUltimaCarga() {
        return fechaUltimaCarga;
    }

    public void setFechaUltimaCarga(String fechaUltimaCarga) {
        this.fechaUltimaCarga = fechaUltimaCarga;
    }

    public String getFechaDeRevisado() {
        return fechaDeRevisado;
    }

    public void setFechaDeRevisado(String fechaDeRevisado) {
        this.fechaDeRevisado = fechaDeRevisado;
    }

    public String getFechaUltimaDescarga() {
        return fechaUltimaDescarga;
    }

    public void setFechaUltimaDescarga(String fechaUltimaDescarga) {
        this.fechaUltimaDescarga = fechaUltimaDescarga;
    }

    public String getFechaPrimeraDescarga() {
        return fechaPrimeraDescarga;
    }

    public void setFechaPrimeraDescarga(String fechaPrimeraDescarga) {
        this.fechaPrimeraDescarga = fechaPrimeraDescarga;
    }

    public String getAsignacionPrincipal() {
        return asignacionPrincipal;
    }

    public void setAsignacionPrincipal(String asignacionPrincipal) {
        this.asignacionPrincipal = asignacionPrincipal;
    }

    public int getAsignacionAlterna() {
        return asignacionAlterna;
    }

    public void setAsignacionAlterna(int asignacionAlterna) {
        this.asignacionAlterna = asignacionAlterna;
    }

    public String getProvNombre() {
        return provNombre;
    }

    public void setProvNombre(String provNombre) {
        this.provNombre = provNombre;
    }

    public String getDistNombre() {
        return distNombre;
    }

    public void setDistNombre(String distNombre) {
        this.distNombre = distNombre;
    }

    public String getCorregNombre() {
        return corregNombre;
    }

    public void setCorregNombre(String corregNombre) {
        this.corregNombre = corregNombre;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public boolean isContingencia() {
        return contingencia;
    }

    public void setContingencia(boolean contingencia) {
        this.contingencia = contingencia;
    }

    public int getTipoEmp() {
        return tipoEmp;
    }

    public void setTipoEmp(int tipoEmp) {
        this.tipoEmp = tipoEmp;
    }

    public List<ListadoFocalizados> getViviendas() {
        return viviendas;
    }

    public void setViviendas(List<ListadoFocalizados> viviendas) {
        this.viviendas = viviendas;
    }
}
