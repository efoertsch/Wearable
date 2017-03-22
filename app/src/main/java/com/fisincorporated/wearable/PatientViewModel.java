package com.fisincorporated.wearable;

import android.databinding.ObservableArrayList;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;

public class PatientViewModel {

    private ObservableArrayList<Patient> patientList = new ObservableArrayList<>();


    public PatientViewModel(View view) {

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.patient_recyclerView);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        setupRecyclerView(recyclerView);
        addPatients();

        PatientAdapter patientAdapter = new PatientAdapter(patientList);
        recyclerView.setAdapter(patientAdapter);
    }

    public void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
    }

    private void addPatients() {
         patientList.addAll(PatientManager.getPatients());

    }
}
