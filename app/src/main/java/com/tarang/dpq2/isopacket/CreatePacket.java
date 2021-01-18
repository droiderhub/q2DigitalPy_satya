package com.tarang.dpq2.isopacket;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.work.Data;

import com.tarang.dpq2.base.AppInit;
import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.jpos_class.ByteConversionUtils;
import com.tarang.dpq2.base.jpos_class.ConstantApp;
import com.tarang.dpq2.base.jpos_class.ConstantAppValue;
import com.tarang.dpq2.base.jpos_class.MadaRequest;
import com.tarang.dpq2.base.terminal_sdk.AppConfig;
import com.tarang.dpq2.base.terminal_sdk.RSAEncrypt;
import com.tarang.dpq2.base.room_database.db.entity.TransactionModelEntity;
import com.tarang.dpq2.base.terminal_sdk.device.SDKDevice;
import com.tarang.dpq2.base.terminal_sdk.event.SimpleTransferListener;
import com.tarang.dpq2.base.terminal_sdk.q2.result.SwipeResult;
import com.tarang.dpq2.base.utilities.Utils;
import com.tarang.dpq2.model.Advice56Model;
import com.tarang.dpq2.worker.PacketDBInfoAsync;
import com.tarang.dpq2.worker.PacketDBInfoWorker;

import org.jpos.iso.ISOUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import static com.tarang.dpq2.base.terminal_sdk.AppConfig.EMV.TAG55;
import static com.tarang.dpq2.base.terminal_sdk.AppConfig.EMV.ic55Data;
import static com.tarang.dpq2.base.terminal_sdk.AppConfig.EMV.pinBlock;
import static com.tarang.dpq2.base.terminal_sdk.AppConfig.EMV.reqObj;

//Used for IOS 8583 Packet creation
public class CreatePacket implements ConstantAppValue {

    Context context;
    public boolean isMada = false;
    public boolean isGCCNet = false;
    boolean isChip;
    boolean isRepeat;
    public boolean isMagneticStripe;
    public boolean isManual;
    boolean isPinRequired;
    boolean isOnlinePin = true;
    //    private K21Pininput pinInput;
    String cardIndicator;
    boolean isFallBack;
    public String expiryDate;
    String magServiceCode;
    public boolean isOffline;
    public static String DATEFORMAT12 = "yyMMddHHmmss";
    private boolean isData55Required = false;

    private static final String RES_TPDU_HEADER = "6080c60007";
    private static final int TPDU_HEADER_LENGTH = 5;

    public boolean isFallBack() {
        return isFallBack;
    }

    public void setFallBack(boolean fallBack) {
        isFallBack = fallBack;
    }

    public void setOnlinePin(boolean onlinePin) {
        isPinRequired = true;
        isOnlinePin = onlinePin;
    }

    public void setCardScheme(String card) {
        isMada = card.equalsIgnoreCase(ConstantAppValue.A0000002282010); //P1
        cardIndicator = card;
        reqObj.setCardIndicator(card);
    }

    public void setGccCardIndicator() {
        isGCCNet = true;
    }

    public void checkBinRangers() {

    }

    public void disablePin() {
        isPinRequired = false;
    }

    public boolean getPinStatus() {
        return isPinRequired;
    }

    public CreatePacket(Context context) {
        this.context = context;
        reqObj = new MadaRequest();
    }

    public static byte[] hradcoded(int i) {
        Logger.v("Position --" + i);
        String rrn = "" + AppManager.getInstance().getRetriveRefNumber37();
        Logger.v("RRN --" + rrn);
        Logger.v("RRN --303230343236313633383536");
        Logger.v("RRN --" + ByteConversionUtils.convertStringToHex(rrn));
        if (i == 0) {
            String data = "02276000370000313130303732333030374331323843323841303531363438393331393831363634333037343830303030303030303030303030303031313130373231313333383536313030313231323030373231313633383536373130333031353131333443303031313030313530363330303530363538383834393333343839333139383136363433303734384431393132323230313537313833303130" + ByteConversionUtils.convertStringToHex(rrn) + "3138323235353838313233343536373832373131323031393135333420202030303652414A425031363832313318FFFF1814787480002C36303931393382027C009F02060000000001119F030600000000000050046D6164619F120A6D6164612044656269749F360203609F2608DC0A90183FFE3C8A9F2701809F34034403028407A00000022820109F101706010A03A4A0020F04000000000000000000007E2929349F1E0830303030303030319F350122950500C00060009F1A0206825F2A0206829A032007219F3704DA2EF8AA9C010057114893198166430748D1912220157183010F4F07A00000022820105A0848931981664307489F3303E0F8C8313136303131303232303330303030333030343030353030374E3430323634365730373935383536303930313030303030303030303031313030303030303030303132313532373232303030313331353237323530303031343032303331353135323732343135303630303035313630313031303130322411AD9EFFFFFFFF";
            return ISOUtil.hex2byte(data);
        } else {
            String data = "022760003700003131303037323330303743313238433238413035313634383933313938313636343330373438303030303030303030303030303030313131303732313133333835363130303132313230303732313136333835363731303330313531313334433030313130303135303633303035303635383838343933333438393331393831363634333037343844313931323232303135373138333031303032303533363136333835363138323235353838313233343536373832373131323031393135333420202030303652414A425031363832313318FFFF1814787480002C36303931393382027C009F02060000000001119F030600000000000050046D6164619F120A6D6164612044656269749F360203609F2608DC0A90183FFE3C8A9F2701809F34034403028407A00000022820109F101706010A03A4A0020F04000000000000000000007E2929349F1E0830303030303030319F350122950500C00060009F1A0206825F2A0206829A032007219F3704DA2EF8AA9C010057114893198166430748D1912220157183010F4F07A00000022820105A0848931981664307489F3303E0F8C8313136303131303232303330303030333030343030353030374e3233353234374530343331333437303930313030303030303030303031313030303030303030303132313532373232303030313331353237323530303031343032303331353135323732343135303630303035313630313031303130322411AD9EFFFFFFFF";
            return ISOUtil.hex2byte(data);
        }
    }

    // Create byte array from ISO Model class
    public static byte[] createISORequest() {
        Logger.v("Send Packet --" + reqObj.toString());
        String RES_TPDU_HEADER = "600" + AppManager.getInstance().getString(ConstantApp.SPRM_NII_ID) + "0000";
        byte[] isoFinalBuffer = IsoRequest.createISORequest(reqObj);
        if (AppInit.HITTING_LIVE_SERVER) {
            byte[] tpduHeader = ISOUtil.hex2byte(RES_TPDU_HEADER);
            byte[] isoBufferEchoResponsetpduHeader = ISOUtil.concat(tpduHeader, isoFinalBuffer);
            byte[] echoResponseLength = ByteConversionUtils.intToByteArray(isoBufferEchoResponsetpduHeader.length); //+TPDU_HEADER_LENGTH);
            byte[] finalData = ISOUtil.concat(echoResponseLength, isoBufferEchoResponsetpduHeader);
            String hexa = ByteConversionUtils.byteArrayToHexString(finalData, finalData.length, false);
            Logger.v("hexa -1-" + hexa);
            return finalData;
        } else {
            byte[] echoResponseLength = ByteConversionUtils.intToByteArray(isoFinalBuffer.length); //+TPDU_HEADER_LENGTH);
            byte[] finalData = ISOUtil.concat(echoResponseLength, isoFinalBuffer);
            String hexa = ByteConversionUtils.byteArrayToHexString(finalData, finalData.length, false);
            Logger.v("hexa -1-" + hexa);
            return finalData;
        }
    }

    public void addCardDetails(Data database, String transactionType, boolean isChip, boolean isRepeat) {
        Logger.v("adding_card_details_of----"+transactionType);
        this.isChip = isChip;
        this.isRepeat = isRepeat;
        //String icServiceCode = AppConfig.EMV.icServiceCode;
        //return 3 digit no ,identify either online offline or signature
        expiryDate = AppConfig.EMV.icExpiredDate;
        reqObj.setModeTransaction(getTransactionMode(isChip ? 3 : 4));
        reqObj.setPrimaryAccNo2(AppConfig.EMV.icCardNum);
        if (!checkTransactionAdvice(transactionType) || reqObj.getPosEntrymode22().trim().length() == 0) {
            reqObj.setPosEntrymode22(getPosDataCode22(isChip ? 5 : 7, transactionType)); //Long process
        }
        reqObj.setPosEntrymode22(getPosDataCode22(isChip ? 5 : 7, transactionType)); //Long process
//        if (!transactionType.equalsIgnoreCase(Constant.FALLBACK)) {
        reqObj.setCardSequenceNumber23(AppConfig.EMV.icCardSerialNum); // Need clarification
        Logger.v("ic55Data--" + ic55Data);
        reqObj.setIccCardSystemRelatedData55(ic55Data);
        reqObj.setTsi(SimpleTransferListener.tsi);
//        }//Card Sequence Number //Confusion on Xcel

        //Checking the Track2 If it has F and then removing it
        reqObj.setTrack2Data35(IsoRequest.getInstance(context).getTrack2DataValidate(AppConfig.EMV.icCardTrack2data));

        addTransactionTypeData(transactionType, database);
    }
    private boolean checkTransactionAdvice(String transactionType) {
        if (transactionType.equalsIgnoreCase(ConstantApp.AUTHORIZATION_ADVICE) || transactionType.equalsIgnoreCase(ConstantApp.FINANCIAL_ADVISE))
            return true;
        return false;
    }

    public void addCardDetails(String transactionType, SwipeResult swipResult, Data database) {
        Logger.v("addCardDetails_isMagneticStripe");
        isMagneticStripe = true;
        expiryDate = swipResult.getExpiry();
        magServiceCode = swipResult.getServiceCode();
        reqObj.setModeTransaction(getTransactionMode(2));
        reqObj.setPrimaryAccNo2(swipResult.getPan());
        reqObj.setPosEntrymode22(getPosDataCode22(2, transactionType)); //Long process
        reqObj.setTrack2Data35(IsoRequest.getInstance(context).getTrack2DataValidate(new String(swipResult.getTrack2Data())));
        addTransactionTypeData(transactionType, database);
    }
    public void addCardDetails(String transactionType, SwipeResult swipResult) {
        Logger.v("addCardDetails_isMagneticStripe_async");
        isMagneticStripe = true;
        expiryDate = swipResult.getExpiry();
        magServiceCode = swipResult.getServiceCode();
        reqObj.setModeTransaction(getTransactionMode(2));
        reqObj.setPrimaryAccNo2(swipResult.getPan());
        reqObj.setPosEntrymode22(getPosDataCode22(2, transactionType)); //Long process
        reqObj.setTrack2Data35(IsoRequest.getInstance(context).getTrack2DataValidate(new String(swipResult.getTrack2Data())));
        addTransactionTypeData(transactionType);
    }

    public void addTransactionTypeData(String transactionType, Data database) {
        Logger.v("transactiontype----" + transactionType);
        Logger.v("cardIndicator----" + cardIndicator);
        reqObj.setNameTransactionTag(transactionType);
        reqObj.setSetKernalID(AppConfig.EMV.icKernalId);
        reqObj.setMti0(IsoRequest.getInstance(context).getMTI(transactionType));
        Logger.v("mti----"+IsoRequest.getInstance(context).getMTI(transactionType));
        if (reqObj.getProcessingCode3().trim().length() == 0)
            reqObj.setProcessingCode3(IsoRequest.getInstance(context).getProcessCode(transactionType));
        reqObj.setAmtTransaction4(getAmount());
        reqObj.setTransmissionDateTime7(ByteConversionUtils.formatTranDateUTC("MMddhhmmss")); // UTC Date and time
        reqObj.setSystemTraceAuditnumber11(AppManager.getInstance().getSystemTraceAuditnumber11());  //ask need more clarification

        reqObj.setTimeLocalTransaction12(ByteConversionUtils.formatTranDate(DATEFORMAT12)); // LOCAL DATE AND TIme

        reqObj.setFunctioncode24(IsoRequest.getInstance(context).getFunctionCode(transactionType));
        reqObj.setCardAcceptorBusinessCode26(AppManager.getInstance().getMerchantCode(ConstantAppValue.A0000002282010)); //Set Value - get it from TMS
        Logger.v("DE26----"+AppManager.getInstance().getMerchantCode(cardIndicator));
        Logger.v("DE26cardindicator----"+(cardIndicator));
        reqObj.setAccuringInsituteIdCode32(AppManager.getInstance().getAccuringInsituteIdCode32()); // get from terminal registration
        if (!checkTransactionAdvice(transactionType) || reqObj.getRetriRefNo37().trim().length() == 0) {
            reqObj.setRetriRefNo37(getRetrieveRefNumber());
        }
        reqObj.setRetriRefNo37(getRetrieveRefNumber());
        reqObj.setCardAcceptorTemId41(getCardAcceptorTerminal()); //Network reg SP
        reqObj.setCardAcceptorIdCode42(getCardAcceptorTerminalCode()); //Terminal Registration
        if (Utils.checkNewAuth(transactionType) && isManual) {
            cardIndicator = "  ";
            reqObj.setAdditionalDataNational47(database.getString(PacketDBInfoWorker.CARD_SCHEME_ID) + "  "); //Scheme Terminal Dependancies
        } else
            reqObj.setAdditionalDataNational47(database.getString(PacketDBInfoWorker.CARD_SCHEME_ID) + cardIndicator); //Scheme Terminal Dependancies
        reqObj.setCurrCodeTransaction49(AppManager.getInstance().getRetailerDataModel().getTerminalCurrencyCode()); //Saudi Riyal currency code
        Logger.v("isPinRequired --" + isPinRequired);
        Logger.v("fallback --" + isFallBack());
        if (isPinRequired && isOnlinePin && (transactionPinAllowed(transactionType)))
            reqObj.setPinData52(getEncryptedPin()); // Not applicable for FINANCIAL_ADVISE // Encripted pin 8Bits
        else
            reqObj.setPinData52(null);

        reqObj.setSecRelatedContInfo53(getKsn() + "363039"); // Confusing have to check
        Logger.v("ksn_requestpacket----"+getKsn());
//        reqObj.setReservedData62("0110220323311804105007N402646W07958560901023311800011233135000122331250001323312900014181229233405150100001601010103");
//        reqObj.setReservedData62(SupportPacket.getReservedData62(context, database));
        reqObj.setReservedData62(SupportPacket.getReservedData62(context));

        switch (transactionType) {
            case ConstantApp.PURCHASE:
                purchaseTransaction(database);
                break;
            case ConstantApp.PURCHASE_NAQD:
                purchaseCashBack();
                break;
            case ConstantApp.PURCHASE_REVERSAL:
                purchaseReversal();
                break;
            case ConstantApp.CASH_ADVANCE:
                cashAdvance();
                break;
            case ConstantApp.REFUND:
                refund(database);
                break;
            case ConstantApp.PRE_AUTHORISATION:
                preAuthorization();
                break;
            case ConstantApp.PRE_AUTHORISATION_VOID:
                preAuthVoid();
                break;
            case ConstantApp.PRE_AUTHORISATION_EXTENSION:
                pareAuthExtension();
                break;
            case ConstantApp.PURCHASE_ADVICE_FULL:
                purchaseAdvice(transactionType);
                break;
            case ConstantApp.PURCHASE_ADVICE_PARTIAL:
                purchaseAdvice(transactionType);
                break;
            case ConstantApp.PURCHASE_ADVICE_MANUAL:
                purchaseAdvice(transactionType);
                break;
            case ConstantApp.FALLBACK:
                fallBack(transactionType);
                break;
            case ConstantApp.AUTHORIZATION_ADVICE:
                authorizationAdvice();
                break;
            case ConstantApp.FINANCIAL_ADVISE:
                financialAdvice();
                break;
            default:
                break;
        }
        if (isFallBack())
            fallBack(transactionType);

//        if (database != null)
//            checkOffLine(transactionType, database);

        try {
            if (isData55Required && !(cardIndicator.equalsIgnoreCase(ConstantAppValue.A0000002281010))) { //reqObj.getMti0().equalsIgnoreCase(ConstantValue.FINANCIAL)
                int[] localTag = SimpleTransferListener.L_55TAGS;
                String[] myData = {"9f26", "9F10", "9F27", "9F37", "9F36", "95", "9A", "9C", "9F02", "5F2A",
                        "82", "9F1A", "9F03", "9F33", "9F34", "9F35", "9F1E", "84", "50", "4f", "9f12"
                        , "DF01", "8a", "5A", "57", "9F6C", "9F6E", "9F24", "9F66", "9F19", "9F25"};
                HashMap<String, String> tag55 = Utils.getParsedTag55(ic55Data);

                //  TLVPackage tlv1 = ISOUtils.newTlvPackage();
//                tlv1.unpack(ISOUtil.hex2byte(ic55Data));
                String myLocalData55 = "";
                for (int i = 0; i < localTag.length; i++) {
                    if ((localTag[i] != 0x9F6C) && (localTag[i] != 0x9F66)) {
//                        String myTagVal = tlv1.getString(localTag[i]);
                        String myTagVal = tag55.get(localTag[i]);
                        if (myTagVal != null && myTagVal.trim().length() != 0) {
                            Logger.v("Length --" + myTagVal.length());
                            int length = (myTagVal.length() / 2);
                            String finalInt = Integer.toHexString(length);
                            if (finalInt.trim().length() == 1) {
                                finalInt = "0" + finalInt;
                            }
                            Logger.v("Length --" + finalInt);
                            Logger.v("TAG --" + myData[i]);
                            Logger.v("Val --" + myTagVal);
                            myLocalData55 = myLocalData55 + myData[i] + finalInt + myTagVal;
                        }
                    }
                }
                Logger.v("MyLocal Data --" + myLocalData55);
                myLocalData55 = myLocalData55.replaceAll(" ", "");
                Logger.v("MyLocal Data --" + myLocalData55);
                reqObj.setIccCardSystemRelatedData55(myLocalData55);
                isData55Required = false;
            }
//            else
//                removeTag55();
        } catch (Exception e) {
            Logger.v("Eception 333");
        }
        if(AppManager.getInstance().isDebugEnabled()){
            reqObj.setMessageAuthenticationCodeField64(ISOUtil.hex2byte("0000000000000000"));

        }else {
            reqObj.setMessageAuthenticationCodeField64(ISOUtil.hex2byte("0000000000000000"));
            reqObj.setMessageAuthenticationCodeField64(IsoRequest.getInstance(context).getMAC(reqObj));
        }
//        SDKDevice.getInstance(context).resetOpen();

    }

    public void addCardDetails(Data database, String transactionType, String accNo, String expiryDate) {
        Logger.v("addCardDetails_isManual");
        isManual = true;
        isPinRequired = false;
        this.expiryDate = expiryDate;
        reqObj.setModeTransaction(getTransactionMode(1));
        reqObj.setPrimaryAccNo2(accNo);
        reqObj.setDateExpiration14(expiryDate);
        reqObj.setPosEntrymode22(getPosDataCode22(6, transactionType)); //Long process
        addTransactionTypeData(transactionType);
    }

    public void addTransactionTypeData(String transactionType) {
        Logger.v("addTransactionTypeData_msr_async");
        Logger.v("transactiontype----" + transactionType);
        Logger.v("cardIndicator----" + cardIndicator);
        reqObj.setNameTransactionTag(transactionType);
        reqObj.setSetKernalID(AppConfig.EMV.icKernalId);
        reqObj.setMti0(IsoRequest.getInstance(context).getMTI(transactionType));
        Logger.v("mti----"+IsoRequest.getInstance(context).getMTI(transactionType));
        if (reqObj.getProcessingCode3().trim().length() == 0)
            reqObj.setProcessingCode3(IsoRequest.getInstance(context).getProcessCode(transactionType));
        reqObj.setAmtTransaction4(getAmount());
        reqObj.setTransmissionDateTime7(ByteConversionUtils.formatTranDateUTC("MMddhhmmss")); // UTC Date and time
        reqObj.setSystemTraceAuditnumber11(AppManager.getInstance().getSystemTraceAuditnumber11());  //ask need more clarification

        reqObj.setTimeLocalTransaction12(ByteConversionUtils.formatTranDate(DATEFORMAT12)); // LOCAL DATE AND TIme

        reqObj.setFunctioncode24(IsoRequest.getInstance(context).getFunctionCode(transactionType));
        reqObj.setCardAcceptorBusinessCode26(AppManager.getInstance().getMerchantCode(ConstantAppValue.A0000002282010)); //Set Value - get it from TMS
        Logger.v("DE26----"+AppManager.getInstance().getMerchantCode(cardIndicator));
        Logger.v("DE26cardindicator----"+(cardIndicator));
        reqObj.setAccuringInsituteIdCode32(AppManager.getInstance().getAccuringInsituteIdCode32()); // get from terminal registration
        if (!checkTransactionAdvice(transactionType) || reqObj.getRetriRefNo37().trim().length() == 0) {
            reqObj.setRetriRefNo37(getRetrieveRefNumber());
        }
        reqObj.setRetriRefNo37(getRetrieveRefNumber());
        reqObj.setCardAcceptorTemId41(getCardAcceptorTerminal()); //Network reg SP
        reqObj.setCardAcceptorIdCode42(getCardAcceptorTerminalCode()); //Terminal Registration
        if (Utils.checkNewAuth(transactionType) && isManual) {
            cardIndicator = "  ";
            reqObj.setAdditionalDataNational47((PacketDBInfoAsync.CARD_SCHEME_ID) + "  "); //Scheme Terminal Dependancies
        } else
            reqObj.setAdditionalDataNational47((PacketDBInfoAsync.CARD_SCHEME_ID) + cardIndicator); //Scheme Terminal Dependancies
        reqObj.setCurrCodeTransaction49(AppManager.getInstance().getRetailerDataModel().getTerminalCurrencyCode()); //Saudi Riyal currency code
        Logger.v("isPinRequired --" + isPinRequired);
        Logger.v("fallback --" + isFallBack());
        if (isPinRequired && isOnlinePin && (transactionPinAllowed(transactionType)))
            reqObj.setPinData52(getEncryptedPin()); // Not applicable for FINANCIAL_ADVISE // Encripted pin 8Bits
        else
            reqObj.setPinData52(null);

        reqObj.setSecRelatedContInfo53(getKsn() + "363039"); // Confusing have to check
        Logger.v("ksn_requestpacket----"+getKsn());
//        reqObj.setReservedData62("0110220323311804105007N402646W07958560901023311800011233135000122331250001323312900014181229233405150100001601010103");
//        reqObj.setReservedData62(SupportPacket.getReservedData62(context, database));
        reqObj.setReservedData62(SupportPacket.getReservedData62(context));

        switch (transactionType) {
            case ConstantApp.PURCHASE:
                purchaseTransaction();
                break;
            case ConstantApp.PURCHASE_NAQD:
                purchaseCashBack();
                break;
            case ConstantApp.PURCHASE_REVERSAL:
                purchaseReversal();
                break;
            case ConstantApp.CASH_ADVANCE:
                cashAdvance();
                break;
            case ConstantApp.REFUND:
                refund();
                break;
            case ConstantApp.PRE_AUTHORISATION:
                preAuthorization();
                break;
            case ConstantApp.PRE_AUTHORISATION_VOID:
                preAuthVoid();
                break;
            case ConstantApp.PRE_AUTHORISATION_EXTENSION:
                pareAuthExtension();
                break;
            case ConstantApp.PURCHASE_ADVICE_FULL:
                purchaseAdvice(transactionType);
                break;
            case ConstantApp.PURCHASE_ADVICE_PARTIAL:
                purchaseAdvice(transactionType);
                break;
            case ConstantApp.PURCHASE_ADVICE_MANUAL:
                purchaseAdvice(transactionType);
                break;
            case ConstantApp.FALLBACK:
                fallBack(transactionType);
                break;
            case ConstantApp.AUTHORIZATION_ADVICE:
                authorizationAdvice();
                break;
            case ConstantApp.FINANCIAL_ADVISE:
                financialAdvice();
                break;
            default:
                break;
        }
        if (isFallBack())
            fallBack(transactionType);

//        if (database != null)
//            checkOffLine(transactionType, database);

        try {
            if (isData55Required && !(cardIndicator.equalsIgnoreCase(ConstantAppValue.A0000002281010))) { //reqObj.getMti0().equalsIgnoreCase(ConstantValue.FINANCIAL)
                int[] localTag = SimpleTransferListener.L_55TAGS;
                String[] myData = {"9f26", "9F10", "9F27", "9F37", "9F36", "95", "9A", "9C", "9F02", "5F2A",
                        "82", "9F1A", "9F03", "9F33", "9F34", "9F35", "9F1E", "84", "50", "4f", "9f12"
                        , "DF01", "8a", "5A", "57", "9F6C", "9F6E", "9F24", "9F66", "9F19", "9F25"};
                HashMap<String, String> tag55 = Utils.getParsedTag55(ic55Data);

                //  TLVPackage tlv1 = ISOUtils.newTlvPackage();
//                tlv1.unpack(ISOUtil.hex2byte(ic55Data));
                String myLocalData55 = "";
                for (int i = 0; i < localTag.length; i++) {
                    if ((localTag[i] != 0x9F6C) && (localTag[i] != 0x9F66)) {
//                        String myTagVal = tlv1.getString(localTag[i]);
                        String myTagVal = tag55.get(localTag[i]);
                        if (myTagVal != null && myTagVal.trim().length() != 0) {
                            Logger.v("Length --" + myTagVal.length());
                            int length = (myTagVal.length() / 2);
                            String finalInt = Integer.toHexString(length);
                            if (finalInt.trim().length() == 1) {
                                finalInt = "0" + finalInt;
                            }
                            Logger.v("Length --" + finalInt);
                            Logger.v("TAG --" + myData[i]);
                            Logger.v("Val --" + myTagVal);
                            myLocalData55 = myLocalData55 + myData[i] + finalInt + myTagVal;
                        }
                    }
                }
                Logger.v("MyLocal Data --" + myLocalData55);
                myLocalData55 = myLocalData55.replaceAll(" ", "");
                Logger.v("MyLocal Data --" + myLocalData55);
                reqObj.setIccCardSystemRelatedData55(myLocalData55);
                isData55Required = false;
            }
//            else
//                removeTag55();
        } catch (Exception e) {
            Logger.v("Eception 333");
        }
        if(AppManager.getInstance().isDebugEnabled()){
            reqObj.setMessageAuthenticationCodeField64(ISOUtil.hex2byte("0000000000000000"));

        }else {
            reqObj.setMessageAuthenticationCodeField64(ISOUtil.hex2byte("0000000000000000"));
            reqObj.setMessageAuthenticationCodeField64(IsoRequest.getInstance(context).getMAC(reqObj));
        }
//        SDKDevice.getInstance(context).resetOpen();

    }

    private boolean transactionPinAllowed(String transactionType) {
        return !transactionType.equalsIgnoreCase(ConstantApp.FINANCIAL_ADVISE)
//                && !transactionType.equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION_VOID) && !transactionType.equalsIgnoreCase(Constant.PRE_AUTHORISATION_EXTENSION)
                && !transactionType.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_FULL) && !transactionType.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_PARTIAL) && !transactionType.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_FULL)
                && !transactionType.equalsIgnoreCase(ConstantApp.AUTHORIZATION_ADVICE) && !transactionType.equalsIgnoreCase(ConstantApp.PURCHASE_REVERSAL);
    }


//    private String checkOfflineOnlineDecline() {
//        String check = AppConfig.EMV.icApplicationCryptogram;
//        Logger.v("offlineStatus=====" + check);
//        if (check.equalsIgnoreCase(APP_CRYPTOGRAM_ARQC)) {
//            check = Constant.APP_CRYPTOGRAM_ARQC;
//        } else if (check.equalsIgnoreCase(APP_CRYPTOGRAM_TC)) {
//            check = Constant.APP_CRYPTOGRAM_TC;
//        } else if (check.equalsIgnoreCase(APP_CRYPTOGRAM_AAC)) {
//            check = Constant.APP_CRYPTOGRAM_AAC;
//        }
//        return check;
//    }

    private byte[] getEncryptedPin() {
        return pinBlock;
    }

    private String getPosDataCode22(int i, String transactionType) {
        String data;
//        if (isRepeat && AppManager.getInstance().getDe39().equalsIgnoreCase("196"))
        if (isManual)
            data = "710310" + i;
        else
            data = "710301" + i;
        if (isPinRequired && transactionPinAllowedNew(transactionType)) {
            data = data + "1" + ((isOnlinePin && transactionPinAllowed(transactionType)) ? "3" : "1");
        } else if (isMada && !isChip) {
            data = data + "03";
        } else if (isManual || !isChip) {
            data = data + "04";
        } else
            data = data + "54";
        return data + "34C";
    }
    private boolean transactionPinAllowedNew(String transactionType) {
        return !transactionType.equalsIgnoreCase(ConstantApp.FINANCIAL_ADVISE)
//                && !transactionType.equalsIgnoreCase(Constant.PRE_AUTHORISATION_VOID) && !transactionType.equalsIgnoreCase(Constant.PRE_AUTHORISATION_EXTENSION)
//                && !transactionType.equalsIgnoreCase(Constant.PURCHASE_ADVICE_FULL) && !transactionType.equalsIgnoreCase(Constant.PURCHASE_ADVICE_PARTIAL) && !transactionType.equalsIgnoreCase(Constant.PURCHASE_ADVICE_FULL)
                && !transactionType.equalsIgnoreCase(ConstantApp.AUTHORIZATION_ADVICE) && !transactionType.equalsIgnoreCase(ConstantApp.PURCHASE_REVERSAL);
    }

    private void fallBack(String transactionType) {
        if (!transactionType.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_FULL) && !transactionType.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_MANUAL) && !transactionType.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_PARTIAL))
            reqObj.setMessageReasonCode25((FALLBACK_ICCTOMSR)); // Dont set for Purchase Advice
        if (!transactionType.equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION))
            reqObj.setPinData52(pinBlock); // not for PreAuth
        if (transactionType.equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION_VOID) || transactionType.equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION_EXTENSION))
            reqObj.setResponseCode39(REFER_TO_CARD_ISSUER_VALUE);
        if (transactionType.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_FULL) || transactionType.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_MANUAL) || transactionType.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_PARTIAL)) {
            if (AppManager.getInstance().getDe38() != null)
                reqObj.setAuthIdResCode38(AppManager.getInstance().getDe38());
            if (AppManager.getInstance().getDe39() != null)
                reqObj.setResponseCode39(AppManager.getInstance().getDe39());
        }
    }

    private void financialAdvice() {
        Logger.v("FINANCIAL_ADVISE");
        reqObj.setMsgReasonCode56(getMsgReasonCode56());
        reqObj.setMti0(ConstantAppValue.FINANCIAL_ADVISE);

//        if (AppManager.getInstance().isFinancialAdviceRequired()) {
//
//        } else {
//            reqObj.setAmtTranProcessingFee30("000000000000"); //Only for PreAuth
//        }

        if (AppManager.getInstance().getDe39() != null) {
            String de39 = AppManager.getInstance().getDe39();
            if (de39.equals(ConstantApp.SUCCESS_RESPONSE_000) || de39.equals(ConstantApp.SUCCESS_RESPONSE_001) || de39.equals(ConstantApp.SUCCESS_RESPONSE_003) || de39.equals(ConstantApp.SUCCESS_RESPONSE_007)) {
                if (AppManager.getInstance().getDe38() != null) {
                    reqObj.setAuthIdResCode38(AppManager.getInstance().getDe38());
                }
            }

//            if(){
//                reqObj.setFunctioncode24(ConstantValue.AMOUNT_ACCURATE);
//                reqObj.setMessageReasonCode25(ONLINE_CARD_ACCEPTOR);
//            }else {
            reqObj.setFunctioncode24(ConstantAppValue.FINANCIAL_TRANSACTION);
            reqObj.setMessageReasonCode25(ICC_PROCESSED);
//            }
            reqObj.setResponseCode39(AppManager.getInstance().getDe39());
            Logger.v("ksn_financial_advice--"+SDKDevice.getInstance(context).getMyCurrentKSN());
            reqObj.setSecRelatedContInfo53(SDKDevice.getInstance(context).getMyCurrentKSN() + "363039"); // Confusing have to check
        }
    }

    public static String getAmount() {
        double amt = AppConfig.EMV.amountValue;
        Logger.v("AMOUNT -VALL-" + AppConfig.EMV.amountValue);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat formatter = new DecimalFormat("####.00", symbols);
        String finalAmt = formatter.format(amt).replaceAll("\\.", "");
        Logger.v("AMTT -" + finalAmt);
        long amount = (Long.parseLong(finalAmt));
        Logger.v("getAmount --" + String.format("%12d", amount));
        return "" + amount;
    }

    private String getCashBackAmount(double amt) {
        long amount = (int) (amt * 100);
        Logger.v("", "" + String.format("%012d", amount));
        return "0040682D" + String.format("%012d", amount);
    }

    private String getRetrieveRefNumber() {
        if (isRepeat)
            return "" + (Long.parseLong(reqObj.getRetriRefNo37()) + 1);
        else
            return "" + AppManager.getInstance().getRetriveRefNumber37();
    }

    private String getCardAcceptorTerminalCode() {
        return AppManager.getInstance().getCardAcceptorCode42();
    }

    private String getCardAcceptorTerminal() {
        return AppManager.getInstance().getCardAcceptorID41();
    }

/*
    private void purchaseAdvice(String transactionType) {
        if (isMada || isGCCNet || (isManual && Utils.checkNewAuth(transactionType))) {
            // MTI + de37 + date
            reqObj.setMsgReasonCode56(ConstantAppValue.AUTH + AppManager.getInstance().getDe37() + AppManager.getInstance().getPurchaseAdviceDate());

        } else {
            setDE56();
            //MTI + stan no + de7 + de12 +de32 +de33
            //set 56 for auth 1st request
            reqObj.setMsgReasonCode56(getMsgReasonCode56Refund(ConstantAppValue.AUTH));
        }
        //depends on pre auth txn
        reqObj.setMti0(FINANCIAL_ADVISE);
        reqObj.setMessageReasonCode25(TERMINAL_PROCESSED);
//        reqObj.setAmtTranProcessingFee30("000000000000");
        if (AppManager.getInstance().getDe38() != null)
            reqObj.setAuthIdResCode38(AppManager.getInstance().getDe38());
        if (AppManager.getInstance().getDe39() != null)
            reqObj.setResponseCode39(AppManager.getInstance().getDe39());
    }
*/

//TODO online changes
    private void purchaseAdvice(String transactionType) {
        //depends on pre auth txn
        reqObj.setMti0(FINANCIAL_ADVISE);
        reqObj.setMessageReasonCode25(TERMINAL_PROCESSED);
        reqObj.setAmtTranProcessingFee30("000000000000");
        if (AppManager.getInstance().getDe38() != null)
            reqObj.setAuthIdResCode38(AppManager.getInstance().getDe38());
        if (AppManager.getInstance().getDe39() != null)
            reqObj.setResponseCode39(AppManager.getInstance().getDe39());
        if (checkIBCS()) {
            reqObj.setResponseCode39(REFER_TO_000);
        }
        if(isManual){
            reqObj.setFunctioncode24(FINANCIAL_TRANSACTION);
        }
        if(isManual && Utils.checkNewAuth(transactionType)){
            setDE56();
            //MTI + stan no + de7 + de12 +de32 +de33
            //set 56 for auth 1st request
            reqObj.setMsgReasonCode56(getMsgReasonCode56Refund(ConstantAppValue.AUTH));
            reqObj.setFunctioncode24(ConstantAppValue.FINANCIAL_TRANSACTION);
        }else if (isMada || isGCCNet) {
            // MTI + de37 + date
            reqObj.setMsgReasonCode56(ConstantAppValue.AUTH + AppManager.getInstance().getDe37() + AppManager.getInstance().getPurchaseAdviceDate());
        } else {
            setDE56();
            //MTI + stan no + de7 + de12 +de32 +de33
            //set 56 for auth 1st request
            reqObj.setMsgReasonCode56(getMsgReasonCode56Refund(ConstantAppValue.AUTH));
            reqObj.setFunctioncode24(ConstantAppValue.FINANCIAL_TRANSACTION);
        }
    }

    private void pareAuthExtension() {
        Logger.v("pareAuthExtension()");
        reqObj.setAmtTranProcessingFee30("000000000000");
        reqObj.setMti0(AUTH_ADVISE);
        reqObj.setMessageReasonCode25(PRE_AUTHORIZATION_EXTENSION);
        reqObj.setResponseCode39(REFER_TO_CARD_ISSUER_VALUE);
        reqObj.setMsgReasonCode56(AUTH + AppManager.getInstance().getDe37() + AppManager.getInstance().getPurchaseAdviceDate());
        if (AppManager.getInstance().getDe38() != null)
            reqObj.setAuthIdResCode38(AppManager.getInstance().getDe38());
        if (checkIBCS()) {
            reqObj.setResponseCode39(REFER_TO_000);
        }
    }
    private boolean checkIBCS() {
        if (cardIndicator.equalsIgnoreCase(A0000000031010)
                || cardIndicator.equalsIgnoreCase(A0000000041010)
                || cardIndicator.equalsIgnoreCase(A00000002501)
                || cardIndicator.equalsIgnoreCase(A000000GN)
                || cardIndicator.trim().length() ==0)
            return true;
        return false;
    }



    private void preAuthVoid() {
        Logger.v("preAuthVoid");
        reqObj.setAmtTranProcessingFee30("000000000000");
        reqObj.setMti0(AUTH_ADVISE);
        reqObj.setMessageReasonCode25(PRE_AUTHORIZATION_VOID);
        reqObj.setMsgReasonCode56(AUTH + AppManager.getInstance().getDe37() + AppManager.getInstance().getPurchaseAdviceDate());
        reqObj.setResponseCode39(REFER_TO_CARD_ISSUER_VALUE);
        if (AppManager.getInstance().getDe38() != null)
            reqObj.setAuthIdResCode38(AppManager.getInstance().getDe38());

        if (checkIBCS()) {
            reqObj.setResponseCode39(REFER_TO_000);
        }
    }

    public boolean checkMada() {
        if (AppInit.VERSION_6_0_5)
            return true;
        return !isMada;
    }
    private void preAuthorization() {
        Logger.v("preAuthorization()");
        if (!isManual) {
            if (isChip && (checkMada()) && !isMagneticStripe) {
                AppManager.getInstance().setAuthorisationAdviceRequired(true);
                reqObj.setMti0((AUTH));
                reqObj.setMessageReasonCode25(ONLINE_CARD_ACCEPTOR);
                setDE56();
            } else {
                AppManager.getInstance().setAuthorisationAdviceRequired(false);
            }
            if (!isMada)
                reqObj.setFunctioncode24(AMOUNT_ACCURATE);
            if (!isMagneticStripe) //!(isMada && isChip)
                reqObj.setMessageReasonCode25(getReasonCode());
        }
    }

    private void setDE56() {
        Advice56Model advice56Model = new Advice56Model();
        advice56Model.setMti(reqObj.getMti0());
        advice56Model.setDe7(reqObj.getTransmissionDateTime7());
        advice56Model.setDe11(reqObj.getSystemTraceAuditnumber11());
        advice56Model.setDe12(reqObj.getTimeLocalTransaction12());
        advice56Model.setDe32(reqObj.getAccuringInsituteIdCode32());
        AppManager.getInstance().setAdvice56Model(advice56Model);
    }

    private void authorizationAdvice() {

        Logger.v("authadvice56----");

        if (AppManager.getInstance().getDe39() != null) {
            String de39 = AppManager.getInstance().getDe39();
            if (de39.equals(ConstantApp.SUCCESS_RESPONSE_000) || de39.equals(ConstantApp.SUCCESS_RESPONSE_001) || de39.equals(ConstantApp.SUCCESS_RESPONSE_003) || de39.equals(ConstantApp.SUCCESS_RESPONSE_007)) {
                if (AppManager.getInstance().getDe38() != null) {
                    reqObj.setAuthIdResCode38(AppManager.getInstance().getDe38());
                }
            } else {
            }

            reqObj.setMsgReasonCode56(getMsgReasonCode56());
            Logger.v("ksn_auth_advice--"+SDKDevice.getInstance(context).getMyCurrentKSN());
            reqObj.setSecRelatedContInfo53(SDKDevice.getInstance(context).getMyCurrentKSN() + "363039"); // Confusing have to check
            reqObj.setMti0(AUTH_ADVISE);
            reqObj.setProcessingCode3(PRE_AUTHORISATION);
            reqObj.setFunctioncode24(PREAUTH_INITIAL_COMPLETION);
            reqObj.setMessageReasonCode25("");
            reqObj.setResponseCode39(AppManager.getInstance().getDe39());
        }

    }

    private String getMsgReasonCode56() {
        StringBuilder authAdviceBuilder = new StringBuilder();

        if (AppManager.getInstance().getAdvice56Model() != null) {
            Advice56Model advice56Model = AppManager.getInstance().getAdvice56Model();
            authAdviceBuilder.append(advice56Model.getMti());
            Logger.v("advice56Model----" + advice56Model.getMti());
            authAdviceBuilder.append(advice56Model.getDe11());
            Logger.v("advice56Model----" + advice56Model.getDe11());
            authAdviceBuilder.append(advice56Model.getDe7());
            Logger.v("advice56Model----" + advice56Model.getDe7());
            authAdviceBuilder.append(advice56Model.getDe12());
            Logger.v("advice56Model----" + advice56Model.getDe12());
            authAdviceBuilder.append("06"); //length of de32
            authAdviceBuilder.append(advice56Model.getDe32());
            Logger.v("advice56Model----" + advice56Model.getDe32());
            authAdviceBuilder.append(advice56Model.getDe33());
            Logger.v("advice56Model----" + advice56Model.getDe33());
            Logger.v("advice56Model--V-------" + authAdviceBuilder.toString());


        }

        return authAdviceBuilder.toString();
    }

    private String getMsgReasonCode56Refund(String mti) {
        StringBuilder authAdviceBuilder = new StringBuilder();

        if (AppManager.getInstance().getAdvice56Model() != null) {
            Advice56Model advice56Model = AppManager.getInstance().getAdvice56Model();
            authAdviceBuilder.append((mti != null) ? mti : advice56Model.getMti());
            Logger.v("advice56Model----" + advice56Model.getMti());
            authAdviceBuilder.append("000000");
            Logger.v("advice56Model----" + advice56Model.getDe11());
            authAdviceBuilder.append("0000000000");
            Logger.v("advice56Model----" + advice56Model.getDe7());
            authAdviceBuilder.append("000000000000");
            Logger.v("advice56Model----" + advice56Model.getDe12());
            authAdviceBuilder.append("06"); //length of de32
            authAdviceBuilder.append(advice56Model.getDe32());
            Logger.v("advice56Model----" + advice56Model.getDe32());
            authAdviceBuilder.append(advice56Model.getDe33());
            Logger.v("advice56Model----" + advice56Model.getDe33());
            Logger.v("advice56Model--V-------" + authAdviceBuilder.toString());


        }

        return authAdviceBuilder.toString();
    }

    private void refund() {
        Logger.v("refund_msr_async");
        if (!isManual) {
            //set for online
            if (isChip && checkMada() && !isMagneticStripe && !isGCCNet) {
                AppManager.getInstance().setFinancialAdviceRequired(true);
                reqObj.setMti0((AUTH));
                reqObj.setFunctioncode24(AMOUNT_ACCURATE);
                reqObj.setMessageReasonCode25(IsoRequest.getInstance(context).getMessageCode(ConstantApp.ONLINE_CARD_ACCEPTOR)); //

            } else {
                AppManager.getInstance().setFinancialAdviceRequired(false);
            }

            if (!isMagneticStripe) //!(isMada && isChip)
                reqObj.setMessageReasonCode25(getReasonCode());

            //set for offline
            if (isOffline) {
                reqObj.setMessageReasonCode25(TERMINAL_PROCESSED);
                if (AppManager.getInstance().getDe38() != null) {
                    reqObj.setAuthIdResCode38(AppManager.getInstance().getDe38());
                }
                if (AppManager.getInstance().getDe39() != null) {
                    reqObj.setResponseCode39(AppManager.getInstance().getDe39());
                }
//                reqObj.setAuthIdResCode38(database.getString(PacketDBInfoWorker.DE_38_DB));
//                reqObj.setResponseCode39(database.getString(PacketDBInfoWorker.DE_39_DB));
            } else {
                if (isGCCNet) {
                    reqObj.setAdditionalDataNational47((PacketDBInfoAsync.CARD_SCHEME_ID) + "GN"); //Scheme Terminal Dependancies
                    reqObj.setCardIndicator(A000000GN);
                }
            }
        }
        String refundMTI = AppManager.getInstance().getRefundMTI() ? ConstantAppValue.AUTH : ConstantAppValue.FINANCIAL;
        Logger.v("REFUND --" + refundMTI);
        if (AppInit.VERSION_6_0_5)
            refundMTI = ConstantAppValue.AUTH;
        if (isMada)
            reqObj.setMsgReasonCode56(refundMTI + AppManager.getInstance().getDe37() + AppManager.getInstance().getPurchaseAdviceDate());
        else {
            Logger.v("de56 --");
            setDE56();
            reqObj.setMsgReasonCode56(getMsgReasonCode56Refund(refundMTI));
        }


    }
    private void refund(Data database) {
        if (!isManual) {
            //set for online
            if (isChip && checkMada() && !isMagneticStripe && !isGCCNet) {
                AppManager.getInstance().setFinancialAdviceRequired(true);
                reqObj.setMti0((AUTH));
                reqObj.setFunctioncode24(AMOUNT_ACCURATE);
                reqObj.setMessageReasonCode25(IsoRequest.getInstance(context).getMessageCode(ConstantApp.ONLINE_CARD_ACCEPTOR)); //

            } else {
                AppManager.getInstance().setFinancialAdviceRequired(false);
            }

            if (!isMagneticStripe) //!(isMada && isChip)
                reqObj.setMessageReasonCode25(getReasonCode());

            //set for offline
            if (isOffline) {
                reqObj.setMessageReasonCode25(TERMINAL_PROCESSED);
                if (AppManager.getInstance().getDe38() != null) {
                    reqObj.setAuthIdResCode38(AppManager.getInstance().getDe38());
                }
                if (AppManager.getInstance().getDe39() != null) {
                    reqObj.setResponseCode39(AppManager.getInstance().getDe39());
                }
//                reqObj.setAuthIdResCode38(database.getString(PacketDBInfoWorker.DE_38_DB));
//                reqObj.setResponseCode39(database.getString(PacketDBInfoWorker.DE_39_DB));
            } else {
                if (isGCCNet) {
                    reqObj.setAdditionalDataNational47(database.getString(PacketDBInfoWorker.CARD_SCHEME_ID) + "GN"); //Scheme Terminal Dependancies
                    reqObj.setCardIndicator(A000000GN);
                }
            }
        }
        String refundMTI = AppManager.getInstance().getRefundMTI() ? ConstantAppValue.AUTH : ConstantAppValue.FINANCIAL;
        Logger.v("REFUND --" + refundMTI);
        if (AppInit.VERSION_6_0_5)
            refundMTI = ConstantAppValue.AUTH;
        if (isMada)
            reqObj.setMsgReasonCode56(refundMTI + AppManager.getInstance().getDe37() + AppManager.getInstance().getPurchaseAdviceDate());
        else {
            Logger.v("de56 --");
            setDE56();
            reqObj.setMsgReasonCode56(getMsgReasonCode56Refund(refundMTI));
        }


    }

    private void cashAdvance() {
        Logger.v("cashAdvance()");
        if (isChip && checkMada() && !isMagneticStripe) {
            AppManager.getInstance().setFinancialAdviceRequired(true);
            reqObj.setMti0((AUTH));
            reqObj.setFunctioncode24(AMOUNT_ACCURATE);
            reqObj.setMessageReasonCode25(ONLINE_CARD_ACCEPTOR); //
            setDE56();
        } else {
            AppManager.getInstance().setFinancialAdviceRequired(false);
        }
    }

    private void purchaseReversal() {
        if (AppManager.getInstance().getTransactionModelEntity() != null) {
            TransactionModelEntity transactionModelEntity = AppManager.getInstance().getTransactionModelEntity();
            Logger.v("transactionModelEntity --" + transactionModelEntity.toString());
            reqObj.setMti0(REVERSAL);
            reqObj.setModeTransaction(transactionModelEntity.getModeTransaction());
            reqObj.setPrimaryAccNo2(Utils.decrypt( transactionModelEntity.getPrimaryAccNo2()));
            reqObj.setProcessingCode3(transactionModelEntity.getProcessingCode3());
            reqObj.setAmtTransaction4(transactionModelEntity.getAmtTransaction4());
            reqObj.setSetKernalID(transactionModelEntity.getKernalID());
            String date = Utils.decrypt(transactionModelEntity.getDateExpiration14());
            if (date.trim().length() == 4) {
                reqObj.setDateExpiration14(date);
            } else if (date.trim().length() == 5) {
                reqObj.setDateExpiration14(date.substring(3) + date.substring(0, 2));
            }
            expiryDate = reqObj.getDateExpiration14();
            if (!reqObj.getModeTransaction().equalsIgnoreCase(ConstantAppValue.KEYED)) {
                reqObj.setDateExpiration14("");
            }
            //de7 de11
            reqObj.setCardIndicator(transactionModelEntity.getCardIndicator());
            reqObj.setTimeLocalTransaction12(transactionModelEntity.getTimeLocalTransaction12());
            reqObj.setPosEntrymode22(transactionModelEntity.getPosEntrymode22());
            reqObj.setCardSequenceNumber23(transactionModelEntity.getCardSequenceNumber23());
            reqObj.setCardAcceptorBusinessCode26(transactionModelEntity.getPosPinCaptureCode26());
            reqObj.setAccuringInsituteIdCode32(transactionModelEntity.getAccuringInsituteIdCode32());
            reqObj.setTrack2Data35(Utils.decrypt(transactionModelEntity.getTrack2Data35()));
            reqObj.setRetriRefNo37(transactionModelEntity.getRetriRefNo37());
            reqObj.setCardAcceptorTemId41(transactionModelEntity.getCardAcceptorTemId41());
            reqObj.setCardAcceptorIdCode42(transactionModelEntity.getCardAcceptorIdCode42());
            reqObj.setAdditionalDataNational47(transactionModelEntity.getAdditionalDataNational47());
            reqObj.setCurrCodeTransaction49(transactionModelEntity.getCurrCodeTransaction49());

            reqObj.setAddlAmt54(transactionModelEntity.getAddlAmt54());
            if (transactionModelEntity.getIccCardSystemRelatedData55_final().trim().length() != 0) {
                reqObj.setIccCardSystemRelatedData55(Utils.decrypt( transactionModelEntity.getIccCardSystemRelatedData55_final()));
            } else {
                reqObj.setIccCardSystemRelatedData55(Utils.decrypt( transactionModelEntity.getIccCardSystemRelatedData55()));
            }

            reqObj.setMsgReasonCode56(getOrginalMTI(transactionModelEntity.getMti0())
                    + checkAddZeros(transactionModelEntity.getSystemTraceAuditnumber11(), 6) + checkAddZeros(transactionModelEntity.getTransmissionDateTime7(), 10)
                    + checkAddZeros(transactionModelEntity.getTimeLocalTransaction12(), 12) + "06" + transactionModelEntity.getAccuringInsituteIdCode32() + "00");
            Logger.v("setMsgReasonCode56 --" + reqObj.getMsgReasonCode56());
            if (AppManager.getInstance().isReversalManual()) {
                reqObj.setMessageReasonCode25(CUSTOMER_CANCELLATION);
                reqObj.setAuthIdResCode38(transactionModelEntity.getAuthIdResCode38());
            } else if (AppManager.getInstance().isAdminNotificationReversal()) {
                reqObj.setMessageReasonCode25(MAC_FAILURE);
                reqObj.setAuthIdResCode38(transactionModelEntity.getAuthIdResCode38());
            } else {
                reqObj.setMessageReasonCode25(TIMEOUT_WAITING_RESP);
            }
            AppManager.getInstance().setReversalTime(transactionModelEntity.getUid());
        } else
            Logger.v("Pruchase reversal else");
    }
    private String getOrginalMTI(String mti0) {
        switch (mti0) {
            case FINANCIAL:
                return FINANCIAL;
            case AUTH_ADVISE:
                return AUTH_ADVISE;
            case AUTH_ADVISE_REPEAT:
                return AUTH_ADVISE_REPEAT;
            case FINANCIAL_ADVISE:
                return FINANCIAL_ADVISE;
            case FINANCIAL_ADVISE_REPEAT:
                return FINANCIAL_ADVISE_REPEAT;
        }
        return AUTH;
    }

    private String checkAddZeros(String value, int size) {
        if (value != null && value.trim().length() != 0 && value.trim().length() == size)
            return value;
        String zeros = "";
        for (int i = 0; i < size; i++)
            zeros = zeros + "0";
        return zeros;
    }

    private void purchaseCashBack() {
//        reqObj.setRetriRefNo37(getRetrieveRefNumber()); //
        reqObj.setAddlAmt54(getCashBackAmount(AppConfig.EMV.amtCashBack));
//            reqObj.setReservedData62("0110220323311804105007N402646W07958560901023311800011233135000122331250001323312900014181229233405150100001601010103");
        if (AppInit.VERSION_6_0_5) {
            if (!isManual) {
//        if (checkOfflineOnlineDecline().equalsIgnoreCase(Constant.APP_CRYPTOGRAM_ARQC)){
                //go online
                if (isChip && !isMagneticStripe && !isGCCNet) { //TODO: ISO 4.0.5
                    AppManager.getInstance().setFinancialAdviceRequired(true);
                    setDE56();
                    reqObj.setMti0(AUTH);
                    reqObj.setFunctioncode24(AMOUNT_ACCURATE);
                    Logger.v("setFinancialAdviceRequired...." + AppManager.getInstance().isFinancialAdviceRequired());

                } else {
                    AppManager.getInstance().setFinancialAdviceRequired(false);
                    Logger.v("setFinancialAdviceRequired...." + AppManager.getInstance().isFinancialAdviceRequired());
                }
            }
        }
        if (!isMagneticStripe && !isManual) //!(isMada && isChip)
            reqObj.setMessageReasonCode25(getReasonCode());
    }

    private void purchaseTransaction() {
        Logger.v("purchaseTransaction_msr_async");
        if (!isManual) {
//        if (checkOfflineOnlineDecline().equalsIgnoreCase(Constant.APP_CRYPTOGRAM_ARQC)){
            //go online
            if (isChip && checkMada() && !isMagneticStripe && !isGCCNet) {
                AppManager.getInstance().setFinancialAdviceRequired(true);
                setDE56();
                reqObj.setMti0(AUTH);
                reqObj.setFunctioncode24(AMOUNT_ACCURATE);
                Logger.v("setFinancialAdviceRequired...." + AppManager.getInstance().isFinancialAdviceRequired());

            } else {
                AppManager.getInstance().setFinancialAdviceRequired(false);
                Logger.v("setFinancialAdviceRequired...." + AppManager.getInstance().isFinancialAdviceRequired());
            }

            if (!isMagneticStripe) //!(isMada && isChip)
                reqObj.setMessageReasonCode25(getReasonCode());
//        }else if (checkOfflineOnlineDecline().equalsIgnoreCase(Constant.APP_CRYPTOGRAM_TC)){

//            go offline
            if (isOffline) {
//                reqObj.setMti0(FINANCIAL_ADVISE);
                reqObj.setFunctioncode24(FINANCIAL_TRANSACTION);
                reqObj.setMessageReasonCode25(getReasonCodeOffline());
            } else {
                if (isGCCNet) {
                    reqObj.setAdditionalDataNational47((PacketDBInfoAsync.CARD_SCHEME_ID) + "GN"); //Scheme Terminal Dependancies
                    reqObj.setCardIndicator(A000000GN);
                }

            }
//        }else if (checkOfflineOnlineDecline().equalsIgnoreCase(Constant.APP_CRYPTOGRAM_AAC)){
//            decline
//        }
        }

    }

    private void purchaseTransaction(Data database) {
        if (!isManual) {
//        if (checkOfflineOnlineDecline().equalsIgnoreCase(Constant.APP_CRYPTOGRAM_ARQC)){
            //go online
            if (isChip && checkMada() && !isMagneticStripe && !isGCCNet) {
                AppManager.getInstance().setFinancialAdviceRequired(true);
                setDE56();
                reqObj.setMti0(AUTH);
                reqObj.setFunctioncode24(AMOUNT_ACCURATE);
                Logger.v("setFinancialAdviceRequired...." + AppManager.getInstance().isFinancialAdviceRequired());

            } else {
                AppManager.getInstance().setFinancialAdviceRequired(false);
                Logger.v("setFinancialAdviceRequired...." + AppManager.getInstance().isFinancialAdviceRequired());
            }

            if (!isMagneticStripe) //!(isMada && isChip)
                reqObj.setMessageReasonCode25(getReasonCode());
//        }else if (checkOfflineOnlineDecline().equalsIgnoreCase(Constant.APP_CRYPTOGRAM_TC)){

//            go offline
            if (isOffline) {
//                reqObj.setMti0(FINANCIAL_ADVISE);
                reqObj.setFunctioncode24(FINANCIAL_TRANSACTION);
                reqObj.setMessageReasonCode25(getReasonCodeOffline());
            } else {
                if (isGCCNet) {
                    reqObj.setAdditionalDataNational47(database.getString(PacketDBInfoWorker.CARD_SCHEME_ID) + "GN"); //Scheme Terminal Dependancies
                    reqObj.setCardIndicator(A000000GN);
                }

            }
//        }else if (checkOfflineOnlineDecline().equalsIgnoreCase(Constant.APP_CRYPTOGRAM_AAC)){
//            decline
//        }
        }

    }

    private String getReasonCode() {
        if (!isChip) {
            //rf
            return IsoRequest.getInstance(context).getMessageCode(ConstantApp.CLESS_TXN);
        } else
            return IsoRequest.getInstance(context).getMessageCode(ConstantApp.ONLINE_CARD_ACCEPTOR);
    }

    private String getReasonCodeOffline() {
        if (!isChip) {
            //rf
            return (CLESS_TXN_ADVICE);
        } else
            return ICC_PROCESSED;
    }

    public void addFileActionDetails(String packet, Data database) {
        SDKDevice.getInstance(context).resetOpen();
        reqObj = new MadaRequest();
        reqObj.setMti0(FILEACTION);
        reqObj.setTransmissionDateTime7(ByteConversionUtils.formatTranDateUTC("MMddhhmmss"));
        reqObj.setSystemTraceAuditnumber11(AppManager.getInstance().getSystemTraceAuditnumber11());  //ask need more clarification
        reqObj.setTimeLocalTransaction12(ByteConversionUtils.formatTranDate(DATEFORMAT12));
        reqObj.setFunctioncode24(packet.substring(0, 3));

        reqObj.setAccuringInsituteIdCode32(AppManager.getInstance().getAccuringInsituteIdCode32()); // get from terminal registration

        reqObj.setCardAcceptorTemId41(getCardAcceptorTerminal()); //Network reg SP
        reqObj.setCardAcceptorIdCode42(getCardAcceptorTerminalCode()); //Terminal Registration

        reqObj.setSecRelatedContInfo53(getKsn() + "363039"); // Confusing have to check

//        reqObj.setReservedData62(SupportPacket.getReservedData62(context, database));
        reqObj.setReservedData62(SupportPacket.getReservedData62(context));

        reqObj.setDataRecord72(packet);
        reqObj.setMacExt128(IsoRequest.getInstance(context).getMAC(reqObj));
//        SDKDevice.getInstance(context).closeDevice();
    }

    public void addNetworkRegistration(Data database) {
        AppInit.lastPacket = AppInit.full_download;
        reqObj = new MadaRequest();
        reqObj.setMti0((NETWORK_MNGMT));
        reqObj.setTransmissionDateTime7(ByteConversionUtils.formatTranDateUTC("MMddhhmmss"));
        reqObj.setSystemTraceAuditnumber11(AppManager.getInstance().getSystemTraceAuditnumber11());  //ask need more clarification
        reqObj.setTimeLocalTransaction12(ByteConversionUtils.formatTranDate(DATEFORMAT12));
        reqObj.setFunctioncode24(ConstantAppValue.TERMINAL_REGISTRATION);
        reqObj.setAdditionalDataPrivate48(getAdditionalDataPrivate48());
//        Logger.e("saty48",getAdditionalDataPrivate48());
//        ReservedData62Model reservedData62Model = SupportPacket.setReservedData62Model("terminal_registration");
//        reqObj.setReservedData62(SupportPacket.getReservedData62(context, database));
        reqObj.setReservedData62(SupportPacket.getReservedData62(context));
//        reqObj.setReservedData62("0110220323311804105007N402646W07958560901023311800011233135000122331250001323312900014181229233405150100001601010103");
//        createISORequest();
    }

    private String getAdditionalDataPrivate48() {
        StringBuilder additionalDataPrivateBuilder = new StringBuilder();
        additionalDataPrivateBuilder.append(AppManager.getInstance().getVendorId());
        additionalDataPrivateBuilder.append(AppManager.getInstance().getVendorTerminalType());
        additionalDataPrivateBuilder.append(AppManager.getInstance().getVendorTerminalSerialNumber());
        additionalDataPrivateBuilder.append(AppManager.getInstance().getVendorPublickKey());
        additionalDataPrivateBuilder.append(AppManager.getInstance().getSamaKey());
        additionalDataPrivateBuilder.append(RANDOM_STRING_LENGTH_SEQUENCE_INDICATOR);
        String randomString32 = getRandomStringSequence32();
        Logger.v("randomstring" + randomString32);
//        byte[] randomString = ISOUtil.hex2byte(randomString32);
        byte[] randomString = ByteConversionUtils.hexStringToByteArray(randomString32);
        String randomStringHexa = ByteConversionUtils.byteArrayToHexString(randomString, randomString.length, false);
        Logger.v("randomstringhexa" + randomStringHexa);
        additionalDataPrivateBuilder.append(randomString32);
        additionalDataPrivateBuilder.append(MADA_SIGNATURE_LENGTH);
        Logger.v("Length -0-" + additionalDataPrivateBuilder.toString().length());
        additionalDataPrivateBuilder.append(getMadaSignature(randomStringHexa));
        Logger.v("Length -1-" + additionalDataPrivateBuilder.toString().length());

        return additionalDataPrivateBuilder.toString();
    }

    //48.7
    private String getRandomStringSequence() {
        return randomAlphaNumeric(16);
    }

    //48.7
    private String getRandomStringSequence32() {
        return randomAlphaNumeric(32);
    }

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }
    public static String newRandonString() {
        String candidateChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int length = 4;
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(candidateChars.charAt(random.nextInt(candidateChars
                    .length())));
        }
        Logger.v("randon string --" + sb.toString());
        return sb.toString();
    }

    private String getMadaSignature(String randomString) {
        StringBuilder madaSignatureBuilder = new StringBuilder();
        madaSignatureBuilder.append(MSD1); //2byt 4
        madaSignatureBuilder.append(MSD2); //106byt
        madaSignatureBuilder.append(MSD3); //1byt 2
        madaSignatureBuilder.append(MSD4); //14byt 28
        madaSignatureBuilder.append(MSD5); //1byt 2
        Logger.v("madaSignatureBuilder_till5------" + madaSignatureBuilder.length()); //124  //142
        madaSignatureBuilder.append(msd6Sha1checkSum(randomString));// SHA CHECKSUM //20byt
//        Logger.v("shachesum----" + msd6Sha1checkSum(randomString));
//        Logger.v("madaSignatureBuilderchesum--" + msd6Sha1checkSum(randomString).length());
        Logger.v("madaSignatureBuilder--" + madaSignatureBuilder.length());
        Logger.v("madaSignatureBuilderdata--" + madaSignatureBuilder.toString());
        //do RSA encription of the string builder

        //generating RSA key
//        SmModule smModule = SDKDevice.getInstance(context).getSmModule();
//        RSAKeyPair rsaKeyPair = smModule.genRSAKeyPair(1024, 0x10001);
        //rsa encryption
        //byte[] rsaMadaSignature = smModule.rsaRecover(new String(rsaKeyPair.prikey.modulus), 128, rsaKeyPair.prikey.exponent, ISOUtil.hex2byte(madaSignatureBuilder.toString()));
        byte[] rsaMadaSignature = RSAEncrypt.rsaRecover(rsaPrivateModKey, rsaPrivateExponentKey, ISOUtil.hex2byte(madaSignatureBuilder.toString()));
//        Logger.v("rsaprivkeymod-----" + (new String(rsaKeyPair.prikey.modulus)));
//        Logger.v("rsaprivkeyexp-----" + (new String(rsaKeyPair.prikey.exponent)));
//        Logger.v("rsapubkeymod-----" + (new String(rsaKeyPair.pubkey.modulus)));
//        try {
//            Logger.v("rsapubkeyexpgbk-----" + (new String(rsaKeyPair.pubkey.exponent, "gbk")));
//            Logger.v("rsapubkeyexp-----" + (new String(rsaKeyPair.pubkey.exponent)));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        Logger.v("rsapubkeyexp-----" + (ISOUtils.hexString(rsaKeyPair.pubkey.exponent)));
        String rsaResult = ByteConversionUtils.byteArrayToHexString(rsaMadaSignature);
        Logger.v("rsaResult----" + rsaResult);
        Logger.v("madasig----" + ISOUtil.hex2byte(madaSignatureBuilder.toString()));
        Logger.v("madasigtotal----" + (madaSignatureBuilder.toString()));
        Logger.v("rsaMadaSignature --" + rsaMadaSignature.length);
        Logger.v("byteArrayToString --" + ByteConversionUtils.byteArrayToString(rsaMadaSignature, rsaMadaSignature.length));
        Logger.v("byteArrayToHEXString --" + ByteConversionUtils.byteArrayToHexString(rsaMadaSignature, rsaMadaSignature.length, false).length());

        Logger.v("mada final-----" + madaSignatureBuilder.toString());
        byte[] rsaEncryData = RSAEncrypt.rsaRecover(rsaPrivateModKey, rsaPrivateExponentKey, ISOUtil.hex2byte(madaSignatureBuilder.toString()));
        if (rsaEncryData != null) {
            Logger.v("encript data -----" + ByteConversionUtils.byteArrayToHexString(rsaEncryData));
        } else
            Logger.v("RSA encript else");
        byte[] rsaDecryData = RSAEncrypt.rsaDecryptRecover(rsaPrivateModKey, rsaEncryData);
        if (rsaDecryData != null) {
            Logger.v("decript data-----" + ByteConversionUtils.byteArrayToHexString(rsaDecryData));
        } else
            Logger.v("rsaDecryData else");

        return rsaResult;
    }


    private String msd6Sha1checkSum(String randomString) {

        //setting all checksum input data
        StringBuilder sha1CheckSumBuilder = new StringBuilder();
        //sh1 ===> de07
        sha1CheckSumBuilder.append(IsoRequest.getInstance(context).getHexaString(ByteConversionUtils.formatTranDateUTC("MMddhhmmss")));
        //sh2 ===>de24
        sha1CheckSumBuilder.append((IsoRequest.getInstance(context).getHexaString(ConstantAppValue.TERMINAL_REGISTRATION)));
        //sh3 ======> vendor id
        sha1CheckSumBuilder.append(AppManager.getInstance().getVendorId());
        //sh4 =========> vendor terminal type
        sha1CheckSumBuilder.append(AppManager.getInstance().getVendorTerminalType());
        //sh5 ===> terminal serila number
        sha1CheckSumBuilder.append(AppManager.getInstance().getVendorTerminalSerialNumber());
        //sh6 =======> vendor public key index
        sha1CheckSumBuilder.append(AppManager.getInstance().getVendorPublickKey());
        //sh7 =====>random string ie 48.6
        sha1CheckSumBuilder.append(randomString);

        //generate chekcsum from check input data =====> 20bytes
        //methods from terminal sdk
//        SmModule smModule = SDKDevice.getInstance(context).getSmModule();
        Logger.v("checksum----" + ISOUtil.hex2byte(sha1CheckSumBuilder.toString()));
        Logger.v("checksumaaa----" + (sha1CheckSumBuilder.toString()));

        byte[] checkSum = getSha1(ByteConversionUtils.hexStringToByteArray(sha1CheckSumBuilder.toString()));
//        Logger.v("madaSignatureBuilder 2 --" + checkSum.length);
//        Logger.e("satyshahexa", ByteConversionUtils.byteArrayToHexString(checkSum, checkSum.length, false));
//        Logger.e("satyshahexafinal", ByteConversionUtils.byteArrayToString(checkSum, checkSum.length));

        String finalValue = ByteConversionUtils.byteArrayToHexString(checkSum, checkSum.length, false);
        Logger.v("shachesum----" + finalValue);
        Logger.v("madaSignatureBuilderchesum--" + finalValue.length());
        return finalValue;
    }

    public static byte[] getSha1(byte[] source) {
        StringBuilder sb = new StringBuilder();
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("SHA-1");
            md5.update(source);
            byte[] md = md5.digest();
            return md;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addReconciliationData(Data database) {
        SDKDevice.getInstance(context).resetOpen();
        reqObj = new MadaRequest();
        reqObj.setMti0(RECONCILIATION);
        reqObj.setPrimaryAccNo2(getCardAcceptorTerminal());
        reqObj.setTransmissionDateTime7(ByteConversionUtils.formatTranDateUTC("MMddhhmmss"));
        reqObj.setSystemTraceAuditnumber11(AppManager.getInstance().getSystemTraceAuditnumber11());  //ask need more clarification
        reqObj.setTimeLocalTransaction12(ByteConversionUtils.formatTranDate(DATEFORMAT12));
        reqObj.setFunctioncode24(ConstantAppValue.TERMINAL_RECONCILIATION);
        reqObj.setCardAcceptorBusinessCode26(AppManager.getInstance().getMerchantCode(ConstantAppValue.A0000002282010));
        Logger.v("DE26----"+AppManager.getInstance().getMerchantCode(cardIndicator));
        reqObj.setReconsilationDate28(ByteConversionUtils.formatTranDate("yyMMdd"));
        reqObj.setAccuringInsituteIdCode32(AppManager.getInstance().getAccuringInsituteIdCode32()); // get from terminal registration

        reqObj.setCardAcceptorTemId41(getCardAcceptorTerminal()); //Network reg SP
        reqObj.setCardAcceptorIdCode42(getCardAcceptorTerminalCode()); //Terminal Registration

        reqObj.setCurrCodeStatleMent50("682");
        reqObj.setSecRelatedContInfo53(getKsn() + "363039"); // Confusing have to check

//        reqObj.setReservedData62(SupportPacket.getReservedData62(context, database));
        reqObj.setReservedData62(SupportPacket.getReservedData62(context));
        reqObj.setReservedData124(database.getString(PacketDBInfoWorker.DE_124));
        reqObj.setMacExt128(IsoRequest.getInstance(context).getMAC(reqObj));
    }

    public String getKsn() {
        return SDKDevice.getInstance(context).getKSN();
    }

    private String getTransactionMode(int i) {
        switch (i) {
            case 1:
                return ConstantAppValue.KEYED;
            case 2:
                return ConstantAppValue.SWIPED;
            case 3:
                return ConstantAppValue.DIPPED;
            case 4:
                return ConstantAppValue.CONTACTLESS;
        }
        return "";
    }


    public static byte[] adminNotification(byte[] response) {
        MadaRequest reqObj = new MadaRequest();
        String responseSreing = ByteConversionUtils.byteArrayToHexString(response, response.length, false);
        String length = responseSreing.substring(0, 4);
        Logger.v("Length --" + Integer.parseInt(length, 16));
        String hexa1 = responseSreing.substring(4, (Integer.parseInt(length, 16)) * 2);
        Logger.v("Length --" + hexa1);
        reqObj.setMti0(ConstantAppValue.ADMIN_NOTIFICATION);
        reqObj.setTransmissionDateTime7(ByteConversionUtils.formatTranDateUTC("MMddhhmmss")); // UTC Date and time
        reqObj.setSystemTraceAuditnumber11(AppManager.getInstance().getSystemTraceAuditnumber11());  //ask need more clarification
        reqObj.setTimeLocalTransaction12(ByteConversionUtils.formatTranDate(DATEFORMAT12)); // LOCAL DATE AND TIme
        reqObj.setFunctioncode24(ConstantAppValue.UNABLE_PARSE_MSG);
        reqObj.setAccuringInsituteIdCode32(AppManager.getInstance().getAccuringInsituteIdCode32()); // get from terminal registration
        reqObj.setResponseCode39(ConstantAppValue.ADMIN_NOTIFICATION_RESPONSE_CODE);
        reqObj.setCardAcceptorTemId41(AppManager.getInstance().getCardAcceptorID41()); //Network reg SP
        reqObj.setCardAcceptorIdCode42(AppManager.getInstance().getCardAcceptorCode42()); //Terminal Registration
        reqObj.setDataRecord72(hexa1.substring(4));
        byte[] isoFinalBuffer = IsoRequest.createISORequest(reqObj);
        String RES_TPDU_HEADER = "600" + AppManager.getInstance().getString(ConstantApp.SPRM_NII_ID) + "0000";
        byte[] finalData;
        if (AppInit.HITTING_LIVE_SERVER) {
            byte[] tpduHeader = ISOUtil.hex2byte(RES_TPDU_HEADER);
            byte[] isoBufferEchoResponsetpduHeader = ISOUtil.concat(tpduHeader, isoFinalBuffer);
            byte[] echoResponseLength = ByteConversionUtils.intToByteArray(isoBufferEchoResponsetpduHeader.length); //+TPDU_HEADER_LENGTH);
            finalData = ISOUtil.concat(echoResponseLength, isoBufferEchoResponsetpduHeader);
            String hexa = ByteConversionUtils.byteArrayToHexString(finalData, finalData.length, false);
            Logger.v("hexa -1-" + hexa);
        } else {
            byte[] echoResponseLength = ByteConversionUtils.intToByteArray(isoFinalBuffer.length); //+TPDU_HEADER_LENGTH);
            finalData = ISOUtil.concat(echoResponseLength, isoFinalBuffer);
            String hexa = ByteConversionUtils.byteArrayToHexString(finalData, finalData.length, false);
            Logger.v("hexa -1-" + hexa);
        }
        return finalData;
    }

    public void setOffline(int isSuccess) {
        isOffline = true;
        Logger.v("setOffline");
        reqObj.setFunctioncode24(ConstantAppValue.FINANCIAL_TRANSACTION);
        reqObj.setMessageReasonCode25(ConstantAppValue.ICC_PROCESSED);
        if (isSuccess == 0) {
            reqObj.setResponseCode39(ConstantAppValue.SAF_APPROVED);
            AppManager.getInstance().setDe39(ConstantAppValue.SAF_APPROVED);
            reqObj.setAuthIdResCode38(ByteConversionUtils.generateTraceNumber(6));
        } else if (isSuccess == 1) {
            reqObj.setResponseCode39(ConstantAppValue.SAF_REJECTED);
        } else if (isSuccess == 2) {
            reqObj.setResponseCode39(ConstantAppValue.SAF_DECLINED);
        } else if (isSuccess == 3) {
            reqObj.setResponseCode39(ConstantAppValue.SAF_APPROVED_UNABLE);
            reqObj.setAuthIdResCode38(ByteConversionUtils.generateTraceNumber(6));
        }
        if (!isMagneticStripe && reqObj.getNameTransactionTag().equalsIgnoreCase(ConstantApp.PURCHASE)) //!(isMada && isChip)
            reqObj.setMessageReasonCode25(getReasonCodeOffline());
    }
}
