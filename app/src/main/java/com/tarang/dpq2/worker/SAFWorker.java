package com.tarang.dpq2.worker;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.cloudpos.pinpad.extend.PINPadExtendDevice;
import com.tarang.dpq2.R;
import com.tarang.dpq2.base.AppInit;
import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.jpos_class.ByteConversionUtils;
import com.tarang.dpq2.base.jpos_class.ConstantApp;
import com.tarang.dpq2.base.jpos_class.ConstantAppValue;
import com.tarang.dpq2.base.jpos_class.MadaRequest;
import com.tarang.dpq2.base.terminal_sdk.AppConfig;
import com.tarang.dpq2.base.room_database.db.AppDatabase;
import com.tarang.dpq2.base.room_database.db.entity.TransactionModelEntity;
import com.tarang.dpq2.base.terminal_sdk.device.SDKDevice;
import com.tarang.dpq2.base.utilities.CountDownResponseTimer;
import com.tarang.dpq2.base.utilities.Utils;
import com.tarang.dpq2.isopacket.CreatePacket;
import com.tarang.dpq2.isopacket.IsoRequest;
import com.tarang.dpq2.model.RetailerDataModel;
import com.tarang.dpq2.model.TerminalConnectionGPRSModel;
import com.tarang.dpq2.model.TerminalConnectionWifiModel;

import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.net.ssl.SSLSocket;

import static com.tarang.dpq2.base.AppInit.sampleBoolean;
import static com.tarang.dpq2.isopacket.IsoRequest.unpackISOFormat;

/* SAF - Store and forward Request Connectivity and Storage */
public class SAFWorker extends Worker {
    private final AppDatabase database;
    private final InputStream inputStream;
    private final Context context;
    private final SDKDevice sdkDevice;
    private final PINPadExtendDevice device;
    private String IP;
    private int port;
    public static String SAF_COUNT = "SAF_COUNT";
    public static String SAF_COUNT_ALL = "SAF_COUNT_ALL";
    public static String SAF_DO_REPEAT = "SAF_DO_REPEAT";
    public static boolean isSAFRepeat = false;
    static Handler eventHandler;
    private final IsoRequest request;
//    private final K21Pininput pinInput;
    private final String version;
    private Data.Builder data = new Data.Builder();
    public static int count = 0;
    public static boolean isReversal = false;
    public static int failureCount = -1;
    public static boolean safTimerInitaited = false;
    SSLSocket requestSocket = null;



    public SAFWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        safTimerInitaited = true;
        this.context = context;
        this.device = SDKDevice.getInstance(context).getDeviceConnectPin();
        this.database = AppDatabase.getInstance(context.getApplicationContext());
        if(AppInit.HITTING_LIVE_SERVER)
            inputStream = context.getResources().openRawResource(R.raw.certnew);
        else
            inputStream = context.getResources().openRawResource(R.raw.cert);
        setPortIP();
//        IP = AppManager.getInstance().getIP();
//        port = Integer.parseInt(AppManager.getInstance().getPort());
        sdkDevice = SDKDevice.getInstance(context);
        request = IsoRequest.getInstance(context);
//        pinInput = SDKDevice.getInstance(context).getK21Pininput();
        version = AppManager.getInstance().getString(ConstantApp.HSTNG_TLS);
    }

    @NonNull
    @Override
    public Result doWork() {
        List<TransactionModelEntity> safModelEntities = database.getSAFDao().getAll();
        RetailerDataModel retailerDataModel = AppManager.getInstance().getRetailerDataModel();
        int retryCount = Integer.parseInt(retailerDataModel.getsAFRetryLimit());
        //if fails change the mti to ==== incresed by 1
        Logger.v("safModelEntitieslength-----" + safModelEntities.size());
        Logger.v("retailerDataModel-----" + retailerDataModel.toString());
        Logger.v("retailerDataModel-----" + retryCount);
        Logger.v("retailerDataModel-----" + failureCount);
        if (retryCount <= failureCount) {
            Logger.v("Inside Failure");
            CountDownResponseTimer.cancelTimer(3);
            data.putInt(SAF_COUNT, -3);
            return Result.success(data.build());
        }
        if (safModelEntities.size() != 0) {
            // check count

            boolean send_all = getInputData().getBoolean(SAFWorker.SAF_COUNT_ALL, false);
            boolean saf_Repeat = getInputData().getBoolean(SAFWorker.SAF_DO_REPEAT, false);
            Logger.v("send_all --" + send_all);
            Logger.v("saf_Repeat --" + saf_Repeat);
            Logger.v("count --" + count);
            if (!send_all)
                if (count == 0)
                    count = Integer.parseInt(retailerDataModel.getsAFDefaultMessageTransmissionNumber());
            Logger.v("Count --" + count);
            if (isReversal || count != 0 || send_all) {
                TransactionModelEntity safEntity = safModelEntities.get(0);

                Logger.v("SAF --" + safEntity.toString());
                safEntity.setUid(0);
                Logger.v("SAF --" + safEntity.getUid());
                Logger.v("SAF --" + safEntity.toString());
                Logger.v("SAF --" + isReversal);
                MadaRequest model = new MadaRequest();
                model.setNameTransactionTag(safEntity.getNameTransactionTag());
                model.setModeTransaction(safEntity.getModeTransaction());
                model.setPrimaryAccNo2(Utils.decrypt(safEntity.getPrimaryAccNo2()));
                model.setProcessingCode3(safEntity.getProcessingCode3());
                model.setAmtTransaction4(safEntity.getAmtTransaction4());
                model.setTimeLocalTransaction12(safEntity.getTimeLocalTransaction12());
//                model.setDateExpiration14(safEntity.getDateExpiration14());
                model.setPosEntrymode22(safEntity.getPosEntrymode22());
                model.setCardSequenceNumber23(safEntity.getCardSequenceNumber23());
                model.setFunctioncode24(safEntity.getFunctioncode24());
                model.setMessageReasonCode25(safEntity.getPosConditionCode25());
                model.setCardAcceptorBusinessCode26(safEntity.getPosPinCaptureCode26());
                model.setReconsilationDate28(safEntity.getAmtTransFee28());
                model.setAmtTranProcessingFee30(safEntity.getAmtTranProcessingFee30());
                model.setAccuringInsituteIdCode32(safEntity.getAccuringInsituteIdCode32());
                model.setTrack2Data35(Utils.decrypt(safEntity.getTrack2Data35()));
                model.setRetriRefNo37(safEntity.getRetriRefNo37());
                model.setAuthIdResCode38(safEntity.getAuthIdResCode38());
                model.setCardAcceptorTemId41(safEntity.getCardAcceptorTemId41());
                model.setCardAcceptorIdCode42(safEntity.getCardAcceptorIdCode42());
                model.setAdditionalDataNational47(safEntity.getAdditionalDataNational47());
                model.setAdditionalDataPrivate48(safEntity.getAdditionalDataPrivate48());
                model.setCurrCodeTransaction49(safEntity.getCurrCodeTransaction49());
                model.setCurrCodeStatleMent50(safEntity.getCurrCodeStatleMent50());
//                byte[] ksn = pinInput.getDukptKsn(AppConfig.Pin.DUKPT_DES_INDEX);
             //   sdkDevice.incrementKSN();
                model.setSecRelatedContInfo53(sdkDevice.getKSN() + "363039");
                model.setAddlAmt54(safEntity.getAddlAmt54());
                model.setIccCardSystemRelatedData55(Utils.decrypt(safEntity.getIccCardSystemRelatedData55()));
                if (!isReversal) {
                    model.setResponseCode39(safEntity.getResponseCode39());
                    if (safEntity.getNameTransactionTag().equalsIgnoreCase(ConstantApp.REFUND))
                        model.setMsgReasonCode56(safEntity.getMsgReasonCode56());
                    else
                        model.setMsgReasonCode56(getMsgReasonCode56(safEntity));

                    if (saf_Repeat)
                        model.setMti0(ConstantAppValue.FINANCIAL_ADVISE_REPEAT);
                    else
                        model.setMti0(ConstantAppValue.FINANCIAL_ADVISE);
                } else {
                    model.setMessageReasonCode25(ConstantAppValue.TIMEOUT_WAITING_RESP);
                    model.setMsgReasonCode56((safEntity.getMti0().equalsIgnoreCase(ConstantAppValue.FINANCIAL) ? ConstantAppValue.FINANCIAL : ConstantAppValue.AUTH) + safEntity.getSystemTraceAuditnumber11() + safEntity.getTransmissionDateTime7()
                            + safEntity.getTimeLocalTransaction12() + "06" + safEntity.getAccuringInsituteIdCode32() + "00");
                    if (saf_Repeat)
                        model.setMti0(ConstantAppValue.REVERSAL_REPEAT);
                    else
                        model.setMti0(ConstantAppValue.REVERSAL);
                }
//                model.setSystemTraceAuditnumber11(AppManager.getInstance().getSystemTraceAuditnumber11());  //ask need more clarification
                model.setSystemTraceAuditnumber11(safEntity.getSystemTraceAuditnumber11());  //ask need more clarification
                model.setTransmissionDateTime7(ByteConversionUtils.formatTranDateUTC("MMddhhmmss")); // UTC Date and time
                model.setEchoData59(safEntity.getEchoData59());
                model.setReservedData62(safEntity.getReservedData62());
                model.setMessageAuthenticationCodeField64(ISOUtil.hex2byte("0000000000000000"));
                model.setMessageAuthenticationCodeField64(request.getMAC(model));
                Logger.v("Model --" + model.toString());
                byte[] isoFinalBuffer = IsoRequest.createISORequest(model);
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
                Logger.v("SAF Socket Database");
                Logger.v("SAF Socket Database");
                Logger.v(finalData);


                Logger.v("Inside Failure ELSE ");
                failureCount = failureCount + 1;
                safEntity.setStartTimeTransaction(Utils.getCurrentDate());
                startTimer();

                // create socket connection
                // Send to host
                try {
                    closeSocket(requestSocket);
                    Logger.v("Socket 5");
                    SecurityManager manager = new SecurityManager();
                    Logger.v("Socket 4");
                    manager.checkConnect(IP, port);
                    Logger.v("Socket 3");
                    Logger.v("Soc --"+manager.getInCheck());
                    SSLSocketFactoryExtended sslsocketfactory = new SSLSocketFactoryExtended(inputStream, version,true);
                    Logger.v("Socket 1");
                    requestSocket = (SSLSocket) sslsocketfactory.createSocket(IP, port);
                    Logger.v("Socket 2");
//                    requestSocket.setSoTimeout(10000);
                    Logger.v("sendISORequest Initiated" + requestSocket.isConnected());
                    safEntity.setStartTimeConnection(Utils.getCurrentDate());
                    requestSocket.setKeepAlive(true);
                    BufferedOutputStream bos = null;
                    BufferedInputStream bis = null;
                    byte[] finalEchoResponse = (finalData);
                    // 1. get Input and Output streams
                    bos = new BufferedOutputStream(requestSocket.getOutputStream());
                    bis = new BufferedInputStream(requestSocket.getInputStream());
                    // 2: Communicating with the server
                    Logger.v("TRY block send");
                    Logger.v(finalEchoResponse);
                    bos.write(finalEchoResponse);
                    bos.flush();

                    // 4: Receive the response data
                    byte[] buffer = new byte[1024];
                    int nBytes = -1;
                    Logger.v("TRY block Receive");
                    while ((nBytes = bis.read(buffer)) >= 0) {
                        Logger.v("buffer --" + buffer.length);
                        SAFWorker.isSAFRepeat = false;
                        CountDownResponseTimer.cancelTimer(4);
                        failureCount = -1;
                        Logger.v(buffer);
                        bos.flush();
                        Logger.v("TRY block End Flush");
                        ISOMsg isoMsg = unpackISOFormat(buffer);
                        String mti = (String) isoMsg.getMTI();
                        AppManager.getInstance().setResponseMTI(mti);
                        String de39 = (String) isoMsg.getValue(39);
                        Logger.v("de39------" + de39);
                        String de38 = (String) isoMsg.getValue(38);
                        Logger.v("de38------" + de38);
                        Logger.v("SAF Database");
                        closeSocket(requestSocket, safEntity);
                        if (IsoRequest.validateMac(isoMsg,device )) {
                      //  if (sampleBoolean) {
                            if (getOfflineDeclinedReversal(model, isReversal) || de39.trim().length() != 0) {
                                isReversal = false;
                                safEntity.setSaf(true);
                                safEntity.setStatusTransaction(1);
                                if ((safEntity.getResponseCode39().equalsIgnoreCase(ConstantAppValue.SAF_APPROVED) || safEntity.getResponseCode39().equalsIgnoreCase(ConstantAppValue.SAF_APPROVED_UNABLE) || safEntity.getResponseCode39().equalsIgnoreCase(ConstantAppValue.SAF_REFUND)))
                                    safEntity.setResponseCode39(de39);
                                // insert in transaction table
                                database.getTransactionDao().insertTransaction(safEntity);
                                // delete from SAF
//                                database.getSAFDao().deleteSAFWithRRn(safEntity.getRetriRefNo37());
//                                if(saf_Repeat)
                                    database.getSAFDao().deleteSAFWithSTAN(safEntity.getSystemTraceAuditnumber11());

                                Logger.v("SAF Deleted");
//                                boolean increment = pinInput.ksnIncrease(AppConfig.Pin.DUKPT_DES_INDEX);
//                                Logger.v("response", "increment:" + increment);

                                Logger.v("SAF Database Updated");
                                Logger.v("requestSocket.isClosed() --" + requestSocket.isClosed());
                                if (!send_all && !saf_Repeat)
                                    count = count - 1;
                                Logger.v("Countt -" + count);
                                List<TransactionModelEntity> safModelEntities1 = database.getSAFDao().getAll();
                                Logger.v("Countt -" + safModelEntities1.size());
                                if (safModelEntities1.size() == 0) {
                                    Logger.v("Succ1");
                                    data.putInt(SAF_COUNT, -2);
                                } else if (!send_all && count == 0) {
                                    Logger.v("Succ2");
                                    data.putInt(SAF_COUNT, -1);
                                } else {
                                    Logger.v("Succ3");
                                    data.putInt(SAF_COUNT, 0);
                                }
                            } else {
                                isReversal = true;
                                data.putInt(SAF_COUNT, -1);
                            }
                            return Result.success(data.build());
                        } else {
                            // insert in transaction table
                            database.getTransactionDao().insertTransaction(safEntity);
                            // delete from SAF
//                            database.getSAFDao().deleteSAFWithRRn(safEntity.getRetriRefNo37());
//                            if(saf_Repeat)
                                database.getSAFDao().deleteSAFWithSTAN(safEntity.getSystemTraceAuditnumber11());

                            Logger.v("Admin Notificayion");
                            byte[] adminResponse = CreatePacket.adminNotification(buffer);
                            bos = new BufferedOutputStream(requestSocket.getOutputStream());
                            Logger.v(adminResponse);
                            bos.write(adminResponse);
                            bos.flush();
                            closeSocket(requestSocket, null);
                            return Result.failure(data.build());
                        }
                    }
                    data.putInt(SAF_COUNT, -2);

                } catch (Exception e) {
                    Logger.v("Exception --" + e.getMessage());
                    data.putBoolean("SHOW_TOAST", true);
                    e.printStackTrace();
                }
                data.putInt(SAF_COUNT, -22);
                closeSocket(requestSocket, null);
            } else {
                data.putInt(SAF_COUNT, -2);
                Logger.v("SAF ELSE");
            }
        } else
            data.putInt(SAF_COUNT, -2);
        return Result.failure(data.build());
    }

    private boolean getOfflineDeclinedReversal(MadaRequest model, boolean isReversal) {
        if (model.getResponseCode39().equalsIgnoreCase("190")) {
            return isReversal;
        }
        return true;
    }

    public void closeSocket(SSLSocket requestSocket, TransactionModelEntity safEntity) {
        if (requestSocket != null && !requestSocket.isClosed()) {
            Logger.v("requestSocket close");
            try {
                if (safEntity != null)
                    safEntity.setEndTimeTransaction(Utils.getCurrentDate());
                Logger.v("requestSocket try close");
                requestSocket.close();
                if (safEntity != null)
                    safEntity.setEndTimeConnection(Utils.getCurrentDate());
            } catch (IOException e) {
                Logger.v("requestSocket try catch--" + e.getMessage());
                e.printStackTrace();
            }
        } else
            Logger.v("requestSocket close else");
    }

    public void closeSocket(SSLSocket requestSocket) {
        if (requestSocket != null && !requestSocket.isClosed()) {
            Logger.v("requestSocket close");
            try {
                Logger.v("requestSocket try close");
                requestSocket.close();
            } catch (IOException e) {
                Logger.v("requestSocket try catch--" + e.getMessage());
                e.printStackTrace();
            }
        } else
            Logger.v("requestSocket close else");
    }

    public static void startTimer() {
        Logger.v("SAF TIMER 122");
        isSAFRepeat = true;
        Message pinFinishMsg = new Message();
        pinFinishMsg.what = AppConfig.EMV.SAF_TIMER_;
//        pinFinishMsg.obj = SAFWorker.count;

        if (getEventHandler() != null)
            getEventHandler().sendMessage(pinFinishMsg);
    }

    public static Handler getEventHandler() {
        return eventHandler;
    }

    public static void setEventHandler(Handler pinEventHandler) {
        eventHandler = pinEventHandler;
    }

    private String getMsgReasonCode56(TransactionModelEntity safEntity) {
        StringBuilder authAdviceBuilder = new StringBuilder();
        authAdviceBuilder.append(safEntity.getMti0());
        Logger.v("advice56Model----" + safEntity.getMti0());
        authAdviceBuilder.append(safEntity.getSystemTraceAuditnumber11());
        Logger.v("advice56Model----" + safEntity.getSystemTraceAuditnumber11());
        authAdviceBuilder.append(safEntity.getTransmissionDateTime7());
        Logger.v("advice56Model----" + safEntity.getTransmissionDateTime7());
        authAdviceBuilder.append(safEntity.getTimeLocalTransaction12());
        Logger.v("advice56Model----" + safEntity.getTimeLocalTransaction12());
        authAdviceBuilder.append("06"); //length of de32
        authAdviceBuilder.append(safEntity.getAccuringInsituteIdCode32());
        Logger.v("advice56Model----" + safEntity.getAccuringInsituteIdCode32());
        authAdviceBuilder.append("00");
        Logger.v("advice56Model----" + "00");
        Logger.v("advice56Model--V-------" + authAdviceBuilder.toString());
        return authAdviceBuilder.toString();
    }

    private void setPortIP() {
        boolean priority = AppManager.getInstance().getConnectonMode();
        TerminalConnectionGPRSModel terminalConnectionGPRSModel1 = AppManager.getInstance().getTerminalConnectionGPRSModel();
        TerminalConnectionWifiModel terminalConnectionWifiModel1 = AppManager.getInstance().getTerminalConnectionWifiModel();

        if (priority) {
            if (terminalConnectionGPRSModel1 != null && terminalConnectionGPRSModel1.getNetworkIpAddress() != null
                    && terminalConnectionGPRSModel1.getNetworkTcpPort() != null &&
                    terminalConnectionGPRSModel1.getNetworkIpAddress().trim().length() != 0
                    && terminalConnectionGPRSModel1.getNetworkTcpPort().trim().length() != 0) {
                IP = terminalConnectionGPRSModel1.getNetworkIpAddress();
                port = Integer.parseInt(terminalConnectionGPRSModel1.getNetworkTcpPort());
                Logger.v("IP --1--" + IP + "-" + port);
            } else if (terminalConnectionWifiModel1 != null && terminalConnectionWifiModel1.getNetworkIpAddress() != null
                    && terminalConnectionWifiModel1.getNetworkTcpPort() != null &&
                    terminalConnectionWifiModel1.getNetworkIpAddress().trim().length() != 0
                    && terminalConnectionWifiModel1.getNetworkTcpPort().trim().length() != 0) {
                IP = terminalConnectionWifiModel1.getNetworkIpAddress();
                port = Integer.parseInt(terminalConnectionWifiModel1.getNetworkTcpPort());
                Logger.v("IP --2--" + IP + "-" + port);
            } else {
                IP = AppManager.getInstance().getIP();
                port = Integer.parseInt(AppManager.getInstance().getPort());
                Logger.v("IP --3--" + IP + "-" + port);
            }
        } else {
            if (terminalConnectionWifiModel1 != null && terminalConnectionWifiModel1.getNetworkIpAddress() != null
                    && terminalConnectionWifiModel1.getNetworkTcpPort() != null &&
                    terminalConnectionWifiModel1.getNetworkIpAddress().trim().length() != 0
                    && terminalConnectionWifiModel1.getNetworkTcpPort().trim().length() != 0) {
                IP = terminalConnectionWifiModel1.getNetworkIpAddress();
                port = Integer.parseInt(terminalConnectionWifiModel1.getNetworkTcpPort());
                Logger.v("IP --4--" + IP + "-" + port);
            } else if (terminalConnectionGPRSModel1 != null && terminalConnectionGPRSModel1.getNetworkIpAddress() != null
                    && terminalConnectionGPRSModel1.getNetworkTcpPort() != null &&
                    terminalConnectionGPRSModel1.getNetworkIpAddress().trim().length() != 0
                    && terminalConnectionGPRSModel1.getNetworkTcpPort().trim().length() != 0) {
                IP = terminalConnectionGPRSModel1.getNetworkIpAddress();
                port = Integer.parseInt(terminalConnectionGPRSModel1.getNetworkTcpPort());
                Logger.v("IP --5--" + IP + "-" + port);
            } else {
                IP = AppManager.getInstance().getIP();
                port = Integer.parseInt(AppManager.getInstance().getPort());
                Logger.v("IP --6--" + IP + "-" + port);
            }
        }

    }
}
