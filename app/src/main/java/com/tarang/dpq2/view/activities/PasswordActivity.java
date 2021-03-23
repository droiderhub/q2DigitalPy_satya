package com.tarang.dpq2.view.activities;

import android.app.Service;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.tarang.dpq2.R;
import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.MapperFlow;
import com.tarang.dpq2.base.baseactivities.BaseActivity;
import com.tarang.dpq2.base.jpos_class.ConstantApp;
import com.tarang.dpq2.base.utilities.Utils;

import static com.tarang.dpq2.base.jpos_class.ConstantApp.REFUND;


public class PasswordActivity extends BaseActivity implements ConstantApp, TextWatcher, TextView.OnEditorActionListener {

    private TextView pin_1;
    private TextView pin_2;
    private TextView pin_3;
    private TextView pin_4;
    private TextView pin_5;
    private TextView pin_6;
    private TextView pin_7;
    private TextView pin_8;
    private TextView pin_9;
    private EditText mPinHiddenEditText;
    TextView password_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        setPasswordTitle();
        init();
        setPINListeners();
        if (getIntent().hasExtra(TAG)) {
            String tag = getIntent().getStringExtra(TAG);
            if (tag.equalsIgnoreCase(REFUND)) {
                password_tv.setText(getString(R.string.please_enter_merchant_password));
            }
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    /**
     * Initialize EditText fields.
     */
    private void init() {
        password_tv = findViewById(R.id.password_tv);
        pin_1 = (TextView) findViewById(R.id.pin_1);
        pin_2 = (TextView) findViewById(R.id.pin_2);
        pin_3 = (TextView) findViewById(R.id.pin_3);
        pin_4 = (TextView) findViewById(R.id.pin_4);
        pin_5 = (TextView) findViewById(R.id.pin_5);
        pin_6 = (TextView) findViewById(R.id.pin_6);
        pin_7 = (TextView) findViewById(R.id.pin_7);
        pin_8 = (TextView) findViewById(R.id.pin_8);
        pin_9 = (TextView) findViewById(R.id.pin_9);
        mPinHiddenEditText = (EditText) findViewById(R.id.pin_hidden_edittext);

//        mPinHiddenEditText.setCustomSelectionActionModeCallback(this);
    }

    /**
     * Hides soft keyboard.
     *
     * @param editText EditText which has focus
     */
    public void hideSoftKeyboard(EditText editText) {
        if (editText == null)
            return;

        InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    @Override
    public void onTextChanged(CharSequence s1, int start, int before, int count) {
        Logger.v("mPinHiddenEditText new-" + mPinHiddenEditText.getText().toString());
        String s = mPinHiddenEditText.getText().toString();
        if (s.length() == 0) {
            pin_1.setText("");
            pin_2.setText("");
            pin_3.setText("");
            pin_4.setText("");
            pin_5.setText("");
            pin_6.setText("");
            pin_7.setText("");
            pin_8.setText("");
            pin_9.setText("");
        } else if (s.length() == 1) {
            pin_1.setText("*");
            pin_2.setText("");
            pin_3.setText("");
            pin_4.setText("");
            pin_5.setText("");
            pin_6.setText("");
            pin_7.setText("");
            pin_8.setText("");
            pin_9.setText("");
        } else if (s.length() == 2) {
            pin_2.setText("*");
            pin_3.setText("");
            pin_4.setText("");
            pin_5.setText("");
            pin_6.setText("");
            pin_7.setText("");
            pin_8.setText("");
            pin_9.setText("");
        } else if (s.length() == 3) {
            pin_3.setText("*");
            pin_4.setText("");
            pin_5.setText("");
            pin_6.setText("");
            pin_7.setText("");
            pin_8.setText("");
            pin_9.setText("");
        } else if (s.length() == 4) {
            pin_4.setText("*");
            pin_5.setText("");
            pin_6.setText("");
            pin_7.setText("");
            pin_8.setText("");
            pin_9.setText("");
        } else if (s.length() == 5) {
            pin_5.setText("*");
            pin_6.setText("");
            pin_7.setText("");
            pin_8.setText("");
            pin_9.setText("");
        } else if (s.length() == 6) {
            pin_6.setText("*");
            pin_7.setText("");
            pin_8.setText("");
            pin_9.setText("");
        } else if (s.length() == 7) {
            pin_7.setText("*");
            pin_8.setText("");
            pin_9.setText("");
        } else if (s.length() == 8) {
            pin_8.setText("*");
            pin_9.setText("");
        } else if (s.length() == 9) {
            pin_9.setText("*");
        }
    }

    /**
     * Sets default PIN background.
     *
     * @param editText edit text to change
     */
    private void setDefaultPinBackground(EditText editText) {
        //setViewBackground(editText, getResources().getDrawable(R.drawable.textfield_default_holo_light));
    }

    /**
     * Sets focused PIN field background.
     *
     * @param editText edit text to change
     */
    private void setFocusedPinBackground(EditText editText) {
        //  setViewBackground(editText, getResources().getDrawable(R.drawable.textfield_focused_holo_light));
    }

    /**
     * Sets background of the view.
     * This method varies in implementation depending on Android SDK version.
     *
     * @param view       View to which set background
     * @param background Background to set to view
     */
    @SuppressWarnings("deprecation")
    public void setViewBackground(View view, Drawable background) {
        if (view == null || background == null)
            return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(background);
        } else {
            view.setBackgroundDrawable(background);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
//        Logger.v("Text changed complete");
//        setFocus(mPinHiddenEditText);
//        showSoftKeyboard(mPinHiddenEditText);
    }

//    @Override
//    public void onFocusChange(View v, boolean hasFocus) {
//        final int id = v.getId();
//        if (hasFocus) {
//            setFocus(mPinHiddenEditText);
//            showSoftKeyboard(mPinHiddenEditText);
//        }
//        switch (id) {
//            case R.id.pin_9:
//                if (hasFocus) {
//                    setFocus(mPinHiddenEditText);
//                    showSoftKeyboard(mPinHiddenEditText);
//                }
//                break;
//
//            case R.id.pin_8:
//                if (hasFocus) {
//                    setFocus(mPinHiddenEditText);
//                    showSoftKeyboard(mPinHiddenEditText);
//                }
//                break;
//
//            case R.id.pin_7:
//                if (hasFocus) {
//                    setFocus(mPinHiddenEditText);
//                    showSoftKeyboard(mPinHiddenEditText);
//                }
//                break;
//            case R.id.pin_first_edittext:
//                if (hasFocus) {
//                    setFocus(mPinHiddenEditText);
//                    showSoftKeyboard(mPinHiddenEditText);
//                }
//                break;
//
//            case R.id.pin_second_edittext:
//                if (hasFocus) {
//                    setFocus(mPinHiddenEditText);
//                    showSoftKeyboard(mPinHiddenEditText);
//                }
//                break;
//
//            case R.id.pin_third_edittext:
//                if (hasFocus) {
//                    setFocus(mPinHiddenEditText);
//                    showSoftKeyboard(mPinHiddenEditText);
//                }
//                break;
//
//            case R.id.pin_forth_edittext:
//                if (hasFocus) {
//                    setFocus(mPinHiddenEditText);
//                    showSoftKeyboard(mPinHiddenEditText);
//                }
//                break;
//
//            case R.id.pin_fifth_edittext:
//                if (hasFocus) {
//                    setFocus(mPinHiddenEditText);
//                    showSoftKeyboard(mPinHiddenEditText);
//                }
//                break;
//            case R.id.pin_sixth_edittext:
//                if (hasFocus) {
//                    setFocus(mPinHiddenEditText);
//                    showSoftKeyboard(mPinHiddenEditText);
//                }
//                break;
//            default:
//                break;
//        }
//    }

    /**
     * Sets focus on a specific EditText field.
     *
     * @param editText EditText to set focus on
     */
    public static void setFocus(EditText editText) {
        if (editText == null)
            return;

        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
    }

    /**
     * Sets listeners for EditText fields.
     */
    private void setPINListeners() {
        mPinHiddenEditText.addTextChangedListener(this);

//        pin_1.setOnFocusChangeListener(this);
//        pin_2.setOnFocusChangeListener(this);
//        pin_3.setOnFocusChangeListener(this);
//        pin_4.setOnFocusChangeListener(this);
//        pin_5.setOnFocusChangeListener(this);
//        pin_6.setOnFocusChangeListener(this);
//        pin_7.setOnFocusChangeListener(this);
//        pin_8.setOnFocusChangeListener(this);
//        pin_9.setOnFocusChangeListener(this);

//        pin_1.setOnKeyListener(this);
//        pin_2.setOnKeyListener(this);
//        pin_3.setOnKeyListener(this);
//        pin_4.setOnKeyListener(this);
//        pin_5.setOnKeyListener(this);
//        pin_6.setOnKeyListener(this);
//        pin_7.setOnKeyListener(this);
//        pin_8.setOnKeyListener(this);
//        pin_9.setOnKeyListener(this);
//        mPinHiddenEditText.setOnKeyListener(this);

        mPinHiddenEditText.setOnEditorActionListener(this);

        mPinHiddenEditText.setAccessibilityDelegate(new View.AccessibilityDelegate() {
            @Override
            public void sendAccessibilityEvent(View host, int eventType) {
                super.sendAccessibilityEvent(host, eventType);
                Logger.v("Called");
                if (eventType == AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED){
                    mPinHiddenEditText.setSelection(mPinHiddenEditText.getText().toString().length());
                }
            }
        });

        mPinHiddenEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    //this is for backspace
                    String s = mPinHiddenEditText.getText().toString();
                    Logger.v("Paswd "+ s);
                    if (s.length() == 0) {
                        pin_1.setText("");
                        pin_2.setText("");
                        pin_3.setText("");
                        pin_4.setText("");
                        pin_5.setText("");
                        pin_6.setText("");
                        pin_7.setText("");
                        pin_8.setText("");
                        pin_9.setText("");
                    } else if (s.length() == 1) {
                        pin_1.setText("*");
                        pin_2.setText("");
                        pin_3.setText("");
                        pin_4.setText("");
                        pin_5.setText("");
                        pin_6.setText("");
                        pin_7.setText("");
                        pin_8.setText("");
                        pin_9.setText("");
                    } else if (s.length() == 2) {
                        pin_2.setText("*");
                        pin_3.setText("");
                        pin_4.setText("");
                        pin_5.setText("");
                        pin_6.setText("");
                        pin_7.setText("");
                        pin_8.setText("");
                        pin_9.setText("");
                    } else if (s.length() == 3) {
                        pin_3.setText("*");
                        pin_4.setText("");
                        pin_5.setText("");
                        pin_6.setText("");
                        pin_7.setText("");
                        pin_8.setText("");
                        pin_9.setText("");
                    } else if (s.length() == 4) {
                        pin_4.setText("*");
                        pin_5.setText("");
                        pin_6.setText("");
                        pin_7.setText("");
                        pin_8.setText("");
                        pin_9.setText("");
                    } else if (s.length() == 5) {
                        pin_5.setText("*");
                        pin_6.setText("");
                        pin_7.setText("");
                        pin_8.setText("");
                        pin_9.setText("");
                    } else if (s.length() == 6) {
                        pin_6.setText("*");
                        pin_7.setText("");
                        pin_8.setText("");
                        pin_9.setText("");
                    } else if (s.length() == 7) {
                        pin_7.setText("*");
                        pin_8.setText("");
                        pin_9.setText("");
                    } else if (s.length() == 8) {
                        pin_8.setText("*");
                        pin_9.setText("");
                    } else if (s.length() == 9) {
                        pin_9.setText("*");
                    }
                }
                return false;
            }
        });
    }

    /**
     * Shows soft keyboard.
     *
     * @param editText EditText which has focus
     */
    public void showSoftKeyboard(EditText editText) {
        if (editText == null)
            return;

        InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
    }

//    @Override
//    public boolean onKey(View v, int keyCode, KeyEvent event) {
//
//        if (event.getAction() == KeyEvent.ACTION_DOWN) {
//            final int id = v.getId();
//            switch (id) {
//                case R.id.pin_hidden_edittext:
//                    if (keyCode == KeyEvent.KEYCODE_DEL) {
//                        if (mPinHiddenEditText.getText().length() == 9)
//                            pin_9.setText("");
//                        else if (mPinHiddenEditText.getText().length() == 8)
//                            pin_8.setText("");
//                        else if (mPinHiddenEditText.getText().length() == 7)
//                            pin_7.setText("");
//                        else if (mPinHiddenEditText.getText().length() == 6)
//                            pin_6.setText("");
//                        else if (mPinHiddenEditText.getText().length() == 5)
//                            pin_5.setText("");
//                        else if (mPinHiddenEditText.getText().length() == 4)
//                            pin_4.setText("");
//                        else if (mPinHiddenEditText.getText().length() == 3)
//                            pin_3.setText("");
//                        else if (mPinHiddenEditText.getText().length() == 2)
//                            pin_2.setText("");
//                        else if (mPinHiddenEditText.getText().length() == 1)
//                            pin_1.setText("");
//
//                        if (mPinHiddenEditText.length() > 0)
//                            mPinHiddenEditText.setText(mPinHiddenEditText.getText().subSequence(0, mPinHiddenEditText.length() - 1));
//
//                        return true;
//                    }
//
//                    break;
//
//                default:
//                    return false;
//            }
//        }
//
//        return false;
//    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            String password = mPinHiddenEditText.getText().toString();
            if (password.length() != 9) {
                Toast.makeText(this, getString(R.string.please_enter_6_digit_password), Toast.LENGTH_SHORT).show();
            } else {
                moveNext(password);
            }
        }
        return false;
    }

    private void moveNext(String password) {
        if (getIntent().hasExtra(TAG)) {
            String tag = getIntent().getStringExtra(TAG);
            if (tag.equalsIgnoreCase(REFUND)) {
                if (password.equalsIgnoreCase(AppManager.getInstance().getMerchantPassword())) {
                    MapperFlow.getInstance().moveToEnterRrnDateAmountActivity(this, REFUND);
                } else {
                    Toast.makeText(context, "Invalid Password", Toast.LENGTH_SHORT).show();
                    removeAllText();
                }
            } else if (tag.equalsIgnoreCase("ADMIN")) {
                if (password.equalsIgnoreCase(AppManager.getInstance().getAdminPassword())) {
                    MapperFlow.getInstance().moveToAdminMenu(context);
                } else {
                    Toast.makeText(context, "Invalid Password", Toast.LENGTH_SHORT).show();
                    removeAllText();
                }
            } else if (tag.equalsIgnoreCase("RECON")) {
                if (password.equalsIgnoreCase(AppManager.getInstance().getAdminPassword())) {
                    MapperFlow.getInstance().moveToReconsilation(context);
                } else {
                    Toast.makeText(context, "Invalid Password", Toast.LENGTH_SHORT).show();
                    removeAllText();
                }
            } else {
                if (password.equalsIgnoreCase(AppManager.getInstance().getMerchantPassword())) {
                    MapperFlow.getInstance().moveToMerchantMenu(context);
                } else {
                    Toast.makeText(context, "Invalid Password", Toast.LENGTH_SHORT).show();
                    removeAllText();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        removeAllText();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utils.hideDialoge1();
    }

    private void removeAllText() {
        pin_1.setText("");
        pin_2.setText("");
        pin_3.setText("");
        pin_4.setText("");
        pin_5.setText("");
        pin_6.setText("");
        pin_7.setText("");
        pin_8.setText("");
        pin_9.setText("");
        mPinHiddenEditText.setText("");
    }

    public void setPasswordTitle() {
        if (getIntent().hasExtra(TAG)) {
            String tag = getIntent().getStringExtra(TAG);
            if (tag.equalsIgnoreCase(REFUND)) {
                setTitle(getCurrentMenu().getMenu_name());
            } else if (tag.equalsIgnoreCase("MERCHANT")) {
                setTitle(getResources().getString(R.string.merchant_menu));
            } else {
                setTitle(getResources().getString(R.string.admin_menu));
            }
        }
    }
}
