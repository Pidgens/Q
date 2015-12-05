package com.example.derekchiu.q;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    Button job_seeker;
    Button recruiter;
    TextView slogan;
    String username;
    EditText your_name;

    // For PART 3 ONLY.
    ImageView login_imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);

        job_seeker = (Button) findViewById(R.id.js_button);
        recruiter = (Button) findViewById(R.id.rec_button);
        your_name = (EditText) findViewById(R.id.your_name);
        slogan = (TextView) findViewById(R.id.app_slogan);
        // Store username.
        recruiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent manageView = new Intent(MainActivity.this, CompanyPersonalize.class);
                username = your_name.getText().toString();
                manageView.putExtra("user", username.toString());
                startActivity(manageView);
            }
        });
        job_seeker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent job_manager = new Intent(MainActivity.this, JSPersonalize.class);
                username = your_name.getText().toString();
                job_manager.putExtra("user", username.toString());
                startActivity(job_manager);
            }
        });
    }
}