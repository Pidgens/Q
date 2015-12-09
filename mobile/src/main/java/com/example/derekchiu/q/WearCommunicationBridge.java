package com.example.derekchiu.q;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

/**
 * Created by tomo on 12/8/15.
 */
public class WearCommunicationBridge {

    private WearCommunicationBridge() {}

    private static GoogleApiClient googleClient;
    private static final String WEARABLE_COMPANY_PATH = "/wearable_company";

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
