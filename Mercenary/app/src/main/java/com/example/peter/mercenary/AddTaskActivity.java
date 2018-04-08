package com.example.peter.mercenary;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import io.searchbox.core.Index;

public class AddTaskActivity extends AppCompatActivity {
    private EditText title;
    private EditText description;
    private EditText status;
    private TextView error1;
    private ImageButton maps;
    private Button done;
    private Task newTask;
    private User user; //currently logged in userid
    private static final String TASKFILE = "taskfile.sav";
    private static final String ADDTASKFILE = "addTaskFile.sav";
    private LatLng geoLocation = new LatLng(0, 0);
    private ArrayList<Task> taskList;
    private ArrayList<Task> offlineAddedTaskList;
    private Index index;
    private String source;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        user = getIntent().getParcelableExtra("user");
        taskList = getIntent().getParcelableArrayListExtra("taskList");
        offlineAddedTaskList = getIntent().getParcelableArrayListExtra("offline");
        done = (Button) findViewById(R.id.done1);

        if(NetworkStatus.connectionStatus(this)) {
            maps = (ImageButton) findViewById(R.id.mapBtn);
            maps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AddTaskActivity.this, MapsActivity.class);
                    intent.putExtra("goal", "getGeoLocation");
                    startActivityForResult(intent, 0);
                }
            });


            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    title = (EditText) findViewById(R.id.title);
                    description = (EditText) findViewById(R.id.desc);
                    status = (EditText) findViewById(R.id.status);
                    error1 = (TextView) findViewById(R.id.error1);

                    // LatLng geoLocation = map.getLocationFromAddress(getApplicationContext(), location.getText().toString());
                    newTask = new Task(title.getText().toString(),
                            description.getText().toString(),
                            geoLocation, status.getText().toString(), user.getId(), user.getUsername());

                    //Toast toast = Toast.makeText(getApplicationContext(), newTask.getTitle() + newTask.getDescription() + newTask.getStatus(),
                    //Toast.LENGTH_LONG);
                    Toast toast = Toast.makeText(getApplicationContext(), "New Task Added",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    ElasticFactory.AddingTasks addTask = new ElasticFactory.AddingTasks();
                    addTask.execute(newTask);
                    onBackPressed();
                }
            });
        }
        else
        {
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    title = (EditText) findViewById(R.id.title);
                    description = (EditText) findViewById(R.id.desc);
                    status = (EditText) findViewById(R.id.status);
                    error1 = (TextView) findViewById(R.id.error1);

                    // LatLng geoLocation = map.getLocationFromAddress(getApplicationContext(), location.getText().toString());
                    newTask = new Task(title.getText().toString(),
                            description.getText().toString(),
                            geoLocation, status.getText().toString(), user.getId(), user.getUsername());

                 //   Log.i("SOURCECHECK",source);

                    //Toast toast = Toast.makeText(getApplicationContext(), newTask.getTitle() + newTask.getDescription() + newTask.getStatus(),
                    //Toast.LENGTH_LONG);
                    Toast toast = Toast.makeText(getApplicationContext(), "New Task Added",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    taskList.add(newTask);
                    offlineAddedTaskList.add(newTask);
                    taskToAddFile();
                    saveTaskFile();
                    onBackPressed();
                }
            });
        }




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
           geoLocation = data.getParcelableExtra("location");
        }
    }

    private void saveTaskFile( ) {
        try {

            FileOutputStream fos = openFileOutput(TASKFILE, Context.MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            Gson gson = new Gson();
            gson.toJson(taskList, writer);
            writer.flush();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }


    private void taskToAddFile( ) {
        try {
            FileOutputStream fos = openFileOutput(ADDTASKFILE, Context.MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            Gson gson = new Gson();
            gson.toJson(offlineAddedTaskList, writer);
            writer.flush();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }




}
