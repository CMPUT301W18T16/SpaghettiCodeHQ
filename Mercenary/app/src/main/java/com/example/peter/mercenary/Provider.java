package com.example.peter.mercenary;

import java.util.List;

/**
 * Created by peter on 2018-02-22.
 */

public class Provider extends User {

    private List biddedTask;

    public Provider(String username){
        super(username);
    }


    public void bidTask(Task task){
        this.biddedTask.add(task);

    }



}
