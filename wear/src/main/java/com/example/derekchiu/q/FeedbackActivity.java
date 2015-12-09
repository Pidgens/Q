package com.example.derekchiu.q;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.wearable.view.BoxInsetLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by tomo on 12/7/15.
 */
public class FeedbackActivity extends Activity {

    public static final String FEEDBACK_TYPE = "type";
    public static final String COMPANY = "company";
    public static final String BUMP = "bump";
    public static final String DROP = "drop";
    public static final String ACCEPT  = "accept";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getMainView());
    }

    private BoxInsetLayout getMainView() {
        BoxInsetLayout layout = new BoxInsetLayout(this);
        layout.setBackgroundColor(Color.WHITE);
        layout.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));

        Intent intent = this.getIntent();
        String type = intent.getExtras().getString(FEEDBACK_TYPE);
        String company = intent.getExtras().getString(COMPANY);

        ImageView dismiss = new ImageView(this);
        dismiss.setImageResource(R.drawable.dismiss);
        dismiss.setX(140);
        dismiss.setY(200);
        ViewGroup.LayoutParams dismissParams = new ViewGroup.LayoutParams(50, 50);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeedbackActivity.this, QueueActivity.class);
                FeedbackActivity.this.startActivity(intent);
            }
        });

        TextView message = new TextView(this);
        ViewGroup.LayoutParams messageParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        message.setGravity(Gravity.CENTER);
        message.setTextColor(Color.BLACK);
        if (type.equals(BUMP)) {
            message.setText("Bumped back in the queue for\n " + company);
        } else if (type.equals(DROP)) {
            message.setText("Dropped out of queue for\n " + company);
        }

        layout.addView(message, messageParams);
        layout.addView(dismiss, dismissParams);
        return layout;
    }


}
