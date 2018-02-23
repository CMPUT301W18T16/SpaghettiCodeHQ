package com.example.peter.mercenary;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by peter on 2018-02-22.
 */

public class Task {
    private String title;
    private String description;
    private List bids;
    private float geoLoc;
    private byte picture;
    private String taskStatus;

    public Task (String title, String description, String taskStatus ){
        this.title=title;
        this.description=description;
        this.taskStatus=taskStatus;

    }

    public void addPicture(byte picture){
        this.picture=picture;
    }

    public void delPicture(){
        this.picture=0;
    }

    public void modTitle(String title){
        this.title=title;
    }

    public void modDescription(String description){
        this.description=description;
    }

    public void addBid(float bid){

            this.bids.add(bid);
    }

    public void modBid(float bid)
    {
        boolean has = false;
        int index;

        try{
            has = this.bids.contains(bid);
            index = this.bids.indexOf(bid);

        }catch(NoSuchElementException e) {
            e.printStackTrace();
        }
    }

}
