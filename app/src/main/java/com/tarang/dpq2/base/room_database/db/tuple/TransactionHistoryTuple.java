package com.tarang.dpq2.base.room_database.db.tuple;

import androidx.room.ColumnInfo;

public class TransactionHistoryTuple {

    @ColumnInfo(name = "uid")
    private int uid;
    @ColumnInfo(name = "iccCardSystemRelatedData55")
    private String iccCardSystemRelatedData55;
    @ColumnInfo(name = "retriRefNo37")
    private String retriRefNo37;
    @ColumnInfo(name = "responseCode39")
    private String responseCode39;
    @ColumnInfo(name = "transmissionDateTime7")
    private String transmissionDateTime7;
    @ColumnInfo(name = "amtTransaction4")
    private String amtTransaction4;
    @ColumnInfo(name = "authIdResCode38")
    private String authIdResCode38;
    @ColumnInfo(name = "startTimeTransaction")
    private String startTimeTransaction = "dd/MM/yyyy HH:mm:ss";

    public String getStartTimeTransaction() {
        return startTimeTransaction;
    }

    public void setStartTimeTransaction(String startTimeTransaction) {
        this.startTimeTransaction = startTimeTransaction;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getIccCardSystemRelatedData55() {
        return iccCardSystemRelatedData55;
    }

    public void setIccCardSystemRelatedData55(String iccCardSystemRelatedData55) {
        this.iccCardSystemRelatedData55 = iccCardSystemRelatedData55;
    }

    public String getRetriRefNo37() {
        return retriRefNo37;
    }

    public void setRetriRefNo37(String retriRefNo37) {
        this.retriRefNo37 = retriRefNo37;
    }

    public String getResponseCode39() {
        return responseCode39;
    }

    public void setResponseCode39(String responseCode39) {
        this.responseCode39 = responseCode39;
    }

    public String getTransmissionDateTime7() {
        return transmissionDateTime7;
    }

    public void setTransmissionDateTime7(String transmissionDateTime7) {
        this.transmissionDateTime7 = transmissionDateTime7;
    }

    public String getAmtTransaction4() {
        return amtTransaction4;
    }

    public void setAmtTransaction4(String amtTransaction4) {
        this.amtTransaction4 = amtTransaction4;
    }

    public String getAuthIdResCode38() {
        return authIdResCode38;
    }

    public void setAuthIdResCode38(String authIdResCode38) {
        this.authIdResCode38 = authIdResCode38;
    }
}
