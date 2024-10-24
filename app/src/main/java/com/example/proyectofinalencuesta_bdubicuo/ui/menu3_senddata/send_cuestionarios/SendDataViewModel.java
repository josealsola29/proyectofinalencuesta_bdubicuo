package com.example.proyectofinalencuesta_bdubicuo.ui.menu3_senddata.send_cuestionarios;

import android.app.Activity;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectofinalencuesta_bdubicuo.data.UbicuoRepository;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Cuestionarios;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.CuestionariosPendientes;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Segmentos;
import com.example.proyectofinalencuesta_bdubicuo.utils.ProcessNotifier;

import java.util.List;

public class SendDataViewModel extends ViewModel {
    private final UbicuoRepository ubicuoRepository;
    private static final String TAG = "SendDataViewModel";

    public SendDataViewModel() {
        ubicuoRepository = new UbicuoRepository();
        Log.i(TAG, "CaptureViewModel: Constructor");
    }

    public LiveData<List<Cuestionarios>> getCuestionariosBySegmentoNotSended(String codigoSemento) {
        return ubicuoRepository.getCuestionariosBySegmentoNotSended(codigoSemento);
    }

    public LiveData<List<Segmentos>> getAllSubZonas() {
        return ubicuoRepository.getAllSubZonasSend();
    }

//    public LiveData<List<Segmentos>> getSegmentosSelected(String subZonaSelect) {
//        return ubicuoRepository.getSegmentosSelected(subZonaSelect);
//    }

    public LiveData<List<CuestionariosPendientes>> getSegmentosCuestionariosNoEnviados(String subzona) {
        return ubicuoRepository.getSegmentosCuestionariosNoEnviados(subzona);
    }

    public LiveData<List<Cuestionarios>> getSegmentosCuestionariosNoEnviados3() {
        return ubicuoRepository.getSegmentosCuestionariosNoEnviados3();
    }

    public LiveData<List<Segmentos>> getSegmentosCuestionariosNoEnviados2(String subzona) {
        return ubicuoRepository.getSegmentosCuestionariosNoEnviados2(subzona);
    }

/*
    public void sendCuestionarioCreate(ProcessNotifier processNotifier, Cuestionarios cuestionarioSelected,
                                       Segmentos segmentos) {
        ubicuoRepository.enviarCuestionarioCreate(processNotifier, cuestionarioSelected, segmentos);
    }
*/

    public void sendCuestionarioCreate2(ProcessNotifier processNotifier, Activity activity, List<Cuestionarios> cuestionarioSelected,
                                        Segmentos segmentos) {
        ubicuoRepository.enviarCuestionarioCreate2(processNotifier, activity, cuestionarioSelected, segmentos);
    }

/*    public void sendCuestionarioUpdate(ProcessNotifier processNotifier, Cuestionarios cuestionarioSelected,
                                       Segmentos segmentos, String llave) {
        ubicuoRepository.enviarCuestionarioUpdate(processNotifier, cuestionarioSelected, segmentos, llave);
    }*/

//    public boolean verificarRefresh(Cuestionarios cuestionarioSelected, FragmentActivity fragmentActivity) {
//        return ubicuoRepository.verificarRefresh(cuestionarioSelected, fragmentActivity);
//    }
}
