package com.tarang.dpq2.base.room_database.db.tuple;

import androidx.room.ColumnInfo;

public class BinRangeTuple {

    @ColumnInfo(name = "cardIndicator")
    private String cardIndicator = "";

    @ColumnInfo(name = "binRanges")
    private String binRanges = "";

    public String getCardIndicator() {
        return cardIndicator;
    }

    public void setCardIndicator(String cardIndicator) {
        this.cardIndicator = cardIndicator;
    }

    public String getBinRanges() {
        return binRanges;
    }

    public void setBinRanges(String binRanges) {
        this.binRanges = binRanges;
    }
}
