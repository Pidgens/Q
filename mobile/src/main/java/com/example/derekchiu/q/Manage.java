package com.example.derekchiu.q;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
    CompanyQueueAdapter listAdapter;
    Bundle extras;
    final Handler handler = new Handler();
    int size;

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.js_list);


        extras = getIntent().getExtras();
        final String name = extras.getString("Name");

        companiesListView = (ListView) findViewById(R.id.companiesListView);

        companiesList = new ArrayList<DataItem>();
        pfobjectsList = new ArrayList<ParseObject>();

        listAdapter = new CompanyQueueAdapter(this, android.R.layout.simple_list_item_1, companiesList);
        companiesListView.setAdapter(listAdapter);
        companiesListView.setDividerHeight(10);

        DBUtil.getCompanies(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> queueList, ParseException e) {
                if (e == null) {
                    companiesList.clear();
                    pfobjectsList.clear();
                    for (final ParseObject object : queueList) {
                        final String temp = object.getString("name");
                        DBUtil.getQueue(object.getString("name"), new FindCallback<ParseObject>() {
                            public void done(List<ParseObject> queueList, ParseException e) {
                                if (e == null) {

                                    companiesList.add(new DataItem(object.getString("name"), queueList.size()));
                                    pfobjectsList.add(object);
                                    listAdapter.notifyDataSetChanged();
                                    Log.d("okay", "Got " + queueList.size());
                                } else {
                                    Log.d("pull queue", "Error: " + e.getMessage());
                                    size = 0;
                                }
//                                companiesList.add(new DataItem(temp, size));
//                                listAdapter.notifyDataSetChanged();
                                Log.d("found list item", "Got " + temp + " and " + size);
                            }
                        });
//                        companiesList.add(new DataItem(object.getString("name"), object.getInt("queueSize")));
//                        pfobjectsList.add(object);
//                        listAdapter.notifyDataSetChanged();

                    }
                    Log.d("okay", "Got " + queueList.size());

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
                            Log.d("Run", "running");
                            for (final DataItem object : companiesList) {
                                final String temp = object.getCompany();
                                DBUtil.getQueue(temp, new FindCallback<ParseObject>() {
                                    public void done(List<ParseObject> queueList, ParseException e) {
                                        if (e == null) {
                                            object.setQueue(queueList.size());
                                            listAdapter.notifyDataSetChanged();
                                            size = queueList.size();
                                            Log.d("okay", "Got " + queueList.size());
                                        } else {
                                            Log.d("pull queue", "Error: " + e.getMessage());
                                            size = 0;
                                        }
                                    }
                                });

                            }
                        } catch (Exception ex) {

                        }
                    }
                });

            }
        };
        timer.schedule(doAsynchronousTask, 2000, 2000);


        companiesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(Manage.this, CompanyDescription.class);
                ParseObject companyObj = pfobjectsList.get(position);
                i.putExtra("Description", companyObj.getString("description"));
                i.putExtra("LogoURL", companyObj.getString("logoURL"));
                i.putExtra("Company", companyObj.getString("name"));
                i.putExtra("Name", name);

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

    /*public void onResume() {
        super.onResume();
        System.out.println("On Resume");
        companiesList = new ArrayList<DataItem>();
        jobseekersList = new ArrayList<String>();
        pfobjectsList = new ArrayList<ParseObject>();
        companiesList = new ArrayList<DataItem>();
        jobseekersList = new ArrayList<String>();
        pfobjectsList = new ArrayList<ParseObject>();
        DBUtil.getCompanies(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> queueList, ParseException e) {
                if (e == null) {
                    companiesList.clear();
                    pfobjectsList.clear();
                    for (ParseObject object : queueList) {
                        final String temp = object.getString("name");
                        DBUtil.getQueue(object.getString("name"), new FindCallback<ParseObject>() {
                            public void done(List<ParseObject> queueList, ParseException e) {
                                if (e == null) {
                                    jobseekersList.clear();
                                    for (ParseObject object : queueList) {
                                        jobseekersList.add(object.getString("name"));
                                    }
                                    size = queueList.size();
                                    Log.d("okay", "Got " + queueList.size());
                                } else {
                                    Log.d("pull queue", "Error: " + e.getMessage());
                                    size = 0;
                                }
                                companiesList.add(new DataItem(temp, size));
                                Log.d("found list item", "Got " + temp + " and " + size);
                            }
                        });

                        pfobjectsList.add(object);
                    }
                    Log.d("okay", "Got " + queueList.size());

                    listAdapter.notifyDataSetChanged();
                } else {
                    Log.d("pull queue", "Error: " + e.getMessage());
                }
            }
        });

        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            System.out.println("Timing working");
                            companiesList = new ArrayList<DataItem>();
                            jobseekersList = new ArrayList<String>();
                            pfobjectsList = new ArrayList<ParseObject>();
                            DBUtil.getCompanies(new FindCallback<ParseObject>() {
                                public void done(List<ParseObject> queueList, ParseException e) {
                                    if (e == null) {
                                        companiesList.clear();
                                        pfobjectsList.clear();
                                        for (ParseObject object : queueList) {
                                            final String temp = object.getString("name");
                                            DBUtil.getQueue(object.getString("name"), new FindCallback<ParseObject>() {
                                                public void done(List<ParseObject> queueList, ParseException e) {
                                                    if (e == null) {
                                                        jobseekersList.clear();
                                                        for (ParseObject object : queueList) {
                                                            jobseekersList.add(object.getString("name"));
                                                        }
                                                        size = queueList.size();
                                                        Log.d("okay", "Got " + queueList.size());
                                                    } else {
                                                        Log.d("pull queue", "Error: " + e.getMessage());
                                                        size = 0;
                                                    }
                                                    companiesList.add(new DataItem(temp, size));
                                                    Log.d("found list item", "Got " + temp + " and " + size);
                                                }
                                            });

                                            pfobjectsList.add(object);
                                        }
                                        Log.d("okay", "Got " + queueList.size());

                                        listAdapter.notifyDataSetChanged();
                                    } else {
                                        Log.d("pull queue", "Error: " + e.getMessage());
                                    }
                                }
                            });

                        } catch (Exception e) {

                        }
                    }
                });

            }
        };
        timer.schedule(doAsynchronousTask, 30000);
    }*/
}
