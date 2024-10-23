package com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities;



import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "controlrecorrido_backup",
        indices = {@Index("codigoSegmento")})
public class ControlRecorridoBackup {

    @PrimaryKey
    @SerializedName("Llave")
    @ColumnInfo(name = "llaveRecorrido")
    @NonNull
    private String llaveRecorrido;

    @SerializedName("codigoSegmento")
    @ColumnInfo(name = "codigoSegmento")
    private String codigoSegmento;

    @ColumnInfo(name = "subzona")
    private String subzona;

    @SerializedName("provinciaID")
    @ColumnInfo(name = "provinciaID")
    private String provinciaID;

    @SerializedName("distritoID")
    @ColumnInfo(name = "distritoID")
    private String distritoID;

    @SerializedName("corregimientoID")
    @ColumnInfo(name = "corregimientoID")
    private String corregimientoID;

    @SerializedName("segmento")
    @ColumnInfo(name = "segmentoId")
    private String segmentoID;

    @SerializedName("division")
    @ColumnInfo(name = "divisionId")
    private String divisionID;

    @SerializedName("Empadronador")
    @ColumnInfo(name = "codigoEmpadronador")
    private String codigoEmpadronador;

    @SerializedName("numEmpadronador")
    @ColumnInfo(name = "numEmpadronador")
    private String numEmpadronador;

    @SerializedName("vivienda")
    @ColumnInfo(name = "vivienda")
    private String vivienda;

/*    @SerializedName("hogar")
    @ColumnInfo(name = "hogar")
    private String hogar;*/

    @SerializedName("observaciones")
    @ColumnInfo(name = "observaciones")
    private String observaciones;

    @SerializedName("condicionID")
    @ColumnInfo(name = "condicionID")
    private Integer condicionID;
//    @SerializedName("UnSoloPresupuesto")
//    @ColumnInfo(name = "preg_amfy0506")
//    private Boolean preg_amfy0506_unSoloPresupuesto;

    @SerializedName("Mayo2022Sembro")
    @ColumnInfo(name = "preg_amfy0708_mayo2022Sembro")
    private Boolean preg_amfy0708_mayo2022Sembro;

    @SerializedName("TienePlantasFrutales")
    @ColumnInfo(name = "preg_afip0910_tienePlantasFrutales")
    private Boolean preg_afip0910_tienePlantasFrutales;

    @SerializedName("TienePlantasMedicinales")
    @ColumnInfo(name = "preg_pmo1112_tienePlantasMedicinales")
    private Boolean preg_pmo1112_tienePlantasMedicinales;

    @SerializedName("TieneGanado")
    @ColumnInfo(name = "preg_gvcc1314_TieneGanado")
    private Boolean preg_gvcc1314_TieneGanado;

    @SerializedName("TieneCultivosEspeciales")
    @ColumnInfo(name = "preg_cggp1516_tieneCultivosEspeciales")
    private Boolean preg_cggp1516_tieneCultivosEspeciales;

    @SerializedName("PracticaPesca")
    @ColumnInfo(name = "preg_eoco1718_practicaPesca")
    private Boolean preg_eoco1718_practicaPesca;

    @SerializedName("ResponsableBrindar")
    @ColumnInfo(name = "preg1920Empresa_responsableBrindar")
    private Boolean preg1920Empresa_responsableBrindar;

    @SerializedName("CantProductoresAgro")
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

    public ControlRecorridoBackup(@NonNull String llaveRecorrido, String codigoSegmento, String subzona, String provinciaID, String distritoID, String corregimientoID, String segmentoID, String divisionID, String codigoEmpadronador, String numEmpadronador, String vivienda, /*String hogar,*/ String observaciones, Integer condicionID/*, Boolean preg_amfy0506_unSoloPresupuesto*/, Boolean preg_amfy0708_mayo2022Sembro, Boolean preg_afip0910_tienePlantasFrutales, Boolean preg_pmo1112_tienePlantasMedicinales, Boolean preg_gvcc1314_TieneGanado, Boolean preg_cggp1516_tieneCultivosEspeciales, Boolean preg_eoco1718_practicaPesca, Boolean preg1920Empresa_responsableBrindar, Integer cantProductoresAgro, Integer cantExplotacionesAgro, String fechaCreacionCR, String fechaModificacionCR, String fechaRecepcionCR, Boolean visivilidadLn, Boolean habilitado, Boolean flagEnvio, Boolean flagPrimerEnvio) {
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
//        this.preg_amfy0506_unSoloPresupuesto = preg_amfy0506_unSoloPresupuesto;
        this.preg_amfy0708_mayo2022Sembro = preg_amfy0708_mayo2022Sembro;
        this.preg_afip0910_tienePlantasFrutales = preg_afip0910_tienePlantasFrutales;
        this.preg_pmo1112_tienePlantasMedicinales = preg_pmo1112_tienePlantasMedicinales;
        this.preg_gvcc1314_TieneGanado = preg_gvcc1314_TieneGanado;
        this.preg_cggp1516_tieneCultivosEspeciales = preg_cggp1516_tieneCultivosEspeciales;
        this.preg_eoco1718_practicaPesca = preg_eoco1718_practicaPesca;
        this.preg1920Empresa_responsableBrindar = preg1920Empresa_responsableBrindar;
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

/*    public String getHogar() {
        return hogar;
    }

    public void setHogar(String hogar) {
        this.hogar = hogar;
    }*/

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

//    public Boolean getPreg_amfy0506_unSoloPresupuesto() {
//        return preg_amfy0506_unSoloPresupuesto;
//    }
//
//    public void setPreg_amfy0506_unSoloPresupuesto(Boolean preg_amfy0506_unSoloPresupuesto) {
//        this.preg_amfy0506_unSoloPresupuesto = preg_amfy0506_unSoloPresupuesto;
//    }

    public Boolean getPreg_amfy0708_mayo2022Sembro() {
        return preg_amfy0708_mayo2022Sembro;
    }

    public void setPreg_amfy0708_mayo2022Sembro(Boolean preg_amfy0708_mayo2022Sembro) {
        this.preg_amfy0708_mayo2022Sembro = preg_amfy0708_mayo2022Sembro;
    }

    public Boolean getPreg_afip0910_tienePlantasFrutales() {
        return preg_afip0910_tienePlantasFrutales;
    }

    public void setPreg_afip0910_tienePlantasFrutales(Boolean preg_afip0910_tienePlantasFrutales) {
        this.preg_afip0910_tienePlantasFrutales = preg_afip0910_tienePlantasFrutales;
    }

    public Boolean getPreg_pmo1112_tienePlantasMedicinales() {
        return preg_pmo1112_tienePlantasMedicinales;
    }

    public void setPreg_pmo1112_tienePlantasMedicinales(Boolean preg_pmo1112_tienePlantasMedicinales) {
        this.preg_pmo1112_tienePlantasMedicinales = preg_pmo1112_tienePlantasMedicinales;
    }

    public Boolean getPreg_gvcc1314_TieneGanado() {
        return preg_gvcc1314_TieneGanado;
    }

    public void setPreg_gvcc1314_TieneGanado(Boolean preg_gvcc1314_TieneGanado) {
        this.preg_gvcc1314_TieneGanado = preg_gvcc1314_TieneGanado;
    }

    public Boolean getPreg_cggp1516_tieneCultivosEspeciales() {
        return preg_cggp1516_tieneCultivosEspeciales;
    }

    public void setPreg_cggp1516_tieneCultivosEspeciales(Boolean preg_cggp1516_tieneCultivosEspeciales) {
        this.preg_cggp1516_tieneCultivosEspeciales = preg_cggp1516_tieneCultivosEspeciales;
    }

    public Boolean getPreg_eoco1718_practicaPesca() {
        return preg_eoco1718_practicaPesca;
    }

    public void setPreg_eoco1718_practicaPesca(Boolean preg_eoco1718_practicaPesca) {
        this.preg_eoco1718_practicaPesca = preg_eoco1718_practicaPesca;
    }

    public Boolean getPreg1920Empresa_responsableBrindar() {
        return preg1920Empresa_responsableBrindar;
    }

    public void setPreg1920Empresa_responsableBrindar(Boolean preg1920Empresa_responsableBrindar) {
        this.preg1920Empresa_responsableBrindar = preg1920Empresa_responsableBrindar;
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

