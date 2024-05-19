package com.example.pscapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class LogStringListAdapter extends ArrayAdapter<LogString> {

    private LayoutInflater inflater;
    private int Layout;
    private List<LogString> logs;

    public LogStringListAdapter(Context context, int resource, List<LogString> logs) {
        super(context, resource, logs);
        this.logs = logs;
        this.Layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(this.Layout, parent, false);

        TextView tvLogsList = view.findViewById(R.id.tvLogsList);

        LogString logString = logs.get(position);

        tvLogsList.setText(logString.getString());
        return view;
    }

}