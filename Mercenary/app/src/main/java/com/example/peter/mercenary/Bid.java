package com.example.peter.mercenary;

import android.os.Parcel;
import android.os.Parcelable;

import io.searchbox.annotations.JestId;

/**
 * @date Created by peter on 2018-02-23. Modified by Shardul on 2018-03-18.
 * @author Peter G.
 * @version version 1.0
 * @see BidList
 *
 */

public class Bid implements Parcelable {
    private String username;
    private float value;
    private String flag;  // is the bid declined or accepted

    @JestId
    private String id;
    private String UserId;

    /**
     *
     * @param username: User's username
     * @param value: User's bid amount in dollars
     *
     */
    public Bid(String username, float value, String flag){
        this.username=username;
        this.value=value;
        this.flag = flag;
    }
    public static final Creator<Bid> CREATOR = new Creator<Bid>() {
        @Override
        public Bid createFromParcel(Parcel in) {
            return new Bid(in);
        }

        @Override
        public Bid[] newArray(int size) {
            return new Bid[size];
        }
    };

    /**
     * @param id: the ID of User's username
     */
    public void setId(String id) {
        this.id = id;
    }


    /**
     *
     * @return: User's username of the bid
     * @see: method Bid(string username, float value) [constructor]
     */
    public String getUsername(){
        return this.username;
    }

    /**
     *
     * @return: User's bid amount
     * @see: method Bid(string username, float value) [constructor]
     */
    public float getValue(){
        return this.value;
    }


    public void setFlag(String flag){this.flag = flag;}
    public String getFlag(){return this.flag;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeFloat(value);
        dest.writeString(id);
        dest.writeString(UserId);
    }

    /**
     *
     * @param username: User's username
     * @param value: User's bid amount in dollars
     *
     */
    public Bid(String username, float value){
        this.username=username;
        this.value=value;
    }

    protected Bid(Parcel in) {
        username = in.readString();
        value = in.readFloat();
        id = in.readString();
        UserId = in.readString();
    }

}
