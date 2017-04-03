package com.fisincorporated.wearable.patientui;

import android.databinding.Bindable;

import com.fisincorporated.wearable.model.ItemViewModel;
import com.fisincorporated.wearable.patient.Patient;

public class PatientItemViewModel extends ItemViewModel<Patient> {

    private Patient patient;

    @Override
    public void setItem(Patient item) {
        patient = item;
        notifyChange();
    }

    @Bindable
    public String getName() {
        return patient.getName();
    }

    @Bindable
    public String getBp() {
        return patient.getBp();
    }

    @Bindable
    public int getPulse() {
        return patient.getPulse();
    }
}
