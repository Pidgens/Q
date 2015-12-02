package com.example.derekchiu.q;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by tomo on 12/1/15.
 */
public class ChooseFlowActivity extends Activity {
    TextView view;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(getMainView());
    }

    private TextView getMainView() {
        view = new TextView(this);
        view.setLayoutParams(
                new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.MATCH_PARENT));
        view.setText("Choose a start.\n For Recruiters Long Click." +
                "\n For Students Click.");
        view.setGravity(Gravity.CENTER);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent queue = new Intent(context, QueueActivity.class);
                startActivity(queue);
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent names = new Intent(context, PeopleNamesActivity.class);
                startActivity(names);
                return true;
            }
        });
        return view;
    }
}
