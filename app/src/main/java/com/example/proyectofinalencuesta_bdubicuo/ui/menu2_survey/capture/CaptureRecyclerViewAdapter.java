package com.example.proyectofinalencuesta_bdubicuo.ui.menu2_survey.capture;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinalencuesta_bdubicuo.R;
import com.example.proyectofinalencuesta_bdubicuo.data.local.constants.AppConstants;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Segmentos;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.TotCuestionarios;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CaptureRecyclerViewAdapter
        extends RecyclerView.Adapter<CaptureRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "CaptureRecyclerViewAdap";
    private final OnSegmentoCardListener onSegmentoCardListener;
    private List<Segmentos> segmentosList;
    //        private List<Cuestionarios> cuestionariosList;
    private List<TotCuestionarios> totCuestionariosList;

    public CaptureRecyclerViewAdapter(List<Segmentos> items, List<TotCuestionarios> totCuestionariosList,
                                      OnSegmentoCardListener onSegmentoCardListener) {
        segmentosList = items;
        this.totCuestionariosList = totCuestionariosList;
        this.onSegmentoCardListener = onSegmentoCardListener;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_capture, parent, false);
        return new ViewHolder(view, onSegmentoCardListener);
    }

    @Override
    public void onBindViewHolder(@NotNull final ViewHolder holder, int position) {
        if (segmentosList != null) {

            Segmentos segmentos = segmentosList.get(position);
//            holder.tvSubZonaInfo.setText(segmentos.getSubZonaID());
            String tipo = "";
            if (segmentos.getTipoEmp() == 1)
                tipo = "(R)";
            else if (segmentos.getTipoEmp() == 2)
                tipo = "(F)";
            holder.tvSegmentoInfo.setText(String.format("%s - %s - %s %s",
                    segmentos.getId().substring(0, 6),
                    segmentos.getId().substring(6, 10),
                    segmentos.getId().substring(10, 12),
                    tipo.isEmpty() ? "" : tipo));

            String estado = "0";
            switch (segmentos.getEstado()) {
                case "1":
                    estado = AppConstants.SEG_ESTADO_HABILITADO;
                    break;
                case "2":
                    estado = AppConstants.SEG_ESTADO_ENPROCESO;
                    break;
                case "3":
                    estado = AppConstants.SEG_ESTADO_CERRADO_INCOMPLETO;
                    break;
                case "4":
                    estado = AppConstants.SEG_ESTADO_CERRADO_COMPLETO;
                    break;
            }
            holder.tvEstadoInfo.setText(estado);

//            int totCuestionarios = 0;
//            for (Cuestionarios cuest : cuestionariosList) {
//                if (cuest.getCodigoSegmento().equals(segmentos.getId())) {
//                    Log.i(TAG, "onBindViewHolder: ");
//                    totCuestionarios++;
//                }
//            }
            for (int x = 0; x < totCuestionariosList.size(); x++) {
                if (totCuestionariosList.get(x).getCodigoSegmento().equals(segmentos.getId())) {
                    holder.tvCuestionariosInfo.setText(
                            String.valueOf(totCuestionariosList.get(x).getTotCuestionarios()));
                    break;
                } else
                    holder.tvCuestionariosInfo.setText("0");
            }
        }
    }

    public void setData(List<Segmentos> segmentosList, List<TotCuestionarios> totCuestionariosList) {
        if (segmentosList != null) {
            this.segmentosList = segmentosList;
        }

        if (totCuestionariosList != null) {
            this.totCuestionariosList = totCuestionariosList;
        }
        notifyDataSetChanged();
    }

//    public void setDataCuestionarios(List<Cuestionarios> cuestionarios) {
//        if (cuestionariosList.size() != 0) {
//            this.cuestionariosList = cuestionarios;
//        }
//        notifyDataSetChanged();
//    }

    @Override
    public int getItemCount() {
        if (segmentosList != null)
            return segmentosList.size();
        else return 0;
    }

    public interface OnSegmentoCardListener {
        void onSegmentoCardClick(int position);

        void onSegmentoCardLongClick(int position, Segmentos segmentos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        public final View mView;
        final OnSegmentoCardListener onSegmentoCardListener;
        //        private final TextView tvSubZonaInfo;
        private final TextView tvSegmentoInfo;
        private final TextView tvEstadoInfo;
        private final TextView tvCuestionariosInfo;

        public ViewHolder(View view, OnSegmentoCardListener onSegmentoCardListener) {
            super(view);
            mView = view;
//            tvSubZonaInfo = mView.findViewById(R.id.txtSubZonaInfo);
            tvSegmentoInfo = mView.findViewById(R.id.txtSegmentoInfo);
            tvEstadoInfo = mView.findViewById(R.id.txtEstadoInfo);
            tvCuestionariosInfo = mView.findViewById(R.id.txtCuestionariosInfo);
            this.onSegmentoCardListener = onSegmentoCardListener;

            mView.setOnClickListener(this);
            mView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onSegmentoCardListener.onSegmentoCardClick(getBindingAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            onSegmentoCardListener.onSegmentoCardLongClick(getBindingAdapterPosition(),segmentosList.get(getBindingAdapterPosition()));
            return false;
        }
    }
}
