package com.fisincorporated.wearable.patient;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;

import com.fisincorporated.wearable.model.RecyclerViewAdapter;
import com.fisincorporated.wearable.model.RecyclerViewViewModel;

public class PatientViewModel extends RecyclerViewViewModel {

    private ObservableArrayList<Patient> patientList = new ObservableArrayList<>();

    private final Context appContext;

    private  PatientAdapter adapter;

    public PatientViewModel(Context context, @Nullable State savedInstanceState) {
        super(savedInstanceState);
        appContext = context.getApplicationContext();

        adapter = new PatientAdapter();
        adapter.setItems(PatientManager.getPatients());
    }


    @Override
    protected RecyclerViewAdapter getAdapter() {
        return adapter;
    }

    @Override
    protected RecyclerView.LayoutManager createLayoutManager() {
        return new LinearLayoutManager(appContext);
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



}
