package com.example.derekchiu.q;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by tomo on 12/1/15.
 */
public class ChooseFlowActivity extends Activity {
    TextView view;
    Context context;

    String WEARABLE_COMPANY_PATH = "/wearable_company";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(getMainView());
    }

    private TextView getMainView() {
        view = new TextView(this);
        view.setLayoutParams(
                new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.MATCH_PARENT));
        view.setText("Choose a start.\n For Recruiters Long Click." +
                "\n For Students Click.");
        view.setGravity(Gravity.CENTER);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent queue = new Intent(context, QueueActivity.class);
                startActivity(queue);
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent names = new Intent(context, PeopleNamesActivity.class);
                startActivity(names);
                return true;
            }
        });
        return view;
    }

    public class MobileConnector extends WearableListenerService {
        @Override
        public void onDataChanged(DataEventBuffer dataEvents) {
            DataMap dataMap;
            for (DataEvent event : dataEvents) {
                // Check the event type
                if (event.getType() == DataEvent.TYPE_CHANGED) {
                    // Check the data path
                    if (event.getDataItem().getUri().getPath().equals(WEARABLE_COMPANY_PATH)) {
                        // Create a local notification
                        dataMap = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
                        sendLocalNotification(dataMap);
                    }
                }
            }
        }
    }

    private void sendLocalNotification(DataMap dataMap) {
        int notificationId = 001;

        Intent startIntent = new Intent(this, PeopleNamesActivity.class)
                .setAction(Intent.ACTION_MAIN);
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
