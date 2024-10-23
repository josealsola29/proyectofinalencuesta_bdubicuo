package com.example.proyectofinalencuesta_bdubicuo.ui.menu2_survey.capture.cen01_viviendas.viviendas;

import android.app.Activity;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectofinalencuesta_bdubicuo.data.CensoRepository;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.ControlRecorrido;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Cuestionarios;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Segmentos;
import com.example.proyectofinalencuesta_bdubicuo.utils.ProcessNotifier;

import java.util.List;

public class ViviendaHogarViewModel extends ViewModel {
    private static final String TAG = "CaptureViewModel";
    //    private LiveData<List<Segmentos>> segmentosList;
    private final CensoRepository censoRepository;

    public ViviendaHogarViewModel() {
        censoRepository = new CensoRepository();
        Log.i(TAG, "CaptureViewModel: Constructor");
    }


    public LiveData<List<ControlRecorrido>> getViviendasBySegmento(String segmentoID) {
        return censoRepository.getViviendasBySegmento(segmentoID);
    }

/*    public LiveData<List<Cuestionarios>> getCuestionariosBySegmento2(String segmentoID) {
        return censoRepository.getCuestionariosBySegmento(segmentoID);
    }*/

    public LiveData<List<ControlRecorrido>> getRecorridosPorSegmentoOP(String id) {
        return censoRepository.getRecorridosPorSegmentoOP(id);
    }

/*    public void addVivienda(ControlRecorrido nuevaVivienda) {
        censoRepository.addVivienda(nuevaVivienda);
    }*/


    public void deleteVivienda(String modo, ProcessNotifier processNotifier, Segmentos segmentos,
                               ControlRecorrido cuestionarioSelected, Activity activity) {
   /*     censoRepository.eliminarCuestionario(modo, processNotifier, segmentos, cuestionarioSelected,
                activity);*/
    }

    public void updateEstadoSegmento(String id) {
        censoRepository.updateEstadoSegmento(id);
    }

    public LiveData<List<Cuestionarios>> getTotalCuestionariosCompletadosPendientes(List<String> hogarList) {
        return censoRepository.getTotalCuestionariosCompletadosPendientes(hogarList);

    }
}
