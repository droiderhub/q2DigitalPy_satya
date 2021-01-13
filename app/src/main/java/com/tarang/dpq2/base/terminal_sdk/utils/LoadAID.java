package com.tarang.dpq2.base.terminal_sdk.utils;

import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.jpos_class.ConstantApp;
import com.tarang.dpq2.base.room_database.db.entity.TMSAIDdataModelEntity;
import com.tarang.dpq2.base.terminal_sdk.q2.AIDTable;
import com.tarang.dpq2.base.utilities.Utils;
import com.tarang.dpq2.model.DeviceSpecificModel;
import com.tarang.dpq2.model.RetailerDataModel;

public class LoadAID {
    public static AIDTable loadAIDS(TMSAIDdataModelEntity aidData) {
        Logger.v("load_aidtable_loadAIDS");
        DeviceSpecificModel deviceSpecificModel1 = AppManager.getInstance().getDeviceSpecificModel();
        RetailerDataModel retailerDataModel = AppManager.getInstance().getRetailerDataModel();
        Logger.v("retailerDataModel --"+retailerDataModel.toString());
        AIDTable aidTable = new AIDTable();
        aidTable.setAid(aidData.getAid());
        aidTable.setAppLabel(aidData.getAidLable());
        aidTable.setAppPreferredName("AEEFFF");
        aidTable.setAppPriority((byte) 0);
        String floorLimit = AppManager.getInstance().getFloorLimit(Utils.fetchIndicatorFromAID(aidData.getAid()));
        boolean enabled = AppManager.getInstance().isFllorLimitEnabled(Utils.fetchIndicatorFromAID(aidData.getAid()));
        if (enabled) {
            aidTable.setTermFloorLimit(Integer.parseInt(floorLimit));
        } else {
            aidTable.setTermFloorLimit(0);
        }
        aidTable.setTACDefault(aidData.getDefaultActionCode());
        aidTable.setTACDenial(aidData.getDenialActionCode());
        aidTable.setTACOnline(aidData.getOnlineActionCode());
        aidTable.setThresholdValue(Integer.parseInt(aidData.getTrsholdValue()));
        aidTable.setMaxTargetPercentage((byte) 0);
        aidTable.setTargetPercentage((byte) 0);
        aidTable.setAcquirerId("000000123456");
        aidTable.setPOSEntryMode((byte) 0x80);
        aidTable.setMCC(AppManager.getInstance().getMerchantCode(Utils.fetchIndicatorFromAID(aidData.getAid())));
        aidTable.setMID(AppManager.getInstance().getCardAcceptorCode42());
        aidTable.setAppVersionNumber("0096");
        aidTable.setTransReferCurrencyCode("0" +retailerDataModel.getTerminalCurrencyCode());
        aidTable.setTransReferCurrencyExponent((byte) (Integer.parseInt(retailerDataModel.getTransactionCurrencyExponent())));
        aidTable.setDefaultTDOL(aidData.getDefaultTDOL());
        aidTable.setDefaultDDOL(aidData.getDefaultDDOL());
        aidTable.setNeedCompleteMatching((byte) 0);
        aidTable.setSupportOnlinePin((byte) 1);

        // C2
//        aidTable.setKernelConfig((byte)0x20);
//        aidTable.setCtlNoOnDeviceCVM(appState.terminalConfig.getContactlessLimit());
//        aidTable.setCtlOnDeviceCVM(appState.terminalConfig.getContactlessLimit());
//        aidTable.setCvmCapCVMRequired((byte)0x60);   // Online PIN & Signature
//        aidTable.setCvmCapNoCVMRequired((byte)0x68); // Online PIN & Signature & No CVM
//        aidTable.setMscvmCapCVMRequired((byte)0x20);
//        aidTable.setMscvmCapNoCVMRequired((byte)0x20);

//        aidTable.setTransReferCurrencyCode("0156");

//        if (aidData.getAid().equalsIgnoreCase(ConstantApp.A0000000041010)) {
//            aidTable.setCvmLimit(parseStringtoInt(deviceSpecificModel1.getTerminalCVMRequiredLimit2().trim()));
//            aidTable.setContactlessFloorLimit(parseStringtoInt(deviceSpecificModel1.getTerminalContactlessFloorLimit2().trim()));
////            config.setNciccTransLimit(ISOUtils.hex2byte(String.format("%012d", Integer.parseInt(deviceSpecificModel1.getTerminalContactlessTransactionLimit2().trim()))));
//        } else if (aidData.getAid().equalsIgnoreCase(ConstantApp.A0000000031010) || aidData.getAid().equalsIgnoreCase(ConstantApp.A0000000032010)) {
//            aidTable.setCvmLimit(parseStringtoInt(deviceSpecificModel1.getTerminalCVMRequiredLimit1().trim()));
//            aidTable.setContactlessFloorLimit(parseStringtoInt(deviceSpecificModel1.getTerminalContactlessFloorLimit1().trim()));
////            config.setNciccTransLimit(ISOUtils.hex2byte(String.format("%012d", Integer.parseInt(deviceSpecificModel1.getTerminalContactlessTransactionLimit1()))));
//        } else if (aidData.getAid().equalsIgnoreCase(ConstantApp.A0000002281010) || aidData.getAid().equalsIgnoreCase(ConstantApp.A0000002282010)) {
            aidTable.setCvmLimit(parseStringtoInt(deviceSpecificModel1.getTerminalCVMRequiredLimitMada().trim()));
            aidTable.setContactlessFloorLimit(parseStringtoInt(deviceSpecificModel1.getTerminalContactlessFloorLimitMada().trim()));
//            config.setNciccTransLimit(ISOUtils.hex2byte(String.format("%12d", Integer.parseInt(deviceSpecificModel1.getTerminalContactlessTransactionLimitMada()))));
//        }

//        if(aidData.getAid().equalsIgnoreCase("A0000002281010"))
//            aidTable.setContactlessKernelID((byte)2D); // mastercard contactless
//        else
        if(aidData.getAid().equalsIgnoreCase("A0000002282010")) {
            aidTable.setKernelConfig((byte)0x20);
            aidTable.setCvmCapCVMRequired((byte)0x60);   // Online PIN & Signature
            aidTable.setCvmCapNoCVMRequired((byte)0x68); // Online PIN & Signature & No CVM
            aidTable.setMscvmCapCVMRequired((byte)0x20);
            aidTable.setMscvmCapNoCVMRequired((byte)0x20);
            aidTable.setContactlessKernelID((byte) 3); // mastercard contactless
            aidTable.setAppVersionNumber("0002");
        }
        Logger.v("aidTable KTD -"+aidTable.getContactlessKernelID());
        Logger.v("aidTable KTD -"+aidTable.getAid());
        return aidTable;
    }

    private static int parseStringtoInt(String data) {
        if(data == null || data.trim().length() == 0)
            return 0;
        return Integer.parseInt(data);
    }

//    private final EmvModule emvModule;
//    Context context;
//
//    public LoadAID(Context context) {
//        this.context = context;
//        emvModule = SDKDevice.getInstance(context).getEmvModuleType();
//        emvModule.initEmvModule(context);
//    }
//
//    public void load() {
////        loadAID();
//        loadCAPKeys();
//    }
//
//    public static AIDConfig loadAID(TMSAIDdataModelEntity aidData) {
////        Logger.v("dataset.size()--"+dataset.size());
//        DeviceSpecificModel deviceSpecificModel1 = AppManager.getInstance().getDeviceSpecificModel();
////        for (int i = 0; i < dataset.size(); i++) {
////            TMSAIDdataModelEntity aidData = dataset.get(i);
//        Logger.v("aidList ---" + aidData.toString());
//        AIDConfig config = new AIDConfig();
//        config.setAid(ISOUtils.hex2byte(aidData.getAid()));
//        config.setAppSelectIndicator(1);
//        config.setAppVersionNumberTerminal(ISOUtils.hex2byte("0000"));
//        config.setTacDefault(ISOUtils.hex2byte(aidData.getDefaultActionCode()));
//        config.setTacOnLine(ISOUtils.hex2byte(aidData.getOnlineActionCode()));
//        config.setTacDenial(ISOUtils.hex2byte(aidData.getDenialActionCode()));
//        String floorLimit = AppManager.getInstance().getFloorLimit(Utils.fetchIndicatorFromAID(aidData.getAid()));
//        boolean enabled = AppManager.getInstance().isFllorLimitEnabled(Utils.fetchIndicatorFromAID(aidData.getAid()));
//        Logger.v("AID -- "+aidData.getAid());
//        Logger.v("Floor Limit - "+floorLimit+"--"+enabled);
//        if (enabled && floorLimit.trim().length() != 0) {
//            String limit = "00000000"+Integer.toHexString(Integer.parseInt(floorLimit));
//            Logger.v("Floor Limit - "+(limit).substring(limit.length() - 8));
//            config.setTerminalFloorLimit(ISOUtils.hex2byte((limit).substring(limit.length() - 8)));
//        }else
//            config.setTerminalFloorLimit(ISOUtils.hex2byte("00000000"));
//        config.setThresholdValueForBiasedRandomSelection(ISOUtils.hex2byte("00000000"));
//        config.setMaxTargetPercentageForBiasedRandomSelection(Integer.parseInt((aidData.getMaxTargetPercentage().trim().length() != 0) ? aidData.getMaxTargetPercentage().trim() : "0"));
//        config.setTargetPercentageForRandomSelection(Integer.parseInt((aidData.getTargetPercentage().trim().length() != 0) ? aidData.getTargetPercentage() : "0"));
//        config.setDefaultDDOL(ISOUtils.hex2byte(aidData.getDefaultDDOL()));
//        config.setOnLinePinCapability(1);
////            config.setEcTransLimit(deviceSpecificModel1.);
//        if (aidData.getAid().equalsIgnoreCase(Constant.A0000000041010)) {
//            config.setNciccOffLineFloorLimit(ISOUtils.hex2byte(String.format("%012d", Integer.parseInt(deviceSpecificModel1.getTerminalContactlessFloorLimit2().trim()))));
//            config.setNciccCVMLimit(ISOUtils.hex2byte(String.format("%012d", Integer.parseInt(deviceSpecificModel1.getTerminalCVMRequiredLimit2().trim()))));
//            config.setNciccTransLimit(ISOUtils.hex2byte(String.format("%012d", Integer.parseInt(deviceSpecificModel1.getTerminalContactlessTransactionLimit2().trim()))));
//        } else if (aidData.getAid().equalsIgnoreCase(Constant.A0000000031010) || aidData.getAid().equalsIgnoreCase(Constant.A0000000032010)) {
//            config.setNciccOffLineFloorLimit(ISOUtils.hex2byte(String.format("%012d", Integer.parseInt(deviceSpecificModel1.getTerminalContactlessFloorLimit1()))));
//            config.setNciccCVMLimit(ISOUtils.hex2byte(String.format("%012d", Integer.parseInt(deviceSpecificModel1.getTerminalCVMRequiredLimit1()))));
//            config.setNciccTransLimit(ISOUtils.hex2byte(String.format("%012d", Integer.parseInt(deviceSpecificModel1.getTerminalContactlessTransactionLimit1()))));
//        } else if (aidData.getAid().equalsIgnoreCase(Constant.A0000002281010) || aidData.getAid().equalsIgnoreCase(Constant.A0000002282010)) {
//            config.setNciccOffLineFloorLimit(ISOUtils.hex2byte(String.format("%012d", Integer.parseInt(deviceSpecificModel1.getTerminalContactlessFloorLimitMada()))));
//            config.setNciccCVMLimit(ISOUtils.hex2byte(String.format("%012d", Integer.parseInt(deviceSpecificModel1.getTerminalCVMRequiredLimitMada()))));
//            config.setNciccTransLimit(ISOUtils.hex2byte(String.format("%12d", Integer.parseInt(deviceSpecificModel1.getTerminalContactlessTransactionLimitMada()))));
//        }
////            config.setNciccOffLineFloorLimit("001010".getBytes());
////            config.setNciccCVMLimit("001500".getBytes());
////            config.setNciccTransLimit("100000".getBytes());
//        Logger.v("Config -1-" + Dump.getHexDump(config.getAid()));
//        Logger.v("Config -model-" + config.toString());
////            boolean statues = emvModule.addAID(config);
////            Logger.v("emvModule.addAID --"+statues);
////        }
//        Logger.v("Load AID End");
//        return config;
//    }
//
//    public static String loadAIDS(TMSAIDdataModelEntity aidData) {
//        DeviceSpecificModel deviceSpecificModel1 = AppManager.getInstance().getDeviceSpecificModel();
//        String floorLimit = AppManager.getInstance().getFloorLimit(Utils.fetchIndicatorFromAID(aidData.getAid()));
//        boolean enabled = AppManager.getInstance().isFllorLimitEnabled(Utils.fetchIndicatorFromAID(aidData.getAid()));
//        Logger.v("AID -- "+aidData.getAid());
//        Logger.v("Floor Limit - "+floorLimit+"--"+enabled);
//        String limit = "00000000";
//        if (enabled && floorLimit.trim().length() != 0) {
//            limit = "00000000" + Integer.toHexString(Integer.parseInt(floorLimit));
//        }
//        limit = (limit).substring(limit.length() - 8);
//
//            String dataSet = "";
//        dataSet = "9F06"+addLength(aidData.getAid())
//                +"DF010101"+"9F08020010"
//                +"DF11"+addLength(aidData.getDefaultActionCode())
//                +"DF12"+addLength(aidData.getOnlineActionCode())
//                +"DF13"+addLength(aidData.getDenialActionCode())
//                +"9F1B"+addLength(limit)
//                +"DF150400000000"
//                +"DF16"+addLength(aidData.getMaxTargetPercentage())
//                +"DF17"+addLength(aidData.getTargetPercentage())
//                +"DF14"+addLength(aidData.getDefaultDDOL())
//                +"DF180101"+"DF460101"+"9F660436004080"+"DF2006999999999999";
//        if (aidData.getAid().equalsIgnoreCase(Constant.A0000000041010)) {
//            dataSet = dataSet
//                    + "DF1906"+appendZeros(deviceSpecificModel1.getTerminalContactlessFloorLimit2(),12)
//                    +"DF2106"+appendZeros(deviceSpecificModel1.getTerminalCVMRequiredLimit2(),12);
////                    +"DF2006"+ appendZeros(deviceSpecificModel1.getTerminalContactlessTransactionLimit2(),12);
//        } else if (aidData.getAid().equalsIgnoreCase(Constant.A0000000031010) || aidData.getAid().equalsIgnoreCase(Constant.A0000000032010)) {
//            dataSet = dataSet
//                    + "DF1906"+appendZeros(deviceSpecificModel1.getTerminalContactlessFloorLimit1(),12)
//                    +"DF2106"+appendZeros(deviceSpecificModel1.getTerminalCVMRequiredLimit1(),12);
////                    +"DF2006"+ appendZeros(deviceSpecificModel1.getTerminalContactlessTransactionLimit1(),12);
//        } else {
//            dataSet = dataSet
//                    + "DF1906"+appendZeros(deviceSpecificModel1.getTerminalContactlessFloorLimitMada(),12)
//                    +"DF2106"+appendZeros(deviceSpecificModel1.getTerminalCVMRequiredLimitMada(),12);
////                    +"DF2006"+ appendZeros(deviceSpecificModel1.getTerminalContactlessTransactionLimitMada(),12);
//        }
//        Logger.v("dataSet ---"+dataSet);
//        return dataSet;
//    }
//
//    private static String appendZeros(String value,int size) {
//        value = "0000000000000"+value;
//        return value.substring(value.length() - size);
//    }
//
//    private static String addLength(String value) {
//        if(value.trim().length() == 0)
//            return "0100";
//        int length = 2;
//        if(1 < value.trim().length())
//            length = value.trim().length();
//        if(value.trim().length() % 2 != 0)
//            value = "0"+value;
//        return String.format("%02d",(length/2))+value;
//    }
//
//    public static void loadAID(EmvModule emvModule) {
//        boolean addResult;
//        String aid1 = "9F0607A0000002281010DF0101009F08020020DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06001000100000DF1906000000000010DF2006000000001000DF2106001000100000";
//        addResult = emvModule.addAIDWithDataSource(ISOUtils.hex2byte(aid1));
//        Logger.v("AddRexult -"+addResult);
//        String aid2 = "9F0607A0000002282010DF0101009F08020020DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06001000100000DF1906000000000010DF2006000000001000DF2106001000100000";
//        addResult = emvModule.addAIDWithDataSource(ISOUtils.hex2byte(aid2));
//        Logger.v("AddRexult -"+addResult);
//        String aid3 = "9F0607A0000000041010DF0101009F08020020DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06001000100000DF1906000000000010DF2006000000001000DF2106001000100000";
//        addResult = emvModule.addAIDWithDataSource(ISOUtils.hex2byte(aid3));
//        Logger.v("AddRexult -"+addResult);
//        String aid4 = "9F0607A0000000043060DF0101009F08020020DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06001000100000DF1906000000000010DF2006000000001000DF2106001000100000";
//        addResult = emvModule.addAIDWithDataSource(ISOUtils.hex2byte(aid4));
//        Logger.v("AddRexult -"+addResult);
//
//        String aid5 = "9F0608A000000333010101DF0101009F08020020DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06001000100000DF1906000000000010DF2006000000001000DF2106001000100000";
//        addResult = emvModule.addAIDWithDataSource(ISOUtils.hex2byte(aid5));
//        Logger.v("AddRexult -"+addResult);
//        String aid6 = "9F0608A000000333010102DF0101009F08020020DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06001000100000DF1906000000000010DF2006000000001000DF2106001000100000";
//        addResult = emvModule.addAIDWithDataSource(ISOUtils.hex2byte(aid6));
//        Logger.v("AddRexult -"+addResult);
//        String aid7 = "9F0608A000000333010103DF0101009F08020020DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06001000100000DF1906000000000010DF2006000000001000DF2106001000100000";
//        addResult = emvModule.addAIDWithDataSource(ISOUtils.hex2byte(aid7));
//        Logger.v("AddRexult -"+addResult);
//        String aid8 = "9F0608A000000333010106DF0101009F08020020DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06001000100000DF1906000000000010DF2006000000001000DF2106001000100000";
//        addResult = emvModule.addAIDWithDataSource(ISOUtils.hex2byte(aid8));
//        Logger.v("AddRexult -"+addResult);
//
//    }
//
//    public static void showMessage(String s, int data) {
//        Logger.v("response", "AID --" + s + " -- " + data);
//    }
//
//    public void loadCAPKeys() {
//
////        boolean addResult1 = false;
////        String capk1 = "A523924AFD826DAD39BC4532CB121C19A702D2B0D3F29CE79E2CBD0F847BC112A5FF61EF0E3913A6DF63A3E8017FC2B19F0E61304889A88E406DAC0FF82A423052E5387EF6C073D2B8C6004D2D4077C5179A78902CE4A8F361A85C6F46D56A75F374AF7AAD0F8409098AC1F388517184001AA316D05C842907BF0D62F8A05E083DBC8FED48FF84108D1C411C5540604408C42066E6B2ED465BC0DCBBB06383EE88C1CF0A7F694317C8B3A8EF1019059B";
////        addResult1 = emvModule.addCAPublicKeyWithDataSource(ISOUtils.hex2byte(capk1));
////        Logger.v("response", ("Add public key,[rid:A000000333; index:01]:" + (addResult1 == true ? context.getString(R.string.msg_common_succ) : context.getString(R.string.msg_common_failed))+" -----"+ MessageTag.DATA+" --200"));
//
////        boolean addResult2 = false;
////        String capk2 = "863B43586D710D2ECCF922644ACDD7015057A26BE7D6999A65D023DE94CD81A171E93C5BAB92C753767A4720C2ACFBF358387790CCD437806F9C1F19CF66FCF20BF42570FFE21ED742608F56C9CB0B4F277CF8EF3394C8BC595B314044197B7AAEAADBF1E44D763CDFE3DF368CBA6B09788F8EAEF9B47DEC02BEA131D58551430621D71AA5EEDE29FC1AC8CBE44CE92177E01EEBC080E94BEAD5FA0CAEF4B487";
////        addResult2 = emvModule.addCAPublicKeyWithDataSource(ISOUtils.hex2byte(capk2));
////        Logger.v("response", ("Add public key,[rid:A000000333; index:02]:" + (addResult2 == true ? context.getString(R.string.msg_common_succ) : context.getString(R.string.msg_common_failed)) + " -----" + MessageTag.DATA + " --201"));
//
////        String capk = "9F0605A000000333" +
////                "9F220101" +
////                "DF05083230303931323331" +
////                "DF060101" +
////                "DF070101" +
////                "DF028180" +
////                "BBE9066D2517511D239C7BFA77884144AE20C7372F515147E8CE6537C54C0A6A4D45F8CA4D290870CDA59F1344EF71D17D3F35D92F3F06778D0D511EC2A7DC4FFEADF4FB1253CE37A7B2B5A3741227BEF72524DA7A2B7B1CB426BEE27BC513B0CB11AB99BC1BC61DF5AC6CC4D831D0848788CD74F6D543AD37C5A2B4C5D5A93B" +
////                "DF040103" +
////                "DF0314" +
////                "E881E390675D44C2DD81234DCE29C3F5AB2297A0";
////        boolean addResult = emvModule.addCAPublicKeyWithDataSource(ISOUtils.hex2byte(capk));
////        Log.v("response", ("Add public key,[rid:00000; index:03]:" + (addResult == true ? context.getString(R.string.msg_common_succ) : context.getString(R.string.msg_common_failed)) + "--" + MessageTag.DATA + " -- " + 125));
////        String capk1 = "9F0605A000000228" +
////                "9F220199" +
////                "DF05083230303931323331" +
////                "DF060101" +
////                "DF070101" +
////                "DF028180" +
////                "AB79FCC9520896967E776E64444E5DCDD6E13611874F3985722520425295EEA4BD0C2781DE7F31CD3D041F565F747306EED62954B17EDABA3A6C5B85A1DE1BEB9A34141AF38FCF8279C9DEA0D5A6710D08DB4124F041945587E20359BAB47B7575AD94262D4B25F264AF33DEDCF28E09615E937DE32EDC03C54445FE7E382777" +
////                "DF040103" +
////                "DF0314" +
////                "4ABFFD6B1C51212D05552E431C5B17007D2F5E6D";
////        boolean addResult1 = emvModule.addCAPublicKeyWithDataSource(ISOUtils.hex2byte(capk1));
////        Log.v("response", ("Add public key,[rid:00000; index:03]:" + (addResult1 == true ? context.getString(R.string.msg_common_succ) : context.getString(R.string.msg_common_failed)) + "--" + MessageTag.DATA + " -- " + 125));
//
//
//        loadCapKeys("A000000228", 18, "A523924AFD826DAD39BC4532CB121C19A702D2B0D3F29CE79E2CBD0F847BC112A5FF61EF0E3913A6DF63A3E8017FC2B19F0E61304889A88E406DAC0FF82A423052E5387EF6C073D2B8C6004D2D4077C5179A78902CE4A8F361A85C6F46D56A75F374AF7AAD0F8409098AC1F388517184001AA316D05C842907BF0D62F8A05E083DBC8FED48FF84108D1C411C5540604408C42066E6B2ED465BC0DCBBB06383EE88C1CF0A7F694317C8B3A8EF1019059B", "9FE167D85CB9A9EF79FCA0B2CAB09C764850B93C", "221231");
//        loadCapKeys("", 20, "EBA2E3FBE75D51C519B7A498CFE53F51519B292C1BBE0C78C14FBE38E3717DE0C0ECC04605879EF617B97ED1E8E989FCD2C7DDF61EE09E96F7EADA7D9F553426D6D6A8BE4DCF943D6C8F3627265520F757BBA16FF68749F3D796A0AAACA0ED0929BE112BF7CAB87BED4D9B5DD9B7A0CAC7F9CA513A6BFC03B4C20EFDC03E2B58D76E2ABF466665CB9D64AA412FBBF85259C480DA2F0896AB28FBB26022EFAE74CCDB9C36749E8D29AE4069A1298A0B07A7F72DFF8E6F442A2393DFF7E4E1F06F", "48AE1E709DBBBDC9CEED5B5FEEE233CD1248CA70", "241231");
//        loadCapKeys("", 21, "863B43586D710D2ECCF922644ACDD7015057A26BE7D6999A65D023DE94CD81A171E93C5BAB92C753767A4720C2ACFBF358387790CCD437806F9C1F19CF66FCF20BF42570FFE21ED742608F56C9CB0B4F277CF8EF3394C8BC595B314044197B7AAEAADBF1E44D763CDFE3DF368CBA6B09788F8EAEF9B47DEC02BEA131D58551430621D71AA5EEDE29FC1AC8CBE44CE92177E01EEBC080E94BEAD5FA0CAEF4B487", "64D1FDA19D5E0893A6A7A31987805BB9A3D19B8B", "201231");
//        loadCapKeys("", 22, "9CC38384539AFFDB955ADF9FCF03A6A669B5F68D91DBE1562002D4617E75FF0BEF16731D8CAE5B6E690E00C0F106BD8EB127DAD03108D40576C95572B3C43E4A9641C11C4C5AED06643543CD02B6E3811FBC4F72956CA9D8641374CFB659263B8B22B5C5A3B624E99BF5CBFD34B99A069DA312B1F7C03CF8F4CFA91BB269DBD565073A9AFCB1DA7D839F13D43B57F924BB85E1BCCE28BE5A8EC03AA56FED231B13B920A3BA4227F53F927EC27F9DB20C32EB2AD8F9C0770EC97D930764E844C9", "E924B09D66B00B7811678092DF8F95D460F4CC47", "241231");
//        loadCapKeys("", 23, "EEEACE9FD905E43DD1EC43F471570F9255379E43813267F0C6C0363977C607E8E169B834A5072977ADA9BDF4D0F50748F3D2DED1F863A9A510C4D67BC923EB53E77711BB079B32F2837F1381F141B27B9361E67DDB5AEF107F05231042A9D003DA49338476FBA2E8FFD8D48621C830A6BAB87751570BEAB77AA501846E8F9EDE25EAD306C45AA21CEEB506E5256AADB01AAA0A5C5773DB7A75DBFB5D1EA30C89BFCB4937C0B1B6EDADFF12F9808F1E9129A39AC6996C7D9E551BD1AF924320D965BC0726AD9F9CE430415F1FDF9AC37C3DC0454452D73F0E0B1CBC8214522F5F", "6DE12BDA2B42F9950578C2C50A94A216636D6045", "251231");
//        loadCapKeys("", 28, "C05B993401063615EF211036DD8154066BE4BBEC7E93A82E83C65E2CFD76E498A6DBF6135C816F606B9564A30D259A9FD5463AA78261223FBB4718EAD4A17347E11BE475BF0DDD9BE9315DCF585D58863A7BCA0E67440586DA098E33047C0DF6F6A1D1BD081BF283321DDF248FDFFA9FB749D0FDA47ADE2E7C0AAA76B146A00A5EBDA270C52832E8132FBC631EAC1120F02215829EB1D852B1969F1C1504A659AB6057C92AF92D981C8171B68E3300E3", "F1B99C16FF415746FF423241E66F17AF0235B2C2", "171231");
//        loadCapKeys("", 29, "B6D73BF68564C88A1AEE8BA70A5F60CE495CA722E097DADEEBB83B28040B1BAD16DBC9AC3CD181BA89193638E600AF397D220F0339A8E792AA08C1878482ACC463B3B3A257AE8667CDBC1D6613CB9CBB612830FDA7F7BA689A148EFFF34476F6E0A70C819C10B3B6150909B58BF9403F5BB2E9790EE82C50C8D6FB267C726DC255AE97FABF5A357B2A0FBD1387168D83B25ECD912027B3868F072E025240CF780CC8E5839823727E5547FD1366A203F4F70FA82660B8401D4D2D06FD9A4036D14C53F6289D6FDC724E7D06F31ED93AC1B54083D9B9FCF09B135FDE9F4F6C1F0BA0142C3715E49015958C45315859DB12D942D75497FB51ED", "08374162F808F8DAD0CECB8FCD1AF5F64F213D6F", "241231");
//        loadCapKeys("", 88, "D6589C42073CD94C3C0008098DE209DA46AC0882AB5A0174AACA64A0E8BF2171098FF8A14470B2C0E76B792DBDA2D7AD27416E8D85190C151BF4DC19F51857A85387BA475D66682EAA954139BD1E7E37CF5F35F37DAAEC25B64722E9D68EDC266B4689F05F74D30370CC7C9F2A4B103B9FCDCD524519E9D80AF49E370116CB2287ABF001B5BCC9A34526EBAFD2E88DB752DFE24FE3DF1AFF71733D457B7899E7EB07DC8812DED19CA06D5BE14EC6A9D5", "36FECD5E5D31CA9D93B91D1B1BB34A150D2834BA", "251231");
//        loadCapKeys("", 99, "AB79FCC9520896967E776E64444E5DCDD6E13611874F3985722520425295EEA4BD0C2781DE7F31CD3D041F565F747306EED62954B17EDABA3A6C5B85A1DE1BEB9A34141AF38FCF8279C9DEA0D5A6710D08DB4124F041945587E20359BAB47B7575AD94262D4B25F264AF33DEDCF28E09615E937DE32EDC03C54445FE7E382777", "4ABFFD6B1C51212D05552E431C5B17007D2F5E6D", "251231");
//
//
//    }
//
//    //key index, punlic key , checksum, expiry date
//    public void loadCapKeys(String rid, int i, String module, String sha, String date) {
//        String exponent = "03";
////        String sha = "36FECD5E5D31CA9D93B91D1B1BB34A150D2834BA";
////        String module = "D6589C42073CD94C3C0008098DE209DA46AC0882AB5A0174AACA64A0E8BF2171098FF8A14470B2C0E76B792DBDA2D7AD27416E8D85190C151BF4DC19F51857A85387BA475D66682EAA954139BD1E7E37CF5F35F37DAAEC25B64722E9D68EDC266B4689F05F74D30370CC7C9F2A4B103B9FCDCD524519E9D80AF49E370116CB2287ABF001B5BCC9A34526EBAFD2E88DB752DFE24FE3DF1AFF71733D457B7899E7EB07DC8812DED19CA06D5BE14EC6A9D5";
////        CAPublicKey key = new CAPublicKey(88, 1, 1, module.getBytes(), exponent.getBytes(), sha.getBytes(), "251231");
//        CAPublicKey key1 = new CAPublicKey(i, 1, 1, ISOUtils.hex2byte(module), ISOUtils.hex2byte(exponent), ISOUtils.hex2byte(sha), date);
//
//        boolean addResult1 = emvModule.addCAPublicKey(ISOUtils.hex2byte(rid), key1);
//        Logger.v("response", ("Add public key,[rid:00000; index:-" + i + "]:" + (addResult1 == true ? context.getString(R.string.msg_common_succ) : context.getString(R.string.msg_common_failed)) + "--" + MessageTag.DATA + " -- " + 125));
//    }
//
//    public void loadCapKeys(List<TMSPublicKeyModelEntity> entity) {
////        for(int i=0;i<entity.size();i++) {
////            TMSPublicKeyModelEntity capKey = entity.get(i);
//////            CAPublicKey key1 = new CAPublicKey(i, 1, 1, ISOUtils.hex2byte(capKey.), ISOUtils.hex2byte(capKey.getExponent()), ISOUtils.hex2byte(capKey.getCheckSum()), capKey.getCaPublicKeyExpiryDate());
////            boolean addResult1 = emvModule.addCAPublicKey(rid.getBytes(), key1);
////        }
//    }
}
