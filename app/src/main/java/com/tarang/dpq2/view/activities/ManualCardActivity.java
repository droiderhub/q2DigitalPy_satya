package com.tarang.dpq2.view.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.tarang.dpq2.BuildConfig;
import com.tarang.dpq2.R;
import com.tarang.dpq2.base.AppInit;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.MapperFlow;
import com.tarang.dpq2.base.baseactivities.BaseActivity;
import com.tarang.dpq2.base.jpos_class.ConstantApp;
import com.tarang.dpq2.base.utilities.Utils;

import java.util.Calendar;



public class ManualCardActivity extends BaseActivity implements View.OnClickListener, ActionMode.Callback {

    EditText etd_account_number;
    EditText etd_expdate;
    private int beforeLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_manual_card);
        Logger.v("app_version---"+ BuildConfig.VERSION_NAME);
        Logger.v("mada_version---"+ AppInit.VERSION_6_0_5);
        etd_account_number = findViewById(R.id.etd_account_number);
        etd_expdate = findViewById(R.id.etd_expdate);
        etd_expdate.addTextChangedListener(watcher1);
        etd_account_number.addTextChangedListener(watcher);
        etd_account_number.setCustomSelectionActionModeCallback(this);
        etd_expdate.setCustomSelectionActionModeCallback(this);
        initData();
        setTitle(getCurrentMenu().getMenu_name());
        findViewById(R.id.btn_submit).setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        String dateFormat = etd_expdate.getText().toString();
        String accNo = etd_account_number.getText().toString().replaceAll(" ", "").trim();
        if (accNo.trim().length() != 0 && accNo.trim().length() > 11 && accNo.trim().length() <= 19) {
            if (dateFormat.trim().length() == 5) {
                String[] date = dateFormat.split("/");
                if (date.length == 2) {
                    int mm;
                    int yy;
                    if(Utils.checkArabicLanguage(this)){
                        mm = Integer.parseInt(date[1]);
                        yy = Integer.parseInt(date[0]);
                    }else {
                        mm = Integer.parseInt(date[0]);
                        yy = Integer.parseInt(date[1]);
                    }
                    int YYYY = Calendar.getInstance().get(Calendar.YEAR);
                    String YY = Integer.toString(YYYY).substring(2);
                    int year = Integer.parseInt(YY);
                    int month = Calendar.getInstance().get(Calendar.MONTH)+1;
                    int dd = Calendar.getInstance().get(Calendar.DATE);

                    Logger.v("manual_transaction_system_date----mm="+month+"----yy=="+year+"---date==="+dd);
                    if (mm > 0 && month <= mm && year <= yy) {
                        if (getCurrentMenu().getMenu_tag().equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_MANUAL))
                            MapperFlow.getInstance().moveToEnterRrnDateAmountActivity(context, accNo, date[1] + date[0],getCurrentMenu().getMenu_tag());
                        else
                            MapperFlow.getInstance().moveToPrintScreen(context, accNo, date[1] + date[0]);
                    } else
                        Toast.makeText(context, getString(R.string.enter_valid_date), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(context, getString(R.string.enter_valid_date), Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(context, getString(R.string.enter_valid_date), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, getString(R.string.enter_valid_account_number), Toast.LENGTH_SHORT).show();
        }

    }

    String beforeText = "";
    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            beforeText = s.toString();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String accNo = etd_account_number.getText().toString().replaceAll(" ", "").trim();
            if (s.toString().contains("\\."))
                accNo = accNo.replaceAll("\\.", "");
            String paddedAccNo = "";
            for (int i = 0; i < accNo.length(); i++) {
                if ((i + 1) % 4 == 0) {
                    paddedAccNo = paddedAccNo + accNo.charAt(i) + " ";
                } else {
                    paddedAccNo = paddedAccNo + accNo.charAt(i);
                }
            }
            etd_account_number.removeTextChangedListener(watcher);
            etd_account_number.setText(paddedAccNo);
            etd_account_number.setSelection(etd_account_number.getText().toString().trim().length());
            etd_account_number.addTextChangedListener(watcher);
        }
    };
    TextWatcher watcher1 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            beforeLength = s.toString().trim().length();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String txt = etd_expdate.getText().toString();
            if (txt.trim().length() != 0) {
                if (beforeLength < txt.trim().length()) {
                    if (txt.trim().length() == 2) {
                        etd_expdate.removeTextChangedListener(watcher1);
                        etd_expdate.setText(txt + "/");
                        etd_expdate.setSelection(etd_expdate.getText().toString().trim().length());
                        etd_expdate.addTextChangedListener(watcher1);
                    }
                } else
                    etd_expdate.setText("");
            }
        }
    };

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
    @Override
    public void onBackPressed() {
        MapperFlow.getInstance().moveToLandingPage(ManualCardActivity.this,false,11);
    }
}
