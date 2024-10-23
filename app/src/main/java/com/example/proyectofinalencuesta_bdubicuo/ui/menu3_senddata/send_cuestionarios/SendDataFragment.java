package com.example.proyectofinalencuesta_bdubicuo.ui.menu3_senddata.send_cuestionarios;

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
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Cuestionarios;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.CuestionariosPendientes;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Segmentos;
import com.example.proyectofinalencuesta_bdubicuo.utils.ProcessNotifier;
import com.example.proyectofinalencuesta_bdubicuo.utils.Utilidad;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SendDataFragment extends Fragment implements SendDataAdapter.OnEnvioClickListener {
    private static final String TAG = "SendDataFragment";
    private CheckBox cbEnvioSelectAll;
    private SendDataViewModel sendDataViewModel;
    private boolean[] checkeds;
    private boolean[] checkedsCuestionarios;
    private SendDataAdapter sendDataAdapter;
    private List<String> subZonasListSpinner;
    private ArrayAdapter<String> arrayAdapter;
    private List<Segmentos> segmentosList;
    private List<CuestionariosPendientes> cuestionariosPendientes;
    private List<Cuestionarios> cuestionariosSegmentoList;
    private List<Cuestionarios> cuestionariosList;
    private ProcessNotifier processNotifier;
    private AlertDialog adMostrarCuestionarios;
//    private boolean flagSendCuestionario;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sendDataViewModel = new ViewModelProvider(this).get(SendDataViewModel.class);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar())
                .setTitle(getText(R.string.title_sendData) + " // " +
                        SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_USERNAME));
        View root = inflater.inflate(R.layout.fragment_senddata, container, false);

        FloatingActionButton fabEnviar = root.findViewById(R.id.fabEnviar);
        cbEnvioSelectAll = root.findViewById(R.id.cbEnvioAllItem);
        Spinner spinnerSegmento = root.findViewById(R.id.spinnerSegmento);

        segmentosList = new ArrayList<>();
        cuestionariosPendientes = new ArrayList<>();
        cuestionariosSegmentoList = new ArrayList<>();
        cuestionariosList = new ArrayList<>();
        subZonasListSpinner = new ArrayList<>();

        fabEnviar.setOnClickListener(v -> {
            processNotifier = new ProcessNotifier(requireActivity());
            cuestionariosList.clear();
            if (Utilidad.verificarRed(requireActivity())) {
                if (checkeds.length != 0) {
                    List<Cuestionarios> segmentosSelect = new ArrayList<>();
                    for (int x = 0; x < checkeds.length; x++) {
                        if (checkeds[x]) {
                            for (int y = 0; y < cuestionariosSegmentoList.size(); y++) {
                                if (cuestionariosSegmentoList.get(y).getCodigoSegmento().equals(
                                        cuestionariosPendientes.get(x).getCodigoSegmento())) {
                                    segmentosSelect.add(cuestionariosSegmentoList.get(y));
                                }
                            }
                        }
                    }
                    if (!segmentosSelect.isEmpty())
                        enviarCuestionarios(segmentosSelect);
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

        arrayAdapter = new ArrayAdapter<>(
                requireActivity(),
                R.layout.support_simple_spinner_dropdown_item,
                subZonasListSpinner
        );

        sendDataViewModel.getAllSubZonas().observe(getViewLifecycleOwner(), segmentos -> {
            subZonasListSpinner.clear();
            for (Segmentos segmentos1 : segmentos) {
                subZonasListSpinner.add(segmentos1.getSubZonaID());
            }
            arrayAdapter.notifyDataSetChanged();
        });

        sendDataViewModel.getSegmentosCuestionariosNoEnviados3().observe(getViewLifecycleOwner(), cuestionariosPendientes -> {
            if (cuestionariosPendientes != null) {
                cuestionariosSegmentoList = cuestionariosPendientes;
            }
        });


        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSegmento.setAdapter(arrayAdapter);

        spinnerSegmento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String subZonaSelect = subZonasListSpinner.get(position);
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
            checkeds = new boolean[segmentosList.size()];
            Arrays.fill(checkeds, isChecked);
            sendDataAdapter.setDataChecked(checkeds);
        });

        checkeds = new boolean[segmentosList.size()];
        Arrays.fill(checkeds, false);

        Context context = root.getContext();
        RecyclerView recyclerView = root.findViewById(R.id.recyclerViewEnvio);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        sendDataAdapter = new SendDataAdapter(cuestionariosPendientes, checkeds, this);
        recyclerView.setAdapter(sendDataAdapter);

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

    private void enviarCuestionarios(@NotNull List<Cuestionarios> cuestionariosList) {
        sendDataViewModel.sendCuestionarioCreate2(processNotifier, requireActivity(),cuestionariosList,
                segmentosList.get(0));

//        for (int x = 0; x < cuestionariosList.size(); x++) {
//            enviarCuestionario(cuestionariosList.get(x));
//        }
    }

    private void actualizarSegmentos(String subZonaSelect) {
        sendDataViewModel.getSegmentosCuestionariosNoEnviados(subZonaSelect)
                .observe(getViewLifecycleOwner(), segmentos -> {
                    if (segmentos != null /*&& segmentos.size() != cuestionariosPendientes.size()*/) {
                        cuestionariosPendientes = segmentos;
                        checkeds = new boolean[cuestionariosPendientes.size()];
                        Arrays.fill(checkeds, false);
                        sendDataAdapter.setData(cuestionariosPendientes, checkeds);
                    }
                    if (cuestionariosPendientes.isEmpty()) {
                        cbEnvioSelectAll.setChecked(false);
                        sendDataViewModel.getSegmentosCuestionariosNoEnviados(subZonaSelect).removeObservers(getViewLifecycleOwner());
                    }

                });

        sendDataViewModel.getSegmentosCuestionariosNoEnviados2(subZonaSelect)
                .observe(getViewLifecycleOwner(), segmentos -> {
                    if (!segmentos.isEmpty()) {
                        segmentosList = segmentos;
                    }
                });
    }

    private void mostrarCuestionariosAD(Segmentos segmentos) {
        if (!cuestionariosList.isEmpty()) {
            checkedsCuestionarios = new boolean[cuestionariosList.size()];
            Arrays.fill(checkedsCuestionarios, false);
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setTitle("Seleccione los cuestionarios a enviar");
//            builder.setMessage("LlaveSegmento - Num_Emp | Vivienda-Hogar | Productor-Explotacion");

            builder.setPositiveButton("Enviar", (dialog, which) -> {
                if (Utilidad.verificarRed(requireActivity())) {
                    processNotifier = new ProcessNotifier(requireActivity());
                    List<Cuestionarios> segmentosSelect = new ArrayList<>();
                    for (int x = 0; x < cuestionariosList.size(); x++) {
                        if (checkedsCuestionarios[x]) {
                            segmentosSelect.add(cuestionariosList.get(x));
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

            CharSequence[] charSequences = new CharSequence[cuestionariosList.size()];
            for (int x = 0; x < cuestionariosList.size(); x++)
                charSequences[x] = cuestionariosList.get(x).getCodigoSegmento()
//                        + " - " + segmentos.getEmpadronadorID()
                        + " | " + cuestionariosList.get(x).getVivienda()
//                        + "-" + cuestionariosList.get(x).getHogar()
                        + " | " + cuestionariosList.get(x).getProductor()
                        + "-" + cuestionariosList.get(x).getExplotacionNum();

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
        cuestionariosList.clear();
        sendDataViewModel.getCuestionariosBySegmentoNotSended(segmentosList.get(position).getId()).observe(
                getViewLifecycleOwner(), cuestionarios -> {
                    if (cuestionarios != null) {
                        cuestionariosList = cuestionarios;
                        if (adMostrarCuestionarios == null) {
//                            adMostrarCuestionarios.dismiss();
                            mostrarCuestionariosAD(segmentosList.get(position));
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
