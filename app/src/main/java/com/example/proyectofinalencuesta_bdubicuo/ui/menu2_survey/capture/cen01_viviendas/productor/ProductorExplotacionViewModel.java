package com.example.proyectofinalencuesta_bdubicuo.ui.menu2_survey.capture.cen01_viviendas.productor;

import android.app.Activity;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectofinalencuesta_bdubicuo.data.CensoRepository;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Cuestionarios;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Segmentos;
import com.example.proyectofinalencuesta_bdubicuo.utils.ProcessNotifier;

import java.util.List;

public class ProductorExplotacionViewModel extends ViewModel {
    private static final String TAG = "CaptureViewModel";
    //    private LiveData<List<Segmentos>> segmentosList;
    private final CensoRepository censoRepository;

    public ProductorExplotacionViewModel() {
        censoRepository = new CensoRepository();
        Log.i(TAG, "CaptureViewModel: Constructor");
    }

    public LiveData<List<Cuestionarios>> getCuestionariosBySegmentoVivienda(String id) {
        return censoRepository.getCuestionariosBySegmentoVivienda(id);
    }

    public void updateErrorHogar(String s, String l) {
        censoRepository.actualizarErrorHogar(s, l);
    }

    public void correctErrorHogar(String s, String l) {
        censoRepository.correctErrorHogar(s, l);
    }

    public void sendCuestionarioCreate(ProcessNotifier processNotifier,Activity activity, Cuestionarios cuestionarioSelected,
                                       Segmentos segmentos) {
        censoRepository.enviarCuestionarioCreate(processNotifier,activity ,cuestionarioSelected, segmentos);
    }

    public void sendCuestionarioUpdate(ProcessNotifier processNotifier, Cuestionarios cuestionarioSelected,
                                       Segmentos segmentos,Activity activity, String llave) {
        censoRepository.enviarCuestionarioUpdate(processNotifier, cuestionarioSelected,activity, segmentos,
                llave);
    }

    public void addCuestionarioDatosDat(Cuestionarios cuestionarioSelected) {
        censoRepository.addCuestionarioDatosDat(cuestionarioSelected);
    }

    public LiveData<List<Cuestionarios>> getCuestionariosBySegmento(String segmentoID) {
        return censoRepository.getCuestionariosBySegmento(segmentoID);
    }

    public void deleteCuestionario(String modo, ProcessNotifier processNotifier, Segmentos segmentos,
                                   Cuestionarios cuestionarioSelected, Activity activity) {
        censoRepository.guardarCuestionarioBackup(cuestionarioSelected);
        censoRepository.eliminarCuestionario(modo, processNotifier, segmentos, cuestionarioSelected,
                activity);

    }

    public void updateEstadoSegmento(String id) {
        censoRepository.updateEstadoSegmento(id);
    }

    public void addExplotacion(List<Cuestionarios> nuevaExplotacion) {
        censoRepository.addExplotacion(nuevaExplotacion);
    }

    public void recuperarCuestionarioSeleccionadoServer(Cuestionarios cuestionarioSelected, ProcessNotifier processNotifier, String dataDirectory) {
        censoRepository.recuperarCuestionarioSeleccionadoServer(cuestionarioSelected, processNotifier, dataDirectory);
    }
}
