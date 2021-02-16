package com.tarang.dpq2.view.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.cloudpos.AlgorithmConstants;
import com.cloudpos.DeviceException;
import com.cloudpos.OperationResult;
import com.cloudpos.TimeConstants;
import com.cloudpos.pinpad.KeyInfo;
import com.cloudpos.pinpad.PINPadDevice;
import com.cloudpos.pinpad.PINPadOperationResult;
import com.cloudpos.pinpad.extend.PINPadExtendDevice;

import com.tarang.dpq2.BuildConfig;
import com.tarang.dpq2.R;
import com.tarang.dpq2.base.AppInit;
import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.MapperFlow;
import com.tarang.dpq2.base.baseactivities.BaseActivity;
import com.tarang.dpq2.base.jpos_class.ByteConversionUtils;
import com.tarang.dpq2.base.jpos_class.ConstantApp;
import com.tarang.dpq2.base.jpos_class.ConstantAppValue;
import com.tarang.dpq2.base.terminal_sdk.AppConfig;
import com.tarang.dpq2.base.terminal_sdk.SdkSupport;
import com.tarang.dpq2.base.terminal_sdk.device.SDKDevice;
import com.tarang.dpq2.base.terminal_sdk.event.SimpleTransferListener;
import com.tarang.dpq2.base.terminal_sdk.utils.LightsDisplay;
import com.tarang.dpq2.base.terminal_sdk.utils.MessageTag;
import com.tarang.dpq2.base.utilities.CountDownResponseTimer;
import com.tarang.dpq2.base.utilities.Utils;
import com.tarang.dpq2.viewmodel.TransactionViewModel;
import com.tarang.dpq2.worker.SAFWorker;
import com.tarang.dpq2.worker.SocketConnectionWorker;

import java.util.List;


import static com.tarang.dpq2.base.Logger.showMessage;
import static com.tarang.dpq2.base.Logger.v;
import static com.tarang.dpq2.base.jpos_class.ConstantApp.TAG_CARD_MANUAL;
import static com.tarang.dpq2.base.jpos_class.ConstantApp.TAG_REVERSAL;
import static com.tarang.dpq2.base.terminal_sdk.AppConfig.EMV.pinBlock;
import static com.tarang.dpq2.base.terminal_sdk.AppConfig.EMV.reqObj;
import static com.tarang.dpq2.base.terminal_sdk.event.SimpleTransferListener.isPinCancelled;

public class PrintActivity extends BaseActivity {

    private TransactionViewModel viewModel;
    TextView etd_number;
    TextView etd_number_;
    TextView txn_type_tv;
    TextView etd_number_ar;
    TextView etd_number_ar_;
    private TextView txt_expiry_date;
    private TextView txt_account_no;
    private TextView card_name_show;
    public static boolean disableBackBtn;
    boolean isChipEnabled, isMagneticEnabled, isRFEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_print);
        Logger.v("app_version---" + BuildConfig.VERSION_NAME);
        Logger.v("mada_version---" + AppInit.VERSION_6_0_5);
        disableBackBtn = false;
        initData();
        SimpleTransferListener.isScreenAvailable = 1;
        SAFWorker.isSAFRepeat = false;
        Logger.v("Menu : " + getCurrentMenu().getMenu_name());
        setTitle(getCurrentMenu().getMenu_name());
        Logger.v("PrintActivity");
        viewModel = ViewModelProviders.of(this).get(TransactionViewModel.class);
        viewModel.init(this, this, this);
        viewModel.transactionType = getCurrentMenu().getMenu_tag();
        viewModel.getConnectionStatus().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer i) {
                Logger.v("getConnectionStatus Socket---" + i);
                makeSocketConnection(i);
            }
        });

        findViewById(R.id.manual_entry_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapperFlow.getInstance().moveToManualEntry(context);
            }
        });

        viewModel.getShowAlert().observe(this, viewModel.shoeAlertObserver);

        viewModel.getPrintStatus().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                Logger.v("printReceipt -" + aBoolean);
                disableBackBtn = true;
                if (!checkPaper(aBoolean)) {
                    viewModel.printReceipt(aBoolean);
                }
            }
        });

        viewModel.getSAFStatus().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                Logger.v("getSAFStatus -" + aBoolean);
                if (aBoolean != null && aBoolean) {
                    viewModel.insertSAFTransaction();
                }
            }
        });
        viewModel.getRestartTimer().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                Logger.v("getSAFStatus -" + aBoolean);
                if (aBoolean != null && aBoolean) {
                    resetTimer();
                } else {
                    showTimer = false;
                }
            }
        });
        viewModel.getOnLInePin().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                Logger.v("getOnLInePin -" + aBoolean);
                if (aBoolean != null && aBoolean) {
                    waitForPinBlock();
                }
            }
        });

        viewModel.initiateTimer().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                viewModel.startCountDownTimer(aBoolean);
            }
        });

        viewModel.initiateRemoveCardTimer().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                cancelTimer();
                viewModel.initSound();
                AppConfig.isCardRemoved = true;
                viewModel.startTimerRemoveCard();
            }
        });

        viewModel.safRepeat().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Logger.v("check SAF1 -" + SAFWorker.isSAFRepeat);
                if (SAFWorker.isSAFRepeat) {
                    if (viewModel.reversalSAF)
                        viewModel.transsactionReversal(true);
                    else
                        viewModel.checkSAF(true);
                }
            }
        });
        Logger.v("PrintActivity");
        hideAmountView();
        initViews();
        checkTransactionTpe();
        checkCardReader();
    }

    @Override
    protected void onResume() {
        showTimer = true;
        super.onResume();
    }

    public void printOkay() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                viewModel.printOkay();
                SocketConnectionWorker.TRANSACTION_START_TIME = Utils.getCurrentDate();
            }
        });
    }

    private void initViews() {
        etd_number = findViewById(R.id.etd_number);
        etd_number_ = findViewById(R.id.etd_number_);
        etd_number_ar = findViewById(R.id.etd_number_ar);
        etd_number_ar_ = findViewById(R.id.etd_number_ar_);
        etd_number.setText(Utils.formatAmountWithoutSAR(this, AppConfig.EMV.amountValue));
        etd_number_.setText(" " + Utils.formatAmountWithoutSAR(this, AppConfig.EMV.amountValue));
        txt_expiry_date = findViewById(R.id.txt_expiry_date);
        card_name_show = findViewById(R.id.card_name_show);
        txt_account_no = findViewById(R.id.txt_account_no);
        etd_number_ar.setText(Utils.formatAmountWithoutSAR(AppConfig.EMV.amountValue)+" ريال ");
        etd_number_ar_.setText(Utils.formatAmountWithoutSAR(AppConfig.EMV.amountValue)+" ريال ");
        if (getCurrentMenu().getMenu_tag().equalsIgnoreCase(ConstantApp.PURCHASE_NAQD))
            setUpCashBackView();
    }


    private void setUpCashBackView() {
        TextView txt_purchasr_cash = findViewById(R.id.txt_purchasr_cash);
        TextView txt_cashback = findViewById(R.id.txt_cashback);
        txt_purchasr_cash.setText(context.getString(R.string.purchase_amount) + "  " + Utils.formatAmount(this, (AppConfig.EMV.amountValue - AppConfig.EMV.amtCashBack)));
        txt_cashback.setText(context.getString(R.string.naqd_amount_) + "       " + Utils.formatAmount(this, AppConfig.EMV.amtCashBack));
    }


    //after card listn moving to printer activity , checking pin required or not only in case of mag
    private void checkCardReader() {
        Logger.v("checkCardReader");
        //TODO online changes
        if (getIntent().hasExtra(ConstantApp.TAG_FALLBACK) && getIntent().getBooleanExtra(ConstantApp.TAG_FALLBACK, false)) {
            Logger.v("fallback---9");
            showAlert(9);
        } else if (getIntent().hasExtra(ConstantApp.DO_WAVE_AGAIN) && getIntent().getBooleanExtra(ConstantApp.DO_WAVE_AGAIN, false)) {
            Logger.v("wave_again---15");
            showAlert(15);
        } else if (getIntent().hasExtra(TAG_REVERSAL) && getIntent().getBooleanExtra(TAG_REVERSAL, false)) {
            Logger.v("reversal");
            findViewById(R.id.main_frame_layout).setVisibility(View.GONE);
            findViewById(R.id.reversal_layout).setVisibility(View.VISIBLE);
            SimpleTransferListener.isEMVCompleted = true;
            SocketConnectionWorker.setEventHandler(viewModel.getEventHandler(), 3);
            viewModel.transsactionReversal(false);
        } else if (getIntent().hasExtra(TAG_CARD_MANUAL) && getIntent().getBooleanExtra(TAG_CARD_MANUAL, false)) {
            Logger.v("card_manual");
            final String accNo = getIntent().getStringExtra(ConstantApp.TAG1);
            final String expDate = getIntent().getStringExtra(ConstantApp.TAG2);
            txt_expiry_date.setText(expDate.substring(2) + "/" + expDate.substring(0, 2));
            txt_account_no.setText(accNo);
            showCard();
            viewModel.readManualEntery(accNo, expDate);
        } else {
            SdkSupport sdkSupport = new SdkSupport(context);
            sdkSupport.cancelMSREader(999);
            Logger.v("closing_msr_listner");

            Logger.v("Card -- " + AppConfig.EMV.consumeType);
            if (AppConfig.EMV.consumeType == 0) {
//            viewModel.readMSCARD();
                Logger.v("read_mag");
                readMSCard();

            } else if (AppConfig.EMV.consumeType == 1) {
                Logger.v("read_chip");
                viewModel.readChipInsert();
            } else if (AppConfig.EMV.consumeType == 2) {
                Logger.v("read_rf");
                viewModel.readRfCard();
            }
            Logger.v("createISORequest_Packet_printactivity");
        }
    }

    private void readMSCard() {
//        if (viewModel.isPinRequired()) {
//        } else {
        magneticFlow();
//        }
    }

    public void waitForPinBlock() {
        Logger.v("Pin entry");
        PINPadExtendDevice device = SDKDevice.getInstance(context).getDeviceConnectPin();
        KeyInfo keyInfo = new KeyInfo(PINPadDevice.KEY_TYPE_TDUKPT, 1, AlgorithmConstants.ALG_3DES);
        try {
            String cardNo = AppConfig.EMV.swipResult.getPan();
            OperationResult operationResult = device.waitForPinBlock(keyInfo, cardNo, false, TimeConstants.FOREVER);
            Logger.v("Result --" + operationResult.getResultCode());
            if (operationResult.getResultCode() == OperationResult.SUCCESS) {
                byte[] pinBlockKSN = ((PINPadOperationResult) operationResult).getEncryptedPINBlock();
                if (pinBlockKSN != null && pinBlockKSN.length != 0) {
                    String pin = ByteConversionUtils.byteArrayToHexString(pinBlockKSN);
                    Logger.v("PINBlock = " + pin);
                    SDKDevice.getInstance(context).setKSN(pin.substring(pin.length() - 20));
                    SimpleTransferListener.isPinRequires = 1;
                    byte[] pinBlock = new byte[8];
                    System.arraycopy(pinBlockKSN, 0, pinBlock, 0, 8);
                    Logger.v(pinBlock);
                    AppConfig.EMV.pinBlock = (pinBlock);
                    SimpleTransferListener.pinBlock = pinBlock;
                    Logger.v("pinBlock --" + new String(pinBlock));
                    Logger.v("pinBlock --" + ByteConversionUtils.byteArrayToHexString(pinBlock));
                    viewModel.msFlow();
                    return;
                } else {
                    isPinCancelled = true;
                }
            } else if (operationResult.getResultCode() == OperationResult.SUCCESS) {
                Logger.v("Pin Bypass");
                AppConfig.EMV.pinBlock = null;
            } else {
                isPinCancelled = true;
                Logger.v("waitForPinBlock fail,");
            }
            if (isPinCancelled) {
                viewModel.moveNext(120);
            } else {
                viewModel.msFlow();
            }
        } catch (DeviceException e) {
            e.printStackTrace();
            Logger.v("waitForPinBlock fail," + e.getMessage());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.v("onActivityResult_print_activity");
        if (requestCode == 001) {
            if (resultCode == RESULT_OK) {
                byte[] pin = data.getByteArrayExtra("pin");
//                if (pin != null && pin.length == 0) {
//                    viewModel.showAlert(0);
//                    showMessage(getString(R.string.msg_free_pwd), MessageTag.TIP);
//                } else {
                pinBlock = pin;
                viewModel.msFlow();
                Log.i("onActivityResult", getString(R.string.msg_enter_succ) + pin);

//                }
            } else if (resultCode == RESULT_CANCELED) {
                viewModel.moveNext(120);
                showMessage(getString(R.string.msg_n900_cancel_enter_pwd) + "\r\n", MessageTag.TIP);
            } else if (resultCode == -2) {
                viewModel.moveNext(130);
                showMessage(getString(R.string.input_pin_fail) + "\r\n", MessageTag.ERROR);
            }
        }
    }

    public void magneticFlow() {
        Logger.v("magneticFlow()");
        String date = AppConfig.EMV.swipResult.getExpiry();
        txt_expiry_date.setText(date.substring(2) + "/" + date.substring(0, 2));
        txt_account_no.setText(Utils.addAccountNumber(AppConfig.EMV.swipResult.getPan()));
        showCard();
        viewModel.addMSCARDDetails();
        Logger.v("createISORequest Packet");
    }

    private void showCard() {
        if (findViewById(R.id.fram_card_view) != null)
            findViewById(R.id.fram_card_view).setVisibility(View.VISIBLE);
        if (findViewById(R.id.listner_view) != null) {
            findViewById(R.id.listner_view).setVisibility(View.GONE);
            viewModel.cardDetailsShown();
            if (findViewById(R.id.main_frame_layout) != null)
                ((FrameLayout) findViewById(R.id.main_frame_layout)).removeView(findViewById(R.id.listner_view));
        }
    }

    //making socket connection in view model
    private void makeSocketConnection(Integer i) {
        Logger.v("makeSocketConnection-----" + i);
        //disableBackBtn = true;
        //creating isopacket , sdtoring refence od AppConfig
//        if (viewModel.disableCreatePacket)
//        viewModel.createISORequest();
        if (i == TransactionViewModel.SAVE_CONNECT || i == TransactionViewModel.SAVE_CONNECT_AGAIN) {
            //inserting isopacket request data to db
            AppConfig.EMV.enableDatabaseUpdate = true;
            viewModel.insertOrUpdateTransaction();
        } else {
            if (reqObj != null && (reqObj.getNameTransactionTag().equalsIgnoreCase(ConstantApp.FINANCIAL_ADVISE) || reqObj.getNameTransactionTag().equalsIgnoreCase(ConstantApp.AUTHORIZATION_ADVICE)))
                AppConfig.EMV.enableDatabaseUpdate = false;
            Logger.v("makeSocketConnection_else");
            //creating socket connection();
            viewModel.makeSocketRequest();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Logger.v("New Intent --" + intent.hasExtra(ConstantApp.TAG_FALLBACK));
        if (intent.hasExtra(ConstantApp.TAG_FALLBACK)) {
            Logger.v("New Intent --" + intent.getBooleanExtra(ConstantApp.TAG_FALLBACK, false));
            if (intent.getBooleanExtra(ConstantApp.TAG_FALLBACK, false)) {
                viewModel.forceClose = false;
                if (AppConfig.EMV.consumeType == 0) {
                    if (!viewModel.transactionType.equalsIgnoreCase(ConstantApp.PURCHASE_NAQD)) {
                        Utils.dismissDialoge();
                        readMSCard();
                    }
                } else if (AppConfig.EMV.consumeType == 1) {
                    viewModel.readChipInsert();
                } else if (AppConfig.EMV.consumeType == 2) {
                    viewModel.readRfCard();
                }
            }
        } else {
            String date = AppConfig.EMV.icExpiredDate;
            if (date != null && date.length() == 4)
                txt_expiry_date.setText(date.substring(2) + "/" + date.substring(0, 2));
            setAccountNo();
            viewModel.validateICResponse();
        }
    }

    public void setAccountNo() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String date = AppConfig.EMV.icExpiredDate;
                if (date != null && date.length() == 4)
                    txt_expiry_date.setText(date.substring(2) + "/" + date.substring(0, 2));
                txt_account_no.setText(Utils.addAccountNumber(AppConfig.EMV.icCardNum));
                showCard();
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        viewModel.closeFallBack(8);
    }

    public void fallBackListner() {
        viewModel.startFallbackListner();
    }

    private void checkTransactionTpe() {
        TextView txt_header = (TextView) findViewById(R.id.txt_header);
        isChipEnabled = true;
        if (!viewModel.transactionType.equalsIgnoreCase(ConstantApp.PURCHASE_NAQD)) {
            isMagneticEnabled = true;
        }
        Logger.v("transaction_type----" + viewModel.transactionType);
        if (viewModel.transactionType.equalsIgnoreCase(ConstantApp.PURCHASE)
                || viewModel.transactionType.equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION_EXTENSION)
                || viewModel.transactionType.equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION_VOID)
                || viewModel.transactionType.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_FULL)
                || viewModel.transactionType.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_PARTIAL)
                || viewModel.transactionType.equalsIgnoreCase(ConstantApp.CASH_ADVANCE)
                || viewModel.transactionType.equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION)
                || viewModel.transactionType.equalsIgnoreCase(ConstantApp.REFUND)
                || viewModel.transactionType.equalsIgnoreCase(ConstantApp.PURCHASE_NAQD)) {
            if (!Utils.checkArabicLanguage(this))
                isRFEnabled = true;
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

    public void showStatusDiaolge(final boolean executeRslt, final boolean noError, final boolean madeOnlineConnection) {
        Logger.v("showStatusDiaolge " + executeRslt + " " + noError + " " + madeOnlineConnection);
        Logger.v("DE39 : " + AppManager.getInstance().getDe39());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                viewModel.isPrinted = false;
                if (AppManager.getInstance().isDebugEnabled()) {
                    if (AppConfig.EMV.consumeType == 2)
                        new LightsDisplay(PrintActivity.this).showGreenLights();
                    // viewModel.showAlert(100);
                    viewModel.setApprovedMsg(100);
                } else if (((!madeOnlineConnection && (executeRslt)) ||
                        (madeOnlineConnection && (executeRslt) && (AppManager.getInstance().getDe39() != null &&
                                (AppManager.getInstance().getDe39().equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_000)
                                        || AppManager.getInstance().getDe39().equalsIgnoreCase("400")
                                        || AppManager.getInstance().getDe39().equalsIgnoreCase(ConstantAppValue.SAF_APPROVED)
                                        || AppManager.getInstance().getDe39().equalsIgnoreCase(ConstantAppValue.SAF_APPROVED_UNABLE)
                                        || AppManager.getInstance().getDe39().equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_003)
                                        || AppManager.getInstance().getDe39().equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_001)
                                        || AppManager.getInstance().getDe39().equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_007))))) && noError) {
                    if (AppConfig.EMV.consumeType == 2)
                        new LightsDisplay(PrintActivity.this).showGreenLights();
                    if (!madeOnlineConnection) {
                        viewModel.setApprovedMsg(AppManager.getInstance().getDe39().equalsIgnoreCase(ConstantAppValue.SAF_APPROVED));
                    } else
                        viewModel.setApprovedMsg(100);
                    Logger.v("showStatusDiaolge 100");
                } else {
                    if (AppConfig.EMV.consumeType == 2)
                        new LightsDisplay(PrintActivity.this).showRedLight();
                    viewModel.setApprovedMsg(101);
                }
            }
        });
    }

    public void showAlert(final int i) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                /*if (i == 15) {
                    viewModel.waveAgain();
                } else*/
                viewModel.showAlert(i);
            }
        });
    }

    private boolean checkPaper(final Boolean aBoolean) {
        return Utils.checkPrinterPaper(context, new Utils.DialogeClick() {
            @Override
            public void onClick() {
                if (!checkPaper(aBoolean))
                    viewModel.printReceipt(aBoolean);
            }
        });
    }


    public void showAppSection(final List<String> nameList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Utils.alertDialogShow(context, nameList);
            }
        });

    }

    private boolean doubleBackToExitPressedOnce;
    private Handler mHandler = new Handler();

    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };

    @Override
    protected void onDestroy() {
        new LightsDisplay(context).hideSingleLight();
        new LightsDisplay(context).hideTwoLight();
        new LightsDisplay(context).hideFourLights();
//        K21Pininput pinInput = SDKDevice.getInstance(context).getK21Pininput();
//        if (pinInput != null) {
//            boolean increment = pinInput.ksnIncrease(AppConfig.Pin.DUKPT_DES_INDEX);
//            Logger.v("response", "increment:" + increment);
//        }
        SimpleTransferListener.isScreenAvailable = 0;
        super.onDestroy();
        CountDownResponseTimer.cancelTimer(204);
        Utils.setNullDialoge();
        Utils.clearData();
        SDKDevice.getInstance(this).incrementKSN();
        viewModel.onDestroy();
        freeMemory();
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
        }
    }

    @Override
    public void onBackPressed() {
        Logger.v("Back_button_printactivity");
        if (!disableBackBtn) {
            if (doubleBackToExitPressedOnce) {
                if (viewModel.forceClose) {
                    MapperFlow.getInstance().moveToLandingPage(this, true, 9);
                    new SdkSupport(context).closeCardReader();
                } else {
                    SimpleTransferListener.getInstance(context).stopEMVFlow();
                    SimpleTransferListener.getInstance(context).stopEMVProcessThread();
                    SimpleTransferListener.isScreenAvailable = 2;
                    Utils.alertDialogOneShow(context, getString(R.string.cancel_wait));
                    return;
                }
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, getString(R.string.plz_click_back_to_cancel_txn), Toast.LENGTH_SHORT).show();

            mHandler.postDelayed(mRunnable, 2000);
        }
    }

    public void showCard(final String indicator, final String card) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (card != null && card.trim().length() != 0) {
                    AppConfig.EMV.card_name = card;
                    card_name_show.setText(card);
                } else {
                    card_name_show.setText(Utils.getCardName(indicator));
                    AppConfig.EMV.card_name = Utils.getCardName(indicator);
                }
            }
        });
    }

    public boolean checkCondition() {
        int responce = AppConfig.EMV.consumeType;
        new LightsDisplay(context).hideSingleLight();
        new LightsDisplay(context).hideTwoLight();
        switch (responce) {
            case 1:
                if (!isChipEnabled)
                    showAlert();
                return isChipEnabled;
            case 2:
                if (!isRFEnabled)
                    showAlert();
                else
                    new LightsDisplay(context).showTwoLights();
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
                Utils.showListnerMsg(PrintActivity.this);
            }
        });
    }

}
