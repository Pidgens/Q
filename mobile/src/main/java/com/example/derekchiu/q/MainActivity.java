package com.example.derekchiu.q;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    Button job_seeker;
    Button recruiter;
    TextView slogan;
    String username;
    EditText your_name;
    Boolean editted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        job_seeker = (Button) findViewById(R.id.js_button);
        recruiter = (Button) findViewById(R.id.rec_button);
        your_name = (EditText) findViewById(R.id.your_name);
        slogan = (TextView) findViewById(R.id.app_slogan);

        recruiter.setEnabled(false);
        job_seeker.setEnabled(false);
        recruiter.setBackgroundColor(getResources().getColor(R.color.grey));
        job_seeker.setBackgroundColor(getResources().getColor(R.color.grey));

        your_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                recruiter.setEnabled(true);
                job_seeker.setEnabled(true);
                recruiter.setBackgroundColor(getResources().getColor(R.color.dark_blue));
                job_seeker.setBackgroundColor(getResources().getColor(R.color.dark_blue));
                editted = true;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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