package com.example.proyectofinalencuesta_bdubicuo.ui.menu2_survey.capture;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectofinalencuesta_bdubicuo.data.CensoRepository;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Segmentos;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.TotCuestionarios;

import java.util.List;

public class CaptureViewModel extends ViewModel {
    private static final String TAG = "CaptureViewModel";
    private final CensoRepository censoRepository;

    public CaptureViewModel() {
        censoRepository = new CensoRepository();
        Log.i(TAG, "CaptureViewModel: Constructor");
    }

    public LiveData<List<Segmentos>> getAllSubZonas() {
        return censoRepository.getAllSubZonas();
    }

    public LiveData<List<Segmentos>> getSegmentosSelected(String subZonaSelect, int empID) {
        return censoRepository.getSegmentosSelected(subZonaSelect, empID);
    }

    public LiveData<List<TotCuestionarios>> getCuestionarios() {
        return censoRepository.getAllCuestionarios();
    }

    public void actualizarEstados(List<Segmentos> segmentos, String usuario, String fechaUltimoSync) {
        censoRepository.actualizarEstados(segmentos, usuario, fechaUltimoSync);
    }
}
