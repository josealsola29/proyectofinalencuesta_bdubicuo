package com.example.proyectofinalencuesta_bdubicuo.ui.menu3_senddata.send_cuestionarios;

import android.app.Activity;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectofinalencuesta_bdubicuo.data.CensoRepository;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Cuestionarios;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.CuestionariosPendientes;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Segmentos;
import com.example.proyectofinalencuesta_bdubicuo.utils.ProcessNotifier;

import java.util.List;

public class SendDataViewModel extends ViewModel {
    private final CensoRepository censoRepository;
    private static final String TAG = "SendDataViewModel";

    public SendDataViewModel() {
        censoRepository = new CensoRepository();
        Log.i(TAG, "CaptureViewModel: Constructor");
    }

    public LiveData<List<Cuestionarios>> getCuestionariosBySegmentoNotSended(String codigoSemento) {
        return censoRepository.getCuestionariosBySegmentoNotSended(codigoSemento);
    }

    public LiveData<List<Segmentos>> getAllSubZonas() {
        return censoRepository.getAllSubZonasSend();
    }

//    public LiveData<List<Segmentos>> getSegmentosSelected(String subZonaSelect) {
//        return censoRepository.getSegmentosSelected(subZonaSelect);
//    }

    public LiveData<List<CuestionariosPendientes>> getSegmentosCuestionariosNoEnviados(String subzona) {
        return censoRepository.getSegmentosCuestionariosNoEnviados(subzona);
    }

    public LiveData<List<Cuestionarios>> getSegmentosCuestionariosNoEnviados3() {
        return censoRepository.getSegmentosCuestionariosNoEnviados3();
    }

    public LiveData<List<Segmentos>> getSegmentosCuestionariosNoEnviados2(String subzona) {
        return censoRepository.getSegmentosCuestionariosNoEnviados2(subzona);
    }

/*
    public void sendCuestionarioCreate(ProcessNotifier processNotifier, Cuestionarios cuestionarioSelected,
                                       Segmentos segmentos) {
        censoRepository.enviarCuestionarioCreate(processNotifier, cuestionarioSelected, segmentos);
    }
*/

    public void sendCuestionarioCreate2(ProcessNotifier processNotifier, Activity activity, List<Cuestionarios> cuestionarioSelected,
                                        Segmentos segmentos) {
        censoRepository.enviarCuestionarioCreate2(processNotifier, activity, cuestionarioSelected, segmentos);
    }

/*    public void sendCuestionarioUpdate(ProcessNotifier processNotifier, Cuestionarios cuestionarioSelected,
                                       Segmentos segmentos, String llave) {
        censoRepository.enviarCuestionarioUpdate(processNotifier, cuestionarioSelected, segmentos, llave);
    }*/

//    public boolean verificarRefresh(Cuestionarios cuestionarioSelected, FragmentActivity fragmentActivity) {
//        return censoRepository.verificarRefresh(cuestionarioSelected, fragmentActivity);
//    }
}
