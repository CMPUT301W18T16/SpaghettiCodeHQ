package com.example.peter.mercenary;

import java.text.DecimalFormat;
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
	
    @JestId
    private String id; //id is equivalent to bidID
    private String userId;
	private String taskId;

    /**
     * Default constructor of bid
     * @param username: User's username
     * @param value: User's bid amount in dollars
     *
     */
    public Bid(String username, float value, String taskId){
		
		
        this.username=username;
        this.value=value;
		this.taskId = taskId;
		
    }

    public String getUserId()
	{
		return this.userId;
	}

    /**
	 * Setting bid ID
     * @param id: the bid ID
     */
    public void setId(String id) {
        this.id = id;
    }
	
	/**
	* Getting bid ID
	* @return id: the bid ID
	*/
	
	public String getID(){
		return this.id;
	}
	
	
	/**
	* Getting taskId
	* @return taskId: the ID of the task
	*/
	public String getTaskId(){
		return this.taskId;
	}


    /**
     *
     * @return User's username of the bid
     * @see: method Bid(String username, float value, String taskId) [constructor]
     */
		
		
    public String getUsername(){
        return this.username;
    }

    /**
     *
     * @return User's bid value
     * @see: method Bid(String username, float value, String taskId) [constructor]
     */
		
    public float getValue(){
        return this.value;
    }
	
	
    /**
     * Function used for comparision
     * @return
		
		-1 if the current bid value is greater than the new bid value
		0 if the current bid value is the same as the new bid value
		1 if the current bid value is less than the new bid value

     * @param: a new Bid
     */
		
	public int compareFxn(Bid bid) {
	        if (this.value > bid.value)
	            return -1;
	        else if (this.value == bid.value)
	            return 0;
	        else
	            return 1;
	    }

}


