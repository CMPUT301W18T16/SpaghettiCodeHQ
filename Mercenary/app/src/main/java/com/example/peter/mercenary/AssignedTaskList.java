package com.example.peter.mercenary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jerrad on 08/04/18.
 */

public class AssignedTaskList extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TASKFILE = "taskfile.sav";
    private static final String ADDTASKFILE = "addTaskFile.sav";
    private EditText bodyText;
    private ListView oldTaskList;
    private ArrayList<Task> taskList;
    private ArrayList<Task> assignedTask;
    private TaskAdapter adapter;
    private TimerTask timerTask;
    private Timer timer;
    private User user; //currently logged in user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        taskList= new ArrayList<Task>();

        user = getIntent().getParcelableExtra("user");
        setContentView(R.layout.drawer_layout_assigned);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarA);
        setSupportActionBar(toolbar);
        oldTaskList = (ListView) findViewById(R.id.myTaskView);
        Button addButton = (Button) findViewById(R.id.add1);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    protected void onStart() {
        super.onStart();

        String query = "{\n" + " \"query\": { \"match\": {\"acceptedUser\":\"" + user.getId() + "\"} }\n" + "}";

        if(NetworkStatus.connectionStatus(this)) {
            ElasticFactory.getListOfTask getTaskList
                    = new ElasticFactory.getListOfTask();
            getTaskList.execute(query);

            try {
                taskList = getTaskList.get();
            } catch (Exception e) {
                Log.i("Error", "Failed to get the tweets from the async object");
            }
        } else {
            loadTaskFile();
        }
        saveTaskFile();

        adapter = new TaskAdapter(this, taskList);
        oldTaskList.setAdapter(adapter);

        // listen to task clicks
        oldTaskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task task = (Task) oldTaskList.getAdapter().getItem(position);

                Intent intent = new Intent(AssignedTaskList.this, SingleTaskActivity.class);
                intent.putExtra("task", task);
                intent.putExtra("user", user);

                startActivityForResult(intent, 0);
            }
        });

    }

    public void onResume() {
        super.onResume();

        try {
            Thread.sleep(500);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        oldTaskList = (ListView) findViewById(R.id.myTaskView);

        String query = "{\n" + " \"query\": { \"match\": {\"userId\":\"" + user.getId() + "\"} }\n" + "}";

        if (NetworkStatus.connectionStatus(this)) {

            ElasticFactory.getListOfTask getTaskList
                    = new ElasticFactory.getListOfTask();

            getTaskList.execute(query);

            try {
                Log.i("Resuming", "Check If resuming");

                taskList = getTaskList.get();
            } catch (Exception e) {
                Log.i("Error", "Failed to get the tweets from the async object");
            }
        } else {
            loadTaskFile();
        }
        saveTaskFile();

        adapter = new TaskAdapter(this, taskList);
        oldTaskList.setAdapter(adapter);

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_user_profile) {
            Intent intent = new Intent(this, UserProfile.class);
            intent.putExtra("user",user);
            startActivity(intent);
        } else if (id == R.id.nav_task_list) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("user",user);
            startActivity(intent);
        }else if (id == R.id.nav_assigned_list) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_userprofile);
            drawer.closeDrawer(GravityCompat.START);
        }

        return true;
    }



    private void loadTaskFile() {
        try {
            FileInputStream fis = openFileInput(TASKFILE);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            //Code taken from http://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt Sept.22,2016
            Type listType = new TypeToken<ArrayList<Task>>(){}.getType();
            taskList = gson.fromJson(in, listType);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            taskList = new ArrayList<Task>();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
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



}

