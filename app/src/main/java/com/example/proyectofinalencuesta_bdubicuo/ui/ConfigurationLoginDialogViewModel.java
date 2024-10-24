package com.example.proyectofinalencuesta_bdubicuo.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectofinalencuesta_bdubicuo.data.UbicuoRepository;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.ControlRecorridoBackup;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.CuestionariosBackup;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.LogErrors;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Segmentos;
import com.example.proyectofinalencuesta_bdubicuo.utils.Resource;

import java.util.List;

public class ConfigurationLoginDialogViewModel extends ViewModel {
//    private static final String TAG = "ConfigurationLoginDialo";
    private final UbicuoRepository ubicuoRepository;


    public ConfigurationLoginDialogViewModel() {
        ubicuoRepository = UbicuoRepository.getInstance();
    }

    public LiveData<Resource<List<Segmentos>>> getSegmentosFromServer() {
        return ubicuoRepository.getSegmentosFromServer();
    }

    public LiveData<List<LogErrors>> getLogErrors() {
        return ubicuoRepository.getLogErrors();
    }
    public LiveData<List<ControlRecorridoBackup>> getLogsControlRecorrido() {
        return ubicuoRepository.getLogsControlRecorrido();
    }
    public LiveData<List<CuestionariosBackup>> getLogsCuestionarios() {
        return ubicuoRepository.getLogsCuestionarios();
    }

    public LiveData<List<Segmentos>> getAllSegmentosFromBD() {
        return ubicuoRepository.getAllSegmentosFromBD();
    }

    public void actualizarEstadosFromServidor(List<Segmentos> segmentos, String usuario, String fechaUltimoSync) {
        ubicuoRepository.actualizarEstados(segmentos, usuario, fechaUltimoSync);
    }
/*    public void deleteAll() {
        ubicuoRepository.eliminarDataBase();
        Log.i(TAG, "Delete: Se eliminó la base de datos.");
    }*/
}
