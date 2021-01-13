package com.tarang.dpq2.view.activities;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.tarang.dpq2.BuildConfig;
import com.tarang.dpq2.R;
import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.MapperFlow;
import com.tarang.dpq2.base.baseactivities.BaseActivity;
import com.tarang.dpq2.base.utilities.Utils;
import com.tarang.dpq2.base.utilities.multilangutils.LocalizationActivityDelegate;
import com.tarang.dpq2.worker.LoadKeyWorker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;



public class SplashActivity extends BaseActivity {

    TextView version, versionCode;
    ImageView  mada_logo;
    LocalizationActivityDelegate localizationDelegate = new LocalizationActivityDelegate(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        LoadKeyWorker.emvKernelInit();
        Logger.v("LoadKeyWorker.emvKernelInit();");
        Logger.v("app_version---"+ BuildConfig.VERSION_NAME);
        Logger.v("os_version----"+ Utils.getAndroidVersion());
//        getLog();
        if(localizationDelegate.getLanguage(context).equals(new Locale("en"))){
            localizationDelegate.setLanguage(context, "en", "US");
        }
        version = findViewById(R.id.version);
        version.setText("App Version : "+ AppManager.getAppVersion(this));
        versionCode = findViewById(R.id.version_code);
        mada_logo = findViewById(R.id.mada_logo);
        versionCode.setText("Version Code : "+AppManager.getAppVersionCode(this));
        startTimer();
    }

    private void checkRunTimePermission() {
        String[] permission = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
        grantPermission(permission);
    }

    private void getLog() {
        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            StringBuilder log=new StringBuilder();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                log.append(line);
            }
            Logger.v("logcat----"+log.toString());
          //  appendLog(log.toString());
            //writeToSD(log.toString());
//            writeFileOnInternalStorage(context,"logcat",log.toString());
        }
        catch (IOException e) {}
    }

    public void setConnectState(final boolean isConnect){
        runOnUiThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                //TODO alert diaolod
                if(isConnect){

                }else {
                    finishAffinity();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        showTimer = false;
        super.onResume();
    }

    private void startTimer() {
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

//                if (millisUntilFinished <= 3000) {
//                    //after 3sec
//                    changeImage();
//                }

            }

            @Override
            public void onFinish() {
                MapperFlow.getInstance().moveToLandingPage(SplashActivity.this);
            }
        }.start();
    }



    public void grantPermission(String[] permissions) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M)
            return;

        List<String> requiredPermissions = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (checkSelfPermission(permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                requiredPermissions.add(permissions[i]);
            }
        }

        String[] stockArr = new String[requiredPermissions.size()];
        stockArr = requiredPermissions.toArray(stockArr);

        if (stockArr == null || stockArr.length == 0)
            return;
        Logger.v("request permission");
        requestPermissions(stockArr, 100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Logger.v("Permisssion result");
    }
}
