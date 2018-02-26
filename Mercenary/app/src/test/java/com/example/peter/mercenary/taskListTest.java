package com.example.peter.mercenary;

import android.test.ActivityInstrumentationTestCase2;
import org.junit.Test;
import java.lang.*;
import java.util.Arrays;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by Shardul Shah on 2018-02-25
 */

public class taskListTest
{

    //public taskListTest()
    //{
  //      super(MainActivity.class);
  //  }

    @Test
    public void add() throws Exception 
    {
    	Tasklist taskList = new Tasklist();
    	Task myTask = new Task("Shovel snow", "Shovelling snow at my house", "open");
    	assertFalse(Arrays.asList(taskList).contains(myTask));
    	taskList.add(myTask);
        /*TODO: contains method does not work on objects; build your own contains method*/
    	assertTrue(Arrays.asList(taskList).contains(myTask));
    }

    @Test
    public void hasTask() throws Exception
    {
    	Tasklist taskList = new Tasklist();
    	Task myTask = new Task("Shovel snow", "Shovelling snow at my house", "open");
    	assertFalse(taskList.hasTask(myTask));
    	taskList.add(myTask);
    	assertTrue(taskList.hasTask(myTask));
    }

    @Test
    public void getTask() throws Exception
    {
    	Tasklist taskList = new Tasklist();
    	Task myTask = new Task("Shovel snow", "Shovelling snow at my house", "open");
    	taskList.add(myTask);
    	assertEquals(taskList.getTask(0), myTask);

    }

    @Test
    public void delTask() throws Exception
    {
    	Tasklist taskList = new Tasklist();
    	Task myTask = new Task("Shovel snow", "Shovelling snow at my house", "open");
    	taskList.add(myTask);
    	assertTrue(Arrays.asList(taskList).contains(myTask));
    	taskList.delTask(myTask);
    	assertFalse(Arrays.asList(taskList).contains(myTask));

    }

  }