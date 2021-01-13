package com.tarang.dpq2.view.activities;

import androidx.annotation.RequiresApi;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.widget.TextView;

import com.tarang.dpq2.BuildConfig;
import com.tarang.dpq2.R;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.baseactivities.BaseActivity;

public class TerminalInfoActivity extends BaseActivity {

    TextView terminal_details_tv, application_details_tv, battery_status_tv,
            firmware_version_tv, manufacturer_tv, application_name_tv, application_version_tv, imie_one_tv;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminal_info);
        setTitle(getString(R.string.terminal_info));
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void init() {

//        securityModule = SDKDevice.getInstance(context).getSecurityModule();
//        deviceInfo = securityModule.getDeviceInfo();
        imie_one_tv = findViewById(R.id.imie_one_tv);
        manufacturer_tv = findViewById(R.id.manufacturer_tv);
        application_name_tv = findViewById(R.id.application_name_tv);
        application_version_tv = findViewById(R.id.application_version_tv);

        terminal_details_tv = findViewById(R.id.terminal_details_tv);
        application_details_tv = findViewById(R.id.application_details_tv);
        battery_status_tv = findViewById(R.id.battery_status_tv);
        firmware_version_tv = findViewById(R.id.firmware_version_tv);
        setView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setView() {
        firmware_version_tv.setText(getString(R.string.firmware_version) + " : " + getsystemPropertie(KERNEL_VERSION,KERNEL_VERSION));
        BatteryManager bm = (BatteryManager) getSystemService(BATTERY_SERVICE);
        int batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        battery_status_tv.setText(getString(R.string.battery_status) + " : " + String.valueOf(batLevel) + "%");

        application_details_tv.setText(getString(R.string.application_details) + " : " + BuildConfig.VERSION_NAME);
        // terminal_details_tv.setText(getString(R.string.serial_number) + " : " + getsystemPropertie(SYSTEM_VERSION,SYSTEM_VERSION));
        terminal_details_tv.setText(getString(R.string.serial_number) + " : " + Build.SERIAL);

        imie_one_tv.setText(getString(R.string.imei_number) + " : " + getDeviceIMEI());
        manufacturer_tv.setText(getString(R.string.manufacturer) + " : " + getString(R.string.app_name));
        application_name_tv.setText(getString(R.string.application_name) + " : " + getString(R.string.app_name));
        application_version_tv.setText(getString(R.string.application_version) + " : " + BuildConfig.VERSION_CODE);
    }

    public static String getsystemPropertie(String key, String defaultValue){
        Object propObj = null ;
        try {
            Class<?> systemProperties = Class.forName("android.os.SystemProperties");
            Logger.v("systemProperties -"+systemProperties.toString());
            propObj = systemProperties.getMethod("get", new Class[]{String.class, String.class}).invoke(systemProperties, new Object[]{key,"unknown"});
            Logger.v("bootloaderVersion"+propObj.getClass().toString());
        }catch (Exception e) {
            e.printStackTrace();
        }
        String prop = propObj.toString().toUpperCase();
        return prop;
    }

    public static final String KERNEL_VERSION  = "ro.wp.kernel.ver";

    public String getDeviceIMEI() {
        String deviceUniqueIdentifier = null;
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (null != tm) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return null;
                }
            }
            deviceUniqueIdentifier = tm.getDeviceId();
        }
        if (null == deviceUniqueIdentifier || 0 == deviceUniqueIdentifier.length()) {
            deviceUniqueIdentifier = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return deviceUniqueIdentifier;
    }


}
