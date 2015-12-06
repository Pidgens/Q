package com.example.derekchiu.q;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by derekchiu on 11/30/15.
 */
public class JobManage extends Activity {

    ImageView company_descrip;
    ListView lv2;
    Bundle extras;
    ArrayAdapter listAdapter;
    ArrayList<String> jobseekersList;


    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.manage_page);

        extras = getIntent().getExtras();

        Log.d("company", "THIS:" + extras.getString("company"));

        DBUtil.getQueue(extras.getString("company"), new FindCallback<ParseObject>() {
            public void done(List<ParseObject> queueList, ParseException e) {
                if (e == null) {
                    jobseekersList.clear();
                    for (ParseObject object : queueList) {
                        jobseekersList.add(object.getString("name"));
                    }
                    Log.d("okay", "Got " + queueList.size());

                    listAdapter.notifyDataSetChanged();
                } else {
                    Log.d("pull queue", "Error: " + e.getMessage());
                }
            }
        });

        jobseekersList = new ArrayList<String>();
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, jobseekersList);
        lv2 = (ListView) findViewById(R.id.lvJSList);
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

