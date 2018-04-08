package com.example.peter.mercenary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by peter on 2018-04-05.
 */

public class NetworkStatus{




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
