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

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AddTaskActivity extends AppCompatActivity {
    EditText title;
    EditText description;
    EditText status;
    TextView error1;
    TextView user;
    Button done;
    Task newTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);


        done = (Button) findViewById(R.id.done1);
        final User taskRequester;
        Bundle extras = getIntent().getExtras();
        taskRequester = (User) extras.getParcelable("user");

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                title = (EditText) findViewById(R.id.title);
                description = (EditText) findViewById(R.id.desc);
                status = (EditText) findViewById(R.id.status);
                error1 = (TextView) findViewById(R.id.error1);


                newTask = new Task(title.getText().toString(),
                        description.getText().toString(),
                        status.getText().toString(), taskRequester.getUsername().toString()
                );


                //Toast toast = Toast.makeText(getApplicationContext(), newTask.getTitle() + newTask.getDescription() + newTask.getStatus(),
                //Toast.LENGTH_LONG);
                Toast toast = Toast.makeText(getApplicationContext(), "New Task Added",
                        Toast.LENGTH_SHORT);
                toast.show();

                ElasticFactory.AddingTasks addTask = new ElasticFactory.AddingTasks();
                addTask.execute(newTask);

                finish();
                //minci: we need to refresh main activity to see the new task after an task is added;
                //one need to manually refresh task if he is back from add_task_activity
                startActivity(getIntent());
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
