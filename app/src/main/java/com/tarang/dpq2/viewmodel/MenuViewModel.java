package com.tarang.dpq2.viewmodel;

import android.app.Application;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
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
import com.tarang.dpq2.base.terminal_sdk.AppConfig;
import com.tarang.dpq2.base.terminal_sdk.device.SDKDevice;
import com.tarang.dpq2.base.terminal_sdk.utils.PrinterReceipt;
import com.tarang.dpq2.base.utilities.CountDownResponseTimer;
import com.tarang.dpq2.base.utilities.Utils;
import com.tarang.dpq2.isopacket.CreatePacket;
import com.tarang.dpq2.isopacket.IsoRequest;
import com.tarang.dpq2.model.ReconcileSetupModel;
import com.tarang.dpq2.worker.PacketDBInfoWorker;
import com.tarang.dpq2.worker.PrinterWorker;
import com.tarang.dpq2.worker.SAFWorker;
import com.tarang.dpq2.worker.SocketConnectionWorker;

import org.jpos.iso.ISOMsg;
import org.jpos.util.Log;

import static com.tarang.dpq2.base.terminal_sdk.AppConfig.EMV.reqObj;

public class MenuViewModel extends BaseViewModel {

    private MenuRepository repo;
    private CreatePacket packet;
    public String de72Header;
    MutableLiveData<Boolean> makeConnection;
    MutableLiveData<Integer> showAlert;
    boolean isPrinting = false;
    MutableLiveData<Boolean> loadKeys;
    MutableLiveData<Integer> printRecipt;
    static MutableLiveData<Boolean> startTimer;
    private Context context;
    public boolean doSAFOnly = false;
    public int isDownload = 0;
    private int duplicate = -100;
    private PrinterDevice devicePrinter;
//    private Printer mPrinter;

    public MenuViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(Context context, LifecycleOwner owner) {
        PrinterWorker.RECON_PRINTED = false;
        PrinterWorker.RECON_PRINTED_DUB = false;
        this.context = context;
        this.repo = new MenuRepository(context, owner);
        this.packet = new CreatePacket(context);
        devicePrinter = SDKDevice.getInstance(context).getPrinter();
    }

    public void makeSocketConnection() {
        AppConfig.EMV.enableDatabaseUpdate = false;
        Utils.hideProgressDialoge();
        if(!reqObj.getMti0().equalsIgnoreCase(FILEACTION) && !reqObj.getMti0().equalsIgnoreCase(FILEACTION_REPEAT))
            showAlert.setValue(3);
        if (increamentRetryCount(false)) {
            repo.makeSocketConnection(new Observer<WorkInfo>() {
                @Override
                public void onChanged(@Nullable WorkInfo workInfo) {
                    if (workInfo != null) {
                        if (workInfo.getState() == WorkInfo.State.SUCCEEDED || workInfo.getState() == WorkInfo.State.FAILED) {
                            if (workInfo.getOutputData().getBoolean("SAF_TOAST", false))
                                ((BaseActivity) context).showToast(context.getString(R.string.invalid_service_ip));
                        }

                        WorkInfo.State state = workInfo.getState();
//                    showAlert.setValue("");
                        Logger.v("makeSocketConnection " + state);
                        if (state == WorkInfo.State.SUCCEEDED) {
                            ISOMsg response = SocketConnectionWorker.responseObjArray;
                            if (response != null) {
                                String mti = AppManager.getInstance().getResponseMTI();
                                if (mti.equalsIgnoreCase(ConstantAppValue.FILEACTION_RESPONSE)) {
                                    byte[] de72 = response.getBytes(72);
                                    String de39 = response.getString(39);
                                    if (de39.trim().equalsIgnoreCase("307")) {
                                        showAlert.setValue(404);
                                        return;
                                    }
                                    String hexD72 = ByteConversionUtils.byteArrayToHexString(de72, de72.length, false);
                                    Logger.v("makeSocketConnection " + IsoRequest.getStringFromHex(hexD72));
                                    String de_72 = IsoRequest.getStringFromHex(hexD72).substring(0, 7);
                                    Logger.v("PACKET --" + de_72);
                                    de72Header = de_72;
                                    AppInit.lastPacket = de_72;
                                    showAlert.setValue(1);
                                    int pack = Integer.parseInt(de_72.substring(3, 5));
                                    int total = Integer.parseInt(de_72.substring(5, 7));
                                    if (pack == total) {
                                        AppInit.lastPacket = AppInit.full_download;
                                        loadKeys.setValue(true);
                                    }else
                                        createTMSPacket(de_72);
                                } else if (mti.equalsIgnoreCase(ConstantAppValue.TERMINAL_REGISTRATION_RESPONSE)) {
                                    AppManager manager = AppManager.getInstance();
                                    manager.setAccuringInsituteIdCode32(response.getString(32));
                                    manager.setCardAcceptorID41(response.getString(41));
                                    manager.setCardAcceptorCode42(response.getString(42));
                                    Toast.makeText(context, R.string.reg_suc, Toast.LENGTH_SHORT).show();
                                    createTMSPacket(AppInit.lastPacket);
                                } else if (mti.equalsIgnoreCase(ConstantAppValue.RECONSILATION_RESPONSE)) {
                                    String de39 = AppManager.getInstance().getDe39();
                                    if (de39 != null && de39.trim().length() != 0) {
                                        if (de39.equalsIgnoreCase("501"))
                                            Toast.makeText(context, context.getString(R.string.out_of_balance), Toast.LENGTH_SHORT).show();
                                        else
                                            Toast.makeText(context, context.getString(R.string.totals_matched), Toast.LENGTH_SHORT).show();
                                    }
                                    AppManager.getInstance().resetDuplicateTransactionModelEntity();
                                    printRecipt.setValue(0);
                                }

                            } else
                                Logger.v("makeSocketConnection else");
                        } else if (state == WorkInfo.State.FAILED) {
                            if (SocketConnectionWorker.cancelledRequest || reqObj.getMti0().equalsIgnoreCase(ConstantAppValue.NETWORK_MNGMT)) {
                                SocketConnectionWorker.cancelledRequest = false;
                                showAlert.setValue(404);
                            }
                            String mti = AppManager.getInstance().getResponseMTI();
                            if (mti != null) {
                                if (mti.equalsIgnoreCase(ConstantAppValue.TERMINAL_REGISTRATION_RESPONSE)) {
                                    showAlert.setValue(22);
                                } else {
                                    showAlert.setValue(404);
                                }
                            }
                        }
                        Logger.v("state else");
//                    status.onStatusChange("inser",state);
                    }
                }
            });
        }else {
            showAlert.setValue(404);
        }
    }

    public void printSnapShot(boolean bool) {
        printReconsilation((bool) ? 1 : 2);
    }

    public void printReconsilation(final Integer status) {
        if (isPrinting)
            return;
        if (Utils.checkPrinterPaper(context, dialoge))
            return;
        showAlert.setValue(6);
        isPrinting = true;
        repo.printReceipt(status, new Observer<WorkInfo>() {
            @Override
            public void onChanged(final WorkInfo workInfo) {
                if (workInfo != null) {
                    Logger.v("printReceipt -State-" + workInfo.getState());
                    if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                        doPrintRecipt(devicePrinter, context, new PrintComplete() {
                            @Override
                            public void onFinish() {
                                Logger.v("OnFInish -" + status);
                                isPrinting = false;
                                if (checkPaper(workInfo, status)) {
                                    if (status == 1) {
                                        int id = AppManager.getInstance().getSnapshotID();
                                        Logger.v("UID--" + id);
                                        int uid = workInfo.getOutputData().getInt(PrinterWorker.UID_LAST_TRANSACTION, id);
                                        Logger.v("UId--" + uid);
                                        AppManager.getInstance().setSnapshotID(uid);
                                        showAlert.setValue(9);
                                    } else if (status == 2)
                                        showAlert.setValue(8);
                                    else if (status == 0) {
                                        Logger.v("status == 0");
                                        ((BaseActivity) context).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                showAlert.setValue(77);
                                            }
                                        });
                                    } else if (status == 3) {
                                        reconContinueFlow();
                                    } else if (status == 4) {
                                        showAlert.setValue(10);
                                    }
                                } else
                                    Logger.v("Check Paper else");
                            }
                        });
                    } else if (workInfo.getState() == WorkInfo.State.FAILED) {
                        isPrinting = false;
                        showAlert.setValue(5);
                    }
                }
            }
        });
    }

    public void reconContinueFlow() {
        ReconcileSetupModel reconcileSetupModel = AppManager.getInstance().getReconcileSetupModel();
        reconcileSetupModel.setReconcileStatus(true);
        AppManager.getInstance().setReconcileSetupModel(reconcileSetupModel);
        if (isDownload == 0) {
            pleaseWaitDialoge();
            createTMSPacket(AppInit.lastPacket);
        }else if (isDownload == 2) {
            createRegistration();
        }  else
            showAlert.setValue(7);
    }

    private boolean checkPaper(WorkInfo workInfo, final int print) {
        boolean printStatus = workInfo.getOutputData().getBoolean(PrinterWorker.STATUS, true);
        Logger.v("printStatus --" + printStatus);
        if (printStatus)
            return true;
        else if (!outOfPaper(print)) {
//            rePrint(print);
        }
        return false;
    }

    private void rePrint(int print) {
        if (print == duplicate)
            printDuplicate();
        else
            printReconsilation(print);
    }

    private boolean outOfPaper(final int print) {
        return Utils.checkPrinterPaper(context, new Utils.DialogeClick() {
            @Override
            public void onClick() {
                if (!outOfPaper(print))
                    rePrint(print);
            }
        });
    }


    private void moveNext() {
        ((BaseActivity) context).finish();
    }

    public void createTMSPacket(final String de_72) {
        repo.fetchDataFromDatabase(ConstantApp.FILEACTION, new Observer<WorkInfo>() {
            @Override
            public void onChanged(@Nullable WorkInfo workInfo) {
                if (workInfo != null) {
                    WorkInfo.State state = workInfo.getState();
                    Logger.v("fetchDataFromDatabase -Starte --" + state);
                    if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                        if (workInfo.getOutputData().getBoolean(PacketDBInfoWorker.TRANSACTION_PENDING, false)) {
                            showAlert.setValue(122);
                        } else {
//                        if (checkCardCondition(transactionType, workInfo.getOutputData())) {
                            packet.addFileActionDetails(de_72, workInfo.getOutputData());
//                        packet.createISORequest();
                            makeConnection.setValue(true);
                        }
//                        } else
//                            showAlert.setValue(true);
                    }
                }
            }
        });
    }

    public void createReconsilation(boolean repeat) {
        if (!doSAFOnly && Utils.checkPrinterPaper(context, true))
            return;
        if (doSAFOnly)
            showAlert.setValue(13);
        else
            showAlert.setValue(3);
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

                    if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                        int current_count = workInfo.getOutputData().getInt(SAFWorker.SAF_COUNT, -1);
                        Logger.v("current_count--" + current_count);
                        if(current_count == 3) {
                            Utils.dismissDialoge();
                            showAlert.setValue(15);
                        }else if (current_count != -1)
                            createReconsilation(false);
                        else {
                            if (doSAFOnly)
                                showAlert.setValue(14);
                            else
                                reconsilationFlow();
                        }
                    } else if (workInfo.getState() == WorkInfo.State.FAILED) {
                        Logger.v("SAF Only --"+doSAFOnly);
                        int current_count = workInfo.getOutputData().getInt(SAFWorker.SAF_COUNT, -1);
                        Logger.v("current_count--" + current_count);
                        if (doSAFOnly) {
                            if (current_count == -2)
                                showAlert.setValue(5);
                            else
                                showAlert.setValue(15);
                        } else if (current_count == -2)
                            reconsilationFlow();
                        else if(current_count == 3) {
                            Utils.dismissDialoge();
                            showAlert.setValue(15);
                        }
                    }
                }
            }
        });
    }

    public void createReconsilation1(boolean repeat) {
        Logger.v("doSAFOnly --"+doSAFOnly);
//        isDownload = true;
        if (!doSAFOnly && Utils.checkPrinterPaper(context, true))
            return;
        if (doSAFOnly)
            showAlert.setValue(13);
        else
            showAlert.setValue(3);
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

                    if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                        int current_count = workInfo.getOutputData().getInt(SAFWorker.SAF_COUNT, -1);
                        Logger.v("current_count--" + current_count);
                        if (current_count != -1)
                            createReconsilation(false);
                        else {
                            if (doSAFOnly)
                                showAlert.setValue(14);
                            else
                                reconsilationFlow();
                        }
                    } else if (workInfo.getState() == WorkInfo.State.FAILED) {
                        if (doSAFOnly) {
                            if (workInfo.getOutputData().getInt(SAFWorker.SAF_COUNT, 0) == -2)
                                showAlert.setValue(5);
                            else
                                showAlert.setValue(15);
                        } else
                            reconsilationFlow();
                    }
                }
            }
        });
    }

    public void doAllSafRequest(boolean repeat) {

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
                    if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                        int current_count = workInfo.getOutputData().getInt(SAFWorker.SAF_COUNT, -1);
                        Logger.v("current_count--" + current_count);
                        if (current_count != -1)
                            doAllSafRequest(false);
                        else {

                        }
                    } else if (workInfo.getState() == WorkInfo.State.FAILED) {

                    }
                }
            }
        });
    }

    public void reconsilationFlow() {
        Logger.v("isDownload --" + isDownload);
        repo.fetchDataFromDatabase(ConstantApp.RECONCILIATION, new Observer<WorkInfo>() {
            @Override
            public void onChanged(@Nullable WorkInfo workInfo) {
                if (workInfo != null) {
                    WorkInfo.State state = workInfo.getState();
                    Logger.v("fetchDataFromDatabase -Starte --" + state);
                    if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
//                        if (checkCardCondition(transactionType, workInfo.getOutputData())) {
                        if (workInfo.getOutputData().getString(PacketDBInfoWorker.DE_124) != null) {
                            packet.addReconciliationData(workInfo.getOutputData());
//                            packet.createISORequest();
                            makeConnection.setValue(true);
                        } else {
                            if (isDownload == 1)
                                showAlert.setValue(5);
                            else if (isDownload == 2) {
                                createRegistration();
                            } else {
                                pleaseWaitDialoge();
                                createTMSPacket(AppInit.lastPacket);
                            }
                        }

//                        } else
//                            showAlert.setValue(true);
                    }
                }
            }
        });
    }

    public void createRegistration() {
        showAlert.setValue(2);
        repo.fetchDataFromDatabase(ConstantApp.REGISTRATION, new Observer<WorkInfo>() {
            @Override
            public void onChanged(@Nullable WorkInfo workInfo) {
                if (workInfo != null) {
                    WorkInfo.State state = workInfo.getState();
                    Logger.v("fetchDataFromDatabase -Starte --" + state);
                    if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                        if (workInfo.getOutputData().getBoolean(PacketDBInfoWorker.TRANSACTION_PENDING, false)) {
                            showAlert.setValue(12);
                        } else {
                            packet.addNetworkRegistration(workInfo.getOutputData());
//                            packet.createISORequest();
                            makeConnection.setValue(true);
                        }
                    }
                }
            }
        });
    }

    public void formatFileSys() {
        showAlert.setValue(2);
        repo.fetchDataFromDatabase(ConstantApp.FORMAT_FILESYS, new Observer<WorkInfo>() {
            @Override
            public void onChanged(@Nullable WorkInfo workInfo) {
                if (workInfo != null) {
                    WorkInfo.State state = workInfo.getState();
                    Logger.v("fetchDataFromDatabase -Starte --" + state);
                    if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                        if (workInfo.getOutputData().getBoolean(PacketDBInfoWorker.TRANSACTION_PENDING, false)) {
                            showAlert.setValue(122);
                        } else {
                            formatFileSysy();
                        }
                    }
                }
            }
        });
    }

    private void formatFileSysy() {
        if (AppManager.getInstance().getInitializationStatus(context)) {
            Utils.alertYesDialogShow(context, context.getString(R.string.are_you_sure_you_want_to_format), new View.OnClickListener() {
                @Override
                public void onClick(View dialog) {
                    AppManager.getInstance().initializationStatus(0);
                    Utils.dismissDialoge();
                    showAlert.setValue(123);
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View dialog) {
//                dialog.dismiss();
                    Utils.dismissDialoge();
                    MapperFlow.getInstance().moveToLandingPage(context,true,66);
                }
            });
        } else {
            showAlert.setValue(123);
        }
    }
    public void loadKeysIntoTerminal() {
        showAlert.setValue(3);
        repo.loadKeys(new Observer<WorkInfo>() {
            @Override
            public void onChanged(@Nullable WorkInfo workInfo) {
                if (workInfo != null) {
                    WorkInfo.State state = workInfo.getState();
                    Logger.v("Starte --" + state);
                    if (state == WorkInfo.State.SUCCEEDED) {
                        AppManager.getInstance().initializationStatus(1);
                        showAlert.setValue(4);
//                        PrinterReceipt.printTmsStatus(mPrinter,context,true);
                    }
                }
            }
        });
    }

    public void loadOnlyKeysIntoTerminal() {
        showAlert.setValue(3);
        repo.loadOnlyKeys(new Observer<WorkInfo>() {
            @Override
            public void onChanged(@Nullable WorkInfo workInfo) {
                if (workInfo != null) {
                    WorkInfo.State state = workInfo.getState();
                    Logger.v("Starte --" + state);
                    if (state == WorkInfo.State.SUCCEEDED) {
                        AppManager.getInstance().initializationStatus(1);
                        showAlert.setValue(4);
                    }
                }
            }
        });
    }

    public LiveData<Boolean> getConnectionStatus() {
        if (makeConnection == null) {
            makeConnection = new MutableLiveData<>();
        }
        return makeConnection;
    }

    public LiveData<Integer> getShowAlert() {
        if (showAlert == null) {
            showAlert = new MutableLiveData<>();
        }
        return showAlert;
    }

    public LiveData<Boolean> loadKeys() {
        if (loadKeys == null) {
            loadKeys = new MutableLiveData<>();
        }
        return loadKeys;
    }

    public LiveData<Boolean> startTimer() {
        if (startTimer == null) {
            startTimer = new MutableLiveData<>();
        }
        return startTimer;
    }

    private static Handler eventHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Logger.v("Handle Message");
            switch (msg.what) {
                case AppConfig.EMV.SOCKET_FINISH:
                    break;
                case AppConfig.EMV.SOCKET_CANCEL:
                    break;
                case AppConfig.EMV.START_TIMER:
                    startTimer.setValue(true);
                    break;
                case AppConfig.EMV.SAF_TIMER_:
                    Logger.v("SAF_TIMER_ time");
//                    count_repeat = (int) msg.obj;
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

    public void startCountDownTimer() {
        Logger.v("Start Time 1");
        CountDownResponseTimer.startTimer(new CountDownResponseTimer.Timer() {
            @Override
            public void onFinished() {
                checkReversal(reversalStatus,0);
            }
        });
    }

    public void startSAFCountDownTimer() {
        Logger.v("SAF Timer --startSAFCountDownTimer");
        Logger.v("SAF repeat --"+ SAFWorker.isSAFRepeat);
        CountDownResponseTimer.startTimerSAF(new CountDownResponseTimer.Timer() {
            @Override
            public void onFinished() {
                if (SAFWorker.isSAFRepeat)
                    createReconsilation(true);
            }
        });
    }

    Reversal reversalStatus = new Reversal() {
        @Override
        public void doReversal() {
            Logger.v("DO Reversal");
        }

        @Override
        public void doRepeat() {
            Logger.v("DO Repeat");
            Logger.v("DO Repeat --" + reqObj.getMti0());
            makeConnection.setValue(true);
        }
    };

    public Observer observable = new Observer<Integer>() {
        @Override
        public void onChanged(Integer status) {
            Logger.v("Integer --" + status);
            if (status == 0) {

                Utils.dismissDialoge();

            } else if (status == 1) {
                if (de72Header != null && de72Header.trim().length() != 0) {
                    Utils.tmsAlertDialogShow(context, de72Header);
                }
            } else if (status == 2) {
                Utils.alertDialogShow(context, context.getString(R.string.registering));
            } else if (status == 22) {
                Utils.alertDialogShow(context, context.getString(R.string.registration_failed), true);
//                PrinterReceipt.printRegistration(mPrinter,context);
                PrinterReceipt.printRegistration(devicePrinter,context, new PrintComplete() {
                    @Override
                    public void onFinish() {

                                                  }
                });
            } else if (status == 3) {
                Utils.alertDialogShow(context, context.getString(R.string.please_wait));
            } else if (status == 4) {
                Utils.alertDialogShow(context, context.getString(R.string.initialization_complted), true);
                SDKDevice.getInstance(context).incrementKSN();
            } else if (status == 5) {
                Utils.alertDialogShow(context, context.getString(R.string.empty_batch), true);
            } else if (status == 6) {
                Utils.alertDialogShow(context, context.getString(R.string.printing));
            } else if (status == 7) {
                Utils.alertDialogShow(context, context.getString(R.string.reconsilation_complete), new View.OnClickListener() {
                    @Override
                    public void onClick(View dialog) {
                        Utils.dismissDialoge();
                        AppManager.getInstance().resetDuplicateTransactionModelEntity();
                        MapperFlow.getInstance().moveToLandingPage(context, true, 5);
                    }
                });
            } else if (status == 77) {
                startCustomerPrintTimer();
                Logger.v("status == 77");
                Logger.v("continue_to_print_duplicate_recon");
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Utils.alertDialogShow(context, context.getString(R.string.continue_to_print_duplicate_copy), new View.OnClickListener() {
                            @Override
                            public void onClick(View dialog) {
                                Utils.dismissDialoge();
                                cancelledTimer = true;
                                cancelCustomerCopy();
                                printReconsilation(3);
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View dialog) {
                                Logger.v("Customer - 1");
                                AppConfig.customerCopyPrinted = true;
                                cancelCustomerCopy();
                                Utils.dismissDialoge();
                                reconContinueFlow();
                            }
                        });
                    }
                },800);

            } else if (status == 8) {
                Utils.alertDialogShow(context, context.getString(R.string.snapshot_complete), false);
            } else if (status == 9) {
                Utils.alertDialogShow(context, context.getString(R.string.running_total_complete), false);
            } else if (status == 10) {
                Utils.alertDialogShow(context, context.getString(R.string.duplicate_print_complete), false);
            } else if (status == 11) {
                Utils.alertDialogShow(context, context.getString(R.string.processing));
            } else if (status == 12) {
                Utils.alertDialogShow(context, context.getString(R.string.please_empty_batch_registartion), false);
            } else if (status == 122) {
                Utils.alertDialogShow(context, context.getString(R.string.please_empty_batch_), false);
            } else if (status == 123) {
                Utils.alertDialogShow(context, context.getString(R.string.format_completed), true);
            } else if (status == 13) {
                Utils.alertDialogShow(context, context.getString(R.string.processing_saf));
            } else if (status == 14) {
                Utils.alertDialogShow(context, context.getString(R.string.saf_complete), new View.OnClickListener() {
                    @Override
                    public void onClick(View dialog) {
                        Utils.dismissDialoge();
                        MapperFlow.getInstance().moveToLandingPage(context, true, 6);
                    }
                });
            } else if (status == 15) {
                Utils.alertDialogShow(context, context.getString(R.string.saf_failed), new View.OnClickListener() {
                    @Override
                    public void onClick(View dialog) {
                        Utils.dismissDialoge();
                        MapperFlow.getInstance().moveToLandingPage(context, true, 7);
                    }
                });
            } else if (status == 404) {
                Utils.alertDialogShow(context, context.getString(R.string.unsuccessful), new View.OnClickListener() {
                    @Override
                    public void onClick(View dialog) {
                        Utils.dismissDialoge();
                        MapperFlow.getInstance().moveToLandingPage(context, true, 8);
                    }
                });
//                if(reqObj.getMti0().equalsIgnoreCase(ConstantValue.FILEACTION) || reqObj.getMti0().equalsIgnoreCase(ConstantValue.FILEACTION_REPEAT))
//                    PrinterReceipt.printTmsStatus(mPrinter,context,false);
            }
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

    CountDownTimer timerPrint = new CountDownTimer(20000, 2000) {

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            if (!cancelledTimer)
                printReconsilation(3);
        }
    };


    public Observer printObservable = new Observer<Integer>() {
        @Override
        public void onChanged(Integer status) {
            printReconsilation(status);

        }
    };

    public LiveData<Integer> getPrintStatus() {
        if (printRecipt == null) {
            printRecipt = new MutableLiveData<>();
        }
        return printRecipt;
    }

    public void printDuplicate() {
        if (AppManager.getInstance().getDuplicateTransactionModelEntity() != null) {
            if (!Utils.checkPrinterPaper(context, dialoge)) {
                showAlert.setValue(6);
                repo.printReceipt(new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(final WorkInfo workInfo) {
                        if (workInfo != null) {
                            Logger.v("workInfo.getState() -" + workInfo.getState());
                            if (workInfo.getState() == WorkInfo.State.SUCCEEDED)
                                doPrintTransactionReceipt(devicePrinter, context, new PrintComplete() {
                                    @Override
                                    public void onFinish() {
                                        Logger.v("OnFinsh Receipt");
                                        if (checkPaper(workInfo, duplicate)) {
                                            showAlert.postValue(10);
                                        }
                                    }
                                });
                        }
                    }
                });
            }
        } else if (AppManager.getInstance().getReconciliationCardSchemeModelList() != null) {
            printReconsilation(4);
        } else
            showAlert.setValue(5);
    }

    public Utils.DialogeClick dialoge = new Utils.DialogeClick() {
        @Override
        public void onClick() {
            Utils.dismissDialoge();
            MapperFlow.getInstance().moveToLandingPage(context, true, 8);
        }
    };


    public void pleaseWaitDialoge() {
        showAlert.setValue(11);
    }
}
