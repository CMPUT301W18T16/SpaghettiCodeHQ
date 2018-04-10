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

import static java.lang.System.in;
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
        Task testTask = new Task("title", "test desc", new LatLng(0,0), "bidded", "sampleUsrID_r56yh", "sampleuserName", new ArrayList<String>());
        assert(testTask.getId() == null);
        testTask.setId("taskId_tdr5invhcbg");
        assert(testTask.getId() != null);

    }

    @Test
    public void setId() throws Exception {
        Task testTask = new Task("title", "test desc", new LatLng(0,0), "bidded", "sampleUsrID_r56yh", "sampleuserName", new ArrayList<String>());
        assert(testTask.getId() == null);
        testTask.setId("taskId_t4rydgv");
        assert(testTask.getId() != null);
    }

    @Test
    public void addPicture() throws Exception {
        String testImgStr = "qqqaaa";
        ArrayList<String> testimgStrList = new ArrayList<String>();
        assertTrue(testimgStrList.get(0) == null);
        testimgStrList.add(testImgStr);
        assertFalse(testimgStrList.get(0) == null);
        assertTrue(testimgStrList.get(0) == testImgStr);
    }

    @Test
    public void addGeo() throws Exception {

        LatLng coord = new LatLng(116.7,3.6);
        assertTrue(coord != null);

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
    public void getPhoto() throws Exception {
        ArrayList<String> imgArray =  new ArrayList<String>();
        assert(imgArray.get(0) == null);
        Task testTask = new Task("title", "test desc", new LatLng(0,0), "bidded", "sampleUsrID_r56yh", "sampleuserName", );
        assert(testTask.getPhoto() == null);
        imgArray.add("test_img_str");
        testTask.setPhoto(imgArray);
        assert(testTask.getPhoto() != null);

    }

    @Test
    public void getTitle() throws Exception {
        Task testTask = new Task("title", "test desc", new LatLng(0,0), "bidded", "sampleUsrID_r56yh", "sampleuserName", );

        assert(testTask.getTitle() != null);
    }

    @Test
    public void getDescription() throws Exception {
        Task testTask = new Task("title", "test desc", new LatLng(0,0), "bidded", "sampleUsrID_r56yh", "sampleuserName", );

        assert(testTask.getDescription() != null);
    }

    @Test
    public void getBids() throws Exception {
    }

}