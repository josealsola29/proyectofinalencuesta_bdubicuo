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

@Entity(tableName = "ListadoFocalizados",
        foreignKeys = {
                @ForeignKey(onDelete = CASCADE, entity = Segmentos.class,
                        parentColumns = "id",
                        childColumns = "segmentoId")},
        indices = {@Index("segmentoId")})
public class ListadoFocalizados {

    @PrimaryKey
    @SerializedName("id")
    @ColumnInfo(name = "id")
    @Expose
    @NonNull
    private String id;

    @SerializedName("segmentoId")
    @ColumnInfo(name = "segmentoId")
    @Expose
    private String segmentoId;

    @SerializedName("calle")
    @ColumnInfo(name = "calle")
    @Expose
    private String calle;

    @SerializedName("edificio")
    @ColumnInfo(name = "edificio")
    @Expose
    private String edificio;

    @SerializedName("cuarto")
    @ColumnInfo(name = "cuarto")
    @Expose
    private String cuarto;

    @SerializedName("informante_Vivienda")
    @ColumnInfo(name = "informante_Vivienda")
    @Expose
    private String informante_Vivienda;

    @SerializedName("telefono_Informante")
    @ColumnInfo(name = "telefono_Informante")
    @Expose
    private String telefono_Informante;

    @SerializedName("jefe_Vivienda")
    @ColumnInfo(name = "jefe_Vivienda")
    @Expose
    private String jefe_Vivienda;

    @SerializedName("nombre_Productor")
    @ColumnInfo(name = "nombre_Productor")
    @Expose
    private String nombre_Productor;

    @SerializedName("telefono_Productor")
    @ColumnInfo(name = "telefono_Productor")
    @Expose
    private String telefono_Productor;

    @SerializedName("observacion")
    @ColumnInfo(name = "observacion")
    @Expose
    private String observacion;


    @SerializedName("direccion")
    @ColumnInfo(name = "direccion")
    @Expose
    private String direccion;


    @SerializedName("fuente")
    @ColumnInfo(name = "fuente")
    @Expose
    private String fuente;

    public ListadoFocalizados(@NonNull String id, String segmentoId, String calle, String edificio, String cuarto, String informante_Vivienda, String telefono_Informante, String jefe_Vivienda, String nombre_Productor, String telefono_Productor, String observacion, String direccion, String fuente) {
        this.id = id;
        this.segmentoId = segmentoId;
        this.calle = calle;
        this.edificio = edificio;
        this.cuarto = cuarto;
        this.informante_Vivienda = informante_Vivienda;
        this.telefono_Informante = telefono_Informante;
        this.jefe_Vivienda = jefe_Vivienda;
        this.nombre_Productor = nombre_Productor;
        this.telefono_Productor = telefono_Productor;
        this.observacion = observacion;
        this.direccion = direccion;
        this.fuente = fuente;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getSegmentoId() {
        return segmentoId;
    }

    public void setSegmentoId(String segmentoId) {
        this.segmentoId = segmentoId;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getEdificio() {
        return edificio;
    }

    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }

    public String getCuarto() {
        return cuarto;
    }

    public void setCuarto(String cuarto) {
        this.cuarto = cuarto;
    }

    public String getInformante_Vivienda() {
        return informante_Vivienda;
    }

    public void setInformante_Vivienda(String informante_Vivienda) {
        this.informante_Vivienda = informante_Vivienda;
    }

    public String getTelefono_Informante() {
        return telefono_Informante;
    }

    public void setTelefono_Informante(String telefono_Informante) {
        this.telefono_Informante = telefono_Informante;
    }

    public String getJefe_Vivienda() {
        return jefe_Vivienda;
    }

    public void setJefe_Vivienda(String jefe_Vivienda) {
        this.jefe_Vivienda = jefe_Vivienda;
    }

    public String getNombre_Productor() {
        return nombre_Productor;
    }

    public void setNombre_Productor(String nombre_Productor) {
        this.nombre_Productor = nombre_Productor;
    }

    public String getTelefono_Productor() {
        return telefono_Productor;
    }

    public void setTelefono_Productor(String telefono_Productor) {
        this.telefono_Productor = telefono_Productor;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getFuente() {
        return fuente;
    }

    public void setFuente(String fuente) {
        this.fuente = fuente;
    }
}
