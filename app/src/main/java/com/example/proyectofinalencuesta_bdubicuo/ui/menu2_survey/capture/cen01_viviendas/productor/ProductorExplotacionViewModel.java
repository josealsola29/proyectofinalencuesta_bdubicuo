package com.example.proyectofinalencuesta_bdubicuo.ui.menu2_survey.capture.cen01_viviendas.productor;

import android.app.Activity;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectofinalencuesta_bdubicuo.data.UbicuoRepository;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Cuestionarios;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Segmentos;
import com.example.proyectofinalencuesta_bdubicuo.utils.ProcessNotifier;

import java.util.List;

public class ProductorExplotacionViewModel extends ViewModel {
    private static final String TAG = "CaptureViewModel";
    //    private LiveData<List<Segmentos>> segmentosList;
    private final UbicuoRepository ubicuoRepository;

    public ProductorExplotacionViewModel() {
        ubicuoRepository = new UbicuoRepository();
        Log.i(TAG, "CaptureViewModel: Constructor");
    }

    public LiveData<List<Cuestionarios>> getCuestionariosBySegmentoVivienda(String id) {
        return ubicuoRepository.getCuestionariosBySegmentoVivienda(id);
    }

    public void updateErrorHogar(String s, String l) {
        ubicuoRepository.actualizarErrorHogar(s, l);
    }

    public void correctErrorHogar(String s, String l) {
        ubicuoRepository.correctErrorHogar(s, l);
    }

    public void sendCuestionarioCreate(ProcessNotifier processNotifier,Activity activity, Cuestionarios cuestionarioSelected,
                                       Segmentos segmentos) {
        ubicuoRepository.enviarCuestionarioCreate(processNotifier,activity ,cuestionarioSelected, segmentos);
    }

    public void sendCuestionarioUpdate(ProcessNotifier processNotifier, Cuestionarios cuestionarioSelected,
                                       Segmentos segmentos,Activity activity, String llave) {
        ubicuoRepository.enviarCuestionarioUpdate(processNotifier, cuestionarioSelected,activity, segmentos,
                llave);
    }

    public void addCuestionarioDatosDat(Cuestionarios cuestionarioSelected) {
        ubicuoRepository.addCuestionarioDatosDat(cuestionarioSelected);
    }

    public LiveData<List<Cuestionarios>> getCuestionariosBySegmento(String segmentoID) {
        return ubicuoRepository.getCuestionariosBySegmento(segmentoID);
    }

    public void deleteCuestionario(String modo, ProcessNotifier processNotifier, Segmentos segmentos,
                                   Cuestionarios cuestionarioSelected, Activity activity) {
        ubicuoRepository.guardarCuestionarioBackup(cuestionarioSelected);
        ubicuoRepository.eliminarCuestionario(modo, processNotifier, segmentos, cuestionarioSelected,
                activity);

    }

    public void updateEstadoSegmento(String id) {
        ubicuoRepository.updateEstadoSegmento(id);
    }

    public void addExplotacion(List<Cuestionarios> nuevaExplotacion) {
        ubicuoRepository.addExplotacion(nuevaExplotacion);
    }

    public void recuperarCuestionarioSeleccionadoServer(Cuestionarios cuestionarioSelected, ProcessNotifier processNotifier, String dataDirectory) {
        ubicuoRepository.recuperarCuestionarioSeleccionadoServer(cuestionarioSelected, processNotifier, dataDirectory);
    }
}
