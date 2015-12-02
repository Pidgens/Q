package com.example.derekchiu.q;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by derekchiu on 12/1/15.
 */
public class JSDescription extends Activity {

    TextView welcome_msg;
    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.manage_page);
    }
}
