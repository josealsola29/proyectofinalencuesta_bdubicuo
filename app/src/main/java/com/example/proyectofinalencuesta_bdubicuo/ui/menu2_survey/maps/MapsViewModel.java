package com.example.proyectofinalencuesta_bdubicuo.ui.menu2_survey.maps;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectofinalencuesta_bdubicuo.data.CensoRepository;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Segmentos;

import java.util.List;

public class MapsViewModel extends ViewModel {
    private static final String TAG = "CaptureViewModel";
    private final CensoRepository censoRepository;

    public MapsViewModel() {
        censoRepository = new CensoRepository();
        Log.i(TAG, "CaptureViewModel: Constructor");
    }

    public LiveData<List<Segmentos>> getAllSubZonas() {
        return censoRepository.getAllSubZonas();
    }

    public LiveData<List<Segmentos>> getSegmentosSelected(String subZonaSelect, int segmentoID) {
        return censoRepository.getSegmentosSelected(subZonaSelect, segmentoID);
    }
}
