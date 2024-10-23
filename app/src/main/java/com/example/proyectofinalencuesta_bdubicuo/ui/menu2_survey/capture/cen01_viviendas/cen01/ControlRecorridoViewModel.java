package com.example.proyectofinalencuesta_bdubicuo.ui.menu2_survey.capture.cen01_viviendas.cen01;

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

public class ControlRecorridoViewModel extends ViewModel {

    private static final String TAG = "CaptureViewModel";
    //    private LiveData<List<Segmentos>> segmentosList;
    private final CensoRepository censoRepository;

    public ControlRecorridoViewModel() {
        censoRepository = new CensoRepository();
        Log.i(TAG, "CaptureViewModel: Constructor");
    }

    public LiveData<List<ControlRecorrido>> getRecorridosPorSegmento(String id) {
        return censoRepository.getRecorridosPorSegmento(id);
    }

    public void addRecorridoHogar(ControlRecorrido nuevaVivienda) {
        censoRepository.addRecorrido(nuevaVivienda);
    }

    public void addRecorridoProductor(Cuestionarios cuestionarios) {
        censoRepository.addRecorridoProductor(cuestionarios);
    }

//    public void updatePreg5Recorrido(ControlRecorrido controlRecorrido) {
//        censoRepository.updatePreg5Recorrido(controlRecorrido.getPreg_amfy0506_unSoloPresupuesto(),
//                controlRecorrido.getLlaveRecorrido());
//    }

    public void updateCondicionViviendaRecorrido(ControlRecorrido controlRecorrido) {
        censoRepository.updateCondicionViviendaRecorrido(controlRecorrido/*controlRecorrido.getCondicionID(),
                controlRecorrido.getLlaveRecorrido()*/);
    }

//    public void updateDatosHogar(ControlRecorrido controlRecorrido) {
//        censoRepository.updateDatosHogar(
//                controlRecorrido.getPreg_amfy0708(),
//                controlRecorrido.getPreg_afip0910(),
//                controlRecorrido.getPreg_pmo1112(),
//                controlRecorrido.getPreg_gvcc1314(),
//                controlRecorrido.getPreg_cggp1516(),
//                controlRecorrido.getPreg_eoco1718(),
//                controlRecorrido.getNumProductores(),
//                controlRecorrido.getLlaveRecorrido());
//    }

/*    public void updateDatosHogar(ControlRecorrido controlRecorrido) {
        censoRepository.updateDatosHogar(controlRecorrido.getCantProductoresAgro(),
                controlRecorrido.getLlaveRecorrido());
    }*/

    public void updateViviendaRecorrido(ControlRecorrido controlRecorrido) {
        censoRepository.updateViviendaRecorrido(controlRecorrido);
    }

/*    public void guardarControlBateriasHogar(ControlRecorrido controlRecorridoHogarSelected) {
        censoRepository.guardarControlBateriasHogar(
                controlRecorridoHogarSelected.getPregA(),
                controlRecorridoHogarSelected.getPregB(),
                controlRecorridoHogarSelected.getPregC(),
                controlRecorridoHogarSelected.getPregD(),
                controlRecorridoHogarSelected.getPregE(),
                controlRecorridoHogarSelected.getPregF(),
                controlRecorridoHogarSelected.getPregG(),
                controlRecorridoHogarSelected.getLlaveRecorrido());
    }*/

/*    public void actualizarVisibilidadLn(String llaveRecorrido, Boolean visibilidad) {
        censoRepository.actualizarVisibilidadLn(llaveRecorrido, visibilidad);
    }*/

    public void enviarControlRecorridoServidor(ControlRecorrido controlRecorridoHogarSelected, Activity activity,
                                               ProcessNotifier processNotifier, Segmentos segmentos) {
        censoRepository.enviarControlRecorridoServidor(controlRecorridoHogarSelected,activity, processNotifier, segmentos);
    }

    public void actualizarObservacionRecorrido(String llaveRecorrido, String observacion) {
        censoRepository.actualizarObservacionRecorrido(llaveRecorrido, observacion);
    }

//    public void actualizarCondicionRecorrido(String llaveRecorrido, int condicionNueva) {
//        censoRepository.actualizarCondicionRecorrido(llaveRecorrido, condicionNueva);
//    }
    public void actualizarCondicionRecorrido(ControlRecorrido controlRecorrido) {
        censoRepository.actualizarCondicionRecorrido(controlRecorrido);
    }

    public LiveData<List<Cuestionarios>> getCuestionariosByRecorrido(String llaveRecorrido) {
       return censoRepository.getCuestionariosByRecorrido(llaveRecorrido);
    }

    public void eliminarRecorrido(ControlRecorrido llaveRecorrido, ProcessNotifier processNotifier) {
        censoRepository.eliminarRecorrido(llaveRecorrido,processNotifier);
    }

    public void limpiarRecorrido(ControlRecorrido controlRecorridoLimpiar) {
        censoRepository.limpiarRecorrido(controlRecorridoLimpiar);
    }

    public void actualizarCondicion(ControlRecorrido controlRecorridoList) {
    }
}