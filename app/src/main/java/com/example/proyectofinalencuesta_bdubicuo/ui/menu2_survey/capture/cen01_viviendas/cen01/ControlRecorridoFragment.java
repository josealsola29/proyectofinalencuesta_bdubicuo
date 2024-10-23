package com.example.proyectofinalencuesta_bdubicuo.ui.menu2_survey.capture.cen01_viviendas.cen01;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinalencuesta_bdubicuo.R;
import com.example.proyectofinalencuesta_bdubicuo.data.local.constants.AppConstants;
import com.example.proyectofinalencuesta_bdubicuo.data.local.constants.SharedPreferencesManager;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.ControlRecorrido;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Cuestionarios;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Segmentos;
import com.example.proyectofinalencuesta_bdubicuo.utils.ProcessNotifier;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ControlRecorridoFragment extends Fragment implements View.OnClickListener, ControlRecorridoAdapter.OnControlRecorridoListener {
    private final FragmentActivity activity;
    private final Segmentos segmentos;

    private ControlRecorridoViewModel controlRecorridoViewModel;

    private List<ControlRecorrido> controlRecorridoList;

    private ControlRecorridoAdapter controlRecorridoAdapter;

    private boolean flagAddRecorrido = false;
    private boolean flagPreguntas = false;
//    private boolean flagAddHogar = false;

    private boolean flagOllaComun = false;
    private boolean flagCondicion = false;
    private boolean flagEliminar = false;
    private boolean flagLimpiar = false;

    public ControlRecorridoFragment(FragmentActivity requireActivity, Segmentos segmentos) {
        this.activity = requireActivity;
        this.segmentos = segmentos;
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        View view = inflater.inflate(R.layout.dialogfragment_cen01, container, false);
        controlRecorridoViewModel = new ViewModelProvider(this).get(ControlRecorridoViewModel.class);
        flagAddRecorrido = true;

        TextView tvTituloRegionCR = view.findViewById(R.id.tvTituloRegionCR);
        TextView tvTituloZonaCR = view.findViewById(R.id.tvTituloZonaCR);
        TextView tvTituloSubzonaCR = view.findViewById(R.id.tvTituloSubzonaCR);
        TextView tvTituloSegmentoCR = view.findViewById(R.id.tvTituloSegmentoCR);
        ExtendedFloatingActionButton efabCen01_AddRecorrido = view.findViewById(R.id.efabCen01_AddRecorrido);
        RecyclerView rvCenRecorridos = view.findViewById(R.id.rvCenRecorridos);

        efabCen01_AddRecorrido.setOnClickListener(this);

        tvTituloRegionCR.setText(String.format("Region: %s", segmentos.getRegionID()));
        tvTituloZonaCR.setText(String.format("Zona: %s", segmentos.getZonaID()));
        tvTituloSubzonaCR.setText(String.format("SubZona: %s", segmentos.getSubZonaID()));
        tvTituloSegmentoCR.setText(String.format("Segmento: %s - %s - %s",
                segmentos.getId().substring(0, 6),
                segmentos.getId().substring(6, 10),
                segmentos.getId().substring(10, 12)));

        controlRecorridoList = new ArrayList<>();

        rvCenRecorridos.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        controlRecorridoAdapter = new ControlRecorridoAdapter(/*segmentos,*/ controlRecorridoList, activity,
                this);
        rvCenRecorridos.setAdapter(controlRecorridoAdapter);

        cargarDatosRV();
        return view;
    }

    private void cargarDatosRV() {
        controlRecorridoViewModel.getRecorridosPorSegmento(segmentos.getId()).observe(
                activity, controlRecorridos -> {
                    controlRecorridoList = controlRecorridos;

                    if (flagAddRecorrido || flagPreguntas || flagOllaComun || flagCondicion || flagEliminar || flagLimpiar) {
                        controlRecorridoAdapter.setData(/*controlRecorridoViviendaList,*/ controlRecorridoList);
                        flagAddRecorrido = false;
                        flagPreguntas = false;
                        flagOllaComun = false;
                        flagCondicion = false;
                        flagEliminar = false;
                        flagLimpiar = false;
                    }
                });
    }

    private ControlRecorrido getNuevoRecorrido(String numVivienda, int condicionVivienda) {
        SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
        Date date = new Date();
        String fecha = sdf.format(date);
        return new ControlRecorrido(
                segmentos.getId() + numVivienda,
                segmentos.getId(),
                segmentos.getSubZonaID(),
                segmentos.getProvinciaID(),
                segmentos.getDistritoID(),
                segmentos.getCorregimientoID(),
                segmentos.getSegmentoID(),
                segmentos.getDivisionID(),
                SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_CODIGO),
                segmentos.getEmpadronadorID(),
                numVivienda,
//                "01",
                null,
                condicionVivienda,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                fecha,
                fecha,
                null,
                null,
                null,
                null,
                null);
    }

    private Cuestionarios getNuevoProductor(ControlRecorrido controlRecorridoSelected, String fecha,
                                            String numProductor) {
        return new Cuestionarios(
                controlRecorridoSelected.getLlaveRecorrido() + numProductor + "01",
                controlRecorridoSelected.getLlaveRecorrido(),
                segmentos.getId(),
                segmentos.getProvinciaID(),
                segmentos.getDistritoID(),
                segmentos.getCorregimientoID(),
                null,
                null,
                null,
                null,
                null,
                null,
                numProductor,
                "01",
                segmentos.getSubZonaID(),
                segmentos.getSegmentoID(),
                segmentos.getDivisionID(),
                SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_CODIGO),
                segmentos.getEmpadronadorID(),
                null,
                controlRecorridoSelected.getVivienda(),
//                controlRecorridoSelected.getHogar(),
                null,
                null,
                null,
                null,
                0,
                fecha,
                fecha,
                null,
                null,
                false,
                false,
                null,
                null
        );
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.efabCen01_AddRecorrido) {
            String[] condicionVivienda = activity
                    .getResources()
                    .getStringArray(R.array.catalogo_condicionvivienda);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
            alertDialogBuilder.setTitle("Condición de la vivienda");
//            alertDialogBuilder.setMessage("Condicón de la vivienda con ocupantes:");
            alertDialogBuilder.setSingleChoiceItems(condicionVivienda, -1, null);
            alertDialogBuilder.setPositiveButton("Crear", (dialog, i) -> {
                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                if (selectedPosition >= 0) {
                    addRecorrido(/*condicionVivienda[selectedPosition]*/(selectedPosition + 1));
                } else
                    Toast.makeText(activity, "Debe seleccionar alguna opción.", Toast.LENGTH_SHORT).show();
            });
            alertDialogBuilder.setNegativeButton("Cancelar", null);
            alertDialogBuilder.show();
        }
    }

    private void addRecorrido(int condicionVivienda) {
        flagAddRecorrido = true;
        String numVivienda;
        int numViviendaSelected;

        if (controlRecorridoList.isEmpty()) {
            numVivienda = "01";
        } else {
            numViviendaSelected = Integer.parseInt(controlRecorridoList.get(
                    controlRecorridoList.size() - 1).getVivienda());
            if (numViviendaSelected < 9)
                numVivienda = "0" + (Integer.parseInt(controlRecorridoList.get(
                        controlRecorridoList.size() - 1).getVivienda()) + 1);
            else
                numVivienda = String.valueOf(Integer.parseInt(controlRecorridoList.get(
                        controlRecorridoList.size() - 1).getVivienda()) + 1);
        }

//            if (controlRecorridoList.size() > 0) {
//                if (controlRecorridoList.get(controlRecorridoList.size() - 1)
//                        .getEstadoVivienda() >= 1) {
//                    controlRecorridoViewModel.addRecorridoHogar(getNuevoRecorrido(numVivienda));
//                    controlRecorridoAdapter.notifyDataSetChanged();
//                } else {
//                    Utilidad.showMessageDialog(
//                            "Creación de vivienda",
//                            "Ya tiene una vivienda creada sin capturar.",
//                            true, activity, R.raw.error_sign);
//                }
//            } else {
        ControlRecorrido nuevoCR = getNuevoRecorrido(numVivienda, condicionVivienda);
        controlRecorridoViewModel.addRecorridoHogar(nuevoCR);
        if (condicionVivienda > 1) {
            enviarRecorridoServidor(nuevoCR, segmentos);
        }
//                viviendaHogarDialogAdapter.notifyDataSetChanged();
//            }
    }

    private void enviarRecorridoServidor(ControlRecorrido nuevoCR, Segmentos segmentos) {
        ProcessNotifier processNotifier = new ProcessNotifier(activity);
        processNotifier.setTitle("Envío al servidor");
        processNotifier.setText("Enviando recorrido al servidor. Espere...");
        processNotifier.show();
        controlRecorridoViewModel.enviarControlRecorridoServidor(nuevoCR, activity, processNotifier, segmentos);
    }

    /**
     * Cuando se presiona sobre la fila del recorrido se hace visible el LinearLayout y se expande.
     * Si se toca nuevamente se hace invisble y se contrae.
     */
    @Override
    public void onControlRecorridoClick(ControlRecorrido controlRecorridoList, View view,
                                        LinearLayout lnCen01_recorrido, int posicion) {
        try {
            if (lnCen01_recorrido.getVisibility() == View.VISIBLE) {
                lnCen01_recorrido.setVisibility(View.GONE);
            } else {
                lnCen01_recorrido.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            Log.e("TAG", "onClick: ");
        }
    }


    private void verBateriasPreguntasAD(ControlRecorrido controlRecorridoHogarSelected) {
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(activity);
        View v = getLayoutInflater().inflate(R.layout.items_cen01_hogarcuestionario2, null);
        TextView tvCen01_Hogar = v.findViewById(R.id.tvCen01_Hogar);
        RadioGroup rgCen01_preg0708Cultivos = v.findViewById(R.id.rgCen01_preg0708Cultivos);
        RadioButton rbCen01_Si_preg0708Cultivos = v.findViewById(R.id.rbCen01_Si_preg0708Cultivos);
        RadioButton rbCen01_No_preg0708Cultivos = v.findViewById(R.id.rbCen01_No_preg0708Cultivos);

        RadioGroup rgCen01_preg0910Frutales = v.findViewById(R.id.rgCen01_preg0910Frutales);
        RadioButton rbCen01_Si_preg0910Frutales = v.findViewById(R.id.rbCen01_Si_preg0910Frutales);
        RadioButton rbCen01_No_preg0910Frutales = v.findViewById(R.id.rbCen01_No_preg0910Frutales);

        RadioGroup rgCen01_preg1112Cultivos = v.findViewById(R.id.rgCen01_preg1112Cultivos);
        RadioButton rbCen01_Si_preg1112Cultivos = v.findViewById(R.id.rbCen01_Si_preg1112Cultivos);
        RadioButton rbCen01_No_preg1112Cultivos = v.findViewById(R.id.rbCen01_No_preg1112Cultivos);

        RadioGroup rgCen01_preg1314Ganado = v.findViewById(R.id.rgCen01_preg1314Ganado);
        RadioButton rbCen01_Si_preg1314Ganado = v.findViewById(R.id.rbCen01_Si_preg1314Ganado);
        RadioButton rbCen01_No_preg1314Ganado = v.findViewById(R.id.rbCen01_No_preg1314Ganado);

        RadioGroup rgCen01_preg1516Especies = v.findViewById(R.id.rgCen01_preg1516Especies);
        RadioButton rbCen01_Si_preg1516Especies = v.findViewById(R.id.rbCen01_Si_preg1516Especies);
        RadioButton rbCen01_No_preg1516Especies = v.findViewById(R.id.rbCen01_No_preg1516Especies);

        RadioGroup rgCen01_preg1718Pesca = v.findViewById(R.id.rgCen01_preg1718Pesca);
        RadioButton rbCen01_Si_preg1718Pesca = v.findViewById(R.id.rbCen01_Si_preg1718Pesca);
        RadioButton rbCen01_No_preg1718Pesca = v.findViewById(R.id.rbCen01_No_preg1718Pesca);

        RadioGroup rgCen01_preg1920Empresa = v.findViewById(R.id.rgCen01_preg1920Empresa);
        RadioButton rbCen01_Si_preg1920Empresa = v.findViewById(R.id.rbCen01_Si_preg1920Empresa);
        RadioButton rbCen01_No_preg1920Empresa = v.findViewById(R.id.rbCen01_No_preg1920Empresa);

        EditText etCen01_numProductores = v.findViewById(R.id.etCen01_numProductores);
        EditText etCen01_numExplotaciones = v.findViewById(R.id.etCen01_numExplotaciones);

        LinearLayout lnCen01_TotalProductores = v.findViewById(R.id.lnCen01_TotalProductores);

        tvCen01_Hogar.setText(String.format("Seleccione para la vivienda %s", controlRecorridoHogarSelected.getVivienda()));
        if (controlRecorridoHogarSelected.getPregA() != null) {
            if (controlRecorridoHogarSelected.getPregA()) {
                rbCen01_Si_preg0708Cultivos.setChecked(true);
                rbCen01_No_preg0708Cultivos.setChecked(false);
            } else {
                rbCen01_Si_preg0708Cultivos.setChecked(false);
                rbCen01_No_preg0708Cultivos.setChecked(true);
            }
        }

        if (controlRecorridoHogarSelected.getPregB() != null) {
            if (controlRecorridoHogarSelected.getPregB()) {
                rbCen01_Si_preg0910Frutales.setChecked(true);
                rbCen01_No_preg0910Frutales.setChecked(false);
            } else {
                rbCen01_Si_preg0910Frutales.setChecked(false);
                rbCen01_No_preg0910Frutales.setChecked(true);
            }
        }

        if (controlRecorridoHogarSelected.getPregC() != null) {
            if (controlRecorridoHogarSelected.getPregC()) {
                rbCen01_Si_preg1112Cultivos.setChecked(true);
                rbCen01_No_preg1112Cultivos.setChecked(false);
            } else {
                rbCen01_Si_preg1112Cultivos.setChecked(false);
                rbCen01_No_preg1112Cultivos.setChecked(true);
            }
        }

        if (controlRecorridoHogarSelected.getPregD() != null) {
            if (controlRecorridoHogarSelected.getPregD()) {
                rbCen01_Si_preg1314Ganado.setChecked(true);
                rbCen01_No_preg1314Ganado.setChecked(false);
            } else {
                rbCen01_Si_preg1314Ganado.setChecked(false);
                rbCen01_No_preg1314Ganado.setChecked(true);
            }
        }

        if (controlRecorridoHogarSelected.getPregE() != null) {
            if (controlRecorridoHogarSelected.getPregE()) {
                rbCen01_Si_preg1516Especies.setChecked(true);
                rbCen01_No_preg1516Especies.setChecked(false);
            } else {
                rbCen01_Si_preg1516Especies.setChecked(false);
                rbCen01_No_preg1516Especies.setChecked(true);
            }
        }

        if (controlRecorridoHogarSelected.getPregF() != null) {
            if (controlRecorridoHogarSelected.getPregF()) {
                rbCen01_Si_preg1718Pesca.setChecked(true);
                rbCen01_No_preg1718Pesca.setChecked(false);
            } else {
                rbCen01_Si_preg1718Pesca.setChecked(false);
                rbCen01_No_preg1718Pesca.setChecked(true);
            }
        }

        if (controlRecorridoHogarSelected.getPregG() != null) {
            if (controlRecorridoHogarSelected.getPregG()) {
                rbCen01_Si_preg1920Empresa.setChecked(true);
                rbCen01_No_preg1920Empresa.setChecked(false);
            } else {
                rbCen01_Si_preg1920Empresa.setChecked(false);
                rbCen01_No_preg1920Empresa.setChecked(true);
            }
        }

        if (controlRecorridoHogarSelected.getCantProductoresAgro() != null)
            etCen01_numProductores.setText(String.valueOf(controlRecorridoHogarSelected.getCantProductoresAgro()));

        if (controlRecorridoHogarSelected.getCantExplotacionesAgro() != null)
            etCen01_numExplotaciones.setText(String.valueOf(controlRecorridoHogarSelected.getCantExplotacionesAgro()));

        lnCen01_TotalProductores.setVisibility(verificarRadioButtons_Si(rbCen01_Si_preg0708Cultivos,
                rbCen01_Si_preg0910Frutales,
                rbCen01_Si_preg1112Cultivos,
                rbCen01_Si_preg1314Ganado,
                rbCen01_Si_preg1516Especies,
                rbCen01_Si_preg1718Pesca,
                rbCen01_Si_preg1920Empresa));
        alertDialogBuilder.setView(v);

        rgCen01_preg0708Cultivos.setOnCheckedChangeListener((radioGroup, i) -> {
            if (radioGroup.getCheckedRadioButtonId() == R.id.rbCen01_Si_preg0708Cultivos
                    || radioGroup.getCheckedRadioButtonId() == R.id.rbCen01_No_preg0708Cultivos) {
                lnCen01_TotalProductores.setVisibility(verificarRadioButtons_Si(rbCen01_Si_preg0708Cultivos,
                        rbCen01_Si_preg0910Frutales,
                        rbCen01_Si_preg1112Cultivos,
                        rbCen01_Si_preg1314Ganado,
                        rbCen01_Si_preg1516Especies,
                        rbCen01_Si_preg1718Pesca,
                        rbCen01_Si_preg1920Empresa));
            }
            controlRecorridoHogarSelected.setPregA(verificarRadioButtonSelected(
                    radioGroup.getCheckedRadioButtonId(), R.id.rbCen01_Si_preg0708Cultivos,
                    R.id.rbCen01_No_preg0708Cultivos));
        });

        rgCen01_preg0910Frutales.setOnCheckedChangeListener((radioGroup, i) -> {
            if (radioGroup.getCheckedRadioButtonId() == R.id.rbCen01_Si_preg0910Frutales
                    || radioGroup.getCheckedRadioButtonId() == R.id.rbCen01_No_preg0910Frutales) {
                lnCen01_TotalProductores.setVisibility(verificarRadioButtons_Si(rbCen01_Si_preg0708Cultivos,
                        rbCen01_Si_preg0910Frutales,
                        rbCen01_Si_preg1112Cultivos,
                        rbCen01_Si_preg1314Ganado,
                        rbCen01_Si_preg1516Especies,
                        rbCen01_Si_preg1718Pesca,
                        rbCen01_Si_preg1920Empresa));
            }
            controlRecorridoHogarSelected.setPregB(verificarRadioButtonSelected(
                    radioGroup.getCheckedRadioButtonId(), R.id.rbCen01_Si_preg0910Frutales,
                    R.id.rbCen01_No_preg0910Frutales));
        });

        rgCen01_preg1112Cultivos.setOnCheckedChangeListener((radioGroup, i) -> {
            if (radioGroup.getCheckedRadioButtonId() == R.id.rbCen01_Si_preg1112Cultivos
                    || radioGroup.getCheckedRadioButtonId() == R.id.rbCen01_No_preg1112Cultivos) {
                lnCen01_TotalProductores.setVisibility(verificarRadioButtons_Si(
                        rbCen01_Si_preg0708Cultivos,
                        rbCen01_Si_preg0910Frutales,
                        rbCen01_Si_preg1112Cultivos,
                        rbCen01_Si_preg1314Ganado,
                        rbCen01_Si_preg1516Especies,
                        rbCen01_Si_preg1718Pesca,
                        rbCen01_Si_preg1920Empresa));
            }
            controlRecorridoHogarSelected.setPregC(verificarRadioButtonSelected(
                    radioGroup.getCheckedRadioButtonId(), R.id.rbCen01_Si_preg1112Cultivos,
                    R.id.rbCen01_No_preg1112Cultivos));
        });

        rgCen01_preg1314Ganado.setOnCheckedChangeListener((radioGroup, i) -> {
            if (radioGroup.getCheckedRadioButtonId() == R.id.rbCen01_Si_preg1314Ganado
                    || radioGroup.getCheckedRadioButtonId() == R.id.rbCen01_No_preg1314Ganado) {
                lnCen01_TotalProductores.setVisibility(verificarRadioButtons_Si(
                        rbCen01_Si_preg0708Cultivos,
                        rbCen01_Si_preg0910Frutales,
                        rbCen01_Si_preg1112Cultivos,
                        rbCen01_Si_preg1314Ganado,
                        rbCen01_Si_preg1516Especies,
                        rbCen01_Si_preg1718Pesca,
                        rbCen01_Si_preg1920Empresa));
            }

            controlRecorridoHogarSelected.setPregD(verificarRadioButtonSelected(
                    radioGroup.getCheckedRadioButtonId(), R.id.rbCen01_Si_preg1314Ganado,
                    R.id.rbCen01_No_preg1314Ganado));
        });

        rgCen01_preg1516Especies.setOnCheckedChangeListener((radioGroup, i) -> {
            if (radioGroup.getCheckedRadioButtonId() == R.id.rbCen01_Si_preg1516Especies
                    || radioGroup.getCheckedRadioButtonId() == R.id.rbCen01_No_preg1516Especies) {
                lnCen01_TotalProductores.setVisibility(verificarRadioButtons_Si(
                        rbCen01_Si_preg0708Cultivos,
                        rbCen01_Si_preg0910Frutales,
                        rbCen01_Si_preg1112Cultivos,
                        rbCen01_Si_preg1314Ganado,
                        rbCen01_Si_preg1516Especies,
                        rbCen01_Si_preg1718Pesca,
                        rbCen01_Si_preg1920Empresa));
            }
            controlRecorridoHogarSelected.setPregE(verificarRadioButtonSelected(
                    radioGroup.getCheckedRadioButtonId(), R.id.rbCen01_Si_preg1516Especies,
                    R.id.rbCen01_No_preg1516Especies));
        });

        rgCen01_preg1718Pesca.setOnCheckedChangeListener((radioGroup, i) -> {
            if (radioGroup.getCheckedRadioButtonId() == R.id.rbCen01_Si_preg1718Pesca
                    || radioGroup.getCheckedRadioButtonId() == R.id.rbCen01_No_preg1718Pesca) {
                lnCen01_TotalProductores.setVisibility(verificarRadioButtons_Si(
                        rbCen01_Si_preg0708Cultivos,
                        rbCen01_Si_preg0910Frutales,
                        rbCen01_Si_preg1112Cultivos,
                        rbCen01_Si_preg1314Ganado,
                        rbCen01_Si_preg1516Especies,
                        rbCen01_Si_preg1718Pesca,
                        rbCen01_Si_preg1920Empresa));
            }
            controlRecorridoHogarSelected.setPregF(verificarRadioButtonSelected(
                    radioGroup.getCheckedRadioButtonId(), R.id.rbCen01_Si_preg1718Pesca,
                    R.id.rbCen01_No_preg1718Pesca));
        });

        rgCen01_preg1920Empresa.setOnCheckedChangeListener((radioGroup, i) -> {
            if (radioGroup.getCheckedRadioButtonId() == R.id.rbCen01_Si_preg1920Empresa
                    || radioGroup.getCheckedRadioButtonId() == R.id.rbCen01_No_preg1920Empresa) {
                lnCen01_TotalProductores.setVisibility(verificarRadioButtons_Si(
                        rbCen01_Si_preg0708Cultivos,
                        rbCen01_Si_preg0910Frutales,
                        rbCen01_Si_preg1112Cultivos,
                        rbCen01_Si_preg1314Ganado,
                        rbCen01_Si_preg1516Especies,
                        rbCen01_Si_preg1718Pesca,
                        rbCen01_Si_preg1920Empresa));
            }
            controlRecorridoHogarSelected.setPregG(verificarRadioButtonSelected(
                    radioGroup.getCheckedRadioButtonId(), R.id.rbCen01_Si_preg1920Empresa,
                    R.id.rbCen01_No_preg1920Empresa));
        });

        alertDialogBuilder.setPositiveButton("Guardar", (dialogInterface, i) -> {
            String falta = "";
            if (controlRecorridoHogarSelected.getPregA() == null)
                falta = falta + "Pregunta A | ";
            if (controlRecorridoHogarSelected.getPregB() == null)
                falta = falta + "Pregunta B | ";
            if (controlRecorridoHogarSelected.getPregC() == null)
                falta = falta + "Pregunta C | ";
            if (controlRecorridoHogarSelected.getPregD() == null)
                falta = falta + "Pregunta D | ";
            if (controlRecorridoHogarSelected.getPregE() == null)
                falta = falta + "Pregunta E | ";
            if (controlRecorridoHogarSelected.getPregF() == null)
                falta = falta + "Pregunta F | ";
            if (controlRecorridoHogarSelected.getPregG() == null)
                falta = falta + "Pregunta G | ";

            if (/*lnCen01_TotalProductores.getVisibility() == View.VISIBLE &&*/
                    controlRecorridoHogarSelected.getPregA() != null && controlRecorridoHogarSelected.getPregB() != null
                            && controlRecorridoHogarSelected.getPregC() != null && controlRecorridoHogarSelected.getPregD() != null
                            && controlRecorridoHogarSelected.getPregE() != null && controlRecorridoHogarSelected.getPregF() != null
                            && controlRecorridoHogarSelected.getPregG() != null) {

                if (lnCen01_TotalProductores.getVisibility() == View.VISIBLE) {
                    String numProductores = String.valueOf(etCen01_numProductores.getText());
                    String numExplotaciones = String.valueOf(etCen01_numExplotaciones.getText());
                    int totProductores = 0;
                    int totExplotaciones;

                    if (!numProductores.isEmpty()) {
                        totProductores = Integer.parseInt(numProductores);
                        controlRecorridoHogarSelected.setCantProductoresAgro(totProductores);
                    } else {
                        falta = falta + "Total de productores | ";
                    }

                    if (!numExplotaciones.isEmpty()) {
                        totExplotaciones = Integer.parseInt(numExplotaciones);
                        controlRecorridoHogarSelected.setCantExplotacionesAgro(totExplotaciones);
                    } else {
                        falta = falta + "Total de explotaciones | ";
                    }
                    if (!numProductores.isEmpty() && !numExplotaciones.isEmpty()) {
                        if (Integer.parseInt(numExplotaciones) >= Integer.parseInt(numProductores)) {


                            SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                            Date date = new Date();
                            String fecha = sdf.format(date);

                            for (int x = 1; x <= totProductores; x++) {
                                String numRecorrido = "";
                                if (x < 10)
                                    numRecorrido = "0" + x;
                                controlRecorridoViewModel.addRecorridoProductor(
                                        getNuevoProductor(controlRecorridoHogarSelected, fecha, numRecorrido));
                            }
                            controlRecorridoHogarSelected.setFechaModificacionCR(fecha);
                            controlRecorridoViewModel.updateViviendaRecorrido(controlRecorridoHogarSelected);
                            AppConstants.flagMostrarViviendas = true;
                            controlRecorridoViewModel.updateViviendaRecorrido(controlRecorridoHogarSelected);
                            enviarRecorridoServidor(controlRecorridoHogarSelected, segmentos);
                            flagPreguntas = true;
                        } else {
                            MaterialAlertDialogBuilder materialAlertDialogBuilder = getMaterialAlertDialogBuilder("Error de captura", "El total de explotaciones debe ser mayor o igual al total de productores. No se ha guardado la información de la pregunta 20 y 21.");
                            materialAlertDialogBuilder.show();
                        }
                    }
                }


                if (lnCen01_TotalProductores.getVisibility() == View.GONE && !controlRecorridoHogarSelected.getPregA()
                        && !controlRecorridoHogarSelected.getPregB() && !controlRecorridoHogarSelected.getPregC()
                        && !controlRecorridoHogarSelected.getPregD() && !controlRecorridoHogarSelected.getPregE()
                        && !controlRecorridoHogarSelected.getPregF() && !controlRecorridoHogarSelected.getPregG()) {
                    SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                    Date date = new Date();
                    String fecha = sdf.format(date);
                    controlRecorridoHogarSelected.setFechaModificacionCR(fecha);
                    controlRecorridoViewModel.updateViviendaRecorrido(controlRecorridoHogarSelected);
                    enviarRecorridoServidor(controlRecorridoHogarSelected, segmentos);
                    flagPreguntas = true;
                }
            }/* else if (lnCen01_TotalProductores.getVisibility() == View.GONE) {

            }*/
//            enviarControlRecorrido(controlRecorridoHogarSelected);


            if (!falta.isEmpty())
                mensajeSimpleAlertDialog("Preguntas faltantes", "No se ha ingresado respuesta: " + falta);
        });
        alertDialogBuilder.show();
    }

    @NonNull
    private MaterialAlertDialogBuilder getMaterialAlertDialogBuilder(String titulo, String mensaje) {
        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(activity);
        materialAlertDialogBuilder.setTitle(titulo);
        materialAlertDialogBuilder.setMessage(mensaje);
        materialAlertDialogBuilder.setIcon(R.drawable.ic_dialog_warning);
        materialAlertDialogBuilder.setPositiveButton("OK, lo corregiré.", (dialog, which) -> dialog.dismiss());
        return materialAlertDialogBuilder;
    }

    private Boolean verificarRadioButtonSelected(int checkedRadioButtonId, int rbYes,
                                                 int rbFalse) {
        if (checkedRadioButtonId == rbYes) {
            return true;
        } else if (checkedRadioButtonId == rbFalse) {
            return false;
        } else return null;
    }

    private int verificarRadioButtons_Si(RadioButton rbCen01_Si_preg0708Cultivos,
                                         RadioButton rbCen01_Si_preg0910Frutales,
                                         RadioButton rbCen01_Si_preg1112Cultivos,
                                         RadioButton rbCen01_Si_preg1314Ganado,
                                         RadioButton rbCen01_Si_preg1516Especies,
                                         RadioButton rbCen01_Si_preg1718Pesca,
                                         RadioButton rbCen01_Si_preg1920Empresa) {
        if (rbCen01_Si_preg0708Cultivos.isChecked()
                || rbCen01_Si_preg0910Frutales.isChecked()
                || rbCen01_Si_preg1112Cultivos.isChecked()
                || rbCen01_Si_preg1314Ganado.isChecked()
                || rbCen01_Si_preg1516Especies.isChecked()
                || rbCen01_Si_preg1718Pesca.isChecked()
                || rbCen01_Si_preg1920Empresa.isChecked())
            return View.VISIBLE;
        else return View.GONE;
    }

    private void limpiarRecorrido(ControlRecorrido controlRecorridoLimpiar) {
        MaterialAlertDialogBuilder madConfirmacion =
                new MaterialAlertDialogBuilder(requireActivity());
        madConfirmacion.setTitle("Limpiar recorrido");
        madConfirmacion.setMessage("Desea proceder con la limpieza del recorrido "
                + controlRecorridoLimpiar.getVivienda() + " ? Recuerde haber eliminado explotaciones " +
                "de haber creado.");
        View viewInflated = LayoutInflater.from(requireActivity())
                .inflate(R.layout.alertdialog_confirmacion_descargacuest, null);
        madConfirmacion.setView(viewInflated);
        final EditText editTextConfirmPss = viewInflated.findViewById(R.id.editTextConfirmPss);
        madConfirmacion.setPositiveButton("Aceptar", (dialog, which) -> {
            String password = editTextConfirmPss.getText().toString().trim();
            if (!password.equals(AppConstants.CODIGO_LIMPIAR_RECORRIDO))
                Toast.makeText(requireActivity(), "Contraseña incorrecta ",
                        Toast.LENGTH_SHORT).show();
            else {
//                controlRecorridoLimpiar.setCondicionID(null);
                controlRecorridoLimpiar.setPregA(null);
                controlRecorridoLimpiar.setPregB(null);
                controlRecorridoLimpiar.setPregC(null);
                controlRecorridoLimpiar.setPregD(null);
                controlRecorridoLimpiar.setPregE(null);
                controlRecorridoLimpiar.setPregF(null);
                controlRecorridoLimpiar.setPregG(null);
                controlRecorridoLimpiar.setCantProductoresAgro(null);
                controlRecorridoLimpiar.setCantExplotacionesAgro(null);
                controlRecorridoLimpiar.setObservaciones(null);
                controlRecorridoViewModel.limpiarRecorrido(controlRecorridoLimpiar);
                flagLimpiar = true;
            }
        });
        madConfirmacion.show();
    }

    @Override
    public void onButtonPreguntasClickListener(ControlRecorrido controlRecorridoList, View view, int position) {
        verBateriasPreguntasAD(controlRecorridoList);
    }

    @Override
    public void onButtonCondicionVivClickListener(ControlRecorrido controlRecorridoList, View view, int position) {
        String[] condicionVivienda = activity
                .getResources()
                .getStringArray(R.array.catalogo_condicionvivienda);
        int condicion = (controlRecorridoList.getCondicionID() == null ? 0 : controlRecorridoList.getCondicionID());

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle("Condición de la vivienda");
//            alertDialogBuilder.setMessage("Condicón de la vivienda con ocupantes:");
        alertDialogBuilder.setSingleChoiceItems(condicionVivienda, condicion - 1, null);
        alertDialogBuilder.setPositiveButton("Cambiar", (dialog, i) -> {
            //Si estuvo desocupada o dejó de ser viviendagetCantProductoresAgro==null) y se cambia a presente
            if (controlRecorridoList.getCantProductoresAgro() == null || controlRecorridoList.getCantExplotacionesAgro() == null ||
                    controlRecorridoList.getCantProductoresAgro() == 0 || controlRecorridoList.getCantExplotacionesAgro() == 0) {
                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                if (selectedPosition == 0) {
                    controlRecorridoList.setCondicionID(selectedPosition + 1);
                    controlRecorridoViewModel.actualizarCondicionRecorrido(controlRecorridoList);
                    flagAddRecorrido = true;
                } else if (selectedPosition == 1 || selectedPosition == 2 || selectedPosition == 3) {//DESOCUPADA
                    controlRecorridoList.setPregA(null);
                    controlRecorridoList.setPregB(null);
                    controlRecorridoList.setPregC(null);
                    controlRecorridoList.setPregD(null);
                    controlRecorridoList.setPregE(null);
                    controlRecorridoList.setPregF(null);
                    controlRecorridoList.setPregG(null);
                    controlRecorridoList.setCondicionID(selectedPosition + 1);
                    controlRecorridoViewModel.actualizarCondicionRecorrido(controlRecorridoList);
                    flagAddRecorrido = true;
                } else
                    Toast.makeText(activity, "Debe seleccionar alguna opción.", Toast.LENGTH_SHORT).show();
                //Si estuvo Presente(viviendagetCantProductoresAgro >0) y se cambia a Desocupada o Dejo de ser vivienda primero debe eliminar el cuestionario y cambiar preguntas
            } else if (controlRecorridoList.getCantProductoresAgro() > 0 && controlRecorridoList.getCantExplotacionesAgro() > 0) {
                getMaterialAlertDialogBuilder("Error de cambio.", "Ya se ha creado un recorrido para esta vivienda. Para realizar el cambio debe primero eliminar el cuestionario y cambiar las preguntas 20 y 21 del CEN-01.").show();
            }
        });
        alertDialogBuilder.setNegativeButton("Cancelar", null);
        alertDialogBuilder.show();
    }

    @Override
    public void onButtonObservacionClickListener(ControlRecorrido controlRecorridoListObservacion,
                                                 View view, int position) {
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(activity);
        View v = getLayoutInflater().inflate(R.layout.items_cen01_observacion, null);
        TextInputEditText tietObservacion = v.findViewById(R.id.tietObservacion);
        Button btnCen01_cancelarObservacion = v.findViewById(R.id.btnCen01_cancelarObservacion);
        Button btnCen01_guardarObservacion = v.findViewById(R.id.btnCen01_guardarObservacion);
        alertDialogBuilder.setView(v);
        AlertDialog dialog = alertDialogBuilder.show();
        if (controlRecorridoListObservacion.getObservaciones() != null
                && !controlRecorridoListObservacion.getObservaciones().isEmpty())
            tietObservacion.setText(controlRecorridoListObservacion.getObservaciones());
        btnCen01_cancelarObservacion.setOnClickListener(view1 -> dialog.dismiss());

        btnCen01_guardarObservacion.setOnClickListener(view12 -> {
            if (tietObservacion.getText() != null && !String.valueOf(tietObservacion.getText()).isEmpty()) {
                String observacion = String.valueOf(tietObservacion.getText());

                if (!observacion.equals(controlRecorridoListObservacion.getObservaciones())) {
                    controlRecorridoListObservacion.setObservaciones(String.valueOf(tietObservacion.getText()));
                    controlRecorridoViewModel.actualizarObservacionRecorrido(
                            controlRecorridoListObservacion.getLlaveRecorrido(), observacion);
                    Toast.makeText(activity, "Observación guardada.", Toast.LENGTH_SHORT).show();

                }
                dialog.dismiss();
            } else
                Toast.makeText(activity, "Observación no guardada.", Toast.LENGTH_SHORT).show();
        });
        dialog.show();
    }

    @Override
    public void onButtonEliminarClickListener(ControlRecorrido controlButton, View view, int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle("Opciones para recorrido " + controlButton.getVivienda());
        CharSequence[] opcionesRecorrido = {"Enviar al servidor", "Limpiar recorrido", "Eliminar recorrido"};

        alertDialogBuilder.setSingleChoiceItems(opcionesRecorrido, 0, null);
        alertDialogBuilder.setPositiveButton("Aceptar", (dialog, which) -> {
            int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
            if (opcionesRecorrido[selectedPosition] == "Enviar al servidor") {
                enviarRecorridoServidor(controlButton, segmentos);
            } else if (opcionesRecorrido[selectedPosition] == "Limpiar recorrido") {
                limpiarRecorrido(controlButton);
            } else if (opcionesRecorrido[selectedPosition] == "Eliminar recorrido") {
                eliminarRecorrido(controlButton);
            } else {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.show();
    }


    @Override
    public void onRGCondicionViviendaClickListener(List<ControlRecorrido> controlRecorridoViviendaList,
                                                   int absoluteAdapterPosition, int checkedId) {
        //        String[] condicionVivienda = activity.getResources().getStringArray(R.array.catalogo_condicionvivienda);

//        RadioButton radioButton = rgCen91_condicionviv.findViewById(checkedRadioButtonId);
//        String condicion = String.valueOf(radioButton.getId());

        ControlRecorrido crActualizarCond = controlRecorridoViviendaList.get(absoluteAdapterPosition);
        if (crActualizarCond.getCondicionID() != null) {
            int condicionBD = crActualizarCond.getCondicionID();
            int nuevaCondicion = 0;
         /*   if (checkedId == R.id.rbCen01_OcupantesPresentes) {

                nuevaCondicion = 1;
            } else if (checkedId == R.id.rbCen01_OcupantesAusentes) {
                nuevaCondicion = 2;
            } else if (checkedId == R.id.rbCen01_Desocupadas) {
                nuevaCondicion = 3;
            } else if (checkedId == R.id.rbCen01_DejoDeSer) {
                nuevaCondicion = 4;
            }*/
            if (condicionBD != nuevaCondicion) {
                crActualizarCond.setCondicionID(nuevaCondicion);
                if (nuevaCondicion >= 2) {
                    crActualizarCond.setPregA(null);
                    crActualizarCond.setPregB(null);
                    crActualizarCond.setPregC(null);
                    crActualizarCond.setPregD(null);
                    crActualizarCond.setPregE(null);
                    crActualizarCond.setPregF(null);
                    crActualizarCond.setPregG(null);
                    crActualizarCond.setCantExplotacionesAgro(null);
                    crActualizarCond.setCantProductoresAgro(null);
                }
                controlRecorridoViewModel.updateCondicionViviendaRecorrido(crActualizarCond);
//                controlRecorridoAdapter.notifyItemChanged(absoluteAdapterPosition);
//                controlRecorridoAdapter.setData(controlRecorridoViviendaList);
                flagCondicion = true;
            }
        }
    }

    private void eliminarRecorrido(ControlRecorrido controlButton) {
        flagEliminar = true;
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(activity);
        alertDialogBuilder.setTitle("Eliminado de recorrido");
        alertDialogBuilder.setMessage("Desea proceder a eliminar el recorrido número : "
                + controlButton.getVivienda());
        alertDialogBuilder.setPositiveButton("Eliminar", (dialogInterface, i) ->
                controlRecorridoViewModel.getCuestionariosByRecorrido(controlButton.getLlaveRecorrido())
                        .observe(activity, cuestionarios -> {
                            if (flagEliminar) {
                                if ((controlButton.getCantExplotacionesAgro() == null || controlButton.getCantExplotacionesAgro() == 0)
                                        && cuestionarios.isEmpty()) {
                                    ProcessNotifier processNotifier = new ProcessNotifier(activity);
                                    controlRecorridoViewModel.eliminarRecorrido(controlButton, processNotifier);
                                    flagEliminar = true;
                                } else {
                                    mensajeSimpleAlertDialog("Eliminado de recorrido",
                                            getResources().getString(R.string.verificacionRecorrido));
                                }
                            }
                        }));

        alertDialogBuilder.setNegativeButton("Cancelar", null);
        alertDialogBuilder.show();
    }

    private void mensajeSimpleAlertDialog(String titulo, String mensaje) {
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(activity);
        alertDialogBuilder.setTitle(titulo);
        alertDialogBuilder.setMessage(mensaje);
        alertDialogBuilder.setPositiveButton("Ok, lo corregiré :)", null);
        alertDialogBuilder.show();
    }
}
