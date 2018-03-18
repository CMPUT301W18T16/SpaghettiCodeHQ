package com.example.peter.mercenary;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by peter on 2018-02-22.
 */
public class User implements Parcelable{
    private String username;
    private String email;
    private String phoneNumber;
    private float rating;
    private Tasklist biddedTask;
    private Tasklist tasks;
    private int mData;

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

    public User(String Username) {}

    public String getUsername(){
        return this.username;
    }

    //Username needs to be unique
    //Username needs to be at least 8 characters (max length would be good too)
    public void setUsername(String name) throws UsernameTooLongException {
        this.username = name;
    }

    public String getEmail(){
        return this.email;
    }

    //Make sure email is in the right format
    public void setEmail(String mail) throws InvalidEmailException {
        this.email = mail;
    }

    //make sure format is in the right format
    public void setPhoneNumber(String number) { this.phoneNumber = number; }
    
    public String getPhoneNumber() { return this.phoneNumber; }

    public float getRating(){
        return this.rating;
    }

    //make sure rating is in the corrrect range
    public void setRating(float rate) {
        this.rating = rate;
    }

    public void bidTask(Task task) {
        this.biddedTask.add(task);
    }

    public Tasklist getTask() {
        return this.tasks;
    }

    public void addTask(Task task) {
        this.tasks.add(task);
    }

    @Override
    public int describeContents() {
        return 0;
    }

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

    private User(Parcel in) {
        mData = in.readInt();
    }
}
