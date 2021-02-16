package com.tarang.dpq2.base.baseactivities;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.CountDownTimer;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.tarang.dpq2.R;
import com.tarang.dpq2.base.AppInit;
import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.MapperFlow;
import com.tarang.dpq2.base.jpos_class.ConstantApp;
import com.tarang.dpq2.base.utilities.Utils;
import com.tarang.dpq2.base.utilities.multilangutils.LocalizationActivityDelegate;
import com.tarang.dpq2.base.utilities.multilangutils.OnLocaleChangedListener;
import com.tarang.dpq2.model.MenuModel;
import com.tarang.dpq2.view.activities.TransactionActivity;

import java.util.Locale;

import static com.tarang.dpq2.base.jpos_class.ConstantApp.TITLE;


public class BaseActivity extends AppCompatActivity implements OnLocaleChangedListener, ComponentCallbacks2 {

    public Context context;
    private static Activity currentActivity;
    private String LogString = "BaseActivity";
    public boolean showTimer = true;
    public Context transContext = null;
    private LocalizationActivityDelegate localizationDelegate = new LocalizationActivityDelegate(this);
//    private LifecycleRegistry mLifecycleRegistry;


    public BaseActivity() {
        Log.d(LogString, "BaseActivity constructor");
    }

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    //
    public static void setCurrentActivity(Activity activity) {
        currentActivity = activity;
    }

    public void setMenu(String title) {
        AppManager.getInstance().setApplicationContext(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        // getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        // getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        toolbar_center_tv = findViewById(R.id.toolbar_center_tv);
        back_tv = findViewById(R.id.back_tv);
        toolbar_center_tv.setText(title);
        getSupportActionBar().setTitle("");

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    protected void onCreate(Bundle bundle) {
       /* StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()   // or .detectAll() for all detectable problems
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());*/
        Log.d(LogString, "Enter onCreate");
//        mLifecycleRegistry = new LifecycleRegistry(this);
//        mLifecycleRegistry.markState(Lifecycle.State.CREATED);
        localizationDelegate.addOnLocaleChangedListener(this);
        localizationDelegate.onCreate(bundle);
        super.onCreate(bundle);
        context = this;
        doSomethingMemoryIntensive();

    }

    public void initData(){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }



    public void resetTimer(){
        cancelTimer();
        showTimer = true;
        timer.start();
    }

    CountDownTimer timer = new CountDownTimer(90000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            Logger.v("TimeOut --");
        }

        @Override
        public void onFinish() {
            Logger.v("TimeOut -finish-"+showTimer);
            if (showTimer) {
                showTimeOut();
            }
        }
    };

    public void showTimeOut() {
        Utils.alertDialogShow(context, getString(R.string.time_out), new View.OnClickListener() {
            @Override
            public void onClick(View dialog) {
                Utils.dismissDialoge();
                MapperFlow.getInstance().moveToLandingPage(context, true,1);
            }
        });

        if(transContext != null && transContext instanceof TransactionActivity){
            Logger.v("Inside Trasaction");
            ((TransactionActivity)transContext).timeOutFinish();
        }else
            ((BaseActivity)context).finish();
            Logger.v("Inside Trasaction else");

    }


    public Toolbar mToolbar;

    public void initcontrols() {
        initControlWithoutKeyPad();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void initControlWithoutKeyPad() {
        AppManager.getInstance().setApplicationContext(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    protected void onDestroy() {
        super.onDestroy();
        cancelTimer();
        AppInit.appInit.mustDie(this);
    }

    protected void onPause() {
        super.onPause();
        cancelTimer();
        invalidateOptionsMenu();
    }

    public void cancelTimer() {
        showTimer = false;
        timer.cancel();
        timer.onFinish();
    }

    public void setTitle() {
        if (getIntent().hasExtra(TITLE)) {
            String title = getIntent().getStringExtra(TITLE);
            setMenu(title);
        }
    }

    TextView toolbar_center_tv, back_tv;

    public void setTitle(String title) {

        setMenu(title);
    }

    protected void onResume() {
        super.onResume();
        localizationDelegate.onResume(this);
        if (showTimer)
            timer.start();
    }

    public MenuModel.MenuItem getCurrentMenu() {
        return AppManager.getInstance().getMenuItem();
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }


    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Logger.v("Toast --"+msg);
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(localizationDelegate.attachBaseContext(newBase));
    }

    @Override
    public Context getApplicationContext() {
        return localizationDelegate.getApplicationContext(super.getApplicationContext());
    }

    @Override
    public Resources getResources() {
        return localizationDelegate.getResources(super.getResources());
    }

    public final void setLanguage(String language) {
        localizationDelegate.setLanguage(this, language);
    }

    public final void setLanguage(Locale locale) {
        localizationDelegate.setLanguage(this, locale);
    }

    public final void setDefaultLanguage(String language) {
        localizationDelegate.setDefaultLanguage(language);
    }

    public final void setDefaultLanguage(Locale locale) {
        localizationDelegate.setDefaultLanguage(locale);
    }

    public final Locale getCurrentLanguage() {
        return localizationDelegate.getLanguage(this);
    }

    // Just override method locale change event
    @Override
    public void onBeforeLocaleChanged() {
    }

    @Override
    public void onAfterLocaleChanged() {
    }

    public void setTitle(MenuModel.MenuItem title) {
        if (title != null)
            setMenu(title.getMenu_name());
    }

    public void showDialoge(final String msg, final boolean showDialoge) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Utils.alertDialogShow(context, msg, new Utils.DialogeClick() {
                    @Override
                    public void onClick() {
                        if (showDialoge)
                            MapperFlow.getInstance().moveToLandingPage(context, true,2);
                    }
                });
            }
        });
    }

    public void doSomethingMemoryIntensive() {

        // Before doing something that requires a lot of memory,
        // check to see whether the device is in a low memory state.
        ActivityManager.MemoryInfo memoryInfo = getAvailableMemory();
        Logger.v("memoryinfoavail----" + memoryInfo.availMem);
        Logger.v("memoryinfothreshold----" + memoryInfo.threshold);
        Logger.v("memoryinfototal----" + memoryInfo.totalMem);
        Logger.v("memoryinfoiflow----" + memoryInfo.lowMemory);
        /*
        Logger.v("memory_max----"+Runtime.getRuntime().maxMemory());
        Logger.v("memory_free----"+Runtime.getRuntime().freeMemory());
        Logger.v("memory_total----"+Runtime.getRuntime().totalMemory());*/

        //max memory = 5682897
        if (memoryInfo.lowMemory) {
            // Do memory intensive work ...
            Utils.alertDialogShow(this, "Low Memory, App may crash, please restart the app", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.dismissDialoge();
                }
            });
            AppInit.appInit.mustDie(this);
        }
    }

    // Get a MemoryInfo object for the device's current memory status.
    private ActivityManager.MemoryInfo getAvailableMemory() {
        ActivityManager activityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo;
    }

    // Other activity code ...

    /**
     * Release memory when the UI becomes hidden or when system resources become low.
     *
     * @param level the memory-related event that was raised.
     */
    public void onTrimMemory(int level) {

        // Determine which lifecycle or system event was raised.
        switch (level) {

            case ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN:

                /*
                   Release any UI objects that currently hold memory.

                   The user interface has moved to the background.
                */
                Logger.v("app_crash-----" + "TRIM_MEMORY_UI_HIDDEN");


                break;

            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE:
                Logger.v("app_crash-----" + "TRIM_MEMORY_RUNNING_MODERATE");
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW:
                Logger.v("app_crash-----" + "TRIM_MEMORY_RUNNING_LOW");
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL:
                Logger.v("app_crash-----" + "TRIM_MEMORY_RUNNING_CRITICAL");

                /*
                   Release any memory that your app doesn't need to run.

                   The device is running low on memory while the app is running.
                   The event raised indicates the severity of the memory-related event.
                   If the event is TRIM_MEMORY_RUNNING_CRITICAL, then the system will
                   begin killing background processes.
                */

                break;

            case ComponentCallbacks2.TRIM_MEMORY_BACKGROUND:
            case ComponentCallbacks2.TRIM_MEMORY_MODERATE:
            case ComponentCallbacks2.TRIM_MEMORY_COMPLETE:
                Logger.v("app_crash-----" + "TRIM_MEMORY_COMPLETE");
                /*
                   Release as much memory as the process can.

                   The app is on the LRU list and the system is running low on memory.
                   The event raised indicates where the app sits within the LRU list.
                   If the event is TRIM_MEMORY_COMPLETE, the process will be one of
                   the first to be terminated.
                */

                break;

            default:
                /*
                  Release any non-critical data structures.

                  The app received an unrecognized memory level value
                  from the system. Treat this as a generic low-memory message.
                */
                break;
        }
    }

    public void unbindDrawables(View view) {
        if (view == null) {
            view = findViewById(R.id.parent_root_view);
        }
        if (view != null) {
            if (view.getBackground() != null) {
                view.getBackground().setCallback(null);
            }

            if (view instanceof ViewGroup) {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                    unbindDrawables(((ViewGroup) view).getChildAt(i));
                }

                ((ViewGroup) view).removeAllViews();
            }
        }
    }

    public void freeMemory() {
        unbindDrawables(null);
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        mLifecycleRegistry.markState(Lifecycle.State.STARTED);
    }

//    @NonNull
//    @Override
//    public Lifecycle getLifecycle() {
//        return mLifecycleRegistry;
//    }

    public void hideAmountView() {
//        if(getCurrentMenu().getMenu_tag().equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION_EXTENSION)) {
//            View view = findViewById(R.id.total_holder);
//            View view1 = findViewById(R.id.txt_total);
//            LinearLayout viewParent = findViewById(R.id.ll_amount_holder);
//            if (view != null) {
//                view.setVisibility(View.GONE);
//            }
//            if (view1 != null) {
//                view1.setVisibility(View.GONE);
//            }
//            if(viewParent != null){
//                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)viewParent.getLayoutParams();
//                params.weight = 0.7f;
//                viewParent.setLayoutParams(params);
//            }
//        }
    }

}

