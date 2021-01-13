
package com.tarang.dpq2.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tarang.dpq2.BuildConfig;
import com.tarang.dpq2.R;
import com.tarang.dpq2.base.AppInit;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.MapperFlow;
import com.tarang.dpq2.base.baseactivities.BaseActivity;
import com.tarang.dpq2.base.jpos_class.ConstantApp;
import com.tarang.dpq2.base.terminal_sdk.AppConfig;
import com.tarang.dpq2.base.utilities.CurrencyEditText;
import com.tarang.dpq2.base.utilities.Utils;

public class EnterAmountActivity extends BaseActivity implements TextView.OnEditorActionListener, ConstantApp, ActionMode.Callback {

    CurrencyEditText etd_number;
    String current = "";
    TextView textView_amount;
    Button pay_button;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amount);
        Logger.v("app_version---"+ BuildConfig.VERSION_NAME);
        Logger.v("mada_version---"+ AppInit.VERSION_6_0_5);
        initControlWithoutKeyPad();
        setTitle(getCurrentMenu());
        initData();
        init();
        toast = Toast.makeText(context, getString(R.string.please_enter_amount), Toast.LENGTH_SHORT);
        setTextView();
//        etd_number.addTextChangedListener(this);
        etd_number.setOnEditorActionListener(this);
    }

    private void init() {
        etd_number = findViewById(R.id.etd_number);
        etd_number.setSelection(etd_number.getText().toString().trim().length());
        etd_number.setCustomSelectionActionModeCallback(this);
        textView_amount =  findViewById(R.id.textview_amount);
        pay_button = findViewById(R.id.pay_button);

        pay_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPerform();
            }
        });
    }

    private void doPerform() {
        String amount = etd_number.getText().toString();
        if (!amount.equalsIgnoreCase("") && amount.replaceAll(",", "").trim().length() != 0 && amount.replaceAll("\\.", "").trim().length() != 0) {
            Double amountValue = Double.valueOf(amount.replaceAll(",", ""));
            Logger.v("AMOUNT -VALL-"+amount);
            if (amountValue > 0) {
                AppConfig.EMV.amtCashBack =0;
                AppConfig.EMV.amountValue = (amountValue);
                Logger.v("amountValue----"+ AppConfig.EMV.amountValue);
                Logger.v("amtCashBack----"+ AppConfig.EMV.amtCashBack);
                if (getIntent().hasExtra(TAG_CARD)) {
                    AppConfig.EMV.amountValue = (amountValue);
//                    CommonCardType card = (CommonCardType) getIntent().getSerializableExtra(TAG_CARD);
                    MapperFlow.getInstance().moveToPrintScreen(context);
                } else if (getIntent().hasExtra(TAG)) {
                    String tag = getIntent().getStringExtra(TAG);
                    if (tag.equalsIgnoreCase(PURCHASE_NAQD)) {
                        Intent intent = new Intent(this, EnterAmountActivity.class);
                        intent.putExtra(TAG, Cashback_Amount);
                        intent.putExtra(Purchase_Amount, amountValue);
                        AppConfig.EMV.amountValue = (amountValue);
                        startActivity(intent);
                        finish();
                    } else if (tag.equalsIgnoreCase(Cashback_Amount)) {
                        Double purchaseAmount = getIntent().getDoubleExtra(Purchase_Amount, 0);
                        Logger.v("purchase -" + purchaseAmount);
                        Double totalAmount = purchaseAmount + amountValue;
                        Logger.v("purchase -" + totalAmount);
                        AppConfig.EMV.amtCashBack = (amountValue);
                        AppConfig.EMV.amountValue = (totalAmount);
                        Logger.v("amountValue1----"+ AppConfig.EMV.amountValue);
                        Logger.v("amtCashBack1----"+ AppConfig.EMV.amtCashBack);
                        Intent intent = new Intent(this, TransactionActivity.class);
                        intent.putExtra(Total_Amount, totalAmount);
                        startActivity(intent);
                        finish();
                    } else if (tag.equalsIgnoreCase(CASH_ADVANCE)) {
                        Intent intent = new Intent(this, TransactionActivity.class);
                        intent.putExtra(Total_Amount, amountValue);
                        AppConfig.EMV.amountValue = (amountValue);
                        startActivity(intent);
                        finish();
                    }else {
                        Intent intent = new Intent(this, TransactionActivity.class);
                        intent.putExtra(Total_Amount, amountValue);
                        AppConfig.EMV.amountValue = (amountValue);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Intent intent = new Intent(this, TransactionActivity.class);
                    intent.putExtra(Total_Amount, amountValue);
                    AppConfig.EMV.amountValue = (amountValue);
                    startActivity(intent);
                    finish();
                }
            } else {
                toast.show();
            }
        } else {
            toast.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utils.hideDialoge1();
        toast.cancel();
    }

    private void setTextView() {
        String tag = getCurrentMenu().getMenu_tag();
        if (getIntent().hasExtra(TAG) && getIntent().getStringExtra(TAG).equalsIgnoreCase(Cashback_Amount)) {
            textView_amount.setText(R.string.cashback_amount);
        }else if (tag.equalsIgnoreCase(PURCHASE)) {
            textView_amount.setText(R.string.purchase_amount);
        } else if (tag.equalsIgnoreCase(PURCHASE_NAQD)) {
            textView_amount.setText(R.string.purchase_amount);
        } else if (tag.equalsIgnoreCase(Cashback_Amount)) {
            textView_amount.setText(R.string.cashback_amount);
        } else if (tag.equalsIgnoreCase(CASH_ADVANCE)) {
            textView_amount.setText(R.string.cash_advance);
        } else if (tag.equalsIgnoreCase(PRE_AUTHORISATION)) {
            textView_amount.setText(R.string.preauth_amount);
        } else if (tag.equalsIgnoreCase(PRE_AUTHORISATION_VOID)) {
            textView_amount.setText(R.string.preauth_void_amt);
        } else if (tag.equalsIgnoreCase(PRE_AUTHORISATION_EXTENSION)) {
            textView_amount.setText(R.string.preauth_extension_amt);
        } else {
            textView_amount.setText(getString(R.string.amount));
        }
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            doPerform();
        }
        return false;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }
}
