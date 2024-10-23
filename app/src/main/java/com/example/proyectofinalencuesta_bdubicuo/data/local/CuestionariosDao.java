package com.example.proyectofinalencuesta_bdubicuo.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RewriteQueriesToDropUnusedColumns;
import androidx.room.Update;

import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.ControlRecorrido;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.ControlRecorridoBackup;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Cuestionarios;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.CuestionariosBackup;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.LogErrors;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.TotCuestionarios;

import java.util.List;

@Dao
public interface CuestionariosDao {

    @Query("SELECT * FROM cuestionarios where codigoViviendaHogares=:id GROUP BY productor")
    LiveData<List<Cuestionarios>> getCuestionariosBySegmentoVivienda(String id);

    @Query("SELECT * FROM cuestionarios where llaveCuestionario=:llave")
    LiveData<Cuestionarios> getCodigoECensoById(String llave);

    @Query("SELECT * FROM cuestionarios where codigoViviendaHogares=:id " +
            "ORDER BY productor AND explotacionNum")
    LiveData<List<Cuestionarios>> getCuestionariosBySegmento(String id);

    @Query("SELECT * FROM cuestionarios where codigoSegmento=:id " +
            "ORDER BY productor AND explotacionNum")
    LiveData<List<Cuestionarios>> getAllCuestionariosBySegmentoReport(String id);

    @Query("SELECT * FROM controlrecorrido where codigoSegmento=:id " +
            "ORDER BY vivienda") //AND hogar
    LiveData<List<ControlRecorrido>> getRecorridosBySegmento(String id);

    @Query("SELECT * FROM cuestionarios " +
            "where codigoSegmento=:codigoSegmento " +
            "AND flagEnvio = 0 " +
            "AND flagPrimerEnvio= 0 " +
            "AND estado_cuestionario > 0")
//true to 1 and false to 0.
    LiveData<List<Cuestionarios>> getCuestionariosBySegmentoNotSended(String codigoSegmento);


//    @Query("SELECT * FROM cuestionarios where vivienda=:viviendaID")
//    LiveData<List<Cuestionarios>> getCuestionariosByVivienda(String viviendaID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addProductor(Cuestionarios nuevoProductor);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addExplotacion(List<Cuestionarios> nuevaExplotacion);


    @Query("SELECT * FROM cuestionarios where estado_cuestionario > 1")
    LiveData<List<Cuestionarios>> getAllCuestionarios();

    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT COUNT(*) AS totCuestionarios, codigoSegmento " +
            " FROM cuestionarios " +
            " GROUP BY codigoSegmento")
    LiveData<List<TotCuestionarios>> getAllCuestionariosCapture();

    @Query("SELECT * FROM cuestionarios where subzona =:subzonaSelect " +
    /*        "GROUP BY explotacionNum  " +*/
            "ORDER BY llaveCuestionario")
    LiveData<List<Cuestionarios>> getAllCuestionariosByZona(String subzonaSelect);

//    @Query("SELECT * FROM cuestionarios where llaveCuestionario =:llave")
//    LiveData<Cuestionarios> getCuestionarioECenso(String llave);

    @Query("UPDATE cuestionarios set erroresEstructura = erroresEstructura " +
            "|| :errorHogar WHERE llaveCuestionario =:llave ")
    void updateErrorHogar(String errorHogar, String llave);

    @Query("UPDATE cuestionarios " +
            "set erroresEstructura = `replace`(erroresEstructura,:errorHogar,'') " +
            "WHERE llaveCuestionario =:llave")
    void correctErrorHogar(String errorHogar, String llave);

//    @Query("SELECT COUNT(codigoSegmento)AS tot, llave, codigoSegmento,numEmpadronador,  estado, " +
//            "flagEnvio, flagPrimerEnvio FROM cuestionarios GROUP BY codigoSegmento")
//    LiveData<List<Cuestionarios>> getCuestionariosBySegmentoID();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void addCuestionarioDatosDat(Cuestionarios cuestionarioSelected);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateCuestionario(Cuestionarios cuestionarioSelected);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addErrorLogs(LogErrors logErrors);

    @Query("DELETE FROM CUESTIONARIOS")
    void deleteCuestionario();

    @Delete
    void eliminarCuestionarioSeleccionado(Cuestionarios cuestionarioSelected);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void guardarCuestionarioEliminado(CuestionariosBackup cuestionarioSelected);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void actualizarCodigoCenso(Cuestionarios cuestionarioSelected);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void actualizarCuestionario(Cuestionarios cuestionarioSelected);


    @Query("SELECT * FROM logerrors")
    LiveData<List<LogErrors>> getLogErrors();

    @Query("SELECT * FROM controlrecorrido_backup")
    LiveData<List<ControlRecorridoBackup>> getLogsControlRecorrido();

    @Query("SELECT * FROM cuestionarios_backup")
    LiveData<List<CuestionariosBackup>> getLogsCuestionarios();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertCuestionarios(List<Cuestionarios> cuestionarios);

    @Query("SELECT * FROM cuestionarios where codigoViviendaHogares =:llaveRecorrido")
    LiveData<List<Cuestionarios>> getCuestionariosByRecorrido(String llaveRecorrido);
}
