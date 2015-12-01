package com.example.derekchiu.q;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * Created by kroy1205 on 11/30/15.
 */
public class DismissCountdownActivity extends Activity {
    private ImageView mImageView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dismiss_countdown_activity);
        mImageView = (ImageView) findViewById(R.id.imageView3);
        mImageView.setImageResource(R.drawable.dismiss_countdown);
    }
}
