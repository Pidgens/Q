package com.example.derekchiu.q;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.wearable.view.BoxInsetLayout;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by tomo on 12/6/15.
 */
public class QueueGridPagerAdapter extends FragmentGridPagerAdapter {

    private Context context;
    private static final int PADDING = 10;

    public QueueGridPagerAdapter(Context ctx, FragmentManager fm) {
        super(fm);
        context = ctx;
    }

    @Override
    public Fragment getFragment(final int i, int j) {
        Log.d("Here", i + " " + j + " Here");
        final CompanyQueue cq = CompanyQueue.getMockData().get(i);
        if (j == 0) {
            Fragment fragment = new Fragment() {
                @Override
                public View onCreateView(LayoutInflater inflater,
                                         ViewGroup container,
                                         Bundle savedInstanceState) {
                    BoxInsetLayout layout = new BoxInsetLayout(context);
                    layout.setLayoutParams(
                            new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT));
                    layout.setPadding(PADDING, PADDING, PADDING, PADDING);
                    layout.setBackgroundColor(Color.WHITE);

                    TextView name = new TextView(context);
                    name.setText(cq.getName());
                    name.setLayoutParams(
                            new BoxInsetLayout.LayoutParams(
                                    BoxInsetLayout.LayoutParams.MATCH_PARENT,
                                    BoxInsetLayout.LayoutParams.MATCH_PARENT));
                    name.setGravity(Gravity.CENTER);
                    name.setTextColor(Color.BLACK);

                    TextView place = new TextView(context);
                    place.setText(Integer.toString(cq.getPlace()));
                    place.setLayoutParams(
                            new BoxInsetLayout.LayoutParams(
                                    BoxInsetLayout.LayoutParams.MATCH_PARENT,
                                    BoxInsetLayout.LayoutParams.MATCH_PARENT));
                    place.setGravity(Gravity.CENTER | Gravity.BOTTOM);
                    place.setPadding(0, 0, 0, 100);
                    place.setTextColor(Color.BLACK);

                    layout.addView(name);
                    layout.addView(place);
                    return layout;
                }
            };
            return fragment;
        } else if (j == 1) {
            Fragment fragment = new Fragment() {
                @Override
                public View onCreateView(LayoutInflater inflater,
                                         ViewGroup container,
                                         Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);
                    return getDropButton(i);
                }
            };
            return fragment;
        } else if (j == 2) {
            Fragment fragment = new Fragment() {
                @Override
                public View onCreateView(LayoutInflater inflater,
                                         ViewGroup container,
                                         Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);
                    return getBumpButton(i);
                }
            };
            return fragment;
        }
        return null;
    }

    @Override
    public int getRowCount() {
        return CompanyQueue.getMockData().size();
    }

    @Override
    public int getColumnCount(int i) {
        return 3;
    }


    private ImageButton getDropButton(final int i) {
        ImageButton button = new ImageButton(context);
        button.setImageResource(R.drawable.drop);
        button.setBackgroundColor(Color.parseColor("#80BEFF"));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompanyQueue.getMockData().remove(i);
                QueueGridPagerAdapter.this.notifyDataSetChanged();
            }
        });
        return button;
    }

    private ImageButton getBumpButton(final int i) {
        ImageButton button = new ImageButton(context);
        button.setImageResource(R.drawable.bump);
        button.setBackgroundColor(Color.parseColor("#80BEFF"));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompanyQueue.getMockData().get(i).bumpQueue();
                QueueGridPagerAdapter.this.notifyDataSetChanged();
            }
        });
        return button;
    }
}
