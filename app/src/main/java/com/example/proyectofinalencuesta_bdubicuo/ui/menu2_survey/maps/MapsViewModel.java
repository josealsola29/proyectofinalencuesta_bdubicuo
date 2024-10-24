package com.example.proyectofinalencuesta_bdubicuo.ui.menu2_survey.maps;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectofinalencuesta_bdubicuo.data.UbicuoRepository;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Segmentos;

import java.util.List;

public class MapsViewModel extends ViewModel {
    private static final String TAG = "CaptureViewModel";
    private final UbicuoRepository ubicuoRepository;

    public MapsViewModel() {
        ubicuoRepository = new UbicuoRepository();
        Log.i(TAG, "CaptureViewModel: Constructor");
    }

    public LiveData<List<Segmentos>> getAllSubZonas() {
        return ubicuoRepository.getAllSubZonas();
    }

    public LiveData<List<Segmentos>> getSegmentosSelected(String subZonaSelect, int segmentoID) {
        return ubicuoRepository.getSegmentosSelected(subZonaSelect, segmentoID);
    }
}
