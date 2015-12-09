package com.example.derekchiu.q;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kroy1205 on 11/30/15.
 */
public class PeopleNamesActivity extends Activity {
    private ImageView mImageView;
    private TextView mTextView;
    private ArrayList<String> mNames = new ArrayList<String>();
    private float downX;
    private float downY;
    private float upX;
    private float upY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.people_names_activity);
        mNames.add("Derek Chiu");
        mNames.add("Roy Kim");
        mImageView = (ImageView) findViewById(R.id.imageView);
        mImageView.setImageResource(R.drawable.watch_background);
        mImageView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {

                    downX = event.getX();
                    downY = event.getY();
                    //mSwipeDetected = Action.None;
                    return true; // allow other events like Click to be processed
                } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                    upX = event.getX();
                    upY = event.getY();

                    float deltaX = downX - upX;

                    // horizontal swipe detection
                    if (Math.abs(deltaX) > 150) {
                        // left or right
                        if (deltaX > 0) {
                            Log.i("My Tag", "Swipe Right to Left");
                            Intent i = new Intent(PeopleNamesActivity.this, PeopleNamesCountdownActivity.class);
                            i.putExtra("next_name", mNames.get(1));
                            startActivity(i);
                            // mSwipeDetected = Action.RL;
                            return false;
                        }
                    }
                }
                return true;
            }
        });
        mTextView = (TextView) findViewById(R.id.textView);
        mTextView.setText(mNames.get(0));
        mTextView.setTypeface(null, Typeface.BOLD);
        mTextView.setTextColor(Color.BLACK);
        /**mImageView.setOnClickListener(new View.OnClickListener() {
         public void onClick(View v) {
         // Perform action on click
         Intent i = new Intent(PeopleNamesActivity.this, PeopleNamesCountdownActivity.class);
         startActivity(i);
         }
         });
         mImageView.setOnLongClickListener(new View.OnLongClickListener() {
        @Override public boolean onLongClick(View v) {
        Intent queue = new Intent(PeopleNamesActivity.this,
        ChooseFlowActivity.class);
        startActivity(queue);
        return true;
        }
        });*/

    }
}