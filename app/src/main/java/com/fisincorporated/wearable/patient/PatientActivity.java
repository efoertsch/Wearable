package com.fisincorporated.wearable.patient;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.fisincorporated.wearable.R;
import com.fisincorporated.wearable.databinding.ActivityMainBinding;
import com.fisincorporated.wearable.model.ViewModel;
import com.fisincorporated.wearable.model.ViewModelActivity;

public class PatientActivity extends ViewModelActivity {

    PatientViewModel patientViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        patientViewModel.setupPatientRecyclerView(binding.patientRecyclerView);
        binding.setViewModel(patientViewModel);
    }

    @Nullable
    @Override
    protected ViewModel createViewModel(@Nullable ViewModel.State savedViewModelState) {
        return patientViewModel = new PatientViewModel(this, savedViewModelState);
    }

}
