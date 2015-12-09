package com.example.derekchiu.q;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.View;
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
        mImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent i = new Intent(PeopleNamesActivity.this, PeopleNamesCountdownActivity.class);
                startActivity(i);
            }
        });
        mImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent queue = new Intent(PeopleNamesActivity.this,
                        ChooseFlowActivity.class);
                startActivity(queue);
                return true;
            }
        });
    }
}
