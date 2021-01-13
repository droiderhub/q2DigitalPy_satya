package com.tarang.dpq2.viewmodel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.cloudpos.printer.PrinterDevice;
import com.tarang.dpq2.R;
import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.room_database.db.AppDatabase;
import com.tarang.dpq2.base.terminal_sdk.device.SDKDevice;
import com.tarang.dpq2.base.terminal_sdk.utils.PrinterReceipt;
import com.tarang.dpq2.base.utilities.Utils;
import com.tarang.dpq2.worker.LoadKeyWorker;
import com.tarang.dpq2.worker.PacketDBInfoWorker;
import com.tarang.dpq2.worker.PrinterWorker;
import com.tarang.dpq2.worker.SAFWorker;
import com.tarang.dpq2.worker.SocketConnectionWorker;

public class MenuRepository {

    private final Context context;
    private final LifecycleOwner owner;
    private final WorkManager mWorkManager;
    AppDatabase database;
    PrinterDevice devicePrinter;
    private Bitmap logoBitmap;
    public int uid;

    public MenuRepository(Context context, LifecycleOwner owner) {
        this.context = context;
        this.owner = owner;
        SocketConnectionWorker.failureCount = -1;
        SocketConnectionWorker.cancelledRequest = false;
        mWorkManager = WorkManager.getInstance(context);
        this.database = AppDatabase.getInstance(context.getApplicationContext());
        devicePrinter = SDKDevice.getInstance(context).getPrinter();
        uid = AppManager.getInstance().getSnapshotID();
        logoBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.mada_logo_black);

    }

    public void makeSocketConnection(Observer<WorkInfo> status) {
        Logger.v("makeSocketConnection_menuRepo");
        if (Utils.isInternetAvailable(context,true)) {
            SocketConnectionWorker.setEventHandler(MenuViewModel.getEventHandler(),2);
            OneTimeWorkRequest mRequest = new OneTimeWorkRequest.Builder(SocketConnectionWorker.class).build();
            mWorkManager.enqueue(mRequest);
            Logger.v("UUID --" + mRequest.getId());
            mWorkManager.getWorkInfoByIdLiveData(mRequest.getId()).observe(owner, status);
        }
    }

    public void fetchDataFromDatabase(String data, Observer<WorkInfo> status) {
        WorkRequest.Builder mRequest = new OneTimeWorkRequest.Builder(PacketDBInfoWorker.class);
        Data.Builder dataSet = new Data.Builder();
        dataSet.putString(PacketDBInfoWorker.TRANSACTION_TYPE, data);
        mRequest.setInputData(dataSet.build());
        WorkRequest build = mRequest.build();
        mWorkManager.enqueue(build);
        mWorkManager.getWorkInfoByIdLiveData(build.getId()).observe(owner, status);
    }

    public void loadKeys(Observer<WorkInfo> status) {
        WorkManager mWorkManager = WorkManager.getInstance(context);
        OneTimeWorkRequest.Builder mRequest = new OneTimeWorkRequest.Builder(LoadKeyWorker.class);
        WorkRequest build = mRequest.build();
        mWorkManager.enqueue(build);
        mWorkManager.getWorkInfoByIdLiveData(build.getId()).observe(owner, status);
    }

    public void loadOnlyKeys(Observer<WorkInfo> status) {
        Logger.v("loadOnlyKeys");
        WorkManager mWorkManager = WorkManager.getInstance(context);
        OneTimeWorkRequest.Builder mRequest = new OneTimeWorkRequest.Builder(LoadKeyWorker.class);
        Data.Builder dataSet = new Data.Builder();
        dataSet.putBoolean(LoadKeyWorker.INJECT_KEYS, true);
        mRequest.setInputData(dataSet.build());
        WorkRequest build = mRequest.build();
        mWorkManager.enqueue(build);
        mWorkManager.getWorkInfoByIdLiveData(build.getId()).observe(owner, status);
    }



/*
    public void printReceipt(int stat, Observer<WorkInfo> status) {
        WorkRequest.Builder mRequest = new OneTimeWorkRequest.Builder(PrinterWorker.class);
        Data.Builder dataSet = new Data.Builder();
        if (stat == 0) {
            Logger.v("Recon");
            //TODO create thread to access db else the db is null
            dataSet.putBoolean(PrinterWorker.RECONSILATION_REQUEST, true);
            boolean printstatus=  PrinterReceipt.printBillReconciliation(database, devicePrinter, logoBitmap, context);
            Logger.v("printstatus_recon-----"+printstatus);
            dataSet.putBoolean(PrinterWorker.STATUS, printstatus);
            if (printstatus) {
                Logger.v("printstatus_recon---nuke_table--" + printstatus);
                database.getTransactionDao().nukeTable();
            }

        }
        //recon from printerworker
        else if (stat == 1 || stat ==2)

        {
//            dataSet.putBoolean(PrinterWorker.RUNNING_TOTAL, true);
            int uidLat = database.getTransactionDao().getLastTransactionUid();
            Logger.v("SnapShot --" + uidLat);
            Logger.v("SnapShot --" + uid);
            if (uid < uidLat) {
                boolean printstatus = PrinterReceipt.printBillSnapShot(database, devicePrinter, uid, true, logoBitmap,context);
            } else {
                Logger.v("uid > uidLat");
            }
        }
       */
/* else if (stat == 2)
            dataSet.putBoolean(PrinterWorker.SNAPSHOT_TOTAL, true);*//*

       */
/* else if (stat == 3 || stat == 4) {
            Logger.v("Recon Dup");
//            dataSet.putBoolean(PrinterWorker.RECONSILATION_REQUEST_DUPLICATE, true);
            boolean printstatus = PrinterReceipt.printBillReconciliationDuplicate(devicePrinter, logoBitmap, context);
        }*//*

        else if (stat == 3 || stat == 4) {
            Logger.v("Recon Dup");
//            dataSet.putBoolean(PrinterWorker.RECONSILATION_REQUEST_DUPLICATE, true);
            boolean printstatus = PrinterReceipt.printBillReconciliationDuplicate(devicePrinter, logoBitmap, context);
            dataSet.putBoolean(PrinterWorker.STATUS, printstatus);
            dataSet.putBoolean(PrinterWorker.RECONSILATION_REQUEST_DUPLICATE, true);
        }
        mRequest.setInputData(dataSet.build());
        WorkRequest build = mRequest.build();
        mWorkManager.enqueue(build);
        Logger.v("UUID -1-" + build.getId());
        mWorkManager.getWorkInfoByIdLiveData(build.getId()).observe(owner, status);
    }
*/

    public void printReceipt(int stat, Observer<WorkInfo> status) {
        WorkRequest.Builder mRequest = new OneTimeWorkRequest.Builder(PrinterWorker.class);
        Data.Builder dataSet = new Data.Builder();
        if (stat == 0)
            dataSet.putBoolean(PrinterWorker.RECONSILATION_REQUEST, true);
        else if (stat == 1)
            dataSet.putBoolean(PrinterWorker.RUNNING_TOTAL, true);
        else if (stat == 2)
            dataSet.putBoolean(PrinterWorker.SNAPSHOT_TOTAL, true);
        else if (stat == 3)
            dataSet.putBoolean(PrinterWorker.RECONSILATION_REQUEST_DUPLICATE, true);
        else if (stat == 4)
            dataSet.putBoolean(PrinterWorker.RECONSILATION_REQUEST_DUPLICATE, true);
        mRequest.setInputData(dataSet.build());
        WorkRequest build = mRequest.build();
        mWorkManager.enqueue(build);
        Logger.v("UUID -1-" + build.getId());
        mWorkManager.getWorkInfoByIdLiveData(build.getId()).observe(owner, status);
    }
    public void printReceipt(Observer<WorkInfo> status) {
        WorkRequest.Builder mRequest = new OneTimeWorkRequest.Builder(PrinterWorker.class);
        Data.Builder dataSet = new Data.Builder();
        dataSet.putBoolean(PrinterWorker.DUPLICATE_PRINT,true);
        mRequest.setInputData(dataSet.build());
        WorkRequest build = mRequest.build();
        mWorkManager.enqueue(build);
        Logger.v("UUID -1-" + build.getId());
        mWorkManager.getWorkInfoByIdLiveData(build.getId()).observe(owner, status);
    }

    public void checkAllSAF(final boolean repeat, final Observer<WorkInfo> saf_worked) {
        if(Utils.isInternetAvailable(context,true)) {
            SAFWorker.setEventHandler(MenuViewModel.getEventHandler());

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    //TODO execeute code
                    WorkManager mWorkManager = WorkManager.getInstance(context);
                    OneTimeWorkRequest.Builder mRequest = new OneTimeWorkRequest.Builder(SAFWorker.class);
                    Data.Builder dataSet = new Data.Builder();
                    dataSet.putBoolean(SAFWorker.SAF_COUNT_ALL, true);
                    dataSet.putBoolean(SAFWorker.SAF_DO_REPEAT, repeat);
                    mRequest.setInputData(dataSet.build());
                    WorkRequest build = mRequest.build();
                    mWorkManager.enqueue(build);
                    mWorkManager.getWorkInfoByIdLiveData(build.getId()).observe(owner, saf_worked);
                }
            });
        }
    }

}
