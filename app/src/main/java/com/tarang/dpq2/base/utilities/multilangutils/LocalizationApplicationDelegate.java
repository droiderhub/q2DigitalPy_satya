package com.tarang.dpq2.base.utilities.multilangutils;

import android.app.Application;
import android.content.Context;


public class LocalizationApplicationDelegate {
    private Application application;

    public LocalizationApplicationDelegate(Application application) {
        this.application = application;
    }

    public void onConfigurationChanged(Context context) {
        LocalizationUtility.applyLocalizationContext(context);
    }

    public Context attachBaseContext(Context context) {
        return LocalizationUtility.applyLocalizationContext(context);
    }

    public Context getApplicationContext(Context applicationContext) {
        return LocalizationUtility.applyLocalizationContext(applicationContext);
    }
}
