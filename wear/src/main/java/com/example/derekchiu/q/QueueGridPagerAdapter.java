package com.example.derekchiu.q;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
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
        final CompanyPlace cq = CompanyPlaceList.getList().get(i);
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
                    name.setTextSize(32);
                    name.setY(name.getY() - 50);

                    TextView place = new TextView(context);
                    place.setText(Integer.toString(cq.getPlace()));
                    place.setLayoutParams(
                            new BoxInsetLayout.LayoutParams(
                                    BoxInsetLayout.LayoutParams.MATCH_PARENT,
                                    BoxInsetLayout.LayoutParams.MATCH_PARENT));
                    place.setGravity(Gravity.CENTER | Gravity.BOTTOM);
                    place.setY(place.getY() - 75);
                    place.setX(place.getX() + 10);
                    place.setTextSize(30);
                    place.setTextColor(Color.RED);

                    ImageView image = new ImageView(context);
                    image.setImageResource(R.drawable.marker);
                    image.requestLayout();
                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(31, 93);
                    image.setX(image.getX() + 100);
                    image.setY(image.getY() + 150);

                    layout.addView(name);
                    layout.addView(place);
                    layout.addView(image, layoutParams);
//                    if (i + 1 == CompanyPlaceList.getList().size()) {
//                        (new Thread() {
//                            public void run() {
//                                try {
//                                    sleep(4000);
//                                    Log.d("were", "We are definitely here");
//                                    Intent timer = new Intent(context, TimerActivity.class);
//                                    timer.putExtra(TimerActivity.COMPANY, cq.getName());
//                                    context.startActivity(timer);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }).start();
//                    }
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
        return CompanyPlaceList.getList().size();
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
                Intent feedback = new Intent(context, FeedbackActivity.class);
                feedback.putExtra(FeedbackActivity.FEEDBACK_TYPE, FeedbackActivity.DROP);
                feedback.putExtra(FeedbackActivity.COMPANY,
                        CompanyPlaceList.getList().get(i).getName());
                CompanyPlaceList.getList().remove(i);
                QueueGridPagerAdapter.this.notifyDataSetChanged();
                context.startActivity(feedback);
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
                CompanyPlaceList.getList().get(i).bumpQueue();
                QueueGridPagerAdapter.this.notifyDataSetChanged();
                Intent feedback = new Intent(context, FeedbackActivity.class);
                feedback.putExtra(FeedbackActivity.FEEDBACK_TYPE, FeedbackActivity.BUMP);
                feedback.putExtra(FeedbackActivity.COMPANY,
                        CompanyPlaceList.getList().get(i).getName());
                context.startActivity(feedback);
            }
        });
        return button;
    }
}
