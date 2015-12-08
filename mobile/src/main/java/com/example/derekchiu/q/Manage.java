package com.example.derekchiu.q;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by derekchiu on 11/30/15.
 */
public class Manage extends Activity {

    ListView companiesListView;
    ArrayList<DataItem> companiesList;
    ArrayList<ParseObject> pfobjectsList;
    ArrayList<String> jobseekersList;
    CompanyQueueAdapter listAdapter;
    Bundle extras;
    final Handler handler = new Handler();
    int size;

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.js_list);


        extras = getIntent().getExtras();

        companiesListView = (ListView) findViewById(R.id.companiesListView);

        companiesList = new ArrayList<DataItem>();
        jobseekersList = new ArrayList<String>();
        pfobjectsList = new ArrayList<ParseObject>();

        listAdapter = new CompanyQueueAdapter(this, android.R.layout.simple_list_item_1, companiesList);
        companiesListView.setAdapter(listAdapter);

        DBUtil.getCompanies(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> queueList, ParseException e) {
                if (e == null) {
                    companiesList.clear();
                    pfobjectsList.clear();
                    for (ParseObject object : queueList) {
                        DBUtil.getQueue(object.getString("name"), new FindCallback<ParseObject>() {
                            public void done(List<ParseObject> queueList, ParseException e) {
                                if (e == null) {
                                    jobseekersList.clear();
                                    for (ParseObject object : queueList) {
                                        jobseekersList.add(object.getString("name"));
                                    }
                                    size = jobseekersList.size();
                                    Log.d("okay", "Got " + queueList.size());
                                } else {
                                    Log.d("pull queue", "Error: " + e.getMessage());
                                    size=0;
                                }
                            }
                        });
                        companiesList.add(new DataItem(object.getString("name"), size));
                        pfobjectsList.add(object);
                    }
                    Log.d("okay", "Got " + queueList.size());

                    listAdapter.notifyDataSetChanged();
                } else {
                    Log.d("pull queue", "Error: " + e.getMessage());
                }
            }
        });

        //Timed refresh
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask(){
            @Override
            public void run(){
                handler.post(new Runnable(){
                    public void run(){
                        try{
                            DBUtil.getCompanies(new FindCallback<ParseObject>() {
                                public void done(List<ParseObject> queueList, ParseException e) {
                                    if (e == null) {
                                        companiesList.clear();
                                        pfobjectsList.clear();
                                        for (ParseObject object : queueList) {
                                            DBUtil.getQueue(extras.getString("company"), new FindCallback<ParseObject>() {
                                                public void done(List<ParseObject> queueList, ParseException e) {
                                                    if (e == null) {
                                                        jobseekersList.clear();
                                                        for (ParseObject object : queueList) {
                                                            jobseekersList.add(object.getString("name"));
                                                        }
                                                        Log.d("okay", "Got " + queueList.size());
                                                    } else {
                                                        Log.d("pull queue", "Error: " + e.getMessage());
                                                    }
                                                }
                                            });
                                            companiesList.add(new DataItem(object.getString("name"), jobseekersList.size()));
                                            pfobjectsList.add(object);
                                        }
                                        Log.d("okay", "Got " + queueList.size());

                                        listAdapter.notifyDataSetChanged();
                                    } else {
                                        Log.d("pull queue", "Error: " + e.getMessage());
                                    }
                                }
                            });

                        } catch (Exception e){

                        }
                    }
                });

            }
        };
        timer.schedule(doAsynchronousTask, 600000000); //should do asynchronous task every minute


        companiesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(Manage.this, CompanyDescription.class);
                ParseObject companyObj = pfobjectsList.get(position);
                i.putExtra("Description", companyObj.getString("description"));
                i.putExtra("LogoURL", companyObj.getString("logoURL"));
                i.putExtra("Company", companyObj.getString("name"));

                String positionsString = "";
                List positions = companyObj.getList("positionsAvailable");
                for (Object s : positions) {
                    positionsString += s.toString() + ",";
                }

                i.putExtra("PositionsAvailable", positionsString);

                startActivity(i);
            }
        });

    }
}
