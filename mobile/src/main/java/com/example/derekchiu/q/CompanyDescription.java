package com.example.derekchiu.q;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

/**
 * Created by derekchiu on 11/30/15.
 */
public class CompanyDescription extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.js_description);

        Bundle extras = getIntent().getExtras();

        DBUtil.getInfoAboutMe(extras.getString("UserID"), new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    Log.d("getUser", "Doesn't exist");
                } else {

                    TextView nameTextView = (TextView)findViewById(R.id.nameLabel);
                    TextView majorTextView = (TextView)findViewById(R.id.majorLabel);
                    TextView positionTextView = (TextView)findViewById(R.id.positionLabel);
                    TextView schoolTextView = (TextView)findViewById(R.id.schoolLabel);

                    nameTextView.setText(object.getString("name"));
                    majorTextView.setText(object.getString("major"));
                    positionTextView.setText(object.getString("position"));
                    schoolTextView.setText(object.getString("school"));
                }
            }
        });
    }
}
