package com.tarang.dpq2.base.utilities;

import android.os.CountDownTimer;

import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.terminal_sdk.event.SimpleTransferListener;
import com.tarang.dpq2.model.DeviceSpecificModel;
import com.tarang.dpq2.worker.SocketConnectionWorker;

public class CountDownResponseTimer {
    public static CountDownTimer timer = null;
    public static CountDownTimer landingTimer = null;
    private static boolean cancelled = false;
    private static boolean cancelled1 = false;

    public static void startTimer(final Timer timerListener) {
        cancelTimer(0);
        cancelled = false;
        timer = new CountDownTimer(SocketConnectionWorker.IDEAL_TIME, 1000) {

            public void onTick(long millisUntilFinished) {
                //   mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
                Logger.v("Reversal --" + SimpleTransferListener.disableReversal);

            }

            public void onFinish() {
                if (!cancelled) {
                    Logger.v("Reversal --fin-" + SimpleTransferListener.disableReversal);
                    if (!SimpleTransferListener.disableReversal) {
                        SimpleTransferListener.doReversal = true;
                        timerListener.onFinished();
                    }
                } else {
                    Logger.v("OnFinish Cancel");
                }

                //  mTextField.setText("done!");
            }

        };
        timer.start();
    }

    public static void startTimerSAF(final Timer timerListener) {
        cancelTimer(11);
        cancelled = false;
        timer = new CountDownTimer(SocketConnectionWorker.IDEAL_TIME, 1000) {

            public void onTick(long millisUntilFinished) {
                //   mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
                Logger.v("Reversal -saf-" + SimpleTransferListener.disableReversal);

            }

            public void onFinish() {
                Logger.v("onFinish -saf-fin");
                if (!cancelled) {
                    timerListener.onFinished();
                } else
                    Logger.v("onFinish Cancelled");
                //  mTextField.setText("done!");
            }

        };
        timer.start();
    }

    public static void startTimerSAFDept(final Timer timerListener) {
        cancelTimer(1);
        cancelled1 = false;
        int time = SocketConnectionWorker.IDEAL_TIME;
        DeviceSpecificModel deviceSpecificModel1 = AppManager.getInstance().getDeviceSpecificModel();
        if (deviceSpecificModel1 != null) {
            String timeLocal = deviceSpecificModel1.getIdleTime();
            if (timeLocal != null && timeLocal.trim().length() != 0)
                time = Integer.parseInt(timeLocal) * 100;
        }
        Logger.v("Timer --"+time);
        landingTimer = new CountDownTimer(time, 1000) {

            public void onTick(long millisUntilFinished) {
                //   mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
                Logger.v("Reversal -saf-" + SimpleTransferListener.disableReversal);

            }

            public void onFinish() {
                Logger.v("onFinish -saf-fin");
                if (!cancelled1) {
                    timerListener.onFinished();
                } else
                    Logger.v("onFinish Cancelled");
                //  mTextField.setText("done!");
            }

        };
        landingTimer.start();
    }

    public static void cancelTimer(int i) {
        Logger.v("Int cancel --"+i);
        cancelled = true;
        if (timer != null) {
            timer.cancel();
            timer.onFinish();
        }
    }

    public static void cancelTimerForce(int i) {
        Logger.v("Int cancel -cancelTimerForce-"+i);
        if (timer != null) {
            timer.cancel();
            timer.onFinish();
        }
    }

    public static void cancelTimerLanding(int i) {
        Logger.v("Int cancel --"+i);
        cancelled1 = true;
        if (landingTimer != null) {
            landingTimer.cancel();
            landingTimer.onFinish();
        }
    }

    public interface Timer {
        public void onFinished();
    }

}
