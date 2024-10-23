package com.example.proyectofinalencuesta_bdubicuo.ui.menu2_survey.capture.cen01_viviendas.viviendas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinalencuesta_bdubicuo.R;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.ControlRecorrido;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class HogaresAdapter extends RecyclerView.Adapter<HogaresAdapter.ViewHolder> {
    private List<ControlRecorrido> controlRecorridoList;
    private final OnHogarListener onHogarListener;

    public HogaresAdapter(List<ControlRecorrido> viviendas, OnHogarListener onHogarListener) {
        this.controlRecorridoList = viviendas;
        this.onHogarListener = onHogarListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_hogares, parent, false);
        return new ViewHolder(view, onHogarListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (controlRecorridoList != null) {
            ControlRecorrido viviendasHogares = controlRecorridoList.get(position + 1);

//            holder.tvHSegmentoInfo.setText(String.format("VIV %s - HOG %s",
//                    viviendasHogares.getVivienda(), viviendasHogares.getHogar()));
//            holder.tvHDivisonInfo.setText(String.format("HOG.: %s", cen01_viviendas.getHogar()));
        }
    }

    @Override
    public int getItemCount() {
        if (controlRecorridoList != null && controlRecorridoList.size() > 1)
            return controlRecorridoList.size() - 1;
        else return 0;
    }

    public void setData(List<ControlRecorrido> viviendas) {
        this.controlRecorridoList = viviendas;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;

        private final TextView tvHSegmentoInfo;
//        private final View vHogarInconsistencia;
        //        private final TextView tvHDivisonInfo;
//        private final TextView tvHViviendaInfo;
//        private final TextView tvHHogarInfo;

        final OnHogarListener onHogarListener;

        public ViewHolder(@NonNull View itemView, OnHogarListener onHogarListener) {
            super(itemView);
            mView = itemView;

            tvHSegmentoInfo = mView.findViewById(R.id.tvHSegmentoInfo);
//            vHogarInconsistencia = mView.findViewById(R.id.vHogarInconsistencia);
//            tvHDivisonInfo = mView.findViewById(R.id.tvHDivisonInfo);
//            tvHViviendaInfo = mView.findViewById(R.id.tvHViviendaInfo);
//            tvHHogarInfo = mView.findViewById(R.id.tvHHogarInfo);

            MaterialCardView cvHogar = mView.findViewById(R.id.cvHogar);

            cvHogar.setOnClickListener(this);
            this.onHogarListener = onHogarListener;
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.cvHogar)
                onHogarListener.onHogarClick(controlRecorridoList.get(getBindingAdapterPosition() + 1),
                        v, getBindingAdapterPosition() + 1);
        }
    }

    public interface OnHogarListener {
        void onHogarClick(ControlRecorrido position, View view, int absoluteAdapterPosition);
    }
}
