package com.example.peter.mercenary;

import java.util.List;

/**
 * Created by peter on 2018-02-22.
 */

public class Requester extends User {
    private List tasks;

    public List getTask(){
        return this.tasks;
    }
    public void addTask(Task task){
        this.tasks.add(task);

    }


}
