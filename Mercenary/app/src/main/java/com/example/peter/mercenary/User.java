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

    public void setUsername(String name) {
        this.username = name;
    }

    private void setPassword(String pass){
        this.password = pass;

    }

    private String getPassword(){
        return this.password;

    }
    public String getEmail(){
        return this.email;
    }

    public void setEmail(String mail) {
        this.email = mail;
    }
    
    public void setPhoneNumber(String number) { this.phoneNumber = number; }
    
    public String getPhoneNumber() { return this.phoneNumber; }

    public float getRating(){
        return this.rating;
    }

    public void setRating(int rate) {
        this.rating = rate;
    }



}
