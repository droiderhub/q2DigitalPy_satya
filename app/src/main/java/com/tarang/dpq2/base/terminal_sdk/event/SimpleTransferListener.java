package com.tarang.dpq2.base.terminal_sdk.event;

import android.content.Context;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.cloudpos.AlgorithmConstants;
import com.cloudpos.DeviceException;
import com.cloudpos.OperationResult;
import com.cloudpos.TimeConstants;
import com.cloudpos.jniinterface.EMVJNIInterface;
import com.cloudpos.jniinterface.IFuntionListener;
import com.cloudpos.pinpad.KeyInfo;
import com.cloudpos.pinpad.PINPadDevice;
import com.cloudpos.pinpad.PINPadOperationResult;
import com.cloudpos.pinpad.extend.PINPadExtendDevice;
import com.tarang.dpq2.R;
import com.tarang.dpq2.base.AppInit;
import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.MapperFlow;
import com.tarang.dpq2.base.baseactivities.BaseActivity;
import com.tarang.dpq2.base.jpos_class.ByteConversionUtils;
import com.tarang.dpq2.base.jpos_class.ConstantApp;
import com.tarang.dpq2.base.jpos_class.ConstantAppValue;
import com.tarang.dpq2.base.jpos_class.ISOUtil;
import com.tarang.dpq2.base.room_database.db.AppDatabase;
import com.tarang.dpq2.base.room_database.db.dao.TMSCardSchemeDao;
import com.tarang.dpq2.base.room_database.db.entity.TMSCardSchemeEntity;
import com.tarang.dpq2.base.room_database.db.entity.TransactionModelEntity;
import com.tarang.dpq2.base.terminal_sdk.AppConfig;
import com.tarang.dpq2.base.terminal_sdk.SdkSupport;
import com.tarang.dpq2.base.terminal_sdk.device.SDKDevice;
import com.tarang.dpq2.base.terminal_sdk.q2.utils.StringUtil;
import com.tarang.dpq2.base.terminal_sdk.utils.LightsDisplay;
import com.tarang.dpq2.base.terminal_sdk.utils.SoundPoolImpl;
import com.tarang.dpq2.base.utilities.CountDownResponseTimer;
import com.tarang.dpq2.base.utilities.Utils;
import com.tarang.dpq2.isopacket.CreatePacket;
import com.tarang.dpq2.isopacket.IsoRequest;
import com.tarang.dpq2.model.RetailerDataModel;
import com.tarang.dpq2.view.activities.LandingPageActivity;
import com.tarang.dpq2.view.activities.PrintActivity;
import com.tarang.dpq2.view.activities.TransactionActivity;
import com.wizarpos.emvsample.constant.Constant;
import com.wizarpos.jni.ContactICCardReaderInterface;
import com.wizarpos.jni.ContactlessICCardReaderInterface;
import com.wizarpos.jni.PinPadCallbackHandler;
import com.wizarpos.jni.PinPadInterface;

import org.jpos.iso.ISOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.cloudpos.jniinterface.EMVJNIInterface.close_reader;
import static com.cloudpos.jniinterface.EMVJNIInterface.emv_anti_shake_finish;
import static com.cloudpos.jniinterface.EMVJNIInterface.emv_get_candidate_list;
import static com.cloudpos.jniinterface.EMVJNIInterface.emv_get_kernel_id;
import static com.cloudpos.jniinterface.EMVJNIInterface.emv_get_tag_data;
import static com.cloudpos.jniinterface.EMVJNIInterface.emv_get_tag_list_data;
import static com.cloudpos.jniinterface.EMVJNIInterface.emv_is_tag_present;
import static com.cloudpos.jniinterface.EMVJNIInterface.emv_kernel_initialize;
import static com.cloudpos.jniinterface.EMVJNIInterface.emv_process_next;
import static com.cloudpos.jniinterface.EMVJNIInterface.emv_set_candidate_list_result;
import static com.cloudpos.jniinterface.EMVJNIInterface.emv_set_kernel_attr;
import static com.cloudpos.jniinterface.EMVJNIInterface.emv_set_kernel_type;
import static com.cloudpos.jniinterface.EMVJNIInterface.emv_set_online_pin_entered;
import static com.cloudpos.jniinterface.EMVJNIInterface.emv_set_online_result;
import static com.cloudpos.jniinterface.EMVJNIInterface.emv_set_pinpad_title;
import static com.cloudpos.jniinterface.EMVJNIInterface.emv_set_tag_data;
import static com.cloudpos.jniinterface.EMVJNIInterface.emv_set_trans_amount;
import static com.cloudpos.jniinterface.EMVJNIInterface.emv_set_trans_type;

import static com.cloudpos.jniinterface.EMVJNIInterface.emv_stop_process;
import static com.cloudpos.jniinterface.EMVJNIInterface.emv_terminal_param_set_drl;
import static com.cloudpos.jniinterface.EMVJNIInterface.emv_trans_initialize;
import static com.cloudpos.jniinterface.EMVJNIInterface.exitEMVKernel;
import static com.cloudpos.jniinterface.EMVJNIInterface.get_card_type;
import static com.cloudpos.jniinterface.EMVJNIInterface.loadEMVKernel;
import static com.cloudpos.jniinterface.EMVJNIInterface.open_reader_ex;
import static com.cloudpos.jniinterface.EMVJNIInterface.registerFunctionListener;
import static com.tarang.dpq2.base.terminal_sdk.AppConfig.EMV.TAG55;
import static com.tarang.dpq2.base.terminal_sdk.AppConfig.EMV.consumeType;
import static com.tarang.dpq2.base.terminal_sdk.AppConfig.EMV.ic55Data;
import static com.tarang.dpq2.base.terminal_sdk.AppConfig.EMV.reqObj;

//TODO Q2
public class SimpleTransferListener implements Constant, IFuntionListener, PinPadCallbackHandler {
    public static String selectApplicationName = "";
    public static String tsi = "";
    public static String cvm9f34 = "";
    public static String tvr = "";
    public static String s0x9f6c = "";
    public static String s0x9F66 = "";
    public static MakeConnection connection;
    private SoundPoolImpl spi;
    public static Context context;
    public static int[] L_55TAGS = new int[32];
    private static int[] L_SCRIPTTAGS = new int[21];
    private static int[] L_REVTAGS = new int[5];
    private static WaitThreat waitPinInputThreat = new WaitThreat();
    private static WaitThreat waitPinInputThreat_rf = new WaitThreat();
    private static WaitThreat waitPinInputThreat_pin = new WaitThreat();
    private static WaitThreat waitSockettThreat = new WaitThreat();
    private static WaitThreat waitSelectApp = new WaitThreat();
    private static WaitThreat waitPinInputThreat_apple_pin = new WaitThreat();
    private static WaitThreat waitPinInputThreat_apple_pin_ = new WaitThreat();
    private static WaitThreat waitSockettThreat_apple = new WaitThreat();
    public static byte[] pinBlock = null;
    private String encryptAlgorithm;
    private String transType = "";
    private int pinAttempt;
    private int trackKeyIndex;
    private int mkIndex;
    public static int selectApplication = -1;
    boolean selectAppCancelled = false;
    public static boolean isCashBackAmountExceed;
    public static boolean isCancelled;
    public static boolean isCardInvalid;
    public static boolean isAmountExceed;
    public static boolean isTransactionNotAllowed;
    public static boolean disableReversal;
    public static boolean doReversal;
    public static int isPinRequires = 0;
    public static boolean isEMVCompleted;
    public static boolean isGccNet;
    public static boolean isSAFDecilined = false;
    public static boolean isPinCancelled = false;
    public static boolean madeOnlineConnection_new = false;
    public static int isScreenAvailable = 0;


    public static int DO_SOCKET_REQ = 0;
    public static int DO_SOCKET_REQ_AGAIN = 100;
    public static int DO_READ_AND_PRINT = 1000;
    public static int DO_FALLBACK = 1;
    public static int DO_FALLBACK_APP = 101;
    public static int DO_REVERSAL = 2;
    public static int DO_REVERSAL_CANCEL = 22;
    public static int DO_TIMER = 3;
    public static int DO_TIMER_REVERSAL = 33;
    public static int DO_SAF = 4;
    public static int DO_SAF_APPROVED = 66;
    public static int DO_SAF_REJECTED = 6;
    public static int DO_SAF_DECLINED = 7;
    public static int FINAL_ERROR = 77;
    public static int DO_ERROR = 5;
    public static int DO_ERROR_MESSAGE = 9;
    public static int DO_CARD_INVALID = 10;
    public static int DO_AMOUNT_EXCEED = 11;
    public static int DO_TRANSACTION = 12;
    public static int DO_CANCEL_AND_MOVE = 13;
    public static int DO_ERROR_PIN_CANCELLED = 14;
    public static int DO_WAVE_AGAIN = 15;
    public static int DO_BEEP_ONCE = 16;
    public static int DO_INVALID_PIN = 17;
    public static int Do_PLZ_SEE_PHONE = 18;

    static {
        L_55TAGS[0] = 0x9f26;//
        L_55TAGS[1] = 0x9F10;//
        L_55TAGS[2] = 0x9F27;//
        L_55TAGS[3] = 0x9F37; //
        L_55TAGS[4] = 0x9F36;//
        L_55TAGS[5] = 0x95;//
        L_55TAGS[6] = 0x9A;//
        L_55TAGS[7] = 0x9C;//
        L_55TAGS[8] = 0x9F02;
        L_55TAGS[9] = 0x5F2A;//
        L_55TAGS[10] = 0x82;
        L_55TAGS[11] = 0x9F1A;//
        L_55TAGS[12] = 0x9F03;
        L_55TAGS[13] = 0x9F33;//
        L_55TAGS[14] = 0x9F34;//
        L_55TAGS[15] = 0x9F35;//
        L_55TAGS[16] = 0x9F1E;//
        L_55TAGS[17] = 0x84;//
//		L_55TAGS[18] = 0x9F09;
//		L_55TAGS[19] = 0x9F41;
//		L_55TAGS[20] = 0x8a;
//		L_55TAGS[21] = 0x9f63;
        L_55TAGS[18] = 0x50;
        L_55TAGS[19] = 0x4f;
        L_55TAGS[20] = 0x9f12;
        L_55TAGS[21] = 0xDF01;//
        L_55TAGS[22] = 0x9B;
        L_55TAGS[23] = 0x8a;
        L_55TAGS[24] = 0x5A;//
        L_55TAGS[25] = 0x57;//
        L_55TAGS[26] = 0x9F6C;//
        L_55TAGS[27] = 0x9F6E;//
        L_55TAGS[28] = 0x9F24;//
        L_55TAGS[29] = 0x9F66;//
        L_55TAGS[30] = 0x9F19;//
        L_55TAGS[31] = 0x9F25;//
//        L_55TAGS[32] = 0x9F66;

        L_SCRIPTTAGS[0] = 0x9F33;
        L_SCRIPTTAGS[1] = 0x9F34;
        L_SCRIPTTAGS[2] = 0x9F35;
        L_SCRIPTTAGS[3] = 0x95;
        L_SCRIPTTAGS[4] = 0x9F37;
        L_SCRIPTTAGS[5] = 0x9F1E;
        L_SCRIPTTAGS[6] = 0x9F10;
        L_SCRIPTTAGS[7] = 0x9F26;
        L_SCRIPTTAGS[8] = 0x9F27;
        L_SCRIPTTAGS[9] = 0x9F36;
        L_SCRIPTTAGS[10] = 0x82;
        L_SCRIPTTAGS[11] = 0xDF31;
        L_SCRIPTTAGS[12] = 0x9F1A;
        L_SCRIPTTAGS[13] = 0x9A;
        L_SCRIPTTAGS[14] = 0x9C;
        L_SCRIPTTAGS[15] = 0x9F02;
        L_SCRIPTTAGS[16] = 0x5F2A;
        L_SCRIPTTAGS[17] = 0x84;
        L_SCRIPTTAGS[18] = 0x9F09;
        L_SCRIPTTAGS[19] = 0x9F41;
        L_SCRIPTTAGS[20] = 0x9F63;

        L_REVTAGS[0] = 0x95;
        L_REVTAGS[1] = 0x9F1E;
        L_REVTAGS[2] = 0x9F10;
        L_REVTAGS[3] = 0x9F36;
        L_REVTAGS[4] = 0xDF31;
    }

    static String responseData55 = "";
    static String responseData38 = "";
    public static String errorMsg = "";
    private boolean madeOnlineConnection;
    private boolean isMada;
    private boolean showPinOkay;
    private boolean doBeepOnce;
    private boolean showCard;
    public static int rf_thread = 0;
    private boolean isAppSelect = false;
    private int errorCode = 0;
    public static SimpleTransferListener myInstance;
    private EMVProcessNextThread mEMVProcessNextThread;
    private boolean pinpadOpened;
    private boolean cardActionHAppenned;
    public static boolean isSwipeHappened = false;
    private boolean onlyOnce = false;
    public static boolean doApplePay;

    public static SimpleTransferListener getInstance(Context context1) {
        if (myInstance == null) {
            myInstance = new SimpleTransferListener();
        }

        context = context1;
        return myInstance;
    }

    public static void resetListner() {
        myInstance = null;
    }

    public SimpleTransferListener() {
        selectApplicationName = "";
        if (!AppInit.loadKernal) {
            loadKernalInit();
            AppInit.loadKernal = true;
        }
        spi = SoundPoolImpl.getInstance();
    }

    private void loadKernalInit() {
        if (ContactICCardReaderInterface.init() >= 0) {
            Logger.v("Init Contact");
        }
        if (ContactlessICCardReaderInterface.init() >= 0) {
            Logger.v("Init ContactLess");
        }

       /* String tmpEmvLibDir = "";
        tmpEmvLibDir = AppInit.getInstance().getDir("", 0).getAbsolutePath();
        tmpEmvLibDir = tmpEmvLibDir.substring(0, tmpEmvLibDir.lastIndexOf('/')) + "/lib/libEMVKernal.so";

        if (loadEMVKernel(tmpEmvLibDir.getBytes(), tmpEmvLibDir.getBytes().length) == 0) {*/
        registerFunctionListener(this);
//            emv_kernel_initialize();
        emv_set_kernel_attr(new byte[]{0x20, 0x08}, 2);
//            emv_terminal_param_set_drl(new byte[]{0x00},1);
//        } else
//            Logger.v("EMV Kernal ElSE");
    }

    public void initListener(Context context, String transType, MakeConnection connection) {
        Logger.v("Listner started -initListener");
        Logger.v("transType " + transType);
//        intEMV();
        SimpleTransferListener.isPinRequires = 0;
        this.context = context;
        this.transType = transType;
        this.connection = connection;
        madeOnlineConnection = false;
        isPinCancelled = false;
        isAppSelect = false;
        AppConfig.EMV.icKernalId = "";
        pinAttempt = 1;
        SimpleTransferListener.pinBlock = null;
        isEMVCompleted = false;
        errorCode = 0;
        isAmountExceed = false;
        isCashBackAmountExceed = false;
        Logger.v("before_starting_emv_thread");
        stopEMVProcessThread();
        stopEMVFlow();
        new EMVThread().start();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void emvProcessCallback(byte[] bytes) {
        Logger.v("EmV COmpleted");
        Logger.v("emvProcessCallback");
        Logger.v("Status--" + bytes[0]);
        Logger.v("emv_code----" + bytes[1]);
        try {
            emvProceedFlow(bytes[0], bytes[1]);
        } catch (InterruptedException | ISOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cardEventOccured(int i) {
        Logger.v("card_event_occured");
        Logger.v("card_event_occured" + i);
        if (i == SMART_CARD_EVENT_REMOVE_CARD) {
            Logger.v("i == SMART_CARD_EVENT_REMOVE_CARD----" + i);
            AppConfig.isCardRemoved = false;
            ((BaseActivity) context).showToast("Card removed");
            return;
        }
        if (context instanceof LandingPageActivity) {
            ((LandingPageActivity) context).removeCard();
            return;
        }
        Logger.v("Card enven Occured--" + i);
        if (i == 12) {
            waveAgain();
        } else if (i == SMART_CARD_EVENT_POWERON_ERROR) {
            Logger.v("i == SMART_CARD_EVENT_POWERON_ERROR----" + i);
            if (context instanceof PrintActivity) {
                cardActionHAppenned = true;
                new SdkSupport(context).closeCardReader1();
                ((PrintActivity) context).showAlert(9);
            } else if (context instanceof TransactionActivity) {
                cardActionHAppenned = true;
                new SdkSupport(context).closeCardReader1();
                MapperFlow.getInstance().moveToPrintScreenFallback(context);
            }
        } else if (i == SMART_CARD_EVENT_CONTALESS_HAVE_MORE_CARD) {
            Logger.v("i == SMART_CARD_EVENT_CONTALESS_HAVE_MORE_CARD----" + i);
            waveAgain();
        } else if (i == SMART_CARD_EVENT_CONTALESS_ANTI_SHAKE) {
            Logger.v("i == SMART_CARD_EVENT_CONTALESS_ANTI_SHAKE----" + i);
            if (!onlyOnce) {
                cardActionHAppenned = false;
                disableAntiShake();
            }
        } else if (i == SMART_CARD_EVENT_INSERT_CARD) {
            Logger.v("i == SMART_CARD_EVENT_INSERT_CARD----" + i);
            cardActionHAppenned = true;
            SdkSupport.readMSRCard = false;
            Logger.v("get_card_type()-" + get_card_type());
            if (get_card_type() == CARD_CONTACT) {
                Logger.v("get_card_type == CARD_CONTACT----" + CARD_CONTACT);
                cancelContactlessCard();
                AppConfig.EMV.consumeType = 1;
                AppConfig.isCardRemoved = true;
            } else if (get_card_type() == CARD_CONTACTLESS) {
                Logger.v("get_card_type == CARD_CONTACTLESS----" + CARD_CONTACTLESS);
                AppConfig.EMV.consumeType = 2;
            }

            if (context instanceof TransactionActivity) {
                Logger.v("moveToPrintScreen_transactionactivity");
                if (((TransactionActivity) context).checkCondition())
                    MapperFlow.getInstance().moveToPrintScreen(context);
            } else if (context instanceof PrintActivity) {
                Logger.v("moveToPrintScreen_printactivity");
                if (((PrintActivity) context).checkCondition())
                    MapperFlow.getInstance().moveToPrintScreen(context, true);
            } else if (context instanceof LandingPageActivity) {
                Logger.v("moveEnterAmount");
                MapperFlow.getInstance().moveEnterAmount(context);
            }
        }
    }

    private void waveAgain() {
        if (context instanceof PrintActivity) {
            new SdkSupport(context).closeCardReader();
            ((PrintActivity) context).showAlert(15);
        } else if (context instanceof TransactionActivity) {
            new SdkSupport(context).closeCardReader();
            MapperFlow.getInstance().moveToPrintScreenWaveAgain(context);
        }
    }

    private void disableAntiShake() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Logger.v("ConsumeType : "+ AppConfig.EMV.consumeType);
                if (AppConfig.EMV.consumeType == -1) {
                    Logger.v("ANTI SHAKE FINISH -" + cardActionHAppenned);
                    emv_anti_shake_finish(0);
                    if (!cardActionHAppenned) {
                        waveAgainSound();
                    }
                } else {
                    // waveAgain();
                    Logger.v("ANTI SHAKE FINISH 1");
                }
            }
        }).start();
    }

    private void waveAgainSound() {
        Logger.v("waveAgainSound");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!cardActionHAppenned && !isSwipeHappened) {
            ((BaseActivity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    spi = SoundPoolImpl.getInstance();
                    spi.initLoad(context, new SoundPool.OnLoadCompleteListener() {
                        @Override
                        public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                            Logger.v("Sound listner 000_simpletransfer");
                            spi.playTwice();
                        }
                    });
                    //SoundPoolImpl.getInstance().playTwice();
                    new LightsDisplay(context).showRedLight();
                    Toast.makeText(context, context.getString(R.string.wave_again), Toast.LENGTH_SHORT).show();
                    new LightsDisplay(context).showBlueLight();
                }
            });
        }
    }

    @Override
    public void processCallback(byte[] data) {
        processCallback(data[0], data[1]);
    }

    @Override
    public void processCallback(int nCount, int nExtra) {
        Message msg = new Message();
        msg.what = OFFLINE_PIN_NOTIFIER;
        msg.arg1 = nCount;
        msg.arg2 = nExtra;
        // mHandler.sendMessage(msg);
    }

    class EMVThread extends Thread {
        public void run() {
            super.run();
            Logger.v("EMV Thread run --" + get_card_type());
            Logger.v("EMV Thread run --" + AppConfig.EMV.consumeType);
            emv_trans_initialize();
            if (AppConfig.EMV.consumeType == 1)
                emv_set_kernel_type(CONTACT_EMV_KERNAL);
            else if (AppConfig.EMV.consumeType == 2)
                emv_set_kernel_type(CONTACTLESS_EMV_KERNAL);

            Logger.v("set_amount_emv----" + CreatePacket.getAmount());
            setEMVTransAmount(CreatePacket.getAmount());
            setEMVData();
            Logger.v("EMV DATA SET DONE");

            //pre-process
//            if(appState.trans.getEMVKernelType() == CONTACTLESS_EMV_KERNAL && !preProcessQpboc())
//            {
//                Message msg = new Message();
//                msg.what = PREPROCESS_ERROR_NOTIFIER;
//                mHandler.sendMessage(msg);
//                return;
//            }
            moveNextFlow();
            //emv_process_next();
        }
    }

    public static void setEMVData() {
        Logger.v("setEMVData method called");
//        if(appState.getTranType() == QUERY_CARD_RECORD)
//        {
//            emv_set_trans_amount(new byte[]{'0', 0x00});
//            emv_set_other_amount(new byte[]{'0', 0x00});
//            if(appState.recordType == 0x00)
//            {
//                emv_set_trans_type(EMV_TRANS_CARD_RECORD);
//            }
//            else
//            {
//                emv_set_trans_type(EMV_TRANS_LOAD_RECORD);
//            }
//        }
//        else{
        setEmvData(0x9A, (ByteConversionUtils.formatTranDate("yyMMdd")));
        setEmvData(0x9F21, (ByteConversionUtils.formatTranDate("hhmmss")));
//        emv_set_tag_data(0x9F41, StringUtil.hexString2bytes(StringUtil.fillZero(Integer.toString(appState.trans.getTrace()), 8)), 4);
        emv_set_trans_type(EMV_TRANS_GOODS_SERVICE);
//        }
    }

    class EMVProcessNextThread extends Thread {
        public void run() {
            super.run();
            Logger.v("EMVProcessNextThread-------emv_process_next() method execute");
            emv_process_next();
            Logger.v("emv_process_next()-------executed");
        }
    }

    void setEMVTransAmount(String strAmt) {
        byte[] amt = new byte[strAmt.length() + 1];
        System.arraycopy(strAmt.getBytes(), 0, amt, 0, strAmt.length());
        emv_set_trans_amount(amt);
    }

    void moveNextFlow() {
        Logger.v("moveNextFlow--EMVProcessNextThread_start");
        mEMVProcessNextThread = new EMVProcessNextThread();
        mEMVProcessNextThread.start();
    }

    public void stopEMVProcessThread() {
        try {
            //    if (mEMVProcessNextThread != null) {
            mEMVProcessNextThread = new EMVProcessNextThread();
            mEMVProcessNextThread.interrupt();
            Logger.v("mEMVProcessNextThread stopped ");
//            mEMVProcessNextThread.stop();
            //    }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void stopEMVFlow() {
        int ret = emv_stop_process();
        Logger.v("EMV_stop_process : " + ret);
        //   if(debug)Log.d(APP_TAG, "emv_stop_process " + ret);
//        if(ret < 0)
//        {
//            new Thread(new Runnable()
//            {
//                @Override
//                public void run()
//                {
//                    appState.autoTestFlag = false;
//                    queryContactless = false;
//                    appState.requestCardFor2Tap = false;
//                    cancelContactlessCard(true, CLOSED_CONTACTLESS_NOTIFIER);
//                }
//            }).start();
//        }

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void emvProceedFlow(int status, int result) throws InterruptedException, ISOException {
        Logger.v("EMV_PROCESS_NEXT_COMPLETED_NOTIFIER, emvStatus = " + status + ", emvRetCode = " + result);
        //    LoadKeyWorker.setEMVTermInfo();
        byte[] tagData = new byte[0];
        int tagDataLength = 0;
        switch (status) {
            case STATUS_CONTINUE:
                Logger.v("STATUS_CONTINUE");
                switch (result) {
                    case EMV_CANDIDATE_LIST:
                        Logger.v("EMV_CANDIDATE_LIST");
//                        appState.aidNumber = emv_get_candidate_list(appState.aidList, appState.aidList.length);
                        Logger.v("moving to selectemvapplist method");
                        //selectEMVAppList();
                        selectAids();
//                        selectEMVAppList(STATE_SELECT_EMV_APP, Activity.RESULT_OK);
                        break;
                    case EMV_APP_SELECTED:
                        Logger.v("EMV_APP_SELECTED");

                        loadEMVTags();
//                        moveNextFlow();
                        break;
                    case EMV_READ_APP_DATA:
                        Logger.v("EMV_READ_APP_DATA");
//                        if (AppConfig.EMV.consumeType == 2) {
//                            loadEMVTags();
//                        }
                        emvReadAppData(tagData, tagDataLength);
                        /*if (emv_is_tag_present(0x9F79) >= 0) {
                            tagData = new byte[6];
                            emv_get_tag_data(0x9F79, tagData, 6);
//                            appState.trans.setECBalance(ByteUtil.bcdToInt(tagData));
//                            Logger.v("0x9F79----byteutil----"+ByteUtil.bcdToInt(tagData));
                            Logger.v("setECBalance----" + (tagData));
                        }

                        tagData = new byte[100];
                        if (emv_is_tag_present(0x5A) >= 0) {
                            tagDataLength = emv_get_tag_data(0x5A, tagData, tagData.length);
//                            appState.trans.setPAN(StringUtil.toString(AppUtil.removeTailF(ByteUtil.bcdToAscii(tagData, 0, tagDataLength))));
                            Logger.v("0x5A----" + (tagData));
//                            Logger.v("setPAN----"+StringUtil.toString(AppUtil.removeTailF(ByteUtil.bcdToAscii(tagData, 0, tagDataLength))));
                        }
                        // Track2
                        if (emv_is_tag_present(0x57) >= 0) {
                            tagDataLength = emv_get_tag_data(0x57, tagData, tagData.length);
//                            appState.trans.setTrack2Data(StringUtil.toString(AppUtil.removeTailF(ByteUtil.bcdToAscii(tagData, 0, tagDataLength))));
                            Logger.v("0x57----" + (tagData));
//                            Logger.v("setTrack2Data------"+StringUtil.toString(AppUtil.removeTailF(ByteUtil.bcdToAscii(tagData, 0, tagDataLength))));
                        }
                        // CSN
                        if (emv_is_tag_present(0x5F34) >= 0) {
                            tagDataLength = emv_get_tag_data(0x5F34, tagData, tagData.length);
//                            appState.trans.setCSN(tagData[0]);
                            Logger.v("0x5F34----" + (tagData));
                            Logger.v("setCSN----------" + tagData[0]);
                        }
                        // Expiry
                        if (emv_is_tag_present(0x5F24) >= 0) {
                            tagDataLength = emv_get_tag_data(0x5F24, tagData, tagData.length);
//                            appState.trans.setExpiry(StringUtil.toHexString(tagData, 0, 3, false).substring(0, 4));
                            Logger.v("0x5F24----" + (tagData));
//                            Logger.v("setExpiry---------"+StringUtil.toHexString(tagData, 0, 3, false).substring(0, 4));
                        }
                        //confirmCard();
                        Logger.v("executing to EMVProcessNextThread");
                        if (isAppSelect) {
                            Logger.v("moveNextFlow---");
                            moveNextFlow();
                        } else{
                            Logger.v("loadEMVTags---");
                            loadEMVTags();
                        }
                        break;
                    case EMV_DATA_AUTH:
                        Logger.v("EMV_DATA_AUTH");
                        byte[] TSI = new byte[2];
                        byte[] TVR = new byte[5];
                        emv_get_tag_data(0x9B, TSI, 2); // TSI
                        emv_get_tag_data(0x95, TVR, 5); // TVR
                        if ((TSI[0] & (byte) 0x80) == (byte) 0x80
                                && (TVR[0] & (byte) 0x40) == (byte) 0x00
                                && (TVR[0] & (byte) 0x08) == (byte) 0x00
                                && (TVR[0] & (byte) 0x04) == (byte) 0x00
                        ) {
//                            appState.promptOfflineDataAuthSucc = true;

                        }
                        if (emv_is_tag_present(0x9B) >= 0) {
                            int tagLength = emv_get_tag_data(0x9B, TSI, 2);
                            Logger.v("0x9B----" + (TSI));
                            Logger.v("0x9B----" + ByteConversionUtils.fetchTagValu(TSI, 0, tagLength));
                            //  String data95 = ByteConversionUtils.fetchTagValu(TVR, 0, tagLength);
                            // String data95 = ISOUtil.hexString(emvModule.getEmvData(0x95));
                            String TSIs = ISOUtil.hexString(TSI);
                            Logger.v("tsi_ox9b------"+TSIs);
                            tsi = TSIs;

                        }

                        int i = emv_get_kernel_id();
                        Logger.v("kernal_id----"+i);
                        if (emv_is_tag_present(0x95) >= 0) {
                            int tagLength = emv_get_tag_data(0x95, TVR, TVR.length);
                            Logger.v("0x95----" + (TVR));
                            Logger.v("0x95----" + ByteConversionUtils.fetchTagValu(TVR, 0, tagLength));
                            //  String data95 = ByteConversionUtils.fetchTagValu(TVR, 0, tagLength);
                            // String data95 = ISOUtil.hexSt`ring(emvModule.getEmvData(0x95));
                            String data95 = ISOUtil.hexString(TVR);
                            Logger.v("tvr_95------"+data95);
                            tvr =data95;

                        }
                        moveNextFlow();*/
                        break;
                    case EMV_OFFLINE_PIN:
                        showPinOkay = true;
                        Logger.v("EMV_OFFLINE_PIN");
                        SimpleTransferListener.isPinRequires = 0;
                        //  setEmvOfflinePinCallbackHandler(this);
                        //   PinPadInterface.setupCallbackHandler(this);
                        //   EMVJNIInterface.emv_process_next();
                        SDKDevice.getInstance(context).incrementKSN(); //closing pinpad device
                        doBeepOnce();
                        moveNextFlow();
//                        MapperFlow.getInstance().startOfflinePinInput();
                        break;
                    case EMV_ONLINE_ENC_PIN:
                        Logger.v("EMV_ONLINE_ENC_PIN");
                        rf_thread = 1;
                        showPinOkay = false;
                        doBeepOnce();
                        SimpleTransferListener.isPinRequires = 1;
                        emv_set_online_pin_entered(1);
                        Logger.v("emv_set_online_pin_entered");
                        if (AppConfig.EMV.consumeType == 2) {
                            moveNextFlow();
                            return;
                        } else {
                            waitForPinBlock(0);
                        }
//                        moveNextFlow();
//                        onlinePinFlow(true);
//                        if(appState.pinpadType == PINPAD_NONE)
//                        {
//                            Logger.v("EMV_ONLINE_ENC_PIN-----PINPAD_NONE");
//                            emv_set_online_pin_entered(1);
//                            Logger.v("executing EMVProcessNextThread");
//                            Logger.v("executing EMVProcessNextTEMV_PROCESS_ONLINEhread");
//                            mEMVProcessNextThread = new EMVProcessNextThread();
//                            mEMVProcessNextThread.start();
//                        }
//                        else
//                        {
//                            Logger.v("EMV_ONLINE_ENC_PIN-----inputOnlinePIN method called");
//                            inputOnlinePIN();
//                        }
                        break;
                    case EMV_PROCESS_ONLINE:
                        Logger.v("EMV_PROCESS_ONLINE");
                        if (AppConfig.EMV.consumeType == 2) {
                            new LightsDisplay(this.context).showAllLights();
                            if (loadEmvTagsForcless()) {
                                emvReadAppDataForCless(tagData, tagDataLength);
                                doBeepOnce();
                                showPinOkay();
                                fetchCardInfo("EMV_PROCESS_ONLINE");
                                Logger.v("Card type : " + AppConfig.EMV.consumeType);
                                Logger.v("PIN required : " + SimpleTransferListener.isPinRequires);
                                if ((AppConfig.EMV.consumeType == 2) && (SimpleTransferListener.isPinRequires == 1)) {
                                    Logger.v("pin_required_waitForPinBlock");
                                    waitForPinBlock(2);
                                } else
                                    sendOnLineRequest();
                            }
                        } else {
                            doBeepOnce();
                            showPinOkay();
                            fetchCardInfo("EMV_PROCESS_ONLINE");
                            if ((AppConfig.EMV.consumeType == 2) && (SimpleTransferListener.isPinRequires == 1)) {
                                Logger.v("pin_required_waitForPinBlock");
                                waitForPinBlock(2);
                            } else
                                sendOnLineRequest();
                        }
                        break;
                    default:
                        Logger.v("processemvcard default case execute------- executing EMVProcessNextThread");
                        moveNextFlow();
                        break;
                }
                break;
            case STATUS_COMPLETION:
                Logger.v("STATUS_COMPLETION");
                if (transType.equalsIgnoreCase(ConstantApp.REFUND) && AppConfig.EMV.consumeType == 1 && result == DECLINE_OFFLINE) {
                    doBeepOnce();
                    showPinOkay();
                    fetchCardInfo("EMV_PROCESS_ONLINE");
                    waitForPinBlock(2);
                } else {
                    flowEMVComplete(true, result);
                }
                break;
            default:
                boolean showError = true;
                switch (result) {
                    case ERROR_NO_APP:
                    case ERROR_INIT_APP:
//    						//appState.trans.setEmvCardError(true);
//    						//setResult(RESULT_OK, getIntent());
//    						appState.setErrorCode(R.string.error_no_app);
//    						finish();
                        errorMsg = "Card Communication Error";
                        break;
                    case ERROR_OTHER_CARD:
//                        appState.trans.setEmvCardError(true);
//                        //setResult(RESULT_OK, getIntent());
//                        appState.setErrorCode(R.string.error_other_card);
//                        finish();
                        errorMsg = "Error Other card";
                        break;
                    case ERROR_EXPIRED_CARD:
//                        appState.setErrorCode(R.string.error_expiry_card);
//                        finish();
                        errorMsg = "Error Expired card";
                        break;
                    case ERROR_CARD_BLOCKED:
//                        appState.setErrorCode(R.string.error_card_blocked);
//                        finish();
                        errorMsg = "Error Card Blocked";
                        break;
                    case ERROR_APP_BLOCKED:
//                        appState.setErrorCode(R.string.error_app_blocked);
//                        finish();
                        errorMsg = "Error App Blocked";
                        break;
                    case ERROR_SERVICE_NOT_ALLOWED:
//                        appState.setErrorCode(R.string.error_not_accepted);
//                        finish();
                        errorMsg = "Error Service not allowed";
                        break;
                    case ERROR_PINENTERY_TIMEOUT:
                        showError = false;
                        connection.onConnect(DO_CANCEL_AND_MOVE);
//                        appState.setErrorCode(R.string.error_pin_timeout);
//                        finish();
                        break;
                    case ERROR_CONTACT_DURING_CONTACTLESS:
                        Logger.v("Error Contact During Contacless");
                        //contact card present during contactless transaction, process contact transaction in conditions.
                        cancelContactlessCard();
                        if (open_reader_ex(1, 1) < 0) {
                            Logger.v("Error Code InsideElse");

                        } else {
                            Logger.v("Error Code Else");
                        }
                        errorMsg = "Card Communication Error";
//				else {
//					new EMVProcessNextThread().start();
//				}
                        break;
                    case ERROR_PROCESS_CMD:
//                        //retry it if contactless reader comm error
//                        if (AppConfig.EMV.consumeType == 2) {
//                            showError = false;
//                            close_reader(2);
//                            emv_set_anti_shake(1);
//                            open_reader(2);
//                            break;
//                        } else {
//
//                        }
//                        appState.setErrorCode(R.string.error_cmd);
//                        finish();
//                        break;
                        errorMsg = "Card Communication Error";
                        break;
                    case ERROR_USER_CANCELLED:
                    case ERROR_DATA_AUTH:
                        showError = false;
                        connection.onConnect(DO_CANCEL_AND_MOVE);
                        break;
                    default:
                        errorMsg = "Card Communication Error";
                        break;
                }
//                errorMsg = errorMsg + " " + result;
//                if (result==10) {
//                    errorMsg = "Card Communication Error";
//                }
                fetchCardInfo("after_error");
                if (showError) {
                    if (AppManager.getInstance().isDebugEnabled()) {
                        insertAndPrint();
                    } else {
                        connection.onConnect(DO_ERROR_MESSAGE);
                    }
                }
        }
    }

    private void insertAndPrint() {
        connection.onConnect(DO_READ_AND_PRINT);
    }

    public void cancelContactlessCard() {
        emv_anti_shake_finish(1);
        close_reader(AppInit.CONTACTLESS_RF);
    }

    private void cancelChipAndContactlessCard() {
        emv_anti_shake_finish(1);
        close_reader(AppInit.CONTACTLESS_RF);
        close_reader(AppInit.CONTACT_CHIP);
    }


    public static void setEmvData(int tag, String value) {
        byte[] dataVal = StringUtil.hexString2bytes(value);
        if (dataVal != null && dataVal.length != 0) {
            emv_set_tag_data(tag, dataVal, dataVal.length);
        } else {
            Logger.v("Set EMV TAG - NULL --" + tag);
        }
    }

    private void showTransactionNotAllowedDialoge() {
        if (context instanceof PrintActivity) {
            ((PrintActivity) context).showAlert(3);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private byte[] getHextByte(String data) {
        return ISOUtil.hex2byte(data);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void emvReadAppDataForcless(byte[] tagData, int tagDataLength) throws ISOException, InterruptedException {
        if (emv_is_tag_present(0x9F79) >= 0) {
            tagData = new byte[6];
            emv_get_tag_data(0x9F79, tagData, 6);
//                            appState.trans.setECBalance(ByteUtil.bcdToInt(tagData));
//                            Logger.v("0x9F79----byteutil----"+ByteUtil.bcdToInt(tagData));
            Logger.v("setECBalance----" + (tagData));
        }

        tagData = new byte[100];
        if (emv_is_tag_present(0x5A) >= 0) {
            tagDataLength = emv_get_tag_data(0x5A, tagData, tagData.length);
//                            appState.trans.setPAN(StringUtil.toString(AppUtil.removeTailF(ByteUtil.bcdToAscii(tagData, 0, tagDataLength))));
            Logger.v("0x5A----" + (tagData));
//                            Logger.v("setPAN----"+StringUtil.toString(AppUtil.removeTailF(ByteUtil.bcdToAscii(tagData, 0, tagDataLength))));
        }
        // Track2
        if (emv_is_tag_present(0x57) >= 0) {
            tagDataLength = emv_get_tag_data(0x57, tagData, tagData.length);
//                            appState.trans.setTrack2Data(StringUtil.toString(AppUtil.removeTailF(ByteUtil.bcdToAscii(tagData, 0, tagDataLength))));
            Logger.v("0x57----" + (tagData));
//                            Logger.v("setTrack2Data------"+StringUtil.toString(AppUtil.removeTailF(ByteUtil.bcdToAscii(tagData, 0, tagDataLength))));
        }
        // CSN
        if (emv_is_tag_present(0x5F34) >= 0) {
            tagDataLength = emv_get_tag_data(0x5F34, tagData, tagData.length);
//                            appState.trans.setCSN(tagData[0]);
            Logger.v("0x5F34----" + (tagData));
            Logger.v("setCSN----------" + tagData[0]);
        }
        // Expiry
        if (emv_is_tag_present(0x5F24) >= 0) {
            tagDataLength = emv_get_tag_data(0x5F24, tagData, tagData.length);
//                            appState.trans.setExpiry(StringUtil.toHexString(tagData, 0, 3, false).substring(0, 4));
            Logger.v("0x5F24----" + (tagData));
//                            Logger.v("setExpiry---------"+StringUtil.toHexString(tagData, 0, 3, false).substring(0, 4));
        }
        //confirmCard();
        Logger.v("executing to EMVProcessNextThread");
//        if (isAppSelect) {
//            Logger.v("moveNextFlow---");
//            moveNextFlow();
//        } else{
//            Logger.v("loadEMVTags---");
//            loadEMVTags();
//        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void emvReadAppData(byte[] tagData, int tagDataLength) throws ISOException, InterruptedException {
        if (emv_is_tag_present(0x9F79) >= 0) {
            tagData = new byte[6];
            emv_get_tag_data(0x9F79, tagData, 6);
//                            appState.trans.setECBalance(ByteUtil.bcdToInt(tagData));
//                            Logger.v("0x9F79----byteutil----"+ByteUtil.bcdToInt(tagData));
            Logger.v("setECBalance----" + (tagData));
        }

        tagData = new byte[100];
        if (emv_is_tag_present(0x5A) >= 0) {
            tagDataLength = emv_get_tag_data(0x5A, tagData, tagData.length);
//                            appState.trans.setPAN(StringUtil.toString(AppUtil.removeTailF(ByteUtil.bcdToAscii(tagData, 0, tagDataLength))));
            Logger.v("0x5A----" + (tagData));
//                            Logger.v("setPAN----"+StringUtil.toString(AppUtil.removeTailF(ByteUtil.bcdToAscii(tagData, 0, tagDataLength))));
        }
        // Track2
        if (emv_is_tag_present(0x57) >= 0) {
            tagDataLength = emv_get_tag_data(0x57, tagData, tagData.length);
//                            appState.trans.setTrack2Data(StringUtil.toString(AppUtil.removeTailF(ByteUtil.bcdToAscii(tagData, 0, tagDataLength))));
            Logger.v("0x57----" + (tagData));
//                            Logger.v("setTrack2Data------"+StringUtil.toString(AppUtil.removeTailF(ByteUtil.bcdToAscii(tagData, 0, tagDataLength))));
        }
        // CSN
        if (emv_is_tag_present(0x5F34) >= 0) {
            tagDataLength = emv_get_tag_data(0x5F34, tagData, tagData.length);
//                            appState.trans.setCSN(tagData[0]);
            Logger.v("0x5F34----" + (tagData));
            Logger.v("setCSN----------" + tagData[0]);
        }
        // Expiry
        if (emv_is_tag_present(0x5F24) >= 0) {
            tagDataLength = emv_get_tag_data(0x5F24, tagData, tagData.length);
//                            appState.trans.setExpiry(StringUtil.toHexString(tagData, 0, 3, false).substring(0, 4));
            Logger.v("0x5F24----" + (tagData));
//                            Logger.v("setExpiry---------"+StringUtil.toHexString(tagData, 0, 3, false).substring(0, 4));
        }
        //confirmCard();
        Logger.v("executing to EMVProcessNextThread");
        if (isAppSelect) {
            Logger.v("moveNextFlow---");
            moveNextFlow();
        } else {
            Logger.v("loadEMVTags---");
            loadEMVTags();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void emvReadAppDataForCless(byte[] tagData, int tagDataLength) throws ISOException, InterruptedException {
        if (emv_is_tag_present(0x9F79) >= 0) {
            tagData = new byte[6];
            emv_get_tag_data(0x9F79, tagData, 6);
//                            appState.trans.setECBalance(ByteUtil.bcdToInt(tagData));
//                            Logger.v("0x9F79----byteutil----"+ByteUtil.bcdToInt(tagData));
            Logger.v("setECBalance----" + (tagData));
        }

        tagData = new byte[100];
        if (emv_is_tag_present(0x5A) >= 0) {
            tagDataLength = emv_get_tag_data(0x5A, tagData, tagData.length);
//                            appState.trans.setPAN(StringUtil.toString(AppUtil.removeTailF(ByteUtil.bcdToAscii(tagData, 0, tagDataLength))));
            Logger.v("0x5A----" + (tagData));
//                            Logger.v("setPAN----"+StringUtil.toString(AppUtil.removeTailF(ByteUtil.bcdToAscii(tagData, 0, tagDataLength))));
        }
        // Track2
        if (emv_is_tag_present(0x57) >= 0) {
            tagDataLength = emv_get_tag_data(0x57, tagData, tagData.length);
//                            appState.trans.setTrack2Data(StringUtil.toString(AppUtil.removeTailF(ByteUtil.bcdToAscii(tagData, 0, tagDataLength))));
            Logger.v("0x57----" + (tagData));
//                            Logger.v("setTrack2Data------"+StringUtil.toString(AppUtil.removeTailF(ByteUtil.bcdToAscii(tagData, 0, tagDataLength))));
        }
        // CSN
        if (emv_is_tag_present(0x5F34) >= 0) {
            tagDataLength = emv_get_tag_data(0x5F34, tagData, tagData.length);
//                            appState.trans.setCSN(tagData[0]);
            Logger.v("0x5F34----" + (tagData));
            Logger.v("setCSN----------" + tagData[0]);
        }
        // Expiry
        if (emv_is_tag_present(0x5F24) >= 0) {
            tagDataLength = emv_get_tag_data(0x5F24, tagData, tagData.length);
//                            appState.trans.setExpiry(StringUtil.toHexString(tagData, 0, 3, false).substring(0, 4));
            Logger.v("0x5F24----" + (tagData));
//                            Logger.v("setExpiry---------"+StringUtil.toHexString(tagData, 0, 3, false).substring(0, 4));
        }
        //confirmCard();
//        Logger.v("executing to EMVProcessNextThread");
//        if (isAppSelect) {
//            Logger.v("moveNextFlow---");
//            moveNextFlow();
//        } else {
//            Logger.v("loadEMVTags---");
//            loadEMVTags();
//        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean loadEmvTagsForcless() throws InterruptedException, ISOException {
        Logger.v("loadEMVTags() cless called");
        if ((isScreenAvailable != 1)) {
            flowEMVComplete(false, 0);
            return false;
        }
        isAppSelect = true;
        doApplePay = false;
        madeOnlineConnection_new = false;
        Logger.ve("Listner State --onFinalAppSelect");
        AppConfig.EMV.icKernalId = String.format("%02x", emv_get_kernel_id());
        Logger.v("kernal_id---" + AppConfig.EMV.icKernalId);
//        Logger.v("Listner State --getKernelId--" + String.format("%02x", context.getKernelId()));
//        Logger.v("Listner State --getKernelId--" + String.format("%02x", context.getKernelId()));
        doBeepOnce = true;
        showCard = true;
        //    isPinCancelled = false;
        rf_thread = 0;
        isTransactionNotAllowed = false;
        isAmountExceed = false;
        isCashBackAmountExceed = false;
        responseData55 = "";
        responseData38 = "";
        //   isPinRequires = 0;
        SimpleTransferListener.isSAFDecilined = false;
//        emvModule.setEmvData(0x9F02, getHextBytes("000000000800"));//You can set emv kernel data in this step
        setEmvData(0xDF24, ("F4C0F0E8AF8E62"));

        String aid = "";
        if (emv_is_tag_present(0x4F) >= 0) {
            byte[] tagData = new byte[100];
            int tagLength = emv_get_tag_data(0x4F, tagData, tagData.length);
            Logger.v("0x4F----" + (tagData));
            aid = ByteConversionUtils.fetchTagValu(tagData, 0, tagLength);
        }
        Logger.v("card_aid--" + aid);
        isGccNet = false;
        AppDatabase database = AppDatabase.getInstance(context.getApplicationContext());
        TMSCardSchemeDao cardSchemeDao = database.getTMSCardSchemeDao();
        TMSCardSchemeEntity cardData = cardSchemeDao.getCardSchemeData(Utils.fetchIndicatorFromAID(aid));
        if (cardData != null) {
            Logger.v("tms_cardData --" + cardData.toString());
            Logger.v("CV Validation --" + cardData.getCardHolderAuth());
            Logger.v("getTransactionAllowed --" + cardData.getTransactionAllowed());
            Logger.v("getManualEntryEnabled --" + cardData.getManualEntryEnabled());
            if (TextUtils.isEmpty(transType)) {
                transType = ((BaseActivity) context).getCurrentMenu().getMenu_tag();
            }
            if (!(Utils.checkTMSValidation(cardData.getTransactionAllowed(), transType))) {
                Logger.v("tms_validation_done");
                isTransactionNotAllowed = true;
                flowEMVComplete(false, 0);
                return false;
            }
        } else {
            isTransactionNotAllowed = true;
            flowEMVComplete(false, 0);
            return false;
        }

        isMada = ConstantAppValue.A0000002281010.equalsIgnoreCase(Utils.fetchIndicatorFromAID(aid));
        Logger.v("IsMada_transaction_type --" + transType);
        Logger.v("IsMada --" + isMada);
        if (!(isMada) && ((transType.equalsIgnoreCase(ConstantApp.PURCHASE_NAQD) && AppConfig.EMV.consumeType == 2) || transType.equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION_EXTENSION) || transType.equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION_VOID))) {
            isCardInvalid = true;
            // showTransactionNotAllowedDialoge();
            flowEMVComplete(false, 0);
            return false;
        } else if (transType.equalsIgnoreCase(ConstantApp.CASH_ADVANCE) && isMada) {
            isCardInvalid = true;
            //    showTransactionNotAllowedDialoge();
            flowEMVComplete(false, 0);
            return false;
        } else
            isCardInvalid = false;
        String versionNum = AppManager.getInstance().getTerminalAIDVersionNumber(aid);
        Logger.v("versionNum---" + versionNum);
        RetailerDataModel retailerDataModel = AppManager.getInstance().getRetailerDataModel();
        Logger.v("retailerDataModel--" + retailerDataModel.toString());
        Logger.v("getMerchantCode(Utils.fetchIndicatorFromAID(aid)--" + AppManager.getInstance().getMerchantCode(Utils.fetchIndicatorFromAID(aid)));
        Logger.v("Utils.getFloorLimit(aid)--" + Utils.getFloorLimit(aid));

        if (transType.equalsIgnoreCase(ConstantApp.REFUND) || transType.equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION) ||
                transType.equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION_VOID) || transType.equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION_EXTENSION)
                || transType.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE) || transType.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_FULL)
                || transType.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_PARTIAL) || transType.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_MANUAL)) {
            Logger.v("refund_offline_enabled-----" + AppManager.getInstance().getRefundOfflineEnabled(Utils.fetchIndicatorFromAID(aid)));
            if (!AppManager.getInstance().getRefundOfflineEnabled(Utils.fetchIndicatorFromAID(aid))) {
                Logger.v("set_0x9F1B_getRefundOfflineEnabled");
                // setEmvData(0x9F1B, (String.format("%08d", 0))); //TODO 9f1b
            }
        }
        String vfloorLimit = (AppManager.getInstance().getFloorLimit(Utils.fetchIndicatorFromAID(aid)));
        String floorLimit1 = (String.format("%08d", Integer.parseInt(vfloorLimit)));
        // setEmvData(0x9F1B, StringUtil.intToByte4(Integer.parseInt(floorLimit1))); //TODO 9f1b
        //  setEmvData(0x9F1B, (floorLimit1));
        setEmvData(0x9A, (ByteConversionUtils.formatTranDate("yyMMdd")));
        setEmvData(0x9C, ((getCode(transType))));
        setEmvData(0xDF34, (("C400")));
//        emvModule.setEmvData(0x9C, getHextBytes(("0" + InnerProcessingCode.USING_STANDARD_PROCESSINGCODE)));
        setEmvData(0x9F21, (ByteConversionUtils.formatTranDate("hhmmss")));
        setEmvData(0x9F1A, ("0" + retailerDataModel.getTerminalCountryCode()));
//        emvModule.setEmvData(0x9F2A, getHextBytes("0" + retailerDataModel.getTerminalCurrencyCode()));
        setEmvData(0x5F2A, ("0" + retailerDataModel.getTerminalCurrencyCode()));
        // setEmvData(0x9F33, (retailerDataModel.getTerminalCapability()));
        setEmvData(0x9F40, (retailerDataModel.getAdditionalTerminalCapabilities()));
        setEmvData(0x9F35, (retailerDataModel.geteMVTerminalType()));
        setEmvData(0x9F1C, (AppManager.getInstance().getCardAcceptorID41())); // Terminal ID
        setEmvData(0x9F16, (AppManager.getInstance().getCardAcceptorCode42()));
        setEmvData(0x9F15, (AppManager.getInstance().getMerchantCode(Utils.fetchIndicatorFromAID(aid))));

        String amount = String.format("%012d", (int) (AppConfig.EMV.amountValue * 100));
        String amount1 = String.format("%08d", (int) (AppConfig.EMV.amountValue * 100));
        String cashBack = String.format("%012d", (int) (AppConfig.EMV.amtCashBack * 100));
        Logger.v("ämount--" + amount);
        Logger.v("ämount 1--" + amount1);
        Logger.v("ämount_cashback--" + cashBack);
        Logger.v("ämount_AppConfig.EMV.amtCashBack--" + AppConfig.EMV.amtCashBack);
//        emvModule.setEmvData(0x9F02, getHextBytes((amount.length() %2 == 0)?amount:"0"+amount));
        setEmvData(0x9F03, ((cashBack.length() % 2 == 0) ? cashBack : "0" + cashBack));
        Logger.v("9f03----" + ((cashBack.length() % 2 == 0) ? cashBack : "0" + cashBack));
//        emvModule.setEmvData(0x9F02, getHextBytes(amount));
//        emvModule.setEmvData(0x81, getHextBytes(amount1));
//        emvModule.setEmvData(0x9F03, getHextBytes(cashBack));

        if (transType.equalsIgnoreCase(ConstantApp.PURCHASE_NAQD)) {
            String amt = AppManager.getInstance().getMaxCashAmount(Utils.fetchIndicatorFromAID(aid));
            if (amt.trim().length() != 0) {
                double ammt = Double.parseDouble(Utils.changeAmountFormatWithDecimal(amt));
                double cashbackamount = (AppConfig.EMV.amtCashBack);
                Logger.v("Amount NAQD-- " + AppConfig.EMV.amtCashBack);
                Logger.v("Amount NAQD-- " + amt);
                Logger.v("Amount -- " + ammt);
                Logger.v("Amount -- " + cashbackamount);
                if (ammt < cashbackamount) {
                    Logger.v("Amount exceed Cashback");
                //    isAmountExceed = true;
                    isCashBackAmountExceed = true;
//                    emvTransController.doEmvFinish(false);
//                    return;
                } else
                    Logger.v("Amount else");
            } else
                Logger.v("amt else");
        }
//        String amtEnabled = AppManager.getInstance().getTransactionAmountEnabled(Utils.fetchIndicatorFromAID(aid));
        String amtEnabled = AppManager.getInstance().getTransactionAmountEnabled(ConstantAppValue.A0000002281010);
        Logger.v("amtEnabled---" + amtEnabled);
        if (Utils.checkTMSValidation(amtEnabled, transType)) {
            String amt = AppManager.getInstance().getMaxAmount(Utils.fetchIndicatorFromAID(aid));
            if (amt.trim().length() != 0) {
                double ammt = Double.parseDouble(Utils.changeAmountFormatWithDecimal(amt));
                double actualamount = (AppConfig.EMV.amountValue - AppConfig.EMV.amtCashBack);
                Logger.v("Amount -- " + AppConfig.EMV.amountValue);
                Logger.v("Amount -- " + AppConfig.EMV.amtCashBack);
                Logger.v("Amount -- " + amt);
                Logger.v("Amount -- " + ammt);
                Logger.v("Amount -- " + actualamount);
                if (ammt < actualamount) {
                    Logger.v("Amount exceed");
                    isAmountExceed = true;
                    //    flowEMVComplete(false, 0);
                    //    return;
                } else
                    Logger.v("Amount else");
            } else
                Logger.v("amt else");
        } else
            Logger.v("amt else 1");

        if (isAmountExceed || isCashBackAmountExceed) {
            stopEMVProcessThread();
            stopEMVFlow();
            flowEMVComplete(false, 0);
            return false;
        }

        String floorLimit = AppManager.getInstance().getFloorLimit(Utils.fetchIndicatorFromAID(aid));
        boolean enabled = AppManager.getInstance().isFllorLimitEnabled(Utils.fetchIndicatorFromAID(aid));
        Logger.v("floorLimit--" + floorLimit);
        Logger.v("floorLimit_enabled--" + enabled);
        boolean forceOnline = false;
        if (transType.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_FULL) || transType.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_PARTIAL)) {
            if (isMada) {
                Logger.v("Offline Disabled");
                Logger.v("set_0x9F1B_Offline Disabled");
                forceOnline = true;
                //  setEmvData(0x9F1B, (String.format("%08d", 0))); //TODO 9f1b
            } else
                Logger.v("Offline Ele disabled");
        }
        if (AppManager.getInstance().isDebugEnabled()) {
            forceOnline = true;
        }
        if (transType.equalsIgnoreCase(ConstantApp.REFUND) || transType.equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION) ||
                transType.equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION_VOID) || transType.equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION_EXTENSION)
                || transType.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE) || transType.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_FULL)
                || transType.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_PARTIAL) || transType.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_MANUAL)) {
            if (!AppManager.getInstance().getRefundOfflineEnabled(Utils.fetchIndicatorFromAID(aid))) {
                forceOnline = true;
            }
        }
        Logger.v("forceOnline --" + forceOnline);
        if (forceOnline) {
            setEmvData(0x9F1B, (String.format("%08d", 0)));
            Logger.v("");
            try {
                byte[] data9f66 /*= getEmvData(0x9F66)*/ = new byte[0];


                if (emv_is_tag_present(0x9F66) >= 0) {
                    byte[] tagData1 = new byte[100];
                    int tagLength = emv_get_tag_data(0x9F66, tagData1, tagData1.length);
                    Logger.v("0x9F66----" + StringUtil.toHexString(tagData1, 0, 3, false));
                    data9f66 = ByteConversionUtils.fetchTagValu(tagData1, 0, tagLength).getBytes();
                }
                Logger.v("data9f66 -" + data9f66[1]);
                Logger.v(data9f66);
                if (data9f66[1] == (byte) 00000000) {
                    byte[] data80 = getHextBytes("80");
                    Logger.v(data80);
                    Logger.v("data9f66 -" + data9f66[1]);
                    Logger.v("data9f66 -" + data80[0]);
                    Logger.v("data9f66 -" + data80.length);
                    data9f66[1] = data80[0];
                    Logger.v("data9f66 -" + data9f66.length);
                    Logger.v(data9f66);
//                    setEmvData(0x9f66, data9f66);
                    setEmvData(0x9f66, "80");
                }
            } catch (Exception e) {
                Logger.v("Exception e");
                e.printStackTrace();
            }
        }

        Logger.v("Trans else");
//        Logger.v("Floor Limit --" + ISOUtils.hexString(emvModule.getEmvData(0x9F1B)));
//        Logger.v("Floor Limit --" + ISOUtils.hexString(emvModule.getEmvData(0x9F33)));
//        Logger.v("Floor Limit --" + ISOUtils.hexString(emvModule.getEmvData(0x9F40)));

        setEmvData(0x9F37, ((CreatePacket.newRandonString())));
//        setEmvData(0x9F1E, (AppManager.getInstance().getVendorTerminalSerialNumber())); // Terminal Serial Num
        setEmvData(0x9F1E, ("3030303030303031")); // Terminal Serial Num

        setEmvData(0x9F09, (versionNum));
        if (aid.toLowerCase().contains("a0000000043060") || aid.toLowerCase().contains("a0000000041010")) {
            Logger.v("New Command Added for Paypass");
            if (aid.toLowerCase().contains("a0000000043060"))
                setEmvData(0x9F1D, ("4C00800000000000"));
            else if (aid.toLowerCase().contains("a0000000041010"))
                setEmvData(0x9F1D, ("6C78000000000000"));
            if (aid.toLowerCase().contains("a0000000041010") && transType.equalsIgnoreCase(ConstantApp.REFUND)) {
                Logger.v("New Condition");
                setEmvData(0xDF13, ("FFFFFFFFFF"));
            }
        }
        Logger.v("Show card --" + Utils.fetchIndicatorFromAID(aid));

        Logger.v("getemvtagvaluedata");
        String s9F37 = ByteConversionUtils.fetchTagValu(new byte[100], 0, emv_get_tag_data(0x9F37, new byte[100], new byte[100].length));
        Logger.v("Floor Limit -9F37-" + s9F37);
        Logger.v("9F33 TAG");
        if (aid.toLowerCase().contains("a0000000043060")) {
            setEmvData(0x9F33, ("E060C8"));
        } else if (aid.toLowerCase().contains("a0000000041010")) {
            setEmvData(0x9F33, ("E060C8"));
        } else {
            setEmvData(0x9F33, (retailerDataModel.getTerminalCapability()));
        }
        String data9f12 = "";
        String cardName = "";
        if (emv_is_tag_present(0x9F12) >= 0) {
            byte[] tag = new byte[100];
            int tagLength = emv_get_tag_data(0x9F12, tag, tag.length);
            Logger.v("0x9F12----" + (tag));
            cardName = new String(tag, 0, tag.length);
            Logger.v("card_name----" + cardName);
            Logger.v("0x9F12----" + ByteConversionUtils.fetchTagValu(tag, 0, tagLength));
            data9f12 = ByteConversionUtils.fetchTagValu(tag, 0, tagLength);
        }
        if (context instanceof PrintActivity) {
            Logger.v("Show Card");
//            ((PrintActivity) context).showCard(Utils.fetchIndicatorFromAID(aid),data9f12);
            ((PrintActivity) context).showCard(Utils.fetchIndicatorFromAID(aid), cardName);
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void loadEMVTags() throws InterruptedException, ISOException {
        Logger.v("loadEMVTags() called");
        if ((isScreenAvailable != 1)) {
            flowEMVComplete(false, 0);
            return;
        }
        isAppSelect = true;
        doApplePay = false;
        madeOnlineConnection_new = false;
        Logger.ve("Listner State --onFinalAppSelect");
        AppConfig.EMV.icKernalId = String.format("%02x", emv_get_kernel_id());
        Logger.v("kernal_id---" + AppConfig.EMV.icKernalId);
//        Logger.v("Listner State --getKernelId--" + String.format("%02x", context.getKernelId()));
//        Logger.v("Listner State --getKernelId--" + String.format("%02x", context.getKernelId()));
        doBeepOnce = true;
        showCard = true;
        isPinCancelled = false;
        rf_thread = 0;
        isTransactionNotAllowed = false;
        isAmountExceed = false;
        isCashBackAmountExceed = false;
        responseData55 = "";
        responseData38 = "";
        isPinRequires = 0;
        SimpleTransferListener.isSAFDecilined = false;
//        emvModule.setEmvData(0x9F02, getHextBytes("000000000800"));//You can set emv kernel data in this step
        setEmvData(0xDF24, ("F4C0F0E8AF8E62"));

        String aid = "";
        if (emv_is_tag_present(0x4F) >= 0) {
            byte[] tagData = new byte[100];
            int tagLength = emv_get_tag_data(0x4F, tagData, tagData.length);
            Logger.v("0x4F----" + (tagData));
            aid = ByteConversionUtils.fetchTagValu(tagData, 0, tagLength);
        }
        Logger.v("card_aid--" + aid);
        isGccNet = false;
        AppDatabase database = AppDatabase.getInstance(context.getApplicationContext());
        TMSCardSchemeDao cardSchemeDao = database.getTMSCardSchemeDao();
        TMSCardSchemeEntity cardData = cardSchemeDao.getCardSchemeData(Utils.fetchIndicatorFromAID(aid));
        if (cardData != null) {
            Logger.v("tms_cardData --" + cardData.toString());
            Logger.v("CV Validation --" + cardData.getCardHolderAuth());
            Logger.v("getTransactionAllowed --" + cardData.getTransactionAllowed());
            Logger.v("getManualEntryEnabled --" + cardData.getManualEntryEnabled());
            if (TextUtils.isEmpty(transType)) {
                transType = ((BaseActivity) context).getCurrentMenu().getMenu_tag();
            }
            if (!(Utils.checkTMSValidation(cardData.getTransactionAllowed(), transType))) {
                Logger.v("tms_validation_done");
                isTransactionNotAllowed = true;
                flowEMVComplete(false, 0);
                return;
            }
        } else {
            isTransactionNotAllowed = true;
            flowEMVComplete(false, 0);
            return;
        }
        if (transType.equalsIgnoreCase(ConstantApp.PURCHASE_NAQD)) {
            String amt = AppManager.getInstance().getMaxCashAmount(Utils.fetchIndicatorFromAID(aid));
            if (amt.trim().length() != 0) {
                double ammt = Double.parseDouble(Utils.changeAmountFormatWithDecimal(amt));
                double amount = (AppConfig.EMV.amtCashBack);
                Logger.v("Amount NAQD-- " + AppConfig.EMV.amtCashBack);
                Logger.v("Amount NAQD-- " + amt);
                Logger.v("Amount -- " + ammt);
                Logger.v("Amount -- " + amount);
                if (ammt < amount) {
                    Logger.v("Amount exceed Cashback");
                //    isAmountExceed = true;
                    isCashBackAmountExceed = true;
//                    emvTransController.doEmvFinish(false);
//                    return;
                } else
                    Logger.v("Amount else");
            } else
                Logger.v("amt else");
        }
//        String amtEnabled = AppManager.getInstance().getTransactionAmountEnabled(Utils.fetchIndicatorFromAID(aid));
        String amtEnabled = AppManager.getInstance().getTransactionAmountEnabled(ConstantAppValue.A0000002281010);
        Logger.v("amtEnabled---" + amtEnabled);
        if (Utils.checkTMSValidation(amtEnabled, transType)) {
            String amt = AppManager.getInstance().getMaxAmount(Utils.fetchIndicatorFromAID(aid));
            if (amt.trim().length() != 0) {
                double ammt = Double.parseDouble(Utils.changeAmountFormatWithDecimal(amt));
                double amount = (AppConfig.EMV.amountValue - AppConfig.EMV.amtCashBack);
                Logger.v("Amount -- " + AppConfig.EMV.amountValue);
                Logger.v("Amount -- " + AppConfig.EMV.amtCashBack);
                Logger.v("Amount -- " + amt);
                Logger.v("Amount -- " + ammt);
                Logger.v("Amount -- " + amount);
                if (ammt < amount) {
                    Logger.v("Amount exceed");
                    isAmountExceed = true;
                    //    flowEMVComplete(false, 0);
                    //    return;
                } else
                    Logger.v("Amount else");
            } else
                Logger.v("amt else");
        } else
            Logger.v("amt else 1");

        if (isAmountExceed || isCashBackAmountExceed) {
            stopEMVProcessThread();
            stopEMVFlow();
            flowEMVComplete(false, 0);
            return;
        }

        isMada = ConstantAppValue.A0000002281010.equalsIgnoreCase(Utils.fetchIndicatorFromAID(aid));
        Logger.v("IsMada_transaction_type --" + transType);
        Logger.v("IsMada --" + isMada);
        if (!(isMada) && ((transType.equalsIgnoreCase(ConstantApp.PURCHASE_NAQD) && AppConfig.EMV.consumeType == 2) || transType.equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION_EXTENSION) || transType.equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION_VOID))) {
            isCardInvalid = true;
            // showTransactionNotAllowedDialoge();
            flowEMVComplete(false, 0);
            return;
        } else if (transType.equalsIgnoreCase(ConstantApp.CASH_ADVANCE) && isMada) {
            isCardInvalid = true;
            //    showTransactionNotAllowedDialoge();
            flowEMVComplete(false, 0);
            return;
        } else
            isCardInvalid = false;
        String versionNum = AppManager.getInstance().getTerminalAIDVersionNumber(aid);
        Logger.v("versionNum---" + versionNum);
        RetailerDataModel retailerDataModel = AppManager.getInstance().getRetailerDataModel();
        Logger.v("retailerDataModel--" + retailerDataModel.toString());
        Logger.v("getMerchantCode(Utils.fetchIndicatorFromAID(aid)--" + AppManager.getInstance().getMerchantCode(Utils.fetchIndicatorFromAID(aid)));
        Logger.v("Utils.getFloorLimit(aid)--" + Utils.getFloorLimit(aid));

        if (transType.equalsIgnoreCase(ConstantApp.REFUND) || transType.equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION) ||
                transType.equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION_VOID) || transType.equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION_EXTENSION)
                || transType.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE) || transType.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_FULL)
                || transType.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_PARTIAL) || transType.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_MANUAL)) {
            Logger.v("refund_offline_enabled-----" + AppManager.getInstance().getRefundOfflineEnabled(Utils.fetchIndicatorFromAID(aid)));
            if (!AppManager.getInstance().getRefundOfflineEnabled(Utils.fetchIndicatorFromAID(aid))) {
                Logger.v("set_0x9F1B_getRefundOfflineEnabled");
                // setEmvData(0x9F1B, (String.format("%08d", 0))); //TODO 9f1b
            }
        }
        String vfloorLimit = (AppManager.getInstance().getFloorLimit(Utils.fetchIndicatorFromAID(aid)));
        String floorLimit1 = (String.format("%08d", Integer.parseInt(vfloorLimit)));
        // setEmvData(0x9F1B, StringUtil.intToByte4(Integer.parseInt(floorLimit1))); //TODO 9f1b
        //  setEmvData(0x9F1B, (floorLimit1));
        setEmvData(0x9A, (ByteConversionUtils.formatTranDate("yyMMdd")));
        setEmvData(0x9C, ((getCode(transType))));
        setEmvData(0xDF34, (("C400")));
//        emvModule.setEmvData(0x9C, getHextBytes(("0" + InnerProcessingCode.USING_STANDARD_PROCESSINGCODE)));
        setEmvData(0x9F21, (ByteConversionUtils.formatTranDate("hhmmss")));
        setEmvData(0x9F1A, ("0" + retailerDataModel.getTerminalCountryCode()));
//        emvModule.setEmvData(0x9F2A, getHextBytes("0" + retailerDataModel.getTerminalCurrencyCode()));
        setEmvData(0x5F2A, ("0" + retailerDataModel.getTerminalCurrencyCode()));
        // setEmvData(0x9F33, (retailerDataModel.getTerminalCapability()));
        setEmvData(0x9F40, (retailerDataModel.getAdditionalTerminalCapabilities()));
        setEmvData(0x9F35, (retailerDataModel.geteMVTerminalType()));
        setEmvData(0x9F1C, (AppManager.getInstance().getCardAcceptorID41())); // Terminal ID
        setEmvData(0x9F16, (AppManager.getInstance().getCardAcceptorCode42()));
        setEmvData(0x9F15, (AppManager.getInstance().getMerchantCode(Utils.fetchIndicatorFromAID(aid))));

        String amount = String.format("%012d", (int) (AppConfig.EMV.amountValue * 100));
        String amount1 = String.format("%08d", (int) (AppConfig.EMV.amountValue * 100));
        String cashBack = String.format("%012d", (int) (AppConfig.EMV.amtCashBack * 100));
        Logger.v("ämount--" + amount);
        Logger.v("ämount 1--" + amount1);
        Logger.v("ämount_cashback--" + cashBack);
        Logger.v("ämount_AppConfig.EMV.amtCashBack--" + AppConfig.EMV.amtCashBack);
//        emvModule.setEmvData(0x9F02, getHextBytes((amount.length() %2 == 0)?amount:"0"+amount));
        setEmvData(0x9F03, ((cashBack.length() % 2 == 0) ? cashBack : "0" + cashBack));
        Logger.v("9f03----" + ((cashBack.length() % 2 == 0) ? cashBack : "0" + cashBack));
//        emvModule.setEmvData(0x9F02, getHextBytes(amount));
//        emvModule.setEmvData(0x81, getHextBytes(amount1));
//        emvModule.setEmvData(0x9F03, getHextBytes(cashBack));

        String floorLimit = AppManager.getInstance().getFloorLimit(Utils.fetchIndicatorFromAID(aid));
        boolean enabled = AppManager.getInstance().isFllorLimitEnabled(Utils.fetchIndicatorFromAID(aid));
        Logger.v("floorLimit--" + floorLimit);
        Logger.v("floorLimit_enabled--" + enabled);
        boolean forceOnline = false;
        if (transType.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_FULL) || transType.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_PARTIAL)) {
            if (isMada) {
                Logger.v("Offline Disabled");
                Logger.v("set_0x9F1B_Offline Disabled");
                forceOnline = true;
                //  setEmvData(0x9F1B, (String.format("%08d", 0))); //TODO 9f1b
            } else
                Logger.v("Offline Ele disabled");
        }
        if (AppManager.getInstance().isDebugEnabled()) {
            forceOnline = true;
        }
        if (transType.equalsIgnoreCase(ConstantApp.REFUND) || transType.equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION) ||
                transType.equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION_VOID) || transType.equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION_EXTENSION)
                || transType.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE) || transType.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_FULL)
                || transType.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_PARTIAL) || transType.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_MANUAL)) {
            if (!AppManager.getInstance().getRefundOfflineEnabled(Utils.fetchIndicatorFromAID(aid))) {
                forceOnline = true;
            }
        }
        Logger.v("forceOnline --" + forceOnline);
        if (forceOnline) {
            setEmvData(0x9F1B, (String.format("%08d", 0)));
            Logger.v("");
            try {
                byte[] data9f66 /*= getEmvData(0x9F66)*/ = new byte[0];


                if (emv_is_tag_present(0x9F66) >= 0) {
                    byte[] tagData1 = new byte[100];
                    int tagLength = emv_get_tag_data(0x9F66, tagData1, tagData1.length);
                    Logger.v("0x9F66----" + StringUtil.toHexString(tagData1, 0, 3, false));
                    data9f66 = ByteConversionUtils.fetchTagValu(tagData1, 0, tagLength).getBytes();
                }
                Logger.v("data9f66 -" + data9f66[1]);
                Logger.v(data9f66);
                if (data9f66[1] == (byte) 00000000) {
                    byte[] data80 = getHextBytes("80");
                    Logger.v(data80);
                    Logger.v("data9f66 -" + data9f66[1]);
                    Logger.v("data9f66 -" + data80[0]);
                    Logger.v("data9f66 -" + data80.length);
                    data9f66[1] = data80[0];
                    Logger.v("data9f66 -" + data9f66.length);
                    Logger.v(data9f66);
//                    setEmvData(0x9f66, data9f66);
                    setEmvData(0x9f66, "80");
                }
            } catch (Exception e) {
                Logger.v("Exception e");
                e.printStackTrace();
            }
        }

        Logger.v("Trans else");
//        Logger.v("Floor Limit --" + ISOUtils.hexString(emvModule.getEmvData(0x9F1B)));
//        Logger.v("Floor Limit --" + ISOUtils.hexString(emvModule.getEmvData(0x9F33)));
//        Logger.v("Floor Limit --" + ISOUtils.hexString(emvModule.getEmvData(0x9F40)));

        setEmvData(0x9F37, ((CreatePacket.newRandonString())));
//        setEmvData(0x9F1E, (AppManager.getInstance().getVendorTerminalSerialNumber())); // Terminal Serial Num
        setEmvData(0x9F1E, ("3030303030303031")); // Terminal Serial Num

        setEmvData(0x9F09, (versionNum));
        if (aid.toLowerCase().contains("a0000000043060") || aid.toLowerCase().contains("a0000000041010")) {
            Logger.v("New Command Added for Paypass");
            if (aid.toLowerCase().contains("a0000000043060"))
                setEmvData(0x9F1D, ("4C00800000000000"));
            else if (aid.toLowerCase().contains("a0000000041010"))
                setEmvData(0x9F1D, ("6C78000000000000"));
            if (aid.toLowerCase().contains("a0000000041010") && transType.equalsIgnoreCase(ConstantApp.REFUND)) {
                Logger.v("New Condition");
                setEmvData(0xDF13, ("FFFFFFFFFF"));
            }
        }
        Logger.v("Show card --" + Utils.fetchIndicatorFromAID(aid));

        Logger.v("getemvtagvaluedata");
        String s9F37 = ByteConversionUtils.fetchTagValu(new byte[100], 0, emv_get_tag_data(0x9F37, new byte[100], new byte[100].length));
        Logger.v("Floor Limit -9F37-" + s9F37);
        Logger.v("9F33 TAG");
        if (aid.toLowerCase().contains("a0000000043060")) {
            setEmvData(0x9F33, ("E060C8"));
        } else if (aid.toLowerCase().contains("a0000000041010")) {
            setEmvData(0x9F33, ("E060C8"));
        } else {
            setEmvData(0x9F33, (retailerDataModel.getTerminalCapability()));
        }
        String data9f12 = "";
        String cardName = "";
        if (emv_is_tag_present(0x9F12) >= 0) {
            byte[] tag = new byte[100];
            int tagLength = emv_get_tag_data(0x9F12, tag, tag.length);
            Logger.v("0x9F12----" + (tag));
            cardName = new String(tag, 0, tag.length);
            Logger.v("card_name----" + cardName);
            Logger.v("0x9F12----" + ByteConversionUtils.fetchTagValu(tag, 0, tagLength));
            data9f12 = ByteConversionUtils.fetchTagValu(tag, 0, tagLength);
        }
        if (context instanceof PrintActivity) {
            Logger.v("Show Card");
//            ((PrintActivity) context).showCard(Utils.fetchIndicatorFromAID(aid),data9f12);
            ((PrintActivity) context).showCard(Utils.fetchIndicatorFromAID(aid), cardName);
        }
        moveNextFlow();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private byte[] getHextBytes(String data) {
        return ISOUtil.hex2byte(data);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean applePayCondition(String kernalid) {
        //  String kernal = String.format("%02x", kernalid);
        String kernal = kernalid;
        if (transType.equalsIgnoreCase(ConstantApp.REFUND) && kernal.equalsIgnoreCase("2d")) {
            try {
                if (emv_is_tag_present(0x95) >= 0) {
                    byte[] TVR = new byte[5];
                    int tagLength = emv_get_tag_data(0x95, TVR, TVR.length);
                    Logger.v("0x95----" + (TVR));
                    Logger.v("0x95----" + ByteConversionUtils.fetchTagValu(TVR, 0, tagLength));
                    //  String data95 = ByteConversionUtils.fetchTagValu(TVR, 0, tagLength);
                    // String data95 = ISOUtil.hexString(emvModule.getEmvData(0x95));
                    String data95 = ISOUtil.hexString(TVR);
                    if (!checkRiskManagement(data95)) {
                        doApplePay = true;
                        Logger.v("doApplePay =true");
                        return true;
                    }
                }

            } catch (Exception e) {
                Logger.v("applepay exception");
            }
        }
        return false;
    }


    //for refund

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void proccedApplePayFlow() throws InterruptedException, ISOException {
        madeOnlineConnection = true;
        madeOnlineConnection_new = true;
        Logger.v("applePay--");

        String aid = "";
        if (emv_is_tag_present(0x4F) >= 0) {
            byte[] tagData = new byte[100];
            int tagLength = emv_get_tag_data(0x4F, tagData, tagData.length);
            Logger.v("0x4F_aid----" + (tagData));
            aid = ByteConversionUtils.fetchTagValu(tagData, 0, tagLength);
            isGccNet = getCurrencyCode(tagData);
        }
        Logger.v("card_aid--" + aid);
//        isGccNet = getCurrencyCode(tagData);
        Logger.v("currencyCode --" + isGccNet);
        String resultMsg = null;
        int[] emvTags = new int[5];
        emvTags[0] = 0x5a;
        emvTags[1] = 0x5F34;
        emvTags[2] = 0x5f24;
        emvTags[3] = 0x57;
        emvTags[4] = 0x81;
        String cardNo = "";
        String cardSN = "";// Card serial number == context.getCardSequenceNumber()
        String track2 = ""; // Two track data == context.getTrack_2_eqv_data()


        if (emv_is_tag_present(0x5A) >= 0) {
            byte[] tagData = new byte[100];
            int tagLength = emv_get_tag_data(0x5A, tagData, tagData.length);
            Logger.v("0x5A----" + (tagData));
            Logger.v("setPAN----" + ByteConversionUtils.fetchTagValu(tagData, 0, tagLength));
            cardNo = ByteConversionUtils.fetchTagValu(tagData, 0, tagLength);
        }
        if (emv_is_tag_present(0x5F34) >= 0) {
            byte[] tagData = new byte[100];
            int tagLength = emv_get_tag_data(0x5F34, tagData, tagData.length);
            Logger.v("0x5F34----" + ByteConversionUtils.fetchTagValu(tagData, 0, tagLength));
            Logger.v("setCSN----------" + tagData[0]);
            cardSN = ByteConversionUtils.fetchTagValu(tagData, 0, tagLength);
        }
        // Track2
        if (emv_is_tag_present(0x57) >= 0) {
            byte[] tagData = new byte[100];
            int tagLength = emv_get_tag_data(0x57, tagData, tagData.length);
            Logger.v("0x57----" + ByteConversionUtils.fetchTagValu(tagData, 0, tagLength));
            Logger.v("setTrack2Data------" + ByteConversionUtils.fetchTagValu(tagData, 0, tagLength));
            track2 = ByteConversionUtils.fetchTagValu(tagData, 0, tagLength);
        }
        if (track2 == null || track2.trim().length() == 0) {
            try {
                track2 = ByteConversionUtils.fetchTagValu(new byte[100], 0, emv_get_tag_data(0x9fb8, new byte[100], new byte[100].length));

//                track2 = ISOUtils.hexString(emvModule.getEmvData(0x9fb8));
            } catch (Exception e) {
                Logger.v("Exception track2");
            }
        }

        if (null == cardNo && track2 != null) {
            cardNo = track2.substring(0, track2.indexOf('D'));
        }
        String expiredDate = null;
        if (track2 != null) {
            expiredDate = track2.substring(track2.indexOf('D') + 1, track2.indexOf('D') + 5);
        }
        if (cardSN == null) {
            cardSN = "000";
        } else {
            cardSN = ISOUtil.padleft(cardSN, 3, '0');
        }

        if (cardSN == null) {
            try {
                //String aid = ByteConversionUtils.byteArrayToHexString(emvTransInfo.getAid(), emvTransInfo.getAid().length, false);
                Logger.v("CARD SN -" + aid);
                if (aid.toUpperCase().contains("A000000004")) {
                    cardSN = "";
                } else {
                    cardSN = "000";
                }
            } catch (Exception e) {
                cardSN = "000";
            }

        } else {
            cardSN = ISOUtil.padleft(cardSN, 3, '0');
        }
        String serviceCode = "";
        if (null != track2) {
            serviceCode = track2.substring(track2.indexOf('D') + 5, track2.indexOf('D') + 8);
        }
        //Since the array is BCD encoded, the last digit of the card number needs to be removed if it is 'F'.
        if (null != cardNo && cardNo.endsWith("F"))
            cardNo = cardNo.substring(0, cardNo.length() - 1);
        AppConfig.EMV.icCardNum = cardNo;
        Logger.v("AppConfig.EMV.icCardSerialNum11 -" + cardSN);
        //Logger.v("emvTransInfo.getCvm() -" + emvTransInfo.getCvm());
        AppConfig.EMV.icCardSerialNum = cardSN;
        AppConfig.EMV.icCardTrack2data = track2;
        if (expiredDate == null || expiredDate.trim().length() == 0) {
            // Expiry
            if (emv_is_tag_present(0x5F24) >= 0) {
                byte[] tagData = new byte[100];
                int tagLength = emv_get_tag_data(0x5F24, tagData, tagData.length);
                Logger.v("0x5F24----" + StringUtil.toHexString(tagData, 0, 3, false).substring(0, 4));
                AppConfig.EMV.icExpiredDate = StringUtil.toHexString(tagData, 0, 3, false).substring(0, 4);
            }
        } else {
            AppConfig.EMV.icExpiredDate = expiredDate;
        }

        byte[] iccData = new byte[1200];
        int iccDataLength = emv_get_tag_list_data(L_55TAGS, L_55TAGS.length, iccData, iccData.length);
        Logger.v("iccDataLength -" + iccDataLength);
        // Logger.v("Ic Data --" + new String(iccData)); //not readable
        String rawData = ByteConversionUtils.fetchTagValu(iccData, 0, iccDataLength);
        Logger.v("tag55data---" + rawData);
        String data55 = (rawData.trim().length() % 2 != 0) ? rawData + "0" : rawData;
        // String data55 = emvModule.fetchEmvData(L_55TAGS);
        Logger.v("MyData55 --" + data55);
        AppConfig.EMV.ic55Data = data55;// Trans info

        if (AppConfig.EMV.consumeType == 2) {
            Logger.v("Inside RF --");
            HashMap<String, String> tag55 = Utils.getParsedTag55(ic55Data);
            String tagoffLine = tag55.get(TAG55[2]);
            Logger.v("tagoffLine -" + tagoffLine);
            Logger.v("madeOnlineConnection -" + madeOnlineConnection);
            String tagOnline = tag55.get(TAG55[14]);
            Logger.v("TAG Online--" + tagOnline);

            String cvm = "";
            if (emv_is_tag_present(0x9F34) >= 0) {
                byte[] tagData = new byte[100];
                int tagLength = emv_get_tag_data(0x9F34, tagData, tagData.length);
                Logger.v("0x9F34----" + StringUtil.toHexString(tagData, 0, 3, false).substring(0, 4));
                cvm = StringUtil.toHexString(tagData, 0, 3, false).substring(0, 2);
            }
            Logger.v("TAG Online--" + cvm);
            // TODO  waitForPinBlock(2);
           /* if (!(data55.contains("9f34") || data55.contains("9F34")))
                AppConfig.EMV.ic55Data = data55 + "9F3403" + appendCVM(cvm.getBytes());// Trans info*/
            Logger.v("AppConfig.EMV.ic55Data --" + AppConfig.EMV.ic55Data);
            // boolean promtPin = ((cvm == EmvConst.OP_ONLINE_PIN));
            boolean promtPin = ((cvm.equalsIgnoreCase("02"))); //02 online pin for contactless
            Logger.v("TAG Online--" + promtPin);
            if (promtPin) {
                isPinRequires = 1;
                rf_thread = 1;
                if ((isScreenAvailable == 1))
                    //MapperFlow.getInstance().startOnlinePinInput(context, cardSN);
                    waitForPinBlock(9999);
                return;
              /*  waitPinInputThreat_apple_pin.waitForRslt();
              //  showMessage(context.getString(R.string.msg_pwd) + (pinBlock == null ? "null" : ISOUtil.hexString(pinBlock)), MessageTag.DATA, 29);
                if (pinBlock == null)
                    isPinRequires = 0;
                else
                    isPinRequires = 1;

                if (isMada && (pinBlock == null || pinBlock.length == 0)) {
                    isPinCancelled = true;
                }

                if (isPinCancelled) {
                    connection.onConnect(DO_ERROR_PIN_CANCELLED);
                    return;
                }*/

            }
        } else {
            Logger.v("Inside RF -else-");
        }
        isEMVCompleted = false;
        sendOnLineRequest();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean getCurrencyCode(byte[] getAid) {
        //  private boolean getCurrencyCode(String getAid) {
        String aid = ByteConversionUtils.byteArrayToHexString(getAid, getAid.length, false);
//        String aid = getAid;
        Logger.v("aid--" + aid);
        if (!isMada) {
            if (transType.equalsIgnoreCase(ConstantApp.REFUND) || transType.equalsIgnoreCase(ConstantApp.PURCHASE)) {
                int country = 0;
                byte[] countrycode = new byte[100];
                byte[] countrycode1 = new byte[100];
                if (emv_is_tag_present(0x5F28) >= 0) {

                    int tagLength = emv_get_tag_data(0x4F, countrycode, countrycode.length);
                    Logger.v("0x4F----" + (countrycode));
                    String countrycodes = ByteConversionUtils.fetchTagValu(countrycode, 0, tagLength);
                }
                if (emv_is_tag_present(0x5F28) >= 0) {

                    int tagLength = emv_get_tag_data(0x4F, countrycode1, countrycode1.length);
                    Logger.v("0x4F----" + (countrycode1));
                    String countrycode1s = ByteConversionUtils.fetchTagValu(countrycode1, 0, tagLength);
                }
                // byte[] countrycode = sdkDevice.getEmvModuleType().getEmvData(0x5F28);
                // byte[] countrycode1 = sdkDevice.getEmvModuleType().getEmvData(0x9F42);
                if (countrycode != null && countrycode.length != 0) {
                    country = Integer.parseInt(ISOUtil.hexString(countrycode));
                    Logger.v(ISOUtil.hexString(countrycode));
                    Logger.v("CurrencyCode - ");
                } else if (countrycode1 != null && (countrycode1.length != 0)) {
                    country = Integer.parseInt(ISOUtil.hexString(countrycode1));
                    Logger.v(ISOUtil.hexString(countrycode1));
                } else
                    Logger.v("Currency else");
                if (country == 48 || country == 414 || country == 512 || country == 634 || country == 784) {
                    return true;
                }
            }
        }
        return false;
    }

    private String appendCVM(String cvm) {

        switch (cvm) {
            case ConstantApp.CVM_ONLINE_PIN_VALUE:
                return ConstantApp.CVM_ONLINE_PIN_VALUE;
            case ConstantApp.CVM_NO:
                return ConstantApp.CVM_NO;
        }
        return ConstantApp.CVM_SIGNATURE;
    }

    private boolean checkRiskManagement(String data95) {
        try {
            Logger.v("data95 --" + data95);
            if (data95.substring(0, 2).equalsIgnoreCase("04") || data95.substring(0, 2).equalsIgnoreCase("40")
                    || data95.substring(2, 4).equalsIgnoreCase("40") || data95.substring(2, 4).equalsIgnoreCase("20")) {
                Logger.v("risk true");
                return true;
            }
        } catch (Exception e) {
            Logger.v("risk management error");
        }
        Logger.v("risk fale");
        return false;
    }


    private void selectAids() throws InterruptedException {
        byte[] aidList = new byte[300];
        int aidNumber = emv_get_candidate_list(aidList, aidList.length);
        Logger.v("AID number --" + aidNumber);
        List<String> nameList = new ArrayList<String>();
        boolean isMada = false;
        int offset = 0;
        for (int i = 0; i < aidNumber; i++) {
            Logger.v("Position --" + i);
            byte textLength = aidList[offset];
            Logger.v("textLength-----" + textLength);

            byte[] text = new byte[textLength];
            Logger.v(StringUtil.toHexString(text, false));
            Logger.v("texthesstring-----" + StringUtil.toHexString(text, false));
            System.arraycopy(aidList, offset + 1, text, 0, text.length);
            Logger.v("text------" + text);
            offset += (1 + textLength);
            String cardName = StringUtil.toString(text);
            nameList.add(cardName);
            Logger.v("items----" + cardName);

            if (cardName.contains("mada")) {
                emv_set_candidate_list_result(i);
                moveNextFlow();
                return;
            }
        }
        if (nameList.size() == 1) {
            emv_set_candidate_list_result(0);
            moveNextFlow();
            return;
        }

        Logger.v("aidList.size() --" + nameList.size());
        ((PrintActivity) context).showAppSection(nameList);
        waitSelectApp.waitForRslt();

        if (0 <= selectApplication) {
            selectAppCancelled = false;
            emv_set_candidate_list_result(selectApplication);
        } else {
            selectAppCancelled = true;
//            emv_set_candidate_list_result(0);
        }
        moveNextFlow();
    }


    private void onlinePinFlow(final boolean moveNext) {
        emv_set_online_pin_entered(1);
        final int PINPAD_CANCEL = -65792;
        final int PINPAD_TIMEOUT = -65538;
        new Thread() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                byte[] pinBlock = new byte[8];
//                byte[] zeroPAN = new byte[]{'0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0'};
                String cardNo = "";
                if (emv_is_tag_present(0x5A) >= 0) {
                    byte[] tagData = new byte[100];
                    int tagLength = emv_get_tag_data(0x5A, tagData, tagData.length);
                    Logger.v("0x5A----" + (tagData));
                    Logger.v("setPAN----" + ByteConversionUtils.fetchTagValu(tagData, 0, tagLength));
                    cardNo = ByteConversionUtils.fetchTagValu(tagData, 0, tagLength);
                }
                byte[] pan = cardNo.getBytes();
//                // masterKey is new byte[]{'1','1','1','1','1','1','1','1' }
//                //Q1上不支持单倍长PINKEY
//                byte[] defaultPINKey = new byte[]{'2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2'};
//
                int ret = -1;
                if (pinpadOpened == false) {
                    int close = PinPadInterface.close();
                    Logger.v("close --" + close);
                    int opened = PinPadInterface.open();
                    Logger.v("opened --" + opened);
                    if (opened < 0) {
                        Logger.v("PinPad error");
                        return;
                    }
                    pinpadOpened = true;
//                    if (appState.pinpadType == PINPAD_CUSTOM_UI) {
//                        PinPadInterface.setupCallbackHandler(InputOnlinePINActivity.this);
//                    }
                }

//                PINPadExtendDevice device = SDKDevice.getInstance(context).getDeviceConnectPin();
//                KeyInfo keyInfo = new KeyInfo(PINPadDevice.KEY_TYPE_TDUKPT, 1, AlgorithmConstants.ALG_3DES);
//                try {
//                    device.listenForPinBlock(keyInfo, cardNo, true, new OperationListener() {
//                        @Override
//                        public void handleResult(OperationResult operationResult) {
//                            operationResult.toString();
//
//                        }
//                    },3000);
//                } catch (DeviceException e) {
//                    e.printStackTrace();
//                }

                //Q1上不支持单倍长PINKEY
                PinPadInterface.setKey(2, 1, 0, SINGLE_KEY);
//    		PinPadInterface.setKey(2, appState.terminalConfig.getKeyIndex(), 0, appState.terminalConfig.getKeyAlgorithm());
                PinPadInterface.setFlagAllowBypass(0);
                Logger.v("Pin Before");
                ret = PinPadInterface.inputPIN(pan, pan.length, pinBlock, 60000, 0);
                Logger.v("Pin Result --" + ret);
                if (ret < 0) {
                    if (ret == PINPAD_CANCEL) {
                        isPinCancelled = true;
                    } else if (ret == PINPAD_TIMEOUT) {
                        isPinCancelled = true;
                    } else {
                        isPinCancelled = true;
                    }
                    PinPadInterface.close();
                    pinpadOpened = false;
                    return;
                }
                if (ret == 0) {
                    SimpleTransferListener.isPinRequires = 0;
                } else {
                    SimpleTransferListener.isPinRequires = 1;
                    AppConfig.EMV.pinBlock = (pinBlock);
                    Logger.v("pinBlock --" + new String(pinBlock));
                    Logger.v("pinBlock --" + ByteConversionUtils.byteArrayToHexString(pinBlock));
                }
                PinPadInterface.close();
                pinpadOpened = false;
                if (moveNext)
                    moveNextFlow();
                else
                    hostPinRetry();

            }
        }.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void waitForPinBlock(int moveNext) {
        Logger.v("pinpad---" + moveNext);
        PINPadExtendDevice device = SDKDevice.getInstance(context).getDeviceConnectPin();

        try {
            device.setPINLength(4,6);
            device.setGUIConfiguration(2, ("Enter your PIN   أدخل الرقم السري").getBytes());
            //   emv_set_pinpad_title(("Please Enter Your PIN1").getBytes(),2);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        KeyInfo keyInfo = new KeyInfo(PINPadDevice.KEY_TYPE_TDUKPT, 1, AlgorithmConstants.ALG_3DES);
        try {
            String cardNo = "";
            if (emv_is_tag_present(0x5A) >= 0) {
                byte[] tagData = new byte[100];
                int tagLength = emv_get_tag_data(0x5A, tagData, tagData.length);
                Logger.v("0x5A----" + (tagData));
                Logger.v("setPAN----" + ByteConversionUtils.fetchTagValu(tagData, 0, tagLength));
                cardNo = ByteConversionUtils.fetchTagValu(tagData, 0, tagLength);
            }
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
                } else {
                    isPinCancelled = true;
                }
            } else if (operationResult.getResultCode() == OperationResult.SUCCESS) {
                Logger.v("Pin Bypass");
            } else {
                isPinCancelled = true;
                Logger.v("waitForPinBlock fail,");
            }
            if (isPinCancelled) {
                flowEMVComplete(false, 0);
            } else if (moveNext == 2) {
                sendOnLineRequest();
            } else if (moveNext == 0) {
                moveNextFlow();
            } else if (moveNext == 3)
                hostPinRetry();
            else if (moveNext == 9999) {
                isEMVCompleted = false;
                sendOnLineRequest();

            }

        } catch (DeviceException | InterruptedException | ISOException e) {
            e.printStackTrace();
            Logger.v("waitForPinBlock fail," + e.getMessage());
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void flowEMVComplete(boolean isFlowSuccess, int result) throws ISOException, InterruptedException {
        Logger.v("flowEMVComplete----" + isFlowSuccess);
        Logger.v("emv_result-----" + result);
        Logger.ve("Listner ended");
        Logger.v("selectAppCancelled --" + selectAppCancelled);
        Logger.v("isPinCancelled --" + isPinCancelled);
        Logger.v("isScreenAvailable --" + isScreenAvailable);
        Logger.v("madeOnlineConnection --" + madeOnlineConnection);

        if (isAmountExceed || isCashBackAmountExceed) {
            Logger.v("isAmountExceed");
            connection.onConnect(DO_AMOUNT_EXCEED);
            return;
        }

        if (selectAppCancelled || isPinCancelled || ((isScreenAvailable != 1) && !madeOnlineConnection)) {
            connection.onConnect(DO_CANCEL_AND_MOVE);
            return;
        }
        showPinOkay();
        isEMVCompleted = true;
        Logger.v("Listner State --onEmvFinished");
        AppConfig.EMV.icKernalId = String.format("%02x", emv_get_kernel_id());
        Logger.v("kernal_id_-----" + AppConfig.EMV.icKernalId);
        Logger.v("kernal_id_origin-----" + emv_get_kernel_id());
        if (isCardInvalid) {
            showTransactionNotAllowedDialoge();
            //  connection.onConnect(DO_CARD_INVALID);
            Logger.v("isCardInvalid");
            return;
        }


        if (isTransactionNotAllowed) {
            connection.onConnect(DO_TRANSACTION);
            Logger.v("isTransactionNotAllowed");
            return;
        }
        fetchCardInfo("flowEMVComplete");

        String cardNo = AppConfig.EMV.icCardSerialNum;
        if (cardNo == null) {
            if (AppConfig.EMV.consumeType == 2) {
                try {
                    String aid = "";
                    if (emv_is_tag_present(0x4F) >= 0) {
                        byte[] tagData = new byte[100];
                        int tagLength = emv_get_tag_data(0x4F, tagData, tagData.length);
                        Logger.v("0x4F----" + (tagData));
                        aid = ByteConversionUtils.fetchTagValu(tagData, 0, tagLength);
                    }
                    Logger.v("card_aid--" + aid);
                    Logger.v("Exception aid -" + aid);
                    if (aid.trim().length() != 0) {
                        Logger.v("Step 1");
                        if (aid.substring(0, 10).equalsIgnoreCase("A000000003")) {
                            Logger.v("Step 2");
                            connection.onConnect(Do_PLZ_SEE_PHONE);
                            return;
                        }
                    }
                } catch (Exception e) {

                }
                connection.onConnect(DO_WAVE_AGAIN);
            } else
                connection.onConnect(DO_ERROR);
            Logger.v("Case 8");
            return;

        }
        int i = emv_get_kernel_id();
        Logger.v("emv_get_kernel_id---" + String.format("%02x", i));
        if ((result != APPROVE_OFFLINE)
                // TODO  && applePayCondition(String.format("%02x", emvTransInfo.getKernelId()))
                && applePayCondition(AppConfig.EMV.icKernalId)
                && AppConfig.EMV.consumeType == 2) {
            proccedApplePayFlow();
            return;
        } else {
            Logger.v("Applepay else");
        }
        doBeepOnce();
        if (AppConfig.EMV.consumeType == 2 && !madeOnlineConnection) {
            new LightsDisplay(this.context).showAllLights();
            /*spi = SoundPoolImpl.getInstance();
            spi.initListner(context, new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                    Logger.v("Sound listner 001");
                    spi.play();
                }
            });*/
           /* try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            new LightsDisplay(this.context).turnOffAllLights();*/
        }

//        if ((context instanceof PrintActivity && !isCancelled) || (isCancelled && !madeOnlineConnection && responseData38.trim().equalsIgnoreCase("5933"))) {
        if (context instanceof PrintActivity && (SimpleTransferListener.isScreenAvailable == 1)) {
            Logger.v("show_status_dialog_simpletransfer");
            ((PrintActivity) context).showStatusDiaolge((result == APPROVE_OFFLINE) || (result == APPROVE_ONLINE), isFlowSuccess, madeOnlineConnection);
            Thread.sleep(1000);
        } else
            Logger.v("showStatusDiaolge else");
//        if (emvTransInfo.getOpenCardType() == ModuleType.COMMON_RFCARDREADER) {
//            Logger.v("Inside RF --");
//            HashMap<String, String> tag55 = Utils.getParsedTag55(AppConfig.EMV.ic55Data);
//            String tagOnline = tag55.get(TAG55[14]);
//            Logger.v("TAG Online--" + tagOnline);
//            Logger.v("TAG Online--" + emvTransInfo.getCvm());
//            if (!(data55.contains("9f34") || data55.contains("9F34")))
//                AppConfig.EMV.ic55Data = data55 + "9F3403" + appendCVM(emvTransInfo.getCvm());// Trans info
//            Logger.v("AppConfig.EMV.ic55Data --" + AppConfig.EMV.ic55Data);
//        }
        String data55 = AppConfig.EMV.ic55Data;
        if (AppConfig.EMV.consumeType == 2) {
            Logger.v("Inside RF --");
            HashMap<String, String> tag55 = Utils.getParsedTag55(AppConfig.EMV.ic55Data);
            String tagOnline = tag55.get(TAG55[14]);
            Logger.v("TAG Online--" + tagOnline);
            //  Logger.v("TAG Online--" + emvTransInfo.getCvm());
//            if (cvm9f34.equalsIgnoreCase("000000"))
//                AppConfig.EMV.ic55Data = data55 + "9F3403" + appendCVM(cvm9f34);// Trans info
//            cvm9f34 = appendCVM(cvm9f34);
//            Logger.v("CVM9f34 cless:"+ cvm9f34);
            Logger.v("AppConfig.EMV.ic55Data --" + AppConfig.EMV.ic55Data);
        }
        Logger.v("emvTransInfo.getCardNo() --" + cardNo);
        showCard();
        Logger.v("isCancelled " + isCancelled);
        Logger.v("isPinRequires " + isPinRequires);
        Logger.v("isPinCancelled " + isPinCancelled);
        Logger.v("Pin " + (pinBlock != null));
//        if (!isCancelled && (!isPinRequires || (pinBlock != null)))
        HashMap<String, String> tag55 = Utils.getParsedTag55(ic55Data);
        String tagoffLine = tag55.get(TAG55[2]);
        Logger.v("tagoffLine -" + tagoffLine);
        Logger.v("madeOnlineConnection -" + madeOnlineConnection);
        Logger.v("AppConfig.EMV.enableDatabaseUpdate --" + AppConfig.EMV.enableDatabaseUpdate);
        if (AppConfig.EMV.enableDatabaseUpdate) {
            if (AppManager.getInstance().isDebugEnabled()) {
                TransactionModelEntity debugTrans = AppManager.getInstance().getDebugTransactionModelEntity();
                debugTrans.setStatusTransaction(1);
                debugTrans.setIccCardSystemRelatedData55_final(/*Utils.encrypt*/(ic55Data));
                AppManager.getInstance().setDuplicateTransactionModelEntity(debugTrans);
                AppManager.getInstance().setDenugTransactionModelEntity(debugTrans);
            } else {
                AppDatabase database = AppDatabase.getInstance(context.getApplicationContext());
                TransactionModelEntity transaction = database.getTransactionDao().getLastTransaction(false);
                if (isScreenAvailable == 1) {
                    database.getTransactionDao().updateResponse(transaction.getUid(), isFlowSuccess ? 1 : 0);
                    AppManager.getInstance().setDuplicateTransactionModelEntity(database.getTransactionDao().getTransaction(transaction.getUid()));
                } else
                    database.getTransactionDao().updateResponse(transaction.getUid(), 0);
                database.getTransactionDao().updateData55(transaction.getUid(), ic55Data);
            }
        }
        Logger.v("responce", "Listner ended 1");

        String data8a = null;
        //The specific reason for the error code
        Logger.v("ErrorCode--" + errorCode);
        if (responseData55.trim().length() != 0) {
            if (responseData55.substring(0, 2).equalsIgnoreCase("8a")) {
//                    ISOUtils.hex2byte(responseData55));
//                    data8a = tlv1.getString(0x8a);
                data8a = ("3030");
                Logger.v("data8a = (\"3030\");");
            } else {
                if (AppManager.getInstance().getDe39().equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_000)
                        || AppManager.getInstance().getDe39().equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_001)
                        || AppManager.getInstance().getDe39().equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_003)
                        || AppManager.getInstance().getDe39().equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_007))
                    data8a = ("3030");// 0x8a Transaction reply code:Taken from the 39 field value of unionpay 8583 specification, this parameter is populated with the actual value of the transaction.
            }
        }
        if (errorCode != 0) {
            if (madeOnlineConnection && data8a != null && data8a.trim().equalsIgnoreCase("3030")) {
                Logger.v("REversal 1");
                if (AppConfig.EMV.enableDatabaseUpdate) {
                    AppDatabase database = AppDatabase.getInstance(context.getApplicationContext());
                    TransactionModelEntity transaction = database.getTransactionDao().getLastTransaction(false);
                    database.getTransactionDao().updateResponse39(transaction.getUid(), "190");
                    AppManager.getInstance().setDuplicateTransactionModelEntity(database.getTransactionDao().getTransaction(transaction.getUid()));
                }
                connection.onConnect(DO_REVERSAL);
                Logger.v("Case 9");
                return;
            }
            Logger.v("Error 1--" + madeOnlineConnection);
            Logger.v("Case 10");
//                connection.onConnect(DO_ERROR);
            if (madeOnlineConnection) {
                if (AppConfig.EMV.enableDatabaseUpdate) {
                    if (AppManager.getInstance().getDe39().equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_000)
                            || AppManager.getInstance().getDe39().equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_001)
                            || AppManager.getInstance().getDe39().equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_003)
                            || AppManager.getInstance().getDe39().equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_007)) {
                        AppDatabase database = AppDatabase.getInstance(context.getApplicationContext());
                        TransactionModelEntity transaction = database.getTransactionDao().getLastTransaction(false);
                        database.getTransactionDao().updateResponse39(transaction.getUid(), "190");
                        AppManager.getInstance().setDuplicateTransactionModelEntity(database.getTransactionDao().getTransaction(transaction.getUid()));
                        connection.onConnect(DO_REVERSAL);
                        return;
                    }
                }
                connection.onConnect(FINAL_ERROR);
            } else
                connection.onConnect(DO_SAF_DECLINED);
            return;
        } else
            Logger.v("No Error");

        if (isPinCancelled) {
            Logger.v("Case 1");
            connection.onConnect(DO_ERROR_PIN_CANCELLED);
        } else if (isSAFDecilined && (transType.equalsIgnoreCase(ConstantApp.REFUND) || transType.equalsIgnoreCase(ConstantApp.PURCHASE) || transType.equalsIgnoreCase(ConstantApp.CASH_ADVANCE))) {
            Logger.v("Case 2");
            CountDownResponseTimer.cancelTimer(999);
            if (result == APPROVE_OFFLINE)
                connection.onConnect(DO_SAF_APPROVED);
            else
                connection.onConnect(DO_SAF_REJECTED);
        } else if (tagoffLine != null && (tagoffLine.equalsIgnoreCase("00") || tagoffLine.equalsIgnoreCase("80") || result == DECLINE_OFFLINE) && !madeOnlineConnection) {
            Logger.v("Case 3");
            CountDownResponseTimer.cancelTimer(888);
            connection.onConnect(DO_SAF_DECLINED);
        } else if ((isFlowSuccess) && ((transType.equalsIgnoreCase(ConstantApp.REFUND) && !madeOnlineConnection) || (tagoffLine != null && tagoffLine.equalsIgnoreCase("40") && !madeOnlineConnection))) {
            Logger.v("Case 4");
            CountDownResponseTimer.cancelTimer(777);
            connection.onConnect(DO_SAF);
        } else if (doReversal) {
            Logger.v("Case 5");
            if (AppConfig.EMV.enableDatabaseUpdate) {
                AppDatabase database = AppDatabase.getInstance(context.getApplicationContext());
                TransactionModelEntity transaction = database.getTransactionDao().getLastTransaction(false);
                database.getTransactionDao().updateResponse39(transaction.getUid(), "190");
                AppManager.getInstance().setDuplicateTransactionModelEntity(database.getTransactionDao().getTransaction(transaction.getUid()));
            }
            connection.onConnect(DO_REVERSAL);
        } else if ((isFlowSuccess) && (madeOnlineConnection && ((result == DECLINE_OFFLINE) && isRequestSuccess()))) {
            if (AppConfig.EMV.enableDatabaseUpdate) {
                AppDatabase database = AppDatabase.getInstance(context.getApplicationContext());
                TransactionModelEntity transaction = database.getTransactionDao().getLastTransaction(false);
                database.getTransactionDao().updateResponse39(transaction.getUid(), "190");
                AppManager.getInstance().setDuplicateTransactionModelEntity(database.getTransactionDao().getTransaction(transaction.getUid()));
            }
            Logger.v("Case 6");
            connection.onConnect(DO_REVERSAL);
        } else if (result == DECLINE_OFFLINE && tagoffLine == null) {
            Logger.v("Case 8");
            CountDownResponseTimer.cancelTimer(222);
            connection.onConnect(DO_SAF_DECLINED);
        } else {
            Logger.v("Case 7");
            if ((isScreenAvailable == 2)) {
                connection.onConnect(DO_REVERSAL_CANCEL);
            } else if ((isScreenAvailable == 1)) {
                MapperFlow.getInstance().moveToPrintScreen(context);
//                if (!(transType.equalsIgnoreCase(ConstantApp.REFUND))) {
//                    MapperFlow.getInstance().moveToPrintScreen(context);
//                }
//
//                if (!(transType.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_PARTIAL))) {
//                    MapperFlow.getInstance().moveToPrintScreen(context);
//                }
//
//                if (!(transType.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_FULL))) {
//                    MapperFlow.getInstance().moveToPrintScreen(context);
//                }
            }
        }

    }

    private boolean isRequestSuccess() {
        String de39 = AppManager.getInstance().getDe39();
        if (de39.equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_000) || de39.equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_001) || de39.equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_003)
                || de39.equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_007)) {
            return true;
        }
        return false;
    }

    private void showCard() {
        if (showCard) {
            if (context instanceof PrintActivity) {
                ((PrintActivity) context).setAccountNo();
            }
            showCard = false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void fetchCardInfo(String s) {
        Logger.v("fetchCardInfo---" + s);
        byte[] iccData = new byte[1200];
        int iccDataLength = emv_get_tag_list_data(L_55TAGS, L_55TAGS.length, iccData, iccData.length);
        Logger.v("iccDataLength -" + iccDataLength);
        // Logger.v("Ic Data --" + new String(iccData)); //not readable
        String rawData = ByteConversionUtils.fetchTagValu(iccData, 0, iccDataLength);
        Logger.v("tag55data---" + rawData);
        ic55Data = (rawData.trim().length() % 2 != 0) ? rawData + "0" : rawData;
        // Expiry
        if (emv_is_tag_present(0x5F24) >= 0) {
            byte[] tagData = new byte[100];
            int tagLength = emv_get_tag_data(0x5F24, tagData, tagData.length);
            Logger.v("0x5F24----" + StringUtil.toHexString(tagData, 0, 3, false).substring(0, 4));
            AppConfig.EMV.icExpiredDate = StringUtil.toHexString(tagData, 0, 3, false).substring(0, 4);
        }
        if (emv_is_tag_present(0x95) >= 0) {
            byte[] TVR = new byte[5];
            int tagLength = emv_get_tag_data(0x95, TVR, TVR.length);
            Logger.v("0x95----" + (TVR));
            Logger.v("0x95----" + ByteConversionUtils.fetchTagValu(TVR, 0, tagLength));
            //  String data95 = ByteConversionUtils.fetchTagValu(TVR, 0, tagLength);
            // String data95 = ISOUtil.hexString(emvModule.getEmvData(0x95));
            String data95 = ISOUtil.hexString(TVR);
            Logger.v("tvr_95------" + data95);
            tvr = data95;

        }
        if (emv_is_tag_present(0x9F6C) >= 0) {
            byte[] t9f6c = new byte[5];
            int tagLength = emv_get_tag_data(0x9F6C, t9f6c, t9f6c.length);
            Logger.v("0x9F6C----" + (t9f6c));
            Logger.v("0x9F6C----" + ByteConversionUtils.fetchTagValu(t9f6c, 0, tagLength));
            String s9f6c = ISOUtil.hexString(t9f6c);
            Logger.v("0x9F6C------" + s9f6c);
            s0x9f6c = s9f6c;
        }

        if (emv_is_tag_present(0x9F66) >= 0) {
            byte[] t9F66 = new byte[5];
            int tagLength = emv_get_tag_data(0x9F66, t9F66, t9F66.length);
            Logger.v("0x9F66----" + (t9F66));
            Logger.v("0x9F66----" + ByteConversionUtils.fetchTagValu(t9F66, 0, tagLength));
            String s9F66 = ISOUtil.hexString(t9F66);
            Logger.v("0x9F66------" + s9F66);
            s0x9F66 = s9F66;
        }

        if (emv_is_tag_present(0x9F34) >= 0) {
            byte[] tagData = new byte[100];
            int tagLength = emv_get_tag_data(0x9F34, tagData, tagData.length);
            Logger.v("0x9F34----" + StringUtil.toHexString(tagData, 0, 3, false).substring(0, 2));
            Logger.v("0x9F34----" + ByteConversionUtils.fetchTagValu(tagData, 0, tagLength));
            cvm9f34 = ByteConversionUtils.fetchTagValu(tagData, 0, tagLength);
        }

        if (emv_is_tag_present(0x5F34) >= 0) {
            byte[] tagData = new byte[100];
            int tagLength = emv_get_tag_data(0x5F34, tagData, tagData.length);
            Logger.v("0x5F34----" + ByteConversionUtils.fetchTagValu(tagData, 0, tagLength));
            Logger.v("setCSN----------" + tagData[0]);
            AppConfig.EMV.icCardSerialNum = ByteConversionUtils.fetchTagValu(tagData, 0, tagLength);
        }

        // Track2
        if (emv_is_tag_present(0x57) >= 0) {
            byte[] tagData = new byte[100];
            int tagLength = emv_get_tag_data(0x57, tagData, tagData.length);
            Logger.v("0x57----" + ByteConversionUtils.fetchTagValu(tagData, 0, tagLength));
            Logger.v("setTrack2Data------" + ByteConversionUtils.fetchTagValu(tagData, 0, tagLength));
            AppConfig.EMV.icCardTrack2data = ByteConversionUtils.fetchTagValu(tagData, 0, tagLength);
        }

        if (emv_is_tag_present(0x5A) >= 0) {
            byte[] tagData = new byte[100];
            int tagLength = emv_get_tag_data(0x5A, tagData, tagData.length);
            Logger.v("0x5A----" + (tagData));
            Logger.v("setPAN----" + ByteConversionUtils.fetchTagValu(tagData, 0, tagLength));
            AppConfig.EMV.icCardNum = ByteConversionUtils.fetchTagValu(tagData, 0, tagLength);
        }

        if (AppConfig.EMV.icCardNum == null){
            if (!TextUtils.isEmpty(AppConfig.EMV.icCardTrack2data)) {
                String track2=	AppConfig.EMV.icCardTrack2data;
                if (track2.contains("="))
                    AppConfig.EMV.icCardNum = track2.substring(0, track2.indexOf('='));
                else
                    AppConfig.EMV.icCardNum = track2.substring(0, track2.indexOf('D'));
                Logger.v("0x5A----" +AppConfig.EMV.icCardNum);
            }
        }

        if (AppConfig.EMV.icExpiredDate == null) {
            if (!TextUtils.isEmpty(AppConfig.EMV.icCardTrack2data)) {
                String track2=	AppConfig.EMV.icCardTrack2data;
                if (track2.contains("=")) {
                    String[] array = track2.split("=");
                    Logger.v("Exp : "+ array[1].substring(0,4));
                    AppConfig.EMV.icExpiredDate = array[1].substring(0,4);
                } else {
                    String[] array = track2.split("D");
                    Logger.v("Exp : "+ array[1].substring(0,4));
                    AppConfig.EMV.icExpiredDate = array[1].substring(0,4);
                }
            }
        }

        AppConfig.EMV.icKernalId = String.format("%02x", emv_get_kernel_id());
    }

    // Old Listner
    public static class WaitThreat {
        Object syncObj = new Object();

        public void waitForRslt() throws InterruptedException {
            synchronized (syncObj) {
                syncObj.wait();
            }
        }

        void notifyThread() {
            synchronized (syncObj) {
                syncObj.notify();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void sendOnLineRequest() throws InterruptedException {
        Logger.v("inside_sendOnLineRequest");
        madeOnlineConnection = true;
        madeOnlineConnection_new = true;
        Logger.v("Chip Transaction");
        // [step1]：get ic card data from emvTransInfo then send to host
        connection.onConnect((rf_thread == 2) ? DO_SOCKET_REQ_AGAIN : DO_SOCKET_REQ);
        waitSockettThreat.waitForRslt();
        Logger.v("Wait ended" + doReversal);
        Logger.v("responseData55 --" + responseData55);
        String de39 = AppManager.getInstance().getDe39();
        Logger.v("de39 --" + de39);
        if (doReversal && !isSAFDecilined && !AppManager.getInstance().isAdminNotificationReversal()) {
            Logger.v("CASE 1");
//            controller.doEmvFinish(false);
            onlineFailFlow();
            return;
        } else if ((AppManager.getInstance().getResponseMTI() != null) && AppManager.getInstance().getResponseMTI().equalsIgnoreCase("1230")) {
            if (de39.equalsIgnoreCase("000")) {
                Logger.v("CASE 2");
                try {
                    flowEMVComplete(true, 2);
                    return;
                } catch (ISOException e) {
                    e.printStackTrace();
                }
            }
//                controller.doEmvFinish(true);
            else {
                Logger.v("CASE 3");
//                controller.doEmvFinish(false);
                //  emv_set_online_result(ONLINE_DENIAL, new byte[]{'0', '0'}, new byte[]{'0','0'}, 0);
                onlineFailFlow();
            }

            return;
        }
        /*else if (AppManager.getInstance().getResponseMTI() != null && AppManager.getInstance().getResponseMTI().equalsIgnoreCase("1210") &&
                transType.equalsIgnoreCase(ConstantApp.REFUND) && AppConfig.EMV.consumeType == 1) {
            if (AppManager.getInstance().getDe39().equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_000)
                    || AppManager.getInstance().getDe39().equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_001)
                    || AppManager.getInstance().getDe39().equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_003)
                    || AppManager.getInstance().getDe39().equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_007)) {
                Logger.v("CASE 4");
                try {
                    flowEMVComplete(true, 2);
                } catch (ISOException e) {
                    e.printStackTrace();
                }
            }
//                controller.doEmvFinish(true);
            else {
                Logger.v("CASE 5");
//                controller.doEmvFinish(false);
                //  emv_set_online_result(ONLINE_DENIAL, new byte[]{'0', '0'}, new byte[]{'0','0'}, 0);
                //     onlineFailFlow();
                try {
                    flowEMVComplete(true, 4);
                } catch (ISOException e) {
                    e.printStackTrace();
                }
            }

            return;
        }*/
        Logger.v("exel");
        disableReversal = true;
        //   if (!(de39.equalsIgnoreCase("117")) || (4 <= pinAttempt)) {
        if (((!(de39.equalsIgnoreCase("117") || de39.equalsIgnoreCase("196")) || (4 <= pinAttempt)))
                || (de39.equalsIgnoreCase("196") && reqObj.getPinData52() != null && reqObj.getPinData52().length != 0)) {

//            SecondIssuanceRequest request = new SecondIssuanceRequest();
            if (!isCancelled && responseData38.trim().length() != 0)
                Logger.v("Case 5 setAuthCode");
//                request.setAuthorisationCode(responseData38);//0x89 Authorization code
            byte[] responce = new byte[]{'0', '0'};
            if (!isCancelled && responseData55 != null && responseData55.trim().length() != 0) {
                Logger.v("Inside Step 2");
                if (AppManager.getInstance().getDe39().equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_000)
                        || AppManager.getInstance().getDe39().equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_001)
                        || AppManager.getInstance().getDe39().equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_003)
                        || AppManager.getInstance().getDe39().equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_007)
                    //  ||AppManager.getInstance().getDe39().equalsIgnoreCase("107")
                    // ||AppManager.getInstance().getDe39().equalsIgnoreCase("207")
                ) {
                    //IsoRequest.getStringFromHex(responseData55)
                    //emv_set_online_result(ONLINE_SUCCESS, responce, IsoRequest.getStringFromHex(responseData55), IsoRequest.getStringFromHex(responseData55).length);
                    byte[] issuerData = new byte[0];
                    issuerData = ISOUtil.hex2byte(responseData55);
                    Logger.v("issuerData---" + ByteConversionUtils.byteToHexString(issuerData[0]));//8A
                    Logger.v("issuerData---" + ByteConversionUtils.byteToHexString(issuerData[1]));//91
                    Logger.v("issuerData---" + ByteConversionUtils.byteToHexString(issuerData[2]));//71
                    Logger.v("issuerData---" + ByteConversionUtils.byteToHexString(issuerData[3]));//72
                    if (issuerData != null && issuerData.length > 0) {
                        Logger.v("setting_issuer_data");
                        emv_set_online_result(ONLINE_SUCCESS, responce, issuerData, issuerData.length);
                        // emv_set_online_result(ONLINE_SUCCESS, responce, new byte[]{' '}, 0);
                    } else {

                        emv_set_online_result(ONLINE_SUCCESS, responce, new byte[]{' '}, 0);
                    }
                } else {
                    Logger.v("Set AUth");
                    onlineFailFlow();
                }
            } else if (responseData55 == null) {

                if (AppManager.getInstance().getDe39().equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_000)
                        || AppManager.getInstance().getDe39().equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_001)
                        || AppManager.getInstance().getDe39().equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_003)
                        || AppManager.getInstance().getDe39().equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_007)
                    //  ||AppManager.getInstance().getDe39().equalsIgnoreCase("107")
                    // ||AppManager.getInstance().getDe39().equalsIgnoreCase("207")
                ) {
                    //IsoRequest.getStringFromHex(responseData55)
                    //emv_set_online_result(ONLINE_SUCCESS, responce, IsoRequest.getStringFromHex(responseData55), IsoRequest.getStringFromHex(responseData55).length);
                    byte[] issuerData = new byte[0];
                    responseData55 = "8a023030";
                    issuerData = ISOUtil.hex2byte(responseData55);
                    Logger.v("issuerData---" + ByteConversionUtils.byteToHexString(issuerData[0]));//8A
                    Logger.v("issuerData---" + ByteConversionUtils.byteToHexString(issuerData[1]));//91
                    Logger.v("issuerData---" + ByteConversionUtils.byteToHexString(issuerData[2]));//71
                    Logger.v("issuerData---" + ByteConversionUtils.byteToHexString(issuerData[3]));//72
                    if (issuerData != null && issuerData.length > 0) {
                        Logger.v("setting_issuer_data");
                        emv_set_online_result(ONLINE_SUCCESS, responce, issuerData, issuerData.length);
                        // emv_set_online_result(ONLINE_SUCCESS, responce, new byte[]{' '}, 0);
                    } else {

                        emv_set_online_result(ONLINE_SUCCESS, responce, new byte[]{' '}, 0);
                    }
                } else {
                    Logger.v("Set AUth");
                    onlineFailFlow();
                }
            }
//            else if (!isCancelled && responseData55 == null) {
//                Logger.v("Inside Step 2 when responsedata55 is not coming form host");
//                if (AppManager.getInstance().getDe39().equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_000)
//                        || AppManager.getInstance().getDe39().equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_001)
//                        || AppManager.getInstance().getDe39().equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_003)
//                        || AppManager.getInstance().getDe39().equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_007)
//                    //  ||AppManager.getInstance().getDe39().equalsIgnoreCase("107")
//                    // ||AppManager.getInstance().getDe39().equalsIgnoreCase("207")
//                ) {
//                    emv_set_online_result(ONLINE_SUCCESS, responce, new byte[]{' '}, 0);
//                } else {
//                    Logger.v("Set AUth");
//                    onlineFailFlow();
//                }
//            }
            else if (isCancelled && responseData38.trim().length() != 0) {
                Logger.v("second_issuer_value_else_if_block");
                madeOnlineConnection = false;
                //  emv_set_online_result(ONLINE_SUCCESS, responce, new byte[]{' '}, 0);
                Logger.v("de39issuerset---" + de39);
                if (de39 == null) { //after offline decline 190. ...mti 220 if 000
                    Logger.v("second_issuer_value_else_if_block_190"); //offline decline case.... net close txns...6.21 test case
                    emv_set_online_result(ONLINE_DENIAL, new byte[]{'0', '0'}, new byte[]{'0', '0'}, 0);
                } else {
                    Logger.v("second_issuer_value_else_if_block_else");
                    emv_set_online_result(ONLINE_FAIL, new byte[]{'0', '0'}, new byte[]{'0', '0'}, 0); //5a33==8a value
                    // emv_set_online_result(ONLINE_SUCCESS, new byte[]{'0', '0'}, new byte[]{'0','0'}, 0);
                }

//                request.setAuthorisationResponseCode(responseData38);//0x89 Authorization code
                Logger.v("--" + isCancelled + "--" + responseData55);
            }
//            else if (de39.equalsIgnoreCase("916")) {
//                Logger.v("response 916");
//                onlineFailFlow();
//            }
            else {
                Logger.v("second_issuer_value_else_block");
                //emv_set_online_result(ONLINE_SUCCESS, responce, new byte[]{' '}, 0);
            }
//            controller.doEmvFinish(false);
            //[Step2].Online transaction success or credit for load wait onemvfinished callback to end porcess after calling secondIssuance.
            Logger.v("Second Issuer Req");
            if (transType.equalsIgnoreCase(ConstantApp.REFUND) && AppConfig.EMV.consumeType == 1) {
                try {
                    if (isMada) {
                        flowEMVComplete(true, 2);
                    } else
                        flowEMVComplete(true, 2);
                } catch (ISOException e) {
                    e.printStackTrace();
                }
            } else {
                moveNextFlow();
            }

//            controller.secondIssuance(request);
        } else {
            Logger.v("pinattempt_fail_or_de39==117");
            SDKDevice.getInstance(context).incrementKSN(); //closing pinpad device and increment ksn value
            pinAttempt = pinAttempt + 1;
            connection.onConnect(DO_INVALID_PIN);
            rf_thread = 2;
            if ((isScreenAvailable == 1))
                waitForPinBlock(3);
//            waitPinInputThreat_pin.waitForRslt();

//            Logger.v("Pin 11" + (pinBlock != null));
//            if (pinBlock == null) {
//                Logger.v("Pinn enter 11");
//                isPinCancelled = true;
//                controller.doEmvFinish(false);
//                return;
//            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void hostPinRetry() {
//        if (pinBlock == null)
//            isPinRequires = 0;
//        if (isPinCancelled) {
////                controller.sendPinInputResult(null);
////                controller.doEmvFinish(false);
//            return;
//        } else
////                controller.sendPinInputResult(pinBlock);

        {
            try {
                sendOnLineRequest();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void onlineFailFlow() {

        Logger.v("online_fail_flow");
        byte[] responce = new byte[]{'0', '0'};
        byte[] issuerData = new byte[0];
        issuerData = ISOUtil.hex2byte(responseData55);
        Logger.v("responseData55_onlineFailFlow----" + (responseData55));
        /*Logger.v("issuerData2---"+ByteConversionUtils.byteToHexString(issuerData[0]));
        Logger.v("issuerData2---"+ByteConversionUtils.byteToHexString(issuerData[1]));
        Logger.v("issuerData1---"+ByteConversionUtils.byteToHexString(issuerData[2]));
        Logger.v("issuerData1---"+ByteConversionUtils.byteToHexString(issuerData[3]));*/
        emv_set_online_result(ONLINE_DENIAL, responce, issuerData, issuerData.length);
        moveNextFlow();
    }


    private static Handler pinEventHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Logger.v("pinEventHandler");
            if (isEMVCompleted && (msg.what != AppConfig.EMV.START_TIMER) && (msg.what != AppConfig.EMV.START_TIMER_REVERSE))
                return;
            switch (msg.what) {
                case AppConfig.EMV.PIN_FINISH:
                    Logger.v("Pin Finished----");
                    if (msg.obj != null) {
                        byte[] pinEntry = (byte[]) msg.obj;
                        if (pinEntry.length != 0)
                            pinBlock = pinEntry;
                        else
                            pinBlock = null;
                    }
                    if (doApplePay) {
                        isPinCancelled = false;
                        if (rf_thread == 1)
                            waitPinInputThreat_apple_pin.notifyThread();
                        else if (rf_thread == 2)
                            waitPinInputThreat_apple_pin_.notifyThread();
                    } else if (rf_thread == 1)
                        waitPinInputThreat_rf.notifyThread();
                    else if (rf_thread == 2)
                        waitPinInputThreat_pin.notifyThread();
                    else
                        waitPinInputThreat.notifyThread();
                    break;
                case AppConfig.EMV.PIN_CANCEL:
                    Logger.v("PIN_CANCEL----");
                    isCancelled = true;
                    if (doApplePay) {
                        if (rf_thread == 1)
                            waitPinInputThreat_apple_pin.notifyThread();
                        else if (rf_thread == 2)
                            waitPinInputThreat_apple_pin_.notifyThread();
                    } else if (rf_thread == 1)
                        waitPinInputThreat_rf.notifyThread();
                    else if (rf_thread == 2)
                        waitPinInputThreat_pin.notifyThread();
                    else
                        waitPinInputThreat.notifyThread();
                    break;
                case AppConfig.EMV.SOCKET_FINISH:
                    Logger.v("SOCKET_FINISH---");
                    String resObj = msg.obj.toString();
                    Logger.v("ResObj --" + resObj);
                    if (resObj.contains("__")) {
                        String[] dataSplit = resObj.split("__");
                        if (1 < dataSplit.length)
                            responseData55 = dataSplit[1].trim();
                        if (dataSplit.length != 0)
                            responseData38 = dataSplit[0].trim();
                    } else {
                        responseData55 = resObj;
                    }
                    Logger.v("responseData55 --" + responseData55);
                    isCancelled = false;
                    if (doApplePay)
                        waitSockettThreat_apple.notifyThread();
                    else
                        waitSockettThreat.notifyThread();
                    break;
                case AppConfig.EMV.SOCKET_CANCEL:
                    Logger.v("SOCKET_CANCEL----");
                    isCancelled = true;
                    responseData55 = "";
                    if (doApplePay)
                        waitSockettThreat_apple.notifyThread();
                    else
                        waitSockettThreat.notifyThread();
                    break;
                case AppConfig.EMV.SOCKET_UNABEL_ONLINE:
                    Logger.v("SOCKET_UNABEL_ONLINE----");
//                    isSAFDecilined = false; // disabling unable to go Online
                    isCancelled = true;
                    if (isSAFDecilined)
                        responseData38 = "Z3";
                    else
                        responseData38 = "";
                    if (doApplePay) {
                        responseData55 = "";
                        waitSockettThreat_apple.notifyThread();
                    } else
                        waitSockettThreat.notifyThread();
                    break;
                case AppConfig.EMV.SELECT_APP:
                    Logger.v("SELECT_APP---");
                    waitSelectApp.notifyThread();
                    break;
                case AppConfig.EMV.START_TIMER:
                    Logger.v("START_TIMER----");
                    connection.onConnect(DO_TIMER);
                    break;
                case AppConfig.EMV.START_TIMER_REVERSE:
                    Logger.v("START_TIMER_REVERSE-----");
                    connection.onConnect(DO_TIMER_REVERSAL);
                    break;
                default:
                    break;
            }
        }

    };

    public static Handler getPinEventHandler() {
        return pinEventHandler;
    }

    public static void setPinEventHandler(Handler pinEventHandler) {
        SimpleTransferListener.pinEventHandler = pinEventHandler;
    }

    public interface MakeConnection {
        public void onConnect(int i);
    }


    public void showPinOkay() {
        Logger.v("showPinOkay");
        if (showPinOkay) {
            ((PrintActivity) context).printOkay();
            showPinOkay = false;
        }
    }

    public void doBeepOnce() {
        Logger.v("doBeepOnce -" + doBeepOnce);
        if (doBeepOnce) {
            if (AppConfig.EMV.consumeType == 2) {
                connection.onConnect(DO_BEEP_ONCE);
            }
            doBeepOnce = false;
        }
    }

    private String getCode(String transactionType) {
        String code = IsoRequest.getInstance(context).getProcessCode(transactionType).substring(0, 2);
        Logger.v("Code --" + code);
        return code;
    }
}
