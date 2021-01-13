package com.tarang.dpq2.base;

import android.util.Log;

import com.tarang.dpq2.base.jpos_class.ByteConversionUtils;

public class Logger {

    public static void v(String s){
        Log.v("responce","Tag Msg --"+s);
    }

    public static void v(String[] s){
        v("Size --"+s.length);
        for(int i=0;i<s.length;i++){
            Log.v("responce","Tag Msg -"+i+"-"+s[i]);
        }
    }

    public static void v(String tag,String s){
        Log.v("responce" ,"Tag Msg --"+s);
    }

    public static void ve(String s){
        Log.v("responce" ,"Tag Msg --"+s);
    }
    public static void e(String tag,String s){
        Log.e(tag ,"Tag Msg --"+s);
    }

    public static void showMessage(String tag , int s){
        Log.v("responce","tag --- "+tag+" \nTag Msg --"+s);
    }

    public static void bytArray(String tag,byte[] isoBufferEchoResponse){
        String hexa1 = ByteConversionUtils.byteArrayToHexString(isoBufferEchoResponse, isoBufferEchoResponse.length, false);
        Logger.v(tag+"-"+hexa1);
    }

    public static void v(byte[] isoBufferEchoResponse){
        String hexa1 = ByteConversionUtils.byteArrayToHexString(isoBufferEchoResponse, isoBufferEchoResponse.length, false);
        Logger.v("byte ---"+hexa1);
    }

}
