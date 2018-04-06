package com.example.peter.mercenary;

/**
 * Created by peter on 2018-03-10.
 */

import android.os.AsyncTask;
import android.system.Os;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.searchbox.core.Delete;
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
                Index index = new Index.Builder(task).index(elasticIndex).type("minciTestTask").build();


                try{
                    DocumentResult result = client.execute(index);
                    if(result.isSucceeded())
                    {
                        task.setId(result.getId());
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
                                  .put("username", user.getUsername())
                                  ;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("json created:",json.toString());

                Index index = new Index.Builder(user).index(elasticIndex).type("minciTestUser").build();
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

    public static class UpdateUser extends AsyncTask<String, Void, Void>{
        protected Void doInBackground(String...search_parameters){
            verifySettings();

            try {
                client.execute(new Update.Builder(search_parameters[0])
                        .index(elasticIndex)
                        .type("minciTestUser")
                        .id("1")
                        .build());
            } catch(Exception e){
                Log.i("Error", "The application failed to build and send the user");
            }
            return null;
        }
    }

    public static class UpdateTask extends AsyncTask<Task, Void, Void> {
        protected Void doInBackground(Task...search_parameters) {
            verifySettings();
            try {
                client.execute(new Update.Builder(search_parameters[0])
                        .index(elasticIndex)
                        .type("minciTestTask")
                        .id("1")
                        .build());
            } catch(Exception e){
                Log.i("Error", "UpdateTask: The application failed to update task");
            }
            return null;
        }
    }




    // search for a user and reuturn the User object if there's one in database
    public static class returnUser extends AsyncTask<String, Void, User>{
        @Override
        protected User doInBackground(String...search_parameters){
            verifySettings();
            
            Search search = new Search.Builder(""+search_parameters[0]+"")
                    .addIndex(elasticIndex)
                    .addType("minciTestUser")
                    .build();
            try{
                SearchResult result = client.execute(search);
                if(result.getTotal()==1)
                {
                    Log.i("RESULT:", result.getSourceAsString());
                    String searchResult = result.getSourceAsString();

                    JSONObject obj = new JSONObject(searchResult);
                    String loginUserName = obj.getString("username");
                    String loginUserEMail = obj.getString("email");
                    String loginUserPhoneNumber = obj.getString("phoneNumber");
                    float loginUserRating = Float.valueOf(obj.getString("rating"));
                    User loginUser = new User(loginUserName, loginUserEMail,loginUserPhoneNumber,loginUserRating);
                    return loginUser;
                }
                else
                {
                    Log.i("Error","The search query failed");
                    User loginUser = null;
                    return loginUser;
                }
            }
            catch (Exception e){
                Log.i("Error: ", "invoking returnUser: Something went wrong when we tried to communicate with the elasticsearch server!");
                User loginUser = null;
                return loginUser;

            }

        }
    }



    public static class checkUserExist extends AsyncTask<String, Void, Boolean>{
        @Override
        protected Boolean doInBackground(String...search_parameters){
            verifySettings();

            Search search = new Search.Builder(search_parameters[0])
                    .addIndex(elasticIndex)
                    .addType("minciTestUser")
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


        }
    }



    public static class getListOfTask extends AsyncTask<String, Void, ArrayList<Task>>{
            @Override
        protected ArrayList<Task> doInBackground(String...search_parameters){
                verifySettings();

                ArrayList<Task> taskList = new ArrayList<Task>();

                Search search = new Search.Builder(search_parameters[0])
                        .addIndex(elasticIndex)
                        .addType("minciTestTask")
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
                        .type("minciTestTask")
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
