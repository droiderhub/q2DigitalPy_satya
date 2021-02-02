package com.tarang.dpq2.worker;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.cloudpos.POSTerminal;
import com.cloudpos.pinpad.extend.PINPadExtendDevice;
import com.tarang.dpq2.R;
import com.tarang.dpq2.base.AppInit;
import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.jpos_class.ByteConversionUtils;
import com.tarang.dpq2.base.jpos_class.ConstantApp;
import com.tarang.dpq2.base.jpos_class.ConstantAppValue;
import com.tarang.dpq2.base.terminal_sdk.AppConfig;
import com.tarang.dpq2.base.terminal_sdk.RSAEncrypt;
import com.tarang.dpq2.base.terminal_sdk.device.SDKDevice;
import com.tarang.dpq2.base.terminal_sdk.event.SimpleTransferListener;
import com.tarang.dpq2.base.room_database.db.AppDatabase;
import com.tarang.dpq2.base.room_database.db.entity.TransactionModelEntity;
import com.tarang.dpq2.base.utilities.CountDownResponseTimer;
import com.tarang.dpq2.base.utilities.Utils;
import com.tarang.dpq2.isopacket.CreatePacket;
import com.tarang.dpq2.isopacket.IsoRequest;
import com.tarang.dpq2.model.TerminalConnectionGPRSModel;
import com.tarang.dpq2.model.TerminalConnectionWifiModel;
import com.tarang.dpq2.tms.TmsResponseParser;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLSocket;

import static com.tarang.dpq2.base.terminal_sdk.AppConfig.EMV.reqObj;
import static com.tarang.dpq2.isopacket.IsoRequest.unpackISOFormat;

/* Connectivity for all request to Host*/
public class SocketConnectionWorker extends Worker {

    public static final int IDEAL_TIME = 20000;
    private final InputStream inputStream;
    private String IP;
    private int port;
    private final TransactionModelEntity transaction;
    PINPadExtendDevice device;
    private final String version;
    AppDatabase database;
    Context context;
    static Handler eventHandler;
    public static byte[] responseTrack55;
    public static ISOMsg responseObjArray;
    public static String ADMIN_NOTIFICATION = "ADMIN_NOTIFICATION";
    public static String ERROR_MSG = "ERROR_MSG";
    private Data.Builder data = new Data.Builder();
    public static String TRANSACTION_START_TIME = "";
    private static boolean regValidation = false;
    public static int failureCount = -1;
    public static boolean cancelledRequest = false;
    SSLSocket requestSocket = null;
    private SSLSocketFactoryExtended sslsocketfactory;


    public SocketConnectionWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        this.database = AppDatabase.getInstance(context.getApplicationContext());
        if (AppInit.HITTING_LIVE_SERVER)
            inputStream = context.getResources().openRawResource(R.raw.certnew);
        else
            inputStream = context.getResources().openRawResource(R.raw.cert);
        transaction = database.getTransactionDao().getLastTransaction(false);
        setPortIP();
//        IP = AppManager.getInstance().getIP();
//        port = Integer.parseInt(AppManager.getInstance().getPort());
        Logger.v("IP --PORT --" + IP + "--" + port);
        device = SDKDevice.getInstance(context).getDeviceConnectPin();
        version = AppManager.getInstance().getString(ConstantApp.HSTNG_TLS);
        AppManager.getInstance().setResponseMTI(null);

    }

    private void setPortIP() {
        if (reqObj != null && (reqObj.getMti0().equalsIgnoreCase(ConstantAppValue.NETWORK_MNGMT) || reqObj.getMti0().equalsIgnoreCase(ConstantAppValue.FILEACTION))) {
            IP = AppManager.getInstance().getIP();
            port = Integer.parseInt(AppManager.getInstance().getPort());
            Logger.v("IP --00--" + IP + "-" + port);
            return;
        }
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

    @NonNull
    @Override
    public Result doWork() {
        Logger.v("AppManager.getInstance().isDebugEnabled() -" + AppManager.getInstance().isDebugEnabled());
        if (AppManager.getInstance().isDebugEnabled() && Utils.allowedMTI(reqObj.getMti0())) {
            SimpleTransferListener.disableReversal = true;
            SimpleTransferListener.doReversal = false;
            CountDownResponseTimer.cancelTimer(6);
            database.getTransactionDao().deleteSAFWithSTAN(transaction.getRetriRefNo37());
            transaction.setEndTimeTransaction(Utils.getCurrentDate());
            String mti = Utils.fetchMti(reqObj.getMti0());
            if (mti.equalsIgnoreCase(ConstantAppValue.REVERSAL_RESPONSE))
                AppManager.getInstance().setReversalStatus(true);
            AppManager.getInstance().setResponseMTI(mti);
            String de39 = (reqObj.getMti0().equalsIgnoreCase(ConstantAppValue.REVERSAL)) ? "400" : "000";
            Logger.v("de39------" + de39);
            String de38 = ByteConversionUtils.generateTraceNumber(6);
            Logger.v("de38------" + de38);
            AppManager.getInstance().setDe55("8A023030");
            Logger.v("AppConfig.EMV.enableDatabaseUpdate --" + AppConfig.EMV.enableDatabaseUpdate);
            final boolean manualReversal = AppManager.getInstance().isReversalManual();
            transaction.setStatusTransaction(1);
            transaction.setAuthIdResCode38(de38);
            transaction.setResponseCode39(de39);
            transaction.setIccCardSystemRelatedData55_final(transaction.getIccCardSystemRelatedData55());
            AppManager.getInstance().setDenugTransactionModelEntity(transaction);
            return Result.success();
        }

        Logger.v("failureCount --" + failureCount);

        String error = "";
        SimpleTransferListener.isSAFDecilined = true;
        data.putString(ERROR_MSG, error);
        if ((reqObj != null)) {
            startTimer(reqObj.getMti0());
        }

        // Save Start Time of the transaction
        if (AppConfig.EMV.enableDatabaseUpdate) {
            if (TRANSACTION_START_TIME.trim().length() == 0)
                database.getTransactionDao().updateStartTime(transaction.getUid(), Utils.getCurrentDate());
            else
                database.getTransactionDao().updateStartTime(transaction.getUid(), TRANSACTION_START_TIME);
        }

        try {
            closeSocket(requestSocket);
            Logger.v("Socket 5");
            SecurityManager manager = new SecurityManager();
            Logger.v("Socket 4");
            manager.checkConnect(IP, port);
            Logger.v("Socket 3");
            Logger.v("Soc --" + manager.getInCheck());
            sslsocketfactory = new SSLSocketFactoryExtended(inputStream, version, false);
            Logger.v("Socket 1");
            requestSocket = (SSLSocket) sslsocketfactory.createSocket(IP, port);
//            requestSocket = new Socket(IP, port);
            Logger.v("Socket 2");
//            requestSocket.setSoTimeout(10000);

            Logger.v("sendISORequest Initiated" + requestSocket.isConnected());

            SimpleTransferListener.isSAFDecilined = false;

//             Save Connection Start Time
            if (AppConfig.EMV.enableDatabaseUpdate)
                database.getTransactionDao().updateConnectionStartTime(transaction.getUid(), Utils.getCurrentDate());

//            requestSocket.setKeepAlive(true);
            Logger.v("echoTestConnection IF block");
            BufferedOutputStream bos = null;
            BufferedInputStream bis = null;
            ISOMsg isoMessage = new ISOMsg();

            //reqObjArray consict the isopacket from printer activity ====> createPAcket class

            byte[] finalEchoResponse = (CreatePacket.createISORequest());

            if (finalEchoResponse == null) {
                return Result.failure(data.build());
            }

            // 1. get Input and Output streams
            bos = new BufferedOutputStream(requestSocket.getOutputStream());
            bis = new BufferedInputStream(requestSocket.getInputStream());

            //Check SAF Request


            // 2: Communicating with the server
//            try {
            Logger.v("TRY block send");
            // 3: Post the request data
            Logger.v(finalEchoResponse);
            bos.write(finalEchoResponse);
//            bos.writeByte(2);
//            bos.writeUTF("{merchantName:DigitalPay,terminalDate:07/04/2020,terminalTime:23:09:26,cardName:RAJB,tId:800150400567,mId:1234567812121388,mcc:7399,stan:0000017,applicationVersion:1.0.0,debitCount:1,debitAmount:100,creditCount:3,creditAmount:240,cashBackAmount:240,cashAdvanceAmount:240}");

            bos.flush();

            // 4: Receive the response data
            byte[] buffer = new byte[1024];
            int nBytes = -1;
            Logger.v("TRY block Receive");
            while ((nBytes = bis.read(buffer)) >= 0) {
                Logger.v("buffer --" + buffer.length);
                Logger.v(buffer);
                bos.flush();
                SimpleTransferListener.disableReversal = true;
                SimpleTransferListener.doReversal = false;
                CountDownResponseTimer.cancelTimer(6);
                Logger.v("TRY block End Flush");
                failureCount = -1;

                // Save End Time of the transaction
                if (AppConfig.EMV.enableDatabaseUpdate)
                    database.getTransactionDao().updateEndTime(transaction.getUid(), Utils.getCurrentDate());

                ISOMsg isoMsg = unpackISOFormat(buffer);
                String mti = (String) isoMsg.getMTI();
                if (mti.equalsIgnoreCase(ConstantAppValue.REVERSAL_RESPONSE))
                    AppManager.getInstance().setReversalStatus(true);
                AppManager.getInstance().setResponseMTI(mti);
                String de39 = (String) isoMsg.getValue(39);
                String de47 = (String) isoMsg.getValue(47);
                Logger.v("de39------" + de39);
                String de38 = (String) isoMsg.getValue(38);
                String de62 = (String) isoMsg.getValue(62);
                String de44 = (String) isoMsg.getValue(44);
                Logger.v("de38------" + de38);
                responseTrack55 = isoMsg.getBytes(55);
                if (AppConfig.EMV.enableDatabaseUpdate) {
                    if (de38 != null || !Utils.check38Tag(transaction.getNameTransactionTag()))
                        AppManager.getInstance().setDe38(de38);
                    else
                        Logger.v("De38 else");
                }
                AppManager.getInstance().setDe39(de39);
                responseObjArray = isoMsg;
                AppManager.getInstance().setAdminReq(false);
                if (mti.equalsIgnoreCase(ConstantAppValue.TERMINAL_REGISTRATION_RESPONSE)) {
                    closeSocket(requestSocket, transaction);
                    //for registration validation
                    if (validateRegistration(isoMsg, mti, de39)) {
                        return Result.success(data.build());
                    } else {
                        return Result.failure(data.build());
                    }
                } else if (IsoRequest.validateMac(isoMsg, device)) {
                    if (responseTrack55 != null)
                        AppManager.getInstance().setDe55(ByteConversionUtils.byteArrayToHexString(responseTrack55, responseTrack55.length, false));
                    else
                        AppManager.getInstance().setDe55("");
                    Logger.v("AppConfig.EMV.enableDatabaseUpdate --" + AppConfig.EMV.enableDatabaseUpdate);
                    final boolean manualReversal = AppManager.getInstance().isReversalManual();
                    if (AppConfig.EMV.enableDatabaseUpdate || isPurchaseAdvice(transaction, reqObj.getMti0()) || (manualReversal && (reqObj.getMti0().equalsIgnoreCase(ConstantAppValue.REVERSAL) || reqObj.getMti0().equalsIgnoreCase(ConstantAppValue.REVERSAL_REPEAT)))) {
                        Logger.v("Database update");
                        AppConfig.EMV.enableDatabaseUpdate = true;
                        if (de62 != null)
                            database.getTransactionDao().update62Response(transaction.getUid(), de62);
                        if (de44 != null)
                            database.getTransactionDao().update44Response(transaction.getUid(), de44);
                        if (mti.equalsIgnoreCase(ConstantAppValue.REVERSAL_RESPONSE) || de38 == null)
                            database.getTransactionDao().updateResponse(transaction.getUid(), de47, de39);
                        else
                            database.getTransactionDao().updateResponse(transaction.getUid(), de47, de38, de39);
                        if (AppConfig.EMV.consumeType == 0)
                            database.getTransactionDao().updateResponse(transaction.getUid(), 1);
                    } else
                        Logger.v("Database update not done");
                    if (mti.equalsIgnoreCase(ConstantAppValue.REVERSAL_RESPONSE)) {
                        AppManager.getInstance().setReversalStatus(true);
                        if (AppManager.getInstance().isReversalManual()) {
                            database.getTransactionDao().updateResponse(transaction.getUid(), 1);
                        } else {
                            database.getTransactionDao().updateResponse(transaction.getUid(), 0);
                        }
                    } else if (mti.equalsIgnoreCase(ConstantAppValue.FILEACTION_RESPONSE)) {
                        byte[] de72 = isoMsg.getBytes(72);
                        String data72 = ByteConversionUtils.byteArrayToHexString(de72, de72.length, false);
                        TmsResponseParser tms = new TmsResponseParser();
                        tms.parseDE72(IsoRequest.getStringFromHex(data72), database);
                    } else if (mti.equalsIgnoreCase(ConstantAppValue.RECONSILATION_RESPONSE)) {
                        String de124 = (String) isoMsg.getValue(124);
                        AppManager.getInstance().setDe124(de124);
                    }
                    closeSocket(requestSocket, transaction);
                    return Result.success(data.build());
                } else {
                    AppManager.getInstance().setAdminReq(true);
                    Logger.v("Admin Notification");
                    if (AppConfig.EMV.enableDatabaseUpdate) {
                        String responceCode = (de39 == null || de39.trim().length() == 0 || de39.equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_000) || de39.equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_001) || de39.equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_003)
                                || de39.equalsIgnoreCase(ConstantAppValue.SAF_APPROVED) || de39.equalsIgnoreCase(ConstantAppValue.SAF_APPROVED_UNABLE)
                                || de39.equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_007) || de39.equalsIgnoreCase(ConstantApp.REVERSAL_RESPONSE_400)) ? "190" : de39;
                        AppManager.getInstance().setDe39(responceCode);
                        if (mti.equalsIgnoreCase(ConstantAppValue.REVERSAL_RESPONSE) || de38 == null)
                            database.getTransactionDao().updateResponse(transaction.getUid(), de47, responceCode);
                        else
                            database.getTransactionDao().updateResponse(transaction.getUid(), de47, de38, responceCode);
                    }
                    byte[] adminResponse = CreatePacket.adminNotification(buffer);
                    bos = new BufferedOutputStream(requestSocket.getOutputStream());
                    Logger.v(adminResponse);
                    bos.write(adminResponse);
                    bos.flush();
                    closeSocket(requestSocket, transaction);
                    data.putBoolean(ADMIN_NOTIFICATION, true);
                    SimpleTransferListener.doReversal = true;
                    return Result.failure(data.build());
                }

                /*    }
                    else {
                        break;
                    }*/
            }
            SimpleTransferListener.isSAFDecilined = true;
            Logger.v("TRY block End" + nBytes);
            Logger.v("requestSocket.isClosed() --" + requestSocket.isClosed());
            data.putBoolean("SHOW_TOAST", true);
//            closeSocket(requestSocket, transaction);
            Logger.v(isoMessage.toString());
//        }else
            Logger.v("requestSocket.isClosed() --" + requestSocket.isClosed());
            Logger.v("echoTestConnection else");
            return Result.failure(data.build());
        } catch (IOException e) {
            error = e.getMessage();
//            if (e.getMessage().contains("failed to connect"))
//                ((BaseActivity) context).showToast(context.getString(R.string.failed_to_connect));
            Logger.v("E1 --" + e.getMessage());
            e.printStackTrace();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            error = e.getMessage();
            Logger.v("E1.1 --" + e.getMessage());
            e.printStackTrace();
        } catch (CertificateException e) {
            error = e.getMessage();
            Logger.v("E3 --" + e.getMessage());
            e.printStackTrace();
        } catch (KeyStoreException e) {
            error = e.getMessage();
            Logger.v("E4 --" + e.getMessage());
            e.printStackTrace();
        } catch (ISOException e) {
            e.printStackTrace();
        } finally {
            Logger.v("Socket finally");
        }
        data.putBoolean("SHOW_TOAST", true);
        closeSocket(requestSocket, transaction);
        data.putString(ERROR_MSG, error);
//        ((BaseActivity) context).showToast("Invalid Service IP address");
        return Result.failure(data.build());
    }

    private boolean isPurchaseAdvice(TransactionModelEntity lastransaction, String mti) {
        if (lastransaction != null) {
            String nameTransactionTag = lastransaction.getNameTransactionTag();
            Logger.v("name -" + nameTransactionTag);
            Logger.v("mti -" + mti);
            Logger.v("bool -" + (nameTransactionTag.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE)
                    || nameTransactionTag.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_FULL)
                    || nameTransactionTag.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_PARTIAL)
                    || nameTransactionTag.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_MANUAL)));
            Logger.v("bool1 -" + mti.equalsIgnoreCase(ConstantAppValue.FINANCIAL_ADVISE_REPEAT));
            return (nameTransactionTag.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE)
                    || nameTransactionTag.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_FULL)
                    || nameTransactionTag.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_PARTIAL)
                    || nameTransactionTag.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_MANUAL)) && mti.equalsIgnoreCase(ConstantAppValue.FINANCIAL_ADVISE_REPEAT);
        } else
            return false;
    }


    private boolean validateRegistration(ISOMsg isoMsg, String mti, String de39) {

        if (mti.equalsIgnoreCase(ConstantAppValue.TERMINAL_REGISTRATION_RESPONSE) && de39.equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_800)) {

            byte[] de48outPut = (byte[]) isoMsg.getBytes(48);
            String de48 = ByteConversionUtils.byteArrayToHexString(de48outPut);
            String madaSignatureOutput = de48.substring(58);
            Logger.v("de48output------" + de48);
            Logger.v("madaSignatureOutput-----" + madaSignatureOutput);
//            SmModule smModule = SDKDevice.getInstance(context).getSmModule();
            String decryptMadaSignature = "";
            if (AppInit.HITTING_LIVE_SERVER) {
                //rsa drcryption
                byte[] rsaDecryptMadaSignature = RSAEncrypt.rsaDecryptRecover(ConstantAppValue.rsaLocalPublicModeKey_live, ISOUtil.hex2byte(madaSignatureOutput));
                decryptMadaSignature = ByteConversionUtils.byteArrayToHexString(rsaDecryptMadaSignature);
                Logger.v("decryptMadaSignature---------" + decryptMadaSignature);
            } else {
                //rsa drcryption
                byte[] rsaDecryptMadaSignature = RSAEncrypt.rsaDecryptRecover(ConstantAppValue.rsaLocalPublicModeKey, ISOUtil.hex2byte(madaSignatureOutput));
                decryptMadaSignature = ByteConversionUtils.byteArrayToHexString(rsaDecryptMadaSignature);
                Logger.v("decryptMadaSignature---------" + decryptMadaSignature);
            }
            //get checksum from mada signature
            String checksum = decryptMadaSignature.substring(decryptMadaSignature.length() - 40);
            // String checksum = madaSignatureOutput.substring(madaSignatureOutput.length() - 40);
            Logger.v("checksumoutput-----" + checksum);


            //calculate checksum from response ka de
            StringBuilder sha1CheckSumBuilder = new StringBuilder();
            sha1CheckSumBuilder.append(IsoRequest.getInstance(context).getHexaString((String) isoMsg.getString(7)));
            sha1CheckSumBuilder.append(IsoRequest.getInstance(context).getHexaString((String) isoMsg.getString(32)));
            sha1CheckSumBuilder.append(IsoRequest.getInstance(context).getHexaString((String) isoMsg.getString(39)));
            sha1CheckSumBuilder.append(IsoRequest.getInstance(context).getHexaString((String) isoMsg.getString(41)));
            sha1CheckSumBuilder.append(IsoRequest.getInstance(context).getHexaString((String) isoMsg.getString(42)));
            Logger.v("de7----" + IsoRequest.getInstance(context).getHexaString((String) isoMsg.getString(7)));
            Logger.v("de32----" + IsoRequest.getInstance(context).getHexaString((String) isoMsg.getString(32)));
            Logger.v("de39----" + IsoRequest.getInstance(context).getHexaString((String) isoMsg.getString(39)));
            Logger.v("de41----" + IsoRequest.getInstance(context).getHexaString((String) isoMsg.getString(41)));
            Logger.v("de42----" + IsoRequest.getInstance(context).getHexaString((String) isoMsg.getString(42)));
            //vendor id
            sha1CheckSumBuilder.append(de48.substring(0, 2));
            Logger.v("vendorid----" + de48.substring(0, 2));
            //terminal type
            sha1CheckSumBuilder.append(de48.substring(2, 4));
            Logger.v("terminatype----" + de48.substring(2, 4));
            //terminal id
            sha1CheckSumBuilder.append(de48.substring(4, 10));
            Logger.v("terminaid----" + de48.substring(4, 10));
            //sama key index
            sha1CheckSumBuilder.append(de48.substring(12, 14));
            Logger.v("samakeyindex----" + de48.substring(12, 14));
            //renadom string after 12,18
            sha1CheckSumBuilder.append(de48.substring(20, 52));
            Logger.v("randomstring----" + de48.substring(20, 52));

            Logger.v("checksumrawoutput----" + (sha1CheckSumBuilder.toString()));

            byte[] checkSum = Utils.getSha1(ISOUtil.hex2byte(sha1CheckSumBuilder.toString()));
            // return ByteConversionUtils.byteArrayToString(checkSum, checkSum.length);
            String checksumHexa = ByteConversionUtils.byteArrayToHexString(checkSum, checkSum.length, false);

            Logger.v("checksumHexa----" + checksumHexa);
            Logger.v("checksumHexaLength----" + checksumHexa.length());
            if (checksumHexa.equalsIgnoreCase(checksum)) {
                //true
                Logger.v("checksum validate successfully");
                regValidation = true;
            } else {
                Logger.v("checksum validate failed");
                regValidation = false;
            }
        } else {
            regValidation = false;
        }
        return regValidation;
    }

    public void closeSocket(SSLSocket requestSocket, TransactionModelEntity transaction) {
        if (requestSocket != null && !requestSocket.isClosed()) {
            Logger.v("requestSocket close");
            try {

                Logger.v("requestSocket try close");
                requestSocket.close();
                // Save Coneection End Time
                if (AppConfig.EMV.enableDatabaseUpdate) {
                    database.getTransactionDao().updateConnectionEndTime(transaction.getUid(), Utils.getCurrentDate());
                    AppManager.getInstance().saveTransactionTime(database.getTransactionDao().getTransaction(transaction.getUid()));
                }

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

    public void startTimer(String mti0) {
        Logger.v("Start Timer");
        SimpleTransferListener.disableReversal = false;
        Message pinFinishMsg = new Message();
        if (mti0.equalsIgnoreCase(ConstantAppValue.REVERSAL) || mti0.equalsIgnoreCase(ConstantAppValue.REVERSAL_REPEAT))
            pinFinishMsg.what = AppConfig.EMV.START_TIMER_REVERSE;
        else
            pinFinishMsg.what = AppConfig.EMV.START_TIMER;
        pinFinishMsg.obj = null;
        getEventHandler().sendMessage(pinFinishMsg);
    }

    public static Handler getEventHandler() {
        return eventHandler;
    }

    public static void setEventHandler(Handler pinEventHandler, int i) {
        Logger.v("eventHandler --" + i);
        eventHandler = pinEventHandler;
    }

    @Override
    public void onStopped() {
        super.onStopped();
        Logger.v("Worker stopped");
        if (requestSocket != null) {
            Logger.v("requestSocket.isClosed() --" + requestSocket.isClosed());
            if (!requestSocket.isClosed()) {
                try {
                    Logger.v("Worker stopped 1");
                    requestSocket.close();
                } catch (IOException e) {
                    Logger.v("Worker stopped 2");
                    e.printStackTrace();
                }
            }
        } else {
            Logger.v("requestSocket else");
        }
    }

}
