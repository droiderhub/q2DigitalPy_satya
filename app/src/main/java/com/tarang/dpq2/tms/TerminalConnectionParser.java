package com.tarang.dpq2.tms;

import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.model.TerminalConnectionGPRSModel;
import com.tarang.dpq2.model.TerminalConnectionWifiModel;

import java.util.List;

public class TerminalConnectionParser {

    public static void toSaveTerminalConnectionDetails(List<String> fsList){
        String communicationType = fsList.get(1);
        if (communicationType.equalsIgnoreCase("03")){
            TerminalConnectionGPRSModel terminalConnectionGPRSModel = new TerminalConnectionGPRSModel();
            terminalConnectionGPRSModel.setPriority(fsList.get(0).substring(3));
            terminalConnectionGPRSModel.setCommunicationType(fsList.get(1));
            terminalConnectionGPRSModel.setGprsDailNumber(fsList.get(2));
            terminalConnectionGPRSModel.setGprsAccessPointName(fsList.get(3));
            terminalConnectionGPRSModel.setConnectTimeForGprsPhone(fsList.get(4));
            terminalConnectionGPRSModel.setNetworkIpAddress(fsList.get(5));
            terminalConnectionGPRSModel.setNetworkTcpPort(fsList.get(6));
            terminalConnectionGPRSModel.setDailAttemptsToNetwork(fsList.get(7));
            terminalConnectionGPRSModel.setResponseTimeout(fsList.get(8));
            terminalConnectionGPRSModel.setSslCirtificateFile(fsList.get(9));

            AppManager.getInstance().setTerminalConnectionGPRSDetailsModel(terminalConnectionGPRSModel);

            TerminalConnectionGPRSModel terminalConnectionGPRSModel1 = AppManager.getInstance().getTerminalConnectionGPRSModel();

            Logger.v("De72 termlina gprs: "+ terminalConnectionGPRSModel1.toString());

        } else if (communicationType.equalsIgnoreCase("04")){
            TerminalConnectionWifiModel terminalConnectionWifiModel = new TerminalConnectionWifiModel();
            terminalConnectionWifiModel.setPriority(fsList.get(0).substring(3));
            terminalConnectionWifiModel.setCommunicationType(fsList.get(1));
            terminalConnectionWifiModel.setNetworkIpAddress(fsList.get(2));
            terminalConnectionWifiModel.setNetworkTcpPort(fsList.get(3));
            terminalConnectionWifiModel.setCountAccessRetries(fsList.get(4));
            terminalConnectionWifiModel.setResponseTimeout(fsList.get(5));
            terminalConnectionWifiModel.setSslCirtificateFile(fsList.get(6));

            AppManager.getInstance().setTerminalConnectionWifiDetailsModel(terminalConnectionWifiModel);

            TerminalConnectionWifiModel terminalConnectionWifiModel1 = AppManager.getInstance().getTerminalConnectionWifiModel();

            Logger.v("De72 terminal wifi: "+ terminalConnectionWifiModel1.toString());
        }


    }
}
