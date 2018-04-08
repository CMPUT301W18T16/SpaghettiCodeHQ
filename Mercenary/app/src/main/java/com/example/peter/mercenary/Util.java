package com.example.peter.mercenary;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;

/**
 * Created by peter on 2018-04-08.
 */

public class Util {

    public static void scheduleJob(Context context){
        ComponentName serviceComponent = new ComponentName(context, ElasticFactory.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setMinimumLatency(1 * 1000);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);

    }

}
