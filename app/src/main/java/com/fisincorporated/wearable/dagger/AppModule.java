package com.fisincorporated.wearable.dagger;

import com.fisincorporated.wearable.app.WearApplication;
import com.fisincorporated.wearable.patient.PatientRecyclerViewAdapter;
import com.fisincorporated.wearable.patient.PatientManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class AppModule {

    private WearApplication application;

    public AppModule(WearApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public PatientManager providePatientManager(){
        return new PatientManager();
    }

    @Provides
    public PatientRecyclerViewAdapter providesPatientAdapter() {
        return new PatientRecyclerViewAdapter();
    }

    @Provides
    public WearApplication providesWearApplication() {
        return application;
    }



}
