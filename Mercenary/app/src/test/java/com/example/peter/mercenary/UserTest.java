package com.example.peter.mercenary;

import android.test.ActivityInstrumentationTestCase2;
import org.junit.Test;
import java.util.ArrayList;

/**
 * Created by Melissa on 2018-02-25.
 */
public class UserTest extends ActivityInstrumentationTestCase2 {

    public UserTest() {
        super(MainActivity.class);
    }

    @Test
    public void setUsername() throws Exception {
        ArrayList<User> UserList = new ArrayList<>();
        User newUser = new User("HelloWorld");
        assertFalse(UserList.contains(newUser));
        UserList.add(newUser);
        assertTrue(UserList.contains(newUser));
    }


/*
    password no longer needed
    @Test
    public void setPassword() throws Exception {
        User newUser = new User("HelloWorld");
        newUser.setPassword("hunter2");
        assertTrue(newUser.getPassword().equals("hunter2"));
    }
*/
    @Test
    public void setEmail() throws Exception {
        User newUser = new User("HelloWorld");
        newUser.setEmail("myEmail@gmail.com");
        assertTrue(newUser.getEmail().equals("myEmail@gmail.com"));
    }

    @Test
    public void setPhoneNumber() throws Exception {
        User newUser = new User("HelloWorld");
        newUser.setPhoneNumber("5551234567");
        assertTrue(newUser.getPhoneNumber().equals("5551234567"));
    }

    @Test
    public void setRating() throws Exception {
        //int test
        User newUser = new User("HelloWorld");
        newUser.setRating(4);
        assertTrue(newUser.getRating() == 4.0);

        //float test
        newUser.setRating(4.15f);
        assertTrue(newUser.getRating() == 4.15);
    }
}