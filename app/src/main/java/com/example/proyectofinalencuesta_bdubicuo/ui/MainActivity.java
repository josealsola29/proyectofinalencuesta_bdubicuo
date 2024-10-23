package com.example.proyectofinalencuesta_bdubicuo.ui;

import static com.example.proyectofinalencuesta_bdubicuo.data.local.constants.AppConstants.fileAccessHelper;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.proyectofinalencuesta_bdubicuo.R;
import com.example.proyectofinalencuesta_bdubicuo.data.local.constants.AppConstants;
import com.example.proyectofinalencuesta_bdubicuo.data.local.constants.SharedPreferencesManager;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.ControlRecorridoBackup;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.CuestionariosBackup;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.LogErrors;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Segmentos;
import com.example.proyectofinalencuesta_bdubicuo.utils.CsproJson;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import gov.census.cspro.csentry.fileaccess.FileAccessHelper;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    BottomNavigationView bottomNavigationView;
    private List<LogErrors> logErrorsList;
    private List<ControlRecorridoBackup> controlRecorridoBackups;
    private List<Segmentos> segmentosList;
    private List<CuestionariosBackup> cuestionariosBackups;
    private ConfigurationLoginDialogViewModel configurationLoginDialogViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fileAccessHelper = new FileAccessHelper(this);
        setContentView(R.layout.activity_main);
//        this.setTitle("orueba");
        configurationLoginDialogViewModel =
                new ViewModelProvider(this).get(ConfigurationLoginDialogViewModel.class);

        initDiccionario();
        Map<String, Integer> mapita = new HashMap<>();
        mapita.put("divpol.dat", R.raw.divpol);
        mapita.put("cultivos.dat", R.raw.cultivos);
        mapita.put("plantas.dat", R.raw.plantas);
        mapita.put("maquinaria.dat", R.raw.maquinaria);
        mapita.put("catalogo_47.dat", R.raw.catalogo_47);
        mapita.put("catalogo_48.dat", R.raw.catalogo_48);
        mapita.put(AppConstants.NOMBRE_PFF, R.raw.cna__2024);
        mapita.put(AppConstants.NOMBRE_PEN, R.raw.cna_2024);

        for (Map.Entry<String, Integer> entry : mapita.entrySet()) {
            createUtilsAssets(entry.getKey(), entry.getValue());
        }


        bottomNavigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_surveys, R.id.navigation_sendData/*,
                R.id.navigation_network, R.id.navigation_backup*/)
                .build();

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavController navController = Objects.requireNonNull(navHostFragment).getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        configurationLoginDialogViewModel.getLogErrors().observe(this, logErrors -> {
            if (logErrors != null) {
                logErrorsList = logErrors;
            }
        });

        configurationLoginDialogViewModel.getLogsControlRecorrido().observe(this, logErrors -> {
            if (logErrors != null) {
                controlRecorridoBackups = logErrors;
            }
        });

        configurationLoginDialogViewModel.getLogsCuestionarios().observe(this, logErrors -> {
            if (logErrors != null) {
                cuestionariosBackups = logErrors;
            }
        });

        configurationLoginDialogViewModel.getAllSegmentosFromBD().observe(this, segmentos -> {
            if (segmentos != null)
                segmentosList = segmentos;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int seleccionado = item.getItemId();
        if (seleccionado == R.id.option_logout) {
            finish();
            return true;
        } else if (seleccionado == R.id.option_log) {
            showAlertDialgoLogs();
            return true;
        } else if (seleccionado == R.id.option_logDelete) {
            showAlertDialgoLogsDelete();
            return true;
        } else if (seleccionado == R.id.option_actualizarEstado) {
            showAlertDialgoActualizarEstados();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAlertDialgoActualizarEstados() {
        MaterialAlertDialogBuilder materialAlertDialogBuilder =
                new MaterialAlertDialogBuilder(this);
        materialAlertDialogBuilder.setTitle("Actualización de estados para segmentos");
        materialAlertDialogBuilder.setMessage("¿Desea actualizar los estados de sus segmentos?");
        materialAlertDialogBuilder.setIcon(R.drawable.ic_dialog_warning);
        materialAlertDialogBuilder.setPositiveButton("Actualizar", (dialogInterface, i) -> {
            if (!segmentosList.isEmpty()) {
                configurationLoginDialogViewModel.actualizarEstadosFromServidor(segmentosList,
                        SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_USERNAME),
                        SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_FECHA));
            } else
                Toast.makeText(MainActivity.this, "No hay segmentos para actualizar.",
                        Toast.LENGTH_SHORT).show();
        });

        materialAlertDialogBuilder.setNegativeButton("Cancelar", null);
        materialAlertDialogBuilder.show();
    }

    private void showAlertDialgoLogs() {
        StringBuilder logs = new StringBuilder();
        if (logErrorsList != null && !logErrorsList.isEmpty()) {
            for (int x = 0; x < logErrorsList.size(); x++) {
                logs.append("-").append(logErrorsList.get(x).getFechaError())
                        .append(": ")
                        .append(logErrorsList.get(x).getLlave())
                        .append(" / ")
                        .append(logErrorsList.get(x).getError())
                        .append("\n");
            }
        } else {
            logs.append("No hay errores guardados.");
        }
        MaterialAlertDialogBuilder materialAlertDialogBuilder =
                new MaterialAlertDialogBuilder(this);
        materialAlertDialogBuilder.setTitle("Lista de errores de red");
        materialAlertDialogBuilder.setMessage(logs);
        materialAlertDialogBuilder.setIcon(R.drawable.ic_dialog_warning);
        materialAlertDialogBuilder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        materialAlertDialogBuilder.show();
    }

    private void showAlertDialgoLogsDelete() {
        StringBuilder logsControlRecorrido = new StringBuilder();
        logsControlRecorrido.append("Control Recorrido \n");
        StringBuilder logsCuestionarios = new StringBuilder();
        logsCuestionarios.append("Cuestionarios \n");
        if (controlRecorridoBackups != null && !controlRecorridoBackups.isEmpty()) {
            for (int x = 0; x < controlRecorridoBackups.size(); x++) {
                logsControlRecorrido.append("--").append(controlRecorridoBackups.get(x).getLlaveRecorrido())
                        .append(" | ")
                        .append(controlRecorridoBackups.get(x).getFechaModificacionCR())
                        .append("\n");
            }
        } else {
            logsControlRecorrido.append("No hay control recorridos eliminados.");
        }

        if (cuestionariosBackups != null && !cuestionariosBackups.isEmpty()) {
            for (int x = 0; x < cuestionariosBackups.size(); x++) {
                logsCuestionarios.append("--").append(cuestionariosBackups.get(x).getLlaveCuestionario())
                        .append(" | ")
                        .append(cuestionariosBackups.get(x).getFechaModificacion())
                        .append("\n");
            }
        } else {
            logsCuestionarios.append("No hay cuestionarios eliminados.");
        }
        MaterialAlertDialogBuilder materialAlertDialogBuilder =
                new MaterialAlertDialogBuilder(this);
        materialAlertDialogBuilder.setTitle("Lista de registros eliminados");
        materialAlertDialogBuilder.setMessage(logsControlRecorrido + "\n\n" + logsCuestionarios);
        materialAlertDialogBuilder.setIcon(R.drawable.ic_dialog_warning);
        materialAlertDialogBuilder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        materialAlertDialogBuilder.show();
    }

    private void createUtilsAssets(String rawName, int rawID) {
//        InputStream inPEN = getResources().openRawResource(R.raw.cenagrop2023);
//        InputStream inPEN = getResources().openRawResource(R.raw.cen2020);
        InputStream inputStream = getResources().openRawResource(rawID);
        FileOutputStream fileOutputStream;
        try {
//            File directory = requireActivity().getFilesDir();
//            File directory = getExternalFilesDir(null);
            File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            String inecMovilDirectory = directory + "/InecMovil";

            //CREACIÓN DE DIRECTORIO INECMOVIL : DATA/COM.EXAMPLE.CENSO/FILE/INECMOVIL
            if (!new File(inecMovilDirectory).exists()) {
                if (new File(inecMovilDirectory).mkdir()) {
                    Log.i(TAG, "createUtilsAssets: Se creo el directorio InecMovil");
                } else
                    Log.e(TAG, "createUtilsAssets: No se creo el directorio InecMovil");
            }

            //CREACIÓN DE DIRECTORIO PROGRAMAS /INECMOVIL/PROGRAMAS
            if (!new File(inecMovilDirectory + "/PROGRAMAS/").exists()) {
                if (new File(inecMovilDirectory + "/PROGRAMAS/").mkdir()) {
                    Log.i("¡Exito!: Creado SA", "Se creo el directorio PROGRAMAS");
                } else
                    Log.e("¡Error:No Creado SA !", "No se creo el directorio PROGRAMAS");
            }

            fileOutputStream = new FileOutputStream(inecMovilDirectory + "/PROGRAMAS/" + rawName);
//            outPEN = new FileOutputStream(Environment.getExternalStorageDirectory() + "/Android/data/"
//                    + AppConstants.APP_CSPRO + "/files/csentry/" + AppConstants.NOMBRE_PEN);

            byte[] buff = new byte[1024];
            int read;
            while ((read = inputStream.read(buff)) > 0) {
                fileOutputStream.write(buff, 0, read);
            }
            inputStream.close();
            fileOutputStream.close();
            fileAccessHelper.pushFiles(
                    inecMovilDirectory + "/PROGRAMAS/",
                    rawName,
                    "/.",
                    false,
                    true,
                    strings -> null
                    , s -> null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initDiccionario() {
        InputStream inputStreamDiccionario = getResources().openRawResource(R.raw.diccionario);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(inputStreamDiccionario,
                    StandardCharsets.UTF_8));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (Exception e) {
            Log.e(TAG, "onActivityResult: " + e.getMessage());
        } finally {
            try {
                inputStreamDiccionario.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String jsonString = writer.toString();
        AppConstants.csproJson = new CsproJson(jsonString);
    }
}
