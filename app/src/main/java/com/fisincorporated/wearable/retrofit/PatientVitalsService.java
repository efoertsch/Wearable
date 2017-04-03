package com.fisincorporated.wearable.retrofit;

import com.fisincorporated.wearable.patient.Patients;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;



public interface PatientVitalsService {

    @GET("patientVitals?")
    Call<Patients> getPatientVitals(@Query("section") String section);
}
