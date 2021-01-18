package com.tarang.dpq2.viewmodel;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.media.SoundPool;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.work.Data;
import androidx.work.WorkInfo;

import com.cloudpos.printer.PrinterDevice;
import com.tarang.dpq2.R;
import com.tarang.dpq2.base.AppInit;
import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.MapperFlow;
import com.tarang.dpq2.base.baseactivities.BaseActivity;
import com.tarang.dpq2.base.jpos_class.ByteConversionUtils;
import com.tarang.dpq2.base.jpos_class.ConstantApp;
import com.tarang.dpq2.base.jpos_class.ConstantAppValue;
import com.tarang.dpq2.base.jpos_class.MadaPackager;
import com.tarang.dpq2.base.room_database.db.AppDatabase;
import com.tarang.dpq2.base.room_database.db.entity.TransactionModelEntity;
import com.tarang.dpq2.base.terminal_sdk.AppConfig;
import com.tarang.dpq2.base.terminal_sdk.SdkSupport;
import com.tarang.dpq2.base.terminal_sdk.device.SDKDevice;
import com.tarang.dpq2.base.terminal_sdk.event.SimpleTransferListener;
import com.tarang.dpq2.base.terminal_sdk.utils.LightsDisplay;
import com.tarang.dpq2.base.terminal_sdk.utils.SoundPoolImpl;
import com.tarang.dpq2.base.utilities.CountDownResponseTimer;
import com.tarang.dpq2.base.utilities.Utils;
import com.tarang.dpq2.isopacket.CreatePacket;
import com.tarang.dpq2.model.MPortalTransactionModel;
import com.tarang.dpq2.model.PrinterModel;
import com.tarang.dpq2.view.activities.PrintActivity;
import com.tarang.dpq2.worker.PacketDBInfoAsync;
import com.tarang.dpq2.worker.PacketDBInfoListener;
import com.tarang.dpq2.worker.PacketDBInfoWorker;
import com.tarang.dpq2.worker.PrinterWorker;
import com.tarang.dpq2.worker.SAFWorker;
import com.tarang.dpq2.worker.SSLSocketFactoryExtended;
import com.tarang.dpq2.worker.SocketConnectionWorker;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.SSLSocket;

import static com.cloudpos.jniinterface.EMVJNIInterface.close_reader;
import static com.cloudpos.jniinterface.EMVJNIInterface.emv_anti_shake_finish;
import static com.tarang.dpq2.base.terminal_sdk.AppConfig.EMV.reqObj;
import static com.tarang.dpq2.base.terminal_sdk.event.SimpleTransferListener.isScreenAvailable;
import static com.tarang.dpq2.base.terminal_sdk.event.SimpleTransferListener.pinBlock;

public class TransactionViewModel extends BaseViewModel implements ConstantAppValue, SimpleTransferListener.MakeConnection {

    public static PrinterModel printerModel = null;
    public static boolean safTable = false;
    private TransactionRepository repo;
    public CreatePacket packet;
    MutableLiveData<Integer> makeConnection;
    MutableLiveData<Integer> showAlert;
    MutableLiveData<Boolean> printRecipt;
    MutableLiveData<Boolean> safTransaction;
    static MutableLiveData<Boolean> restartTimer;
    MutableLiveData<Boolean> onlinePin;
    static MutableLiveData<Boolean> startTimer;
    MutableLiveData<Boolean> cardRemoveTimer;
    static MutableLiveData<Boolean> safRepeat;

    private Context context;
    private Activity activity;
    public String transactionType;
    public static CountDownTimer timer = null;
    public static boolean msCancelled = false;
    public boolean reversalSAF = false;
    private boolean isSAFPrint = false;
    public boolean forceClose = false;
    private SoundPoolImpl spi;
    public int waveRettryCount = 0;
    private boolean emvReversal;
    public boolean isPrinted = false;
    boolean doneSocket = false;
    private int pinAttempt;
    private PrinterDevice devicePrinter;
    String statusMsg = "";
    boolean cardDetailShown = false;
    private boolean isPlayedStatus = false;
    private boolean showApprovedBeepOffline;
    private boolean statusApproved = false;


    public TransactionViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(Context context, LifecycleOwner owner, Activity activity) {
        this.context = context;
        this.activity = activity;
        pinAttempt = 1;
        SAFWorker.isSAFRepeat = false;
        forceClose = false;
        this.repo = new TransactionRepository(context, owner);
        this.packet = new CreatePacket(context);
        spi = SoundPoolImpl.getInstance();
        spi.initLoad(context);
        emvReversal = false;
        devicePrinter = SDKDevice.getInstance(context).getPrinter();
        isPrinted = false;
        isPlayedStatus = false;
        statusMsg = "";
        showApprovedBeepOffline = false;
        statusApproved = false;
    }

    public void initSound() {
        spi.initListner(context, new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                Logger.v("Sound listner 000_transaviewmodel");
                loopPlay();
            }
        });
    }

    public void initSoundPlay(final boolean playTwise) {
        spi.initLoad(context);
        spi.initListner(context, new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                Logger.v("Sound listner 000_TTTT");
                if (!playTwise) {
                    spi.initListner(context, new SoundPool.OnLoadCompleteListener() {
                        @Override
                        public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                            Logger.v("Sound listner 000_TTTF");
                            spi.play();
                        }
                    });
                } else {
                    spi.initListner(context, new SoundPool.OnLoadCompleteListener() {
                        @Override
                        public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                            Logger.v("Sound listner 000");
                            spi.playTwice();
                        }
                    });
                }

            }
        });
    }

    public void insertOrUpdateTransaction() {
        Logger.v("insertOrUpdateTransaction()");
        AppConfig.customerCopyPrinted = false;
        repo.insertTransaction(packet.expiryDate, new Observer<WorkInfo>() {
            @Override
            public void onChanged(@Nullable WorkInfo workInfo) {
                if (workInfo != null) {
                    WorkInfo.State state = workInfo.getState();
                    Logger.v("Insert Transaction Status " + state);
                    if (checkSuccessStatus(workInfo)) {
                        Logger.v("AppConfig.printerDataAvailable = true");
                        AppConfig.printerDataAvailable = true;
                        makeSocketRequest();
                    }
                }
            }
        });
    }

    public void insertSAFTransaction() {
        Logger.v("insertSAFTransaction()");
        AppConfig.customerCopyPrinted = false;
        showAlert.setValue(2);
        repo.insertSAFTransaction(packet.expiryDate, new Observer<WorkInfo>() {
            @Override
            public void onChanged(@Nullable WorkInfo workInfo) {
                if (workInfo != null) {
                    WorkInfo.State state = workInfo.getState();
                    Logger.v("Insert Transaction Status " + state);
                    if (checkSuccessStatus(workInfo)) {
                        Logger.v("AppConfig.printerDataAvailable = true;---()");
                        AppConfig.printerDataAvailable = true;
                        showAlert.setValue(5);
                        //added successflow for SAF transaction
                        statusMsg = "";
                        showApprovedMessage();
                        printSAFReceipt(true);
                    }
                }
            }
        });
    }

    public void insertAdviceTransaction() {
        Logger.v("insertAdviceTransaction()");
        if (AppManager.getInstance().isFinancialAdviceRequired()) {
            reqObj.setMti0(ConstantAppValue.FINANCIAL_ADVISE);
        } else if (AppManager.getInstance().isAuthorisationAdviceRequired())
            reqObj.setMti0(ConstantAppValue.AUTH_ADVISE);
        if (reqObj.getResponseCode39() == null || reqObj.getResponseCode39().trim().length() == 0)
            reqObj.setResponseCode39(ConstantAppValue.SAF_REJECTED);
        repo.insertSAFTransaction(packet.expiryDate, new Observer<WorkInfo>() {
            @Override
            public void onChanged(@Nullable WorkInfo workInfo) {
                if (workInfo != null) {
                    WorkInfo.State state = workInfo.getState();
                    Logger.v("Insert Transaction Status " + state);
                    if (checkSuccessStatus(workInfo)) {
                        savePrintFlow();
                    }
                }
            }
        });
    }


    public void onLinePrint(int i) {
        Logger.v("onLinePrint --" + i);
        printRecipt.postValue(true);

    }

    public void makeSocketRequest() {
        Logger.v("makeSocketRequest()");
        if (increamentRetryCount(true)) {
            doneSocket = false;
            repo.makeSocketConnection(new Observer<WorkInfo>() {
                @Override
                public void onChanged(@Nullable WorkInfo workInfo) {
                    Logger.v("makeSocketConnectionUnpack onChanged 11");
                    if (workInfo != null) {
                        Logger.v("worker info" + workInfo);
                        if (repo.uuID.equals(workInfo.getId())) {
                            if (workInfo.getState() == WorkInfo.State.SUCCEEDED || workInfo.getState() == WorkInfo.State.FAILED) {
                                PrintActivity.disableBackBtn = true;
                                if (doneSocket) {
                                    Logger.v("Flow Ended");
                                    return;
                                }
                                if (workInfo.getOutputData().getBoolean("SHOW_TOAST", false)) {
                                    PrintActivity.disableBackBtn = false;
                                    ((BaseActivity) context).showToast(context.getString(R.string.invalid_service_ip));
                                } else
                                    Logger.v("SAF_TOAST else");
                            }


                            WorkInfo.State state = workInfo.getState();
                            Logger.v("Starte 4--" + isScreenAvailable);
                            Logger.v("Starte 5--" + SocketConnectionWorker.failureCount);
                            Logger.v("Starte 5--" + SimpleTransferListener.isSAFDecilined);
                            Logger.v("Starte 6--" + SocketConnectionWorker.cancelledRequest);
                            Logger.v("Starte 6--" + SimpleTransferListener.isEMVCompleted);
                            if (isScreenAvailable == 2) {
                                doneSocket = true;
                                repo.cancelRequest();
                                CountDownResponseTimer.cancelTimerForce(1010);
                                if (SimpleTransferListener.isEMVCompleted) {
                                    printFlow();
                                } else {
                                    notifyUnableToGoOnline();
                                }
                                return;
                            }
                            if (checkSuccessStatusOnly(workInfo)) {
                                Logger.v("Starte --" + state);
                                Logger.v("setFinancialAdviceRequired_checkSuccessStatusOnly...." + AppManager.getInstance().isFinancialAdviceRequired());
                                Logger.v("isAuthorisationAdviceRequired_checkSuccessStatusOnly...." + AppManager.getInstance().isAuthorisationAdviceRequired());
                                if (packet.isMagneticStripe) {
                                    String de39 = AppManager.getInstance().getDe39();
                                    if ((de39.equalsIgnoreCase("117") || de39.equalsIgnoreCase("121") || de39.equalsIgnoreCase("196")) && (pinAttempt < 3)) {
                                        pinAttempt = pinAttempt + 1;
                                        Toast.makeText(context, context.getString(R.string.wrong_pin), Toast.LENGTH_SHORT).show();
                                        addMSCARDDetails();
                                        return;
                                    }
                                }
                                if (packet.isMagneticStripe || packet.isManual || SimpleTransferListener.isEMVCompleted
                                        || AppManager.getInstance().getResponseMTI().equalsIgnoreCase(ConstantAppValue.REVERSAL_RESPONSE)) {
                                    Logger.v("LOg 1");
                                    if (packet.isMagneticStripe || packet.isManual) {
                                        showApprovedMessage();
                                    }
                                    if (AppManager.getInstance().getResponseMTI().equalsIgnoreCase(ConstantAppValue.REVERSAL_RESPONSE)) {
                                        if (SocketConnectionWorker.cancelledRequest) {
                                            Logger.v("LOg 2");
                                            SocketConnectionWorker.cancelledRequest = false;
                                            notifyUnableToGoOnline();
//                                        showSocektError(workInfo);
                                        } else {
                                            Logger.v("LOg 3");
                                            if (!SimpleTransferListener.isSAFDecilined) {
                                                Logger.v("LOg 4");
                                                showApprovedMessage();
                                                if (!AppConfig.customerCopyPrinted) {
                                                    printRecipt.setValue(false);
                                                } else
                                                    printFlow();
                                            } else
                                                Logger.v("LOg 5");
                                        }
                                        Logger.v("LOg 6");
                                    } else {
                                        sleep1Sec();
                                        if (!isPrinted) {
                                            Logger.v("LOg 7");
                                            onLinePrint(5);
                                        } else
                                            printFlow();
                                    }
                                    Logger.v("LOg 8");
                                } else if (AppManager.getInstance().getResponseMTI().equalsIgnoreCase(ConstantAppValue.FINANCIAL_ADVICE_RESPONSE) && AppManager.getInstance().isFinancialAdviceRequired()) {
                                    Logger.v("LOg 9");
                                    AppManager.getInstance().setFinancialAdviceRequired(false);
                                    onLinePrint(4);
                                } else if (AppManager.getInstance().getResponseMTI().equalsIgnoreCase(ConstantAppValue.AUTH_ADVISE_RESPONSE) && AppManager.getInstance().isAuthorisationAdviceRequired()) {
                                    Logger.v("LOg 10");
                                    AppManager.getInstance().setAuthorisationAdviceRequired(false);
                                    onLinePrint(3);
                                } else {
                                    Logger.v("LOg 11");
                                    notifySuccess();
                                }
                                Logger.v("LOg 12");
//                        status.onStatusChange("socket",state);
                            } else if (workInfo.getState() == WorkInfo.State.FAILED) {
                                Logger.v("LOg 13");
                                if (!(workInfo.getOutputData().getBoolean(SocketConnectionWorker.ADMIN_NOTIFICATION, false))) {
                                    Logger.v("LOg 14");
                                    if (AppManager.getInstance().getResponseMTI() != null) {
                                        Logger.v("LOg 15");
                                        if (AppManager.getInstance().getResponseMTI().equalsIgnoreCase(ConstantAppValue.FINANCIAL_ADVICE_RESPONSE)) {
                                            Logger.v("LOg 16");
                                            if (AppManager.getInstance().isFinancialAdviceRequired()) {
                                                Logger.v("LOg 17");
                                                AppManager.getInstance().setFinancialAdviceRequired(false);
                                                onLinePrint(2);
                                            }
                                        } else if (AppManager.getInstance().getResponseMTI().equalsIgnoreCase(ConstantAppValue.AUTH_ADVISE_RESPONSE)) {
                                            Logger.v("LOg 18");
                                            if (AppManager.getInstance().isAuthorisationAdviceRequired()) {
                                                Logger.v("LOg 19");
                                                AppManager.getInstance().setAuthorisationAdviceRequired(false);
                                                onLinePrint(1);
                                            }
                                        } else {
                                            Logger.v("LOg 155");
                                            notifySuccess();
                                        }
                                    } else {
                                        Logger.v("LOg 20");
                                        if (SocketConnectionWorker.cancelledRequest) {
                                            SocketConnectionWorker.cancelledRequest = false;
                                            if (packet.isMagneticStripe || packet.isManual) {
                                                showApprovedMessage();
                                                sleep1Sec();
                                                CountDownResponseTimer.cancelTimer(555);
                                                onLinePrint(66);
                                            } else {
                                                CountDownResponseTimer.cancelTimer(55);
                                                Logger.v("LOg 21");
                                                if (SimpleTransferListener.isEMVCompleted) {
                                                    printFlow();
                                                } else {
                                                    notifyUnableToGoOnline();
                                                }
                                            }
//                                            showSocektError(workInfo);
                                        } else {
                                            Logger.v("LOg 22");
                                            if (packet.isMagneticStripe || packet.isManual || SimpleTransferListener.isEMVCompleted) {
                                                Logger.v("LOg 23");
                                                if (!SimpleTransferListener.isSAFDecilined) {
                                                    Logger.v("LOg 24");
                                                    if (!isPrinted && SimpleTransferListener.disableReversal)
                                                        printRecipt.setValue(false);
                                                } else
                                                    startCountDownTimer(true);
                                            }

//                                            else
//                                                notifyCancel();
                                        }
                                    }
                                } else {
                                    Logger.v("LOg 25");
                                    if (packet.isMagneticStripe || packet.isManual || SimpleTransferListener.isEMVCompleted) {
                                        if ((packet.isMagneticStripe || packet.isManual)) {
                                            showApprovedMessage();
                                            Logger.v("Log 777");
                                            if (!isPrinted) {
                                                Logger.v("LOg 77");
                                                onLinePrint(5);
                                            } else
                                                printFlow();
                                        } else {
                                            printFlow();
                                        }
                                    } else
                                        notifyCancel();
                                }
                            }
                        } else {
                            Logger.v("worker info UUID" + repo.uuID);
                        }
                    }
                }
            });
        } else {
            Logger.v("MAX TRY --" + reqObj.getMti0());
            Logger.v("setFinancialAdviceRequired...." + AppManager.getInstance().isFinancialAdviceRequired());
            Logger.v("isAuthorisationAdviceRequired...." + AppManager.getInstance().isAuthorisationAdviceRequired());
            doneSocket = true;
            boolean manualReversal = AppManager.getInstance().isReversalManual();
            if (reqObj.getMti0().equalsIgnoreCase(ConstantAppValue.REVERSAL) || reqObj.getMti0().equalsIgnoreCase(ConstantAppValue.FINANCIAL_ADVISE) || reqObj.getMti0().equalsIgnoreCase(ConstantAppValue.AUTH_ADVISE)) {
                twoLegReversal();
            } else if (packet.isMagneticStripe || packet.isManual || (manualReversal && (reqObj.getMti0().equalsIgnoreCase(ConstantAppValue.REVERSAL) || reqObj.getMti0().equalsIgnoreCase(ConstantAppValue.REVERSAL_REPEAT)))) {
                showApprovedMessage();
                CountDownResponseTimer.cancelTimer(555);
                onLinePrint(66);
            } else {
                CountDownResponseTimer.cancelTimer(55);
                Logger.v("LOg 021");
                if (SimpleTransferListener.isEMVCompleted) {
                    if (reqObj.getMti0().equalsIgnoreCase(ConstantAppValue.FINANCIAL_ADVISE)) {
                        Logger.v("LOg 023");
                        twoLegReversal();
                    } else {
                        Logger.v("LOg 022");
                        Logger.v("LOg 024--" + isPrinted);
                        if (AppManager.getInstance().isFinancialAdviceRequired() || AppManager.getInstance().isAuthorisationAdviceRequired()) {
                            insertAdviceTransaction();
                        } else if (isScreenAvailable == 2) {
                            checkReversal(reversalStatus, 5);
                        } else
                            savePrintFlow();
                    }
                } else {
                    Logger.v("LOg 026");
                    notifyUnableToGoOnline();
                }
            }
        }
    }

    public void twoLegReversal() {
        SocketConnectionWorker.failureCount = -1;
        checkReversal(reversalStatus, 1);
    }


    private void showSocektError(WorkInfo workInfo) {
        String error = workInfo.getOutputData().getString(SocketConnectionWorker.ERROR_MSG);
        if (error != null && error.trim().length() != 0) {
            Utils.alertDialogShow(context, error, listner);
        } else {
            showAlert.setValue(404);
        }
    }

    private void showApprovedMessage() {
        Logger.v("Status msg : "+ statusMsg);
        if (statusMsg.trim().length() != 0) {
            Logger.v("Already data set");
            return;
        }
        Logger.v("Show message --new2");
        if (AppManager.getInstance().getDe39() != null) {
            String de39 = AppManager.getInstance().getDe39();
            Logger.v("Show message --" + de39);
            if (de39.trim().length() != 0) {
                boolean status = (de39.equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_000)
                        || de39.equalsIgnoreCase(ConstantAppValue.SAF_APPROVED)
                        || de39.equalsIgnoreCase(ConstantAppValue.SAF_APPROVED_UNABLE)
                        || de39.equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_003)
                        || de39.equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_001)
                        || de39.equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_007));
                if (status) {
                    if (AppConfig.EMV.consumeType == 2)
                        new LightsDisplay(context).showGreenLights();
                    statusMsg = successFlow();
                } else if (de39.equalsIgnoreCase("400")) {
                    statusMsg = reversalMsgFlow();
                } else {
                    statusMsg = declinedFlow();
                }
            } else {
                statusMsg = declinedFlow();
            }
        }
    }

    private String declinedFlow() {
        Logger.v("declinedFlow -" + isPrinted);
        Logger.v("isPlayedStatus -" + isPlayedStatus);
        Logger.v("declinedFlow -" + AppConfig.EMV.consumeType);
        statusApproved = false;
        if (!isPrinted) {
            if (AppConfig.EMV.consumeType == 2 && !isPlayedStatus)
                initSoundPlay(true);
        }
        return context.getString(R.string.declined);
    }

    private String declinedFlow1() {
        Logger.v("declinedFlow -11");
        Logger.v("isPlayedStatus -" + isPlayedStatus);
        Logger.v("declinedFlow -" + AppConfig.EMV.consumeType);
        statusApproved = false;
        if (AppConfig.EMV.consumeType == 2 && !isPlayedStatus)
            initSoundPlay(true);
        return context.getString(R.string.declined);
    }

    private String reversalMsgFlow() {
        Logger.v("reversalMsgFlow()_transviewmodel");
        String authCode = "";
        statusApproved = true;
        if (AppManager.getInstance().getDe38() != null)
            authCode = (AppManager.getInstance().getDe38());
//        if (authCode.trim().length() != 0) {
//            return context.getString(R.string.accepted) + "-" + Utils.formatLanguageNumber(activity, authCode);
//        } else {
        return context.getString(R.string.accepted) + "-" + Utils.formatLanguageNumber(activity, "400");
//        }
    }

    private String successFlow() {
        if (AppConfig.EMV.consumeType == 2)
            initSoundPlay(false);
        String authCode = "";
        Logger.v("Status approved");
        statusApproved = true;
        if (AppManager.getInstance().getDe38() != null)
            authCode = (AppManager.getInstance().getDe38());
        if (authCode.trim().length() != 0) {
            return context.getString(R.string.approved) + "-" + Utils.formatLanguageNumber(activity, authCode);
        } else {
            return context.getString(R.string.approved);
        }
    }

    public void sleep1Sec() {
//        try {
//            Logger.v("Thread Sleep 1");
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            Logger.v("Thread Sleep Excep");
//            e.printStackTrace();
//        }
    }


    private boolean checkSuccessStatus(WorkInfo workInfo) {
        if (repo.uuID.equals(workInfo.getId())) {
            if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
//            repo.removeObserver();
                return true;
            } else if (workInfo.getState() == WorkInfo.State.FAILED)
                if (workInfo.getOutputData().getBoolean(PacketDBInfoWorker.INVALID_CARD, false))
                    showAlert.setValue(10);
                else
                    showAlert.setValue(404);
        }
        return false;
    }

    private boolean checkSuccessStatusOnly(WorkInfo workInfo) {
        if (repo.uuID.equals(workInfo.getId())) {
            if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
//            repo.removeObserver();
                return true;
            }
        }
        return false;
    }

    public void printReceipt(final Boolean aBoolean) {
        Logger.v("Print 2");
        if (checkAutomaticReversal() || !isPrinted) {
            showAlert.postValue(4);
            repo.printReceipt(new Observer<WorkInfo>() {
                @Override
                public void onChanged(final WorkInfo workInfo) {
                    Logger.v("printReceipt -State-1-" + workInfo.getState());
                    if (checkSuccessStatus(workInfo)) {
                        doPrintTransactionReceipt(devicePrinter, context, new PrintComplete() {
                            @Override
                            public void onFinish() {
                                Logger.v("OnFinsh Receipt");
                                isPrinted = true;
                                if (checkPaper(workInfo, (aBoolean) ? 0 : 1) != 0) {
                                    if (aBoolean) {
                                        checkSAF(false);
                                    } else {
                                        printFlow();
                                    }
                                }
                            }
                        });
                    }
                }
            });
        } else {
            Logger.v("TIMEOUT ---" + false);
            if (aBoolean) {
                checkSAF(false);
            } else {
                printFlow();
            }
        }
    }

    private boolean checkAutomaticReversal() {
        if (AppConfig.customerCopyPrinted)
            return true;
        if ((reqObj != null) && reqObj.getNameTransactionTag().equalsIgnoreCase(ConstantApp.PURCHASE_REVERSAL)) {
            if (reqObj.getMessageReasonCode25() != null) {
                Logger.v("TIMEOUT --" + reqObj.getMessageReasonCode25());
                return (reqObj.getMessageReasonCode25().equalsIgnoreCase(CUSTOMER_CANCELLATION));
            }
        } else
            return true;
        return false;
    }

    private int checkPaper(WorkInfo workInfo, final int print) {
        boolean printStatus = workInfo.getOutputData().getBoolean(PrinterWorker.STATUS, true);
        Logger.v("printStatus --" + printStatus);
        boolean outPaper = false;
        if (printStatus)
            return 1;
        else if (!outOfPaper(print)) {
            outPaper = true;
//            reprint(print);
        }
        ((BaseActivity) context).resetTimer();
        return (outPaper) ? 0 : 3;
    }

    private void reprint(int print) {
        if (print == 5) {
            printReceipt();
        } else if (print == 2) {
            printReceipt();
        } else if (print == 3 || print == 4) {
            printSAFReceipt(print == 3);
        } else {
            printReceipt(print == 0);
        }
    }

    private boolean outOfPaper(final int print) {
        return Utils.checkPrinterPaper(context, new Utils.DialogeClick() {
            @Override
            public void onClick() {
                if (!outOfPaper(print))
                    reprint(print);
            }
        });
    }

    public void printReceipt() {
        Logger.v("Print 1");
        showApprovedMessage();
        if (checkAutomaticReversal() || !isPrinted) {
            showAlert.setValue(4);
            if (AppConfig.EMV.enableDatabaseUpdate) {
                repo.printReceipt(new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(final WorkInfo workInfo) {
                        Logger.v("printReceipt -State-2-" + workInfo.getState());
                        if (checkSuccessStatus(workInfo)) {
                            doPrintTransactionReceipt(devicePrinter, context, new PrintComplete() {
                                @Override
                                public void onFinish() {
                                    Logger.v("OnFinsh Receipt");
                                    isPrinted = true;
                                    if (checkPaper(workInfo, 2) != 0)
                                        transsactionReversal(false);
                                }
                            });
                        }
                    }
                });
            } else
                transsactionReversal(false);
        } else {
            Logger.v("TIMEOUT ---" + false);
            transsactionReversal(false);
        }
    }

    public void printFlow() {
        Logger.v("AppConfig.customerCopyPrinted -" + AppConfig.customerCopyPrinted);
        if (!AppConfig.customerCopyPrinted) {
            isSAFPrint = false;
            showAlert.setValue(11);
        } else
            moveNext(0);
    }

    public void savePrintFlow() {
        Logger.v("AppConfig.customerCopyPrinted save-" + AppConfig.customerCopyPrinted);
        if (isPrinted)
            printFlow();
        else
            onLinePrint(951);
    }


    public void moveNext(int ii) {
        Logger.v("Move Next -" + ii);
//        try {
        if (Utils.checkCardPresent(context)) {
            Logger.v("moveNext_checkCardPresent");
            showAlert.postValue(12);
        } else if (PrinterWorker.DO_PARTIAL_DOWNLOAD) {
            Logger.v("moveNext_DO_PARTIAL_DOWNLOAD");
            PrinterWorker.DO_PARTIAL_DOWNLOAD = false;
            MapperFlow.getInstance().moveToFullDownload(context);
        } else if (PrinterWorker.DO_RECON_AMOUNT) {
            Logger.v("moveNext_DO_RECON_AMOUNT");
            PrinterWorker.DO_RECON_AMOUNT = false;
            MapperFlow.getInstance().moveToReconsilation(context);
        } else {
            Logger.v("moveNext_moveToLandingPage");
            MapperFlow.getInstance().moveToLandingPage(context, true, 13);
        }
//        } catch (DeviceRTException e) {
//            MapperFlow.getInstance().moveToLandingPage(context, true, 14);
//        }
    }

    public void printSAFReceipt(final boolean customerCopy) {
        Logger.v("Print SAF");
        showAlert.setValue(4);
        repo.printSAFReceipt(new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                Logger.v("printReceipt -State-3-" + workInfo.getState());
                if (checkSuccessStatus(workInfo) && (checkPaper(workInfo, (customerCopy) ? 3 : 4)) != 0) {
                    doPrintTransactionReceipt(devicePrinter, context, new PrintComplete() {
                        @Override
                        public void onFinish() {
                            Logger.v("OnFinsh Receipt");
                            isSAFPrint = true;
                            if ((reqObj.getResponseCode39().equalsIgnoreCase(ConstantAppValue.SAF_DECLINED)
                                    || reqObj.getResponseCode39().equalsIgnoreCase(ConstantAppValue.SAF_APPROVED_UNABLE)
                                    || reqObj.getResponseCode39().equalsIgnoreCase(ConstantAppValue.SAF_DECLINED))
                                    && SimpleTransferListener.doReversal && SimpleTransferListener.madeOnlineConnection_new) {
                                ((BaseActivity) context).resetTimer();
                                transsactionReversal(false);
                            } else if (customerCopy)
                                showAlert.postValue(11);
                                //showAlert.setValue(11);

                            else
                                moveNext(1);
                        }
                    });
                }
            }
        });
    }

    public static ISOMsg unpackISOFormat(byte[] rawBytes) throws ISOException {
        ISOMsg isoMessage = new ISOMsg();
        isoMessage.setPackager(new MadaPackager());
        String hexReq = ByteConversionUtils.byteArrayToHexString(rawBytes, rawBytes.length, false);
        Logger.v("hexaresponse---" + hexReq);
        int reqLen = Integer.parseInt(hexReq.substring(0, 4), 16);
        Logger.v("reqLen---" + reqLen);
        byte[] afterBuffer = ISOUtil.hex2byte(hexReq.substring(4, hexReq.length()).trim().getBytes(), 0, reqLen);
        Logger.v("afterBuffer---" + afterBuffer);
        System.out.println(">>> " + ByteConversionUtils.byteArrayToHexString(afterBuffer, afterBuffer.length, true));
        if (reqLen == afterBuffer.length) {
            isoMessage.unpack(afterBuffer);
            isoMessage.dump(new PrintStream(System.err), "");
        } else {
            System.out.println("Receive the packet length not match...");
        }
        return isoMessage;
    }

    public void createISORequest() {
//        packet.createISORequest();
    }

    public void transsactionReversal(boolean repeat) {
        Logger.v("Reversal Initiated");
        showAlert.postValue(1);
        reversalSAF = true;
        repo.checkAllSAF(repeat, new Observer<WorkInfo>() {
            @Override
            public void onChanged(@Nullable WorkInfo workInfo) {
                if (workInfo != null) {
                    WorkInfo.State state = workInfo.getState();
                    Logger.v("checkSAF --" + state);
                    if (workInfo.getState() == WorkInfo.State.SUCCEEDED || workInfo.getState() == WorkInfo.State.FAILED) {
                        if (workInfo.getOutputData().getBoolean("SAF_TOAST", false))
                            ((BaseActivity) context).showToast(context.getString(R.string.invalid_service_ip));
                    }

                    if (workInfo.getState() == WorkInfo.State.SUCCEEDED || workInfo.getState() == WorkInfo.State.FAILED) {
                        int current_count = workInfo.getOutputData().getInt(SAFWorker.SAF_COUNT, -1);
                        Logger.v("current_count--" + current_count);
                        if (current_count == 3) {
                            final boolean manualReversal = AppManager.getInstance().isReversalManual();
                            if (manualReversal) {
                                Utils.dismissDialoge();
                                showAlert.setValue(15);
                            } else
                                reversalFlow();
                        } else if (current_count == -2) {
                            reversalFlow();
                        } else if (current_count == -1)
                            transsactionReversal(false);
                        else {
                            reversalFlow();
                        }
                    }
                }
            }
        });
    }

    public void reversalFlow() {
        Logger.v("reversalFlow()");
        showAlert.setValue(6);
        final boolean manualReversal = AppManager.getInstance().isReversalManual();
        repo.fetchDataFromDatabase(ConstantApp.PURCHASE_REVERSAL, new Observer<WorkInfo>() {
            @Override
            public void onChanged(@Nullable WorkInfo workInfo) {
                if (workInfo != null) {
                    WorkInfo.State state = workInfo.getState();
                    Logger.v("reversalFlow -Starte --" + state);
                    if (checkSuccessStatus(workInfo)) {
                        if (!manualReversal || workInfo.getOutputData().getBoolean(PacketDBInfoWorker.REFUND_STATUS, false)) {
//                            packet = new CreatePacket(context);
//                            packet.setCardScheme(workInfo.getOutputData().getString(PacketDBInfoWorker.CARD_SCHEME));
                            packet.setFallBack(false);
                            packet.addTransactionTypeData(ConstantApp.PURCHASE_REVERSAL, workInfo.getOutputData());
                            if (isScreenAvailable == 2)
                                makeConnection.setValue(CONNECT_ONLY);
                            else
                                makeConnection.setValue(SAVE_CONNECT);
                        } else {
                            showAlert.setValue(7);
                            // 4 - Refund TAG
//                            MapperFlow.moveToMenuClick(context, MenuModel.getTransactionMenu(context).get(4));
                        }
                    }
                }
            }
        });
    }

    public boolean isPinRequired() {
        boolean pin = repo.isPinRequires();
        Logger.v("repo.isPinRequires() -MVVM-" + pin);
        if (pin) {
            setPinOnline();
        } else {
            packet.disablePin();
        }
        return pin;
    }

    public void setPinOnline() {
        packet.setOnlinePin(true);
    }

    public void addMSCARDDetails() {
        Logger.v("addMSCARDDetails()");
        showAlert.setValue(1);
        SocketConnectionWorker.setEventHandler(getEventHandler(), 4);
//        repo.fetchDataFromDatabase(transactionType, AppConfig.EMV.swipResult.getAccount().getAcctNo(), new Observer<WorkInfo>() {
        Logger.v("transactionType----" + transactionType + "____" + "swipResult.getPan()----" + AppConfig.EMV.swipResult.getPan());

        /*//TODO replacing packetdbinfoworker with async
        try {
            new PacketDBInfoAsync(context, transactionType, PacketDBInfoAsync.MSCARD, AppConfig.EMV.swipResult.getPan().substring(0, 6), new PacketDBInfoListener() {
                @Override
                public void packetDbInfoResult(boolean result) {
                    if (result){
                        Logger.v("PacketDBInfoAsync.CARD_SCHEME----"+PacketDBInfoAsync.CARD_SCHEME);
                        ((PrintActivity) context).showCard(PacketDBInfoAsync.CARD_SCHEME,"");
                        if (checkCardCondition(transactionType)) {
                            if (validationDateAmount(transactionType)) {
                                String cardVerification = (PacketDBInfoAsync.CARD_VERIFICATION);
                                Logger.v("cardVerification --" + cardVerification);
                                Logger.v("cardVerification --" + Utils.checkCardIndication(cardVerification, transactionType));
                                if (packet.isFallBack()) {
                                    String data = (PacketDBInfoAsync.CARD_SCHEME);
                                    if ( data != null && (data).equalsIgnoreCase(ConstantAppValue.A0000002281010)) {
                                        showAlert.setValue(99);
                                        return;
                                    }
                                }
                                if (transactionType.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_FULL) || transactionType.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_PARTIAL) || transactionType.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_MANUAL)) {
                                    packet.disablePin();
                                    msFlow(PacketDBInfoAsync.CARD_SCHEME);
                                } else if (Utils.checkCardValidation(cardVerification, transactionType)) {
                                    packet.setOnlinePin(true);
                                    onlinePin.setValue(true);
                                } else if (Utils.checkCardIndication(cardVerification, transactionType).equalsIgnoreCase("0") && isPinRequired()) {
                                    packet.setOnlinePin(true);
                                    onlinePin.setValue(true);
                                } else {
                                    packet.disablePin();
                                    msFlow(PacketDBInfoAsync.CARD_SCHEME);
                                }
                            }
                        } else
                            showAlert.setValue(3);


                    }else {
                        if (PacketDBInfoAsync.INVALID_CARD== false)
                            showAlert.setValue(10);
                        else
                            showAlert.setValue(404);
                    }

                }
            }).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        repo.fetchDataFromDatabase(transactionType, AppConfig.EMV.swipResult.getPan(), new Observer<WorkInfo>() {
            @Override
            public void onChanged(@Nullable WorkInfo workInfo) {
                Logger.v("addMSCARDDetails_onChanged");
                if (workInfo != null) {
                    WorkInfo.State state = workInfo.getState();
                    Logger.v("fetchDataFromDatabase -Starte --" + state);
                    if (checkSuccessStatus(workInfo)) {
                        ((PrintActivity) context).showCard(workInfo.getOutputData().getString(PacketDBInfoWorker.CARD_SCHEME), "");
                        if (checkCardCondition(transactionType, workInfo.getOutputData())) {
                            if (validationDateAmount(transactionType, workInfo.getOutputData())) {
                                String cardVerification = workInfo.getOutputData().getString(PacketDBInfoWorker.CARD_VERIFICATION);
                                Logger.v("cardVerification --" + cardVerification);
                                Logger.v("cardVerification --" + Utils.checkCardIndication(cardVerification, transactionType));
                                msWorkInfo = workInfo;
                                if (packet.isFallBack()) {
                                    String data = msWorkInfo.getOutputData().getString(PacketDBInfoWorker.CARD_SCHEME);
                                    if (msWorkInfo != null && data != null && (data).equalsIgnoreCase(ConstantAppValue.A0000002281010)) {
                                        showAlert.setValue(99);
                                        return;
                                    }
                                }
                                if (transactionType.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_FULL) || transactionType.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_PARTIAL) || transactionType.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_MANUAL)) {
                                    packet.disablePin();
                                    msFlow();
                                } else if (Utils.checkCardValidation(cardVerification, transactionType)) {
                                    packet.setOnlinePin(true);
                                    onlinePin.setValue(true);
                                } else if (Utils.checkCardIndication(cardVerification, transactionType).equalsIgnoreCase("0") && isPinRequired()) {
                                    packet.setOnlinePin(true);
                                    onlinePin.setValue(true);
                                } else {
                                    packet.disablePin();
                                    msFlow();
                                }
                            }
                        } else
                            showAlert.setValue(3);
                    }
                }
            }
        });
    }

    public static WorkInfo msWorkInfo;

    public void msFlow(String cardScheme) {
        Logger.v("msFlow()_cardScheme----" + cardScheme);
        if (cardScheme != null && (cardScheme).equalsIgnoreCase(ConstantAppValue.A0000002281010)) {
            if (packet != null && packet.getPinStatus()) {
                if (pinBlock == null || pinBlock.length == 0) {
                    moveNext(121);
                }
            }
        }
        SocketConnectionWorker.TRANSACTION_START_TIME = Utils.getCurrentDate();
        if (pinBlock == null || pinBlock.length == 0) {
            Logger.v("packet.disablePin()");
            packet.disablePin();
        }
        packet.addCardDetails(transactionType, AppConfig.EMV.swipResult);
        makeConnection.setValue(SAVE_CONNECT);
    }

    public void msFlow() {
        Logger.v("msFlow()");
        String data = msWorkInfo.getOutputData().getString(PacketDBInfoWorker.CARD_SCHEME);
        if (msWorkInfo != null && data != null && (data).equalsIgnoreCase(ConstantAppValue.A0000002281010)) {
            if (packet != null && packet.getPinStatus()) {
                if (pinBlock == null || pinBlock.length == 0) {
                    moveNext(121);
                }
            }
        }
        SocketConnectionWorker.TRANSACTION_START_TIME = Utils.getCurrentDate();
        WorkInfo workInfo = msWorkInfo;
        if (pinBlock == null || pinBlock.length == 0) {
            Logger.v("packet.disablePin()");
            packet.disablePin();
        }
        packet.addCardDetails(transactionType, AppConfig.EMV.swipResult, workInfo.getOutputData());
        makeConnection.setValue(SAVE_CONNECT);
    }


    private boolean validationDateAmount(String transactionType, Data outputData) {
        String aid = outputData.getString(PacketDBInfoWorker.CARD_SCHEME);
        String amtEnabled = AppManager.getInstance().getTransactionAmountEnabled(aid);
        Logger.v("Ämount enabled--" + amtEnabled);
        if (Utils.checkTMSValidation(amtEnabled, transactionType)) {
            String amt = AppManager.getInstance().getMaxAmount(aid);
            Logger.v("Ämount enabled--" + amt);
            Logger.v("Amount -- " + AppConfig.EMV.amountValue);

            if (amt.trim().length() != 0) {
                double ammt = Double.parseDouble(amt);
                Logger.v("Amount -- " + amt);
                Logger.v("Amount -- " + ammt);
                Logger.v("Amount -- " + AppConfig.EMV.amountValue);
                if (ammt < AppConfig.EMV.amountValue) {
                    showAlert.setValue(13);
                    return false;
                } else {
                    return checkDate(aid);
                }
            } else
                return checkDate(aid);
        } else
            return checkDate(aid);
    }

    private boolean validationDateAmount(String transactionType) {
        String aid = (PacketDBInfoAsync.CARD_SCHEME);
        String amtEnabled = AppManager.getInstance().getTransactionAmountEnabled(aid);
        Logger.v("Ämount enabled--" + amtEnabled);
        if (Utils.checkTMSValidation(amtEnabled, transactionType)) {
            String amt = AppManager.getInstance().getMaxAmount(aid);
            Logger.v("Ämount enabled--" + amt);
            Logger.v("Amount -- " + AppConfig.EMV.amountValue);

            if (amt.trim().length() != 0) {
                double ammt = Double.parseDouble(amt);
                Logger.v("Amount -- " + amt);
                Logger.v("Amount -- " + ammt);
                Logger.v("Amount -- " + AppConfig.EMV.amountValue);
                if (ammt < AppConfig.EMV.amountValue) {
                    showAlert.setValue(13);
                    return false;
                } else {
                    return checkDate(aid);
                }
            } else
                return checkDate(aid);
        } else
            return checkDate(aid);
    }

    public boolean checkDate(String aid) {
//        if(transactionType.equalsIgnoreCase(Constant.PURCHASE_NAQD)){
//            String amt = AppManager.getInstance().getMaxCashAmount(aid);
//            Logger.v("Ämount enabled--" + amt);
//            Logger.v("Amount -- " + AppConfig.EMV.amtCashBack);
//            if (amt.trim().length() != 0) {
//                double ammt = Double.parseDouble(amt);
//                Logger.v("Amount -- " + amt);
//                Logger.v("Amount -- " + ammt);
//                Logger.v("Amount -- " + AppConfig.EMV.amtCashBack);
//                if (ammt < AppConfig.EMV.amtCashBack) {
//                    showAlert.setValue(13);
//                    return false;
//                }
//            }
//        }
        try {
            int YYYY = Calendar.getInstance().get(Calendar.YEAR);
            int mm = Calendar.getInstance().get(Calendar.MONTH);
            String YY = Integer.toString(YYYY).substring(2);
            int current = Integer.parseInt(YY + getMonth(mm));
            int date = Integer.parseInt(AppConfig.EMV.swipResult.getExpiry());
            Logger.v("Year --" + current);
            Logger.v("Year --" + date);
            Logger.v("Year --" + (date < current));
            if ((current <= date))
                return true;
            else {
                showAlert.setValue(10);
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert.setValue(10);
            return false;
        }
    }

    public static String getMonth(int mm) {
        int month = mm + 1;
        if ((month) < 10)
            return "0" + month;
        return "" + month;
    }

    public void addCHIPANDRFDetails(final String transactionType, final int connect) {
        Logger.v("addCHIPANDRFDetails---");
        showAlert.setValue(2);
        SocketConnectionWorker.setEventHandler(SimpleTransferListener.getPinEventHandler(), 5);
        repo.fetchDataFromDatabase(transactionType, new Observer<WorkInfo>() {
            @Override
            public void onChanged(@Nullable WorkInfo workInfo) {
                if (workInfo != null) {
                    WorkInfo.State state = workInfo.getState();
                    Logger.v("fetchDataFromDatabase -Starte --" + state);
                    if (checkSuccessStatus(workInfo)) {
                        if (checkCardCondition(transactionType, workInfo.getOutputData())) {
                            if (SimpleTransferListener.isGccNet)
                                packet.setGccCardIndicator();
                            packet.addCardDetails(workInfo.getOutputData(), transactionType, (AppConfig.EMV.consumeType == 1), (connect == SAVE_CONNECT_AGAIN));
                            makeConnection.setValue(connect);
                        } else {
                            notifyCancel();
                        }
                    }
                }
            }
        });


    }

    public void addCHIPANDRFDetailsDemo(final String transactionType) {
        showAlert.setValue(2);
        SocketConnectionWorker.setEventHandler(SimpleTransferListener.getPinEventHandler(), 5);
        repo.fetchDataFromDatabase(transactionType, new Observer<WorkInfo>() {
            @Override
            public void onChanged(@Nullable WorkInfo workInfo) {
                if (workInfo != null) {
                    WorkInfo.State state = workInfo.getState();
                    Logger.v("fetchDataFromDatabase -Starte --" + state);
                    if (checkSuccessStatus(workInfo)) {
                        if (checkCardCondition(transactionType, workInfo.getOutputData())) {
                            if (SimpleTransferListener.isGccNet)
                                packet.setGccCardIndicator();
                            packet.addCardDetails(workInfo.getOutputData(), transactionType, (AppConfig.EMV.consumeType == 1), false);
                            loadDemoPacket();
                        }
                    }
                }
            }
        });


    }

    private void loadDemoPacket() {
        String date = packet.expiryDate;
        TransactionModelEntity model = new TransactionModelEntity();
        model.setNameTransactionTag(reqObj.getNameTransactionTag());
        model.setCardIndicator(reqObj.getCardIndicator());
        model.setKernalID(reqObj.getSetKernalID());
        model.setModeTransaction(reqObj.getModeTransaction());
        model.setMti0(reqObj.getMti0());
        model.setPrimaryAccNo2(reqObj.getPrimaryAccNo2());
        model.setProcessingCode3(reqObj.getProcessingCode3());
        model.setAmtTransaction4(reqObj.getAmtTransaction4());
        model.setTransmissionDateTime7(reqObj.getTransmissionDateTime7());
        model.setSystemTraceAuditnumber11(reqObj.getSystemTraceAuditnumber11());
        model.setTimeLocalTransaction12(reqObj.getTimeLocalTransaction12());
        if (date != null)
            if (date.trim().length() == 5) {
                model.setDateExpiration14(date);
            } else {
                model.setDateExpiration14(date.substring(2) + "/" + date.substring(0, 2));
            }
        model.setPosEntrymode22(reqObj.getPosEntrymode22());
        model.setCardSequenceNumber23(reqObj.getCardSequenceNumber23());
        model.setFunctioncode24(reqObj.getFunctioncode24());
        model.setPosConditionCode25(reqObj.getMessageReasonCode25());
        model.setPosPinCaptureCode26(reqObj.getCardAcceptorBusinessCode26());
        model.setAmtTransFee28(reqObj.getReconsilationDate28());
        model.setAmtTranProcessingFee30(reqObj.getAmtTranProcessingFee30());
        model.setAccuringInsituteIdCode32(reqObj.getAccuringInsituteIdCode32());
        model.setTrack2Data35(reqObj.getTrack2Data35());
        model.setRetriRefNo37(reqObj.getRetriRefNo37());
        model.setAuthIdResCode38(reqObj.getAuthIdResCode38());
        model.setResponseCode39(reqObj.getResponseCode39());
        model.setCardAcceptorTemId41(reqObj.getCardAcceptorTemId41());
        model.setCardAcceptorIdCode42(reqObj.getCardAcceptorIdCode42());
        model.setAdditionalDataNational47(reqObj.getAdditionalDataNational47());
        model.setAdditionalDataPrivate48(reqObj.getAdditionalDataPrivate48());
        model.setCurrCodeTransaction49(reqObj.getCurrCodeTransaction49());
        model.setCurrCodeStatleMent50(reqObj.getCurrCodeStatleMent50());
        model.setSecRelatedContInfo53(reqObj.getSecRelatedContInfo53());
        model.setAddlAmt54(reqObj.getAddlAmt54());
        model.setIccCardSystemRelatedData55(reqObj.getIccCardSystemRelatedData55());
        model.setMsgReasonCode56(reqObj.getMsgReasonCode56());
        model.setEchoData59(reqObj.getEchoData59());
        model.setReservedData62(reqObj.getReservedData62());
        model.setReservedData62Responce(reqObj.getReservedData62Responce());
        model.setMessageAuthenticationCodeField64(reqObj.getMessageAuthenticationCodeField64());
        model.setDataRecord72(reqObj.getDataRecord72());
        model.setReservedData124(reqObj.getReservedData124());
        model.setMacExt128(reqObj.getMacExt128());
        model.setSaf(false);
        model.setEndTimeTransaction(Utils.getCurrentDate());

        String mti = Utils.fetchMti(reqObj.getMti0());
        if (mti.equalsIgnoreCase(ConstantAppValue.REVERSAL_RESPONSE))
            AppManager.getInstance().setReversalStatus(true);
        AppManager.getInstance().setResponseMTI(mti);
        String de39 = (reqObj.getMti0().equalsIgnoreCase(ConstantAppValue.REVERSAL)) ? "400" : "000";
        Logger.v("de39------" + de39);
        String de38 = ByteConversionUtils.generateTraceNumber(6);
        Logger.v("de38------" + de38);
        AppManager.getInstance().setDe55("8A023030");
        Logger.v("AppConfig.EMV.enableDatabaseUpdate --" + AppConfig.EMV.enableDatabaseUpdate);
        model.setStatusTransaction(1);
        model.setAuthIdResCode38(de38);
        model.setResponseCode39(de39);
        model.setIccCardSystemRelatedData55_final(model.getIccCardSystemRelatedData55());
        AppManager.getInstance().setDenugTransactionModelEntity(model);
        printReceipt(false);
    }

    private boolean checkCardCondition(String transactionType) {
        Logger.v("checkCardCondition---");
        packet.setCardScheme(PacketDBInfoAsync.CARD_SCHEME);
        Logger.v("CARD_SCHEME -" + (PacketDBInfoAsync.CARD_SCHEME));
        Logger.v("packet.isMada -" + transactionType);
        Logger.v("packet.isMada -" + packet.isMada);
        Logger.v("packet.isMada -" + (PacketDBInfoAsync.TRANSACTION_ALLOWED));
        switch (transactionType) {
            case ConstantApp.PURCHASE:
                return Utils.checkTMSValidation((PacketDBInfoAsync.TRANSACTION_ALLOWED), transactionType);
            case ConstantApp.REFUND:
                return Utils.checkTMSValidation((PacketDBInfoAsync.TRANSACTION_ALLOWED), transactionType);
            case ConstantApp.PURCHASE_NAQD:
                Logger.v("packet.isMada -" + packet.isMada);
//                if (packet.isMada) {
                return Utils.checkTMSValidation((PacketDBInfoAsync.TRANSACTION_ALLOWED), transactionType);
//                } else
//                    return false;
            case ConstantApp.CASH_ADVANCE:
                Logger.v("packet.isMada -" + packet.isMada);
                Logger.v("packet.isMada -" + (AppConfig.EMV.consumeType == 1));
                if (packet.isMada) {
                    return false;
                } else
                    return Utils.checkTMSValidation((PacketDBInfoAsync.TRANSACTION_ALLOWED), transactionType);
            case ConstantApp.PRE_AUTHORISATION:
                return Utils.checkTMSValidation((PacketDBInfoAsync.TRANSACTION_ALLOWED), transactionType);
            case ConstantApp.PRE_AUTHORISATION_VOID:
                if (packet.isMada) {
                    return Utils.checkTMSValidation((PacketDBInfoAsync.TRANSACTION_ALLOWED), transactionType);
                } else
                    return false;
            case ConstantApp.PRE_AUTHORISATION_EXTENSION:
                if (packet.isMada) {
                    return Utils.checkTMSValidation((PacketDBInfoAsync.TRANSACTION_ALLOWED), transactionType);
                } else
                    return false;
            case ConstantApp.PURCHASE_ADVICE_FULL:
                return Utils.checkTMSValidation((PacketDBInfoAsync.TRANSACTION_ALLOWED), transactionType);
            case ConstantApp.PURCHASE_ADVICE_PARTIAL:
                return Utils.checkTMSValidation((PacketDBInfoAsync.TRANSACTION_ALLOWED), transactionType);

            default:
                break;
        }
        return true;
    }

    private boolean checkCardCondition(String transactionType, Data outputData) {
        Logger.v("checkCardCondition---");
        packet.setCardScheme(outputData.getString(PacketDBInfoWorker.CARD_SCHEME));
        Logger.v("CARD_SCHEME -" + outputData.getString(PacketDBInfoWorker.CARD_SCHEME));
        Logger.v("packet.isMada -" + transactionType);
        Logger.v("packet.isMada -" + packet.isMada);
        Logger.v("packet.isMada -" + outputData.getString(PacketDBInfoWorker.TRANSACTION_ALLOWED));
        switch (transactionType) {
            case ConstantApp.PURCHASE:
                return Utils.checkTMSValidation(outputData.getString(PacketDBInfoWorker.TRANSACTION_ALLOWED), transactionType);
            case ConstantApp.REFUND:
                return Utils.checkTMSValidation(outputData.getString(PacketDBInfoWorker.TRANSACTION_ALLOWED), transactionType);
            case ConstantApp.PURCHASE_NAQD:
                Logger.v("packet.isMada -" + packet.isMada);
//                if (packet.isMada) {
                return Utils.checkTMSValidation(outputData.getString(PacketDBInfoWorker.TRANSACTION_ALLOWED), transactionType);
//                } else
//                    return false;
            case ConstantApp.CASH_ADVANCE:
                Logger.v("packet.isMada -" + packet.isMada);
                Logger.v("packet.isMada -" + (AppConfig.EMV.consumeType == 1));
                if (packet.isMada) {
                    return false;
                } else
                    return Utils.checkTMSValidation(outputData.getString(PacketDBInfoWorker.TRANSACTION_ALLOWED), transactionType);
            case ConstantApp.PRE_AUTHORISATION:
                return Utils.checkTMSValidation(outputData.getString(PacketDBInfoWorker.TRANSACTION_ALLOWED), transactionType);
            case ConstantApp.PRE_AUTHORISATION_VOID:
                if (packet.isMada) {
                    return Utils.checkTMSValidation(outputData.getString(PacketDBInfoWorker.TRANSACTION_ALLOWED), transactionType);
                } else
                    return false;
            case ConstantApp.PRE_AUTHORISATION_EXTENSION:
                if (packet.isMada) {
                    return Utils.checkTMSValidation(outputData.getString(PacketDBInfoWorker.TRANSACTION_ALLOWED), transactionType);
                } else
                    return false;
            case ConstantApp.PURCHASE_ADVICE_FULL:
                return Utils.checkTMSValidation(outputData.getString(PacketDBInfoWorker.TRANSACTION_ALLOWED), transactionType);
            case ConstantApp.PURCHASE_ADVICE_PARTIAL:
                return Utils.checkTMSValidation(outputData.getString(PacketDBInfoWorker.TRANSACTION_ALLOWED), transactionType);

            default:
                break;
        }
        return true;
    }

    public void readMSCARD() {
        repo.readMSCARD();
    }

    public void checkSAF(boolean repeat) {
        reversalSAF = false;
        repo.checkSAF(repeat, new Observer<WorkInfo>() {
            @Override
            public void onChanged(@Nullable WorkInfo workInfo) {
                if (workInfo != null) {
                    WorkInfo.State state = workInfo.getState();
                    Logger.v("checkSAF --" + state);
                    if (workInfo.getState() == WorkInfo.State.SUCCEEDED || workInfo.getState() == WorkInfo.State.FAILED) {
                        if (workInfo.getOutputData().getBoolean("SAF_TOAST", false))
                            ((BaseActivity) context).showToast(context.getString(R.string.invalid_service_ip));
                    }

                    if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                        int current_count = workInfo.getOutputData().getInt(SAFWorker.SAF_COUNT, -1);
                        Logger.v("current_count--" + current_count);
                        if (current_count != -1)
                            checkSAF(false);
                        else {
                            printFlow();
                        }
                    } else if (workInfo.getState() == WorkInfo.State.FAILED) {
                        printFlow();
                    }
                }
            }
        });
    }

    public void readChipInsert() {
        Logger.v("read_chip_transaction_viewmodel");
//        try {
        showAlert.setValue(1);
        repo.readInsert(this, transactionType);
//        } catch (EmvTransferException e) {
//            Logger.v("EmvTransferException ");
//        }
    }

    public void readRfCard() {
        Logger.v("read_rf_transaction_viewmodel");
//        try {
        showAlert.setValue(1);
        repo.readRfCard(transactionType, this);
//        } catch (EmvTransferException e) {
//            Logger.v("EmvTransferException ");
//        }
    }

    @Override
    public void onConnect(int i) {
        Logger.v("onConnect_interface_called");
        final int position = i;
        Logger.ve("Position --" + i);
        ((PrintActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                /*if (position == SimpleTransferListener.DO_READ_AND_PRINT) {
                    Logger.v("DO_READ_AND_PRINT---");
                    if (SimpleTransferListener.isPinRequires == 0)
                        packet.disablePin();
                    else if (SimpleTransferListener.isPinRequires == 1)
                        packet.setOnlinePin(true);
                    else
                        packet.setOnlinePin(false);
                    addCHIPANDRFDetailsDemo(transactionType);
                } else*/
                if (position == SimpleTransferListener.DO_SOCKET_REQ) {
                    Logger.v("DO_SOCKET_REQ---");
                    if (SimpleTransferListener.isPinRequires == 0)
                        packet.disablePin();
                    else if (SimpleTransferListener.isPinRequires == 1)
                        packet.setOnlinePin(true);
                    else
                        packet.setOnlinePin(false);
                    addCHIPANDRFDetails(transactionType, SAVE_CONNECT);
                } else if (position == SimpleTransferListener.DO_SOCKET_REQ_AGAIN) {
                    Logger.v("DO_SOCKET_REQ_AGAIN---");
                    packet.setOnlinePin(true);
                    addCHIPANDRFDetails(transactionType, SAVE_CONNECT_AGAIN);
                } else if (position == SimpleTransferListener.DO_FALLBACK) {
                    Logger.v("DO_FALLBACK---");
                    startFallbackListner();
                } else if (position == SimpleTransferListener.DO_FALLBACK_APP) {
                    Logger.v("DO_FALLBACK_APP---");
                    Utils.alertDialogShow(context, context.getString(R.string.app_blocked), listner);
                } else if (position == SimpleTransferListener.DO_REVERSAL) {
                    Logger.v("DO_REVERSAL---");
                    emvReversal = true;
                    checkReversal(reversalStatus, 2);
                } else if (position == SimpleTransferListener.DO_REVERSAL_CANCEL) {
                    Logger.v("DO_REVERSAL_CANCEL---");
                    emvReversal = true;
                    checkReversal(reversalStatus, 3);
                } else if (position == SimpleTransferListener.DO_TIMER) {
                    Logger.v("DO_TIMER---");
                    startTimer();
                } else if (position == SimpleTransferListener.DO_TIMER_REVERSAL) {
                    Logger.v("DO_TIMER_REVERSAL---");
                    restartTimer.setValue(true);
                    startCountDownTimer(true);
                } else if (position == SimpleTransferListener.DO_SAF) {
                    Logger.v("DO_SAF---");
                    addSAFRequest(0);
                } else if (position == SimpleTransferListener.DO_SAF_REJECTED) {
                    Logger.v("DO_SAF_REJECTED---");
                    addSAFRequest(1);
                } else if (position == SimpleTransferListener.DO_SAF_APPROVED) {
                    Logger.v("DO_SAF_APPROVED---");
                    addSAFRequest(3);
                } else if (position == SimpleTransferListener.FINAL_ERROR) {
                    Logger.v("FINAL_ERROR---");
                    if (!checkAdvice()) {
                        showFinalLaert();
                    } else {
                        printAfvice();
                    }
                } else if (position == SimpleTransferListener.DO_SAF_DECLINED) {
                    Logger.v("DO_SAF_DECLINED---");
                    restartTimer.setValue(true);
                    addSAFRequest(2);
                } else if (position == SimpleTransferListener.DO_ERROR) {
                    Logger.v("DO_ERROR---");
                    showAlert.setValue(0);
                } else if (position == SimpleTransferListener.DO_CARD_INVALID) {
                    Logger.v("DO_CARD_INVALID---");
                    showAlert.setValue(3);
                } else if (position == SimpleTransferListener.DO_AMOUNT_EXCEED) {
                    Logger.v("DO_AMOUNT_EXCEED---");
                    showAlert.setValue(13);
                } else if (position == SimpleTransferListener.DO_TRANSACTION) {
                    Logger.v("DO_TRANSACTION---");
                    showAlert.setValue(3);
                } else if (position == SimpleTransferListener.DO_ERROR_MESSAGE) {
                    Logger.v("DO_ERROR_MESSAGE---");
                    Utils.alertDialogShow(context, SimpleTransferListener.errorMsg, listner);
                } else if (position == SimpleTransferListener.DO_ERROR_PIN_CANCELLED) {
                    Logger.v("DO_ERROR_PIN_CANCELLED---");
                    Utils.alertDialogShow(context, context.getString(R.string.pin_error), listner);
                } else if (position == SimpleTransferListener.DO_CANCEL_AND_MOVE) {
                    Logger.v("DO_CANCEL_AND_MOVE---");
                    moveNext(2);
                } else if (position == SimpleTransferListener.DO_WAVE_AGAIN) {
                    Logger.v("DO_WAVE_AGAIN---");
                    spi.initListner(context, new SoundPool.OnLoadCompleteListener() {
                        @Override
                        public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                            Logger.v("Sound listner 000");
                            spi.playTwice();
                        }
                    });
                    new LightsDisplay(context).showRedLight();
                    showAlert.setValue(15);
                } else if (position == SimpleTransferListener.Do_PLZ_SEE_PHONE) {
                    spi.playTwice();
                    forceClose = true;
                    new LightsDisplay(context).showRedLight();
                    Utils.alertDialogShowBottom(context, "Please see Phone");
                    checkContactLessPresent();
                } else if (position == SimpleTransferListener.DO_BEEP_ONCE) {
                    Logger.v("DO_BEEP_ONCE---" + position + "==" + SimpleTransferListener.DO_BEEP_ONCE);
                    spi.initListner(context, new SoundPool.OnLoadCompleteListener() {
                        @Override
                        public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                            Logger.v("Sound listner 000");
                            spi.playBeep();
                        }
                    });
                    //spi.playBeep();
                } else if (position == SimpleTransferListener.DO_INVALID_PIN) {
                    Logger.v("DO_INVALID_PIN---");
                    Toast.makeText(context, context.getString(R.string.wrong_pin), Toast.LENGTH_SHORT).show();
                }
            }
        });
//        Message msg = handler.obtainMessage();
//        msg.what = 0;
//        handler.sendMessage(msg);
    }

    private void checkContactLessPresent() {
        startTimerRemoveCardNoSound();
    }

    public Reversal reversalStatus = new Reversal() {
        @Override
        public void doReversal() {
            Logger.v("DO Reversal 12");
            restartTimer.setValue(true);
            printReceipt();
        }

        @Override
        public void doRepeat() {
            Logger.v("DO Repeat 21");
            makeConnection.setValue(CONNECT_ONLY);
        }
    };

    public void waveAgain() {
        spi.initListner(context, new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                Logger.v("Sound listner 000");
                spi.playTwice();
            }
        });
        new LightsDisplay(context).showRedLight();
        showAlert.setValue(15);
    }

    private void addSAFRequest(final int status) {
        Logger.v("addSAFRequest : "+status);
        packet.disablePin();
        repo.fetchDataFromDatabase(transactionType, new Observer<WorkInfo>() {
            @Override
            public void onChanged(@Nullable WorkInfo workInfo) {
                if (workInfo != null) {
                    WorkInfo.State state = workInfo.getState();
                    Logger.v("addSAFRequest -Starte --" + state);
                    if (checkSuccessStatus(workInfo)) {
                        if (checkCardCondition(transactionType, workInfo.getOutputData())) {
                            packet.setOffline(status);
                            packet.setCardScheme(workInfo.getOutputData().getString(PacketDBInfoWorker.CARD_SCHEME));
                            packet.addCardDetails(workInfo.getOutputData(), transactionType, (AppConfig.EMV.consumeType == 1), false);
                            packet.setOffline(status);
                            safTransaction.setValue(true);
                        } else {
                            showAlert.setValue(404);
                        }
                    }
                }
            }
        });
    }

    public void startTimer() {
        Logger.v("Start Time 2");
        CountDownResponseTimer.startTimer(new CountDownResponseTimer.Timer() {
            @Override
            public void onFinished() {
                makeSocketRequest();
            }
        });
    }


    public void validateICResponse() {
        Logger.v("validateICResponse()");
        if (SimpleTransferListener.isCardInvalid) {
            showAlert.setValue(3);
            return;
        }

        if (SimpleTransferListener.isAmountExceed) {
            showAlert.setValue(13);
            return;
        }
        if (!checkAdvice()) {
            showFinalLaert();
        } else {
            printAfvice();
        }
//        if (AppConfig.EMV.emvTransInfo != null) {
//            Logger.v("Result --" + AppConfig.EMV.emvTransInfo.getExecuteRslt());
//            switch (AppConfig.EMV.emvTransInfo.getExecuteRslt()) {
//                case 0:
//                case 1:
//                case 2:
//                case 3:
//                    if (!checkAdvice()) {
//                        showFinalLaert();
//                    } else {
//                        printAfvice();
//                    }
////                    resultMsg = context.getString(R.string.msg_trans_acc);
//                    break;
//
////                    resultMsg = context.getString(R.string.msg_trans_reject);
//
////                    resultMsg = context.getString(R.string.msg_request_online);
////                break;
//                case -2105:
//                    showAlert.setValue(13);
////                    resultMsg = context.getString(R.string.msg_trans_exceed_limit);
//                    break;
//                default:
//                    showFinalLaert();
////                    showAlert.setValue(404);
////                    resultMsg = context.getString(R.string.msg_trans_failed);
//                    break;
//            }
//        }
    }

    private void showFinalLaert() {
        Logger.v("showFinalLaert()----AppConfig.printerDataAvailable----" + AppConfig.printerDataAvailable);
        if (AppConfig.printerDataAvailable)
            onLinePrint(0);
        else
            moveNext(666);
    }

    SdkSupport support = null;

    public void startFallbackListner() {
        if (transactionType.equalsIgnoreCase(ConstantApp.PURCHASE_NAQD)) {
            showAlert.setValue(99);
        } else {
            showAlert.setValue(9);
        }
    }

    public void closeFallBack(int i) {
        if (support != null)
            support.closeCardReader();
    }

    private void setFallBackView() {
        if (AppConfig.EMV.consumeType == 2) {
//            Utils.alertDialogShow(context,"Please see Phone");
        } else {
            Utils.alertDialogShowBottom(context, (context.getString(R.string.swipe_card_2)));
        }
        packet.setFallBack(true);
        support = new SdkSupport(context);
        support.closeCardReader();
        support.initFallbackReader();
    //    support.closeCardReader();
    }

    private boolean checkAdvice() {
        Logger.v("checkAdvice");
        if (AppManager.getInstance().isFinancialAdviceRequired() || AppManager.getInstance().isAuthorisationAdviceRequired()) {
            return true;
        }
        Logger.v("checkAdvice true");
        return false;
    }

    private void printAfvice() {
        if (checkAutomaticReversal() || !isPrinted) {
            showAlert.setValue(4);
            repo.printReceipt(new Observer<WorkInfo>() {
                @Override
                public void onChanged(final WorkInfo workInfo) {
                    Logger.v("printReceipt -State-1-" + workInfo.getState());
                    if (checkSuccessStatus(workInfo)) {
                        doPrintTransactionReceipt(devicePrinter, context, new PrintComplete() {
                            @Override
                            public void onFinish() {
                                Logger.v("OnFinsh Receipt");
                                isPrinted = true;
                                if (checkPaper(workInfo, 5) != 0) {
                                    makeAdviceRequest();
                                }
                            }
                        });
                    }
                }
            });
        } else {
            makeAdviceRequest();
        }
    }

    private void makeAdviceRequest() {
        if (AppManager.getInstance().isFinancialAdviceRequired()) {
            addCHIPANDRFDetails(ConstantApp.FINANCIAL_ADVISE, CONNECT_ONLY);
        } else if (AppManager.getInstance().isAuthorisationAdviceRequired()) {
            addCHIPANDRFDetails(ConstantApp.AUTHORIZATION_ADVICE, CONNECT_ONLY);
        }
    }

    public void readManualEntery(final String accNo, final String expDate) {
        Logger.v("manual_entry_txns");

        AppConfig.EMV.consumeType = 0;
        showAlert.setValue(1);
        SocketConnectionWorker.setEventHandler(getEventHandler(), 6);
        repo.fetchDataFromDatabase(transactionType, accNo, new Observer<WorkInfo>() {
            @Override
            public void onChanged(@Nullable WorkInfo workInfo) {
                if (workInfo != null) {
                    WorkInfo.State state = workInfo.getState();
                    // packet.setCardScheme((PacketDBInfoWorker.CARD_SCHEME)); //TODO satyedit
                    // Logger.v("card_indicator_for_manual_txns----"+(PacketDBInfoWorker.CARD_SCHEME));
                    // Logger.v("fetchDataFromDatabase -Starte --" + state);
                    if (checkSuccessStatus(workInfo)) {
                        if (Utils.checkNewAuth(transactionType)) {
                            packet.disablePin();
                            packet.addCardDetails(workInfo.getOutputData(), transactionType, accNo, expDate);
                            makeConnection.setValue(SAVE_CONNECT);
                        } else if (Utils.checkTMSValidation(workInfo.getOutputData().getString(PacketDBInfoWorker.MANUAL_ENTRY_ALLOWED), transactionType)) {
                            if (checkCardCondition(transactionType, workInfo.getOutputData())) {
                                Logger.v("inside_check_condition_manual_txns");

                                packet.setCardScheme(workInfo.getOutputData().getString(PacketDBInfoWorker.CARD_SCHEME));
                                packet.disablePin();
                                packet.addCardDetails(workInfo.getOutputData(), transactionType, accNo, expDate);
                                makeConnection.setValue(SAVE_CONNECT);
                                //context.startActivity(new Intent(context, LandingPageActivity.class));
                            } else
                                showAlert.setValue(3);
                        } else
                            showAlert.setValue(3);
                    }
                }
            }
        });
    }

    public LiveData<Integer> getConnectionStatus() {
        if (makeConnection == null) {
            Logger.v("makeConnection_getConnectionStatus");
            makeConnection = new MutableLiveData<>();

        }
        Logger.v("makeConnection---" + makeConnection);
        return makeConnection;
    }

    public LiveData<Integer> getShowAlert() {
        if (showAlert == null) {
            showAlert = new MutableLiveData<>();
        }
        return showAlert;
    }

    public LiveData<Boolean> getPrintStatus() {
        if (printRecipt == null) {
            printRecipt = new MutableLiveData<>();
        }
        return printRecipt;
    }

    public LiveData<Boolean> getSAFStatus() {
        if (safTransaction == null) {
            safTransaction = new MutableLiveData<>();
        }
        return safTransaction;
    }

    public LiveData<Boolean> getRestartTimer() {
        if (restartTimer == null) {
            restartTimer = new MutableLiveData<>();
        }
        return restartTimer;
    }

    public LiveData<Boolean> getOnLInePin() {
        if (onlinePin == null) {
            onlinePin = new MutableLiveData<>();
        }
        return onlinePin;
    }

    public static void notifyCancel() {
        Message pinFinishMsg = new Message();
        pinFinishMsg.what = AppConfig.EMV.SOCKET_CANCEL;
        pinFinishMsg.obj = new byte[]{};
        SimpleTransferListener.getPinEventHandler().sendMessage(pinFinishMsg);
    }

    public static void notifyUnableToGoOnline() {
        Message pinFinishMsg = new Message();
        pinFinishMsg.what = AppConfig.EMV.SOCKET_UNABEL_ONLINE;
        pinFinishMsg.obj = new byte[]{};
        SimpleTransferListener.getPinEventHandler().sendMessage(pinFinishMsg);
    }

    public void notifySuccess() {
        Logger.v("AppManager.getInstance().getDe55() --" + AppManager.getInstance().getDe55());
        Message pinFinishMsg = new Message();
        pinFinishMsg.what = AppConfig.EMV.SOCKET_FINISH;
        String value = "";
        if (AppManager.getInstance().getDe38() != null)
            value = AppManager.getInstance().getDe38() + "__";
        if (AppManager.getInstance().getDe55() != null)
            value = value + AppManager.getInstance().getDe55();
        pinFinishMsg.obj = value;
        SimpleTransferListener.getPinEventHandler().sendMessage(pinFinishMsg);
    }

    public void loopPlay() {
        spi.loopPlay();
    }

    public void startTimerRemoveCard() {
        Utils.alertDialogShow(context, context.getString(R.string.remove_card));
        initSound();
//        try {
//            final ICCardModule iCCardModule = SDKDevice.getInstance(context).getICCardModule();
//            final K21RFCardModule rfCardModule = SDKDevice.getInstance(context).getRFCardModule();
        final CountDownTimer timer = new CountDownTimer(3000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                Logger.v("Tick Tick");
            }

            @Override
            public void onFinish() {
//                    try {
                Logger.v("OnTick");
//                        Map<ICCardSlot, ICCardSlotState> map = iCCardModule.checkSlotsState();
                if (AppConfig.EMV.consumeType == 2) {
                    AppConfig.isCardRemoved = false;
//                            AppConfig.isCardRemoved = rfCardModule.isRfcardExist();
                } else {
//                            for (Map.Entry<ICCardSlot, ICCardSlotState> entry : map.entrySet()) {
//                                if (entry.getKey() != null)
//                                    Logger.v("Key --" + entry.getKey() + "Value --" + entry.getValue());
//                                if (!entry.getValue().toString().equals("NO_CARD")) {
//                                    AppConfig.isCardRemoved = true;
//                                }
//                            }
                }
                if (!AppConfig.isCardRemoved) {
                    cancel();
                    spi.removeBeep();
                    moveNext(17);
                 //   moveNextFallback(17);
                } else {
                    spi.removeBeep();
                    startTimerRemoveCard();
                }
                Logger.v("OnTick END");
//                    } catch (DeviceRTException e) {
//                        spi.removeBeep();
//                        Logger.v("print_catch");
//                        moveNext(19);
//                    }
            }
        };
        timer.start();
//        } catch (DeviceRTException e) {
//            moveNext(199);
//        }
    }

    public void startTimerRemoveCardFallback() {
        Utils.alertDialogShowBottom(context, "Not Acceptable \n Please swipe \n Please remove card");
        initSound();
        final CountDownTimer timer = new CountDownTimer(3000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                Logger.v("Tick Tick");
            }

            @Override
            public void onFinish() {

                if (AppConfig.EMV.consumeType == 2) {
                    AppConfig.isCardRemoved = false;
                } else {

                }

                if (!AppConfig.isCardRemoved) {
                    cancel();
                    spi.removeBeep();
                    if (packet.isFallBack()) {
                        moveNextFallback(17);
                    } else {
                        moveNext(17);
                    }

                } else {
                    spi.removeBeep();
                    startTimerRemoveCardFallback();
                }

                Logger.v("OnTick END");

            }
        };
        timer.start();

    }

    public void startTimerRemoveCardNoSound() {
        try {
            final CountDownTimer timer = new CountDownTimer(3000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    Logger.v("Tick Tick");
                }

                @Override
                public void onFinish() {
                    try {
                        Logger.v("OnTick");
                        if (AppConfig.EMV.consumeType == 2) {
                            AppConfig.isCardRemoved = false;
                        } else {

                        }
                        if (!AppConfig.isCardRemoved) {
                            cancel();
                            moveNextPhone(17);
                        } else {
                            spi.removeBeep();
                            startTimerRemoveCardNoSound();
                        }
                        Logger.v("OnTick END");
                    } catch (Exception e) {
                        Logger.v("print_catch");
                        moveNextPhone(19);
                    }
                }
            };
            timer.start();
        } catch (Exception e) {
            moveNextPhone(199);
        }
    }

    private void moveNextPhone(int i) {
        new LightsDisplay(context).showBlueLight();
        Utils.dismissDialoge();
        startDefaultListner();
    }

    private void moveNextFallback(int i) {
        Logger.v("moveNextFallback -" + i);
        setFallBackView();
//        Utils.dismissDialoge();
    }


    private static Handler eventHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Logger.v("Handle msg");
            switch (msg.what) {
                case AppConfig.EMV.SOCKET_FINISH:
                    break;
                case AppConfig.EMV.SOCKET_CANCEL:
                    break;
                case AppConfig.EMV.START_TIMER:
                    startTimer.setValue(true);
                    break;
                case AppConfig.EMV.START_TIMER_REVERSE:
                    restartTimer.setValue(true);
                    startTimer.setValue(true);
                    break;
                case AppConfig.EMV.STOP_TIMER:
                    startTimer.setValue(true);
                    break;
                case AppConfig.EMV.PRINT_CUSTOMER_COPY:
                    break;
                case AppConfig.EMV.PRINT_CUSTOMER_COPY_CANCEL:
                    break;
                case AppConfig.EMV.SAF_TIMER_:
//                    count_repeat = (int) msg.obj;
                    if (startTimer != null && SAFWorker.safTimerInitaited)
                        startTimer.setValue(false);
                    break;
                default:
                    break;
            }
        }

    };

    public static Handler getEventHandler() {
        return eventHandler;
    }

    public void startCountDownTimer(Boolean aBoolean) {
        if (aBoolean) {
            Logger.v("Start Time 3");
            CountDownResponseTimer.startTimer(new CountDownResponseTimer.Timer() {
                @Override
                public void onFinished() {
                    Logger.v("Strat Count Tim");
                    checkReversal(reversalStatus, 4);
                }
            });
        } else {
            startSAFTimer();
        }
    }

    public LiveData<Boolean> initiateTimer() {
        if (startTimer == null) {
            startTimer = new MutableLiveData<>();
        }
        return startTimer;
    }

    public LiveData<Boolean> initiateRemoveCardTimer() {
        if (cardRemoveTimer == null) {
            cardRemoveTimer = new MutableLiveData<>();
        }
        return cardRemoveTimer;
    }

    public LiveData<Boolean> safRepeat() {
        if (safRepeat == null) {
            safRepeat = new MutableLiveData<>();
        }
        return safRepeat;
    }

    public void startSAFTimer() {
        CountDownResponseTimer.startTimerSAF(new CountDownResponseTimer.Timer() {
            @Override
            public void onFinished() {
                Logger.v("Timer -12");
//                SAFWorker.count = 0;
                if (SAFWorker.isSAFRepeat)
                    safRepeat.setValue(true);
            }
        });
    }

    public void showAlert(int i) {
        showAlert.setValue(i);
    }


    public Observer shoeAlertObserver = new Observer<Integer>() {
        @Override
        public void onChanged(@Nullable Integer status) {
            Logger.v("getConnectionStatusTransactionViewmodel -" + status);
            Utils.dismissDialoge();
            if (status != null) {
                if (status == 404) {
                    Utils.alertDialogShow(context, context.getString(R.string.unsuccessful), listner);
                } else if (status == 100) {
                    if (AppConfig.EMV.consumeType == 2)
                        initSoundPlay(false);
                    String authCode = "";
                    if (AppManager.getInstance().getDe38() != null)
                        authCode = (AppManager.getInstance().getDe38());
                    if (authCode.trim().length() != 0) {
                        Utils.alertDialogOneShow(context, context.getString(R.string.approved) + "-" + Utils.formatLanguageNumber(activity, authCode));
                    } else {
                        Utils.alertDialogOneShow(context, context.getString(R.string.approved));
                    }
                } else if (status == 102) {
                    String authCode = "";
                    if (AppManager.getInstance().getDe38() != null)
                        authCode = (AppManager.getInstance().getDe38());
                    if (authCode.trim().length() != 0) {
                        Utils.alertDialogOneShow(context, context.getString(R.string.accepted) + "-" + Utils.formatLanguageNumber(activity, authCode));
                    } else {
                        Utils.alertDialogOneShow(context, context.getString(R.string.accepted));
                    }
                } else if (status == 101) {
                    if (AppConfig.EMV.consumeType == 2)
                        initSoundPlay(true);
                    Utils.alertDialogOneShow(context, context.getString(R.string.declined));
                } else if (status == 0) {
                    Utils.alertDialogShow(context, context.getString(R.string.encountered_error), listner);
                } else if (status == 1) {
                    if (cardDetailShown)
                        Utils.alertDialogShow(context, context.getString(R.string.please_wait));
                    else
                        Utils.alertDialogShowBottom(context, context.getString(R.string.please_wait));
                } else if (status == 2) {
                    if (isScreenAvailable == 1) {
                        if (cardDetailShown)
                            Utils.alertDialogShow(context, context.getString(R.string.processing));
                        else
                            Utils.alertDialogShowBottom(context, context.getString(R.string.processing));
                    }
                } else if (status == 3) {
                    Utils.alertDialogShow(context, context.getString(R.string.transaction_not_allowed), listner);
                } else if (status == 4) {
                    Logger.v("statusMsg --" + statusMsg + "--" + showApprovedBeepOffline);
                    Logger.v("statusApproved --" + statusApproved);
                    statusMsg = (isScreenAvailable == 2) ? declinedFlow() : statusMsg;
                    if (!isPrinted && statusMsg.trim().length() != 0) {
                        if (showApprovedBeepOffline && isScreenAvailable != 2) {
                            if (AppConfig.EMV.consumeType == 2)
                                initSoundPlay(false);
                        }
                        Utils.alertDialogShowStatus(context, context.getString(R.string.printing), statusMsg, statusApproved);
                        statusMsg = "";
                    } else {
                        Utils.alertDialogShow(context, context.getString(R.string.printing));
                    }
                } else if (status == 5) {
                    Utils.alertDialogShow(context, context.getString(R.string.saf_approved), listner);
                } else if (status == 6) {
                    Utils.alertDialogOneShowBottom(context, context.getString(R.string.reversal_initialed));
                } else if (status == 7) {
                    Utils.alertDialogShow(context, context.getString(R.string.reversal_time_out), listner);
                } else if (status == 9) {
                    forceClose = true;
                    if (AppConfig.EMV.consumeType == 2) {
                        String messgae = (AppConfig.EMV.consumeType == 2) ? context.getString(R.string.fallback__insert_swipe_card) : context.getString(R.string.fallback_swipe_card);
                        Utils.alertDialogShow(context, messgae, new View.OnClickListener() {
                            @Override
                            public void onClick(View dialog) {
                                setFallBackView();
                                Utils.dismissDialoge();
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View dialog) {
                                Utils.dismissDialoge();
                                moveNext(3);
                            }
                        });
                    } else {
                        AppConfig.isCardRemoved = true;
                //        new SdkSupport(context).startAllListner();
                        startTimerRemoveCardFallback();
                    }
                } else if (status == 99) {
                    forceClose = true;
                    Utils.alertDialogShow(context, context.getString(R.string.fallback_not_allowed), listner);
                } else if (status == 10) {
                    Utils.alertDialogShow(context, context.getString(R.string.invalid_card), listner);
                } else if (status == 11) {
                    processEreceipt(MPortalTransactionModel.SEND_QR, "");
//                    String de39 = PrinterWorker.printDe30.trim();
//                    if (de39.equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_000) || de39.equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_001) || de39.equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_003)
//                            || de39.equalsIgnoreCase(ConstantAppValue.SAF_APPROVED) || de39.equalsIgnoreCase(ConstantAppValue.SAF_APPROVED_UNABLE)
//                            || de39.equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_007) || de39.equalsIgnoreCase(ConstantApp.REVERSAL_RESPONSE_400)) {
//                        startCustomerPrintTimer();
//                        Utils.alertDialogShow(context, context.getString(R.string.continue_to_print_customer_copy),
//                                new View.OnClickListener() {
//                                    public void onClick(View dialog) {
//                                        Utils.dismissDialoge();
//                                        printCustomerCopy();
//                                    }
//                                }, new View.OnClickListener() {
//                                    public void onClick(View dialog) {
//                                        Logger.v("Customer - 2");
//                                        AppConfig.customerCopyPrinted = true;
//                                        cancelCustomerCopy();
//                                        Utils.dismissDialoge();
//                                        moveNext(4);
//                                    }
//                                });
//                    } else {
//                        AppConfig.customerCopyPrinted = true;
//                        moveNext(44);
//                    }
                } else if (status == 12) {
                    cardRemoveTimer.setValue(true);
                    Utils.alertDialogShow(context, context.getString(R.string.remove_card));
                    Logger.v("STATUS 12");
                } else if (status == 13) {
                    Utils.alertDialogShow(context, context.getString(R.string.amount_exceed_limit), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Utils.dismissDialoge();
                            MapperFlow.getInstance().moveToReEnterAmount(context);
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Utils.dismissDialoge();
                            moveNext(6);
                        }
                    });
                } else if (status == 14) {
                    Utils.alertDialogShow(context, context.getString(R.string.pin_okay));
                } else if (status == 15) {
                    Logger.v("waveRettryCount --" + waveRettryCount);
                    if (waveRettryCount < 3) {
                        forceClose = true;
                        Toast.makeText(context, context.getString(R.string.wave_again), Toast.LENGTH_SHORT).show();
                        new LightsDisplay(context).showBlueLight();
                        waveRettryCount = waveRettryCount + 1;
                        startDefaultListner();
                    } else {
                        moveNext(55);
                    }
                }
            }
        }
    };

    private Thread thread;
    boolean forceCancel = false;
    private void processEreceipt(final String flow, String data) {
        if (printerModel == null || (!AppManager.getInstance().isMerchantPoratalEnable())) {
            if (printerModel != null) {
                merchantPortalFloe("");
            } else {
                Logger.v("else");
                AppConfig.customerCopyPrinted = true;
                moveNext(44);
            }
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
                if(forceCancel)
                    return;
                try {
                    thread.stop();
                } catch (Exception e) {

                }
                merchantPortalFloe("");
            }
        };

        MPortalTransactionModel hostPacket = new MPortalTransactionModel(printerModel);
        final String requestPack = hostPacket.mergePrintModel(flow, data);
        final String pedningReq = hostPacket.getPendingAppend();
        final String IP = AppManager.getInstance().getMerchantIP();
        final int port = Integer.parseInt(AppManager.getInstance().getMerchantPort());
        final InputStream inputStream = context.getResources().openRawResource(R.raw.newcerten);
        final String version = AppManager.getInstance().getString(ConstantApp.HSTNG_TLS);
        final AppDatabase database = AppDatabase.getInstance(context.getApplicationContext());
        Logger.v("lastUUid -" + safTable);

        final Handler handler = new Handler();
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Replace below IP with the IP of that device in which server socket open.
                    //If you change port then change the port number in the server side code also.
                    Logger.v("printerModel.getStan() --" + printerModel.getStan());
                    byte[] request = toHexadecimal(requestPack);
                    byte[] requestPending = toHexadecimal(pedningReq);
                    if (safTable)
                        database.getSAFDao().updateSAFMerchantPortalRequest(printerModel.getStan(), ByteConversionUtils.byteArrayToHexString(requestPending, requestPending.length, false));
                    else
                        database.getTransactionDao().updateSAFMerchantPortalRequest(printerModel.getStan(), ByteConversionUtils.byteArrayToHexString(requestPending, requestPending.length, false));

                    SSLSocketFactoryExtended sslsocketfactory = new SSLSocketFactoryExtended(inputStream, version, false);
                    Logger.v("Socket 1");
                    Logger.v("IP-PORT --" + IP + "--" + port);
                    SSLSocket requestSocket = (SSLSocket) sslsocketfactory.createSocket(IP, port);
                    Logger.v("Socket connected Merchant-" + requestSocket.isConnected());
                    BufferedInputStream bis = new BufferedInputStream(requestSocket.getInputStream());
                    BufferedOutputStream bos = new BufferedOutputStream(requestSocket.getOutputStream());
                    // 2: Communicating with the server
                    Logger.v("TRY block send");
                    // 3: Post the request data

                    bos.write(request);
                    bos.flush();

                    // 4: Receive the response data
                    byte[] buffer = new byte[1024];
                    int nBytes = -1;
                    Logger.v("TRY block Receive");
                    while ((nBytes = bis.read(buffer)) >= 0) {
                        Logger.v("buffer --" + buffer.length);
                        Logger.v(buffer);
                        if (safTable) {
                            database.getSAFDao().updateSAFMerchantPortal(printerModel.getStan(), true, "");
                        } else {
                            database.getTransactionDao().updateSAFMerchantPortal(printerModel.getStan(), true, "");
                        }
                        final String output = ByteConversionUtils.byteArrayToHexString(buffer, buffer.length, false);
                        bos.flush();

                        TransactionModelEntity oldRequest = database.getTransactionDao().getMPortalRequest("", false);
                        if (oldRequest == null) {
                            Logger.v("Fetched from SAF");
                            safTable = true;
                            oldRequest = database.getSAFDao().getMPortalRequest("", false);
                        }
                        if (oldRequest != null) {
                            Logger.v("OLD transaction not null");
                            try {
                                Logger.v("Socket connected Merchant-" + requestSocket.isConnected());
                                closeRequest(requestSocket);
                                Logger.v("Socket connected Merchant-" + requestSocket.isConnected());
                                requestSocket = (SSLSocket) sslsocketfactory.createSocket(IP, port);
                                bis = new BufferedInputStream(requestSocket.getInputStream());
                                bos = new BufferedOutputStream(requestSocket.getOutputStream());
                                Logger.v(oldRequest.getRequest_mportal());
                                bos.write(ByteConversionUtils.HexStringToByteArray(oldRequest.getRequest_mportal()));
                                bos.flush();
                                // 4: Receive the response data
                                byte[] buffer1 = new byte[1024];
                                int nBytes1 = -1;
                                Logger.v("Socket connected Merchant-" + requestSocket.isConnected());
                                Logger.v("TRY block Receive");
                                while ((nBytes = bis.read(buffer1)) >= 0) {
                                    final String output1 = ByteConversionUtils.byteArrayToHexString(buffer1, buffer1.length, false);
                                    String result1 = ByteConversionUtils.convertHexToString(output1);
                                    Logger.v("Result -" + result1);
                                    try {
                                        String[] tags = result1.split("<GS>");
                                        Logger.v(tags);
                                        if (tags[1].equalsIgnoreCase("200")) {
                                            if (safTable)
                                                database.getSAFDao().updateSAFMerchantPortal(oldRequest.getSystemTraceAuditnumber11(), true, "");
                                            else
                                                database.getTransactionDao().updateSAFMerchantPortal(oldRequest.getSystemTraceAuditnumber11(), true, "");
                                        }
                                    }catch (Exception e){
                                        Logger.v("Exception 33");
                                    }
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            forceCancel = true;
                                            mPortalTimer.cancel();
                                            mPortalTimer.onFinish();
                                            if (flow.equalsIgnoreCase(MPortalTransactionModel.SEND_QR)) {
                                                try {
                                                    String result = ByteConversionUtils.convertHexToString(output);
                                                    Logger.v("Result -" + result);
                                                    String[] tags = result.split("<GS>");
                                                    Logger.v(tags);
                                                    if (tags[1].equalsIgnoreCase("200")) {
                                                        merchantPortalFloe(tags[3]);
                                                    } else {
                                                        Logger.v("else");
                                                        AppConfig.customerCopyPrinted = true;
                                                        moveNext(44);
                                                    }

                                                } catch (Exception e) {
                                                    Logger.v("Exception eee");
                                                    AppConfig.customerCopyPrinted = true;
                                                    moveNext(44);
                                                }
                                            } else {
                                                boolean sms = flow.equalsIgnoreCase(MPortalTransactionModel.SEND_SMS);
                                                Toast.makeText(context, sms ? "SMS Sent Successfully" : "Email Send Successfully", Toast.LENGTH_SHORT).show();
                                                AppConfig.customerCopyPrinted = true;
                                                moveNext(44);
                                            }
                                        }
                                    });
                                    return;
                                }
                            } catch (Exception e) {
                                Logger.v("Exception eee");
                            }
                        }
                        closeRequest(requestSocket);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                forceCancel = true;
                                mPortalTimer.cancel();
                                mPortalTimer.onFinish();
                                if (flow.equalsIgnoreCase(MPortalTransactionModel.SEND_QR)) {
                                    try {
                                        String result = ByteConversionUtils.convertHexToString(output);
                                        Logger.v("Result -" + result);
                                        String[] tags = result.split("<GS>");
                                        Logger.v(tags);
                                        if (tags[1].equalsIgnoreCase("200")) {
                                            merchantPortalFloe(tags[3]);
                                        } else {
                                            Logger.v("else");
                                            AppConfig.customerCopyPrinted = true;
                                            moveNext(44);
                                        }

                                    } catch (Exception e) {
                                        Logger.v("Exception eee");
                                        AppConfig.customerCopyPrinted = true;
                                        moveNext(44);
                                    }
                                } else {
                                    boolean sms = flow.equalsIgnoreCase(MPortalTransactionModel.SEND_SMS);
                                    Toast.makeText(context, sms ? "SMS Sent" : "Email Send", Toast.LENGTH_SHORT).show();
                                    AppConfig.customerCopyPrinted = true;
                                    moveNext(44);
                                }
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

    private void closeRequest(SSLSocket requestSocket) {
        try {
            if (requestSocket.isClosed()) {
                requestSocket.close();
            }
        } catch (Exception e) {
            Logger.v("Exceptin ssl");
        }
    }

    public static byte[] toHexadecimal(String text) throws UnsupportedEncodingException {
        Logger.v("SAMPLE text 11");
        byte[] inputData = ((text.getBytes("UTF-8")));
        byte[] appendData = ISOUtil.concat(inputData, ByteConversionUtils.HexStringToByteArray("0D0A7D"));
        Logger.v(appendData);
        byte[] echoResponseLength = ByteConversionUtils.intToByteArray(appendData.length); //+TPDU_HEADER_LENGTH);
        byte[] finalData = ISOUtil.concat(echoResponseLength, appendData);
        Logger.v(finalData);
        return finalData;
//        return ByteConversionUtils.HexStringToByteArray("02747B226D65726368616E744E616D65223A224469676974616C20506179222C227472616E73616374696F6E54797065223A225055524348415345222C227465726D696E616C44617465223A2230382F31322F32303230222C227465726D696E616C54696D65223A2230393A32363A3337222C22636172644E616D65223A2252414A42222C22744964223A2231323334353637383132313231323334222C226D4964223A22383030313530343030353636222C226D6363223A2237333939222C227374616E223A22313030353333222C226170706C69636174696F6E56657273696F6E223A2256312E312E32222C2263617264536368656D614E616D65223A226D616461222C22636172644E756D6265724D61736B223A223435353033362A2A2A2A2A2A37363139222C2263617264457870223A2231322F3230222C22616D6F756E74223A22534152313434342E3434222C227472616E73616374696F6E537461747573223A22415050524F564544222C22617070726F76616C436F6465223A22303937363139222C2273657276657244617465223A2230382F31322F32303230222C2273657276657254696D65223A2230393A32363A3530222C226361726454797065223A22434F4E544143544C455353222C2264653339223A22303030222C2263617264414964223A224130303030303032323832303130222C22747672223A2230303030303030303030222C22747369223A2230303030222C2263766D223A22222C2261717263223A223830222C22656D7664617461223A2241423843413943413643343638373646222C22656D766B65726E656C223A223033222C2272726E223A22303334333039303932363236220D0A7D");
    }

    private void merchantPortalFloe(String url) {
//        if(true){
//            Utils.alertDialogShow(url,context,
//                    new View.OnClickListener() {
//                        public void onClick(View dialog) {
//                            Utils.dismissDialoge();
//                            printCustomerCopy();
//                        }
//                    }, new View.OnClickListener() {
//                        public void onClick(View dialog) {
//                            cancelPrintFlow();
//                        }
//                    }, new View.OnClickListener() {
//                        public void onClick(View dialog) {
//                            cancelCustomerCopy();
//                            showAlertDialoge(true);
//                        }
//                    }, new View.OnClickListener() {
//                        public void onClick(View dialog) {
//                            cancelCustomerCopy();
//                            showAlertDialoge(false);
//                        }
//                    });
//            return;
//        }
        String de39 = PrinterWorker.printDe30.trim();
        if (de39.equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_000) || de39.equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_001) || de39.equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_003)
                || de39.equalsIgnoreCase(ConstantApp.SAF_APPROVED) || de39.equalsIgnoreCase(ConstantApp.SAF_APPROVED_UNABLE)
                || de39.equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_007) || de39.equalsIgnoreCase(ConstantApp.REVERSAL_RESPONSE_400)) {
            startCustomerPrintTimer();
            Utils.alertDialogShow(url, context,
                    new View.OnClickListener() {
                        public void onClick(View dialog) {
                            Utils.dismissDialoge();
                            printCustomerCopy();
                        }
                    }, new View.OnClickListener() {
                        public void onClick(View dialog) {
                            cancelPrintFlow();
                        }
                    }, new View.OnClickListener() {
                        public void onClick(View dialog) {
                            cancelCustomerCopy();
                            showAlertDialoge(true);
                        }
                    }, new View.OnClickListener() {
                        public void onClick(View dialog) {
                            cancelCustomerCopy();
                            showAlertDialoge(false);
                        }
                    });
        } else {
            AppConfig.customerCopyPrinted = true;
            moveNext(44);
        }
    }

    private void showAlertDialoge(final boolean isSMS) {
        final Dialog alertDialog = new Dialog(context);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (alertDialog.getWindow() != null)
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.setContentView(R.layout.custom_dialog_transparent_3);
        alertDialog.setCanceledOnTouchOutside(false);
        final EditText etd_sms = alertDialog.findViewById(R.id.etd_sms);
        final EditText etd_email = alertDialog.findViewById(R.id.etd_email);
        TextView txt_hint = alertDialog.findViewById(R.id.txt_hint);
        Button ok = alertDialog.findViewById(R.id.ok);
        Button cancel = alertDialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCustomerPrintTimer();
                alertDialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etd_sms.getText().toString().trim().length() == 0 && etd_email.getText().toString().trim().length() == 0) {
                    String tostData = (isSMS) ? "Mobile number is empty \n رقم الجوال فارغ" : "Email id is Empty \n معرف البريد الإلكتروني فارغ";
                    Toast.makeText(context, tostData, Toast.LENGTH_SHORT).show();
                    return;
                }
                alertDialog.dismiss();
                processEreceipt((isSMS) ? MPortalTransactionModel.SEND_SMS : MPortalTransactionModel.SEND_MAIL
                        , (isSMS) ? etd_sms.getText().toString() : etd_email.getText().toString());
            }
        });
        if (isSMS) {
            txt_hint.setText("Enter Phone number");
            etd_email.setVisibility(View.GONE);
            etd_sms.setVisibility(View.VISIBLE);
        } else {
            txt_hint.setText("Enter Email address");
            etd_email.setVisibility(View.VISIBLE);
            etd_sms.setVisibility(View.GONE);
        }
        alertDialog.show();

    }

    private void cancelPrintFlow() {
        Logger.v("Customer - 2");
        AppConfig.customerCopyPrinted = true;
        cancelCustomerCopy();
        Utils.dismissDialoge();
        moveNext(4);
    }

    private void startDefaultListner() {
        support = new SdkSupport(context);
        support.initReader();
    }

    CountDownTimer timerPrint = new CountDownTimer(10000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            if (!cancelledTimer)
                printCustomerCopy();
        }
    };
    public boolean cancelledTimer = false;


    private void startCustomerPrintTimer() {
        cancelledTimer = false;
        timerPrint.start();
    }

    private void cancelCustomerCopy() {
        cancelledTimer = true;
        timerPrint.cancel();
        timerPrint.onFinish();
    }

    private void printCustomerCopy() {
        cancelCustomerCopy();
        Logger.v("Customer - 3");
        AppConfig.customerCopyPrinted = true;
        if (!Utils.checkPrinterPaper(context, dialoge)) {
            if (isSAFPrint)
                printSAFReceipt(false);
            else
                printReceipt(false);
        }
    }

    Utils.DialogeClick listner = new Utils.DialogeClick() {
        @Override
        public void onClick() {
            Utils.dismissDialoge();
            moveNext(6);
        }
    };

    public Utils.DialogeClick dialoge = new Utils.DialogeClick() {
        @Override
        public void onClick() {
            Utils.dismissDialoge();
            AppConfig.customerCopyPrinted = false;
            printFlow();
        }
    };


    public void printOkay() {
        showAlert.setValue(14);
    }

    public void onDestroy() {
        spi.release();
        AppConfig.EMV.consumeType = -1;
        Logger.ve("Closed All Reader");
        close_reader(AppInit.CONTACT_CHIP);
        emv_anti_shake_finish(1);
        close_reader(AppInit.CONTACTLESS_RF);
    }

    public void setApprovedMsg(int i) {
        if (isScreenAvailable == 2) {
            statusMsg = declinedFlow1();
        } else if (i == 100)
            statusMsg = successFlow();
        else
            statusMsg = declinedFlow1();
    }

    public void setApprovedMsg(boolean i) {
        Logger.v("setApprovedMsg_transviewmodel");
        statusApproved = true;
        showApprovedBeepOffline = true;
//        if (i)
        statusMsg = context.getString(R.string.approved) + "-" + Utils.formatLanguageNumber(activity, "087");
//        else
//            statusMsg = context.getString(R.string.approved) + "-" + Utils.formatLanguageNumber(activity, "089");
    }

    public void cardDetailsShown() {
        cardDetailShown = true;
    }
}
