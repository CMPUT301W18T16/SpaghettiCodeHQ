package com.example.peter.mercenary;

/**
 * Created by peter on 2018-02-23.
 */

public class Bid {
    private String username;
    private float value;

    public Bid(String username, float value){
        this.username=username;
        this.value=value;
    }

    public String getUsername(){
        return this.username;
    }

    public float getValue(){
        return this.value;
    }
}
