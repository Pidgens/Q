package com.example.derekchiu.q;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;
import android.util.Log;

/**
 * Created by tomo on 12/1/15.
 */

public class MobileConnector extends WearableListenerService {

    String WEARABLE_COMPANY_PATH = "/wearable_company";
    String WEARABLE_JS_PATH = "/wearable_js";

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.v("k", "received");

        DataMap dataMap;
        for (DataEvent event : dataEvents) {
            // Check the event type
                Log.v("k", "received");
                // Check the data path
                String path = event.getDataItem().getUri().getPath();
                // Create a local notification
                dataMap = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
                sendLocalNotification(dataMap, path);

        }
    }

    private void sendLocalNotification(DataMap dataMap, String path) {
        int notificationId = 001;

        Intent startIntent;
        Log.v("p", path);
        if (path.equals(WEARABLE_COMPANY_PATH)) {
            Log.v("path", "comp");
            startIntent = new Intent(this, PeopleNamesActivity.class)
                    .setAction(Intent.ACTION_MAIN);
        } else if (path.equals(WEARABLE_JS_PATH)) {
            Log.v("path", "js");

            startIntent = new Intent(this, QueueActivity.class)
                    .setAction(Intent.ACTION_MAIN);
        } else {
            Log.v("path", "choose");

            startIntent = new Intent(this, ChooseFlowActivity.class)
                    .setAction(Intent.ACTION_MAIN);
        }
        PendingIntent startPendingIntent =
                PendingIntent.getActivity(this, 0, startIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notify = new NotificationCompat.Builder(this)
                .setContentTitle("Open Q!")
                .setAutoCancel(true)
                .setContentIntent(startPendingIntent)
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationId, notify);
    }
}
