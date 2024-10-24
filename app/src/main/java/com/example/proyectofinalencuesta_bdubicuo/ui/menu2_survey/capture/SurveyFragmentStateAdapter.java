package com.example.proyectofinalencuesta_bdubicuo.ui.menu2_survey.capture;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class SurveyFragmentStateAdapter extends FragmentStateAdapter {

    private final ArrayList<Fragment> fragments;

    public SurveyFragmentStateAdapter(@NonNull FragmentActivity fragmentActivity,
                                      ArrayList<Fragment> fragments) {
        super(fragmentActivity);
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
}
