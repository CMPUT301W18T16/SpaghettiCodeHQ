package com.example.peter.mercenary;

import java.util.List;

import io.searchbox.annotations.JestId;

/**
 * Created by peter on 2018-02-22.
 * Editted by Jason L. and Shardul S. on 2018-05-15
 */

public class Task {
    private String title;
    private String description;
    private BidList listBids;
    private float geoLoc;
    private byte picture;
    private String status;
    @JestId
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Task(String title, String description, String status) {
        this.title = title;
        this.description = description;
        this.status = status;

    }

    public void setTitle(String title) {

        if (title.length() > 30) {
            throw new TitleTooLongException();
        }
        this.title = title;
    }

    public void setDescription(String description) {

        if (description.length() > 300) {
            throw new DescTooLongException();
        }
        this.description = description;

    }
    public void setPhoto(byte picture){
        this.picture=picture;
    }
    public void setGeo(float geoLoc){ this.geoLoc=geoLoc;}
    public void setStatus(String status){this.status=status;}

    public String getTitle(){return this.title;}
    public String getDescription(){return this.description;}
    public byte getPhoto(){return this.picture;}
    public float getGeoLoc(){return this.geoLoc;}
    //public void getStatus(String taskStatus){this.status = status;}
    public String getStatus(){return this.status;}


    //public BidList getBids(){return this.listBids;}


    /* Save this for subclass "MyTasks"
    public void addBid(Bid bid){
        this.listBids.add(bid);
    }
    public void delBid(Bid bid){
        this.listBids.delBid(bid);}

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
    */

}
