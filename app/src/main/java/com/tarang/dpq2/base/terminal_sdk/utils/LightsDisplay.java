package com.tarang.dpq2.base.terminal_sdk.utils;

import android.content.Context;

import com.cloudpos.DeviceException;
import com.cloudpos.POSTerminal;
import com.cloudpos.led.LEDDevice;
import com.tarang.dpq2.base.Logger;

public class LightsDisplay {
    Context context;
    private LEDDevice device = null;
    final int redLight =3;
    final int orangeLight =1;
    final int greenLight =2;
    final int blueLight =0;

    private LEDDevice device0 = null;
    private LEDDevice device1 = null;
    private LEDDevice device2 = null;
    private LEDDevice device3 = null;


    public LightsDisplay(Context context) {
        this.context = context;
        if (device == null) {
            device = (LEDDevice) POSTerminal.getInstance(context)
                    .getDevice("cloudpos.device.led");
        }

    }


    public void showBlueLight() {
        hideSingleLight();
        hideTwoLight();
       // hideFourLights();

        try {
            device.open(blueLight);
            device.turnOn();
            Logger.v("Light_blue_On --");
        } catch (DeviceException e) {
            e.printStackTrace();
            Logger.v("blue_light_catchblock");
        }

    }

    public void showTwoLights() {
        hideSingleLight();
        hideTwoLight();
      //  hideFourLights();

        device2 = (LEDDevice) POSTerminal.getInstance(context)
                .getDevice("cloudpos.device.led",2);
        device3 = (LEDDevice) POSTerminal.getInstance(context)
                .getDevice("cloudpos.device.led",3);
        Logger.v("show_all_light");

        try {

            device2.open();
            device2.turnOn();

            device3.open();
            device3.turnOn();
            Logger.v("two_light --" );
        } catch (Exception e) {
            e.printStackTrace();
            Logger.v("two_light_catchblock");
        }

    }

    public void showOrangeLight(){
        hideSingleLight();
        hideTwoLight();
       // hideFourLights();
        try {
            device.open(orangeLight);
            device.turnOn();
            Logger.v("orange_light --" );
        } catch (DeviceException e) {
            e.printStackTrace();
            Logger.v("orange_catchblock");
        }
    }

    public void showGreenLights() {
        hideSingleLight();
        hideTwoLight();
     //   hideFourLights();

        try {
            device.open(greenLight);
            device.turnOn();
            Logger.v("Light_green_On --" );
        } catch (DeviceException e) {
            e.printStackTrace();
            Logger.v("Light_green_On_catchblock");
        }
    }

    public void showRedLight() {
        hideSingleLight();
        hideTwoLight();
      //  hideFourLights();
        try {
            device.open(redLight);
            device.turnOn();
            Logger.v("red_light --" );
        } catch (DeviceException e) {
            e.printStackTrace();
            Logger.v("red_light_catchblock");
        }
    }


    public void blinkAllOneByOneLights() {
        hideSingleLight();
        hideTwoLight();
     //   hideFourLights();
        try {
            device.open(greenLight);
            //  device.turnOn();
            device.startBlink(100, 100, 10);
            device.turnOff();
            device.close();
            device.open(redLight);
            // device.turnOn();
            device.startBlink(100, 100, 10);
            device.turnOff();
            device.close();
            device.open(orangeLight);
            // device.turnOn();
            device.startBlink(100, 100, 10);
            device.turnOff();
            device.close();
            device.open(blueLight);
            // device.turnOn();
            device.startBlink(100, 100, 10);
            device.turnOff();
            device.close();
            Logger.v("all_light --" );
        } catch (DeviceException e) {
            e.printStackTrace();
            Logger.v("all_light_catchblock");
        }
    }
    public void blinkRedLight(){
        hideSingleLight();
        hideTwoLight();
        hideFourLights();
        try {
            device.open(redLight);
            device.blink(100,100,100);
        }catch (Exception e){

        }
    }

    public void hideSingleLight() {
        try {
            device.turnOff();
            device.close();
            Logger.v("light_device_close");
        } catch (DeviceException e) {
            e.printStackTrace();
        }

    }
    public void hideTwoLight(){
        try {
            device2 = (LEDDevice) POSTerminal.getInstance(context)
                    .getDevice("cloudpos.device.led",2);
            device3 = (LEDDevice) POSTerminal.getInstance(context)
                    .getDevice("cloudpos.device.led",3);
            device2.turnOff();
            device2.close();

            device3.turnOff();
            device3.close();

        }catch (Exception e){
            Logger.v("hideTwoLight_catchblock");
        }
    }

    public void showAllLights(){
        hideSingleLight();
        hideTwoLight();
       // hideFourLights();
        device0 = (LEDDevice) POSTerminal.getInstance(context)
                .getDevice("cloudpos.device.led",0);
        device1 = (LEDDevice) POSTerminal.getInstance(context)
                .getDevice("cloudpos.device.led",1);
        device2 = (LEDDevice) POSTerminal.getInstance(context)
                .getDevice("cloudpos.device.led",2);
        device3 = (LEDDevice) POSTerminal.getInstance(context)
                .getDevice("cloudpos.device.led",3);
        Logger.v("show_all_light");

        try {
            device0.open();
            device0.turnOn();

            device1.open();
            device1.turnOn();

            device2.open();
            device2.turnOn();

            device3.open();
            device3.turnOn();
            Logger.v("show_all_light_success");
            Thread.sleep(1000);
            hideFourLights();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.v("show_all_light_failure-----"+e.getMessage());
        }
    }

    public void hideFourLights(){
        device0 = (LEDDevice) POSTerminal.getInstance(context)
                .getDevice("cloudpos.device.led",0);
        device1 = (LEDDevice) POSTerminal.getInstance(context)
                .getDevice("cloudpos.device.led",1);
        device2 = (LEDDevice) POSTerminal.getInstance(context)
                .getDevice("cloudpos.device.led",2);
        device3 = (LEDDevice) POSTerminal.getInstance(context)
                .getDevice("cloudpos.device.led",3);
        try {
            device0.turnOff();
            device0.close();

            device1.turnOff();
            device1.close();

            device2.turnOff();
            device2.close();

            device3.turnOff();
            device3.close();
        } catch (DeviceException e) {
            e.printStackTrace();
        }
    }

}
