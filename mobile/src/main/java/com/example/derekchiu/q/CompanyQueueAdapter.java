package com.example.derekchiu.q;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.ArrayList;

/**
 * Created by DanielHe on 12/7/2015.
 */
public class CompanyQueueAdapter extends ArrayAdapter<DataItem> {
    private ArrayList<DataItem> parseObj;
    public CompanyQueueAdapter(Context context, int textViewResourceId, ArrayList<DataItem> objects) {
        super(context, textViewResourceId, objects);
        this.parseObj = objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.row, null);
        }


        DataItem i = parseObj.get(position);

        if (i != null) {
            TextView companyView = (TextView) v.findViewById(R.id.company);
            TextView queueView = (TextView) v.findViewById(R.id.queue);

            if (companyView != null){
                companyView.setText(i.getCompany());
            }
            if (queueView != null){
                queueView.setText(i.getQueue());
            }

        }

        return v;
    }

}
