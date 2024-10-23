package com.example.proyectofinalencuesta_bdubicuo.ui.menu2_survey.maps;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.example.proyectofinalencuesta_bdubicuo.R;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Segmentos;

public class EtiquetaDialog extends DialogFragment {
    private final Segmentos segmentos;

    public EtiquetaDialog(FragmentActivity activity, Segmentos segmentos) {
        this.segmentos = segmentos;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_etiqueta, container, false);
//        TextView tvEtiquetaSegmento = v.findViewById(R.id.tvEtiquetaSegmento);
        TextView tvRegionEtiqueta = v.findViewById(R.id.tvRegionEtiqueta);
        TextView tvZonaEtiqueta = v.findViewById(R.id.tvZonaEtiqueta);
        TextView tvSubZonaEtiqueta = v.findViewById(R.id.tvSubZonaEtiqueta);
        TextView tvProvEtiqueta = v.findViewById(R.id.tvProvEtiqueta);
        TextView tvDistritoEtiqueta = v.findViewById(R.id.tvDistritoEtiqueta);
        TextView tvCorregimientoEtiqueta = v.findViewById(R.id.tvCorregimientoEtiqueta);
        TextView tvLugarPEtiqueta = v.findViewById(R.id.tvLugarPEtiqueta);
        TextView tvBarrioEtiqueta = v.findViewById(R.id.tvBarrioEtiqueta);
        TextView tvSegmentoEtiqueta = v.findViewById(R.id.tvSegmentoEtiqueta);
        TextView tvSegmentoDescripcion = v.findViewById(R.id.tvSegmentoDescripcion);

        tvRegionEtiqueta.setText(String.format("Región: %s", segmentos.getRegionID()));
        tvZonaEtiqueta.setText(String.format("Zona: %s", segmentos.getZonaID()));
        tvSubZonaEtiqueta.setText(String.format("SubZona: %s", segmentos.getSubZonaID()));
        tvProvEtiqueta.setText(String.format("Provincia: %s", segmentos.getProvinciaID()));
        tvDistritoEtiqueta.setText(String.format("Distrito: %s", segmentos.getDistritoID()));
        tvCorregimientoEtiqueta.setText(String.format("Corregimiento: %s", segmentos.getCorregimientoID()));
        tvLugarPEtiqueta.setText(String.format("Lugar Poblado: %s", segmentos.getLugarPobladoDescripcion()));
        tvBarrioEtiqueta.setText(String.format("Barrio: %s", segmentos.getBarrioDescripcion()));
        tvSegmentoEtiqueta.setText(String.format("Segmento: %s - %s - %s",
                segmentos.getId().substring(0, 6),
                segmentos.getId().substring(6, 10),
                segmentos.getId().substring(10, 12)));
        tvSegmentoDescripcion.setText(String.format("Detalle: %s", segmentos.getDetalle()));

/*        tvEtiquetaSegmento.setText(String.format("Segmento \n%s - %s - %s - %s - %s",
                segmentos.getProvinciaID(),
                segmentos.getDistritoID(),
                segmentos.getCorregimientoID(),
                segmentos.getSegmentoID(),
                segmentos.getDivisionID()));*/

//        RecyclerView recyclerView = v.findViewById(R.id.rvEtiqueta);
//        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
//        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
//        EtiquetaAdapter etiquetaAdapter = new EtiquetaAdapter(infoEtiquete, tipoEtiquete, activity);
//        recyclerView.setAdapter(etiquetaAdapter);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
