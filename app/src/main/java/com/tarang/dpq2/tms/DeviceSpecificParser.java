package com.tarang.dpq2.tms;

import android.util.Log;

import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.model.DeviceSpecificModel;

import java.util.List;


public class DeviceSpecificParser {

    public static void toSaveDeviceSpecificDetails(List<String> fsList){
        DeviceSpecificModel deviceSpecificModel = new DeviceSpecificModel();

        deviceSpecificModel.setCardSchemeMada(fsList.get(0).substring(3,5));
        deviceSpecificModel.setTerminalContactlessTransactionLimitMada(fsList.get(0).substring(5,10));
        deviceSpecificModel.setTerminalCVMRequiredLimitMada(fsList.get(0).substring(10,15));
        deviceSpecificModel.setTerminalContactlessFloorLimitMada(fsList.get(0).substring(15,20));
      //  deviceSpecificModel.setTerminalContactlessFloorLimitMada("3000");
       // Logger.v("terminal_contactless_floor_limit_setto_hardcode_100");

        deviceSpecificModel.setCardScheme1(fsList.get(0).substring(20,22));
        deviceSpecificModel.setTerminalContactlessTransactionLimit1(fsList.get(0).substring(22,27));
        deviceSpecificModel.setTerminalCVMRequiredLimit1(fsList.get(0).substring(27,32));
        deviceSpecificModel.setTerminalContactlessFloorLimit1(fsList.get(0).substring(32,37));

        deviceSpecificModel.setCardScheme2(fsList.get(0).substring(37,39));
        deviceSpecificModel.setTerminalContactlessTransactionLimit2(fsList.get(0).substring(39,44));
        deviceSpecificModel.setTerminalCVMRequiredLimit2(fsList.get(0).substring(44,49));
        deviceSpecificModel.setTerminalContactlessFloorLimit2(fsList.get(0).substring(49,54));

        deviceSpecificModel.setMaxSAFDepth(fsList.get(0).substring(54,57));
        deviceSpecificModel.setMaxSAFCumulativeAmount(fsList.get(0).substring(57,64));
        deviceSpecificModel.setIdleTime(fsList.get(0).substring(64,67));
        deviceSpecificModel.setMaxReconciliationAmount(fsList.get(0).substring(67,75));
        deviceSpecificModel.setMaxTransactionsProcessed(fsList.get(0).substring(75,79));
        deviceSpecificModel.setqRCodePrintIndicator(fsList.get(0).substring(79));

        AppManager.getInstance().setDeviceSpecificDetailsModel(deviceSpecificModel);

        DeviceSpecificModel deviceSpecificModel1 = AppManager.getInstance().getDeviceSpecificModel();

        Log.i("De72 device: ", deviceSpecificModel1.toString());

    }
}
