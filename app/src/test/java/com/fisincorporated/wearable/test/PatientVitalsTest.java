package com.fisincorporated.wearable.test;

import com.fisincorporated.wearable.patient.Patients;
import com.fisincorporated.wearable.retrofit.AppRetrofit;
import com.fisincorporated.wearable.retrofit.PatientVitalsService;

import org.junit.Test;

import java.io.IOException;

import retrofit.MockInterceptor;
import retrofit2.Call;

public class PatientVitalsTest {

    PatientVitalsService client = new AppRetrofit(new MockInterceptor()).getRetrofit().create(PatientVitalsService.class);

    @Test
    public void tesPatientVitalsApiCall() throws IOException {
        Call<Patients> call = client.getPatientVitals("abcde");
        Patients patients = call.execute().body();
        System.out.println(patients.toString());
    }
}
