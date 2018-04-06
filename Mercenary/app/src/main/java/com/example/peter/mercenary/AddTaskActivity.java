package com.example.peter.mercenary;

import android.content.Context;
import android.content.Intent;
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
<<<<<<< HEAD
    private EditText title;
    private EditText description;
    private EditText status;
    private TextView error1;
    private Button done;
    private Task newTask;
    private User user; //currently logged in userid
=======
    EditText title;
    EditText description;
    EditText status;
    EditText location;
    TextView error1;
    Button done;
    Task newTask;
    MapsActivity map;
>>>>>>> da4d0daf000ba3d6843353be49173f07a30ab2cf

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        map = new MapsActivity();
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
<<<<<<< HEAD
        user = getIntent().getParcelableExtra("user");
=======
>>>>>>> da4d0daf000ba3d6843353be49173f07a30ab2cf


        done = (Button) findViewById(R.id.done1);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                title = (EditText) findViewById(R.id.title);
                description = (EditText) findViewById(R.id.desc);
                status = (EditText) findViewById(R.id.status);
                error1 = (TextView) findViewById(R.id.error1);
                location = (EditText) findViewById(R.id.location);

                LatLng geoLocation = map.getLocationFromAddress(location.getText().toString());
                newTask = new Task(title.getText().toString(),
                        description.getText().toString(),
<<<<<<< HEAD
                        status.getText().toString(), user.getId() );
=======
                        geoLocation,
                        status.getText().toString());
>>>>>>> da4d0daf000ba3d6843353be49173f07a30ab2cf

                //Toast toast = Toast.makeText(getApplicationContext(), newTask.getTitle() + newTask.getDescription() + newTask.getStatus(),
                //Toast.LENGTH_LONG);
                Toast toast = Toast.makeText(getApplicationContext(), "New Task Added",
                        Toast.LENGTH_SHORT);
                toast.show();

                ElasticFactory.AddingTasks addTask = new ElasticFactory.AddingTasks();
                addTask.execute(newTask);

                finish();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
