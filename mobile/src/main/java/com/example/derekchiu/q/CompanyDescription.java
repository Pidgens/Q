package com.example.derekchiu.q;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

/**
 * Created by derekchiu on 12/1/15.
 */
public class CompanyDescription extends Activity {

    TextView welcome_msg;
    Button joinQueue;

    Bundle extras = getIntent().getExtras();

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.manage_page);
        //getcompanydescription
        //getcompanyimage

        joinQueue = (Button) findViewById(R.id.joinButton);
        joinQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBUtil.addSelfToQueue(extras.getString("companyName"), extras.getString("userId"), new SaveCallback() {
                    @Override
                    public void done(ParseException e) {

                    }
                });
            }
        });
    }
}
