package com.fisincorporated.wearable.patient;



import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.databinding.ObservableArrayList;

import com.fisincorporated.wearable.retrofit.AppRetrofit;
import com.fisincorporated.wearable.retrofit.PatientVitalsService;

import java.util.List;

import javax.inject.Inject;

import okhttp3.Interceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PatientManagerService extends JobService{

    private static final int JOB_ID = 1;
    private static final long PERIODIC = 10000;

    private PatientVitalsService service;
    private ObservableArrayList<Patient> patientList = new ObservableArrayList<>();
    private String patientSection = "";
    private JobParameters jobParameters;
    private Context context;
    private  JobInfo jobInfo;

    private PatientVitalsCallback patientVitalsCallback;

    public interface PatientVitalsCallback {
        void updatePatientVitals(List<Patient> patient);
    }

    public void setPatientVitalsCallback(PatientVitalsCallback patientVitals) {
        this.patientVitalsCallback = patientVitals;
    }

    public ObservableArrayList<Patient> getPatientList() {
        return patientList;
    }

    public PatientManagerService(){}

    @Inject
    public PatientManagerService (Context context, Interceptor interceptor) {
        this.context = context;
        service = new AppRetrofit(interceptor).getRetrofit().create(PatientVitalsService.class);
//        ComponentName serviceName = new ComponentName(context, PatientManagerService.class);
//        jobInfo = new JobInfo.Builder(JOB_ID, serviceName)
//                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
//                .setPeriodic(PERIODIC)
//                .setRequiresDeviceIdle(true)
//                .setPersisted(true)
//                .build();
    }


    public void setPatientSection(String section) {
        patientSection = section;
    }

    public void startPatientUpdates() {
//        JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
//        int result = scheduler.schedule(jobInfo);
//        if (result == JobScheduler.RESULT_SUCCESS) {
//            Log.d(TAG, "Job scheduled successfully!");
//        }
    }

    private void callForPatientVitals() {
        Call<Patients> call = service.getPatientVitals(patientSection);
        call.enqueue(new Callback<Patients>() {
            @Override
            public void onResponse(Call<Patients> call, Response<Patients> response) {
                if (response.body().getPatients() != null) {
                    if (patientVitalsCallback != null) {
                        patientVitalsCallback.updatePatientVitals(response.body().getPatients());
                    }
                    System.out.print(response.body().getPatients());
                } else {
                    processError(response);
                }
                callComplete();
            }

            @Override
            public void onFailure(Call<Patients> call, Throwable t) {
                processError(call, t);
            }
        });
    }

    private void processError(Response<Patients> response) {
        //TODO
    }

    private void processError(Call<Patients> call, Throwable t) {
        // TODO
    }

    private void callComplete() {
        jobFinished(jobParameters, true);
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        // This is preformed on the main thread so make sure work done on background thread
        callForPatientVitals();
        return true;
    }


    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }


    private void addStaticPatients() {
        Patient patient = new Patient(1, "John Doe", "120/80", 92);
        patientList.add(patient);

        patient = new Patient(2, "Jane Smith", "110/70", 72);
        patientList.add(patient);

        patient = new Patient(3, "Iam Critical", "120/80", 72);
        patientList.add(patient);

        patient = new Patient(4, "Iam Lazarus", "120/70", 62);
        patientList.add(patient);

        patient = new Patient(5, "Young Blood", "130/80", 72);
        patientList.add(patient);
    }

}
