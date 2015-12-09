package com.example.derekchiu.q;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by kroy1205 on 11/30/15.
 */
public class FailedToArriveActivity extends Activity {
    private ImageView mImageView;
    private TextView mTextView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.failed_to_arrive_activity);
        mImageView = (ImageView) findViewById(R.id.imageView3);
        mImageView.setImageResource(R.drawable.black_watch_background);
        mTextView = (TextView) findViewById(R.id.textView4);
        mTextView.setText(R.string.noshow_message);
        mTextView.setTextColor(Color.WHITE);
        mImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent i = new Intent(FailedToArriveActivity.this, PeopleNamesActivity.class);
                startActivity(i);
            }
        });

    }
}
