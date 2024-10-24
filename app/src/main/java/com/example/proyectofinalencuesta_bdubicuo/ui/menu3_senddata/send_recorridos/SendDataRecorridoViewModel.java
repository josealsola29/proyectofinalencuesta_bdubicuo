package com.example.proyectofinalencuesta_bdubicuo.ui.menu3_senddata.send_recorridos;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectofinalencuesta_bdubicuo.data.UbicuoRepository;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.ControlRecorrido;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.CuestionariosPendientes;
import com.example.proyectofinalencuesta_bdubicuo.utils.ProcessNotifier;

import java.util.List;

public class SendDataRecorridoViewModel extends ViewModel {
    private final UbicuoRepository ubicuoRepository;
    private static final String TAG = "SendDataViewModel";

    public SendDataRecorridoViewModel() {
        ubicuoRepository = new UbicuoRepository();
        Log.i(TAG, "CaptureViewModel: Constructor");
    }

    public LiveData<List<ControlRecorrido>> getRecorridosBySegmentoNotSended(String codigoSegmento) {
        return ubicuoRepository.getRecorridosBySegmentoNotSended(codigoSegmento);
    }

    public LiveData<List<ControlRecorrido>> getAllSubZonasRecorridoSend() {
        return ubicuoRepository.getAllSubZonasRecorridoSend();
    }

//    public LiveData<List<Segmentos>> getSegmentosSelected(String subZonaSelect) {
//        return ubicuoRepository.getSegmentosSelected(subZonaSelect);
//    }

    public LiveData<List<CuestionariosPendientes>> getSegmentosRecorridosNoEnviados(String subzona) {
        return ubicuoRepository.getSegmentosRecorridosNoEnviados(subzona);
    }

    public LiveData<List<ControlRecorrido>> getRecorridosNoEnviados() {
        return ubicuoRepository.getRecorridosNoEnviados();
    }

    public LiveData<List<ControlRecorrido>> getSegmentosRecorridoNoEnviadosIndiv(String subzona) {
        return ubicuoRepository.getSegmentosRecorridoNoEnviadosIndiv(subzona);
    }

    public void enviarRecorridoListNoEnviados(ProcessNotifier processNotifier,
                                              List<ControlRecorrido> cuestionarioSelected) {
        ubicuoRepository.enviarRecorridoListNoEnviados(processNotifier, cuestionarioSelected);
    }
}
