package com.tarang.dpq2.view.activities;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.tarang.dpq2.R;
import com.tarang.dpq2.base.AppInit;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.baseactivities.BaseActivity;
import com.tarang.dpq2.base.jpos_class.ConstantApp;
import com.tarang.dpq2.base.utilities.Utils;
import com.tarang.dpq2.viewmodel.MenuViewModel;
import com.tarang.dpq2.worker.PrinterWorker;

public class AdminMenuActivity extends BaseActivity implements ConstantApp {

    private MenuViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_initialisation);
        viewModel = ViewModelProviders.of(this).get(MenuViewModel.class);
        viewModel.init(this, this);

        viewModel.getConnectionStatus().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                Logger.v("getConnectionStatusadmin -" + aBoolean);
                if (aBoolean != null && aBoolean) {
                    viewModel.makeSocketConnection();
                }

            }
        });

        viewModel.getShowAlert().observe(this, viewModel.observable);

        viewModel.loadKeys().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean != null && aBoolean)
                    viewModel.loadKeysIntoTerminal();
            }
        });

        viewModel.startTimer().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean != null && aBoolean)
                    viewModel.startCountDownTimer();
            }
        });

        viewModel.getPrintStatus().observe(this, viewModel.printObservable);

        setTextView();
    }

    @Override
    protected void onResume() {
        if (getCurrentMenu().getMenu_tag().equalsIgnoreCase(REGISTRATION) || getCurrentMenu().getMenu_tag().equalsIgnoreCase(PARTIAL_DOWNLOAD) || getCurrentMenu().getMenu_tag().equalsIgnoreCase(FULL_DOWNLOAD))
            showTimer = false;
        else
            showTimer = true;
        super.onResume();
    }

    private void setTextView() {
        setTitle(getCurrentMenu().getMenu_name());
        String tag = getCurrentMenu().getMenu_tag();
        //  Toast.makeText(context, tag, Toast.LENGTH_SHORT).show();
        switch (tag) {
            case REGISTRATION:
//                byte[] ksn = SDKDevice.getInstance(context).getK21Pininput().getDukptKsn(AppConfig.Pin.DUKPT_DES_INDEX);
//                if(ksn == null || ksn.length == 0){
//                    Utils.alertDialogShow(context, "Please do Key Injection", true);
//                    return;
//                }
//                viewModel.createTMSPacket("3020000");
                viewModel.doSAFOnly = false;
                viewModel.isDownload = 2;
                viewModel.createReconsilation1(false);
                break;
            case PARTIAL_DOWNLOAD:
                resetLastPacket(false);
                viewModel.doSAFOnly = false;
                viewModel.isDownload = 0;
                viewModel.createReconsilation1(false);
//                viewModel.createRegistration();
                break;
            case FULL_DOWNLOAD:
                resetLastPacket(true);
                viewModel.doSAFOnly = false;
                viewModel.isDownload = 0;
                viewModel.createReconsilation1(false);
//                viewModel.createRegistration();
                break;
            case KEY_INJECTION:
                viewModel.loadOnlyKeysIntoTerminal();
                break;
            case FORMAT_FILESYS:
                viewModel.formatFileSys();

        }
//        if (tag.equalsIgnoreCase(RECONCILE_SETUP)) {
//            setTitle("Reconcile setup");
//            reconciliation_setup_layout.setVisibility(View.VISIBLE);
//        } else if (tag.equalsIgnoreCase(DUPLICATE)) {
//            setTitle("DUPLICATE");
//            merchantmenu_layout.setVisibility(View.VISIBLE);
//            merchantmenu_text.setText(getCurrentMenu().getMenu_name());
//        } else if (tag.equalsIgnoreCase(RECONCILIATION)) {
//            setTitle("RECONCILIATION");
//            merchantmenu_layout.setVisibility(View.VISIBLE);
//            merchantmenu_text.setText(getCurrentMenu().getMenu_name());
//        } else if (tag.equalsIgnoreCase(SANPSHOT_TOTAL)) {
//            setTitle("SANPSHOT TOTAL");
//            merchantmenu_layout.setVisibility(View.VISIBLE);
//            merchantmenu_text.setText(getCurrentMenu().getMenu_name());
//        } else if (tag.equalsIgnoreCase(RUNNING_TOTAL)) {
//            setTitle("RUNNING TOTAL");
//            merchantmenu_layout.setVisibility(View.VISIBLE);
//            merchantmenu_text.setText(getCurrentMenu().getMenu_name());
//        } else if (tag.equalsIgnoreCase(HISTORY_VIEW)) {
//            setTitle("HISTORY VIEW");
//        } else if (tag.equalsIgnoreCase(LAST_EMV)) {
//            setTitle("LAST EMV");
//        } else if (tag.equalsIgnoreCase(CHANGE_PASSWORD)) {
//            setTitle("CHANGE PASSWORD");
//        } else if (tag.equalsIgnoreCase(TERMINFO_MENU)) {
//            setTitle("TERMINFO MENU");
//        } else if (tag.equalsIgnoreCase(SIM_NUMBER)) {
//            setTitle("SIM NUMBER");
//        } else if (tag.equalsIgnoreCase(DE_SAF_ALL_FILE)) {
//            setTitle("DE-SAF ALL FILE");
//        } else if (tag.equalsIgnoreCase(SWITCH_CONNECTION)) {
//            setTitle("SWITCH CONNECTION");
//        } else if (tag.equalsIgnoreCase(SET_GPS_LOCATION)) {
//            setTitle("SET GPS LOCATION");
//        } else if (tag.equalsIgnoreCase(SELECT_LANGUAGE)) {
//            setTitle("SELECT LANGUAGE");
//        }
    }

    private void resetLastPacket(boolean isFullDownload) {
        if(AppInit.lastPacket.equalsIgnoreCase(AppInit.full_download) || AppInit.lastPacket.equalsIgnoreCase(AppInit.partial_download)){
            AppInit.lastPacket = (isFullDownload)?AppInit.full_download:AppInit.partial_download;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utils.hideDialoge1();
        PrinterWorker.RECON_PRINTED = false;
        PrinterWorker.RECON_PRINTED_DUB = false;
        Utils.setNullDialoge();

    }
}
