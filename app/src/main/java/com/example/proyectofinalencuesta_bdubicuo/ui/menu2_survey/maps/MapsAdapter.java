package com.example.proyectofinalencuesta_bdubicuo.ui.menu2_survey.maps;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinalencuesta_bdubicuo.R;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Segmentos;

import java.util.List;

public class MapsAdapter extends RecyclerView.Adapter<MapsAdapter.ViewHolder> {
    private final OnMapsSelectListener onMapsSelectListener;
    private List<Segmentos> segmentosMapsList;

    public MapsAdapter(List<Segmentos> segmentosMapsList, OnMapsSelectListener onMapsSelectListener) {
        this.segmentosMapsList = segmentosMapsList;
        this.onMapsSelectListener = onMapsSelectListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_maps, parent, false);
        return new ViewHolder(view, onMapsSelectListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MapsAdapter.ViewHolder holder, int position) {
        if (segmentosMapsList != null) {
            Segmentos segmentos = segmentosMapsList.get(position);
            holder.tvMapsInfo.setText(String.format("Segmento: %s - %s - %s",
                    segmentos.getId().substring(0, 6),
                    segmentos.getId().substring(6, 10),
                    segmentos.getId().substring(10, 12)));
        }
    }

    @Override
    public int getItemCount() {
        if (segmentosMapsList != null)
            return segmentosMapsList.size();
        else return 0;
    }

    public void setData(List<Segmentos> segmentosMapsList) {
        if (segmentosMapsList != null) {
            if (!segmentosMapsList.isEmpty()) {
                this.segmentosMapsList = segmentosMapsList;
            }
            notifyDataSetChanged();
        }
    }

    public interface OnMapsSelectListener {
        void onMapsSelectListener(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        final OnMapsSelectListener onMapsSelectListener;
        private final TextView tvMapsInfo;

        public ViewHolder(@NonNull View itemView, OnMapsSelectListener onMapsSelectListener) {
            super(itemView);
            mView = itemView;
            tvMapsInfo = mView.findViewById(R.id.tvMapsInfo);
            this.onMapsSelectListener = onMapsSelectListener;
            mView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onMapsSelectListener.onMapsSelectListener(getBindingAdapterPosition());
        }
    }
}
