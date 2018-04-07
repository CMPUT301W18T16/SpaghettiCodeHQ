package com.example.peter.mercenary;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AddTaskActivity extends AppCompatActivity {
    private EditText title;
    private EditText description;
    private EditText status;
    private TextView error1;
    private EditText location;
    private Button done;
    private Task newTask;
    private User user; //currently logged in userid


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        final MapsActivity map = new MapsActivity();
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        user = getIntent().getParcelableExtra("user");

        done = (Button) findViewById(R.id.done1);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                title = (EditText) findViewById(R.id.title);
                description = (EditText) findViewById(R.id.desc);
                status = (EditText) findViewById(R.id.status);
                error1 = (TextView) findViewById(R.id.error1);
                location = (EditText) findViewById(R.id.location);

                LatLng geoLocation = map.getLocationFromAddress(getApplicationContext(), location.getText().toString());
                newTask = new Task(title.getText().toString(),
                        description.getText().toString(),
                        geoLocation, status.getText().toString(), user.getId(), user.getUsername() );

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

}
