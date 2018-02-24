package com.example.peter.mercenary;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by peter on 2018-02-22.
 */

public class Requester extends User {
    private Tasklist tasks;

    public Requester(String username){
        super(username);
    }

    public Tasklist getTask(){
        return this.tasks;
    }
    public void addTask(Task task){
        this.tasks.add(task);

    }




}
