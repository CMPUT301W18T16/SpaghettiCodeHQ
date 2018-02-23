package com.example.peter.mercenary;

import java.util.List;

/**
 * Created by peter on 2018-02-22.
 */

public class User {
    protected String Username;
    protected String Password;
    protected String email;
    protected int rating;

    public User(String Username, String Password, String email, int rating){
        this.Username=Username;
        this.Password=Password;
        this.email=email;
        this.rating= rating;
    }

    public User(String Username) {

    }


    public String getUsername(){
        return this.Username;
    }
    public void setUsername(String name) {
        this.Username = name;
    }

    private void setPassword(String pass){
        this.Password = pass;

    }

    private String getPassword(){
        return this.Password;

    }
    public String getEmail(){
        return this.email;
    }

    public void setEmail(String mail) {
        this.email = mail;
    }

    public int getRating(){
        return this.rating;
    }

    public void setRating(int rate) {
        this.rating = rate;
    }



}
