package com.tarang.dpq2.isopacket;

import android.content.Context;

import com.cloudpos.AlgorithmConstants;
import com.cloudpos.DeviceException;
import com.cloudpos.pinpad.KeyInfo;
import com.cloudpos.pinpad.PINPadDevice;
import com.cloudpos.pinpad.extend.PINPadExtendDevice;
import com.tarang.dpq2.R;
import com.tarang.dpq2.base.AppInit;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.jpos_class.ByteConversionUtils;
import com.tarang.dpq2.base.jpos_class.ConstantApp;
import com.tarang.dpq2.base.jpos_class.ConstantAppValue;
import com.tarang.dpq2.base.jpos_class.MadaPackager;
import com.tarang.dpq2.base.jpos_class.MadaRequest;
import com.tarang.dpq2.base.terminal_sdk.device.SDKDevice;
import com.tarang.dpq2.base.terminal_sdk.utils.Dump;

import org.jpos.iso.ISOBinaryField;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOField;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;

public class IsoRequest {

    public static Context context;
    private static IsoRequest request;

    private IsoRequest(Context context) {
        this.context = context;
    }

    public static IsoRequest getInstance(Context context) {
        if (request == null) {
            synchronized (IsoRequest.class) {
                if (request == null) {
                    request = new IsoRequest(context);
                }
            }
        }
        IsoRequest.context = context;
        return request;
    }

    //  private static String ECHO_REQUEST_MTI = getMTI();

    private static String ECHO_RESPONSE_MTI = "0810";

    private static String ECHO_NETWORK_CODE = "301";

    /**
     * This method creates a ISO format Request String ISO formatted string
     *
     * @param reqObj
     * @return byte[]- the request byte that need to be sent to the server
     */
    public static byte[] createISORequest(MadaRequest reqObj) {
        byte[] isoRequest = new byte[1024];

        // Create the ISO message
        ISOMsg isoMsg = new ISOMsg();
        try {
            isoMsg.setMTI(reqObj.getMti0());

            if (!reqObj.getPrimaryAccNo2().equalsIgnoreCase("")) {
                isoMsg.set(new ISOField(2, reqObj.getPrimaryAccNo2()));
            }
            if (!reqObj.getProcessingCode3().equalsIgnoreCase("")) {
                isoMsg.set(new ISOField(3, reqObj.getProcessingCode3()));
            }
            if (!reqObj.getAmtTransaction4().equalsIgnoreCase("")) {
                isoMsg.set(new ISOField(4, reqObj.getAmtTransaction4()));
            }
            if (!reqObj.getTransmissionDateTime7().equalsIgnoreCase("")) {
                isoMsg.set(new ISOField(7, reqObj.getTransmissionDateTime7()));
            }
            if (!reqObj.getSystemTraceAuditnumber11().equalsIgnoreCase("")) {
                isoMsg.set(new ISOField(11, reqObj.getSystemTraceAuditnumber11()));
            }
            if (!reqObj.getTimeLocalTransaction12().equalsIgnoreCase("")) {
                isoMsg.set(new ISOField(12, reqObj.getTimeLocalTransaction12()));
            }
            if (!reqObj.getDateExpiration14().equalsIgnoreCase("")) {
                isoMsg.set(new ISOField(14, reqObj.getDateExpiration14()));
            }
            if (!reqObj.getPosEntrymode22().equalsIgnoreCase("")) {
                isoMsg.set(new ISOField(22, reqObj.getPosEntrymode22()));
            }
            if (!reqObj.getCardSequenceNumber23().equalsIgnoreCase("")) {
                isoMsg.set(new ISOField(23, reqObj.getCardSequenceNumber23()));
            }
            if (!reqObj.getFunctioncode24().equalsIgnoreCase("")) {
                isoMsg.set(new ISOField(24, reqObj.getFunctioncode24()));
            }
            if (!reqObj.getMessageReasonCode25().equalsIgnoreCase("")) {
                isoMsg.set(new ISOField(25, reqObj.getMessageReasonCode25()));
            }
            if (!reqObj.getCardAcceptorBusinessCode26().equalsIgnoreCase("")) {
                isoMsg.set(new ISOField(26, reqObj.getCardAcceptorBusinessCode26()));
            }
            if (!reqObj.getReconsilationDate28().equalsIgnoreCase("")) {
                isoMsg.set(new ISOField(28, reqObj.getReconsilationDate28()));
            }
            if (!reqObj.getAmtTranProcessingFee30().equalsIgnoreCase("")) {
                isoMsg.set(new ISOField(30, reqObj.getAmtTranProcessingFee30()));
            }
            if (!reqObj.getAccuringInsituteIdCode32().equalsIgnoreCase("")) {
                isoMsg.set(new ISOField(32, reqObj.getAccuringInsituteIdCode32()));
            }
            if (!reqObj.getTrack2Data35().equalsIgnoreCase("")) {
                isoMsg.set(new ISOField(35, reqObj.getTrack2Data35()));
            }
            if (!reqObj.getRetriRefNo37().equalsIgnoreCase("")) {
                isoMsg.set(new ISOField(37, reqObj.getRetriRefNo37()));
            }
            if (reqObj.getAuthIdResCode38() != null && !reqObj.getAuthIdResCode38().equalsIgnoreCase("")) {
                isoMsg.set(new ISOField(38, reqObj.getAuthIdResCode38()));
            }
            if (reqObj.getResponseCode39() != null && !reqObj.getResponseCode39().equalsIgnoreCase("")) {
                isoMsg.set(new ISOField(39, reqObj.getResponseCode39()));
            }
            if (!reqObj.getCardAcceptorTemId41().equalsIgnoreCase("")) {
                isoMsg.set(new ISOField(41, reqObj.getCardAcceptorTemId41()));
            }
            if (!reqObj.getCardAcceptorIdCode42().equalsIgnoreCase("")) {
                isoMsg.set(new ISOField(42, reqObj.getCardAcceptorIdCode42()));
            }
            if (!reqObj.getAdditionalResData44().equalsIgnoreCase("")) {
                isoMsg.set(new ISOField(44, reqObj.getAdditionalResData44()));
            }
            if (!reqObj.getAdditionalDataNational47().equalsIgnoreCase("")) {
                isoMsg.set(new ISOField(47, reqObj.getAdditionalDataNational47()));
            }
            if (!reqObj.getAdditionalDataPrivate48().equalsIgnoreCase("")) {
                isoMsg.set(new ISOBinaryField(48, ByteConversionUtils.hexStringToByteArray(reqObj.getAdditionalDataPrivate48())));
            }
            if (!reqObj.getCurrCodeTransaction49().equalsIgnoreCase("")) {
                isoMsg.set(new ISOField(49, reqObj.getCurrCodeTransaction49()));
            }
            if (!reqObj.getCurrCodeStatleMent50().equalsIgnoreCase("")) {
                isoMsg.set(new ISOField(50, reqObj.getCurrCodeStatleMent50()));
            }
            if (reqObj.getPinData52() != null && reqObj.getPinData52().length != 0) {
                isoMsg.set(new ISOBinaryField(52, reqObj.getPinData52()));
            }
            if (!reqObj.getSecRelatedContInfo53().equalsIgnoreCase("")) {
                isoMsg.set(new ISOBinaryField(53, ByteConversionUtils.hexStringToByteArray(reqObj.getSecRelatedContInfo53())));
            }
            if (!reqObj.getAddlAmt54().equalsIgnoreCase("")) {
                isoMsg.set(new ISOField(54, reqObj.getAddlAmt54()));
            }
            if (!reqObj.getIccCardSystemRelatedData55().equalsIgnoreCase("")) {
                isoMsg.set(new ISOBinaryField(55, ByteConversionUtils.hexStringToByteArray(reqObj.getIccCardSystemRelatedData55())));
            }
            if (!reqObj.getMsgReasonCode56().equalsIgnoreCase("")) {
                isoMsg.set(new ISOField(56, reqObj.getMsgReasonCode56()));
            }
            if (!reqObj.getEchoData59().equalsIgnoreCase("")) {
                isoMsg.set(new ISOField(59, reqObj.getEchoData59()));
            }
            if (!reqObj.getReservedData62().equalsIgnoreCase("")) {
                isoMsg.set(new ISOField(62, reqObj.getReservedData62()));
            }
            if (reqObj.getMessageAuthenticationCodeField64() != null && reqObj.getMessageAuthenticationCodeField64().length != 0) {
                isoMsg.set(new ISOBinaryField(64, reqObj.getMessageAuthenticationCodeField64()));
            }
            if (!reqObj.getDataRecord72().equalsIgnoreCase("")) {
                isoMsg.set(new ISOField(72, reqObj.getDataRecord72()));
            }
            if (!reqObj.getReservedData124().equalsIgnoreCase("")) {
                isoMsg.set(new ISOField(124, reqObj.getReservedData124()));
            }
            // Set 128 bit data
            if (reqObj.getMacExt128() != null && reqObj.getMacExt128().length != 0) {
                isoMsg.set(new ISOBinaryField(128, reqObj.getMacExt128()));
            }

            // Packing the ISO
            isoMsg.setPackager(new MadaPackager());
            isoRequest = isoMsg.pack();
//            isoMsg.dump(new PrintStream(System.err), "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isoRequest;
    }

    public static byte[] createISORequest(ISOMsg isoMsg) {
        byte[] isoRequest = new byte[1024];

        // Create the ISO message
//        ISOMsg isoMsg = new ISOMsg();
        try {
            // Packing the ISO
            isoMsg.setPackager(new MadaPackager());
            isoRequest = isoMsg.pack();
//            isoMsg.dump(new PrintStream(System.err), "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isoRequest;
    }


    /**
     * This method unpacks the raw data and packages it into a ISOMessage format
     * as specified by the format of the packager that ViVO Pay determines. It
     * also validates the generated ISOMessage
     * <p>
     * //  * @param bytes
     * [] rawBytes
     *
     * @return ISOMsg
     * // * @throws ISOException
     */

    public static ISOMsg unpackISOFormat(byte[] rawBytes) throws ISOException {
        ISOMsg isoMessage = new ISOMsg();
        isoMessage.setPackager(new MadaPackager());
        int length = 4;
        if (AppInit.HITTING_LIVE_SERVER)
            length = 14;

        String hexReq = ByteConversionUtils.byteArrayToHexString(rawBytes, rawBytes.length, false);
        int reqLen = Integer.parseInt(hexReq.substring(0, 4), 16);
        byte[] afterBuffer = ISOUtil.hex2byte(hexReq.substring(length, hexReq.length()).trim().getBytes(), 0, reqLen);
        System.out.println(">>> " + ByteConversionUtils.byteArrayToHexString(afterBuffer, afterBuffer.length, true));
        if (reqLen == afterBuffer.length) {
            isoMessage.unpack(afterBuffer);
            isoMessage.dump(new PrintStream(System.err), "");
        } else {
            System.out.println("Receive the packet length not match...");
        }
        return isoMessage;
    }

    /**
     * This method used for read the echo custom_dialog_transparent connection request and sends a echo
     * response to the server
     * <p>
     * //* @param _hostName
     * //* @param _portString
     * //* @param reqBuffer
     * //* @param requestSocket
     *
     * @return boolean
     */
    public boolean echoTestConnection(Socket requestSocket) {
        boolean echoTestConnection = false;
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        ISOMsg isoMessage = new ISOMsg();
        try {
            System.out.println("Echo Test Connection starting...");
            // 1: get Input and Output streams
            bis = new BufferedInputStream(requestSocket.getInputStream());
            bos = new BufferedOutputStream(requestSocket.getOutputStream());
            // 2: Receive the response data
            byte[] buffer = new byte[1024];
            int nBytes = -1;
            while ((nBytes = bis.read(buffer)) >= 0) {
                System.out.println(">>> Reading from Server: " + ByteConversionUtils.byteArrayToHexString(buffer, nBytes, true));

                // 3: If length of pack is correct then unpack otherwise ignore packet
                isoMessage = unpackISOFormat(buffer);
                String echoMTI = (String) isoMessage.getMTI();
                String echoNetworkCode = (String) isoMessage.getValue(70);

                System.out.println("Echo ConstantValue : " + echoMTI + " Network Code : " + echoNetworkCode);
                //   if(echoMTI.equals(ECHO_REQUEST_MTI) && echoNetworkCode.equals(ECHO_NETWORK_CODE)) {
//                byte[] reqBuffer = createEchoResponse();
//                bos.write(reqBuffer);
                bos.flush();
                echoTestConnection = true;
                break;
                // }
            }
        } catch (UnknownHostException unknownHost) {
            System.err.println("You are trying to connect to an unknown host!");
            unknownHost.printStackTrace();
        } catch (IOException ioException) {
            System.err.println("Unable receive/send the data");
            ioException.printStackTrace();
        } catch (Exception exception) {
            System.err.println("Data received in unknown format");
            exception.printStackTrace();
        }
        return echoTestConnection;
    }

    public String getMessageCode(String valueMessageCode) {
        if (valueMessageCode.equalsIgnoreCase(ConstantApp.TERMINAL_PROCESSED)) {
            valueMessageCode = ConstantAppValue.TERMINAL_PROCESSED;
        } else if (valueMessageCode.equalsIgnoreCase(ConstantApp.ICC_PROCESSED)) {
            valueMessageCode = ConstantAppValue.ICC_PROCESSED;
        } else if (valueMessageCode.equalsIgnoreCase(ConstantApp.UNDER_FLOOR_LIMIT)) {
            valueMessageCode = ConstantAppValue.UNDER_FLOOR_LIMIT;
        } else if (valueMessageCode.equalsIgnoreCase(ConstantApp.TER_RANDOM_SEL)) {
            valueMessageCode = ConstantAppValue.TER_RANDOM_SEL;
        } else if (valueMessageCode.equalsIgnoreCase(ConstantApp.ONLINE_ICC)) {
            valueMessageCode = ConstantAppValue.ONLINE_ICC;
        } else if (valueMessageCode.equalsIgnoreCase(ConstantApp.ONLINE_CARD_ACCEPTOR)) {
            valueMessageCode = ConstantAppValue.ONLINE_CARD_ACCEPTOR;
        } else if (valueMessageCode.equalsIgnoreCase(ConstantApp.ONLINE_TERMINAL)) {
            valueMessageCode = ConstantAppValue.ONLINE_TERMINAL;
        } else if (valueMessageCode.equalsIgnoreCase(ConstantApp.ONLINE_CARD_ISSUER)) {
            valueMessageCode = ConstantAppValue.ONLINE_CARD_ISSUER;
        } else if (valueMessageCode.equalsIgnoreCase(ConstantApp.MERCHANT_SUSPICIOUS)) {
            valueMessageCode = ConstantAppValue.MERCHANT_SUSPICIOUS;
        } else if (valueMessageCode.equalsIgnoreCase(ConstantApp.FALLBACK_ICCTOMSR)) {
            valueMessageCode = ConstantAppValue.FALLBACK_ICCTOMSR;
        } else if (valueMessageCode.equalsIgnoreCase(ConstantApp.CLESS_TXN)) {
            valueMessageCode = ConstantAppValue.CLESS_TXN;
        } else if (valueMessageCode.equalsIgnoreCase(ConstantApp.CLESS_TXN_ADVICE)) {
            valueMessageCode = ConstantAppValue.CLESS_TXN_ADVICE;
        } else if (valueMessageCode.equalsIgnoreCase(ConstantApp.CUSTOMER_CANCELLATION)) {
            valueMessageCode = ConstantAppValue.CUSTOMER_CANCELLATION;
        } else if (valueMessageCode.equalsIgnoreCase(ConstantApp.UNSPECIFIED)) {
            valueMessageCode = ConstantAppValue.UNSPECIFIED;
        } else if (valueMessageCode.equalsIgnoreCase(ConstantApp.SUSPECTED_MALFUNC)) {
            valueMessageCode = ConstantAppValue.SUSPECTED_MALFUNC;
        } else if (valueMessageCode.equalsIgnoreCase(ConstantApp.FORMAT_ERROR)) {
            valueMessageCode = ConstantAppValue.FORMAT_ERROR;
        } else if (valueMessageCode.equalsIgnoreCase(ConstantApp.INCORRECT_AMOUNT)) {
            valueMessageCode = ConstantAppValue.INCORRECT_AMOUNT;
        } else if (valueMessageCode.equalsIgnoreCase(ConstantApp.RES_RECEIVED_LATE)) {
            valueMessageCode = ConstantAppValue.RES_RECEIVED_LATE;
        } else if (valueMessageCode.equalsIgnoreCase(ConstantApp.UNABLE_COMPLETE_TXN)) {
            valueMessageCode = ConstantAppValue.UNABLE_COMPLETE_TXN;
        } else if (valueMessageCode.equalsIgnoreCase(ConstantApp.UNABLE_DELIVER_MSG)) {
            valueMessageCode = ConstantAppValue.UNABLE_DELIVER_MSG;
        } else if (valueMessageCode.equalsIgnoreCase(ConstantApp.INVALID_RESP)) {
            valueMessageCode = ConstantAppValue.INVALID_RESP;
        } else if (valueMessageCode.equalsIgnoreCase(ConstantApp.TIMEOUT_WAITING_RESP)) {
            valueMessageCode = ConstantAppValue.TIMEOUT_WAITING_RESP;
        } else if (valueMessageCode.equalsIgnoreCase(ConstantApp.MAC_FAILURE)) {
            valueMessageCode = ConstantAppValue.MAC_FAILURE;
        }
        return valueMessageCode;
    }

    //    public String getFunctionCode(String valueFunctionCode) {
//        if (valueFunctionCode.equalsIgnoreCase(Constant.AMOUNT_ACCURATE)) {
//            valueFunctionCode = ConstantValue.AMOUNT_ACCURATE;
//        } else if (valueFunctionCode.equalsIgnoreCase(Constant.AMOUNT_ESTIMATE)) {
//            valueFunctionCode = ConstantValue.AMOUNT_ESTIMATE;
//        } else if (valueFunctionCode.equalsIgnoreCase(Constant.PREAUTH_INITIAL_COMPLETION)) {
//            valueFunctionCode = ConstantValue.PREAUTH_INITIAL_COMPLETION;
//        } else if (valueFunctionCode.equalsIgnoreCase(Constant.AUTHORIZATION_EXTENSION)) {
//            valueFunctionCode = ConstantValue.AUTHORIZATION_EXTENSION;
//        } else if (valueFunctionCode.equalsIgnoreCase(Constant.FINANCIAL_TRANSACTION)) {
//            valueFunctionCode = ConstantValue.FINANCIAL_TRANSACTION;
//        } else if (valueFunctionCode.equalsIgnoreCase(Constant.APPROVED_AUTH_SAME_AMOUNT)) {
//            valueFunctionCode = ConstantValue.APPROVED_AUTH_SAME_AMOUNT;
//        } else if (valueFunctionCode.equalsIgnoreCase(Constant.APPROVED_AUTH_DIFF_AMOUNT)) {
//            valueFunctionCode = ConstantValue.APPROVED_AUTH_DIFF_AMOUNT;
//        } else if (valueFunctionCode.equalsIgnoreCase(Constant.REPLACE_FIELDS_RECORD)) {
//            valueFunctionCode = ConstantValue.REPLACE_FIELDS_RECORD;
//        } else if (valueFunctionCode.equalsIgnoreCase(Constant.REPLACE_ENTIRE_RECORD)) {
//            valueFunctionCode = ConstantValue.REPLACE_ENTIRE_RECORD;
//        } else if (valueFunctionCode.equalsIgnoreCase(Constant.REPLACE_FILE)) {
//            valueFunctionCode = ConstantValue.REPLACE_FILE;
//        } else if (valueFunctionCode.equalsIgnoreCase(Constant.TXN_NOT_COMPLETED)) {
//            valueFunctionCode = ConstantValue.TXN_NOT_COMPLETED;
//        } else if (valueFunctionCode.equalsIgnoreCase(Constant.TERMINAL_RECONCILIATION)) {
//            valueFunctionCode = ConstantValue.TERMINAL_RECONCILIATION;
//        } else if (valueFunctionCode.equalsIgnoreCase(Constant.FORCE_RECONCILIATION)) {
//            valueFunctionCode = ConstantValue.FORCE_RECONCILIATION;
//        } else if (valueFunctionCode.equalsIgnoreCase(Constant.UNABLE_PARSE_MSG)) {
//            valueFunctionCode = ConstantValue.UNABLE_PARSE_MSG;
//        } else if (valueFunctionCode.equalsIgnoreCase(Constant.MAC_ERROR)) {
//            valueFunctionCode = ConstantValue.MAC_ERROR;
//        } else if (valueFunctionCode.equalsIgnoreCase(Constant.TERMINAL_REGISTRATION)) {
//            valueFunctionCode = ConstantValue.TERMINAL_REGISTRATION;
//        }
//        return valueFunctionCode;
//    }
    public String getFunctionCode(String valueFunctionCode) {
        String code = ConstantAppValue.FINANCIAL_TRANSACTION;
        if (valueFunctionCode.equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION)) {
            code = ConstantAppValue.AMOUNT_ESTIMATE;
        } else if (valueFunctionCode.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_FULL)) {
            code = ConstantAppValue.APPROVED_AUTH_SAME_AMOUNT;
        } else if (valueFunctionCode.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_MANUAL)) {
            code = ConstantAppValue.FINANCIAL_TRANSACTION;
        } else if (valueFunctionCode.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_PARTIAL)) {
            code = ConstantAppValue.APPROVED_AUTH_DIFF_AMOUNT;
        } else if (valueFunctionCode.equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION_VOID)) {
            code = ConstantAppValue.AMOUNT_ESTIMATE;
        } else if (valueFunctionCode.equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION_EXTENSION)) {
            code = ConstantAppValue.AUTHORIZATION_EXTENSION;
        } else if (valueFunctionCode.equalsIgnoreCase(ConstantApp.PURCHASE_REVERSAL)) {
            code = ConstantAppValue.TXN_NOT_COMPLETED;
        }
        return code;
    }

    public String getProcessCode(String valueProcessCode) {
        //for debit
        if (valueProcessCode.equalsIgnoreCase(ConstantApp.NOTXN_DEBIT)) {
            valueProcessCode = ConstantAppValue.NOTXN;
        } else if (valueProcessCode.equalsIgnoreCase(ConstantApp.PURCHASE)) {
            valueProcessCode = ConstantAppValue.PURCHASE;
        } else if (valueProcessCode.equalsIgnoreCase(ConstantApp.AUTHORIZATION_ADVICE)) {
            valueProcessCode = ConstantAppValue.PRE_AUTHORISATION;
        } else if (valueProcessCode.equalsIgnoreCase(ConstantApp.FINANCIAL_ADVISE)) {
            valueProcessCode = ConstantAppValue.PURCHASE;
        } else if (valueProcessCode.equalsIgnoreCase(ConstantApp.FINANCIAL_AUTHORIZATION)) {
            valueProcessCode = ConstantAppValue.PURCHASE;
        } else if (valueProcessCode.equalsIgnoreCase(ConstantApp.PURCHASE_NAQD)) {
            valueProcessCode = ConstantAppValue.PURCHASE_NAQD;
        } else if (valueProcessCode.equalsIgnoreCase(ConstantApp.PURCHASE_REVERSAL)) {
            valueProcessCode = ConstantAppValue.PURCHASE_REVERSAL;
        } else if (valueProcessCode.equalsIgnoreCase(ConstantApp.CASH_ADVANCE)) {
            valueProcessCode = ConstantAppValue.CASH_ADVANCE;
        } else if (valueProcessCode.equalsIgnoreCase(ConstantApp.REFUND)) {
            valueProcessCode = ConstantAppValue.REFUND;
        } else if (valueProcessCode.equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION)) {
            valueProcessCode = ConstantAppValue.PRE_AUTHORISATION;
        } else if (valueProcessCode.equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION_COMPLETE)) {
            valueProcessCode = ConstantAppValue.PRE_AUTHORISATION_COMPLETE;
        } else if (valueProcessCode.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_FULL)) {
            valueProcessCode = ConstantAppValue.PURCHASE_ADVICE_FULL;
        } else if (valueProcessCode.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_MANUAL)) {
            valueProcessCode = ConstantAppValue.PURCHASE_ADVICE_FULL;
        } else if (valueProcessCode.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_PARTIAL)) {
            valueProcessCode = ConstantAppValue.PURCHASE_ADVICE_PARTIAL;
        } else if (valueProcessCode.equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION_VOID)) {
            valueProcessCode = ConstantAppValue.PRE_AUTHORISATION_VOID;
        } else if (valueProcessCode.equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION_EXTENSION)) {
            valueProcessCode = ConstantAppValue.PRE_AUTHORISATION;
        } else if (valueProcessCode.equalsIgnoreCase(ConstantApp.NO_OF_TXNS)) {
            valueProcessCode = ConstantAppValue.NO_OF_TXNS;
        } else
            valueProcessCode = ConstantAppValue.PURCHASE_ADVICE_FULL;
        //for credit
       /* else if (valueProcessCode.equalsIgnoreCase(Constant.NOTXN_CREDIT)) {
            valueProcessCode = ConstantValue.NOTXN_CREDIT;
        } else if (valueProcessCode.equalsIgnoreCase(Constant.PURCHASE_CREDIT)) {
            valueProcessCode = ConstantValue.PURCHASE_CREDIT;
        } else if (valueProcessCode.equalsIgnoreCase(Constant.PURCHASE_CASHBACK_CREDIT)) {
            valueProcessCode = ConstantValue.PURCHASE_CASHBACK_CREDIT;
        } else if (valueProcessCode.equalsIgnoreCase(Constant.PURCHASE_REVERSAL_CREDIT)) {
            valueProcessCode = ConstantValue.PURCHASE_REVERSAL_CREDIT;
        } else if (valueProcessCode.equalsIgnoreCase(Constant.CASH_ADVANCE_CREDIT)) {
            valueProcessCode = ConstantValue.CASH_ADVANCE_CREDIT;
        } else if (valueProcessCode.equalsIgnoreCase(Constant.REFUND_CREDIT)) {
            valueProcessCode = ConstantValue.REFUND_CREDIT;
        } else if (valueProcessCode.equalsIgnoreCase(Constant.PRE_AUTHORISATION_CREDIT)) {
            valueProcessCode = ConstantValue.PRE_AUTHORISATION_CREDIT;
        } else if (valueProcessCode.equalsIgnoreCase(Constant.PRE_AUTHORISATION_COMPLET_CREDIT)) {
            valueProcessCode = ConstantValue.PRE_AUTHORISATION_COMPLET_CREDIT;
        } else if (valueProcessCode.equalsIgnoreCase(Constant.PURCHASE_ADVICE_CREDIT)) {
            valueProcessCode = ConstantValue.PURCHASE_ADVICE_CREDIT;
        } else if (valueProcessCode.equalsIgnoreCase(Constant.NO_OF_TXNS_CREDIT)) {
            valueProcessCode = ConstantValue.NO_OF_TXNS_CREDIT;
        }*/
        return valueProcessCode;
    }

    public String getMTI(String valueMTI) {
        if (valueMTI.equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION)) {
            valueMTI = ConstantAppValue.AUTH;
        } else if (valueMTI.equalsIgnoreCase(ConstantApp.OFFLINE_PURCHASE)) {
            valueMTI = ConstantAppValue.FINANCIAL_ADVISE;
        } else if (valueMTI.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_FULL)) {
            valueMTI = ConstantAppValue.FINANCIAL_ADVISE;
        } else if (valueMTI.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_PARTIAL)) {
            valueMTI = ConstantAppValue.FINANCIAL_ADVISE;
        } else if (valueMTI.equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION_VOID)) {
            valueMTI = ConstantAppValue.AUTH_ADVISE;
        } else if (valueMTI.equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION_EXTENSION)) {
            valueMTI = ConstantAppValue.AUTH_ADVISE;
        } else {
            valueMTI = ConstantAppValue.FINANCIAL;
        }
        return valueMTI;
    }
//    public String getMTI(String valueMTI) {
//        if (valueMTI.equalsIgnoreCase(Constant.ADMIN_NOTIFICATION)) {
//            valueMTI = ConstantValue.ADMIN_NOTIFICATION;
//        } else if (valueMTI.equalsIgnoreCase(Constant.AUTH)) {
//            valueMTI = ConstantValue.AUTH;
//        } else if (valueMTI.equalsIgnoreCase(Constant.AUTH_ADVISE)) {
//            valueMTI = ConstantValue.AUTH_ADVISE;
//        } else if (valueMTI.equalsIgnoreCase(Constant.AUTH_ADVISE_REPEAT)) {
//            valueMTI = ConstantValue.AUTH_ADVISE_REPEAT;
//        } else if (valueMTI.equalsIgnoreCase(Constant.FILEACTION)) {
//            valueMTI = ConstantValue.FILEACTION;
//        } else if (valueMTI.equalsIgnoreCase(Constant.FILEACTION_REPEAT)) {
//            valueMTI = ConstantValue.FILEACTION_REPEAT;
//        } else if (valueMTI.equalsIgnoreCase(Constant.FINANCIAL)) {
//            valueMTI = ConstantValue.FINANCIAL;
//        } else if (valueMTI.equalsIgnoreCase(Constant.NETWORK_MNGMT)) {
//            valueMTI = ConstantValue.NETWORK_MNGMT;
//        } else if (valueMTI.equalsIgnoreCase(Constant.RECONCILIATION)) {
//            valueMTI = ConstantValue.RECONCILIATION;
//        } else if (valueMTI.equalsIgnoreCase(Constant.RECONCILIATION_REPEAT)) {
//            valueMTI = ConstantValue.RECONCILIATION_REPEAT;
//        } else if (valueMTI.equalsIgnoreCase(Constant.REVERSAL)) {
//            valueMTI = ConstantValue.REVERSAL;
//        } else if (valueMTI.equalsIgnoreCase(Constant.REVERSAL_REPEAT)) {
//            valueMTI = ConstantValue.REVERSAL_REPEAT;
//        } else if (valueMTI.equalsIgnoreCase(Constant.FINANCIAL_ADVISE_REPEAT)) {
//            valueMTI = ConstantValue.FINANCIAL_ADVISE_REPEAT;
//        } else if (valueMTI.equalsIgnoreCase(Constant.FINANCIAL_ADVISE)) {
//            valueMTI = ConstantValue.FINANCIAL_ADVISE;
//        }
//        return valueMTI;
//    }

    public String getBusinessCode26(String valueProcessCode) {
        return "";
    }

    public String getBitmap(MadaRequest reqObj) {
        String RES_TPDU_HEADER = context.getString(R.string.TPDU_HEADER);
        int TPDU_HEADER_LENGTH = 5;
        byte[] isoBufferEchoResponse = createISORequest(reqObj);
        byte[] tpduHeader = ISOUtil.hex2byte(RES_TPDU_HEADER);
        byte[] isoBufferEchoResponsetpduHeader = ISOUtil.concat(tpduHeader, isoBufferEchoResponse);
        byte[] echoResponseLength = ByteConversionUtils.intToByteArray(isoBufferEchoResponse.length + TPDU_HEADER_LENGTH);
        byte[] finalData = ISOUtil.concat(echoResponseLength, isoBufferEchoResponsetpduHeader);
        String hexa = ByteConversionUtils.byteArrayToHexString(finalData, finalData.length, false);
        Logger.v("satyisowithdumpmac--" + hexa);
        String bitmap = hexa.substring(22, 38);
        Logger.v("satybitmap --" + bitmap);
        return bitmap;
    }

    public byte[] getMAC(MadaRequest reqObj) {
        Logger.v("ReqObj : "+ reqObj);
        PINPadExtendDevice device = SDKDevice.getInstance(context).getDeviceConnectPin();

        byte[] finalData = createISORequest(reqObj);
        String hexa = ByteConversionUtils.byteArrayToHexString(finalData, finalData.length, false);
        Logger.v("finalData Hexa--" + hexa);
        String valueBitmap = hexa.substring(8, 40);
        Logger.v("finalData bitmap --" + valueBitmap);

        String valueMTI;
        valueMTI = reqObj.getMti0();

        StringBuilder stringBuilder = new StringBuilder();
        if (valueMTI.equalsIgnoreCase(ConstantAppValue.AUTH) || valueMTI.equalsIgnoreCase(ConstantAppValue.AUTH_ADVISE) || valueMTI.equalsIgnoreCase(ConstantAppValue.AUTH_ADVISE_REPEAT)
                || valueMTI.equalsIgnoreCase(ConstantAppValue.FINANCIAL) || valueMTI.equalsIgnoreCase(ConstantAppValue.REVERSAL) || valueMTI.equalsIgnoreCase(ConstantAppValue.REVERSAL_REPEAT)) {
            try {
                stringBuilder.append(getHexaString(valueMTI));
                stringBuilder.append(valueBitmap);
                stringBuilder.append(getHexaString(reqObj.getPrimaryAccNo2()));
                stringBuilder.append(getHexaString(reqObj.getProcessingCode3()));
                stringBuilder.append(getHexaString(addZeros(reqObj.getAmtTransaction4(), 12)));
                stringBuilder.append(getHexaString(addZeros(reqObj.getSystemTraceAuditnumber11(), 6)));
                stringBuilder.append(getHexaString(reqObj.getTimeLocalTransaction12()));
                if(!AppInit.VERSION_6_0_5) {
                    stringBuilder.append(getHexaString(reqObj.getAdditionalDataNational47()));
                }
                stringBuilder.append(reqObj.getSecRelatedContInfo53());
                if(!AppInit.VERSION_6_0_5) {
                    stringBuilder.append(reqObj.getIccCardSystemRelatedData55());
                }
            }catch (Throwable t) {
                t.printStackTrace();
            }

        } else if (valueMTI.equalsIgnoreCase(ConstantAppValue.FINANCIAL_ADVISE) || valueMTI.equalsIgnoreCase(ConstantAppValue.FINANCIAL_ADVISE_REPEAT)) {
            stringBuilder.append(getHexaString(ConstantAppValue.FINANCIAL_ADVISE));
            stringBuilder.append(valueBitmap);
            stringBuilder.append(getHexaString(reqObj.getPrimaryAccNo2()));
            stringBuilder.append(getHexaString(reqObj.getProcessingCode3()));
            stringBuilder.append(getHexaString(addZeros(reqObj.getAmtTransaction4(), 12)));
            stringBuilder.append(getHexaString(addZeros(reqObj.getSystemTraceAuditnumber11(), 6)));
            stringBuilder.append(getHexaString(reqObj.getTimeLocalTransaction12()));
            if (reqObj.getResponseCode39()!=null)
            stringBuilder.append(getHexaString(reqObj.getResponseCode39()));
            if(!AppInit.VERSION_6_0_5) {
                stringBuilder.append(getHexaString(reqObj.getAdditionalDataNational47()));
            }
            stringBuilder.append(reqObj.getSecRelatedContInfo53());
            if(!AppInit.VERSION_6_0_5) {
                stringBuilder.append(reqObj.getIccCardSystemRelatedData55());
            }

        } else if (valueMTI.equalsIgnoreCase(ConstantAppValue.FILEACTION) || valueMTI.equalsIgnoreCase(ConstantAppValue.FILEACTION_REPEAT)) {
            stringBuilder.append(getHexaString(ConstantAppValue.FILEACTION));
            stringBuilder.append(valueBitmap);
            stringBuilder.append(getHexaString(addZeros(reqObj.getSystemTraceAuditnumber11(), 6)));
            stringBuilder.append(getHexaString(reqObj.getTimeLocalTransaction12()));
            stringBuilder.append(reqObj.getSecRelatedContInfo53());

        } else if (valueMTI.equalsIgnoreCase(ConstantAppValue.RECONCILIATION) || valueMTI.equalsIgnoreCase(ConstantAppValue.RECONCILIATION_REPEAT)) {
            stringBuilder.append(getHexaString(ConstantAppValue.RECONCILIATION));
            stringBuilder.append(valueBitmap);
            stringBuilder.append(getHexaString(addZeros(reqObj.getSystemTraceAuditnumber11(), 6)));
            stringBuilder.append(getHexaString(reqObj.getTimeLocalTransaction12()));
            stringBuilder.append(reqObj.getSecRelatedContInfo53());
            stringBuilder.append(getHexaString(reqObj.getReservedData124()));

        } else if (valueMTI.equalsIgnoreCase(ConstantAppValue.ADMIN_NOTIFICATION)) {
            //dont set any value

        } else if (valueMTI.equalsIgnoreCase(ConstantAppValue.NETWORK_MNGMT)) {
            //dont set any value

        }
        Logger.v("ksn_sending_to_host----"+reqObj.getSecRelatedContInfo53());

        stringBuilder.append("80");
        Logger.v("Mac String", stringBuilder.toString());
        Logger.v("Mac String --" + ISOUtil.hex2byte(stringBuilder.toString()).length);
        String finalString = addZerosIfRequired(stringBuilder.toString());
        Logger.v("finalString --" + finalString);
        Logger.v("Mac String --" + ISOUtil.hex2byte(finalString).length);

        //mac encryption
        KeyInfo keyInfo = new KeyInfo(PINPadDevice.KEY_TYPE_TDUKPT, 1, AlgorithmConstants.ALG_3DES);
        byte[] outputMac = new byte[0];
        try {
            outputMac = device.calculateMac(keyInfo, AlgorithmConstants.ALG_MAC_METHOD_SE919, ISOUtil.hex2byte(finalString));
        } catch (DeviceException e) {
            e.printStackTrace();
            Logger.v("Exception -"+e.toString());
        }
        Logger.v("outputMac --" + outputMac.length);
        String outputMacStr = ByteConversionUtils.byteArrayToHexString(outputMac);
        Logger.v("outputMac -V1-" + outputMacStr);
        Logger.v("outputMac --" + ByteConversionUtils.hexStringToByteArray("FFFFFFFF").length);

        Logger.v("get_ksn----"+SDKDevice.getInstance(context).getKSN());
//        String mac = new String(outputMac);
        Logger.v(outputMac);
        byte[] concatArray = new byte[4];
        System.arraycopy(outputMac, 0, concatArray, 0, 4);
        Logger.v("outputMac --"+concatArray);
        Logger.v(new String(ISOUtil.concat(concatArray, ByteConversionUtils.hexStringToByteArray("FFFFFFFF"))));
        return ISOUtil.concat(concatArray, ByteConversionUtils.hexStringToByteArray("FFFFFFFF"));
    }

    public static boolean validateMac(ISOMsg isoMsg, PINPadExtendDevice device) throws ISOException {
        byte[] isoFinalBuffer = createISORequest(isoMsg);
        String packet = Dump.getHexDump(isoFinalBuffer).replaceAll(" ", "");
        Logger.v("isoFinalBuffer --" + Dump.getHexDump(isoFinalBuffer).replaceAll(" ", ""));
        StringBuilder stringBuilder = new StringBuilder();
        String valueMTI_01 = isoMsg.getMTI();
        String valueBitmap = (String) isoMsg.getValue(1);
        String valuePAN_02 = (String) isoMsg.getValue(2);
        String valueProcessingCode_03 = (String) isoMsg.getValue(3);
        String valueAmountTransaction_04 = (String) isoMsg.getValue(4);
        String valueSystemTraceAuditNumber_11 = (String) isoMsg.getValue(11);
        String dateTimeLocalTransaction_12 = (String) isoMsg.getValue(12);
        String actionCode_39 = (String) isoMsg.getValue(39);
        String nationalData_47 = (String) isoMsg.getValue(47);
        byte[] data55 = isoMsg.getBytes(55);
        String EVMdata_55 = "";
        if (data55 != null) {
            EVMdata_55 = Dump.getHexDump(data55).replaceAll(" ", "");
        }
        String SRCI_53 = Dump.getHexDump(isoMsg.getBytes(53)).replaceAll(" ", "");
        byte[] dataRecord_72 = isoMsg.getBytes(72);
        String data_124 = (String) isoMsg.getValue(124);

        String macResponcce = "";
        if (isoMsg.getBytes(128) != null && isoMsg.getBytes(128).length != 0)
            macResponcce = Dump.getHexDump(isoMsg.getBytes(128)).replaceAll(" ", "");
        else
            macResponcce = Dump.getHexDump(isoMsg.getBytes(64)).replaceAll(" ", "");

//        stringBuilder.append(getHexaString(valueMTI_01));
        Logger.v("macResponcce --" + macResponcce);
        macResponcce = macResponcce.replaceAll("FFFFFFFF","");
        Logger.v("macResponcefromhost --" + macResponcce);
        Logger.v("GetKSNfor_response--"+SDKDevice.getInstance(context).getMyCurrentKSN());
        Logger.v("Bitmap --" + packet.substring(0, 40));
        // MTI and BITMAP
        stringBuilder.append(packet.substring(0, 40));

        if (!ConstantAppValue.FILEACTION_RESPONSE.equalsIgnoreCase(valueMTI_01) && !ConstantAppValue.RECONSILATION_RESPONSE.equalsIgnoreCase(valueMTI_01)) {
            stringBuilder.append(getHexaString(valuePAN_02));
            stringBuilder.append(getHexaString(valueProcessingCode_03));
            stringBuilder.append(getHexaString(addZeros(valueAmountTransaction_04, 12)));
        }
        stringBuilder.append(getHexaString(addZeros(valueSystemTraceAuditNumber_11, 6)));
        stringBuilder.append(getHexaString(dateTimeLocalTransaction_12));
        stringBuilder.append(getHexaString(actionCode_39));
        if(!AppInit.VERSION_6_0_5) {
            if (!ConstantAppValue.FILEACTION_RESPONSE.equalsIgnoreCase(valueMTI_01) && !ConstantAppValue.RECONSILATION_RESPONSE.equalsIgnoreCase(valueMTI_01))
                stringBuilder.append(getHexaString(nationalData_47));
        }
        stringBuilder.append(SRCI_53);
        if(!AppInit.VERSION_6_0_5) {
            if (!ConstantAppValue.REVERSAL_RESPONSE.equalsIgnoreCase(valueMTI_01)
                    && !ConstantAppValue.FILEACTION_RESPONSE.equalsIgnoreCase(valueMTI_01) && !ConstantAppValue.RECONSILATION_RESPONSE.equalsIgnoreCase(valueMTI_01))
                if (EVMdata_55.trim().length() != 0)
                    stringBuilder.append(EVMdata_55);
        }
        if (ConstantAppValue.FILEACTION_RESPONSE.equalsIgnoreCase(valueMTI_01)) {
            assert dataRecord_72 != null;
            if (dataRecord_72 != null && dataRecord_72.length != 0) {
                Logger.v("Dump.getHexDump(dataRecord_72) --" + Dump.getHexDump(dataRecord_72));
                stringBuilder.append(Dump.getHexDump(dataRecord_72).replaceAll(" ", ""));
            }
        }
        if (ConstantAppValue.RECONSILATION_RESPONSE.equalsIgnoreCase(valueMTI_01))
            stringBuilder.append(getHexaString(data_124));

        stringBuilder.append("80");
        String finalString = addZerosIfRequired(stringBuilder.toString());
        Logger.v("validate_mac_string----"+ stringBuilder.toString());
        Logger.v("validate_mac_string_final----"+ finalString);
        Logger.v("validate_mac_string_final_length-----"+ finalString.length());
        Logger.v("Mac_String_response_length--" + ISOUtil.hex2byte(stringBuilder.toString()).length);
        Logger.v("ksn_response--" + SRCI_53.substring(0, 20));
        Logger.v("ksn_response--" + SRCI_53);

        //mac encryption
        KeyInfo keyInfo = new KeyInfo(PINPadDevice.KEY_TYPE_TDUKPT, 1, AlgorithmConstants.ALG_3DES);
        boolean outputMac = false;
        try {
            outputMac = device.verifyResponseMac(keyInfo, ISOUtil.hex2byte(stringBuilder.toString()),AlgorithmConstants.ALG_MAC_METHOD_SE919,ISOUtil.hex2byte(macResponcce),0);
          //  outputMac = device.verifyResponseMac(keyInfo, ISOUtil.hex2byte(finalString),AlgorithmConstants.ALG_MAC_METHOD_SE919,ISOUtil.hex2byte(macResponcce),0);
        } catch (DeviceException e) {
            e.printStackTrace();
            Logger.v("Exception -"+e.toString());
        }
        Logger.v("GetKSNfor_response_aftermacval--"+SDKDevice.getInstance(context).getMyCurrentKSN());
        Logger.v("outputMacResponse --" + outputMac);
        Logger.v("outputMacResponse --" + ByteConversionUtils.hexStringToByteArray("FFFFFFFF").length);
        SDKDevice.getInstance(context).incrementKSN();

//        byte[] concatArray = new byte[4];
//        System.arraycopy(outputMac, 0, concatArray, 0, 4);
//        Logger.v(concatArray);
//        Logger.v("outputMac --");
//        String calMac = Dump.getHexDump(concatArray).replaceAll(" ", "").substring(0, 8) + "FFFFFFFF";
//        Logger.v("macResponcce -1-" + macResponcce);
//        Logger.v("macResponcce -final-" + calMac.equalsIgnoreCase(macResponcce));
        return outputMac;
    }

    public static String addZerosIfRequired(String str) {
        int length = ISOUtil.hex2byte(str).length;
        for (int i = 0; i < 8; i++) {
            if (length % 8 != 0) {
                str = str + "00";
                length = ISOUtil.hex2byte(str).length;
            } else {
                return str;
            }
        }
        return "";
    }

    public static String addZeros(String amtTransaction4, int i) {
        long amount = Long.parseLong(amtTransaction4);
        String format = "%0" + i + "d";
        Logger.v("amount --" + String.format(format, amount));
        return String.format(format, amount);
    }


    public static String getHexaString(String str) {
        return String.format("%x", new BigInteger(1, str.getBytes()));
    }

    public byte[] getHexaByte(String str) {
        return ByteConversionUtils.hexStringToByteArray(String.format("%x", new BigInteger(1, str.getBytes())));
    }

    public String getTrack2DataValidate(String track2) {
//        if (track_2_eqv_data != null) {
//            String track2 = ISOUtils.hexString(track_2_eqv_data);
        if (track2.endsWith("F")) {
            Logger.v("emvTransInfo.getTrack_2_eqv_data() -" + track2);
            track2 = track2.substring(0, track2.length() - 1);
        }
        Logger.v("emvTrsanInfo.getTrack_2_eqv_data() -" + track2);
        if (37 <= track2.trim().length())
            return (track2.substring(0, 37));
        else
            return track2;
//        }
//        return null;
    }


    public static String getStringFromHex(String hex) {
        if (hex != null && hex.length() != 0) {
            StringBuilder str = new StringBuilder();
            for (int i = 0; i < hex.length(); i += 2) {
                str.append((char) Integer.parseInt(hex.substring(i, i + 2), 16));
            }
            return str.toString();
        } else
            return "";
    }


}
