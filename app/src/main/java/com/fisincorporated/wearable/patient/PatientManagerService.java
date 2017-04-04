package com.fisincorporated.wearable.patient;



import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.databinding.ObservableArrayList;
import android.os.PersistableBundle;

import com.fisincorporated.wearable.retrofit.AppRetrofit;
import com.fisincorporated.wearable.retrofit.PatientVitalsService;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import okhttp3.Interceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientManagerService extends JobService {
    private static final String TAG = PatientManagerService.class.getSimpleName();

    private static int JOB_ID = 0;
    private static final long IMMEDIATE_CALL = 50;
    private static final long PERIODIC_CALL = 5000;
    private static final String PATIENT_SECTION = "PATIENT_SECTION";

    private static PatientVitalsService service;
    private static String patientSection = "";
    private static Context context;
    private static PatientVitalsCallback patientVitalsCallback;

    private ObservableArrayList<Patient> patientList = new ObservableArrayList<>();
    private JobParameters jobParameters;

    private JobInfo jobInfo;

    public interface PatientVitalsCallback {
        void updatePatientVitals(List<Patient> patient);
    }

    public ObservableArrayList<Patient> getPatientList() {
        return patientList;
    }

    public PatientManagerService() {
        System.out.println("PatientManagerService default constructor called");
    }

    @Inject
    public PatientManagerService(Context context, Interceptor interceptor) {
        this.context = context;
        service = new AppRetrofit(interceptor).getRetrofit().create(PatientVitalsService.class);

    }

    public void setPatientSection(String section) {
        patientSection = section;
    }

    public void schedulePatientUpdate(boolean firstTimeOrError, String patientSection) {
        // See https://code.google.com/p/android/issues/detail?id=229398
        // But anyway, want to immediately schedule if first time or error or every minute(or whatever
        // specified thereafter
        // Set specifically for N or higher

        this.patientSection = patientSection;

        long scheduleInMillis;
        if (firstTimeOrError) {
            scheduleInMillis = IMMEDIATE_CALL;
        } else {
            scheduleInMillis = PERIODIC_CALL;
        }

        ComponentName serviceName = new ComponentName(context, PatientManagerService.class);
        PersistableBundle persistableBundler = new PersistableBundle();
        persistableBundler.putString(PATIENT_SECTION, patientSection);
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID++, serviceName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setRequiresDeviceIdle(false)
                .setMinimumLatency(scheduleInMillis)
                .setExtras(persistableBundler);
        jobInfo = builder.build();

        JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        int result = scheduler.schedule(jobInfo);
        System.out.println("Job scheduled " + (result == JobScheduler.RESULT_SUCCESS ? "successfully" : "unsuccessfully"));
    }

    private void callForPatientVitals() {
        if (service != null) {
            Call<Patients> call = service.getPatientVitals(patientSection);
            call.enqueue(new Callback<Patients>() {
                @Override
                public void onResponse(Call<Patients> call, Response<Patients> response) {
                    if (response.body().getPatients() != null) {
                        if (patientVitalsCallback != null) {
                            patientVitalsCallback.updatePatientVitals(response.body().getPatients());
                        }
                        System.out.println(response.body().getPatients());
                    } else {
                        processError(response);
                    }
                    callComplete(false);
                }

                @Override
                public void onFailure(Call<Patients> call, Throwable t) {
                    processError(call, t);
                }
            });
        }
    }

    private void processError(Response<Patients> response) {
        // TODO
        System.out.println("processError() response.code(): " + response.code() + "errorBody:" + response.errorBody());
        callComplete(true);
    }


    private void processError(Call<Patients> call, Throwable t) {
        // TODO
        System.out.println("processError() : " + t.toString());
        callComplete(true);
    }

    private void callComplete(boolean firstTimeOrError) {
        jobFinished(jobParameters, false);
        schedulePatientUpdate(firstTimeOrError, patientSection);

    }

    // If onStartJob not being call make sure setAmbientEnabled() called for WearActivity app
    // (so not in Doze/App Standby mode
    @Override
    public boolean onStartJob(JobParameters params) {
        // This is preformed on the main thread so make sure work done on background thread
        System.out.println("onStartJob at:" + new Date());
        this.jobParameters = params;
        callForPatientVitals();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        System.out.println("onStopJob at:" + new Date());
        this.jobParameters = params;
        return true;
    }

    public void setPatientVitalsCallback(PatientVitalsCallback patientVitals) {
        this.patientVitalsCallback = patientVitals;
    }

    public void removePatientVitalsCallback() {
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.cancelAll();
        this.patientVitalsCallback = null;
    }


    public void addStaticPatients() {
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
