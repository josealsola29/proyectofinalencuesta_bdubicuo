package com.example.proyectofinalencuesta_bdubicuo.ui.menu2_survey.capture.cen01_viviendas.viviendas;

import android.app.Activity;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectofinalencuesta_bdubicuo.data.UbicuoRepository;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.ControlRecorrido;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Cuestionarios;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Segmentos;
import com.example.proyectofinalencuesta_bdubicuo.utils.ProcessNotifier;

import java.util.List;

public class ViviendaHogarViewModel extends ViewModel {
    private static final String TAG = "CaptureViewModel";
    //    private LiveData<List<Segmentos>> segmentosList;
    private final UbicuoRepository ubicuoRepository;

    public ViviendaHogarViewModel() {
        ubicuoRepository = new UbicuoRepository();
        Log.i(TAG, "CaptureViewModel: Constructor");
    }


    public LiveData<List<ControlRecorrido>> getViviendasBySegmento(String segmentoID) {
        return ubicuoRepository.getViviendasBySegmento(segmentoID);
    }

/*    public LiveData<List<Cuestionarios>> getCuestionariosBySegmento2(String segmentoID) {
        return ubicuoRepository.getCuestionariosBySegmento(segmentoID);
    }*/

    public LiveData<List<ControlRecorrido>> getRecorridosPorSegmentoOP(String id) {
        return ubicuoRepository.getRecorridosPorSegmentoOP(id);
    }

/*    public void addVivienda(ControlRecorrido nuevaVivienda) {
        ubicuoRepository.addVivienda(nuevaVivienda);
    }*/


    public void deleteVivienda(String modo, ProcessNotifier processNotifier, Segmentos segmentos,
                               ControlRecorrido cuestionarioSelected, Activity activity) {
   /*     ubicuoRepository.eliminarCuestionario(modo, processNotifier, segmentos, cuestionarioSelected,
                activity);*/
    }

    public void updateEstadoSegmento(String id) {
        ubicuoRepository.updateEstadoSegmento(id);
    }

    public LiveData<List<Cuestionarios>> getTotalCuestionariosCompletadosPendientes(List<String> hogarList) {
        return ubicuoRepository.getTotalCuestionariosCompletadosPendientes(hogarList);

    }
}
