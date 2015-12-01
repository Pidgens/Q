package com.example.derekchiu.q;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    ImageButton login;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = (ImageButton) findViewById(R.id.login);
        final Intent manageView = new Intent(this, Manage.class);
        // Store username.
        manageView.putExtra("username", username);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(manageView);
            }
        });
    }


}
