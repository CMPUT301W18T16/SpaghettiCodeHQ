package com.example.peter.mercenary;

import android.media.Image;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.Object;
import java.io.File;
import java.io.IOException;
import org.junit.Test;
import java.io.InputStream;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by minci on 2018/3/15.
 */
public class TaskTest {

    @Test
    public void setTaskTitle() throws Exception {
        String taskTitle = null;
        LatLng coord = new LatLng(1,1);
        Task testTask = new Task(taskTitle, "test desc", coord, "bidded", "sampleUsrID_r56yh", "sampleuserName", new ArrayList<String>());
        assertFalse(testTask.getTitle() != null);
        testTask.setTitle("testTitle");
        assertTrue(testTask.getTitle() == "testTitle");
    }

    @Test
    public void getId() throws Exception {

    }

    @Test
    public void setId() throws Exception {
    }

    @Test
    public void addPicture() throws Exception {
        int imgSize;
        int sizeLimit = 65536;

        InputStream in = this.getClass().getClassLoader().getResourceAsStream("temple.gif");
        System.out.println(in.available());
        imgSize = in.available();
        assertNotNull(in);
        assertTrue(imgSize<sizeLimit);

    }

    @Test
    public void addGeo() throws Exception {
    }

    @Test
    public void modDescription() throws Exception {
    }

    @Test
    public void addBid() throws Exception {
    }

    @Test
    public void modTitle() throws Exception {
    }

    @Test
    public void delBid() throws Exception {
    }

    @Test
    public void getLowestBid() throws Exception {
    }

    @Test
    public void getPicture() throws Exception {
    }

    @Test
    public void getTitle() throws Exception {
    }

    @Test
    public void getDescription() throws Exception {
    }

    @Test
    public void getBids() throws Exception {
    }

}