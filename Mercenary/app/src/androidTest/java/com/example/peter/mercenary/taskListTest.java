package com.example.peter.mercenary;

import android.test.ActivityInstrumentationTestCase2;
import org.junit.Test;
import java.util.ArrayList;

/**
 * Created by Shardul Shah on 2018-02-25
 */

public class taskListTest extends ActivityInstrumentationTestCase2 
{

    public taskListTest() 
    {
        super(MainActivity.class);
    }

    @Test
    public void add() throws Exception 
    {
    	Tasklist taskList = new Tasklist();
    	Task myTask = new Task("Shovel snow", "Shovelling snow at my house", "open");
    	assertFalse(taskList.contains(myTask));
    	taskList.add(myTask);
    	assertTrue(taskList.contains(myTask));
    }

    public void hasTask() throws Exception
    {
    	Tasklist taskList = new Tasklist();
    	Task myTask = new Task("Shovel snow", "Shovelling snow at my house", "open");
    	assertFalse(taskList.hasTask(myTask));
    	taskList.add(myTask);
    	assertTrue(taskList.hasTask(myTask));
    }

    public void getTask() throws Exception
    {
    	Tasklist taskList = new Tasklist();
    	Task myTask = new Task("Shovel snow", "Shovelling snow at my house", "open");
    	taskList.add(myTask);
    	assertEquals(taskList.getTask(0), myTask);

    }

    public void delTask() throws Exception
    {
    	Tasklist taskList = new Tasklist();
    	Task myTask = new Task("Shovel snow", "Shovelling snow at my house", "open");
    	taskList.add(myTask);
    	assertTrue(taskList.contains(myTask));
    	taskList.delTask(myTask);
    	assertFalse(taskList.contains(myTask));

    }

  }