package com.example.derekchiu.q;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by kroy1205 on 11/30/15.
 */
public class PeopleNamesCountdownActivity extends Activity {

    private ImageView mImageView;
    private TextView mTextView;
    public int seconds = 60;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        String name = i.getStringExtra("next_name");
        setContentView(R.layout.people_names_countdown_activity);
        mImageView = (ImageView) findViewById(R.id.imageView2);
        mImageView.setImageResource(R.drawable.count_down_watch_background);
        mTextView = (TextView) findViewById(R.id.textView2);
        mTextView.setText(name);
        mTextView.setTextColor(Color.BLACK);
        mTextView.setTypeface(null, Typeface.BOLD);
        final Timer t = new Timer();
        //Set the schedule function and rate
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (seconds >= 0) {
                            TextView tv = (TextView) findViewById(R.id.textView3);
                            tv.setText(String.valueOf(seconds) + "s");
                            tv.setTextColor(Color.RED);
                            seconds -= 1;
                        } else {
                            Intent in = new Intent(PeopleNamesCountdownActivity.this, FailedToArriveActivity.class);
                            in.putExtra("next_name", "Roy Kim");
                            t.cancel();
                            startActivity(in);
                        }


                    }

                });
            }

        }, 0, 1000);
        mImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent in2 = new Intent(PeopleNamesCountdownActivity.this, PeopleNamesActivity.class);
                in2.putExtra("next_name", "Roy Kim");
                t.cancel();
                startActivity(in2);
            }
        });
    }
}
