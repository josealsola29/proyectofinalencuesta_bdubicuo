package com.example.proyectofinalencuesta_bdubicuo.ui.menu3_senddata;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.proyectofinalencuesta_bdubicuo.R;
import com.example.proyectofinalencuesta_bdubicuo.data.local.constants.AppConstants;
import com.example.proyectofinalencuesta_bdubicuo.data.local.constants.SharedPreferencesManager;
import com.example.proyectofinalencuesta_bdubicuo.ui.menu2_survey.capture.SurveyFragmentStateAdapter;
import com.example.proyectofinalencuesta_bdubicuo.ui.menu3_senddata.send_cuestionarios.SendDataFragment;
import com.example.proyectofinalencuesta_bdubicuo.ui.menu3_senddata.send_recorridos.SendDataRecorrido;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Objects;

public class EnvioDeDatosFragment extends Fragment {

    private ViewPager2 viewPager2;
    private TabLayout tabLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        SurveyViewModel surveyViewModel = new ViewModelProvider(this).get(SurveyViewModel.class);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar())
                .setTitle(getText(R.string.title_sendData) + " // " +
                        SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_USERNAME));
        //        View root = inflater.inflate(R.layout.fragment_survey, container, false);

        return inflater.inflate(R.layout.fragment_survey, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tabLayout = view.findViewById(R.id.tlHostSurveys);
        viewPager2 = view.findViewById(R.id.vpHostSurveys);
        setUpViewPaper();
        super.onViewCreated(view, savedInstanceState);
    }

    private ArrayList<Fragment> agregarFragment() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new SendDataRecorrido());
        fragments.add(new SendDataFragment());
//        fragments.add(new SendDataOtrasEstructuras());
        return fragments;
    }

    private void setUpViewPaper() {
        viewPager2.setAdapter(new SurveyFragmentStateAdapter(requireActivity(), agregarFragment()));
        new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> {
                    if (position == 0)
                        tab.setText("Recorridos");
                    else if (position == 1)
                        tab.setText("Cuestionarios");
                }).attach();
    }
}
