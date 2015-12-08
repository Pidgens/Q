package com.example.derekchiu.q;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.view.BoxInsetLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by tomo on 11/30/15.
 */
public class TimerActivity extends Activity {

    public static final String COMPANY = "company";

    TextView timerText;
    Timer timer;
    String companyString;
    final Handler handler = new Handler();
    int i = 60;

    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            timerText.setText(i + "s");
        }
    };
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        companyString = getIntent().getExtras().getString(COMPANY);
        setContentView(getMainView());
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateGUI();
            }
        }, 0, 1000);
    }

    private void updateGUI() {
        if (i > 0) {
            i--;
        } else if (i == 0) {
            startDrop();
        }
        handler.post(runnable);
    }

    private BoxInsetLayout getMainView() {
        BoxInsetLayout layout = new BoxInsetLayout(this);
        layout.setBackgroundColor(Color.WHITE);

        TextView company = new TextView(this);
        company.setTextColor(Color.BLACK);
        company.setText(companyString);
        company.setGravity(Gravity.CENTER);
        company.setTextSize(32);
        company.setY(company.getY() - 50);

        timerText = new TextView(this);
        timerText.setTextColor(Color.RED);
        timerText.setText(i + "s");
        timerText.setTextSize(28);
        timerText.setGravity(Gravity.CENTER);

        ImageView accept = new ImageView(this);
        accept.setImageResource(R.drawable.accept);
        accept.setX(180);
        accept.setY(200);
        ViewGroup.LayoutParams acceptParams = new ViewGroup.LayoutParams(75, 75);

        ImageView decline = new ImageView(this);
        decline.setImageResource(R.drawable.dismiss);
        decline.setX(80);
        decline.setY(200);
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDrop();
            }
        });
        ViewGroup.LayoutParams declineParams = new ViewGroup.LayoutParams(75, 75);

        layout.addView(timerText);
        layout.addView(company);
        layout.addView(accept, acceptParams);
        layout.addView(decline, declineParams);
        return layout;
    }

    private void startDrop() {
        Intent intent = new Intent(this, FeedbackActivity.class);
        intent.putExtra(FeedbackActivity.FEEDBACK_TYPE, FeedbackActivity.DROP);
        intent.putExtra(FeedbackActivity.COMPANY, companyString);
        CompanyQueue.getMockData().remove(CompanyQueue.getMockData().size() - 1);
        startActivity(intent);
    }

}
