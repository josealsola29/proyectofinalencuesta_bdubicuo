package com.example.proyectofinalencuesta_bdubicuo.data;

import static com.example.proyectofinalencuesta_bdubicuo.utils.Utilidad.deleteCsProFiles;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;

import com.example.proyectofinalencuesta_bdubicuo.R;
import com.example.proyectofinalencuesta_bdubicuo.app.MyApp;
import com.example.proyectofinalencuesta_bdubicuo.data.local.ControlRecorridoDao;
import com.example.proyectofinalencuesta_bdubicuo.data.local.CuestionariosDao;
import com.example.proyectofinalencuesta_bdubicuo.data.local.SegmentosDao;
import com.example.proyectofinalencuesta_bdubicuo.data.local.constants.AppConstants;
import com.example.proyectofinalencuesta_bdubicuo.data.local.constants.SharedPreferencesManager;
import com.example.proyectofinalencuesta_bdubicuo.data.local.constants.clients.CensoClient;
import com.example.proyectofinalencuesta_bdubicuo.data.local.constants.clients.MapClient;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.ControlRecorrido;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.ControlRecorridoBackup;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Cuestionarios;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.CuestionariosBackup;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.CuestionariosPendientes;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.LogErrors;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Segmentos;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.TotCuestionarios;
import com.example.proyectofinalencuesta_bdubicuo.data.remote.ApiResponse;
import com.example.proyectofinalencuesta_bdubicuo.data.remote.NetworkBoundResource;
import com.example.proyectofinalencuesta_bdubicuo.data.remote.networkEntities.GetInconsistenciasResponse;
import com.example.proyectofinalencuesta_bdubicuo.data.remote.networkEntities.GetRefreshEstado;
import com.example.proyectofinalencuesta_bdubicuo.data.remote.networkEntities.GetSegmentosResponse;
import com.example.proyectofinalencuesta_bdubicuo.data.remote.networkEntities.PostCuestionarioEnvio;
import com.example.proyectofinalencuesta_bdubicuo.data.remote.networkEntities.PostEnviarRecorrido;
import com.example.proyectofinalencuesta_bdubicuo.data.remote.networkServices.CensoApiService;
import com.example.proyectofinalencuesta_bdubicuo.data.remote.networkServices.CensoMapService;
import com.example.proyectofinalencuesta_bdubicuo.data.repo.CensoDataBase;
import com.example.proyectofinalencuesta_bdubicuo.utils.AppExecutors;
import com.example.proyectofinalencuesta_bdubicuo.utils.ProcessNotifier;
import com.example.proyectofinalencuesta_bdubicuo.utils.RateLimiter;
import com.example.proyectofinalencuesta_bdubicuo.utils.Resource;
import com.example.proyectofinalencuesta_bdubicuo.utils.Utilidad;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CensoRepository {
    private static final String TAG = "CensoRepository";
    private static CensoRepository instance;
    private final CensoApiService censoApiService;
    private final CensoMapService censoApiServiceMap;
    //    private final UsuariosDao usuariosDao;
    private final SegmentosDao segmentosDao;
    private final CuestionariosDao cuestionariosDAO;
    private final ControlRecorridoDao controlRecorridoDAO;
    private final AppExecutors appExecutors;
    private final RateLimiter<String> repoListRateLimit = new RateLimiter<>(30, TimeUnit.MINUTES);
    private String msgResponse;

    public CensoRepository() {
        appExecutors = AppExecutors.getInstance();
        CensoDataBase directorioRoomDatabase = CensoDataBase.getDataBase(MyApp.getInstance());
//        usuariosDao = directorioRoomDatabase.getUserDAO();
        segmentosDao = directorioRoomDatabase.getSegmentosDAO();
        cuestionariosDAO = directorioRoomDatabase.getCuestionariosDAO();
        controlRecorridoDAO = directorioRoomDatabase.getViviviendasDAO();

        CensoClient censoClient = CensoClient.getInstance();
        MapClient mapClient = MapClient.getInstance();

        censoApiService = censoClient.getCensoApiService();
        censoApiServiceMap = mapClient.getCensoMapService();
    }

    public static CensoRepository getInstance() {
        if (instance == null) {
            instance = new CensoRepository();
        }
        return instance;
    }

    public LiveData<Resource<List<Segmentos>>> getSegmentosFromServer() {
        return new NetworkBoundResource<List<Segmentos>, List<GetSegmentosResponse>>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull List<GetSegmentosResponse> responseSeg) {
                if (!responseSeg.isEmpty()) {
                    List<Segmentos> segmentosNuevos = new ArrayList<>();
                    for (int x = 0; x < Objects.requireNonNull(responseSeg).size(); x++) {
                        int idAsignacionAlterna = 0;
                        if (responseSeg.get(x).getAsignacionAlterna() != null) {
                            idAsignacionAlterna = responseSeg.get(x).getAsignacionAlterna().getId();
                        }
                        Segmentos segmento = new Segmentos(responseSeg.get(x).getId(),
                                responseSeg.get(x).getRegionID(),
                                responseSeg.get(x).getZonaID(),
                                responseSeg.get(x).getSubZonaID(),
                                responseSeg.get(x).getProvinciaID(),
                                responseSeg.get(x).getDistritoID(),
                                responseSeg.get(x).getCorregimientoID(),
                                responseSeg.get(x).getLugarPobladoID(),
                                responseSeg.get(x).getLugarPobladoDescripcion(),
                                responseSeg.get(x).getBarrioID(),
                                responseSeg.get(x).getBarrioDescripcion(),
                                responseSeg.get(x).getSegmentoID(),
                                responseSeg.get(x).getDivisionID(),
                                responseSeg.get(x).getEstado(),
                                responseSeg.get(x).getCuestionarios(),
                                responseSeg.get(x).isInconsistencias(),
                                responseSeg.get(x).getEmpadronadorID(),
                                responseSeg.get(x).getFechaDeHabilitacion(),
                                responseSeg.get(x).getFechaUltimaCarga(),
                                responseSeg.get(x).getFechaDeRevisado(),
                                responseSeg.get(x).getFechaUltimaCarga(),
                                responseSeg.get(x).getFechaUltimaCarga(),
                                responseSeg.get(x).getAsignacionPrincipal(),
                                idAsignacionAlterna,
                                responseSeg.get(x).getProvNombre(),
                                responseSeg.get(x).getDistNombre(),
                                responseSeg.get(x).getCorregNombre(),
                                responseSeg.get(x).getArea(),
                                responseSeg.get(x).getDetalle(),
                                responseSeg.get(x).getContingencia(),
                                responseSeg.get(x).getTipoEmp(),
                                responseSeg.get(x).getViviendas()

                        );
                        segmentosNuevos.add(segmento);
                    }
                    segmentosDao.insertSegmentos(segmentosNuevos);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Segmentos> data) {
                return data == null || data.isEmpty() ||
                        repoListRateLimit.shouldFetch(
                                SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_USERNAME));
            }

            @NonNull
            @Override
            protected LiveData<List<Segmentos>> loadFromDb() {
                return segmentosDao.loadSegmentos();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<GetSegmentosResponse>>> createCall() {
                return censoApiService.getSegmentosAsignados(
                        SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_USERNAME));
            }

            @Override
            protected void onFetchFailed() {
                repoListRateLimit.reset(SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_USERNAME));
            }
        }.asLiveData();
    }

    /**
     * Si es 1, desvuelve solamente los segmentos en estado 1 de la BD del servidor.
     *
     * @return LiveData<Resource < List < Segmentos>>>
     */
    public LiveData<Resource<List<Segmentos>>> getSegmentosNuevos(List<Segmentos> segmentosListBD) {
        return new NetworkBoundResource<List<Segmentos>, List<GetSegmentosResponse>>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull @NotNull List<GetSegmentosResponse> responseSeg) {
                if (!responseSeg.isEmpty()) {
                    List<Segmentos> segmentosNuevos = new ArrayList<>();
                    List<Segmentos> segmentsListDelete = new ArrayList<>();
                    Segmentos segmentoEliminar = null;
                    for (int i = 0; i < segmentosListBD.size(); i++) {
                        for (int x = 0; x < responseSeg.size(); x++) {
                            if (segmentosListBD.get(i).getId().equals(responseSeg.get(x).getId())) {
                                segmentoEliminar = null;
                                break;
                            } else {
                                if (Integer.parseInt(segmentosListBD.get(i).getEstado()) <= 1)
                                    segmentoEliminar = segmentosListBD.get(i);
                            }
                        }
                        if (segmentoEliminar != null) segmentsListDelete.add(segmentoEliminar);
                    }

                    if (!segmentsListDelete.isEmpty())
                        segmentosDao.deleteSegmentos(segmentsListDelete);

                    for (int x = 0; x < Objects.requireNonNull(responseSeg).size(); x++) {
                        int idAsignacionAlterna = 0;
                        if (responseSeg.get(x).getAsignacionAlterna() != null) {
                            idAsignacionAlterna = responseSeg.get(x).getAsignacionAlterna().getId();
                        }
                        Segmentos segmento = new Segmentos(responseSeg.get(x).getId(),
                                responseSeg.get(x).getRegionID(),
                                responseSeg.get(x).getZonaID(),
                                responseSeg.get(x).getSubZonaID(),
                                responseSeg.get(x).getProvinciaID(),
                                responseSeg.get(x).getDistritoID(),
                                responseSeg.get(x).getCorregimientoID(),
                                responseSeg.get(x).getLugarPobladoID(),
                                responseSeg.get(x).getLugarPobladoDescripcion(),
                                responseSeg.get(x).getBarrioID(),
                                responseSeg.get(x).getBarrioDescripcion(),
                                responseSeg.get(x).getSegmentoID(),
                                responseSeg.get(x).getDivisionID(),
                                responseSeg.get(x).getEstado(),
                                responseSeg.get(x).getCuestionarios(),
                                responseSeg.get(x).isInconsistencias(),
                                responseSeg.get(x).getEmpadronadorID(),
                                responseSeg.get(x).getFechaDeHabilitacion(),
                                responseSeg.get(x).getFechaModificacion(),
                                responseSeg.get(x).getFechaDeRevisado(),
                                responseSeg.get(x).getFechaUltimaCarga(),
                                responseSeg.get(x).getFechaUltimaCarga(),
                                responseSeg.get(x).getAsignacionPrincipal(),
                                idAsignacionAlterna,
                                responseSeg.get(x).getProvNombre(),
                                responseSeg.get(x).getDistNombre(),
                                responseSeg.get(x).getCorregNombre(),
                                responseSeg.get(x).getArea(),
                                responseSeg.get(x).getDetalle(),
                                responseSeg.get(x).getContingencia(),
                                responseSeg.get(x).getTipoEmp(),
                                responseSeg.get(x).getViviendas());
                        segmentosNuevos.add(segmento);
                    }
                    segmentosDao.insertSegmentosAdicionales(segmentosNuevos);
                } else {
                    segmentosDao.deleteAllSegmentosEstado1();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Segmentos> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Segmentos>> loadFromDb() {
                return segmentosDao.loadSegmentosFaltantesBackup();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<GetSegmentosResponse>>> createCall() {
                return censoApiService
                        .getSegmentosAsignadosNuevosFiltro(SharedPreferencesManager
                                .getSomeStringValue(AppConstants.PREF_USERNAME), "1");
            }

            @Override
            protected void onFetchFailed() {
                repoListRateLimit.reset(SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_USERNAME));
            }
        }.asLiveData();
    }

    public LiveData<Resource<List<Cuestionarios>>> getCuestionariosBackup() {
        return new NetworkBoundResource<List<Cuestionarios>, List<Cuestionarios>>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull @NotNull List<Cuestionarios> item) {
                for (int x = 0; x < item.size(); x++) {
                    item.get(x).setFlagPrimerEnvio(true);
                    item.get(x).setCodigoViviendaHogares(item.get(x).getLlaveCuestionario().substring(0, 14));
                }
                cuestionariosDAO.insertCuestionarios(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable @org.jetbrains.annotations.Nullable List<Cuestionarios> data) {
                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch(SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_USERNAME));
            }

            @NonNull
            @NotNull
            @Override
            protected LiveData<List<Cuestionarios>> loadFromDb() {
                return cuestionariosDAO.getAllCuestionarios();
            }

            @NonNull
            @NotNull
            @Override
            protected LiveData<ApiResponse<List<Cuestionarios>>> createCall() {
                return censoApiService.getCuestionarios(
                        SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_USERNAME));
            }

            @Override
            protected void onFetchFailed() {
                repoListRateLimit.reset(SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_USERNAME));
            }

        }.asLiveData();
    }

    public LiveData<Resource<List<ControlRecorrido>>> getControlRecorridoBackup() {
        return new NetworkBoundResource<List<ControlRecorrido>, List<ControlRecorrido>>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull @NotNull List<ControlRecorrido> item) {
                controlRecorridoDAO.insertarControlRecorridoBackupFromServer(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable @org.jetbrains.annotations.Nullable List<ControlRecorrido> data) {
                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch(SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_USERNAME));
            }

            @NonNull
            @NotNull
            @Override
            protected LiveData<List<ControlRecorrido>> loadFromDb() {
                return controlRecorridoDAO.getAllControlRecorridos();
            }

            @NonNull
            @NotNull
            @Override
            protected LiveData<ApiResponse<List<ControlRecorrido>>> createCall() {
                return censoApiService.getRecorridosByEmp(
                        SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_USERNAME));
            }

            @Override
            protected void onFetchFailed() {
                repoListRateLimit.reset(SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_USERNAME));
            }

        }.asLiveData();
    }

    /***
     * Si es 0, desvuelve todos los segmentos en la BD del servidor.
     * @return LiveData<Resource < List < Segmentos>>>
     */
    public LiveData<Resource<List<Segmentos>>> getSegmentosBackup() {
        return new NetworkBoundResource<List<Segmentos>, List<GetSegmentosResponse>>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull @NotNull List<GetSegmentosResponse> responseSeg) {
                List<Segmentos> segmentosNuevos = new ArrayList<>();
                for (int x = 0; x < Objects.requireNonNull(responseSeg).size(); x++) {
                    int idAsignacionAlterna = 0;
                    if (responseSeg.get(x).getAsignacionAlterna() != null) {
                        idAsignacionAlterna = responseSeg.get(x).getAsignacionAlterna().getId();
                    }
                    Segmentos segmento = new Segmentos(
                            responseSeg.get(x).getId(),
                            responseSeg.get(x).getRegionID(),
                            responseSeg.get(x).getZonaID(),
                            responseSeg.get(x).getSubZonaID(),
                            responseSeg.get(x).getProvinciaID(),
                            responseSeg.get(x).getDistritoID(),
                            responseSeg.get(x).getCorregimientoID(),
                            responseSeg.get(x).getLugarPobladoID(),
                            responseSeg.get(x).getLugarPobladoDescripcion(),
                            responseSeg.get(x).getBarrioID(),
                            responseSeg.get(x).getBarrioDescripcion(),
                            responseSeg.get(x).getSegmentoID(),
                            responseSeg.get(x).getDivisionID(),
                            responseSeg.get(x).getEstado(),
                            responseSeg.get(x).getCuestionarios(),
                            responseSeg.get(x).isInconsistencias(),
                            responseSeg.get(x).getEmpadronadorID(),
                            responseSeg.get(x).getFechaDeHabilitacion(),
                            responseSeg.get(x).getFechaModificacion(),
                            responseSeg.get(x).getFechaDeRevisado(),
                            responseSeg.get(x).getFechaUltimaCarga(),
                            responseSeg.get(x).getFechaUltimaCarga(),
                            responseSeg.get(x).getAsignacionPrincipal(),
                            idAsignacionAlterna,
                            responseSeg.get(x).getProvNombre(),
                            responseSeg.get(x).getDistNombre(),
                            responseSeg.get(x).getCorregNombre(),
                            responseSeg.get(x).getArea(),
                            responseSeg.get(x).getDetalle(),
                            responseSeg.get(x).isInconsistencias(),
                            responseSeg.get(x).getTipoEmp(),
                            responseSeg.get(x).getViviendas()
                    );
                    segmentosNuevos.add(segmento);
                }
                segmentosDao.insertSegmentosAdicionales(segmentosNuevos);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Segmentos> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Segmentos>> loadFromDb() {
                return segmentosDao.loadSegmentosFaltantesBackup();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<GetSegmentosResponse>>> createCall() {
                return censoApiService
                        .getSegmentosAsignadosNuevosFiltro(SharedPreferencesManager
                                .getSomeStringValue(AppConstants.PREF_USERNAME), "");
            }

            @Override
            protected void onFetchFailed() {
                repoListRateLimit.reset(SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_USERNAME));
            }
        }.asLiveData();
    }

    public LiveData<Resource<List<Segmentos>>> getSegmentosDetalleActualizado() {
        return new NetworkBoundResource<List<Segmentos>, List<GetSegmentosResponse>>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull @NotNull List<GetSegmentosResponse> responseSeg) {
//                List<Segmentos> segmentosNuevos = new ArrayList<>();
                for (int x = 0; x < Objects.requireNonNull(responseSeg).size(); x++) {
                    int idAsignacionAlterna = 0;
                    if (responseSeg.get(x).getAsignacionAlterna() != null) {
                        idAsignacionAlterna = responseSeg.get(x).getAsignacionAlterna().getId();
                    }
                    segmentosDao.updateDetalleSegmento(responseSeg.get(x).getDetalle(),
                            responseSeg.get(x).getId(), responseSeg.get(x).getLugarPobladoID(),
                            responseSeg.get(x).getLugarPobladoDescripcion(), responseSeg.get(x).getBarrioID(),
                            responseSeg.get(x).getBarrioDescripcion(), idAsignacionAlterna);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Segmentos> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Segmentos>> loadFromDb() {
                return segmentosDao.loadSegmentosFaltantesBackup();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<GetSegmentosResponse>>> createCall() {
                return censoApiService.getSegmentosAsignadosNuevosFiltro(
                        SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_USERNAME), "0");
            }

            @Override
            protected void onFetchFailed() {
                repoListRateLimit.reset(SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_USERNAME));
            }
        }.asLiveData();
    }

    public LiveData<List<Segmentos>> getAllSubZonas() {
        return segmentosDao.getAllSubZonas();
    }

    public LiveData<List<Segmentos>> getAllSegmentos() {
        return segmentosDao.loadSegmentos();
    }

    public LiveData<List<Segmentos>> getAllSubZonasSend() {
        return segmentosDao.getAllSubZonasSend();
    }

    public LiveData<List<ControlRecorrido>> getAllSubZonasRecorridoSend() {
        return controlRecorridoDAO.getAllSubZonasRecorridoSend();
    }

    public LiveData<List<Segmentos>> getSegmentosSelected(String subZonaSelect, int empID) {
        return segmentosDao.getSegmentosSelected(subZonaSelect, empID);
    }

    public LiveData<List<CuestionariosPendientes>> getSegmentosCuestionariosNoEnviados(String subzona) {
        return segmentosDao.getSegmentosCuestionariosNoEnviados(subzona);
    }

    public LiveData<List<CuestionariosPendientes>> getSegmentosRecorridosNoEnviados(String subzona) {
        return segmentosDao.getSegmentosRecorridosNoEnviados(subzona);
    }

    public LiveData<List<Cuestionarios>> getSegmentosCuestionariosNoEnviados3() {
        return segmentosDao.getSegmentosCuestionariosNoEnviados3();
    }

    public LiveData<List<Segmentos>> getSegmentosCuestionariosNoEnviados2(String subzona) {
        return segmentosDao.getSegmentosCuestionariosNoEnviados2(subzona);
    }

    public LiveData<List<ControlRecorrido>> getRecorridosNoEnviados() {
        return controlRecorridoDAO.getRecorridosNoEnviados();
    }

    public LiveData<List<ControlRecorrido>> getSegmentosRecorridoNoEnviadosIndiv(String subzona) {
        return controlRecorridoDAO.getSegmentosRecorridoNoEnviadosIndiv(subzona);
    }

    public LiveData<List<Segmentos>> getSegmentosSelectedGroup(String subZonaSelect) {
        return segmentosDao.getSegmentosSelectedGroup(subZonaSelect);
    }

    public LiveData<List<Cuestionarios>> getCuestionariosBySegmentoVivienda(String id) {
        return cuestionariosDAO.getCuestionariosBySegmentoVivienda(id);
    }

    public LiveData<List<ControlRecorrido>> getRecorridosPorSegmentoOP(String id) {
        return controlRecorridoDAO.getRecorridosPorSegmentoOP(id);
    }

    public LiveData<List<Cuestionarios>> getTotalCuestionariosCompletadosPendientes(List<String> hogarList) {
        return controlRecorridoDAO.getTotalCuestionariosCompletadosPendientes(hogarList);
    }

    public LiveData<List<ControlRecorrido>> getRecorridosPorSegmento(String id) {
        return controlRecorridoDAO.getRecorridosPorSegmento(id);
    }

    public LiveData<List<Cuestionarios>> getCuestionariosBySegmentoNotSended(String codigoSegmento) {
        return cuestionariosDAO.getCuestionariosBySegmentoNotSended(codigoSegmento);
    }

    public LiveData<List<ControlRecorrido>> getRecorridosBySegmentoNotSended(String codigoSegmento) {
        return controlRecorridoDAO.getSegmentosRecorridoNoEnviados(codigoSegmento);
    }


    public LiveData<List<Cuestionarios>> getAllCuestionariosBySegmentoReport(String id) {
        return cuestionariosDAO.getAllCuestionariosBySegmentoReport(id);
    }

    public LiveData<List<Cuestionarios>> getCuestionariosBySegmento(String id) {
        return cuestionariosDAO.getCuestionariosBySegmento(id);
    }

    public LiveData<List<ControlRecorrido>> getRecorridosBySegmento(String id) {
        return cuestionariosDAO.getRecorridosBySegmento(id);
    }

    public LiveData<List<ControlRecorrido>> getViviendasBySegmento(String id) {
        return controlRecorridoDAO.getViviendasBySegmento(id);
    }

    public LiveData<List<TotCuestionarios>> getAllCuestionarios() {
        return cuestionariosDAO.getAllCuestionariosCapture();
    }

    public LiveData<List<Cuestionarios>> getAllCuestionariosByZona(String subzona) {
        return cuestionariosDAO.getAllCuestionariosByZona(subzona);
    }

    public LiveData<List<ControlRecorrido>> getAllRecorridosByZona(String subzona) {
        return controlRecorridoDAO.getAllRecorridosByZona(subzona);
    }

    public LiveData<List<Segmentos>> getSegmentosMaps() {
        return segmentosDao.getSegmentosMaps();
    }

    public LiveData<List<LogErrors>> getLogErrors() {
        return cuestionariosDAO.getLogErrors();
    }

    public LiveData<List<ControlRecorridoBackup>> getLogsControlRecorrido() {
        return cuestionariosDAO.getLogsControlRecorrido();
    }

    public LiveData<List<CuestionariosBackup>> getLogsCuestionarios() {
        return cuestionariosDAO.getLogsCuestionarios();
    }

/*    public void addVivienda(ControlRecorrido nuevaVivienda) {
        appExecutors.diskIO().execute(() -> controlRecorridoDAO.addVivienda(nuevaVivienda));
    }*/

    public void addRecorrido(ControlRecorrido nuevaVivienda) {
        appExecutors.diskIO().execute(() -> controlRecorridoDAO.addVivienda(nuevaVivienda));
    }

    public void addRecorridoProductor(Cuestionarios cuestionarios) {
        appExecutors.diskIO().execute(() -> controlRecorridoDAO.addRecorridoProductor(cuestionarios));
    }

//    public void updatePreg5Recorrido(boolean preg5, String llaveRecorrido) {
//        appExecutors.diskIO().execute(() -> controlRecorridoDAO
//                .updatePreg5Recorrido(preg5, llaveRecorrido));
//    }

    public void updateCondicionViviendaRecorrido(ControlRecorrido controlRecorrido/*int condicionViv, String llaveRecorrido*/) {
        appExecutors.diskIO().execute(() -> controlRecorridoDAO
                .updateCondicionViviendaRecorrido(controlRecorrido/*condicionViv, llaveRecorrido*/));
    }

/*    public void updateDatosHogar(Integer cantProductoresAgro, String llaveRecorrido) {
        appExecutors.diskIO().execute(() -> controlRecorridoDAO
                .updateDatosHogar(cantProductoresAgro, llaveRecorrido));
    }*/

    public void updateViviendaRecorrido(ControlRecorrido controlRecorrido) {
        appExecutors.diskIO().execute(() -> controlRecorridoDAO
                .updateViviendaRecorrido(controlRecorrido));
    }

/*    public void actualizarVisibilidadLn(String llaveRecorrido, Boolean visibilidad) {
        appExecutors.diskIO().execute(() -> controlRecorridoDAO
                .actualizarVisibilidadLn(llaveRecorrido, visibilidad));
    }*/

    public void guardarControlBateriasHogar(Boolean pregAmfy0708, Boolean pregAfip0910, Boolean pregPmo1112,
                                            Boolean pregGvcc1314, Boolean pregCggp1516, Boolean pregEoco1718,
                                            Boolean preg1920Empresa, String llaveRecorrido) {
        appExecutors.diskIO().execute(() -> controlRecorridoDAO
                .guardarControlBateriasHogar(pregAmfy0708, pregAfip0910, pregPmo1112, pregGvcc1314, pregCggp1516,
                        pregEoco1718, preg1920Empresa, llaveRecorrido));
    }

//    public void updateTotalHogaresRecorrido(ControlRecorrido nuevaVivienda) {
//        appExecutors.diskIO().execute(() -> controlRecorridoDAO.updateTotalHogaresRecorrido(nuevaVivienda));
//    }

//    public void addProductor(Cuestionarios nuevoProductor) {
//        appExecutors.diskIO().execute(() -> cuestionariosDAO.addProductor(nuevoProductor));
//    }

    public void addExplotacion(List<Cuestionarios> nuevoProductor) {
        appExecutors.diskIO().execute(() -> cuestionariosDAO.addExplotacion(nuevoProductor));
    }

    public void addCuestionarioDatosDat(Cuestionarios cuestionarioSelected) {
        appExecutors.diskIO().execute(() -> cuestionariosDAO.addCuestionarioDatosDat(cuestionarioSelected));
    }

    public void actualizarErrorHogar(String s, String l) {
        appExecutors.diskIO().execute(() -> cuestionariosDAO.updateErrorHogar(s, l));
    }

    public void updateEstadoSegmento(String id) {
        appExecutors.diskIO().execute(() -> segmentosDao.updateEstadoSegmento(id));
    }

    public void correctErrorHogar(String s, String l) {
        appExecutors.diskIO().execute(() -> cuestionariosDAO.correctErrorHogar(s, l));
    }

    /**
     * Elimina cuestionarios en el servidor y después en la bd local Android.
     *
     * @param modo                 - Eliminar solod DB  o Elimnar BD y server
     * @param processNotifier      - Para mostrar las notificaciones de AlertDialog
     * @param segmentos            - Para enviar paramentros
     * @param cuestionarioSelected - Cuestionario a eliminar
     * @param activity             - ref
     */
    public void eliminarCuestionario(String modo, ProcessNotifier processNotifier, Segmentos segmentos,
                                     Cuestionarios cuestionarioSelected, Activity activity) {
        if (modo.equals("remoto")) {
            processNotifier.setTitle("Eliminando del servidor");
            processNotifier.setText("Espere...");
            processNotifier.show();
            Call<Void> call = censoApiService.eliminarCuestionarioServer(cuestionarioSelected.getLlaveCuestionario());
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (response.isSuccessful()) {
                        Utilidad.respaldarCuestionarioConDatos(segmentos, cuestionarioSelected);
                        appExecutors.diskIO().execute(() -> cuestionariosDAO.eliminarCuestionarioSeleccionado(cuestionarioSelected));

                    } else if (response.code() == 404) {
                        appExecutors.diskIO().execute(() -> cuestionariosDAO.eliminarCuestionarioSeleccionado(cuestionarioSelected));
                        Toast.makeText(MyApp.getContext(), "Cuestionario eliminado.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MyApp.getContext(), "Código de error: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                    processNotifier.deInflate();
                    processNotifier.dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    mostrarAlertDialog(processNotifier.getContext(), "Envio", "Error de envio" /*" + t.getCause()*/, 1);
                    SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                    Date date = new Date();
                    String fecha = sdf.format(date);
                    guardarError(cuestionarioSelected.getLlaveCuestionario(), fecha, t.getMessage());
                    processNotifier.deInflate();
                    processNotifier.dismiss();
                }
            });
        } else {
            Utilidad.showMessageDialog("Cuestionario Eliminado", "", activity, R.raw.ok_sign);
            appExecutors.diskIO().execute(() -> cuestionariosDAO.eliminarCuestionarioSeleccionado(cuestionarioSelected));
        }
    }

    /*    public void actualizarCodigoCenso(Cuestionarios cuestionarioSelected) {
            appExecutors.diskIO().execute(() -> cuestionariosDAO.actualizarCodigoCenso(cuestionarioSelected));
        }*/
    public void guardarError(String llave, String fecha, String errorBody) {
        if (errorBody != null) {
            try {
                LogErrors logErrors = new LogErrors(llave, errorBody, fecha);
                appExecutors.diskIO().execute(() -> cuestionariosDAO.addErrorLogs(logErrors));
            } catch (Exception e) {
                Log.e(TAG, "actualizarEnvio: ", e);
            }
        }
    }

    private void actualizarEnvio(Cuestionarios cuestionarioSelected, Segmentos segmentos, boolean flagPrimerEnvio, boolean flagEnvio, String mensaje) {
        try {
            if (mensaje.contains("Llave recibida existente") || mensaje.contains("Los datos proporcionados no concuerdan con la llave") || mensaje.contains("Error en DatosJson") || mensaje.contains("Revise las fechas o algun otro valor que no deberia ser nulo")) {
                cuestionarioSelected.setFlagPrimerEnvio(false);
            } else {
                cuestionarioSelected.setFlagPrimerEnvio(flagPrimerEnvio);
            }

            cuestionarioSelected.setFlagEnvio(flagEnvio);
            segmentos.setEstado("2");
            appExecutors.diskIO().execute(() -> cuestionariosDAO.updateCuestionario(cuestionarioSelected));
            appExecutors.diskIO().execute(() -> segmentosDao.actualizarEstadoSegmento(segmentos));
        } catch (Exception e) {
            Log.e(TAG, "actualizarEnvio: ", e);
            Log.e(TAG, "actualizarEnvio: ", e);
        }
    }

    private void actualizarEnvioRecorridoPrimerEnvio(ControlRecorrido controlRecorrido, boolean flagPrimerEnvio,
                                                     boolean flagEnvio, String mensaje, String segmentoId) {
        try {
            if (mensaje.contains("Llave recibida existente")
                    || mensaje.contains("Los datos proporcionados no concuerdan con la llave")
                    || mensaje.contains("Error en DatosJson")
                    || mensaje.contains("Revise las fechas o algun otro valor que no deberia ser nulo")) {
                controlRecorrido.setFlagPrimerEnvio(false);
            } else {
                controlRecorrido.setFlagPrimerEnvio(flagPrimerEnvio);
            }
//            segmentos.setEstado("2");
            controlRecorrido.setFlagEnvio(flagEnvio);
            appExecutors.diskIO().execute(() -> segmentosDao.actualizarEstadoSegmentoRecorrido(segmentoId));
            appExecutors.diskIO().execute(() -> controlRecorridoDAO.actualizarControlRecorridoEnvio(controlRecorrido));
        } catch (Exception e) {
            Log.e(TAG, "actualizarEnvio: ", e);
        }
    }

    private void actualizarEnvioUpdate(Cuestionarios cuestionarioSelected, Segmentos segmentos, boolean flagPrimerEnvio, String errorMsg, int code) {
        try {
            if (errorMsg.contains("El cuestionario ya fue revisado por el supervisor")
                    || errorMsg.contains("El segmento del cuestionario esta cerrado")
                    || errorMsg.contains("El segmento de este cuestionario no esta asignado a este usuario")
                    || code == 400
                    || code == 404
                    || errorMsg.contains("Error en DatosJson"))
                cuestionarioSelected.setFlagEnvio(flagPrimerEnvio);

            cuestionarioSelected.setFlagEnvio(flagPrimerEnvio);
            cuestionarioSelected.setFlagPrimerEnvio(flagPrimerEnvio);
            segmentos.setEstado("2");
            appExecutors.diskIO().execute(() -> cuestionariosDAO.updateCuestionario(cuestionarioSelected));
            appExecutors.diskIO().execute(() -> segmentosDao.actualizarEstadoSegmento(segmentos));
        } catch (Exception e) {
            Log.e(TAG, "actualizarEnvio: ", e);
        }
    }

    private void actualizarEnvioRecorridoPut(ControlRecorrido controlRecorrido, boolean flagPrimerEnvio, String errorMsg) {
        try {
            if (errorMsg.contains("El cuestionario ya fue revisado por el supervisor")
                    || errorMsg.contains("El segmento del cuestionario esta cerrado")
                    || errorMsg.contains("El segmento de este cuestionario no esta asignado a este usuario")
                    || errorMsg.contains("Error en DatosJson"))
                controlRecorrido.setFlagEnvio(flagPrimerEnvio);

            controlRecorrido.setFlagEnvio(flagPrimerEnvio);
            controlRecorrido.setFlagPrimerEnvio(flagPrimerEnvio);
            appExecutors.diskIO().execute(() -> controlRecorridoDAO.actualizarControlRecorridoEnvio(controlRecorrido));
        } catch (Exception e) {
            Log.e(TAG, "actualizarEnvio: ", e);
        }
    }

    public void enviarCuestionarioCreate(ProcessNotifier processNotifier, Activity activity, Cuestionarios cuestionarioSelected,
                                         Segmentos segmentos) {
        processNotifier.setTitle("Enviando al servidor");
        processNotifier.setText("Espere...");
        processNotifier.show();
        Call<Void> call = censoApiService.sendCuestionario(getPostCuestionarioEnvio(cuestionarioSelected));
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                try {
                    if (response.isSuccessful()) {
                        actualizarEnvio(cuestionarioSelected, segmentos, true, true,
                                msgResponse = "");
                        Toast.makeText(MyApp.getContext(), "Cuestionario enviado: "
                                + cuestionarioSelected.getLlaveCuestionario(), Toast.LENGTH_SHORT).show();
                    } else {
                        msgResponse = getDetailError(Objects.requireNonNull(response.errorBody()));

                        SimpleDateFormat sdf =
                                new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                        Date date = new Date();
                        String fecha = sdf.format(date);
                        guardarError(cuestionarioSelected.getLlaveCuestionario(), fecha, msgResponse);

                        actualizarEnvio(cuestionarioSelected, segmentos, true, false,
                                msgResponse);
                        if (msgResponse.contains("El cuestionario ya fue revisado por el supervisor")) {
                            Utilidad.showMessageDialog("Error de envío", "El cuestionario ya fue revisado por el supervisor",
                                    activity, R.raw.error_sign);
//                            Toast.makeText(MyApp.getContext(), "El cuestionario ya fue revisado por el supervisor",
//                                    Toast.LENGTH_SHORT).show();
                        } else if (!cuestionarioSelected.isFlagPrimerEnvio())
                            enviarCuestionarioUpdate(processNotifier, cuestionarioSelected, activity,
                                    segmentos, cuestionarioSelected.getLlaveCuestionario());
                        if (response.code() == 500) {
                            mostrarAlertDialog(processNotifier.getContext(),
                                    "Envio",
                                    "Error de envio de cuestionaroo." + msgResponse,
                                    1);
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onResponse: " + e.getMessage());
                }
                processNotifier.dismiss();
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                mostrarAlertDialog(processNotifier.getContext(), "Envio", "Error de envio" /*" + t.getCause()*/, 1);
                SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                Date date = new Date();
                String fecha = sdf.format(date);
                guardarError(cuestionarioSelected.getLlaveCuestionario(), fecha, t.getMessage());
                actualizarEnvio(cuestionarioSelected, segmentos, false, false, msgResponse = "");
                processNotifier.dismiss();
            }
        });
    }

    private PostCuestionarioEnvio getPostCuestionarioEnvio(Cuestionarios cuestionarioSelected) {
        return new PostCuestionarioEnvio(
                cuestionarioSelected.getLlaveCuestionario(),
                cuestionarioSelected.getCodigoSegmento(),
                cuestionarioSelected.getProvinciaIdExp(),
                cuestionarioSelected.getDistritoIdExp(),
                cuestionarioSelected.getCorregimientoIdExp(),
                cuestionarioSelected.getProductor(),
                cuestionarioSelected.getExplotacionNum(),
                cuestionarioSelected.getVivienda(),
                cuestionarioSelected.getNotas(),
                cuestionarioSelected.getDatos(),
                cuestionarioSelected.getDatosJson(),
                cuestionarioSelected.getFechaCreacion(),
                cuestionarioSelected.getFechaModificacion(),
                cuestionarioSelected.getEstado());
    }

    public void enviarControlRecorridoServidor(ControlRecorrido controlRecorridoHogarSelected, Activity activity,
                                               ProcessNotifier processNotifier, Segmentos segmentos) {
        PostEnviarRecorrido postEnviarRecorrido = new PostEnviarRecorrido(
                controlRecorridoHogarSelected.getLlaveRecorrido(),
                controlRecorridoHogarSelected.getCodigoSegmento(),
                controlRecorridoHogarSelected.getDivisionID(),
                controlRecorridoHogarSelected.getVivienda(),
                controlRecorridoHogarSelected.getCondicionID(),
                controlRecorridoHogarSelected.getPregA(),
                controlRecorridoHogarSelected.getPregB(),
                controlRecorridoHogarSelected.getPregC(),
                controlRecorridoHogarSelected.getPregD(),
                controlRecorridoHogarSelected.getPregE(),
                controlRecorridoHogarSelected.getPregF(),
                controlRecorridoHogarSelected.getPregG(),
                controlRecorridoHogarSelected.getCantProductoresAgro(),
                controlRecorridoHogarSelected.getCantExplotacionesAgro(),
                null,
                controlRecorridoHogarSelected.getObservaciones(),
                controlRecorridoHogarSelected.getFechaCreacionCR(),
                controlRecorridoHogarSelected.getFechaModificacionCR());

        Call<Void> call = censoApiService.sendControlRecorrido(postEnviarRecorrido);
        call.enqueue(new Callback<>() {//
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                try {
                    if (response.isSuccessful()) {
                        actualizarEnvioRecorridoPrimerEnvio(controlRecorridoHogarSelected, true, true,
                                msgResponse = "", segmentos.getId());
                        Toast.makeText(MyApp.getContext(), "ControlRecorrido enviado: "
                                + controlRecorridoHogarSelected.getLlaveRecorrido(), Toast.LENGTH_LONG).show();
                    } else {
                        msgResponse = getDetailError(Objects.requireNonNull(response.errorBody()));

                        SimpleDateFormat sdf =
                                new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                        Date date = new Date();
                        String fecha = sdf.format(date);
                        guardarError(controlRecorridoHogarSelected.getLlaveRecorrido(), fecha, msgResponse);

                        actualizarEnvioRecorridoPrimerEnvio(controlRecorridoHogarSelected, true, false,
                                msgResponse, segmentos.getId());
                        if (msgResponse.contains("El cuestionario ya fue revisado por el supervisor")) {
                            Utilidad.showMessageDialog("Error de envío", "El cuestionario ya fue revisado por el supervisor",
                                    activity, R.raw.error_sign);
                        } else if (!controlRecorridoHogarSelected.isFlagPrimerEnvio())
                            enviarControlRecorridoActualizadoServidor(processNotifier, controlRecorridoHogarSelected,
                                    postEnviarRecorrido, controlRecorridoHogarSelected.getLlaveRecorrido());

                        if (!msgResponse.contains("Llave recibida existente"))//
                            Toast.makeText(MyApp.getContext(), "Error envío : "
                                    + msgResponse, Toast.LENGTH_LONG).show();
                    }

                    if (response.code() == 500) {
                        mostrarAlertDialog(processNotifier.getContext(),
                                "Envio",
                                "Error de envio. Error: " + response.code(),
                                1);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onResponse: " + e.getMessage());
                }
                processNotifier.dismiss();
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                mostrarAlertDialog(processNotifier.getContext(), "Envio", "Error de envio" /*" + t.getCause()*/, 1);
                SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                Date date = new Date();
                String fecha = sdf.format(date);
                guardarError(controlRecorridoHogarSelected.getLlaveRecorrido(), fecha, t.getMessage());
                actualizarEnvioRecorridoPrimerEnvio(controlRecorridoHogarSelected,
                        false, false, msgResponse = "", segmentos.getId());
                processNotifier.dismiss();
            }
        });
    }

    public void enviarCuestionarioCreate2(ProcessNotifier processNotifier, Activity activity,
                                          List<Cuestionarios> cuestionarioSelected, Segmentos segmentos) {
        processNotifier.setTitle("Enviando al servidor");
        processNotifier.setText("Espere...");
        processNotifier.show();
        Cuestionarios cuestionarioEnv = cuestionarioSelected.get(0);
        cuestionarioEnv.setDatos("\uFEFF" + cuestionarioEnv.getDatos());
        Call<Void> callSendCuestionario = censoApiService.sendCuestionario(getPostCuestionarioEnvio(cuestionarioEnv));
        callSendCuestionario.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
//                String errorMensajeCreate2 = "";
                if (response.isSuccessful()) {
                    actualizarEnvio(cuestionarioEnv, segmentos, true, true, msgResponse = "");
                    Toast.makeText(MyApp.getContext(), "Cuestionario enviado: " + cuestionarioEnv.getLlaveCuestionario(), Toast.LENGTH_SHORT).show();
                    if (cuestionarioSelected.size() > 1)
                        enviarRestantes(processNotifier, activity, cuestionarioSelected, segmentos);
                } else {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                        Date date = new Date();
                        String fecha = sdf.format(date);
                        if (response.code() == 400)
                            msgResponse = getDetailError(Objects.requireNonNull(response.errorBody()));
                        if (msgResponse != null)
                            guardarError(cuestionarioEnv.getLlaveCuestionario(), fecha, msgResponse);
                        else msgResponse = "";
                    } catch (Exception e) {
                        Log.e(TAG, "onResponse: " + e.getMessage());
                    }

                    actualizarEnvio(cuestionarioEnv, segmentos, false, false, msgResponse);

                    if (msgResponse != null && Objects.requireNonNull(msgResponse)
                            .contains("Error al enviar cuestionario: el cuestionario ya fue revisado por el supervisor")) {
                        Toast.makeText(MyApp.getContext(), msgResponse, Toast.LENGTH_SHORT).show();
                    } else if (!cuestionarioEnv.isFlagPrimerEnvio())
                        enviarCuestionarioUpdate(processNotifier, cuestionarioEnv, activity, segmentos,
                                cuestionarioEnv.getLlaveCuestionario());
                    enviarRestantes(processNotifier, activity, cuestionarioSelected, segmentos);
                }
                processNotifier.dismiss();
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                Date date = new Date();
                String fecha = sdf.format(date);
                guardarError(cuestionarioEnv.getLlaveCuestionario(), fecha, t.getMessage());
                mostrarAlertDialog(processNotifier.getContext(), "Envio", "Error de envio"/*: " + t.getCause()*/, 1);
                actualizarEnvio(cuestionarioEnv, segmentos, false, false, msgResponse = "");
                processNotifier.dismiss();
            }
        });
    }

    public void enviarRecorridoListNoEnviados(ProcessNotifier processNotifier,
                                              List<ControlRecorrido> controlRecorridoHogarSelected) {
        processNotifier.setTitle("Enviando al servidor");
        processNotifier.setText("Espere...");
        processNotifier.show();
        PostEnviarRecorrido postEnviarRecorrido = new PostEnviarRecorrido(
                controlRecorridoHogarSelected.get(0).getLlaveRecorrido(),
                controlRecorridoHogarSelected.get(0).getCodigoSegmento(),
                controlRecorridoHogarSelected.get(0).getDivisionID(),
                controlRecorridoHogarSelected.get(0).getVivienda(),
                controlRecorridoHogarSelected.get(0).getCondicionID(),
                controlRecorridoHogarSelected.get(0).getPregA(),
                controlRecorridoHogarSelected.get(0).getPregB(),
                controlRecorridoHogarSelected.get(0).getPregC(),
                controlRecorridoHogarSelected.get(0).getPregD(),
                controlRecorridoHogarSelected.get(0).getPregE(),
                controlRecorridoHogarSelected.get(0).getPregF(),
                controlRecorridoHogarSelected.get(0).getPregG(),
                controlRecorridoHogarSelected.get(0).getCantProductoresAgro(),
                controlRecorridoHogarSelected.get(0).getCantExplotacionesAgro(),
                null,
                controlRecorridoHogarSelected.get(0).getObservaciones(),
                controlRecorridoHogarSelected.get(0).getFechaCreacionCR(),
                controlRecorridoHogarSelected.get(0).getFechaModificacionCR());
        ControlRecorrido controlRecorrido = controlRecorridoHogarSelected.get(0);
        Call<Void> callSendCuestionario = censoApiService.sendControlRecorrido(postEnviarRecorrido);


        callSendCuestionario.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
//                String errorMensajeCreate2 = "";
                if (response.isSuccessful()) {
                    actualizarEnvioRecorridoPrimerEnvio(controlRecorrido, true, true, msgResponse = "", controlRecorrido.getCodigoSegmento());
                    Toast.makeText(
                            MyApp.getContext(), "Recorrido enviado: "
                                    + controlRecorrido.getLlaveRecorrido(),
                            Toast.LENGTH_SHORT).show();
                    if (controlRecorridoHogarSelected.size() > 1)
                        enviarRestantesRecorrido(processNotifier, controlRecorridoHogarSelected);
                } else {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                        Date date = new Date();
                        String fecha = sdf.format(date);
                        if (response.code() == 400)
                            msgResponse = getDetailError(Objects.requireNonNull(response.errorBody()));
                        if (msgResponse != null)
                            guardarError(controlRecorrido.getLlaveRecorrido(), fecha, msgResponse);
                        else msgResponse = "";
                    } catch (Exception e) {
                        Log.e(TAG, "onResponse: " + e.getMessage());
                    }

                    actualizarEnvioRecorridoPrimerEnvio(controlRecorrido, false,
                            false, msgResponse, controlRecorrido.getCodigoSegmento());

                    if (msgResponse != null && Objects.requireNonNull(msgResponse)
                            .contains("Error al enviar cuestionario: el cuestionario ya fue revisado por el supervisor")) {
                        Toast.makeText(MyApp.getContext(), msgResponse, Toast.LENGTH_SHORT).show();
                    } else if (!controlRecorrido.isFlagPrimerEnvio())
                        enviarControlRecorridoActualizadoServidor(processNotifier, controlRecorrido,
                                postEnviarRecorrido, controlRecorrido.getLlaveRecorrido());
                    enviarRestantesRecorrido(processNotifier, controlRecorridoHogarSelected);
                }
                processNotifier.dismiss();
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                Date date = new Date();
                String fecha = sdf.format(date);
                guardarError(controlRecorrido.getLlaveRecorrido(), fecha, t.getMessage());
                mostrarAlertDialog(processNotifier.getContext(), "Envio", "Error de envio"/*: " + t.getCause()*/, 1);
                actualizarEnvioRecorridoPrimerEnvio(controlRecorrido, false, false, msgResponse = "", controlRecorrido.getCodigoSegmento());
                processNotifier.dismiss();
            }
        });
    }

    private void enviarRestantes(ProcessNotifier processNotifier, Activity activity, List<Cuestionarios> cuestionarioSelected,
                                 Segmentos segmentos) {
        for (int x = 1; x < cuestionarioSelected.size(); x++) {
            processNotifier.setTitle("Enviando al servidor");
            processNotifier.setText("Espere...");
            processNotifier.show();
            Cuestionarios cuestionarioEnv = cuestionarioSelected.get(x);
            cuestionarioEnv.setDatos("\uFEFF" + cuestionarioEnv.getDatos());

            Call<Void> call = censoApiService.sendCuestionario(getPostCuestionarioEnvio(cuestionarioEnv));
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
//                    String errorEnviarRest = "";
                    if (response.isSuccessful()) {
                        Toast.makeText(MyApp.getContext(), "Cuestionario enviado: " + cuestionarioEnv.getLlaveCuestionario(), Toast.LENGTH_SHORT).show();
                        actualizarEnvio(cuestionarioEnv, segmentos, true, true, msgResponse);
                    } else {
                        try {
                            if (response.code() == 400)
                                msgResponse = getDetailError(Objects.requireNonNull(response.errorBody()));
                            SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                            Date date = new Date();
                            String fecha = sdf.format(date);
                            guardarError(cuestionarioEnv.getLlaveCuestionario(), fecha, msgResponse);
                        } catch (Exception e) {
                            Log.e(TAG, "onResponse: " + e.getMessage());
                        }

                        actualizarEnvio(cuestionarioEnv, segmentos, true, false, msgResponse);
                        if (msgResponse.contains("El cuestionario ya fue revisado por el supervisor")) {
                            Toast.makeText(MyApp.getContext(), msgResponse, Toast.LENGTH_SHORT).show();
                        } else if (!cuestionarioEnv.isFlagPrimerEnvio())
                            enviarCuestionarioUpdate(processNotifier, cuestionarioEnv, activity, segmentos,
                                    cuestionarioEnv.getLlaveCuestionario());
                    }
                    processNotifier.dismiss();
                }

                @Override
                public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                    SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                    Date date = new Date();
                    String fecha = sdf.format(date);
                    guardarError(cuestionarioEnv.getLlaveCuestionario(), fecha, t.getMessage());
                    actualizarEnvio(cuestionarioEnv, segmentos, false, false, msgResponse = "");
                    processNotifier.dismiss();
                }
            });
        }
    }

    private void enviarRestantesRecorrido(ProcessNotifier processNotifier, List<ControlRecorrido> controlRecorridos) {
        for (int x = 1; x < controlRecorridos.size(); x++) {
            processNotifier.setTitle("Enviando al servidor");
            processNotifier.setText("Espere...");
            processNotifier.show();
            PostEnviarRecorrido postEnviarRecorrido = new PostEnviarRecorrido(
                    controlRecorridos.get(x).getLlaveRecorrido(),
                    controlRecorridos.get(x).getCodigoSegmento(),
                    controlRecorridos.get(x).getDivisionID(),
                    controlRecorridos.get(x).getVivienda(),
                    controlRecorridos.get(x).getCondicionID(),
                    controlRecorridos.get(x).getPregA(),
                    controlRecorridos.get(x).getPregB(),
                    controlRecorridos.get(x).getPregC(),
                    controlRecorridos.get(x).getPregD(),
                    controlRecorridos.get(x).getPregE(),
                    controlRecorridos.get(x).getPregF(),
                    controlRecorridos.get(x).getPregG(),
                    controlRecorridos.get(x).getCantProductoresAgro(),
                    controlRecorridos.get(x).getCantExplotacionesAgro(),
                    null,
                    controlRecorridos.get(x).getObservaciones(),
                    controlRecorridos.get(x).getFechaCreacionCR(),
                    controlRecorridos.get(x).getFechaModificacionCR());

            ControlRecorrido cuestionarioEnv = controlRecorridos.get(x);

            Call<Void> call = censoApiService.sendControlRecorrido(postEnviarRecorrido);
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
//                    String errorEnviarRest = "";
                    if (response.isSuccessful()) {
                        Toast.makeText(MyApp.getContext(),
                                "Recorrido enviado: " + cuestionarioEnv.getLlaveRecorrido(),
                                Toast.LENGTH_SHORT).show();
                        actualizarEnvioRecorridoPrimerEnvio(cuestionarioEnv, true, true, msgResponse, cuestionarioEnv.getCodigoSegmento());
                    } else {
                        try {
                            if (response.code() == 400)
                                msgResponse = getDetailError(Objects.requireNonNull(response.errorBody()));
                            SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                            Date date = new Date();
                            String fecha = sdf.format(date);
                            guardarError(cuestionarioEnv.getLlaveRecorrido(), fecha, msgResponse);
                        } catch (Exception e) {
                            Log.e(TAG, "onResponse: " + e.getMessage());
                        }

                        actualizarEnvioRecorridoPrimerEnvio(cuestionarioEnv, true, false, msgResponse, cuestionarioEnv.getCodigoSegmento());
                        if (msgResponse.contains("El recorrido ya fue revisado por el supervisor")) {
                            Toast.makeText(MyApp.getContext(), msgResponse, Toast.LENGTH_SHORT).show();
                        } else if (!cuestionarioEnv.isFlagPrimerEnvio())
                            enviarControlRecorridoActualizadoServidor(processNotifier, cuestionarioEnv,
                                    postEnviarRecorrido, cuestionarioEnv.getLlaveRecorrido());
                    }
                    processNotifier.dismiss();
                }

                @Override
                public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                    SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                    Date date = new Date();
                    String fecha = sdf.format(date);
                    guardarError(cuestionarioEnv.getLlaveRecorrido(), fecha, t.getMessage());
                    actualizarEnvioRecorridoPrimerEnvio(cuestionarioEnv, false, false, msgResponse = "", cuestionarioEnv.getCodigoSegmento());
                    processNotifier.dismiss();
                }
            });
        }
    }

    public void enviarCuestionarioUpdate(ProcessNotifier processNotifier,
                                         Cuestionarios cuestionarioSelected, Activity activity,
                                         Segmentos segmentos,
                                         String llave) {
        processNotifier.setTitle("Enviando al servidor");
        processNotifier.setText("Espere...");
        processNotifier.show();

        Call<Void> call = censoApiService.sendCuestionarioUpdate(cuestionarioSelected, llave);
//        call.timeout().timeout(0, TimeUnit.MINUTES);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
//                String errorMensaje = "";
                if (response.isSuccessful()) {
                    actualizarEnvioUpdate(cuestionarioSelected, segmentos, true, msgResponse = "", response.raw().code());
                    Toast.makeText(MyApp.getContext(), "Cuestionario enviado: " + cuestionarioSelected.getLlaveCuestionario(), Toast.LENGTH_SHORT).show();
                    processNotifier.dismiss();
                    Log.d(TAG, "Se ha enviado el cuestionario");
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                    Date date = new Date();
                    String fecha = sdf.format(date);
                    try {
                        msgResponse = getDetailError(Objects.requireNonNull(response.errorBody()));
                        guardarError(cuestionarioSelected.getLlaveCuestionario(), fecha, msgResponse);
                    } catch (Exception e) {
                        Log.e(TAG, "actualizarEnvio: ", e);
                        Toast.makeText(MyApp.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    processNotifier.dismiss();
                    Utilidad.showMessageDialog("Error de envío", "Error envío actualizar cuestionario: " + msgResponse,
                            activity, R.raw.error_sign);
                    actualizarEnvioUpdate(cuestionarioSelected, segmentos, false, msgResponse, response.raw().code());

                    if (response.code() == 500) {
                        mostrarAlertDialog(processNotifier.getContext(),
                                "Envio",
                                "Error de envio de cuestionaroo." + msgResponse,
                                1);
                    }
                }
                processNotifier.dismiss();
            }


            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                mostrarAlertDialog(processNotifier.getContext(), "Envio", "Error de envio"/* " + t.getCause()*/, 1);
                SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                Date date = new Date();
                String fecha = sdf.format(date);
                guardarError(cuestionarioSelected.getLlaveCuestionario(), fecha, t.getMessage());
                actualizarEnvioUpdate(cuestionarioSelected, segmentos, false, msgResponse = "", 0);
                processNotifier.dismiss();
            }
        });
    }

    private String getDetailError(ResponseBody responseBody) {
        String errorBody = "";
        try {
            if (responseBody != null) {
                errorBody = responseBody.string();
                if (!errorBody.isEmpty()) {
                    JSONObject jsonObject = new JSONObject(errorBody);
                    return jsonObject.getString("detail");
                }
            } else
                return "";
        } catch (Exception e) {
            Log.e(TAG, "getDetailError: ", e);
        }
        return errorBody;
    }

    public void enviarControlRecorridoActualizadoServidor(ProcessNotifier processNotifier,
                                                          ControlRecorrido controlRecorrido,
                                                          PostEnviarRecorrido postEnviarRecorrido,
                                                          String llave) {
        Call<Void> call = censoApiService.sendControlRecorridoUpdate(postEnviarRecorrido, llave);
//        call.timeout().timeout(0, TimeUnit.MINUTES);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
//                String errorMensaje = "";
                if (response.isSuccessful()) {
                    actualizarEnvioRecorridoPut(controlRecorrido, true, msgResponse = "");
                    Toast.makeText(MyApp.getContext(), "Control Recorrido enviado: "
                            + controlRecorrido.getLlaveRecorrido(), Toast.LENGTH_LONG).show();
                    processNotifier.dismiss();
                    Log.d(TAG, "Se ha enviado el cuestionario");
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                    Date date = new Date();
                    String fecha = sdf.format(date);
                    msgResponse = getDetailError(Objects.requireNonNull(response.errorBody()));
                    guardarError(controlRecorrido.getLlaveRecorrido(), fecha, msgResponse);

                    if (!msgResponse.contains("Llave recibida existente"))//
                        Toast.makeText(MyApp.getContext(), "Error envío : "
                                + msgResponse, Toast.LENGTH_LONG).show();

                    actualizarEnvioRecorridoPut(controlRecorrido, false, msgResponse);
                    processNotifier.dismiss();
                }
                processNotifier.dismiss();
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                mostrarAlertDialog(processNotifier.getContext(), "Envio", "Error de envio"/* " + t.getCause()*/, 1);
                SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                Date date = new Date();
                String fecha = sdf.format(date);
                guardarError(controlRecorrido.getLlaveRecorrido(), fecha, t.getMessage());
                actualizarEnvioRecorridoPut(controlRecorrido, false, msgResponse = "");
                processNotifier.dismiss();
            }
        });
    }

    public void mostrarAlertDialog(Context activity, String titulo, String msg, int tipo) {
        try {
            MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(activity);
            materialAlertDialogBuilder.setTitle(titulo);
            materialAlertDialogBuilder.setMessage(msg);
            if (tipo == 0) materialAlertDialogBuilder.setIcon(R.drawable.ic_send_ok);
            else materialAlertDialogBuilder.setIcon(R.drawable.ic_dialog_warning);
            materialAlertDialogBuilder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
            materialAlertDialogBuilder.show();
        } catch (Exception e) {
            Log.e(TAG, "actualizarEnvio: ", e);
        }
    }

/*    public void eliminarDataBase() {
        appExecutors.diskIO().execute(() -> {
            cuestionariosDAO.deleteCuestionario();
            cuestionariosDAO.deleteSegmentos();
            cuestionariosDAO.deleteUsuarios();
        });
    }*/

    /*public void getMapsByContingente(FragmentActivity fragmentActivity, ProcessNotifier processNotifier, String region, String zona, String subzona) {
        processNotifier.setTitle("Descarga de Mapa");
        processNotifier.setText("Espere...");
        processNotifier.show();
        Call<ResponseBody> call = censoApiServiceMap.getMapsByRegionZonaSubzonaZIP(region, SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_CODIGO), zona, subzona);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    appExecutors.diskIO().execute(() -> {
                        if (response.body() != null) {
                            saveToDisk(fragmentActivity, processNotifier, response.body(), "m_" + region + zona + subzona);
                        }
                    });
                } else {
                    if (response.raw().message().equals("Not Found") && response.raw().code() == 404) {
                        Toast.makeText(MyApp.getContext(), "No hay mapas para descargar.", Toast.LENGTH_SHORT).show();
                    }

                    if (response.raw().code() == 404) {
                        Toast.makeText(MyApp.getContext(), "No hay mapas para descargar. Error: " + response.raw().code(), Toast.LENGTH_SHORT).show();
                    }
                }
                processNotifier.dismiss();
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Log.i(TAG, "onFailure: ");
                mostrarAlertDialog(processNotifier.getContext(), "Envio", "Error de descarga"*//*: " + t.getCause()*//*, 1);
                processNotifier.dismiss();
            }
        });
    }*/

    public void getMapsByRegionZonaSubzona(FragmentActivity fragmentActivity,
                                           ProcessNotifier processNotifier, String region,
                                           String zona, String subzona) {
        processNotifier.setTitle("Descarga de Mapa");
        processNotifier.setText("Espere...");
        processNotifier.show();
        Call<ResponseBody> call = censoApiServiceMap.getMapsByRegionZonaSubzonaZIP(region,
                SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_CODIGO), zona, subzona);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    appExecutors.diskIO().execute(() -> {
                        if (response.body() != null) {
                            if (SharedPreferencesManager.getSomeStringValue(AppConstants.PREF_CODIGO).startsWith("C00")) {
                                saveToDisk(fragmentActivity, processNotifier, response.body(), "mc_" + subzona);
                            } else {
                                saveToDisk(fragmentActivity, processNotifier, response.body(), "m_" + region + zona + subzona);
                            }

                        }
                    });
                } else {
                    if (response.raw().message().equals("Not Found") && response.raw().code() == 404) {
                        Toast.makeText(MyApp.getContext(), "No hay mapas para descargar.", Toast.LENGTH_SHORT).show();
                    }

                    if (response.raw().code() == 404) {
                        Toast.makeText(MyApp.getContext(), "No hay mapas para descargar. Error: " + response.raw().code(), Toast.LENGTH_SHORT).show();
                    }
                }
                processNotifier.dismiss();
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Log.i(TAG, "onFailure: ");
                mostrarAlertDialog(processNotifier.getContext(), "Envio", "Error de descarga"/*: " + t.getCause()*/, 1);
                processNotifier.dismiss();
            }
        });
    }

    private void saveToDisk(FragmentActivity fragmentActivity, ProcessNotifier processNotifier, ResponseBody body, String filename) {
        ZipInputStream zipInputStream;
        processNotifier.inflate();
        try {
            File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            String inecMovilDirectory = directory + "/InecMovil/MAPAS";

            if (new File(inecMovilDirectory).mkdir()) {
                Log.i(TAG, "saveToDisk: inecMovil Directory MAPAS");
            }

            File destinationFile = new File(inecMovilDirectory + "/" + filename);

            if ((destinationFile).exists()) {
                if (destinationFile.delete()) {
                    Log.i(TAG, "abrirCsPro: se ha eliminado el archivo .pff exitosamente");
                } else Log.e(TAG, "No se ha eliminado el archivo .pff ");
            }

            InputStream is = null;
            OutputStream os = null;

            try {
                long filesize = body.contentLength();
                is = body.byteStream();
                os = new FileOutputStream(destinationFile);

                byte[] data = new byte[4096];
                int count;
                int progress = 0;
                float tot;
                while ((count = is.read(data)) != -1) {
                    os.write(data, 0, count);
                    progress += count;
                    tot = (((float) progress / filesize) * 100);
                    processNotifier.setText("Descargando: " + (int) tot + "%");
                }

                os.flush();

                Log.d(TAG, "File saved successfully!");
            } catch (IOException e) {
                processNotifier.dismiss();
                Log.e(TAG, "actualizarEnvio: ", e);
                Log.d(TAG, "Failed to save the file!");
            } finally {
                if (is != null) is.close();
                if (os != null) os.close();
            }

            long total;
            long kbs;
            int countMapZip = 0;
            String fileZipName;
            ZipFile zipFileMap = new ZipFile(destinationFile);
            is = new FileInputStream(destinationFile);
            zipInputStream = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry zipEntry;
            byte[] buffer = new byte[1024];

            File dirMapFilePath = new File(inecMovilDirectory + "/dir" + filename);
            if (dirMapFilePath.exists()) {
                String[] children = dirMapFilePath.list();
                if (children != null) {
                    for (String child : children) {
                        if (new File(dirMapFilePath, child).delete()) Log.i(TAG, "saveToDisk: ");
                    }
                }

                if (dirMapFilePath.delete()) {
                    Log.i(TAG, "abrirCsPro: se ha eliminado el archivo .pff exitosamente");
                } else Log.e(TAG, "No se ha eliminado el archivo .pff ");
            }

            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                countMapZip++;

                fileZipName = zipEntry.getName();
                File mapFile = new File(dirMapFilePath + "/" + fileZipName);

                if (dirMapFilePath.mkdirs())
                    Log.i(TAG, "Se ha creado el dirrectorio satisfactoriamente");
                else Log.e(TAG, "No se ha creado el dirrectorio satisfactoriamente");

                if (mapFile.exists()) {
                    if (mapFile.delete()) {
                        Log.i(TAG, "abrirCsPro: se ha eliminado el archivo .pff exitosamente");
                    } else Log.e(TAG, "No se ha eliminado el archivo .pff ");
                }

                if (mapFile.createNewFile())
                    Log.i(TAG, "Se ha creado el archivo satisfactoriamente");
                else Log.e(TAG, "No se ha creado el archivo satisfactoriamente");

                FileOutputStream fOut = new FileOutputStream(mapFile);
                total = 0;
                kbs = 0;
                int count;

                // cteni zipu a zapis
                while ((count = zipInputStream.read(buffer)) != -1) {
                    total += count;
                    if (kbs != (total / 1024) / 1024) {
                        kbs = (total / 1024) / 1024;
//                        mrq.actualizar("descomprimiendo...: " + (total / 1024) + "Kb");
                        processNotifier.setText("Descomp. .tif: " + countMapZip + " / " + zipFileMap.size() + " - " + kbs + " mbs");
                    }
                    fOut.write(buffer, 0, count);
                }
                fOut.close();
                zipInputStream.closeEntry();
            }
            fragmentActivity.runOnUiThread(() -> Toast.makeText(fragmentActivity, "Mapas guardados con exito.", Toast.LENGTH_SHORT).show());
            zipInputStream.close();
            processNotifier.dismiss();
        } catch (Exception e) {
            processNotifier.dismiss();
            mostrarAlertDialog(fragmentActivity.getApplicationContext(), "Mapas", "Error al guardar mapas.", 1);
            Log.d(TAG, "Failed to save the file! // " + e.getMessage());
        }
        processNotifier.dismiss();
    }

    public void getInconsistencias(FragmentActivity fragmentActivity, ProcessNotifier processNotifier, String usuario) {
        processNotifier.setTitle("Descargando inconsistencias");
        processNotifier.setText("Espere...");
        processNotifier.show();
        Call<List<GetInconsistenciasResponse>> call = censoApiService.getInconsistencias(usuario);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<List<GetInconsistenciasResponse>> call, @NotNull Response<List<GetInconsistenciasResponse>> response) {
                if (response.isSuccessful()) {
                    List<GetInconsistenciasResponse> inconsistenciasResponseList = response.body();
                    if (inconsistenciasResponseList != null && !inconsistenciasResponseList.isEmpty()) {
                        mostrarLlavesInconsistencias(fragmentActivity, inconsistenciasResponseList);
                    } else {
                        Toast.makeText(fragmentActivity, "No hay inconsistencias.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                    Date date = new Date();
                    String fecha = sdf.format(date);
                    try {
                        guardarError("Inconsistencia de " + usuario, fecha, Objects.requireNonNull(response.errorBody()).string());
                    } catch (IOException e) {
                        Log.e(TAG, "actualizarEnvio: ", e);
                    }
                }
                processNotifier.dismiss();
            }

            @Override
            public void onFailure(@NotNull Call<List<GetInconsistenciasResponse>> call, @NotNull Throwable t) {
                mostrarAlertDialog(processNotifier.getContext(), "Inconsistencias", "Error de descarga" /*de envio: " + t.getCause()*/, 1);
                SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                Date date = new Date();
                String fecha = sdf.format(date);
                guardarError("Inconsistencia de " + usuario, fecha, t.getMessage());
                processNotifier.dismiss();
            }
        });
    }

    private void mostrarLlavesInconsistencias(FragmentActivity fragmentActivity,
                                              List<GetInconsistenciasResponse> inconsistenciasResponseList) {
        boolean flagIncon;
        List<String> segmentos = new ArrayList<>();
        String llave = inconsistenciasResponseList.get(0).getLlave().substring(0, 12);

        segmentos.add(llave);/*
        llave.substring(0, 6) + "-" +
                llave.substring(6, 10) + "-" +
                llave.substring(10, 12)*/
        for (int x = 0; x < inconsistenciasResponseList.size(); x++) {
            flagIncon = false;
            try {
                llave = inconsistenciasResponseList.get(x).getLlave().substring(0, 12);
                for (int y = 0; y < segmentos.size(); y++) {
                    if (llave.equals(segmentos.get(y))) {
                        flagIncon = false;
                        break;
                    } else {
                        flagIncon = true;
                    }
                }
                if (flagIncon) {
                    segmentos.add(llave);
                }
            } catch (Exception e) {
                Toast.makeText(fragmentActivity, "Error en descarga de inconsistencia: " + x,
                        Toast.LENGTH_SHORT).show();
                x++;
            }
        }
//        CharSequence[] llaves = new CharSequence[segmentos.size()];
//        llaves[x] = inconsistenciasResponseList.get(x).getLlaveCuestionario();
//        llaves[x] = inconsistenciasResponseList.get(x).getLlaveCuestionario().substring(0, 6) + "-" +
//                inconsistenciasResponseList.get(x).getLlaveCuestionario().substring(6, 10) + "-" +
//                inconsistenciasResponseList.get(x).getLlaveCuestionario().substring(10, 12) + "-" +
//                inconsistenciasResponseList.get(x).getLlaveCuestionario().charAt(12) + "-" //+
                /*    inconsistenciasResponseList.get(x).getLlaveCuestionario().substring(13, 15) + "-" +
                    inconsistenciasResponseList.get(x).getLlaveCuestionario().charAt(15)*/

        CharSequence[] segmentosFiltrados = new CharSequence[segmentos.size()];
        for (int x = 0; x < segmentos.size(); x++)
            segmentosFiltrados[x] = segmentos.get(x).substring(0, 6) + "-"
                    + segmentos.get(x).substring(6, 10) + "-" + segmentos.get(x).substring(10, 12);
        AlertDialog.Builder builderInconsistencias = new AlertDialog.Builder(fragmentActivity);
        builderInconsistencias.setTitle("Seleccione un segmento");
        builderInconsistencias.setSingleChoiceItems(segmentosFiltrados, 0, null);

        builderInconsistencias.setPositiveButton("Aceptar", (dialog, which) -> {
            int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
            abrirPDF(fragmentActivity, inconsistenciasResponseList, segmentos.get(selectedPosition));

        });
        builderInconsistencias.setNegativeButton("Cancelar", null);
        AlertDialog alertDialog = builderInconsistencias.create();
        alertDialog.show();
    }

    private void abrirPDF(FragmentActivity fragmentActivity,
                          List<GetInconsistenciasResponse> inconsistenciasResponseList, String segmentoSelected) {
        StringBuilder inconsistencias = new StringBuilder();
        String archivoIncons = getString();
        String llaveTitulo = "";
        for (int x = 0; x < inconsistenciasResponseList.size(); x++) {
            if (inconsistenciasResponseList.get(x).getLlave().contains(segmentoSelected)) {
                String llave = getLlave(inconsistenciasResponseList, x);
                if (!llave.equals(llaveTitulo)) {
                    inconsistencias
                            .append(llave.substring(0, 6))
                            .append("-").append(llave.substring(6, 10))//SEGMENTO
                            .append("-").append(llave.substring(10, 12))//DIVISION
                            .append(" Vivienda ").append(llave.substring(12, 14))//Vivienda
                            .append(" | Productor ").append(llave.substring(14, 16))//Productor
                            .append(" | Cuestionario ").append(llave.substring(16))//Cuestionario
                            .append("\n");
                    llaveTitulo = llave;
                }
                String mensaje = Arrays.toString(inconsistenciasResponseList.get(x).getMensajes().split(llave)).replace("[", "");
                inconsistencias.append(mensaje.replace("]", ""));
                inconsistencias.append("\n\n");
            }
        }

        Document document = new Document();
        String outPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + "incon_" + archivoIncons + ".pdf";
        try {
            PdfWriter.getInstance(document, new FileOutputStream(outPath));
            document.open();
            document.add(new Paragraph(inconsistencias.toString()));
            document.close();
            Toast.makeText(MyApp.getContext(), "PDF creado", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setDataAndType(Uri.fromFile(new File(outPath)),"application/pdf");

            Uri uri = FileProvider.getUriForFile(MyApp.getContext(), "gov.census.cspro.fileaccess.fileprovider", new File(outPath));
            String mime = MyApp.getContext().getContentResolver().getType(uri);
            intent.setDataAndType(uri, mime);

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

//            Intent chooser = Intent.createChooser(intent, "Abrir pdf");
            fragmentActivity.startActivity(intent);

        } catch (Exception e) {
            Toast.makeText(MyApp.getContext(), "Error al crear el PDF. " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    private static String getLlave(List<GetInconsistenciasResponse> inconsistenciasResponseList, int x) {

        return inconsistenciasResponseList.get(x).getLlave();
    }

    @NonNull
    private static String getString() {
        Calendar calendar = Calendar.getInstance();

        String dia = "00";
        String mes = "00";
        String anno = "0000";
        String hora = "00";
        String minutos = "00";
        dia += calendar.get(Calendar.DAY_OF_MONTH);
        mes += (calendar.get(Calendar.MONTH) + 1);
        anno += calendar.get(Calendar.YEAR);
        hora += calendar.get(Calendar.HOUR_OF_DAY);
        minutos += calendar.get(Calendar.MINUTE);
        String archivoIncons;

        archivoIncons = dia.substring(dia.length() - 2)
                + mes.substring(mes.length() - 2)
                + anno.substring(anno.length() - 4) + "_"
                + hora.substring(hora.length() - 2)
                + minutos.substring(minutos.length() - 2);
        return archivoIncons;
    }

    /**
     * Se envia la fecha de bd correspondiente a la ultima vez qu se actualizó
     *
     * @param segmentosSelected segmento seleccionado
     * @param usuario           El usuario con que inicios seción en la ap
     * @param fechaUltimoSync   Fecha en que se realizó la actualiaación
     */
    public void actualizarEstados(List<Segmentos> segmentosSelected, String usuario, String fechaUltimoSync) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN_ESTADOS, Locale.US);
            Date sdf2 = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN_ESTADOS, Locale.US).parse(fechaUltimoSync);

            String fecha = sdf.format(Objects.requireNonNull(sdf2));

            Call<List<GetRefreshEstado>> call = censoApiService.actualizarEstados(usuario, fecha);
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<List<GetRefreshEstado>> call,
                                       @NotNull Response<List<GetRefreshEstado>> response) {
                    if (response.isSuccessful()) {
                        List<GetRefreshEstado> refreshEstado = response.body();
//                        List<Segmentos> newSegmentos = new ArrayList<>();
                        for (int x = 0; x < Objects.requireNonNull(refreshEstado).size(); x++) {
    /*                        for (int y = 0; y < segmentosSelected.size(); y++) {
                                if (refreshEstado.get(x).getId().equals(segmentosSelected.get(y).getId())) {
                                    newSegmentos.add(segmentoUpdate(refreshEstado.get(x), segmentosSelected.get(y)));
                                    if (refreshEstado.size() == x + 1) {
                                        SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN_ESTADOS, Locale.US);
                                        Date date = new Date();
                                        String fecha = sdf.format(date);
                                        SharedPreferencesManager.setSomeStringValue(AppConstants.PREF_FECHA, fecha);
                                    }
                                    break;
                                }
                            }*/
                            int estado = refreshEstado.get(x).getEstado();
                            String llaveSegmento = refreshEstado.get(x).getId();
                            appExecutors.diskIO().execute(() -> segmentosDao.actualizarSegmentos(
                                    String.valueOf(estado), llaveSegmento));
                        }

                        //Guardo la fecha en que se cambio la fecha del servidor.
                        SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN_ESTADOS,
                                Locale.US);
                        Date date = new Date();
                        String fecha = sdf.format(date);
                        SharedPreferencesManager.setSomeStringValue(AppConstants.PREF_FECHA, fecha);

//                        if (newSegmentos.size() > 0)
//                            appExecutors.diskIO().execute(() -> segmentosDao.updateSegmentosEstado2(newSegmentos));
//                        SharedPreferencesManager.setSomeBooleanValue(AppConstants.ESTADOS_STATUS, false);
                    } else {
                        Toast.makeText(MyApp.getContext(), "No hay estado que actualizar.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<List<GetRefreshEstado>> call, @NotNull Throwable t) {
                    Toast.makeText(MyApp.getContext(), "Error al actualizar los estados. " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (ParseException e) {
            Log.e(TAG, "actualizarEnvio: ", e);
        }
    }

/*    private Segmentos segmentoUpdate(GetRefreshEstado getRefreshEstado, Segmentos segmentos) {
        segmentos.setEstado(String.valueOf(getRefreshEstado.getEstado()));
        return segmentos;
    }*/

    public void actualizarObservacionRecorrido(String llaveRecorrido, String observacion) {
        appExecutors.diskIO().execute(() -> controlRecorridoDAO
                .actualizarObservacionRecorrido(llaveRecorrido, observacion));
    }

    public void actualizarCondicionRecorrido(ControlRecorrido controlRecorrido) {
        appExecutors.diskIO().execute(() -> controlRecorridoDAO
                .actualizarCondicionRecorrido(controlRecorrido));
    }

    public LiveData<List<Cuestionarios>> getCuestionariosByRecorrido(String llaveRecorrido) {
        return cuestionariosDAO.getCuestionariosByRecorrido(llaveRecorrido);
    }

    public void eliminarRecorrido(ControlRecorrido controlRecorridoEliminar, ProcessNotifier processNotifier) {
        processNotifier.setTitle("Eliminando del servidor");
        processNotifier.setText("Espere...");
        processNotifier.show();
        Call<Void> call = censoApiService.eliminarRecorridoServer(controlRecorridoEliminar.getLlaveRecorrido());
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                SimpleDateFormat sdf =
                        new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                Date date = new Date();
                String fecha = sdf.format(date);
                controlRecorridoEliminar.setFechaModificacionCR(fecha);
                ControlRecorridoBackup backupC = new ControlRecorridoBackup(
                        controlRecorridoEliminar.getLlaveRecorrido(),
                        controlRecorridoEliminar.getCodigoSegmento(),
                        controlRecorridoEliminar.getSubzona(),
                        controlRecorridoEliminar.getProvinciaID(),
                        controlRecorridoEliminar.getDistritoID(),
                        controlRecorridoEliminar.getCorregimientoID(),
                        controlRecorridoEliminar.getSegmentoID(),
                        controlRecorridoEliminar.getDivisionID(),
                        controlRecorridoEliminar.getCodigoEmpadronador(),
                        controlRecorridoEliminar.getNumEmpadronador(),
                        controlRecorridoEliminar.getVivienda(),
//                        controlRecorridoEliminar.getHogar(),
                        controlRecorridoEliminar.getObservaciones(),
                        controlRecorridoEliminar.getCondicionID(),
//                        controlRecorridoEliminar.getPreg_amfy0506_unSoloPresupuesto(),
                        controlRecorridoEliminar.getPregA(),
                        controlRecorridoEliminar.getPregB(),
                        controlRecorridoEliminar.getPregC(),
                        controlRecorridoEliminar.getPregD(),
                        controlRecorridoEliminar.getPregE(),
                        controlRecorridoEliminar.getPregF(),
                        controlRecorridoEliminar.getPregG(),
                        controlRecorridoEliminar.getCantProductoresAgro(),
                        controlRecorridoEliminar.getCantExplotacionesAgro(),
                        controlRecorridoEliminar.getFechaCreacionCR(),
                        fecha,
                        controlRecorridoEliminar.getFechaRecepcionCR(),
                        controlRecorridoEliminar.getVisivilidadLn(),
                        controlRecorridoEliminar.getHabilitado(),
                        controlRecorridoEliminar.isFlagEnvio(),
                        controlRecorridoEliminar.isFlagPrimerEnvio()
                );

                if (response.isSuccessful()) {
                    appExecutors.diskIO().execute(() -> controlRecorridoDAO.guardarRecorridoEliminado(backupC));
                    appExecutors.diskIO().execute(() -> controlRecorridoDAO.eliminarRecorrido(controlRecorridoEliminar.getLlaveRecorrido()));

                } else if (response.code() == 404) {
                    appExecutors.diskIO().execute(() -> controlRecorridoDAO.guardarRecorridoEliminado(backupC));
                    appExecutors.diskIO().execute(() -> controlRecorridoDAO.eliminarRecorrido(controlRecorridoEliminar.getLlaveRecorrido()));
                    Toast.makeText(MyApp.getContext(), "Recorrido eliminado.", Toast.LENGTH_SHORT).show();
                }
                processNotifier.deInflate();
                processNotifier.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                mostrarAlertDialog(processNotifier.getContext(), "Eliminación de Recorrido",
                        "Verifique que tenga los datos moviles activos." /*" + t.getCause()*/, 1);
                SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                Date date = new Date();
                String fecha = sdf.format(date);
                guardarError(controlRecorridoEliminar.getLlaveRecorrido(), fecha, t.getMessage());
                processNotifier.deInflate();
                processNotifier.dismiss();
            }
        });
    }

    public void guardarCuestionarioBackup(Cuestionarios cuestionarios) {
        appExecutors.diskIO().execute(() -> cuestionariosDAO
                .guardarCuestionarioEliminado(convertirCuestionariosBackup(cuestionarios)));
    }

    private CuestionariosBackup convertirCuestionariosBackup(Cuestionarios cuestionarios) {
        SimpleDateFormat sdf =
                new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
        Date date = new Date();
        String fecha = sdf.format(date);
        return new CuestionariosBackup(
                cuestionarios.getLlaveCuestionario(),
                cuestionarios.getCodigoViviendaHogares(),
                cuestionarios.getCodigoSegmento(),
                cuestionarios.getProvinciaID(),
                cuestionarios.getDistritoID(),
                cuestionarios.getCorregimientoID(),
                cuestionarios.getProvinciaIdExp(),
                cuestionarios.getProvinciaIdExpDesc(),
                cuestionarios.getDistritoIdExp(),
                cuestionarios.getDistritoIdExpDesc(),
                cuestionarios.getCorregimientoIdExp(),
                cuestionarios.getCorregimientoIdExpDesc(),
                cuestionarios.getProductor(),
                cuestionarios.getExplotacionNum(),
                cuestionarios.getSubzona(),
                cuestionarios.getSegmento(),
                cuestionarios.getDivision(),
                cuestionarios.getEmpadronador(),
                cuestionarios.getNumEmpadronador(),
                cuestionarios.getJefe(),
                cuestionarios.getVivienda(),
//                cuestionarios.getHogar(),
                cuestionarios.getNotas(),
                cuestionarios.getDatos(),
                cuestionarios.getDatosJson(),
                cuestionarios.getErroresEstructura(),
                cuestionarios.getEstado(),
                cuestionarios.getFechaCreacion(),
                fecha,
                cuestionarios.getFechaRecepcion(),
                cuestionarios.getFechaEntrada(),
                cuestionarios.isFlagEnvio(),
                cuestionarios.isFlagPrimerEnvio(),
                cuestionarios.getCantExplotaciones()
        );
    }

    public void recuperarCuestionarioSeleccionadoServer(Cuestionarios cuestionarioSelected, ProcessNotifier processNotifier, String dataDirectory) {
        processNotifier.show();
        Call<Cuestionarios> call = censoApiService.getCuestionarioByLlave(cuestionarioSelected.getLlaveCuestionario());
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Cuestionarios> call, @NonNull Response<Cuestionarios> response) {
                try {
                    if (response.isSuccessful()) {//TODO VERIFICAR
                        appExecutors.diskIO().execute(() -> cuestionariosDAO
                                .guardarCuestionarioEliminado(convertirCuestionariosBackup(cuestionarioSelected)));
                        deleteCsProFiles(cuestionarioSelected);
                        appExecutors.diskIO().execute(() -> cuestionariosDAO.actualizarCuestionario(response.body()));
                        Toast.makeText(MyApp.getContext(), "Cuestionario actualizado: "
                                + cuestionarioSelected.getLlaveCuestionario(), Toast.LENGTH_SHORT).show();
                    } else {
                        msgResponse = getDetailError(Objects.requireNonNull(response.errorBody()));

                        SimpleDateFormat sdf =
                                new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                        Date date = new Date();
                        String fecha = sdf.format(date);
                        guardarError(cuestionarioSelected.getLlaveCuestionario(), fecha, msgResponse);
                        Toast.makeText(MyApp.getContext(), "Cuestionario no actualizado: "
                                + cuestionarioSelected.getLlaveCuestionario(), Toast.LENGTH_SHORT).show();
                    }

                    if (response.code() == 500) {
                        mostrarAlertDialog(processNotifier.getContext(),
                                "Envio",
                                "Error de envio. Error: " + response.code(),
                                1);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onResponse: " + e.getMessage());
                }
                processNotifier.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<Cuestionarios> call, @NonNull Throwable t) {
                mostrarAlertDialog(processNotifier.getContext(),
                        "Recuperación de cuestionario",
                        "Error de recuperación" /*" + t.getCause()*/,
                        1);
                SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.TIMESTAMP_PATTERN, Locale.US);
                Date date = new Date();
                String fecha = sdf.format(date);
                guardarError(cuestionarioSelected.getLlaveCuestionario(), fecha, t.getMessage());
                processNotifier.dismiss();
            }
        });
    }

    public LiveData<List<Segmentos>> getAllSegmentosFromBD() {
        return segmentosDao.loadSegmentos();
    }

    public void limpiarRecorrido(ControlRecorrido controlRecorridoLimpiar) {
        appExecutors.diskIO().execute(() -> controlRecorridoDAO
                .controlRecorridoLimpiar(controlRecorridoLimpiar));
    }
}
