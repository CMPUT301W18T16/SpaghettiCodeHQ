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
import android.widget.Button;
import android.util.Log;
import android.widget.ImageView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.ObjectUtils;

import java.util.concurrent.ExecutionException;


/**
 * Created by minci on 2018/3/19.
 */

public class SingleTaskActivity extends AppCompatActivity {
    private User user; //currently logged in user
    private Task task;
    private User clickedUser; //target user
    private static final String TASKFILE = "taskfile.sav";
    private static final String ELASTICFILE = "elasticfile.sav";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_task_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView taskTitle = findViewById(R.id.task_title);
        TextView taskDesc = findViewById(R.id.task_desc);
        TextView taskStatus = findViewById(R.id.task_status);
        TextView userText = findViewById(R.id.usernameText);
        Button editTask = findViewById(R.id.edit_task);

        ImageButton map = findViewById(R.id.mapBtn);
        task = getIntent().getParcelableExtra("task");
        user = getIntent().getParcelableExtra("user");

        /*if (bundle != null) {

            userText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //todo
                    //when the user clicks on the username go to the userprofile
                }
            });

            editTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentEdit = new Intent(getApplicationContext(), EditTaskActivity.class);
                    startActivity(intentEdit);
                    intentEdit.putExtra("task", task);
                }
            });

            map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    intent.putExtra("Lat", 53.526f); //needs to pass task location
                    intent.putExtra("Long", -113.525f);
                    startActivity(intent);
                }
            });
        }*/
              
        taskTitle.setText(task.getTitle());
        taskDesc.setText(task.getDescription());
        taskStatus.setText(task.getStatus());

        String query = "{\n" + " \"query\": { \"match\": {\"_id\":\"" + task.getUserId() + "\"} }\n" + "}";

        if (NetworkStatus.connectionStatus(this)) {
            try {
                ElasticFactory.GetUser getUser = new ElasticFactory.GetUser();
                clickedUser = getUser.execute(query).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            userText.setText(clickedUser.getUsername());
            userText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //todo
                    //when the user clicks on the username go to the userprofile
                    Intent intent = new Intent(SingleTaskActivity.this, UserProfile.class);
                    intent.putExtra("user", user.getUsername());
                    intent.putExtra("clicked_user", clickedUser.getUsername());
                    startActivity(intent);
                }
            });

            map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    intent.putExtra("goal", "single");
                    intent.putExtra("lat", task.getGeoLoc().latitude);
                    intent.putExtra("long", task.getGeoLoc().longitude);
                    startActivity(intent);
                }
            });
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(), "Currently offline, functionalities may not be available.", Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);
        super.onBackPressed();
    }
}