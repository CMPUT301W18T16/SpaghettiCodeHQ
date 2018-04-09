package com.example.peter.mercenary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

/**
 * Created by peter on 2018-04-06.
 * Modified by Shardul on 2018-04-09.
 */

public class NetworkStatusReceiver extends BroadcastReceiver {

    /**
     *
     * @param context: the current Context of NetworkStatusReceiver
     * @param intent: Intent intent, used for Receiving a Network Status
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        boolean isConnected = intent.getBooleanExtra(
                ConnectivityManager.EXTRA_NO_CONNECTIVITY,
                false);
        if (isConnected) {
            Toast.makeText(context, "Connection Lost", Toast.LENGTH_LONG).show();


        } else {
            Toast.makeText(context, "Connected", Toast.LENGTH_LONG).show();


        }
    }

}