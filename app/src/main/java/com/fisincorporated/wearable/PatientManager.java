package com.fisincorporated.wearable;



import android.databinding.ObservableArrayList;

public class PatientManager {

    private ObservableArrayList<Patient> patientList = new ObservableArrayList<>();

    private static PatientManager patientManager;

    public static ObservableArrayList<Patient> getPatients() {
        if (patientManager == null) {
            patientManager = new PatientManager();
            patientManager.addPatients();
        }
        return patientManager.patientList;
    }

    private void addPatients() {
        Patient patient = new Patient("John Doe", "120/80", 92);
        patientList.add(patient);

        patient = new Patient("Jane Smith", "110/70", 72);
        patientList.add(patient);

        patient = new Patient("Iam Critical", "90/60", 52);
        patientList.add(patient);

        patient = new Patient("Iam Lazarus", "120/70", 62);
        patientList.add(patient);

        patient = new Patient("Young Blood", "130/80", 72);
        patientList.add(patient);
    }
}
