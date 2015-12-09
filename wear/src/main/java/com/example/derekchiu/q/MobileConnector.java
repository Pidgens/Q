package com.example.derekchiu.q;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by tomo on 12/1/15.
 */

public class MobileConnector extends WearableListenerService {

    String WEARABLE_COMPANY_PATH = "/wearable_company";
    String WEARABLE_JS_PATH = "/wearable_js";
    public static final String MOBILE_GET_UPDATE = "/get_update";
    public static final String MOBILE_DROP = "/mobile_drop";
    public static final String MOBILE_BUMP = "/mobile_bump";


    private static GoogleApiClient googleClient;

    public static GoogleApiClient getClient(Context context) {
        initClient(context);
        return googleClient;
    }

    public static void initClient(Context context) {
        if (googleClient == null) {
            googleClient = new GoogleApiClient.Builder(context)
                    .addApi(Wearable.API)
                    .build();
            googleClient.connect();
        }
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        DataMap dataMap;
        for (DataEvent event : dataEvents) {
                String path = event.getDataItem().getUri().getPath();
                dataMap = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
            if (path.equals(WEARABLE_COMPANY_PATH)) {
                Log.d("MobileConnector", "WEARABLE_COMPANY_PATH");
                sendLocalNotification(dataMap, PeopleNamesActivity.class);
            } else if (path.equals(WEARABLE_JS_PATH)) {
                Log.d("MobileConnector", "WEARABLE_JS_PATH");
                sendLocalNotification(dataMap, QueueActivity.class);
            }
        }
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.v("message", "received");
        super.onMessageReceived(messageEvent);
    }

    private void sendLocalNotification(DataMap dataMap, Class<?> class_) {
        int notificationId = 001;

        Intent startIntent = new Intent(this, class_);
        PendingIntent startPendingIntent =
                PendingIntent.getActivity(this, 0, startIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notify = new NotificationCompat.Builder(this)
                .setContentTitle("Open Q!")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_full_sad)
                .setContentIntent(startPendingIntent)
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationId, notify);
    }

    public static void updateQueue() {
        DataMap notifyWearable = new DataMap();
        notifyWearable.putLong("time", System.currentTimeMillis());
        new SendToDataLayerThread(MOBILE_GET_UPDATE, notifyWearable).start();
    }

    public static void dropCompany(String company) {
        DataMap notifyWearable = new DataMap();
        notifyWearable.putLong("time", System.currentTimeMillis());
        notifyWearable.putString("company", company);
        new SendToDataLayerThread(MOBILE_DROP, notifyWearable).start();
    }

    public static void bumpCompany(String company) {
        DataMap notifyWearable = new DataMap();
        notifyWearable.putLong("time", System.currentTimeMillis());
        notifyWearable.putString("company", company);
        new SendToDataLayerThread(MOBILE_BUMP, notifyWearable).start();
    }

    private static class SendToDataLayerThread extends Thread {
        String path;
        DataMap dataMap;

        // Constructor for sending data objects to the data layer
        SendToDataLayerThread(String p, DataMap data) {
            path = p;
            dataMap = data;
        }

        public void run() {
            // Construct a DataRequest and send over the data layer
            PutDataMapRequest putDMR = PutDataMapRequest.create(path);
            putDMR.getDataMap().putAll(dataMap);
            PutDataRequest request = putDMR.asPutDataRequest();
            DataApi.DataItemResult result = Wearable.DataApi.putDataItem(googleClient, request).await();
            if (result.getStatus().isSuccess()) {
                Log.v("myTag", "DataMap: " + dataMap + " sent successfully to data layer ");
            }
            else {
                // Log an error
                Log.v("myTag", "ERROR: failed to send DataMap to data layer");
            }
        }
    }
}
