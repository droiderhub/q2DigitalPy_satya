package com.tarang.dpq2.viewmodel;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.cloudpos.printer.PrinterDevice;
import com.tarang.dpq2.R;
import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.jpos_class.ConstantApp;
import com.tarang.dpq2.base.jpos_class.ConstantAppValue;
import com.tarang.dpq2.base.terminal_sdk.utils.PrinterReceipt;
import com.tarang.dpq2.base.utilities.CountDownResponseTimer;
import com.tarang.dpq2.worker.PrinterWorker;
import com.tarang.dpq2.worker.SocketConnectionWorker;

import static com.tarang.dpq2.base.terminal_sdk.AppConfig.EMV.reqObj;

public class BaseViewModel extends AndroidViewModel implements ConstantAppValue {

    public static int SAVE_CONNECT = 0;
    public static int SAVE_CONNECT_AGAIN = 2;
    public static int CONNECT_ONLY = 1;

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }

    public void checkReversal(Reversal reversal, int i) {
        Logger.v("Check Reversal --" + reqObj.getMti0());
        Logger.v("Check Reversal --" + reqObj.getNameTransactionTag());
        boolean makeCall = true;
        if ((((reqObj.getNameTransactionTag().equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE)
                || reqObj.getNameTransactionTag().equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_FULL)
                || reqObj.getNameTransactionTag().equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_PARTIAL)
                || reqObj.getNameTransactionTag().equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_MANUAL))
                && ((reqObj.getMti0().equalsIgnoreCase(ConstantAppValue.FINANCIAL_ADVISE_REPEAT) && SocketConnectionWorker.failureCount <= 2)
                || AppManager.getInstance().isAdminNotificationReversal())))
                || reqObj.getNameTransactionTag().equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION_VOID)
                || reqObj.getNameTransactionTag().equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION_EXTENSION)
                || reqObj.getNameTransactionTag().equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION)) {
            makeCall = false;
            reqObj.setResponseCode39(null);
            reqObj.setAuthIdResCode38(null);
        } if (reqObj.getMti0().equalsIgnoreCase(AUTH_ADVISE) || reqObj.getMti0().equalsIgnoreCase(AUTH_ADVISE_REPEAT))
            reqObj.setMti0(AUTH_ADVISE_REPEAT);
        else if (reqObj.getMti0().equalsIgnoreCase(FINANCIAL_ADVISE) || reqObj.getMti0().equalsIgnoreCase(FINANCIAL_ADVISE_REPEAT))
            reqObj.setMti0(FINANCIAL_ADVISE_REPEAT);
        else if (reqObj.getMti0().equalsIgnoreCase(REVERSAL) || reqObj.getMti0().equalsIgnoreCase(REVERSAL_REPEAT))
            reqObj.setMti0(REVERSAL_REPEAT);
        else if (reqObj.getMti0().equalsIgnoreCase(RECONCILIATION) || reqObj.getMti0().equalsIgnoreCase(RECONCILIATION_REPEAT))
            reqObj.setMti0(RECONCILIATION_REPEAT);
        else if (reqObj.getMti0().equalsIgnoreCase(FILEACTION) || reqObj.getMti0().equalsIgnoreCase(FILEACTION_REPEAT))
            reqObj.setMti0(FILEACTION_REPEAT);
        else {
            makeCall = false;
        }
        if (makeCall)
            reversal.doRepeat();
        else {
            if (reqObj.getMti0().equalsIgnoreCase(AUTH) || reqObj.getMti0().equalsIgnoreCase(FINANCIAL)
                    || reqObj.getNameTransactionTag().equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE)
                    || reqObj.getNameTransactionTag().equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_FULL)
                    || reqObj.getNameTransactionTag().equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_PARTIAL)
                    || reqObj.getNameTransactionTag().equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_MANUAL)
                    || reqObj.getNameTransactionTag().equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION_VOID)
                    || reqObj.getNameTransactionTag().equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION_EXTENSION)
                    || reqObj.getNameTransactionTag().equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION)) {
                AppManager.getInstance().setReversalManual(false);
                reversal.doReversal();
            } else
                Logger.v("Reversal Else");
        }
    }

    public boolean increamentRetryCount(boolean disableAttempt) {
        int retryCount = AppManager.getInstance().getSafRetryCount();
        Logger.v("retry count --" + retryCount);
        Logger.v("retry count --" + SocketConnectionWorker.failureCount);
        if ((retryCount - 1) <= (SocketConnectionWorker.failureCount) ||
                (disableAttempt && ((SocketConnectionWorker.failureCount != -1) &&
                        !reqObj.getMti0().equalsIgnoreCase(ConstantAppValue.REVERSAL_REPEAT)
                        && !reqObj.getMti0().equalsIgnoreCase(ConstantAppValue.FINANCIAL_ADVISE_REPEAT)))) {
            CountDownResponseTimer.cancelTimer(5);
            return false;
        }
        SocketConnectionWorker.failureCount = SocketConnectionWorker.failureCount + 1;
        return true;
    }


    public interface Reversal{
        public void doReversal();
        public void doRepeat();
    }


    public interface PrintComplete{
        public void onFinish();
    }

    public void doPrintTransactionReceipt(PrinterDevice devicePrinter, Context context, PrintComplete complete) {
        Logger.v("doPrintTransactionReceipt");
        Bitmap logoBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.mada_logo_black_small);
//        Bitmap logoBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.mada_logo_black);
//        Bitmap logoBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.digitalpay_logo);
        PrinterReceipt.printBill(PrinterWorker.printerModel, logoBitmap, devicePrinter, context,complete);
    }
    public void doPrintRecipt(PrinterDevice devicePrinter, Context context, PrintComplete complete) {
        Logger.v("doPrintRecipt");
        Bitmap logoBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.mada_logo_black_small);
//        Bitmap logoBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.mada_logo_black);
        PrinterReceipt.topPrintBillData(devicePrinter,PrinterReceipt.printData, logoBitmap, context,complete);
    }



}
