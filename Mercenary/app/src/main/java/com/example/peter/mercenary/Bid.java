package com.example.peter.mercenary;

import io.searchbox.annotations.JestId;

/**
 * Created by peter on 2018-02-23.
 */

public class Bid {
    private String username;
    private float value;
    @JestId
    private String id;




    public Bid(String username, float value){
        this.username=username;
        this.value=value;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getUsername(){
        return this.username;
    }

    public float getValue(){
        return this.value;
    }

}
