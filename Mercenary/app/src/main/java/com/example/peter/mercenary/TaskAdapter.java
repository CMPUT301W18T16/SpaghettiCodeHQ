package com.example.peter.mercenary;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

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
        Log.i("LOOKHERE", task.getTitle());

        if (convertView == null) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView textName = (TextView) convertView.findViewById(R.id.TextName);
        TextView textUser = (TextView) convertView.findViewById(R.id.TextUser);
        TextView textStatus = (TextView) convertView.findViewById(R.id.TextStatus);
        TextView textDescription = (TextView) convertView.findViewById(R.id.TextDescription);

        textName.setText(String.format("Title: %s", task.getTitle()));
        textUser.setText(String.format("Date Started: %s", task.getUserName()));
        textStatus.setText(String.format("Status: %s", task.getStatus()));
        //textUser.setText(String.format("Date Started: %s", task.getUserId()));
        textDescription.setText(String.format("Description: %s", task.getDescription()));

        String query = "{\n" + " \"query\": { \"match\": {\"_id\":\"" + task.getUserId() + "\"} }\n" + "}";



        return convertView;
        }


}