package com.tarang.dpq2.view.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tarang.dpq2.R;
import com.tarang.dpq2.base.baseactivities.BaseActivity;
import com.tarang.dpq2.base.terminal_sdk.utils.LuhnAlgorithm;
import com.tarang.dpq2.base.utilities.Utils;

public class ValidateCardActivity extends BaseActivity {

    Button validate_btn;
    EditText card_number_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_card);
        setTitle(getString(R.string.manager_menu));
        initViews();
    }

    private void initViews() {
        validate_btn = findViewById(R.id.validate_btn);
        card_number_et = findViewById(R.id.card_number_et);

        validate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               String str_cardNumber = card_number_et.getText().toString().trim();
               String errMsg = null;

                  if (str_cardNumber.equals("") || str_cardNumber==null) {
                    errMsg = getString(R.string.enter_card_number);
                } else if (str_cardNumber.length() < 12) {
                    errMsg =getString(R.string.enter_card_number);
                } else if (!(new LuhnAlgorithm().validate(str_cardNumber,null))) {
                    errMsg = getString(R.string.enter_card_number);
                }else {
                      errMsg = getString(R.string.success);
                  }
                    if (errMsg!=null)
                Utils.alertDialogShow(ValidateCardActivity.this, errMsg, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.dismissDialoge();
                    }
                });
            }
        });

    }
}
