package com.example.derekchiu.q;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by derekchiu on 12/1/15.
 */
public class EnterQueue extends Activity {

    ListView lv;
    Button joinButton;
    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.cp_description);

        lv = (ListView) findViewById(R.id.positionsAvailableListView);
        joinButton = (Button) findViewById(R.id.joinButton);

        ArrayList<String> companies =  new ArrayList<String>();
        companies.add("Business Analyst");
        ListAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, companies);
        lv.setAdapter(listAdapter);

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinButton.setText("Entered Queue!");
            }
        });
    }
}
