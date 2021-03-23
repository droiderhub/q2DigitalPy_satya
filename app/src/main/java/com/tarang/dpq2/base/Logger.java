package com.tarang.dpq2.base;

import android.util.Log;

import com.tarang.dpq2.base.jpos_class.ByteConversionUtils;

public class Logger {

    public static boolean checkLogEnabled(){
        if(!AppInit.HITTING_LIVE_SERVER)
            return true;
        else {
            return AppManager.getInstance().isDebugEnabled1();
        }
    }

    public static void v(String s){
        if(checkLogEnabled()) {
            Log.v("responce", "Tag Msg --" + s);
        }
    }

    public static void v(String[] s){
        if(checkLogEnabled()) {
            v("Size --" + s.length);
            for (int i = 0; i < s.length; i++) {
                Log.v("responce", "Tag Msg -" + i + "-" + s[i]);
            }
        }
    }

    public static void v(String tag,String s){
        if(checkLogEnabled()) {
            Log.v("responce", "Tag Msg --" + s);
        }
    }

    public static void ve(String s){
        if(checkLogEnabled()) {
            Log.v("responce", "Tag Msg --" + s);
        }
    }
    public static void e(String tag,String s){
        if(checkLogEnabled()) {
            Log.e(tag, "Tag Msg --" + s);
        }
    }

    public static void showMessage(String tag , int s){
        if(checkLogEnabled()) {
            Log.v("responce", "tag --- " + tag + " \nTag Msg --" + s);
        }
    }

    public static void bytArray(String tag,byte[] isoBufferEchoResponse){
        String hexa1 = ByteConversionUtils.byteArrayToHexString(isoBufferEchoResponse, isoBufferEchoResponse.length, false);
        Logger.v(tag+"-"+hexa1);
    }

    public static void v(byte[] isoBufferEchoResponse){
        if(checkLogEnabled()) {
            String hexa1 = ByteConversionUtils.byteArrayToHexString(isoBufferEchoResponse, isoBufferEchoResponse.length, false);
            Logger.v("byte ---" + hexa1);
        }
    }

    public static void vv(byte[] isoBufferEchoResponse){
        if(checkLogEnabled()) {
            String hexa1 = ByteConversionUtils.byteArrayToHexString(isoBufferEchoResponse, isoBufferEchoResponse.length, false);
            Logger.v("byte ---" + hexa1);
            String result2 = ByteConversionUtils.convertHexToString(hexa1);
            Logger.v("RESULT --" + result2);
        }
    }

}
