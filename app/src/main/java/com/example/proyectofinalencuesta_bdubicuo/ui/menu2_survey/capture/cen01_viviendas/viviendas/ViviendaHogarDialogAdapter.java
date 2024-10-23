package com.example.proyectofinalencuesta_bdubicuo.ui.menu2_survey.capture.cen01_viviendas.viviendas;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinalencuesta_bdubicuo.R;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.ControlRecorrido;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Cuestionarios;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class ViviendaHogarDialogAdapter
        extends RecyclerView.Adapter<ViviendaHogarDialogAdapter.ViewHolder> {
    //    private static final String TAG = "ViviendaHogarDialogAdap";
    private final OnViviendaHogarListener onViviendaHogarListener;
    //    private final ViviendaHogarViewModel viviendaHogarViewModel;
    private List<ControlRecorrido> viviendasHogaresList;
    private List<Cuestionarios> cuestionarios;

    /*ViviendaHogarViewModel viviendaHogarViewModel,*/
    public ViviendaHogarDialogAdapter(List<ControlRecorrido> viviendasHogaresList,
                                      OnViviendaHogarListener onViviendaHogarListener, List<Cuestionarios> cuestionarios) {
//        this.viviendaHogarViewModel = viviendaHogarViewModel;
        this.viviendasHogaresList = viviendasHogaresList;
        this.onViviendaHogarListener = onViviendaHogarListener;
        this.cuestionarios = cuestionarios;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_viviendashogar, parent, false);
        return new ViewHolder(view, onViviendaHogarListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        try {
            if (viviendasHogaresList != null) {
                ControlRecorrido controlRecorrido = viviendasHogaresList.get(position);
//                if (cen01_viviendas.getEstadoVivienda() == 0)
//                    holder.cvViviendaHogar.setCardBackgroundColor(Color.rgb(255, 160, 122));
//                if (cen01_viviendas.getEstadoVivienda() == 1)
//                    holder.cvViviendaHogar.setCardBackgroundColor(Color.rgb(255, 255, 155));
//                if (cen01_viviendas.getEstadoVivienda() == 2)
//                    holder.cvViviendaHogar.setCardBackgroundColor(Color.rgb(169, 208, 142));
//                if (cen01_viviendas.getEstadoVivienda() == 3)
//                    holder.cvViviendaHogar.setCardBackgroundColor(Color.rgb(189, 215, 238));

                holder.tvVHViviendaInfo.setText(String.format("VIV. %s ",
                        controlRecorrido.getVivienda()));

//                if (cuestionarios.getEstado() == 1)//AMARILLO
//                    holder.cvProductorExplotacion.setCardBackgroundColor(Color.rgb(255, 255, 155));
//                if (cuestionarios.getEstado() == 2)//verde
//                    holder.cvProductorExplotacion.setCardBackgroundColor(Color.rgb(169, 208, 142));
//                if (cuestionarios.getEstado() == 3)//celeste
//
                int cuestionariosCompletados = 0;
                int cuestionariosPendientes = 0;
                int contCuestionariosExistentes = 0;//contador para cuestionarios existentes por recorrido
                for (int x = 0; x < cuestionarios.size(); x++) {
                    if (cuestionarios.get(x).getCodigoViviendaHogares().equals(controlRecorrido.getLlaveRecorrido())) {
                        if (cuestionarios.get(x).getEstado() == 0 || cuestionarios.get(x).getEstado() == 1) {
                            cuestionariosPendientes++;
                        }
                        contCuestionariosExistentes++;
                    }
                }
                if (cuestionariosPendientes > 0)
                    holder.tvVHPendientes.setText(String.format("PEND.: %s ",
                            cuestionariosPendientes));
                else if (contCuestionariosExistentes > 0 && cuestionariosPendientes == 0)
                    holder.tvVHPendientes.setText(String.format("PEND.: %s ", 0));
                else
                    holder.tvVHPendientes.setText(String.format("PEND.: %s ", controlRecorrido.getCantProductoresAgro()));


                holder.tvVHCompletados.setText(String.format("PROD.: %s ",
                        controlRecorrido.getCantProductoresAgro()));


              /*  if (cuestionarios.getErroresEstructura() != null
                        && cuestionarios.getErroresEstructura().length() > 4)
                    holder.vInconsistencias.setBackgroundColor(Color.RED);
                else
                    holder.vInconsistencias.setBackgroundColor(Color.BLACK);*/


         /*       holder.rvHogares.setLayoutManager(new LinearLayoutManager(holder.mView.getContext()));
                HogaresAdapter hogaresAdapter = new HogaresAdapter(viviendasHogaresList.get(position),
                        holder.onViviendaHogarListener);
                holder.rvHogares.setAdapter(hogaresAdapter);*/
            }
        } catch (Exception e) {
            Log.e("TAG", "onBindViewHolder: ", e);
        }
    }

    @Override
    public int getItemCount() {
        if (viviendasHogaresList != null) {
            return viviendasHogaresList.size();
        } else return 0;
    }

    public void setData(List<ControlRecorrido> lista, List<Cuestionarios> cuestionarios) {
        if (lista != null) {
            this.viviendasHogaresList = lista;
            this.cuestionarios = cuestionarios;
        }
        notifyDataSetChanged();
    }

    public interface OnViviendaHogarListener extends HogaresAdapter.OnHogarListener {
        void onViviendaHogarClick(ControlRecorrido controlRecorrido, View view);

        @Override
        void onHogarClick(ControlRecorrido position, View view, int absoluteAdapterPosition);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        final OnViviendaHogarListener onViviendaHogarListener;
        //        private final TextView tvVHSegmentoInfo;
//        private final TextView tvVHDivisonInfo;
        private final TextView tvVHViviendaInfo;
        private final TextView tvVHPendientes;
        private final TextView tvVHCompletados;
//        private final View vInconsistencias;
        //        private final MaterialCardView cvViviendaHogar;

//        private final RecyclerView rvHogares;

        public ViewHolder(View view, OnViviendaHogarListener onViviendaHogarListener) {
            super(view);
            mView = view;
//            tvVHSegmentoInfo = mView.findViewById(R.id.tvVHSegmentoInfo);
            tvVHViviendaInfo = mView.findViewById(R.id.tvVHViviendaInfo);
            tvVHPendientes = mView.findViewById(R.id.tvVHPendientes);
            tvVHCompletados = mView.findViewById(R.id.tvVHCompletados);
//            vInconsistencias = mView.findViewById(R.id.vInconsistencias);
//            tvVHHogarInfo = mView.findViewById(R.id.tvVHHogarInfo);

            //        private final TextView tvVHHogarInfo;
            MaterialCardView cvViviendaHogar = mView.findViewById(R.id.cvViviendaHogar);
//            ImageButton ibAddHogar = mView.findViewById(R.id.ibAddHogar);
//            rvHogares = mView.findViewById(R.id.rvHogares);

//            ibAddHogar.setOnClickListener(this);
            cvViviendaHogar.setOnClickListener(this);

            this.onViviendaHogarListener = onViviendaHogarListener;
        }

        @Override
        public void onClick(View v) {
            ControlRecorrido cuestionarioSeleccionado =
                    viviendasHogaresList.get(getBindingAdapterPosition());
            if (v.getId() == R.id.cvViviendaHogar)
                onViviendaHogarListener.onViviendaHogarClick(cuestionarioSeleccionado, v);
//            else if (v.getId() == R.id.ibAddHogar) {
              /*  String datosJson = cuestionarioSeleccionado.get(0).getDatosJson();
                if (datosJson != null) {
                    JsonObject jsonObject = new JsonParser().parse(datosJson).getAsJsonObject();

                    if (cuestionarioSeleccionado.get(0).getEstado() > 0) {
                        if (jsonObject.get("REC_VIVIENDA").getAsJsonObject()
                                .get("V01_TIPO").getAsString().matches("^([1-4]|[0][1-4])$")
                                && jsonObject.get("REC_VIVIENDA").getAsJsonObject()
                                .get("V02_COND").getAsString().matches("^([1]|[0][1])$")) {//Cambiar espacios

                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder.setTitle("Creación de hogar");
                            builder.setMessage("¿Desea crear un nuevo hogar?");
                            builder.setPositiveButton("Crear", (dialog, which) -> {
                                SimpleDateFormat sdf =
                                        new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                                Date date = new Date();
                                String fecha = sdf.format(date);
                                int numHogar = (Integer.parseInt(cuestionarioSeleccionado.get(
                                        cuestionarioSeleccionado.size() - 1).getHogar())) + 1;
                                String numVivienda = cuestionarioSeleccionado.get(0).getVivienda();

                                int hogarCuestionario;
                                if (jsonObject.has("REC_VIVIENDA")) {
                                    if (jsonObject.get("REC_VIVIENDA").getAsJsonObject().has("V17_NHOG")
                                            && jsonObject.get("REC_VIVIENDA").getAsJsonObject()
                                            .get("V17_NHOG").getAsString().matches("^([1-9])$")) {


                                        hogarCuestionario = jsonObject.get("REC_VIVIENDA")
                                                .getAsJsonObject().get("V17_NHOG").getAsInt();
                                        if (numHogar <= hogarCuestionario) {
                                            if (cuestionarioSeleccionado.get(cuestionarioSeleccionado.size() - 1)
                                                    .getEstado() >= 1) {
                                                Cuestionarios cuestionaroNuevo = new Cuestionarios(
                                                        segmentos.getId() + segmentos.getEmpadronadorID() + numVivienda + numHogar,
                                                        segmentos.getId(),
                                                        segmentos.getProvinciaID(),
                                                        segmentos.getDistritoID(),
                                                        segmentos.getCorregimientoID(),
                                                        segmentos.getSubZonaID(),
                                                        segmentos.getSegmentoID(),
                                                        segmentos.getDivisionID(),
                                                        SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_CODIGO),
                                                        null,
                                                        segmentos.getEmpadronadorID(),
                                                        numVivienda,
                                                        String.valueOf(numHogar),
                                                        null,
                                                        null,
                                                        null,
                                                        0,
                                                        fecha,
                                                        null,
                                                        fecha,
                                                        null,
                                                        false,
                                                        false,
                                                        "",
                                                        null,
                                                        "",
                                                        false
                                                );
                                                cuestionarioSeleccionado.add(cuestionaroNuevo);
                                                viewModel.addVivienda(cuestionaroNuevo);
                                                hogaresAdapter.setData(cuestionarioSeleccionado);
                                            } else {
                                                MaterialAlertDialogBuilder materialAlertDialogBuilder =
                                                        new MaterialAlertDialogBuilder(activity);
                                                materialAlertDialogBuilder.setTitle("Creación de hogar.");
                                                materialAlertDialogBuilder.setMessage("Debe terminar con el hogar anterior para" +
                                                        " proceder a crear uno nuevo.");
                                                materialAlertDialogBuilder.setIcon(R.drawable.ic_dialog_warning);
                                                materialAlertDialogBuilder.setPositiveButton("OK", (dialog1, which1) -> dialog1.dismiss());
                                                materialAlertDialogBuilder.show();
                                            }
                                        } else {
                                            MaterialAlertDialogBuilder materialAlertDialogBuilder =
                                                    new MaterialAlertDialogBuilder(activity);
                                            materialAlertDialogBuilder.setTitle("Creación de hogar.");
                                            materialAlertDialogBuilder.setMessage("Solo hay " + hogarCuestionario + " hogar(es) en la vivienda " +
                                                    "(Pregunta 17- Número de hogares)");
                                            materialAlertDialogBuilder.setIcon(R.drawable.ic_dialog_warning);
                                            materialAlertDialogBuilder.setPositiveButton("OK", (dialog12, which12) -> dialog12.dismiss());
                                            materialAlertDialogBuilder.show();
                                        }

                                    } else {
                                        MaterialAlertDialogBuilder materialAlertDialogBuilder =
                                                new MaterialAlertDialogBuilder(activity);
                                        materialAlertDialogBuilder.setTitle("Creación de hogar.");
                                        materialAlertDialogBuilder.setMessage("No puede crear hogar adicional.");
                                        materialAlertDialogBuilder.setIcon(R.drawable.ic_dialog_warning);
                                        materialAlertDialogBuilder.setPositiveButton("OK", (dialog12, which12) -> dialog12.dismiss());
                                        materialAlertDialogBuilder.show();
                                    }
                                }
                            });

                            builder.setNegativeButton("Cancelar", (dialog, which) -> Log.i(TAG, "onClick: Cancelado."));
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        } else {
                            MaterialAlertDialogBuilder materialAlertDialogBuilder =
                                    new MaterialAlertDialogBuilder(activity);
                            materialAlertDialogBuilder.setTitle("Creación de hogar.");
                            materialAlertDialogBuilder.setMessage("No puede crear hogar adicional para esta vivienda.");
                            materialAlertDialogBuilder.setIcon(R.drawable.ic_dialog_warning);
                            materialAlertDialogBuilder.setPositiveButton("OK", (dialog12, which12) -> dialog12.dismiss());
                            materialAlertDialogBuilder.show();
                        }
                    } else {
                        MaterialAlertDialogBuilder materialAlertDialogBuilder =
                                new MaterialAlertDialogBuilder(activity);
                        materialAlertDialogBuilder.setTitle("Creación de hogar.");
                        materialAlertDialogBuilder.setMessage("Debe completar la vivienda principal para agregar un nuevo hogar.");
                        materialAlertDialogBuilder.setIcon(R.drawable.ic_dialog_warning);
                        materialAlertDialogBuilder.setPositiveButton("OK", (dialog12, which12) -> dialog12.dismiss());
                        materialAlertDialogBuilder.show();
                    }
                } else {
                    Snackbar.make(itemView, "El cuestionario no ha sido iniciado.", Snackbar.LENGTH_SHORT)
                            .show();
                }*/
//            }
        }
    }
}
