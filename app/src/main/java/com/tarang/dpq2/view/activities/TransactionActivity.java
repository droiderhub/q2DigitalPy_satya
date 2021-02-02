package com.tarang.dpq2.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.tarang.dpq2.BuildConfig;
import com.tarang.dpq2.R;
import com.tarang.dpq2.base.AppInit;
import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.MapperFlow;
import com.tarang.dpq2.base.baseactivities.BaseActivity;
import com.tarang.dpq2.base.jpos_class.ConstantApp;
import com.tarang.dpq2.base.terminal_sdk.AppConfig;
import com.tarang.dpq2.base.terminal_sdk.SdkSupport;
import com.tarang.dpq2.base.terminal_sdk.utils.LightsDisplay;
import com.tarang.dpq2.base.terminal_sdk.utils.SoundPoolImpl;
import com.tarang.dpq2.base.utilities.Utils;
import com.tarang.dpq2.worker.SAFWorker;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;



public class TransactionActivity extends BaseActivity implements ConstantApp {

    String transactionType = getCurrentMenu().getMenu_tag();
    boolean isChipEnabled, isMagneticEnabled, isRFEnabled;
    private SdkSupport support;
    TextView etd_number;
    TextView txt_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.v("TransactionActivity");
        Logger.v("app_version---"+ BuildConfig.VERSION_NAME);
        Logger.v("mada_version---"+ AppInit.VERSION_6_0_5);
        SAFWorker.safTimerInitaited = false;

        setContentView(R.layout.activity_purchase);
        context = AppManager.getInstance().getApplicationContext();
        setTitle(getCurrentMenu().getMenu_name());
        etd_number = findViewById(R.id.etd_number);
        etd_number.setText(getAmount());
        findViewById(R.id.manual_entry_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                support.closeCardReader();
                MapperFlow.getInstance().moveToManualEntry(context);
            }
        });
        hideAmountView();
        checkTransactionTpe();
//        if(getIntent().getBooleanExtra("SHOW_AGAIN",false)) {
//            disableDialoge();
//        }else
//            AppConfig.waveRettryCount = 0;
    }


    private void disableDialoge() {
        new CountDownTimer(1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
//                if(AppConfig.waveRettryCount == 3){
//                    MapperFlow.getInstance().moveToLandingPage(context,true);
//                }else {
//                    showDialogePleaseWait();
//                    checkTransactionTpe();
//                    Logger.v("OnFinish Dialoge");
//                }
            }
        }.start();
    }

    private void showDialogePleaseWait() {
//        AppConfig.waveRettryCount = AppConfig.waveRettryCount+1;
//        Logger.v("waveCount --"+AppConfig.waveRettryCount);
    }

    private String getAmount() {
        Double amount = AppConfig.EMV.amountValue;
        Logger.v("AMT --" + amount);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat formatter = new DecimalFormat("#,##0.00", symbols);
        Logger.v(formatter.format(amount));
        return Utils.formatLanguageNumber(this,formatter.format(amount));
    }

    public void startListener() {
        Logger.v("startListener");
        support = new SdkSupport(this);
        support.initReader();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (support != null){
        //    support.closeCardReader();
        }
       // support.closeCardReader();
    }

    @Override
    protected void onResume() {
        transContext = this;
        showTimer = true;
        super.onResume();
        startListener();
    }

    private void checkTransactionTpe() {
        Logger.v("transaction_type--txn_act---"+transactionType);
        txt_header = (TextView) findViewById(R.id.txt_header);

        isChipEnabled = true;
        if (!transactionType.equalsIgnoreCase(ConstantApp.PURCHASE_NAQD)) {
            isMagneticEnabled = true;
        }
        if (transactionType.equalsIgnoreCase(ConstantApp.PURCHASE)
                || transactionType.equalsIgnoreCase(ConstantApp.PURCHASE_NAQD)
                || transactionType.equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION_EXTENSION)
                || transactionType.equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION_VOID)
                || transactionType.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_FULL)
                || transactionType.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_PARTIAL)
                || transactionType.equalsIgnoreCase(ConstantApp.CASH_ADVANCE)
                || transactionType.equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION)
                || transactionType.equalsIgnoreCase(ConstantApp.REFUND)) {
            isRFEnabled = true;
            showLights(true);
        }

        if (AppInit.VERSION_6_0_5) {
            Logger.v("VERSION_6_0_5");
            if (transactionType.equalsIgnoreCase(ConstantApp.PURCHASE_NAQD)) {
                isMagneticEnabled = false;
                isRFEnabled = false;
                new LightsDisplay(context).hideSingleLight();
                new LightsDisplay(context).hideTwoLight();
                new LightsDisplay(context).hideFourLights();
            }else if(transactionType.equalsIgnoreCase(ConstantApp.CASH_ADVANCE)){
                isRFEnabled = false;
                new LightsDisplay(context).hideSingleLight();
                new LightsDisplay(context).hideTwoLight();
                new LightsDisplay(context).hideFourLights();
            }
        }

        if (AppInit.VERSION_6_0_5) {
            if (transactionType.equalsIgnoreCase(ConstantApp.PURCHASE_NAQD)) {
                isMagneticEnabled = false;
                isRFEnabled = false;
                new LightsDisplay(context).hideSingleLight();
                new LightsDisplay(context).hideTwoLight();
                new LightsDisplay(context).hideFourLights();
            }else if(transactionType.equalsIgnoreCase(ConstantApp.CASH_ADVANCE)){
                isRFEnabled = false;
                new LightsDisplay(context).hideSingleLight();
                new LightsDisplay(context).hideTwoLight();
                new LightsDisplay(context).hideFourLights();
            }
        }


        String msg = "";

        if (isChipEnabled)
            msg = context.getString(R.string.ic_card);
        if (isMagneticEnabled)
            msg = ((msg.trim().length() != 0) ? msg + " / " : "") + context.getString(R.string.magstrip_card);
        if (isRFEnabled && (!Utils.checkArabicLanguage(this)))
            msg = ((msg.trim().length() != 0) ? msg + " / " : "") + context.getString(R.string.contact_less_card);

        txt_header.setText(msg);
    }

    private void showLights(boolean b) {
        if (b) {
            new LightsDisplay(context).showBlueLight();
        } else {
            /*SoundPoolImpl spi= SoundPoolImpl.getInstance();
            spi.initLoad(context);
            spi.playTwice();*/
            new LightsDisplay(context).showTwoLights();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.edit:
                Intent intent = new Intent(this, ManualCardActivity.class);
                intent.putExtra(TITLE, ConstantApp.PURCHASE);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean checkCondition() {
        int responce = AppConfig.EMV.consumeType;
        new LightsDisplay(context).hideSingleLight();
        new LightsDisplay(context).hideTwoLight();
        new LightsDisplay(context).hideFourLights();
        switch (responce) {
            case 1:
                if (!isChipEnabled)
                    showAlert();
                return isChipEnabled;
            case 2:
                if (!isRFEnabled)
                    showAlert();
                else
                    showLights(false);
                return isRFEnabled;
            case 0:
                if (!isMagneticEnabled)
                    showAlert();
                return isMagneticEnabled;
        }
        return false;
    }

    private void showAlert() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Utils.showListnerMsg(TransactionActivity.this);
            }
        });
    }

    public void timeOutFinish() {
        Toast.makeText(context, getString(R.string.time_out), Toast.LENGTH_SHORT).show();
        Utils.dismissDialoge();
        support.closeCardReader();
        new LightsDisplay(context).hideSingleLight();
        new LightsDisplay(context).hideTwoLight();
        new LightsDisplay(context).hideFourLights();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        freeMemory();
    }
}
