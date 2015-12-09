package com.example.derekchiu.q;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.wearable.view.GridViewPager;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.Queue;

/**
 * Created by tomo on 11/30/15.
 */
public class QueueActivity extends Activity implements DataApi.DataListener,
        MessageApi.MessageListener {

    GridViewPager pager;
    TextView text;
    private static final String DATA_PLACE_ARRAY = "/place_update_array";
    public static final String WEARABLE_QUEUED_PATH = "/wearable_queued_path";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobileConnector.initClient(this);
        Wearable.DataApi.addListener(MobileConnector.getClient(this), this);
        MobileConnector.updateQueue();
        QueueGridPagerAdapter.getInstance(this, getFragmentManager());
        setContentView(R.layout.queue_layout);
        pager = (GridViewPager) findViewById(R.id.pager);
        createTextView();
    }

    private void createTextView() {
        text = (TextView) findViewById(R.id.notfound);
        text.setBackgroundColor(Color.WHITE);
        text.setText("You have not queued for any companies");
        text.setGravity(Gravity.CENTER);
        text.setTextColor(Color.BLACK);
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        Log.d("QueueActivity", "This event is definitely changed");
        for (DataEvent dataEvent : dataEventBuffer) {
            if (dataEvent.getType() == DataEvent.TYPE_CHANGED
                    || dataEvent.getType() == DataEvent.TYPE_DELETED) {
                String path = dataEvent.getDataItem().getUri().getPath();
                DataMap dataMap = DataMapItem.fromDataItem(dataEvent.getDataItem()).getDataMap();
                if (path.equals(WEARABLE_QUEUED_PATH)){
                    Log.d("MobileConnector", "WEARABLE_QUEUED_PATH");
                    Log.d("Hello world", "Why isn't it calling the information?");
                    updateQueue(dataMap);
                }

            }
        }
    }

    private void updateQueue(DataMap dataMap) {
        Log.d("Party", "In the USA");
        ArrayList<String> currentList = dataMap.getStringArrayList(DATA_PLACE_ARRAY);
        if (currentList != null) {
            Log.d("MobileConnector", currentList.toString());
            CompanyPlaceList.updateList(currentList);
            Log.v("path", "queue_updated");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("QueueActivity", "This is running");
                    if (QueueGridPagerAdapter.getInstance() != null) {
                        Log.d("QueueActivity", "QueueGridPagerAdapter updated");
                        CompanyPlaceList.forceChange();
                        QueueGridPagerAdapter.getInstance().notifyDataSetChanged();
                        setUI();
                    }
                }
            });
        } else {
            Log.d("Why is", "there nothing");
        }
    }

    private void setUI() {
        if (CompanyPlaceList.getList().size() == 0) {
            text.setVisibility(View.VISIBLE);
            pager.setVisibility(View.GONE);
        } else {
            text.setVisibility(View.GONE);
            pager.setVisibility(View.VISIBLE);
            pager = (GridViewPager) findViewById(R.id.pager);
            pager.setAdapter(QueueGridPagerAdapter.getInstance());
        }
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

    }
}
