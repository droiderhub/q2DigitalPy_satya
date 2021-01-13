package com.tarang.dpq2.base.terminal_sdk.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.cloudpos.DeviceException;
import com.cloudpos.printer.Format;
import com.cloudpos.printer.PrinterDevice;
import com.tarang.dpq2.BuildConfig;
import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.jpos_class.ConstantApp;
import com.tarang.dpq2.base.jpos_class.ConstantAppValue;
import com.tarang.dpq2.base.room_database.db.AppDatabase;
import com.tarang.dpq2.base.room_database.db.entity.TMSCardSchemeEntity;
import com.tarang.dpq2.base.room_database.db.entity.TransactionModelEntity;
import com.tarang.dpq2.base.utilities.Utils;
import com.tarang.dpq2.model.KeyValueModel;
import com.tarang.dpq2.model.PrinterModel;
import com.tarang.dpq2.model.ReconcilationTopModel;
import com.tarang.dpq2.model.ReconciliationBottomModel;
import com.tarang.dpq2.model.ReconciliationCardSchemeModel;
import com.tarang.dpq2.model.RetailerDataModel;
import com.tarang.dpq2.viewmodel.BaseViewModel;
import com.cloudpos.sdk.printer.html.PrinterHtmlListener;

import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.tarang.dpq2.base.jpos_class.ConstantApp.RECEIPT_TYPE_RECONCILIATION;
import static com.tarang.dpq2.base.terminal_sdk.AppConfig.EMV.reqObj;

public class PrinterReceipt {
    public static String printData = "";

    //******************************print key value for q2*******************************************
//    public static boolean printKeyValue(List<KeyValueModel> list, PrinterDevice printerDevice, Context context, final BaseViewModel.PrintComplete complete) throws DeviceException {
//        StringBuffer keyValueScriptBuffer = new StringBuffer();
//        keyValueScriptBuffer.append("<!DOCTYPE html>" +
//                "<html>" +
//                "<head>" +
//                "    <style type=\"text/css\">" +
//                "     * {" +
//                "        margin:0;" +
//                "        padding:0;" +
//                "font-family:Arial Black;" +
//                "     }" +
//                "    </style>" +
//                "</head>" +
//                "<body>");
//        for (int i = 0; i < list.size(); i++) {
//            if (list.get(i).getKey().trim().length() != 0) {
//                Logger.v("KEY --" + list.get(i).getKey().trim() + "--");
//                keyValueScriptBuffer.append("<br />" + "<div style=\"font-size:15px; font-weight:500\">" + list.get(i).getKey().trim() + "</div>"); // small left
//
//                if (list.get(i).getValue() != null && list.get(i).getValue().trim().length() != 0) {
//                    Logger.v("Val --" + list.get(i).getValue().trim() + "--");
//                    if (Utils.isProbablyArabic(list.get(i).getKey().trim())) {
//                        keyValueScriptBuffer.append("<br />" + "<div style=\"font-size:15px; font-weight:500\">" + list.get(i).getValue().trim() + "</div>"); // small left
//
//                    } else {
//                        keyValueScriptBuffer.append("<br />" + "<div style=\"font-size:15px; font-weight:500\">" + list.get(i).getValue().trim() + "</div>"); // small left
//
//                    }
//
//                }
//            }
//
//            if (8888 < keyValueScriptBuffer.length()) {
//                printerDevice.open();
//                printerDevice.printHTML(context, keyValueScriptBuffer.toString(), null);
//                //device.close();
//                printerDevice.queryStatus();
////                    printerResult = mPrinter.printByScript(PrintContext.defaultContext(), scriptBuffer.toString(), map, 60, TimeUnit.SECONDS);
//                keyValueScriptBuffer = new StringBuffer();
//            }
//        }
////            scriptBuffer.append("*feedline 2\n");
//        keyValueScriptBuffer.append("\"<br />\" +\n" +
//                "                    \"<br />\" +\n" +
//                "                    \"<br />\" +");
//        Logger.v("scriptBuffer.toString() 1--" + keyValueScriptBuffer.length());
//
//        printerDevice.open();
//        printerDevice.printHTML(context, keyValueScriptBuffer.toString(), new PrinterHtmlListener() {
//            @Override
//            public void onGet(Bitmap bitmap, int i) {
//
//            }
//
//            @Override
//            public void onFinishPrinting(int i) {
//                complete.onFinish();
//            }
//        });
//        //device.close();
//
////            printerResult = mPrinter.printByScript(PrintContext.defaultContext(), scriptBuffer.toString(), map, 60, TimeUnit.SECONDS);
////            Logger.v("printerResult --" + printerResult.toString());
//        if (printerDevice.queryStatus() == 1) {
//            Logger.v("Logger.v +Success");
//            return true;
//            //   showMessage(context.getString(R.string.msg_print_script_success) + "\r\n", MessageTag.NORMAL);
//        } else {
//            Logger.v("Logger.v +Failed");
//            return false;
//            //   showMessage(context.getString(R.string.msg_print_script_error) + printerResult.toString() + "\r\n", MessageTag.NORMAL);
//        }
//
//    }

    public static boolean printKeyValue(List<KeyValueModel> list, final PrinterDevice printerDevice, Context context, final BaseViewModel.PrintComplete complete) throws DeviceException {
        StringBuffer keyValueScriptBuffer = new StringBuffer();
//        printerDevice.open();
        keyValueScriptBuffer.append("<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <style type=\"text/css\">" +
                "     * {" +
                "        margin:0;" +
                "        padding:0;" +
                "font-family:Arial Black;" +
                "     }" +
                "    </style>" +
                "</head>" +
                "<body>");
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getKey().trim().length() != 0) {
                Logger.v("KEY --" + list.get(i).getKey().trim() + "--");
                keyValueScriptBuffer.append("<br />" + "<div style=\"font-size:15px; font-weight:500\">" + list.get(i).getKey().trim() + "</div>"); // small left

                if (list.get(i).getValue() != null && list.get(i).getValue().trim().length() != 0) {
                    Logger.v("Val --" + list.get(i).getValue().trim() + "--");
                    if (Utils.isProbablyArabic(list.get(i).getKey().trim())) {
                        keyValueScriptBuffer.append("<br />" + "<div style=\"font-size:15px; font-weight:500\">" + list.get(i).getValue().trim() + "</div>"); // small left

                    } else {
                        keyValueScriptBuffer.append("<br />" + "<div style=\"font-size:15px; font-weight:500\">" + list.get(i).getValue().trim() + "</div>"); // small left

                    }

                }
            }

           /* if (8888 < keyValueScriptBuffer.length()) {
                Logger.v("inside_loop_printing");
                printerDevice.open();
                printerDevice.printHTML(context, keyValueScriptBuffer.toString(), new PrinterHtmlListener() {
                    @Override
                    public void onGet(Bitmap bitmap, int i) {

                    }

                    @Override
                    public void onFinishPrinting(int i) {

                        try {

                            printerDevice.printlnText(" ");
                            printerDevice.printlnText(" ");
                            printerDevice.printlnText(" ");
                            Logger.v("printerclose_insideloop_begins");
                            printerDevice.close();
                            Logger.v("printerclose_insideloop_closed");
                        }catch (DeviceException e){

                        }
                        complete.onFinish();

                    }
                });
                printerDevice.queryStatus();
                keyValueScriptBuffer = new StringBuffer();
               // printerDevice.close();
            }*/
        }
//            scriptBuffer.append("*feedline 2\n");

        Logger.v("scriptBuffer.toString() 1--" + keyValueScriptBuffer.length());
        Logger.v("outside_loop_printing");
        printerDevice.open();
        printerDevice.printHTML(context, keyValueScriptBuffer.toString(), new PrinterHtmlListener() {
            @Override
            public void onGet(Bitmap bitmap, int i) {

            }

            @Override
            public void onFinishPrinting(int i) {

                try {
                    printerDevice.printlnText(" ");
                    printerDevice.printlnText(" ");
                    printerDevice.printlnText(" ");
                    printerDevice.close();

                } catch (DeviceException e) {

                }
                complete.onFinish();
            }
        });
        if (printerDevice.queryStatus() == 1) {
            Logger.v("Logger.v +Success");
            return true;
            //   showMessage(context.getString(R.string.msg_print_script_success) + "\r\n", MessageTag.NORMAL);
        } else {
            Logger.v("Logger.v +Failed");
            return false;
            //   showMessage(context.getString(R.string.msg_print_script_error) + printerResult.toString() + "\r\n", MessageTag.NORMAL);
        }

    }

    //******************************print key value for q2*******************************************

    static boolean isPrintComplete = false;

    //******************************print bill for q2*******************************************
    public static void printBill(final PrinterModel printerModel, final Bitmap logoBitmap, final PrinterDevice printerDevice, final Context context, final BaseViewModel.PrintComplete complete) {

        Logger.v("Print Initiated");
        isPrintComplete = false;
        final StringBuffer printBillBuffer = new StringBuffer();
        printBillBuffer.append("<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <style type=\"text/css\">" +
                "     * {" +
                "        margin:0;" +
                "        padding:0;" +
                "font-family:Arial Black;" +
                "     }" +
                "    </style>" +
                "</head>" +
                "<body>");

        if (printerModel.getRetailerNameArabic() != null)
            printBillBuffer.append("<div style=\"text-align: center;font-size:30px; font-weight:900\">" + printerModel.getRetailerNameArabic() + "</div>" + "\n"); //LARGE CENTER
        if (printerModel.getRetailerNameEnglish() != null)
            printBillBuffer.append("<div style=\"text-align: center;font-size:30px; font-weight:900\">" + printerModel.getRetailerNameEnglish() + "</div>" + "\n"); //LARGE CENTER
        if (printerModel.getTerminalCityArabic() != null && printerModel.getTerminalStreetArabic() != null)
            printBillBuffer.append("<div style=\"text-align:center;font-size:20px; font-weight:500\">" + printerModel.getTerminalCityArabic() + ", " + printerModel.getTerminalStreetArabic() + "</div>"); //SMALL CENTER
        if (printerModel.getTerminalStreetEnglish() != null || printerModel.getTerminaCityEnglish() != null)
            printBillBuffer.append("<div style=\"text-align:center;font-size:20px; font-weight:500\">" + printerModel.getTerminalStreetEnglish() + ", " + printerModel.getTerminaCityEnglish() + "</div>"); //SMALL CENTER
        if (printerModel.getRetailerTelephone() != null)
            printBillBuffer.append("<div style=\"text-align:center;font-size:15px; font-weight:400\">" + "TEL: +" + printerModel.getRetailerTelephone() + "</div>"); //small center
        if (printerModel.getStartDate() != null || printerModel.getStartTime() != null)
            printBillBuffer.append("<div style=\"display:flex;justify-content:space-between;width:100%\"> \n" +
                    " <div style=\"font-size:20px; font-weight:500\">" + printerModel.getStartDate() + "</div> \n" +
                    "<div style=\"font-size:20px; font-weight:500\">" + printerModel.getStartTime() + "</div> \n" +
                    "</div>"); // medium 2 allignment
        if (printerModel.getbId() != null && printerModel.getmId() != null && printerModel.gettId() != null)  //small 3 allignment
            printBillBuffer.append("<div style=\"display:flex;justify-content:space-between;width:100%\"> \n" +
                    " <div style=\"font-size:15px; font-weight:500\">" + printerModel.getbId() + "</div> \n" +
                    "<div style=\"font-size:15px; font-weight:500\">" + printerModel.getmId() + "</div> \n" +
                    "<div style=\"font-size:15px; font-weight:500\">" + printerModel.gettId() + "</div> \n" +
                    "</div>"); //small 3 allignment
        if (printerModel.getMcc() != null && printerModel.getStan() != null && printerModel.getPosSoftwareVersionNumber() != null && printerModel.getRrn() != null)// small 4 allignment
            printBillBuffer.append("<div style=\"display:flex;justify-content:space-between;width:100%\"> \n" +
                    " <div style=\"font-size:15px; font-weight:500\">" + printerModel.getMcc() + "</div> \n" +
                    "<div style=\"font-size:15px; font-weight:500\">" + printerModel.getStan() + "</div> \n" +
                    "<div style=\"font-size:15px; font-weight:500\">" + printerModel.getPosSoftwareVersionNumber() + "</div> \n" +
                    "<div style=\"font-size:15px; font-weight:500\">" + printerModel.getRrn() + "</div> \n" +
                    "</div>");
        if (printerModel.getApplicationLabelArabic() != null)
            printBillBuffer.append("<div style=\"float:right;font-size:20px; font-weight:500\">" + printerModel.getApplicationLabelArabic() + "</div> \n"); //medium right
        if (printerModel.getApplicationLabelEnglish() != null)
            printBillBuffer.append("<br />" + "<div style=\"font-size:20px; font-weight:500\">" + printerModel.getApplicationLabelEnglish() + "</div>"); // medium left
        if (printerModel.getTransactionTypeArabic() != null)
            printBillBuffer.append("<br />" + "<div style=\"float:right;font-size:20px; font-weight:500\">" + printerModel.getTransactionTypeArabic() + "</div> \n"); //medium right
        if (printerModel.getTransactionTypeEnglish() != null)
            printBillBuffer.append("<br />" + "<div style=\"font-size:20px; font-weight:500\">" + printerModel.getTransactionTypeEnglish() + "</div>"); // medium left
        if (!TextUtils.isEmpty(printerModel.getCardExpry()))

            printBillBuffer.append("<div style=\"display:flex;justify-content:space-between;width:100%\"> \n" +
                    " <div style=\"font-size:20px; font-weight:500\">" + printerModel.getPan() + "</div> \n" +
                    "<div style=\"font-size:20px; font-weight:500\">" + printerModel.getCardExpry() + "</div> \n" +
                    "</div>"); // medium 2 allignment
        else
            printBillBuffer.append("<br />" + "<div style=\"font-size:20px; font-weight:500\">" + printerModel.getPan() + "</div>");

        //purchase with cash back setting arabic data
        if (printerModel.getPurchaseAmountStringArabic() != null)
            printBillBuffer.append("<div style=\"float:right;font-size:15px; font-weight:500\">" + printerModel.getPurchaseAmountStringArabic() + "</div> \n"); //small right
        if (printerModel.getPurchaseAmountArabic() != null)
            printBillBuffer.append("<br />" + "<div style=\"font-size:15px; font-weight:500\">" + printerModel.getPurchaseAmountArabic() + "</div>"); // small left
        if (printerModel.getPurchaseWithCashBackAmountStringArabic() != null)
            printBillBuffer.append("<div style=\"float:right;font-size:15px; font-weight:500\">" + printerModel.getPurchaseWithCashBackAmountStringArabic() + "</div> \n"); //small right
        if (printerModel.getPurchaseWithCashBackAmountArabic() != null)
            printBillBuffer.append("<br />" + "<div style=\"font-size:15px; font-weight:500\">" + printerModel.getPurchaseWithCashBackAmountArabic() + "</div>"); // small left


        if (printerModel.getTotalAmountArabic() != null)
            printBillBuffer.append("<div style=\"float:right;font-size:30px; font-weight:900\">" + printerModel.getTotalAmountArabic() + "</div> \n"); //large right
        if (printerModel.getAmountArabic() != null)
            printBillBuffer.append("<br />" + "<div style=\"font-size:30px; font-weight:900\">" + printerModel.getAmountArabic() + "</div>"); // large left

        //purchase with cash back setting english data
        if (printerModel.getPurchaseAmountStringEnglish() != null)
            printBillBuffer.append("<br />" + "<div style=\"font-size:15px; font-weight:500\">" + printerModel.getPurchaseAmountStringEnglish() + "</div>"); // small left

        if (printerModel.getPurchaseAmountEnglish() != null)
            printBillBuffer.append("<br />" + "<div style=\"float:right;font-size:15px; font-weight:500\">" + printerModel.getPurchaseAmountEnglish() + "</div> \n"); //small right

        if (printerModel.getPurchaseWithCashBackAmountStringEnglish() != null)
            printBillBuffer.append("<br />" + "<div style=\"font-size:15px; font-weight:500\">" + printerModel.getPurchaseWithCashBackAmountStringEnglish() + "</div>"); // small left

        if (printerModel.getPurchaseWithCashBackAmountEnglish() != null)
            printBillBuffer.append("<br />" + "<div style=\"float:right;font-size:15px; font-weight:500\">" + printerModel.getPurchaseWithCashBackAmountEnglish() + "</div> \n"); //small right


        if (printerModel.getTotalAmountEnglish() != null)
            printBillBuffer.append("<br />" + "<div style=\"font-size:30px; font-weight:900\">" + printerModel.getTotalAmountEnglish() + "</div>"); // large left
        if (printerModel.getAmountEnglish() != null)
            printBillBuffer.append("<br />" + "<div style=\"float:right;font-size:30px; font-weight:900\">" + printerModel.getAmountEnglish() + "</div> \n"); //large right
        printBillBuffer.append("<br />");
        printBillBuffer.append("<br />");

        if (printerModel.getTransactionOutcomeArabic() != null)
            printBillBuffer.append("<div style=\"text-align:center;font-size:20px; font-weight:500\">" + printerModel.getTransactionOutcomeArabic() + "</div>"); //medium center

        if (printerModel.getTransactionOutcomeEnglish() != null)
            printBillBuffer.append("<div style=\"text-align:center;font-size:20px; font-weight:500\">" + printerModel.getTransactionOutcomeEnglish() + "</div>"); //medium center

        if (printerModel.getCardHolderVerificationOrReasonForDeclineArabic() != null)
            printBillBuffer.append("<div style=\"text-align:center;font-size:20px; font-weight:500\">" + printerModel.getCardHolderVerificationOrReasonForDeclineArabic() + "</div>"); //medium center

        if (printerModel.getCardHolderVerificationOrReasonForDeclineEnglish() != null)
            if (printerModel.getCardHolderVerificationOrReasonForDeclineEnglish().contains("---")) {
                String[] splitVal = printerModel.getCardHolderVerificationOrReasonForDeclineEnglish().split("---");
                printBillBuffer.append("<div style=\"text-align:center;font-size:20px; font-weight:500\">" + splitVal[0].trim() + "</div>"); //medium center
                printBillBuffer.append("<div style=\"text-align:center;font-size:20px; font-weight:500\">" + splitVal[1].trim() + "</div>"); //medium center
            } else
                printBillBuffer.append("<div style=\"text-align:center;font-size:20px; font-weight:500\">" + printerModel.getCardHolderVerificationOrReasonForDeclineEnglish().trim() + "</div>"); //medium center

        if (printerModel.getSignBelowArabic() != null)
            printBillBuffer.append("<br />" + "<div style=\"float:right;font-size:15px; font-weight:500\">" + printerModel.getSignBelowArabic() + "</div> \n"); //small right

        if (printerModel.getSignBelowEnglish() != null)
            printBillBuffer.append("<br />" + "<div style=\"font-size:15px; font-weight:500\">" + printerModel.getSignBelowEnglish() + "</div>"); // small left
        if (printerModel.getUnderline() != null)
            //scriptBuffer.append("*feedline 1\n");
            printBillBuffer.append("<br />" + "<br />" + "<div style=\"font-size:15px; font-weight:500\">" + printerModel.getUnderline() + "</div>"); // small left with feedline
        if (printerModel.getAccountForTheAmountArabic() != null)
            printBillBuffer.append("<br />" + "<div style=\"float:right;font-size:15px; font-weight:500\">" + printerModel.getAccountForTheAmountArabic() + "</div> \n"); //small right

        if (printerModel.getAccountForTheAmountEnglish() != null)
            printBillBuffer.append("<br />" + "<div style=\"font-size:15px; font-weight:500\">" + printerModel.getAccountForTheAmountEnglish() + "</div>"); // small left


        if (printerModel.getApprovalCodeArabic() != null)
            printBillBuffer.append("<div style=\"display:flex;justify-content:space-between;width:100%\"> \n" +
                    " <div style=\"font-size:15px; font-weight:500\">" + printerModel.getApprovalCodeArabic() + "</div> \n" +
                    "<div style=\"font-size:15px; font-weight:500\">" + printerModel.getApprovalCodeStringArabic() + "</div> \n" +
                    "</div>"); //small 2 allignment
        if (printerModel.getApprovalCodeStringEnglish() != null)
            printBillBuffer.append("<div style=\"display:flex;justify-content:space-between;width:100%\"> \n" +
                    " <div style=\"font-size:15px; font-weight:500\">" + printerModel.getApprovalCodeStringEnglish() + "</div> \n" +
                    "<div style=\"font-size:15px; font-weight:500\">" + printerModel.getApprovalCodeEnglish() + "</div> \n" +
                    "</div>"); //small 2 allignment
        if (printerModel.getEndDate() != null && printerModel.getEndTime() != null)
            printBillBuffer.append("<div style=\"display:flex;justify-content:space-between;width:100%\"> \n" +
                    " <div style=\"font-size:15px; font-weight:500\">" + printerModel.getEndDate() + "</div> \n" +
                    "<div style=\"font-size:15px; font-weight:500\">" + printerModel.getEndTime() + "</div> \n" +
                    "</div>"); //small 2 allignment
        if (printerModel.getThankYouArabic() != null)
            printBillBuffer.append("<div style=\"text-align:center;font-size:15px; font-weight:500\">" + printerModel.getThankYouArabic() + "</div>"); //small center
        if (printerModel.getThankYouEnglish() != null)
            printBillBuffer.append("<div style=\"text-align:center;font-size:15px; font-weight:500\">" + printerModel.getThankYouEnglish() + "</div>"); //small center
        if (printerModel.getPleaseRetainYourReceiptArabic() != null)
            printBillBuffer.append("<div style=\"text-align:center;font-size:15px; font-weight:500\">" + printerModel.getPleaseRetainYourReceiptArabic() + "</div>"); //small center
        if (printerModel.getPleaseRetainYourReceiptEnglish() != null)
            printBillBuffer.append("<div style=\"text-align:center;font-size:15px; font-weight:500\">" + printerModel.getPleaseRetainYourReceiptEnglish() + "</div>"); //small center
        if (printerModel.getReceiptVersionArabic() != null)
            printBillBuffer.append("<div style=\"text-align:center;font-size:15px; font-weight:500\">" + printerModel.getReceiptVersionArabic() + "</div>"); //small center
        if (printerModel.getReceiptVersionEnglish() != null)
            printBillBuffer.append("<div style=\"text-align:center;font-size:15px; font-weight:500\">" + printerModel.getReceiptVersionEnglish() + "</div>"); //small center
//        if (!printerModel.getPosEntryMode().equalsIgnoreCase(ConstantAppValue.CONTACTLESS))
        printBillBuffer.append("<div style=\"display:flex;justify-content:space-between;width:100%\"> \n" +
                " <div style=\"font-size:13px; font-weight:500\">" + printerModel.getPosEntryMode() + "</div> \n" +
                "<div style=\"font-size:13px; font-weight:500\">" + printerModel.getAlpharesponseCode() + "</div> \n" +
                "<div style=\"font-size:13px; font-weight:500\">" + printerModel.getAid() + "</div> \n" +
                "<div style=\"font-size:13px; font-weight:500\">" + printerModel.getTvr() + "</div> \n" +
                "</div>");  // small 4 allignment
//        else {
//            printBillBuffer.append("<div style=\"display:flex;justify-content:space-between;width:100%\"> \n" +
//                    " <div style=\"font-size:15px; font-weight:500\">" + printerModel.getPosEntryMode() + "</div> \n" +
//                    "<div style=\"font-size:15px; font-weight:500\">" + printerModel.getAlpharesponseCode() + "</div> \n" +
//                    "<div style=\"font-size:15px; font-weight:500\">" + printerModel.getAid() + "</div> \n" +
//                    "</div>");  // small 3 allignment
//            printBillBuffer.append("<div style=\"text-align:center;font-size:15px; font-weight:500\">" + printerModel.getTvr() + "</div>"); //small center
//        }
        printBillBuffer.append("<div style=\"display:flex;justify-content:space-between;width:100%\"> \n" +
                " <div style=\"font-size:13px; font-weight:500\">" + printerModel.getTsi() + "</div> \n" +
                "<div style=\"font-size:13px; font-weight:500\">" + printerModel.getCvr() + "</div> \n" +
                "<div style=\"font-size:13px; font-weight:500\">" + printerModel.getApplicationCryptogramInfo() + "</div> \n" +
                "<div style=\"font-size:13px; font-weight:500\">" + printerModel.getApplicationCryptogram() + "</div> \n" +
                "<div style=\"font-size:13px; font-weight:500\">" + printerModel.getKernalId() + "</div> \n" +
                "</div>");  // small 5 allignment
        if (printerModel.getData44().trim().length() != 0) {
            printBillBuffer.append("<div style=\"text-align:center;font-size:15px; font-weight:500\">" + printerModel.getData44() + "</div>"); //small center
        }
        Logger.v("scriptBuffer.toString() 1--" + printBillBuffer.length());


        try {
            // PrinterDevice    device = (PrinterDevice) POSTerminal.getInstance(context).getDevice("cloudpos.device.printer");

            printerDevice.open();
            //  Bitmap bitmap = null;
            // bitmap = BitmapFactory.decodeStream(context.getResources().getAssets().open("print_logo.png"));
            final Format format = new Format();
            format.setParameter(Format.FORMAT_ALIGN, Format.FORMAT_ALIGN_CENTER);
            printerDevice.printBitmap(format, logoBitmap);
            printerDevice.printHTML(context, printBillBuffer.toString(), new PrinterHtmlListener() {
                @Override
                public void onGet(Bitmap bitmap, int i) {

                }

                @Override
                public void onFinishPrinting(int i) {
                    Logger.v("printer---q2-----success----printbill");
                    try {
                        if (isPrintComplete) {
                            return;
                        }
                        Logger.v("BEFORE BITMAP");
                        if (printerModel.getQrCodeData() != null)
                            printerDevice.printBitmap(format, Utils.getQrCode(printerModel.getQrCodeData(), context));
                        Logger.v("AFTER BITMAP");
                        printerDevice.printlnText(" ");
                        printerDevice.printlnText(" ");
                        printerDevice.printlnText(" ");

                        if (printerDevice.queryStatus() == 1) {
                            Logger.v("Print success");
                            //   showMessage(context.getString(R.string.msg_print_script_success) + "\r\n", MessageTag.NORMAL);
                        } else {
                            Logger.v("Print failed");
                            //   showMessage(context.getString(R.string.msg_print_script_error) + printerResult.toString() + "\r\n", MessageTag.NORMAL);
                        }
                        isPrintComplete = true;
                        printerDevice.close();
                        complete.onFinish();
                    } catch (DeviceException e) {
                        e.printStackTrace();
                        Logger.v("Eception -" + e.getMessage());
                        isPrintComplete = true;
                        complete.onFinish();
                    }
                }
            });
        } catch (Exception e) {
            Logger.v("printer---q2-----failed----printbill----" + e.getMessage());
        }
    }


    //******************************printbill for q2*******************************************
    static boolean status = false;

    public static boolean printBillReconciliation(AppDatabase database, PrinterDevice mPrinter, Bitmap bitmap1, Context context) {
        printData = "";
        try {
            String id = getCardSchemIDFromDB(database);
            ReconcilationTopModel reconcilationTopModel = getTopLayoutModel(0, id);

//            StringBuffer top = reconiclationTopLayout(arial, arialBold, arabic, arabicBold, reconcilationTopModel);
            StringBuffer top = reconciliationTopLayout(reconcilationTopModel);
            printData = printData + top.toString();

//            topPrintBill(mPrinter, top.toString(), bitmap1, context);

            List<ReconciliationCardSchemeModel> reconciliationCardSchemeModelList = new ArrayList<>();
            for (String cardScheme : ConstantAppValue.LIST_CARD_PRINT) {
                String nameArabic = Utils.getCardName(cardScheme);
                String nameEnglish = Utils.getCardName(cardScheme);
                String indicator = cardScheme;
                String indicator1 = cardScheme;
                TMSCardSchemeEntity cardSchemeActual = database.getTMSCardSchemeDao().getCardSchemeData(cardScheme);
                if (cardSchemeActual != null) {
                    nameArabic = new String(cardSchemeActual.getCardNameArabic().getBytes(Charset.forName("Cp1252")), Charset.forName("ISO-8859-6"));
                    nameEnglish = cardSchemeActual.getCardNameEnglish();
                    indicator = cardSchemeActual.getCardSchemeID() + cardSchemeActual.getCardIndicator();
                    indicator1 = cardSchemeActual.getCardIndicator() + cardSchemeActual.getCardSchemeID();
                }
                List<TransactionModelEntity> transaction = database.getTransactionDao().getSuccessTransaction(cardScheme, "400", 1, "000", "001", "003", "007");
                if (transaction.size() != 0) {
                    ReconciliationCardSchemeModel reconciliationCardSchemeModel = getCardSchemeLayoutModel(indicator1, indicator, cardScheme, database, nameArabic, nameEnglish);
                    reconciliationCardSchemeModel.setCardSchemeEnglish(nameEnglish);
                    reconciliationCardSchemeModel.setCardSchemeArabic(nameArabic);
                    reconciliationCardSchemeModelList.add(reconciliationCardSchemeModel);
//                    StringBuffer cardSchemeEach = reconciliationCardSchemeLayout(arial, arialBold, arabic, arabicBold, nameArabic, nameEnglish, reconciliationCardSchemeModel, reconcilationTopModel.getTransactionTypeEnglish(), "Available");
                    StringBuffer cardSchemeEach = reconciliationCardSchemeLayout(nameArabic, nameEnglish, reconciliationCardSchemeModel, reconcilationTopModel.getTransactionTypeEnglish(), "Available");
                    printData = printData + cardSchemeEach.toString();
//                    topPrintBill(mPrinter, cardSchemeEach.toString(), null, context);
                } else {
                    ReconciliationCardSchemeModel reconciliationCardScheme = new ReconciliationCardSchemeModel();
                    reconciliationCardScheme.setCardSchemeEnglish(nameEnglish);
                    reconciliationCardScheme.setCardSchemeArabic(nameArabic);
                    reconciliationCardScheme.setMadaHostTotalsCount(null);
                    reconciliationCardScheme.setMadaHostTotalsAmountInSar(null);
                    reconciliationCardSchemeModelList.add(reconciliationCardScheme);
//                    StringBuffer cardSchemeEach = reconciliationCardSchemeLayout(arial, arialBold, arabic, arabicBold, nameArabic, nameEnglish, null, reconcilationTopModel.getTransactionTypeEnglish(), "NotAvailable");
                    StringBuffer cardSchemeEach = reconciliationCardSchemeLayout(nameArabic, nameEnglish, null, reconcilationTopModel.getTransactionTypeEnglish(), "NotAvailable");
                    printData = printData + cardSchemeEach.toString();
//                    topPrintBill(mPrinter, cardSchemeEach.toString(), null, context);
                }
            }
            AppManager.getInstance().setReconciliationCardSchemeModelList(reconciliationCardSchemeModelList);
            //shared preference

            ReconciliationBottomModel reconciliationBottomModel = getBottomLayoutModel(false);

//            StringBuffer bottom = reconiclationBottomLayout(arial, arialBold, arabic, arabicBold, reconciliationBottomModel);
            StringBuffer bottom = reconiclationBottomLayout(reconciliationBottomModel);

            printData = printData + bottom.toString();
//            topPrintBill(mPrinter, bottom.toString(), null, context);

        } catch (Throwable t) {
            t.printStackTrace();
        }
        Logger.v("TopStatus --" + status);
        status = true;
        return status;
    }

    private static String getCardSchemIDFromDB(AppDatabase database) {
        String cardSchemeID = "";
        for (String cardScheme : ConstantAppValue.LIST_CARD_PRINT_ID) {
            List<TransactionModelEntity> transaction = database.getTransactionDao().getSuccessTransaction(cardScheme, "400", 1, "000", "001", "003", "007");
            if (transaction.size() != 0) {
                cardSchemeID = database.getTMSCardSchemeDao().getCardSchemeID(cardScheme);
                break;
            }
        }
        return cardSchemeID;
    }

    public static boolean printBillReconciliationDuplicate(PrinterDevice mPrinter, Bitmap bitmap1, Context context) {
        printData = "";
        boolean status = false;
        try {
            ReconcilationTopModel reconcilationTopModel = AppManager.getInstance().getReconciliationTopCard();

//            StringBuffer top = reconiclationTopLayout(arial, arialBold, arabic, arabicBold, reconcilationTopModel);
            StringBuffer top = reconciliationTopLayout(reconcilationTopModel);
            printData = top.toString();
//            topPrintBill(mPrinter, top.toString(), bitmap1, context);

            List<ReconciliationCardSchemeModel> listData = AppManager.getInstance().getReconciliationCardSchemeModelList();
            for (int i = 0; i < listData.size(); i++) {
                if (listData.get(i).getMadaHostTotalsCount() == null && listData.get(i).getMadaHostTotalsAmountInSar() == null) {
                    //shared preference
                    StringBuffer cardSchemeEach = reconciliationCardSchemeLayout(listData.get(i).getCardSchemeArabic(), listData.get(i).getCardSchemeEnglish(), listData.get(i), reconcilationTopModel.getTransactionTypeEnglish(), "NotAvailable");
//                    StringBuffer cardSchemeEach = reconciliationCardSchemeLayout(arial, arialBold, arabic, arabicBold, listData.get(i).getCardSchemeArabic(), listData.get(i).getCardSchemeEnglish(), listData.get(i), reconcilationTopModel.getTransactionTypeEnglish(), "NotAvailable");
                    printData = printData + cardSchemeEach.toString();
//                    topPrintBill(mPrinter, cardSchemeEach.toString(), null, context);
                } else {
                    StringBuffer cardSchemeEach = reconciliationCardSchemeLayout(listData.get(i).getCardSchemeArabic(), listData.get(i).getCardSchemeEnglish(), listData.get(i), reconcilationTopModel.getTransactionTypeEnglish(), "Available");
//                    StringBuffer cardSchemeEach = reconciliationCardSchemeLayout(arial, arialBold, arabic, arabicBold, listData.get(i).getCardSchemeArabic(), listData.get(i).getCardSchemeEnglish(), listData.get(i), reconcilationTopModel.getTransactionTypeEnglish(), "Available");
                    printData = printData + cardSchemeEach.toString();
//                    topPrintBill(mPrinter, cardSchemeEach.toString(), null, context);
                }
            }
            ReconciliationBottomModel reconciliationBottomModel = getBottomLayoutModel(true);

//            StringBuffer bottom = reconiclationBottomLayout(arial, arialBold, arabic, arabicBold, reconciliationBottomModel);
            StringBuffer bottom = reconiclationBottomLayout(reconciliationBottomModel);
            printData = printData + bottom.toString();
//            topPrintBill(mPrinter, bottom.toString(), null, context);


        } catch (Throwable t) {
            t.printStackTrace();
        }
        status = true;
        return status;
    }


    public static boolean printBillSnapShot(AppDatabase database, PrinterDevice mPrinter, int uid, boolean isSnpshot, Bitmap bitmap1, Context context) {
        printData = "";
        try {
            ReconcilationTopModel reconcilationTopModel = getTopLayoutModel(isSnpshot ? 1 : 2, null);
            StringBuffer top = reconciliationTopLayout(reconcilationTopModel);
            printData = printData + top.toString();
            for (String cardScheme : ConstantAppValue.LIST_CARD_PRINT) {
                String nameArabic = Utils.getCardName(cardScheme);
                String nameEnglish = Utils.getCardName(cardScheme);
                String indicator = cardScheme;
                TMSCardSchemeEntity cardSchemeActual = database.getTMSCardSchemeDao().getCardSchemeData(cardScheme);
                if (cardSchemeActual != null) {
                    nameArabic = new String(cardSchemeActual.getCardNameArabic().getBytes(Charset.forName("Cp1252")), Charset.forName("ISO-8859-6"));
                    nameEnglish = cardSchemeActual.getCardNameEnglish();
                    indicator = cardSchemeActual.getCardSchemeID() + cardSchemeActual.getCardIndicator();
                }
                List<TransactionModelEntity> transaction = database.getTransactionDao().getSuccessAfterRunningTransaction(1, "000", cardScheme, uid);
                if (transaction.size() != 0) {
                    ReconciliationCardSchemeModel reconciliationCardSchemeModel = getCardSchemeSnapShotLayoutModel(indicator, cardScheme, database, uid);
                    StringBuffer cardSchemeEach = reconciliationCardSchemeLayout(nameArabic, nameEnglish, reconciliationCardSchemeModel, reconcilationTopModel.getTransactionTypeEnglish(), "Available");
//                    StringBuffer cardSchemeEach = reconciliationCardSchemeLayout(arial, arialBold, arabic, arabicBold, nameArabic, nameEnglish, reconciliationCardSchemeModel, reconcilationTopModel.getTransactionTypeEnglish(), "Available");
                    printData = printData + cardSchemeEach.toString();
//                    topPrintBill(mPrinter, cardSchemeEach.toString(), null, context);
                } else {
                    StringBuffer cardSchemeEach = reconciliationCardSchemeLayout(nameArabic, nameEnglish, null, reconcilationTopModel.getTransactionTypeEnglish(), "NotAvailable");
//                    StringBuffer cardSchemeEach = reconciliationCardSchemeLayout(arial, arialBold, arabic, arabicBold, nameArabic, nameEnglish, null, reconcilationTopModel.getTransactionTypeEnglish(), "NotAvailable");
                    printData = printData + cardSchemeEach.toString();
//                    topPrintBill(mPrinter, cardSchemeEach.toString(), null, context);
                }

            }

            ReconciliationBottomModel reconciliationBottomModel = getBottomLayoutModel(false);

            StringBuffer bottom = reconiclationBottomLayout(reconciliationBottomModel);
//            StringBuffer bottom = reconiclationBottomLayout(arial, arialBold, arabic, arabicBold, reconciliationBottomModel);
            printData = printData + bottom.toString();
//            topPrintBill(mPrinter, bottom.toString(), null, context);

//            topPrintBill(mPrinter, printData, bitmap1, context);
            return true;
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }

    public static ReconciliationCardSchemeModel getCardSchemeLayoutModel(String indicator1, String indicator, String chemeID, AppDatabase database, String nameArabic, String nameEnglish) {
        List<TransactionModelEntity> transaction = database.getTransactionDao().getSuccessTransaction(chemeID, "400", 1, "000", "001", "003", "007");
        ReconciliationCardSchemeModel reconciliationCardSchemeModel = new ReconciliationCardSchemeModel();
        Logger.v("indicator--" + indicator);
        Logger.v("indicator--" + indicator1);
        Logger.v("DE124 --" + AppManager.getInstance().getDe124());
        String[] data = AppManager.getInstance().getDe124().split(indicator1);
        reconciliationCardSchemeModel.setCardSchemeArabic(nameArabic); //TMS
        reconciliationCardSchemeModel.setCardSchemeEnglish(nameEnglish);

        if (data.length > 1) {
            String data124 = data[1];
            Logger.v("Data127 -1-" + data[1]);
            Logger.v("Data127 -1-" + data[0]);
            reconciliationCardSchemeModel.setMadaHostTotalDbCount(Integer.parseInt(data124.substring(0, 10)) + "");
            reconciliationCardSchemeModel.setMadaHostTotalDbAmountInSar(addDecimal(Integer.parseInt(data124.substring(10, 25))));
            reconciliationCardSchemeModel.setMadaHostTotalCrCount(Integer.parseInt(data124.substring(25, 35)) + "");
            reconciliationCardSchemeModel.setMadaHostTotalCrAmountInSar(addDecimal(Integer.parseInt(data124.substring(35, 50))));
            reconciliationCardSchemeModel.setMadaHostNAQDCount("-");
            reconciliationCardSchemeModel.setMadaHostNAQDAmountInSar(addDecimal(Integer.parseInt(data124.substring(50, 65))));
            reconciliationCardSchemeModel.setMadaHostCashAdvanceCount("-");
            reconciliationCardSchemeModel.setMadaHostCashAdvanceAmountInSar(addDecimal(Integer.parseInt(data124.substring(65, 80))));
            reconciliationCardSchemeModel.setMadaHostAuthCount(Integer.parseInt(data124.substring(80, 90)) + "");
            reconciliationCardSchemeModel.setMadaHostAuthAmountInSar("-");
            reconciliationCardSchemeModel.setMadaHostTotalsCount(getTotalCount(reconciliationCardSchemeModel));
            reconciliationCardSchemeModel.setMadaHostTotalsAmountInSar(getTotalAmount(reconciliationCardSchemeModel));
        }
        if (data.length > 0)
            Logger.v("Data127 -" + data[0]);
        long credit = 0, debit = 0, creditAmount = 0, debitAmount = 0, cashBack = 0, cashAdvance = 0, authCount = 0;

        for (int i = 0; i < transaction.size(); i++) {
            TransactionModelEntity trans = transaction.get(i);
            Logger.v("trans -" + trans.toString());
            if (Utils.reconsilationTag(trans.getNameTransactionTag())) {
                if (trans.getNameTransactionTag().equalsIgnoreCase(ConstantApp.REFUND) || (trans.getNameTransactionTag().equalsIgnoreCase(ConstantApp.PURCHASE_REVERSAL) && trans.getPosConditionCode25().equalsIgnoreCase(ConstantAppValue.CUSTOMER_CANCELLATION))) {
                    credit = credit + 1;
                    creditAmount = creditAmount + Long.parseLong(trans.getAmtTransaction4());
                } else {
                    debit = debit + 1;
                    debitAmount = debitAmount + Long.parseLong(trans.getAmtTransaction4());
                }
            }
            if (trans.getNameTransactionTag().equalsIgnoreCase(ConstantApp.PURCHASE_REVERSAL) && trans.getPosConditionCode25().equalsIgnoreCase(ConstantAppValue.CUSTOMER_CANCELLATION)) {
                if (!(trans.getProcessingCode3().equalsIgnoreCase(ConstantAppValue.REFUND))) {
                    credit = credit + 1;
                    creditAmount = creditAmount + Long.parseLong(trans.getAmtTransaction4());
                } else {
                    debit = debit + 1;
                    debitAmount = debitAmount + Long.parseLong(trans.getAmtTransaction4());
                }
            }
            if (trans.getNameTransactionTag().equalsIgnoreCase(ConstantApp.CASH_ADVANCE)) {
                int amt = Integer.parseInt(trans.getAmtTransaction4());
                cashAdvance = cashAdvance + amt;
            } else if (trans.getNameTransactionTag().equalsIgnoreCase(ConstantApp.PURCHASE_NAQD)) {
                if (trans.getAddlAmt54().trim().length() != 0) {
                    int amt = Integer.parseInt(trans.getAddlAmt54().substring(8));
                    cashBack = cashBack + amt;
                }
            }

            if (trans.getMti0().equalsIgnoreCase("1110") || trans.getMti0().equalsIgnoreCase("1120") || trans.getNameTransactionTag().equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION)) {
                authCount = authCount + 1;
            }


        }

        Logger.v("Credit --" + credit + "-Amount-" + creditAmount);
        Logger.v("Debit --" + debit + "-Amount-" + debitAmount);
        Logger.v("cashBack --" + cashBack);
        Logger.v("cashAdvance-" + cashAdvance);
        Logger.v("authCount-" + authCount);

        reconciliationCardSchemeModel.setPosTerminalTotalDbCount(getCountString(debit));
        reconciliationCardSchemeModel.setPosTerminalTotalDbAmountInSar(getAmtString(debitAmount));
        reconciliationCardSchemeModel.setPosTerminalTotalCrCount(getCountString(credit));
        reconciliationCardSchemeModel.setPosTerminalTotalCrAmountInSar(getAmtString(creditAmount));
        reconciliationCardSchemeModel.setPosTerminalNAQDCount("-");
        reconciliationCardSchemeModel.setPosTerminalNAQDAmountInSar(getAmtString(cashBack));
        reconciliationCardSchemeModel.setPasTerminalCashAdvancedCount("-");
        reconciliationCardSchemeModel.setPosTerminalCashAdvanceAmountInSar(getAmtString(cashAdvance));
        reconciliationCardSchemeModel.setPosTerminalAuthCount(getCountString(authCount));
        reconciliationCardSchemeModel.setPosTerminalAuthAmountInSar("-");
        reconciliationCardSchemeModel.setPosTerminalTotalsCount(getCountString(debit + credit));
        reconciliationCardSchemeModel.setPosTerminalTotalsAmountInSar(getAmtString(debitAmount - creditAmount));

        List<TransactionModelEntity> purchaseOnline = database.getTransactionDao().getSuccessTransaction(1, "000", indicator, ConstantApp.PURCHASE, false);
        List<TransactionModelEntity> purchaseOffLine = database.getTransactionDao().getSuccessTransaction(1, "000", indicator, true);
        List<TransactionModelEntity> purchaseCashBack = database.getTransactionDao().getSuccessTransaction(1, "000", indicator, ConstantApp.PURCHASE_NAQD);
        List<TransactionModelEntity> purchaseReversal = database.getTransactionDao().getSuccessTransaction(1, "400", indicator, ConstantApp.PURCHASE_REVERSAL);
        List<TransactionModelEntity> purchaseRefund = database.getTransactionDao().getSuccessTransaction(1, "000", indicator, ConstantApp.REFUND);
        List<TransactionModelEntity> completion = database.getTransactionDao().getSuccessTransaction(1, "000", indicator, ConstantApp.PURCHASE_ADVICE_FULL);
        List<TransactionModelEntity> completion1 = database.getTransactionDao().getSuccessTransaction(1, "000", indicator, ConstantApp.PURCHASE_ADVICE_PARTIAL);
        List<TransactionModelEntity> completion2 = database.getTransactionDao().getSuccessTransaction(1, "000", indicator, ConstantApp.PURCHASE_ADVICE_MANUAL);

        List<TransactionModelEntity> total = new ArrayList<>();
        total.addAll(completion);
        total.addAll(completion1);
        total.addAll(completion2);

        reconciliationCardSchemeModel.setPosTerminalDetailsPOFFCount(getCountString(purchaseOffLine.size()));
        reconciliationCardSchemeModel.setPosTerminalDetailsPOFFAmountInSar(getAmount(purchaseOffLine));
        reconciliationCardSchemeModel.setPosTerminalDetailsPONCount(getCountString(purchaseOnline.size()));
        reconciliationCardSchemeModel.setPosTerminalDetailsPONAmountInSar(getAmount(purchaseOnline));
        reconciliationCardSchemeModel.setPosTerminalDetailsPurNaqdCount(getCountString(purchaseCashBack.size()));
        reconciliationCardSchemeModel.setPosTerminalDetailsPurNaqdAmountInSar(getAmount(purchaseCashBack));
        reconciliationCardSchemeModel.setPosTerminalDetailsReversalCount(getCountString(purchaseReversal.size()));
        reconciliationCardSchemeModel.setPosTerminalDetailsReversalAmountInSar(getAmount(purchaseReversal));
        reconciliationCardSchemeModel.setPosTerminalDetailsRefundCount(getCountString(purchaseRefund.size()));
        reconciliationCardSchemeModel.setPosTerminalDetailsRefundAmountInSar(getAmount(purchaseRefund));
        reconciliationCardSchemeModel.setPosTerminalDetailsCompCount(getCountString(total.size()));
        reconciliationCardSchemeModel.setPosTerminalDetailsCompAmountInSar(getAmount(total));
        return reconciliationCardSchemeModel;
    }

    private static String addDecimal(int parseInt) {
        float amtDecimal = (float) parseInt / 100;
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat formatter = new DecimalFormat("###0.00", symbols);
        return formatter.format(amtDecimal);
    }

    private static String addDecimal(long parseInt) {
        float amtDecimal = (float) parseInt / 100;
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat formatter = new DecimalFormat("###0.00", symbols);
        return formatter.format(amtDecimal);
    }

    public static ReconciliationCardSchemeModel getCardSchemeSnapShotLayoutModel(String indicator, String chemeID, AppDatabase database, int uid) {
        List<TransactionModelEntity> transaction = database.getTransactionDao().getSuccessAfterRunningTransaction(1, "000", chemeID, uid);
        ReconciliationCardSchemeModel reconciliationCardSchemeModel = new ReconciliationCardSchemeModel();
        Logger.v("indicator--" + indicator);
        Logger.v("DE124 --" + AppManager.getInstance().getDe124());

        long credit = 0, debit = 0, creditAmount = 0, debitAmount = 0, cashBack = 0, cashAdvance = 0, authCount = 0;

        for (int i = 0; i < transaction.size(); i++) {
            TransactionModelEntity trans = transaction.get(i);
            Logger.v("trans -" + trans.toString());
            if (Utils.reconsilationTag(trans.getNameTransactionTag())) {
                if (trans.getNameTransactionTag().equalsIgnoreCase(ConstantApp.REFUND) || (trans.getNameTransactionTag().equalsIgnoreCase(ConstantApp.PURCHASE_REVERSAL) && trans.getPosConditionCode25().equalsIgnoreCase(ConstantAppValue.CUSTOMER_CANCELLATION))) {
                    credit = credit + 1;
                    creditAmount = creditAmount + Long.parseLong(trans.getAmtTransaction4());
                } else {
                    debit = debit + 1;
                    debitAmount = debitAmount + Long.parseLong(trans.getAmtTransaction4());
                }
            }
            if (trans.getNameTransactionTag().equalsIgnoreCase(ConstantApp.PURCHASE_REVERSAL) && trans.getPosConditionCode25().equalsIgnoreCase(ConstantAppValue.CUSTOMER_CANCELLATION)) {
                if (!(trans.getProcessingCode3().equalsIgnoreCase(ConstantAppValue.REFUND))) {
                    credit = credit + 1;
                    creditAmount = creditAmount + Long.parseLong(trans.getAmtTransaction4());
                } else {
                    debit = debit + 1;
                    debitAmount = debitAmount + Long.parseLong(trans.getAmtTransaction4());
                }
            }
            if (trans.getNameTransactionTag().equalsIgnoreCase(ConstantApp.CASH_ADVANCE)) {
                int amt = Integer.parseInt(trans.getAmtTransaction4());
                cashAdvance = cashAdvance + amt;
            } else if (trans.getNameTransactionTag().equalsIgnoreCase(ConstantApp.PURCHASE_NAQD)) {
                if (trans.getAddlAmt54().trim().length() != 0) {
                    int amt = Integer.parseInt(trans.getAddlAmt54().substring(8));
                    cashBack = cashBack + amt;
                }
            }

            if (trans.getMti0().equalsIgnoreCase("1110") || trans.getMti0().equalsIgnoreCase("1120") || trans.getNameTransactionTag().equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION)) {
                authCount = authCount + 1;
            }


        }
        List<TransactionModelEntity> safLiat = database.getSAFDao().getAllSuccess(ConstantAppValue.SAF_APPROVED, ConstantAppValue.SAF_APPROVED_UNABLE);
        for (int i = 0; i < safLiat.size(); i++) {
            TransactionModelEntity trans = safLiat.get(i);
            if (Utils.reconsilationTag(trans.getNameTransactionTag())) {
                if (!(trans.getNameTransactionTag().equalsIgnoreCase(ConstantApp.REFUND) || (trans.getNameTransactionTag().equalsIgnoreCase(ConstantApp.PURCHASE_REVERSAL) && trans.getPosConditionCode25().equalsIgnoreCase(ConstantAppValue.CUSTOMER_CANCELLATION)))) {
                    credit = credit + 1;
                    creditAmount = creditAmount + Integer.parseInt(trans.getAmtTransaction4());
                } else {
                    debit = debit + 1;
                    debitAmount = debitAmount + Integer.parseInt(trans.getAmtTransaction4());
                }
            }
        }

        Logger.v("Credit --" + credit + "-Amount-" + creditAmount);
        Logger.v("Debit --" + debit + "-Amount-" + debitAmount);
        Logger.v("cashBack --" + cashBack);
        Logger.v("cashAdvance-" + cashAdvance);
        Logger.v("authCount-" + authCount);

        reconciliationCardSchemeModel.setPosTerminalTotalDbCount(getCountString(debit));
        reconciliationCardSchemeModel.setPosTerminalTotalDbAmountInSar(getAmtString(debitAmount));
        reconciliationCardSchemeModel.setPosTerminalTotalCrCount(getCountString(credit));
        reconciliationCardSchemeModel.setPosTerminalTotalCrAmountInSar(getAmtString(creditAmount));
        reconciliationCardSchemeModel.setPosTerminalNAQDCount("-");
        reconciliationCardSchemeModel.setPosTerminalNAQDAmountInSar(getAmtString(cashBack));
        reconciliationCardSchemeModel.setPasTerminalCashAdvancedCount("-");
        reconciliationCardSchemeModel.setPosTerminalCashAdvanceAmountInSar(getAmtString(cashAdvance));
        reconciliationCardSchemeModel.setPosTerminalAuthCount(getCountString(authCount));
        reconciliationCardSchemeModel.setPosTerminalAuthAmountInSar("-");
        reconciliationCardSchemeModel.setPosTerminalTotalsCount(getCountString(debit + credit));
        reconciliationCardSchemeModel.setPosTerminalTotalsAmountInSar(getAmtString(debitAmount - creditAmount));

        List<TransactionModelEntity> purchaseOnline = database.getTransactionDao().getSuccessTransaction(1, "000", indicator, ConstantApp.PURCHASE, false);
        List<TransactionModelEntity> purchaseOffLine = database.getTransactionDao().getSuccessTransaction(1, "000", indicator, true);
        List<TransactionModelEntity> purchaseCashBack = database.getTransactionDao().getSuccessTransaction(1, "000", indicator, ConstantApp.PURCHASE_NAQD);
        List<TransactionModelEntity> purchaseReversal = database.getTransactionDao().getSuccessTransaction(1, "400", indicator, ConstantApp.PURCHASE_REVERSAL);
        List<TransactionModelEntity> purchaseRefund = database.getTransactionDao().getSuccessTransaction(1, "000", indicator, ConstantApp.REFUND);
        List<TransactionModelEntity> completion = database.getTransactionDao().getSuccessTransaction(1, "000", indicator, ConstantApp.PURCHASE_ADVICE_FULL);
        List<TransactionModelEntity> completion1 = database.getTransactionDao().getSuccessTransaction(1, "000", indicator, ConstantApp.PURCHASE_ADVICE_PARTIAL);
        List<TransactionModelEntity> completion2 = database.getTransactionDao().getSuccessTransaction(1, "000", indicator, ConstantApp.PURCHASE_ADVICE_MANUAL);

        List<TransactionModelEntity> total = new ArrayList<>();
        total.addAll(completion);
        total.addAll(completion1);
        total.addAll(completion2);


        reconciliationCardSchemeModel.setPosTerminalDetailsPOFFCount(getCountString(purchaseOffLine.size()));
        reconciliationCardSchemeModel.setPosTerminalDetailsPOFFAmountInSar(getAmount(purchaseOffLine));
        reconciliationCardSchemeModel.setPosTerminalDetailsPONCount(getCountString(purchaseOnline.size()));
        reconciliationCardSchemeModel.setPosTerminalDetailsPONAmountInSar(getAmount(purchaseOnline));
        reconciliationCardSchemeModel.setPosTerminalDetailsPurNaqdCount(getCountString(purchaseCashBack.size()));
        reconciliationCardSchemeModel.setPosTerminalDetailsPurNaqdAmountInSar(getAmount(purchaseCashBack));
        reconciliationCardSchemeModel.setPosTerminalDetailsReversalCount(getCountString(purchaseReversal.size()));
        reconciliationCardSchemeModel.setPosTerminalDetailsReversalAmountInSar(getAmount(purchaseReversal));
        reconciliationCardSchemeModel.setPosTerminalDetailsRefundCount(getCountString(purchaseRefund.size()));
        reconciliationCardSchemeModel.setPosTerminalDetailsRefundAmountInSar(getAmount(purchaseRefund));
        reconciliationCardSchemeModel.setPosTerminalDetailsCompCount(getCountString(total.size()));
        reconciliationCardSchemeModel.setPosTerminalDetailsCompAmountInSar(getAmount(total));
        return reconciliationCardSchemeModel;
    }

    private static String getAmtString(long s) {
        return addDecimal(s);
    }

    private static String getCountString(long s) {
        return s + "";
    }

    private static String getAmount(List<TransactionModelEntity> purchaseOffLine) {
        long amount = 0;
        for (int i = 0; i < purchaseOffLine.size(); i++) {
            long amt = Long.parseLong(purchaseOffLine.get(i).getAmtTransaction4());
            amount = amount + amt;
        }
        return addDecimal(amount);
    }

    private static String getTotalCount(ReconciliationCardSchemeModel reconciliationCardSchemeModel) {
        long count = 0;
        count = count + convertLongCount(reconciliationCardSchemeModel.getMadaHostTotalCrCount());
        count = count + convertLongCount(reconciliationCardSchemeModel.getMadaHostTotalDbCount());
//        count = count + convertLong(reconciliationCardSchemeModel.getMadaHostNAQDCount());
//        count = count + convertLong(reconciliationCardSchemeModel.getMadaHostAuthCount());
//        count = count + convertLong(reconciliationCardSchemeModel.getMadaHostCashAdvanceCount());
        return count + "";
    }

    private static String getTotalAmount(ReconciliationCardSchemeModel reconciliationCardSchemeModel) {
        long count = 0;
        count = count + convertLong(reconciliationCardSchemeModel.getMadaHostTotalCrAmountInSar());
        count = count + convertLong(reconciliationCardSchemeModel.getMadaHostTotalDbAmountInSar());
//        count = count + convertLong(reconciliationCardSchemeModel.getMadaHostNAQDAmountInSar());
//        count = count + convertLong(reconciliationCardSchemeModel.getMadaHostAuthAmountInSar());
//        count = count + convertLong(reconciliationCardSchemeModel.getMadaHostCashAdvanceAmountInSar());
        return addDecimal(count);
    }

    private static Long convertLong(String madaHostTotalCrCount) {
        if (madaHostTotalCrCount.trim().length() == 0)
            return 0L;
        String amount = madaHostTotalCrCount.replaceAll("-", "").replaceAll("/.", "");
        return ((long) Float.parseFloat(amount) * 100);
    }

    private static Long convertLongCount(String madaHostTotalCrCount) {
        if (madaHostTotalCrCount.trim().length() == 0)
            return 0L;
        String amount = madaHostTotalCrCount.replaceAll("-", "");
        return Long.parseLong(amount);
    }

    public static ReconcilationTopModel getTopLayoutModel(int isRecon, String id) {

        RetailerDataModel retailerDataModel = AppManager.getInstance().getRetailerDataModel();
        Logger.v("retailerDataModel --" + retailerDataModel.toString());

        ReconcilationTopModel reconcilationTopModel = AppManager.getInstance().getReconciliationTopCard();
        reconcilationTopModel.setRetailerNameArabic(new String(retailerDataModel.getRetailerNameInArabic().getBytes(Charset.forName("Cp1252")), Charset.forName("ISO-8859-6"))); //TMS
        reconcilationTopModel.setRetailerNameEnglish(retailerDataModel.getRetailerNameEnglish()); //TMS
        reconcilationTopModel.setTerminalStreetArabic(new String(retailerDataModel.getRetailerAddress1Arabic().getBytes(Charset.forName("Cp1252")), Charset.forName("ISO-8859-6"))); //TMS
        reconcilationTopModel.setTerminalStreetEnglish(retailerDataModel.getRetailerAddress1English());//TMS
        reconcilationTopModel.setTerminalCityArabic(new String(retailerDataModel.getRetailerAddress2Arabic().getBytes(Charset.forName("Cp1252")), Charset.forName("ISO-8859-6"))); //TMS
        reconcilationTopModel.setTerminaCityEnglish(retailerDataModel.getRetailerAddress2English());//TMS
        reconcilationTopModel.setRetailerTelephone(AppManager.getInstance().getString(ConstantApp.SPRM_PHONE_NUMBER));//TMS
        String date[] = Utils.getCurrentDate().split(" ");
        reconcilationTopModel.setStartDate(date[0]);
        reconcilationTopModel.setStartTime(date[1]);
        reconcilationTopModel.setmId(AppManager.getInstance().getCardAcceptorCode42()); // 41 Data
        reconcilationTopModel.settId(AppManager.getInstance().getCardAcceptorID41()); // 42
        reconcilationTopModel.setMcc(AppManager.getInstance().getMerchantCode(ConstantAppValue.A0000002282010)); //TMS
        Logger.v("DE26----" + AppManager.getInstance().getMerchantCode(ConstantAppValue.A0000002282010));
        reconcilationTopModel.setPosSoftwareVersionNumber(BuildConfig.VERSION_NAME);
        reconcilationTopModel.setSchemID(id);
        if (isRecon == 0) {
            reconcilationTopModel.setTransactionSTAN(reqObj.getSystemTraceAuditnumber11()); //TMS
            reconcilationTopModel.setTransactionTypeArabic("موازنة");
            reconcilationTopModel.setTransactionTypeEnglish("RECONCILIATION");

            if (AppManager.getInstance().getDe39().equalsIgnoreCase("501")) {
                reconcilationTopModel.setTransactionOutcomeArabic("المجاميع غير متوافقة");
                reconcilationTopModel.setTransactionOutcomeEnglish("TOTALS UNMATCHED");
            } else {
                reconcilationTopModel.setTransactionOutcomeArabic("المجاميع متوافقة");
                reconcilationTopModel.setTransactionOutcomeEnglish("TOTALS MATCHED");
            }
            AppManager.getInstance().setReconciliationTopCard(reconcilationTopModel);
        } else if (isRecon == 1) {
            reconcilationTopModel.setTransactionTypeArabic("أرصدة لحظية");
            reconcilationTopModel.setTransactionTypeEnglish("SNAPSHOT BALANCE");
            reconcilationTopModel.setTransactionOutcomeArabic("المجاميع مقدمة");
            reconcilationTopModel.setTransactionOutcomeEnglish("TOTALS PROVIDED");
        } else if (isRecon == 2) {
            reconcilationTopModel.setTransactionTypeArabic("أرصدة جارية");
            reconcilationTopModel.setTransactionTypeEnglish("RUNNING BALANCE");
            reconcilationTopModel.setTransactionOutcomeArabic("المجاميع مقدمة");
            reconcilationTopModel.setTransactionOutcomeEnglish("TOTALS PROVIDED");
        }
        return reconcilationTopModel;
    }

    public static ReconciliationBottomModel getBottomLayoutModel(boolean isDuplicate) {
        ReconciliationBottomModel reconciliationBottomModel = new ReconciliationBottomModel();
        String date[] = Utils.getCurrentDate().split(" ");
        reconciliationBottomModel.setEndDate(date[0]);
        reconciliationBottomModel.setEndTime(date[1]);
        reconciliationBottomModel.setThankYouEnglish("THANK YOU FOR USING mada");
        reconciliationBottomModel.setThankYouArabic("شكرا لك على استخدام مادا");
        reconciliationBottomModel.setRecordOfTransctionArabic("يرجي الاحتفاظ بالايصال");
        reconciliationBottomModel.setRecordOfTransctionEnglish("PLEASE RETAIN RECEIPT");
        if (isDuplicate) {
            reconciliationBottomModel.setReceiptVersionEnglish("*DUPLICATE COPY*"); // Hard coded
            reconciliationBottomModel.setReceiptVersionArabic("*نسخة إضافية*"); // Hard coded Based on Copy
        } else {
            reconciliationBottomModel.setReceiptVersionArabic("نسخة التاجر");
            reconciliationBottomModel.setReceiptVersionEnglish("RETAILER COPY");
        }
        return reconciliationBottomModel;
    }

    //*******************************print recon scheme layout for q2**************************
    public static StringBuffer reconciliationCardSchemeLayout(String arabicCardName, String englishCardName, ReconciliationCardSchemeModel reconciliationCardSchemeModel, String transactionType, String status) {
        StringBuffer reconCardSchemeBuffer = new StringBuffer();

        reconCardSchemeBuffer.append("<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <style type=\"text/css\">" +
                "     * {" +
                "        margin:0;" +
                "        padding:0;" +
                "font-family:Arial Black;" +
                "     }" +
                "    </style>" +
                "</head>" +
                "<body>");


        reconCardSchemeBuffer.append("<br />" + "<div style=\"float:right;font-size:20px; font-weight:900\">" + arabicCardName + "</div> \n"); //medium right bold
        reconCardSchemeBuffer.append("<br />" + "<div style=\"font-size:20px; font-weight:900\">" + englishCardName + "</div>"); // medium left bold

        if (status.equalsIgnoreCase("Available")) {

            if (transactionType.equalsIgnoreCase(RECEIPT_TYPE_RECONCILIATION)) {
                //mada host
                Logger.v("RECEIPT_TYPE_RECONCILIATION");
                reconCardSchemeBuffer.append("<br />" + "<div style=\"float:right;font-size:20px; font-weight:500\">" + "خادم مدى" + "</div> \n"); //medium right
                reconCardSchemeBuffer.append("<br />" + "<div style=\"font-size:20px; font-weight:500\">" + "mada HOST" + "</div>"); // medium left
                reconCardSchemeBuffer.append("<div style=\"display:flex;justify-content:space-between;width:100%\"> \n" +
                        " <div style=\"font-size:15px; font-weight:900;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all\">" + "نوع العملية" + "</div> \n" +
                        "<div style=\"font-size:15px; font-weight:900;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all; text-align: center\">" + "العدد" + "</div> \n" +
                        "<div style=\"font-size:15px; font-weight:900;width:35% ;max-width:35% ; overflow:hidden; word-break : break-all; text-align: right\">" + "المبلغ بالريال" + "</div> \n" +
                        "</div>"); //small 3 allignment with bold
                reconCardSchemeBuffer.append("<div style=\"display:flex;justify-content:space-between;width:100%\"> \n" +
                        " <div style=\"font-size:15px;font-weight:900;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all\">" + "TXN TYPE" + "</div> \n" +
                        "<div style=\"font-size:15px; font-weight:900;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all; text-align: center\">" + "COUNT" + "</div> \n" +
                        "<div style=\"font-size:15px; font-weight:900;width:35% ;max-width:35% ; overflow:hidden; word-break : break-all; text-align: right\">" + "AMOUNT IN SAR" + "</div> \n" +
                        "</div>"); //small 3 allignment with bold
                reconCardSchemeBuffer.append("<br />" + "<div style=\"font-size:15px; font-weight:500\">" + "إجمالي المدين" + "</div>"); // small left
                reconCardSchemeBuffer.append("<div style=\"display:flex;justify-content:space-between;width:100%\"> \n" +
                        " <div style=\"font-size:15px; font-weight:500;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all\">" + "TOTAL DB" + "</div> \n" +
                        "<div style=\"font-size:15px; font-weight:500;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all; text-align: center\">" + reconciliationCardSchemeModel.getMadaHostTotalDbCount() + "</div> \n" +
                        "<div style=\"font-size:15px; font-weight:500;width:35% ;max-width:35% ; overflow:hidden; word-break : break-all; text-align: right\">" + reconciliationCardSchemeModel.getMadaHostTotalDbAmountInSar() + "</div> \n" +
                        "</div>"); //small 3 allignment
                reconCardSchemeBuffer.append("<br />" + "<div style=\"font-size:15px; font-weight:500\">" + "إجمالي الدائن" + "</div>"); // small left
                reconCardSchemeBuffer.append("<div style=\"display:flex;justify-content:space-between;width:100%\"> \n" +
                        " <div style=\"font-size:15px; font-weight:500;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all\">" + "TOTAL CR" + "</div> \n" +
                        "<div style=\"font-size:15px; font-weight:500;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all; text-align: center\">" + reconciliationCardSchemeModel.getMadaHostTotalCrCount() + "</div> \n" +
                        "<div style=\"font-size:15px; font-weight:500;width:35% ;max-width:35% ; overflow:hidden; word-break : break-all;text-align: right\">" + reconciliationCardSchemeModel.getMadaHostTotalCrAmountInSar() + "</div> \n" +
                        "</div>"); //small 3 allignment
                reconCardSchemeBuffer.append("<br />" + "<div style=\"font-size:15px; font-weight:500\">" + "نقد" + "</div>"); // small left
                reconCardSchemeBuffer.append("<div style=\"display:flex;justify-content:space-between;width:100%\"> \n" +
                        " <div style=\"font-size:15px; font-weight:500;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all\">" + "NAQD" + "</div> \n" +
                        "<div style=\"font-size:15px; font-weight:500;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all; text-align: center\">" + reconciliationCardSchemeModel.getMadaHostNAQDCount() + "</div> \n" +
                        "<div style=\"font-size:15px; font-weight:500;width:35% ;max-width:35% ; overflow:hidden; word-break : break-all;text-align: right\">" + reconciliationCardSchemeModel.getMadaHostNAQDAmountInSar() + "</div> \n" +
                        "</div>"); //small 3 allignment
                reconCardSchemeBuffer.append("<br />" + "<div style=\"font-size:15px; font-weight:500\">" + "سلفة نقدية" + "</div>"); // small left
                reconCardSchemeBuffer.append("<div style=\"display:flex;justify-content:space-between;width:100%\"> \n" +
                        " <div style=\"font-size:15px; font-weight:500;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all\">" + "C/ADV" + "</div> \n" +
                        "<div style=\"font-size:15px; font-weight:500;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all; text-align: center\">" + reconciliationCardSchemeModel.getMadaHostCashAdvanceCount() + "</div> \n" +
                        "<div style=\"font-size:15px; font-weight:500;width:35% ;max-width:35% ; overflow:hidden; word-break : break-all;text-align: right\">" + reconciliationCardSchemeModel.getMadaHostCashAdvanceAmountInSar() + "</div> \n" +
                        "</div>"); //small 3 allignment
                reconCardSchemeBuffer.append("<br />" + "<div style=\"font-size:15px; font-weight:500\">" + "تفويض" + "</div>"); // small left
                reconCardSchemeBuffer.append("<div style=\"display:flex;justify-content:space-between;width:100%\"> \n" +
                        " <div style=\"font-size:15px; font-weight:500;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all\">" + "AUTH" + "</div> \n" +
                        "<div style=\"font-size:15px; font-weight:500;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all; text-align: center\">" + reconciliationCardSchemeModel.getMadaHostAuthCount() + "</div> \n" +
                        "<div style=\"font-size:15px; font-weight:500;width:35% ;max-width:35% ; overflow:hidden; word-break : break-all ;text-align: right\">" + reconciliationCardSchemeModel.getMadaHostAuthAmountInSar() + "</div> \n" +
                        "</div>"); //small 3 allignment
                reconCardSchemeBuffer.append("<br />" + "<div style=\"font-size:15px; font-weight:900\">" + "المجموع" + "</div>"); // small left bold
                reconCardSchemeBuffer.append("<div style=\"display:flex;justify-content:space-between;width:100%\"> \n" +
                        " <div style=\"font-size:15px; font-weight:900;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all\">" + "TOTALS" + "</div> \n" +
                        "<div style=\"font-size:15px; font-weight:900;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all; text-align: center\">" + reconciliationCardSchemeModel.getMadaHostTotalsCount() + "</div> \n" +
                        "<div style=\"font-size:15px; font-weight:900;width:35% ;max-width:35% ; overflow:hidden; word-break : break-all; text-align: right\">" + reconciliationCardSchemeModel.getMadaHostTotalsAmountInSar() + "</div> \n" +
                        "</div>"); //small 3 allignment bold
            }

            //pos terminal
            reconCardSchemeBuffer.append("<br />" + "<div style=\"float:right;font-size:20px; font-weight:900\">" + "جهاز نقطة البيع" + "</div> \n"); //medium right bold
            reconCardSchemeBuffer.append("<br />" + "<div style=\"font-size:20px; font-weight:900\">" + "POS TERMINAL" + "</div>"); // medium left bold

            reconCardSchemeBuffer.append("<div style=\"display:flex;justify-content:space-between;width:100%\"> \n" +
                    " <div style=\"font-size:15px; font-weight:900;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all\">" + "نوع العملية" + "</div> \n" +
                    "<div style=\"font-size:15px; font-weight:900;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all; text-align: center\">" + "العدد" + "</div> \n" +
                    "<div style=\"font-size:15px; font-weight:900;width:35% ;max-width:35% ; overflow:hidden; word-break : break-all; text-align: right\">" + "المبلغ بالريال" + "</div> \n" +
                    "</div>"); //small 3 allignment bold
            reconCardSchemeBuffer.append("<div style=\"display:flex;justify-content:space-between;width:100%\"> \n" +
                    " <div style=\"font-size:15px; font-weight:900;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all\">" + "TXN TYPE" + "</div> \n" +
                    "<div style=\"font-size:15px; font-weight:900;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all; text-align: center\">" + "COUNT" + "</div> \n" +
                    "<div style=\"font-size:15px; font-weight:900;width:35% ;max-width:35% ; overflow:hidden; word-break : break-all; text-align: right\">" + "AMOUNT IN SAR" + "</div> \n" +
                    "</div>"); //small 3 allignment bold
            reconCardSchemeBuffer.append("<br />" + "<div style=\"font-size:15px; font-weight:500\">" + "إجمالي المدين" + "</div>"); // small left
            reconCardSchemeBuffer.append("<div style=\"display:flex;justify-content:space-between;width:100%\"> \n" +
                    " <div style=\"font-size:15px; font-weight:500;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all\">" + "TOTAL DB" + "</div> \n" +
                    "<div style=\"font-size:15px; font-weight:500;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all; text-align: center\">" + reconciliationCardSchemeModel.getPosTerminalTotalDbCount() + "</div> \n" +
                    "<div style=\"font-size:15px; font-weight:500;width:35% ;max-width:35% ; overflow:hidden; word-break : break-all; text-align: right\">" + reconciliationCardSchemeModel.getPosTerminalTotalDbAmountInSar() + "</div> \n" +
                    "</div>"); //small 3 allignment
            reconCardSchemeBuffer.append("<br />" + "<div style=\"font-size:15px; font-weight:500\">" + "إجمالي الدائن" + "</div>"); // small left
            reconCardSchemeBuffer.append("<div style=\"display:flex;justify-content:space-between;width:100%\"> \n" +
                    " <div style=\"font-size:15px; font-weight:500;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all\">" + "TOTAL CR" + "</div> \n" +
                    "<div style=\"font-size:15px; font-weight:500;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all; text-align: center\">" + reconciliationCardSchemeModel.getPosTerminalTotalCrCount() + "</div> \n" +
                    "<div style=\"font-size:15px; font-weight:500;width:35% ;max-width:35% ; overflow:hidden; word-break : break-all; text-align: right\">" + reconciliationCardSchemeModel.getPosTerminalTotalCrAmountInSar() + "</div> \n" +
                    "</div>"); //small 3 allignment
            reconCardSchemeBuffer.append("<br />" + "<div style=\"font-size:15px; font-weight:500\">" + "نقد" + "</div>"); // small left
            reconCardSchemeBuffer.append("<div style=\"display:flex;justify-content:space-between;width:100%\"> \n" +
                    " <div style=\"font-size:15px; font-weight:500;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all\">" + "NAQD" + "</div> \n" +
                    "<div style=\"font-size:15px; font-weight:500;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all; text-align: center\">" + reconciliationCardSchemeModel.getPosTerminalNAQDCount() + "</div> \n" +
                    "<div style=\"font-size:15px; font-weight:500;width:35% ;max-width:35% ; overflow:hidden; word-break : break-all; text-align: right\">" + reconciliationCardSchemeModel.getPosTerminalNAQDAmountInSar() + "</div> \n" +
                    "</div>"); //small 3 allignment
            reconCardSchemeBuffer.append("<br />" + "<div style=\"font-size:15px; font-weight:500\">" + "سلفة نقدية" + "</div>"); // small left
            reconCardSchemeBuffer.append("<div style=\"display:flex;justify-content:space-between;width:100%\"> \n" +
                    " <div style=\"font-size:15px; font-weight:500;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all\">" + "C/ADV" + "</div> \n" +
                    "<div style=\"font-size:15px; font-weight:500;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all; text-align: center\">" + reconciliationCardSchemeModel.getPasTerminalCashAdvancedCount() + "</div> \n" +
                    "<div style=\"font-size:15px; font-weight:500;width:35% ;max-width:35% ; overflow:hidden; word-break : break-all; text-align: right\">" + reconciliationCardSchemeModel.getPosTerminalCashAdvanceAmountInSar() + "</div> \n" +
                    "</div>"); //small 3 allignment
            reconCardSchemeBuffer.append("<br />" + "<div style=\"font-size:15px; font-weight:500\">" + "تفويض" + "</div>"); // small left
            reconCardSchemeBuffer.append("<div style=\"display:flex;justify-content:space-between;width:100%\"> \n" +
                    " <div style=\"font-size:15px; font-weight:500;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all\">" + "AUTH" + "</div> \n" +
                    "<div style=\"font-size:15px; font-weight:500;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all; text-align: center\">" + reconciliationCardSchemeModel.getPosTerminalAuthCount() + "</div> \n" +
                    "<div style=\"font-size:15px; font-weight:500;width:35% ;max-width:35% ; overflow:hidden; word-break : break-all; text-align: right\">" + reconciliationCardSchemeModel.getPosTerminalAuthAmountInSar() + "</div> \n" +
                    "</div>"); //small 3 allignment
            reconCardSchemeBuffer.append("<br />" + "<div style=\"font-size:15px; font-weight:900\">" + "المجموع" + "</div>"); // small left bold
            reconCardSchemeBuffer.append("<div style=\"display:flex;justify-content:space-between;width:100%\"> \n" +
                    " <div style=\"font-size:15px; font-weight:900;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all\">" + "TOTALS" + "</div> \n" +
                    "<div style=\"font-size:15px; font-weight:900;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all; text-align: center\">" + reconciliationCardSchemeModel.getPosTerminalTotalsCount() + "</div> \n" +
                    "<div style=\"font-size:15px; font-weight:900;width:35% ;max-width:35% ; overflow:hidden; word-break : break-all; text-align: right\">" + reconciliationCardSchemeModel.getPosTerminalTotalsAmountInSar() + "</div> \n" +
                    "</div>"); //small 3 allignment bold

            //pos terminal details
            reconCardSchemeBuffer.append("<br />" + "<div style=\"float:right;font-size:20px; font-weight:900\">" + "تفاصيل جهاز نقطة البيع" + "</div> \n"); //medium right bold
            reconCardSchemeBuffer.append("<br />" + "<div style=\"font-size:20px; font-weight:900\">" + "POS TERMINAL DETAILS" + "</div>"); // medium left bold

            reconCardSchemeBuffer.append("<div style=\"display:flex;justify-content:space-between;width:100%\"> \n" +
                    " <div style=\"font-size:15px; font-weight:900;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all\">" + "نوع العملية" + "</div> \n" +
                    "<div style=\"font-size:15px; font-weight:900;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all; text-align: center\">" + "العدد" + "</div> \n" +
                    "<div style=\"font-size:15px; font-weight:900;width:35% ;max-width:35% ; overflow:hidden; word-break : break-all; text-align: right\">" + "المبلغ بالريال" + "</div> \n" +
                    "</div>"); //small 3 allignment bold
            reconCardSchemeBuffer.append("<div style=\"display:flex;justify-content:space-between;width:100%\"> \n" +
                    " <div style=\"font-size:15px; font-weight:900;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all\">" + "TXN TYPE" + "</div> \n" +
                    "<div style=\"font-size:15px; font-weight:900;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all; text-align: center\">" + "COUNT" + "</div> \n" +
                    "<div style=\"font-size:15px; font-weight:900;width:35% ;max-width:35% ; overflow:hidden; word-break : break-all; text-align: right\">" + "AMOUNT IN SAR" + "</div> \n" +
                    "</div>"); //small 3 allignment bold
            reconCardSchemeBuffer.append("<br />" + "<div style=\"font-size:15px; font-weight:500\">" + "شراء (ب)" + "</div>"); // small left
            reconCardSchemeBuffer.append("<div style=\"display:flex;justify-content:space-between;width:100%\"> \n" +
                    " <div style=\"font-size:15px; font-weight:500;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all\">" + "P/OFF" + "</div> \n" +
                    "<div style=\"font-size:15px; font-weight:500;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all; text-align: center\">" + reconciliationCardSchemeModel.getPosTerminalDetailsPOFFCount() + "</div> \n" +
                    "<div style=\"font-size:15px; font-weight:500;width:35% ;max-width:35% ; overflow:hidden; word-break : break-all; text-align: right\">" + reconciliationCardSchemeModel.getPosTerminalDetailsPOFFAmountInSar() + "</div> \n" +
                    "</div>"); //small 3 allignment
            reconCardSchemeBuffer.append("<br />" + "<div style=\"font-size:15px; font-weight:500\">" + "شراء (ا)" + "</div>"); // small left
            reconCardSchemeBuffer.append("<div style=\"display:flex;justify-content:space-between;width:100%\"> \n" +
                    " <div style=\"font-size:15px; font-weight:500;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all\">" + "P/ON" + "</div> \n" +
                    "<div style=\"font-size:15px; font-weight:500;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all; text-align: center\">" + reconciliationCardSchemeModel.getPosTerminalDetailsPONCount() + "</div> \n" +
                    "<div style=\"font-size:15px; font-weight:500;width:35% ;max-width:35% ; overflow:hidden; word-break : break-all; text-align: right\">" + reconciliationCardSchemeModel.getPosTerminalDetailsPONAmountInSar() + "</div> \n" +
                    "</div>"); //small 3 allignment
            reconCardSchemeBuffer.append("<br />" + "<div style=\"font-size:15px; font-weight:500\">" + "شراء مع نقد" + "</div>"); // small left
            reconCardSchemeBuffer.append("<div style=\"display:flex;justify-content:space-between;width:100%\"> \n" +
                    " <div style=\"font-size:15px; font-weight:500;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all\">" + "PUR NAQD" + "</div> \n" +
                    "<div style=\"font-size:15px; font-weight:500;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all; text-align: center\">" + reconciliationCardSchemeModel.getPosTerminalDetailsPurNaqdCount() + "</div> \n" +
                    "<div style=\"font-size:15px; font-weight:500;width:35% ;max-width:35% ; overflow:hidden; word-break : break-all; text-align: right\">" + reconciliationCardSchemeModel.getPosTerminalDetailsPurNaqdAmountInSar() + "</div> \n" +
                    "</div>"); //small 3 allignment
            reconCardSchemeBuffer.append("<br />" + "<div style=\"font-size:15px; font-weight:500\">" + "عملية معكوسة" + "</div>"); // small left
            reconCardSchemeBuffer.append("<div style=\"display:flex;justify-content:space-between;width:100%\"> \n" +
                    " <div style=\"font-size:15px; font-weight:500;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all\">" + "REVERSAL" + "</div> \n" +
                    "<div style=\"font-size:15px; font-weight:500;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all; text-align: center\">" + reconciliationCardSchemeModel.getPosTerminalDetailsReversalCount() + "</div> \n" +
                    "<div style=\"font-size:15px; font-weight:500;width:35% ;max-width:35% ; overflow:hidden; word-break : break-all; text-align: right\">" + reconciliationCardSchemeModel.getPosTerminalDetailsReversalAmountInSar() + "</div> \n" +
                    "</div>"); //small 3 allignment
            reconCardSchemeBuffer.append("<br />" + "<div style=\"font-size:15px; font-weight:500\">" + "استرداد" + "</div>"); // small left
            reconCardSchemeBuffer.append("<div style=\"display:flex;justify-content:space-between;width:100%\"> \n" +
                    " <div style=\"font-size:15px; font-weight:500;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all\">" + "REFUND" + "</div> \n" +
                    "<div style=\"font-size:15px; font-weight:500;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all; text-align: center\">" + reconciliationCardSchemeModel.getPosTerminalDetailsRefundCount() + "</div> \n" +
                    "<div style=\"font-size:15px; font-weight:500;width:35% ;max-width:35% ; overflow:hidden; word-break : break-all; text-align: right\">" + reconciliationCardSchemeModel.getPosTerminalDetailsRefundAmountInSar() + "</div> \n" +
                    "</div>"); //small 3 allignment
            reconCardSchemeBuffer.append("<br />" + "<div style=\"font-size:15px; font-weight:500\">" + "اكتمال" + "</div>"); // small left
            reconCardSchemeBuffer.append("<div style=\"display:flex;justify-content:space-between;width:100%\"> \n" +
                    " <div style=\"font-size:15px; font-weight:500;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all\">" + "COMP" + "</div> \n" +
                    "<div style=\"font-size:15px; font-weight:500;width:30% ;max-width:30% ; overflow:hidden; word-break : break-all; text-align: center\">" + reconciliationCardSchemeModel.getPosTerminalDetailsCompCount() + "</div> \n" +
                    "<div style=\"font-size:15px; font-weight:500;width:35% ;max-width:35% ; overflow:hidden; word-break : break-all; text-align: right\">" + reconciliationCardSchemeModel.getPosTerminalDetailsCompAmountInSar() + "</div> \n" +
                    "</div>"); //small 3 allignment


        } else {
            reconCardSchemeBuffer.append("<br />" + "<div style=\"float:right;font-size:20px; font-weight:900\">" + "<لايوجد عمليات>" + "</div> \n"); //medium right bold
            reconCardSchemeBuffer.append("<br />" + "<div style=\"font-size:20px; font-weight:900\">" + "<NO TRANSACTIONS>" + "</div>"); // medium left bold
        }
        return reconCardSchemeBuffer;
    }


    //*******************************print recon scheme layout for q2**************************


    //*******************************print recon top layout for q2**************************

    public static StringBuffer reconciliationTopLayout(ReconcilationTopModel reconcilationTopModel) {
        Logger.v("reconciliationTopLayout");

        //print logo TODO topStringBuffer.append("*image x:90 200*80 path:" + bmp0 + "\n");
        StringBuffer reconTopStringBuffer = new StringBuffer();

        reconTopStringBuffer.append("<div style=\"text-align: center;font-size:30px; font-weight:900\">" + reconcilationTopModel.getRetailerNameArabic() + "</div>" + "\n"); //LARGE CENTER
        reconTopStringBuffer.append("<div style=\"text-align: center;font-size:30px; font-weight:900\">" + reconcilationTopModel.getRetailerNameEnglish() + "</div>" + "\n"); //LARGE CENTER
        reconTopStringBuffer.append("<div style=\"text-align:center;font-size:20px; font-weight:500\">" + reconcilationTopModel.getTerminalCityArabic() + ", " + reconcilationTopModel.getTerminalStreetArabic() + "</div>"); //SMALL CENTER
        reconTopStringBuffer.append("<div style=\"text-align:center;font-size:20px; font-weight:500\">" + reconcilationTopModel.getTerminalStreetEnglish() + ", " + reconcilationTopModel.getTerminaCityEnglish() + "</div>"); //SMALL CENTER
        reconTopStringBuffer.append("<div style=\"text-align:center;font-size:15px; font-weight:400\">" + "TEL: +" + reconcilationTopModel.getRetailerTelephone() + "</div>"); //small center
        reconTopStringBuffer.append("<div style=\"display:flex;justify-content:space-between;width:100%\"> \n" +
                " <div style=\"font-size:20px; font-weight:500\">" + reconcilationTopModel.getStartDate() + "</div> \n" +
                "<div style=\"font-size:20px; font-weight:500\">" + reconcilationTopModel.getStartTime() + "</div> \n" +
                "</div>"); // medium 2 allignment
        reconTopStringBuffer.append("<div style=\"display:flex;justify-content:space-between;width:100%\"> \n" +
                " <div style=\"font-size:15px; font-weight:500\">" + reconcilationTopModel.getSchemID() + "</div> \n" +
                "<div style=\"font-size:15px; font-weight:500\">" + reconcilationTopModel.getmId() + "</div> \n" +
                "<div style=\"font-size:15px; font-weight:500\">" + reconcilationTopModel.gettId() + "</div> \n" +
                "</div>"); //small 3 allignment
        reconTopStringBuffer.append("<div style=\"display:flex;justify-content:space-between;width:100%\"> \n" +
                " <div style=\"font-size:15px; font-weight:500\">" + reconcilationTopModel.getMcc() + "</div> \n" +
                "<div style=\"font-size:15px; font-weight:500\">" + reconcilationTopModel.getTransactionSTAN() + "</div> \n" +
                "<div style=\"font-size:15px; font-weight:500\">" + reconcilationTopModel.getPosSoftwareVersionNumber() + "</div> \n" +
                "</div>");
        reconTopStringBuffer.append("<br />" + "<div style=\"float:right;font-size:20px; font-weight:900\">" + reconcilationTopModel.getTransactionTypeArabic() + "</div> \n"); //medium right bold
        reconTopStringBuffer.append("<br />" + "<div style=\"font-size:20px; font-weight:900\">" + reconcilationTopModel.getTransactionTypeEnglish() + "</div>"); // medium left bold

        reconTopStringBuffer.append("<br />" + "<div style=\"float:right;font-size:20px; font-weight:900\">" + reconcilationTopModel.getTransactionOutcomeArabic() + "</div> \n"); //medium right bold
        reconTopStringBuffer.append("<br />" + "<div style=\"font-size:20px; font-weight:900\">" + reconcilationTopModel.getTransactionOutcomeEnglish() + "</div>"); // medium left bold
        reconTopStringBuffer.append("<div style=\"text-align:center;font-size:20px; font-weight:900\">" + "**********************************************" + "</div>"); //medium center
        return reconTopStringBuffer;
    }


    //*******************************print recon top layout for q2**************************


    //*******************************print recon bottom layout for q2**************************

    public static StringBuffer reconiclationBottomLayout(ReconciliationBottomModel reconciliationBottomModel) {
        Logger.v("reconiclationBottomLayout");
        StringBuffer reconBottomStringBuffer = new StringBuffer();

        reconBottomStringBuffer.append("<div style=\"text-align:center;font-size:20px; font-weight:900\">" + "**********************************************" + "</div>"); //medium center
        reconBottomStringBuffer.append("<div style=\"display:flex;justify-content:space-between;width:100%\"> \n" +
                " <div style=\"font-size:20px; font-weight:500\">" + reconciliationBottomModel.getEndDate() + "</div> \n" +
                "<div style=\"font-size:20px; font-weight:500\">" + reconciliationBottomModel.getEndTime() + "</div> \n" +
                "</div>"); // medium 2 allignment
        reconBottomStringBuffer.append("<div style=\"text-align:center;font-size:15px; font-weight:500\">" + reconciliationBottomModel.getThankYouArabic() + "</div>"); //small center
        reconBottomStringBuffer.append("<div style=\"text-align:center;font-size:15px; font-weight:500\">" + reconciliationBottomModel.getThankYouEnglish() + "</div>"); //small center
        reconBottomStringBuffer.append("<div style=\"text-align:center;font-size:15px; font-weight:500\">" + reconciliationBottomModel.getRecordOfTransctionArabic() + "</div>"); //small center

        reconBottomStringBuffer.append("<div style=\"text-align:center;font-size:15px; font-weight:500\">" + reconciliationBottomModel.getRecordOfTransctionEnglish() + "</div>"); //small center
        reconBottomStringBuffer.append("<div style=\"text-align:center;font-size:15px; font-weight:900\">" + reconciliationBottomModel.getReceiptVersionArabic() + "</div>"); //small center bold
        reconBottomStringBuffer.append("<div style=\"text-align:center;font-size:15px; font-weight:500\">" + reconciliationBottomModel.getReceiptVersionEnglish() + "</div>"); //small center bold
        reconBottomStringBuffer.append("<div style=\"text-align:center;font-size:15px; font-weight:500\">" +" ."+ "</div>"); //small center bold
        reconBottomStringBuffer.append("<div style=\"text-align:center;font-size:15px; font-weight:500\">" + ". "+"</div>"); //small center bold
        reconBottomStringBuffer.append("<div style=\"text-align:center;font-size:15px; font-weight:500\">" + " ."+"</div>"); //small center bold
        reconBottomStringBuffer.append("<div style=\"text-align:center;font-size:15px; font-weight:500\">" + ". "+"</div>"); //small center bold
        reconBottomStringBuffer.append("<div style=\"text-align:center;font-size:15px; font-weight:500\">" +" ."+ "</div>"); //small center bold

        return reconBottomStringBuffer;
    }


    //*******************************print recon bottom layout for q2**************************


   /* public static boolean topPrintBill(PrinterDevice printerDevice, String s, Bitmap map, Context context) {
//        PrinterResult printerResult = mPrinter.printByScript(PrintContext.defaultContext(), s, map, 60, TimeUnit.SECONDS);

        try {
            printerDevice.open();
            Format format = new Format();
            format.setParameter(Format.FORMAT_ALIGN, Format.FORMAT_ALIGN_CENTER);
            printerDevice.printBitmap(format, map);
            printerDevice.printHTML(context, s, null);
            if (printerDevice.queryStatus() == 1) {
                Log.i("Printer status :", "Success");
                return true;
//            showMessage(context.getString(R.string.msg_print_script_success) + "\r\n", MessageTag.NORMAL);
            } else {
                Log.i("Pinter status :", "Fail");
                return false;
                //     showMessage(context.getString(R.string.msg_print_script_error) + printerResult.toString() + "\r\n", MessageTag.NORMAL);
            }
        } catch (Exception e) {
            return false;
        }
    }*/

    static Executor executor = Executors.newSingleThreadExecutor();

    public static void topPrintBill(final PrinterDevice printerDevice, final String s, final Bitmap map, final Context context) {
        final Object syncPrintHtmlLock = new Object();
        final boolean result[] = new boolean[1];
        //synchronized is not run UI thread
//        Executor executor= Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    printerDevice.open();
                    // Bitmap bitmap = null;
                    // bitmap = BitmapFactory.decodeStream(context.getResources().getAssets().open("print_logo.png"));
                    final Format format = new Format();
                    format.setParameter(Format.FORMAT_ALIGN, Format.FORMAT_ALIGN_CENTER);
                    synchronized (syncPrintHtmlLock) {
                        //render web view
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (map != null)
                                        printerDevice.printBitmap(format, map);
                                    printerDevice.printHTML(context, s.toString(), new PrinterHtmlListener() {
                                        @Override
                                        public void onGet(Bitmap bitmap, int i) {

                                        }

                                        @Override
                                        public void onFinishPrinting(int i) {
                                            try {
                                                // Logger.v("qrdata----" + printerModel.getQrCodeData());
                                                synchronized (syncPrintHtmlLock) {
                                                    syncPrintHtmlLock.notifyAll();
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            Logger.v("printer---q2-----success----printbill");
                                        }
                                    });

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Logger.v("top_print_catch_block----0" + e.getMessage());
                                }
                            }
                        });

                        try {
                            syncPrintHtmlLock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Logger.v("top_print_catch_block----1" + e.getMessage());
                        }
                        //print bitmap
                        //  printerDevice.printBitmap(format, map );
                        printerDevice.printlnText(" ");
                        // printerDevice.printlnText(" ");
                        // printerDevice.printlnText(" ");
                    }
                    if (printerDevice.queryStatus() == 1) {
                        Logger.v("Print_success");
                        result[0] = true;
                    } else {
                        Logger.v("Print_failed");
                        result[0] = false;
                    }
                    printerDevice.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    Logger.v("top_print_catch_block----2" + e.getMessage());
                }
            }
        });

        Logger.v("top_print_final_status---" + result[0]);
//        return result[0];
        // return true;

    }

    public static void topPrintBillData(final PrinterDevice printerDevice, final String s, final Bitmap map, final Context context, final BaseViewModel.PrintComplete complete) {
        final boolean[] isPrintComplete = {false};
        try {
            printerDevice.open();
            final Format format = new Format();
            format.setParameter(Format.FORMAT_ALIGN, Format.FORMAT_ALIGN_CENTER);
            if (map != null)
                printerDevice.printBitmap(format, map);
            Logger.v("String size --"+s.length());
            printerDevice.printHTML(context, s.toString(), new PrinterHtmlListener() {
                @Override
                public void onGet(Bitmap bitmap, int i) {

                }

                @Override
                public void onFinishPrinting(int i) {
                    if (isPrintComplete[0]) {
                        Logger.v("Print complete again");
                        return;
                    }
                    Logger.v("printer---q2-----success----printbill");
                    try {
                        if (printerDevice.queryStatus() == 1) {
                            Logger.v("Print_success 1");
                        } else {
                            Logger.v("Print_failed");
                        }
                        printerDevice.close();
                    } catch (DeviceException e) {
                        e.printStackTrace();
                    }
                        isPrintComplete[0] = true;
                        complete.onFinish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Logger.v("top_print_catch_block----2" + e.getMessage());
        }
    }

    public static void printRegistration(PrinterDevice mPrinter, Context context, BaseViewModel.PrintComplete complete) {
        List<KeyValueModel> modelList = new ArrayList<>();
        modelList.add(new KeyValueModel("**** REGISTRATION FAILED ****"));
        modelList.add(new KeyValueModel("Terminal ID    :" + AppManager.getInstance().getVendorTerminalSerialNumber()));
        modelList.add(new KeyValueModel("Terminal Type  :" + AppManager.getInstance().getVendorTerminalType()));
        modelList.add(new KeyValueModel("Responce Code  :" + AppManager.getInstance().getDe39()));
        modelList.add(new KeyValueModel("Date & Time    :" + Utils.getCurrentDate()));
        try {
            PrinterReceipt.printKeyValue(modelList, mPrinter, context, complete);
        } catch (DeviceException e) {
            e.printStackTrace();
        }
    }
}
