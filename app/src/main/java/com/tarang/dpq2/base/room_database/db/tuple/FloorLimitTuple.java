package com.tarang.dpq2.base.room_database.db.tuple;

import androidx.room.ColumnInfo;

public class FloorLimitTuple {
    @ColumnInfo(name = "floorLimitEnabled")
    private boolean floorLimitEnabled;

    @ColumnInfo(name = "terminalFloorLimit")
    private String terminalFloorLimit = "";

    @ColumnInfo(name = "terminalFloorLimitFallback")
    private String terminalFloorLimitFallback = "";


    public boolean isFloorLimitEnabled() {
        return floorLimitEnabled;
    }

    public void setFloorLimitEnabled(boolean floorLimitEnabled) {
        this.floorLimitEnabled = floorLimitEnabled;
    }

    public String getTerminalFloorLimit() {
        return terminalFloorLimit;
    }

    public void setTerminalFloorLimit(String terminalFloorLimit) {
        this.terminalFloorLimit = terminalFloorLimit;
    }

    public String getTerminalFloorLimitFallback() {
        return terminalFloorLimitFallback;
    }

    public void setTerminalFloorLimitFallback(String terminalFloorLimitFallback) {
        this.terminalFloorLimitFallback = terminalFloorLimitFallback;
    }
}
