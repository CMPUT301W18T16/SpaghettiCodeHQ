package com.example.peter.mercenary;

/**
 * Created by peter on 2018-03-10.
 */

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.searchbox.client.JestResult;
import io.searchbox.core.Delete;
import io.searchbox.core.DeleteByQuery;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.Update;

public class ElasticFactory {
    private static String elasticIndex = "cmput301w18t16";
    private static JestDroidClient client;

    public static class AddingTasks extends AsyncTask<Task, Void, Void>{
        @Override
        protected Void doInBackground(Task...tasks){
            verifySettings();
            //String uniqueID = UUID.randomUUID().toString();

            for(Task task : tasks){
                Index index = new Index.Builder(task).index(elasticIndex).type("task1").build();

                try{
                    DocumentResult result = client.execute(index);
                    if(result.isSucceeded())
                    {
                        Log.i("ADDID", result.getId());

                        task.setId(result.getId());
                        Log.i("taskid!!!!!!!!!!!!",task.getId());

                    }
                    else{
                        Log.i("Error","Elasticsearch was not able to add the task");
                    }
                }
                catch(Exception e){
                    Log.i("Error", "The application failed to build and send the task");

                }
            }
            return null;
        }
    }

    public static Index buildTaskOffline(Task task){
        Index index = new Index.Builder(task).index(elasticIndex).type("task1").build();
        return index;
    }

    public static class AddingUser extends AsyncTask<User, Void, Void>{
        @Override
        protected Void doInBackground(User...users){
            verifySettings();
            //String uniqueID = UUID.randomUUID().toString();

            for(User user : users){
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

    public static class UpdateTask extends AsyncTask<Task, Void, Void> {
        protected Void doInBackground(Task...tasks) {
            verifySettings();

            String taskID;
            for (Task task : tasks){

                taskID = task.getId();
                Log.i("ID_in_ES", taskID);

                /*
                ArrayList<String> imageListToBeUpdated = task.getPhoto();
                // last element of img list
                String imageToBeUpdated = imageListToBeUpdated.get(imageListToBeUpdated.size()-1);
                if (!StringUtils.isEmpty(imageToBeUpdated)) {
                    String escapedImg = org.apache.lucene.queryparser.classic.QueryParser.escape(imageToBeUpdated);
                    // StringEscapeUtils is extremely slow
                    //String escapedImg = StringEscapeUtils.escapeJson(imageToBeUpdated);
                    task.setPhoto(escapedImg);
                }
                else{
                }
                */
                try {
                    // minci: changed Update to Index;
                    // Error:
                    // nested: ScriptException[scripts of type [indexed], operation [update] and lang [groovy] are disabled]
                    // The ES server disabled update and lang groovy
                    DocumentResult result = client.execute(new Index.Builder(task)
                            .index(elasticIndex)
                            .type("minciTestTask1")
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
                    Log.i("Errorcheckuserexist", "Something went wrong when we tried to communicate with the elasticsearch server!");
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
                        .addType("task1")
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
                        Log.i("Error","The search query failed");
                    }
                }
                catch (Exception e){
                    Log.i("Errorgetlistoftask", "Something went wrong when we tried to communicate with the elasticsearch server!");

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
                        .type("task1")
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
