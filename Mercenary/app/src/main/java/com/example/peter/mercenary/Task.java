package com.example.peter.mercenary;

import java.util.List;

/**
 * Created by peter on 2018-02-22.
 */

public class Task {
    private String title;
    private String description;
    private BidList listBids;
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

    public void addBid(Bid bid){
        this.listBids.add(bid);
    }

    public void modTitle(String title){
        this.title=title;
    }

    public void modDescription(String description){
        this.description=description;
    }

    public float getLowestBid() {
        float value=100000;
        for(int i=0; i<listBids.size(); i++){

            if (i==0){value = listBids.getBid(i).getValue();}
            else{
                if (value > listBids.getBid(i).getValue()){
                    value =listBids.getBid(i).getValue();
                }
            }

        }
        return value;
    }

}
