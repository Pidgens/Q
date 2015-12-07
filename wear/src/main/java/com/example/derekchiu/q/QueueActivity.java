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
import android.widget.ImageButton;
import android.widget.LinearLayout;

/**
 * Created by tomo on 11/30/15.
 */
public class QueueActivity extends Activity {

    GridViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.queue_layout);
        pager = (GridViewPager) findViewById(R.id.pager);
        pager.setAdapter(new QueueGridPagerAdapter(this, getFragmentManager()));
    }

}
