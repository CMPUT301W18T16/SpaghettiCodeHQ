package com.example.peter.mercenary;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.*;

/**
 * Created by minci on 2018/2/25.
 */
public class BidListTest {
    @Test
    public void add() throws Exception {
        BidList bidsList = new BidList();
        Bid myBid = new Bid("test_user_name", (float) 19.99);
        assertFalse(Arrays.asList(bidsList).contains(myBid));
        bidsList.add(myBid);
        System.out.println("Test user name is: " + bidsList.getBid(0).getUsername() + '\n');
        System.out.println("Test bid value is: " + bidsList.getBid(0).getValue() + '\n');
        /*TODO: contains method does not work on objects; build your own contains method*/
        assertTrue(Arrays.asList(bidsList).contains(myBid));
    }

    @Test
    public void hasBid() throws Exception {
        BidList bidsList = new BidList();
        Bid myBid = new Bid("test_user_name", (float) 19.99);
        assertFalse(bidsList.hasBid(myBid));
        bidsList.add(myBid);
        assertTrue(bidsList.hasBid(myBid));
    }

    @Test
    public void getBid() throws Exception {
        BidList bidsList = new BidList();
        Bid myBid = new Bid("test_user_name", (float) 19.99);
        bidsList.add(myBid);
        assertEquals(bidsList.getBid(0), myBid);
    }

    @Test
    public void delBid() throws Exception {
        BidList bidsList = new BidList();
        Bid myBid = new Bid("test_user_name", (float) 19.99);
        assertFalse(bidsList.hasBid(myBid));
        bidsList.add(myBid);
        assertTrue(bidsList.hasBid(myBid));
        bidsList.delBid(myBid);
        assertFalse(bidsList.hasBid(myBid));
    }
}