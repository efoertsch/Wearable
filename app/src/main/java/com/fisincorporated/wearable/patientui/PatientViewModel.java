package com.fisincorporated.wearable.patientui;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;

import com.fisincorporated.wearable.dagger.DaggerComponentProvider;
import com.fisincorporated.wearable.model.RecyclerViewViewModel;
import com.fisincorporated.wearable.patient.Patient;
import com.fisincorporated.wearable.patient.PatientManagerService;

import java.util.List;

import javax.inject.Inject;

//public class PatientViewModel extends RecyclerViewViewModel implements AmbientChange, PatientManagerService.PatientVitalsCallback {
public class PatientViewModel extends RecyclerViewViewModel implements AmbientChange, PatientManagerService.PatientVitalsCallback, LifeCycle {
    private final Context context;

    @Inject
    public PatientRecyclerViewAdapter adapter;

    @Inject
    public PatientManagerService patientManagerService;

    public PatientViewModel(Context context, @Nullable State savedInstanceState) {
        super(savedInstanceState);
        this.context = context;
        DaggerComponentProvider.getComponent().inject(this);
        adapter.setItems(new ObservableArrayList<Patient>());

        // This might be better put in onResume, but only if it is ok to stop getting updates if
        // user backgrounds tha app.
        patientManagerService.setPatientVitalsCallback(this);
        // TODO get patient section
        patientManagerService.schedulePatientUpdate(true, "abcde");

        //patientManagerService.addStaticPatients();
        //updatePatientVitals(patientManagerService.getPatientList());
    }

    @Override
    protected PatientRecyclerViewAdapter getAdapter() {
        return adapter;
    }

    @Override
    protected RecyclerView.LayoutManager createLayoutManager() {
        return new LinearLayoutManager(context);
    }

    /**
     * Attach SnapHelper to recycler view
     * @param recyclerView
     */
    public  void setupPatientRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
    }

    /***
     *  Update UI as needed (text to white/disable anti-aliasing)
     * @param ambientDetails
     */
    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        //textView.setTextColor(Color.WHITE);
        //textView.getPaint().setAntiAlias(false);
    }

    @Override
    public void onExitAmbient() {
        //textView.setTextColor(Color.GREEN);
        //textView.getPaint().setAntiAlias(true);
    }

    @Override
    public void onUpdateAmbient(){
        // do update
    }

    public void updatePatientVitals(List<Patient> patientList) {
        for (Patient patient : patientList) {
            updatePatient(patient, false);
        }
    }

    public void updatePatient(Patient patient) {
        updatePatient(patient, true);
    }

    public void updatePatient(Patient patientUpdate, boolean scrollToPatient) {
        ObservableArrayList<Patient> patients =  adapter.getItems();
        for (int i = 0 ; i < patients.size(); ++i) {
            if (patients.get(i).getId() == patientUpdate.getId()) {
                Patient patient = patients.get(i);
                patient.setBp(patientUpdate.getBp());
                patient.setPulse(patientUpdate.getPulse());
                adapter.notifyItemRangeChanged(i, 1);
                if (scrollToPatient) {
                    scrollToPosition(i);
                }
                return;
            }
        }
        // here if new patient
        patients.add(patientUpdate);
        adapter.notifyItemInserted(patients.size() -1 );
    }

    private void scrollToPosition(int i) {
        layoutManager.scrollToPosition(i);
    }


    @Override
    public void onResume() {}

    @Override
    public void onPause() {}

    public void onDestroy() {
        if (patientManagerService != null) {
            patientManagerService.removePatientVitalsCallback();
        }

    }
}
