package com.example.proyectofinalencuesta_bdubicuo.ui.menu3_senddata.send_recorridos;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinalencuesta_bdubicuo.R;
import com.example.proyectofinalencuesta_bdubicuo.data.local.constants.AppConstants;
import com.example.proyectofinalencuesta_bdubicuo.data.local.constants.SharedPreferencesManager;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.ControlRecorrido;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.CuestionariosPendientes;
import com.example.proyectofinalencuesta_bdubicuo.utils.ProcessNotifier;
import com.example.proyectofinalencuesta_bdubicuo.utils.Utilidad;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SendDataRecorrido extends Fragment implements SendDataRecorridoAdapter.OnEnvioClickListener {
    private static final String TAG = "SendDataFragment";
    private CheckBox cbEnvioSelectAll;
    private SendDataRecorridoViewModel sendDataRecorridoViewModel;
    private boolean[] checkeds;
    private boolean[] checkedsCuestionarios;
    private SendDataRecorridoAdapter sendDataRecorridoAdapter;
    private List<String> subZonasRecorridoListSpinner;
    private ArrayAdapter<String> subzonasArrayAdapter;
    private List<ControlRecorrido> controlRecorrido;
    private List<CuestionariosPendientes> recorridosPendientes;
    private List<ControlRecorrido> controlRecorridoList;
    private List<ControlRecorrido> recorridosSegmentosList;
    private ProcessNotifier processNotifier;
    private AlertDialog adMostrarCuestionarios;
//    private boolean flagSendCuestionario;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sendDataRecorridoViewModel = new ViewModelProvider(this).get(SendDataRecorridoViewModel.class);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar())
                .setTitle(getText(R.string.title_sendData) + " // " +
                        SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_USERNAME));
        View root = inflater.inflate(R.layout.fragment_senddata, container, false);

        FloatingActionButton fabEnviar = root.findViewById(R.id.fabEnviar);
        cbEnvioSelectAll = root.findViewById(R.id.cbEnvioAllItem);
        Spinner spinnerSubzona = root.findViewById(R.id.spinnerSegmento);

        controlRecorrido = new ArrayList<>();
        recorridosPendientes = new ArrayList<>();
        controlRecorridoList = new ArrayList<>();
        recorridosSegmentosList = new ArrayList<>();
        subZonasRecorridoListSpinner = new ArrayList<>();

        fabEnviar.setOnClickListener(v -> {
            processNotifier = new ProcessNotifier(requireActivity());
            recorridosSegmentosList.clear();
            if (Utilidad.verificarRed(requireActivity())) {
                if (checkeds.length != 0) {
                    List<ControlRecorrido> controlRecorridoListCheck = new ArrayList<>();
                    for (int x = 0; x < checkeds.length; x++) {
                        if (checkeds[x]) {
                            for (int y = 0; y < controlRecorridoList.size(); y++) {
                                if (controlRecorridoList.get(y).getCodigoSegmento().equals(
                                        recorridosPendientes.get(x).getCodigoSegmento())) {
                                    controlRecorridoListCheck.add(controlRecorridoList.get(y));
                                }
                            }
                        }
                    }
                    if (!controlRecorridoListCheck.isEmpty())
                        enviarCuestionarios(controlRecorridoListCheck);
                    else Toast.makeText(requireActivity(), "Debe seleccionar un cuestionario.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    showAlertDialog("No hay cuestionarios seleccionados.");
                }
            } else {
                processNotifier.dismiss();
                showAlertDialog("Debe verificar su conexiòn de red.");
            }
        });

        subzonasArrayAdapter = new ArrayAdapter<>(
                requireActivity(),
                R.layout.support_simple_spinner_dropdown_item,
                subZonasRecorridoListSpinner
        );

        sendDataRecorridoViewModel.getAllSubZonasRecorridoSend().observe(getViewLifecycleOwner(), controlRecorridos -> {
            subZonasRecorridoListSpinner.clear();
            for (ControlRecorrido segmentos1 : controlRecorridos) {
                subZonasRecorridoListSpinner.add(segmentos1.getSubzona());
            }
            subzonasArrayAdapter.notifyDataSetChanged();
        });

        sendDataRecorridoViewModel.getRecorridosNoEnviados().observe(getViewLifecycleOwner(), controlRecorridosBD -> {
            if (controlRecorridosBD != null) {
                controlRecorridoList = controlRecorridosBD;
            }
        });


        subzonasArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubzona.setAdapter(subzonasArrayAdapter);

        spinnerSubzona.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String subZonaSelect = subZonasRecorridoListSpinner.get(position);
                actualizarSegmentos(subZonaSelect);
                cbEnvioSelectAll.setChecked(false);
//                actualizarSegmentos2(subZonaSelect);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i(TAG, "onNothingSelected: ");
            }
        });

        cbEnvioSelectAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            buttonView.setChecked(isChecked);
            checkeds = new boolean[controlRecorrido.size()];
            Arrays.fill(checkeds, isChecked);
            sendDataRecorridoAdapter.setDataChecked(checkeds);
        });

        checkeds = new boolean[controlRecorrido.size()];
        Arrays.fill(checkeds, false);

        Context context = root.getContext();
        RecyclerView recyclerView = root.findViewById(R.id.recyclerViewEnvio);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        sendDataRecorridoAdapter = new SendDataRecorridoAdapter(recorridosPendientes, checkeds, this);
        recyclerView.setAdapter(sendDataRecorridoAdapter);

        return root;
    }

    private void showAlertDialog(String mensaje) {
        MaterialAlertDialogBuilder materialAlertDialogBuilder =
                new MaterialAlertDialogBuilder(requireActivity());
        materialAlertDialogBuilder.setTitle("Envio de datos.");
        materialAlertDialogBuilder.setMessage(mensaje);
        materialAlertDialogBuilder.setIcon(R.drawable.ic_dialog_warning);
        materialAlertDialogBuilder.setPositiveButton("OK",
                (dialog, which) -> dialog.dismiss());
        materialAlertDialogBuilder.show();
    }

    private void enviarCuestionarios(@NotNull List<ControlRecorrido> cuestionariosList) {
        sendDataRecorridoViewModel.enviarRecorridoListNoEnviados(processNotifier, cuestionariosList);

//        for (int x = 0; x < cuestionariosList.size(); x++) {
//            enviarCuestionario(cuestionariosList.get(x));
//        }
    }

    private void actualizarSegmentos(String subZonaSelect) {
        sendDataRecorridoViewModel.getSegmentosRecorridosNoEnviados(subZonaSelect)
                .observe(getViewLifecycleOwner(), cuestionariosPendientes -> {
                    if (cuestionariosPendientes != null /*&& segmentos.size() != cuestionariosPendientes.size()*/) {
                        recorridosPendientes = cuestionariosPendientes;
                        checkeds = new boolean[recorridosPendientes.size()];
                        Arrays.fill(checkeds, false);
                        sendDataRecorridoAdapter.setData(recorridosPendientes, checkeds);
                    }

                    if (recorridosPendientes.isEmpty()) {
                        cbEnvioSelectAll.setChecked(false);
                        sendDataRecorridoViewModel.getSegmentosRecorridosNoEnviados(subZonaSelect).removeObservers(getViewLifecycleOwner());
                    }

                });

        sendDataRecorridoViewModel.getSegmentosRecorridoNoEnviadosIndiv(subZonaSelect)
                .observe(getViewLifecycleOwner(), controlRecorridoListBD -> {
                    if (controlRecorridoListBD != null) {
                        controlRecorrido = controlRecorridoListBD;
                    }
                });
    }

    private void mostrarCuestionariosAD(ControlRecorrido controlRecorridoSelect) {
        if (!recorridosSegmentosList.isEmpty()) {
            checkedsCuestionarios = new boolean[recorridosSegmentosList.size()];
            Arrays.fill(checkedsCuestionarios, false);
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setTitle("Seleccione los recorridos a enviar");
//            builder.setMessage("LlaveSegmento - Num_Emp | Vivienda-Hogar | Productor-Explotacion");

            builder.setPositiveButton("Enviar", (dialog, which) -> {
                if (Utilidad.verificarRed(requireActivity())) {
                    processNotifier = new ProcessNotifier(requireActivity());
                    List<ControlRecorrido> segmentosSelect = new ArrayList<>();
                    for (int x = 0; x < recorridosSegmentosList.size(); x++) {
                        if (checkedsCuestionarios[x]) {
                            segmentosSelect.add(recorridosSegmentosList.get(x));
                        }
                    }
                    if (!segmentosSelect.isEmpty())
                        enviarCuestionarios(segmentosSelect);
                    else
                        Toast.makeText(getActivity(), "Cuestionarios no seleccionados.",
                                Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "onClick: onEnvioSegmentoClick");
                } else {
                    showAlertDialog("Debe verificar su conexiòn de red.");
                }
            });

            builder.setNegativeButton("Cancelar", (dialog, which) -> {
                dialog.dismiss();
                Log.i(TAG, "onClick: onEnvioSegmentoClick");
            });

            CharSequence[] charSequences = new CharSequence[recorridosSegmentosList.size()];
            for (int x = 0; x < recorridosSegmentosList.size(); x++)
                charSequences[x] = recorridosSegmentosList.get(x).getCodigoSegmento()
//                        + " - " + segmentos.getEm()
                        + " | V. " + recorridosSegmentosList.get(x).getVivienda()
                        /*+ "- H. " + recorridosSegmentosList.get(x).getHogar()*/;

            builder.setMultiChoiceItems(charSequences, null, (dialog, which, isChecked) -> {
                checkedsCuestionarios[which] = isChecked;
                Log.i(TAG, "onClick: onEnvioSegmentoClick");
            });

            adMostrarCuestionarios = builder.create();
            adMostrarCuestionarios.show();
        } else
            Toast.makeText(requireActivity(), "Sin cuestionarios.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCardViewSegmentoClick(int position) {
        adMostrarCuestionarios = null;
        recorridosSegmentosList.clear();
        sendDataRecorridoViewModel.getRecorridosBySegmentoNotSended(controlRecorrido.get(position).getCodigoSegmento()).observe(
                getViewLifecycleOwner(), controlRecorridoBD -> {
                    if (controlRecorridoBD != null) {
                        recorridosSegmentosList = controlRecorridoBD;
                        if (adMostrarCuestionarios == null) {
//                            adMostrarCuestionarios.dismiss();
                            mostrarCuestionariosAD(controlRecorrido.get(position));
                        }
                    } else
                        Toast.makeText(requireActivity(), "Sin cuestionarios.",
                                Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onCheckBoxSegmentoClick(int position) {
        checkeds[position] = !checkeds[position];
    }
}
