package com.fisincorporated.wearable.patient;

import android.os.Bundle;

/**
 * Created by ericfoertsch on 3/24/17.
 */

interface AmbientChange {
    void onEnterAmbient(Bundle ambientDetails);

    void onExitAmbient();

    void onUpdateAmbient();
}
