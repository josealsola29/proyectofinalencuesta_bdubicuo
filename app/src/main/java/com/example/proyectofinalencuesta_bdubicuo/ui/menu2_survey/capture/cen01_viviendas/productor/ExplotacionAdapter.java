package com.example.proyectofinalencuesta_bdubicuo.ui.menu2_survey.capture.cen01_viviendas.productor;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinalencuesta_bdubicuo.R;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Cuestionarios;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class ExplotacionAdapter extends RecyclerView.Adapter<ExplotacionAdapter.ViewHolder> {

    private List<Cuestionarios> cuestionarioList;
    private final OnExplotacionListener onExplotacionListener;

    public ExplotacionAdapter(List<Cuestionarios> cuestionarios, OnExplotacionListener onExplotacionListener) {
        this.cuestionarioList = cuestionarios;
        this.onExplotacionListener = onExplotacionListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_explotaciones, parent, false);
        return new ViewHolder(view, onExplotacionListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (cuestionarioList != null) {
            Cuestionarios cuestionarios = cuestionarioList.get(position + 1);
            if (cuestionarios.getEstado() == 0)
                holder.cvExplotacion.setCardBackgroundColor(Color.rgb(255, 160, 122));
            if (cuestionarios.getEstado() == 1)
                holder.cvExplotacion.setCardBackgroundColor(Color.rgb(255, 255, 155));
            if (cuestionarios.getEstado() == 2)
                holder.cvExplotacion.setCardBackgroundColor(Color.rgb(169, 208, 142));
            if (cuestionarios.getEstado() == 3)
                holder.cvExplotacion.setCardBackgroundColor(Color.rgb(189, 215, 238));

            holder.tvExplotacionInfo.setText(String.format("PROD. %s | EXPL. %s",
                    cuestionarios.getProductor(), cuestionarios.getExplotacionNum()));
            holder.tvExplotacionJefeInfo.setText(String.format("%s",
                    cuestionarios.getJefe() == null ? "":cuestionarios.getJefe()));
        }
    }

    @Override
    public int getItemCount() {
        if (cuestionarioList != null && cuestionarioList.size() > 1)
            return cuestionarioList.size() - 1;
        else return 0;
    }

    public void setData(List<Cuestionarios> cuestionariosList) {
        this.cuestionarioList = cuestionariosList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;

        private final TextView tvExplotacionInfo;
//        private final View vExplotacionInconsistencia;
        //        private final TextView tvHDivisonInfo;
        private final TextView tvExplotacionJefeInfo;
//        private final TextView tvHHogarInfo;

        private final MaterialCardView cvExplotacion;

        final OnExplotacionListener onExplotacionListener;

        public ViewHolder(@NonNull View itemView, OnExplotacionListener onExplotacionListener) {
            super(itemView);
            mView = itemView;

            tvExplotacionInfo = mView.findViewById(R.id.tvExplotacionInfo);
//            vExplotacionInconsistencia = mView.findViewById(R.id.vHogarInconsistencia);
//            tvHDivisonInfo = mView.findViewById(R.id.tvHDivisonInfo);
            tvExplotacionJefeInfo = mView.findViewById(R.id.tvExplotacionJefeInfo);
//            tvHHogarInfo = mView.findViewById(R.id.tvHHogarInfo);

            cvExplotacion = mView.findViewById(R.id.cvExplotacion);

            cvExplotacion.setOnClickListener(this);
            this.onExplotacionListener = onExplotacionListener;
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.cvExplotacion)
                onExplotacionListener.onExplotacionClick(cuestionarioList,
                        v, getBindingAdapterPosition() + 1);
        }
    }

    public interface OnExplotacionListener {
        void onExplotacionClick(List<Cuestionarios> position, View view, int absoluteAdapterPosition);
    }
}
