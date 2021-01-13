package com.tarang.dpq2.base.room_database.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Arrays;

@Entity
public class SAFModelEntity {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    //Request  Fields
    @ColumnInfo(name = "mti0")
    private String mti0 = "";

    @ColumnInfo(name = "cardIndicator")
    private String cardIndicator = "";

    @ColumnInfo(name = "tsii")
    private String tsii = "";

    @ColumnInfo(name = "kernalID")
    private String kernalID = "";

    @ColumnInfo(name = "primaryAccNo2")
    private String primaryAccNo2 = "";

    @ColumnInfo(name = "processingCode3")
    private String processingCode3 = "";

    @ColumnInfo(name = "amtTransaction4")
    private String amtTransaction4 = "";

    @ColumnInfo(name = "transmissionDateTime7")
    private String transmissionDateTime7 = "";

    @ColumnInfo(name = "systemTraceAuditnumber11")
    private String systemTraceAuditnumber11 = "";

    @ColumnInfo(name = "timeLocalTransaction12")
    private String timeLocalTransaction12 = "";

    @ColumnInfo(name = "dateExpiration14")
    private String dateExpiration14 = "";

    @ColumnInfo(name = "posEntrymode22")
    private String posEntrymode22 = "";

    @ColumnInfo(name = "cardSequenceNumber23")
    private String cardSequenceNumber23 = "";

    @ColumnInfo(name = "functioncode24")
    private String functioncode24 = "";

    @ColumnInfo(name = "posConditionCode25")
    private String posConditionCode25 = "";

    @ColumnInfo(name = "posPinCaptureCode26")
    private String posPinCaptureCode26 = "";

    @ColumnInfo(name = "amtTransFee28")
    private String amtTransFee28 = "";

    @ColumnInfo(name = "amtTranProcessingFee30")
    private String amtTranProcessingFee30 = "";

    @ColumnInfo(name = "accuringInsituteIdCode32")
    private String accuringInsituteIdCode32 = "";

    @ColumnInfo(name = "track2Data35")
    private String track2Data35 = "";

    @ColumnInfo(name = "retriRefNo37")
    private String retriRefNo37 = "";

    @ColumnInfo(name = "authIdResCode38")
    private String authIdResCode38 = "";

    @ColumnInfo(name = "responseCode39")
    private String responseCode39 = "";

    @ColumnInfo(name = "cardAcceptorTemId41")
    private String cardAcceptorTemId41 = "";

    @ColumnInfo(name = "cardAcceptorIdCode42")
    private String cardAcceptorIdCode42 = "";

    @ColumnInfo(name = "additionalDataNational47")
    private String additionalDataNational47 = "";

    @ColumnInfo(name = "additionalDataPrivate48")
    private String additionalDataPrivate48 = "";

    @ColumnInfo(name = "currCodeTransaction49")
    private String currCodeTransaction49 = "";

    @ColumnInfo(name = "currCodeStatleMent50")
    private String currCodeStatleMent50 = "";

    @ColumnInfo(name = "pinData52")
    private String pinData52 = "";

    @ColumnInfo(name = "secRelatedContInfo53")
    private String secRelatedContInfo53 = "";

    @ColumnInfo(name = "addlAmt54")
    private String addlAmt54 = "";

    @ColumnInfo(name = "iccCardSystemRelatedData55")
    private String iccCardSystemRelatedData55 = "";

    @ColumnInfo(name = "iccCardSystemRelatedData55_final")
    private String iccCardSystemRelatedData55_final = "";

    @ColumnInfo(name = "msgReasonCode56")
    private String msgReasonCode56 = "";

    @ColumnInfo(name = "echoData59")
    private String echoData59 = "";

    @ColumnInfo(name = "reservedData62")
    private String reservedData62 = "";

    @ColumnInfo(name = "reservedData62Responce")
    private String reservedData62Responce = "";

    @ColumnInfo(name = "messageAuthenticationCodeField64")
    private byte[] messageAuthenticationCodeField64;

    @ColumnInfo(name = "dataRecord72")
    private String dataRecord72 = "";

    @ColumnInfo(name = "reservedData124")
    private String reservedData124 = "";

    @ColumnInfo(name = "macExt128")
    private byte[] macExt128;

    // Transaction Flow Fields
    @ColumnInfo(name = "nameTransactionTag")
    private String nameTransactionTag;

    @ColumnInfo(name = "safEntity")
    private boolean saf = false;

    @ColumnInfo(name = "statusTransaction")
    private int statusTransaction;

    @ColumnInfo(name = "responseData44")
    private String responseData44 = "";

    public String getIccCardSystemRelatedData55_final() {
        return iccCardSystemRelatedData55_final;
    }

    public void setIccCardSystemRelatedData55_final(String iccCardSystemRelatedData55_final) {
        this.iccCardSystemRelatedData55_final = iccCardSystemRelatedData55_final;
    }

    public String getResponseData44() {
        return responseData44;
    }

    public void setResponseData44(String responseData44) {
        this.responseData44 = responseData44;
    }

    public int getStatusTransaction() {
        return statusTransaction;
    }

    public void setStatusTransaction(int statusTransaction) {
        this.statusTransaction = statusTransaction;
    }

    public String getReservedData62Responce() {
        return reservedData62Responce;
    }

    public void setReservedData62Responce(String reservedData62Responce) {
        this.reservedData62Responce = reservedData62Responce;
    }

    public boolean isSaf() {
        return saf;
    }

    public void setSaf(boolean saf) {
        this.saf = saf;
    }

    @ColumnInfo(name = "startTimeTransaction")
    private String startTimeTransaction = "dd/MM/yyyy HH:mm:ss";

    @ColumnInfo(name = "endTimeTransaction")
    private String endTimeTransaction = "dd/MM/yyyy HH:mm:ss";

    @ColumnInfo(name = "startTimeConnection")
    private String startTimeConnection = "dd/MM/yyyy HH:mm:ss";

    @ColumnInfo(name = "endTimeConnection")
    private String endTimeConnection = "dd/MM/yyyy HH:mm:ss";

    @ColumnInfo(name = "modeTransaction")
    private String modeTransaction;

    //Request  Fields
    @ColumnInfo(name = "status_mportal")
    private boolean status_mportal = false;

    //Request  Fields
    @ColumnInfo(name = "request_mportal")
    private String request_mportal = "";

    public boolean isStatus_mportal() {
        return status_mportal;
    }

    public void setStatus_mportal(boolean status_mportal) {
        this.status_mportal = status_mportal;
    }

    public String getRequest_mportal() {
        return request_mportal;
    }

    public void setRequest_mportal(String request_mportal) {
        this.request_mportal = request_mportal;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getCardIndicator() {
        return cardIndicator;
    }

    public void setCardIndicator(String cardIndicator) {
        this.cardIndicator = cardIndicator;
    }

    public String getKernalID() {
        return kernalID;
    }

    public void setKernalID(String kernalID) {
        this.kernalID = kernalID;
    }

    public String getMti0() {
        return mti0;
    }

    public void setMti0(String mti0) {
        this.mti0 = mti0;
    }

    public String getPrimaryAccNo2() {
        return primaryAccNo2;
    }

    public void setPrimaryAccNo2(String primaryAccNo2) {
        this.primaryAccNo2 = primaryAccNo2;
    }

    public String getProcessingCode3() {
        return processingCode3;
    }

    public void setProcessingCode3(String processingCode3) {
        this.processingCode3 = processingCode3;
    }

    public String getAmtTransaction4() {
        return amtTransaction4;
    }

    public void setAmtTransaction4(String amtTransaction4) {
        this.amtTransaction4 = amtTransaction4;
    }

    public String getTransmissionDateTime7() {
        return transmissionDateTime7;
    }

    public void setTransmissionDateTime7(String transmissionDateTime7) {
        this.transmissionDateTime7 = transmissionDateTime7;
    }

    public String getSystemTraceAuditnumber11() {
        return systemTraceAuditnumber11;
    }

    public void setSystemTraceAuditnumber11(String systemTraceAuditnumber11) {
        this.systemTraceAuditnumber11 = systemTraceAuditnumber11;
    }

    public String getTimeLocalTransaction12() {
        return timeLocalTransaction12;
    }

    public void setTimeLocalTransaction12(String timeLocalTransaction12) {
        this.timeLocalTransaction12 = timeLocalTransaction12;
    }

    public String getDateExpiration14() {
        return dateExpiration14;
    }

    public void setDateExpiration14(String dateExpiration14) {
        this.dateExpiration14 = dateExpiration14;
    }

    public String getPosEntrymode22() {
        return posEntrymode22;
    }

    public void setPosEntrymode22(String posEntrymode22) {
        this.posEntrymode22 = posEntrymode22;
    }

    public String getCardSequenceNumber23() {
        return cardSequenceNumber23;
    }

    public void setCardSequenceNumber23(String cardSequenceNumber23) {
        this.cardSequenceNumber23 = cardSequenceNumber23;
    }

    public String getFunctioncode24() {
        return functioncode24;
    }

    public void setFunctioncode24(String functioncode24) {
        this.functioncode24 = functioncode24;
    }

    public String getPosConditionCode25() {
        return posConditionCode25;
    }

    public void setPosConditionCode25(String posConditionCode25) {
        this.posConditionCode25 = posConditionCode25;
    }

    public String getPosPinCaptureCode26() {
        return posPinCaptureCode26;
    }

    public void setPosPinCaptureCode26(String posPinCaptureCode26) {
        this.posPinCaptureCode26 = posPinCaptureCode26;
    }

    public String getAmtTransFee28() {
        return amtTransFee28;
    }

    public void setAmtTransFee28(String amtTransFee28) {
        this.amtTransFee28 = amtTransFee28;
    }

    public String getAmtTranProcessingFee30() {
        return amtTranProcessingFee30;
    }

    public void setAmtTranProcessingFee30(String amtTranProcessingFee30) {
        this.amtTranProcessingFee30 = amtTranProcessingFee30;
    }

    public String getAccuringInsituteIdCode32() {
        return accuringInsituteIdCode32;
    }

    public void setAccuringInsituteIdCode32(String accuringInsituteIdCode32) {
        this.accuringInsituteIdCode32 = accuringInsituteIdCode32;
    }

    public String getTrack2Data35() {
        return track2Data35;
    }

    public void setTrack2Data35(String track2Data35) {
        this.track2Data35 = track2Data35;
    }

    public String getRetriRefNo37() {
        return retriRefNo37;
    }

    public void setRetriRefNo37(String retriRefNo37) {
        this.retriRefNo37 = retriRefNo37;
    }

    public String getAuthIdResCode38() {
        return authIdResCode38;
    }

    public void setAuthIdResCode38(String authIdResCode38) {
        this.authIdResCode38 = authIdResCode38;
    }

    public String getResponseCode39() {
        return responseCode39;
    }

    public void setResponseCode39(String responseCode39) {
        this.responseCode39 = responseCode39;
    }

    public String getCardAcceptorTemId41() {
        return cardAcceptorTemId41;
    }

    public void setCardAcceptorTemId41(String cardAcceptorTemId41) {
        this.cardAcceptorTemId41 = cardAcceptorTemId41;
    }

    public String getCardAcceptorIdCode42() {
        return cardAcceptorIdCode42;
    }

    public void setCardAcceptorIdCode42(String cardAcceptorIdCode42) {
        this.cardAcceptorIdCode42 = cardAcceptorIdCode42;
    }

    public String getAdditionalDataNational47() {
        return additionalDataNational47;
    }

    public void setAdditionalDataNational47(String additionalDataNational47) {
        this.additionalDataNational47 = additionalDataNational47;
    }

    public String getAdditionalDataPrivate48() {
        return additionalDataPrivate48;
    }

    public void setAdditionalDataPrivate48(String additionalDataPrivate48) {
        this.additionalDataPrivate48 = additionalDataPrivate48;
    }

    public String getCurrCodeTransaction49() {
        return currCodeTransaction49;
    }

    public void setCurrCodeTransaction49(String currCodeTransaction49) {
        this.currCodeTransaction49 = currCodeTransaction49;
    }

    public String getCurrCodeStatleMent50() {
        return currCodeStatleMent50;
    }

    public void setCurrCodeStatleMent50(String currCodeStatleMent50) {
        this.currCodeStatleMent50 = currCodeStatleMent50;
    }

    public String getPinData52() {
        return pinData52;
    }

    public void setPinData52(String pinData52) {
        this.pinData52 = pinData52;
    }

    public String getSecRelatedContInfo53() {
        return secRelatedContInfo53;
    }

    public void setSecRelatedContInfo53(String secRelatedContInfo53) {
        this.secRelatedContInfo53 = secRelatedContInfo53;
    }

    public String getAddlAmt54() {
        return addlAmt54;
    }

    public void setAddlAmt54(String addlAmt54) {
        this.addlAmt54 = addlAmt54;
    }

    public String getIccCardSystemRelatedData55() {
        return iccCardSystemRelatedData55;
    }

    public void setIccCardSystemRelatedData55(String iccCardSystemRelatedData55) {
        this.iccCardSystemRelatedData55 = iccCardSystemRelatedData55;
    }

    public String getMsgReasonCode56() {
        return msgReasonCode56;
    }

    public void setMsgReasonCode56(String msgReasonCode56) {
        this.msgReasonCode56 = msgReasonCode56;
    }

    public String getEchoData59() {
        return echoData59;
    }

    public void setEchoData59(String echoData59) {
        this.echoData59 = echoData59;
    }

    public String getReservedData62() {
        return reservedData62;
    }

    public void setReservedData62(String reservedData62) {
        this.reservedData62 = reservedData62;
    }

    public byte[] getMessageAuthenticationCodeField64() {
        return messageAuthenticationCodeField64;
    }

    public void setMessageAuthenticationCodeField64(byte[] messageAuthenticationCodeField64) {
        this.messageAuthenticationCodeField64 = messageAuthenticationCodeField64;
    }

    public String getDataRecord72() {
        return dataRecord72;
    }

    public void setDataRecord72(String dataRecord72) {
        this.dataRecord72 = dataRecord72;
    }

    public String getReservedData124() {
        return reservedData124;
    }

    public void setReservedData124(String reservedData124) {
        this.reservedData124 = reservedData124;
    }

    public byte[] getMacExt128() {
        return macExt128;
    }

    public void setMacExt128(byte[] macExt128) {
        this.macExt128 = macExt128;
    }

    public String getNameTransactionTag() {
        return nameTransactionTag;
    }

    public void setNameTransactionTag(String nameTransactionTag) {
        this.nameTransactionTag = nameTransactionTag;
    }

    public String getStartTimeTransaction() {
        return startTimeTransaction;
    }

    public void setStartTimeTransaction(String startTimeTransaction) {
        this.startTimeTransaction = startTimeTransaction;
    }

    public String getEndTimeTransaction() {
        return endTimeTransaction;
    }

    public void setEndTimeTransaction(String endTimeTransaction) {
        this.endTimeTransaction = endTimeTransaction;
    }

    public String getModeTransaction() {
        return modeTransaction;
    }

    public void setModeTransaction(String modeTransaction) {
        this.modeTransaction = modeTransaction;
    }

    public String getStartTimeConnection() {
        return startTimeConnection;
    }

    public void setStartTimeConnection(String startTimeConnection) {
        this.startTimeConnection = startTimeConnection;
    }

    public String getEndTimeConnection() {
        return endTimeConnection;
    }

    public void setEndTimeConnection(String endTimeConnection) {
        this.endTimeConnection = endTimeConnection;
    }

    public String getTsii() {
        return tsii;
    }

    public void setTsii(String tsii) {
        this.tsii = tsii;
    }

    @Override
    public String toString() {
        return "SAFModelEntity{" +
                "uid=" + uid +
                ", mti0='" + mti0 + '\'' +
                ", cardIndicator='" + cardIndicator + '\'' +
                ", tsii='" + tsii + '\'' +
                ", kernalID='" + kernalID + '\'' +
                ", primaryAccNo2='" + primaryAccNo2 + '\'' +
                ", processingCode3='" + processingCode3 + '\'' +
                ", amtTransaction4='" + amtTransaction4 + '\'' +
                ", transmissionDateTime7='" + transmissionDateTime7 + '\'' +
                ", systemTraceAuditnumber11='" + systemTraceAuditnumber11 + '\'' +
                ", timeLocalTransaction12='" + timeLocalTransaction12 + '\'' +
                ", dateExpiration14='" + dateExpiration14 + '\'' +
                ", posEntrymode22='" + posEntrymode22 + '\'' +
                ", cardSequenceNumber23='" + cardSequenceNumber23 + '\'' +
                ", functioncode24='" + functioncode24 + '\'' +
                ", posConditionCode25='" + posConditionCode25 + '\'' +
                ", posPinCaptureCode26='" + posPinCaptureCode26 + '\'' +
                ", amtTransFee28='" + amtTransFee28 + '\'' +
                ", amtTranProcessingFee30='" + amtTranProcessingFee30 + '\'' +
                ", accuringInsituteIdCode32='" + accuringInsituteIdCode32 + '\'' +
                ", track2Data35='" + track2Data35 + '\'' +
                ", retriRefNo37='" + retriRefNo37 + '\'' +
                ", authIdResCode38='" + authIdResCode38 + '\'' +
                ", responseCode39='" + responseCode39 + '\'' +
                ", cardAcceptorTemId41='" + cardAcceptorTemId41 + '\'' +
                ", cardAcceptorIdCode42='" + cardAcceptorIdCode42 + '\'' +
                ", additionalDataNational47='" + additionalDataNational47 + '\'' +
                ", additionalDataPrivate48='" + additionalDataPrivate48 + '\'' +
                ", currCodeTransaction49='" + currCodeTransaction49 + '\'' +
                ", currCodeStatleMent50='" + currCodeStatleMent50 + '\'' +
                ", pinData52='" + pinData52 + '\'' +
                ", secRelatedContInfo53='" + secRelatedContInfo53 + '\'' +
                ", addlAmt54='" + addlAmt54 + '\'' +
                ", iccCardSystemRelatedData55='" + iccCardSystemRelatedData55 + '\'' +
                ", iccCardSystemRelatedData55_final='" + iccCardSystemRelatedData55_final + '\'' +
                ", msgReasonCode56='" + msgReasonCode56 + '\'' +
                ", echoData59='" + echoData59 + '\'' +
                ", reservedData62='" + reservedData62 + '\'' +
                ", reservedData62Responce='" + reservedData62Responce + '\'' +
                ", messageAuthenticationCodeField64=" + Arrays.toString(messageAuthenticationCodeField64) +
                ", dataRecord72='" + dataRecord72 + '\'' +
                ", reservedData124='" + reservedData124 + '\'' +
                ", macExt128=" + Arrays.toString(macExt128) +
                ", nameTransactionTag='" + nameTransactionTag + '\'' +
                ", saf=" + saf +
                ", statusTransaction=" + statusTransaction +
                ", responseData44='" + responseData44 + '\'' +
                ", startTimeTransaction='" + startTimeTransaction + '\'' +
                ", endTimeTransaction='" + endTimeTransaction + '\'' +
                ", startTimeConnection='" + startTimeConnection + '\'' +
                ", endTimeConnection='" + endTimeConnection + '\'' +
                ", modeTransaction='" + modeTransaction + '\'' +
                '}';
    }
}
