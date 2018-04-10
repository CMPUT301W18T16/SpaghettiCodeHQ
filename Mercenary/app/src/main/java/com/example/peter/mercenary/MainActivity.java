package com.example.peter.mercenary;

import android.Manifest;
import android.content.Context;
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

/*
                        ___    ___
                       / _ \  / _ \
                      ( (_) )( (_) )
                       \_ _/  \_ _/
             __       _.-\\----//--._
         _  / _\___.-'/ _| / _\  /\/\`-._.-.__   _
        (_\_)| \___   ||_  ((_  //\/\\  _.-._ \-' )
          \__)   __)  | _| _) ) ||  || (_    \_.-'
                /_-.  ||   \_/  ||  .-'-.\
            _._//  / .--._______.-'\ \   \\__._
           /_._/   \ \              ))    \__._)
          (/     _.-')             ( `-._
                (_.-'         :F_P: `--._)
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TASKFILE = "taskfile.sav";
    private static final String ADDTASKFILE = "addTaskFile.sav";
    private EditText bodyText;
    private ListView oldTaskList;
    private ArrayList<Task> taskList;
    private ArrayList<Task> offlineAddedTaskList;
    private TaskAdapter adapter;
    private TimerTask timerTask;
    private Timer timer;
    private User user; //currently logged in user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        taskList= new ArrayList<Task>();
        offlineAddedTaskList = new ArrayList<Task>();
        loadOfflineTaskFile();
        addOfflineToOnline();

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
                intent.putParcelableArrayListExtra("taskList",taskList);
                intent.putParcelableArrayListExtra("offline",offlineAddedTaskList);
                startActivity(intent);
            }
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
        loadOfflineTaskFile();
        addOfflineToOnline();
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

                    Intent intent = new Intent(MainActivity.this, SingleTaskActivity.class);
                    intent.putExtra("task", task);
                    // I must pass taskId here, otherwise there's no way a task can update
                    // unless you want to pass a whole tasklist to every activity uses taskid ^^
                    intent.putExtra("taskid",task.getId());
                    intent.putExtra("user", user);

                    startActivityForResult(intent, 0);
                }
            });

    }

    public void onResume() {
        super.onResume();
        loadOfflineTaskFile();
        addOfflineToOnline();
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
            Intent intent = new Intent(MainActivity.this, MyUserProfile.class);
            intent.putExtra("user",user);
            startActivity(intent);

        } else if (id == R.id.nav_task_list) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }else if (id == R.id.nav_assigned_list) {
            Intent intent = new Intent(MainActivity.this, AssignedTaskList.class);
            intent.putExtra("user",user);
            startActivity(intent);
        }else if (id == R.id.activity_search){
            Intent intent = new Intent(this, SearchActivity.class);
            intent.putExtra("user",user);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
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


    private void loadOfflineTaskFile() {
        try {
            FileInputStream fis = openFileInput(ADDTASKFILE);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            //Code taken from http://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt Sept.22,2016
            Type listType = new TypeToken<ArrayList<Task>>(){}.getType();
            offlineAddedTaskList = gson.fromJson(in, listType);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            offlineAddedTaskList = new ArrayList<Task>();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }

    public void addOfflineToOnline(){

        if(NetworkStatus.connectionStatus(this)) {
            if (offlineAddedTaskList.isEmpty()) {

            } else {
                for (Task task : offlineAddedTaskList) {
                    ElasticFactory.AddingTasks addTask = new ElasticFactory.AddingTasks();
                    addTask.execute(task);
                }
                offlineAddedTaskList.clear();
                getApplicationContext().deleteFile(ADDTASKFILE);
            }
        }
    }

}

