package com.tarang.dpq2.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.tarang.dpq2.base.room_database.db.AppDatabase;
import com.tarang.dpq2.tms.TmsResponseParser;

// ************ Hardcoded TMS loader for Testing
public class TmsLoadWorker extends Worker {
    private final AppDatabase database;
//    private final EmvModule emvModule;
    TmsResponseParser tms;

    public TmsLoadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        database = AppDatabase.getInstance(context.getApplicationContext());
//        emvModule = SDKDevice.getInstance(context).getEmvModuleType();
//        emvModule.initEmvModule(context);
        tms = new TmsResponseParser(database);
    }

    @NonNull
    @Override
    public Result doWork() {
        tms.loadTMS();
        return Result.success();
    }
}
