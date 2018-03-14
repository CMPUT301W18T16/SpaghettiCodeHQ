package com.example.peter.mercenary;

import java.util.ArrayList;

/**
 * Created by peter on 2018-02-23.
 */
//Custom ArrayList type BidList
public class BidList {

    private ArrayList<Bid> bids = new ArrayList<Bid>();

    public void add(Bid bid) {

        if (hasBid(bid)) {
            throw new IllegalArgumentException("Duplicate bid found!");
        }
        bids.add(bid);


    }

    public boolean hasBid(Bid bid) {

        //return Boolean.FALSE;
        return bids.contains(bid);

    }

    public Bid getBid(int index) {

        return bids.get(index);

    }

    public void delBid(Bid bid) {

        bids.remove(bid);
    }

    public int size(){
        return bids.size();
    }

}
