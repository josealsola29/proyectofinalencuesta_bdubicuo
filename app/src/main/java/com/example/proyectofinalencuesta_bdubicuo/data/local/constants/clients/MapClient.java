package com.example.proyectofinalencuesta_bdubicuo.data.local.constants.clients;

import com.example.proyectofinalencuesta_bdubicuo.data.local.constants.AppConstants;
import com.example.proyectofinalencuesta_bdubicuo.data.local.constants.RequestInterceptor;
import com.example.proyectofinalencuesta_bdubicuo.data.local.constants.TokenAuthenticator;
import com.example.proyectofinalencuesta_bdubicuo.data.remote.LiveDataCallAdapterFactory;
import com.example.proyectofinalencuesta_bdubicuo.data.remote.networkServices.CensoMapService;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapClient {
    private static MapClient instance = null;
    private final CensoMapService censoMapService;

    public MapClient() {
        SSLContext sslContext = null;
        TrustManager[] trustAllCerts = null;
        try {
            trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        if (sslContext != null) {
            okHttpClientBuilder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier((hostname, session) -> true)
                    .build();
        }

//         RequestInterceptor: incluir en la cabecera (URL) de la
//         petición el TOKEN o API_KEY que autoriza al usuario
        okHttpClientBuilder.authenticator(new TokenAuthenticator());
        okHttpClientBuilder.addInterceptor(new RequestInterceptor("Map"));
//        okHttpClientBuilder.addInterceptor(new RefreshInterceptor());
        OkHttpClient cliente = okHttpClientBuilder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL_MAPS)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .client(cliente)
                .build();

        censoMapService = retrofit.create(CensoMapService.class);
    }

    //Patron Singleton
    public static MapClient getInstance() {
        if (instance == null) {
            instance = new MapClient();
        }
        return instance;
    }

    public CensoMapService getCensoMapService() {
        return censoMapService;
    }
}
