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
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.js_list);

        extras = getIntent().getExtras();

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
                ParseObject companyObj = pfobjectsList.get(position);
                i.putExtra("Description", companyObj.getString("description"));
                i.putExtra("LogoURL", companyObj.getString("logoURL"));
                i.putExtra("Company", companyObj.getString("name"));

                startActivity(i);
            }
        });

    }
}
