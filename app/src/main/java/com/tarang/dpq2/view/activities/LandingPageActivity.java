package com.tarang.dpq2.view.activities;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import com.bumptech.glide.Glide;
import com.tarang.dpq2.BuildConfig;
import com.tarang.dpq2.R;
import com.tarang.dpq2.base.AppInit;
import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.AutoReconsilation;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.MapperFlow;
import com.tarang.dpq2.base.baseactivities.BaseActivity;
import com.tarang.dpq2.base.jpos_class.ConstantAppValue;
import com.tarang.dpq2.base.terminal_sdk.AppConfig;
import com.tarang.dpq2.base.terminal_sdk.SdkSupport;
import com.tarang.dpq2.base.terminal_sdk.device.SDKDevice;
import com.tarang.dpq2.base.terminal_sdk.utils.LightsDisplay;
import com.tarang.dpq2.base.utilities.CountDownResponseTimer;
import com.tarang.dpq2.base.utilities.CurrencyEditText;
import com.tarang.dpq2.base.utilities.Utils;
import com.tarang.dpq2.base.utilities.multilangutils.LocalizationActivityDelegate;
import com.tarang.dpq2.model.DeviceSpecificModel;
import com.tarang.dpq2.viewmodel.LandingPageViewModel;
import com.tarang.dpq2.worker.LoadKeyWorker;
import com.tarang.dpq2.worker.SAFWorker;

import java.util.List;
import java.util.Locale;


public class LandingPageActivity extends BaseActivity implements View.OnClickListener , TextView.OnEditorActionListener, ActionMode.Callback,View.OnFocusChangeListener {

    private LandingPageViewModel viewModel;
    private AutoReconsilation reconsilation;
    private Dialog alertDialoge;
    CurrencyEditText edtAmt;
    TextView txt_header_tiid;
    FrameLayout frm_amount;
    LinearLayout ll_data_visib;
    LocalizationActivityDelegate localizationDelegate = new LocalizationActivityDelegate(this);
    private boolean clickEnter = false;
    private SDKDevice sdkDevice;
    private boolean resetFlag;
    private boolean shownOnce = false;
    private SdkSupport support;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_landing_page);

        Logger.v("initNew- " + 101);
        Logger.v("app_version---"+ BuildConfig.VERSION_NAME);
        Logger.v("mada_version---"+ AppInit.VERSION_6_0_5);
        support = new SdkSupport(LandingPageActivity.this);
        Logger.v("support.closeLister --" + support.closeLister);
        /*new LightsDisplay(context).hideSingleLight();
        new LightsDisplay(context).hideTwoLight();
        new LightsDisplay(context).hideFourLights();*/
        initcontrols();
        initViews();
        if (localizationDelegate.getLanguage(context).equals(new Locale("en"))) {
            localizationDelegate.setLanguage(context, "en", "US");
        }
        DeviceSpecificModel deviceSpecificModel1 = AppManager.getInstance().getDeviceSpecificModel();

        Logger.v("De72 device: --" + deviceSpecificModel1.toString());

        viewModel = ViewModelProviders.of(this).get(LandingPageViewModel.class);
        viewModel.init(this, this);

        viewModel.getSAFConnection().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                Logger.v("getConnectionStatuslanding -" + aBoolean);
                if (aBoolean != null && aBoolean) {
                    viewModel.checkSAF(false);
                }

            }
        });

        viewModel.startTimer().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean != null && aBoolean)
                    viewModel.startCountDownTimer();
            }
        });
        findViewById(R.id.ideal_screen).setOnClickListener(this);


        viewModel.changeIdealScreen().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Logger.v("changeIdealScreen()");
                hideKeyboard(LandingPageActivity.this);
                if (aBoolean != null && aBoolean) {
//                 findViewById(R.id.parent_root_view).setVisibility(View.GONE);
                    support.closeCardReader();
                    findViewById(R.id.ideal_screen).setVisibility(View.VISIBLE);
                    loadImage();
                } else {
                    resetViews();
                }
            }
        });

        reconsilation = new AutoReconsilation(context);
        getMemory();
    }
    private void loadImage() {
        List<String> aidList = AppManager.getInstance().getAidListSplash();
        Logger.v("aidList -" + aidList.toString());
        Logger.v("aidList -" + aidList.size());
        boolean ststus = aidList.contains(ConstantAppValue.A0000000031010);
        Logger.v("ststus -" + ststus);
        ImageView image = (ImageView) findViewById(R.id.img_ideal);
        if ((aidList.contains(ConstantAppValue.A0000000031010)) //Visa
                && aidList.contains(ConstantAppValue.A0000000041010) // Master
                && aidList.contains(ConstantAppValue.A00000002501) //Am Exp
                && (aidList.contains(ConstantAppValue.A000000333010101))) { // UionPay
            Glide.with(this).load(R.drawable.screen5).into(image);
        } else if ((aidList.contains(ConstantAppValue.A0000000031010)) //Visa
                && aidList.contains(ConstantAppValue.A0000000041010) // Master
                && aidList.contains(ConstantAppValue.A00000002501) //Am Exp
        ) { // UionPay
            Glide.with(this).load(R.drawable.screen4).into(image);
        } else if ((aidList.contains(ConstantAppValue.A0000000031010)) //Visa
                && aidList.contains(ConstantAppValue.A0000000041010) // Master
                && (aidList.contains(ConstantAppValue.A000000333010101))) { // UionPay
            Glide.with(this).load(R.drawable.screen3).into(image);
        } else if ((aidList.contains(ConstantAppValue.A0000000031010)) //Visa
                && aidList.contains(ConstantAppValue.A0000000041010) // Master
        ) { // UionPay
            Glide.with(this).load(R.drawable.screen2).into(image);
        } else {
            Glide.with(this).load(R.drawable.screen1).into(image);
        }
    }
    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    private void resetViews() {
//        findViewById(R.id.parent_root_view).setVisibility(View.VISIBLE);
        connDevice(false);
        findViewById(R.id.ideal_screen).setVisibility(View.GONE);
        viewModel.startIdealTimer();
    }

    private void getMemory() {

        Logger.v("runmemory_max----" + Runtime.getRuntime().maxMemory());
        Logger.v("runmemory_free----" + Runtime.getRuntime().freeMemory());
        Logger.v("runmemory_total----" + Runtime.getRuntime().totalMemory());
    }

    public void keyBoard() {
        Logger.v("keyboard_executed");
        final View constraintLayout = findViewById(R.id.my_root_);
        constraintLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                constraintLayout.getWindowVisibleDisplayFrame(r);
                int screenHeight = constraintLayout.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;
                if (keypadHeight > screenHeight * 0.15) {
                    Logger.v("Scrren Pad shown");
                    try {
                        edtAmt.setSelection(edtAmt.getText().toString().trim().length());
                        viewModel.cancelIdealTimer();
                        CountDownResponseTimer.cancelTimerLanding(22);
                    } catch (Exception e) {
                    }
                } else {
                    Logger.v("Scrren Pad not shown -" + clickEnter);
                    processwithFlow();
                }
            }
        });
    }

    private void processwithFlow() {
        if(!shownOnce) {
            if (!clickEnter) {
                viewModel.startSAFTimer();
            }else {
                viewModel.cancelIdealTimer();
                CountDownResponseTimer.cancelTimerLanding(221);
            }
            shownOnce = true;
            resetShown();
        }
    }

    private void resetShown() {
        new CountDownTimer(2000,1000){

            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                shownOnce = false;
            }
        }.start();
    }
    // crad performance either mag , chip , or ic


    public void connDevice(boolean isreset) {
        resetFlag = isreset;
        viewModel.loadKeys();
        //connecting device from terminal sdk
//        SDKDevice sdkDevice1 = SDKDevice.getInstance(context);
//        sdkDevice1.disconnect();
        Logger.v("Connection ");
      //  sdkDevice = SDKDevice.getInstance(context);
       // sdkDevice.connectDevice();
       // Logger.v("Connection Exception -" + sdkDevice.connectException);
//        if (sdkDevice.connectException) {
//            connDevice(isreset);
//        }
    }


    @Override
    protected void onResume() {
        Logger.v("Landing page on resume");
        showTimer = false;
        super.onResume();
        connDevice(false);
        Utils.setNullDialoge();
        clickEnter = false;
        edtAmt.setText("0.00");
        findViewById(R.id.ideal_screen).setVisibility(View.GONE);
        getMemory();
        SAFWorker.failureCount = -1;
        viewModel.landingPageSAF();
        reconsilation.start();
        viewModel.initReaderLandingPage();
        AppConfig.EMV.consumeType = -1;
//        countDownTimer.start();
    }

    @Override
    protected void onPause() {
        Logger.v("oppause_landingpage_closeallreader");
        viewModel.closeCardReader();
        viewModel.cancelSoundTimer();
        viewModel.cancelRequest(true);

        new LightsDisplay(context).hideSingleLight();
        new LightsDisplay(context).hideTwoLight();
        new LightsDisplay(context).hideFourLights();
        CountDownResponseTimer.cancelTimerLanding(2);
        super.onPause();
    }

    private void initViews() {
        ImageView loader = (ImageView) findViewById(R.id.loader);
        findViewById(R.id.txt_transaction_menu).setOnClickListener(this);
        findViewById(R.id.txt_merchant_menu).setOnClickListener(this);
        loader.setOnClickListener(this);
        findViewById(R.id.setting_img).setOnClickListener(this);
        txt_header_tiid = (TextView) findViewById(R.id.txt_header_tiid);
        frm_amount = (FrameLayout) findViewById(R.id.frm_amount);
        ll_data_visib = (LinearLayout) findViewById(R.id.ll_data_visib);
        edtAmt = findViewById(R.id.etd_amnt);
        keyBoard();
        if (AppManager.getInstance().getInitializationStatus(this)) {
            frm_amount.setVisibility(View.VISIBLE);
            loader.setPadding(0, 0, 0, 0);
            edtAmt.setOnEditorActionListener(this);
            frm_amount.setOnClickListener(this);
            edtAmt.setSelection(edtAmt.getText().toString().trim().length());
            edtAmt.setCustomSelectionActionModeCallback(this);
            String txtData = getDataTMD();
            if (txtData.trim().length() != 0) {
                ll_data_visib.setVisibility(View.VISIBLE);
                txt_header_tiid.setText(txtData);
                txt_header_tiid.setSelected(true);
                txt_header_tiid.setHorizontallyScrolling(true);
            } else {
                ll_data_visib.setVisibility(View.GONE);
            }
        } else {
            ll_data_visib.setVisibility(View.GONE);
            frm_amount.setVisibility(View.GONE);
        }
    }

    private String getDataTMD() {
        String dataSet = "";
        AppManager manager = AppManager.getInstance();
        try {
            String tid = manager.getCardAcceptorID41();
            if (tid.trim().length() != 0) {
                dataSet = dataSet + "TID:" + tid.substring(0, 8);
            }
            String name = manager.getRetailerDataModel().getRetailerNameEnglish();
            if(name.trim().length() != 0){
                dataSet = dataSet +"  " + "Merchant Name:" + name;
            }
        } catch (Exception e) {
            Logger.v("Exception e");
        }
        dataSet = dataSet + "  " + "Toll Free Number:920028806";
        return dataSet;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.v("OnDestroy Landing");
        freeMemory();
        Utils.hideDialoge1();
    }

    /*@Override
    public void onClick(View v) {
        Logger.v("OnClick");
        switch (v.getId()) {
            case R.id.txt_transaction_menu:
                MapperFlow.getInstance().moveToTransactionMenu(context);
                break;
            case R.id.txt_merchant_menu:
                MapperFlow.getInstance().moveToPasswrod(context, false);
                break;
            case R.id.setting_img:
                MapperFlow.getInstance().moveToPasswrod(context, true);
                break;
            case R.id.loader:
//                viewModel.makeConnection();
                viewModel.loadKeys();

                break;
            case R.id.dp_tms:
//                getAllAid();
//                Logger.v("Keys --" + emvModule.fetchAllCAPublicKey().toString());
                emv_log_file_clear();
                break;
            case R.id.ideal_screen:
                resetViews();
                break;
        }
    }*/
    @Override
    public void onClick(View v) {
        Logger.v("OnClick");
        switch (v.getId()) {
            case R.id.txt_transaction_menu:
                clickEnter = true;
                MapperFlow.getInstance().moveToTransactionMenu(context);
                break;
            case R.id.txt_merchant_menu:
                clickEnter = true;
                MapperFlow.getInstance().moveToPasswrod(context, false);
                break;
            case R.id.setting_img:
                clickEnter = true;
                MapperFlow.getInstance().moveToPasswrod(context, true);
                break;
            case R.id.ideal_screen:
                resetViews();
               // emv_log_file_clear(); //TODO clear emv logs from terminal
                break;
            case R.id.etd_amnt:
//                viewModel.startSAFTimer();
                edtAmt.requestFocus();
                InputMethodManager imm1 = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm1.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                break;
            case R.id.frm_amount:
//                viewModel.startSAFTimer();
                edtAmt.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                break;
        }
    }

   /* private void getAllCapk() {
        try {
            List<CAPublicKey> listCAPublicKey;
            emvModule.initEmvModule(context);
            listCAPublicKey = emvModule.fetchAllCAPublicKey();
            if (listCAPublicKey != null) {
                for (Iterator i = listCAPublicKey.iterator(); i.hasNext(); ) {
                    CAPublicKey caPublicKey = (CAPublicKey) i.next();
                    int index = caPublicKey.getIndex();
                    byte[] rid = caPublicKey.getRid();
                    String rid1 = Dump.getHexDump(rid);
                    Logger.v(context.getString(R.string.msg_get_pk_index) + "--" + index);
                    Logger.v(context.getString(R.string.msg_get_pk_rid) + "--" + rid1);

                }
                if (listCAPublicKey.size() == 0) {
                    Logger.v("NO Cap key");
                }
            } else {
                Logger.v("NO Cap key -else");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.v("NO Cap key --e--" + e.getMessage());
        }
    }*/

    private static boolean isHexNumber (String cadena) {
        try {
            Long.parseLong(cadena, 16);
            return true;
        }
        catch (NumberFormatException ex) {
            // Error handling code...
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        Logger.v("BACK");
        resetViews();
        alertDialoge = Utils.alertYesDialogShow1(this, getString(R.string.exit_msg), new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Logger.v("onBACKK");
                if (alertDialoge != null) {
                    alertDialoge.dismiss();
                    alertDialoge.cancel();
                }
                finish();
                finishAffinity();
                System.exit(0);
            }
        }, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialoge.dismiss();
                alertDialoge.cancel();
            }
        });
    }


    public void removeCard() {
        Logger.v("removeCard()");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                viewModel.cancelRequest(false);
                CountDownResponseTimer.cancelTimerLanding(2);
                viewModel.initSound();
                AppConfig.isCardRemoved = true;
                Utils.alertDialogOneShow(context, context.getString(R.string.plz_select_card));
                viewModel.startTimerRemoveCard();
            }
        });
    }

    public void restart(){
        Utils.dismissDialoge();
        onResume();
    }
    public void moveToPurchase() {
        if (AppManager.getInstance().getTemprovaryOutService()) {
            viewModel.outOfServiceDialoe(true);
        } else if (Utils.isInternetAvailable(context, false)) {
            if (!Utils.checkPrinterPaper(this)) {
                viewModel.checkSAFCount(edtAmt.getText().toString());
            }
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        Logger.v("onEditorAction -" + i);
        if (i == EditorInfo.IME_ACTION_DONE) {
            clickEnter = true;
            //    connDevice(true);
                moveToPurchase();
        }
        return false;

    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {

    }
    @Override
    public void onFocusChange(View view, boolean b) {
        Logger.v("Boolean Focus-" + b);
        edtAmt.setSelection(edtAmt.getText().toString().trim().length());
    }
    public void reconnectDevice() {
        Logger.v("reconnectDevice");
        if (clickEnter)
            connDevice(true);
        else
            connDevice(false);
    }
}
