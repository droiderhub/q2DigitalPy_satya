package com.tarang.dpq2.view.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
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
import com.tarang.dpq2.base.jpos_class.ByteConversionUtils;
import com.tarang.dpq2.base.jpos_class.ConstantApp;
import com.tarang.dpq2.base.utilities.Utils;
import com.tarang.dpq2.worker.SSLSocketFactoryExtended;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLSocket;

public class ActivationActivity extends BaseActivity implements ConstantApp, TextWatcher, TextView.OnEditorActionListener {

    private TextView pin_1;
    private TextView pin_2;
    private TextView pin_3;
    private TextView pin_4;
    private TextView pin_5;
    private TextView pin_6;
    private TextView pin_7;
    private TextView pin_8;

    private EditText mPinHiddenEditText;
    private boolean resultEnded = false;
    private String deviceInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation);
        setPasswordTitle();
        init();
        setPINListeners();
        boolean status = getIntent().hasExtra("SNNUMBER");
        deviceInfo = getIntent().getStringExtra("SNNUMBER");
        Logger.v("status -" + status + "--" + deviceInfo);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    /**
     * Initialize EditText fields.
     */
    private void init() {
        pin_1 = (TextView) findViewById(R.id.pin_1);
        pin_2 = (TextView) findViewById(R.id.pin_2);
        pin_3 = (TextView) findViewById(R.id.pin_3);
        pin_4 = (TextView) findViewById(R.id.pin_4);
        pin_5 = (TextView) findViewById(R.id.pin_5);
        pin_6 = (TextView) findViewById(R.id.pin_6);
        pin_7 = (TextView) findViewById(R.id.pin_7);
        pin_8 = (TextView) findViewById(R.id.pin_8);

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
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Logger.v("mPinHiddenEditText new-" + mPinHiddenEditText.getText().toString());
        if(s.length() == 0){
            pin_1.setText("");
            pin_2.setText("");
            pin_3.setText("");
            pin_4.setText("");
            pin_5.setText("");
            pin_6.setText("");
            pin_7.setText("");
            pin_8.setText("");

        }else if (s.length() == 1) {
            pin_1.setText("*");
            pin_2.setText("");
            pin_3.setText("");
            pin_4.setText("");
            pin_5.setText("");
            pin_6.setText("");
            pin_7.setText("");
            pin_8.setText("");

        } else if (s.length() == 2) {
            pin_2.setText("*");
            pin_3.setText("");
            pin_4.setText("");
            pin_5.setText("");
            pin_6.setText("");
            pin_7.setText("");
            pin_8.setText("");

        } else if (s.length() == 3) {
            pin_3.setText("*");
            pin_4.setText("");
            pin_5.setText("");
            pin_6.setText("");
            pin_7.setText("");
            pin_8.setText("");

        } else if (s.length() == 4) {
            pin_4.setText("*");
            pin_5.setText("");
            pin_6.setText("");
            pin_7.setText("");
            pin_8.setText("");

        } else if (s.length() == 5) {
            pin_5.setText("*");
            pin_6.setText("");
            pin_7.setText("");
            pin_8.setText("");

        } else if (s.length() == 6) {
            pin_6.setText("*");
            pin_7.setText("");
            pin_8.setText("");

        } else if (s.length() == 7) {
            pin_7.setText("*");
            pin_8.setText("");

        } else if (s.length() == 8) {
            pin_8.setText("*");

        } else if (s.length() == 9) {

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

    }


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


//        pin_1.setOnKeyListener(this);
//        pin_2.setOnKeyListener(this);
//        pin_3.setOnKeyListener(this);
//        pin_4.setOnKeyListener(this);
//        pin_5.setOnKeyListener(this);
//        pin_6.setOnKeyListener(this);
//        pin_7.setOnKeyListener(this);
//        pin_8.setOnKeyListener(this);

//        mPinHiddenEditText.setOnKeyListener(this);

        mPinHiddenEditText.setOnEditorActionListener(this);
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


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            String password = mPinHiddenEditText.getText().toString();
            if (password.length() != 8) {
                Toast.makeText(this, getString(R.string.please_enter_8_digit_password), Toast.LENGTH_SHORT).show();
            } else {
                try {
                    sendMessage(password);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Logger.v("UnSpoorted mssg");
                }
            }
        }
        return false;
    }

    private void moveNext(String finalCode, String finalMsg) {
        Logger.v("finalCode -" + finalCode);
        if (finalCode.equalsIgnoreCase("SUCC")) {
            MapperFlow.getInstance().moveToRegistration(context);
        }else{
            Utils.alertDialogShow(context, finalMsg, true);
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

        mPinHiddenEditText.setText("");
    }


    public void setPasswordTitle() {
//        setTitle("Activation code");
    }

    boolean forceCancel = false;
    Thread thread;

    private void sendMessage(String code) throws UnsupportedEncodingException {
        String ip = "";
        int port = 0;
        String activation = "";
        try{
            activation = AppManager.getInstance().getActivationURL();
            String[] ipPort = activation.split(":");
            ip = ipPort[0];
            port = Integer.parseInt(ipPort[1]);
        }catch (Exception e){
            Logger.v("Excp -"+e.getMessage());
        }
        if(activation.trim().length() == 0 ||(ip.trim().length() == 0) || port == 0){
            Utils.alertDialogShowBottom(context, "Activation address not configured");
            return;
        }
        Logger.v("Merchant portal ");
        Utils.alertDialogShowBottom(context, context.getString(R.string.processing));
        final CountDownTimer mPortalTimer = new CountDownTimer(15000, 1000) {

            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                Utils.dismissDialoge();
                if (forceCancel)
                    return;
                try {
                    thread.stop();
                } catch (Exception e) {

                }
                moveNextFinish();
            }
        };
        final InputStream inputStream = context.getResources().openRawResource(R.raw.activation);
        final String version = AppManager.getInstance().getString(ConstantApp.HSTNG_TLS);
        final String path = "/sxtms/online/tms/" + deviceInfo + "/active";
        Logger.v("PATHH -" + path);
        Logger.v("code -" + code);
        final String params = "message={\"headMsg\":{\"oid\":\"07CAFCA1950592EED9A130F341BEAB11\",\"firmCode\":\"NEWLAND\",\"productType\":\"Q@\",\"productType2\":\"Q2\",\"locale\":\"en_US\",\"nonce\":\"198A8E09011BF1B9A1E7712B86A9ED67\",\"timestamp\":\"20200703113556\",\"version\":\"09.04.20\",\"posVerType\":\"Newland\",\"activeCode\":\"" + code + "\",\"signature\":\"4A7A549FE7AC50FF360A77EA24DC334D\"}}";
        resultEnded = false;
        final Handler handler = new Handler();
        final int finalPort = port;
        final String finalIp = ip;
        final String finalActivation = activation;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Replace below IP with the IP of that device in which server socket open.
                    //If you change port then change the port number in the server side code also.

                    SSLSocketFactoryExtended sslsocketfactory = new SSLSocketFactoryExtended(inputStream, version, false);
                    Logger.v("Socket 1");
                    Logger.v("IP-PORT --" + finalIp + "--" + finalPort);
//                    SSLSocket requestSocket = (SSLSocket) sslsocketfactory.createSocket(IP, port);
                    SSLSocket requestSocket = (SSLSocket) sslsocketfactory.createSocket(finalIp, finalPort);
                    Logger.v("Socket connected Merchant-" + requestSocket.isConnected());
                    BufferedInputStream bis = new BufferedInputStream(requestSocket.getInputStream());
//                    BufferedWriter bos = new BufferedWriter(new OutputStreamWriter(requestSocket.getOutputStream(), "UTF8"));
                    PrintWriter bos = new PrintWriter(requestSocket.getOutputStream());
                    Logger.v("TRY block send");
                    // 3: Post the request data
                    bos.print("POST " + path + " HTTP/1.1\r\n" + "Content-Length: " + params.length() + "\r\n" + "Content-Type: application/x-www-form-urlencoded \r\n" + "Host: "+ finalActivation +"\r\n" + "\r\n" +  params);
                    Logger.v("Request");
                    Logger.v("POST " + path + " HTTP/1.1\r\n" + "Content-Length: " + params.length() + "\r\n" + "Content-Type: application/x-www-form-urlencoded \r\n" + "Host: "+ finalActivation +"\r\n" + "\r\n" +  params);
                    bos.flush();

                    // 4: Receive the response data
                    byte[] buffer = new byte[1024];
                    int nBytes = -1;
                    Logger.v("TRY block Receive 1");
                    while ((nBytes = bis.read(buffer)) >= 0) {
                        Logger.v("buffer --" + buffer.length);
                        Logger.vv(buffer);
                        if (resultEnded)
                            return;
                        String code = "";
                        String msg = context.getResources().getString(R.string.encountered_error);
                        try {
                            final String output = ByteConversionUtils.byteArrayToHexString(buffer, buffer.length, false);
                            String result2 = ByteConversionUtils.convertHexToString(output);
                            String body = getBody(result2);
                            JSONObject object = new JSONObject(body);
                            code = object.getString("retCode");
                            msg = object.getString("retMsg");
                            resultEnded = true;
                        } catch (Exception e) {
                            Logger.v("Exceptionee");
                        }
                        closeRequest(requestSocket);
                        final String finalCode = code;
                        final String finalMsg = msg;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                forceCancel = true;
                                mPortalTimer.cancel();
                                mPortalTimer.onFinish();
                                moveNext(finalCode, finalMsg);
                            }
                        });
                    }
                    closeRequest(requestSocket);
                    Logger.v("Socket close");
                } catch (IOException e) {
                    e.printStackTrace();
                    Logger.v("Exception");
                } catch (CertificateException e) {
                    Logger.v("Exception 1");
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    Logger.v("Exception 2");
                    e.printStackTrace();
                } catch (KeyStoreException e) {
                    Logger.v("Exception 3");
                    e.printStackTrace();
                } catch (KeyManagementException e) {
                    Logger.v("Exception 4");
                    e.printStackTrace();
                }
            }
        });
        mPortalTimer.start();
        thread.start();
    }

    private void moveNextFinish() {
        Toast.makeText(context, "Activation failed, Plz check contact our Support team. \n فشل التنشيط ، يرجى الاتصال بفريق الدعم.", Toast.LENGTH_SHORT).show();
        finish();
    }

    private String getBody(String result2) {
        String[] parse = result2.split("\n");
        for (int i = 0; i < parse.length; i++) {
            Logger.v("Position--"+i+"--"+parse[i]);
            if(parse[i].trim().length() != 0) {
                Logger.v("First-"+parse[i].trim().substring(0, 1));
                if (parse[i].trim().substring(0, 1).equalsIgnoreCase("{")) {
                    Logger.v("Length " + i + "--" + parse[i]);
                    return parse[i];
                }
            }
        }
        return "";
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


}