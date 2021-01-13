package com.tarang.dpq2.view.activities;

import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;


import com.tarang.dpq2.R;
import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.GpsTracker;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.baseactivities.BaseActivity;
import com.tarang.dpq2.isopacket.SupportPacket;



public class GPSLocationActivity extends BaseActivity {

    TextView gps_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gpslocation);
        setTitle("");
        init();
    }
    public static String getLocation() {
        String latnlong = "";
        GpsTracker gpsTracker = new GpsTracker(AppManager.getContext());
        if (gpsTracker.canGetLocation()) {
            double longitude = gpsTracker.getLongitude();
            double latitude = gpsTracker.getLatitude();
            StringBuilder builder = new StringBuilder();

            if (latitude < 0) {
                builder.append("S");
            } else {
                builder.append("N");
            }

            String latitudeDegrees = Location.convert(Math.abs(latitude), Location.FORMAT_SECONDS);
            Logger.v("latitudeDegrees --"+latitudeDegrees);
            String[] latitudeSplit = latitudeDegrees.split(":");
            builder.append(addZeroWithCordinate(latitudeSplit[0],2));
//            builder.append("°");
            builder.append(addZeroWithCordinate(latitudeSplit[1],2));
//            builder.append("'");
            builder.append(addZeroWithCordinate(latitudeSplit[2],2));
//            builder.append("\"");

//            builder.append(" ");

            if (longitude < 0) {
                builder.append("W");
            } else {
                builder.append("E");
            }

            String longitudeDegrees = Location.convert(Math.abs(longitude), Location.FORMAT_SECONDS);
            Logger.v("latitudeDegrees --"+longitudeDegrees);
            String[] longitudeSplit = longitudeDegrees.split(":");
            builder.append(addZeroWithCordinate(longitudeSplit[0],3));
//            builder.append("°");
            builder.append(addZeroWithCordinate(longitudeSplit[1],2));
//            builder.append("'");
            builder.append(addZeroWithCordinate(longitudeSplit[2],2));
//            builder.append("\"");
            Logger.v("builder -"+builder.toString());
            latnlong = builder.toString();
        } else {
            gpsTracker.showSettingsAlert();
        }

        return latnlong.replaceAll(".","0");
    }
    private static String addZeroWithCordinate(String val,int i) {
        String s = ((int)Float.parseFloat(val))+"";
        Logger.v("val -"+s);
        if(s.trim().length() < i)
            return addZero(i - s.trim().length())+s;
        else if(s.trim().length() > i)
            return s.substring(0,i);
        else
            return s;
    }

    private static String addZero(int zero) {
        String s = "";
        for(int i=0;i<zero;i++ )
            s = s + "0";
        return s;
    }


    private void init() {
        gps_tv = findViewById(R.id.gps_tv);
        String gps = SupportPacket.getLocation(context);
        Logger.v("gps-----"+gps);
        gps_tv.setText(gps);


    }
}
