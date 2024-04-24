package com.example.focus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

public class CalendarAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;

    public CalendarAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 1; // Only one calendar view
    }

    @Override
    public Object getItem(int position) {
        return null; // Not used
    }

    @Override
    public long getItemId(int position) {
        return 0; // Not used
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            //convertView = (LinearLayout) View.inflate(this, R.layout.calender_view, null);
            convertView = inflater.inflate(R.layout.calender_view, parent, false);
        }
        return convertView;
    }
}
