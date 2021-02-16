package com.tarang.dpq2.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.StrictMode;

import androidx.multidex.MultiDex;

import com.tarang.dpq2.base.terminal_sdk.q2.card.SmartCardControl;
import com.tarang.dpq2.base.utilities.multilangutils.LocalizationApplicationDelegate;
import com.wizarpos.emvsample.constant.Constant;

/**
 * Created by satyajittarafdar on 04/03/18.
 */

public class AppInit extends Application implements Constant {

    public static boolean loadKernal = false;
    // TODO Q2
    public static int CONTACT_CHIP = 1;
    public static int CONTACTLESS_RF = 2;

    public static String[] apnName = {"MOBILY","STC","SKY_BAND"};
    public static String[] apnList = {"POS-M2M","POS.M2M","IBS"};
    public static String[] mcc = {"420","420","420"};
    public static String[] mnc = {"03","01","05"};

    private byte tranType = TRAN_GOODS;
    private byte paramType = -1;   // 参数设置类型
    private byte processState = 0;  // 处理阶段
    private byte state = 0;         //
    private int errorCode = 0;
    public static final boolean ContactlessRetry = false;
    //    public TransDetailInfo trans = new TransDetailInfo();
    public boolean needCard = false;
    public boolean enableContactlessCard = false;
    public boolean promptCardCanRemoved = false;
    public boolean promptOfflineDataAuthSucc = false;
    public boolean resetCardError = false;

    public int cardType = -1;
    public boolean msrError = false;

    public SmartCardControl contactUserCard;

    public boolean acceptMSR = true;
    public boolean acceptContactCard = true;
    public boolean acceptContactlessCard = true;
    public boolean promptCardIC = false;

    public byte recordType = 0x00;
//    public BatchInfo batchInfo;
//    public TerminalConfig terminalConfig;

    public boolean emvParamLoadFlag = false;
    public boolean emvParamChanged = false;


    // Old Code
    public static boolean HITTING_LIVE_SERVER = false; //TODO : Change TRUE - live server False - Local Server
    public static boolean VERSION_6_0_5 = false; // TODO : Change True - 6.0.5 version, False - 6.0.9 version
    public static boolean ENCRYPT_DISABLE = true; // TODO : change true if want to disbale encrypt data , false do encrpt before storing

    private static Context context;
    public static boolean sampleBoolean = true;
    public static AppInit appInit;
    public static String full_download = "3060000";
    public static String partial_download = "3020000";
    public static String lastPacket = full_download;
    LocalizationApplicationDelegate localizationDelegate = new LocalizationApplicationDelegate(this);

    public AppInit() {
    }

    public static Context getContext() {
        return context;
    }

    public static AppInit getInstance() {
        return appInit;
    }

    public static String getPrefsName() {

        return context.getPackageName();
    }

    @Override
    public void onCreate() {
          /*  StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
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
        super.onCreate();
//
        appInit = this;
        context = getApplicationContext();
        contactUserCard = new SmartCardControl(SmartCardControl.CARD_CONTACT, 0);
//        androidx.work.Configuration configuration = (new androidx.work.Configuration.Builder()).setExecutor(Executors.newFixedThreadPool(1)).build();
//        WorkManager.initialize(context,configuration);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(localizationDelegate.attachBaseContext(base));
        // super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        localizationDelegate.onConfigurationChanged(this);
    }

    @Override
    public Context getApplicationContext() {
        return localizationDelegate.getApplicationContext(super.getApplicationContext());
    }

    public void mustDie(Object object) {
//        if (refWatcher != null) {
//            refWatcher.watch(object);
//        }
    }

}
