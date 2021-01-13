package com.tarang.dpq2.base.terminal_sdk.device;

import android.content.Context;

import com.cloudpos.DeviceException;
import com.cloudpos.POSTerminal;
import com.cloudpos.pinpad.extend.PINPadExtendDevice;
import com.cloudpos.printer.PrinterDevice;
import com.cloudpos.sdk.util.StringUtility;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.jpos_class.ByteConversionUtils;


public class SDKDevice {

   // private static final String K21_DRIVER_NAME = "com.newland.me.K21Driver";
//	private static WaitThreat waitNull = new WaitThreat();


    private static Context context;
    public static SDKDevice sdkDevice;
    private boolean failedListner = false;
    private PINPadExtendDevice device = null;
    private boolean deviceOpened = false;
    private String ksn = "";

//	int retry = 0;

    private SDKDevice(Context context) {
        device = (PINPadExtendDevice) POSTerminal.getInstance(context)
                .getDevice("cloudpos.device.pinpad");
        Logger.v("DEVICE CREATED");
        openDevice();
    }

    public static SDKDevice getInstance(Context context) {
        if (sdkDevice == null) {
            synchronized (SDKDevice.class) {
                if (sdkDevice == null) {
                    sdkDevice = new SDKDevice(context);
                }
            }
        }
        SDKDevice.context = context;
        return sdkDevice;
    }

    public void incrementKSN() {
        closeDevice();
        sdkDevice = null;
    }

    public PINPadExtendDevice getDeviceConnectPin() {
        return device;
    }

    public void openDevice() {
        try {
            Logger.v("Device Status - Open");
            device.open();
            deviceOpened = true;
            byte[] dataKey = ByteConversionUtils.hexStringToByteArray("18ffff00362815000000");
            int val = device.getDukptStatus(1, dataKey);
            String ksnVal = StringUtility.ByteArrayToString(dataKey, val);
            Logger.v("GET KSN --" + ksnVal.trim().replace(" ", ""));
            ksn = ksnVal.trim().replace(" ", "");
        } catch (DeviceException e) {
            Logger.v("Device open Exception");
            e.printStackTrace();
        }
    }

    public String getMyCurrentKSN() {
        String ksn = "";
        try {
            byte[] dataKey = ByteConversionUtils.hexStringToByteArray("18ffff00362815000000");
            int val = device.getDukptStatus(1, dataKey);
            String ksnVal = StringUtility.ByteArrayToString(dataKey, val);
           // Logger.v("GET KSN --" + ksnVal.trim().replace(" ", ""));
            ksn = ksnVal.trim().replace(" ", "");
            Logger.v("getMyCurrentKSN()_inside --" + ksn);
        } catch (DeviceException e) {
            Logger.v("Device open Exception");
        }
        return ksn;
    }

    public void resetOpen() {
        Logger.v("Device Status - Reset");
        closeDevice();
        openDevice();
    }

    public void closeDevice() {
        if (device != null && deviceOpened) {
            try {
                Logger.v("Device Status - close");
                deviceOpened = false;
                device.close();
            } catch (DeviceException e) {
                Logger.v("Device Status - close e -"+e.getMessage());
                e.printStackTrace();
            }
        }else{
            Logger.v("Device Status - close device null");
        }
    }

    public String getKSN() {
        Logger.v("Current KSN --"+ksn);
        return ksn;
    }

    public void setKSN(String ksn1) {
        ksn = ksn1;
        Logger.v("KSN --" + ksn1);
    }

    public PrinterDevice getPrinter() {
        return (PrinterDevice) POSTerminal.getInstance(context).getDevice("cloudpos.device.printer");
    }
}
