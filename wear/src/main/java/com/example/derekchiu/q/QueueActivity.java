package com.example.derekchiu.q;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.util.Queue;
import java.util.Timer;

/**
 * Created by tomo on 11/30/15.
 */
public class QueueActivity extends Activity {

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
        view.setImageResource(R.drawable.queue);
        view.setBackgroundColor(Color.WHITE);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent drop = new Intent(context, DropActivity.class);
                startActivity(drop);
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent timer = new Intent(context, TimerActivity.class);
                startActivity(timer);
                return true;
            }
        });
        return view;
    }

}
