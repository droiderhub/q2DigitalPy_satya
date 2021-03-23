package com.tarang.dpq2.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.tarang.dpq2.base.AppInit;
import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.jpos_class.ByteConversionUtils;
import com.tarang.dpq2.base.jpos_class.ConstantApp;
import com.tarang.dpq2.base.jpos_class.ConstantAppValue;
import com.tarang.dpq2.base.room_database.db.AppDatabase;
import com.tarang.dpq2.base.room_database.db.dao.TMSCardSchemeDao;
import com.tarang.dpq2.base.room_database.db.entity.TMSAIDdataModelEntity;
import com.tarang.dpq2.base.room_database.db.entity.TMSCardSchemeEntity;
import com.tarang.dpq2.base.room_database.db.entity.TMSPublicKeyModelEntity;
import com.tarang.dpq2.base.terminal_sdk.q2.AIDTable;
import com.tarang.dpq2.base.terminal_sdk.q2.CAPKTable;
import com.tarang.dpq2.base.terminal_sdk.q2.utils.StringUtil;
import com.tarang.dpq2.base.terminal_sdk.utils.LoadAID;
import com.tarang.dpq2.base.utilities.Utils;
import com.tarang.dpq2.model.DeviceSpecificModel;
import com.tarang.dpq2.model.RetailerDataModel;
import com.tarang.dpq2.view.activities.LandingPageActivity;

import org.jpos.iso.ISOUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.cloudpos.jniinterface.EMVJNIInterface.emv_aidparam_add;
import static com.cloudpos.jniinterface.EMVJNIInterface.emv_aidparam_clear;
import static com.cloudpos.jniinterface.EMVJNIInterface.emv_capkparam_add;
import static com.cloudpos.jniinterface.EMVJNIInterface.emv_capkparam_clear;
import static com.cloudpos.jniinterface.EMVJNIInterface.emv_kernel_initialize;
import static com.cloudpos.jniinterface.EMVJNIInterface.emv_set_kernel_attr;
import static com.cloudpos.jniinterface.EMVJNIInterface.emv_terminal_param_set_drl;
import static com.cloudpos.jniinterface.EMVJNIInterface.emv_terminal_param_set_tlv;
import static com.cloudpos.jniinterface.EMVJNIInterface.exitEMVKernel;
import static com.cloudpos.jniinterface.EMVJNIInterface.loadEMVKernel;
import static com.cloudpos.jniinterface.EMVJNIInterface.registerFunctionListener;

// Injecting CAP keys and AID from database to Terminal
public class LoadKeyWorker extends Worker {
    private final AppDatabase database;
    //    private final EmvModule emvModule;
    public static String INJECT_KEYS = "INJECT_KEYS";
//    private final LoadKeys keys;

    public LoadKeyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        database = AppDatabase.getInstance(context.getApplicationContext());
//        emvModule = SDKDevice.getInstance(context).getEmvModuleType();
//        emvModule.initEmvModule(context);
//        K21Pininput pinkey = SDKDevice.getInstance(context).getK21Pininput();
//        keys = new LoadKeys(context, pinkey);

    }

    @NonNull
    @Override
    public Result doWork() {
        Logger.v("Load Keys init");
        initKernal();
        int clearAllAID = emv_aidparam_clear();
        Logger.v("clearAllAID --" + clearAllAID);
        int clearAllCAPublicKey = emv_capkparam_clear();
        Logger.v("clearAllACAP --" + clearAllCAPublicKey);

        TMSCardSchemeDao cardSchemeDao = database.getTMSCardSchemeDao();
        List<TMSCardSchemeEntity> cardSchemeData = cardSchemeDao.getCardSchemeData();
        List<String> lister = new ArrayList<>();
        for(int i=0;i<cardSchemeData.size();i++){
            lister.add(cardSchemeData.get(i).getCardIndicator().toUpperCase());
        }
        AppManager.getInstance().setAidListDetailsSplash(lister);


        List<TMSAIDdataModelEntity> aidList = database.getAIDDataDao().getAllAIDData();
        Logger.v("emvModule.addAID --Size --" + aidList.size());
        for (int j = 0; j < aidList.size(); j++) {
            Logger.v("int val --" + j);
            Logger.v("emvModule.addAID --Size --" + aidList.get(j).toString());
            AIDTable myAIDDataTable = LoadAID.loadAIDS(aidList.get(j));
            int statues;
            if (aidList.get(j).getAid().equalsIgnoreCase(ConstantApp.A0000002281010)) {
//                myAIDDataTable.setKernelConfig((byte)2D);
                byte[] aidInfo = myAIDDataTable.getDataBuffer();
//                statues = emv_aidparam_add(aidInfo, aidInfo.length);
//                Logger.v("Status --"+statues);
                myAIDDataTable.setKernelConfig((byte)0x20);
                myAIDDataTable.setCvmCapCVMRequired((byte)0x60);   // Online PIN & Signature
                myAIDDataTable.setCvmCapNoCVMRequired((byte)0x68); // Online PIN & Signature & No CVM
                myAIDDataTable.setMscvmCapCVMRequired((byte)0x20);
                myAIDDataTable.setMscvmCapNoCVMRequired((byte)0x20);
                myAIDDataTable.setKernelConfig((byte)2);
                aidInfo = myAIDDataTable.getDataBuffer();
                statues = emv_aidparam_add(aidInfo, aidInfo.length);

                //TODO condition for applepay
                /*myAIDDataTable.setKernelConfig((byte)0x2D);
                aidInfo = myAIDDataTable.getDataBuffer();
                statues = emv_aidparam_add(aidInfo, aidInfo.length);*/



//                String data1 = data + "DF370102";
//                String data2 = data + "DF37012D";
//                statues = emvModule.addAIDWithDataSource(ISOUtils.hex2byte(data1));
//                Logger.v("emvModule.addAID1 --" + j + "--" + statues);
//                statues = emvModule.addAIDWithDataSource(ISOUtils.hex2byte(data2));
//                Logger.v("emvModule.addAID2 --" + j + "--" + statues);
            }
            else if (aidList.get(j).getAid().equalsIgnoreCase(ConstantApp.A0000000041010)) {
                byte[] config = new byte[]{ 0x30};
                byte[] extednedData = new byte[]{(byte) 0xDF, (byte) 0x81, 0x33, 0x02, 0x00, 0x32, (byte) 0xDF, (byte) 0x81, 0x32, 0x02, 0x00, 0x14, (byte) 0xDF, (byte) 0x81, 0x36, 0x02, 0x01, 0x2C, (byte) 0xDF, (byte) 0x81, 0x37, 0x01, 0x32, (byte) 0xDF, (byte) 0x81, 0x34, 0x02, 0x00, 0x0B, (byte) 0xDF, (byte) 0x81, 0x35, 0x02, 0x00, 0x0D};
                String myData = ByteConversionUtils.byteArrayToHexString(extednedData,extednedData.length,false);
                String myConfig = ByteConversionUtils.byteArrayToHexString(config,config.length,false);
               // String data1 = data +"DF2F01"+myConfig+"DF5223"+myData;
               // Logger.v("DATA 11-"+data1);
               // statues = emvModule.addAIDWithDataSource(ISOUtil.hex2byte(data1));
//                Logger.v("emvModule.addAID1 --" + j + "--" + statues);
            } else if (aidList.get(j).getAid().equalsIgnoreCase(ConstantApp.A0000000043060)) {
//                String data1 = data + "DF2F01A0";
//                statues = emvModule.addAIDWithDataSource(ISOUtil.hex2byte(data1));
//                Logger.v("emvModule.addAID1 --" + j + "--" + statues);
            } 
            else{
                byte[] aidInfo = myAIDDataTable.getDataBuffer();
                statues = emv_aidparam_add(aidInfo, aidInfo.length);
            }
//            Logger.v("emvModule.addAID --" + j + "--" + statues);
        }

        List<TMSPublicKeyModelEntity> capKeys = database.getTMSPublicKeyModelDao().getCapKeys();
        Logger.v("Cap Size --" + capKeys.size());
        for (int i = 0; i < capKeys.size(); i++) {
            TMSPublicKeyModelEntity capKey = capKeys.get(i);
            Logger.v("CAP KEY --" + capKey.toString());
            CAPKTable capkTable = new CAPKTable();
            capkTable.setRID(capKey.getRID());
            capkTable.setArithIndex((byte)0x01);
            capkTable.setHashIndex((byte)0x01);
            capkTable.setExpiry(capKey.getCaPublicKeyExpiryDate());
            capkTable.setCapki(capKey.getKeyIndex());
            capkTable.setExponent(capKey.getExponent());
            capkTable.setChecksum(capKey.getCheckSum());
            capkTable.setModul(capKey.getPublicKey());
            byte[] capkInfo = capkTable.getDataBuffer();
            int addResult1 = emv_capkparam_add(capkInfo, capkInfo.length);
            Logger.v(("Add public key,[rid:00000; index:-" + i + "]:") + addResult1);
            Logger.v(("Add public key--" + Integer.parseInt(capKey.getKeyIndex(), 16)));
        }
        AppInit.loadKernal = false;
        Logger.v("ALL CAP KEYS");
//        int exception = emv_exception_file_add(null);
//        Logger.v("Exception --"+exception);
//        int exception1 = emv_revoked_cert_add(null);
//        Logger.v("Exception --"+exception1);
        setEMVTermInfo();
//        List<CAPublicKey> keys1 = emvModule.fetchAllCAPublicKey();
//        for (int i = 0; i < keys1.size(); i++) {
//            Logger.v("KEYS -" + i + "-" + keys1.get(i).toString());
//        }
        return Result.success();
    }

    public static void initKernal() {
        /*Logger.v("Init Kernal");
        String tmpEmvLibDir = "";
        tmpEmvLibDir = AppInit.getInstance().getDir("", 0).getAbsolutePath();
        tmpEmvLibDir = tmpEmvLibDir.substring(0, tmpEmvLibDir.lastIndexOf('/')) + "/lib/libEMVKernal.so";

        if (loadEMVKernel(tmpEmvLibDir.getBytes(), tmpEmvLibDir.getBytes().length) == 0) {*/
           // emv_kernel_initialize();
        emv_set_kernel_attr(new byte[]{0x20, 0x08}, 2);
//            emv_terminal_param_set_drl(new byte[]{0x00},1);
//        } else
//            Logger.v("EMV Kernal ElSE");

    }

    public static void emvKernelInit() {
        Logger.v("emvKernelInit()");
        if (LandingPageActivity.isTxnCancelled) {
            Logger.v("Iscancelled : "+ LandingPageActivity.isTxnCancelled);
            exitEMVKernel();
        }

        String tmpEmvLibDir = "";
        tmpEmvLibDir = AppInit.getInstance().getDir("", 0).getAbsolutePath();
        tmpEmvLibDir = tmpEmvLibDir.substring(0, tmpEmvLibDir.lastIndexOf('/')) + "/lib/libEMVKernal.so";
        if (loadEMVKernel(tmpEmvLibDir.getBytes(), tmpEmvLibDir.getBytes().length) == 0) {
            emv_kernel_initialize();
            Logger.v("emvKernelInit_success");
        }else {
            Logger.v("emvKernelInit_failed");
        }
    }

    public static void emvKernelInitForLanding() {
        Logger.v("emvKernelInit()");
        if (LandingPageActivity.isTxnCancelled) {
            Logger.v("Iscancelled : "+ LandingPageActivity.isTxnCancelled);
            exitEMVKernel();
            String tmpEmvLibDir = "";
            tmpEmvLibDir = AppInit.getInstance().getDir("", 0).getAbsolutePath();
            tmpEmvLibDir = tmpEmvLibDir.substring(0, tmpEmvLibDir.lastIndexOf('/')) + "/lib/libEMVKernal.so";
            if (loadEMVKernel(tmpEmvLibDir.getBytes(), tmpEmvLibDir.getBytes().length) == 0) {
                emv_kernel_initialize();
                Logger.v("emvKernelInit_success");
            }else {
                Logger.v("emvKernelInit_failed");
            }
        }
    }

    public static void setEMVTermInfo() {
        DeviceSpecificModel deviceSpecificModel1 = AppManager.getInstance().getDeviceSpecificModel();
        RetailerDataModel retailerDataModel = AppManager.getInstance().getRetailerDataModel();
        byte[] termInfo = new byte[256];
        int offset = 0;
        // 5F2A: Transaction Currency Code
        termInfo[offset] = (byte) 0x5F;
        termInfo[offset + 1] = 0x2A;
        termInfo[offset + 2] = 2;
        offset += 3;
        System.arraycopy(StringUtil.hexString2bytes("0" + retailerDataModel.getTerminalCurrencyCode()), 0, termInfo, offset, 2);
        offset += 2;
//        // 5F36: Transaction Currency Exponent
//        termInfo[offset] = (byte)0x5F;
//        termInfo[offset+1] = 0x36;
//        termInfo[offset+2] = 1;
//        termInfo[offset+3] = appState.terminalConfig.getCurrencyExponent();
//        offset += 4;
        // 9F16: Merchant Identification
        String mid = AppManager.getInstance().getCardAcceptorCode42();
        if (mid.trim().length() == 12) {
            termInfo[offset] = (byte) 0x9F;
            termInfo[offset + 1] = 0x16;
            termInfo[offset + 2] = 12;
            offset += 3;
            System.arraycopy(mid.getBytes(), 0, termInfo, offset, 12);
            offset += 12;
        }
        // 9F1A: Terminal Country Code
        termInfo[offset] = (byte) 0x9F;
        termInfo[offset + 1] = 0x1A;
        termInfo[offset + 2] = 2;
        offset += 3;
        System.arraycopy(StringUtil.hexString2bytes("0" + retailerDataModel.getTerminalCountryCode()),
                0, termInfo, offset, 2);
        offset += 2;
        // 9F1C: Terminal Identification
        String tid = AppManager.getInstance().getCardAcceptorID41();
        if (tid.length() == 8) {
            termInfo[offset] = (byte) 0x9F;
            termInfo[offset + 1] = 0x1C;
            termInfo[offset + 2] = 8;
            offset += 3;
            System.arraycopy(tid.getBytes(), 0, termInfo, offset, 8);
            offset += 8;
        }
        // 9F1E: IFD Serial Number
        String ifd = AppManager.getInstance().getVendorTerminalSerialNumber();
        if (ifd.length() > 0) {
            termInfo[offset] = (byte) 0x9F;
            termInfo[offset + 1] = 0x1E;
            termInfo[offset + 2] = (byte) ifd.length();
            offset += 3;
            System.arraycopy(ifd.getBytes(), 0, termInfo, offset, ifd.length());
            offset += ifd.length();
        }

        //setting floor limit 9f1B
        String vfloorLimit = AppManager.getInstance().getFloorLimit(ConstantAppValue.A0000002281010);
        String floorLimit= (String.format("%08d", Integer.parseInt(vfloorLimit)));
        Logger.v("terminal_floor_limit_loadkeyworker----"+floorLimit);

        if (floorLimit.length() > 0) {
            termInfo[offset] = (byte) 0x9F;
            termInfo[offset + 1] = 0x1B;
            termInfo[offset + 2] = 4;
            offset += 3;
            System.arraycopy(StringUtil.intToByte4((Integer.parseInt(floorLimit))), 0, termInfo, offset, 4);
            offset += 4;
        }


        // 9F33: Terminal Capabilities
        termInfo[offset] = (byte) 0x9F;
        termInfo[offset + 1] = 0x33;
        termInfo[offset + 2] = 3;
        offset += 3;
        System.arraycopy(StringUtil.hexString2bytes(retailerDataModel.getTerminalCapability()),
                0, termInfo, offset, 3);
        offset += 3;
        // 9F35: Terminal Type
        termInfo[offset] = (byte) 0x9F;
        termInfo[offset + 1] = 0x35;
        termInfo[offset + 2] = 1;
        termInfo[offset + 3] = StringUtil.hexString2bytes(retailerDataModel.geteMVTerminalType())[0];
        offset += 4;
        // 9F40: Additional Terminal Capabilities
        termInfo[offset] = (byte) 0x9F;
        termInfo[offset + 1] = 0x40;
        termInfo[offset + 2] = 3;
        offset += 3;
        System.arraycopy(StringUtil.hexString2bytes(retailerDataModel.getTerminalCapability()),
                0, termInfo, offset, 3);
        offset += 3;
//        // 9F4E: Merchant Name and Location
//        int merNameLength = appState.terminalConfig.getMerchantName1().length();
//        if (merNameLength > 0) {
//            termInfo[offset] = (byte)0x9F;
//            termInfo[offset+1] = 0x4E;
//            termInfo[offset+2] = (byte)merNameLength;
//            offset += 3;
//            System.arraycopy(appState.terminalConfig.getMerchantName1().getBytes(), 0, termInfo, offset, merNameLength);
//            offset += merNameLength;
//        }
//        // 9F66: TTQ first byte
        termInfo[offset] = (byte)0x9F;
        termInfo[offset+1] = 0x66;
        termInfo[offset+2] = 1;
        termInfo[offset+3] = 0x36;
        offset += 4;
//        // DF19: Contactless floor limit
//        if(appState.terminalConfig.getContactlessFloorLimit() >= 0)
//        {
//            termInfo[offset] = (byte)0xDF;
//            termInfo[offset+1] = 0x19;
//            termInfo[offset+2] = 6;
//            offset += 3;
//            System.arraycopy(NumberUtil.intToBcd(appState.terminalConfig.getContactlessFloorLimit(), 6),
//                    0, termInfo, offset, 6);
//            offset += 6;
//        }
//        // DF20: Contactless transaction limit
//        if(appState.terminalConfig.getContactlessLimit() >= 0)
//        {
//            termInfo[offset] = (byte)0xDF;
//            termInfo[offset+1] = 0x20;
//            termInfo[offset+2] = 6;
//            offset += 3;
//            System.arraycopy(NumberUtil.intToBcd(appState.terminalConfig.getContactlessLimit(), 6),
//                    0, termInfo, offset, 6);
//            offset += 6;
//        }
//        // DF21: CVM limit
//        if(appState.terminalConfig.getCvmLimit() >= 0)
//        {
//            termInfo[offset] = (byte)0xDF;
//            termInfo[offset+1] = 0x21;
//            termInfo[offset+2] = 6;
//            offset += 3;
//            System.arraycopy(NumberUtil.intToBcd(appState.terminalConfig.getCvmLimit(), 6),
//                    0, termInfo, offset, 6);
//            offset += 6;
//        }
//        // EF01: Status check support
//        termInfo[offset] = (byte)0xEF;
//        termInfo[offset+1] = 0x01;
//        termInfo[offset+2] = 1;
//        termInfo[offset+3] = appState.terminalConfig.getStatusCheckSupport();
//        offset += 4;

        int info = emv_terminal_param_set_tlv(termInfo, offset);
        Logger.v("SET TERMINAL INFO --" + info);

    }

    public Date getDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        try {
            Logger.v("Date");
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Logger.v("Date null");
        return null;
    }
}
