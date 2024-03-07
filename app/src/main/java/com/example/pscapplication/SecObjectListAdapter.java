package com.example.pscapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class SecObjectListAdapter extends ArrayAdapter<SecObject> {
    private LayoutInflater inflater;
    private int Layout;
    private List<SecObject> objects;

    public SecObjectListAdapter(Context context, int resource, List<SecObject> objects) {
        super(context, resource, objects);
        this.objects = objects;
        this.Layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(this.Layout, parent, false);

        TextView tvObjectNameList = view.findViewById(R.id.tvObjectNameList);
        TextView tvObjectAddressList = view.findViewById(R.id.tvObjectAddressList);

        SecObject object = objects.get(position);

        tvObjectNameList.setText(object.getName());
        tvObjectAddressList.setText(object.getAddress());
        return view;
    }
}
