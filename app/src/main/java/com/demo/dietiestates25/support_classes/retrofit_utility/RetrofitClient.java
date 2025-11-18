package com.demo.dietiestates25.support_classes.retrofit_utility;

import android.content.Context;

import androidx.annotation.NonNull;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.data.model.Role;
import com.demo.dietiestates25.utils.ResourceUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Classe Helper con la SOLA RESPONSABILITA di creare e configurare un'istanza Singleton di Retrofit.
 * Non conosce i servizi specifici, è un costruttre generico.
 */
public final class RetrofitClient {
    public static final String PROTOCOL = "http://";
    private static String baseUrl;
    private static final int PORT = 8080;
    private static volatile Retrofit retrofitInstance = null; // Volatile per il thread safety

    /* COSTRUTTORE PRIVATO */
    private RetrofitClient(){}

    /**
     * DTO interno per restituire il risultato della creazione di Retrofit.
     */
    public static class RetrofitResult {
        public final PropertyServiceAPI serviceAPI;
        public final String error;
        public RetrofitResult(PropertyServiceAPI serviceAPI, String error) {
            this.serviceAPI = serviceAPI;
            this.error = error;
        }
        public boolean isSuccess() { return serviceAPI != null; }
    }

    /**
     * Ottiene l'istanza Singleton di Retrofit.
     * Usa il pattern Double-Checked Locking per garantire il thread safety in modo efficiente.
     */
    public static Retrofit getInstance(@NonNull Context context) {
        // Double-Checked Locking per performance e thread safety
        if (retrofitInstance == null) {
            synchronized (RetrofitClient.class) {
                if(retrofitInstance == null) {
                    Context appContext = context.getApplicationContext();
                    String newIp = IpAddressReader.readIpAddressFromFile(appContext);
                    String domain = newIp + ":" + PORT;

                    // Controlla il dominio. Lancia un'eccezione se non è permesso.
                    if(!NetworkConfigUpdater.isDomainAllowed(domain)) {
                        String errorMsg = ResourceUtils.getXMLString(appContext, R.string.domain_connection_denied) + ": " + domain;
                        throw new SecurityException(errorMsg);
                    }
                    baseUrl = PROTOCOL + newIp + ":" + PORT + "/";

                    // Costruisce il Client OkHttp con entrambi gli interceptor
                    OkHttpClient okHttpClient = getOkHttpClient(appContext, 15);

                    retrofitInstance = new Retrofit.Builder()
                            .baseUrl(baseUrl)
                            .client(okHttpClient)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return retrofitInstance;
    }

    /**
     * Metodo Factory per creare un'istanza Retrofit con health check.
     */
    public static RetrofitResult getRetrofitInstance2(Context context, String userRole) {
        try {
            String newIp = IpAddressReader.readIpAddressFromFile(context);
            baseUrl = PROTOCOL + newIp + ":" + PORT + "/";
            String domain = newIp + ":" + PORT;

            // Usa ResourceUtils
            if(!NetworkConfigUpdater.isDomainAllowed(domain)) {
                String err = ResourceUtils.getXMLString(context, R.string.domain_connection_denied) + ": " + domain;
                return new RetrofitResult(null, err);
            }

            // Crea un client per il ping (health check)
            PropertyServiceAPI tmpService = createNewInstance(context, 2, baseUrl, ScalarsConverterFactory.create()).create(PropertyServiceAPI.class);
            // Crea il client principale per l'app
            PropertyServiceAPI service = createNewInstance(context, 10, baseUrl, GsonConverterFactory.create()).create(PropertyServiceAPI.class);

            if(isServerOnline(tmpService)) {
                return new RetrofitResult(service, null);
            }
            else {
                String serverError;
                if(userRole != null && !userRole.equals(Role.USER.getApiName())) {
                    serverError = String.format(ResourceUtils.getXMLString(context, R.string.server_not_reachable), domain);
                }
                else {
                    serverError = ResourceUtils.getXMLString(context, R.string.server_not_reachable_for_user);
                }
                return new RetrofitResult(null, serverError);
            }

        } catch (Exception e) {
            return new RetrofitResult(null,"Config error in Retrofit client: " + e.getMessage());
        }
    }

    /**
     * Metodo 'getRetrofitInstance' chiama il Singleton 'getInstance' e poi crea il servizio.
     */
    public static PropertyServiceAPI getRetrofitInstance(Context context) {
        return getInstance(context).create(PropertyServiceAPI.class);
    }

    public static String getBaseUrl() { return baseUrl; }

    /**
     * Helper privato per creare una NUOVA istanza di Retrofit (non singleton).
     */
    private static Retrofit createNewInstance(Context context, int timeout, @NonNull String baseUrl, @NonNull Converter.Factory converterFactory) {
        OkHttpClient client = getOkHttpClient(context, timeout);
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(converterFactory)
                .build();
    }

    /**
     * Helper privato per creare un OkHttpClient configurato.
     */
    @NonNull
    private static OkHttpClient getOkHttpClient(Context context, int timeout) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(context))
                .addInterceptor(logging)
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .readTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS)
                .build();
    }

    /**
     * Esegue un 'ping' (health check) sincrono al server.
     */
    private static boolean isServerOnline(PropertyServiceAPI tmpApiService) {
        try {
            Call<String> call = tmpApiService.healthCheck();
            Response<String> response = call.execute();
            return response.isSuccessful() && response.body() != null && !response.body().trim().isEmpty();
        } catch(IOException e) {
            return false;
        }
    }
}
