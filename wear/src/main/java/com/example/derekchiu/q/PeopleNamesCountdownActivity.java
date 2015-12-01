package com.example.derekchiu.q;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by kroy1205 on 11/30/15.
 */
public class PeopleNamesCountdownActivity extends Activity {

    private ImageView mImageView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.people_names_countdown_activity);
        mImageView = (ImageView) findViewById(R.id.imageView2);
        mImageView.setImageResource(R.drawable.person_name_countdown);
        final Button button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent i = new Intent(PeopleNamesCountdownActivity.this, DismissCountdownActivity.class);
                startActivity(i);
            }
        });
    }
}
