package com.example.derekchiu.q;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by tomo on 11/30/15.
 */
public class DropActivity extends Activity {

    ImageButton view;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, QueueActivity.class);
        context = this;
        setContentView(getMainView());
    }

    private ImageButton getMainView() {
        view = new ImageButton(this);
        view.setImageResource(R.drawable.drop);
        view.setBackgroundColor(Color.parseColor("#80BEFF"));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent drop = new Intent(context, BumpActivity.class);
                startActivity(drop);
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent queue = new Intent(context, QueueActivity.class);
                startActivity(queue);
                return true;
            }
        });
        return view;
    }
}
