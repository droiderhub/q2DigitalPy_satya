package com.tarang.dpq2.view.activities;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.tarang.dpq2.R;
import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.baseactivities.BaseActivity;

import java.lang.reflect.Method;

public class SwitchConnectionActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_connection);
        setTitle(getString(R.string.switch_connection));
        switchConnection();
    }

    private void switchConnection() {
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        boolean switchConnection = AppManager.getInstance().getConnectonMode();
        ((RadioButton) findViewById(R.id.radioGPRS)).setChecked(switchConnection);
        ((RadioButton) findViewById(R.id.radioWifi)).setChecked(!switchConnection);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rbtGPRS = group.findViewById(R.id.radioGPRS);
                RadioButton rbtWifi = group.findViewById(R.id.radioWifi);
                if (rbtGPRS.isChecked()) {
                    if (checkSimStatus(SwitchConnectionActivity.this)) {
                        AppManager.getInstance().setConnectonMode(true);
                        saveConnection();
                    } else {
                        rbtWifi.setChecked(true);
                        Toast.makeText(SwitchConnectionActivity.this, getString(R.string.sim_not_present), Toast.LENGTH_SHORT).show();
                    }
                } else if (rbtWifi.isClickable()) {
                    AppManager.getInstance().setConnectonMode(false);
                    saveConnection();
                }
            }
        });
    }

    public void saveConnection(){
        boolean switchConnection = AppManager.getInstance().getConnectonMode();
        enableWifi(SwitchConnectionActivity.this, !switchConnection);
        enableMobileData(SwitchConnectionActivity.this, switchConnection);
//        if (!switchConnection)
//            Toast.makeText(SwitchConnectionActivity.this, getString(R.string.connection_switched_to_wifi), Toast.LENGTH_SHORT).show();
//        else
//            Toast.makeText(SwitchConnectionActivity.this, getString(R.string.connection_switched_to_mobile_data), Toast.LENGTH_SHORT).show();
    }

    public boolean checkSimStatus(Context context) {
        TelephonyManager telMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telMgr.getSimState();
        if (simState == TelephonyManager.SIM_STATE_READY)
            return true;
        return false;
    }

    public void enableWifi(Context context, boolean enable) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(enable);
    }

    public void enableMobileData(Context context, boolean mobileDataEnabled) {
        try {
            TelephonyManager telephonyService = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            Method setMobileDataEnabledMethod = telephonyService.getClass().getDeclaredMethod("setDataEnabled", boolean.class);
            setMobileDataEnabledMethod.invoke(telephonyService, mobileDataEnabled);
        } catch (Exception ex) {
            Logger.v("Exception --" + ex.getMessage());
        }

    }


}
