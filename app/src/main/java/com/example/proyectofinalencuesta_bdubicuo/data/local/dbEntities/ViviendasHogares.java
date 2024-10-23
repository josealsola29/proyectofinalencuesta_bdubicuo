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

@Entity(tableName = "viviendashogares",
        foreignKeys = {
                @ForeignKey(onDelete = CASCADE, entity = Segmentos.class,
                        parentColumns = "id", childColumns = "codigoSegmento")},
        indices = {@Index("codigoSegmento")})
public class ViviendasHogares {
    @PrimaryKey
    @SerializedName("llaveVivienda")
    @ColumnInfo(name = "llaveVivienda")
    @Expose
    @NonNull
    private String llaveVivienda;

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

    @SerializedName("vivienda")
    @ColumnInfo(name = "vivienda")
    @Expose
    private String vivienda;

    @SerializedName("hogar")
    @ColumnInfo(name = "hogar")
    @Expose
    private String hogar;

    @SerializedName("estadoVivienda")
    @ColumnInfo(name = "estadoVivienda")
    @Expose
    private int estadoVivienda;

    public ViviendasHogares(@NonNull String llaveVivienda, String codigoSegmento, String provinciaID,
                            String distritoID, String corregimientoID, String subzona,
                            String segmento, String division, String empadronador, String vivienda, String hogar,
                            int estadoVivienda) {
        this.llaveVivienda = llaveVivienda;
        this.codigoSegmento = codigoSegmento;
        this.provinciaID = provinciaID;
        this.distritoID = distritoID;
        this.corregimientoID = corregimientoID;
        this.subzona = subzona;
        this.segmento = segmento;
        this.division = division;
        this.empadronador = empadronador;
        this.vivienda = vivienda;
        this.hogar = hogar;
        this.estadoVivienda = estadoVivienda;
    }

    @NonNull
    public String getLlaveVivienda() {
        return llaveVivienda;
    }

    public void setLlaveVivienda(@NonNull String llaveVivienda) {
        this.llaveVivienda = llaveVivienda;
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

    public String getVivienda() {
        return vivienda;
    }

    public void setVivienda(String vivienda) {
        this.vivienda = vivienda;
    }

    public String getHogar() {
        return hogar;
    }

    public void setHogar(String hogar) {
        this.hogar = hogar;
    }

    public int getEstadoVivienda() {
        return estadoVivienda;
    }

    public void setEstadoVivienda(int estadoVivienda) {
        this.estadoVivienda = estadoVivienda;
    }
}
