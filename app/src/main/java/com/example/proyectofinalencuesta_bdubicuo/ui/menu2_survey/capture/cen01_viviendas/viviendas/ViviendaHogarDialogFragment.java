package com.example.proyectofinalencuesta_bdubicuo.ui.menu2_survey.capture.cen01_viviendas.viviendas;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinalencuesta_bdubicuo.R;
import com.example.proyectofinalencuesta_bdubicuo.data.local.constants.AppConstants;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.ControlRecorrido;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Cuestionarios;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Segmentos;
import com.example.proyectofinalencuesta_bdubicuo.ui.menu2_survey.capture.cen01_viviendas.productor.ProductorExplotacionDialogFragment;
import com.example.proyectofinalencuesta_bdubicuo.utils.ProcessNotifier;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class ViviendaHogarDialogFragment extends Fragment
        implements ViviendaHogarDialogAdapter.OnViviendaHogarListener {

    private static final String TAG = "ViviendaHogarDialogFrag";
    private final Segmentos segmentos;
    private final FragmentActivity activity;
    private List<ControlRecorrido> viviendasHogaresGroupByVivienda;
    //    private List<ControlRecorrido> hogarList;
    private List<ControlRecorrido> viviendasHogaresList;
    private List<Cuestionarios> cuestionariosPorLLave;
    private ControlRecorrido viviendasHogaresSelected;
    private ViviendaHogarDialogAdapter viviendaHogarDialogAdapter;
    private ViviendaHogarViewModel viviendaHogarViewModel;
    private ProcessNotifier processNotifier;
    private boolean flagHogaresObserver;
    private boolean flagEliminarObserver;
    private FragmentManager fragmentManager;

    public ViviendaHogarDialogFragment(FragmentActivity activity, Segmentos segmentos) {
        this.segmentos = segmentos;
        this.activity = activity;
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentManager = activity.getSupportFragmentManager();

        flagHogaresObserver = true;
//        flagEliminarObserver = true;
        flagEliminarObserver = false;
        processNotifier = new ProcessNotifier(activity);

        // Inflate the layout to use as dialog or embedded fragment
        View view = inflater.inflate(R.layout.dialogfragment_viviendas, container, false);
        viviendaHogarViewModel = new ViewModelProvider(this).get(ViviendaHogarViewModel.class);

        TextView tvTituloRegionVivienda = view.findViewById(R.id.tvTituloRegionVivienda);
        TextView tvTituloZonaVivienda = view.findViewById(R.id.tvTituloZonaVivienda);
        TextView tvTituloSubzonaVivienda = view.findViewById(R.id.tvTituloSubzonaVivienda);
        TextView tvTituloSegmentoViviendaHogar = view.findViewById(R.id.tvTituloSegmentoViviendaHogar);

        tvTituloRegionVivienda.setText(String.format("Region: %s", segmentos.getRegionID()));
        tvTituloZonaVivienda.setText(String.format("Zona: %s", segmentos.getZonaID()));
        tvTituloSubzonaVivienda.setText(String.format("SubZona: %s", segmentos.getSubZonaID()));
        tvTituloSegmentoViviendaHogar.setText(String.format("Segmento: %s - %s - %s",
                segmentos.getId().substring(0, 6),
                segmentos.getId().substring(6, 10),
                segmentos.getId().substring(10, 12)));

//        Button btnAgregarVivienda = view.findViewById(R.id.btnControlRecorridoCen01);
//        ImageView ivClose = view.findViewById(R.id.ivCloseViviendasHogar);

//        btnAgregarVivienda.setOnClickListener(this);
//        ivClose.setOnClickListener(v -> dismiss());

        viviendasHogaresGroupByVivienda = new ArrayList<>();
        viviendasHogaresList = new ArrayList<>();
        cuestionariosPorLLave = new ArrayList<>();

        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.rvViviendasHogar);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        //ADAPTADOR VIVIENDAS
        viviendaHogarDialogAdapter = new ViviendaHogarDialogAdapter(
                viviendasHogaresList, this, cuestionariosPorLLave);
        recyclerView.setAdapter(viviendaHogarDialogAdapter);

        setData();
        return view;
    }

    private void setData() {
        viviendaHogarViewModel.getRecorridosPorSegmentoOP(segmentos.getId()).observe(
                activity, viviendas -> {
                    if (viviendas != null) {
                        viviendasHogaresGroupByVivienda = viviendas;
                        if (flagHogaresObserver || AppConstants.flagMostrarViviendas)
                            getHogaresObserve();
                        if (!flagEliminarObserver)
                            flagEliminarObserver = true;
                    }
                });
    }

    private void getHogaresObserve() {
        try {
            viviendaHogarViewModel.getViviendasBySegmento(segmentos.getId())
                    .observe(activity, hogares -> {
                        if (hogares != null && viviendasHogaresGroupByVivienda != null) {
//                            viviendaList = hogares;
                            if (flagEliminarObserver) {
                                flagHogaresObserver = false;
                                AppConstants.flagMostrarViviendas = false;
                                List<String> llaveRecorrido = new ArrayList<>();
                                for (int x = 0; x < hogares.size(); x++) {
                                    llaveRecorrido.add(hogares.get(x).getLlaveRecorrido());
                                }
                                viviendasHogaresList = hogares;
                                viviendaHogarViewModel.getTotalCuestionariosCompletadosPendientes(llaveRecorrido).observe(activity, new Observer<List<Cuestionarios>>() {
                                    @Override
                                    public void onChanged(List<Cuestionarios> cuestionarios) {
                                        cuestionariosPorLLave = cuestionarios;
                                        viviendaHogarDialogAdapter.setData(viviendasHogaresList, cuestionariosPorLLave);

                                    }
                                });
//                                if (viviendasHogaresList.size() < 1)//TODO KHA
//                                    viviendaHogarViewModel.updateEstadoSegmento(segmentos.getId());
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "getHogaresObserve: ");
        }
    }

/*    @Override
    public void onClick(View v) {
        int opcion = v.getId();
        if (opcion == R.id.btnControlRecorridoCen01) {
            DialogFragment newFragment = new ControlRecorridoFragment(requireActivity(),
                    segmentos);
            newFragment.show(fragmentManager, "ViviendaHogarDialogFragment");

           *//* String numVivienda;
            SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
            Date date = new Date();
            String fecha = sdf.format(date);
            int numViviendaSelected;

            if (viviendasHogaresList.size() == 0) {
                numVivienda = "01";
            } else {
                numViviendaSelected = Integer.parseInt(viviendasHogaresList.get(
                        viviendasHogaresList.size() - 1).get(0).getVivienda());
                if (numViviendaSelected < 9)
                    numVivienda = "0" + (Integer.parseInt(viviendasHogaresList.get(
                            viviendasHogaresList.size() - 1).get(0).getVivienda()) + 1);
                else
                    numVivienda = String.valueOf(Integer.parseInt(viviendasHogaresList.get(
                            viviendasHogaresList.size() - 1).get(0).getVivienda()) + 1);
            }

            if (viviendasHogaresList.size() > 0) {
                if (viviendasHogaresList.get(viviendasHogaresList.size() - 1)
                        .get(0).getEstadoVivienda() >= 1) {
                    viviendaHogarViewModel.addVivienda(getNuevaVivienda(numVivienda));
                    viviendaHogarDialogAdapter.notifyDataSetChanged();
                } else {
                    Utilidad.showMessageDialog(
                            "Creación de vivienda",
                            "Ya tiene una vivienda creada sin capturar.",
                            true, activity, R.raw.error_sign);
                }
            } else {
                viviendaHogarViewModel.addVivienda(getNuevaVivienda(numVivienda));
//                viviendaHogarDialogAdapter.notifyDataSetChanged();
            }*//*
        }
    }*/

    @Override
    public void onViviendaHogarClick(ControlRecorrido viviendasHogaresSelected, View v) {
        PopupMenu popup = new PopupMenu(activity, v);

        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(item -> {
            this.viviendasHogaresSelected = viviendasHogaresSelected;
            if (item.getItemId() == R.id.popup_revisarproductor) {
                DialogFragment newFragment = new ProductorExplotacionDialogFragment(requireActivity(),
                        segmentos, this.viviendasHogaresSelected);
                newFragment.show(fragmentManager, "ViviendaHogarDialogFragment");

                return true;
            } /*else if (item.getItemId() == R.id.popup_eliminarcuestionario) {
                Toast.makeText(activity, "En revisón >:)", Toast.LENGTH_SHORT).show();*/
            //TODO VERIFICAR QUE NO EXISTA PRODUCTOR EXPLOTACION
//                if (this.viviendasHogaresSelected.getEstadoVivienda() == 0) {
//                    flagHogaresObserver = true;
//                    showDeleteDialog(this.viviendasHogaresSelected);
//                } else if (this.viviendasHogaresSelected.getEstadoVivienda() > 0) {
//                    flagHogaresObserver = true;
//                    showConfirmDeleteCuestionarioDialog(this.viviendasHogaresSelected);
//                } else
//                    Toast.makeText(activity, "Eliminación no autorizada, el cuestionario tiene datos.",
//                            Toast.LENGTH_SHORT).show();
/*                 return true;
            }else if (item.getItemId() == R.id.popup_editarnhogares) {
                Toast.makeText(activity, "Mostrar AlertDialog", Toast.LENGTH_SHORT).show();
                return true;
            } */
            else
                return false;
        });
        popup.inflate(R.menu.menu_popup_viviendahogar);
        popup.show();
    }

    @Override
    public void onHogarClick(ControlRecorrido viviendasHogares, View v, int position) {
        PopupMenu popup = new PopupMenu(activity, v);

        popup.setOnMenuItemClickListener(item -> {
            viviendasHogaresSelected = viviendasHogares;
            if (item.getItemId() == R.id.popup_revisarproductor) {
                DialogFragment newFragment = new ProductorExplotacionDialogFragment(requireActivity(),
                        segmentos, this.viviendasHogaresSelected);
                newFragment.show(fragmentManager, "ViviendaHogarDialogFragment");
                Log.d(TAG, "onViviendaHogarClick: revise el cuestionario.");
                return true;
            } /*else if (item.getItemId() == R.id.popup_eliminarcuestionario) {
                if (viviendasHogaresSelected.getEstadoVivienda() == 0)
                    showDeleteDialog(viviendasHogaresSelected);
                else if (this.viviendasHogaresSelected.getEstadoVivienda() > 0) {
                    flagHogaresObserver = true;
                    showConfirmDeleteCuestionarioDialog(this.viviendasHogaresSelected);
                } else
                    Toast.makeText(activity, "Eliminación no autorizada, el cuestionario tiene datos.",
                            Toast.LENGTH_SHORT).show();
                return true;
            }*/ else
                return false;
        });
        popup.inflate(R.menu.menu_popup_viviendahogar);
        popup.show();
    }

//    private ControlRecorrido getNuevaVivienda(String numVivienda) {
//        return new ControlRecorrido(
//                segmentos.getId() + segmentos.getEmpadronadorID() + numVivienda + "1",
//                segmentos.getId(),
//                segmentos.getProvinciaID(),
//                segmentos.getDistritoID(),
//                segmentos.getCorregimientoID(),
//                segmentos.getSubZonaID(),
//                segmentos.getSegmentoID(),
//                segmentos.getDivisionID(),
//                0,
//                "no",
//                SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_CODIGO),
//                numVivienda,
//                "1",
//                false,
//                false,
//                false,
//                false,
//                false,
//                false,
//                false,
//                0);
//    }

    private boolean verificarRed() {
        ConnectivityManager connectivityManager = (ConnectivityManager) requireActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = connectivityManager.getActiveNetwork();
        if (network == null) return false;
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
        return networkCapabilities != null
                && (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
    }

    private void showConfirmDeleteCuestionarioDialog(ControlRecorrido viviendasHogaresSelected) {
        if (verificarRed()) {
            MaterialAlertDialogBuilder madConfirmacion =
                    new MaterialAlertDialogBuilder(requireActivity());
            madConfirmacion.setTitle("Eliminación de cuestionario");
            madConfirmacion.setMessage("¿Desea eliminar un cuestionario con datos?\n" +
                    "Vivienda: " + viviendasHogaresSelected.getVivienda() /*+ "  " +
                    "Hogar: " + viviendasHogaresSelected.getHogar()*/);
            View viewInflated = getLayoutInflater()
                    .inflate(R.layout.alertdialog_confirmacion_descargacuest, null);
            madConfirmacion.setView(viewInflated);
            final EditText editTextConfirmPss = viewInflated.findViewById(R.id.editTextConfirmPss);
            madConfirmacion.setPositiveButton("Aceptar", (dialog, which) -> {
                String password = editTextConfirmPss.getText().toString().trim();
                if (!password.equals(AppConstants.CODIGO_ELIMINAR_CUESTIONARIO))
                    Toast.makeText(requireActivity(), "Contraseña incorrecta ",
                            Toast.LENGTH_SHORT).show();
                else {
                    flagEliminarObserver = false;
                    processNotifier = new ProcessNotifier(activity);
                    viviendaHogarViewModel.deleteVivienda("remoto", processNotifier, segmentos,
                            viviendasHogaresSelected, activity);
                }
            });
            madConfirmacion.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
            madConfirmacion.show();
        } else
            Toast.makeText(activity, "Debe estar conectado a una red.", Toast.LENGTH_SHORT).show();
    }

    private void showDeleteDialog(ControlRecorrido viviendasHogaresSelected) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.popup_eliminarcuestionario);
        builder.setMessage("¿Desea eliminar este cuestionario?");
        builder.setPositiveButton("Aceptar", (dialog, which) -> {
            flagEliminarObserver = false;
            processNotifier = new ProcessNotifier(activity);
            viviendaHogarViewModel.deleteVivienda("local", processNotifier, segmentos,
                    viviendasHogaresSelected, activity);
        });
        builder.setNegativeButton("Cancelar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
