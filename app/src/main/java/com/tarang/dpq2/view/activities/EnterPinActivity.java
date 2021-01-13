package com.tarang.dpq2.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import com.cloudpos.AlgorithmConstants;
import com.cloudpos.DeviceException;
import com.cloudpos.OperationResult;
import com.cloudpos.TimeConstants;
import com.cloudpos.pinpad.KeyInfo;
import com.cloudpos.pinpad.PINPadDevice;
import com.cloudpos.pinpad.PINPadOperationResult;
import com.cloudpos.pinpad.extend.PINPadExtendDevice;
import com.tarang.dpq2.R;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.baseactivities.BaseActivity;
import com.tarang.dpq2.base.jpos_class.ByteConversionUtils;
import com.tarang.dpq2.base.jpos_class.ConstantApp;
import com.tarang.dpq2.base.terminal_sdk.AppConfig;
import com.tarang.dpq2.base.terminal_sdk.device.SDKDevice;
import com.tarang.dpq2.base.terminal_sdk.event.SimpleTransferListener;
import com.tarang.dpq2.base.terminal_sdk.utils.SoundPoolImpl;
import com.tarang.dpq2.base.utilities.Utils;
import com.wizarpos.emvsample.constant.Constant;


public class EnterPinActivity extends BaseActivity implements Constant {

    private final int PINPAD_CANCEL = -65792;
    private final int PINPAD_TIMEOUT = -65538;

    private static final String TAG = "response";
    private TextView txtPassword;
    private TextView card_name_show;
    private StringBuffer buffer;
    private int inputLen = 0;
    private SoundPoolImpl spi;
    private CountDownTimer timer;
    boolean isCancelled = false;
    TextView back_tv;
    private boolean isPinCancelled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isCancelled = false;
        Logger.v("Pin Screen - Online");
        setContentView(R.layout.activity_enter_pin);
        setTitle(getCurrentMenu().getMenu_name());

        back_tv = findViewById(R.id.back_tv);
        back_tv.setVisibility(View.GONE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        if (getCurrentMenu().getMenu_tag().equalsIgnoreCase(ConstantApp.PURCHASE_NAQD))
            setUpCashBackView();

        SimpleTransferListener.pinBlock = null;
        spi = SoundPoolImpl.getInstance();
        spi.initLoad(this);
//        Logger.v("AppConfig.EMV.consumeType --" + AppConfig.EMV.consumeType);
//        if (AppConfig.EMV.consumeType != 0) {
//            startTimerRemoveCard();
//        }
        init();
    }

    private void setUpCashBackView() {
        TextView txt_purchasr_cash = findViewById(R.id.txt_purchasr_cash);
        TextView txt_cashback = findViewById(R.id.txt_cashback);
        txt_purchasr_cash.setText(context.getString(R.string.purchase_amount) + "  " + Utils.formatAmount(this, (AppConfig.EMV.amountValue - AppConfig.EMV.amtCashBack)));
        txt_cashback.setText(context.getString(R.string.naqd_amount_) + "       " + Utils.formatAmount(this, AppConfig.EMV.amtCashBack));
    }

    @Override
    protected void onResume() {
        showTimer = false;
        super.onResume();
    }

    private void init() {
        txtPassword = (TextView) findViewById(R.id.txt_password);
        card_name_show = (TextView) findViewById(R.id.card_name_show);
        TextView txt_account_no = findViewById(R.id.txt_account_no);
        txt_account_no.setText(Utils.addAccountNumber(AppConfig.EMV.icCardNum));
        card_name_show.setText(Utils.getCardName(AppConfig.EMV.card_name));
        TextView txt_amount = findViewById(R.id.txt_amount);
        txt_amount.setText(" " + Utils.formatAmountWithoutSAR(this, AppConfig.EMV.amountValue));
        final String accNo = getIntent().getStringExtra("accNo");
        Logger.v("response", "Encripted pin --accNo---" + accNo);
        loadPinPad();
    }

    private void loadPinPad() {
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
                    Logger.v("pinBlock --" + new String(pinBlock));
                    Logger.v("pinBlock --" + ByteConversionUtils.byteArrayToHexString(pinBlock));
                    setResult(pinBlock);

                } else {
                    isPinCancelled = true;
                    setResult();
                }
            } else if (operationResult.getResultCode() == OperationResult.SUCCESS) {
                Logger.v("Pin Bypass");
                setResult(null);
            } else {
                isPinCancelled = true;
                Logger.v("waitForPinBlock fail,");
                setResult();
            }
        } catch (DeviceException e) {
            e.printStackTrace();
        }
    }

    private void setResult(byte[] pinBlock) {
        Intent i = new Intent();
        i.putExtra("pin", pinBlock);
        setResult(RESULT_OK, i);
        finish();
    }

    private void setResult() {
        Intent i = new Intent();
//        i.putExtra("pin", pinBlock);
        setResult(RESULT_CANCELED, i);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.v("On Destroyed Pin");
        isCancelled = true;
        spi.release();
        if (timer != null) {
            timer.cancel();
            timer.onFinish();
        }
//        freeMemory();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
