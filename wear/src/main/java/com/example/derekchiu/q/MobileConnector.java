package com.example.derekchiu.q;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.ArrayList;

/**
 * Created by tomo on 12/1/15.
 */

public class MobileConnector extends WearableListenerService {

    String WEARABLE_COMPANY_PATH = "/wearable_company";
    String WEARABLE_JS_PATH = "/wearable_js";
    private static final String WEARABLE_QUEUED_PATH = "/wearable_queued_path";
    private static final String DATA_PLACE_ARRAY = "/place_update_array";

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

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.v("message", "received");
        super.onMessageReceived(messageEvent);
    }

    private void sendLocalNotification(DataMap dataMap, String path) {
        int notificationId = 001;

        Intent startIntent = null;
        Log.v("p", path);
        if (path.equals(WEARABLE_COMPANY_PATH)) {
            Log.v("path", "comp");
            startIntent = new Intent(this, PeopleNamesActivity.class);
        } else if (path.equals(WEARABLE_JS_PATH)) {
            Log.v("path", "js");

            startIntent = new Intent(this, QueueActivity.class);
        } else if (path.equals(WEARABLE_QUEUED_PATH)) {
            ArrayList<String> currentList = dataMap.getStringArrayList(DATA_PLACE_ARRAY);
            if (currentList != null) {
                Log.d("MobileConnector", currentList.toString());
                CompanyPlaceList.updateList(currentList);
                if (QueueGridPagerAdapter.getInstance() != null) {
                    QueueGridPagerAdapter.getInstance().notifyDataSetChanged();
                }
            } else {
                Log.d("MobileConnector", "Did not receive data");
            }
            Log.v("path", "queued_update");
        } else {
            Log.v("path", "choose");

            startIntent = new Intent(this, ChooseFlowActivity.class);
        }
        PendingIntent startPendingIntent =
                PendingIntent.getActivity(this, 0, startIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notify = new NotificationCompat.Builder(this)
                .setContentTitle("Open Q!")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_full_sad)
                .setContentIntent(startPendingIntent)
                .build();

        Log.v("path", "hello world");
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        Log.v("path", "hello world2");
        notificationManager.notify(notificationId, notify);
    }
}
