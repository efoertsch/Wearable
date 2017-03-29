package com.fisincorporated.wearable.patient;



import android.databinding.ObservableArrayList;

import javax.inject.Inject;


public class PatientManager {

    private ObservableArrayList<Patient> patientList = new ObservableArrayList<>();

    @Inject
    public PatientManager() {
        addPatients();
    }

    private void addPatients() {
        Patient patient = new Patient(1, "John Doe", "120/80", 92);
        patientList.add(patient);

        patient = new Patient(2, "Jane Smith", "110/70", 72);
        patientList.add(patient);

        patient = new Patient(3, "Iam Critical", "120/80", 72);
        patientList.add(patient);

        patient = new Patient(4, "Iam Lazarus", "120/70", 62);
        patientList.add(patient);

        patient = new Patient(5, "Young Blood", "130/80", 72);
        patientList.add(patient);
    }

    public ObservableArrayList<Patient> getPatientList(){
        return patientList;
    }
}
