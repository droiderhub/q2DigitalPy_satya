package com.tarang.dpq2.view.activities;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.cloudpos.Device;
import com.cloudpos.printer.PrinterDevice;
import com.tarang.dpq2.BuildConfig;
import com.tarang.dpq2.R;
import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.MapperFlow;
import com.tarang.dpq2.base.baseactivities.BaseActivity;
import com.tarang.dpq2.base.jpos_class.ConstantApp;
import com.tarang.dpq2.base.terminal_sdk.device.SDKDevice;
import com.tarang.dpq2.base.terminal_sdk.utils.PrinterReceipt;
import com.tarang.dpq2.base.utilities.Utils;
import com.tarang.dpq2.model.MenuModel;
import com.tarang.dpq2.model.RetailerDataModel;
import com.tarang.dpq2.view.adapter.DisplayMenuDataRecyclerAdapter;
import com.tarang.dpq2.viewmodel.BaseViewModel;
import com.tarang.dpq2.worker.PacketDBInfoWorker;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static com.tarang.dpq2.base.jpos_class.ConstantApp.MenuList;

public class DisplaySubMenuData extends BaseActivity {

    private RecyclerView rcv_list_menu;
    private PrinterDevice devicePrinter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_set_parameter);
        Logger.v("getCurrentMenu().getMenu_tag() -"+getCurrentMenu().getMenu_tag());
        rcv_list_menu = findViewById(R.id.rcv_list_menu);
        rcv_list_menu.setLayoutManager(new LinearLayoutManager(this));
        devicePrinter = SDKDevice.getInstance(context).getPrinter();
        setTitle();
        setUpData();
    }

    @Override
    protected void onResume() {
        showTimer = false;
        super.onResume();
    }

    private void setUpData() {
        switch (getCurrentMenu().getMenu_tag()){
            case ConstantApp.TMS_RETAILER_DATA : loadRetailerData();
            break;
            case ConstantApp.TMS_AID_LIST: loadAidList();
            break;
            case ConstantApp.LAST_EMV: loadEmVTags();
            break;
            default: loadMenu(); break;
        }
    }

    private void loadEmVTags() {
        fetchDataBase(ConstantApp.LAST_EMV, new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                if(workInfo.getState() == WorkInfo.State.SUCCEEDED){
                    List<MenuModel.DisplayValue> display = new ArrayList<>();
                    HashMap<String, String> tag55 = Utils.getParsedTag55(workInfo.getOutputData().getString(PacketDBInfoWorker.DE_124));
                    Set<String> tags = tag55.keySet();
                    for (String tag : tags) {
                        display.add(new MenuModel.DisplayValue(tag, tag55.get(tag)));
                    }
                    rcv_list_menu.setAdapter(new DisplayMenuDataRecyclerAdapter(DisplaySubMenuData.this,display));
                }else if(workInfo.getState() == WorkInfo.State.FAILED){
                    Utils.alertDialogShow(context,getResources().getString(R.string.no_data),true);
                }

            }
        });
    }

    private void loadMenu() {
        if (getIntent().hasExtra(MenuList)) {
            Logger.v("setUpData MenuList");
            MenuModel.MenuItemHolder lister = (MenuModel.MenuItemHolder) getIntent().getSerializableExtra(MenuList);
            rcv_list_menu.setAdapter(new DisplayMenuDataRecyclerAdapter(this, lister.getMenu_data(), true));
        }
    }

    private void loadAidList() {
        List<String> aidList1 = AppManager.getInstance().getAidList();
        List<MenuModel.DisplayValue> display = new ArrayList<>();
        for(int i=0;i<aidList1.size();i++) {
            if(aidList1.get(i).trim().length() != 0) {
                display.add(new MenuModel.DisplayValue("AID "+display.size()+1, aidList1.get(i)));
            }
        }
        Logger.v("display --"+display.size());
        rcv_list_menu.setAdapter(new DisplayMenuDataRecyclerAdapter(this,display));
    }

    private void loadRetailerData() {
        Logger.v("setUpData TMS_RETAILER_DATA");
        RetailerDataModel retailerDataModel = AppManager.getInstance().getRetailerDataModel();
        List<MenuModel.DisplayValue> display = new ArrayList<>();
        display.add(new MenuModel.DisplayValue("Reconciliation Time",retailerDataModel.getReconcillationTime()));
        display.add(new MenuModel.DisplayValue("Retailer Name Arabic",new String(retailerDataModel.getRetailerNameInArabic().getBytes(Charset.forName("Cp1252")), Charset.forName("ISO-8859-6")).trim())); //TMS
        display.add(new MenuModel.DisplayValue("Retailer Name English",retailerDataModel.getRetailerNameEnglish()));
        display.add(new MenuModel.DisplayValue("Retailer Number",AppManager.getInstance().getString(ConstantApp.SPRM_PHONE_NUMBER)));
        display.add(new MenuModel.DisplayValue("Retailer Address",retailerDataModel.getRetailerAddress1English()));
        Logger.v("display --"+display.size());
        rcv_list_menu.setAdapter(new DisplayMenuDataRecyclerAdapter(this,display));
    }

    private void loadTerminalInfo(){
        List<MenuModel.DisplayValue> display = new ArrayList<>();
//        display.add(new MenuModel.DisplayValue("Firmare Details",android.os.Build.VERSION.));
//        display.add(new MenuModel.DisplayValue("Battery Status",)); //TMS
        display.add(new MenuModel.DisplayValue("Application Version", BuildConfig.VERSION_NAME));
        display.add(new MenuModel.DisplayValue("Application Version Code", BuildConfig.VERSION_CODE+""));
        Logger.v("display --"+display.size());
        rcv_list_menu.setAdapter(new DisplayMenuDataRecyclerAdapter(this,display));
    }

    public void fetchDataBase(String data, Observer<WorkInfo> status){
        WorkManager mWorkManager = WorkManager.getInstance(context);
        WorkRequest.Builder mRequest = new OneTimeWorkRequest.Builder(PacketDBInfoWorker.class);
        Data.Builder dataSet = new Data.Builder();
        dataSet.putString(PacketDBInfoWorker.TRANSACTION_TYPE, data);
        mRequest.setInputData(dataSet.build());
        WorkRequest build = mRequest.build();
        mWorkManager.enqueue(build);
        mWorkManager.getWorkInfoByIdLiveData(build.getId()).observe(this, status);
    }

    private Dialog alertDialoge;
    @Override
    public void onBackPressed() {
        if (getCurrentMenu().getMenu_tag().equalsIgnoreCase(ConstantApp.SET_PARAMATERS)) {
            alertDialoge = Utils.alertYesDialogShow1(this, getString(R.string.save_set_parameters), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Logger.v("onBACKK");
                    if (alertDialoge != null) {
                        alertDialoge.dismiss();
                        alertDialoge.cancel();
                    }
                    moveToPrint();
                }
            }, new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    alertDialoge.dismiss();
                    alertDialoge.cancel();
                    ((BaseActivity)context).finish();
                }
            });
        } else {
            super.onBackPressed();
        }
    }

    private void moveToPrint() {
        if(devicePrinter != null)
            PrinterReceipt.printSetParameters(devicePrinter, context, Build.SERIAL, new BaseViewModel.PrintComplete() {
                @Override
                public void onFinish() {
            //        MapperFlow.getInstance().moveToLandingPage(DisplaySubMenuData.this,true,123);
                    MapperFlow.getInstance().moveToLandingActivity(DisplaySubMenuData.this);
                }
            });
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        MapperFlow.getInstance().moveToLandingPage(DisplaySubMenuData.this,true,123);
//                    }
//                },3000);
//            }
//        });


    }

}
