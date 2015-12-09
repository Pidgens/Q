package com.example.derekchiu.q;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.support.wearable.view.GridViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by tomo on 11/30/15.
 */
public class QueueActivity extends Activity {

    GridViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.queue_layout);
        if (CompanyPlaceList.getList().size() == 0) {
            setContentView(getMainView());
        } else {
            pager = (GridViewPager) findViewById(R.id.pager);
            pager.setAdapter(new QueueGridPagerAdapter(this, getFragmentManager()));
        }
    }

    private TextView getMainView() {
        TextView text = new TextView(this);
        text.setBackgroundColor(Color.WHITE);
        text.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        text.setText("You have not queued for any companies");
        text.setGravity(Gravity.CENTER);
        text.setTextColor(Color.BLACK);
        return text;
    }
}
