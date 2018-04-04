package com.example.peter.mercenary;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


/**
 * Created by minci on 2018/3/19.
 */

public class SingleTaskActivity extends AppCompatActivity {
    User currentUser; //currently logged in user

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_task_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView taskTitle = findViewById(R.id.task_title);
        TextView taskDesc = findViewById(R.id.task_desc);
        TextView taskStatus = findViewById(R.id.task_status);
        TextView imgByte = findViewById(R.id.byte_img);
        TextView userText = findViewById(R.id.usernameText);

        ImageButton map = findViewById(R.id.mapBtn);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null){
            taskTitle.setText(bundle.getString("task_title"));
            taskDesc.setText(bundle.getString("task_desc"));
            taskStatus.setText(bundle.getString("task_status"));

            currentUser = bundle.getParcelable("user");
            if (bundle.getString("task_img") == null){
                imgByte.setText("null_img");
            }
        }

        userText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo
                //when the user clicks on the username go to the userprofile
            }
        });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo
                //view the task on the map when clicked
            }
        });
    }

    @Override
    public void onBackPressed() {

        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);
        super.onBackPressed();
    }


}



