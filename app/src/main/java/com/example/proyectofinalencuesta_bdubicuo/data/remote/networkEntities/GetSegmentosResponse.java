package com.example.proyectofinalencuesta_bdubicuo.data.remote.networkEntities;

import androidx.room.ColumnInfo;
import androidx.room.TypeConverters;

import com.example.proyectofinalencuesta_bdubicuo.data.local.constants.TypeConverterCens;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.ListadoFocalizados;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

//GET: api/Segmentos/Asignados/{UsuarioId}
//[HttpGet("Asignados/{usuarioId}")]

////GET: api/Segmentos/todos/{UsuarioId}
////[HttpGet("todos/{usuarioId}")]

//         //GET: api/Segmentos/Adicionales/{UsuarioId}
//        [HttpGet("Adicionales/{usuarioId}")]
public class GetSegmentosResponse {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("regionId")
    @Expose
    private String regionID;

    @SerializedName("zonaId")
    @Expose
    private String zonaID;

    @SerializedName("subZonaId")
    @Expose
    private String subZonaID;

    @SerializedName("provinciaId")
    @Expose
    private String provinciaID;

    @SerializedName("distritoId")
    @Expose
    private String distritoID;

    @SerializedName("corregimientoId")
    @Expose
    private String corregimientoID;

    @SerializedName("lugarPobladoId")
    @Expose
    private String lugarPobladoID;

    @SerializedName("lugarPobladoDescripcion")
    @Expose
    private String lugarPobladoDescripcion;

    @SerializedName("barrioId")
    @Expose
    private String barrioID;

    @SerializedName("barrioDescripcion")
    @Expose
    private String barrioDescripcion;

    @SerializedName("segmentoId")
    @Expose
    private String segmentoID;

    @SerializedName("divisionId")
    @Expose
    private String divisionID;

    @SerializedName("estado")
    @Expose
    private String estado;

    @SerializedName("cuestionarios")
    @Expose
    private String cuestionarios;//FK

    @SerializedName("inconsistencias")
    @Expose
    private boolean inconsistencias;// CONFIRMAR

    @SerializedName("empadronadorId")
    @Expose
    private String empadronadorID;

    @SerializedName("fechaDeHabilitación")
    @Expose
    private String fechaDeHabilitacion;//DATE TIME

    @SerializedName("fechaModificacion")
    @Expose
    private String fechaModificacion;//DATE TIME

    @SerializedName("fechaUltimaCarga")
    @Expose
    private String fechaUltimaCarga;//DATE TIME

    @SerializedName("fechaDeRevisado")
    @Expose
    private String fechaDeRevisado;//DATE TIME

    @SerializedName("asignacionPrincipal")
    @Expose
    private String asignacionPrincipal;

    @SerializedName("asignacionAlterna")
    @Expose
    private AsignacionAlterna asignacionAlterna;

    @SerializedName("provNombre")
    @Expose
    private String provNombre;

    @SerializedName("distNombre")
    @Expose
    private String distNombre;

    @SerializedName("corregNombre")
    @Expose
    private String corregNombre;

    @SerializedName("area")
    @Expose
    private int area;

    @SerializedName("detalle")
    @Expose
    private String detalle;

    @ColumnInfo(name = "contingencia")
    @Expose
    private Boolean contingencia;

    @ColumnInfo(name = "tipoEmp")
    @Expose
    private int tipoEmp;

    @ColumnInfo(name = "viviendas")
    @Expose
    @TypeConverters(TypeConverterCens.class)
    private List<ListadoFocalizados> viviendas;

    public GetSegmentosResponse(String id, String regionID, String zonaID, String subZonaID, String provinciaID, String distritoID, String corregimientoID, String lugarPobladoID, String lugarPobladoDescripcion, String barrioID, String barrioDescripcion, String segmentoID, String divisionID, String estado, String cuestionarios, boolean inconsistencias, String empadronadorID, String fechaDeHabilitacion, String fechaModificacion, String fechaUltimaCarga, String fechaDeRevisado, String asignacionPrincipal, AsignacionAlterna asignacionAlterna, String provNombre, String distNombre, String corregNombre, int area, String detalle, Boolean contingencia, int tipoEmp, List<ListadoFocalizados> viviendas) {
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
        this.fechaModificacion = fechaModificacion;
        this.fechaUltimaCarga = fechaUltimaCarga;
        this.fechaDeRevisado = fechaDeRevisado;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(String fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
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

    public String getAsignacionPrincipal() {
        return asignacionPrincipal;
    }

    public void setAsignacionPrincipal(String asignacionPrincipal) {
        this.asignacionPrincipal = asignacionPrincipal;
    }

    public AsignacionAlterna getAsignacionAlterna() {
        return asignacionAlterna;
    }

    public void setAsignacionAlterna(AsignacionAlterna asignacionAlterna) {
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

    public Boolean getContingencia() {
        return contingencia;
    }

    public void setContingencia(Boolean contingencia) {
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
