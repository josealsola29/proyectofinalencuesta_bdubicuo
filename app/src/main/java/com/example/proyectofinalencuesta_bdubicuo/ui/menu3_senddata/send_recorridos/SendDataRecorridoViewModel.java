package com.example.proyectofinalencuesta_bdubicuo.ui.menu3_senddata.send_recorridos;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectofinalencuesta_bdubicuo.data.CensoRepository;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.ControlRecorrido;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.CuestionariosPendientes;
import com.example.proyectofinalencuesta_bdubicuo.utils.ProcessNotifier;

import java.util.List;

public class SendDataRecorridoViewModel extends ViewModel {
    private final CensoRepository censoRepository;
    private static final String TAG = "SendDataViewModel";

    public SendDataRecorridoViewModel() {
        censoRepository = new CensoRepository();
        Log.i(TAG, "CaptureViewModel: Constructor");
    }

    public LiveData<List<ControlRecorrido>> getRecorridosBySegmentoNotSended(String codigoSegmento) {
        return censoRepository.getRecorridosBySegmentoNotSended(codigoSegmento);
    }

    public LiveData<List<ControlRecorrido>> getAllSubZonasRecorridoSend() {
        return censoRepository.getAllSubZonasRecorridoSend();
    }

//    public LiveData<List<Segmentos>> getSegmentosSelected(String subZonaSelect) {
//        return censoRepository.getSegmentosSelected(subZonaSelect);
//    }

    public LiveData<List<CuestionariosPendientes>> getSegmentosRecorridosNoEnviados(String subzona) {
        return censoRepository.getSegmentosRecorridosNoEnviados(subzona);
    }

    public LiveData<List<ControlRecorrido>> getRecorridosNoEnviados() {
        return censoRepository.getRecorridosNoEnviados();
    }

    public LiveData<List<ControlRecorrido>> getSegmentosRecorridoNoEnviadosIndiv(String subzona) {
        return censoRepository.getSegmentosRecorridoNoEnviadosIndiv(subzona);
    }

    public void enviarRecorridoListNoEnviados(ProcessNotifier processNotifier,
                                              List<ControlRecorrido> cuestionarioSelected) {
        censoRepository.enviarRecorridoListNoEnviados(processNotifier, cuestionarioSelected);
    }
}
