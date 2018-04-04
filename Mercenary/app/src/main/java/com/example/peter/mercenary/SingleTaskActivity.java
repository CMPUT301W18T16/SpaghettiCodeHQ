package com.example.peter.mercenary;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.lang3.ObjectUtils;


/**
 * Created by minci on 2018/3/19.
 */

public class SingleTaskActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_task_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView taskTitle = findViewById(R.id.task_title);
        TextView taskDesc = findViewById(R.id.task_desc);
        TextView taskStatus = findViewById(R.id.task_status);

        ImageView imgByte = findViewById(R.id.byte_img);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null){
            taskTitle.setText(bundle.getString("task_title"));
            taskDesc.setText(bundle.getString("task_desc"));
            taskStatus.setText(bundle.getString("task_status"));


            // deal with single image first
            if (bundle.getString("task_img") == null){
                taskTitle.setText("OMG THERES NO IMG");
            }
            else{

                byte[] decodedString = Base64.decode(bundle.getString("task_img"), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imgByte.setImageBitmap(decodedByte);


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



