package com.fisincorporated.wearable.retrofit;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AppRetrofit {

    public static final String PATIENT_URL = "https://dummy.com/patients/vitals/";

    private Retrofit retrofit;

    @Inject
    public AppRetrofit(Interceptor interceptor){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(interceptor);
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(PATIENT_URL)
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create());

        retrofit = builder.client(httpClient.build()).build();
    };

    public Retrofit getRetrofit() {
        return retrofit;
    }



}
