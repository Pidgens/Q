package com.example.derekchiu.q;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.net.URL;
import java.util.List;

/**
 * Created by derekchiu on 12/1/15.
 */
public class CompanyDescription extends Activity {

    TextView welcome_msg;
    Button joinQueue;

    Bundle extras;
    String android_id;
    int queuesJoined;
    boolean inQueue;

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.cp_description);

        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        final Bundle extras = getIntent().getExtras();

        TextView descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
        descriptionTextView.setText(extras.getString("Description"));


        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Log.d("URL", extras.getString("LogoURL"));
                    URL newUrl = new URL(extras.getString("LogoURL"));
                    final Bitmap bm = BitmapFactory.decodeStream(newUrl.openConnection().getInputStream());

                    runOnUiThread(new Runnable() {
                        public void run() {
                            ImageView logoView = (ImageView) findViewById(R.id.logoView);
                            logoView.setImageBitmap(bm);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();


        final Button addToQueue = (Button) findViewById(R.id.joinButton);
        DBUtil.getPlaceInQueue(extras.getString("Company"), android_id, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object != null) {
                    addToQueue.setText("Already in Queue");
                    addToQueue.setEnabled(false);
                    addToQueue.setBackgroundColor(getResources().getColor(R.color.grey));
                } else {
                    DBUtil.getQueuesUserIsPartOf(android_id, new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> queueList, ParseException e) {
                            if (e == null) {
                                queuesJoined = queueList.size();
                                if (queuesJoined >= 5) {
                                    addToQueue.setText("Queue Cap Reached");
                                    addToQueue.setEnabled(false);
                                    addToQueue.setBackgroundColor(getResources().getColor(R.color.grey));
                                } else {

                                }addToQueue.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v) {

                                        if (queuesJoined < 5) {
                                            System.out.println("Added to queue");
                                            DBUtil.addSelfToQueue(extras.getString("Company"), extras.getString("Name"), android_id, new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    Toast.makeText(getApplicationContext(), "Added to queue",
                                                            Toast.LENGTH_SHORT).show();
                                                    addToQueue.setText("Entered Queue");
                                                    WearCommunicationBridge.updateQueue(CompanyDescription.this, android_id);
                                                }
                                            });
                                        }
                                    }
                                });
                            } else {
                                Log.d("Pull number of queues", "Error: " + e.getMessage());
                                addToQueue.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v) {

                                        if (queuesJoined < 5) {
                                            System.out.println("Added to queue");
                                            DBUtil.addSelfToQueue(extras.getString("Company"), extras.getString("Name"), android_id, new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    Toast.makeText(getApplicationContext(), "Added to queue",
                                                            Toast.LENGTH_SHORT).show();
                                                    addToQueue.setText("Entered Queue");
                                                    WearCommunicationBridge.updateQueue(CompanyDescription.this, android_id);
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }

                    });

                }
            }
        });


        Log.d("posi", extras.getString("PositionsAvailable"));
        String[] positionsAvailable = extras.getString("PositionsAvailable").split(",");
        ArrayAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, positionsAvailable);
        ListView positionsListView = (ListView) findViewById(R.id.positionsAvailableListView);
        positionsListView.setAdapter(listAdapter);

    }
}