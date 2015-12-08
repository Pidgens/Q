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

import com.parse.ParseException;
import com.parse.SaveCallback;

import java.net.URL;

/**
 * Created by derekchiu on 12/1/15.
 */
public class CompanyDescription extends Activity {

    TextView welcome_msg;
    Button joinQueue;

    Bundle extras;
    Button nextButton;
    String android_id;

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
        addToQueue.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //if(DBUtil.getNumOfQueues() < 5)
                DBUtil.addSelfToQueue(extras.getString("Company"), android_id,  new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Toast.makeText(getApplicationContext(), "Added to queue",
                                Toast.LENGTH_SHORT).show();
                        addToQueue.setText("Entered Queue");
                    }
                });
            }
        });

        Log.d("posi", extras.getString("PositionsAvailable"));
        String[] positionsAvailable = extras.getString("PositionsAvailable").split(",");
        ArrayAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, positionsAvailable);
        ListView positionsListView = (ListView) findViewById(R.id.positionsAvailableListView);
        positionsListView.setAdapter(listAdapter);
    }
}
