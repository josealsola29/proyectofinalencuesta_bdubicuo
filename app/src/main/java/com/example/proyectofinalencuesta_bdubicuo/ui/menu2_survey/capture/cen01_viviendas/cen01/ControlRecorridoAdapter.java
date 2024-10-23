package com.example.proyectofinalencuesta_bdubicuo.ui.menu2_survey.capture.cen01_viviendas.cen01;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinalencuesta_bdubicuo.R;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.ControlRecorrido;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class ControlRecorridoAdapter extends RecyclerView.Adapter<ControlRecorridoAdapter.ViewHolder> {
    private final OnControlRecorridoListener onControlRecorridoListener;
    private List<ControlRecorrido> controlRecorridoViviendaList;
    private final Activity activity;
    private final String TAG = "ControlRecorridoAdapterTAG";

    public ControlRecorridoAdapter(/*Segmentos segmentos,*/ List<ControlRecorrido> controlRecorridoViviendaList,
                                                            FragmentActivity activity, OnControlRecorridoListener onControlRecorridoListener) {
        this.controlRecorridoViviendaList = controlRecorridoViviendaList;
        this.activity = activity;
        this.onControlRecorridoListener = onControlRecorridoListener;
    }

    @NonNull
    @Override
    public ControlRecorridoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_cen01_recorrido, parent, false);
        return new ViewHolder(view, onControlRecorridoListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ControlRecorridoAdapter.ViewHolder holder, int position) {
        if (controlRecorridoViviendaList != null) {
            ControlRecorrido controlRecorrido = controlRecorridoViviendaList.get(position);
            holder.tvCen01_Titulo.setText(String.format("Recorrido %s", controlRecorrido.getVivienda()));
    /*        RadioButton[] rbCoondicion = new RadioButton[]{
                    holder.rbCen01_OcupantesPresentes,//1
                    holder.rbCen01_OcupantesAusentes,//2
                    holder.rbCen01_Desocupadas,//3
                    holder.rbCen01_DejoDeSer,//4
            };*/
            if (controlRecorrido.getCondicionID() != null) {
                Drawable icono = ResourcesCompat.getDrawable(activity.getResources(), R.drawable.advertencia_cen01_3, null);
//                rbCoondicion[controlRecorrido.getCondicionID() - 1].setChecked(true);

                switch (controlRecorrido.getCondicionID()) {
                    case 1:
                        if (controlRecorrido.getPregA() != null && controlRecorrido.getPregB() != null
                                && controlRecorrido.getPregC() != null && controlRecorrido.getPregD() != null
                                && controlRecorrido.getPregE() != null && controlRecorrido.getPregF() != null
                                && controlRecorrido.getPregG() != null) {
                            if (!controlRecorrido.getPregA()
                                    && !controlRecorrido.getPregB()
                                    && !controlRecorrido.getPregC()
                                    && !controlRecorrido.getPregD()
                                    && !controlRecorrido.getPregE()
                                    && !controlRecorrido.getPregF()
                                    && !controlRecorrido.getPregG()) {
                                holder.tvCen01_Titulo.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                                holder.tvCen01_Titulo.setBackgroundColor(Color.rgb(150, 200, 230));//CELESTE claro
                            } else if (controlRecorrido.getCantProductoresAgro() != null
                                    && controlRecorrido.getCantExplotacionesAgro() != null) {
                                holder.tvCen01_Titulo.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                                holder.tvCen01_Titulo.setBackgroundColor(Color.rgb(236, 236, 236));//GRIS
                            }
                        } else{
                            holder.tvCen01_Titulo.setBackgroundColor(Color.rgb(236, 236, 236));//GRIS
                            holder.tvCen01_Titulo.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, icono, null);
                        }

                        holder.btnCen01_Preguntas.setEnabled(true);
                        break;
                    case 2:
                        holder.tvCen01_Titulo.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, icono, null);
                        holder.tvCen01_Titulo.setBackgroundColor(Color.rgb(236, 236, 236));//GRIS
                        holder.btnCen01_Preguntas.setEnabled(false);
                        break;
                    case 3:
                    case 4:
                        holder.tvCen01_Titulo.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                        holder.tvCen01_Titulo.setBackgroundColor(Color.rgb(80, 160, 230));//CELESTE
                        holder.btnCen01_Preguntas.setEnabled(false);
                        break;
                    default:
                        holder.tvCen01_Titulo.setBackgroundColor(Color.rgb(236, 236, 236));//GRIS
                        break;
                }
            }
        } else
            Toast.makeText(activity, "Lista de recorridos nula.", Toast.LENGTH_LONG).show();
    }

    @Override
    public int getItemCount() {
        if (controlRecorridoViviendaList != null) {
            return controlRecorridoViviendaList.size();
        } else return 0;
    }

    public void setData(List<ControlRecorrido> controlRecorridoViviendaList) {
        if (controlRecorridoViviendaList != null) {
            this.controlRecorridoViviendaList = controlRecorridoViviendaList;
        }
        notifyDataSetChanged();
    }


    public interface OnControlRecorridoListener {
        void onControlRecorridoClick(ControlRecorrido controlRecorridoList, View view,
                                     LinearLayout lnCen01_recorrido, int posicion);

        //        void onButtonClickListener(List<ControlRecorrido> controlRecorridoList, View view, int position);
        void onButtonPreguntasClickListener(ControlRecorrido controlRecorridoList, View view, int position);

        void onButtonCondicionVivClickListener(ControlRecorrido controlRecorridoList, View view, int position);

        void onButtonObservacionClickListener(ControlRecorrido controlRecorridoList, View view, int position);

        void onButtonEliminarClickListener(ControlRecorrido controlRecorridoList, View view, int position);


        void onRGCondicionViviendaClickListener(List<ControlRecorrido> controlRecorridoViviendaList, int absoluteAdapterPosition, int checkedId);

//        void onRGOllaComunClickListener(ControlRecorrido controlRecorrido,
//                                        RadioGroup rgCen01_ollaComun, int checkedRadioButtonId);

//        @Override
//        void onHogarRecorridoClick(ControlRecorrido position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

        private final OnControlRecorridoListener onControlRecorridoListener;
        private final LinearLayout lnCen01_recorrido;
        //        private final LinearLayout lnPreguntaOllaComun;
        private final TextView tvCen01_Titulo;
/*        private final RadioButton rbCen01_OcupantesPresentes;
        private final RadioButton rbCen01_OcupantesAusentes;
        private final RadioButton rbCen01_Desocupadas;
        private final RadioButton rbCen01_DejoDeSer;*/

        private final Button btnCen01_Preguntas;
        //        private final RadioGroup rgCen01_ollaComun;
//        private final RadioButton rb_ollacomun_si;
//        private final RadioButton rb_ollacomun_no;

//        private final RecyclerView rvCen01_HogaresRecorrido;

        public ViewHolder(@NonNull View itemView, OnControlRecorridoListener onControlRecorridoListener) {
            super(itemView);
            lnCen01_recorrido = itemView.findViewById(R.id.lnCen01_recorrido);
//            lnPreguntaOllaComun = itemView.findViewById(R.id.lnPreguntaOllaComun);
            tvCen01_Titulo = itemView.findViewById(R.id.tvCen01_Titulo);
//            btnCen01_AddNumHogares = itemView.findViewById(R.id.btnCen01_AddNumHogares);

            Button btnCen01_Observacion = itemView.findViewById(R.id.btnCen01_Observacion);
            btnCen01_Preguntas = itemView.findViewById(R.id.btnCen01_Preguntas);
            Button btnCen01_condicionVivienda = itemView.findViewById(R.id.btnCen01_condicionVivienda);
            ImageButton ibEliminarRecorrido = itemView.findViewById(R.id.btnEliminarRecorrido);

            //        private final Button btnCen01_AddNumHogares;
/*            RadioGroup rgCen01_condicionViv = itemView.findViewById(R.id.rgCen01_condicionViv);
            rbCen01_OcupantesPresentes = itemView.findViewById(R.id.rbCen01_OcupantesPresentes);
            rbCen01_OcupantesAusentes = itemView.findViewById(R.id.rbCen01_OcupantesAusentes);
            rbCen01_Desocupadas = itemView.findViewById(R.id.rbCen01_Desocupadas);
            rbCen01_DejoDeSer = itemView.findViewById(R.id.rbCen01_DejoDeSer);*/

//            rgCen01_ollaComun = itemView.findViewById(R.id.rgCen01_ollaComun);
//            rb_ollacomun_si = itemView.findViewById(R.id.rb_ollacomun_si);
//            rb_ollacomun_no = itemView.findViewById(R.id.rb_ollacomun_no);

//            rvCen01_HogaresRecorrido = itemView.findViewById(R.id.rvCen01_HogaresRecorrido);
            MaterialCardView cvCen01_Vivienda = itemView.findViewById(R.id.cvCen01_Vivienda);

//            rgCen01_condicionViv.setOnCheckedChangeListener(this);
//            rgCen01_condicionViv.setOnClickListener(this);
//            rgCen01_ollaComun.setOnCheckedChangeListener(this);
//            btnCen01_AddNumHogares.setOnClickListener(this);
            btnCen01_Observacion.setOnClickListener(this);
            btnCen01_Preguntas.setOnClickListener(this);
            btnCen01_condicionVivienda.setOnClickListener(this);
            ibEliminarRecorrido.setOnClickListener(this);
            tvCen01_Titulo.setOnClickListener(this);
            cvCen01_Vivienda.setOnClickListener(this);

            this.onControlRecorridoListener = onControlRecorridoListener;
        }

        @Override
        public void onClick(View view) {
            int posicion = getAbsoluteAdapterPosition();
            ControlRecorrido controlRecorridoClick = controlRecorridoViviendaList.get(posicion);
            if (view.getId() == R.id.tvCen01_Titulo) {
                try {
                    onControlRecorridoListener.onControlRecorridoClick(
                            controlRecorridoClick,
                            view, lnCen01_recorrido, posicion);
                } catch (Exception e) {
                    Log.e("TAG", "onClick: ");
                }
            } /*else if (view.getId() == R.id.btnCen01_AddNumHogares) {
                onControlRecorridoListener.onButtonClickListener(controlRecorridoViviendaList,
                        view, getAbsoluteAdapterPosition());
            }*/ else if (view.getId() == R.id.btnCen01_condicionVivienda) {//BOTON DE PREGUNTAS
                onControlRecorridoListener.onButtonCondicionVivClickListener(controlRecorridoClick,
                        view, getAbsoluteAdapterPosition());
            }  else if (view.getId() == R.id.btnCen01_Preguntas) {//BOTON DE PREGUNTAS
                onControlRecorridoListener.onButtonPreguntasClickListener(controlRecorridoClick,
                        view, getAbsoluteAdapterPosition());
            } else if (view.getId() == R.id.btnCen01_Observacion) { //BOTON OBSERVACIONES
                onControlRecorridoListener.onButtonObservacionClickListener(controlRecorridoClick,
                        view, getAbsoluteAdapterPosition());
            } else if (view.getId() == R.id.btnEliminarRecorrido) {
                onControlRecorridoListener.onButtonEliminarClickListener(controlRecorridoClick,
                        view, getAbsoluteAdapterPosition());
            }
        }

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
            onControlRecorridoListener.onRGCondicionViviendaClickListener(
                    controlRecorridoViviendaList, getAbsoluteAdapterPosition(), checkedId);
        }

/*        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int posicion) {
            if (radioGroup.getId() == R.id.rgCen01_condicionViv) {
                onControlRecorridoListener.onRGCondicionViviendaClickListener(
                        controlRecorridoViviendaList.get(getAbsoluteAdapterPosition()),
                        rgCen01_condicionViv, posicion);
            }*//* else if (radioGroup.getId() == R.id.rgCen01_ollaComun) {
                onControlRecorridoListener.onRGOllaComunClickListener(
                        controlRecorridoViviendaList.get(getAbsoluteAdapterPosition()),
                        rgCen01_ollaComun, posicion);
            }*//*
        }*/
    }
}
