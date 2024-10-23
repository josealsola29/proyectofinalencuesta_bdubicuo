package com.example.proyectofinalencuesta_bdubicuo.ui.menu1_home;

import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectofinalencuesta_bdubicuo.data.CensoRepository;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.ControlRecorrido;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Cuestionarios;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Segmentos;
import com.example.proyectofinalencuesta_bdubicuo.utils.ProcessNotifier;
import com.example.proyectofinalencuesta_bdubicuo.utils.Resource;

import java.util.List;

public class HomeViewModel extends ViewModel {
    private static final String TAG = "CaptureViewModel";
    private final CensoRepository censoRepository;
    private LiveData<String> estado;

    public HomeViewModel() {
        censoRepository = new CensoRepository();
        Log.i(TAG, "CaptureViewModel: Constructor");
    }

    public LiveData<Resource<List<Segmentos>>> getSegmentosNuevos(List<Segmentos> segmentosList) {
        return censoRepository.getSegmentosNuevos(segmentosList);
    }

    public void getMapsByRegionZonaSubzona(FragmentActivity fragmentActivity,
                                           ProcessNotifier processNotifier, String region, String zona,
                                           String subzona) {
        censoRepository.getMapsByRegionZonaSubzona(fragmentActivity, processNotifier, region, zona, subzona);
    }

    public LiveData<String> getEstado() {
        return estado;
    }

    public void setEstado(LiveData<String> estado) {
        this.estado = estado;
    }

    public LiveData<List<Segmentos>> getSegmentosMaps() {
        return censoRepository.getSegmentosMaps();
    }

    public LiveData<List<Segmentos>> getAllSegmentos() {
        return censoRepository.getAllSegmentos();
    }

    public LiveData<Resource<List<Segmentos>>> getSegmentosBackup() {
        return censoRepository.getSegmentosBackup();
    }

    public LiveData<Resource<List<Segmentos>>> getSegmentosDetalleActualizado() {
        return censoRepository.getSegmentosDetalleActualizado();
    }

    public LiveData<Resource<List<Cuestionarios>>> getCuestionariosBackup() {
        return censoRepository.getCuestionariosBackup();
    }

    public LiveData<Resource<List<ControlRecorrido>>> getControlRecorridoBackup() {
        return censoRepository.getControlRecorridoBackup();
    }
    public void setErrorsLog(String id, String date, String llave) {
        censoRepository.guardarError(id, date, llave);
    }

    public void getInconsistencias(FragmentActivity fragmentActivity, ProcessNotifier processNotifier, String usuario) {
        censoRepository.getInconsistencias(fragmentActivity, processNotifier, usuario);
    }
}
