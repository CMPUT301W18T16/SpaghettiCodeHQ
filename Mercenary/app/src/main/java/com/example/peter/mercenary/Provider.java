package com.example.peter.mercenary;

import java.util.List;

/**
 * Created by peter on 2018-02-22.
 */

public class Provider extends User {

    private List biddedTask;

    public Provider(String Username, List biddedTask){
        super(Username);
        this.biddedTask=biddedTask;
    }


    public void bidTask(Task task){
        this.biddedTask.add(task);

    }



}
