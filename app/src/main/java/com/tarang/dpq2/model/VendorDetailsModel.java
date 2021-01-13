package com.tarang.dpq2.model;

public class VendorDetailsModel {
    private String vendorId;  //2 char 1 bytes
    private String vendorTerminalType;  //2 char 1 bytes
    private String vendorTerminalSerialNumber; //6char 3 bytes


    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorTerminalType() {
        return vendorTerminalType;
    }

    public void setVendorTerminalType(String vendorTerminalType) {
        this.vendorTerminalType = vendorTerminalType;
    }

    public String getVendorTerminalSerialNumber() {
        return vendorTerminalSerialNumber;
    }

    public void setVendorTerminalSerialNumber(String vendorTerminalSerialNumber) {
        this.vendorTerminalSerialNumber = vendorTerminalSerialNumber;
    }
}
