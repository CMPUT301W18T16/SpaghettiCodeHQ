package com.example.peter.mercenary;

import io.searchbox.annotations.JestId;

/**
 * @date Created by peter on 2018-02-23. Modified by Shardul on 2018-03-18.
 * @author Peter G.
 * @version version 1.0
 * @see BidList
 *
 */

public class Bid {
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

}
