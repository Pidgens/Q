package com.example.derekchiu.q;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by derekchiu on 11/30/15.
 */
public class JobManage extends Activity {

    ImageView company_descrip;

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.js_list);

        company_descrip = (ImageView) findViewById(R.id.cp_list);
        company_descrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent descrip_page = new Intent(JobManage.this, CompanyDescription.class);
                startActivity(descrip_page);
            }
        });
    }

}

