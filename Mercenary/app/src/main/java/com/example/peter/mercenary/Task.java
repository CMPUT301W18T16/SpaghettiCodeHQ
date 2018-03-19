package com.example.peter.mercenary;

import io.searchbox.annotations.JestId;
import android.media.Image;
/**
 * Created by peter on 2018-02-22.
 * Edited by Jason L. and Shardul S. on 2018-05-15
 * @date 2018-02-22
 * @author Peter G, Jason L, Shardul S.
 * @see Tasklist
 * @version 1.0
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

    public Task(String title, String description, String status) {
        this.title = title;
        this.description = description;
        this.status = status;

    }

    /**
     *
      * @param title
     * @throws TitleTooLongException
     */
    public void setTitle(String title) throws TitleTooLongException{

        if (title.length() > 30) {
            throw new TitleTooLongException();
        }
        this.title = title;
    }

    /**
     *
     * @param description
     * @throws DescTooLongException
     */
    public void setDescription(String description) throws DescTooLongException{

        if (description.length() > 300) {
            throw new DescTooLongException();
        }
        this.description = description;

    }

    /**
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @param picture
     */
    public void setPhoto(byte picture){
        this.picture=picture;
    }

    /**
     *
     * @param geoLoc
     */
    public void setGeo(float geoLoc){ this.geoLoc=geoLoc;}

    /**
     *
     * @param status
     */
    public void setStatus(String status){this.status=status;}

    /**
     *
     * @return
     */
    public String getTitle(){return this.title;}

    /**
     *
     * @return
     */
    public String getDescription(){return this.description;}

    /**
     *
     * @return
     */
    public String getId() {return this.id;}

    /**
     *
     * @return
     */
    public byte getPhoto(){return this.picture;}

    /**
     *
     * @return
     */
    public float getGeoLoc(){return this.geoLoc;}

    /**
     *
     * @return
     */
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
