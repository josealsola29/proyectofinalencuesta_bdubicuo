package com.example.proyectofinalencuesta_bdubicuo.data.local.constants.clients;

import com.example.proyectofinalencuesta_bdubicuo.data.local.constants.AppConstants;
import com.example.proyectofinalencuesta_bdubicuo.data.local.constants.RequestInterceptor;
import com.example.proyectofinalencuesta_bdubicuo.data.local.constants.TokenAuthenticator;
import com.example.proyectofinalencuesta_bdubicuo.data.remote.LiveDataCallAdapterFactory;
import com.example.proyectofinalencuesta_bdubicuo.data.remote.networkServices.CensoApiService;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CensoClient {
    private static CensoClient instance = null;
    private final CensoApiService censoApiService;

    public CensoClient() {
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
//         petición el TOKEN o API_KEEY que autoriza al usuario
        okHttpClientBuilder.authenticator(new TokenAuthenticator());
        okHttpClientBuilder.addInterceptor(new RequestInterceptor("Censo"));
//        okHttpClientBuilder.addInterceptor(new RefreshInterceptor());
        OkHttpClient cliente = okHttpClientBuilder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .client(cliente)
                .build();

        censoApiService = retrofit.create(CensoApiService.class);
    }

    //Patron Singleton
    public static CensoClient getInstance() {
        if (instance == null) {
            instance = new CensoClient();
        }
        return instance;
    }

    public CensoApiService getCensoApiService() {
        return censoApiService;
    }
}
