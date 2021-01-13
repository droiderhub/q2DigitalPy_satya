package com.tarang.dpq2.tms;

import android.util.Log;

import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.jpos_class.ConstantApp;
import com.tarang.dpq2.model.ReconcileSetupModel;
import com.tarang.dpq2.model.RetailerDataModel;

import java.util.List;

public class RetailerDataParser {

    public static void parseRDTDataSegment1(List<String> fsList) {
        RetailerDataModel retailerDataModel = AppManager.getInstance().getRetailerDataModel();

        //adding extra space for split the string
        Logger.v("De72 segment1: "+fsList.size());

        retailerDataModel.setNextLoad(fsList.get(0).substring(3));
        retailerDataModel.setReconcillationTime(fsList.get(1));
        retailerDataModel.setRetailerNameInArabic(fsList.get(2));
        retailerDataModel.setRetailerNumberEnglish(fsList.get(3));
        retailerDataModel.setRetailerNameEnglish(fsList.get(4));
        retailerDataModel.setRetailerLanguageIndicator(fsList.get(5));
        retailerDataModel.setTerminalCurrencyCode(fsList.get(6));
        retailerDataModel.setTerminalCountryCode(fsList.get(7));
        retailerDataModel.setTransactionCurrencyExponent(fsList.get(8));
        retailerDataModel.setCurrencySymbolArabic(fsList.get(9));
        retailerDataModel.setCurrencySymbolEnglish(fsList.get(10));
        retailerDataModel.setArabicReceipt1(fsList.get(11));
        retailerDataModel.setArabicReceipt2(fsList.get(12));
        retailerDataModel.setEnglishReceipt1(fsList.get(13));
        retailerDataModel.setEnglishReceipt2(fsList.get(14).trim());

        String time = retailerDataModel.getReconcillationTime();
        if(time.length() != 0) {
            String timer = String.format("%02d:%02d", time.substring(0, 2), time.substring(2));
            AppManager.getInstance().setReconcileSetupModel(new ReconcileSetupModel(false, timer, true));
        }else {
            AppManager.getInstance().setReconcileSetupModel(new ReconcileSetupModel(false, "00:00", true));
        }
        AppManager.getInstance().setRetailerDetailsModel(retailerDataModel);
        AppManager.getInstance().setString(ConstantApp.SPRM_PHONE_NUMBER,retailerDataModel.getRetailerNumberEnglish());
    }

    public static void parseRDTDataSegment2(List<String> fsList) {
        RetailerDataModel retailerDataModel = AppManager.getInstance().getRetailerDataModel();

        //adding extra space for split the string
//        String seg2 = segment2 + " ";
//        List<String> fsList = Arrays.asList(seg2.split("\u001C"));
        Log.i("De72 segment2: ", fsList.size()+"");

        retailerDataModel.setRetailerAddress1Arabic(fsList.get(0).substring(3));
        //     retailerDataModel.setRetailerAddress1English(fsList.get(1).substring(0,fsList.get(1).length()-1));
        retailerDataModel.setRetailerAddress1English(fsList.get(1).trim());

        AppManager.getInstance().setRetailerDetailsModel(retailerDataModel);
    }

    public static void parseRDTDataSegment3(List<String> fsList) {
        RetailerDataModel retailerDataModel = AppManager.getInstance().getRetailerDataModel();

        //adding extra space for split the string
//        String seg3 = segment3 + " ";
//        List<String> fsList = Arrays.asList(seg3.split("\u001C"));
        Log.i("De72 segment3: ", fsList.size()+"");

        retailerDataModel.setRetailerAddress2Arabic(fsList.get(0).substring(3));
        retailerDataModel.setRetailerAddress2English(fsList.get(1).trim());

        AppManager.getInstance().setRetailerDetailsModel(retailerDataModel);
    }

    public static void parseRDTDataSegment4(List<String> fsList) {
        RetailerDataModel retailerDataModel = AppManager.getInstance().getRetailerDataModel();

        //adding extra space for split the string
//        String seg4 = segment4 + " ";
//        List<String> fsList = Arrays.asList(seg4.split("\u001C"));
        Log.i("De72 segment4: ", fsList.size()+"");

        retailerDataModel.setTerminalCapability(fsList.get(0).substring(3));
        retailerDataModel.setAdditionalTerminalCapabilities(fsList.get(1));
        retailerDataModel.setDownloadPhoneNumber(fsList.get(2));
        retailerDataModel.seteMVTerminalType(fsList.get(3));
        retailerDataModel.setAutomaticLoad(fsList.get(4));
        retailerDataModel.setsAFRetryLimit(fsList.get(5));
        retailerDataModel.setsAFDefaultMessageTransmissionNumber(fsList.get(6).trim());

        AppManager.getInstance().setRetailerDetailsModel(retailerDataModel);
        AppManager.getInstance().setSafRetryCount(retailerDataModel.getsAFRetryLimit());
        RetailerDataModel retailerDataModel1 = AppManager.getInstance().getRetailerDataModel();

        Log.i("De72 retailerModel: ", retailerDataModel1.toString());
    }
}
