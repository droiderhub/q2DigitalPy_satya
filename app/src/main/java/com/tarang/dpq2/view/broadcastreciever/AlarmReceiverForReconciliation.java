package com.tarang.dpq2.view.broadcastreciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tarang.dpq2.R;
import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.jpos_class.ConstantApp;
import com.tarang.dpq2.model.MenuModel;
import com.tarang.dpq2.view.activities.MerchantMenuActivity;

public class AlarmReceiverForReconciliation extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        AppManager.getInstance().saveReconsilationDate();
        AppManager.getInstance().setMenuItem(new MenuModel.MenuItem(context.getString(R.string.reconcilation), ConstantApp.RECONCILIATION));
        intent = new Intent(context, MerchantMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        context.startActivity(intent);
    }
}
