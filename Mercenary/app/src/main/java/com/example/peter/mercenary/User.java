package com.example.peter.mercenary;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by peter on 2018-02-22.
 * @date 2018-02-22
 * @author Peter Gao
 * @version 1.0
 */
public class User implements Parcelable{
    private String username;
    private String email;
    private String phoneNumber;
    private float rating;
    private Tasklist biddedTask;
    private Tasklist tasks;
    private int mData;

    /**
     *
     * @param username: user's username
     * @param email: user's email address
     * @param phoneNumber: user's phone #
     * @param rating: the rating of the user in terms of bidding/completing task/etc
     * @throws UsernameTooLongException: if username is too long (greater than 8 characters)
     * @throws InvalidEmailException: if email address is not valid and does not fit email address syntax
     * Constructor
     */

    //Email format check from https://stackoverflow.com/questions/42266148/email-validation-regex-java
    public User(String username, String email, String phoneNumber, float rating) throws UsernameTooLongException, InvalidEmailException {
        if (username.length() > 8) {
            throw new UsernameTooLongException();
        }
        String regex = "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            throw new InvalidEmailException();
        }
        this.username=username;
        this.email=email;
        this.phoneNumber = phoneNumber;
        this.rating= rating;
    }

    /**
     *
     * @param Username: user's username
     * Constructor
     */

    public User(String Username) {}

    /**
     *
     * @return: the username of the user
     * Getter function.
     */

    public String getUsername(){
        return this.username;
    }

    //Username needs to be unique
    //Username needs to be at least 8 characters (max length would be good too)
    public void setUsername(String name) throws UsernameTooLongException {
        this.username = name;
    }

    /**
     *
     * @return email address of the user
     * Getter function
     */

    public String getEmail(){
        return this.email;
    }

    /**
     *
     * @param mail: a valid email address
     * @throws InvalidEmailException: if the email address is invalid
     * Setter
     */

    //Make sure email is in the right format
    public void setEmail(String mail) throws InvalidEmailException {
        this.email = mail;
    }

    /**
     *
     * @param number: phone # of a user
     *  Setter
     */

    //make sure format is in the right format
    public void setPhoneNumber(String number) { this.phoneNumber = number; }

    /**
     *
     * @return phone number of user.
     * @see  public void setPhoneNumber(String number)
     * Getter
     */
    
    public String getPhoneNumber() { return this.phoneNumber; }

    /**
     * @return rating of user.
     * @see  public void setRating(float rate)
     * Getter
     */

    public float getRating(){
        return this.rating;
    }

    /**
     *
     * @param rate: rating of the user
     * Setter
     */

    //make sure rating is in the corrrect range
    public void setRating(float rate) {
        this.rating = rate;
    }

    /**
     *
     * @param task: a task to be bid on by the user
     */

    public void bidTask(Task task) {
        this.biddedTask.add(task);
    }

    /**
     *
     * @return the entire Tasklist of a user
     */

    public Tasklist getTask() {
        return this.tasks;
    }

    /**
     *
     * @param task: a task to be added to the TaskList of a user
     */

    public void addTask(Task task) {
        this.tasks.add(task);
    }

    /**
     *
     * @return: 0, to return true
     */

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     *
     * @param out: Parcel type object (OUT)
     * @param flags: any flags
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mData);
    }


    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    /**
     *
     * @param in: parcel type object (IN)
     */
    private User(Parcel in) {
        mData = in.readInt();
    }
}
