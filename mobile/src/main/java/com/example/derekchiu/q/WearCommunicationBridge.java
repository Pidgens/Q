package com.example.derekchiu.q;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomo on 12/8/15.
 */
public class WearCommunicationBridge extends WearableListenerService {

    public WearCommunicationBridge() {}

    private static GoogleApiClient googleClient;
    private static String android_id;
    private static final String WEARABLE_COMPANY_PATH = "/wearable_company";
    private static final String WEARABLE_JS_PATH = "/wearable_js";
    private static final String WEARABLE_QUEUED_PATH = "/wearable_queued_path";
    private static final String DATA_PLACE_ARRAY = "/place_update_array";
    public static final String MOBILE_GET_UPDATE = "/get_update";
    public static final String MOBILE_DROP = "/mobile_drop";
    public static final String MOBILE_BUMP = "/mobile_bump";

    private static void init(Context context) {
        if (googleClient == null) {
            googleClient = new GoogleApiClient.Builder(context)
                    .addApi(Wearable.API)
                    .build();
            googleClient.connect();
        }
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.d("WearConnection", "WEARABLE_QUEUED_PATH");
        DataMap dataMap;
        for (DataEvent event : dataEvents) {
            String path = event.getDataItem().getUri().getPath();
            dataMap = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
            if (path.equals(MOBILE_GET_UPDATE)) {
                Log.d("WearConnection", "MOBILE_GET_UPDATE");
                Log.d("WearConnection", android_id);
                updateQueue(null, android_id);
            } else if (path.equals(MOBILE_BUMP)) {
                Log.d("WearConnection", "MOBILE_BUMP");
                String company = dataMap.getString("company");
                Log.d("WearConnection", company);
                DBUtil.bumpBackInQueue(company, android_id, null);
            } else if (path.equals(MOBILE_DROP)) {
                Log.d("WearConnection", "MOBILE_DROP");
                String company = dataMap.getString("company");
                Log.d("WearConnection", company);
                DBUtil.removeSelfFromQueue(company, android_id, null);
            }
        }
    }

    public static void startCompanyWearable(Context context) {
        init(context);
        sendNotificationPath(WEARABLE_COMPANY_PATH);
    }

    public static void startJSWearable(Context context, String id) {
        init(context);
        android_id = id;
        sendNotificationPath(WEARABLE_JS_PATH);
    }

    private static void sendNotificationPath(String path) {
        DataMap notifyWearable = new DataMap();
        notifyWearable.putLong("time", System.currentTimeMillis());
        new SendToDataLayerThread(path, notifyWearable).start();
    }


    public static void updateQueue(Context context, String android_id) {
        init(context);
        android_id = android_id;
        Log.d("WearCommunicationBridge", "Updating Queue");
        DBUtil.getQueuesUserIsPartOf(android_id, new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                ArrayList<String> list = new ArrayList<String>();
                for (ParseObject object : objects) {
                    Log.d("WearCommunicationBridge", object.getString("company"));
                    Log.d("WearCommunicationBridge", Integer.toString(object.getInt("place")));
                    String company = object.getString("company").replace(' ', '_');
                    Log.d("WearCommunicationBridge", company);
                    list.add(company + " " + object.getInt("place"));
                }
                DataMap map = new DataMap();
                map.putStringArrayList(DATA_PLACE_ARRAY, list);
                map.putLong("timer", System.currentTimeMillis());
                new SendToDataLayerThread(WEARABLE_QUEUED_PATH, map).start();
            }
        });
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
