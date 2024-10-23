package com.example.proyectofinalencuesta_bdubicuo.ui.menu2_survey.capture.cen01_viviendas.productor;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinalencuesta_bdubicuo.R;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Cuestionarios;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class ProductorExplotacionAdapter
        extends RecyclerView.Adapter<ProductorExplotacionAdapter.ViewHolder> {
    private static final String TAG = "ViviendaHogarDialogAdap";
    private final OnProductorExplotacionListener onProductorExplotacionListener;
    //    private final ProductorExplotacionViewModel productorExplotacionViewModel;
    private List<List<Cuestionarios>> cuestionariosViviendaHogaresList;

    public ProductorExplotacionAdapter(/*ProductorExplotacionViewModel productorExplotacionViewModel,*/
            List<List<Cuestionarios>> cuestionariosViviendaHogares,
            OnProductorExplotacionListener onProductorExplotacionListener) {
//        this.productorExplotacionViewModel = productorExplotacionViewModel;
        this.cuestionariosViviendaHogaresList = cuestionariosViviendaHogares;
        this.onProductorExplotacionListener = onProductorExplotacionListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_productor, parent, false);
        return new ViewHolder(view, onProductorExplotacionListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            if (cuestionariosViviendaHogaresList != null) {//Color
                Cuestionarios cuestionarios = cuestionariosViviendaHogaresList.get(position).get(0);
                if (cuestionarios.getEstado() == 0)//SALMON
                    holder.cvProductorExplotacion.setCardBackgroundColor(Color.rgb(255, 160, 122));
                if (cuestionarios.getEstado() == 1)//AMARILLO
                    holder.cvProductorExplotacion.setCardBackgroundColor(Color.rgb(255, 255, 155));
                if (cuestionarios.getEstado() == 2)//verde
                    holder.cvProductorExplotacion.setCardBackgroundColor(Color.rgb(169, 208, 142));
                if (cuestionarios.getEstado() == 3)//celeste
                    holder.cvProductorExplotacion.setCardBackgroundColor(Color.rgb(189, 215, 238));

                holder.tvProductorInfo.setText(String.format("PROD. %s | EXPL. %s (P6=%s)",
                        cuestionarios.getProductor(), cuestionarios.getExplotacionNum(),
                        cuestionarios.getCantExplotaciones() != null ? cuestionarios.getCantExplotaciones() : "--"));

                if (cuestionarios.getErroresEstructura() != null
                        && cuestionarios.getErroresEstructura().length() > 4)
                    holder.vInconsistenciasP.setBackgroundColor(Color.RED);
                else
                    holder.vInconsistenciasP.setBackgroundColor(Color.BLACK);

                if (cuestionarios.getJefe() == null)
                    holder.tvProductorJefeInfo.setText("");
                else
                    holder.tvProductorJefeInfo.setText(cuestionarios.getJefe().trim());

                holder.rvExplotaciones.setLayoutManager(new LinearLayoutManager(holder.mView.getContext()));
                ExplotacionAdapter explotacionAdapter =
                        new ExplotacionAdapter(cuestionariosViviendaHogaresList.get(position),
                                holder.onProductorExplotacionListener);
                holder.rvExplotaciones.setAdapter(explotacionAdapter);
            }
        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder: ", e);
        }
    }

    @Override
    public int getItemCount() {
        if (cuestionariosViviendaHogaresList != null) {
            return cuestionariosViviendaHogaresList.size();
        } else return 0;
    }

    public void setData(List<List<Cuestionarios>> lista) {
        if (lista != null) {
            this.cuestionariosViviendaHogaresList = lista;
        }
        notifyDataSetChanged();
    }

    public interface OnProductorExplotacionListener extends ExplotacionAdapter.OnExplotacionListener {
        void onProductorExplotacionClick(List<Cuestionarios> position, View view);


        void onExplotacionClick(List<Cuestionarios> position, View view, int absoluteAdapterPosition);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        final OnProductorExplotacionListener onProductorExplotacionListener;
        //        private final TextView tvVHSegmentoInfo;
//        private final TextView tvVHDivisonInfo;
        private final TextView tvProductorInfo;
        private final View vInconsistenciasP;
        //        private final TextView tvVHHogarInfo;
        private final TextView tvProductorJefeInfo;
        private final MaterialCardView cvProductorExplotacion;
        private final RecyclerView rvExplotaciones;

        public ViewHolder(View view, OnProductorExplotacionListener onProductorExplotacionListener) {
            super(view);
            mView = view;
//            tvVHSegmentoInfo = mView.findViewById(R.id.tvVHSegmentoInfo);
            tvProductorInfo = mView.findViewById(R.id.tvProductorInfo);
            tvProductorJefeInfo = mView.findViewById(R.id.tvProductorJefeInfo);
            vInconsistenciasP = mView.findViewById(R.id.vInconsistenciasP);
//            tvVHHogarInfo = mView.findViewById(R.id.tvVHHogarInfo);

            cvProductorExplotacion = mView.findViewById(R.id.cvProductorExplotacion);
//            ImageButton ibAddExplotacion = mView.findViewById(R.id.ibAddExplotacion);
            rvExplotaciones = mView.findViewById(R.id.rvExplotaciones);

//            ibAddExplotacion.setOnClickListener(this);
            cvProductorExplotacion.setOnClickListener(this);

            this.onProductorExplotacionListener = onProductorExplotacionListener;
        }

        @Override
        public void onClick(View v) {
            List<Cuestionarios> cuestionarioSeleccionado =
                    cuestionariosViviendaHogaresList.get(getBindingAdapterPosition());
            if (v.getId() == R.id.cvProductorExplotacion)
                onProductorExplotacionListener.onProductorExplotacionClick(cuestionarioSeleccionado, v);
//            else if (v.getId() == R.id.ibAddExplotacion) {
//                String datosJson = cuestionarioSeleccionado.get(0).getDatosJson();
//                if (datosJson != null) {
//                    JsonObject jsonObject = new JsonParser().parse(datosJson).getAsJsonObject();
//
//                    if (cuestionarioSeleccionado.get(0).getEstado() > 0) {
//                        if (jsonObject.get("REC_VIVIENDA").getAsJsonObject()
//                                .get("V01_TIPO").getAsString().matches("^([1-4]|[0][1-4])$")
//                                && jsonObject.get("REC_VIVIENDA").getAsJsonObject()
//                                .get("V02_COND").getAsString().matches("^([1]|[0][1])$")) {//Cambiar espacios
//
//                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//                            builder.setTitle("Creación de hogar");
//                            builder.setMessage("¿Desea crear una nueva explotación?");
//                            builder.setPositiveButton("Crear", (dialog, which) -> {
//                                SimpleDateFormat sdf =
//                                        new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
//                                Date date = new Date();
//                                String fecha = sdf.format(date);
//                                int numExplotacion = (Integer.parseInt(cuestionarioSeleccionado.get(
//                                        cuestionarioSeleccionado.size() - 1).getExplotacion())) + 1;
//                                String numProductor = cuestionarioSeleccionado.get(0).getProductor();
//
//                                int explotacionCuestionario;
//                                if (jsonObject.has("REC_VIVIENDA")) {
//                                    if (jsonObject.get("REC_VIVIENDA").getAsJsonObject().has("V17_NHOG")
//                                            && jsonObject.get("REC_VIVIENDA").getAsJsonObject()
//                                            .get("V17_NHOG").getAsString().matches("^([1-9])$")) {
//
//
//                                        explotacionCuestionario = jsonObject.get("REC_VIVIENDA")
//                                                .getAsJsonObject().get("V17_NHOG").getAsInt();
//                                        if (numExplotacion <= explotacionCuestionario) {
//                                            if (cuestionarioSeleccionado.get(cuestionarioSeleccionado.size() - 1)
//                                                    .getEstado() >= 1) {
//                                                Cuestionarios cuestionaroNuevo = new Cuestionarios(
//                                                        segmentos.getId() + segmentos.getEmpadronadorID() + numProductor + numExplotacion,
//                                                        segmentos.getId(),
//                                                        segmentos.getProvinciaID(),
//                                                        segmentos.getDistritoID(),
//                                                        segmentos.getCorregimientoID(),
//                                                        segmentos.getSubZonaID(),
//                                                        segmentos.getSegmentoID(),
//                                                        segmentos.getDivisionID(),
//                                                        SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_CODIGO),
//                                                        segmentos.getEmpadronadorID(),
//                                                        null,
//                                                        cuestionarioSeleccionado.get(0).getVivienda(),
//                                                        cuestionarioSeleccionado.get(0).getHogar(),
//                                                        numProductor,
//                                                        String.valueOf(numExplotacion),
//                                                        null,
//                                                        null,
//                                                        null,
//                                                        0,
//                                                        fecha,
//                                                        null,
//                                                        fecha,
//                                                        null,
//                                                        false,
//                                                        false,
//                                                        ""
//                                                );
//                                                cuestionarioSeleccionado.add(cuestionaroNuevo);
//                                                productorExplotacionViewModel.addProductor(cuestionaroNuevo);
//                                                explotacionAdapter.setData(cuestionarioSeleccionado);
//                                            } else {
//                                                MaterialAlertDialogBuilder materialAlertDialogBuilder =
//                                                        new MaterialAlertDialogBuilder(activity);
//                                                materialAlertDialogBuilder.setTitle("Creación de hogar.");
//                                                materialAlertDialogBuilder.setMessage("Debe terminar con el hogar anterior para" +
//                                                        " proceder a crear uno nuevo.");
//                                                materialAlertDialogBuilder.setIcon(R.drawable.ic_dialog_warning);
//                                                materialAlertDialogBuilder.setPositiveButton("OK", (dialog1, which1) -> dialog1.dismiss());
//                                                materialAlertDialogBuilder.show();
//                                            }
//                                        } else {
//                                            MaterialAlertDialogBuilder materialAlertDialogBuilder =
//                                                    new MaterialAlertDialogBuilder(activity);
//                                            materialAlertDialogBuilder.setTitle("Creación de hogar.");
//                                            materialAlertDialogBuilder.setMessage("Solo hay " + explotacionCuestionario + " hogar(es) en la vivienda " +
//                                                    "(Pregunta 17- Número de hogares)");
//                                            materialAlertDialogBuilder.setIcon(R.drawable.ic_dialog_warning);
//                                            materialAlertDialogBuilder.setPositiveButton("OK", (dialog12, which12) -> dialog12.dismiss());
//                                            materialAlertDialogBuilder.show();
//                                        }
//
//                                    } else {
//                                        MaterialAlertDialogBuilder materialAlertDialogBuilder =
//                                                new MaterialAlertDialogBuilder(activity);
//                                        materialAlertDialogBuilder.setTitle("Creación de hogar.");
//                                        materialAlertDialogBuilder.setMessage("No puede crear hogar adicional.");
//                                        materialAlertDialogBuilder.setIcon(R.drawable.ic_dialog_warning);
//                                        materialAlertDialogBuilder.setPositiveButton("OK", (dialog12, which12) -> dialog12.dismiss());
//                                        materialAlertDialogBuilder.show();
//                                    }
//                                }
//                            });
//
//                            builder.setNegativeButton("Cancelar", (dialog, which) -> Log.i(TAG, "onClick: Cancelado."));
//                            AlertDialog alertDialog = builder.create();
//                            alertDialog.show();
//                        } else {
//                            MaterialAlertDialogBuilder materialAlertDialogBuilder =
//                                    new MaterialAlertDialogBuilder(activity);
//                            materialAlertDialogBuilder.setTitle("Creación de hogar.");
//                            materialAlertDialogBuilder.setMessage("No puede crear hogar adicional para esta vivienda.");
//                            materialAlertDialogBuilder.setIcon(R.drawable.ic_dialog_warning);
//                            materialAlertDialogBuilder.setPositiveButton("OK", (dialog12, which12) -> dialog12.dismiss());
//                            materialAlertDialogBuilder.show();
//                        }
//                    } else {
//                        MaterialAlertDialogBuilder materialAlertDialogBuilder =
//                                new MaterialAlertDialogBuilder(activity);
//                        materialAlertDialogBuilder.setTitle("Creación de hogar.");
//                        materialAlertDialogBuilder.setMessage("Debe completar la vivienda principal para agregar un nuevo hogar.");
//                        materialAlertDialogBuilder.setIcon(R.drawable.ic_dialog_warning);
//                        materialAlertDialogBuilder.setPositiveButton("OK", (dialog12, which12) -> dialog12.dismiss());
//                        materialAlertDialogBuilder.show();
//                    }
//                } else {
//                    Snackbar.make(itemView, "El cuestionario no ha sido iniciado.", Snackbar.LENGTH_SHORT)
//                            .show();
//                }
//            }
        }
    }
}
