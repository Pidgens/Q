package com.example.derekchiu.q;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by derekchiu on 11/30/15.
 */
public class Manage extends Activity {

    ListView companiesListView;
    ArrayList<String> companiesList;
    ArrayList<ParseObject> pfobjectsList;
    ArrayAdapter listAdapter;
    Bundle extras = getIntent().getExtras();

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.js_list);

        companiesListView = (ListView) findViewById(R.id.companiesListView);

        companiesList = new ArrayList<String>();
        pfobjectsList = new ArrayList<ParseObject>();

        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, companiesList);
        companiesListView.setAdapter(listAdapter);

        DBUtil.getCompanies(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> queueList, ParseException e) {
                if (e == null) {
                    companiesList.clear();
                    pfobjectsList.clear();
                    for (ParseObject object : queueList) {
                        companiesList.add(object.getString("name"));
                        pfobjectsList.add(object);
                    }
                    Log.d("okay", "Got " + queueList.size());

                    listAdapter.notifyDataSetChanged();
                } else {
                    Log.d("pull queue", "Error: " + e.getMessage());
                }
            }
        });

        companiesListView.setAdapter(listAdapter);

        companiesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(Manage.this, CompanyDescription.class);
                ParseObject userObj = pfobjectsList.get(position);
                i.putExtra("companyName", userObj.getString("name"));
                i.putExtra("userId", extras.getString("userID"));

                startActivity(i);
            }
        });

//        manageView = (ImageView) findViewById(R.id.cp_list);
//        manageView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                int action = MotionEventCompat.getActionMasked(event);
//                if (action == MotionEvent.ACTION_SCROLL) {
//                    // do something
//                }
//                return false;
//            }
//        });
//
//        manageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // IF WE DO RESUME -- GET REQUEST ON RESUME
//                Intent i = new Intent(Manage.this, EnterQueue.class);
//                startActivity(i);
//            }
//        });

    }
}
