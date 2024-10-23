package com.example.proyectofinalencuesta_bdubicuo.data.remote.networkServices;

import androidx.lifecycle.LiveData;

import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.ControlRecorrido;
import com.example.proyectofinalencuesta_bdubicuo.data.local.dbEntities.Cuestionarios;
import com.example.proyectofinalencuesta_bdubicuo.data.remote.ApiResponse;
import com.example.proyectofinalencuesta_bdubicuo.data.remote.networkEntities.GetInconsistenciasResponse;
import com.example.proyectofinalencuesta_bdubicuo.data.remote.networkEntities.GetRefreshEstado;
import com.example.proyectofinalencuesta_bdubicuo.data.remote.networkEntities.GetSegmentosResponse;
import com.example.proyectofinalencuesta_bdubicuo.data.remote.networkEntities.PostCuestionarioEnvio;
import com.example.proyectofinalencuesta_bdubicuo.data.remote.networkEntities.PostEnviarRecorrido;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CensoApiService {
    // The Endpoint is defined in the @GET annotation.

    @GET("Segmentos/todos/{empadronadorID}")
    LiveData<ApiResponse<List<GetSegmentosResponse>>> getSegmentosAsignados(
            @Path("empadronadorID") String empadronadorID);

//    @GET("Segmentos/todos/{empadronadorID}")
//    LiveData<ApiResponse<List<Segmentos>>> getSegmentosAsignadosNuevos(
//            @Path("empadronadorID") String empadronadorID,
//            @Query("filtrados") String tipo);

    @GET("Segmentos/todos/{empadronadorID}")
    LiveData<ApiResponse<List<GetSegmentosResponse>>> getSegmentosAsignadosNuevosFiltro(
            @Path("empadronadorID") String empadronadorID,//
            @Query("filtrados") String tipo);

/*
    @GET("Segmentos/todos/{empadronadorID}")
    Call<List<GetSegmentosResponse>> getSegmentosAsignadosNuevos3(
            @Path("empadronadorID") String empadronadorID,
            @Query("filtrados") String tipo);
*/

    @GET("Segmentos/estados/{empadronadorID}/{fechaUltimoSync}")
    Call<List<GetRefreshEstado>> actualizarEstados(
            @Path("empadronadorID") String usuario,
            @Path("fechaUltimoSync") String fechaUltimoSync);

    @GET("Cuestionarios/single/{llaveId}")
    Call<Cuestionarios> getCuestionarioByLlave(
            @Path("llaveId") String llaveId);

    //    @GET("Cuestionarios/propios")
    @GET("Cuestionarios/empadronador/{empadronadorID}")
    LiveData<ApiResponse<List<Cuestionarios>>> getCuestionarios(
            @Path("empadronadorID") String usuario);

    @GET("Cuestionarios/recorridos/empadronador/{empadronadorID}")
    LiveData<ApiResponse<List<ControlRecorrido>>> getRecorridosByEmp(
            @Path("empadronadorID") String usuario);


    @POST("Cuestionarios/")
    Call<Void> sendCuestionario(@Body PostCuestionarioEnvio cuestionarioSelected);


    @POST("cuestionarios/recorridos/")
    Call<Void> sendControlRecorrido(@Body PostEnviarRecorrido controlRecorridoSelected);

    @PUT("Cuestionarios/{llave}")
    Call<Void> sendCuestionarioUpdate(@Body Cuestionarios cuestionarioSelected,
                                      @Path("llave") String llave);

    @PUT("Cuestionarios/recorridos/{llave}")
    Call<Void> sendControlRecorridoUpdate(@Body PostEnviarRecorrido cuestionarioSelected,
                                          @Path("llave") String llave);

    @DELETE("Cuestionarios/{llave}")
    Call<Void> eliminarCuestionarioServer(
            @Path("llave") String llave);

    @DELETE("Cuestionarios/recorridos/{llave}")
    Call<Void> eliminarRecorridoServer(
            @Path("llave") String llave);

/*    @DELETE("OtrasEstructuras/{llave}")
    Call<Void> eliminarOtraEstructuraServer(
            @Path("llave") String llave);*/


    @GET("Inconsistencias/empadronador/{empadronadorID}")
    Call<List<GetInconsistenciasResponse>> getInconsistencias(
            @Path("empadronadorID") String empadronadorID);

/*    @PUT("Cuestionarios/ecenso/agregar/{llave}")
    LiveData<ApiResponse<CodigoAccesoResponse>> sendCodigoECenso(@Path("llave") String llave);*/
}
