package com.example.proyectofinalencuesta_bdubicuo.data.repo;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.proyectofinalencuesta_bdubicuo.data.local.ControlRecorridoDao;
import com.example.proyectofinalencuesta_bdubicuo.data.local.CuestionariosDao;
import com.example.proyectofinalencuesta_bdubicuo.data.local.SegmentosDao;
import com.example.proyectofinalencuesta_bdubicuo.data.local.constants.TypeConverterCens;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.ControlRecorrido;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.ControlRecorridoBackup;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Cuestionarios;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.CuestionariosBackup;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.ListadoFocalizados;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.LogErrors;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Segmentos;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Usuarios;

@Database(entities = {
        Usuarios.class,
        Segmentos.class,
        ControlRecorrido.class,
        ControlRecorridoBackup.class,
        ListadoFocalizados.class,
        Cuestionarios.class,
        CuestionariosBackup.class,
        LogErrors.class,},
        version = 1/*,
       autoMigrations = {
                @AutoMigration(from = 0, to = 1)
        }*/)
@TypeConverters({TypeConverterCens.class})
public abstract class CensoDataBase extends RoomDatabase {
    private static volatile CensoDataBase INSTANCE;

    public static CensoDataBase getDataBase(final Context context) {
        if (INSTANCE == null) {

            synchronized (CensoDataBase.class) {
                if (INSTANCE == null)
                    INSTANCE = Room.databaseBuilder(
                                    context,
                                    CensoDataBase.class,
                                    "ubicuo_db")
                            .build();
            }
        }
        return INSTANCE;
    }

    public abstract SegmentosDao getSegmentosDAO();
    public abstract ControlRecorridoDao getViviviendasDAO();

    public abstract CuestionariosDao getCuestionariosDAO();
}
