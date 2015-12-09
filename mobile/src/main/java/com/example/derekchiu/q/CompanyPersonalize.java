package com.example.derekchiu.q;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by derekchiu on 11/30/15.
 */
public class CompanyPersonalize extends Activity {

    Button next;
    Bundle extras;
    TextView introText;
    DBUtil dbutil;
    Button add_text;
    Button add_text2;
    Button add_text3;
    String androidId;

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.cp_personalize);

        extras = getIntent().getExtras();

        androidId = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);


        final AutoCompleteTextView company = (AutoCompleteTextView) findViewById(R.id.company_name);
        final AutoCompleteTextView positions = (AutoCompleteTextView) findViewById(R.id.cp_seek);
        final AutoCompleteTextView cp_seek2 = (AutoCompleteTextView) findViewById(R.id.cp_seek2);
        final AutoCompleteTextView cp_seek3 = (AutoCompleteTextView) findViewById(R.id.cp_seek3);

        introText = (TextView) findViewById(R.id.textView15);
        introText.setText("Hi " + extras.getString("user") + " let us know what candidates you are interested in.");

        try {
            DBUtil.getCompanies(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
//                    Log.i("WORKS", "YES");
                    ArrayList<String> arrayList = new ArrayList<String>();
                    ArrayList<String> posArrayList = new ArrayList<String>();
                    for (ParseObject comp : objects) {
                        arrayList.add(comp.getString("name"));
                    }
                    String[] company_list = arrayList.toArray(new String[arrayList.size()]);
                    ArrayAdapter<String> cpAdapter = new ArrayAdapter<String>(CompanyPersonalize.this, android.R.layout.simple_dropdown_item_1line, company_list);
                    company.setAdapter(cpAdapter);
                }
            });
        } catch (Exception e) {
//            Log.i("WORKS", "NO");
        }
        company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                company.showDropDown();
            }
        });

        company.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    try {
                        DBUtil.getPositionsAvailable(v.getText().toString(), new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject objects, ParseException e) {
                                ArrayList<String> posArrayList = new ArrayList<String>();
                                if (objects.getList("positionsAvailable") == null) {
                                    return;
                                } else {
                                    List positionList = objects.getList("positionsAvailable");
                                    for (Object strg : positionList) {
                                        posArrayList.add(strg.toString());
                                    }
                                    String[] posList = posArrayList.toArray(new String[posArrayList.size()]);
                                    ArrayAdapter<String> posAdapter = new ArrayAdapter<String>(CompanyPersonalize.this, android.R.layout.simple_dropdown_item_1line, posList);
                                    positions.setAdapter(posAdapter);
                                    cp_seek2.setAdapter(posAdapter);
                                    cp_seek3.setAdapter(posAdapter);
                                    return;
                                }
                            }
                        });
                    } catch (Exception e) {
                        Log.i("WORKS", "NOPE");
                    }
                    return true;

                }
                return false;
            }
        });

        positions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positions.showDropDown();
            }
        });


        cp_seek2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cp_seek2.showDropDown();
            }
        });

        cp_seek3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cp_seek3.showDropDown();
            }
        });

        add_text = (Button) findViewById(R.id.add_text);
        add_text2 = (Button) findViewById(R.id.add_text2);
        add_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cp_seek2.setVisibility(View.VISIBLE);
                add_text.setVisibility(View.INVISIBLE);
                add_text2.setVisibility(View.VISIBLE);
            }
        });
        add_text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cp_seek3.setVisibility(View.VISIBLE);
                add_text2.setVisibility(View.INVISIBLE);
            }
        });


        next = (Button) findViewById(R.id.nextButton);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int seconds = c.get(Calendar.SECOND);
                Intent i = new Intent(CompanyPersonalize.this, JobManage.class);
                i.putExtra("user", extras.getString("user"));

                String name = extras.getString("user");
                EditText company = (EditText) findViewById(R.id.company_name);
                EditText seeking = (EditText) findViewById(R.id.cp_seek);
                i.putExtra("company", company.getText().toString());
                dbutil.saveRecruiter(androidId, name.toString(), company.getText().toString(), seeking.getText().toString());
                WearCommunicationBridge.startCompanyWearable(CompanyPersonalize.this);
                startActivity(i);
            }
        });
    }
}
