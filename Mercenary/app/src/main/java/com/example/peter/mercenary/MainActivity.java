package com.example.peter.mercenary;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TASKFILE = "taskfile.sav";
    private static final String ELASTICFILE = "elasticfile.sav";
    private EditText bodyText;
    private ListView oldTaskList;
    private ArrayList<Task> taskList = new ArrayList<Task>();
    private TaskAdapter adapter;
    private TimerTask timerTask;
    private Timer timer;
    private User user; //currently logged in user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = getIntent().getParcelableExtra("user");
        setContentView(R.layout.drawer_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        oldTaskList = (ListView) findViewById(R.id.myTaskView);
        Button addButton = (Button) findViewById(R.id.add1);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddTaskActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
            /*public void onActivityResult(int requestCode, int resultCode, Intent data){
                String title = data.getStringExtra("title");
                String desc = data.getStringExtra("description");
                String status = data.getStringExtra("status");
                String id = data.getStringExtra("id");
                Task newTask = new Task(title, desc, status, id);
                taskList.add(newTask);

                Toast toast = Toast.makeText(getApplicationContext(), title+
                        desc+status+id, Toast.LENGTH_LONG);
                toast.show();
                adapter.notifyDataSetChanged();
            }*/
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        //loadFromFile(); // TODO replace this with elastic search

        String query = "{\n" + " \"query\": { \"match\": {\"userId\":\"" + user.getId() + "\"} }\n" + "}";

        if(NetworkStatus.connectionStatus(this)) {

            ElasticFactory.getListOfTask getTaskList
                    = new ElasticFactory.getListOfTask();
            getTaskList.execute(query);

            try {
                taskList = getTaskList.get();
            } catch (Exception e) {
                Log.i("Error", "Failed to get the tweets from the async object");
            }

        /*taskList.add(new Task("Snow", "Please shovel the snow",
                new LatLng(53.5424028, -113.5095353), "requested"));
        taskList.add(new Task("Fix my xbox", "My xbox is broken please fix it",
                new LatLng(53.4658257, -113.4946275), "requested"));*/

        Intent intent = new Intent(MainActivity.this, RateReviewActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);

        /*intent = new Intent(MainActivity.this, UserProfile.class);
        intent.putExtra("user", user);
        intent.putExtra("clicked_user", user);
        startActivity(intent);*/

        adapter = new TaskAdapter(this, taskList);
        oldTaskList.setAdapter(adapter);

            // listen to task clicks
            oldTaskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Task task = (Task) oldTaskList.getAdapter().getItem(position);

                    Intent intent = new Intent(MainActivity.this, SingleTaskActivity.class);
                    intent.putExtra("task", task);
                    intent.putExtra("user", user);

                    startActivityForResult(intent, 0);

                }
            });

        }

        else{
            Toast.makeText(getApplicationContext(),"Lmao",Toast.LENGTH_LONG).show();
        }
    }

    public void onResume() {
        super.onResume();

        String query = "{\n" + " \"query\": { \"match\": {\"userId\":\"" + user.getId() + "\"} }\n" + "}";

        if (NetworkStatus.connectionStatus(this)) {

            ElasticFactory.getListOfTask getTaskList
                    = new ElasticFactory.getListOfTask();
            getTaskList.execute(query);

            try {
                taskList = getTaskList.get();
            } catch (Exception e) {
                Log.i("Error", "Failed to get the tweets from the async object");
            }
            adapter = new TaskAdapter(this, taskList);
            oldTaskList.setAdapter(adapter);
        }

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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private void loadFromFile(String FILENAME) {
        try {
            FileInputStream fis = openFileInput(FILENAME);
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

    private void saveInFile(String FILENAME) {
        try {

            FileOutputStream fos = openFileOutput(FILENAME,0);
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
