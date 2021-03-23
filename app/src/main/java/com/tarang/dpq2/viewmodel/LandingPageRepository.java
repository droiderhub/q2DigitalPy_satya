package com.tarang.dpq2.viewmodel;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.utilities.Utils;
import com.tarang.dpq2.worker.LoadKeyWorker;
import com.tarang.dpq2.worker.PacketDBInfoWorker;
import com.tarang.dpq2.worker.SAFWorker;
import com.tarang.dpq2.worker.SocketConnectionWorker;
import com.tarang.dpq2.worker.TmsLoadWorker;

import java.util.UUID;

public class LandingPageRepository {

    private final Context context;
    private final LifecycleOwner owner;
    private final WorkManager mWorkManager;
    public UUID uuID;

    public LandingPageRepository(Context context, LifecycleOwner owner) {
        this.context = context;
        this.owner = owner;
        mWorkManager = WorkManager.getInstance(context);
    }

    public void checkSAF(boolean repeat, Observer<WorkInfo> saf_worked) {
        if(Utils.isInternetAvailable1(context)) {
            mWorkManager.cancelWorkById(uuID);
            mWorkManager.cancelUniqueWork("SAFWORKER");
            SAFWorker.setEventHandler(LandingPageViewModel.getEventHandler());
            OneTimeWorkRequest.Builder mRequest = new OneTimeWorkRequest.Builder(SAFWorker.class);
            Data.Builder dataSet = new Data.Builder();
            dataSet.putBoolean(SAFWorker.SAF_COUNT_ALL, false);
            dataSet.putBoolean(SAFWorker.SAF_DO_REPEAT, repeat);
            mRequest.setInputData(dataSet.build());
            OneTimeWorkRequest build = mRequest.build();
            this.uuID = build.getId();
//            mWorkManager.enqueue(build);
            mWorkManager.enqueueUniqueWork("SAFWORKER", ExistingWorkPolicy.REPLACE , build);
            mWorkManager.getWorkInfoByIdLiveData(build.getId()).observe(owner, saf_worked);
        }
    }

    public void cancelWorkmanager(){
        mWorkManager.cancelWorkById(uuID);
        mWorkManager.cancelUniqueWork("SAFWORKER");
        mWorkManager.cancelAllWorkByTag("SAFWORKER");
        mWorkManager.cancelAllWork();
    }


    public void makeSocketConnection(Observer<WorkInfo> status){
        Logger.v("makeSocketConnection_landingpageRepo");
        SocketConnectionWorker.setEventHandler(MenuViewModel.getEventHandler(),1);
        OneTimeWorkRequest mRequest = new OneTimeWorkRequest.Builder(SocketConnectionWorker.class).build();
        mWorkManager.enqueue(mRequest);
        Logger.v("UUID --"+mRequest.getId());
        mWorkManager.getWorkInfoByIdLiveData(mRequest.getId()).observe(owner,status);
    }

    public void fetchDataFromDatabase(String data,Observer<WorkInfo> status){
        WorkRequest.Builder mRequest = new OneTimeWorkRequest.Builder(PacketDBInfoWorker.class);
        Data.Builder dataSet = new Data.Builder();
        dataSet.putString(PacketDBInfoWorker.TRANSACTION_TYPE,data);
        mRequest.setInputData(dataSet.build());
        WorkRequest build = mRequest.build();
        mWorkManager.enqueue(build);
        mWorkManager.getWorkInfoByIdLiveData(build.getId()).observe(owner,status);
    }

    public void loadKeys(Observer<WorkInfo> status) {
        WorkManager mWorkManager = WorkManager.getInstance(context);
        OneTimeWorkRequest.Builder mRequest = new OneTimeWorkRequest.Builder(LoadKeyWorker.class);
        WorkRequest build = mRequest.build();
        mWorkManager.enqueue(build);
        mWorkManager.getWorkInfoByIdLiveData(build.getId()).observe(owner,status );
    }

    public void loadTms(Observer<WorkInfo> status) {
        WorkManager mWorkManager = WorkManager.getInstance(context);
        OneTimeWorkRequest.Builder mRequest = new OneTimeWorkRequest.Builder(TmsLoadWorker.class);
        WorkRequest build = mRequest.build();
        mWorkManager.enqueue(build);
        mWorkManager.getWorkInfoByIdLiveData(build.getId()).observe(owner,status );
    }

}
