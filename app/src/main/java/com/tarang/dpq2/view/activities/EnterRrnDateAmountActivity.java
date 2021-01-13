package com.tarang.dpq2.view.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


import com.tarang.dpq2.R;
import com.tarang.dpq2.base.AppInit;
import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.baseactivities.BaseActivity;
import com.tarang.dpq2.base.jpos_class.ConstantApp;
import com.tarang.dpq2.base.jpos_class.ConstantAppValue;
import com.tarang.dpq2.base.terminal_sdk.AppConfig;
import com.tarang.dpq2.base.utilities.CurrencyEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;



public class EnterRrnDateAmountActivity extends BaseActivity implements TextView.OnEditorActionListener, ConstantApp, ActionMode.Callback {

    CurrencyEditText etd_number;
    TextView proceed_btn;
    String current = "";
    EditText enterRRNNumber;
    EditText etd_day;
    EditText etd_month;
    EditText etd_year;
    EditText aproval_code_et;
    RadioButton rbt_purchase_advice;
    LinearLayout date_tv;
    LinearLayout approval_code_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_purchase_advice);
        initControlWithoutKeyPad();
        setTitle(getCurrentMenu().getMenu_name());
        init();
    }
    final String[] dateString = new String[1];
    private void init() {
        etd_number = findViewById(R.id.etd_number);
        etd_number.setSelection(etd_number.getText().toString().trim().length());
        proceed_btn = findViewById(R.id.proceed_btn);
        enterRRNNumber = findViewById(R.id.enterRRNNumber);
        rbt_purchase_advice = findViewById(R.id.rbt_purchase_advice);
        etd_day = findViewById(R.id.etd_day);
        etd_month = findViewById(R.id.etd_month);
        etd_year = findViewById(R.id.etd_year);
        date_tv = findViewById(R.id.date_tv);
        aproval_code_et = findViewById(R.id.aproval_code_et);
        approval_code_layout = findViewById(R.id.approval_code_layout);
        if (getCurrentMenu().getMenu_tag().equalsIgnoreCase(REFUND) && !AppInit.VERSION_6_0_5){
            approval_code_layout.setVisibility(View.GONE);
            findViewById(R.id.rbt_group).setVisibility(View.VISIBLE);
            rbt_purchase_advice.setChecked(true);
        }else
            findViewById(R.id.rbt_group).setVisibility(View.GONE);

        if (!getCurrentMenu().getMenu_tag().equalsIgnoreCase(PRE_AUTHORISATION_EXTENSION)){
            findViewById(R.id.ll_amount).setVisibility(View.VISIBLE);
        }else
        {
            etd_number.setEnabled(false);
        }



        etd_number.setOnEditorActionListener(this);
        enterRRNNumber.setCustomSelectionActionModeCallback(this);
        aproval_code_et.setCustomSelectionActionModeCallback(this);
        etd_number.setCustomSelectionActionModeCallback(this);
        date_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear, mMonth, mDay;
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(EnterRrnDateAmountActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                c.set(year, monthOfYear, dayOfMonth);
                                SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
                                dateString[0] = sdf.format(c.getTime());
                                etd_year.setText(""+year);
                                etd_month.setText(addZeros(monthOfYear+1));
                                etd_day.setText(addZeros(dayOfMonth));
                                AppManager.getInstance().setPurchaseAdviceDate(dateString[0]);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()+ (1000 * 60 * 60));
                datePickerDialog.show();
            }

        });
        proceed_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceedFlow();
            }
        });
    }

    private void proceedFlow() {
        if(enterRRNNumber.getText().toString().trim().length() == 12) {
            if(dateString[0] != null && dateString[0].trim().length() != 0) {
                String amount = etd_number.getText().toString();
                AppManager.getInstance().setDe37(enterRRNNumber.getText().toString().trim());
                AppManager.getInstance().setPurchaseAdviceDate(dateString[0]);
                if (!amount.equalsIgnoreCase("") && amount.replaceAll(",", "").trim().length() != 0 && amount.replaceAll("\\.", "").trim().length() != 0) {
                    Double amountValue = Double.valueOf(amount.replaceAll(",", ""));
                    if (amountValue > 0 || getCurrentMenu().getMenu_tag().equalsIgnoreCase(PRE_AUTHORISATION_EXTENSION)) {
                        if(aproval_code_et.getText().toString().trim().length() == 6 || (approval_code_layout.getVisibility() == View.GONE)) {
                            if(approval_code_layout.getVisibility() == View.VISIBLE) {
                                AppManager.getInstance().setDe38(aproval_code_et.getText().toString());
                            }
                            if (getCurrentMenu().getMenu_name().equalsIgnoreCase(REFUND)){
                                AppManager.getInstance().setRefundMTI(!rbt_purchase_advice.isChecked());
                            }
                            AppManager.getInstance().setDe39(ConstantAppValue.REFER_TO_CARD_ISSUER_VALUE);
                            if(getCurrentMenu().getMenu_tag().equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_MANUAL)){
                                Intent intent = new Intent(EnterRrnDateAmountActivity.this, PrintActivity.class);
                                intent.putExtra(Total_Amount, amountValue);
                                AppConfig.EMV.amountValue = (amountValue);
                                AppConfig.EMV.amtCashBack = 0;
                                intent.putExtra(TAG_CARD_MANUAL, true);
                                intent.putExtra(TAG1, getIntent().getStringExtra(TAG1));
                                intent.putExtra(TAG2, getIntent().getStringExtra(TAG2));
                                startActivity(intent);
                                finish();
                            }else {
                                Intent intent = new Intent(EnterRrnDateAmountActivity.this, TransactionActivity.class);
                                intent.putExtra(Total_Amount, amountValue);
                                AppConfig.EMV.amountValue = (amountValue);
                                AppConfig.EMV.amtCashBack = 0;
                                startActivity(intent);
                                finish();
                            }
                        }else
                            Toast.makeText(context, getString(R.string.please_enter_valid_approval_code), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, getString(R.string.please_enter_amount), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, getString(R.string.please_enter_amount), Toast.LENGTH_SHORT).show();
                }
            }else
                Toast.makeText(context, getString(R.string.please_select_date), Toast.LENGTH_SHORT).show();
        }else
            Toast.makeText(context, getString(R.string.please_enter_valid_rrn), Toast.LENGTH_SHORT).show();
    }

    private String addZeros(int dayOfMonth) {
        if(dayOfMonth <= 9)
            return "0"+dayOfMonth;
        return ""+dayOfMonth;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            proceedFlow();
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
