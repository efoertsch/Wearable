package com.fisincorporated.wearable.patient;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.wearable.view.WearableDialogHelper;
import android.util.Log;

import com.fisincorporated.wearable.R;
import com.fisincorporated.wearable.databinding.ActivityMainBinding;
import com.fisincorporated.wearable.model.ViewModel;
import com.fisincorporated.wearable.model.ViewModelActivity;
import com.google.firebase.iid.FirebaseInstanceId;

// TODO - check for network connection and if non start NetworkSetupActivity
public class PatientActivity extends ViewModelActivity {

    private static final String TAG = PatientActivity.class.getSimpleName();

    PatientViewModel patientViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        patientViewModel.setupPatientRecyclerView(binding.patientRecyclerView);
        binding.setViewModel(patientViewModel);
        getFirebaseToken();
        checkForAlert(getIntent());
    }

    public void onNewIntent(Intent intent) {
        checkForAlert(intent);
    }

    private void checkForAlert(Intent intent) {
        Patient patient = intent.getParcelableExtra(getResources().getString(R.string.patient_alert));
        if (patient != null) {
            StringBuilder sb = new StringBuilder();
            if (patient.getId() > 0) {
                Resources res = getResources();
                sb.append(res.getString(R.string.patient_name, patient.getName()) + "\n"
                        + res.getString(R.string.patient_bp_label, patient.getBp()) + "\n"
                        + res.getString(R.string.patient_pulse_label, patient.getPulse()));
            }
            if (sb.length() > 0) {
                WearableDialogHelper.DialogBuilder builder = new WearableDialogHelper.DialogBuilder(this);
                builder.setIcon(R.drawable.ic_action_heart).setTitle(R.string.patient_alert).setMessage(sb.toString())
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                builder.create().show();
            }
            patientViewModel.updatePatient(patient);
        }
    }


    @Nullable
    @Override
    protected ViewModel createViewModel(@Nullable ViewModel.State savedViewModelState) {
        return patientViewModel = new PatientViewModel(this, savedViewModelState);
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        patientViewModel.onEnterAmbient(ambientDetails);

    }

    @Override
    public void onExitAmbient() {
        super.onExitAmbient();
        patientViewModel.onExitAmbient();
    }


    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        patientViewModel.onUpdateAmbient();
        // Update the content
    }


    private void getFirebaseToken() {
        String token = FirebaseInstanceId.getInstance().getToken();
        // Log and toast
        String msg = "Firebase Token:" + token;
        Log.d(TAG, msg);
        // Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

}
