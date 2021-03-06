package com.fisincorporated.wearable.firebase;

/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.fisincorporated.wearable.R;
import com.fisincorporated.wearable.patient.Patient;
import com.fisincorporated.wearable.patientui.PatientActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

// From https://github.com/firebase/quickstart-android/blob/master/messaging/app/src/main/java/com/google/firebase/quickstart/fcm/MyFirebaseMessagingService.java
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload. (from FCM console 'advanced options')
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            handleNow(remoteMessage.getData());
        }

    }

    /**
     * Handle time allotted to BroadcastReceivers. (Shortlived tasks)
     */
    private void handleNow(Map<String, String> messageMap) {
        Intent intent = new Intent(this, PatientActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Patient patient = new Patient(messageMap);
        intent.putExtra(getResources().getString(R.string.patient_alert), patient);
        startActivity(intent);
    }

    /**
     * Handle time allotted to BroadcastReceivers. (Shortlived tasks)
     */
    private void handleNow(RemoteMessage remoteMessage) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
            sb.append(entry.getKey() + ":" + entry.getValue() + "\n");
        }
        sendNotification(sb.toString());
        Log.d(TAG, "Short lived task is done.");
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param message
     */
    private void sendNotification(String message) {
        if (message != null) {
            Intent intent = new Intent(this, PatientActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
            intent.putExtra(getResources().getString(R.string.patient_alert),message);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, message.hashCode(), intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);


            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_action_heart)
                    .setContentTitle("Alert")
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_MAX);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        }
    }
}