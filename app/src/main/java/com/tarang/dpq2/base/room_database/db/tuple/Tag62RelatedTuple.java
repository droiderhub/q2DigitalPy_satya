package com.tarang.dpq2.base.room_database.db.tuple;

import androidx.room.ColumnInfo;

public class Tag62RelatedTuple {

    @ColumnInfo(name = "startTimeTransaction")
    private String startTimeTransaction = "";

    @ColumnInfo(name = "endTimeTransaction")
    private String endTimeTransaction = "";

    @ColumnInfo(name = "startTimeConnection")
    private String startTimeConnection = "";

    @ColumnInfo(name = "endTimeConnection")
    private String endTimeConnection = "";

    @ColumnInfo(name = "retriRefNo37")
    private String retriRefNo37 = "";

    public String getStartTimeTransaction() {
        return startTimeTransaction;
    }

    public void setStartTimeTransaction(String startTimeTransaction) {
        this.startTimeTransaction = startTimeTransaction;
    }

    public String getEndTimeTransaction() {
        return endTimeTransaction;
    }

    public void setEndTimeTransaction(String endTimeTransaction) {
        this.endTimeTransaction = endTimeTransaction;
    }

    public String getStartTimeConnection() {
        return startTimeConnection;
    }

    public void setStartTimeConnection(String startTimeConnection) {
        this.startTimeConnection = startTimeConnection;
    }

    public String getEndTimeConnection() {
        return endTimeConnection;
    }

    public void setEndTimeConnection(String endTimeConnection) {
        this.endTimeConnection = endTimeConnection;
    }

    public String getRetriRefNo37() {
        return retriRefNo37;
    }

    public void setRetriRefNo37(String retriRefNo37) {
        this.retriRefNo37 = retriRefNo37;
    }
}
