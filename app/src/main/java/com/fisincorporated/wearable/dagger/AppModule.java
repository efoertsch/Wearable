package com.fisincorporated.wearable.dagger;

import com.fisincorporated.wearable.app.WearApplication;
import com.fisincorporated.wearable.patient.PatientManagerService;
import com.fisincorporated.wearable.patientui.PatientRecyclerViewAdapter;
import com.fisincorporated.wearable.retrofit.AppRetrofit;
import com.fisincorporated.wearable.retrofit.LoggingInterceptor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import retrofit2.Retrofit;


@Module
public class AppModule {

    private WearApplication application;

    public AppModule(WearApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public WearApplication providesWearApplication() {
        return application;
    }

    @Provides
    @Singleton
    public PatientManagerService providePatientManager(){
        return new PatientManagerService(providesWearApplication(), getInterceptor());
    }

    @Provides
    public PatientRecyclerViewAdapter providesPatientAdapter() {
        return new PatientRecyclerViewAdapter();
    }


    @Provides
    @Singleton
    public Retrofit provideAppRetrofit(){
        return new AppRetrofit(getInterceptor()).getRetrofit();
    }

    @Provides
    @Singleton
    public Interceptor getInterceptor(){
        return new LoggingInterceptor();
    }



}
