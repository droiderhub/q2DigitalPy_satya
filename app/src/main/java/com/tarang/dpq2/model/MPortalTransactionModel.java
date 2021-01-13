package com.tarang.dpq2.model;

import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.jpos_class.ByteConversionUtils;

import org.jpos.iso.ISOUtil;

import java.io.UnsupportedEncodingException;

public class MPortalTransactionModel {

    PrinterModel printerModel;
    public static String SEND_SMS = "2";
    public static String SEND_MAIL = "3";
    public static String SEND_QR = "4";

    String merchantName = ""; //Digital Pay
    String transactionType = ""; //PURCHASE
    String terminalDate = ""; //19/10/2020
    String terminalTime = ""; //19:05:16
    String cardName = ""; //RAJB
    String tId = ""; //1234567812121234
    String mId = ""; //800150400566
    String mcc = ""; //7399
    String stan = ""; //100069
    String applicationVersion = ""; //V1.1.2
    String cardSchemaName = ""; //mada
    String cardNumberMask = ""; //455036******7619
    String cardExp = ""; //12/20
    String amount = ""; //SAR111.11
    String naqdAmount = ""; //SAR111.11
    String totalAmount = ""; //SAR111.11
    String transactionStatus = ""; //APPROVED
    String approvalCode = ""; //097619
    String serverDate = ""; //19/10/2020
    String serverTime = ""; //19:05:06
    String cardType = ""; //CONTACTLESS
    String de39 = ""; //000
    String cardAId = ""; //A0000002282010
    String tvr = ""; //0000000000
    String tsi = ""; //0000
    String cvm = ""; //
    String aqrc = ""; //80
    String emvdata = ""; //B623ADA452BF6CB6
    String emvkernel = ""; //03
    String rrn = ""; //029319190442

    String ErptType = "";
    String ErptData = "";
    String cardholder_verification_declined_reason = "";
    String signature_required = "";
    String tag_data_44 = "";
    private String pendingReq = "";


    public MPortalTransactionModel(PrinterModel printerModel) {
        this.printerModel = printerModel;
    }

    public String mergePrintModel(String status, String data) {
//        printerModel.getRetailerNameArabic().trim();
//        printerModel.getTerminalCityArabic() + ", " + printerModel.getTerminalStreetArabic();
//        printerModel.getTerminalStreetEnglish() + ", " + printerModel.getTerminaCityEnglish();
//        printerModel.getApplicationLabelArabic();
//        printerModel.getTransactionTypeArabic();
//        printerModel.getPurchaseAmountStringArabic();
//        printerModel.getPurchaseAmountArabic() + "\n");
//        printerModel.getPurchaseWithCashBackAmountStringArabic() + "\n");
//        printerModel.getPurchaseWithCashBackAmountArabic() + "\n");
//        printerModel.getTotalAmountArabic() + "\n");
//        printerModel.getAmountArabic() + "\n");
//        printerModel.getPurchaseAmountStringEnglish() + "\n");
//        printerModel.getPurchaseWithCashBackAmountStringEnglish() + "\n");
//        printerModel.getPurchaseWithCashBackAmountEnglish() + "\n");
//
//        printerModel.getTotalAmountEnglish() + "\n");
//        printerModel.getAmountEnglish() + "\n");
//        printerModel.getTransactionOutcomeArabic() + "\n");
//
//        printerModel.getCardHolderVerificationOrReasonForDeclineArabic().trim() + "\n");
//        if (printerModel.getCardHolderVerificationOrReasonForDeclineEnglish() != null)
//            if (printerModel.getCardHolderVerificationOrReasonForDeclineEnglish().contains("---")) {
//                String[] splitVal = printerModel.getCardHolderVerificationOrReasonForDeclineEnglish().split("---");
//                splitVal[0].trim() + "\n");
//                splitVal[1].trim() + "\n");
//            } else
//                printerModel.getCardHolderVerificationOrReasonForDeclineEnglish().trim() + "\n");
//
//        printerModel.getSignBelowArabic() + "\n");
//        printerModel.getSignBelowEnglish() + "\n");
//        printerModel.getUnderline() + "\n");
//        printerModel.getAccountForTheAmountArabic() + "\n");
//        printerModel.getAccountForTheAmountEnglish() + "\n");
//        printerModel.getApprovalCodeArabic() + printerModel.getApprovalCodeStringArabic() + "\n");
//        printerModel.getApprovalCodeStringEnglish() + printerModel.getApprovalCodeEnglish() + "\n");
//        printerModel.getThankYouArabic() + "\n");
//        printerModel.getThankYouEnglish() + "\n");
//        printerModel.getPleaseRetainYourReceiptArabic() + "\n");
//        printerModel.getPleaseRetainYourReceiptEnglish() + "\n");
//        printerModel.getReceiptVersionArabic() + " *\n");
//        printerModel.getReceiptVersionEnglish() + " *\n");
//        printerModel.getData44() + "\n"); //SMALL CENTER
        merchantName = printerModel.getRetailerNameEnglish(); //Digital Pay
        transactionType = printerModel.getTransactionTypeEnglish().replaceAll(" ", ""); //PURCHASE
        terminalDate = printerModel.getStartDate();//19/10/2020
        terminalTime = printerModel.getStartTime(); //19:05:06
        serverDate = printerModel.getEndDate(); //19/10/2020
        serverTime = printerModel.getEndTime(); //19:05:16

        cardName = printerModel.getbId(); //RAJB
        tId = printerModel.gettId(); //1234567812121234
        mId = printerModel.getmId().replaceAll(" ", ""); //800150400566
        mcc = printerModel.getMcc(); //7399
        stan = printerModel.getStan(); //100069
        applicationVersion = printerModel.getPosSoftwareVersionNumber(); //V1.1.2
        rrn = printerModel.getRrn(); //029319190442

        cardSchemaName = printerModel.getApplicationLabelEnglish(); //mada
        cardNumberMask = printerModel.getPan(); //455036******7619
        cardExp = printerModel.getCardExpry(); //12/20

        transactionStatus = printerModel.getTransactionOutcomeEnglish(); //APPROVED
        approvalCode = printerModel.getApprovalCodeEnglish(); //097619


        cardType = printerModel.getPosEntryMode(); //CONTACTLESS
        de39 = printerModel.getAlpharesponseCode(); //000
        cardAId = printerModel.getAid(); //A0000002282010
        tvr = printerModel.getTvr(); //0000000000
        tsi = printerModel.getTsi(); //0000
        cvm = printerModel.getCvr(); //
        aqrc = printerModel.getApplicationCryptogramInfo(); //80
        emvdata = printerModel.getApplicationCryptogram(); //B623ADA452BF6CB6
        emvkernel = printerModel.getKernalId(); //03
        ErptType = status;
        ErptData = data;
        cardholder_verification_declined_reason = printerModel.getCardHolderVerificationOrReasonForDeclineEnglish();
        tag_data_44 = printerModel.getData44();
        signature_required = (printerModel.getUnderline() == null) ? "0" : "1";
        if (printerModel.getPurchaseWithCashBackAmountEnglish() != null) {
            amount = printerModel.getPurchaseAmountEnglish(); //SAR111.11
            naqdAmount = printerModel.getPurchaseWithCashBackAmountEnglish(); //SAR111.11
            totalAmount = printerModel.getAmountEnglish(); //SAR111.11
            pendingReq = NAQDtoStringPending();
            return NAQDtoString();
        } else {
            amount = printerModel.getAmountEnglish();
            pendingReq = transactiontoStringPending();
            return transactiontoString();
        }
    }
//    public String mergePrintModel(String status,String data) {
//        merchantName = "Digital Pay";//printerModel.getRetailerNameEnglish(); //Digital Pay
//        transactionType = "PURCHASE";
//        terminalDate = "08/12/2020";
//        terminalTime = "09:26:37";
//        cardName = "RAJB";
//        tId = "1234567812121234";
//        mId = "800150400566";
//        mcc = "7399";
//        stan = "100533";
//        applicationVersion = "V1.1.2";
//        rrn = "034309092626"; //029319190442
//
//        cardSchemaName = "mada"; //mada
//        cardNumberMask = "455036******7619"; //455036******7619
//        cardExp = "12/20"; //12/20
//
//        transactionStatus = "APPROVED"; //APPROVED
//        approvalCode = "097619"; //097619
//
//        serverDate = "08/12/2020";//19/10/2020
//        serverTime = "09:26:50"; //19:05:06
//        cardType = "CONTACTLESS"; //CONTACTLESS
//        de39 = "000"; //000
//        cardAId = "A0000002282010"; //A0000002282010
//        tvr = "0000000000"; //0000000000
//        tsi = "0000"; //0000
//        cvm = ""; //
//        aqrc = "80";
//        emvdata = "AB8CA9CA6C46876F";
//        emvkernel = "03";
//        ErptType = status;
//        ErptData = data;
//        cardholder_verification_declined_reason = printerModel.getCardHolderVerificationOrReasonForDeclineEnglish();
//        tag_data_44 = printerModel.getData44();
//        signature_required = (printerModel.getUnderline() == null)?"0":"1";
//        if (printerModel.getPurchaseWithCashBackAmountEnglish() != null) {
//            amount = printerModel.getPurchaseAmountEnglish(); //SAR111.11
//            naqdAmount = printerModel.getPurchaseWithCashBackAmountEnglish(); //SAR111.11
//            totalAmount = printerModel.getAmountEnglish(); //SAR111.11
//            return NAQDtoString();
//        } else {
//            amount = "SAR1444.44";
//            return transactiontoString();
//        }
//    }

    public String transactiontoString() {
        return "{" +
                "\"merchantName\":\"" + merchantName + "\"" +
                ",\"ErptType\":\"" + ErptType + "\"" +
                ",\"ErptData\":\"" + ErptData + "\"" +
                ",\"terminalType\":\"" + "N910" + "\"" +
                ",\"request_stat\":\"" + "active" + "\"" +
                ",\"cardholder_verification_declined_reason\":\"" + cardholder_verification_declined_reason + "\"" +
                ",\"signature_required\":\"" + signature_required + "\"" +
                ",\"tag_data_44\":\"" + tag_data_44 + "\"" +
                ",\"transactionType\":\"" + transactionType + "\"" +
                ",\"terminalDate\":\"" + terminalDate + "\"" +
                ",\"terminalTime\":\"" + terminalTime + "\"" +
                ",\"serverTime\":\"" + serverTime + "\"" +
                ",\"serverDate\":\"" + serverDate + "\"" +
                ",\"cardName\":\"" + cardName + "\"" +
                ",\"tId\":\"" + tId + "\"" +
                ",\"mId\":\"" + mId + "\"" +
                ",\"mcc\":\"" + mcc + "\"" +
                ",\"stan\":\"" + stan + "\"" +
                ",\"applicationVersion\":\"" + applicationVersion + "\"" +
                ",\"cardSchemaName\":\"" + cardSchemaName + "\"" +
                ",\"cardNumberMask\":\"" + cardNumberMask + "\"" +
                ",\"cardExp\":\"" + cardExp + "\"" +
                ",\"amount\":\"" + amount + "\"" +
                ",\"transactionStatus\":\"" + transactionStatus + "\"" +
                ",\"approvalCode\":\"" + approvalCode + "\"" +
                ",\"serverDate\":\"" + serverDate + "\"" +
                ",\"serverTime\":\"" + serverTime + "\"" +
                ",\"cardType\":\"" + cardType + "\"" +
                ",\"de39\":\"" + de39 + "\"" +
                ",\"cardAId\":\"" + cardAId + "\"" +
                ",\"tvr\":\"" + tvr + "\"" +
                ",\"tsi\":\"" + tsi + "\"" +
                ",\"cvm\":\"" + cvm + "\"" +
                ",\"aqrc\":\"" + aqrc + "\"" +
                ",\"emvdata\":\"" + emvdata + "\"" +
                ",\"emvkernel\":\"" + emvkernel + "\"" +
                ",\"rrn\":\"" + rrn + "\"";
    }

    public String NAQDtoString() {
        return "{" +
                "\"merchantName\":\"" + merchantName + "\"" +
                ",\"ErptType\":\"" + ErptType + "\"" +
                ",\"ErptData\":\"" + ErptData + "\"" +
                ",\"terminalType\":\"" + "N910" + "\"" +
                ",\"request_stat\":\"" + "active" + "\"" +
                ",\"cardholder_verification_declined_reason\":\"" + cardholder_verification_declined_reason + "\"" +
                ",\"signature_required\":\"" + signature_required + "\"" +
                ",\"tag_data_44\":\"" + tag_data_44 + "\"" +
                ",\"transactionType\":\"" + "NAQD" + "\"" +
                ",\"terminalDate\":\"" + terminalDate + "\"" +
                ",\"terminalTime\":\"" + terminalTime + "\"" +
                ",\"serverTime\":\"" + serverTime + "\"" +
                ",\"serverDate\":\"" + serverDate + "\"" +
                ",\"cardName\":\"" + cardName + "\"" +
                ",\"tId\":\"" + tId + "\"" +
                ",\"mId\":\"" + mId + "\"" +
                ",\"mcc\":\"" + mcc + "\"" +
                ",\"stan\":\"" + stan + "\"" +
                ",\"applicationVersion\":\"" + applicationVersion + "\"" +
                ",\"cardSchemaName\":\"" + cardSchemaName + "\"" +
                ",\"cardNumberMask\":\"" + cardNumberMask + "\"" +
                ",\"cardExp\":\"" + cardExp + "\"" +
                ",\"amount\":\"" + amount + "\"" +
                ",\"naqdAmount\":\"" + naqdAmount + "\"" +
                ",\"totalAmount\":\"" + totalAmount + "\"" +
                ",\"transactionStatus\":\"" + transactionStatus + "\"" +
                ",\"approvalCode\":\"" + approvalCode + "\"" +
                ",\"serverDate\":\"" + serverDate + "\"" +
                ",\"serverTime\":\"" + serverTime + "\"" +
                ",\"cardType\":\"" + cardType + "\"" +
                ",\"de39\":\"" + de39 + "\"" +
                ",\"cardAId\":\"" + cardAId + "\"" +
                ",\"tvr\":\"" + tvr + "\"" +
                ",\"tsi\":\"" + tsi + "\"" +
                ",\"cvm\":\"" + cvm + "\"" +
                ",\"aqrc\":\"" + aqrc + "\"" +
                ",\"emvdata\":\"" + emvdata + "\"" +
                ",\"emvkernel\":\"" + emvkernel + "\"" +
                ",\"rrn\":\"" + rrn + "\"";
    }

    public String transactiontoStringPending() {
        return "{" +
                "\"merchantName\":\"" + merchantName + "\"" +
                ",\"ErptType\":\"" + ErptType + "\"" +
                ",\"ErptData\":\"" + ErptData + "\"" +
                ",\"terminalType\":\"" + "N910" + "\"" +
                ",\"request_stat\":\"" + "pending" + "\"" +
                ",\"cardholder_verification_declined_reason\":\"" + cardholder_verification_declined_reason + "\"" +
                ",\"signature_required\":\"" + signature_required + "\"" +
                ",\"tag_data_44\":\"" + tag_data_44 + "\"" +
                ",\"transactionType\":\"" + transactionType + "\"" +
                ",\"terminalDate\":\"" + terminalDate + "\"" +
                ",\"terminalTime\":\"" + terminalTime + "\"" +
                ",\"serverTime\":\"" + serverTime + "\"" +
                ",\"serverDate\":\"" + serverDate + "\"" +
                ",\"cardName\":\"" + cardName + "\"" +
                ",\"tId\":\"" + tId + "\"" +
                ",\"mId\":\"" + mId + "\"" +
                ",\"mcc\":\"" + mcc + "\"" +
                ",\"stan\":\"" + stan + "\"" +
                ",\"applicationVersion\":\"" + applicationVersion + "\"" +
                ",\"cardSchemaName\":\"" + cardSchemaName + "\"" +
                ",\"cardNumberMask\":\"" + cardNumberMask + "\"" +
                ",\"cardExp\":\"" + cardExp + "\"" +
                ",\"amount\":\"" + amount + "\"" +
                ",\"transactionStatus\":\"" + transactionStatus + "\"" +
                ",\"approvalCode\":\"" + approvalCode + "\"" +
                ",\"serverDate\":\"" + serverDate + "\"" +
                ",\"serverTime\":\"" + serverTime + "\"" +
                ",\"cardType\":\"" + cardType + "\"" +
                ",\"de39\":\"" + de39 + "\"" +
                ",\"cardAId\":\"" + cardAId + "\"" +
                ",\"tvr\":\"" + tvr + "\"" +
                ",\"tsi\":\"" + tsi + "\"" +
                ",\"cvm\":\"" + cvm + "\"" +
                ",\"aqrc\":\"" + aqrc + "\"" +
                ",\"emvdata\":\"" + emvdata + "\"" +
                ",\"emvkernel\":\"" + emvkernel + "\"" +
                ",\"rrn\":\"" + rrn + "\"";
    }

    public String NAQDtoStringPending() {
        return "{" +
                "\"merchantName\":\"" + merchantName + "\"" +
                ",\"ErptType\":\"" + ErptType + "\"" +
                ",\"ErptData\":\"" + ErptData + "\"" +
                ",\"terminalType\":\"" + "N910" + "\"" +
                ",\"request_stat\":\"" + "pending" + "\"" +
                ",\"cardholder_verification_declined_reason\":\"" + cardholder_verification_declined_reason + "\"" +
                ",\"signature_required\":\"" + signature_required + "\"" +
                ",\"tag_data_44\":\"" + tag_data_44 + "\"" +
                ",\"transactionType\":\"" + "NAQD" + "\"" +
                ",\"terminalDate\":\"" + terminalDate + "\"" +
                ",\"terminalTime\":\"" + terminalTime + "\"" +
                ",\"serverTime\":\"" + serverTime + "\"" +
                ",\"serverDate\":\"" + serverDate + "\"" +
                ",\"cardName\":\"" + cardName + "\"" +
                ",\"tId\":\"" + tId + "\"" +
                ",\"mId\":\"" + mId + "\"" +
                ",\"mcc\":\"" + mcc + "\"" +
                ",\"stan\":\"" + stan + "\"" +
                ",\"applicationVersion\":\"" + applicationVersion + "\"" +
                ",\"cardSchemaName\":\"" + cardSchemaName + "\"" +
                ",\"cardNumberMask\":\"" + cardNumberMask + "\"" +
                ",\"cardExp\":\"" + cardExp + "\"" +
                ",\"amount\":\"" + amount + "\"" +
                ",\"naqdAmount\":\"" + naqdAmount + "\"" +
                ",\"totalAmount\":\"" + totalAmount + "\"" +
                ",\"transactionStatus\":\"" + transactionStatus + "\"" +
                ",\"approvalCode\":\"" + approvalCode + "\"" +
                ",\"serverDate\":\"" + serverDate + "\"" +
                ",\"serverTime\":\"" + serverTime + "\"" +
                ",\"cardType\":\"" + cardType + "\"" +
                ",\"de39\":\"" + de39 + "\"" +
                ",\"cardAId\":\"" + cardAId + "\"" +
                ",\"tvr\":\"" + tvr + "\"" +
                ",\"tsi\":\"" + tsi + "\"" +
                ",\"cvm\":\"" + cvm + "\"" +
                ",\"aqrc\":\"" + aqrc + "\"" +
                ",\"emvdata\":\"" + emvdata + "\"" +
                ",\"emvkernel\":\"" + emvkernel + "\"" +
                ",\"rrn\":\"" + rrn + "\"";
    }

    public byte[] appendCount(String text) {
        byte[] inputData = new byte[0];
        try {
            inputData = ((text.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] appendData1 = ISOUtil.concat( ByteConversionUtils.HexStringToByteArray("2C"),inputData);
        byte[] appendData = ISOUtil.concat(appendData1, ByteConversionUtils.HexStringToByteArray("0D0A7D2C"));
        Logger.v(appendData);
        return appendData;
    }

    public byte[] get100Req() {
        String count = "{\"Count\":\"" + 50 + "\"";
        try {
            byte[] countArray = count.getBytes("UTF-8");
            byte[] appendData = ISOUtil.concat(countArray, appendCount(transactiontoString()));
            for (int i = 1; i < 50; i++) {
                appendData = ISOUtil.concat(appendData, appendCount(transactiontoString()));
            }
            byte[] finalData = ISOUtil.concat(appendData, ByteConversionUtils.HexStringToByteArray("0D0A7D"));
            AppManager.getInstance().setString("sample55",ByteConversionUtils.byteArrayToHexString(finalData,finalData.length,false));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Logger.v("Exceptionee");
        }
        return null;
    }

    public String getPendingAppend() {
        return pendingReq;
    }
}
