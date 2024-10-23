package com.example.proyectofinalencuesta_bdubicuo.ui.menu2_survey.capture.cen01_viviendas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.proyectofinalencuesta_bdubicuo.R;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Segmentos;
import com.example.proyectofinalencuesta_bdubicuo.ui.menu2_survey.capture.SurveyFragmentStateAdapter;
import com.example.proyectofinalencuesta_bdubicuo.ui.menu2_survey.capture.cen01_viviendas.cen01.ControlRecorridoFragment;
import com.example.proyectofinalencuesta_bdubicuo.ui.menu2_survey.capture.cen01_viviendas.viviendas.ViviendaHogarDialogFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class ViviendaFragment extends DialogFragment {

    private ViewPager2 vpHostVivienda;
    private TabLayout tlHostVivienda;
    private FragmentActivity activity;
    private Segmentos segmentos;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    public ViviendaFragment() {
        // Required empty public constructor
    }

    public ViviendaFragment(FragmentActivity requireActivity, Segmentos segmentos) {
        this.activity = requireActivity;
        this.segmentos = segmentos;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vivienda, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tlHostVivienda = view.findViewById(R.id.tlHostVivienda);
        vpHostVivienda = view.findViewById(R.id.vpHostVivienda);
        setUpViewPaper();
        super.onViewCreated(view, savedInstanceState);
    }

    private ArrayList<Fragment> agregarFragment() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new ControlRecorridoFragment(activity, segmentos));
        fragments.add(new ViviendaHogarDialogFragment(activity, segmentos));
        return fragments;
    }

    private void setUpViewPaper() {
        vpHostVivienda.setAdapter(new SurveyFragmentStateAdapter(activity, agregarFragment()));
        new TabLayoutMediator(tlHostVivienda, vpHostVivienda,
                (tab, position) -> {
                    if (position == 0)
                        tab.setText("CEN-01");
                    else if (position == 1)
                        tab.setText("Viviendas");
                }).attach();
    }
}
