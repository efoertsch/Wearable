package com.fisincorporated.wearable.test;


import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.fisincorporated.wearable.test.utils.RecyclerViewMatcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Used to set various values/classes used in testing.
 */
public class BaseTest {

    public static final String airportList = "KORH KFIT";

    public Context targetContext;

    public void setup() {
        targetContext = InstrumentationRegistry.getTargetContext();
    }

    public Context getContext() {
        return targetContext ;
    }

    // See https://spin.atomicobject.com/2016/04/15/espresso-testing-recyclerviews/
    // Convenience helper
    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    public static void scrollToRecyclerPosition(int recyclerViewId, int position) {
        onView(withId(recyclerViewId)).perform(scrollToPosition(position));

    }
}


