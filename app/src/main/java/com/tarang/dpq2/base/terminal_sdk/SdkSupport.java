package com.tarang.dpq2.base.terminal_sdk;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.cloudpos.DeviceException;
import com.cloudpos.OperationListener;
import com.cloudpos.OperationResult;
import com.cloudpos.POSTerminal;
import com.cloudpos.TimeConstants;
import com.cloudpos.msr.MSRDevice;
import com.cloudpos.msr.MSROperationResult;
import com.cloudpos.msr.MSRTrackData;
import com.cloudpos.sdk.util.StringUtility;
import com.tarang.dpq2.R;
import com.tarang.dpq2.base.AppInit;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.MapperFlow;
import com.tarang.dpq2.base.baseactivities.BaseActivity;
import com.tarang.dpq2.base.terminal_sdk.device.SDKDevice;
import com.tarang.dpq2.base.terminal_sdk.event.SimpleTransferListener;
import com.tarang.dpq2.base.terminal_sdk.q2.result.SwipeResult;
import com.tarang.dpq2.base.utilities.Utils;
import com.tarang.dpq2.view.activities.LandingPageActivity;
import com.tarang.dpq2.worker.LoadKeyWorker;
import com.wizarpos.emvsample.constant.Constant;
import com.wizarpos.jni.MsrInterface;

import java.util.concurrent.TimeUnit;

import static com.cloudpos.jniinterface.EMVJNIInterface.close_reader;
import static com.cloudpos.jniinterface.EMVJNIInterface.emv_anti_shake_finish;
import static com.cloudpos.jniinterface.EMVJNIInterface.emv_set_anti_shake;
import static com.cloudpos.jniinterface.EMVJNIInterface.open_reader;

public class SdkSupport implements Constant {

    //    private K21Swiper k21swiper;
//    private EmvModule emvModule;
    Context context;
    boolean isLandingPage;
    boolean isFallback;
    public boolean closeLister = false;
    //    private K21CardReader cardReader;
    int swiperCount = 0;
    private MSRDevice msrDevice;
//    CardReader reader;

    public SdkSupport(Context context) {
        this.context = context;
        if (msrDevice == null) {
            Logger.v("init_msr");
            msrDevice = (MSRDevice) POSTerminal.getInstance(context).getDevice("cloudpos.device.msr");
        }
//        this.reader = reader;
    }

    public static void loadKeys(Context context) {
//        K21Pininput pinkey = SDKDevice.getInstance(context).getK21Pininput();
//        new LoadKeys(context, pinkey).load();
//        new LoadAID(context).load();
    }

    public void initReader() {
        Logger.v("initReader()");
        closeCardReader();
        intEMV();
        isLandingPage = false;
        isFallback = false;
        if (checkCondition()) {
            Logger.v("startListener");
            startAllListner();
        }
    }

    public void falseIsLandingPage() {
        isLandingPage = false;
    }

    private boolean checkCondition() {
        return true;
    }

    public void closeCardReader() {
        Logger.v("closeCardReader()");
        SimpleTransferListener.resetListner();
        cancelMSREader(0);
        close_reader(AppInit.CONTACT_CHIP);
        emv_anti_shake_finish(1);
        close_reader(AppInit.CONTACTLESS_RF);
    }

    public void closeCardReader1() {
        Logger.v("closeCardReader()");
      //  SimpleTransferListener.resetListner();
        cancelMSREader(0);
     //   close_reader(AppInit.CONTACT_CHIP);
        emv_anti_shake_finish(1);
        close_reader(AppInit.CONTACTLESS_RF);
    }


    public void closeLandindCardReader() {
        SDKDevice.getInstance(context).incrementKSN();
        SimpleTransferListener.resetListner();
        cancelMSREader(2);
        close_reader(AppInit.CONTACT_CHIP);
    }

    //starts the reader wait for the result
    public void initReaderLandingPage() {
        Logger.v("conn 2");
        Logger.v("initReaderLandingPage()");
     //   closeCardReader();
        isLandingPage = true;
        isFallback = false;
        if (checkCondition()) {
            startLandingListner();
            Logger.v("startListener");
        } else
            Logger.v("conn else");
    }

    public void initFallbackReader() {
        isFallback = true;
        isLandingPage = false;
        if (checkCondition()) {
            Logger.v("startListener");
            startMSReader("initFallbackReader");
        }
    }

    private void showAlert() {
        Utils.alertDialogShow(context, "", new Utils.DialogeClick() {
            @Override
            public void onClick() {
                restartListner();
            }
        });
    }

    private void restartListner() {
        Logger.v("closeLister --" + closeLister);
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        if (!closeLister) {
            if (isFallback) {
                initFallbackReader();
            } else if (isLandingPage) {
                initReaderLandingPage();
            } else {
                initReader();
            }
        }
    }

    private void showToast(String msg) {
        ((BaseActivity) context).showToast(msg);
    }


    private boolean readMSCARD() {
        return false;
    }

    // TODO Q2
    protected static boolean msrThreadActived = false;
    public static boolean readMSRCard = false;
    protected static boolean msrClosed = true;
    protected static HandleAction mHandler = new HandleAction();
    //private MSRThread msrThread;

    protected boolean read_track2_data() {
        Logger.v("read_track2_data");
        int trackDatalength;
        byte[] byteArry = new byte[255];
        trackDatalength = MsrInterface.getTrackData(1, byteArry, byteArry.length);  // nTrackIndex: 0-Track1; 1-track2; 2-track3
        if (debug) {
            String strDebug = "";
            for (int i = 0; i < trackDatalength; i++)
                strDebug += String.format("%02X ", byteArry[i]);
            Log.d(APP_TAG, "track2 Data: " + strDebug);
        }
        if (trackDatalength > 0) {
            if (trackDatalength > 37
                    || trackDatalength < 21
            ) {
                return false;
            }

            int panStart = -1;
            int panEnd = -1;
            for (int i = 0; i < trackDatalength; i++) {
                if (byteArry[i] >= (byte) '0' && byteArry[i] <= (byte) '9') {
                    if (panStart == -1) {
                        panStart = i;
                    }
                } else if (byteArry[i] == (byte) '=') {
                    /* Field separator */
                    panEnd = i;
                    break;
                } else {
                    panStart = -1;
                    panEnd = -1;
                    break;
                }
            }
            if (panEnd == -1 || panStart == -1) {
                return false;
            }
            SwipeResult result = new SwipeResult();
            result.setPan(new String(byteArry, panStart, panEnd - panStart));
            result.setExpiry(new String(byteArry, panEnd + 1, 4));
            result.setServiceCode(new String(byteArry, panEnd + 5, 3));
           // result.setTrack2Data(byteArry, 0, trackDatalength);
            AppConfig.EMV.icCardNum = result.getPan();
            Logger.v("swipResult_getPan----" + result.getPan());
            Logger.v("swipResult_getExpiry----" + result.getExpiry());
            Logger.v("swipResult_getServiceCode----" + result.getServiceCode());
            Logger.v("swipResult_getTrack2Data----" + result.getTrack2Data());
            AppConfig.EMV.icExpiredDate = result.getExpiry();
            Logger.v("Read complete");
            Logger.v("Result --" + result.toString());
            AppConfig.EMV.swipResult = result;
            return true;
        }
        return false;
    }

    public void startMSReader(String i) {
        Logger.v("startMSReader()----"+i);
        /*msrThread = new MSRThread();
        msrThread.start();*/

//         msrDevice = (MSRDevice) POSTerminal.getInstance(context).getDevice("cloudpos.device.msr");
        try {
            msrDevice.open();
//            listenForSwipe(msrDevice);
            listenForSwipe();
        } catch (DeviceException e) {
            Logger.v("msrDeviceopenfail----"+e.getMessage());
            try {
                msrDevice.close();
                startMSReader("DeviceException");
            } catch (DeviceException deviceException) {
                deviceException.printStackTrace();
            } catch (Throwable t) {
                t.printStackTrace();
            }
            //startMSReader();
            e.printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void listenForSwipe(){
        try {
            OperationListener listener = new OperationListener() {

                @Override
                public void handleResult(OperationResult result) {
                    Logger.v("msr_OperationResult----"+result.getResultCode());
                    if (result.getResultCode() == OperationResult.SUCCESS) {


                        if (isLandingPage) {
                            Logger.v("msr_reading_from_landingpage");
                            readMSRCard = false;
                            msrClosed = true;
                            notifyMSRNotAllowed();
                          //close msr reader
                        }else {

                            MSRTrackData data = ((MSROperationResult) result).getMSRTrackData();
                            int trackError = 0;
                            byte[] trackData = null;
                            int track1 =0;
                            int track2 =1;
                            int track3 =2;
                         //   for (int trackNo = 0; trackNo < 3; trackNo++) {
                                trackError = data.getTrackError(track2);
                                Logger.v("msr_trackError-----"+trackError);
                                if (trackError == MSRTrackData.NO_ERROR) {
                                    trackData = data.getTrackData(track2);

                                    String track2Data = new String(data.getTrackData(1));
                                    Logger.v("track2Data----" + track2Data);
                                    Logger.v("msr_track_data----"+String.format("trackNO = %d, trackData = %s", track2, StringUtility.ByteArrayToString(trackData, trackData.length)));

                                    String[] track2array = track2Data.split("=");
                                    String expiryDate = track2array[1].substring(0,4);
                                    String serviceCode = track2array[1].substring(4,6);
                                    SwipeResult swipeResult = new SwipeResult();
                                    swipeResult.setPan(track2array[0]);
                                    swipeResult.setExpiry(expiryDate);
                                    swipeResult.setServiceCode(serviceCode);
                                    swipeResult.setTrack2Data(track2Data);
                                    AppConfig.EMV.icCardNum = swipeResult.getPan();
                                    Logger.v("swipResult_getPan----" + swipeResult.getPan());
                                    Logger.v("swipResult_getExpiry----" + swipeResult.getExpiry());
                                    Logger.v("swipResult_getServiceCode----" + swipeResult.getServiceCode());
                                    Logger.v("swipResult_getTrack2Data----" + swipeResult.getTrack2Data());
                                    AppConfig.EMV.icExpiredDate = swipeResult.getExpiry();
                                    Logger.v("Read complete");
                                    Logger.v("Result --" + swipeResult.toString());
                                    AppConfig.EMV.swipResult = swipeResult;

                                    Logger.v("Mag_card_reading");
                                    char index = AppConfig.EMV.swipResult.getServiceCode().charAt(0);
                                    Logger.v("check_condition_for_mag_if_chip_allowed");
                                    if ((index == '2' || index == '6') && !isFallback) {
                                        Logger.v("msr_do_chip");
                                        showToast(context.getString(R.string.do_chip_insert));
                                        SimpleTransferListener.isSwipeHappened = true;
                                        readMSRCard = false;
                                        msrClosed = true;
                                        cancelMSREader(4);
                                        startMSReaderAgain();
//close msr reader
                                        return;
                                    }else {
                                        Logger.v("msr_allperfect_proceed_txn");
                                        readMSRCard = false;
                                        msrClosed = true;
                                        notifyMSR();

                                    }

                                } else {
                                    Logger.v("msr_failed_track_data----"+String.format("trackNO = %d, trackError = %s", track2, trackError));
//                                sendFailedLog2(String.format("trackNO = %d, trackError = %s", trackNo, trackError));
                                    msrClosed = true;
                                    readMSRCard = false;
                                    notifyMsrReadError();
                                }
                          //  }
                        }


                    } else {
                        Logger.v("failed_msr_reading");
//                        sendFailedLog2(mContext.getString(R.string.find_card_failed));
                    }
                }
            };
            msrDevice.listenForSwipe(listener, TimeConstants.FOREVER);
            Logger.v("sendSuccessLog");
        } catch (DeviceException e) {
            e.printStackTrace();
            Logger.v("msr_reading_catch----"+e.getMessage());
//            sendFailedLog(mContext.getString(R.string.operation_failed));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void startMSReaderAgain() {
        Logger.v("startMSReaderAgain");
        /*msrThread = new MSRThread();
        msrThread.start();*/

        try {
            msrDevice.open();
            listenForSwipe();
        } catch (DeviceException e) {
            Logger.v("msrDeviceopenfail----"+e.getMessage());
            e.printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        resetFlag();
    }

    private void resetFlag() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SimpleTransferListener.isSwipeHappened = false;
            }
        }.start();
    }

    public void startAllListner() {
        LoadKeyWorker.initKernal();
        startMSReader("startAllListner");
        Logger.v("OPen Reader 1");
        open_reader(AppInit.CONTACT_CHIP);
        emv_set_anti_shake(AppInit.CONTACT_CHIP);
        open_reader(AppInit.CONTACTLESS_RF);
    }

    public void startLandingListner() {
        startMSReader("startLandingListner");
        Logger.v("OPen Reader 2");
        open_reader(AppInit.CONTACT_CHIP);
    }

    public void intEMV() {
        Logger.v("intEMV()");
        SimpleTransferListener.getInstance(context);
    }

    public void cancelMSREader(int i) {
        Logger.v("cancelMSREader()----"+i);
        readMSRCard = false;
//        MSRThread.interrupted();
        Logger.v("msrDevice.close();");
        try {
            msrDevice.close();
        } catch (DeviceException deviceException) {
            Logger.v("msrclose_catch----"+deviceException.getMessage());
            try {
                msrDevice.open();
                cancelMSREader(999);
            } catch (DeviceException e) {
                e.printStackTrace();
            } catch (Throwable t) {
                t.printStackTrace();
            }
            deviceException.printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    /*class MSRThread extends Thread {
        public void run() {
            super.run();
            Logger.v("Run");
            msrThreadActived = true;
            readMSRCard = false;
            if (msrClosed == true) {
                int open = MsrInterface.open();
                Logger.v("MsrInterface.open() --" + open);
                if (open >= 0) {
                    msrClosed = false;
                }
            }
            Logger.v("msrClosed --" + msrClosed);
            if (msrClosed == false) {
                readMSRCard = true;
                do {
                    int nReturn = -1;
                    nReturn = MsrInterface.poll(500);
//                    appState.msrPollResult = nReturn;
                    Logger.v("MsrInterface.poll, " + nReturn);
                    if (readMSRCard == false) {
                        MsrInterface.close();
                        msrClosed = true;
                        Logger.v("MsrInterface.close");
                    } else if (nReturn >= 0) {
                        if (isLandingPage) {
                            MsrInterface.close();
                            readMSRCard = false;
                            msrClosed = true;
                            notifyMSRNotAllowed();
                            return;
                        }
                        Logger.v("Inside read");
                        if (read_track2_data()) {
                            Logger.v("Mag_card_reading");
                            char index = AppConfig.EMV.swipResult.getServiceCode().charAt(0);
                            Logger.v("check_condition_for_mag_if_chip_allowed");
                            if ((index == '2' || index == '6') && !isFallback) {
                                showToast(context.getString(R.string.do_chip_insert));
                                SimpleTransferListener.isSwipeHappened = true;
                                MsrInterface.close();
                                readMSRCard = false;
                                msrClosed = true;
                                startMSReaderAgain();
//                                restartListner();
                                return;
                            }
//                            read_track3_data();
                            MsrInterface.close();
                            readMSRCard = false;
                            msrClosed = true;
                            notifyMSR();
                        } else {
                            MsrInterface.close();
                            msrClosed = true;
                            readMSRCard = false;
                            notifyMsrReadError();
                        }
                    }
                } while (readMSRCard == true);
            } else {
                notifyMsrOpenError();
            }
            Logger.v("MSRThread.exit");
            msrThreadActived = false;
        }
    }*/

    protected void notifyMSR() {
        Logger.v("notifyMSR()");
        AppConfig.EMV.consumeType = 0;
        close_reader(AppInit.CONTACT_CHIP);
        emv_anti_shake_finish(1);
        close_reader(AppInit.CONTACTLESS_RF);
        moveNext();
    }

    protected void notifyMSRNotAllowed() {
        Logger.v("notifyMSRNotAllowed()");
        if (context instanceof LandingPageActivity) {
            AppConfig.EMV.consumeType = 0;
        //    ((LandingPageActivity) context).removeCard();
            cancelMSREader(3);
            startMSReaderAgain();
        }
    }

    protected void notifyMsrReadError() {
        Logger.v("notifyMsrReadError()");
//        Message msg = new Message();
//        msg.what = MSR_READ_ERROR_NOTIFIER;
//        mHandler.sendMessage(msg);
        restartMsReader();
    }

    protected void notifyMsrOpenError() {
        Message msg = new Message();
        msg.what = MSR_OPEN_ERROR_NOTIFIER;
        mHandler.sendMessage(msg);
    }

    protected static class HandleAction extends Handler {

        @Override
        public void handleMessage(Message msg) {
            Logger.v("handleAction_SDKsupport");
            Logger.v("What --" + msg);
            switch (msg.what) {
                case MSR_READ_DATA_NOTIFIER:
//                    if(   appState.trans.getServiceCode().length() > 0
//                            && (   appState.trans.getServiceCode().getBytes()[0] == '2'
//                            || appState.trans.getServiceCode().getBytes()[0] == '6'
//                    )
//                    )
//                    {
//                        if(appState.trans.getEmvCardError() == false)
//                        {
//                            startMSRThread();
//                            appState.promptCardIC = true;
//                            Toast.makeText(this, "Please Insert/Tap Card", Toast.LENGTH_SHORT).show();
//                        }
//                        else{
//                            cancelAllCard();
//                            setResult(Activity.RESULT_OK, getIntent());
//                            finish();
//                        }
//                    }
//                    else{
//                        if(   appState.trans.getServiceCode().length() > 0
//                                && appState.trans.getServiceCode().getBytes()[0] == '1'
//                        )
//                        {
//                            appState.trans.setEmvCardError(false);
//                            appState.trans.setPanViaMSR(true);
//                        }
//                        else{
//                            appState.trans.setEmvCardError(false);
//                            appState.trans.setPanViaMSR(false);
//                        }
//                        cancelAllCard();
//                        setResult(Activity.RESULT_OK, getIntent());
//                        finish();
//                    }
                    break;
                case MSR_OPEN_ERROR_NOTIFIER:
//                    appState.msrError = true;
//                    appState.acceptMSR = false;
//                    txtPrompt.setText(appState.getString(R.string.insert_card));
                    break;
                case MSR_READ_ERROR_NOTIFIER:
//                    readAllCard();
                    break;
                case CARD_INSERT_NOTIFIER:
                    emv_anti_shake_finish(1);
                    Bundle bundle = msg.getData();
                    int nEventID = bundle.getInt("nEventID");
                    int nSlotIndex = bundle.getInt("nSlotIndex");
                    if (debug)
                        Log.d(APP_TAG, "get CONTACT_CARD_EVENT_NOTIFIER,event[" + nEventID + "]slot[" + nSlotIndex + "]");
                    if (nSlotIndex == 0
                            && nEventID == SMART_CARD_EVENT_INSERT_CARD
                    ) {
//                        appState.trans.setEmvCardError(false);
//                        if(appState.acceptContactlessCard == true)
//                        {
//                            cancelContactlessCard();
//                        }
//                        appState.trans.setCardEntryMode(INSERT_ENTRY);
//                        setResult(Activity.RESULT_OK, getIntent());
//                        exit();
                    }
                    break;
                case CARD_TAPED_NOTIFIER:
                    bundle = msg.getData();
                    nEventID = bundle.getInt("nEventID");
                    if (nEventID == SMART_CARD_EVENT_INSERT_CARD) {
//                        cancelContactCard();
//                        cancelMSRThread();
//                        appState.trans.setCardEntryMode(CONTACTLESS_ENTRY);
//                        if(debug) Log.d(APP_TAG, "get CONTACTLESS_CARD_EVENT_NOTIFIER" );
//                        setResult(Activity.RESULT_OK, getIntent());
//                        exit();
                    }
                    break;
                case CONTACTLESS_HAVE_MORE_CARD_NOTIFIER:
                    if (debug) Log.d(APP_TAG, "error, have more card");
//                    appState.setErrorCode(R.string.error_more_card);
//                    setResult(Activity.RESULT_OK, getIntent());
//                    exit();
                    break;
                case CARD_ERROR_NOTIFIER:
//                    txtError.setText("IC POWERON ERROR");
//                    txtPrompt.setText("PLEASE INSERT CARD");
//                    appState.trans.setEmvCardError(true);
                    break;
                case CARD_CONTACTLESS_ANTISHAKE:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(400);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Logger.v("anti shake finish");
//                            if(appState.msrPollResult == -1)
//                            {
//                                emv_anti_shake_finish(0);
//                            }
//                            else
//                            {
//                                emv_anti_shake_finish(1);
//                            }
                        }
                    }).start();
                    break;
            }
        }
    }

    public void restartMsReader() {
        Logger.v("restartMsReader()");
        if ((isLandingPage)) {
            showToast(context.getString(R.string.swipe_card_properly));
            startMSReader("restartMsReader");
        } else {
            Logger.v("Swipe count == "+swiperCount);
            swiperCount = swiperCount + 1;
            if (swiperCount == 3) {
                closeCardReader();
                Logger.v("Swipe count == "+swiperCount);
                swiperCount = 0;
                Logger.v("Swipe count == "+swiperCount);
                MapperFlow.getInstance().moveToManualEntry(context);
            } else {
                showToast(context.getString(R.string.swipe_card_properly));
                startMSReader("restartMsReader");
            }
        }
    }

    public void moveNext() {
        Logger.v("moveNext()_sdksupport");
        if (isFallback) {
            MapperFlow.getInstance().moveToPrintScreenFallback(context);
        } else if (isLandingPage) {
            MapperFlow.getInstance().moveEnterAmount(context);
        } else {
            MapperFlow.getInstance().moveToPrintScreen(context);
        }
    }
}
