package com.example.proyectofinalencuesta_bdubicuo.ui.menu2_survey.report;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinalencuesta_bdubicuo.R;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Recorridos;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.ControlRecorrido;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Cuestionarios;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Segmentos;
import com.example.proyectofinalencuesta_bdubicuo.utils.JsonQueriesCenso;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class ReportFragment extends Fragment {

    private static final String TAG = "ReportFragment";
    private List<Recorridos> recorridos;
    private ReportViewModel reportViewModel;
    //    private ReportSegmentosAdapter reportSegmentosAdapter;
    private ReportSubZonaTotAdapter reportSubZonaTotAdapter;
    private ReportCen01Adapter reportCen01Adapter;
    private RecorridosAdapter recorridosAdapter;
    private List<String> subZonasListSpinner;
    private List<String> segmentosListSpinner;
    private List<Integer> reporteTotales;
    private List<Integer> reporteCuestionarioTotales;
    private ArrayAdapter<String> subzonasArrayAdapter;
    private ArrayAdapter<String> segmentosArrayAdapter;
    private Spinner spinnerReportSegmentos;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        reportViewModel = new ViewModelProvider(this).get(ReportViewModel.class);
        spinnerReportSegmentos = view.findViewById(R.id.spinnerReportSegmentos);
        subZonasListSpinner = new ArrayList<>();
        reporteTotales = new ArrayList<>();
        reporteCuestionarioTotales = new ArrayList<>();
        recorridos = new ArrayList<>();
        setDataSpinner(view);

        RecyclerView rvSubZonaTotal = view.findViewById(R.id.rvTotZona);
        RecyclerView rvRecorridos = view.findViewById(R.id.rvRecorridos);
        RecyclerView rvReporteCuestionarios = view.findViewById(R.id.rvReporteCuestionarios);

        rvSubZonaTotal.setLayoutManager(new GridLayoutManager(getContext(), 2));
        reportSubZonaTotAdapter = new ReportSubZonaTotAdapter(reporteTotales);
        rvSubZonaTotal.setAdapter(reportSubZonaTotAdapter);

        rvReporteCuestionarios.setLayoutManager(new LinearLayoutManager(getContext()));
        reportCen01Adapter = new ReportCen01Adapter(reporteCuestionarioTotales);
        rvReporteCuestionarios.setAdapter(reportCen01Adapter);

        rvRecorridos.setLayoutManager(new LinearLayoutManager(getContext()));
        recorridosAdapter = new RecorridosAdapter(recorridos);
        rvRecorridos.setAdapter(recorridosAdapter);
        return view;
    }

    private void setDataSpinner(View view) {
        Spinner spinnerReporteSubzona = view.findViewById(R.id.spinnerReporteSubzona);

        subzonasArrayAdapter = new ArrayAdapter<>(
                requireActivity(),
                R.layout.support_simple_spinner_dropdown_item,
                subZonasListSpinner
        );

        //Listar todas las subzonas y guardar en subZonasListSpinner
        reportViewModel.getAllSubZonas().observe(requireActivity(), segmentos -> {
            if (segmentos != null) {
                subZonasListSpinner.clear();
                for (Segmentos segmentos1 : segmentos) {
                    subZonasListSpinner.add("Subzona " + segmentos1.getSubZonaID());
                }
                subzonasArrayAdapter.notifyDataSetChanged();
            }
        });

        //Condiguración de spinner y se añade el adaptador que contiene la lista para el spinner
        subzonasArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerReporteSubzona.setAdapter(subzonasArrayAdapter);

        spinnerReporteSubzona.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String subZonaSelect = subZonasListSpinner.get(position).substring(8);
                actualizarSegmentos(subZonaSelect);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i(TAG, "onNothingSelected: ");
            }
        });
    }

    private void actualizarSegmentos(String subZonaSelect) {
        segmentosListSpinner = new ArrayList<>();
        segmentosArrayAdapter = new ArrayAdapter<>(
                requireActivity(),
                R.layout.support_simple_spinner_dropdown_item,
                segmentosListSpinner
        );

        //Lista los segmentos de la subzona seleccionada
        reportViewModel.getSegmentosSelectedGroup(subZonaSelect).observe(this, segmentos -> {
            segmentosListSpinner.clear();
            segmentosListSpinner.add("TODOS");
//            if (segmentos.size() != 0) {
//                segmentosList = segmentos;
////                reportSegmentosAdapter.setData(segmentosList);
//            }

            for (Segmentos segmentos1 : segmentos) {
                segmentosListSpinner.add("Segmento " + String.format("%s - %s - %s",
                        segmentos1.getId().substring(0, 6),
                        segmentos1.getId().substring(6, 10),
                        segmentos1.getId().substring(10, 12)));
            }
            segmentosArrayAdapter.notifyDataSetChanged();
        });

        segmentosArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerReportSegmentos.setAdapter(segmentosArrayAdapter);

        spinnerReportSegmentos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, View view, int position, long id) {
                String segmentoSelect;
                if (segmentosListSpinner.get(position).equals("TODOS"))
                    segmentoSelect = segmentosListSpinner.get(position);
                else
                    segmentoSelect = segmentosListSpinner.get(position).substring(9);
                actualizarTotales(segmentoSelect.replace(" - ", ""), subZonaSelect);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i(TAG, "onNothingSelected: ");
            }
        });
    }

    private void actualizarTotales(String segmentoSelect, String subZonaSelect) {
        if (segmentoSelect.equals("TODOS")) {
            reportViewModel.getAllRecorridosByZona(subZonaSelect).observe(this, controlRecorrido -> {
                reporteTotales.clear();
                reporteCuestionarioTotales.clear();
                if (controlRecorrido != null) {
//                    getTotalesQueries(cuestionarios, true);
                    getTotalReportesRecorrido(controlRecorrido, true);
                    reportViewModel.getAllCuestionariosByZona(subZonaSelect).observe(this, cuestionarios -> {
                        if (cuestionarios != null) {
                            getTotalReportesCuestionario(cuestionarios);
                        }
                    });
                }

                reportSubZonaTotAdapter.setData(reporteTotales);
                reportCen01Adapter.setDataCuestionarios(reporteCuestionarioTotales);
            });

        } else {
            reportViewModel.getRecorridosBySegmentos(segmentoSelect).observe(this, controlRecorridos -> {
                reporteTotales.clear();
                reporteCuestionarioTotales.clear();
                recorridos.clear();
                if (controlRecorridos != null) {
//                    getTotalesQueries(cuestionarios, false);
                    getTotalReportesRecorrido(controlRecorridos, true);
                    reportViewModel.getAllCuestionariosBySegmentoReport(segmentoSelect).observe(this, cuestionarios -> {
                        if (cuestionarios != null) {
                            getTotalReportesCuestionario(cuestionarios);
                        }
                    });
                }
                reportSubZonaTotAdapter.setData(reporteTotales);
                reportCen01Adapter.setDataCuestionarios(reporteCuestionarioTotales);
            });
        }
    }

    private void getTotalesQueries(List<Cuestionarios> cuestionarios, boolean flag) {
        Recorridos recorridos1;
        int totViviendaParticular = 0;
        int ocupadas = 0;
        int personasAusentes = 0;
        int desocupadas = 0;
//        int hogaresAdicionales = 0;
        int hombres = 0;
        int mujeres = 0;
        int totHombres = 0;
        int totMujeres = 0;
        int totPersonas = 0;

        if (!cuestionarios.isEmpty()) {
            for (Cuestionarios c : cuestionarios) {
                if (c.getEstado() > 0) {

//                    if (c.getHogar().equals("1")) {
//                        totViviendaParticular += 1;
//                    }
//
//                    if (Integer.parseInt(c.getHogar()) > 1 && c.getEstado() > 0) {
//                        hogaresAdicionales += 1;
//                    }

                    if (c.getDatosJson() != null) {//0810020721002021 02 1
                        JsonObject cuestionarioObject = new JsonParser().parse(c.getDatosJson()).getAsJsonObject();

                        try {
                            ocupadas += JsonQueriesCenso.INSTANCE.IsViviendaParticularOcupadaReporte(cuestionarioObject) ? 1 : 0;
                            personasAusentes += JsonQueriesCenso.INSTANCE.IsViviendaOcupantesAusentes(cuestionarioObject) ? 1 : 0;
                            desocupadas += JsonQueriesCenso.INSTANCE.IsViviendaDesocupada(cuestionarioObject) ? 1 : 0;
                      /*      if (Integer.parseInt(c.getHogar()) > 0) {
                                int hogares = JsonQueriesCenso.INSTANCE.GetTotalHogaresDeclarados(cuestionarioObject) - 1;
                                hogaresAdicionales += Math.max(hogares, 0);
                            }*/
                            hombres = JsonQueriesCenso.INSTANCE.GetTotalHombresDeclarados(cuestionarioObject);
                            totHombres += hombres;
                            mujeres = JsonQueriesCenso.INSTANCE.GetTotalMujeresDeclaradas(cuestionarioObject);
                            totMujeres += mujeres;
                        } catch (Exception e) {
                            Log.e(TAG, "actualizarTotales: " + e.getMessage());
                        }
                        String condicion = "";

                        try {
                            if (cuestionarioObject.getAsJsonObject("REC_VIVIENDA") == null
                                /*&& cuestionarioObject.getAsJsonObject("REC_VIVIENDA").get("V01_TIPO") == null*/) {
                                Recorridos rec = recorridos.get(recorridos.size() - 1);
                                rec.setTotalPersonas(String.valueOf(Integer.parseInt(rec.getTotalPersonas()) + hombres + mujeres));
                                rec.setTotHombres(String.valueOf(Integer.parseInt(rec.getTotHombres()) + hombres));
                                rec.setTotMujeres(String.valueOf(Integer.parseInt(rec.getTotMujeres()) + mujeres));
                                recorridos.set(recorridos.size() - 1, rec);
                            } else {
                                if (cuestionarioObject.getAsJsonObject("REC_VIVIENDA") != null) {
                                    if (cuestionarioObject.getAsJsonObject("REC_VIVIENDA").get("V02_COND") != null) {
                                        String V02_COND = cuestionarioObject.getAsJsonObject("REC_VIVIENDA")
                                                .get("V02_COND").getAsString();

                                        if (V02_COND != null) {
                                            if (V02_COND.matches("^([1]|[0][1])$"))
                                                condicion = "Ocupada";
                                            else if (V02_COND.matches("^([3-7]|[0][3-7])$"))
                                                condicion = "Desocupada";
                                            else if (V02_COND.matches("^([2]|[0][2])$"))
                                                condicion = "Ausente";
                                        }
                                    }

                                    if (cuestionarioObject.getAsJsonObject("REC_VIVIENDA").get("V01_TIPO") != null
                                            && !cuestionarioObject.getAsJsonObject("REC_VIVIENDA").get("V01_TIPO").getAsString().isEmpty()) {
                                        String V01_TIPO = String.valueOf(cuestionarioObject.getAsJsonObject("REC_VIVIENDA")
                                                .get("V01_TIPO").getAsInt());
                                        if (V01_TIPO.matches("^([5-9]|[1][0-6])$")) {
                                            condicion = "No particular";
                                        }
                                    }
                                }

                                recorridos1 = new Recorridos(c.getLlaveCuestionario(),
                                        c.getVivienda(),
                                        condicion,
                                        String.valueOf((hombres + mujeres)),
                                        String.valueOf(hombres),
                                        String.valueOf(mujeres));
                                recorridos.add(recorridos1);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "getTotalesQueries: ");
                        }
                    }
                }
                totPersonas = totHombres + totMujeres;
            }
            if (!flag)
                recorridosAdapter.setData(recorridos);
            else {
                recorridos.clear();
                recorridosAdapter.setData(recorridos);
            }
        } else {
            recorridos.clear();
            recorridosAdapter.setData(recorridos);
        }
        reporteTotales.add(totViviendaParticular);
        reporteTotales.add(ocupadas);
        reporteTotales.add(personasAusentes);
        reporteTotales.add(desocupadas);
//        reporteTotales.add(hogaresAdicionales);
        reporteTotales.add(totPersonas);
        reporteTotales.add(totHombres);
        reporteTotales.add(totMujeres);
    }

    private void getTotalReportesRecorrido(List<ControlRecorrido> controlRecorridos, boolean flag) {
//        Recorridos recorridos1;
        int totalViviendas = 0;
        int totalViviendasOcupadas = 0;
        int totalViviendasAusentes = 0;
        int totalViviendasDesocupadas = 0;
        int totalViviendasDejoDeSer = 0;
        int totalHogares = 0;

        if (!controlRecorridos.isEmpty()) {
            for (ControlRecorrido crFor : controlRecorridos) {
                if (crFor.getCondicionID() >= 1 && crFor.getCondicionID() <= 4) {
                    totalViviendas++;
                }

                if (crFor.getCondicionID() == 1)//Total de viviendas: Sumatoria de la columna 1
                    totalViviendasOcupadas++;

                if (crFor.getCondicionID() == 2)//Viviendas ocupadas: Sumatoria de la columna 2
                    totalViviendasAusentes++;

                if (crFor.getCondicionID() == 3)//Viviendas desocupadas: Sumatoria de la columna 4
                    totalViviendasDesocupadas++;

                if (crFor.getCondicionID() == 4)//Viviendas desocupadas: Sumatoria de la columna 4
                    totalViviendasDejoDeSer++;

                //Total de hogares: Sumatoria de la columna 5
//                if (crFor.getHogar() != null && Integer.parseInt(crFor.getHogar()) > 0)
//                    totalHogares++;
            }
        }
        reporteTotales.add(totalViviendas);
        reporteTotales.add(totalViviendasOcupadas);
        reporteTotales.add(totalViviendasAusentes);
        reporteTotales.add(totalViviendasDesocupadas);
        reporteTotales.add(totalViviendasDejoDeSer);
//        reporteTotales.add(totalHogares);
//        reporteTotales.add(totalProductores);
//        reporteTotales.add(totalExplotaciones);
    }

    private void getTotalReportesCuestionario(List<Cuestionarios> cuestionarios) {
        int totalProductores = 0;
        int totalExplotaciones= 0;
        if (cuestionarios != null && !cuestionarios.isEmpty()) {
            for (Cuestionarios cuestionariosFor : cuestionarios) {
                //Total de productores: Sumatoria de la columna 19
                if (cuestionariosFor.getExplotacionNum() != null
                        && Integer.parseInt(cuestionariosFor.getExplotacionNum()) == 1
                        && cuestionariosFor.getEstado() > 0)
                    totalProductores++;

                if (cuestionariosFor.getExplotacionNum() != null
                        && cuestionariosFor.getEstado() > 0)
                    totalExplotaciones++;
            }
        }
        reporteCuestionarioTotales.add(totalProductores);
        reporteCuestionarioTotales.add(totalExplotaciones);
        reportCen01Adapter.notifyDataSetChanged();
    }
}
