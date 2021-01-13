package com.tarang.dpq2.viewmodel;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.tarang.dpq2.base.AppInit;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.jpos_class.ConstantApp;
import com.tarang.dpq2.base.terminal_sdk.AppConfig;
import com.tarang.dpq2.base.terminal_sdk.event.SimpleTransferListener;
import com.tarang.dpq2.base.utilities.Utils;
import com.tarang.dpq2.worker.InsertTransactionWorker;
import com.tarang.dpq2.worker.PacketDBInfoWorker;
import com.tarang.dpq2.worker.PrinterWorker;
import com.tarang.dpq2.worker.SAFWorker;
import com.tarang.dpq2.worker.SocketConnectionWorker;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.tarang.dpq2.base.terminal_sdk.AppConfig.EMV.swipResult;

public class TransactionRepository {
//    private final K21Swiper k21swiper;
//    private final EmvModule emvModule;
//    private EmvTransController controller;

    Context context;
    LifecycleOwner owner;
    WorkManager mWorkManager;
    public UUID uuID;


    public TransactionRepository(Context context, LifecycleOwner owner) {
        this.context = context;
        this.owner = owner;
        SocketConnectionWorker.failureCount = -1;
        SocketConnectionWorker.cancelledRequest = false;
        mWorkManager = WorkManager.getInstance(context);
//        k21swiper = SDKDevice.getInstance(context).getK21Swiper();
//        emvModule = SDKDevice.getInstance(context).getEmvModuleType();
    }

    public void insertTransaction(String dateExpiry, final Observer<WorkInfo> status) {
        mWorkManager = WorkManager.getInstance(context);
        mWorkManager.cancelWorkById(this.uuID);
        OneTimeWorkRequest.Builder mRequest = new OneTimeWorkRequest.Builder(InsertTransactionWorker.class);
        Data.Builder dataSet = new Data.Builder();
        dataSet.putString(InsertTransactionWorker.EXPIRY_DATE, dateExpiry);
        dataSet.putBoolean(InsertTransactionWorker.SAF_REQUEST, false);
        mRequest.setInputData(dataSet.build());
        WorkRequest build = mRequest.build();
        mWorkManager.enqueue(build);
        this.uuID = build.getId();
        mWorkManager.getWorkInfoByIdLiveData(build.getId()).observe(owner, status);
    }

    public void insertSAFTransaction(String dateExpiry, final Observer<WorkInfo> status) {
        mWorkManager = WorkManager.getInstance(context);
        mWorkManager.cancelWorkById(this.uuID);
        OneTimeWorkRequest.Builder mRequest = new OneTimeWorkRequest.Builder(InsertTransactionWorker.class);
        Data.Builder dataSet = new Data.Builder();
        dataSet.putString(InsertTransactionWorker.EXPIRY_DATE, dateExpiry);
        dataSet.putBoolean(InsertTransactionWorker.SAF_REQUEST, true);
        mRequest.setInputData(dataSet.build());
        WorkRequest build = mRequest.build();
        mWorkManager.enqueue(build);
        this.uuID = build.getId();
        mWorkManager.getWorkInfoByIdLiveData(build.getId()).observe(owner, status);
    }

    public void makeSocketConnection(Observer<WorkInfo> status) {
        Logger.v("makeSocketConnection_transaction_repo");
        if (Utils.isInternetAvailable(context,true)) {
            mWorkManager.cancelWorkById(this.uuID);
            mWorkManager.cancelUniqueWork("REQUESTWORK");
            OneTimeWorkRequest mRequest = new OneTimeWorkRequest.Builder(SocketConnectionWorker.class).addTag("REQUESTWORK").build();
            mWorkManager.enqueueUniqueWork("REQUESTWORK", ExistingWorkPolicy.REPLACE , mRequest);
            Logger.v("UUID --" + mRequest.getId());
            this.uuID = mRequest.getId();
            mWorkManager.getWorkInfoByIdLiveData(mRequest.getId()).observe(owner, status);
        }
    }

    public void cancelRequest(){
        Logger.v("Cancelled--11");
        mWorkManager.cancelWorkById(this.uuID);
        mWorkManager.cancelUniqueWork("REQUESTWORK");
        mWorkManager.cancelAllWorkByTag("REQUESTWORK");
        mWorkManager.cancelAllWork();
        mWorkManager.pruneWork();
    }

    public void printReceipt(Observer<WorkInfo> status) {
        SocketConnectionWorker.failureCount = -1;
        mWorkManager.cancelWorkById(this.uuID);
        OneTimeWorkRequest.Builder mRequest = new OneTimeWorkRequest.Builder(PrinterWorker.class);
        Data.Builder dataSet = new Data.Builder();
        dataSet.putBoolean(PrinterWorker.SAF_REQUEST,false);
        dataSet.putBoolean(PrinterWorker.CANCELLED_REQUEST,(SimpleTransferListener.isScreenAvailable == 2));
        mRequest.setInputData(dataSet.build());
        WorkRequest build = mRequest.build();
        mWorkManager.enqueue(build);
        Logger.v("UUID -1-" + build.getId());
        this.uuID = build.getId();
        mWorkManager.getWorkInfoByIdLiveData(build.getId()).observe(owner, status);
    }
    public void printSAFReceipt(Observer<WorkInfo> status) {
        SocketConnectionWorker.failureCount = -1;
        mWorkManager.cancelWorkById(this.uuID);
        Logger.v("SAF REquest");
        OneTimeWorkRequest.Builder mRequest = new OneTimeWorkRequest.Builder(PrinterWorker.class);
        Data.Builder dataSet = new Data.Builder();
        dataSet.putBoolean(PrinterWorker.SAF_REQUEST,true);
        mRequest.setInputData(dataSet.build());
        WorkRequest build = mRequest.build();
        mWorkManager.enqueue(build);
        Logger.v("UUID -1-" + build.getId());
        this.uuID = build.getId();
        mWorkManager.getWorkInfoByIdLiveData(build.getId()).observe(owner, status);
    }

    public void fetchDataFromDatabase(String transactionType, Observer<WorkInfo> status) {
        mWorkManager.cancelWorkById(this.uuID);
        WorkRequest.Builder mRequest = new OneTimeWorkRequest.Builder(PacketDBInfoWorker.class);
        Data.Builder dataSet = new Data.Builder();
        dataSet.putString(PacketDBInfoWorker.CARD_TYPE, PacketDBInfoWorker.ICRFCARD);
        dataSet.putString(PacketDBInfoWorker.TRANSACTION_TYPE, transactionType);
        //dataSet.putString(PacketDBInfoWorker.CARD_SCHEME,aid);
        mRequest.setInputData(dataSet.build());
        WorkRequest build = mRequest.build();
        mWorkManager.enqueue(build);
        this.uuID = build.getId();
        mWorkManager.getWorkInfoByIdLiveData(build.getId()).observe(owner, status);
    }

    public void fetchDataFromDatabase(String transactionType, String accNo, Observer<WorkInfo> status) {
        Logger.v("fetchDataFromDatabase_txntype----"+transactionType+"-----"+accNo);
        mWorkManager.cancelWorkById(this.uuID);
        WorkRequest.Builder mRequest = new OneTimeWorkRequest.Builder(PacketDBInfoWorker.class);
        Data.Builder dataSet = new Data.Builder();
        dataSet.putString(PacketDBInfoWorker.CARD_TYPE, PacketDBInfoWorker.MSCARD);
        dataSet.putString(PacketDBInfoWorker.CARD_NUMBER, accNo.substring(0, 6));
        dataSet.putString(PacketDBInfoWorker.TRANSACTION_TYPE, transactionType);
        mRequest.setInputData(dataSet.build());
        WorkRequest build = mRequest.build();
        mWorkManager.enqueue(build);
        this.uuID = build.getId();
        mWorkManager.getWorkInfoByIdLiveData(build.getId()).observe(owner, status);
        Logger.v("build.getId()_fetchDataFromDatabase----"+build.getId());
    }

    public void fetchDataFromDatabaseManual(String transactionType, String accNo, Observer<WorkInfo> status) {
        mWorkManager.cancelWorkById(this.uuID);
        WorkRequest.Builder mRequest = new OneTimeWorkRequest.Builder(PacketDBInfoWorker.class);
        Data.Builder dataSet = new Data.Builder();
        if (Utils.checkNewAuth(transactionType))
            dataSet.putString(PacketDBInfoWorker.CARD_TYPE, PacketDBInfoWorker.MANUAL_ENTRY);
        else
            dataSet.putString(PacketDBInfoWorker.CARD_TYPE, PacketDBInfoWorker.MSCARD);
        dataSet.putString(PacketDBInfoWorker.CARD_NUMBER, accNo.substring(0, 6));
        dataSet.putString(PacketDBInfoWorker.TRANSACTION_TYPE, transactionType);
        mRequest.setInputData(dataSet.build());
        WorkRequest build = mRequest.build();
        mWorkManager.enqueue(build);
        this.uuID = build.getId();
        mWorkManager.getWorkInfoByIdLiveData(build.getId()).observe(owner, status);
    }



    public boolean isPinRequires() {
        String[] track2 = new String(swipResult.getTrack2Data()).split("=");
        boolean isPinRequired = false;
        Logger.v("response", "track2[1].charAt(6) --" + track2.length);
        if (track2.length >= 2) {
            Logger.v("response", "track2[1].charAt(6) --" + track2[1].charAt(6));
            isPinRequired = ((track2[1].charAt(6)) + "").equalsIgnoreCase("0") || ((track2[1].charAt(6)) + "").equalsIgnoreCase("6");
        }
        return isPinRequired;
    }

    public void readMSCARD() {
        AppConfig.printerDataAvailable = false;
//        SwipResult swipRslt = k21swiper.readPlainResult(new SwiperReadModel[]{SwiperReadModel.READ_FIRST_TRACK, SwiperReadModel.READ_SECOND_TRACK, SwiperReadModel.READ_THIRD_TRACK});
//        if (null != swipRslt && swipRslt.getRsltType() == SwipResultType.SUCCESS) {
//            AppConfig.EMV.swipResult = swipRslt;
//            Logger.v("AppConfig.EMV.swipResult");
//        } else {
//            Logger.v("response", "Account -else-");
//        }
        AppConfig.EMV.consumeType = 0;
//        return swipRslt;
    }

    public void readInsert(SimpleTransferListener.MakeConnection connection, String transactionType)  {
        Logger.v("response", "readInsert -"+transactionType);
        startEMVListner(connection,transactionType,true);
    }

    private void startEMVListner(SimpleTransferListener.MakeConnection connection, String transactionType,boolean isChip) {
        Logger.ve("startEMVListner_Listner_started");
        int transType;
//        if(transactionType.equalsIgnoreCase(Constant.REFUND)){
//            transType = InnerProcessingCode.REFUND;
//        }else
//            transType = InnerProcessingCode.USING_STANDARD_PROCESSINGCODE;
        AppConfig.printerDataAvailable = false;
//        AppConfig.isChipTransaction = true;
        SimpleTransferListener simpleTransferListener = SimpleTransferListener.getInstance(context);
        Logger.v("AppInit.loadKernal -"+AppInit.loadKernal);
//        if(AppInit.loadKernal){
            simpleTransferListener.initListener(context, transactionType, connection);
//        }
//            controller = emvModule.getEmvTransController(simpleTransferListener);
//            if (controller != null) {
        boolean forceOnline = getOnlineTransaction(transactionType);
        Logger.v("ForceOnline --"+forceOnline);
        BigDecimal ammt = new BigDecimal(AppConfig.EMV.amountValue - AppConfig.EMV.amtCashBack);
        Logger.v("Amount -121-"+ammt);
//                controller.startEmv(ProcessingCode.GOODS_AND_SERVICE, transType,ammt , new BigDecimal(AppConfig.EMV.amtCashBack), forceOnline, true);
//            }

    }

    private boolean getOnlineTransaction(String transactionType) {
        return !transactionType.equalsIgnoreCase(ConstantApp.REFUND) && !transactionType.equalsIgnoreCase(ConstantApp.PURCHASE) && !transactionType.equalsIgnoreCase(ConstantApp.CASH_ADVANCE);
    }

    public void readRfCard(String transactionType,SimpleTransferListener.MakeConnection connection )  {
        Logger.v("response", "readRf--"+transactionType);
        Logger.ve("Listner started");
        startEMVListner(connection,transactionType,false);
    }
    public void checkSAF(final boolean repeat, final Observer<WorkInfo> saf_worked) {
        Logger.v("SAF 1");
        if(Utils.isInternetAvailable(context,true)) {
            mWorkManager.cancelWorkById(this.uuID);
            SAFWorker.setEventHandler(TransactionViewModel.getEventHandler());

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    //TODO execeute code
                    WorkManager mWorkManager = WorkManager.getInstance(context);
                    OneTimeWorkRequest.Builder mRequest = new OneTimeWorkRequest.Builder(SAFWorker.class);
                    Data.Builder dataSet = new Data.Builder();
                    dataSet.putBoolean(SAFWorker.SAF_COUNT_ALL, false);
                    dataSet.putBoolean(SAFWorker.SAF_DO_REPEAT, repeat);
                    mRequest.setInputData(dataSet.build());
                    OneTimeWorkRequest build = mRequest.build();
                    mWorkManager.enqueueUniqueWork("SAFWORKER", ExistingWorkPolicy.REPLACE , build);
                    mWorkManager.getWorkInfoByIdLiveData(build.getId()).observe(owner, saf_worked);
                }
            });
        }
    }

    public void checkAllSAF(final boolean repeat, final Observer<WorkInfo> saf_worked) {
        Logger.v("SAF 2");
        if(Utils.isInternetAvailable(context,true)) {
            mWorkManager.cancelWorkById(this.uuID);
            SAFWorker.setEventHandler(TransactionViewModel.getEventHandler());

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
                    OneTimeWorkRequest build = mRequest.build();
                    mWorkManager.enqueue(build);
                    mWorkManager.getWorkInfoByIdLiveData(build.getId()).observe(owner, saf_worked);
                }
            });
        }
    }

}
