package com.example.peter.mercenary;

import java.util.List;

/**
 * Created by peter on 2018-02-22.
 */

public class User {
    protected String username;
    protected String password;
    protected String email;
    protected String phoneNumber;
    protected float rating;

    public User(String username, String password, String email, float rating){
        this.username=username;
        this.password=password;
        this.email=email;
        this.rating= rating;
    }

    public User(String Username) {

    }

    public String getUsername(){
        return this.username;
    }

    //Username needs to be unique
    //Username needs to be at least 8 characters (max length would be good too)
    public void setUsername(String name) {
        this.username = name;
    }

    public void setPassword(String pass){
        this.password = pass;

    }

    public String getPassword(){
        return this.password;

    }
    public String getEmail(){
        return this.email;
    }

    //Make sure email is in the right format
    public void setEmail(String mail) {
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
}
