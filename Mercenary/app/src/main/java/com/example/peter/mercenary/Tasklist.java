package com.example.peter.mercenary;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by peter on 2018-02-23.
 * @See: Task
 */

public class Tasklist implements Parcelable {

    private ArrayList<Task> tasks = new ArrayList<Task>();

    /**
     *
     * @param task: a task to be added to the TaskList
     */

    public void add(Task task) {
        tasks.add(task);
    }

    /**
     *
     * @param task: a task to be checked for in the Tasklist
     * @return True/False: true if Tasklist contains task, false otherwise
     */

    public boolean hasTask(Task task) {

        //return Boolean.FALSE;
        return tasks.contains(task);

    }

    /**
     *
     * @param index: a given index in the TaskList
     * @return Task: returns the task at Index index in the Tasklist
     */

    public Task getTask(int index) {

        return tasks.get(index);

    }

    /**
     *
     * @return int: returns size of the taskList; i.e. how many tasks are in the tasKList
     */
    public int length() {
        return tasks.size();
    }

    /**
     *
     * @param task: a task to be removed from the TaskList
     */

    public void delTask(Task task) {

        tasks.remove(task);
    }

    /**
     *
     * @return: 0 //dummy value
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     *
     * @param dest: the destination to be written to
     * @param flags: any flags to be written
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(tasks);
    }

    /**
     *
     * @param in: object of type Parcel
     */

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
