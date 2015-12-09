package com.example.derekchiu.q;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomo on 12/8/15.
 */
public class WearCommunicationBridge {

    private WearCommunicationBridge() {}

    private static GoogleApiClient googleClient;
    private static final String WEARABLE_COMPANY_PATH = "/wearable_company";
    private static final String WEARABLE_QUEUED_PATH = "/wearable_queued_path";
    private static final String DATA_PLACE_ARRAY = "/place_update_array";

    private static void init(Context context) {
        if (googleClient == null) {

            googleClient = new GoogleApiClient.Builder(context)
                    .addApi(Wearable.API)
                    .build();
            googleClient.connect();
        }
    }

    public static void startWearable(Context context) {
        init(context);
        DataMap notifyWearable = new DataMap();
        notifyWearable.putLong("time", System.currentTimeMillis());
        new SendToDataLayerThread(WEARABLE_COMPANY_PATH, notifyWearable).start();
    }

    public static void updateQueue(Context context, String android_id) {
        init(context);
        DBUtil.getQueuesUserIsPartOf(android_id, new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                ArrayList<String> list = new ArrayList<String>();
                for (ParseObject object: objects) {
                    list.add(object.getString("company") + object.getInt("place"));
                }
                DataMap map = new DataMap();
                map.putStringArray(DATA_PLACE_ARRAY, (String[]) list.toArray());
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
