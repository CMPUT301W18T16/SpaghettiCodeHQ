package com.example.peter.mercenary;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

import android.widget.TextView;


/**
 * Created by minci on 2018/3/19.
 */

public class SingleTaskActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_task_activity);

        TextView taskTitle = findViewById(R.id.task_title);
        TextView taskDesc = findViewById(R.id.task_desc);
        TextView taskStatus = findViewById(R.id.task_status);
        TextView imgByte = findViewById(R.id.byte_img);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null){
            taskTitle.setText(bundle.getString("task_title"));
            taskDesc.setText(bundle.getString("task_desc"));
            taskStatus.setText(bundle.getString("task_status"));
            if (bundle.getString("task_img") == null){
                imgByte.setText("null_img");


            }

        }


    }

    @Override
    public void onBackPressed() {

        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);
        super.onBackPressed();
    }


}



