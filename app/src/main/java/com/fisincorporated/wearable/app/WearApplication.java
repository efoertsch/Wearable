package com.fisincorporated.wearable.app;

import android.app.Application;

import com.fisincorporated.wearable.dagger.DaggerComponentProvider;
import com.fisincorporated.wearable.dagger.DiComponent;
import com.fisincorporated.wearable.firebase.MyFirebaseInstanceIDService;
import com.fisincorporated.wearable.network.NetworkManager;

public class WearApplication extends Application {

    protected DiComponent component;

    private static WearApplication wearApplication;


    public void onCreate() {
        super.onCreate();

        wearApplication = this;

        MyFirebaseInstanceIDService myFirebaseInstanceIDService = new MyFirebaseInstanceIDService();
        myFirebaseInstanceIDService.onTokenRefresh();

        DaggerComponentProvider.init(this);
        NetworkManager.init(this);
    }

    public static WearApplication get() {
        return wearApplication;
    }


}