package com.fisincorporated.wearable.test;

import android.content.Context;
import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;
import com.fisincorporated.wearable.R;
import com.fisincorporated.wearable.patient.PatientActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


public class PatientActivityTest extends BaseTest{
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
    public void PatientActivityLaunches() {
        Context targetContext = getContext();
        Intent intent = new Intent(targetContext, PatientActivity.class);
        mActivityRule.launchActivity(intent);
    }

    @Test
    public void checkPatients(){
        Context targetContext = getContext();
        Intent intent = new Intent(targetContext, PatientActivity.class);

        mActivityRule.launchActivity(intent);

        scrollToRecyclerPosition(R.id.patient_recyclerView, 0);
         ViewInteraction viewInteraction = onView(withRecyclerView(R.id.patient_recyclerView).atPosition(0));
//        // Check name, bp, pulse
        viewInteraction.check(matches(hasDescendant(withId(R.id.patient_patientName))));

    }

}
