package com.example.derekchiu.q;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by derekchiu on 11/30/15.
 */
public class JSPersonalize extends Activity {

    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.js_personalize);

        nextButton = (Button) findViewById(R.id.jsNext);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(JSPersonalize.this, Manage.class);
                startActivity(i);
            }
        });
    }
}
