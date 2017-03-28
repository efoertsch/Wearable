package com.fisincorporated.wearable.dagger;


import com.fisincorporated.wearable.app.WearApplication;

public class DaggerComponentProvider {

    static private DiComponent component;

    static public void init(WearApplication application) {
        component = DaggerDiComponent.builder()
                .appModule(new AppModule(application))
                .build();
        // Currently nothing to inject into this class
        //component.inject(this);
    }

    static public DiComponent getComponent() {
        return component;
    }
}
