package com.example.proyectofinalencuesta_bdubicuo.ui.menu2_survey.report;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectofinalencuesta_bdubicuo.data.UbicuoRepository;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.ControlRecorrido;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Cuestionarios;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Segmentos;

import java.util.List;

public class ReportViewModel extends ViewModel {
    private final UbicuoRepository ubicuoRepository;
    private static final String TAG = "CaptureViewModel";

    public ReportViewModel() {
        ubicuoRepository = new UbicuoRepository();
        Log.i(TAG, "CaptureViewModel: Constructor");
    }

    public LiveData<List<Segmentos>> getAllSubZonas() {
        return ubicuoRepository.getAllSubZonas();
    }

 /*   public LiveData<List<Segmentos>> getSegmentosSelected(String subZonaSelect) {
        return ubicuoRepository.getSegmentosSelected(subZonaSelect);
    }*/

    public LiveData<List<Segmentos>> getSegmentosSelectedGroup(String subZonaSelect) {
        return ubicuoRepository.getSegmentosSelectedGroup(subZonaSelect);
    }

    public LiveData<List<Cuestionarios>> getAllCuestionariosBySegmentoReport(String subZonaSelect) {
        return ubicuoRepository.getAllCuestionariosBySegmentoReport(subZonaSelect);
    }

    public LiveData<List<ControlRecorrido>> getRecorridosBySegmentos(String subZonaSelect) {
        return ubicuoRepository.getRecorridosBySegmento(subZonaSelect);
    }

/*    public LiveData<List<Cuestionarios>> getCuestionariosTODOS() {
        return ubicuoRepository.getAllCuestionarios();
    }*/

    public LiveData<List<Cuestionarios>> getAllCuestionariosByZona(String subzona) {
        return ubicuoRepository.getAllCuestionariosByZona(subzona);
    }
    public LiveData<List<ControlRecorrido>> getAllRecorridosByZona(String subzona) {
        return ubicuoRepository.getAllRecorridosByZona(subzona);
    }
}
