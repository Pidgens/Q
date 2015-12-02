package com.example.derekchiu.q;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by derekchiu on 11/30/15.
 */
public class JobManage extends Activity {

    ImageView company_descrip;
    ListView lv2;

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.manage_page);

        lv2 = (ListView) findViewById(R.id.lvJSList);
        ArrayList<String> jobseekersList = new ArrayList<String>();
        jobseekersList.add("John Doe");
        jobseekersList.add("Tomo Ueda");
        jobseekersList.add("Patrick Mc Gartoll");
        jobseekersList.add("Derek Chiu");
        jobseekersList.add("Roy Kim");
        jobseekersList.add("Daniel He");
        ListAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, jobseekersList);
        lv2.setAdapter(listAdapter);

        lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent i = new Intent(JobManage.this, CompanyDescription.class);
                    startActivity(i);
                }
            }
        });

//        company_descrip = (ImageView) findViewById(R.id.manageImage);
//        company_descrip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent descrip_page = new Intent(JobManage.this, CompanyDescription.class);
//                startActivity(descrip_page);
//            }
//        });
    }

}

