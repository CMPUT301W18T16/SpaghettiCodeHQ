package com.example.peter.mercenary;

import io.searchbox.annotations.JestId;
import android.media.Image;
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
    @JestId
    private String id;

    public String getId() { return  id; }
    public void setId(String id) { this.id = id; }

    public Task (String title, String description, String taskStatus ){
        this.title=title;
        this.description=description;
        this.taskStatus=taskStatus;

    }


    public void addPicture(byte picture){
        this.picture=picture;
    }

    public void addGeo(float geoLoc){ this.geoLoc=geoLoc;}

    public void modDescription(String description){
        this.description=description;
    }

    public void addBid(Bid bid){
        this.listBids.add(bid);
    }

    public void modTitle(String title){
        this.title=title;
    }

    public void delBid(Bid bid){this.listBids.delBid(bid);}

    public float getLowestBid() {
        float value=-1;

        for(int i=0; i<listBids.size(); i++){

            if (i==0 || value==-1){
                value = listBids.getBid(i).getValue();
            }
            else{
                if (value > listBids.getBid(i).getValue()){
                    value =listBids.getBid(i).getValue();
                }
            }

        }
        return value;
    }

    public byte getPicture(){return this.picture;}

    public String getTitle(){return this.title;}

    public String getDescription(){return this.description;}

    public BidList getBids(){return this.listBids;}

    public float getGeoLoc(){return this.geoLoc;}

    public void modTaskStatus(String taskStatus){this.taskStatus=taskStatus;}

    public String getTaskStatus(){return this.taskStatus;}
}
