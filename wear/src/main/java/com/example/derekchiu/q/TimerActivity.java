package com.example.derekchiu.q;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by tomo on 11/30/15.
 */
public class TimerActivity extends Activity {
    ImageButton view;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(getMainView());
    }

    private ImageButton getMainView() {
        view = new ImageButton(this);
        view.setImageResource(R.drawable.timer);
        view.setBackgroundColor(Color.WHITE);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent queue = new Intent(context, QueueActivity.class);
                startActivity(queue);
            }
        });
        return view;
    }
}
