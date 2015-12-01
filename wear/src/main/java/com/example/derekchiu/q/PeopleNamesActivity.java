package com.example.derekchiu.q;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by kroy1205 on 11/30/15.
 */
public class PeopleNamesActivity extends Activity {
    private ImageView mImageView;
    private GestureDetector mGestureDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.people_names_activity);
        mImageView = (ImageView) findViewById(R.id.imageView);
        mImageView.setImageResource(R.drawable.person_name);
        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent i = new Intent(PeopleNamesActivity.this, PeopleNamesCountdownActivity.class);
                startActivity(i);
            }
        });
        /**CustomGestureDetector customGestureDetector = new CustomGestureDetector();
        // Create a GestureDetector
        mGestureDetector = new GestureDetector(this, customGestureDetector);
        // Attach listeners that'll be called for double-tap and related gestures
        mGestureDetector.setOnDoubleTapListener(customGestureDetector);
         */
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){


        mGestureDetector.onTouchEvent(event);

        return super.onTouchEvent(event);
        /**
        int action = MotionEventCompat.getActionMasked(event);

        switch(action) {
            case (MotionEvent.ACTION_DOWN) :
                Log.d(getString(R.string.debug_tag), "Action was DOWN");
                x1 = event.getX();
                return true;
            case (MotionEvent.ACTION_MOVE) :
                Log.d(getString(R.string.debug_tag),"Action was MOVE");
                return true;
            case (MotionEvent.ACTION_UP) :
                Log.d(getString(R.string.debug_tag),"Action was UP");
                x2 = event.getX();
                float deltaX = x2 - x1;
                if (Math.abs(deltaX) > MIN_DISTANCE)
                {
                    Toast.makeText(this, "left2right swipe", Toast.LENGTH_SHORT).show ();
                }
                else
                {
                    // consider as something else - a screen tap for example
                    Toast.makeText(this, "not left to right", Toast.LENGTH_SHORT).show ();
                }
                return true;
            case (MotionEvent.ACTION_CANCEL) :
                Log.d(getString(R.string.debug_tag),"Action was CANCEL");
                return true;
            case (MotionEvent.ACTION_OUTSIDE) :
                Log.d(getString(R.string.debug_tag),"Movement occurred outside bounds " +
                        "of current screen element");
                return true;
            default :
                return super.onTouchEvent(event);
         }
    }
         */
    }
}
