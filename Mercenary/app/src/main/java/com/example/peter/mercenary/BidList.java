package com.example.peter.mercenary;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * @date: Created by peter on 2018-02-23. Modified by Shardul on 2018-03-18.
 * @author Peter
 * @version version 1.0
 * @see class Bid
 *
 */

//Custom ArrayList type BidList
public class BidList implements Parcelable {

    private ArrayList<Bid> bids = new ArrayList<>();

    /**
     *
     * @param bid: a user Bid
     * @throws: IllegalArgumentException: if user trying to add duplicate bid
     * @see: class Bid
     */
    public void add(Bid bid) {
        if (hasBid(bid)) {
            throw new IllegalArgumentException("Duplicate bid found!");
        }
        bids.add(bid);

    }

    /**
     * @param bid: a user Bid
     * @return: a Boolean. True if bidList has the bid, else False.
     * @see: class Bid
     */

    public boolean hasBid(Bid bid) {

        //return Boolean.FALSE;
        return bids.contains(bid);

    }

    /**
     * @param index: the index at which the Bid required is at in the BidList
     * @return: the Bid at index in the Bidlist
     * @see: class Bid
     */

    public Bid getBid(int index) {
        return bids.get(index);

    }

    /**
     * @param bid: a user Bid
     * @see: class Bid
     */

    public void delBid(Bid bid) {

        bids.remove(bid);
    }

    /**
     *
     * @return: returns how many total user Bids there are
     */
    public int size(){
        return bids.size();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(bids);
    }

    protected BidList(Parcel in) {
        bids = in.createTypedArrayList(Bid.CREATOR);
    }

    public static final Creator<BidList> CREATOR = new Creator<BidList>() {
        @Override
        public BidList createFromParcel(Parcel in) {
            return new BidList(in);
        }

        @Override
        public BidList[] newArray(int size) {
            return new BidList[size];
        }
    };
}