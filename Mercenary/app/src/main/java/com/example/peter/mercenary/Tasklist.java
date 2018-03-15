package com.example.peter.mercenary;

import java.util.ArrayList;

/**
 * Created by peter on 2018-02-23.
 */

public class Tasklist {

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

    public void delTask(Task task) {

        tasks.remove(task);
    }
}
