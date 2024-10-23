package com.example.proyectofinalencuesta_bdubicuo.ui.menu2_survey.capture.cen01_viviendas.productor;

import static com.example.proyectofinalencuesta_bdubicuo.data.local.constants.AppConstants.fileAccessHelper;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinalencuesta_bdubicuo.R;
import com.example.proyectofinalencuesta_bdubicuo.data.local.constants.AppConstants;
import com.example.proyectofinalencuesta_bdubicuo.data.local.constants.SharedPreferencesManager;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.ControlRecorrido;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Cuestionarios;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Segmentos;
import com.example.proyectofinalencuesta_bdubicuo.utils.ProcessNotifier;
import com.example.proyectofinalencuesta_bdubicuo.utils.Utilidad;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ProductorExplotacionDialogFragment extends DialogFragment
        implements ProductorExplotacionAdapter.OnProductorExplotacionListener, View.OnClickListener {
    private ProductorExplotacionViewModel productorExplotacionViewModel;

    private final File downloadDirectory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    private final String inecMovilDirectory = downloadDirectory + "/InecMovil";
    private final String dataDirectory = inecMovilDirectory + "/DATA";

    private final String csEntryDirectory = Environment.getExternalStorageDirectory()
            + "/Android/data/"
            + AppConstants.APP_CSPRO;
    ActivityResultLauncher<Intent> activityResultLauncher;
    private String inputDataPath;
    private String inputDataPath2;
    private String idDAT;

    private Cuestionarios cuestionarioSelected;

    private final FragmentActivity activity;
    private final ControlRecorrido controlRecorridoSelected;
    private final Segmentos segmentos;
    private final String TAG = "ProductorExplotacionDialogFragment";
    private View view;
    private boolean flagHogaresObserver;
    private boolean flagEliminarObserver;
    private ProcessNotifier processNotifier;
    private List<Cuestionarios> productorList;
    private List<Cuestionarios> explotacionList;
    private List<List<Cuestionarios>> productorExplotacionList;
    private ProductorExplotacionAdapter productorExplotacionAdapter;

    public ProductorExplotacionDialogFragment(FragmentActivity requireActivity, Segmentos segmentos,
                                              ControlRecorrido viviendasHogaresSelected) {
        this.activity = requireActivity;
        this.controlRecorridoSelected = viviendasHogaresSelected;
        this.segmentos = segmentos;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        flagHogaresObserver = true;
//        flagEliminarObserver = true;
        flagEliminarObserver = false;
        processNotifier = new ProcessNotifier(activity);

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result ->
                        fileAccessHelper.pullFiles("/.",
                                "C" + idDAT,
                                inputDataPath2,
                                true,
                                true,
                                strings -> {
                                    String csproPath = csEntryDirectory + "/files/csentry/C" + idDAT;
                                    if (new File(csproPath + ".csnot").exists()) {
                                        pullFilesCsProToDevice("C" + idDAT + ".csnot");
                                        startCuestionarioResults(true);
                                    } else
                                        startCuestionarioResults(false);

                                    if (new File(csproPath + ".lst").exists())
                                        pullFilesCsProToDevice("C" + idDAT + ".lst");

                                    if (new File(csproPath + ".sts").exists())
                                        pullFilesCsProToDevice("C" + idDAT + ".sts");

                                    if (new File(csproPath + ".csidx").exists())
                                        pullFilesCsProToDevice("C" + idDAT + ".csidx");

                                    if (new File(csproPath + ".csnot").exists())
                                        pullFilesCsProToDevice("C" + idDAT + ".csnot");

                                    return null;
                                }, s -> null));

        // Inflate the layout to use as dialog or embedded fragment
        view = inflater.inflate(R.layout.dialogfragment_productores, container, false);
        productorExplotacionViewModel = new ViewModelProvider(this).get(ProductorExplotacionViewModel.class);

        TextView tvTituloSubzonaP = view.findViewById(R.id.tvTituloSubzonaP);
        TextView tvTituloSubzonaPVivienda = view.findViewById(R.id.tvTituloSubzonaPVivienda);
//        TextView tvTituloSubzonaPHogar = view.findViewById(R.id.tvTituloSubzonaPHogar);
        TextView tvTituloSegmentoP = view.findViewById(R.id.tvTituloSegmentoP);

        tvTituloSubzonaP.setText(String.format("Subzona: %s", controlRecorridoSelected.getSubzona()));
        tvTituloSubzonaPVivienda.setText(String.format("Vivienda: %s", controlRecorridoSelected.getVivienda()));
//        tvTituloSubzonaPHogar.setText(String.format("Hogar: %s", controlRecorridoSelected.getHogar()));
//        tvTituloSubzonaPHogar.setText("Hogar: " + controlRecorridoSelected.getHogar());
        tvTituloSegmentoP.setText(String.format("Segmento: %s - %s - %s",
                segmentos.getId().substring(0, 6),
                segmentos.getId().substring(6, 10),
                segmentos.getId().substring(10, 12)));

//        Button btnAgregarProductor = view.findViewById(R.id.btnAgregarProductor);
//        ImageView ivClose = view.findViewById(R.id.ivCloseViviendasHogar);

//        btnAgregarProductor.setOnClickListener(this);
//        ivClose.setOnClickListener(v -> dismiss());

        productorList = new ArrayList<>();
        productorExplotacionList = new ArrayList<>();

        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.rvProductorExplotacion);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        productorExplotacionAdapter = new ProductorExplotacionAdapter(/*productorExplotacionViewModel,*/
                productorExplotacionList, this);
        recyclerView.setAdapter(productorExplotacionAdapter);

        setData();
        return view;
    }

    private void setData() {
        productorExplotacionViewModel.getCuestionariosBySegmentoVivienda(
                controlRecorridoSelected.getLlaveRecorrido()).observe(
                activity, viviendas -> {
                    if (viviendas != null) {
                        productorList = viviendas;
                        if (flagHogaresObserver)
                            getHogaresObserve();
                        if (!flagEliminarObserver)
                            flagEliminarObserver = true;
                    }
                });
    }

    private void getHogaresObserve() {
        try {
            productorExplotacionViewModel.getCuestionariosBySegmento(controlRecorridoSelected.getLlaveRecorrido())
                    .observe(activity, explotaciones -> {
                        if (explotaciones != null && productorList != null) {
//                            productorList = hogares;
                            if (flagEliminarObserver) {

                                flagHogaresObserver = false;
                                List<List<Cuestionarios>> productorExplotacionListfin = new ArrayList<>();
                                for (int x = 0; x < productorList.size(); x++) {
                                    explotacionList = new ArrayList<>();
                                    for (int z = 0; z < explotaciones.size(); z++) {
                                        if (productorList.get(x).getProductor()
                                                .equals(explotaciones.get(z).getProductor())) {
                                            explotacionList.add(explotaciones.get(z));
                                        }
                                    }
                                    productorExplotacionListfin.add(explotacionList);
                                }
                                productorExplotacionList = productorExplotacionListfin;
                                productorExplotacionAdapter.setData(productorExplotacionList);
//                                if (productorExplotacionList.size() < 1)
//                                    productorExplotacionViewModel.updateEstadoSegmento(segmentos.getId());
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "getHogaresObserve: ");
        }
    }

    @Override
    public void onClick(View v) {
//        int opcion = v.getId();
//        if (opcion == R.id.btnAgregarProductor) {
//            String numProductor;
//            SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
//            Date date = new Date();
//            String fecha = sdf.format(date);
//            int numProductorSelected;
//
//            if (productorExplotacionList.size() == 0) {
//                numProductor = "01";
//            } else {
//                numProductorSelected = Integer.parseInt(productorExplotacionList.get(
//                        productorExplotacionList.size() - 1).get(0).getProductor());
//                if (numProductorSelected < 9)
//                    numProductor = "0" + (Integer.parseInt(productorExplotacionList.get(
//                            productorExplotacionList.size() - 1).get(0).getProductor()) + 1);
//                else
//                    numProductor = String.valueOf(Integer.parseInt(productorExplotacionList.get(
//                            productorExplotacionList.size() - 1).get(0).getProductor()) + 1);
//            }
//
//            if (productorExplotacionList.size() > 0) {
//                if (productorExplotacionList.get(productorExplotacionList.size() - 1)
//                        .get(0).getEstado() >= 1) {
//                    productorExplotacionViewModel.addProductor(getNuevoProductor(numProductor, fecha));
//                    productorExplotacionAdapter.notifyDataSetChanged();
//                } else {
//                    Utilidad.showMessageDialog(
//                            "Creación de vivienda",
//                            "Ya tiene una vivienda creada sin capturar.",
//                            true, activity, R.raw.error_sign);
//                }
//            } else {
//                productorExplotacionViewModel.addProductor(getNuevoProductor(numProductor, fecha));
//                productorExplotacionAdapter.notifyDataSetChanged();
//            }
//        }
    }

    @Override
    public void onProductorExplotacionClick(List<Cuestionarios> cuestionariosSelect, View v) {
        PopupMenu popup = new PopupMenu(activity, v);

        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(item -> {
            this.cuestionarioSelected = cuestionariosSelect.get(0);
            if (item.getItemId() == R.id.popup_revisarexplotacion) {
//                if (this.cuestionarioSelected.getCodigoECenso() == null) {
                if (abrirCsPro(this.cuestionarioSelected))
                    Log.d(TAG, "onViviendaHogarClick: revise el cuestionario.");
//                } else {
//                    Toast.makeText(activity,
//                            "Acceso no permitido, este cuestionario esta habilitado para Ecenso",
//                            Toast.LENGTH_SHORT).show();
//                }
                return true;
            } /*else if (item.getItemId() == R.id.popup_listarerrores) {
                showErrorsDialog();
                return true;
            }*/ else if (item.getItemId() == R.id.popup_eliminarcuestionario) {
                if (cuestionariosSelect.size() <= 1) {
                    if (this.cuestionarioSelected.getEstado() == 0) {
                        flagHogaresObserver = true;
                        showDeleteDialog(this.cuestionarioSelected);
                    } else if (this.cuestionarioSelected.getEstado() > 0) {
                        flagHogaresObserver = true;
                        showConfirmDeleteCuestionarioDialog(this.cuestionarioSelected);
                    } else
                        Toast.makeText(activity, "Eliminación no autorizada, el cuestionario tiene datos.",
                                Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "Debe eliminar primero las explotaciones adicionales.", Toast.LENGTH_SHORT).show();
                }
                return true;
            } else if (item.getItemId() == R.id.popup_recuperar_cuestionario) {
                showConfirmBackupCuesitonario(cuestionarioSelected);
                return true;
            } else
                return false;
        });
        popup.inflate(R.menu.menu_popup_productorexplotacion);
        popup.show();
    }


    @Override
    public void onExplotacionClick(List<Cuestionarios> cuestionario, View v, int position) {
        PopupMenu popup = new PopupMenu(activity, v);

        popup.setOnMenuItemClickListener(item -> {
            cuestionarioSelected = cuestionario.get(position);
            if (item.getItemId() == R.id.popup_revisarexplotacion) {
                if (abrirCsPro(cuestionarioSelected))
                    Log.d(TAG, "onViviendaHogarClick: revise el cuestionario.");
                return true;
            } /*else if (item.getItemId() == R.id.popup_listarerrores) {
                showErrorsDialog();
                return true;
            }*/ else if (item.getItemId() == R.id.popup_eliminarcuestionario) {
                if (cuestionario.get(0).getCantExplotaciones() != null
                        && cuestionario.size() > cuestionario.get(0).getCantExplotaciones()) {
                    if (cuestionarioSelected.getEstado() == 0)
                        showDeleteDialog(cuestionarioSelected);
                    else if (this.cuestionarioSelected.getEstado() > 0) {
                        flagHogaresObserver = true;
                        showConfirmDeleteCuestionarioDialog(this.cuestionarioSelected);
                    } else
                        Toast.makeText(activity, "Eliminación no autorizada, el cuestionario tiene datos.",
                                Toast.LENGTH_SHORT).show();
                } else {
                    MaterialAlertDialogBuilder madConfirmacion =
                            new MaterialAlertDialogBuilder(requireActivity());
                    madConfirmacion.setTitle("Eliminación de cuestionario");
                    madConfirmacion.setMessage("Error de eliminado, el cuestionario base mantiene " +
                            cuestionario.get(0).getCantExplotaciones() + " explotaciones declaradas " +
                            "en la pregunta 6 del cuestionario.");
                    madConfirmacion.setPositiveButton("OK)", null);
                    madConfirmacion.show();
                }
                return true;
            } else if (item.getItemId() == R.id.popup_recuperar_cuestionario) {
                recuperarCuestionarioIndividualServer(cuestionarioSelected);
                return true;
            } else
                return false;
        });
        popup.inflate(R.menu.menu_popup_productorexplotacion);
        popup.show();
    }

    private void showConfirmBackupCuesitonario(Cuestionarios cuestionarioSelected) {
        MaterialAlertDialogBuilder madConfirmacion =
                new MaterialAlertDialogBuilder(requireActivity());
        madConfirmacion.setTitle("Recuperación de cuestionario");
        madConfirmacion.setMessage("¿Desea proceder a realizar la recuperación del cuestionario "
                + cuestionarioSelected.getLlaveCuestionario() + "? " +
                "Recuerde que si procede con esta operación el sistema borrará el cuestionario " +
                "en BD y guardará el que viene del servidor.");
        View viewInflated = getLayoutInflater()
                .inflate(R.layout.alertdialog_confirmacion_descargacuest, null);
        madConfirmacion.setView(viewInflated);
        final EditText editTextConfirmPss = viewInflated.findViewById(R.id.editTextConfirmPss);
        madConfirmacion.setPositiveButton("Aceptar", (dialog, which) -> {
            String password = editTextConfirmPss.getText().toString().trim();
            if (!password.equals(AppConstants.CODIGO_BACKUP))
                Toast.makeText(requireActivity(), "Contraseña incorrecta ",
                        Toast.LENGTH_SHORT).show();
            else
                recuperarCuestionarioIndividualServer(cuestionarioSelected);
        });
        madConfirmacion.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
        madConfirmacion.show();
    }

    private void recuperarCuestionarioIndividualServer(Cuestionarios cuestionarioSelected) {
//        Toast.makeText(activity, "En construcción >:3 . . .", Toast.LENGTH_SHORT).show();
        ProcessNotifier processNotifier = new ProcessNotifier(activity);
        processNotifier.setTitle("Recuperación de cuestionario");
        processNotifier.setText("Recuperando... ");
        productorExplotacionViewModel.recuperarCuestionarioSeleccionadoServer(cuestionarioSelected, processNotifier, dataDirectory);
    }

  /*  private void showErrorsDialog() {
        if (cuestionarioSelected.getErroresEstructura() != null
                && !cuestionarioSelected.getErroresEstructura().trim().isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(R.string.popup_listarerrores);
            builder.setMessage(cuestionarioSelected.getErroresEstructura());
            builder.setPositiveButton("OK", (dialog, which) -> Toast.makeText(activity,
                    "Lista de errores cerrado.", Toast.LENGTH_SHORT).show());
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            Toast.makeText(requireActivity(), "No hay errores encontrado por el sistema.",
                    Toast.LENGTH_SHORT).show();
        }
    }*/

    private void showDeleteDialog(Cuestionarios cuestionarioSelected) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.popup_eliminarcuestionario);
        builder.setMessage("¿Desea eliminar este cuestionario?");
        builder.setPositiveButton("Aceptar", (dialog, which) -> {
            flagEliminarObserver = false;
            processNotifier = new ProcessNotifier(activity);
            productorExplotacionViewModel.deleteCuestionario("local", processNotifier, segmentos,
                    cuestionarioSelected, activity);
        });
        builder.setNegativeButton("Cancelar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showConfirmDeleteCuestionarioDialog(Cuestionarios cuestionarioSelected) {
        if (verificarRed()) {
            MaterialAlertDialogBuilder madConfirmacion =
                    new MaterialAlertDialogBuilder(requireActivity());
            madConfirmacion.setTitle("Eliminación de cuestionario");
            madConfirmacion.setMessage("¿Desea eliminar un cuestionario con datos?\n" +
                    "Productor: " + cuestionarioSelected.getProductor() + "  Explotación: " + cuestionarioSelected.getExplotacionNum());
            View viewInflated = getLayoutInflater()
                    .inflate(R.layout.alertdialog_confirmacion_descargacuest, null);
            madConfirmacion.setView(viewInflated);
            final EditText editTextConfirmPss = viewInflated.findViewById(R.id.editTextConfirmPss);
            madConfirmacion.setPositiveButton("Aceptar", (dialog, which) -> {
                String password = editTextConfirmPss.getText().toString().trim();
                if (!password.equals(AppConstants.CODIGO_ELIMINAR_CUESTIONARIO))
                    Toast.makeText(requireActivity(), "Contraseña incorrecta ",
                            Toast.LENGTH_SHORT).show();
                else {
                    flagEliminarObserver = false;
                    processNotifier = new ProcessNotifier(activity);
                    productorExplotacionViewModel.deleteCuestionario("remoto", processNotifier, segmentos,
                            cuestionarioSelected, activity);
                }
            });
            madConfirmacion.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
            madConfirmacion.show();
        } else
            Toast.makeText(activity, "Debe estar conectado a una red.", Toast.LENGTH_SHORT).show();
    }

    private boolean verificarRed() {
        ConnectivityManager connectivityManager = (ConnectivityManager) requireActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = connectivityManager.getActiveNetwork();
        if (network == null) return false;
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
        return networkCapabilities != null
                && (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
    }

    private boolean abrirCsPro(Cuestionarios cuestionarioCSPro) {
        String datPath = "/ENCUESTADOR/" +
                //EMPADRONADOR
                SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_USERNAME) + "/" +
                //PROVINCIA + DISTRITO + CORREGIMIENTO / SEGMENTO
                controlRecorridoSelected.getSubzona() + "/"
                + controlRecorridoSelected.getProvinciaID()
                + controlRecorridoSelected.getDistritoID()
                + controlRecorridoSelected.getSegmentoID()
                + controlRecorridoSelected.getDivisionID() + "/" +
                //VIVIENDA
                cuestionarioCSPro.getVivienda() + "/";

        //    private String startMode;
        String parametro;
        try {
            //VERIFICACIÓN DE CS ENTRY APP INSTALADO
            if (!(new File(csEntryDirectory).exists())) {
                Utilidad.showMessageDialog("Error de CsPro", "Verificar la instalacion de CSentry, no se " +
                                "encontró el archivo: " + AppConstants.APP_CSPRO,
                        requireActivity(), R.raw.error_sign);
//                Toast.makeText(activity, "Verificar la instalacion de CSentry, no se " +
//                        "encontró el archivo: " + AppConstants.APP_CSPRO, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "abrirCsPro: no se encontró el archivo de CSEntry.");
                return false;
            }

/*            //VERIFICACIÓN DE .PEN INSTALADO
            if (!(new File(penPath).exists())) {
                Toast.makeText(activity, "Verificar la instalación del programa de "
                        + "captura .PEN en /PROGRAMAS/", Toast.LENGTH_SHORT).show();
                return false;
            }*/

            //VERIFICAR SI EXISTE LAS CARPETAS DAT
            if (!(new File(dataDirectory + datPath).exists())) {
                if (new File(dataDirectory + datPath).mkdirs()) {
                    Log.i(TAG, "abrirCsPro: se ha creado la carpeta datPath");
                } else Log.e(TAG, "abrirCsPro: no se ha creado la carpeta");
            }

            String cuestionarioID = cuestionarioCSPro.getProvinciaID()
                    + cuestionarioCSPro.getDistritoID()
                    + cuestionarioCSPro.getCorregimientoID()
                    + cuestionarioCSPro.getSegmento()
                    + cuestionarioCSPro.getDivision()
                    + cuestionarioCSPro.getVivienda()
//                    +"00"
//                    + cuestionarioCSPro.getHogar()
                    + cuestionarioCSPro.getProductor()
                    + cuestionarioCSPro.getExplotacionNum();

            StringBuilder lugarPobladoDesc = parametroExpString(segmentos.getLugarPobladoDescripcion(), 100);
//            StringBuilder barrioDesc = parametroExpString(segmentos.getBarrioDescripcion(), 43);
            StringBuilder provinciaExp = parametroExpString(cuestionarioCSPro.getProvinciaIdExp(), 2);
//            StringBuilder provinciaIdExpDesc = parametroExpString(cuestionarioCSPro.getProvinciaIdExpDesc(), 50);
            StringBuilder distritoIdExp = parametroExpString(cuestionarioCSPro.getDistritoIdExp(), 2);
//            StringBuilder distritoIdExpDesc = parametroExpString(cuestionarioCSPro.getDistritoIdExpDesc(), 50);
            StringBuilder corregimientoIdExp = parametroExpString(cuestionarioCSPro.getCorregimientoIdExp(), 2);
//            StringBuilder corregimientoIdExpDesc = parametroExpString(cuestionarioCSPro.getCorregimientoIdExpDesc(), 50);

           /* String tienePesca;
            if (controlRecorridoSelected.getPregF() == null)
                tienePesca = " ";
            else {
                if (!controlRecorridoSelected.getPregA() &&
                        !controlRecorridoSelected.getPregB() &&
                        !controlRecorridoSelected.getPregC() &&
                        !controlRecorridoSelected.getPregD() &&
                        !controlRecorridoSelected.getPregE() &&
                        controlRecorridoSelected.getPregF())
                    tienePesca = "1";
                else
                    tienePesca = "0";

            }*/
            String maqunaria;
            if (cuestionarioCSPro.getP170maquinaria() != null) {
                if (cuestionarioCSPro.getP170maquinaria() == 1)
                    maqunaria = "1";
                else if (cuestionarioCSPro.getP170maquinaria() == 2)
                    maqunaria = "2";
                else
                    maqunaria = " ";
            } else
                maqunaria = " ";

            parametro = cuestionarioID
                    + segmentos.getLugarPobladoID()
                    + lugarPobladoDesc.substring(0, 100)
                    + segmentos.getRegionID()
                    + segmentos.getZonaID()
                    + cuestionarioCSPro.getNumEmpadronador()
                    + ((controlRecorridoSelected.getPregA() == null) ? " " : (controlRecorridoSelected.getPregA() ? 1 : 0))
                    + ((controlRecorridoSelected.getPregB() == null) ? " " : (controlRecorridoSelected.getPregB() ? 1 : 0))
                    + ((controlRecorridoSelected.getPregC() == null) ? " " : (controlRecorridoSelected.getPregC() ? 1 : 0))
                    + ((controlRecorridoSelected.getPregD() == null) ? " " : (controlRecorridoSelected.getPregD() ? 1 : 0))
                    + ((controlRecorridoSelected.getPregE() == null) ? " " : (controlRecorridoSelected.getPregE() ? 1 : 0))
                    + ((controlRecorridoSelected.getPregF() == null) ? " " : (controlRecorridoSelected.getPregF() ? 1 : 0))//PESCA
                    + ((controlRecorridoSelected.getPregG() == null) ? " " : (controlRecorridoSelected.getPregG() ? 1 : 0))
                    + (controlRecorridoSelected.getCantProductoresAgro() < 10 ? ("0" + controlRecorridoSelected.getCantProductoresAgro()) : controlRecorridoSelected.getCantProductoresAgro())
                    + (controlRecorridoSelected.getCantExplotacionesAgro() < 10 ? ("0" + controlRecorridoSelected.getCantExplotacionesAgro()) : controlRecorridoSelected.getCantExplotacionesAgro())
//                    + controlRecorridoSelected.getCantExplotacionesAgro()
                    + provinciaExp
//                    + provinciaIdExpDesc
                    + distritoIdExp
//                    + distritoIdExpDesc
                    + corregimientoIdExp
                    + maqunaria
//                    + segmentos.getLugarPobladoID()
//                    + "lugarPobladoDesc"
//                    + corregimientoIdExpDesc
//                    + tienePesca
//                    + segmentos.getBarrioID()
//                    + barrioDesc.substring(0, 40)
//                    + segmentos.getArea()
            ;

            idDAT = cuestionarioID + ".dat";
            String idNot = cuestionarioID + ".dat.csnot";
            inputDataPath = dataDirectory + datPath + "C" + idDAT;
            String inputDataPathNot = dataDirectory + datPath + "C" + idNot;
//            inputDataPath2 = csEntryDirectory + "/files/csentry/C" + idDAT;
            inputDataPath2 = dataDirectory + datPath;
//            String startMode = leerLineasDatModo(inputDataPath + ".sts", idDAT.substring(0, 20));
//Si existe en INECMOVIL y si existe en CS Entry Files
            if (new File(inputDataPath).exists() && new File(csEntryDirectory
                    + "/files/csentry/C" + idDAT).exists()) {
                launchIntentCsPro(cuestionarioID, csEntryDirectory, parametro/*, startMode*/);
//                casos = LeerLineas(cuestionarioID, inputDataPath);
            } else if (cuestionarioCSPro.getDatos() != null && !cuestionarioCSPro.getDatos().trim().isEmpty()) {
                crearDat(cuestionarioCSPro.getDatos(), inputDataPath);
                fileAccessHelper.pushFiles(inputDataPath2,
                        "C" + idDAT,
                        "/.",
                        false,
                        true,
                        strings -> {
                            launchIntentCsPro(cuestionarioID, csEntryDirectory, parametro/*, startMode*/);
                            return null;
                        },
                        s -> null);


                if (cuestionarioCSPro.getNotas() != null && !cuestionarioCSPro.getNotas().trim().isEmpty()) {
                    crearDat(cuestionarioCSPro.getNotas(), inputDataPathNot);
                    fileAccessHelper.pushFiles(inputDataPath2,
                            "C" + idNot,
                            "/.",
                            false,
                            true,
                            strings -> {
                                launchIntentCsPro(cuestionarioID, csEntryDirectory, parametro/*, startMode*/);
                                return null;
                            },
                            s -> null);
                }
//                casos = LeerLineas(cuestionarioID, inputDataPath);
            } else {
                launchIntentCsPro(cuestionarioID, csEntryDirectory, parametro/*, startMode*/);
            }
        } catch (Exception e) {
            Log.e(TAG, "abrirCsPro: " + e.getMessage());
            Toast.makeText(activity, "abrirCsPro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private StringBuilder parametroExpString(String parametro, int largo) {
        StringBuilder nuevoParametro;

        if (parametro != null && !parametro.trim().equals("NULL")) {
            nuevoParametro = new StringBuilder(parametro);
            if (nuevoParametro.length() != largo) {
                for (int x = nuevoParametro.length(); x < largo; x++)
                    nuevoParametro.append(" ");
                return nuevoParametro;
            }
        } else {
            nuevoParametro = new StringBuilder();
            if (nuevoParametro.length() != largo) {
                for (int x = 0; x < largo; x++)
                    nuevoParametro.append(" ");
                return nuevoParametro;
            }
        }
        return nuevoParametro;
    }

    private void launchIntentCsPro(String key, String csEntryDirectory, String parametro/*, String startMode*/) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setComponent(new ComponentName(AppConstants.APP_CSPRO,
                    "gov.census.cspro.csentry.ui.EntryActivity"));
            intent.putExtra("PffFilename", AppConstants.NOMBRE_PFF);
            intent.putExtra("Key", key);
//            intent.putExtra("StartMode", "ADD");
            intent.putExtra("InputData", csEntryDirectory + "/files/csentry/C" + idDAT);
//            intent.putExtra("InputData", inputDataPath);
            intent.putExtra("Parameter", parametro);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            activityResultLauncher.launch(intent);
        } catch (Exception e) {
            Log.e(TAG, "abrirCsPro: " + e.getMessage());
        }
    }

    private void crearDat(String datos, String inputDataPath) {
        BufferedWriter bufferedWriter = null;
        FileWriter fileWriter = null;
        try {
            File datFile = new File(inputDataPath);
            if (datFile.createNewFile()) {
                Log.i(TAG, "abrirCsPro: archivo creado");
            }
            fileWriter = new FileWriter(datFile);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(inputDataPath), StandardCharsets.UTF_8));
//            bufferedWriter.write("\uFEFF");
            String data = datos;
            if (!inputDataPath.contains(".dat.csnot"))
                data = "\uFEFF" + data;
            bufferedWriter.write(data);
        } catch (IOException e) {
            Log.e(TAG, "crearDat: ", e);
        } finally {
            try {
                if (bufferedWriter != null)
                    bufferedWriter.close();

                if (fileWriter != null)
                    fileWriter.close();
            } catch (Exception e) {
                Log.e(TAG, "abrirCsPro: " + e.getMessage());
            }
        }
    }

    private void startCuestionarioResults(boolean flagCsNot) {
        StringBuilder fileDats = new StringBuilder();
        StringBuilder fileDatsNot = new StringBuilder();
        String resultado;
        String csNotPath = inputDataPath + ".csnot";
        InputStream inputStream = null;
//        InputStream inputStreamDATNot = null;
        SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
        Date date = new Date();
        String Linea;
        int countLineas = 0;
//            boolean info = false;
        try {
            File datFile = new File(inputDataPath);
            Date lastModified = new Date(datFile.lastModified());

            try {
                inputStream = new FileInputStream(datFile);
                BufferedReader br = new BufferedReader(new FileReader(inputDataPath));
                while ((Linea = br.readLine()) != null && !(Linea.isEmpty())) {
//                        Linea = Utilidad.removeUTF8BOM(Linea);
                    fileDats.append(Linea).append("\n");
//                    fileDats.append(Linea);
                    countLineas++;
                }
                br.close();
            } catch (FileNotFoundException e) {
                Log.e(TAG, "onActivityResult: " + e.getMessage());
            }

            if (flagCsNot) {
                try {
                    //                File csnotFile = new File(csNotPath);
                    //                inputStreamDATNot = new FileInputStream(csnotFile);
                    BufferedReader bufferedReaderNot = new BufferedReader(new FileReader(csNotPath));
                    while ((Linea = bufferedReaderNot.readLine()) != null && !(Linea.isEmpty())) {
                        //                        Linea = Utilidad.removeUTF8BOM(Linea);
                        fileDatsNot.append(Linea).append("\n");
                    }
                    bufferedReaderNot.close();
                } catch (FileNotFoundException e) {
                    Log.e(TAG, "onActivityResult: " + e.getMessage());
                }
            }

            resultado = AppConstants.csproJson.getJson(Objects.requireNonNull(inputStream));
            JsonObject jsonObject = new JsonParser().parse(resultado).getAsJsonObject();
            int comparacionDatos = 1;
            if (cuestionarioSelected.getDatos() != null)
                comparacionDatos = fileDats.substring(1).compareTo(cuestionarioSelected.getDatos());
            cuestionarioSelected.setFechaEntrada(sdf.format(date));

            /*&& info */
//            if (countLineas == 1 && fileDats.length() > 10
//                    && fileDats.substring(1, 2).equals("1") && fileDats.substring(483, 527).trim().isEmpty()) {
            if (countLineas == 1 && jsonObject.size() == 1
                    && (jsonObject.get("CAP_I_1_RESIDENCIA").getAsJsonObject().get("P6_CANTIDAD") == null
                    && jsonObject.get("CAP_I_1_RESIDENCIA").getAsJsonObject().get("P6_CANTIDAD").getAsString().isEmpty())
                    && (jsonObject.get("CAP_I_1_RESIDENCIA").getAsJsonObject().get("CONDICION_EMPA") == null
                    && jsonObject.get("CAP_I_1_RESIDENCIA").getAsJsonObject().get("CONDICION_EMPA").getAsString().isEmpty())
                    && (jsonObject.get("CAP_I_1_RESIDENCIA").getAsJsonObject().get("P08_PROV") == null
                    && jsonObject.get("CAP_I_1_RESIDENCIA").getAsJsonObject().get("P08_PROV").getAsString().isEmpty())
                    && (jsonObject.get("CAP_I_1_RESIDENCIA").getAsJsonObject().get("DIST") == null
                    && jsonObject.get("CAP_I_1_RESIDENCIA").getAsJsonObject().get("DIST").getAsString().isEmpty())
                    && (jsonObject.get("CAP_I_1_RESIDENCIA").getAsJsonObject().get("CORREG") == null
                    && jsonObject.get("CAP_I_1_RESIDENCIA").getAsJsonObject().get("CORREG").getAsString().isEmpty())
                    && (jsonObject.get("CAP_I_1_RESIDENCIA").getAsJsonObject().get("LUG_POB") == null
                    && jsonObject.get("CAP_I_1_RESIDENCIA").getAsJsonObject().get("LUG_POB").getAsString().isEmpty())
                    && (jsonObject.get("CAP_I_1_RESIDENCIA").getAsJsonObject().get("HH_GPS_READING") == null)
                    && jsonObject.get("CAP_I_1_RESIDENCIA").getAsJsonObject().get("HH_GPS_READING").getAsString().isEmpty()) {
                Snackbar.make(view, "Falta información. ", Snackbar.LENGTH_SHORT)
                        .show();
            } else if (fileDats.length() > 10 && comparacionDatos != 0) {

                String jefe;

                if (jsonObject.size() > 0) {
                    cuestionarioSelected.setEstado(getEstadoCuestionario(jsonObject));

                    if (jsonObject.get("CAP_II_III") != null
                            && jsonObject.get("CAP_II_III").getAsJsonArray() != null
                            && jsonObject.get("CAP_II_III").getAsJsonArray().get(0) != null
                            && jsonObject.get("CAP_II_III").getAsJsonArray().get(0)
                            .getAsJsonObject().get("P9_NOM") != null) {
                        jsonObject.get("CAP_II_III").getAsJsonArray().get(0).getAsJsonObject().get("P9_NOM").getAsString();
                        jefe = jsonObject.get("CAP_II_III").getAsJsonArray().get(0)
                                .getAsJsonObject().get("P9_NOM").getAsString();
                        cuestionarioSelected.setJefe(jefe.trim());
                    } else {
                        cuestionarioSelected.setJefe("");
                    }
                    int cantExplotaciones = getCantExplotaciones(jsonObject);
                    if (cantExplotaciones <= 20) {
                        addExplotacionesUpdate(jsonObject, cantExplotaciones);
                        cuestionarioSelected.setCantExplotaciones(cantExplotaciones);

                    } else {
                        MaterialAlertDialogBuilder madConfirmacion =
                                new MaterialAlertDialogBuilder(requireActivity());
                        madConfirmacion.setTitle("Adición de explotaciones");
                        madConfirmacion.setMessage("El número de explotaciones declaradas supera el rango permitido," +
                                " consulte a su supervisor.");
                        madConfirmacion.setPositiveButton("Ok, lo reportaré :)", null);
                        madConfirmacion.show();
                    }

//                    resultado = resultado.replace("\"", "\\\"");
                    cuestionarioSelected.setDatos(fileDats.toString());
                    if (!fileDatsNot.toString().isEmpty())
                        cuestionarioSelected.setNotas(fileDatsNot.toString());
                    else
                        cuestionarioSelected.setNotas(null);
                    cuestionarioSelected.setDatosJson(resultado);
                    cuestionarioSelected.setFechaModificacion(sdf.format(lastModified));

                    processNotifier = new ProcessNotifier(activity);
                    if (cuestionarioSelected.isFlagPrimerEnvio()) {
                        productorExplotacionViewModel.sendCuestionarioUpdate(processNotifier,
                                cuestionarioSelected, segmentos,activity, cuestionarioSelected.getLlaveCuestionario());
                    } else {
//                    primer envio y el normal
                        productorExplotacionViewModel.sendCuestionarioCreate(processNotifier, activity,
                                cuestionarioSelected, segmentos);
                    }
                    productorExplotacionViewModel.addCuestionarioDatosDat(cuestionarioSelected);


                } else {
                    Snackbar.make(view, "El cuestionario no ha sido iniciado.", Snackbar.LENGTH_SHORT)
                            .show();
                    Log.e(TAG, "onActivityResult: El cuestionario no se ha modificado.");
                }
            } else {
                Snackbar.make(view, "El cuestionario no ha sido modificado.", Snackbar.LENGTH_SHORT)
                        .show();
            }
        } catch (Exception e) {
            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, "onActivityResult: " + e.getMessage());
        }
    }

    private int getCantExplotaciones(JsonObject jsonObject) {
        if (jsonObject.get("CAP_I_1_RESIDENCIA") != null
                && jsonObject.get("CAP_I_1_RESIDENCIA").getAsJsonObject().get("P6_CANTIDAD") != null
                && !jsonObject.get("CAP_I_1_RESIDENCIA").getAsJsonObject().get("P6_CANTIDAD").getAsString().trim().isEmpty()) {
            return jsonObject.get("CAP_I_1_RESIDENCIA").getAsJsonObject().get("P6_CANTIDAD").getAsInt();
        }
        return 0;
    }

    private void pullFilesCsProToDevice(String patternFile) {
        try {
            fileAccessHelper.pullFiles("/.",
                    patternFile,
                    inputDataPath2,
                    true,
                    true,
                    strings -> null, s -> null);
        } catch (Exception e) {
            Log.e(TAG, "pullFilesCsProToDevice: ");
        }
    }

    private int getEstadoCuestionario(JsonObject jsonObject) {
        int estado = 1;
        try {
            JsonElement capI_1_Residencia = jsonObject.get("CAP_I_1_RESIDENCIA");
            JsonElement capI_2_Local = jsonObject.get("CAP_I_2_LOCAL");
//            JsonElement cap_II_III = jsonObject.get("CAP_II_III");
//            JsonElement recViviendaObj = jsonObject.get("REC_VIVIENDA");
//            JsonElement recHogarObj = jsonObject.get("REC_HOGAR");
            JsonElement recPersonaObj = jsonObject.get("REC_PERSONA");
//            String regexFive = "^(?:[1-9][0-9]{4}|[1-9][0-9]{3}|[1-9][0-9]{2}|[1-9][0-9]|[1-9])$";//NO ZERO

            if (capI_1_Residencia == null && capI_2_Local == null && recPersonaObj == null /*&& recPersonaObj.getAsJsonArray().size() == 0*/)
                Log.i(TAG, "getEstadoCuestionario: Estado 1 Amarrillo");
            else {
                if (capI_1_Residencia != null && capI_1_Residencia.getAsJsonObject().get("CONDICION_EMPA") != null) {
                    JsonElement condicionEmpa = capI_1_Residencia.getAsJsonObject().get("CONDICION_EMPA");
                    if (condicionEmpa.getAsInt() == 1) {
                        estado = 2; //Completa
                    } else if (condicionEmpa.getAsInt() > 1 && condicionEmpa.getAsInt() <= 5) {
                        estado = 1;
                    }
                }
            }
        } catch (Exception e) {
            Snackbar.make(view, "Error en asignación de ESTADO. " + e.getMessage(), Snackbar.LENGTH_SHORT)
                    .show();
            return estado;
        }
        return estado;
    }


    private void addExplotacionesUpdate(JsonObject jsonObject, int cantExplotaciones) {
//        int totExplotaciones = jsonObject.get("");

        if (cantExplotaciones != 0 && cantExplotaciones <= 10) {
            if (jsonObject.get("CAP_I_2_LOCAL") != null) {
                JsonElement capI_Localizacion = jsonObject.get("CAP_I_2_LOCAL");

                List<Cuestionarios> nuevasExplotaciones = new ArrayList<>();
                for (int x = 0; x < cantExplotaciones; x++) {
                    if (capI_Localizacion.getAsJsonArray().get(x).getAsJsonObject().get("P7_PROV") != null
                            && capI_Localizacion.getAsJsonArray().get(x).getAsJsonObject().get("P7_DIST") != null
                            && capI_Localizacion.getAsJsonArray().get(x).getAsJsonObject().get("P7_CORREG") != null) {
                        int cont = x + 1;
                        String provinciaIDExp = Objects.equals(capI_Localizacion.getAsJsonArray().get(x)
                                .getAsJsonObject().get("P7_PROV").getAsString(), "") ? null :
                                capI_Localizacion.getAsJsonArray().get(x)
                                        .getAsJsonObject().get("P7_PROV").getAsString();
                        String provinciaIDExpDesc = Objects.equals(capI_Localizacion.getAsJsonArray().get(x)
                                .getAsJsonObject().get("P7_PROV_DESC").getAsString(), "") ? null :
                                capI_Localizacion.getAsJsonArray().get(x)
                                        .getAsJsonObject().get("P7_PROV_DESC").getAsString();
                        String distritoIDExp = Objects.equals(capI_Localizacion.getAsJsonArray().get(x)
                                .getAsJsonObject().get("P7_DIST").getAsString(), "") ? null :
                                capI_Localizacion.getAsJsonArray().get(x)
                                        .getAsJsonObject().get("P7_DIST").getAsString();
                        String distritoIDExpDesc = Objects.equals(capI_Localizacion.getAsJsonArray().get(x)
                                .getAsJsonObject().get("P7_DIST_DESC").getAsString(), "") ? null :
                                capI_Localizacion.getAsJsonArray().get(x)
                                        .getAsJsonObject().get("P7_DIST_DESC").getAsString();//TODO
                        String corregimientoIDExp = Objects.equals(capI_Localizacion.getAsJsonArray().get(x)
                                .getAsJsonObject().get("P7_CORREG").getAsString(), "") ? null :
                                capI_Localizacion.getAsJsonArray().get(x)
                                        .getAsJsonObject().get("P7_CORREG").getAsString();
                        String corregimientoIDExpDesc = Objects.equals(capI_Localizacion.getAsJsonArray().get(x)
                                .getAsJsonObject().get("P7_CORREG_DESC").getAsString(), "") ? null :
                                capI_Localizacion.getAsJsonArray().get(x)
                                        .getAsJsonObject().get("P7_CORREG_DESC").getAsString();
                        if (cont == 1) {
                            cuestionarioSelected.setProvinciaIdExp(provinciaIDExp);
                            cuestionarioSelected.setDistritoIdExp(distritoIDExp);
                            cuestionarioSelected.setCorregimientoIdExp(corregimientoIDExp);
                        } else {
                            SimpleDateFormat sdf =
                                    new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                            Date date = new Date();
                            String fecha = sdf.format(date);
                            String numExplotacion;
                            int maquinaria = 0;
                            if (cont < 10)
                                numExplotacion = "0" + cont;
                            else
                                numExplotacion = String.valueOf(cont);
                            if (jsonObject.get("CAP_XI_INFRAESTRUCTURA") != null) {
                                if (jsonObject.get("CAP_XI_INFRAESTRUCTURA")
                                        .getAsJsonArray().get(0).getAsJsonObject().get("P170_MAQUINARIA") != null) {
                                    cuestionarioSelected.setP170maquinaria(jsonObject.get("CAP_XI_INFRAESTRUCTURA")
                                            .getAsJsonArray().get(0).getAsJsonObject().get("P170_MAQUINARIA").getAsInt());
                                }
                            }
                            Cuestionarios cuestionaroNuevo = new Cuestionarios(
                                    cuestionarioSelected.getLlaveCuestionario().substring(0, 16) + numExplotacion,
                                    cuestionarioSelected.getCodigoViviendaHogares(),
                                    segmentos.getId(),
                                    cuestionarioSelected.getProvinciaID(),
                                    cuestionarioSelected.getDistritoID(),
                                    cuestionarioSelected.getCorregimientoID(),
                                    provinciaIDExp,
                                    provinciaIDExpDesc,
                                    distritoIDExp,
                                    distritoIDExpDesc,
                                    corregimientoIDExp,
                                    corregimientoIDExpDesc,
                                    cuestionarioSelected.getProductor(),
                                    numExplotacion,
                                    cuestionarioSelected.getSubzona(),
                                    cuestionarioSelected.getSegmento(),
                                    cuestionarioSelected.getDivision(),
                                    cuestionarioSelected.getEmpadronador(),
                                    cuestionarioSelected.getNumEmpadronador(),
                                    null,
                                    cuestionarioSelected.getVivienda(),
//                                    cuestionarioSelected.getHogar(),
                                    null,
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
                                    null,
                                    cuestionarioSelected.getP170maquinaria()
                            );
                            nuevasExplotaciones.add(cuestionaroNuevo);
                        }
                    }
                }
                productorExplotacionViewModel.addExplotacion(nuevasExplotaciones);
            }
//            jefe = jsonObject.getAsJsonArray("REC_LIST_OCUP")
//                    .get(0).getAsJsonObject().get("LO_NOMBRE").getAsString();
        }
    }

}
