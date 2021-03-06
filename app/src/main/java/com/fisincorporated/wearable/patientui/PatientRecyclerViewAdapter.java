package com.fisincorporated.wearable.patientui;


import android.databinding.ObservableArrayList;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fisincorporated.wearable.R;
import com.fisincorporated.wearable.databinding.PatientLayoutBinding;
import com.fisincorporated.wearable.model.RecyclerViewAdapter;
import com.fisincorporated.wearable.patient.Patient;

import javax.inject.Inject;

public class PatientRecyclerViewAdapter extends RecyclerViewAdapter<Patient, PatientItemViewModel> {


    @Inject
    public PatientRecyclerViewAdapter() {}

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Get detail layout
        View itemView = LayoutInflater.from(parent.getContext()).inflate(ViewHolder.LAYOUT_RESOURCE, parent, false);
        // Create new patient detail view model
        PatientItemViewModel patientItemViewModel = new PatientItemViewModel();
        // Get binding to the view
        PatientLayoutBinding binding = PatientLayoutBinding.bind(itemView);
        // Store the binding and model for the item
        binding.setPatient(patientItemViewModel);
        return new PatientRecyclerViewAdapter.ViewHolder(itemView, binding, patientItemViewModel);

    }

    // Note the RecyclerViewAdapter superclass has a generic onBindViewHolder method

    public void setItems(ObservableArrayList<Patient> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    public ObservableArrayList<Patient> getItems() {
        return items;
    }

    public static class ViewHolder extends ItemViewHolder<Patient, PatientItemViewModel> {

        protected static final int LAYOUT_RESOURCE = R.layout.patient_layout;

        public ViewHolder(View itemView, ViewDataBinding binding, PatientItemViewModel viewModel) {
            super(itemView, binding, viewModel);
        }

    }


}
