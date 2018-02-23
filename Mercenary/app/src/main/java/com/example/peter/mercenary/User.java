package com.example.peter.mercenary;

import java.util.List;

/**
 * Created by peter on 2018-02-22.
 */

public class User {
    private String Username;
    private String Password;
    private String email;
    private int rating;

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
