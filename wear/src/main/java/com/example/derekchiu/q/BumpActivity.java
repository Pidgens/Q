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
public class BumpActivity extends Activity {

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
        view.setImageResource(R.drawable.bump);
        view.setBackgroundColor(Color.parseColor("#80BEFF"));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent drop = new Intent(context, DropActivity.class);
                startActivity(drop);
            }
        });
        return view;
    }
}
