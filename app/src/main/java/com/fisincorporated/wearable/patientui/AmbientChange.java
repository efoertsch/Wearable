package com.fisincorporated.wearable.patientui;

import android.os.Bundle;


interface AmbientChange {
    void onEnterAmbient(Bundle ambientDetails);

    void onExitAmbient();

    void onUpdateAmbient();
}
