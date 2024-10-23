package com.example.proyectofinalencuesta_bdubicuo.ui.menu2_survey.report;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectofinalencuesta_bdubicuo.data.CensoRepository;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.ControlRecorrido;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Cuestionarios;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Segmentos;

import java.util.List;

public class ReportViewModel extends ViewModel {
    private final CensoRepository censoRepository;
    private static final String TAG = "CaptureViewModel";

    public ReportViewModel() {
        censoRepository = new CensoRepository();
        Log.i(TAG, "CaptureViewModel: Constructor");
    }

    public LiveData<List<Segmentos>> getAllSubZonas() {
        return censoRepository.getAllSubZonas();
    }

 /*   public LiveData<List<Segmentos>> getSegmentosSelected(String subZonaSelect) {
        return censoRepository.getSegmentosSelected(subZonaSelect);
    }*/

    public LiveData<List<Segmentos>> getSegmentosSelectedGroup(String subZonaSelect) {
        return censoRepository.getSegmentosSelectedGroup(subZonaSelect);
    }

    public LiveData<List<Cuestionarios>> getAllCuestionariosBySegmentoReport(String subZonaSelect) {
        return censoRepository.getAllCuestionariosBySegmentoReport(subZonaSelect);
    }

    public LiveData<List<ControlRecorrido>> getRecorridosBySegmentos(String subZonaSelect) {
        return censoRepository.getRecorridosBySegmento(subZonaSelect);
    }

/*    public LiveData<List<Cuestionarios>> getCuestionariosTODOS() {
        return censoRepository.getAllCuestionarios();
    }*/

    public LiveData<List<Cuestionarios>> getAllCuestionariosByZona(String subzona) {
        return censoRepository.getAllCuestionariosByZona(subzona);
    }
    public LiveData<List<ControlRecorrido>> getAllRecorridosByZona(String subzona) {
        return censoRepository.getAllRecorridosByZona(subzona);
    }
}
