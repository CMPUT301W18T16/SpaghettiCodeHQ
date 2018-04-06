package com.example.peter.mercenary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Peter on 2018-04-04.
 */

public class TaskAdapter extends ArrayAdapter<Task> {
    public TaskAdapter(Context context, ArrayList<Task> task){
        super(context, 0, task);
        }

    //@Override
    public View getView(int position, View convertView, ViewGroup parent){

        Task task = getItem(position);

        if (convertView == null) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView textName = (TextView) convertView.findViewById(R.id.TextName);
        TextView textStatus = (TextView) convertView.findViewById(R.id.TextStatus);
        TextView textDescription = (TextView) convertView.findViewById(R.id.TextDescription);
        TextView textUser = (TextView) convertView.findViewById(R.id.textUser);

        textName.setText("Name: " +task.getTitle());
        textStatus.setText("Status: "+task.getStatus());
        textUser.setText("Date Started: "+task.getUser());
        textDescription.setText("Description: "+task.getDescription());

        return convertView;
        }


}