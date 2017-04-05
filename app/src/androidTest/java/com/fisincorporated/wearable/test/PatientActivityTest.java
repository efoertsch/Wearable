package com.fisincorporated.wearable.test;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import com.fisincorporated.wearable.R;
import com.fisincorporated.wearable.patient.Patient;
import com.fisincorporated.wearable.patient.PatientManagerService;
import com.fisincorporated.wearable.patientui.PatientActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;


public class PatientActivityTest extends BaseTest {

    private Resources res;

    @Rule
    public ActivityTestRule<PatientActivity> mActivityRule =
            new ActivityTestRule<>(PatientActivity.class, true, false);

    @Before
    public void setup() {
        super.setup();
        Log.d("Setup", "Setup called");
    }

    // Make sure activity can start
    @Test
    public void patientActivityLaunches() {
        Context targetContext = getContext();
        Intent intent = new Intent(targetContext, PatientActivity.class);
        mActivityRule.launchActivity(intent);
    }

    @Test
    public void checkPatients() {
        ArrayList<Patient> patientList = (new PatientManagerService()).getPatientList();
        Patient patient;
        patientActivityLaunches();
        res = mActivityRule.getActivity().getResources();

        // Cycle through each row of patient, and check for display of name/bp/pulse
        for (int i = 0; i < patientList.size(); ++i) {
            patient = patientList.get(i);
            scrollToRecyclerPosition(R.id.patient_recyclerView, i);
            // On row i in recyclerview, find text field and check text matches
            onView(withRecyclerView(R.id.patient_recyclerView).atPosition(i)).check(matches(hasDescendant(allOf(
                    withId(R.id.patient_patientName), withText(patient.getName())))));

            onView(withRecyclerView(R.id.patient_recyclerView).atPosition(i)).check(matches(hasDescendant(allOf(
                    withId(R.id.patient_blood_pressure), withText(res.getString(R.string.patient_bp_label,patient.getBp()))))));

            onView(withRecyclerView(R.id.patient_recyclerView).atPosition(i)).check(matches(hasDescendant(allOf(
                    withId(R.id.patient_heart_rate), withText(res.getString(R.string.patient_pulse_label, patient.getPulse()))))));
        }

    }

    @Test
    public void patienUpdateDisplaysDialogAndScrollsToPatient() {

        Context targetContext = getContext();
        res = targetContext.getResources();
        Intent intent = new Intent(targetContext, PatientActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        Patient patientUpdate = new Patient(3, "Iam Critical", "100/60", 42);
        intent.putExtra(targetContext.getResources().getString(R.string.patient_alert), patientUpdate);
        mActivityRule.launchActivity(intent);

         onView(withText(res.getString(R.string.patient_alert))).check(matches(isDisplayed()));
         onView(withId(android.R.id.button1)).perform(click());

        int i = 2;
        scrollToRecyclerPosition(R.id.patient_recyclerView, i);
        // Check name, bp, pulse
        onView(withRecyclerView(R.id.patient_recyclerView).atPosition(i)).check(matches(hasDescendant(allOf(
                withId(R.id.patient_patientName), withText(patientUpdate.getName())))));

        onView(withRecyclerView(R.id.patient_recyclerView).atPosition(i)).check(matches(hasDescendant(allOf(
                withId(R.id.patient_blood_pressure), withText(res.getString(R.string.patient_bp_label,patientUpdate.getBp()))))));

        onView(withRecyclerView(R.id.patient_recyclerView).atPosition(i)).check(matches(hasDescendant(allOf(
                withId(R.id.patient_heart_rate), withText(res.getString(R.string.patient_pulse_label, patientUpdate.getPulse()))))));
    }

}
