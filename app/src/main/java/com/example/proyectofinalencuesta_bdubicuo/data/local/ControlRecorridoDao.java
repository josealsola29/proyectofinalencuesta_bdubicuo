package com.example.proyectofinalencuesta_bdubicuo.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.ControlRecorrido;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.ControlRecorridoBackup;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Cuestionarios;

import java.util.List;

@Dao
public interface ControlRecorridoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addVivienda(ControlRecorrido nuevaVivienda);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addRecorridoProductor(Cuestionarios nuevaVivienda);

//    @Query("UPDATE ControlRecorrido " +
//            "SET preg_amfy0506=:preg5" +
//            " WHERE llaveRecorrido=:llaveRecorridoR ")
//    void updatePreg5Recorrido(boolean preg5, String llaveRecorridoR);

/*    @Query("UPDATE ControlRecorrido " +
            "SET condicionID=:condicionViv" +
            " WHERE llaveRecorrido=:llaveRecorridoR ")
    void updateCondicionViviendaRecorrido(Integer condicionViv, String llaveRecorridoR);*/

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateCondicionViviendaRecorrido(ControlRecorrido controlRecorrido);

    @Query("UPDATE ControlRecorrido " +
            "SET cantProductoresAgro=:cantProductoresAgro " +
            " WHERE llaveRecorrido=:llaveRecorridoR ")
    void updateDatosHogar(int cantProductoresAgro, String llaveRecorridoR);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateViviendaRecorrido(ControlRecorrido controlRecorrido);


    @Query("UPDATE ControlRecorrido " +
            "SET visivilidadLn=:visibilidad " +
            " WHERE llaveRecorrido=:llaveRecorrido ")
    void actualizarVisibilidadLn(String llaveRecorrido, Boolean visibilidad);


    @Query("UPDATE ControlRecorrido " +
            "SET observaciones=:observacion " +
            " WHERE llaveRecorrido=:llaveRecorrido ")
    void actualizarObservacionRecorrido(String llaveRecorrido, String observacion);

//    @Query("UPDATE ControlRecorrido " +
//            "SET condicionID=:condicionNueva " +
//            " WHERE llaveRecorrido=:llaveRecorrido ")
//    void actualizarCondicionRecorrido(String llaveRecorrido, int condicionNueva);
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void actualizarCondicionRecorrido(ControlRecorrido controlRecorrido);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void controlRecorridoLimpiar(ControlRecorrido controlRecorridoLimpiar);

    @Query("UPDATE ControlRecorrido " +
            "SET pregA=:preg_amfy0708, " +
            " pregB=:preg_afip0910, " +
            " pregC=:preg_pmo1112, " +
            " pregD=:preg_gvcc1314, " +
            " pregE=:preg_cggp1516, " +
            " pregF=:preg_eoco1718, " +
            " pregG=:preg1920Empresa " +
            " WHERE llaveRecorrido=:llaveRecorridoR ")
    void guardarControlBateriasHogar(Boolean preg_amfy0708, Boolean preg_afip0910,
                                     Boolean preg_pmo1112, Boolean preg_gvcc1314,
                                     Boolean preg_cggp1516, Boolean preg_eoco1718,
                                     Boolean preg1920Empresa, String llaveRecorridoR);

    /*
    SQLite does not have a separate Boolean storage class.
    Instead, Boolean values are stored as integers 0 (false) and 1 (true).
     */
    @Query("SELECT * FROM ControlRecorrido " +
            "where codigoSegmento=:id " +
            "AND (pregA=1 " +
            "OR pregB=1 " +
            "OR pregC=1 " +
            "OR pregD=1 " +
            "OR pregE=1 " +
            "OR pregF=1 " +
            "OR pregG=1 ) AND (cantProductoresAgro > 0 AND cantExplotacionesAgro > 0)")
    LiveData<List<ControlRecorrido>> getRecorridosPorSegmentoOP(String id);

    @Query("SELECT * FROM cuestionarios WHERE codigoViviendaHogares IN (:hogarList)")
    LiveData<List<Cuestionarios>> getTotalCuestionariosCompletadosPendientes(List<String> hogarList);

    @Query("SELECT * FROM ControlRecorrido where codigoSegmento=:id ORDER BY vivienda")
    LiveData<List<ControlRecorrido>> getRecorridosPorSegmento(String id);

//    @Query("SELECT * FROM ControlRecorrido where codigoSegmento=:id " +
//            "GROUP BY vivienda")
//    LiveData<List<ControlRecorrido>> getRecorridosPorSegmento(String id);

    @Query("SELECT * FROM ControlRecorrido where codigoSegmento=:id " +
            "AND (pregA=1 " +
            "OR pregB=1 " +
            "OR pregC=1 " +
            "OR pregD=1 " +
            "OR pregE=1 " +
            "OR pregF=1 " +
            "OR pregG=1 ) " +
            "ORDER BY llaveRecorrido")
    LiveData<List<ControlRecorrido>> getViviendasBySegmento(String id);

/*    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTotalHogaresRecorrido(ControlRecorrido controlRecorrido);*/

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertarControlRecorridoBackupFromServer(List<ControlRecorrido> controlRecorridoList);

    @Query("SELECT * FROM controlrecorrido ")
    LiveData<List<ControlRecorrido>> getAllControlRecorridos();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void actualizarControlRecorridoEnvio(ControlRecorrido controlRecorrido);

    @Query("SELECT * FROM controlrecorrido S " +
            "WHERE flagEnvio  = 0 AND flagPrimerEnvio  = 0 " +
            "AND subzona =:subzona " +
            "GROUP BY codigoSegmento ")
    LiveData<List<ControlRecorrido>> getSegmentosRecorridoNoEnviadosIndiv(String subzona);

    @Query("SELECT * FROM controlrecorrido " +
            "WHERE flagEnvio  = 0 AND flagPrimerEnvio  = 0 " +
            "AND codigoSegmento =:codigoSegmento ")
    LiveData<List<ControlRecorrido>> getSegmentosRecorridoNoEnviados(String codigoSegmento);

    @Query("SELECT * " +
            "FROM controlrecorrido C " +
            "where flagEnvio = 0 AND flagPrimerEnvio = 0 ")
    LiveData<List<ControlRecorrido>> getRecorridosNoEnviados();

    @Query("SELECT * FROM controlrecorrido S " +
            "WHERE flagPrimerEnvio  = 0 " +
            "AND flagEnvio = 0 " +
            " GROUP BY subzona")
    LiveData<List<ControlRecorrido>> getAllSubZonasRecorridoSend();

    @Query("SELECT * FROM controlrecorrido where subzona =:subzonaSelect " +
            "ORDER BY llaveRecorrido")
    LiveData<List<ControlRecorrido>> getAllRecorridosByZona(String subzonaSelect);

    @Query("DELETE FROM controlrecorrido where llaveRecorrido =:llaveRecorrido ")
    void eliminarRecorrido(String llaveRecorrido);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void guardarRecorridoEliminado(ControlRecorridoBackup controlRecorridoList);
}
