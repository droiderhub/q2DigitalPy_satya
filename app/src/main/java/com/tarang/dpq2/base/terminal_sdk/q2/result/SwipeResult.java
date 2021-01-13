package com.tarang.dpq2.base.terminal_sdk.q2.result;

public class SwipeResult {

    String pan;
    String expiry;
    String serviceCode;
    String track2Data;

    public void setPan(String pan) {
        this.pan = pan;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getPan() {
        return pan;
    }

    public String getExpiry() {
        return expiry;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public String getTrack2Data() {
        return track2Data;
    }

   /* public void setTrack2Data(byte[] track2Data, int offset, int length)
    {
        if(   track2Data != null
                && track2Data.length > 0
                && (offset + length) < track2Data.length
        )
        {
            byte[] tmpData = new byte[length];
            System.arraycopy(track2Data, offset, tmpData, 0, length);
            this.track2Data = new String(tmpData);
        }
    }*/
    public void setTrack2Data(String track2Data)
    {
        this.track2Data =track2Data;

    }

    public SwipeResult() {
    }

    @Override
    public String toString() {
        return "SwipeResult{" +
                "pan='" + pan + '\'' +
                ", expiry='" + expiry + '\'' +
                ", serviceCode='" + serviceCode + '\'' +
                ", track2Data='" + track2Data + '\'' +
                '}';
    }
}
