package com.example.proyectofinalencuesta_bdubicuo.ui.menu2_survey.capture;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinalencuesta_bdubicuo.R;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.ListadoFocalizados;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Segmentos;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FocalizadasAdapter
        extends RecyclerView.Adapter<FocalizadasAdapter.ViewHolder> {

    private List<ListadoFocalizados> listadoFocalizadosList;
    private List<Segmentos> segmentosList;

    //        private List<Cuestionarios> cuestionariosList;
    public FocalizadasAdapter(List<Segmentos> segmentosList, List<ListadoFocalizados> listadoFocalizadosList) {
        this.listadoFocalizadosList = listadoFocalizadosList;
        this.segmentosList = segmentosList;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_focalizadas, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull final ViewHolder holder, int position) {
        try {
            if (listadoFocalizadosList != null) {

                ListadoFocalizados focalizados = listadoFocalizadosList.get(position);
                Segmentos segmentos = segmentosList.get(position);

                holder.tvLugarP.setText(String.format("Lugar poblado: %s ", segmentos.getLugarPobladoDescripcion() == null ? "—" : segmentos.getLugarPobladoDescripcion()));
                holder.tvBarrioViv.setText(String.format("Barrio: %s ", segmentos.getBarrioDescripcion() == null ? "—" : segmentos.getBarrioDescripcion()));
                holder.tvInformanteViv.setText(String.format("Informante PyV: %s ", focalizados.getInformante_Vivienda() == null ? "—" : focalizados.getInformante_Vivienda()));
                holder.tvInfoTelViv.setText(String.format("Teléfono informante: %s", focalizados.getTelefono_Informante() == null ? "—" : focalizados.getTelefono_Informante()));
                holder.tvJefeVivienda.setText(String.format("Jefe de vivienda: %s", focalizados.getJefe_Vivienda() == null ? "—" : focalizados.getJefe_Vivienda()));
                holder.tvCalleViv.setText(String.format("Calle: %s", focalizados.getCalle() == null ? "—" : focalizados.getCalle()));
                holder.tvEdificioViv.setText(String.format("Casa/Edificio: %s", focalizados.getEdificio() == null ? "—" : focalizados.getEdificio()));
                holder.tvCuartoViv.setText(String.format("Cuarto: %s", focalizados.getCuarto() == null ? "—" : focalizados.getCuarto()));
                holder.tvProductorViv.setText(String.format("Productor/Empresa: %s", focalizados.getNombre_Productor() == null ? "—" : focalizados.getNombre_Productor()));
                holder.tvTelProductorViv.setText(String.format("Teléfono productor: %s", focalizados.getTelefono_Productor() == null ? "—" : focalizados.getTelefono_Productor()));
                holder.tvDireccionViv.setText(String.format("Dirección: %s", focalizados.getDireccion() == null ? "—" : focalizados.getDireccion()));
                holder.tvFuenteViv.setText(String.format("Fuente: %s", focalizados.getFuente() == null ? "—" : focalizados.getFuente()));
            }
        } catch (Exception e) {
            Log.i("TAG", "onBindViewHolder: ");
        }
    }

    public void setData(List<ListadoFocalizados> segmentosList) {
        if (segmentosList != null) {
            this.listadoFocalizadosList = segmentosList;
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
        if (listadoFocalizadosList != null)
            return listadoFocalizadosList.size();
        else return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        //        private final TextView tvSubZonaInfo;
        private final TextView tvLugarP;
        private final TextView tvBarrioViv;
        private final TextView tvInformanteViv;
        private final TextView tvInfoTelViv;
        private final TextView tvJefeVivienda;
        private final TextView tvProductorViv;
        private final TextView tvCalleViv;
        private final TextView tvEdificioViv;
        private final TextView tvCuartoViv;
        private final TextView tvTelProductorViv;
        private final TextView tvDireccionViv;
        private final TextView tvFuenteViv;

        public ViewHolder(View view) {
            super(view);
            mView = view;
//            tvSubZonaInfo = mView.findViewById(R.id.txtSubZonaInfo);
            tvLugarP = mView.findViewById(R.id.tvLugarP);
            tvBarrioViv = mView.findViewById(R.id.tvBarrioViv);
            tvInformanteViv = mView.findViewById(R.id.tvInformanteViv);
            tvInfoTelViv = mView.findViewById(R.id.tvInfoTelViv);
            tvJefeVivienda = mView.findViewById(R.id.tvJefeVivienda);
            tvProductorViv = mView.findViewById(R.id.tvProductorViv);
            tvCalleViv = mView.findViewById(R.id.tvCalleViv);
            tvEdificioViv = mView.findViewById(R.id.tvEdificioViv);
            tvCuartoViv = mView.findViewById(R.id.tvCuartoViv);
            tvTelProductorViv = mView.findViewById(R.id.tvTelProductorViv);
            tvDireccionViv = mView.findViewById(R.id.tvDireccionViv);
            tvFuenteViv = mView.findViewById(R.id.tvFuenteViv);
        }
    }
}
