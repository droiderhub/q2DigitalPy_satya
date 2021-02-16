package com.tarang.dpq2.view.activities;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import com.tarang.dpq2.R;
import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.baseactivities.BaseActivity;
import com.tarang.dpq2.base.jpos_class.ByteConversionUtils;
import com.tarang.dpq2.base.jpos_class.ConstantApp;
import com.tarang.dpq2.base.room_database.db.AppDatabase;
import com.tarang.dpq2.base.room_database.db.entity.TransactionModelEntity;
import com.tarang.dpq2.base.utilities.Utils;
import com.tarang.dpq2.model.ReconcileSetupModel;
import com.tarang.dpq2.viewmodel.MenuViewModel;
import com.tarang.dpq2.worker.PrinterWorker;
import com.tarang.dpq2.worker.SSLSocketFactoryExtended;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Calendar;

import javax.net.ssl.SSLSocket;


public class MerchantMenuActivity extends BaseActivity implements ConstantApp, View.OnClickListener {

    LinearLayout reconciliation_setup_layout, merchantmenu_layout,simNumberLayout,changePasswordLayout,select_connection_layout;
    TextView merchantmenu_text,reconciliation_time,reconcilation_status;
    private MenuViewModel viewModel;
    ToggleButton reconciliation_enable;
    EditText sim_number_et;
    Button submit_simNumber;
    private boolean retryRequest = false;
    private int attempt;
    String currentStan = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_merchantmenu);
        initData();
        viewModel = ViewModelProviders.of(this).get(MenuViewModel.class);
        viewModel.init(this, this);

        viewModel.getConnectionStatus().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                Logger.v("getConnectionStatusMerchant -"+aBoolean);
                if(aBoolean != null && aBoolean){
                    viewModel.makeSocketConnection();
                }
            }
        });

        viewModel.getShowAlert().observe(this, viewModel.observable);

        viewModel.loadKeys().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean != null && aBoolean)
                    viewModel.loadKeysIntoTerminal();
            }
        });

        viewModel.startTimer().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean)
                    viewModel.startCountDownTimer();
                else
                    viewModel.startSAFCountDownTimer();
            }
        });

        viewModel.getPrintStatus().observe(this, viewModel.printObservable);


        init();
        setTextView();
    }


    private void init() {
        reconciliation_setup_layout = (LinearLayout) findViewById(R.id.reconcilation_setup);
        merchantmenu_text = (TextView) findViewById(R.id.merchant_menu_type);
        merchantmenu_layout = (LinearLayout) findViewById(R.id.merchantmenu_layout);
        simNumberLayout = (LinearLayout) findViewById(R.id.simNumberLayout);
        changePasswordLayout = (LinearLayout) findViewById(R.id.changePasswordLayout);
        select_connection_layout = (LinearLayout) findViewById(R.id.select_connection_layout);
        reconciliation_time = (TextView) findViewById(R.id.reconciliation_time);
        ReconcileSetupModel reconcileSetupModel1 = AppManager.getInstance().getReconcileSetupModel();
        reconciliation_time.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        showTimer = true;
        super.onResume();
    }

    private void setTextView() {
        String tag = getCurrentMenu().getMenu_tag();
        //  Toast.makeText(context, tag, Toast.LENGTH_SHORT).show();
        switch (tag) {
            case DUPLICATE:
                setTitle(getCurrentMenu().getMenu_name());
                merchantmenu_layout.setVisibility(View.VISIBLE);
                merchantmenu_text.setText(getCurrentMenu().getMenu_name());
                printDuplicate();
                break;
            case RECONCILIATION:
                setTitle(getCurrentMenu().getMenu_name());
                merchantmenu_layout.setVisibility(View.VISIBLE);
                makeReconsilation();
                break;
            case SANPSHOT_TOTAL:
                setTitle(getCurrentMenu().getMenu_name());
                merchantmenu_layout.setVisibility(View.VISIBLE);
                merchantmenu_text.setText(getCurrentMenu().getMenu_name());
                makeSnapShot(false);
                break;
            case RUNNING_TOTAL:
                setTitle(getCurrentMenu().getMenu_name());
                merchantmenu_layout.setVisibility(View.VISIBLE);
                merchantmenu_text.setText(getCurrentMenu().getMenu_name());
                makeSnapShot(true);
                break;
            case RECONCILE_SETUP:
                setTitle(getCurrentMenu().getMenu_name());
                reconciliation_setup_layout.setVisibility(View.VISIBLE);
                initReconcileView();
                break;
            case HISTORY_VIEW:
                setTitle(getCurrentMenu().getMenu_name());
                break;
            case LAST_EMV:
                setTitle(getCurrentMenu().getMenu_name());
                break;
            case CHANGE_PASSWORD:
                setTitle(getCurrentMenu().getMenu_name());
                changePasswordLayout.setVisibility(View.VISIBLE);
                setUpPasswordView();
                break;
            case CHANGE_PASSWORD_ADMIN:
                setTitle(getCurrentMenu().getMenu_name());
                changePasswordLayout.setVisibility(View.VISIBLE);
                setUpPasswordView();
                break;
            case TERMINFO_MENU:
                setTitle(getCurrentMenu().getMenu_name());
                break;
            case SIM_NUMBER:
                setTitle(getCurrentMenu().getMenu_name());
                simNumberLayout.setVisibility(View.VISIBLE);
                storeSimNumberLocally();
                break;
            case DE_SAF_ALL_FILE:
                setTitle(getCurrentMenu().getMenu_name());
                deSafFile();
                break;
            case SWITCH_CONNECTION:
                setTitle(getCurrentMenu().getMenu_name());
                select_connection_layout.setVisibility(View.VISIBLE);
                break;
            case SET_GPS_LOCATION:
                setTitle(getCurrentMenu().getMenu_name());
                break;
            case SELECT_LANGUAGE:
                setTitle(getCurrentMenu().getMenu_name());
                break;
            case MPORTAL_BATCH_UPLOAD:
                setTitle(getCurrentMenu().getMenu_name());
                uploadBatchData();
                break;
        }
    }

    private void uploadBatchData() {
        attempt = 0;
        sendMessage();
    }

    Thread thread;

    private void sendMessage() {
        cancelTimer();
        Logger.v("Merchant portal--"+attempt);
        if(4 < attempt){
            Utils.alertDialogShow(context, "Host not available, try again later \n المضيف غير متوفر. حاول مرة أخرى في وقت لاحق", true);
            return;
        }
        if (!AppManager.getInstance().isMerchantPoratalEnable()) {
            Utils.alertDialogShow(context, context.getString(R.string.empty_batch), true);
            return;
        }
        final Handler handler = new Handler();
        final CountDownTimer mPortalTimer = new CountDownTimer(15000, 1000) {

            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                try {
                    thread.stop();
                } catch (Exception e) {

                }
                if (!retryRequest) {
                    sendMessage();
                }
            }
        };
        final String IP = AppManager.getInstance().getMerchantIP();
        final int port = Integer.parseInt(AppManager.getInstance().getMerchantPort());
        final InputStream inputStream = context.getResources().openRawResource(R.raw.newcerten);
        final String version = AppManager.getInstance().getString(ConstantApp.HSTNG_TLS);
        final AppDatabase database = AppDatabase.getInstance(context.getApplicationContext());
        Runnable run = new Runnable() {
            @Override
            public void run() {
                SSLSocket requestSocket;
                try {
                    //Replace below IP with the IP of that device in which server socket open.
                    //If you change port then change the port number in the server side code also.
                    boolean safTable = false;
                    TransactionModelEntity oldRequest = database.getTransactionDao().getMPortalRequest("", false);
                    if (oldRequest == null) {
                        Logger.v("Fetched from SAF");
                        safTable = true;
                        oldRequest = database.getSAFDao().getMPortalRequest("", false);
                    }
                    if (oldRequest != null) {
                        Logger.v("OLD transaction not null"+oldRequest.getSystemTraceAuditnumber11());
                        if(oldRequest.getSystemTraceAuditnumber11().equalsIgnoreCase(currentStan)){
                            attempt = attempt +1;
                        }else {
                            attempt = 1;
                            currentStan = oldRequest.getSystemTraceAuditnumber11();
                        }
                        SSLSocketFactoryExtended sslsocketfactory = new SSLSocketFactoryExtended(inputStream, version, false);
                        Logger.v("Socket 1");
                        requestSocket = (SSLSocket) sslsocketfactory.createSocket(IP, port);
                        retryRequest = false;
                        BufferedInputStream bis = new BufferedInputStream(requestSocket.getInputStream());
                        BufferedOutputStream bos = new BufferedOutputStream(requestSocket.getOutputStream());
                        Logger.v(oldRequest.getRequest_mportal());
                        bos.write(ByteConversionUtils.HexStringToByteArray(oldRequest.getRequest_mportal()));
                        bos.flush();
                        // 4: Receive the response data
                        byte[] buffer1 = new byte[1024];
                        int nBytes1 = -1;
                        Logger.v("Socket connected Merchant-" + requestSocket.isConnected());
                        Logger.v("TRY block Receive");
                        while ((nBytes1 = bis.read(buffer1)) >= 0) {
                            final String output1 = ByteConversionUtils.byteArrayToHexString(buffer1, buffer1.length, false);
                            String result1 = ByteConversionUtils.convertHexToString(output1);
                            Logger.v("Result -" + result1);
                            try {
                                String[] tags = result1.split("<GS>");
                                Logger.v(tags);
                                if (tags[1].equalsIgnoreCase("200")) {
                                    Logger.v("UPDATE RECONNS");
                                    if (safTable)
                                        database.getSAFDao().updateSAFMerchantPortal(oldRequest.getSystemTraceAuditnumber11(), true, "");
                                    else
                                        database.getTransactionDao().updateSAFMerchantPortal(oldRequest.getSystemTraceAuditnumber11(), true, "");
                                }
                            } catch (Exception e) {
                                Logger.v("Exceptionee -" + e.getMessage());
                            }

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mPortalTimer.cancel();
                                    mPortalTimer.onFinish();
                                }
                            });
                            return;
                        }
                        closeRequest(requestSocket);
                        Logger.v("Socket close");
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                retryRequest = true;
                                Utils.alertDialogShow(context, context.getString(R.string.complete), true);
                                mPortalTimer.cancel();
                                mPortalTimer.onFinish();
                            }
                        });
                    }
                } catch (
                        IOException e) {
                    e.printStackTrace();
                    Logger.v("Exception");
                    exceptionFlow(handler,mPortalTimer);
                } catch (
                        CertificateException e) {
                    Logger.v("Exception 1");
                    e.printStackTrace();
                    exceptionFlow(handler,mPortalTimer);
                } catch (
                        NoSuchAlgorithmException e) {
                    Logger.v("Exception 2");
                    e.printStackTrace();
                    exceptionFlow(handler,mPortalTimer);
                } catch (
                        KeyStoreException e) {
                    Logger.v("Exception 3");
                    e.printStackTrace();
                    exceptionFlow(handler,mPortalTimer);
                } catch (KeyManagementException e) {
                    Logger.v("Exception 4");
                    e.printStackTrace();
                    exceptionFlow(handler,mPortalTimer);
                } catch (NullPointerException e) {
                    Logger.v("Exception 44");
                    e.printStackTrace();
                    exceptionFlow(handler,mPortalTimer);
                }
            }
        };
        mPortalTimer.start();
        Utils.alertDialogShow(context, context.getString(R.string.processing));
        thread = new Thread(run);
        thread.start();
    }

    private void exceptionFlow(Handler handler, final CountDownTimer mPortalTimer) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                mPortalTimer.cancel();
                mPortalTimer.onFinish();
            }
        });
    }

    private void closeRequest(SSLSocket requestSocket) {
        try {
            if (requestSocket.isClosed()) {
                requestSocket.close();
            }
        } catch (Exception e) {
            Logger.v("Exceptin ssl");
        }
    }

    private void storeSimNumberLocally() {
        sim_number_et = findViewById(R.id.sim_number_et);
        submit_simNumber = findViewById(R.id.submit_simNumber);
        if (AppManager.getInstance().getString("sim_number")!=null){
            sim_number_et.setText(AppManager.getInstance().getString("sim_number"));
        }
        submit_simNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sim_number_et.getText().toString().trim()!= null && !sim_number_et.getText().toString().trim().equalsIgnoreCase("")) {
                    AppManager.getInstance().setString("sim_number", sim_number_et.getText().toString().trim());
                    Toast.makeText(context, context.getString(R.string.sim_number_changed_successfully), Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(MerchantMenuActivity.this, getString(R.string.plz_enter_valid_sim_number), Toast.LENGTH_SHORT).show();
                }
            }
        });
        //AppManager.getInstance().setString("sim_number", sim_number_et.getText().toString().trim());
    }

    private void setUpPasswordView() {
        final EditText input_current_password = findViewById(R.id.input_current_password);
        final EditText input_new_password = findViewById(R.id.input_new_password);
        final EditText input_confirm_password = findViewById(R.id.input_confirm_password);
        findViewById(R.id.submit_newpassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(input_current_password.getText().toString().trim().length() != 0){
                    if(input_current_password.getText().toString().trim().length() == 6){
                        if(input_new_password.getText().toString().trim().length() != 0) {
                            if (input_new_password.getText().toString().trim().length() == 6) {
                                if (input_new_password.getText().toString().equalsIgnoreCase(input_confirm_password.getText().toString())) {
                                    if (getCurrentMenu().getMenu_tag().equalsIgnoreCase(ConstantApp.CHANGE_PASSWORD_ADMIN)) {
                                        if (input_current_password.getText().toString().equalsIgnoreCase(AppManager.getInstance().getAdminPassword())) {
                                            AppManager.getInstance().setAdminPassword(input_new_password.getText().toString());
                                            Toast.makeText(context, context.getString(R.string.password_changed_successfully), Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Toast.makeText(context, getString(R.string.please_enter_valid_current_password), Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        if (input_current_password.getText().toString().equalsIgnoreCase(AppManager.getInstance().getMerchantPassword())) {
                                            AppManager.getInstance().setMerchantPassword(input_new_password.getText().toString());
                                            Toast.makeText(context, context.getString(R.string.password_changed_successfully), Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Toast.makeText(context, getString(R.string.please_enter_valid_current_password), Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                } else
                                    Toast.makeText(context, getString(R.string.password_missmatch), Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(context, getString(R.string.please_enter_6_digit_password), Toast.LENGTH_SHORT).show();
                        }else
                            Toast.makeText(context, getString(R.string.please_enter_6_digit_password), Toast.LENGTH_SHORT).show();
                    }else
                        Toast.makeText(context, getString(R.string.invalid_current_password), Toast.LENGTH_SHORT).show();

                }else
                    Toast.makeText(context, getString(R.string.please_enter_current_passwrod), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deSafFile() {
        viewModel.doSAFOnly = true;
        viewModel.isDownload = 1;
        viewModel.createReconsilation(false);
    }

    private void printDuplicate() {
        viewModel.printDuplicate();
    }

    private void makeSnapShot(boolean isRunning) {
        viewModel.printSnapShot(isRunning);
    }

    private void makeReconsilation() {
        viewModel.doSAFOnly = false;
        viewModel.isDownload = 1;
        showTimer = false;
        viewModel.createReconsilation(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case (R.id.reconciliation_time):
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(MerchantMenuActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
                        String date = String.format("%02d:%02d", hourOfDay, minuteOfHour);
                        if(!(reconciliation_time.getText().toString().equalsIgnoreCase(date))){
                            AppManager.getInstance().resetReconsilationDate();
                        }
                        reconciliation_time.setText(date);
                        Logger.v("reconcile_time----"+reconciliation_time.getText().toString().trim());
                        reconcileSetupModel.setReconcileTime(reconciliation_time.getText().toString().trim());
                        AppManager.getInstance().setReconcileSetupModel(reconcileSetupModel);
                    }
                }, hour, minute,true);

                mTimePicker.show();
                break;
        }
    }
    ReconcileSetupModel reconcileSetupModel;
    private void initReconcileView() {
        reconcilation_status = findViewById(R.id.reconcilation_status);
        reconciliation_enable = findViewById(R.id.reconciliation_enable);
        if (AppManager.getInstance().getReconcileSetupModel()!=null){
            reconcileSetupModel = AppManager.getInstance().getReconcileSetupModel();
            reconcilation_status.setText(reconcileSetupModel.getReconcileStatus()?getString(R.string.reconsiled):getString(R.string.not_reconsiled));
            reconciliation_enable.setText(reconcileSetupModel.getReconcileToggle()?getString(R.string.on):getString(R.string.off));
            reconciliation_time.setText(reconcileSetupModel.getReconcileTime());
        }
        else{
            reconcileSetupModel = new ReconcileSetupModel();
            reconcilation_status.setText(getString(R.string.not_configured));
        }
        reconciliation_enable.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //toggle.toggle();
                if ( reconciliation_enable.getText().toString().equalsIgnoreCase("ON")) {
                    reconcileSetupModel.setReconcileToggle(true);
                    reconcileSetupModel.setReconcileTime(reconciliation_time.getText().toString().trim());
                    AppManager.getInstance().setReconcileSetupModel(reconcileSetupModel);
//                    Toast.makeText(context, "on", Toast.LENGTH_SHORT).show();


                    reconciliation_enable.setChecked(true);
                } else if ( reconciliation_enable.getText().toString().equalsIgnoreCase("OFF")) {
                    reconcileSetupModel.setReconcileToggle(false);
                    reconciliation_enable.setChecked(false);
                    reconcileSetupModel.setReconcileTime(reconciliation_time.getText().toString().trim());
                    AppManager.getInstance().setReconcileSetupModel(reconcileSetupModel);
//                    Toast.makeText(context, "off", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PrinterWorker.RECON_PRINTED = false;
        PrinterWorker.RECON_PRINTED_DUB = false;
        Utils.setNullDialoge();
    }
}
