package com.fisincorporated.wearable.patient;

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

import javax.inject.Inject;

public class PatientViewModel extends RecyclerViewViewModel implements AmbientChange {

    private ObservableArrayList<Patient> patientList = new ObservableArrayList<>();

    private final Context context;

    @Inject
    public PatientRecyclerViewAdapter adapter;

    @Inject
    public PatientManager patientManager;

    public PatientViewModel(Context context, @Nullable State savedInstanceState) {
        super(savedInstanceState);
        this.context = context;
        DaggerComponentProvider.getComponent().inject(this);
        adapter.setItems(patientManager.getPatientList());
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
//        textView.setTextColor(Color.WHITE);
//        textView.getPaint().setAntiAlias(false);
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

    public void updatePatient(Patient patientUpdate) {
        ObservableArrayList<Patient> patients =  adapter.getItems();
        for (int i = 0 ; i < patients.size(); ++i) {
            if (patients.get(i).getId() == patientUpdate.getId()) {
                Patient patient = patients.get(i);
                patient.setBp(patientUpdate.getBp());
                patient.setPulse(patientUpdate.getPulse());
                adapter.notifyItemRangeChanged(i, 1);
                scrollToPosition(i);
                break;
            }
        }
    }

    public void scrollToPosition(int i) {
        layoutManager.scrollToPosition(i);
    }
}
