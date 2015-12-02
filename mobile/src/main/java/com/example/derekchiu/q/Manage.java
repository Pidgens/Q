package com.example.derekchiu.q;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Created by derekchiu on 11/30/15.
 */
public class Manage extends Activity {

    ImageView manageView;
    ListView peopleinQueue;

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.js_list);

        manageView = (ImageView) findViewById(R.id.cp_list);
        manageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = MotionEventCompat.getActionMasked(event);
                if (action == MotionEvent.ACTION_SCROLL) {
                    // do something
                }
                return false;
            }
        });

        manageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // IF WE DO RESUME -- GET REQUEST ON RESUME
                Intent i = new Intent(Manage.this, EnterQueue.class);
                startActivity(i);
            }
        });

    }
}
