package com.example.proyectofinalencuesta_bdubicuo.ui.menu2_survey.capture;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinalencuesta_bdubicuo.R;
import com.example.proyectofinalencuesta_bdubicuo.data.local.constants.AppConstants;
import com.example.proyectofinalencuesta_bdubicuo.data.local.constants.SharedPreferencesManager;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Segmentos;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.TotCuestionarios;
import com.example.proyectofinalencuesta_bdubicuo.ui.menu2_survey.capture.cen01_viviendas.ViviendaFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CaptureFragment extends Fragment
        implements CaptureRecyclerViewAdapter.OnSegmentoCardListener {
    private static final String TAG = "CaptureFragment";
    private FragmentManager fragmentManager;
    private TextView tvlocalizacion;
    private List<Segmentos> segmentosList;
    //    private List<Cuestionarios> cuestionariosList;
    private List<TotCuestionarios> totCuestionariosList;
    private CaptureViewModel captureViewModel;
    private CaptureRecyclerViewAdapter captureRecyclerViewAdapter;
    private List<String> subZonasListSpinner;
    private ArrayAdapter<String> arrayAdapter;
    private boolean flagInitFragment = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_capture_list, container, false);
        captureViewModel = new ViewModelProvider(this).get(CaptureViewModel.class);
        fragmentManager = requireActivity().getSupportFragmentManager();
        subZonasListSpinner = new ArrayList<>();
//        cuestionariosList = new ArrayList<>();
        totCuestionariosList = new ArrayList<>();

        tvlocalizacion = view.findViewById(R.id.tvlocalizacion);

        Spinner spinnerSubzona = view.findViewById(R.id.spinnerSubzonaCuestionario);

        arrayAdapter = new ArrayAdapter<>(
                requireActivity(),
                R.layout.support_simple_spinner_dropdown_item,
                subZonasListSpinner
        );

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubzona.setAdapter(arrayAdapter);

        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewSegmentos);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(requireActivity(),
                LinearLayoutManager.HORIZONTAL));
        captureRecyclerViewAdapter = new CaptureRecyclerViewAdapter(segmentosList, totCuestionariosList,
                this);
        recyclerView.setAdapter(captureRecyclerViewAdapter);

        spinnerSubzona.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String subZonaSelect = subZonasListSpinner.get(position).substring(8);
                SharedPreferencesManager.setSomeBooleanValue(AppConstants.ESTADOS_STATUS, true);
                actualizarSegmentos(subZonaSelect);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i(TAG, "onNothingSelected: ");
            }
        });
        actualizarSubZona();
        return view;
    }

    private void actualizarSubZona() {
        captureViewModel.getAllSubZonas().observe(CaptureFragment.this.getViewLifecycleOwner(), segmentos -> {
            subZonasListSpinner.clear();
            if (segmentos != null && segmentos.size() != subZonasListSpinner.size()) {
                for (Segmentos segmentos1 : segmentos) {
                    subZonasListSpinner.add("Subzona " + segmentos1.getSubZonaID());
                }
                arrayAdapter.notifyDataSetChanged();
            }
            captureViewModel.getAllSubZonas().removeObservers(CaptureFragment.this.getViewLifecycleOwner());
        });
    }

    private void actualizarSegmentos(String subZonaSelect) {
        captureViewModel.getSegmentosSelected(subZonaSelect,
                        SharedPreferencesManager.getSomeIntValue(AppConstants.PREF_ID))
                .observe(getViewLifecycleOwner(), segmentos -> {
                    if (!segmentos.isEmpty()) {
                        flagInitFragment = true;
                        segmentosList = segmentos;
                        if (SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_CODIGO).startsWith("00", 1)) {
//                            tvlocalizacion.setVisibility(View.INVISIBLE);
                            tvlocalizacion.setText(String.format("Centro de digitación: %s ",
                                    SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_REGION).substring(2)));

                        } else if (SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_CODIGO).startsWith("C")) {
                            tvlocalizacion.setText(String.format("Región: %s ",
                                    segmentosList.get(0).getRegionID()));
                        } else {
                            tvlocalizacion.setText(String.format("Región: %s - Zona: %s",
                                    segmentosList.get(0).getRegionID(), segmentosList.get(0).getZonaID()));

                        }

                        if (Objects.equals(segmentos.get(0).getSubZonaID(), subZonaSelect)) {
                            captureRecyclerViewAdapter.setData(segmentosList, totCuestionariosList);
                            captureViewModel.getSegmentosSelected(
                                            subZonaSelect,
                                            SharedPreferencesManager.getSomeIntValue(AppConstants.PREF_ID))
                                    .removeObservers(getViewLifecycleOwner());
                        }
                    }
                });

        captureViewModel.getCuestionarios().observe(getViewLifecycleOwner(), totCuestionarios -> {
            if (totCuestionarios != null) {
                totCuestionariosList = totCuestionarios;
                captureRecyclerViewAdapter.setData(segmentosList, totCuestionariosList);
            }
        });
    }

    @Override
    public void onSegmentoCardClick(int position) {
        if (segmentosList.get(position).getEstado().equals("3") || segmentosList.get(position).getEstado().equals("4")) {
            Toast.makeText(requireActivity(), "Segmento cerrado.", Toast.LENGTH_SHORT).show();
        } else {
            DialogFragment newFragment = new ViviendaFragment(requireActivity(),
                    segmentosList.get(position));
            newFragment.show(fragmentManager, "ViviendaHogarDialogFragment");
        }
    }

    @Override
    public void onSegmentoCardLongClick(int position, Segmentos segmentos) {
        MaterialAlertDialogBuilder madConfirmacion =
                new MaterialAlertDialogBuilder(requireActivity());
        if (segmentos.getTipoEmp() == 1) {//REGULAR{
            madConfirmacion.setTitle("Descripción del segmento");
            madConfirmacion.setMessage(segmentosList.get(position).getDetalle());
        } else {
            View viewInflated = LayoutInflater.from(requireActivity())
                    .inflate(R.layout.alertdialog_listado_focalizados, null);
            RecyclerView recyclerViewFocalizados = viewInflated.findViewById(R.id.rvListadoFocalizados);
            FocalizadasAdapter focalizadasAdapter = new FocalizadasAdapter(segmentosList, segmentosList.get(position).getViviendas());
            recyclerViewFocalizados.setLayoutManager(new LinearLayoutManager(requireActivity()));
            recyclerViewFocalizados.setAdapter(focalizadasAdapter);
            madConfirmacion.setView(viewInflated);
        }
        madConfirmacion.setPositiveButton("Ok", (dialog, which) -> {
        });
        madConfirmacion.show();
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (menuVisible && flagInitFragment) {
            actualizarSubZona();
        }
    }
}
