package com.tarang.dpq2.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tarang.dpq2.BuildConfig;
import com.tarang.dpq2.R;
import com.tarang.dpq2.base.baseactivities.BaseActivity;
import com.tarang.dpq2.base.jpos_class.ByteConversionUtils;
import com.tarang.dpq2.base.jpos_class.ConstantApp;
import com.tarang.dpq2.base.room_database.db.entity.SAFModelEntity;
import com.tarang.dpq2.base.room_database.db.entity.TransactionModelEntity;
import com.tarang.dpq2.base.utilities.Utils;
import com.tarang.dpq2.model.Advice56Model;
import com.tarang.dpq2.model.DeviceSpecificModel;
import com.tarang.dpq2.model.MenuModel;
import com.tarang.dpq2.model.PrintIsoPacketModel;
import com.tarang.dpq2.model.ReconcilationTopModel;
import com.tarang.dpq2.model.ReconcileSetupModel;
import com.tarang.dpq2.model.ReconciliationCardSchemeModel;
import com.tarang.dpq2.model.RetailerDataModel;
import com.tarang.dpq2.model.TerminalConnectionGPRSModel;
import com.tarang.dpq2.model.TerminalConnectionWifiModel;
import com.tarang.dpq2.model.VendorDetailsModel;
import com.wizarpos.emvsample.constant.Constant;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AppManager {
    private static AppManager instance = null;
    private static Context mContext;
    private static String LogString = "AppManager";
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public AppManager(Context context) {
        mContext = context;
        preferences = context.getSharedPreferences("payswiff_data", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public static AppManager getInstance() {
        if (instance == null) {
            instance = new AppManager(AppInit.getContext());
        }
        return instance;
    }

    public static Context getContext() {
        return mContext;
    }

    public void setApplicationContext(Context context) {
        mContext = context;
    }

    public Context getApplicationContext() {
        return mContext;
    }
    //get the Device info


    public static String getDeviceVersion() {
        return BuildConfig.VERSION_NAME;
//        return android.os.Build.VERSION.RELEASE;
    }

    //gets the unique android id...
    public static String getUniqueId(Context context) {
        return android.provider.Settings.Secure.getString(context.getContentResolver(), "android_id");
    }


    //gets the App Version
    public static String getAppVersion(Context context) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        }
        // Misplaced declaration of an exception variable
        catch (Exception e) {
            e.printStackTrace();

        }
        if (packageInfo != null)
            return packageInfo.versionName;
        else
            return "";
    }

    public static int getAppVersionCode(Context context) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        }
        // Misplaced declaration of an exception variable
        catch (Exception e) {
            e.printStackTrace();

        }
        if (packageInfo != null)
            return packageInfo.versionCode;
        else
            return 0;
    }

    public void setString(String key, String value) {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("payswiff_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void setInt(String key, int value) {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("payswiff_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void setLong(String key, long value) {
        editor.putLong(key, value);
        editor.commit();
    }

    public void setBoolean(String key, boolean value) {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("payswiff_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public String getString(String key) {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("payswiff_data", Context.MODE_PRIVATE);
        return preferences.getString(key, getDefaultStringValue(key));
    }

    public char getCharKSN() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("payswiff_data", Context.MODE_PRIVATE);
        return preferences.getString(DUKPT_KEY_KSN_INDICATOR, getDefaultStringValue(DUKPT_KEY_KSN_INDICATOR)).toCharArray()[0];
    }

    public void setCharKSN(char indicator) {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("payswiff_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(DUKPT_KEY_KSN_INDICATOR, ""+indicator);
        editor.apply();    }

    public int getInt(String key) {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("payswiff_data", Context.MODE_PRIVATE);
        return preferences.getInt(key, 0);
    }

    private int getDefaultInt(String key) {
        return 0;
    }

    public boolean getBoolean(String key) {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("payswiff_data", Context.MODE_PRIVATE);
        return preferences.getBoolean(key, getDefaultBool(key));
    }

    public long getLong(String key) {
        return preferences.getLong(key, defaultValue(key));
    }

    private long defaultValue(String key) {
        if (key == RETRIVEREFERENCE_NUMBER)
            return Long.parseLong(12 + ByteConversionUtils.generateTraceNumber(10));
        return 0;
    }

    private String getDefaultStringValue(String key) {
        if (key.equalsIgnoreCase(ConstantApp.SET_GPS_LOCATION))
            return "N000000W0000000";
        else if (key.equalsIgnoreCase(ConstantApp.SPRM_PHONE_NUMBER))
            return "1234567890";
        else if (key.equalsIgnoreCase("KSN_DATA"))
            return "0";
        else if (key.equalsIgnoreCase(MERCHANT_PASSWORD))
            return "000000";
        else if (key.equalsIgnoreCase(DUKPT_KEY_KSN_INDICATOR))
            return "A";
        else if (key.equalsIgnoreCase(ADMIN_PASSWORD))
            return "000000";
        else {
            switch (key) {
                case ConstantApp.SPRM_VENDER_ID:
                    return "18";
                case ConstantApp.SPRM_TERMINAL_TYPE:
                    return "09";
                case ConstantApp.SPRM_TRSM_ID:
                    return "00362815";
//                case ConstantApp.SPRM_TERMINAL_SERIAL_NUM:
//                    return "362815";
                case ConstantApp.SPRM_KEY_ISSUE_:
                    return "FFFF";
                case ConstantApp.SPRM_IP_CONFIG:
                    return "122.165.192.253";
                case ConstantApp.SPRM_PORT:
                    return "1000";
                case ConstantApp.MERCHANT_IP_CONFIG:
                    return "35.154.44.222";
                case ConstantApp.MERCHANT_PORT:
                    return "8443";
                case ConstantApp.CHANGE_PASSWORD:
                    return "000000";
                case ConstantApp.SPRM_VENDOR_KEY1:
                    return "01";
                case ConstantApp.SPRM_NII_ID:
                    return "037";
                case ConstantApp.SPRM_SAMA_KEY1:
                    return "00";
                case ConstantApp.HSTNG_TLS:
                    return "1.2";
            }
        }
        return "";
    }


    //---------------------------------------App Specific  starts here ---------------------------------------------------

    //set and get bitmap value temporary

    MenuModel.MenuItem menuItem;

    public MenuModel.MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuModel.MenuItem menuItem) {
        this.menuItem = menuItem;
    }


    //-----------------------------------------Data Elements ----------------------------------------------------------


    //getting value from terminal registration
    String CARDACCEPTORID = "CardAcceptorID";
    String CARDACCEPTORCODE = "CardAcceptorCode";
    String CARDACCEPTORBUSINESSCODE = "CardAcceptorBusinessCode";
    String ACCURINGINSITUTEIDCODE = "AccuringInsituteIdCode";
    String SYATEMTRACEAUDIT_NUMBER = "SystemTraceAuditnumber";
    String RETRIVEREFERENCE_NUMBER = "RETRIVEREFERENCE_NUMBER";
    String AID_TERMINAL_VERSION_NUMBER_ = "Terminal_VersionNumber_";
    String MERCHANT_CODE = "MERCHANT_CODE_";
    String REFUND_OFFLINE_ENABLE = "REFUND_OFFLINE_ENABLE";
    String FLOOR_LIMIT = "FLOOR_LIMIT_";
    String AMOUNT_ENABLED = "AMOUNT_ENABLED";
    String MAX_AMOUNT = "MAX_AMOUNT";
    String MAX_AMOUNT_CASH = "MAX_AMOUNT_CASH";
    String FLOOR_LIMIT_ENABLED = "FLOOR_LIMIT_ENABLED";
    String INITILIZATION_STATUS = "INITILIZATION_STATUS_NEW";
    String REVERSAL_RESPONCE_TIME = "REVERSAL_RESPONCE_TIME";
    String REVERSAL_RESPONCE_STATUS = "REVERSAL_RESPONCE_STATUS";
    String SNAPSHOT_ID = "SNAPSHOT_ID";
    String LAST_TRANSACTION = "LAST_TRANSACTION";
    String ADMIN_PASSWORD = "ADMIN_PASSWORD";
    String MERCHANT_PASSWORD = "MERCHANT_PASSWORD";
    String PRIMARY_CONNECTION = "PRIMARY_CONNECTION";
    String RECON_DATE = "RECON_DATE";
    String DUKPT_KEY_KSN_INDICATOR = "DUKPT_KEY_KSN_INDICATOR";


    public void setPrimaryConnection(boolean b){
        setBoolean(PRIMARY_CONNECTION,b);
    }

    public boolean getConnectionPriority(){
        return getBoolean(PRIMARY_CONNECTION);
    }

    public void initializationStatus(int b) {
        Logger.v("setInitializationStatus --" + b);
        setInt(INITILIZATION_STATUS, b);
    }

    public boolean getInitializationStatus(Context context) {
        int status = (getInt(INITILIZATION_STATUS));
        Logger.v("getInitializationStatus--" + status);
        if(status == 2){
            ((BaseActivity) context).showToast(context.getString(R.string.plz_do_TMS));
        } else if (status == 0) {
            ((BaseActivity) context).showToast(context.getString(R.string.plz_do_registration));
        }
        return status == 1;
    }

    public boolean getOnlyInitializationStatus(Context context) {
        int status = (getInt(INITILIZATION_STATUS));
        Logger.v("getInitializationStatus--" + status);
        if (status == 0) {
            ((BaseActivity) context).showToast(context.getString(R.string.plz_do_registration));
        }
        return status != 0;
    }

    public void setLastTransaction(boolean aBoolean) {
        setBoolean(LAST_TRANSACTION, aBoolean);
    }


    public String getSystemTraceAuditnumber11() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("SystemTraceAuditnumber", Context.MODE_PRIVATE);
        int i = preferences.getInt(SYATEMTRACEAUDIT_NUMBER, 0);
        int added = i + 1;
        SharedPreferences.Editor edit = preferences.edit();
        edit.putInt(SYATEMTRACEAUDIT_NUMBER, added);
        edit.apply();
        Logger.v("stan_no---"+String.format("%06d", (added)));
        return String.format("%06d", (added));
    }

    public boolean checkPassword(String pass,String key) {
        return (getString(key).trim().equalsIgnoreCase(pass));
    }

    public void setReversalStatus(boolean bool) {
        Logger.v("Reversal --" + bool);
        setBoolean(REVERSAL_RESPONCE_STATUS, bool);
    }

    public boolean getReversalStatus() {
        Logger.v("Reversal --" + getBoolean(REVERSAL_RESPONCE_STATUS));
        return getBoolean(REVERSAL_RESPONCE_STATUS);
    }

    public void setReversalTime(int s) {
        Logger.v("Reversal --" + s);
        setInt(REVERSAL_RESPONCE_TIME, s);
        setReversalStatus(false);
    }

    public boolean getReversalStatus(int s) {
        int reversal = getInt(REVERSAL_RESPONCE_TIME);
        Logger.v("Reversal --" + s);
        Logger.v("Reversal --" + reversal);
        if (reversal == 0)
            return true;
        if(s < reversal)
            return false;
        if (reversal != s)
            return true;
        else
            return (!getReversalStatus());

    }

    public void setSnapshotID(int s) {
        setInt(SNAPSHOT_ID, s);
        Logger.v("Uid--" + getInt(SNAPSHOT_ID));
    }

    public int getSnapshotID() {
        return getInt(SNAPSHOT_ID);
    }

    //TODO fileaction values

    public String getCardAcceptorBusinessCode26() {
//        return "7399";
        return getString(CARDACCEPTORBUSINESSCODE);
    }

    public String getAccuringInsituteIdCode32() {
        return "588847";
//        return getString(ACCURINGINSITUTEIDCODE);
    }

    public String getAdminPassword() {
        return getString(ADMIN_PASSWORD);
    }

    public String getMerchantPassword() {
        return getString(MERCHANT_PASSWORD);
    }

    public void setAdminPassword(String pass) {
        setString(ADMIN_PASSWORD, pass);
    }

    public void setMerchantPassword(String pass) {
        setString(MERCHANT_PASSWORD, pass);
    }

    public void resetAdminPassword() {
        setString(ADMIN_PASSWORD, "000000");
    }

    public void resetMerchantPassword() {
        setString(MERCHANT_PASSWORD, "000000");
    }

    public int getReversalUid() {
        return getInt(REVERSAL_RESPONCE_TIME);
    }

    public long getRetriveRefNumber37() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("RetriveRefNumber", Context.MODE_PRIVATE);
        long val = preferences.getLong(RETRIVEREFERENCE_NUMBER, defaultValue(RETRIVEREFERENCE_NUMBER));
        val = val + 1;
        SharedPreferences.Editor edit = preferences.edit();
        edit.putLong(RETRIVEREFERENCE_NUMBER, val);
        edit.apply();
        return val;
    }

    public String getCardAcceptorID41() {
        return "1234567812121234";
//        return getString(CARDACCEPTORID);
    }

    public String getCardAcceptorCode42() {
        return "800150400566";
//        return getString(CARDACCEPTORCODE);
    }

    public String getTerminalAIDVersionNumber(String aid) {
        Logger.v("AID_TERMINAL_VERSION_NUMBER_+aid -- " + aid.trim().toLowerCase());
        Logger.v("Val get--" + getString(AID_TERMINAL_VERSION_NUMBER_ + aid.trim().toLowerCase()));
        return getString(AID_TERMINAL_VERSION_NUMBER_ + aid.trim().toLowerCase());
    }

    public String getMerchantCode(String aid) {
        return getString(MERCHANT_CODE + aid);
    }

    public boolean getRefundOfflineEnabled(String aid) {
        boolean offline = getBoolean(REFUND_OFFLINE_ENABLE + aid);
        Logger.v("getRefundOfflineEnabled --"+offline);
        return offline;
    }

    public String getFloorLimit(String aid) {
        return getString(FLOOR_LIMIT + aid);
    }

    public String getTransactionAmountEnabled(String aid) {
        Logger.v("Amount enabled -" + getString(AMOUNT_ENABLED + aid));
        return getString(AMOUNT_ENABLED + aid);
    }

    public String getMaxAmount(String aid) {
        Logger.v("MAX_AMOUNT -" + getString(MAX_AMOUNT + aid));
        return getString(MAX_AMOUNT + aid);
    }

    public String getMaxCashAmount(String aid) {
        Logger.v("MAX_AMOUNT -" + getString(MAX_AMOUNT_CASH + aid));
        return getString(MAX_AMOUNT_CASH + aid);
    }

    public void setTerminalAIDVersionNumber(String aid, String id) {
        Logger.v("AID_TERMINAL_VERSION_NUMBER_+aid -- " + aid.trim().toLowerCase());
        Logger.v("Val --" + id);
        setString(AID_TERMINAL_VERSION_NUMBER_ + aid.trim().toLowerCase(), id);
        ;
    }

    public void setMerchantCode(String aid, String code) {
        setString(MERCHANT_CODE + aid, code);
    }

    public void setRefundOfflineEnabled(String aid, boolean code) {
        setBoolean(REFUND_OFFLINE_ENABLE + aid, code);
    }

    public void setFllorLimit(String aid, String code) {
        setString(FLOOR_LIMIT + aid, code);
    }

    public void setTransactionAmountEnabled(String aid, String code) {
        setString(AMOUNT_ENABLED + aid, code);
    }

    public void setMaxAmount(String aid, String code) {
        setString(MAX_AMOUNT + aid, code);
    }

    public void setMaxCashBackAmount(String aid, String code) {
        setString(MAX_AMOUNT_CASH + aid, code);
    }

    public void setFllorLimitEnabled(String aid, boolean code) {
        Logger.v("FloorLimitEnabled -"+FLOOR_LIMIT_ENABLED + aid);
        Logger.v("FloorLimitEnabled -"+code);
        setBoolean(FLOOR_LIMIT_ENABLED + aid, code);
    }

    public boolean isFllorLimitEnabled(String aid) {
        return getBoolean(FLOOR_LIMIT_ENABLED + aid);
    }

    public void setCardAcceptorBusinessCode26(String val) {
        setString(CARDACCEPTORBUSINESSCODE, val);
    }

    public void setAccuringInsituteIdCode32(String val) {
        setString(ACCURINGINSITUTEIDCODE, val);
    }

    public void setCardAcceptorID41(String id) {
        setString(CARDACCEPTORID, id);
    }

    public void setCardAcceptorCode42(String id) {
        setString(CARDACCEPTORCODE, id);
    }

    //*******************************storing vendordetails to shared preference *******************************************
    VendorDetailsModel vendorDetailsModel;

    public VendorDetailsModel getVendorDetailsModel() {
        preferences = getApplicationContext().getSharedPreferences("vendorDetails", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("vendor", "");
        if (json.equals("")) {
            this.vendorDetailsModel = null;
        } else {
            this.vendorDetailsModel = gson.fromJson(json, VendorDetailsModel.class);
        }

        return vendorDetailsModel;
    }

    public void setVendorDetailsModel(VendorDetailsModel vendorDetailsModel) {
        preferences = getApplicationContext().getSharedPreferences("vendorDetails", Context.MODE_PRIVATE);
        editor = preferences.edit();
        String vendorDetails = new Gson().toJson(vendorDetailsModel);
        editor.putString("vendor", vendorDetails);
        editor.apply();
        this.vendorDetailsModel = vendorDetailsModel;
    }
    //*************************************************vendor details stored********************************************

    //*******************************storing retailerdetails to shared preference *******************************************
    public RetailerDataModel retailerDataModel;

    public RetailerDataModel getRetailerDataModel() {
        preferences = getApplicationContext().getSharedPreferences("retailerDetails", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("retailer", "");
        if (json != null && json.trim().length() == 0) {
            //     this.retailerDataModel = null;
            this.retailerDataModel = new RetailerDataModel();
        } else {
            this.retailerDataModel = gson.fromJson(json, RetailerDataModel.class);
        }

        return retailerDataModel;
    }

    public void setRetailerDetailsModel(RetailerDataModel retailerDetailsModel) {
        preferences = getApplicationContext().getSharedPreferences("retailerDetails", Context.MODE_PRIVATE);
        editor = preferences.edit();
        String retailerDetails = new Gson().toJson(retailerDetailsModel);
        editor.putString("retailer", retailerDetails);
        editor.apply();
        this.retailerDataModel = retailerDetailsModel;
    }

    ReconcileSetupModel reconcileSetupModel;

    public ReconcileSetupModel getReconcileSetupModel() {

        preferences = getApplicationContext().getSharedPreferences("reconcileSetup", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("reconcileSetupJson", "");
        if (json != null && json.trim().length() == 0) {
            //     this.retailerDataModel = null;
            this.reconcileSetupModel = new ReconcileSetupModel();
        } else {
            this.reconcileSetupModel = gson.fromJson(json, ReconcileSetupModel.class);
        }
        return reconcileSetupModel;
    }

    public void setReconcileSetupModel(ReconcileSetupModel reconcileSetupModel) {

        preferences = getApplicationContext().getSharedPreferences("reconcileSetup", Context.MODE_PRIVATE);
        editor = preferences.edit();
        String reconcile = new Gson().toJson(reconcileSetupModel);
        editor.putString("reconcileSetupJson", reconcile);
        editor.apply();
        this.reconcileSetupModel = reconcileSetupModel;
    }

    public int getSafRetryCount() {
        preferences = getApplicationContext().getSharedPreferences("retailerDetails_count", Context.MODE_PRIVATE);
        return preferences.getInt("retry_count", 3);
    }

    public void setSafRetryCount(String count) {
        preferences = getApplicationContext().getSharedPreferences("retailerDetails_count", Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putInt("retry_count", Integer.parseInt(count));
        editor.apply();
    }

    //*************************************************retailer details stored********************************************

    //*******************************storing connection gprs details to shared preference *******************************************

    public TerminalConnectionGPRSModel terminalConnectionGPRSModel;

    public TerminalConnectionGPRSModel getTerminalConnectionGPRSModel() {
        preferences = getApplicationContext().getSharedPreferences("terminalConnectionGPRSDetails", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("terminalConnectionGPRS", "");
        if (json.equals("")) {
            //     this.terminalConnectionsModel = null;
            this.terminalConnectionGPRSModel = new TerminalConnectionGPRSModel();
        } else {
            this.terminalConnectionGPRSModel = gson.fromJson(json, TerminalConnectionGPRSModel.class);
        }

        return terminalConnectionGPRSModel;
    }

    public void setTerminalConnectionGPRSDetailsModel(TerminalConnectionGPRSModel terminalConnectionGPRSDetailsModel) {
        preferences = getApplicationContext().getSharedPreferences("terminalConnectionGPRSDetails", Context.MODE_PRIVATE);
        editor = preferences.edit();
        String terminalConnectionGPRSDetails = new Gson().toJson(terminalConnectionGPRSDetailsModel);
        editor.putString("terminalConnectionGPRS", terminalConnectionGPRSDetails);
        editor.apply();
        this.terminalConnectionGPRSModel = terminalConnectionGPRSDetailsModel;
    }

    //*************************************************connection details details stored********************************************

    //*******************************storing connection gprs details to shared preference *******************************************

    public TerminalConnectionWifiModel terminalConnectionWifiModel;

    public TerminalConnectionWifiModel getTerminalConnectionWifiModel() {
        preferences = getApplicationContext().getSharedPreferences("terminalConnectionWifiDetails", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("terminalConnectionWifi", "");
        if (json.equals("")) {
            //     this.terminalConnectionsModel = null;
            this.terminalConnectionWifiModel = new TerminalConnectionWifiModel();
        } else {
            this.terminalConnectionWifiModel = gson.fromJson(json, TerminalConnectionWifiModel.class);
        }

        return terminalConnectionWifiModel;
    }

    public void setTerminalConnectionWifiDetailsModel(TerminalConnectionWifiModel terminalConnectionWifiDetailsModel) {
        preferences = getApplicationContext().getSharedPreferences("terminalConnectionWifiDetails", Context.MODE_PRIVATE);
        editor = preferences.edit();
        String terminalConnectionWifiDetails = new Gson().toJson(terminalConnectionWifiDetailsModel);
        editor.putString("terminalConnectionWifi", terminalConnectionWifiDetails);
        editor.apply();
        this.terminalConnectionWifiModel = terminalConnectionWifiDetailsModel;
    }

    //*************************************************storing connection gprs details details stored********************************************

    //*******************************device specific details to shared preference *******************************************

    public DeviceSpecificModel deviceSpecificModel;

    public DeviceSpecificModel getDeviceSpecificModel() {
        preferences = getApplicationContext().getSharedPreferences("deviceSpecificDetails", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("deviceSpecific", "");
        if (json.equals("")) {
            //     this.terminalConnectionsModel = null;
            this.deviceSpecificModel = new DeviceSpecificModel();
        } else {
            this.deviceSpecificModel = gson.fromJson(json, DeviceSpecificModel.class);
        }

        return deviceSpecificModel;
    }

    public void setDeviceSpecificDetailsModel(DeviceSpecificModel deviceSpecificDetailsModel) {
        preferences = getApplicationContext().getSharedPreferences("deviceSpecificDetails", Context.MODE_PRIVATE);
        editor = preferences.edit();
        String deviceSpecificDetails = new Gson().toJson(deviceSpecificDetailsModel);
        editor.putString("deviceSpecific", deviceSpecificDetails);
        editor.apply();
        this.deviceSpecificModel = deviceSpecificDetailsModel;
    }

    //*************************************************connection details details stored********************************************

    //******************************* Aid list details to shared preference *******************************************

    //   public List<String> aidList;

    public List<String> getAidList() {
        preferences = getApplicationContext().getSharedPreferences("aidListDetails", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        Set<String> set = preferences.getStringSet("aidList", null);
        return new ArrayList<>(set);
    }

    public void setAidListDetails(List<String> aidList) {
        preferences = getApplicationContext().getSharedPreferences("aidListDetails", Context.MODE_PRIVATE);
        editor = preferences.edit();
        Set<String> set = new HashSet<String>();
        set.addAll(aidList);
        editor.putStringSet("aidList", set);
        editor.apply();
        //  this.aidList = aidList;
    }

    //*************************************************Aid list details details stored********************************************

    boolean financialAdviceRequired, authorisationAdviceRequired;

    public boolean isFinancialAdviceRequired() {
        return financialAdviceRequired;
    }

    public void setFinancialAdviceRequired(boolean financialAdviceRequired) {
        this.financialAdviceRequired = financialAdviceRequired;
    }

    public boolean isAuthorisationAdviceRequired() {
        return authorisationAdviceRequired;
    }

    public void setAuthorisationAdviceRequired(boolean authorisationAdviceRequired) {
        this.authorisationAdviceRequired = authorisationAdviceRequired;
    }

    String de38, de39 = "", de55, responseMTI = "", de124 = "";
    boolean refundMTI = false;

    public String getDe38() {
        return de38;
    }

    public void setDe38(String de38) {
        this.de38 = de38;
    }

    public String getDe39() {
        return de39;
    }

    public void setDe39(String de39) {
        this.de39 = de39;
    }

    public boolean getRefundMTI() {
        return refundMTI;
    }

    public void setRefundMTI(boolean refundMTI) {
        this.refundMTI = refundMTI;
    }

    public String getDe124() {
        return de124;
    }

    public void setDe124(String de124) {
        this.de124 = de124;
    }

    public String getDe55() {
        return de55;
    }

    public void setDe55(String de55) {
        this.de55 = de55;
    }

    public String getResponseMTI() {
        return responseMTI;
    }

    public void setResponseMTI(String responseMTI) {
        this.responseMTI = responseMTI;
    }


    // Admin Menu Set Parameters Values ****************************

    public String getVendorId() {
        return getString(ConstantApp.SPRM_VENDER_ID);
    }

    public String getVendorTerminalType() {
        return getString(ConstantApp.SPRM_TERMINAL_TYPE);
    }

    public String getVendorTerminalSerialNumber() {
        String trsm = getString(ConstantApp.SPRM_TRSM_ID);
        return trsm.substring(trsm.length()-6);
    }

    private String appendZeros(int length) {
        String zeros = "";
        for(int i=0;i<(6-length);i++){
            zeros = zeros+"0";
        }
        return zeros;
    }

    public String getIP() {
        return getString(ConstantApp.SPRM_IP_CONFIG);
    }

    public String getPort() {
        return getString(ConstantApp.SPRM_PORT);
    }

    String enteredAmount, de37, purchaseAdviceDate;

    public String getDe37() {
        return de37;
    }

    public void setDe37(String de37) {
        this.de37 = de37;
    }

    public String getPurchaseAdviceDate() {
        return purchaseAdviceDate;
    }

    public void setPurchaseAdviceDate(String purchaseAdviceDate) {
        this.purchaseAdviceDate = purchaseAdviceDate;
    }

    Advice56Model advice56Model;

    public Advice56Model getAdvice56Model() {
        return advice56Model;
    }

    public void setAdvice56Model(Advice56Model advice56Model) {
        this.advice56Model = advice56Model;
    }

    public String getVendorPublickKey() {
        return getString(ConstantApp.SPRM_VENDOR_KEY1);
    }

    public String getSamaKey() {
        return getString(ConstantApp.SPRM_SAMA_KEY1);
    }


    public boolean getConnectonMode() {
        return getBoolean("CONNECTION_MODE");
    }

    public void setConnectonMode(boolean bool) {
        setBoolean("CONNECTION_MODE", bool);
    }

    public String getGPRSAPNName() {
        String apn = getString("GPRS_APN");
        Logger.v("APNN -" + apn);
        return apn;
    }

    public String getGPRSAPN() {
        String apn = getString("GPRS_APN");
        Logger.v("APNN -" + apn);
        if (apn.trim().length() == 0) {
            return "GPRS";
        } else
            return "GPRS" + "\n" + apn;
    }

    public String getGPRSAPN1() {
        String apn = getString("GPRS_APN");
        Logger.v("APNN -" + apn);
        if (apn.trim().length() == 0) {
            return "";
        } else
            return "\n" + apn;
    }

    public void setGPRSAPN(String data) {
        setString("GPRS_APN", data);
    }


    //********************************************* last transaction details******************************************************

    TransactionModelEntity transactionModelEntity;

    public TransactionModelEntity getTransactionModelEntity() {
        preferences = getApplicationContext().getSharedPreferences("lastTransactionDetails", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("lastTransaction", "");
        if (json.equals("")) {
            //     this.retailerDataModel = null;
            this.transactionModelEntity = new TransactionModelEntity();
        } else {
            this.transactionModelEntity = gson.fromJson(json, TransactionModelEntity.class);
        }
        return transactionModelEntity;
    }

    public void setTransactionModelEntity(TransactionModelEntity transactionModelEntity) {
        preferences = getApplicationContext().getSharedPreferences("lastTransactionDetails", Context.MODE_PRIVATE);
        editor = preferences.edit();
        String lastTransaction = new Gson().toJson(transactionModelEntity);
        editor.putString("lastTransaction", lastTransaction);
        editor.apply();
        this.transactionModelEntity = transactionModelEntity;
    }

    public TransactionModelEntity getDuplicateTransactionModelEntity() {
        preferences = getApplicationContext().getSharedPreferences("duplicateTransaction", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("duplicateTrans", "");
        Logger.v("json --" + json);
        if (json == null || json.trim().length() == 0) {
            //     this.retailerDataModel = null;
            this.transactionModelEntity = null;
        } else {
            this.transactionModelEntity = gson.fromJson(json, TransactionModelEntity.class);
        }
        return transactionModelEntity;
    }

    public void setDuplicateTransactionModelEntity(TransactionModelEntity transactionModelEntity) {
        preferences = getApplicationContext().getSharedPreferences("duplicateTransaction", Context.MODE_PRIVATE);
        String lastTransaction = new Gson().toJson(transactionModelEntity);
        editor = preferences.edit();
        editor.putString("duplicateTrans", lastTransaction);
        editor.apply();
    }

    public void setDuplicateTransactionModelEntity(SAFModelEntity transactionModelEntity) {
        preferences = getApplicationContext().getSharedPreferences("duplicateTransaction", Context.MODE_PRIVATE);
        String lastTransaction = new Gson().toJson(transactionModelEntity);
        editor = preferences.edit();
        editor.putString("duplicateTrans", lastTransaction);
        editor.apply();
    }

    public void resetDuplicateTransactionModelEntity() {
        preferences = getApplicationContext().getSharedPreferences("duplicateTransaction", Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString("duplicateTrans", "");
        editor.apply();
    }

    //********************************************* last transaction details******************************************************
    boolean reversalManual;
    boolean adminNotification = false;

    public boolean isReversalManual() {
        return reversalManual;
    }

    public void setReversalManual(boolean reversalManual) {
        this.reversalManual = reversalManual;
    }

    // setting data for print packet data
    PrintIsoPacketModel printIsoPacketModel;

    public PrintIsoPacketModel getPrintIsoPacketModel() {
        return printIsoPacketModel;
    }

    public void setPrintIsoPacketModel(PrintIsoPacketModel printIsoPacketModel) {
        this.printIsoPacketModel = printIsoPacketModel;
    }

    String historyView, adminSafHistoryView;

    public String getHistoryView() {
        return historyView;
    }

    public void setHistoryView(String historyView) {
        this.historyView = historyView;
    }

    public String getAdminSafHistoryView() {
        return adminSafHistoryView;
    }

    public void setAdminSafHistoryView(String adminSafHistoryView) {
        this.adminSafHistoryView = adminSafHistoryView;
    }

    List<ReconciliationCardSchemeModel> reconciliationCardSchemeModelList;

    public List<ReconciliationCardSchemeModel> getReconciliationCardSchemeModelList() {
        preferences = getApplicationContext().getSharedPreferences("reconciliationCardSchemeModelList", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("reconciliationCardSchemeList", "");
        if (json.equals("")) {
            //     this.retailerDataModel = null;
            this.reconciliationCardSchemeModelList = new ArrayList<>();
        } else {
            Type type = new TypeToken<List<ReconciliationCardSchemeModel>>() {
            }.getType();
            this.reconciliationCardSchemeModelList = gson.fromJson(json, type);
        }
        return reconciliationCardSchemeModelList;
    }

    public void setReconciliationCardSchemeModelList(List<ReconciliationCardSchemeModel> reconciliationCardSchemeModelList) {
        preferences = getApplicationContext().getSharedPreferences("reconciliationCardSchemeModelList", Context.MODE_PRIVATE);
        editor = preferences.edit();
        String list = new Gson().toJson(reconciliationCardSchemeModelList);
        editor.putString("reconciliationCardSchemeList", list);
        editor.apply();
        this.reconciliationCardSchemeModelList = reconciliationCardSchemeModelList;
    }

    public ReconcilationTopModel getReconciliationTopCard() {
        preferences = getApplicationContext().getSharedPreferences("reconciliationTopCardScheme", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        ReconcilationTopModel reconciliationTopCard;
        String json = preferences.getString("reconciliationCardSchemeList", "");
        if (json.equals("")) {
            //     this.retailerDataModel = null;
            reconciliationTopCard = new ReconcilationTopModel();
        } else {
            Type type = new TypeToken<ReconcilationTopModel>() {
            }.getType();
            reconciliationTopCard = gson.fromJson(json, type);
        }
        return reconciliationTopCard;
    }

    public void setReconciliationTopCard(ReconcilationTopModel reconciliationCardSchemeModelList) {
        preferences = getApplicationContext().getSharedPreferences("reconciliationTopCardScheme", Context.MODE_PRIVATE);
        editor = preferences.edit();
        String list = new Gson().toJson(reconciliationCardSchemeModelList);
        editor.putString("reconciliationCardSchemeList", list);
        editor.apply();
    }

    public void saveReconsilationDate() {
        setString(RECON_DATE, Utils.getCurrentDate().split(" ")[0]);
    }

    public void resetReconsilationDate() {
        setString(RECON_DATE, "");
    }

    public boolean getReconsilationDate() {
        return !(Utils.getCurrentDate().split(" ")[0].equalsIgnoreCase(getString(RECON_DATE)));
    }


    public void setTemprovaryOutService(boolean b) {
        Logger.v("setTemprovaryOutService --"+b);
        setBoolean("SAF_OUT_SERVICE",b);
    }

    public boolean getTemprovaryOutService() {
        Logger.v("setTemprovaryOutService --"+getBoolean("SAF_OUT_SERVICE"));
        return getBoolean("SAF_OUT_SERVICE");
    }

    public void saveTransactionTime(TransactionModelEntity transaction) {
        String cStartTime = transaction.getStartTimeConnection();
        String cEndTime = transaction.getEndTimeConnection();
        String startTime = transaction.getStartTimeTransaction();
        String endTime = transaction.getEndTimeTransaction();
        if(startTime != null && endTime != null && cStartTime != null && cEndTime != null) {
            if (startTime.trim().length() != 0 && endTime.trim().length() != 0 && cStartTime.trim().length() != 0 && cEndTime.trim().length() != 0) {
                preferences = getApplicationContext().getSharedPreferences("lastTransactionTime", Context.MODE_PRIVATE);
                editor = preferences.edit();
                editor.putString("getStartTimeConnection", cStartTime);
                editor.putString("getEndTimeConnection", cEndTime);
                editor.putString("getStartTimeTransaction", startTime);
                editor.putString("getEndTimeTransaction", endTime);
                editor.putString("getLastRRN", transaction.getRetriRefNo37());
                editor.apply();
            }
        }
    }

    public String[] getLastTransactionTime() {
        String[] time = {"000000000" , "000000000" ,"000000000" , "000000000", "000000000000"};
        preferences = getApplicationContext().getSharedPreferences("lastTransactionTime", Context.MODE_PRIVATE);
//        String cStartTime = ByteConversionUtils.changeDateFormat(preferences.getString("getStartTimeConnection",""),"HHMMSSsss");
//        String cEndTime = ByteConversionUtils.changeDateFormat(preferences.getString("getEndTimeConnection", ""),"HHMMSSsss");
        String startTime = ByteConversionUtils.changeDateFormat(preferences.getString("getStartTimeTransaction", ""),"HHMMSSsss");
        String endTime = ByteConversionUtils.changeDateFormat(preferences.getString("getEndTimeTransaction", ""),"HHMMSSsss");
        String lastrrn = preferences.getString("getLastRRN", "");

        if(startTime != null && endTime != null) {
            if (startTime.trim().length() != 0 && endTime.trim().length() != 0 ) {
                time[2] = startTime;
                time[3] = endTime;
            }
        }

        if(lastrrn != null && lastrrn.trim().length() != 0)
            time[4] = lastrrn;
        return time;
    }

    public void setAdminReq(boolean b) {
        adminNotification = b;
    }

    public boolean isAdminNotificationReversal(){
        return adminNotification;
    }

    public boolean isDebugEnabled() {
        return getBoolean("DEBUG_ENABLED");
    }

    public void setDebugmode(boolean enable){
        setBoolean("DEBUG_ENABLED",enable);
    }

    public TransactionModelEntity getDebugTransactionModelEntity() {
        preferences = getApplicationContext().getSharedPreferences("debugTransaction", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("debugTransaction", "");
        Logger.v("json --" + json);
        if (json == null || json.trim().length() == 0) {
            //     this.retailerDataModel = null;
            this.transactionModelEntity = null;
        } else {
            this.transactionModelEntity = gson.fromJson(json, TransactionModelEntity.class);
        }
        return transactionModelEntity;
    }

    public void setDenugTransactionModelEntity(TransactionModelEntity transactionModelEntity) {
        Logger.v("SAVEDEBUG -"+transactionModelEntity.toString());
        preferences = getApplicationContext().getSharedPreferences("debugTransaction", Context.MODE_PRIVATE);
        String lastTransaction = new Gson().toJson(transactionModelEntity);
        editor = preferences.edit();
        editor.putString("debugTransaction", lastTransaction);
        editor.apply();
    }

    public String getLocation() {
        return getString(ConstantApp.SET_GPS_LOCATION);
    }

    public List<String> getAidListSplash() {
        preferences = getApplicationContext().getSharedPreferences("aidListDetailsSplash", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        Set<String> set = preferences.getStringSet("aidListSplash", null);
        if (set != null)
            return new ArrayList<>(set);
        else
            return new ArrayList<>();
    }

    public void setAidListDetailsSplash(List<String> aidList) {
        preferences = getApplicationContext().getSharedPreferences("aidListDetailsSplash", Context.MODE_PRIVATE);
        editor = preferences.edit();
        Set<String> set = new HashSet<String>();
        set.addAll(aidList);
        editor.putStringSet("aidListSplash", set);
        editor.apply();
        //  this.aidList = aidList;
    }

    public boolean isMerchantPoratalEnable() {
        return getBoolean("MERCHANT_PORTAL_ENABLED");
    }

    public void setMerchantPortal(boolean enable) {
        setBoolean("MERCHANT_PORTAL_ENABLED", enable);
    }

    private boolean getDefaultBool(String key) {
        if (key.equalsIgnoreCase("MERCHANT_PORTAL_ENABLED"))
            return true;
        return false;
    }

    public String getMerchantIP() {
        return getString(ConstantApp.MERCHANT_IP_CONFIG);
    }

    public String getMerchantPort() {
        return getString(ConstantApp.MERCHANT_PORT);
    }

    public boolean getAPNStatus() {
        boolean stat = getBoolean("APN_LOADED");
        Logger.v("STAT -" + stat);
        return !stat;
    }

}
