package com.tarang.dpq2.viewmodel;

import android.app.Application;
import android.content.Context;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.work.WorkInfo;

import com.tarang.dpq2.R;
import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.MapperFlow;
import com.tarang.dpq2.base.baseactivities.BaseActivity;
import com.tarang.dpq2.base.room_database.db.AppDatabase;
import com.tarang.dpq2.base.room_database.db.entity.TransactionModelEntity;
import com.tarang.dpq2.base.terminal_sdk.AppConfig;
import com.tarang.dpq2.base.terminal_sdk.SdkSupport;
import com.tarang.dpq2.base.terminal_sdk.device.SDKDevice;
import com.tarang.dpq2.base.terminal_sdk.utils.SoundPoolImpl;
import com.tarang.dpq2.base.utilities.CountDownResponseTimer;
import com.tarang.dpq2.base.utilities.Utils;
import com.tarang.dpq2.model.DeviceSpecificModel;
import com.tarang.dpq2.model.MenuModel;
import com.tarang.dpq2.view.activities.LandingPageActivity;
import com.tarang.dpq2.worker.LoadKeyWorker;
import com.tarang.dpq2.worker.PrinterWorker;
import com.tarang.dpq2.worker.SAFWorker;

import java.util.List;

import static com.cloudpos.jniinterface.EMVJNIInterface.emv_set_kernel_attr;

public class LandingPageViewModel extends BaseViewModel {

    private LandingPageRepository repo;
    public String de72Header;
    MutableLiveData<Boolean> makeConnection;
    static MutableLiveData<Boolean> startTimer;
    static MutableLiveData<Boolean> changeView;
    private Context context;
    MenuModel.MenuItem currentMenuItem = null;
    private boolean showTemprovary = false;
    private SoundPoolImpl spi;
    private CountDownTimer timer = null;
    private SdkSupport support;
    private CountDownTimer timer_new = null;
    boolean isCancelled = false;
    boolean isCancelledIdeal = false;
    private CountDownTimer idealtimer;
    private String amount;

    public LandingPageViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(Context context, LifecycleOwner owner) {
        this.context = context;
        spi = SoundPoolImpl.getInstance();
        support = new SdkSupport(context);
        SDKDevice.getInstance(context).incrementKSN();
        spi.initLoad(context);
        this.repo = new LandingPageRepository(context, owner);
    }

    public LiveData<Boolean> startTimer() {
        if (startTimer == null) {
            startTimer = new MutableLiveData<>();
        }
        return startTimer;
    }

    public LiveData<Boolean> changeIdealScreen() {
        if (changeView == null) {
            changeView = new MutableLiveData<>();
        }
        return changeView;
    }

    public void initSound() {
        spi.initListner(context, new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                Logger.v("Sound listner 000_landingviewmodel");
                loopPlay();
            }
        });
    }

    public void loopPlay() {
        spi.loopPlay();
    }

    public void startTimerRemoveCard() {
        Logger.v("startTimerRemoveCard()");
        initSound();
        timer = new CountDownTimer(3000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                Logger.v("Tick Tick");
            }

            @Override
            public void onFinish() {
                Logger.v("OnTick");
                if (AppConfig.EMV.consumeType == 2) {
//                            AppConfig.isCardRemoved = rfCardModule.isRfcardExist();
                } else if (AppConfig.EMV.consumeType == 0) {
                    AppConfig.isCardRemoved = false;
                } else {
                }
                if (!AppConfig.isCardRemoved) {
                    cancel();
                    spi.removeBeep();
                    ((LandingPageActivity) context).restart();
                } else {
                    spi.removeBeep();
                    startTimerRemoveCard();
                }
                Logger.v("OnTick END");
            }
        };
        timer.start();
    }

    public void cancelSoundTimer() {
        if (timer != null) {
            AppConfig.isCardRemoved = false;
            timer.onFinish();
            timer.cancel();
        }
    }


    public LiveData<Boolean> getSAFConnection() {
        if (makeConnection == null) {
            makeConnection = new MutableLiveData<>();
        }
        return makeConnection;
    }

    public void checkSAF(final boolean repeat) {
       // Utils.alertDialogOneShow(context, context.getString(R.string.send_saf));
        SDKDevice.getInstance(context).incrementKSN();
        startCancelTimer();
        repo.checkSAF(repeat, new Observer<WorkInfo>() {
            @Override
            public void onChanged(@Nullable WorkInfo workInfo) {
                if (workInfo != null) {
                    WorkInfo.State state = workInfo.getState();
                    Logger.v("checkSAF --" + state);
                    cancelTimerDialog();
                    if (workInfo.getState() == WorkInfo.State.SUCCEEDED || workInfo.getState() == WorkInfo.State.FAILED) {
                        if (workInfo.getOutputData().getBoolean("SAF_TOAST", false))
                            ((BaseActivity) context).showToast(context.getString(R.string.invalid_service_ip));

                        int current_count = workInfo.getOutputData().getInt(SAFWorker.SAF_COUNT, -1);
                        Logger.v("current_count--" + current_count);
                        if (!showTemprovary) {
                            if (current_count == -22) {
                                Utils.dismissDialoge();
                                startIdealTimer();
                            } else if (current_count == -2) {
                                AppManager.getInstance().setTemprovaryOutService(false);
                                Utils.dismissDialoge();
                                startIdealTimer();
                            } else if (current_count == -3) {
                                outOfServiceDialoe(false);
                            } else if (current_count != -1)
                                checkSAF(false);
                            else {
                                Utils.dismissDialoge();
                                startSAFTimer();
                            }
                        }
                    }
                } else {
                    Utils.dismissDialoge();
                }
            }
        });
    }

    public void resetIdealTimer() {
        cancelIdealTimer();
        startIdealTimer();
    }

    public void startIdealTimer() {
        if (idealtimer != null) {
            cancelIdealTimer();
        }
        isCancelledIdeal = false;
        idealtimer = new CountDownTimer(30000, 1000) {

            @Override
            public void onTick(long l) {
                Logger.v("IdealTime --" + l);
            }

            @Override
            public void onFinish() {
                if (!isCancelledIdeal) {
                    changeView.setValue(true);
                }
            }
        };
        idealtimer.start();
    }

    private void cancelTimerDialog() {
        isCancelled = true;
        if (timer_new != null) {
            timer_new.onFinish();
            timer_new.cancel();
            timer_new = null;
        }
    }

    public void cancelIdealTimer(){
        isCancelledIdeal = true;
        if(idealtimer != null){
            idealtimer.onFinish();
            idealtimer.cancel();
            idealtimer = null;
        }
    }
    public void cancelRequest() {
        repo.cancelWorkmanager();
        cancelIdealTimer();
    }

    private void startCancelTimer() {
        isCancelled = false;
        timer_new = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                if (!isCancelled)
                    Utils.dismissDialoge();
            }
        };
        timer_new.start();
    }


    public void outOfServiceDialoe(final boolean doSaf) {
        showTemprovary = true;
        CountDownResponseTimer.cancelTimerLanding(99);
        AppManager.getInstance().setTemprovaryOutService(true);
        Utils.alertDialogShow(context, context.getString(R.string.temprovery_out_service), new Utils.DialogeClick() {
            @Override
            public void onClick() {
                Utils.dismissDialoge();
                showTemprovary = false;
                SAFWorker.count = 0;
                SAFWorker.failureCount = -1;
                if (doSaf)
                    startSAF();
                else
                    startSAFTimer();
            }
        });
//        Utils.alertDialogShowStatus(context, context.getString(R.string.printing), "Approved - 789560",true);
    }


    private static Handler eventHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Logger.v("message --" + msg.what);
            switch (msg.what) {
                case AppConfig.EMV.SOCKET_FINISH:
                    break;
                case AppConfig.EMV.SOCKET_CANCEL:
                    break;
                case AppConfig.EMV.SAF_TIMER_:
//                    count_repeat = (int) msg.obj;
                    startTimer.setValue(true);
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
        Utils.alertDialogOneShow(context, context.getString(R.string.send_saf));
        CountDownResponseTimer.startTimerSAF(new CountDownResponseTimer.Timer() {
            @Override
            public void onFinished() {
                Logger.v("Timer -1");
                if (SAFWorker.isSAFRepeat) {
                    checkSAF(true);
                } else {
                    Utils.hideDialoge1();
                }
            }
        });
    }

    public void startSAFTimer() {
        CountDownResponseTimer.startTimerSAFDept(new CountDownResponseTimer.Timer() {
            @Override
            public void onFinished() {
                Logger.v("Timer -2");
                startSAF();
            }
        });
    }

    public void startSAF() {
        if (!AppManager.getInstance().isDebugEnabled()) {
            showTemprovary = false;
            SAFWorker.count = 0;
            makeConnection.setValue(true);
        }
    }

    public void loadKeys() {
        Logger.v("loadKeys()_landinpage");
        repo.loadKeys(new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                if (workInfo != null) {
                    Logger.v("STATE --" + workInfo.getState());
                    if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                        support.initReaderLandingPage();
                    }
                }
            }
        });
    }
    public void cancelRequest(boolean onPause) {
        repo.cancelWorkmanager();
        cancelIdealTimer();
        if (onPause) {
            // closeSDK(); //TODO
        }
    }
    public void makeConnection() {
        repo.makeSocketConnection(new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {

            }
        });
    }

    public void loadTMS() {
        repo.loadTms(new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                if (workInfo != null) {
                    Logger.v("STATE --" + workInfo.getState());
                    if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                        loadKeys();
                    }
                }
            }
        });
    }

    public void landingPageSAF() {
        Logger.v("Landing page saf");
        if (AppManager.getInstance().getInitializationStatus(context)) {
            Logger.v("Landing page saf 1");
            if (PrinterWorker.DO_SAF_NOW)
                startSAF();
            else
                startSAFTimer();
        }
    }
    public void checkSAFCount(String amt) {
        amount = amt;
        if (Utils.isInternetAvailable(context, false)) {
            new AsyncTaskExample().execute();
        }
    }
    public class AsyncTaskExample extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            Logger.v("SAF Count check");
            AppDatabase database = AppDatabase.getInstance(context.getApplicationContext());
            List<TransactionModelEntity> safModelEntities = database.getSAFDao().getAll();
            DeviceSpecificModel deviceSpecificModel1 = AppManager.getInstance().getDeviceSpecificModel();
            if (safModelEntities.size() == 0) {
                AppManager.getInstance().setTemprovaryOutService(false);
                return true;
            }
//        else if(AppManager.getInstance().getTemprovaryOutService()){
//            return Result.failure();
//        }

            long count = safModelEntities.size();
            Logger.v("SAF COunt --" + count);
            long amount = 0;

            for (int i = 0; i < safModelEntities.size(); i++) {
                String amt = safModelEntities.get(i).getAmtTransaction4();
                amount = amount + Long.parseLong(amt);
                ;
            }
            Logger.v("SAF amount --" + amount);
            if (deviceSpecificModel1 != null) {
                String maxAmt = deviceSpecificModel1.getMaxSAFCumulativeAmount();
                String maxCount = deviceSpecificModel1.getMaxSAFDepth();

                Logger.v("Max Amount --" + maxAmt);
                Logger.v("Max Amount --" + maxCount);

                if (maxAmt != null && maxAmt.trim().length() != 0) {
                    long maxAmount = Long.parseLong(maxAmt);
                    if (maxAmount != 0 && maxAmount <= (amount / 100)) {
                        return false;
                    }
                }
                if (maxCount != null && maxCount.trim().length() != 0) {
                    long maxCont = Long.parseLong(maxCount);
                    if (maxCont != 0 && maxCont <= count) {
                        return false;
                    }
                }
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            Logger.v("aBoolean -" + aBoolean);
            if (aBoolean) {
                CountDownResponseTimer.cancelTimerLanding(2222);
//                SDKDevice.getInstance(context).incrementKSN();
                MapperFlow.getInstance().movePurchase(context, amount);
            } else {
                outOfServiceDialoe(true);
            }

        }
    }


    public void initReaderLandingPage() {
                Logger.v("initReaderLandingPage()_landingpageviewmodel");
                if (AppManager.getInstance().getInitializationStatus(context)) {
//            if (AppInit.loadKernal) {
                    LoadKeyWorker.initKernal();
                    LoadKeyWorker.setEMVTermInfo();
                    support.intEMV();
                    support.initReaderLandingPage();
//            } else {
//                support.intEMV();
//                loadKeys();
//            }
                }
    }

    public void closeCardReader() {
        Logger.v("closeCardReader()");
        if (AppManager.getInstance().getInitializationStatus(context)) //TODO check condition for registTRTION
            support.closeLandindCardReader();
    }
}
