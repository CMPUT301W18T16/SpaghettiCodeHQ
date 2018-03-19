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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AddTaskActivity extends AppCompatActivity {
    Integer numId = 0;
    EditText title;
    EditText description;
    EditText status;
    TextView error1;
    Button done;
    Intent data = new Intent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        done = (Button) findViewById(R.id.done1);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                title = (EditText) findViewById(R.id.title);
                description = (EditText) findViewById(R.id.desc);
                status = (EditText) findViewById(R.id.status);
                error1 = (TextView) findViewById(R.id.error1);

                numId += 1;
                if (title.getText().toString().length() > 30){
                    error1.setText("Task title must be less than 30 characters in length.");
                }
                if (description.getText().toString().length() > 300){
                    error1.setText("Task description must be less than 300 characters in length.");
                }
                data.putExtra("title", title.getText().toString());
                data.putExtra("description", description.getText().toString());
                data.putExtra("status", status.getText().toString());
                data.putExtra("id", Integer.toString(numId));
                setResult(RESULT_OK, data);
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
