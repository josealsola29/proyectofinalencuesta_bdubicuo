package com.example.proyectofinalencuesta_bdubicuo.data.local.constants.clients;

import android.util.Log;

import com.example.proyectofinalencuesta_bdubicuo.data.local.constants.AppConstants;
import com.example.proyectofinalencuesta_bdubicuo.data.remote.LiveDataCallAdapterFactory;
import com.example.proyectofinalencuesta_bdubicuo.data.remote.networkServices.CensoAuthService;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthClient {
    private static AuthClient instance = null;
    //    private DirectorioApiService directorioApiService;
    private final CensoAuthService censoAuthService;

    public AuthClient() {
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
            Log.e("TAG", "AuthClient: ",e );
        }

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        if (sslContext != null) {
            okHttpClientBuilder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier((hostname, session) -> true)
                    .build();
        }

        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://84d1-200-46-58-8.ngrok.io/api/")
                .baseUrl(AppConstants.BASE_URL)
//                .baseUrl("http://localhost:5000/api/")
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClientBuilder.build())
                .build();

        censoAuthService = retrofit.create(CensoAuthService.class);
    }

    //Patron Singleton
    public static AuthClient getInstance() {
        if (instance == null) {
            instance = new AuthClient();
        }
        return instance;
    }

    public CensoAuthService getCensoAuthService() {
        return censoAuthService;
    }
}
