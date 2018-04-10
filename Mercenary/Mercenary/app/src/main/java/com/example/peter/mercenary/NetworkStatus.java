package com.example.peter.mercenary;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by peter on 2018-04-05.
 */

public class NetworkStatus{
    private static final String ADDTASKFILE = "addTaskFile.sav";
    private ArrayList<Task> offlineAddedTaskList;

    public static boolean connectionStatus(Context context){

        ConnectivityManager conMng =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork= conMng.getActiveNetworkInfo();
        if (null!=activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {

                return true;
            }
            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                return true;
            }
        }
        return false;
    }



}
