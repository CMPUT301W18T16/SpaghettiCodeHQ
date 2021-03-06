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

    private String taskProvider;
    private LatLng geoLoc = new LatLng(0, 0);
    private ArrayList<String> pictureArray;
    private String status;
    private String userId;

    private String userName;
    private String acceptedUserId;

    @JestId
    private String id;


    public Task(String title, String description, LatLng geoLoc, String status, String userId, String userName, ArrayList<String> pictureList) {
        this.title = title;
        this.description = description;
        this.geoLoc = geoLoc;
        this.status = status;
        this.pictureArray = pictureArray;
        this.userId = userId;
        this.userName = userName;
        this.pictureArray = pictureList;

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

        this.pictureArray=pictureList;
    }

    /**
     *
     * @param geoLoc: the geolocation (most likely a pair of float coordinates) the user will assign to the task
     * Setter
     */
    public void setGeo(LatLng geoLoc){
        this.geoLoc=geoLoc;
    }

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

    public String getUserId() {
        return this.userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getAcceptedUser() {
        return this.acceptedUserId;
    }

    public void setAcceptedUser(String username) {
        this.acceptedUserId = acceptedUserId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(title);
        out.writeString(description);
        out.writeDouble(geoLoc.latitude);
        out.writeDouble(geoLoc.longitude);
        out.writeString(status);
        out.writeString(userId);
        out.writeString(userName);
        out.writeString(acceptedUserId);
        out.writeList(pictureArray);

    }

    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
        public Task createFromParcel(Parcel in) { return new Task(in);
        }

        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    // TODO: taskProvider and taskRequester of a task, getters and setters


    public String getProvider(){
        return this.taskProvider;

    }

    public void setTaskProvider(String taskProvider){
        this.taskProvider = taskProvider;
    }

    private Task(Parcel in) {
        this.title = in.readString();
        this.description = in.readString();
        this.geoLoc = new LatLng(in.readDouble(), in.readDouble());
        this.status = in.readString();
        this.userId = in.readString();
        this.userName = in.readString();
        this.acceptedUserId = in.readString();
        this.pictureArray = new ArrayList<String>();
        in.readList(pictureArray, String.class.getClassLoader());
    }


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