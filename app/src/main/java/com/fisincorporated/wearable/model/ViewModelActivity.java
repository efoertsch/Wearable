package com.fisincorporated.wearable.model;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.wearable.activity.WearableActivity;


// From https://medium.com/@hiBrianLee/writing-testable-android-mvvm-app-part-1-ac2c39f31710#.juryh1k9x
// see link for Fragment version
public abstract class ViewModelActivity extends WearableActivity {

    private static final String EXTRA_VIEW_MODEL_STATE = "viewModelState";

    private ViewModel viewModel;

    @Nullable
    protected abstract ViewModel createViewModel(@Nullable ViewModel.State savedViewModelState);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewModel.State savedViewModelState = null;
        if (savedInstanceState != null) {
            savedViewModelState = savedInstanceState.getParcelable(EXTRA_VIEW_MODEL_STATE);
        }
        viewModel = createViewModel(savedViewModelState);

        setAmbientEnabled();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (viewModel != null) {
            viewModel.onStart();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (viewModel != null) {
            outState.putParcelable(EXTRA_VIEW_MODEL_STATE, viewModel.getInstanceState());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (viewModel != null) {
            viewModel.onStop();
        }
    }
}