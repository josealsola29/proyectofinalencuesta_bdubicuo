package com.example.proyectofinalencuesta_bdubicuo.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectofinalencuesta_bdubicuo.data.CensoRepository;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.ControlRecorridoBackup;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.CuestionariosBackup;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.LogErrors;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Segmentos;
import com.example.proyectofinalencuesta_bdubicuo.utils.Resource;

import java.util.List;

public class ConfigurationLoginDialogViewModel extends ViewModel {
//    private static final String TAG = "ConfigurationLoginDialo";
    private final CensoRepository censoRepository;


    public ConfigurationLoginDialogViewModel() {
        censoRepository = CensoRepository.getInstance();
    }

    public LiveData<Resource<List<Segmentos>>> getSegmentosFromServer() {
        return censoRepository.getSegmentosFromServer();
    }

    public LiveData<List<LogErrors>> getLogErrors() {
        return censoRepository.getLogErrors();
    }
    public LiveData<List<ControlRecorridoBackup>> getLogsControlRecorrido() {
        return censoRepository.getLogsControlRecorrido();
    }
    public LiveData<List<CuestionariosBackup>> getLogsCuestionarios() {
        return censoRepository.getLogsCuestionarios();
    }

    public LiveData<List<Segmentos>> getAllSegmentosFromBD() {
        return censoRepository.getAllSegmentosFromBD();
    }

    public void actualizarEstadosFromServidor(List<Segmentos> segmentos, String usuario, String fechaUltimoSync) {
        censoRepository.actualizarEstados(segmentos, usuario, fechaUltimoSync);
    }
/*    public void deleteAll() {
        censoRepository.eliminarDataBase();
        Log.i(TAG, "Delete: Se eliminó la base de datos.");
    }*/
}
