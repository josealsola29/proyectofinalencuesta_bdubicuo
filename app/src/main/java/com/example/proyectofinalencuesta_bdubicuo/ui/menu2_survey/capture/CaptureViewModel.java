package com.example.proyectofinalencuesta_bdubicuo.ui.menu2_survey.capture;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectofinalencuesta_bdubicuo.data.UbicuoRepository;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Segmentos;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.TotCuestionarios;

import java.util.List;

public class CaptureViewModel extends ViewModel {
    private static final String TAG = "CaptureViewModel";
    private final UbicuoRepository ubicuoRepository;

    public CaptureViewModel() {
        ubicuoRepository = new UbicuoRepository();
        Log.i(TAG, "CaptureViewModel: Constructor");
    }

    public LiveData<List<Segmentos>> getAllSubZonas() {
        return ubicuoRepository.getAllSubZonas();
    }

    public LiveData<List<Segmentos>> getSegmentosSelected(String subZonaSelect, int empID) {
        return ubicuoRepository.getSegmentosSelected(subZonaSelect, empID);
    }

    public LiveData<List<TotCuestionarios>> getCuestionarios() {
        return ubicuoRepository.getAllCuestionarios();
    }

    public void actualizarEstados(List<Segmentos> segmentos, String usuario, String fechaUltimoSync) {
        ubicuoRepository.actualizarEstados(segmentos, usuario, fechaUltimoSync);
    }
}
