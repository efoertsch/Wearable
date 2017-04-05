package com.fisincorporated.wearable.dagger;

import com.fisincorporated.wearable.patientui.PatientViewModel;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface DiComponent {

    void inject(PatientViewModel patientViewModel);

}
