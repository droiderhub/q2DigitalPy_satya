package com.tarang.dpq2.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.tarang.dpq2.base.AppInit;
import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.jpos_class.ConstantApp;
import com.tarang.dpq2.base.room_database.db.AppDatabase;
import com.tarang.dpq2.base.room_database.db.entity.TMSAIDdataModelEntity;
import com.tarang.dpq2.base.room_database.db.entity.TMSPublicKeyModelEntity;
import com.tarang.dpq2.base.room_database.db.entity.TransactionModelEntity;
import com.tarang.dpq2.base.terminal_sdk.q2.AIDTable;
import com.tarang.dpq2.base.terminal_sdk.q2.CAPKTable;
import com.tarang.dpq2.base.terminal_sdk.utils.LoadAID;
import com.tarang.dpq2.model.DeviceSpecificModel;

import java.util.List;

import static com.cloudpos.jniinterface.EMVJNIInterface.emv_aidparam_add;
import static com.cloudpos.jniinterface.EMVJNIInterface.emv_aidparam_clear;
import static com.cloudpos.jniinterface.EMVJNIInterface.emv_capkparam_add;
import static com.cloudpos.jniinterface.EMVJNIInterface.emv_capkparam_clear;
import static com.cloudpos.jniinterface.EMVJNIInterface.emv_kernel_initialize;
import static com.cloudpos.jniinterface.EMVJNIInterface.emv_set_kernel_attr;
import static com.cloudpos.jniinterface.EMVJNIInterface.loadEMVKernel;

/* Validation for Max SAF count and Amount */
public class SAFCountWorker extends Worker {

    AppDatabase database;
    public static boolean DO_SAF_NOW = false;

    public SAFCountWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.database = AppDatabase.getInstance(context.getApplicationContext());

    }

    @NonNull
    @Override
    public Result doWork() {

        /*String tmpEmvLibDir = "";
        tmpEmvLibDir = AppInit.getInstance().getDir("", 0).getAbsolutePath();
        tmpEmvLibDir = tmpEmvLibDir.substring(0, tmpEmvLibDir.lastIndexOf('/')) + "/lib/libEMVKernal.so";

        if (loadEMVKernel(tmpEmvLibDir.getBytes(), tmpEmvLibDir.getBytes().length) == 0) {*/
           // emv_kernel_initialize();
            emv_set_kernel_attr(new byte[]{0x20}, 1);
//        } else
//            Logger.v("EMV Kernal ElSE");

        int clearAllAID = emv_aidparam_clear();
        Logger.v("clearAllAID --" + clearAllAID);
        int clearAllCAPublicKey = emv_capkparam_clear();
        Logger.v("clearAllAID --" + clearAllCAPublicKey);
        List<TMSAIDdataModelEntity> aidList = database.getAIDDataDao().getAllAIDData();
        Logger.v("emvModule.addAID --Size --" + aidList.size());
        for (int j = 0; j < aidList.size(); j++) {
            Logger.v("int val --" + j);
            Logger.v("emvModule.addAID --Size --" + aidList.get(j).toString());
            AIDTable myAIDDataTable = LoadAID.loadAIDS(aidList.get(j));
            int statues;
            if (aidList.get(j).getAid().equalsIgnoreCase(ConstantApp.A0000002281010)) {
//                myAIDDataTable.setKernelConfig((byte)2D);
//                byte[] aidInfo = myAIDDataTable.getDataBuffer();
//                statues = emv_aidparam_add(aidInfo, aidInfo.length);
//                Logger.v("Status --"+statues);
                myAIDDataTable.setKernelConfig((byte)0x20);
                myAIDDataTable.setCvmCapCVMRequired((byte)0x60);   // Online PIN & Signature
                myAIDDataTable.setCvmCapNoCVMRequired((byte)0x68); // Online PIN & Signature & No CVM
                myAIDDataTable.setMscvmCapCVMRequired((byte)0x20);
                myAIDDataTable.setMscvmCapNoCVMRequired((byte)0x20);
                myAIDDataTable.setContactlessKernelID((byte)2);
                myAIDDataTable.setAppVersionNumber("0002"); //set for aid A0000002282010
                byte[] aidInfo = myAIDDataTable.getDataBuffer();
                statues = emv_aidparam_add(aidInfo, aidInfo.length);
            } else{
                byte[] aidInfo = myAIDDataTable.getDataBuffer();
                statues = emv_aidparam_add(aidInfo, aidInfo.length);
            }
            Logger.v("emvModule.addAID --" + j + "--" + statues);
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
        LoadKeyWorker.setEMVTermInfo();



        //        List<TransactionModelEntity> safModelEntities = database.getSAFDao().getAllSuccess(ConstantValue.SAF_APPROVED , ConstantValue.SAF_APPROVED_UNABLE);
        List<TransactionModelEntity> safModelEntities = database.getSAFDao().getAll();
        DeviceSpecificModel deviceSpecificModel1 = AppManager.getInstance().getDeviceSpecificModel();
        if(safModelEntities.size() == 0){
            AppManager.getInstance().setTemprovaryOutService(false);
            return Result.success();
        }
//        else if(AppManager.getInstance().getTemprovaryOutService()){
//            return Result.failure();
//        }

        long count = safModelEntities.size();
        Logger.v("SAF COunt --"+count);
        long amount = 0;

        for(int i=0;i<safModelEntities.size();i++){
            String amt = safModelEntities.get(i).getAmtTransaction4();
            amount = amount + Long.parseLong(amt);;
        }
        Logger.v("SAF amount --"+amount);
        if(deviceSpecificModel1 != null){
            String maxAmt = deviceSpecificModel1.getMaxSAFCumulativeAmount();
            String maxCount = deviceSpecificModel1.getMaxSAFDepth();

            Logger.v("Max Amount --"+maxAmt);
            Logger.v("Max Amount --"+maxCount);

            if(maxAmt != null && maxAmt.trim().length() != 0){
                long maxAmount = Long.parseLong(maxAmt);
                if(maxAmount != 0 && maxAmount <= (amount /100)){
                    return Result.failure();
                }
            }
            if(maxCount != null && maxCount.trim().length() != 0){
                long maxCont = Long.parseLong(maxCount);
                if(maxCont != 0 && maxCont <= count){
                    return Result.failure();
                }
            }
        }
        return Result.success();
    }
}
