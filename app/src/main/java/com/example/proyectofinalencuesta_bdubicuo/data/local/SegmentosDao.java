package com.example.proyectofinalencuesta_bdubicuo.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Cuestionarios;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.CuestionariosPendientes;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.ListadoFocalizados;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Segmentos;

import java.util.List;

@Dao
public interface SegmentosDao {
    //    If the return value is -1 or negative consider the operation is failed according to docs.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertSegmentosAdicionales(List<Segmentos> segmentos);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSegmentos(List<Segmentos> segmentos);

    @Query("UPDATE segmentos " +
            "set detalle = :detalle ," +
            " lugarPobladoId= :lugarPobladoId, " +
            " lugarPobladoDescripcion= :lugarPobladoDescripcion, " +
            " barrioId= :barrioId, " +
            " barrioDescripcion= :barrioDescripcion, " +
            " asignacionAlterna = :idAsignacionAlterna " +
            " WHERE id=:id")
    void updateDetalleSegmento(String detalle, String id, String lugarPobladoId, String lugarPobladoDescripcion,
                               String barrioId, String barrioDescripcion, int idAsignacionAlterna);

    @Delete
    void deleteSegmentos(List<Segmentos> segmentos);

    @Query("DELETE FROM segmentos WHERE estado = 1 ")
    void deleteAllSegmentosEstado1();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void actualizarEstadoSegmento(Segmentos segmentos);

    @Query("UPDATE segmentos set estado = 2 WHERE id=:segmentoID")
    void actualizarEstadoSegmentoRecorrido(String segmentoID);

    @Query("UPDATE segmentos set estado = 1 WHERE id=:id")
    void updateEstadoSegmento(String id);

//    @Update
//    void updateSegmentos(Segmentos segmentos);

    @Query("SELECT * FROM segmentos where estado > 0")
    LiveData<List<Segmentos>> loadSegmentosFaltantesBackup();

    @Query("SELECT * FROM segmentos")
    LiveData<List<Segmentos>> loadSegmentos();

    @Query("SELECT * FROM listadofocalizados")
    LiveData<List<ListadoFocalizados>> loadListadoFocalizados();

    @Query("SELECT * FROM segmentos GROUP BY subZonaId ORDER BY subZonaId ASC")
    LiveData<List<Segmentos>> getAllSubZonas();

    @Query("SELECT * FROM segmentos S " +
            "INNER JOIN cuestionarios C " +
            "ON S.id = C.codigoSegmento " +
            "WHERE C.flagPrimerEnvio  = 0 " +
            "AND C.flagEnvio = 0 AND C.estado_cuestionario > 0 " +
            " GROUP BY S.subZonaId")
    LiveData<List<Segmentos>> getAllSubZonasSend();

    @Query("SELECT * FROM segmentos where subZonaID=:subZonaSelect " +
            "AND (asignacionAlterna=:asignacionAlterna OR asignacionAlterna= 0 )" +
            "ORDER BY id ASC ")
    LiveData<List<Segmentos>> getSegmentosSelected(String subZonaSelect, int asignacionAlterna);

    @Query("SELECT COUNT(*) as totPendientes, codigoSegmento, subzona " +
            "FROM cuestionarios C " +
            "INNER JOIN segmentos S " +
            "ON C.codigoSegmento = S.id " +
            "where S.subZonaId= :subzona AND C.estado_cuestionario > 0 AND flagEnvio = 0 AND flagPrimerEnvio = 0 " +
            "GROUP BY codigoSegmento ")
    LiveData<List<CuestionariosPendientes>> getSegmentosCuestionariosNoEnviados(String subzona);

    @Query("SELECT COUNT(*) as totPendientes, CodigoSegmento, subzona " +
            "FROM controlrecorrido C " +
            "where subzona= :subzona  AND flagEnvio = 0 AND flagPrimerEnvio = 0 " +
            "GROUP BY codigoSegmento ")
    LiveData<List<CuestionariosPendientes>> getSegmentosRecorridosNoEnviados(String subzona);

    @Query("SELECT * " +
            "FROM cuestionarios C " +
            "INNER JOIN segmentos S " +
            "ON C.codigoSegmento = S.id " +
            "where C.estado_cuestionario > 0 AND flagEnvio = 0 AND flagPrimerEnvio = 0 ")
    LiveData<List<Cuestionarios>> getSegmentosCuestionariosNoEnviados3();
    
    @Query("SELECT * FROM segmentos S " +
            "INNER JOIN cuestionarios C " +
            "ON S.id = C.Codigosegmento " +
            "WHERE C.flagEnvio  = 0 AND C.estado_cuestionario > 0 AND C.flagPrimerEnvio  = 0 " +
            "AND S.subZonaId =:subzona " +
            "GROUP BY id ")
    LiveData<List<Segmentos>> getSegmentosCuestionariosNoEnviados2(String subzona);

    @Query("SELECT * FROM segmentos where subZonaID=:subZonaSelect")
    LiveData<List<Segmentos>> getSegmentosSelectedGroup(String subZonaSelect);

    @Query("SELECT * FROM segmentos GROUP BY subZonaId")
    LiveData<List<Segmentos>> getSegmentosMaps();

    @Query("UPDATE segmentos" +
            " SET estado =:estado" +
            " WHERE id =:llaveSegmento")
    void actualizarSegmentos(String estado, String llaveSegmento);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateSegmentosEstado2(List<Segmentos> segmentos);

   /* @Query("SELECT * FROM segmentos")
    List<Segmentos> getAllSegmentosEstados();*/
}
