package com.example.proyectofinalencuesta_bdubicuo.ui.menu2_survey.report;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinalencuesta_bdubicuo.R;

import java.util.ArrayList;
import java.util.List;

public class ReportCen01Adapter extends RecyclerView.Adapter<ReportCen01Adapter.ViewHolder> {

    private List<Integer> reporteTotales;
    private final List<String> subZonaTitulos;
//    private static final String TAG = "ReportSubZonaTotAdapter";

    public ReportCen01Adapter(List<Integer> reporteTotales) {
        this.reporteTotales = reporteTotales;
        subZonaTitulos = new ArrayList<>();
        initTitulos();
    }

    private void initTitulos() {
        subZonaTitulos.add("PRODUCTORES");
        subZonaTitulos.add("EXPLOTACIONES");

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_report_subzonastot, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportCen01Adapter.ViewHolder holder, int position) {
        if (!reporteTotales.isEmpty()) {
            holder.tvReportSubZonaTitulo.setText(subZonaTitulos.get(position));
            holder.tvReportSubZonaInfo.setText(String.valueOf(reporteTotales.get(position)));
//        if (segmentosList != null) {
//            Segmentos segmentos = segmentosList.get(position);
//            holder.tvReportSubZonaInfo.setText("00");
//            Log.i(TAG, "onBindViewHolder: fila cargada ReportSegmentos");
//        }
        }
    }
    public void setDataCuestionarios(List<Integer> reporteTotales) {
        if (reporteTotales != null) {
            this.reporteTotales = reporteTotales;
            notifyDataSetChanged();
        }
    }


    @Override
    public int getItemCount() {
        if (subZonaTitulos != null)
            return subZonaTitulos.size();
        else return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        private final TextView tvReportSubZonaTitulo;
        private final TextView tvReportSubZonaInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            tvReportSubZonaTitulo = mView.findViewById(R.id.tvReportTitulo);
            tvReportSubZonaInfo = mView.findViewById(R.id.tvReportInfo);
        }
    }
}
