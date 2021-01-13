package com.tarang.dpq2.base;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.tarang.dpq2.model.ReconcileSetupModel;
import com.tarang.dpq2.view.broadcastreciever.AlarmReceiverForReconciliation;

import java.util.Calendar;

public class AutoReconsilation {

    public static AlarmManager alarmMgr;
    public static PendingIntent alarmIntent;

    Context context;

    public AutoReconsilation(Context context) {
        this.context = context;
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiverForReconciliation.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

    }

    public void start() {

        ReconcileSetupModel reconcileSetupModel = AppManager.getInstance().getReconcileSetupModel();
        Logger.v("reconciliationtime_setup----------" + reconcileSetupModel.getReconcileTime());

        if (reconcileSetupModel.getReconcileTime() != null && reconcileSetupModel.getReconcileTime().trim().length() != 0&& reconcileSetupModel.getReconcileToggle()) {
            String[] time = reconcileSetupModel.getReconcileTime().split(":");
            if(time.length == 2) {
                if(time[0].trim().length() != 0 && time[1].trim().length() != 0) {
                    int hh = Integer.parseInt(time[0]);
                    int mm = Integer.parseInt(time[1]);
                    if (hh!=0 && mm!=0 && AppManager.getInstance().getReconsilationDate()) {
                        doReconcilation(hh, mm);
                    }
                }
            }
        }
    }

    private static void doReconcilation(int hh, int mm) {
        alarmMgr.cancel(alarmIntent);
// Set the alarm to start at 21:32 PM
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hh);
        calendar.set(Calendar.MINUTE, mm);
        // calendar.set(Calendar.HOUR_OF_DAY, 21);
        //calendar.set(Calendar.MINUTE, 32);

// setRepeating() lets you specify a precise custom interval--in this case,
// 1 day
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);
    }

}
