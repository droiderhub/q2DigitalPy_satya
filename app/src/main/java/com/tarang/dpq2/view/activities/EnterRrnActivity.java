package com.tarang.dpq2.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tarang.dpq2.R;
import com.tarang.dpq2.base.baseactivities.BaseActivity;
import com.tarang.dpq2.base.jpos_class.ConstantApp;

public class EnterRrnActivity extends BaseActivity implements ConstantApp, View.OnClickListener {

    TextView mTitleTextView, mSubmit;
    EditText mEnterNumber;
    String tag = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_enter_rrn);
        init();
        setTitle(getCurrentMenu().getMenu_name());
        tag = getCurrentMenu().getMenu_tag();
        setTextView();
    }

    private void init() {
        mTitleTextView = (TextView) findViewById(R.id.textview_title);
        mEnterNumber = (EditText) findViewById(R.id.enterNumber);
        mSubmit = (TextView) findViewById(R.id.rrn_submit_btn);

        mSubmit.setOnClickListener(this);
    }

    private void setTextView() {
        if (tag.equalsIgnoreCase(REFUND)) {
            mTitleTextView.setText(R.string.rrn_number);
            mEnterNumber.setHint(R.string.enter_rrn_number);
        }
        if (tag.equalsIgnoreCase(PURCHASE_ADVICE)) {
            mTitleTextView.setText(R.string.auth_code);
            mEnterNumber.setHint(R.string.enter_auth_code);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rrn_submit_btn:
                Intent intent = new Intent(this, EnterAmountActivity.class);
                intent.putExtra("rrnValue",mEnterNumber.getText().toString().trim());
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
