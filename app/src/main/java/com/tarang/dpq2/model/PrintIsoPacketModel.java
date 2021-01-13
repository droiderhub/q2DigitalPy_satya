package com.tarang.dpq2.model;

public class PrintIsoPacketModel {

    private String isoRequestPacket;
    private String isoResponsePacket;
    boolean enablePrint;

    public String getIsoRequestPacket() {
        return isoRequestPacket;
    }

    public void setIsoRequestPacket(String isoRequestPacket) {
        this.isoRequestPacket = isoRequestPacket;
    }

    public String getIsoResponsePacket() {
        return isoResponsePacket;
    }

    public void setIsoResponsePacket(String isoResponsePacket) {
        this.isoResponsePacket = isoResponsePacket;
    }

    public boolean isEnablePrint() {
        return enablePrint;
    }

    public void setEnablePrint(boolean enablePrint) {
        this.enablePrint = enablePrint;
    }
}
