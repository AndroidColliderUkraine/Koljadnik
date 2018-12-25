package com.androidcollider.koljadnik.feedback;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.androidcollider.koljadnik.R;

public class ActionsAdapter extends ArrayAdapter<String> {

    private LayoutInflater inflater;

    public ActionsAdapter(Activity activity, int res) {
        super(activity, res);
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        View row = inflater.inflate(R.layout.item_submit_action_type, parent, false);
        TextView label = (TextView) row.findViewById(R.id.tv_item);

        label.setText(getItem(position));
        return row;
    }

    public void update(String[] data) {
        addAll(data);
        notifyDataSetChanged();
    }
}
