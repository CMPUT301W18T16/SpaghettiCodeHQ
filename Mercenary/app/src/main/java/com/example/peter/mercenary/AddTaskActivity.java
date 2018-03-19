package com.example.peter.mercenary;

import android.content.Context;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button done = (Button) findViewById(R.id.done1);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                EditText title = (EditText) findViewById(R.id.title);
                EditText description = (EditText) findViewById(R.id.desc);
                EditText status = (EditText) findViewById(R.id.status);
                TextView error1 = (TextView) findViewById(R.id.error1);

                try {
                    Task newTask = new Task(title.getText().toString(), description.getText().toString(),
                            status.getText().toString(), Integer.toString(numId));
                    numId += 1;
                    throw new TitleTooLongException();
                    throw new DescTooLongException();
                } catch (TitleTooLongException e){
                    error1.setText("Title must be less than 30 characters in length.");
                } catch (DescTooLongException e){
                    error1.setText("Description must be less than 300 characters in length.");
                }
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
