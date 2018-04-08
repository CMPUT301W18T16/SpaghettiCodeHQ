package com.example.peter.mercenary;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by peter on 2018-02-23.
 */

public class Tasklist implements Parcelable {

    private ArrayList<Task> tasks = new ArrayList<Task>();

    public void add(Task task) {
        tasks.add(task);
    }

    public boolean hasTask(Task task) {

        //return Boolean.FALSE;
        return tasks.contains(task);

    }

    public Task getTask(int index) {

        return tasks.get(index);

    }

    public int length() {
        return tasks.size();
    }

    public void delTask(Task task) {

        tasks.remove(task);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(tasks);
    }

    protected Tasklist(Parcel in) {
        tasks = in.createTypedArrayList(Task.CREATOR);
    }

    public static final Creator<Tasklist> CREATOR = new Creator<Tasklist>() {
        @Override
        public Tasklist createFromParcel(Parcel in) {
            return new Tasklist(in);
        }

        @Override
        public Tasklist[] newArray(int size) {
            return new Tasklist[size];
        }
    };
}
