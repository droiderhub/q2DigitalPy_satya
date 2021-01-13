package com.tarang.dpq2.view.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.cloudpos.DeviceException;
import com.cloudpos.printer.PrinterDevice;

import com.tarang.dpq2.R;
import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.MapperFlow;
import com.tarang.dpq2.base.baseactivities.BaseActivity;
import com.tarang.dpq2.base.jpos_class.ConstantApp;
import com.tarang.dpq2.base.room_database.db.AppDatabase;
import com.tarang.dpq2.base.room_database.db.entity.TransactionModelEntity;
import com.tarang.dpq2.base.terminal_sdk.device.SDKDevice;
import com.tarang.dpq2.base.terminal_sdk.utils.PrinterReceipt;
import com.tarang.dpq2.base.utilities.Utils;
import com.tarang.dpq2.model.DeviceSpecificModel;
import com.tarang.dpq2.model.MenuModel;
import com.tarang.dpq2.view.adapter.SubMenuRecyclerAdapter;
import com.tarang.dpq2.viewmodel.BaseViewModel;
import com.tarang.dpq2.worker.PrinterWorker;
import com.tarang.dpq2.worker.SAFCountWorker;


import java.util.List;

import static com.tarang.dpq2.base.jpos_class.ConstantApp.MenuList;

public class SubMenuActivity extends BaseActivity implements SubMenuRecyclerAdapter.Printer {

    RecyclerView rcv_list_menu;
    private PrinterDevice devicePrinter;
    private MenuModel.MenuItem myMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        devicePrinter = SDKDevice.getInstance(context).getPrinter();
        setContentView(R.layout.activity_transaction_menu);
        PrinterWorker.DO_SAF_NOW= false;
        // Crashlytics.getInstance().crash(); // Force a crash
        setTitle();
        MenuModel.MenuItemHolder lister = (MenuModel.MenuItemHolder) getIntent().getSerializableExtra(MenuList);
        rcv_list_menu = findViewById(R.id.rcv_list_menu);
        if (lister.isGrid())
            rcv_list_menu.setLayoutManager(new GridLayoutManager(this, 2));
        else
            rcv_list_menu.setLayoutManager(new LinearLayoutManager(this));
        rcv_list_menu.setAdapter(new SubMenuRecyclerAdapter(this, lister.getMenu_data(), this, this));

    }

    @Override
    protected void onResume() {
        showTimer = false;
        super.onResume();
    }

    public void printReceipt(int stat, Observer<WorkInfo> status) {
        WorkManager mWorkManager = WorkManager.getInstance(context);
        WorkRequest.Builder mRequest = new OneTimeWorkRequest.Builder(PrinterWorker.class);
        Data.Builder dataSet = new Data.Builder();
        dataSet.putBoolean(PrinterWorker.KEY_VALUE_PRINT, true);
        dataSet.putInt(PrinterWorker.KEY_VALUE_DATA, stat);
        mRequest.setInputData(dataSet.build());
        WorkRequest build = mRequest.build();
        mWorkManager.enqueue(build);
        Logger.v("UUID -1-" + build.getId());
        mWorkManager.getWorkInfoByIdLiveData(build.getId()).observe(this, status);
    }

    @Override
    public void doPrint(final int i) {
        Utils.alertDialogShow(this, getString(R.string.printing));
        printReceipt(i, new Observer<WorkInfo>() {
            @Override
            public void onChanged(final WorkInfo workInfo) {
                if (workInfo.getState() == WorkInfo.State.SUCCEEDED || workInfo.getState() == WorkInfo.State.FAILED)
                    doPrintReceipt(new BaseViewModel.PrintComplete() {
                        @Override
                        public void onFinish() {
                            if (checkPaper(workInfo, i)) {

                                Utils.alertDialogShow(SubMenuActivity.this, getString(R.string.complete), new Utils.DialogeClick() {
                                    @Override
                                    public void onClick() {
                                        Utils.dismissDialoge();
                                    }
                                });

                              /*  new Handler(Looper.getMainLooper()) {
                                    @Override
                                    public void handleMessage(Message message) {
                                        // This is where you do your work in the UI thread.
                                        // Your worker tells you in the message what to do.
                                        Utils.alertDialogShow(SubMenuActivity.this, getString(R.string.complete), new Utils.DialogeClick() {
                                            @Override
                                            public void onClick() {
                                                Utils.dismissDialoge();
                                            }
                                        });
                                    }
                                };*/

                            }
                        }
                    });
            }
        });
    }

    private void doPrintReceipt(BaseViewModel.PrintComplete complete) {
        boolean status = false;
        Logger.v("Size --" + PrinterWorker.modelList.size());
        try {
            status = PrinterReceipt.printKeyValue(PrinterWorker.modelList, devicePrinter, context, complete);
        } catch (DeviceException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void normalFlow(MenuModel.MenuItem i) {
        if (checkTransaction(i) && AppManager.getInstance().getTemprovaryOutService()) {
            outOfServiceDialoe();
        } else if (checkOutOfService(i)) {
            Logger.v("IF Else");
        } else {
            Logger.v("Else");
            MapperFlow.getInstance().moveToMenuClick(context, i);
        }
    }

    private boolean checkTransaction(MenuModel.MenuItem menuItem) {
        return (menuItem.getMenu_tag().equalsIgnoreCase(ConstantApp.PURCHASE) || menuItem.getMenu_tag().equalsIgnoreCase(ConstantApp.PURCHASE_NAQD)
                || menuItem.getMenu_tag().equalsIgnoreCase(ConstantApp.PURCHASE_REVERSAL) || menuItem.getMenu_tag().equalsIgnoreCase(ConstantApp.REFUND)
                || menuItem.getMenu_tag().equalsIgnoreCase(ConstantApp.CASH_ADVANCE) || menuItem.getMenu_tag().equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION)
                || menuItem.getMenu_tag().equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION_VOID) || menuItem.getMenu_tag().equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION_EXTENSION)
                || menuItem.getMenu_tag().equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE) || menuItem.getMenu_tag().equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_MANUAL)
                || menuItem.getMenu_tag().equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_FULL) || menuItem.getMenu_tag().equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_PARTIAL));
    }

    private boolean checkOutOfService(final MenuModel.MenuItem menuItem) {
        if (menuItem.getMenu_tag().equalsIgnoreCase(ConstantApp.PURCHASE) || menuItem.getMenu_tag().equalsIgnoreCase(ConstantApp.PURCHASE_NAQD)
                || menuItem.getMenu_tag().equalsIgnoreCase(ConstantApp.PURCHASE_REVERSAL) || menuItem.getMenu_tag().equalsIgnoreCase(ConstantApp.REFUND)
                || menuItem.getMenu_tag().equalsIgnoreCase(ConstantApp.CASH_ADVANCE) || menuItem.getMenu_tag().equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION)
                || menuItem.getMenu_tag().equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION_VOID) || menuItem.getMenu_tag().equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION_EXTENSION)
                || menuItem.getMenu_tag().equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE) || menuItem.getMenu_tag().equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_MANUAL)
                || menuItem.getMenu_tag().equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_FULL) || menuItem.getMenu_tag().equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_PARTIAL)) {
            if (!Utils.checkPrinterPaper(this)) {
               /* WorkManager mWorkManager = WorkManager.getInstance(this);
                OneTimeWorkRequest.Builder mRequest = new OneTimeWorkRequest.Builder(SAFCountWorker.class);
                Data.Builder dataSet = new Data.Builder();
                mRequest.setInputData(dataSet.build());
                OneTimeWorkRequest build = mRequest.build();
                mWorkManager.enqueueUniqueWork("SAFWORKER", ExistingWorkPolicy.REPLACE, build);
                Logger.v("UUID -1-" + build.getId());
                mWorkManager.getWorkInfoByIdLiveData(build.getId()).observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo != null) {
                            Logger.v("STATE --" + workInfo.getState());
                            if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                                MapperFlow.getInstance().moveToMenuClick(context, menuItem);
                            } else if (workInfo.getState() == WorkInfo.State.FAILED) {
                                outOfServiceDialoe();
                            }
                        }
                    }
                });*/
                myMenu = menuItem;
                new AsyncTaskExample().execute();
            }
            return true;
        } else
            return false;
    }

    private void outOfServiceDialoe() {
        PrinterWorker.DO_SAF_NOW = true;
        Utils.alertDialogShow(context, context.getString(R.string.temprovery_out_service), new Utils.DialogeClick() {
            @Override
            public void onClick() {
                MapperFlow.getInstance().moveToLandingPage(context, true, 10);
            }
        });
    }


    private boolean checkPaper(WorkInfo workInfo, final int print) {
        boolean printStatus = workInfo.getOutputData().getBoolean(PrinterWorker.STATUS, true);
        if (printStatus)
            return true;
        else if (!outOfPaper(print)) {
//            doPrint(print);
        }
        return false;
    }

    private boolean outOfPaper(final int print) {
        return Utils.checkPrinterPaper(context, new Utils.DialogeClick() {
            @Override
            public void onClick() {
                if (!outOfPaper(print))
                    doPrint(print);
            }
        });
    }
    public class AsyncTaskExample extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            Logger.v("SAF Count check");
            AppDatabase database = AppDatabase.getInstance(context.getApplicationContext());
            List<TransactionModelEntity> safModelEntities = database.getSAFDao().getAll();
            DeviceSpecificModel deviceSpecificModel1 = AppManager.getInstance().getDeviceSpecificModel();
            if(safModelEntities.size() == 0){
                AppManager.getInstance().setTemprovaryOutService(false);
                return true;
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
                        return false;
                    }
                }
                if(maxCount != null && maxCount.trim().length() != 0){
                    int maxCont = Integer.parseInt(maxCount);
                    Logger.v("MAXX Count -"+maxCont);
                    if(maxCont != 0 && maxCont <= count){
                        return false;
                    }
                }
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            Logger.v("aBoolean -"+aBoolean);
            if (aBoolean) {
                MapperFlow.getInstance().moveToMenuClick(context, myMenu);
            } else {
                outOfServiceDialoe();
            }
        }
    }

}
