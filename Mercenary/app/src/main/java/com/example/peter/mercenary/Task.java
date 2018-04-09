package com.example.peter.mercenary;

import io.searchbox.annotations.JestId;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by peter on 2018-02-22.
 * Edited by Jason L. and Shardul S. on 2018-05-15
 * @date 2018-02-22
 * @author Peter G, Jason L, Shardul S.
 * @see Tasklist
 * @version 1.0
 */

public class Task implements Parcelable {
    private String title;
    private String description;
    private BidList listBids;
    private LatLng geoLoc;
    private ArrayList<String> pictureArray;
    private String status;
    private String taskProvider;
    private String taskRequester;
    User user;
    private int mData;


    @JestId
    private String id;

    public Task(String title, String description, String status, String taskRequester, ArrayList<String> pictureArray) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.taskRequester = taskRequester;
        this.pictureArray = pictureArray;
    }

    /**
     *
     * @param title: title of the task
     * @throws TitleTooLongException if the title is longer than 30 characters!
     */
    public void setTitle(String title) throws TitleTooLongException{

        if (title.length() > 30) {
            throw new TitleTooLongException();
        }
        this.title = title;
    }

    /**
     *
     * @param description: description of the task
     * @throws DescTooLongException if the description is longer than 300 characters!
     * Setter
     */
    public void setDescription(String description) throws DescTooLongException{

        if (description.length() > 300) {
            throw new DescTooLongException();
        }
        this.description = description;

    }

    /**
     *
     * @param id: id of the task
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @param pictureList: the photo the user wishes to assign to the task
     * Setter
     */
    public void setPhoto(ArrayList<String> pictureList){

        this.pictureArray = pictureList;
    }

    /**
     *
     * @param geoLoc: the geolocation (most likely a pair of float coordinates) the user will assign to the task
     * Setter
     */
    public void setGeo(LatLng geoLoc){ this.geoLoc=geoLoc;}

    /**
     *
     * @param status: the status the user wants to assign to the task (i.e. open, closed, pending, etc).
     * Setter
     */
    public void setStatus(String status){this.status=status;}

    /**
     *
     * @return title: title of the task.
     * Setter
     */
    public String getTitle(){return this.title;}

    /**
     * @return description: return the description of the task.
     * Getter.
     * @see  public Task(String title, String description, String status) (Task constructor)
     */
    public String getDescription(){return this.description;}

    /**
     *
     * @return id: return the id of the task.
     * Getter.
     * @see public void SetId(...)
     */
    public String getId() {return this.id;}

    /**
     * @return picture: return the photo assigned to the task.
     * Getter.
     * @see public void setPhoto(...)
     */

    public ArrayList<String> getPhoto(){return this.pictureArray;}

    /**
     *
     * @return geoLoc: return the geoLoc coordinates of the task.
     * Getter.
     * @see public void setGeo(...)
     */
    public LatLng getGeoLoc(){return this.geoLoc;}

    /**
     *
     * @return status: return the status of the task
     * Getter.
     * @see public Task(String title, String description, String status) (Task constructor)
     */
    public String getStatus(){return this.status;}

    public User getUser() {
        return this.user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mData);
    }

    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
        public Task createFromParcel(Parcel in) { return new Task(in);
        }

        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    // TODO: taskProvider and taskRequester of a task, getters and setters

    public String getRequester(){
        return this.taskRequester;
    }

    public String getProvider(){
        return this.taskProvider;

    }

    public void setRequester(String taskRequester){
        this.taskRequester = taskRequester;
    }

    public void setTaskProvider(String taskProvider){
        this.taskRequester = taskProvider;
    }




    private Task(Parcel in) {
        mData = in.readInt();
    }



    public BidList getBids(){return this.listBids;}

    public void setBids(BidList listbids){this.listBids = listbids;}

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
