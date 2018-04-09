package com.example.peter.mercenary;

/**
 * Created by peter on 2018-03-10.
 */

import android.os.AsyncTask;
import android.system.Os;
import android.util.Log;
import android.widget.Toast;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.searchbox.client.JestResult;
import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.Update;

/**
 * Main elastic factory class
 */
public class ElasticFactory {
    private static String elasticIndex = "cmput301w18t16";
    private static JestDroidClient client;

    public static class AddingTasks extends AsyncTask<Task, Void, Void>{

        /**
         *
         * @param tasks: a Tasklist
         * @return Void: function just updates elastic search in background, returns nothing
         *
         */
        @Override
        protected Void doInBackground(Task...tasks){
            verifySettings();

            for(Task task : tasks){

                JSONObject json = new JSONObject();

                try {
                    json = new JSONObject()
                            .put("description", task.getDescription())
                            .put("mData", 0)
                            .put("picture", task.getPhoto())
                            .put("status", task.getStatus())
                            .put("title",task.getTitle())
                            .put("taskRequester",task.getRequester())
                    ;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("json created:",json.toString());


                Index index = new Index.Builder(task).index(elasticIndex).type("task").build();

                try{
                    DocumentResult result = client.execute(index);
                    if(result.isSucceeded())
                    {
                        Log.i("ADDID", result.getId());
                        task.setId(result.getId());

                    }
                    else{
                        Log.i("Error","Elasticsearch was not able to add the task");
                        Log.i("addTask", result.getJsonString());
                    }
                }
                catch(Exception e){
                    Log.i("Error", "The application failed to build and send the task");
                }
            }
            return null;
        }
    }

    /**
     * Function used to build Task with offline functionality
     * @param task: a specific Task
     * @return Index: returns the Index of elasticSearch for the task
     */
    public static Index buildTaskOffline(Task task){
        Index index = new Index.Builder(task).index(elasticIndex).type("task").build();
        return index;
    }


    public static class AddingUser extends AsyncTask<User, Void, Void>{
        @Override
        protected Void doInBackground(User...users){
            verifySettings();
            //String uniqueID = UUID.randomUUID().toString();

            for(User user : users){
                JSONObject json = new JSONObject();

                try {
                     json = new JSONObject()
                                  .put("email", user.getEmail())
                                  .put("mData", 0)
                                  .put("phoneNumber", user.getPhoneNumber())
                                  .put("username", user.getUsername()
                                  )
                                  ;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("json created:",json.toString());

                Index index = new Index.Builder(user).index(elasticIndex).type("User").build();
                try{
                    DocumentResult result = client.execute(index);
                    if(result.isSucceeded())
                    {
                        user.setId(result.getId());
                    }
                    else{
                        Log.i("Error","Elasticsearch was not able to add the user");
                    }
                }
                catch(Exception e){
                    Log.i("Error", "The application failed to build and send the user");

                }
            }
            return null;
        }
    }

    public static class GetUser extends AsyncTask<String, Void, User>{
        @Override
        protected User doInBackground(String...search_paramters) {
            verifySettings();

            Search search = new Search.Builder("" + search_paramters[0] + "")
                    .addIndex(elasticIndex)
                    .addType("User")
                    .build();

            try {
                SearchResult result = client.execute(search);
                User user = result.getSourceAsObject(User.class);
                return user;
            } catch (Exception e) {
                Log.i("Error", "Failed to find user");
            }
            return null;
        }
    }

    public static class UpdateUser extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... search_parameters) {
            verifySettings();

            try {
                client.execute(new Update.Builder("" + search_parameters[0] + "")
                        .index(elasticIndex)

                        .type("user")
                        .id(search_parameters[1])
                        .build());
        } catch(Exception e) {
            Log.i("Error", "The application failed to build and find user");
        }
        return null;
        }
    }

    public static class UpdateTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... search_parameters) {
            verifySettings();

            try {
                client.execute(new Update.Builder("" + search_parameters[0] + "")
                        .index(elasticIndex)
                        .type("task")
                        .id(search_parameters[1])
                        .build());
            } catch(Exception e) {
                Log.i("Error", "The application failed to build and find task");
            }
            return null;
        }
    }

    public static class UpdateTask extends AsyncTask<Task, Void, Void> {
        protected Void doInBackground(Task...tasks) {
            verifySettings();

            String taskID;
            for (Task task : tasks){

                taskID = task.getId();
                Log.i("ID_in_ES", taskID);

                try {
                    // minci: changed Update to Index;
                    // Error:
                    // nested: ScriptException[scripts of type [indexed], operation [update] and lang [groovy] are disabled]
                    // The ES server disabled update and lang groovy
                    DocumentResult result = client.execute(new Index.Builder(task)
                            .index(elasticIndex)
                            .type("User")
                            .id(taskID)
                            .build());
                    Log.i("YOU TRIED", "to update a task");
                    if(result.getJsonString() != null)
                    {
                        if (result.isSucceeded()){
                            // make a toast, havent figured out how to do this in non-activity class
                            Log.i("RESULT:", result.getJsonString());
                        }
                        else {
                            // find out what's going on with our query
                            Log.i("RESULT:", result.getJsonString());
                        }
                    }

                    else
                    {
                        JSONObject obj = new JSONObject(result.getSourceAsString());
                        Log.i("Error","UpdateTask FAILED" + obj);
                    }
                } catch(Exception e){
                    Log.i("Error", "UpdateTask: The application failed to update task");
                    e.printStackTrace();

                }
            }

            return null;
        }
    }


    public static class checkUserExist extends AsyncTask<String, Void, Boolean>{
        @Override
        protected Boolean doInBackground(String...search_parameters){
            verifySettings();

            Search search = new Search.Builder(search_parameters[0])
                    .addIndex(elasticIndex)
                    .addType("User")
                    .build();

            try{
                SearchResult result = client.execute(search);

                if(result.getTotal() == 1)
                {
                    return true;
                }
                else // including multiple duplicated username in the db
                {
                    Log.i("Error","checkUserExist: The search query failed");
                    return false;
                }
            }
            catch (Exception e){

                Log.i("Error", "checkUserExist: Something went wrong when we tried to communicate with the elasticsearch server!");
                Log.i("exfeption", e.toString());
                e.printStackTrace();
                return false;

            }



    public static class checkUserExist extends AsyncTask<String, Void, Boolean>{
        @Override
        protected Boolean doInBackground(String...search_parameters){
            verifySettings();

            Search search = new Search.Builder("" + search_parameters[0] + "")
                        .addIndex(elasticIndex)
                        .addType("User")
                        .build();
                try {
                    SearchResult result = client.execute(search);
                    if (result.getTotal() == 1) {
                        return true;
                    } else {
                        Log.i("Error", "The search query failed");
                        return false;
                    }
                } catch (Exception e) {
                    Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
                    return false;
                }
        }
    }



    public static class getListOfTask extends AsyncTask<String, Void, ArrayList<Task>>{
            @Override
        protected ArrayList<Task> doInBackground(String...search_parameters){
                verifySettings();

                ArrayList<Task> taskList = new ArrayList<Task>();

                Search search = new Search.Builder(search_parameters[0])
                        .addIndex(elasticIndex)
                        .addType("task")
                        .build();
                try{
                    SearchResult result = client.execute(search);
                    if(result.isSucceeded())
                    {
                        List<Task> foundTasks =result.getSourceAsObjectList(Task.class);
                        taskList.addAll(foundTasks);
                        //Log.i("print",taskList.toString());
                    }
                    else
                    {
                        Log.i("Error","getListOfTask: The search query failed");
                    }
                }
                catch (Exception e){
                    Log.i("Error", "getListOfTask: Something went wrong when we tried to communicate with the elasticsearch server!");

                }
              return taskList;

            }
    }

    public static class DeletingTask extends AsyncTask<String, Void, Task>{
        @Override
        protected Task doInBackground(String...search_parameters){
            verifySettings();

            try {
                client.execute(new Delete.Builder(search_parameters[0])
                        .index(elasticIndex)
                        .type("task")
                        .build());

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

    }

    public static void verifySettings() {
        if (client == null) {
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }
}
