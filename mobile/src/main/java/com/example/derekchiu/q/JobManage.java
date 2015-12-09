package com.example.derekchiu.q;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
public class JobManage extends Activity {

    ListView lv2;
    TextView queueLength;
    TextView title;
    Bundle extras;
    ArrayAdapter listAdapter;
    ArrayList<String> jobseekersList;
    ArrayList<ParseObject> pfobjectsList;
    final Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.manage_page);

        extras = getIntent().getExtras();

        jobseekersList = new ArrayList<String>();
        pfobjectsList = new ArrayList<ParseObject>();



        DBUtil.getQueue(extras.getString("company"), new FindCallback<ParseObject>() {
            public void done(List<ParseObject> queueList, ParseException e) {
                if (e == null) {
                    jobseekersList.clear();
                    pfobjectsList.clear();
                    int i = 0;
                    for (ParseObject object : queueList) {
                        i++;
                        jobseekersList.add("(" + String.valueOf(i) + ") " + object.getString("name"));
                        pfobjectsList.add(object);
                    }
                    Log.d("okay", "Got " + queueList.size());

                    listAdapter.notifyDataSetChanged();
                } else {
                    Log.d("pull queue", "Error: " + e.getMessage());
                }
                title = (TextView) findViewById(R.id.textView18);
                title.setText(extras.getString("company") + "(" + String.valueOf(queueList.size()) + ")");
                title.setTextColor(getResources().getColor(R.color.black));

            }
        });





        TextView person = (TextView) findViewById(R.id.person);
        listAdapter = new ArrayAdapter<String>(this, R.layout.people, jobseekersList);
        LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.people, null);


        lv2 = (ListView) findViewById(R.id.lvJSList);
        lv2.setAdapter(listAdapter);
        lv2.setDividerHeight(10);

        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask(){
            @Override
            public void run(){
                handler.post(new Runnable(){
                    public void run(){
                        try{
                            DBUtil.getQueue(extras.getString("company"), new FindCallback<ParseObject>() {
                                public void done(List<ParseObject> queueList, ParseException e) {
                                    if (e == null) {
                                        jobseekersList.clear();
                                        pfobjectsList.clear();
                                        for (ParseObject object : queueList) {
                                            jobseekersList.add(object.getString("name"));
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
        timer.schedule(doAsynchronousTask, 30000, 30000); //should do asynchronous task every minute

        lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(JobManage.this, JSDescription.class);
                ParseObject userObj = pfobjectsList.get(position);
                i.putExtra("UserID", userObj.getString("userID"));

                startActivity(i);
            }
        });

    }

}

