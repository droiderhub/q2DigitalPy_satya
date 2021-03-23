package com.tarang.dpq2.model;

import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.jpos_class.ByteConversionUtils;

import org.jpos.iso.ISOUtil;

import java.util.List;

public class MPortalReconModel {

    String merchantName = "Digital Pay";
    String transactionType = "RECONCILIATION";
    String terminalDate = "03/10/2020";
    String terminalTime = "18:04:42";
    String serverTime = "03/10/2020";
    String serverDate = "18:04:42";
    String cardName = "RAJB";
    String tId = "1234567812121234";
    String mId = "800150400566";
    String mcc = "7399";
    String stan = "100128";
    String appVersion = "1.1.0";

    String MhDbtCnt = "1";
    String MhDbtAmt = "SAR90.00";
    String MhCrdtCnt = "0";
    String MhCrdtAmt = "SAR0.00";
    String MhNqdCnt = "0";
    String MhNqdAmt = "SAR0.00";
    String MhCshAdvCnt = "0";
    String MhCshAdvAmt = "SAR0.00";
    String MhAuthCnt = "0";
    String MhAuthAmt = "0";
    String MhTtlCnt = "1";
    String MhTtlAmt = "90.00";

    String PtDbtCnt = "2";
    String PtDbtAmt = "SAR444.44";
    String PtCrdtCnt = "0";
    String PtCrdtAmt = "SAR0.00";
    String PtNqdCnt = "0";
    String PtNqdAmt = "SAR0.00";
    String PtCshAdvCnt = "0";
    String PtCshAdvAmt = "SAR0.00";
    String PtAuthCnt = "0";
    String PtAuthAmt = "0";
    String PtTtlCnt = "0";
    String PtTtlAmt = "0.00";

    String PtdPOffCnt = "0";
    String PtdPOffAmt = "0.00";
    String PtdPOnCnt = "(null)";
    String PtdPOnAmt = "0.00";
    String PtdPNqdCnt = "(null)";
    String PtdPNqdAmt = "0.00";
    String PtdPRevCnt = "(null)";
    String PtdPRevAmt = "0.00";
    String PtdRfndCnt = "(null)";
    String PtdRfndAmt = "0.00";
    String PtdCompCnt = "(null)";
    String PtdCompAmt = "0.00";

    String P1cardSchID = "MADA";
    String P1Txnstatus = "AVAILABLE";

    String VCcardSchID = "VISA";
    String VCTxnstatus = "NOTAVAILABLE";

    String GNcardSchID = "GCCNET";
    String GNTxnstatus = "NOTAVAILABLE";

    String MCcardSchID = "MASTERCARD";
    String MCTxnstatus = "NOTAVAILABLE";

    String DMcardSchID = "MAESTRO";
    String DMTxnstatus = "NOTAVAILABLE";

    String AXcardSchID = "AMERICANEXPRESS";
    String AXTxnstatus = "NOTAVAILABLE";

    String UPcardSchID = "UNIONPAY";
    String UPTxnstatus = "NOTAVAILABLE";
    private String pendingData = "";

    public String sendReconData(int i) {
        if(i == -1){
            return sendReconDataEmpty();
        }
        ReconcilationTopModel reconcilationTopModel = AppManager.getInstance().getReconciliationTopCard();
        List<ReconciliationCardSchemeModel> listData = AppManager.getInstance().getReconciliationCardSchemeModelList();
        Logger.v("recon size --"+listData.size());
        if (listData.size() <= i) {
            return "";
        }
        String data = "";
        dumpTopSection(reconcilationTopModel);
        if (listData.get(i).getMadaHostTotalsCount() == null && listData.get(i).getMadaHostTotalsAmountInSar() == null) {
            return "1";
        } else {
            dumptDataSection(listData.get(i));
            String schem = appendCardSchem(listData.get(i).getCardSchemeEnglish());
            data = transactionWithData(schem,false) + appendCardSchem(listData.get(i).getCardSchemeEnglish(), true);
            pendingData = transactionWithData(schem,true) + appendCardSchem(listData.get(i).getCardSchemeEnglish(), true);
        }
        return data;
    }
    public String sendReconDataEmpty() {
        ReconcilationTopModel reconcilationTopModel = AppManager.getInstance().getReconciliationTopCard();
        List<ReconciliationCardSchemeModel> listData = AppManager.getInstance().getReconciliationCardSchemeModelList();
        Logger.v("recon size --"+listData.size());
        String data = transactionWithOutData();
        pendingData = transactionWithOutDataPending();
        dumpTopSection(reconcilationTopModel);
        for(int i =0;i<listData.size();i++) {
            if (listData.get(i).getMadaHostTotalsCount() == null && listData.get(i).getMadaHostTotalsAmountInSar() == null) {
                data =  data + appendCardSchem(listData.get(i).getCardSchemeEnglish(), false);
                pendingData = pendingData + appendCardSchem(listData.get(i).getCardSchemeEnglish(), false);
            }
        }
        return data;
    }

    private void dumpTopSection(ReconcilationTopModel reconcilationTopModel) {
        merchantName = reconcilationTopModel.getRetailerNameEnglish();
        transactionType = "RECONCILIATION";
        terminalDate = reconcilationTopModel.getStartDate();
        terminalTime = reconcilationTopModel.getStartTime();
        serverDate = reconcilationTopModel.getEndDate();
        serverTime = reconcilationTopModel.getEndTime();
        cardName = reconcilationTopModel.getSchemID();
        tId = reconcilationTopModel.gettId();
        mId = reconcilationTopModel.getmId().trim();
        mcc = reconcilationTopModel.getMcc();
        stan = reconcilationTopModel.getTransactionSTAN();
        appVersion = reconcilationTopModel.getPosSoftwareVersionNumber();
    }

    public void dumptDataSection(ReconciliationCardSchemeModel reconciliationCardSchemeModel) {

        MhDbtCnt = checkDashEmpty(reconciliationCardSchemeModel.getMadaHostTotalDbCount());
        MhDbtAmt = checkDashEmpty(reconciliationCardSchemeModel.getMadaHostTotalDbAmountInSar());
        MhCrdtCnt = checkDashEmpty(reconciliationCardSchemeModel.getMadaHostTotalCrCount());
        MhCrdtAmt = checkDashEmpty(reconciliationCardSchemeModel.getMadaHostTotalCrAmountInSar());
        MhNqdCnt = checkDashEmpty(reconciliationCardSchemeModel.getMadaHostNAQDCount());
        MhNqdAmt = checkDashEmpty(reconciliationCardSchemeModel.getMadaHostNAQDAmountInSar());
        MhCshAdvCnt = checkDashEmpty(reconciliationCardSchemeModel.getMadaHostCashAdvanceCount());
        MhCshAdvAmt = checkDashEmpty(reconciliationCardSchemeModel.getMadaHostCashAdvanceAmountInSar());
        MhAuthCnt = checkDashEmpty(reconciliationCardSchemeModel.getMadaHostAuthCount());
        MhAuthAmt = checkDashEmpty(reconciliationCardSchemeModel.getMadaHostAuthAmountInSar());
        MhTtlCnt = checkDashEmpty(reconciliationCardSchemeModel.getMadaHostTotalsCount());
        MhTtlAmt = checkDashEmpty(reconciliationCardSchemeModel.getMadaHostTotalsAmountInSar());

        PtDbtCnt = checkDashEmpty(reconciliationCardSchemeModel.getPosTerminalTotalDbCount());
        PtDbtAmt = checkDashEmpty(reconciliationCardSchemeModel.getPosTerminalTotalDbAmountInSar());
        PtCrdtCnt = checkDashEmpty(reconciliationCardSchemeModel.getPosTerminalTotalCrCount());
        PtCrdtAmt = checkDashEmpty(reconciliationCardSchemeModel.getPosTerminalTotalCrAmountInSar());
        PtNqdCnt = checkDashEmpty(reconciliationCardSchemeModel.getPosTerminalNAQDCount());
        PtNqdAmt = checkDashEmpty(reconciliationCardSchemeModel.getPosTerminalNAQDAmountInSar());
        PtCshAdvCnt = checkDashEmpty(reconciliationCardSchemeModel.getPasTerminalCashAdvancedCount());
        PtCshAdvAmt = checkDashEmpty(reconciliationCardSchemeModel.getPosTerminalCashAdvanceAmountInSar());
        PtAuthCnt = checkDashEmpty(reconciliationCardSchemeModel.getPosTerminalAuthCount());
        PtAuthAmt = checkDashEmpty(reconciliationCardSchemeModel.getPosTerminalAuthAmountInSar());
        PtTtlCnt = checkDashEmpty(reconciliationCardSchemeModel.getPosTerminalTotalsCount());
        PtTtlAmt = checkDashEmpty(reconciliationCardSchemeModel.getPosTerminalTotalsAmountInSar());

        PtdPOffCnt = checkDashEmpty(reconciliationCardSchemeModel.getPosTerminalDetailsPOFFCount());
        PtdPOffAmt = checkDashEmpty(reconciliationCardSchemeModel.getPosTerminalDetailsPOFFAmountInSar());
        PtdPOnCnt = checkDashEmpty(reconciliationCardSchemeModel.getPosTerminalDetailsPONCount());
        PtdPOnAmt = checkDashEmpty(reconciliationCardSchemeModel.getPosTerminalDetailsPONAmountInSar());
        PtdPNqdCnt = checkDashEmpty(reconciliationCardSchemeModel.getPosTerminalDetailsPurNaqdCount());
        PtdPNqdAmt = checkDashEmpty(reconciliationCardSchemeModel.getPosTerminalDetailsPurNaqdAmountInSar());
        PtdPRevCnt = checkDashEmpty(reconciliationCardSchemeModel.getPosTerminalDetailsReversalCount());
        PtdPRevAmt = checkDashEmpty(reconciliationCardSchemeModel.getPosTerminalDetailsReversalAmountInSar());
        PtdRfndCnt = checkDashEmpty(reconciliationCardSchemeModel.getPosTerminalDetailsRefundCount());
        PtdRfndAmt = checkDashEmpty(reconciliationCardSchemeModel.getPosTerminalDetailsRefundAmountInSar());
        PtdCompCnt = checkDashEmpty(reconciliationCardSchemeModel.getPosTerminalDetailsCompCount());
        PtdCompAmt = checkDashEmpty(reconciliationCardSchemeModel.getPosTerminalDetailsCompAmountInSar());
    }

    private String checkDashEmpty(String data) {
        if(data == null || data.trim().length() == 0 || data.trim().equalsIgnoreCase("-"))
            return "0";
        return data;
    }


    public byte[] toHexadecimal(String text) {
        Logger.v("SAMPLE text 11");
        try {
            byte[] inputData = ((text.getBytes("UTF-8")));
            byte[] appendData = ISOUtil.concat(inputData, ByteConversionUtils.HexStringToByteArray("0D0A7D"));
            Logger.v(appendData);
            byte[] echoResponseLength = ByteConversionUtils.intToByteArray(appendData.length); //+TPDU_HEADER_LENGTH);
            byte[] finalData = ISOUtil.concat(echoResponseLength, appendData);
            Logger.v(finalData);
            return finalData;
        } catch (Exception e) {

        }
        return null;
//        return ByteConversionUtils.HexStringToByteArray("02747B226D65726368616E744E616D65223A224469676974616C20506179222C227472616E73616374696F6E54797065223A225055524348415345222C227465726D696E616C44617465223A2230382F31322F32303230222C227465726D696E616C54696D65223A2230393A32363A3337222C22636172644E616D65223A2252414A42222C22744964223A2231323334353637383132313231323334222C226D4964223A22383030313530343030353636222C226D6363223A2237333939222C227374616E223A22313030353333222C226170706C69636174696F6E56657273696F6E223A2256312E312E32222C2263617264536368656D614E616D65223A226D616461222C22636172644E756D6265724D61736B223A223435353033362A2A2A2A2A2A37363139222C2263617264457870223A2231322F3230222C22616D6F756E74223A22534152313434342E3434222C227472616E73616374696F6E537461747573223A22415050524F564544222C22617070726F76616C436F6465223A22303937363139222C2273657276657244617465223A2230382F31322F32303230222C2273657276657254696D65223A2230393A32363A3530222C226361726454797065223A22434F4E544143544C455353222C2264653339223A22303030222C2263617264414964223A224130303030303032323832303130222C22747672223A2230303030303030303030222C22747369223A2230303030222C2263766D223A22222C2261717263223A223830222C22656D7664617461223A2241423843413943413643343638373646222C22656D766B65726E656C223A223033222C2272726E223A22303334333039303932363236220D0A7D");
    }

    public String transactionWithData(String schem, boolean pending) {
        return "{" +
                "\"merchantName\":\"" + merchantName + "\"" +
                ",\"terminalType\":\"" + "N910" + "\"" +
                ",\"request_stat\":\"" + (pending?"pending":"active") + "\"" +
                ",\"transactionType\":\"" + transactionType + "\"" +
                ",\"terminalDate\":\"" + terminalDate + "\"" +
                ",\"terminalTime\":\"" + terminalTime + "\"" +
                ",\"serverTime\":\"" + serverTime + "\"" +
                ",\"serverDate\":\"" + serverDate + "\"" +
                ",\"cardName\":\"" + cardName + "\"" +
                ",\"tId\":\"" + tId + "\"" +
                ",\"mId\":\"" + mId + "\"" +
                ",\"mcc\":\"" + mcc + "\"" +
                ",\"stan\":\"" + stan + "\"" +
                ",\"appVersion\":\"" + appVersion + "\"" +

                ",\""+schem+"MhHeading\":\"" + "MadaHostDetails" + "\"" +
                ",\""+schem+"MhDbtCnt\":\"" + MhDbtCnt + "\"" +
                ",\""+schem+"MhDbtAmt\":\"" + MhDbtAmt + "\"" +
                ",\""+schem+"MhCrdtCnt\":\"" + MhCrdtCnt + "\"" +
                ",\""+schem+"MhCrdtAmt\":\"" + MhCrdtAmt + "\"" +
                ",\""+schem+"MhNqdCnt\":\"" + MhNqdCnt + "\"" +
                ",\""+schem+"MhNqdAmt\":\"" + MhNqdAmt + "\"" +
                ",\""+schem+"MhCshAdvCnt\":\"" + MhCshAdvCnt + "\"" +
                ",\""+schem+"MhCshAdvAmt\":\"" + MhCshAdvAmt + "\"" +
                ",\""+schem+"MhAuthCnt\":\"" + MhAuthCnt + "\"" +
                ",\""+schem+"MhAuthAmt\":\"" + MhAuthAmt + "\"" +
                ",\""+schem+"MhTtlCnt\":\"" + MhTtlCnt + "\"" +
                ",\""+schem+"MhTtlAmt\":\"" + MhTtlAmt + "\"" +

                ",\""+schem+"PtHeading\":\"" + "POSTerminal" + "\"" +
                ",\""+schem+"PtDbtCnt\":\"" + PtDbtCnt + "\"" +
                ",\""+schem+"PtDbtAmt\":\"" + PtDbtAmt + "\"" +
                ",\""+schem+"PtCrdtCnt\":\"" + PtCrdtCnt + "\"" +
                ",\""+schem+"PtCrdtAmt\":\"" + PtCrdtAmt + "\"" +
                ",\""+schem+"PtNqdCnt\":\"" + PtNqdCnt + "\"" +
                ",\""+schem+"PtNqdAmt\":\"" + PtNqdAmt + "\"" +
                ",\""+schem+"PtCshAdvCnt\":\"" + PtCshAdvCnt + "\"" +
                ",\""+schem+"PtCshAdvAmt\":\"" + PtCshAdvAmt + "\"" +
                ",\""+schem+"PtAuthCnt\":\"" + PtAuthCnt + "\"" +
                ",\""+schem+"PtAuthAmt\":\"" + PtAuthAmt + "\"" +
                ",\""+schem+"PtTtlCnt\":\"" + PtTtlCnt + "\"" +
                ",\""+schem+"PtTtlAmt\":\"" + PtTtlAmt + "\"" +

                ",\""+schem+"PtdHeading\":\"" + "POSTerminalDetails" + "\"" +
                ",\""+schem+"PtdPOffCnt\":\"" + PtdPOffCnt + "\"" +
                ",\""+schem+"PtdPOffAmt\":\"" + PtdPOffAmt + "\"" +
                ",\""+schem+"PtdPOnCnt\":\"" + PtdPOnCnt + "\"" +
                ",\""+schem+"PtdPOnAmt\":\"" + PtdPOnAmt + "\"" +
                ",\""+schem+"PtdPNqdCnt\":\"" + PtdPNqdCnt + "\"" +
                ",\""+schem+"PtdPNqdAmt\":\"" + PtdPNqdAmt + "\"" +
                ",\""+schem+"PtdPRevCnt\":\"" + PtdPRevCnt + "\"" +
                ",\""+schem+"PtdPRevAmt\":\"" + PtdPRevAmt + "\"" +
                ",\""+schem+"PtdRfndCnt\":\"" + PtdRfndCnt + "\"" +
                ",\""+schem+"PtdRfndAmt\":\"" + PtdRfndAmt + "\"" +
                ",\""+schem+"PtdCompCnt\":\"" + PtdCompCnt + "\"" +
                ",\""+schem+"PtdCompAmt\":\"" + PtdCompAmt + "\"";
    }

    public String transactionWithOutData() {
        return "{" +
                "\"merchantName\":\"" + merchantName + "\"" +
                ",\"terminalType\":\"" + "N910" + "\"" +
                ",\"request_stat\":\"" + "active" + "\"" +
                ",\"transactionType\":\"" + transactionType + "\"" +
                ",\"terminalDate\":\"" + terminalDate + "\"" +
                ",\"serverTime\":\"" + serverTime + "\"" +
                ",\"serverDate\":\"" + serverDate + "\"" +
                ",\"terminalTime\":\"" + terminalTime + "\"" +
                ",\"cardName\":\"" + cardName + "\"" +
                ",\"tId\":\"" + tId + "\"" +
                ",\"mId\":\"" + mId + "\"" +
                ",\"mcc\":\"" + mcc + "\"" +
                ",\"stan\":\"" + stan + "\"" +
                ",\"appVersion\":\"" + appVersion + "\"";
    }

    public String transactionWithDataPending() {
        return "{" +
                "\"merchantName\":\"" + merchantName + "\"" +
                ",\"terminalType\":\"" + "N910" + "\"" +
                ",\"request_stat\":\"" + "pending" + "\"" +
                ",\"serverTime\":\"" + serverTime + "\"" +
                ",\"serverDate\":\"" + serverDate + "\"" +
                ",\"transactionType\":\"" + transactionType + "\"" +
                ",\"terminalDate\":\"" + terminalDate + "\"" +
                ",\"terminalTime\":\"" + terminalTime + "\"" +
                ",\"cardName\":\"" + cardName + "\"" +
                ",\"tId\":\"" + tId + "\"" +
                ",\"mId\":\"" + mId + "\"" +
                ",\"mcc\":\"" + mcc + "\"" +
                ",\"stan\":\"" + stan + "\"" +
                ",\"appVersion\":\"" + appVersion + "\"" +

                ",\"MhHeading\":\"" + "MadaHostDetails" + "\"" +
                ",\"MhDbtCnt\":\"" + MhDbtCnt + "\"" +
                ",\"MhDbtAmt\":\"" + MhDbtAmt + "\"" +
                ",\"MhCrdtCnt\":\"" + MhCrdtCnt + "\"" +
                ",\"MhCrdtAmt\":\"" + MhCrdtAmt + "\"" +
                ",\"MhNqdCnt\":\"" + MhNqdCnt + "\"" +
                ",\"MhNqdAmt\":\"" + MhNqdAmt + "\"" +
                ",\"MhCshAdvCnt\":\"" + MhCshAdvCnt + "\"" +
                ",\"MhCshAdvAmt\":\"" + MhCshAdvAmt + "\"" +
                ",\"MhAuthCnt\":\"" + MhAuthCnt + "\"" +
                ",\"MhAuthAmt\":\"" + MhAuthAmt + "\"" +
                ",\"MhTtlCnt\":\"" + MhTtlCnt + "\"" +
                ",\"MhTtlAmt\":\"" + MhTtlAmt + "\"" +

                ",\"PtHeading\":\"" + "POSTerminal" + "\"" +
                ",\"PtDbtCnt\":\"" + PtDbtCnt + "\"" +
                ",\"PtDbtAmt\":\"" + PtDbtAmt + "\"" +
                ",\"PtCrdtCnt\":\"" + PtCrdtCnt + "\"" +
                ",\"PtCrdtAmt\":\"" + PtCrdtAmt + "\"" +
                ",\"PtNqdCnt\":\"" + PtNqdCnt + "\"" +
                ",\"PtNqdAmt\":\"" + PtNqdAmt + "\"" +
                ",\"PtCshAdvCnt\":\"" + PtCshAdvCnt + "\"" +
                ",\"PtCshAdvAmt\":\"" + PtCshAdvAmt + "\"" +
                ",\"PtAuthCnt\":\"" + PtAuthCnt + "\"" +
                ",\"PtAuthAmt\":\"" + PtAuthAmt + "\"" +
                ",\"PtTtlCnt\":\"" + PtTtlCnt + "\"" +
                ",\"PtTtlAmt\":\"" + PtTtlAmt + "\"" +

                ",\"PtdHeading\":\"" + "POSTerminalDetails" + "\"" +
                ",\"PtdPOffCnt\":\"" + PtdPOffCnt + "\"" +
                ",\"PtdPOffAmt\":\"" + PtdPOffAmt + "\"" +
                ",\"PtdPOnCnt\":\"" + PtdPOnCnt + "\"" +
                ",\"PtdPOnAmt\":\"" + PtdPOnAmt + "\"" +
                ",\"PtdPNqdCnt\":\"" + PtdPNqdCnt + "\"" +
                ",\"PtdPNqdAmt\":\"" + PtdPNqdAmt + "\"" +
                ",\"PtdPRevCnt\":\"" + PtdPRevCnt + "\"" +
                ",\"PtdPRevAmt\":\"" + PtdPRevAmt + "\"" +
                ",\"PtdRfndCnt\":\"" + PtdRfndCnt + "\"" +
                ",\"PtdRfndAmt\":\"" + PtdRfndAmt + "\"" +
                ",\"PtdCompCnt\":\"" + PtdCompCnt + "\"" +
                ",\"PtdCompAmt\":\"" + PtdCompAmt + "\"";
    }

    public String transactionWithOutDataPending() {
        return "{" +
                "\"merchantName\":\"" + merchantName + "\"" +
                ",\"terminalType\":\"" + "N910" + "\"" +
                ",\"request_stat\":\"" + "pending" + "\"" +
                ",\"serverTime\":\"" + serverTime + "\"" +
                ",\"serverDate\":\"" + serverDate + "\"" +
                ",\"transactionType\":\"" + transactionType + "\"" +
                ",\"terminalDate\":\"" + terminalDate + "\"" +
                ",\"terminalTime\":\"" + terminalTime + "\"" +
                ",\"cardName\":\"" + cardName + "\"" +
                ",\"tId\":\"" + tId + "\"" +
                ",\"mId\":\"" + mId + "\"" +
                ",\"mcc\":\"" + mcc + "\"" +
                ",\"stan\":\"" + stan + "\"" +
                ",\"appVersion\":\"" + appVersion + "\"";
    }


    public String appendCardSchem(String cardSchem, boolean available) {
        String status = available ? "AVAILABLE" : "NOTAVAILABLE";
        Logger.v("cardSchem.toUpperCase() --" + cardSchem.toUpperCase());
        switch (changeIndicaor(cardSchem.toUpperCase())) {
            case "VC":
                return ",\"VCcardSchID\":\"" + "VISA" + "\"" + ",\"VCTxnstatus\":\"" + status + "\"";
            case "VD":
                return ",\"VCcardSchID\":\"" + "VISA" + "\"" + ",\"VCTxnstatus\":\"" + status + "\"";
            case "P1":
                return ",\"P1cardSchID\":\"" + "MADA" + "\"" + ",\"P1Txnstatus\":\"" + status + "\"";
            case "GN":
                return ",\"GNcardSchID\":\"" + "GCCNET" + "\"" + ",\"GNTxnstatus\":\"" + status + "\"";
            case "MC":
                return ",\"MCcardSchID\":\"" + "MASTERCARD" + "\"" + ",\"MCTxnstatus\":\"" + status + "\"";
            case "DM":
                return ",\"DMcardSchID\":\"" + "MAESTRO" + "\"" + ",\"DMTxnstatus\":\"" + status + "\"";
            case "AX":
                return ",\"AXcardSchID\":\"" + "AMERICANEXPRESS" + "\"" + ",\"AXTxnstatus\":\"" + status + "\"";
            case "UP":
                return ",\"UPcardSchID\":\"" + "UNIONPAY" + "\"" + ",\"UPTxnstatus\":\"" + status + "\"";
        }
        return "";
    }
    public String appendCardSchem(String cardSchem) {
        Logger.v("cardSchem.toUpperCase() --" + cardSchem.toUpperCase());
        String schem = changeIndicaor(cardSchem.toUpperCase());
        if(schem.equalsIgnoreCase("P1"))
            return "";
        return schem;
    }

    private String changeIndicaor(String indicator) {
        if (indicator.contains("MADA") || indicator.contains("SPAN"))
            return "P1";
        else if (indicator.contains("VISA"))
            return "VC";
        else if (indicator.contains("MASTER"))
            return "MC";
        else if (indicator.contains("MAESTRO"))
            return "DM";
        else if (indicator.contains("AMERICAN"))
            return "AX";
        else if (indicator.contains("UNIONPAY"))
            return "UP";
        else if (indicator.contains("GCCNET"))
            return "GN";
        return "";
    }

    public byte[] getPendingData() {
        return toHexadecimal(pendingData);
    }
}
