package com.fisincorporated.wearable;

import android.app.Application;

import com.fisincorporated.wearable.firebase.MyFirebaseInstanceIDService;

/**
 * Created by ericfoertsch on 3/24/17.
 */

public class WearableApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MyFirebaseInstanceIDService myFirebaseInstanceIDService = new MyFirebaseInstanceIDService();
        myFirebaseInstanceIDService.onTokenRefresh();
    }
}
