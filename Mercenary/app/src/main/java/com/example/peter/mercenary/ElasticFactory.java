package com.example.peter.mercenary;

/**
 * Created by peter on 2018-03-10.
 */

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.searchbox.core.Delete;
import io.searchbox.core.DeleteByQuery;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;


public class ElasticFactory {
    private static JestDroidClient client;

    public static class AddingTasks extends AsyncTask<Task, Void, Void>{
        @Override
        protected Void doInBackground(Task...tasks){
            verifySettings();
            String uniqueID = UUID.randomUUID().toString();

            for(Task task : tasks){
                Index index = new Index.Builder(task).index("testing").type("task").id(uniqueID).build();
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

    public static class getListOfTask extends AsyncTask<String, Void, ArrayList<Task>>{
            @Override
        protected ArrayList<Task> doInBackground(String...search_parameters){
                verifySettings();

                ArrayList<Task> taskList = new ArrayList<Task>();

                Search search = new Search.Builder(search_parameters[0])
                        .addIndex("testing")
                        .addType("task")
                        .build();
                try{
                    SearchResult result = client.execute(search);
                    if(result.isSucceeded())
                    {
                        List<Task> foundTasks =result.getSourceAsObjectList(Task.class);
                        taskList.addAll(foundTasks);
                    }
                    else
                    {
                        Log.i("Error","The search query failed");
                    }
                }
                catch (Exception e){
                    Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");

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
                        .index("testing")
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
