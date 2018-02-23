package com.example.peter.mercenary;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by peter on 2018-02-22.
 */

public class Requester extends User {
    private List tasks;

    public Requester(String Username){
        super(Username);
    }

    public List getTask(){
        return this.tasks;
    }
    public void addTask(Task task){
        this.tasks.add(task);

    }

    public void modTask(Task task)
    {
        boolean has = false;
        int index;

        try{
            has = this.tasks.contains(task);
            index = this.tasks.indexOf(task);

        }catch(NoSuchElementException e){
            e.printStackTrace();
        }





    }


}
