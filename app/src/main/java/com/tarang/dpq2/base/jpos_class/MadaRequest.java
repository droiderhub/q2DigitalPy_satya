package com.tarang.dpq2.base.jpos_class;

import java.util.Arrays;

public class MadaRequest {
    private String cardIndicator = "";
    private String setKernalID = "";
    private String nameTransactionTag = "";
    private String modeTransaction = "";
    private String reservedData62Responce = "";


    public String getReservedData62Responce() {
        return reservedData62Responce;
    }

    public void setReservedData62Responce(String reservedData62Responce) {
        this.reservedData62Responce = reservedData62Responce;
    }

    public String getSetKernalID() {
        return setKernalID;
    }

    public void setSetKernalID(String setKernalID) {
        this.setKernalID = setKernalID;
    }

    public String getCardIndicator() {
        return cardIndicator;
    }

    public void setCardIndicator(String cardIndicator) {
        this.cardIndicator = cardIndicator;
    }

    public String getNameTransactionTag() {
        return nameTransactionTag;
    }

    public void setNameTransactionTag(String nameTransactionTag) {
        this.nameTransactionTag = nameTransactionTag;
    }

    public String getModeTransaction() {
        return modeTransaction;
    }

    public void setModeTransaction(String modeTransaction) {
        this.modeTransaction = modeTransaction;
    }

    private String mti0 = "";

    private String primaryAccNo2 = "";

    private String processingCode3 = "";

    private String amtTransaction4 = "";

    private String amtSattlement5 = "";

    private String amtCardholderbilling6 = "";

    private String transmissionDateTime7 = "";

    private String amtCardholderbillingfee8 = "";

    private String conversationRateSattlement9 = "";

    private String conversationRateCardholderbilling10 = "";

    private String systemTraceAuditnumber11 = "";

    private String timeLocalTransaction12 = "";

    private String dateLocalTransaction13 = "";

    private String dateExpiration14 = "";

    private String dateSattlement15 = "";

    private String dateConversion16 = "";

    private String dateCapture17 = "";

    private String merchantType18 = "";

    private String countryCodeAcquiringinstitution19 = "";

    private String countryCodePrimaryaccountno20 = "";

    private String countryCodeForwardinginstitution21 = "";

    private String posEntrymode22 = "";

    private String cardSequenceNumber23 = "";

    private String functioncode24 = "";


    private String messageReasonCode25 = "";


    private String cardAcceptorBusinessCode26 = "";

    private String autidResLength27 = "";

    private String reconsilationDate28 = "";

    private String amtSattlementFee29 = "";

    private String amtTranProcessingFee30 = "";

    private String amtSattleProcessingFee31 = "";

    private String accuringInsituteIdCode32 = "";

    private String forwardInsituteIdCode33 = "";

    private String primaryAccountNumberExtended34 = "";

    private String track2Data35 = "";

    private String track3Data36 = "";

    private String retriRefNo37 = "";

    private String authIdResCode38 = "";

    private String responseCode39 = "";

    private String serviceRestricCode40 = "";

    private String cardAcceptorTemId41 = "";

    private String cardAcceptorIdCode42 = "";

    private String cardAcceptorNameLocation43 = "";

    private String additionalResData44 = "";

    private String trackOneData45 = "";

    private String amountsFees46 = "";

    private String additionalDataNational47 = "";

    private String additionalDataPrivate48 = "";

    private String currCodeTransaction49 = "";

    private String currCodeStatleMent50 = "";

    private String currencyCodeCardholderbilling51 = "";

    private byte[] pinData52;

    private String secRelatedContInfo53 = "";

    private String addlAmt54 = "";

    private String iccCardSystemRelatedData55 = "";

    private String msgReasonCode56 = "";

    private String authLifeCode57 = "";

    private String authAgentIdCode58 = "";

    private String echoData59 = "";

    private String reservedData60 = "";

    private String reservedData61 = "";

    private String reservedData62 = "";

    private String reservedData63 = "";

    private byte[] messageAuthenticationCodeField64;

    private String reservedData65 = "";

    private String statleMentCode66 = "";

    private String extPaymentCode67 = "";

    private String countryCodeReceivingInstitution68 = "";

    private String countryCodeSettlementInstitution69 = "";

    private String netMgnInfoCode70 = "";

    private String messageNumber71 = "";

    private String dataRecord72 = "";

    private String dateAction73 = "";

    private String creditNo74 = "";

    private String creditRevNo75 = "";

    private String debitNo76 = "";

    private String debitRevNo77 = "";

    private String transperNo78 = "";

    private String transperRevNo79 = "";

    private String inquiriesNo80 = "";

    private String authNo81 = "";

    private String creditProcessFeeAmt82 = "";

    private String creditTransFeeAmt83 = "";

    private String debitProcessFeeAmt84 = "";

    private String debitTransFeeAmt85 = "";

    private String creditAmt86 = "";

    private String creditRevAmt87 = "";

    private String debitAmt88 = "";

    private String debitRevAmt89 = "";

    private String orgDataElement90 = "";

    private String fileUpdateCode91 = "";

    private String countryCodeTransactionOrig92 = "";

    private String transactionDest93 = "";

    private String transactionOrig94 = "";

    private String replaceAmt95 = "";

    private String keyManagementData96 = "";

    private String amtNetSattle97 = "";

    private String payee98 = "";

    private String settlementInstitutionIdCode99 = "";

    private String receiveInsituteIdCode100 = "";

    private String fileName101 = "";

    private String accIdentOne102 = "";

    private String accIdentTwo103 = "";

    private String reservedData104 = "";

    private String creditsChargebackAmount105 = "";

    private String debitsChargebackAmount106 = "";

    private String creditsChargebackNumber107 = "";

    private String debitsChargebackNumber108 = "";

    private String creditsFeeAmounts109 = "";

    private String debitsFeAmounts110 = "";

    private String reservedData111 = "";

    private String reservedData112 = "";

    private String reservedData113 = "";

    private String reservedData114 = "";

    private String reservedData115 = "";

    private String reservedData116 = "";

    private String reservedData117 = "";

    private String paymentNo118 = "";

    private String paymentRevNo119 = "";

    private String reservedData120 = "";

    private String reservedData121 = "";

    private String reservedData122 = "";

    private String posDataCode123 = "";

    private String reservedData124 = "";

    private String netMgnInfo125 = "";

    private String reservedData126 = "";

    private String switchKey127_2 = "";

    private String routeInfo127_3 = "";

    private String posData127_4 = "";

    private String serStationData127_5 = "";

    private String authProfile127_6 = "";

    private String checkData127_7 = "";

    private String retentionData127_8 = "";

    private String addlNodeData127_9 = "";

    private String cvv2127_10 = "";

    private String orgKey127_11 = "";

    private String termOwner127_12 = "";

    private String posGeograpicData127_13 = "";

    private String sponsorBank127_14 = "";

    private String addressVerificData127_15 = "";

    private String addressVerificResult127_16 = "";

    private String cardHolderInfo127_17 = "";

    private String validationData127_18 = "";

    private String bankDetail127_19 = "";

    private String origOrAuthorizerDateStattleMent127_20 = "";

    private String recordIdentification127_21 = "";

    private String srtucreData127_22 = "";

    private String payeeNameAddress127_23 = "";

    private String payerAcc127_24 = "";

    private String iccData127_25 = "";

    private String amtAuthData127_25_2 = "";

    private String amtOtherData127_25_3 = "";

    private String appIdentifierData127_25_4 = "";

    private String appInterChangePrifileData127_25_5 = "";

    private String appTransCounterData127_25_6 = "";

    private String appUsageData127_25_7 = "";

    private String authorizationResponseCode127_25_8 = "";

    private String cardAuthenticationReliabilityIndicator127_25_9 = "";

    private String cardAuthenticationResultsCode127_25_10 = "";

    private String chipConditionCode127_25_11 = "";

    private String appCryptogramData127_25_12 = "";

    private String appCryptoGramInfoData127_25_13 = "";

    private String cvmList127_25_14 = "";

    private String cvmResultData127_25_15 = "";

    private String interfaceDeviceSerialNumber127_25_16 = "";

    private String issuerAccCodeData127_25_17 = "";

    private String issueAppData127_25_18 = "";

    private String issuerScriptResults127_25_19 = "";

    private String terminalApplicationVersionNumber127_25_20 = "";

    private String termCapableData127_25_21 = "";

    private String termCounCodeData127_25_22 = "";

    private String termTypeData127_25_23 = "";

    private String termVerResData127_25_24 = "";

    private String transactionCategoryCode127_25_25 = "";

    private String tranCurrCodeData127_25_26 = "";

    private String tranDateData127_25_27 = "";

    private String transactionSequenceCounter127_25_28 = "";

    private String tranTypeData127_25_29 = "";

    private String unpretictableData127_25_30 = "";

    private String issuerAuthenticationData127_25_31 = "";

    private String issuerScriptTemplateOne127_25_32 = "";

    private String issuerScriptTemplateTwo127_25_33 = "";

    private String orgNode127_26 = "";

    private String cardVerResult127_27 = "";

    private String americanExpCardIdentifier127_28 = "";

    private String secureData127_29 = "";

    private String secureResult127_30 = "";

    private String issuerNetId127_31 = "";

    private String ucafData127_32 = "";

    private String extentedTransType127_33 = "";

    private String accTypeQualifiers127_34 = "";

    private String acquireNetId127_35 = "";

    private String cusId127_36 = "";

    private String extResCode127_37 = "";

    private String addlPosDataCode127_38 = "";

    private String orgResCode127_39 = "";

    private byte[] macExt128;

    /**
     * Default constructor.
     */
    public MadaRequest() {
    }

    /**
     * Returns the mti0 value
     *
     * @return the mti0
     */
    public String getMti0() {
        return mti0;
    }

    /**
     * Set the mti0 value
     *
     * @param mti0
     *          the mti0 to set
     */
    public void setMti0(String mti0) {
        this.mti0 = mti0;
    }

    /**
     * Returns the primaryAccNo2 value
     *
     * @return the primaryAccNo2
     */
    public String getPrimaryAccNo2() {
        return primaryAccNo2;
    }

    /**
     * Set the primaryAccNo2 value
     *
     * @param primaryAccNo2
     *          the primaryAccNo2 to set
     */
    public void setPrimaryAccNo2(String primaryAccNo2) {
        this.primaryAccNo2 = primaryAccNo2;
    }

    /**
     * Returns the processingCode3 value
     *
     * @return the processingCode3
     */
    public String getProcessingCode3() {
        return processingCode3;
    }

    /**
     * Set the processingCode3 value
     *
     * @param processingCode3
     *          the processingCode3 to set
     */
    public void setProcessingCode3(String processingCode3) {
        this.processingCode3 = processingCode3;
    }

    /**
     * Returns the amtTransaction4 value
     *
     * @return the amtTransaction4
     */
    public String getAmtTransaction4() {
        return amtTransaction4;
    }

    /**
     * Set the amtTransaction4 value
     *
     * @param amtTransaction4
     *          the amtTransaction4 to set
     */
    public void setAmtTransaction4(String amtTransaction4) {
        this.amtTransaction4 = amtTransaction4;
    }

    /**
     * Returns the amtSattlement5 value
     *
     * @return the amtSattlement5
     */
    public String getAmtSattlement5() {
        return amtSattlement5;
    }

    /**
     * Set the amtSattlement5 value
     *
     * @param amtSattlement5
     *          the amtSattlement5 to set
     */
    public void setAmtSattlement5(String amtSattlement5) {
        this.amtSattlement5 = amtSattlement5;
    }

    /**
     * Returns the amtCardholderbilling6 value
     *
     * @return the amtCardholderbilling6
     */
    public String getAmtCardholderbilling6() {
        return amtCardholderbilling6;
    }

    /**
     * Set the amtCardholderbilling6 value
     *
     * @param amtCardholderbilling6
     *          the amtCardholderbilling6 to set
     */
    public void setAmtCardholderbilling6(String amtCardholderbilling6) {
        this.amtCardholderbilling6 = amtCardholderbilling6;
    }

    /**
     * Returns the transmissionDateTime7 value
     *
     * @return the transmissionDateTime7
     */
    public String getTransmissionDateTime7() {
        return transmissionDateTime7;
    }

    /**
     * Set the transmissionDateTime7 value
     *
     * @param transmissionDateTime7
     *          the transmissionDateTime7 to set
     */
    public void setTransmissionDateTime7(String transmissionDateTime7) {
        this.transmissionDateTime7 = transmissionDateTime7;
    }

    /**
     * Returns the amtCardholderbillingfee8 value
     *
     * @return the amtCardholderbillingfee8
     */
    public String getAmtCardholderbillingfee8() {
        return amtCardholderbillingfee8;
    }

    /**
     * Set the amtCardholderbillingfee8 value
     *
     * @param amtCardholderbillingfee8
     *          the amtCardholderbillingfee8 to set
     */
    public void setAmtCardholderbillingfee8(String amtCardholderbillingfee8) {
        this.amtCardholderbillingfee8 = amtCardholderbillingfee8;
    }

    /**
     * Returns the conversationRateSattlement9 value
     *
     * @return the conversationRateSattlement9
     */
    public String getConversationRateSattlement9() {
        return conversationRateSattlement9;
    }

    /**
     * Set the conversationRateSattlement9 value
     *
     * @param conversationRateSattlement9
     *          the conversationRateSattlement9 to set
     */
    public void setConversationRateSattlement9(String conversationRateSattlement9) {
        this.conversationRateSattlement9 = conversationRateSattlement9;
    }

    /**
     * Returns the conversationRateCardholderbilling10 value
     *
     * @return the conversationRateCardholderbilling10
     */
    public String getConversationRateCardholderbilling10() {
        return conversationRateCardholderbilling10;
    }

    /**
     * Set the conversationRateCardholderbilling10 value
     *
     * @param conversationRateCardholderbilling10
     *          the conversationRateCardholderbilling10 to set
     */
    public void setConversationRateCardholderbilling10(String conversationRateCardholderbilling10) {
        this.conversationRateCardholderbilling10 = conversationRateCardholderbilling10;
    }

    /**
     * Returns the systemTraceAuditnumber11 value
     *
     * @return the systemTraceAuditnumber11
     */
    public String getSystemTraceAuditnumber11() {
        return systemTraceAuditnumber11;
    }

    /**
     * Set the systemTraceAuditnumber11 value
     *
     * @param systemTraceAuditnumber11
     *          the systemTraceAuditnumber11 to set
     */
    public void setSystemTraceAuditnumber11(String systemTraceAuditnumber11) {
        this.systemTraceAuditnumber11 = systemTraceAuditnumber11;
    }

    /**
     * Returns the timeLocalTransaction12 value
     *
     * @return the timeLocalTransaction12
     */
    public String getTimeLocalTransaction12() {
        return timeLocalTransaction12;
    }

    /**
     * Set the timeLocalTransaction12 value
     *
     * @param timeLocalTransaction12
     *          the timeLocalTransaction12 to set
     */
    public void setTimeLocalTransaction12(String timeLocalTransaction12) {
        this.timeLocalTransaction12 = timeLocalTransaction12;
    }

    /**
     * Returns the dateLocalTransaction13 value
     *
     * @return the dateLocalTransaction13
     */
    public String getDateLocalTransaction13() {
        return dateLocalTransaction13;
    }

    /**
     * Set the dateLocalTransaction13 value
     *
     * @param dateLocalTransaction13
     *          the dateLocalTransaction13 to set
     */
    public void setDateLocalTransaction13(String dateLocalTransaction13) {
        this.dateLocalTransaction13 = dateLocalTransaction13;
    }

    /**
     * Returns the dateExpiration14 value
     *
     * @return the dateExpiration14
     */
    public String getDateExpiration14() {
        return dateExpiration14;
    }

    /**
     * Set the dateExpiration14 value
     *
     * @param dateExpiration14
     *          the dateExpiration14 to set
     */
    public void setDateExpiration14(String dateExpiration14) {
        this.dateExpiration14 = dateExpiration14;
    }

    /**
     * Returns the dateSattlement15 value
     *
     * @return the dateSattlement15
     */
    public String getDateSattlement15() {
        return dateSattlement15;
    }

    /**
     * Set the dateSattlement15 value
     *
     * @param dateSattlement15
     *          the dateSattlement15 to set
     */
    public void setDateSattlement15(String dateSattlement15) {
        this.dateSattlement15 = dateSattlement15;
    }

    /**
     * Returns the dateConversion16 value
     *
     * @return the dateConversion16
     */
    public String getDateConversion16() {
        return dateConversion16;
    }

    /**
     * Set the dateConversion16 value
     *
     * @param dateConversion16
     *          the dateConversion16 to set
     */
    public void setDateConversion16(String dateConversion16) {
        this.dateConversion16 = dateConversion16;
    }

    /**
     * Returns the dateCapture17 value
     *
     * @return the dateCapture17
     */
    public String getDateCapture17() {
        return dateCapture17;
    }

    /**
     * Set the dateCapture17 value
     *
     * @param dateCapture17
     *          the dateCapture17 to set
     */
    public void setDateCapture17(String dateCapture17) {
        this.dateCapture17 = dateCapture17;
    }

    /**
     * Returns the merchantType18 value
     *
     * @return the merchantType18
     */
    public String getMerchantType18() {
        return merchantType18;
    }

    /**
     * Set the merchantType18 value
     *
     * @param merchantType18
     *          the merchantType18 to set
     */
    public void setMerchantType18(String merchantType18) {
        this.merchantType18 = merchantType18;
    }

    /**
     * Returns the countryCodeAcquiringinstitution19 value
     *
     * @return the countryCodeAcquiringinstitution19
     */
    public String getCountryCodeAcquiringinstitution19() {
        return countryCodeAcquiringinstitution19;
    }

    /**
     * Set the countryCodeAcquiringinstitution19 value
     *
     * @param countryCodeAcquiringinstitution19
     *          the countryCodeAcquiringinstitution19 to set
     */
    public void setCountryCodeAcquiringinstitution19(String countryCodeAcquiringinstitution19) {
        this.countryCodeAcquiringinstitution19 = countryCodeAcquiringinstitution19;
    }

    /**
     * Returns the countryCodePrimaryaccountno20 value
     *
     * @return the countryCodePrimaryaccountno20
     */
    public String getCountryCodePrimaryaccountno20() {
        return countryCodePrimaryaccountno20;
    }

    /**
     * Set the countryCodePrimaryaccountno20 value
     *
     * @param countryCodePrimaryaccountno20
     *          the countryCodePrimaryaccountno20 to set
     */
    public void setCountryCodePrimaryaccountno20(String countryCodePrimaryaccountno20) {
        this.countryCodePrimaryaccountno20 = countryCodePrimaryaccountno20;
    }

    /**
     * Returns the countryCodeForwardinginstitution21 value
     *
     * @return the countryCodeForwardinginstitution21
     */
    public String getCountryCodeForwardinginstitution21() {
        return countryCodeForwardinginstitution21;
    }

    /**
     * Set the countryCodeForwardinginstitution21 value
     *
     * @param countryCodeForwardinginstitution21
     *          the countryCodeForwardinginstitution21 to set
     */
    public void setCountryCodeForwardinginstitution21(String countryCodeForwardinginstitution21) {
        this.countryCodeForwardinginstitution21 = countryCodeForwardinginstitution21;
    }

    /**
     * Returns the posEntrymode22 value
     *
     * @return the posEntrymode22
     */
    public String getPosEntrymode22() {
        return posEntrymode22;
    }

    /**
     * Set the posEntrymode22 value
     *
     * @param posEntrymode22
     *          the posEntrymode22 to set
     */
    public void setPosEntrymode22(String posEntrymode22) {
        this.posEntrymode22 = posEntrymode22;
    }

    /**
     * Returns the cardSequenceNumber23 value
     *
     * @return the cardSequenceNumber23
     */
    public String getCardSequenceNumber23() {
        return cardSequenceNumber23;
    }

    /**
     * Set the cardSequenceNumber23 value
     *
     * @param cardSequenceNumber23
     *          the cardSequenceNumber23 to set
     */
    public void setCardSequenceNumber23(String cardSequenceNumber23) {
        this.cardSequenceNumber23 = cardSequenceNumber23;
    }

    /**
     * Returns the functioncode24 value
     *
     * @return the functioncode24
     */
    public String getFunctioncode24() {
        return functioncode24;
    }

    /**
     * Set the functioncode24 value
     *
     * @param functioncode24
     *          the functioncode24 to set
     */
    public void setFunctioncode24(String functioncode24) {
        this.functioncode24 = functioncode24;
    }

    public String getMessageReasonCode25() {
        return messageReasonCode25;
    }

    public void setMessageReasonCode25(String messageReasonCode25) {
        this.messageReasonCode25 = messageReasonCode25;
    }


    public String getCardAcceptorBusinessCode26() {
        return cardAcceptorBusinessCode26;
    }

    public void setCardAcceptorBusinessCode26(String cardAcceptorBusinessCode26) {
        this.cardAcceptorBusinessCode26 = cardAcceptorBusinessCode26;
    }


    /**
     * Returns the autidResLength27 value
     *
     * @return the autidResLength27
     */
    public String getAutidResLength27() {
        return autidResLength27;
    }

    /**
     * Set the autidResLength27 value
     *
     * @param autidResLength27
     *          the autidResLength27 to set
     */
    public void setAutidResLength27(String autidResLength27) {
        this.autidResLength27 = autidResLength27;
    }

    public String getReconsilationDate28() {
        return reconsilationDate28;
    }

    public void setReconsilationDate28(String reconsilationDate28) {
        this.reconsilationDate28 = reconsilationDate28;
    }

    /**
     * Returns the amtSattlementFee29 value
     *
     * @return the amtSattlementFee29
     */
    public String getAmtSattlementFee29() {
        return amtSattlementFee29;
    }

    /**
     * Set the amtSattlementFee29 value
     *
     * @param amtSattlementFee29
     *          the amtSattlementFee29 to set
     */
    public void setAmtSattlementFee29(String amtSattlementFee29) {
        this.amtSattlementFee29 = amtSattlementFee29;
    }

    /**
     * Returns the amtTranProcessingFee30 value
     *
     * @return the amtTranProcessingFee30
     */
    public String getAmtTranProcessingFee30() {
        return amtTranProcessingFee30;
    }

    /**
     * Set the amtTranProcessingFee30 value
     *
     * @param amtTranProcessingFee30
     *          the amtTranProcessingFee30 to set
     */
    public void setAmtTranProcessingFee30(String amtTranProcessingFee30) {
        this.amtTranProcessingFee30 = amtTranProcessingFee30;
    }

    /**
     * Returns the amtSattleProcessingFee31 value
     *
     * @return the amtSattleProcessingFee31
     */
    public String getAmtSattleProcessingFee31() {
        return amtSattleProcessingFee31;
    }

    /**
     * Set the amtSattleProcessingFee31 value
     *
     * @param amtSattleProcessingFee31
     *          the amtSattleProcessingFee31 to set
     */
    public void setAmtSattleProcessingFee31(String amtSattleProcessingFee31) {
        this.amtSattleProcessingFee31 = amtSattleProcessingFee31;
    }

    /**
     * Returns the accuringInsituteIdCode32 value
     *
     * @return the accuringInsituteIdCode32
     */
    public String getAccuringInsituteIdCode32() {
        return accuringInsituteIdCode32;
    }

    /**
     * Set the accuringInsituteIdCode32 value
     *
     * @param accuringInsituteIdCode32
     *          the accuringInsituteIdCode32 to set
     */
    public void setAccuringInsituteIdCode32(String accuringInsituteIdCode32) {
        this.accuringInsituteIdCode32 = accuringInsituteIdCode32;
    }

    /**
     * Returns the forwardInsituteIdCode33 value
     *
     * @return the forwardInsituteIdCode33
     */
    public String getForwardInsituteIdCode33() {
        return forwardInsituteIdCode33;
    }

    /**
     * Set the forwardInsituteIdCode33 value
     *
     * @param forwardInsituteIdCode33
     *          the forwardInsituteIdCode33 to set
     */
    public void setForwardInsituteIdCode33(String forwardInsituteIdCode33) {
        this.forwardInsituteIdCode33 = forwardInsituteIdCode33;
    }

    /**
     * Returns the primaryAccountNumberExtended34 value
     *
     * @return the primaryAccountNumberExtended34
     */
    public String getPrimaryAccountNumberExtended34() {
        return primaryAccountNumberExtended34;
    }

    /**
     * Set the primaryAccountNumberExtended34 value
     *
     * @param primaryAccountNumberExtended34
     *          the primaryAccountNumberExtended34 to set
     */
    public void setPrimaryAccountNumberExtended34(String primaryAccountNumberExtended34) {
        this.primaryAccountNumberExtended34 = primaryAccountNumberExtended34;
    }

    /**
     * Returns the track2Data35 value
     *
     * @return the track2Data35
     */
    public String getTrack2Data35() {
        return track2Data35;
    }

    /**
     * Set the track2Data35 value
     *
     * @param track2Data35
     *          the track2Data35 to set
     */
    public void setTrack2Data35(String track2Data35) {
        this.track2Data35 = track2Data35;
    }

    /**
     * Returns the track3Data36 value
     *
     * @return the track3Data36
     */
    public String getTrack3Data36() {
        return track3Data36;
    }

    /**
     * Set the track3Data36 value
     *
     * @param track3Data36
     *          the track3Data36 to set
     */
    public void setTrack3Data36(String track3Data36) {
        this.track3Data36 = track3Data36;
    }

    /**
     * Returns the retriRefNo37 value
     *
     * @return the retriRefNo37
     */
    public String getRetriRefNo37() {
        return retriRefNo37;
    }

    /**
     * Set the retriRefNo37 value
     *
     * @param retriRefNo37
     *          the retriRefNo37 to set
     */
    public void setRetriRefNo37(String retriRefNo37) {
        this.retriRefNo37 = retriRefNo37;
    }

    /**
     * Returns the authIdResCode38 value
     *
     * @return the authIdResCode38
     */
    public String getAuthIdResCode38() {
        return authIdResCode38;
    }

    /**
     * Set the authIdResCode38 value
     *
     * @param authIdResCode38
     *          the authIdResCode38 to set
     */
    public void setAuthIdResCode38(String authIdResCode38) {
        this.authIdResCode38 = authIdResCode38;
    }

    /**
     * Returns the responseCode39 value
     *
     * @return the responseCode39
     */
    public String getResponseCode39() {
        return responseCode39;
    }

    /**
     * Set the responseCode39 value
     *
     * @param responseCode39
     *          the responseCode39 to set
     */
    public void setResponseCode39(String responseCode39) {
        this.responseCode39 = responseCode39;
    }

    /**
     * Returns the serviceRestricCode40 value
     *
     * @return the serviceRestricCode40
     */
    public String getServiceRestricCode40() {
        return serviceRestricCode40;
    }

    /**
     * Set the serviceRestricCode40 value
     *
     * @param serviceRestricCode40
     *          the serviceRestricCode40 to set
     */
    public void setServiceRestricCode40(String serviceRestricCode40) {
        this.serviceRestricCode40 = serviceRestricCode40;
    }

    /**
     * Returns the cardAcceptorTemId41 value
     *
     * @return the cardAcceptorTemId41
     */
    public String getCardAcceptorTemId41() {
        return cardAcceptorTemId41;
    }

    /**
     * Set the cardAcceptorTemId41 value
     *
     * @param cardAcceptorTemId41
     *          the cardAcceptorTemId41 to set
     */
    public void setCardAcceptorTemId41(String cardAcceptorTemId41) {
        this.cardAcceptorTemId41 = cardAcceptorTemId41;
    }

    /**
     * Returns the cardAcceptorIdCode42 value
     *
     * @return the cardAcceptorIdCode42
     */
    public String getCardAcceptorIdCode42() {
        return cardAcceptorIdCode42;
    }

    /**
     * Set the cardAcceptorIdCode42 value
     *
     * @param cardAcceptorIdCode42
     *          the cardAcceptorIdCode42 to set
     */
    public void setCardAcceptorIdCode42(String cardAcceptorIdCode42) {
        this.cardAcceptorIdCode42 = cardAcceptorIdCode42;
    }

    /**
     * Returns the cardAcceptorNameLocation43 value
     *
     * @return the cardAcceptorNameLocation43
     */
    public String getCardAcceptorNameLocation43() {
        return cardAcceptorNameLocation43;
    }

    /**
     * Set the cardAcceptorNameLocation43 value
     *
     * @param cardAcceptorNameLocation43
     *          the cardAcceptorNameLocation43 to set
     */
    public void setCardAcceptorNameLocation43(String cardAcceptorNameLocation43) {
        this.cardAcceptorNameLocation43 = cardAcceptorNameLocation43;
    }

    /**
     * Returns the additionalResData44 value
     *
     * @return the additionalResData44
     */
    public String getAdditionalResData44() {
        return additionalResData44;
    }

    /**
     * Set the additionalResData44 value
     *
     * @param additionalResData44
     *          the additionalResData44 to set
     */
    public void setAdditionalResData44(String additionalResData44) {
        this.additionalResData44 = additionalResData44;
    }

    /**
     * Returns the trackOneData45 value
     *
     * @return the trackOneData45
     */
    public String getTrackOneData45() {
        return trackOneData45;
    }

    /**
     * Set the trackOneData45 value
     *
     * @param trackOneData45
     *          the trackOneData45 to set
     */
    public void setTrackOneData45(String trackOneData45) {
        this.trackOneData45 = trackOneData45;
    }

    /**
     * Returns the amountsFees46 value
     *
     * @return the amountsFees46
     */
    public String getAmountsFees46() {
        return amountsFees46;
    }

    /**
     * Set the amountsFees46 value
     *
     * @param amountsFees46
     *          the amountsFees46 to set
     */
    public void setAmountsFees46(String amountsFees46) {
        this.amountsFees46 = amountsFees46;
    }

    /**
     * Returns the additionalDataNational47 value
     *
     * @return the additionalDataNational47
     */
    public String getAdditionalDataNational47() {
        return additionalDataNational47;
    }

    /**
     * Set the additionalDataNational47 value
     *
     * @param additionalDataNational47
     *          the additionalDataNational47 to set
     */
    public void setAdditionalDataNational47(String additionalDataNational47) {
        this.additionalDataNational47 = additionalDataNational47;
    }

    /**
     * Returns the additionalDataPrivate48 value
     *
     * @return the additionalDataPrivate48
     */
    public String getAdditionalDataPrivate48() {
        return additionalDataPrivate48;
    }

    /**
     * Set the additionalDataPrivate48 value
     *
     * @param additionalDataPrivate48
     *          the additionalDataPrivate48 to set
     */
    public void setAdditionalDataPrivate48(String additionalDataPrivate48) {
        this.additionalDataPrivate48 = additionalDataPrivate48;
    }

    /**
     * Returns the currCodeTransaction49 value
     *
     * @return the currCodeTransaction49
     */
    public String getCurrCodeTransaction49() {
        return currCodeTransaction49;
    }

    /**
     * Set the currCodeTransaction49 value
     *
     * @param currCodeTransaction49
     *          the currCodeTransaction49 to set
     */
    public void setCurrCodeTransaction49(String currCodeTransaction49) {
        this.currCodeTransaction49 = currCodeTransaction49;
    }

    /**
     * Returns the currCodeStatleMent50 value
     *
     * @return the currCodeStatleMent50
     */
    public String getCurrCodeStatleMent50() {
        return currCodeStatleMent50;
    }

    /**
     * Set the currCodeStatleMent50 value
     *
     * @param currCodeStatleMent50
     *          the currCodeStatleMent50 to set
     */
    public void setCurrCodeStatleMent50(String currCodeStatleMent50) {
        this.currCodeStatleMent50 = currCodeStatleMent50;
    }

    /**
     * Returns the currencyCodeCardholderbilling51 value
     *
     * @return the currencyCodeCardholderbilling51
     */
    public String getCurrencyCodeCardholderbilling51() {
        return currencyCodeCardholderbilling51;
    }

    /**
     * Set the currencyCodeCardholderbilling51 value
     *
     * @param currencyCodeCardholderbilling51
     *          the currencyCodeCardholderbilling51 to set
     */
    public void setCurrencyCodeCardholderbilling51(String currencyCodeCardholderbilling51) {
        this.currencyCodeCardholderbilling51 = currencyCodeCardholderbilling51;
    }

    /**
     * Returns the pinData52 value
     *
     * @return the pinData52
     */
    public byte[] getPinData52() {
        return pinData52;
    }

    /**
     * Set the pinData52 value
     *
     * @param pinData52
     *          the pinData52 to set
     */
    public void setPinData52(byte[] pinData52) {
        this.pinData52 = pinData52;
    }

    /**
     * Returns the secRelatedContInfo53 value
     *
     * @return the secRelatedContInfo53
     */
    public String getSecRelatedContInfo53() {
        return secRelatedContInfo53;
    }

    /**
     * Set the secRelatedContInfo53 value
     *
     * @param secRelatedContInfo53
     *          the secRelatedContInfo53 to set
     */
    public void setSecRelatedContInfo53(String secRelatedContInfo53) {
        this.secRelatedContInfo53 = secRelatedContInfo53;
    }

    /**
     * Returns the addlAmt54 value
     *
     * @return the addlAmt54
     */
    public String getAddlAmt54() {
        return addlAmt54;
    }

    /**
     * Set the addlAmt54 value
     *
     * @param addlAmt54
     *          the addlAmt54 to set
     */
    public void setAddlAmt54(String addlAmt54) {
        this.addlAmt54 = addlAmt54;
    }

    /**
     * Returns the iccCardSystemRelatedData55 value
     *
     * @return the iccCardSystemRelatedData55
     */
    public String getIccCardSystemRelatedData55() {
        return iccCardSystemRelatedData55;
    }

    /**
     * Set the iccCardSystemRelatedData55 value
     *
     * @param iccCardSystemRelatedData55
     *          the iccCardSystemRelatedData55 to set
     */
    public void setIccCardSystemRelatedData55(String iccCardSystemRelatedData55) {
        this.iccCardSystemRelatedData55 = iccCardSystemRelatedData55;
    }

    /**
     * Returns the msgReasonCode56 value
     *
     * @return the msgReasonCode56
     */
    public String getMsgReasonCode56() {
        return msgReasonCode56;
    }

    /**
     * Set the msgReasonCode56 value
     *
     * @param msgReasonCode56
     *          the msgReasonCode56 to set
     */
    public void setMsgReasonCode56(String msgReasonCode56) {
        this.msgReasonCode56 = msgReasonCode56;
    }

    /**
     * Returns the authLifeCode57 value
     *
     * @return the authLifeCode57
     */
    public String getAuthLifeCode57() {
        return authLifeCode57;
    }

    /**
     * Set the authLifeCode57 value
     *
     * @param authLifeCode57
     *          the authLifeCode57 to set
     */
    public void setAuthLifeCode57(String authLifeCode57) {
        this.authLifeCode57 = authLifeCode57;
    }

    /**
     * Returns the authAgentIdCode58 value
     *
     * @return the authAgentIdCode58
     */
    public String getAuthAgentIdCode58() {
        return authAgentIdCode58;
    }

    /**
     * Set the authAgentIdCode58 value
     *
     * @param authAgentIdCode58
     *          the authAgentIdCode58 to set
     */
    public void setAuthAgentIdCode58(String authAgentIdCode58) {
        this.authAgentIdCode58 = authAgentIdCode58;
    }

    /**
     * Returns the echoData59 value
     *
     * @return the echoData59
     */
    public String getEchoData59() {
        return echoData59;
    }

    /**
     * Set the echoData59 value
     *
     * @param echoData59
     *          the echoData59 to set
     */
    public void setEchoData59(String echoData59) {
        this.echoData59 = echoData59;
    }

    /**
     * Returns the reservedData60 value
     *
     * @return the reservedData60
     */
    public String getReservedData60() {
        return reservedData60;
    }

    /**
     * Set the reservedData60 value
     *
     * @param reservedData60
     *          the reservedData60 to set
     */
    public void setReservedData60(String reservedData60) {
        this.reservedData60 = reservedData60;
    }

    /**
     * Returns the reservedData61 value
     *
     * @return the reservedData61
     */
    public String getReservedData61() {
        return reservedData61;
    }

    /**
     * Set the reservedData61 value
     *
     * @param reservedData61
     *          the reservedData61 to set
     */
    public void setReservedData61(String reservedData61) {
        this.reservedData61 = reservedData61;
    }

    /**
     * Returns the reservedData62 value
     *
     * @return the reservedData62
     */
    public String getReservedData62() {
        return reservedData62;
    }

    /**
     * Set the reservedData62 value
     *
     * @param reservedData62
     *          the reservedData62 to set
     */
    public void setReservedData62(String reservedData62) {
        this.reservedData62 = reservedData62;
    }

    /**
     * Returns the reservedData63 value
     *
     * @return the reservedData63
     */
    public String getReservedData63() {
        return reservedData63;
    }

    /**
     * Set the reservedData63 value
     *
     * @param reservedData63
     *          the reservedData63 to set
     */
    public void setReservedData63(String reservedData63) {
        this.reservedData63 = reservedData63;
    }

    /**
     * Returns the messageAuthenticationCodeField64 value
     *
     * @return the messageAuthenticationCodeField64
     */
    public byte[] getMessageAuthenticationCodeField64() {
        return messageAuthenticationCodeField64;
    }

    /**
     * Set the messageAuthenticationCodeField64 value
     *
     * @param messageAuthenticationCodeField64
     *          the messageAuthenticationCodeField64 to set
     */
    public void setMessageAuthenticationCodeField64(byte[] messageAuthenticationCodeField64) {
        this.messageAuthenticationCodeField64 = messageAuthenticationCodeField64;
    }

    /**
     * Returns the reservedData65 value
     *
     * @return the reservedData65
     */
    public String getReservedData65() {
        return reservedData65;
    }

    /**
     * Set the reservedData65 value
     *
     * @param reservedData65
     *          the reservedData65 to set
     */
    public void setReservedData65(String reservedData65) {
        this.reservedData65 = reservedData65;
    }

    /**
     * Returns the statleMentCode66 value
     *
     * @return the statleMentCode66
     */
    public String getStatleMentCode66() {
        return statleMentCode66;
    }

    /**
     * Set the statleMentCode66 value
     *
     * @param statleMentCode66
     *          the statleMentCode66 to set
     */
    public void setStatleMentCode66(String statleMentCode66) {
        this.statleMentCode66 = statleMentCode66;
    }

    /**
     * Returns the extPaymentCode67 value
     *
     * @return the extPaymentCode67
     */
    public String getExtPaymentCode67() {
        return extPaymentCode67;
    }

    /**
     * Set the extPaymentCode67 value
     *
     * @param extPaymentCode67
     *          the extPaymentCode67 to set
     */
    public void setExtPaymentCode67(String extPaymentCode67) {
        this.extPaymentCode67 = extPaymentCode67;
    }

    /**
     * Returns the countryCodeReceivingInstitution68 value
     *
     * @return the countryCodeReceivingInstitution68
     */
    public String getCountryCodeReceivingInstitution68() {
        return countryCodeReceivingInstitution68;
    }

    /**
     * Set the countryCodeReceivingInstitution68 value
     *
     * @param countryCodeReceivingInstitution68
     *          the countryCodeReceivingInstitution68 to set
     */
    public void setCountryCodeReceivingInstitution68(String countryCodeReceivingInstitution68) {
        this.countryCodeReceivingInstitution68 = countryCodeReceivingInstitution68;
    }

    /**
     * Returns the countryCodeSettlementInstitution69 value
     *
     * @return the countryCodeSettlementInstitution69
     */
    public String getCountryCodeSettlementInstitution69() {
        return countryCodeSettlementInstitution69;
    }

    /**
     * Set the countryCodeSettlementInstitution69 value
     *
     * @param countryCodeSettlementInstitution69
     *          the countryCodeSettlementInstitution69 to set
     */
    public void setCountryCodeSettlementInstitution69(String countryCodeSettlementInstitution69) {
        this.countryCodeSettlementInstitution69 = countryCodeSettlementInstitution69;
    }

    /**
     * Returns the netMgnInfoCode70 value
     *
     * @return the netMgnInfoCode70
     */
    public String getNetMgnInfoCode70() {
        return netMgnInfoCode70;
    }

    /**
     * Set the netMgnInfoCode70 value
     *
     * @param netMgnInfoCode70
     *          the netMgnInfoCode70 to set
     */
    public void setNetMgnInfoCode70(String netMgnInfoCode70) {
        this.netMgnInfoCode70 = netMgnInfoCode70;
    }

    /**
     * Returns the messageNumber71 value
     *
     * @return the messageNumber71
     */
    public String getMessageNumber71() {
        return messageNumber71;
    }

    /**
     * Set the messageNumber71 value
     *
     * @param messageNumber71
     *          the messageNumber71 to set
     */
    public void setMessageNumber71(String messageNumber71) {
        this.messageNumber71 = messageNumber71;
    }

    /**
     * Returns the dataRecord72 value
     *
     * @return the dataRecord72
     */
    public String getDataRecord72() {
        return dataRecord72;
    }

    /**
     * Set the dataRecord72 value
     *
     * @param dataRecord72
     *          the dataRecord72 to set
     */
    public void setDataRecord72(String dataRecord72) {
        this.dataRecord72 = dataRecord72;
    }

    /**
     * Returns the dateAction73 value
     *
     * @return the dateAction73
     */
    public String getDateAction73() {
        return dateAction73;
    }

    /**
     * Set the dateAction73 value
     *
     * @param dateAction73
     *          the dateAction73 to set
     */
    public void setDateAction73(String dateAction73) {
        this.dateAction73 = dateAction73;
    }

    /**
     * Returns the creditNo74 value
     *
     * @return the creditNo74
     */
    public String getCreditNo74() {
        return creditNo74;
    }

    /**
     * Set the creditNo74 value
     *
     * @param creditNo74
     *          the creditNo74 to set
     */
    public void setCreditNo74(String creditNo74) {
        this.creditNo74 = creditNo74;
    }

    /**
     * Returns the creditRevNo75 value
     *
     * @return the creditRevNo75
     */
    public String getCreditRevNo75() {
        return creditRevNo75;
    }

    /**
     * Set the creditRevNo75 value
     *
     * @param creditRevNo75
     *          the creditRevNo75 to set
     */
    public void setCreditRevNo75(String creditRevNo75) {
        this.creditRevNo75 = creditRevNo75;
    }

    /**
     * Returns the debitNo76 value
     *
     * @return the debitNo76
     */
    public String getDebitNo76() {
        return debitNo76;
    }

    /**
     * Set the debitNo76 value
     *
     * @param debitNo76
     *          the debitNo76 to set
     */
    public void setDebitNo76(String debitNo76) {
        this.debitNo76 = debitNo76;
    }

    /**
     * Returns the debitRevNo77 value
     *
     * @return the debitRevNo77
     */
    public String getDebitRevNo77() {
        return debitRevNo77;
    }

    /**
     * Set the debitRevNo77 value
     *
     * @param debitRevNo77
     *          the debitRevNo77 to set
     */
    public void setDebitRevNo77(String debitRevNo77) {
        this.debitRevNo77 = debitRevNo77;
    }

    /**
     * Returns the transperNo78 value
     *
     * @return the transperNo78
     */
    public String getTransperNo78() {
        return transperNo78;
    }

    /**
     * Set the transperNo78 value
     *
     * @param transperNo78
     *          the transperNo78 to set
     */
    public void setTransperNo78(String transperNo78) {
        this.transperNo78 = transperNo78;
    }

    /**
     * Returns the transperRevNo79 value
     *
     * @return the transperRevNo79
     */
    public String getTransperRevNo79() {
        return transperRevNo79;
    }

    /**
     * Set the transperRevNo79 value
     *
     * @param transperRevNo79
     *          the transperRevNo79 to set
     */
    public void setTransperRevNo79(String transperRevNo79) {
        this.transperRevNo79 = transperRevNo79;
    }

    /**
     * Returns the inquiriesNo80 value
     *
     * @return the inquiriesNo80
     */
    public String getInquiriesNo80() {
        return inquiriesNo80;
    }

    /**
     * Set the inquiriesNo80 value
     *
     * @param inquiriesNo80
     *          the inquiriesNo80 to set
     */
    public void setInquiriesNo80(String inquiriesNo80) {
        this.inquiriesNo80 = inquiriesNo80;
    }

    /**
     * Returns the authNo81 value
     *
     * @return the authNo81
     */
    public String getAuthNo81() {
        return authNo81;
    }

    /**
     * Set the authNo81 value
     *
     * @param authNo81
     *          the authNo81 to set
     */
    public void setAuthNo81(String authNo81) {
        this.authNo81 = authNo81;
    }

    /**
     * Returns the creditProcessFeeAmt82 value
     *
     * @return the creditProcessFeeAmt82
     */
    public String getCreditProcessFeeAmt82() {
        return creditProcessFeeAmt82;
    }

    /**
     * Set the creditProcessFeeAmt82 value
     *
     * @param creditProcessFeeAmt82
     *          the creditProcessFeeAmt82 to set
     */
    public void setCreditProcessFeeAmt82(String creditProcessFeeAmt82) {
        this.creditProcessFeeAmt82 = creditProcessFeeAmt82;
    }

    /**
     * Returns the creditTransFeeAmt83 value
     *
     * @return the creditTransFeeAmt83
     */
    public String getCreditTransFeeAmt83() {
        return creditTransFeeAmt83;
    }

    /**
     * Set the creditTransFeeAmt83 value
     *
     * @param creditTransFeeAmt83
     *          the creditTransFeeAmt83 to set
     */
    public void setCreditTransFeeAmt83(String creditTransFeeAmt83) {
        this.creditTransFeeAmt83 = creditTransFeeAmt83;
    }

    /**
     * Returns the debitProcessFeeAmt84 value
     *
     * @return the debitProcessFeeAmt84
     */
    public String getDebitProcessFeeAmt84() {
        return debitProcessFeeAmt84;
    }

    /**
     * Set the debitProcessFeeAmt84 value
     *
     * @param debitProcessFeeAmt84
     *          the debitProcessFeeAmt84 to set
     */
    public void setDebitProcessFeeAmt84(String debitProcessFeeAmt84) {
        this.debitProcessFeeAmt84 = debitProcessFeeAmt84;
    }

    /**
     * Returns the debitTransFeeAmt85 value
     *
     * @return the debitTransFeeAmt85
     */
    public String getDebitTransFeeAmt85() {
        return debitTransFeeAmt85;
    }

    /**
     * Set the debitTransFeeAmt85 value
     *
     * @param debitTransFeeAmt85
     *          the debitTransFeeAmt85 to set
     */
    public void setDebitTransFeeAmt85(String debitTransFeeAmt85) {
        this.debitTransFeeAmt85 = debitTransFeeAmt85;
    }

    /**
     * Returns the creditAmt86 value
     *
     * @return the creditAmt86
     */
    public String getCreditAmt86() {
        return creditAmt86;
    }

    /**
     * Set the creditAmt86 value
     *
     * @param creditAmt86
     *          the creditAmt86 to set
     */
    public void setCreditAmt86(String creditAmt86) {
        this.creditAmt86 = creditAmt86;
    }

    /**
     * Returns the creditRevAmt87 value
     *
     * @return the creditRevAmt87
     */
    public String getCreditRevAmt87() {
        return creditRevAmt87;
    }

    /**
     * Set the creditRevAmt87 value
     *
     * @param creditRevAmt87
     *          the creditRevAmt87 to set
     */
    public void setCreditRevAmt87(String creditRevAmt87) {
        this.creditRevAmt87 = creditRevAmt87;
    }

    /**
     * Returns the debitAmt88 value
     *
     * @return the debitAmt88
     */
    public String getDebitAmt88() {
        return debitAmt88;
    }

    /**
     * Set the debitAmt88 value
     *
     * @param debitAmt88
     *          the debitAmt88 to set
     */
    public void setDebitAmt88(String debitAmt88) {
        this.debitAmt88 = debitAmt88;
    }

    /**
     * Returns the debitRevAmt89 value
     *
     * @return the debitRevAmt89
     */
    public String getDebitRevAmt89() {
        return debitRevAmt89;
    }

    /**
     * Set the debitRevAmt89 value
     *
     * @param debitRevAmt89
     *          the debitRevAmt89 to set
     */
    public void setDebitRevAmt89(String debitRevAmt89) {
        this.debitRevAmt89 = debitRevAmt89;
    }

    /**
     * Returns the orgDataElement90 value
     *
     * @return the orgDataElement90
     */
    public String getOrgDataElement90() {
        return orgDataElement90;
    }

    /**
     * Set the orgDataElement90 value
     *
     * @param orgDataElement90
     *          the orgDataElement90 to set
     */
    public void setOrgDataElement90(String orgDataElement90) {
        this.orgDataElement90 = orgDataElement90;
    }

    /**
     * Returns the fileUpdateCode91 value
     *
     * @return the fileUpdateCode91
     */
    public String getFileUpdateCode91() {
        return fileUpdateCode91;
    }

    /**
     * Set the fileUpdateCode91 value
     *
     * @param fileUpdateCode91
     *          the fileUpdateCode91 to set
     */
    public void setFileUpdateCode91(String fileUpdateCode91) {
        this.fileUpdateCode91 = fileUpdateCode91;
    }

    /**
     * Returns the countryCodeTransactionOrig92 value
     *
     * @return the countryCodeTransactionOrig92
     */
    public String getCountryCodeTransactionOrig92() {
        return countryCodeTransactionOrig92;
    }

    /**
     * Set the countryCodeTransactionOrig92 value
     *
     * @param countryCodeTransactionOrig92
     *          the countryCodeTransactionOrig92 to set
     */
    public void setCountryCodeTransactionOrig92(String countryCodeTransactionOrig92) {
        this.countryCodeTransactionOrig92 = countryCodeTransactionOrig92;
    }

    /**
     * Returns the transactionDest93 value
     *
     * @return the transactionDest93
     */
    public String getTransactionDest93() {
        return transactionDest93;
    }

    /**
     * Set the transactionDest93 value
     *
     * @param transactionDest93
     *          the transactionDest93 to set
     */
    public void setTransactionDest93(String transactionDest93) {
        this.transactionDest93 = transactionDest93;
    }

    /**
     * Returns the transactionOrig94 value
     *
     * @return the transactionOrig94
     */
    public String getTransactionOrig94() {
        return transactionOrig94;
    }

    /**
     * Set the transactionOrig94 value
     *
     * @param transactionOrig94
     *          the transactionOrig94 to set
     */
    public void setTransactionOrig94(String transactionOrig94) {
        this.transactionOrig94 = transactionOrig94;
    }

    /**
     * Returns the replaceAmt95 value
     *
     * @return the replaceAmt95
     */
    public String getReplaceAmt95() {
        return replaceAmt95;
    }

    /**
     * Set the replaceAmt95 value
     *
     * @param replaceAmt95
     *          the replaceAmt95 to set
     */
    public void setReplaceAmt95(String replaceAmt95) {
        this.replaceAmt95 = replaceAmt95;
    }

    /**
     * Returns the keyManagementData96 value
     *
     * @return the keyManagementData96
     */
    public String getKeyManagementData96() {
        return keyManagementData96;
    }

    /**
     * Set the keyManagementData96 value
     *
     * @param keyManagementData96
     *          the keyManagementData96 to set
     */
    public void setKeyManagementData96(String keyManagementData96) {
        this.keyManagementData96 = keyManagementData96;
    }

    /**
     * Returns the amtNetSattle97 value
     *
     * @return the amtNetSattle97
     */
    public String getAmtNetSattle97() {
        return amtNetSattle97;
    }

    /**
     * Set the amtNetSattle97 value
     *
     * @param amtNetSattle97
     *          the amtNetSattle97 to set
     */
    public void setAmtNetSattle97(String amtNetSattle97) {
        this.amtNetSattle97 = amtNetSattle97;
    }

    /**
     * Returns the payee98 value
     *
     * @return the payee98
     */
    public String getPayee98() {
        return payee98;
    }

    /**
     * Set the payee98 value
     *
     * @param payee98
     *          the payee98 to set
     */
    public void setPayee98(String payee98) {
        this.payee98 = payee98;
    }

    /**
     * Returns the settlementInstitutionIdCode99 value
     *
     * @return the settlementInstitutionIdCode99
     */
    public String getSettlementInstitutionIdCode99() {
        return settlementInstitutionIdCode99;
    }

    /**
     * Set the settlementInstitutionIdCode99 value
     *
     * @param settlementInstitutionIdCode99
     *          the settlementInstitutionIdCode99 to set
     */
    public void setSettlementInstitutionIdCode99(String settlementInstitutionIdCode99) {
        this.settlementInstitutionIdCode99 = settlementInstitutionIdCode99;
    }

    /**
     * Returns the receiveInsituteIdCode100 value
     *
     * @return the receiveInsituteIdCode100
     */
    public String getReceiveInsituteIdCode100() {
        return receiveInsituteIdCode100;
    }

    /**
     * Set the receiveInsituteIdCode100 value
     *
     * @param receiveInsituteIdCode100
     *          the receiveInsituteIdCode100 to set
     */
    public void setReceiveInsituteIdCode100(String receiveInsituteIdCode100) {
        this.receiveInsituteIdCode100 = receiveInsituteIdCode100;
    }

    /**
     * Returns the fileName101 value
     *
     * @return the fileName101
     */
    public String getFileName101() {
        return fileName101;
    }

    /**
     * Set the fileName101 value
     *
     * @param fileName101
     *          the fileName101 to set
     */
    public void setFileName101(String fileName101) {
        this.fileName101 = fileName101;
    }

    /**
     * Returns the accIdentOne102 value
     *
     * @return the accIdentOne102
     */
    public String getAccIdentOne102() {
        return accIdentOne102;
    }

    /**
     * Set the accIdentOne102 value
     *
     * @param accIdentOne102
     *          the accIdentOne102 to set
     */
    public void setAccIdentOne102(String accIdentOne102) {
        this.accIdentOne102 = accIdentOne102;
    }

    /**
     * Returns the accIdentTwo103 value
     *
     * @return the accIdentTwo103
     */
    public String getAccIdentTwo103() {
        return accIdentTwo103;
    }

    /**
     * Set the accIdentTwo103 value
     *
     * @param accIdentTwo103
     *          the accIdentTwo103 to set
     */
    public void setAccIdentTwo103(String accIdentTwo103) {
        this.accIdentTwo103 = accIdentTwo103;
    }

    /**
     * Returns the reservedData104 value
     *
     * @return the reservedData104
     */
    public String getReservedData104() {
        return reservedData104;
    }

    /**
     * Set the reservedData104 value
     *
     * @param reservedData104
     *          the reservedData104 to set
     */
    public void setReservedData104(String reservedData104) {
        this.reservedData104 = reservedData104;
    }

    /**
     * Returns the creditsChargebackAmount105 value
     *
     * @return the creditsChargebackAmount105
     */
    public String getCreditsChargebackAmount105() {
        return creditsChargebackAmount105;
    }

    /**
     * Set the creditsChargebackAmount105 value
     *
     * @param creditsChargebackAmount105
     *          the creditsChargebackAmount105 to set
     */
    public void setCreditsChargebackAmount105(String creditsChargebackAmount105) {
        this.creditsChargebackAmount105 = creditsChargebackAmount105;
    }

    /**
     * Returns the debitsChargebackAmount106 value
     *
     * @return the debitsChargebackAmount106
     */
    public String getDebitsChargebackAmount106() {
        return debitsChargebackAmount106;
    }

    /**
     * Set the debitsChargebackAmount106 value
     *
     * @param debitsChargebackAmount106
     *          the debitsChargebackAmount106 to set
     */
    public void setDebitsChargebackAmount106(String debitsChargebackAmount106) {
        this.debitsChargebackAmount106 = debitsChargebackAmount106;
    }

    /**
     * Returns the creditsChargebackNumber107 value
     *
     * @return the creditsChargebackNumber107
     */
    public String getCreditsChargebackNumber107() {
        return creditsChargebackNumber107;
    }

    /**
     * Set the creditsChargebackNumber107 value
     *
     * @param creditsChargebackNumber107
     *          the creditsChargebackNumber107 to set
     */
    public void setCreditsChargebackNumber107(String creditsChargebackNumber107) {
        this.creditsChargebackNumber107 = creditsChargebackNumber107;
    }

    /**
     * Returns the debitsChargebackNumber108 value
     *
     * @return the debitsChargebackNumber108
     */
    public String getDebitsChargebackNumber108() {
        return debitsChargebackNumber108;
    }

    /**
     * Set the debitsChargebackNumber108 value
     *
     * @param debitsChargebackNumber108
     *          the debitsChargebackNumber108 to set
     */
    public void setDebitsChargebackNumber108(String debitsChargebackNumber108) {
        this.debitsChargebackNumber108 = debitsChargebackNumber108;
    }

    /**
     * Returns the creditsFeeAmounts109 value
     *
     * @return the creditsFeeAmounts109
     */
    public String getCreditsFeeAmounts109() {
        return creditsFeeAmounts109;
    }

    /**
     * Set the creditsFeeAmounts109 value
     *
     * @param creditsFeeAmounts109
     *          the creditsFeeAmounts109 to set
     */
    public void setCreditsFeeAmounts109(String creditsFeeAmounts109) {
        this.creditsFeeAmounts109 = creditsFeeAmounts109;
    }

    /**
     * Returns the debitsFeAmounts110 value
     *
     * @return the debitsFeAmounts110
     */
    public String getDebitsFeAmounts110() {
        return debitsFeAmounts110;
    }

    /**
     * Set the debitsFeAmounts110 value
     *
     * @param debitsFeAmounts110
     *          the debitsFeAmounts110 to set
     */
    public void setDebitsFeAmounts110(String debitsFeAmounts110) {
        this.debitsFeAmounts110 = debitsFeAmounts110;
    }

    /**
     * Returns the reservedData111 value
     *
     * @return the reservedData111
     */
    public String getReservedData111() {
        return reservedData111;
    }

    /**
     * Set the reservedData111 value
     *
     * @param reservedData111
     *          the reservedData111 to set
     */
    public void setReservedData111(String reservedData111) {
        this.reservedData111 = reservedData111;
    }

    /**
     * Returns the reservedData112 value
     *
     * @return the reservedData112
     */
    public String getReservedData112() {
        return reservedData112;
    }

    /**
     * Set the reservedData112 value
     *
     * @param reservedData112
     *          the reservedData112 to set
     */
    public void setReservedData112(String reservedData112) {
        this.reservedData112 = reservedData112;
    }

    /**
     * Returns the reservedData113 value
     *
     * @return the reservedData113
     */
    public String getReservedData113() {
        return reservedData113;
    }

    /**
     * Set the reservedData113 value
     *
     * @param reservedData113
     *          the reservedData113 to set
     */
    public void setReservedData113(String reservedData113) {
        this.reservedData113 = reservedData113;
    }

    /**
     * Returns the reservedData114 value
     *
     * @return the reservedData114
     */
    public String getReservedData114() {
        return reservedData114;
    }

    /**
     * Set the reservedData114 value
     *
     * @param reservedData114
     *          the reservedData114 to set
     */
    public void setReservedData114(String reservedData114) {
        this.reservedData114 = reservedData114;
    }

    /**
     * Returns the reservedData115 value
     *
     * @return the reservedData115
     */
    public String getReservedData115() {
        return reservedData115;
    }

    /**
     * Set the reservedData115 value
     *
     * @param reservedData115
     *          the reservedData115 to set
     */
    public void setReservedData115(String reservedData115) {
        this.reservedData115 = reservedData115;
    }

    /**
     * Returns the reservedData116 value
     *
     * @return the reservedData116
     */
    public String getReservedData116() {
        return reservedData116;
    }

    /**
     * Set the reservedData116 value
     *
     * @param reservedData116
     *          the reservedData116 to set
     */
    public void setReservedData116(String reservedData116) {
        this.reservedData116 = reservedData116;
    }

    /**
     * Returns the reservedData117 value
     *
     * @return the reservedData117
     */
    public String getReservedData117() {
        return reservedData117;
    }

    /**
     * Set the reservedData117 value
     *
     * @param reservedData117
     *          the reservedData117 to set
     */
    public void setReservedData117(String reservedData117) {
        this.reservedData117 = reservedData117;
    }

    /**
     * Returns the paymentNo118 value
     *
     * @return the paymentNo118
     */
    public String getPaymentNo118() {
        return paymentNo118;
    }

    /**
     * Set the paymentNo118 value
     *
     * @param paymentNo118
     *          the paymentNo118 to set
     */
    public void setPaymentNo118(String paymentNo118) {
        this.paymentNo118 = paymentNo118;
    }

    /**
     * Returns the paymentRevNo119 value
     *
     * @return the paymentRevNo119
     */
    public String getPaymentRevNo119() {
        return paymentRevNo119;
    }

    /**
     * Set the paymentRevNo119 value
     *
     * @param paymentRevNo119
     *          the paymentRevNo119 to set
     */
    public void setPaymentRevNo119(String paymentRevNo119) {
        this.paymentRevNo119 = paymentRevNo119;
    }

    /**
     * Returns the reservedData120 value
     *
     * @return the reservedData120
     */
    public String getReservedData120() {
        return reservedData120;
    }

    /**
     * Set the reservedData120 value
     *
     * @param reservedData120
     *          the reservedData120 to set
     */
    public void setReservedData120(String reservedData120) {
        this.reservedData120 = reservedData120;
    }

    /**
     * Returns the reservedData121 value
     *
     * @return the reservedData121
     */
    public String getReservedData121() {
        return reservedData121;
    }

    /**
     * Set the reservedData121 value
     *
     * @param reservedData121
     *          the reservedData121 to set
     */
    public void setReservedData121(String reservedData121) {
        this.reservedData121 = reservedData121;
    }

    /**
     * Returns the reservedData122 value
     *
     * @return the reservedData122
     */
    public String getReservedData122() {
        return reservedData122;
    }

    /**
     * Set the reservedData122 value
     *
     * @param reservedData122
     *          the reservedData122 to set
     */
    public void setReservedData122(String reservedData122) {
        this.reservedData122 = reservedData122;
    }

    /**
     * Returns the posDataCode123 value
     *
     * @return the posDataCode123
     */
    public String getPosDataCode123() {
        return posDataCode123;
    }

    /**
     * Set the posDataCode123 value
     *
     * @param posDataCode123
     *          the posDataCode123 to set
     */
    public void setPosDataCode123(String posDataCode123) {
        this.posDataCode123 = posDataCode123;
    }

    /**
     * Returns the reservedData124 value
     *
     * @return the reservedData124
     */
    public String getReservedData124() {
        return reservedData124;
    }

    /**
     * Set the reservedData124 value
     *
     * @param reservedData124
     *          the reservedData124 to set
     */
    public void setReservedData124(String reservedData124) {
        this.reservedData124 = reservedData124;
    }

    /**
     * Returns the netMgnInfo125 value
     *
     * @return the netMgnInfo125
     */
    public String getNetMgnInfo125() {
        return netMgnInfo125;
    }

    /**
     * Set the netMgnInfo125 value
     *
     * @param netMgnInfo125
     *          the netMgnInfo125 to set
     */
    public void setNetMgnInfo125(String netMgnInfo125) {
        this.netMgnInfo125 = netMgnInfo125;
    }

    /**
     * Returns the reservedData126 value
     *
     * @return the reservedData126
     */
    public String getReservedData126() {
        return reservedData126;
    }

    /**
     * Set the reservedData126 value
     *
     * @param reservedData126
     *          the reservedData126 to set
     */
    public void setReservedData126(String reservedData126) {
        this.reservedData126 = reservedData126;
    }

    /**
     * Returns the switchKey127_2 value
     *
     * @return the switchKey127_2
     */
    public String getSwitchKey127_2() {
        return switchKey127_2;
    }

    /**
     * Set the switchKey127_2 value
     *
     * @param switchKey127_2
     *          the switchKey127_2 to set
     */
    public void setSwitchKey127_2(String switchKey127_2) {
        this.switchKey127_2 = switchKey127_2;
    }

    /**
     * Returns the routeInfo127_3 value
     *
     * @return the routeInfo127_3
     */
    public String getRouteInfo127_3() {
        return routeInfo127_3;
    }

    /**
     * Set the routeInfo127_3 value
     *
     * @param routeInfo127_3
     *          the routeInfo127_3 to set
     */
    public void setRouteInfo127_3(String routeInfo127_3) {
        this.routeInfo127_3 = routeInfo127_3;
    }

    /**
     * Returns the posData127_4 value
     *
     * @return the posData127_4
     */
    public String getPosData127_4() {
        return posData127_4;
    }

    /**
     * Set the posData127_4 value
     *
     * @param posData127_4
     *          the posData127_4 to set
     */
    public void setPosData127_4(String posData127_4) {
        this.posData127_4 = posData127_4;
    }

    /**
     * Returns the serStationData127_5 value
     *
     * @return the serStationData127_5
     */
    public String getSerStationData127_5() {
        return serStationData127_5;
    }

    /**
     * Set the serStationData127_5 value
     *
     * @param serStationData127_5
     *          the serStationData127_5 to set
     */
    public void setSerStationData127_5(String serStationData127_5) {
        this.serStationData127_5 = serStationData127_5;
    }

    /**
     * Returns the authProfile127_6 value
     *
     * @return the authProfile127_6
     */
    public String getAuthProfile127_6() {
        return authProfile127_6;
    }

    /**
     * Set the authProfile127_6 value
     *
     * @param authProfile127_6
     *          the authProfile127_6 to set
     */
    public void setAuthProfile127_6(String authProfile127_6) {
        this.authProfile127_6 = authProfile127_6;
    }

    /**
     * Returns the checkData127_7 value
     *
     * @return the checkData127_7
     */
    public String getCheckData127_7() {
        return checkData127_7;
    }

    /**
     * Set the checkData127_7 value
     *
     * @param checkData127_7
     *          the checkData127_7 to set
     */
    public void setCheckData127_7(String checkData127_7) {
        this.checkData127_7 = checkData127_7;
    }

    /**
     * Returns the retentionData127_8 value
     *
     * @return the retentionData127_8
     */
    public String getRetentionData127_8() {
        return retentionData127_8;
    }

    /**
     * Set the retentionData127_8 value
     *
     * @param retentionData127_8
     *          the retentionData127_8 to set
     */
    public void setRetentionData127_8(String retentionData127_8) {
        this.retentionData127_8 = retentionData127_8;
    }

    /**
     * Returns the addlNodeData127_9 value
     *
     * @return the addlNodeData127_9
     */
    public String getAddlNodeData127_9() {
        return addlNodeData127_9;
    }

    /**
     * Set the addlNodeData127_9 value
     *
     * @param addlNodeData127_9
     *          the addlNodeData127_9 to set
     */
    public void setAddlNodeData127_9(String addlNodeData127_9) {
        this.addlNodeData127_9 = addlNodeData127_9;
    }

    /**
     * Returns the cvv2127_10 value
     *
     * @return the cvv2127_10
     */
    public String getCvv2127_10() {
        return cvv2127_10;
    }

    /**
     * Set the cvv2127_10 value
     *
     * @param cvv2127_10
     *          the cvv2127_10 to set
     */
    public void setCvv2127_10(String cvv2127_10) {
        this.cvv2127_10 = cvv2127_10;
    }

    /**
     * Returns the orgKey127_11 value
     *
     * @return the orgKey127_11
     */
    public String getOrgKey127_11() {
        return orgKey127_11;
    }

    /**
     * Set the orgKey127_11 value
     *
     * @param orgKey127_11
     *          the orgKey127_11 to set
     */
    public void setOrgKey127_11(String orgKey127_11) {
        this.orgKey127_11 = orgKey127_11;
    }

    /**
     * Returns the termOwner127_12 value
     *
     * @return the termOwner127_12
     */
    public String getTermOwner127_12() {
        return termOwner127_12;
    }

    /**
     * Set the termOwner127_12 value
     *
     * @param termOwner127_12
     *          the termOwner127_12 to set
     */
    public void setTermOwner127_12(String termOwner127_12) {
        this.termOwner127_12 = termOwner127_12;
    }

    /**
     * Returns the posGeograpicData127_13 value
     *
     * @return the posGeograpicData127_13
     */
    public String getPosGeograpicData127_13() {
        return posGeograpicData127_13;
    }

    /**
     * Set the posGeograpicData127_13 value
     *
     * @param posGeograpicData127_13
     *          the posGeograpicData127_13 to set
     */
    public void setPosGeograpicData127_13(String posGeograpicData127_13) {
        this.posGeograpicData127_13 = posGeograpicData127_13;
    }

    /**
     * Returns the sponsorBank127_14 value
     *
     * @return the sponsorBank127_14
     */
    public String getSponsorBank127_14() {
        return sponsorBank127_14;
    }

    /**
     * Set the sponsorBank127_14 value
     *
     * @param sponsorBank127_14
     *          the sponsorBank127_14 to set
     */
    public void setSponsorBank127_14(String sponsorBank127_14) {
        this.sponsorBank127_14 = sponsorBank127_14;
    }

    /**
     * Returns the addressVerificData127_15 value
     *
     * @return the addressVerificData127_15
     */
    public String getAddressVerificData127_15() {
        return addressVerificData127_15;
    }

    /**
     * Set the addressVerificData127_15 value
     *
     * @param addressVerificData127_15
     *          the addressVerificData127_15 to set
     */
    public void setAddressVerificData127_15(String addressVerificData127_15) {
        this.addressVerificData127_15 = addressVerificData127_15;
    }

    /**
     * Returns the addressVerificResult127_16 value
     *
     * @return the addressVerificResult127_16
     */
    public String getAddressVerificResult127_16() {
        return addressVerificResult127_16;
    }

    /**
     * Set the addressVerificResult127_16 value
     *
     * @param addressVerificResult127_16
     *          the addressVerificResult127_16 to set
     */
    public void setAddressVerificResult127_16(String addressVerificResult127_16) {
        this.addressVerificResult127_16 = addressVerificResult127_16;
    }

    /**
     * Returns the cardHolderInfo127_17 value
     *
     * @return the cardHolderInfo127_17
     */
    public String getCardHolderInfo127_17() {
        return cardHolderInfo127_17;
    }

    /**
     * Set the cardHolderInfo127_17 value
     *
     * @param cardHolderInfo127_17
     *          the cardHolderInfo127_17 to set
     */
    public void setCardHolderInfo127_17(String cardHolderInfo127_17) {
        this.cardHolderInfo127_17 = cardHolderInfo127_17;
    }

    /**
     * Returns the validationData127_18 value
     *
     * @return the validationData127_18
     */
    public String getValidationData127_18() {
        return validationData127_18;
    }

    /**
     * Set the validationData127_18 value
     *
     * @param validationData127_18
     *          the validationData127_18 to set
     */
    public void setValidationData127_18(String validationData127_18) {
        this.validationData127_18 = validationData127_18;
    }

    /**
     * Returns the bankDetail127_19 value
     *
     * @return the bankDetail127_19
     */
    public String getBankDetail127_19() {
        return bankDetail127_19;
    }

    /**
     * Set the bankDetail127_19 value
     *
     * @param bankDetail127_19
     *          the bankDetail127_19 to set
     */
    public void setBankDetail127_19(String bankDetail127_19) {
        this.bankDetail127_19 = bankDetail127_19;
    }

    /**
     * Returns the origOrAuthorizerDateStattleMent127_20 value
     *
     * @return the origOrAuthorizerDateStattleMent127_20
     */
    public String getOrigOrAuthorizerDateStattleMent127_20() {
        return origOrAuthorizerDateStattleMent127_20;
    }

    /**
     * Set the origOrAuthorizerDateStattleMent127_20 value
     *
     * @param origOrAuthorizerDateStattleMent127_20
     *          the origOrAuthorizerDateStattleMent127_20 to set
     */
    public void setOrigOrAuthorizerDateStattleMent127_20(String origOrAuthorizerDateStattleMent127_20) {
        this.origOrAuthorizerDateStattleMent127_20 = origOrAuthorizerDateStattleMent127_20;
    }

    /**
     * Returns the recordIdentification127_21 value
     *
     * @return the recordIdentification127_21
     */
    public String getRecordIdentification127_21() {
        return recordIdentification127_21;
    }

    /**
     * Set the recordIdentification127_21 value
     *
     * @param recordIdentification127_21
     *          the recordIdentification127_21 to set
     */
    public void setRecordIdentification127_21(String recordIdentification127_21) {
        this.recordIdentification127_21 = recordIdentification127_21;
    }

    /**
     * Returns the srtucreData127_22 value
     *
     * @return the srtucreData127_22
     */
    public String getSrtucreData127_22() {
        return srtucreData127_22;
    }

    /**
     * Set the srtucreData127_22 value
     *
     * @param srtucreData127_22
     *          the srtucreData127_22 to set
     */
    public void setSrtucreData127_22(String srtucreData127_22) {
        this.srtucreData127_22 = srtucreData127_22;
    }

    /**
     * Returns the payeeNameAddress127_23 value
     *
     * @return the payeeNameAddress127_23
     */
    public String getPayeeNameAddress127_23() {
        return payeeNameAddress127_23;
    }

    /**
     * Set the payeeNameAddress127_23 value
     *
     * @param payeeNameAddress127_23
     *          the payeeNameAddress127_23 to set
     */
    public void setPayeeNameAddress127_23(String payeeNameAddress127_23) {
        this.payeeNameAddress127_23 = payeeNameAddress127_23;
    }

    /**
     * Returns the payerAcc127_24 value
     *
     * @return the payerAcc127_24
     */
    public String getPayerAcc127_24() {
        return payerAcc127_24;
    }

    /**
     * Set the payerAcc127_24 value
     *
     * @param payerAcc127_24
     *          the payerAcc127_24 to set
     */
    public void setPayerAcc127_24(String payerAcc127_24) {
        this.payerAcc127_24 = payerAcc127_24;
    }

    /**
     * Returns the iccData127_25 value
     *
     * @return the iccData127_25
     */
    public String getIccData127_25() {
        return iccData127_25;
    }

    /**
     * Set the iccData127_25 value
     *
     * @param iccData127_25
     *          the iccData127_25 to set
     */
    public void setIccData127_25(String iccData127_25) {
        this.iccData127_25 = iccData127_25;
    }

    /**
     * Returns the amtAuthData127_25_2 value
     *
     * @return the amtAuthData127_25_2
     */
    public String getAmtAuthData127_25_2() {
        return amtAuthData127_25_2;
    }

    /**
     * Set the amtAuthData127_25_2 value
     *
     * @param amtAuthData127_25_2
     *          the amtAuthData127_25_2 to set
     */
    public void setAmtAuthData127_25_2(String amtAuthData127_25_2) {
        this.amtAuthData127_25_2 = amtAuthData127_25_2;
    }

    /**
     * Returns the amtOtherData127_25_3 value
     *
     * @return the amtOtherData127_25_3
     */
    public String getAmtOtherData127_25_3() {
        return amtOtherData127_25_3;
    }

    /**
     * Set the amtOtherData127_25_3 value
     *
     * @param amtOtherData127_25_3
     *          the amtOtherData127_25_3 to set
     */
    public void setAmtOtherData127_25_3(String amtOtherData127_25_3) {
        this.amtOtherData127_25_3 = amtOtherData127_25_3;
    }

    /**
     * Returns the appIdentifierData127_25_4 value
     *
     * @return the appIdentifierData127_25_4
     */
    public String getAppIdentifierData127_25_4() {
        return appIdentifierData127_25_4;
    }

    /**
     * Set the appIdentifierData127_25_4 value
     *
     * @param appIdentifierData127_25_4
     *          the appIdentifierData127_25_4 to set
     */
    public void setAppIdentifierData127_25_4(String appIdentifierData127_25_4) {
        this.appIdentifierData127_25_4 = appIdentifierData127_25_4;
    }

    /**
     * Returns the appInterChangePrifileData127_25_5 value
     *
     * @return the appInterChangePrifileData127_25_5
     */
    public String getAppInterChangePrifileData127_25_5() {
        return appInterChangePrifileData127_25_5;
    }

    /**
     * Set the appInterChangePrifileData127_25_5 value
     *
     * @param appInterChangePrifileData127_25_5
     *          the appInterChangePrifileData127_25_5 to set
     */
    public void setAppInterChangePrifileData127_25_5(String appInterChangePrifileData127_25_5) {
        this.appInterChangePrifileData127_25_5 = appInterChangePrifileData127_25_5;
    }

    /**
     * Returns the appTransCounterData127_25_6 value
     *
     * @return the appTransCounterData127_25_6
     */
    public String getAppTransCounterData127_25_6() {
        return appTransCounterData127_25_6;
    }

    /**
     * Set the appTransCounterData127_25_6 value
     *
     * @param appTransCounterData127_25_6
     *          the appTransCounterData127_25_6 to set
     */
    public void setAppTransCounterData127_25_6(String appTransCounterData127_25_6) {
        this.appTransCounterData127_25_6 = appTransCounterData127_25_6;
    }

    /**
     * Returns the appUsageData127_25_7 value
     *
     * @return the appUsageData127_25_7
     */
    public String getAppUsageData127_25_7() {
        return appUsageData127_25_7;
    }

    /**
     * Set the appUsageData127_25_7 value
     *
     * @param appUsageData127_25_7
     *          the appUsageData127_25_7 to set
     */
    public void setAppUsageData127_25_7(String appUsageData127_25_7) {
        this.appUsageData127_25_7 = appUsageData127_25_7;
    }

    /**
     * Returns the authorizationResponseCode127_25_8 value
     *
     * @return the authorizationResponseCode127_25_8
     */
    public String getAuthorizationResponseCode127_25_8() {
        return authorizationResponseCode127_25_8;
    }

    /**
     * Set the authorizationResponseCode127_25_8 value
     *
     * @param authorizationResponseCode127_25_8
     *          the authorizationResponseCode127_25_8 to set
     */
    public void setAuthorizationResponseCode127_25_8(String authorizationResponseCode127_25_8) {
        this.authorizationResponseCode127_25_8 = authorizationResponseCode127_25_8;
    }

    /**
     * Returns the cardAuthenticationReliabilityIndicator127_25_9 value
     *
     * @return the cardAuthenticationReliabilityIndicator127_25_9
     */
    public String getCardAuthenticationReliabilityIndicator127_25_9() {
        return cardAuthenticationReliabilityIndicator127_25_9;
    }

    /**
     * Set the cardAuthenticationReliabilityIndicator127_25_9 value
     *
     * @param cardAuthenticationReliabilityIndicator127_25_9
     *          the cardAuthenticationReliabilityIndicator127_25_9 to set
     */
    public void setCardAuthenticationReliabilityIndicator127_25_9(String cardAuthenticationReliabilityIndicator127_25_9) {
        this.cardAuthenticationReliabilityIndicator127_25_9 = cardAuthenticationReliabilityIndicator127_25_9;
    }

    /**
     * Returns the cardAuthenticationResultsCode127_25_10 value
     *
     * @return the cardAuthenticationResultsCode127_25_10
     */
    public String getCardAuthenticationResultsCode127_25_10() {
        return cardAuthenticationResultsCode127_25_10;
    }

    /**
     * Set the cardAuthenticationResultsCode127_25_10 value
     *
     * @param cardAuthenticationResultsCode127_25_10
     *          the cardAuthenticationResultsCode127_25_10 to set
     */
    public void setCardAuthenticationResultsCode127_25_10(String cardAuthenticationResultsCode127_25_10) {
        this.cardAuthenticationResultsCode127_25_10 = cardAuthenticationResultsCode127_25_10;
    }

    /**
     * Returns the chipConditionCode127_25_11 value
     *
     * @return the chipConditionCode127_25_11
     */
    public String getChipConditionCode127_25_11() {
        return chipConditionCode127_25_11;
    }

    /**
     * Set the chipConditionCode127_25_11 value
     *
     * @param chipConditionCode127_25_11
     *          the chipConditionCode127_25_11 to set
     */
    public void setChipConditionCode127_25_11(String chipConditionCode127_25_11) {
        this.chipConditionCode127_25_11 = chipConditionCode127_25_11;
    }

    /**
     * Returns the appCryptogramData127_25_12 value
     *
     * @return the appCryptogramData127_25_12
     */
    public String getAppCryptogramData127_25_12() {
        return appCryptogramData127_25_12;
    }

    /**
     * Set the appCryptogramData127_25_12 value
     *
     * @param appCryptogramData127_25_12
     *          the appCryptogramData127_25_12 to set
     */
    public void setAppCryptogramData127_25_12(String appCryptogramData127_25_12) {
        this.appCryptogramData127_25_12 = appCryptogramData127_25_12;
    }

    /**
     * Returns the appCryptoGramInfoData127_25_13 value
     *
     * @return the appCryptoGramInfoData127_25_13
     */
    public String getAppCryptoGramInfoData127_25_13() {
        return appCryptoGramInfoData127_25_13;
    }

    /**
     * Set the appCryptoGramInfoData127_25_13 value
     *
     * @param appCryptoGramInfoData127_25_13
     *          the appCryptoGramInfoData127_25_13 to set
     */
    public void setAppCryptoGramInfoData127_25_13(String appCryptoGramInfoData127_25_13) {
        this.appCryptoGramInfoData127_25_13 = appCryptoGramInfoData127_25_13;
    }

    /**
     * Returns the cvmList127_25_14 value
     *
     * @return the cvmList127_25_14
     */
    public String getCvmList127_25_14() {
        return cvmList127_25_14;
    }

    /**
     * Set the cvmList127_25_14 value
     *
     * @param cvmList127_25_14
     *          the cvmList127_25_14 to set
     */
    public void setCvmList127_25_14(String cvmList127_25_14) {
        this.cvmList127_25_14 = cvmList127_25_14;
    }

    /**
     * Returns the cvmResultData127_25_15 value
     *
     * @return the cvmResultData127_25_15
     */
    public String getCvmResultData127_25_15() {
        return cvmResultData127_25_15;
    }

    /**
     * Set the cvmResultData127_25_15 value
     *
     * @param cvmResultData127_25_15
     *          the cvmResultData127_25_15 to set
     */
    public void setCvmResultData127_25_15(String cvmResultData127_25_15) {
        this.cvmResultData127_25_15 = cvmResultData127_25_15;
    }

    /**
     * Returns the interfaceDeviceSerialNumber127_25_16 value
     *
     * @return the interfaceDeviceSerialNumber127_25_16
     */
    public String getInterfaceDeviceSerialNumber127_25_16() {
        return interfaceDeviceSerialNumber127_25_16;
    }

    /**
     * Set the interfaceDeviceSerialNumber127_25_16 value
     *
     * @param interfaceDeviceSerialNumber127_25_16
     *          the interfaceDeviceSerialNumber127_25_16 to set
     */
    public void setInterfaceDeviceSerialNumber127_25_16(String interfaceDeviceSerialNumber127_25_16) {
        this.interfaceDeviceSerialNumber127_25_16 = interfaceDeviceSerialNumber127_25_16;
    }

    /**
     * Returns the issuerAccCodeData127_25_17 value
     *
     * @return the issuerAccCodeData127_25_17
     */
    public String getIssuerAccCodeData127_25_17() {
        return issuerAccCodeData127_25_17;
    }

    /**
     * Set the issuerAccCodeData127_25_17 value
     *
     * @param issuerAccCodeData127_25_17
     *          the issuerAccCodeData127_25_17 to set
     */
    public void setIssuerAccCodeData127_25_17(String issuerAccCodeData127_25_17) {
        this.issuerAccCodeData127_25_17 = issuerAccCodeData127_25_17;
    }

    /**
     * Returns the issueAppData127_25_18 value
     *
     * @return the issueAppData127_25_18
     */
    public String getIssueAppData127_25_18() {
        return issueAppData127_25_18;
    }

    /**
     * Set the issueAppData127_25_18 value
     *
     * @param issueAppData127_25_18
     *          the issueAppData127_25_18 to set
     */
    public void setIssueAppData127_25_18(String issueAppData127_25_18) {
        this.issueAppData127_25_18 = issueAppData127_25_18;
    }

    /**
     * Returns the issuerScriptResults127_25_19 value
     *
     * @return the issuerScriptResults127_25_19
     */
    public String getIssuerScriptResults127_25_19() {
        return issuerScriptResults127_25_19;
    }

    /**
     * Set the issuerScriptResults127_25_19 value
     *
     * @param issuerScriptResults127_25_19
     *          the issuerScriptResults127_25_19 to set
     */
    public void setIssuerScriptResults127_25_19(String issuerScriptResults127_25_19) {
        this.issuerScriptResults127_25_19 = issuerScriptResults127_25_19;
    }

    /**
     * Returns the terminalApplicationVersionNumber127_25_20 value
     *
     * @return the terminalApplicationVersionNumber127_25_20
     */
    public String getTerminalApplicationVersionNumber127_25_20() {
        return terminalApplicationVersionNumber127_25_20;
    }

    /**
     * Set the terminalApplicationVersionNumber127_25_20 value
     *
     * @param terminalApplicationVersionNumber127_25_20
     *          the terminalApplicationVersionNumber127_25_20 to set
     */
    public void setTerminalApplicationVersionNumber127_25_20(String terminalApplicationVersionNumber127_25_20) {
        this.terminalApplicationVersionNumber127_25_20 = terminalApplicationVersionNumber127_25_20;
    }

    /**
     * Returns the termCapableData127_25_21 value
     *
     * @return the termCapableData127_25_21
     */
    public String getTermCapableData127_25_21() {
        return termCapableData127_25_21;
    }

    /**
     * Set the termCapableData127_25_21 value
     *
     * @param termCapableData127_25_21
     *          the termCapableData127_25_21 to set
     */
    public void setTermCapableData127_25_21(String termCapableData127_25_21) {
        this.termCapableData127_25_21 = termCapableData127_25_21;
    }

    /**
     * Returns the termCounCodeData127_25_22 value
     *
     * @return the termCounCodeData127_25_22
     */
    public String getTermCounCodeData127_25_22() {
        return termCounCodeData127_25_22;
    }

    /**
     * Set the termCounCodeData127_25_22 value
     *
     * @param termCounCodeData127_25_22
     *          the termCounCodeData127_25_22 to set
     */
    public void setTermCounCodeData127_25_22(String termCounCodeData127_25_22) {
        this.termCounCodeData127_25_22 = termCounCodeData127_25_22;
    }

    /**
     * Returns the termTypeData127_25_23 value
     *
     * @return the termTypeData127_25_23
     */
    public String getTermTypeData127_25_23() {
        return termTypeData127_25_23;
    }

    /**
     * Set the termTypeData127_25_23 value
     *
     * @param termTypeData127_25_23
     *          the termTypeData127_25_23 to set
     */
    public void setTermTypeData127_25_23(String termTypeData127_25_23) {
        this.termTypeData127_25_23 = termTypeData127_25_23;
    }

    /**
     * Returns the termVerResData127_25_24 value
     *
     * @return the termVerResData127_25_24
     */
    public String getTermVerResData127_25_24() {
        return termVerResData127_25_24;
    }

    /**
     * Set the termVerResData127_25_24 value
     *
     * @param termVerResData127_25_24
     *          the termVerResData127_25_24 to set
     */
    public void setTermVerResData127_25_24(String termVerResData127_25_24) {
        this.termVerResData127_25_24 = termVerResData127_25_24;
    }

    /**
     * Returns the transactionCategoryCode127_25_25 value
     *
     * @return the transactionCategoryCode127_25_25
     */
    public String getTransactionCategoryCode127_25_25() {
        return transactionCategoryCode127_25_25;
    }

    /**
     * Set the transactionCategoryCode127_25_25 value
     *
     * @param transactionCategoryCode127_25_25
     *          the transactionCategoryCode127_25_25 to set
     */
    public void setTransactionCategoryCode127_25_25(String transactionCategoryCode127_25_25) {
        this.transactionCategoryCode127_25_25 = transactionCategoryCode127_25_25;
    }

    /**
     * Returns the tranCurrCodeData127_25_26 value
     *
     * @return the tranCurrCodeData127_25_26
     */
    public String getTranCurrCodeData127_25_26() {
        return tranCurrCodeData127_25_26;
    }

    /**
     * Set the tranCurrCodeData127_25_26 value
     *
     * @param tranCurrCodeData127_25_26
     *          the tranCurrCodeData127_25_26 to set
     */
    public void setTranCurrCodeData127_25_26(String tranCurrCodeData127_25_26) {
        this.tranCurrCodeData127_25_26 = tranCurrCodeData127_25_26;
    }

    /**
     * Returns the tranDateData127_25_27 value
     *
     * @return the tranDateData127_25_27
     */
    public String getTranDateData127_25_27() {
        return tranDateData127_25_27;
    }

    /**
     * Set the tranDateData127_25_27 value
     *
     * @param tranDateData127_25_27
     *          the tranDateData127_25_27 to set
     */
    public void setTranDateData127_25_27(String tranDateData127_25_27) {
        this.tranDateData127_25_27 = tranDateData127_25_27;
    }

    /**
     * Returns the transactionSequenceCounter127_25_28 value
     *
     * @return the transactionSequenceCounter127_25_28
     */
    public String getTransactionSequenceCounter127_25_28() {
        return transactionSequenceCounter127_25_28;
    }

    /**
     * Set the transactionSequenceCounter127_25_28 value
     *
     * @param transactionSequenceCounter127_25_28
     *          the transactionSequenceCounter127_25_28 to set
     */
    public void setTransactionSequenceCounter127_25_28(String transactionSequenceCounter127_25_28) {
        this.transactionSequenceCounter127_25_28 = transactionSequenceCounter127_25_28;
    }

    /**
     * Returns the tranTypeData127_25_29 value
     *
     * @return the tranTypeData127_25_29
     */
    public String getTranTypeData127_25_29() {
        return tranTypeData127_25_29;
    }

    /**
     * Set the tranTypeData127_25_29 value
     *
     * @param tranTypeData127_25_29
     *          the tranTypeData127_25_29 to set
     */
    public void setTranTypeData127_25_29(String tranTypeData127_25_29) {
        this.tranTypeData127_25_29 = tranTypeData127_25_29;
    }

    /**
     * Returns the unpretictableData127_25_30 value
     *
     * @return the unpretictableData127_25_30
     */
    public String getUnpretictableData127_25_30() {
        return unpretictableData127_25_30;
    }

    /**
     * Set the unpretictableData127_25_30 value
     *
     * @param unpretictableData127_25_30
     *          the unpretictableData127_25_30 to set
     */
    public void setUnpretictableData127_25_30(String unpretictableData127_25_30) {
        this.unpretictableData127_25_30 = unpretictableData127_25_30;
    }

    /**
     * Returns the issuerAuthenticationData127_25_31 value
     *
     * @return the issuerAuthenticationData127_25_31
     */
    public String getIssuerAuthenticationData127_25_31() {
        return issuerAuthenticationData127_25_31;
    }

    /**
     * Set the issuerAuthenticationData127_25_31 value
     *
     * @param issuerAuthenticationData127_25_31
     *          the issuerAuthenticationData127_25_31 to set
     */
    public void setIssuerAuthenticationData127_25_31(String issuerAuthenticationData127_25_31) {
        this.issuerAuthenticationData127_25_31 = issuerAuthenticationData127_25_31;
    }

    /**
     * Returns the issuerScriptTemplateOne127_25_32 value
     *
     * @return the issuerScriptTemplateOne127_25_32
     */
    public String getIssuerScriptTemplateOne127_25_32() {
        return issuerScriptTemplateOne127_25_32;
    }

    /**
     * Set the issuerScriptTemplateOne127_25_32 value
     *
     * @param issuerScriptTemplateOne127_25_32
     *          the issuerScriptTemplateOne127_25_32 to set
     */
    public void setIssuerScriptTemplateOne127_25_32(String issuerScriptTemplateOne127_25_32) {
        this.issuerScriptTemplateOne127_25_32 = issuerScriptTemplateOne127_25_32;
    }

    /**
     * Returns the issuerScriptTemplateTwo127_25_33 value
     *
     * @return the issuerScriptTemplateTwo127_25_33
     */
    public String getIssuerScriptTemplateTwo127_25_33() {
        return issuerScriptTemplateTwo127_25_33;
    }

    /**
     * Set the issuerScriptTemplateTwo127_25_33 value
     *
     * @param issuerScriptTemplateTwo127_25_33
     *          the issuerScriptTemplateTwo127_25_33 to set
     */
    public void setIssuerScriptTemplateTwo127_25_33(String issuerScriptTemplateTwo127_25_33) {
        this.issuerScriptTemplateTwo127_25_33 = issuerScriptTemplateTwo127_25_33;
    }

    /**
     * Returns the orgNode127_26 value
     *
     * @return the orgNode127_26
     */
    public String getOrgNode127_26() {
        return orgNode127_26;
    }

    /**
     * Set the orgNode127_26 value
     *
     * @param orgNode127_26
     *          the orgNode127_26 to set
     */
    public void setOrgNode127_26(String orgNode127_26) {
        this.orgNode127_26 = orgNode127_26;
    }

    /**
     * Returns the cardVerResult127_27 value
     *
     * @return the cardVerResult127_27
     */
    public String getCardVerResult127_27() {
        return cardVerResult127_27;
    }

    /**
     * Set the cardVerResult127_27 value
     *
     * @param cardVerResult127_27
     *          the cardVerResult127_27 to set
     */
    public void setCardVerResult127_27(String cardVerResult127_27) {
        this.cardVerResult127_27 = cardVerResult127_27;
    }

    /**
     * Returns the americanExpCardIdentifier127_28 value
     *
     * @return the americanExpCardIdentifier127_28
     */
    public String getAmericanExpCardIdentifier127_28() {
        return americanExpCardIdentifier127_28;
    }

    /**
     * Set the americanExpCardIdentifier127_28 value
     *
     * @param americanExpCardIdentifier127_28
     *          the americanExpCardIdentifier127_28 to set
     */
    public void setAmericanExpCardIdentifier127_28(String americanExpCardIdentifier127_28) {
        this.americanExpCardIdentifier127_28 = americanExpCardIdentifier127_28;
    }

    /**
     * Returns the secureData127_29 value
     *
     * @return the secureData127_29
     */
    public String getSecureData127_29() {
        return secureData127_29;
    }

    /**
     * Set the secureData127_29 value
     *
     * @param secureData127_29
     *          the secureData127_29 to set
     */
    public void setSecureData127_29(String secureData127_29) {
        this.secureData127_29 = secureData127_29;
    }

    /**
     * Returns the secureResult127_30 value
     *
     * @return the secureResult127_30
     */
    public String getSecureResult127_30() {
        return secureResult127_30;
    }

    /**
     * Set the secureResult127_30 value
     *
     * @param secureResult127_30
     *          the secureResult127_30 to set
     */
    public void setSecureResult127_30(String secureResult127_30) {
        this.secureResult127_30 = secureResult127_30;
    }

    /**
     * Returns the issuerNetId127_31 value
     *
     * @return the issuerNetId127_31
     */
    public String getIssuerNetId127_31() {
        return issuerNetId127_31;
    }

    /**
     * Set the issuerNetId127_31 value
     *
     * @param issuerNetId127_31
     *          the issuerNetId127_31 to set
     */
    public void setIssuerNetId127_31(String issuerNetId127_31) {
        this.issuerNetId127_31 = issuerNetId127_31;
    }

    /**
     * Returns the ucafData127_32 value
     *
     * @return the ucafData127_32
     */
    public String getUcafData127_32() {
        return ucafData127_32;
    }

    /**
     * Set the ucafData127_32 value
     *
     * @param ucafData127_32
     *          the ucafData127_32 to set
     */
    public void setUcafData127_32(String ucafData127_32) {
        this.ucafData127_32 = ucafData127_32;
    }

    /**
     * Returns the extentedTransType127_33 value
     *
     * @return the extentedTransType127_33
     */
    public String getExtentedTransType127_33() {
        return extentedTransType127_33;
    }

    /**
     * Set the extentedTransType127_33 value
     *
     * @param extentedTransType127_33
     *          the extentedTransType127_33 to set
     */
    public void setExtentedTransType127_33(String extentedTransType127_33) {
        this.extentedTransType127_33 = extentedTransType127_33;
    }

    /**
     * Returns the accTypeQualifiers127_34 value
     *
     * @return the accTypeQualifiers127_34
     */
    public String getAccTypeQualifiers127_34() {
        return accTypeQualifiers127_34;
    }

    /**
     * Set the accTypeQualifiers127_34 value
     *
     * @param accTypeQualifiers127_34
     *          the accTypeQualifiers127_34 to set
     */
    public void setAccTypeQualifiers127_34(String accTypeQualifiers127_34) {
        this.accTypeQualifiers127_34 = accTypeQualifiers127_34;
    }

    /**
     * Returns the acquireNetId127_35 value
     *
     * @return the acquireNetId127_35
     */
    public String getAcquireNetId127_35() {
        return acquireNetId127_35;
    }

    /**
     * Set the acquireNetId127_35 value
     *
     * @param acquireNetId127_35
     *          the acquireNetId127_35 to set
     */
    public void setAcquireNetId127_35(String acquireNetId127_35) {
        this.acquireNetId127_35 = acquireNetId127_35;
    }

    /**
     * Returns the cusId127_36 value
     *
     * @return the cusId127_36
     */
    public String getCusId127_36() {
        return cusId127_36;
    }

    /**
     * Set the cusId127_36 value
     *
     * @param cusId127_36
     *          the cusId127_36 to set
     */
    public void setCusId127_36(String cusId127_36) {
        this.cusId127_36 = cusId127_36;
    }

    /**
     * Returns the extResCode127_37 value
     *
     * @return the extResCode127_37
     */
    public String getExtResCode127_37() {
        return extResCode127_37;
    }

    /**
     * Set the extResCode127_37 value
     *
     * @param extResCode127_37
     *          the extResCode127_37 to set
     */
    public void setExtResCode127_37(String extResCode127_37) {
        this.extResCode127_37 = extResCode127_37;
    }

    /**
     * Returns the addlPosDataCode127_38 value
     *
     * @return the addlPosDataCode127_38
     */
    public String getAddlPosDataCode127_38() {
        return addlPosDataCode127_38;
    }

    /**
     * Set the addlPosDataCode127_38 value
     *
     * @param addlPosDataCode127_38
     *          the addlPosDataCode127_38 to set
     */
    public void setAddlPosDataCode127_38(String addlPosDataCode127_38) {
        this.addlPosDataCode127_38 = addlPosDataCode127_38;
    }

    /**
     * Returns the orgResCode127_39 value
     *
     * @return the orgResCode127_39
     */
    public String getOrgResCode127_39() {
        return orgResCode127_39;
    }

    /**
     * Set the orgResCode127_39 value
     *
     * @param orgResCode127_39
     *          the orgResCode127_39 to set
     */
    public void setOrgResCode127_39(String orgResCode127_39) {
        this.orgResCode127_39 = orgResCode127_39;
    }

    /**
     * Returns the macExt128 value
     *
     * @return the macExt128
     */
    public byte[] getMacExt128() {
        return macExt128;
    }

    /**
     * Set the macExt128 value
     *
     * @param macExt128
     *          the macExt128 to set
     */
    public void setMacExt128(byte[] macExt128) {
        this.macExt128 = macExt128;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */

    String tsi;

    public String getTsi() {
        return tsi;
    }

    public void setTsi(String tsi) {
        this.tsi = tsi;
    }

    @Override
    public String toString() {
        return "MadaRequest{" +
                "cardIndicator='" + cardIndicator + '\'' +
                ", setKernalID='" + setKernalID + '\'' +
                ", nameTransactionTag='" + nameTransactionTag + '\'' +
                ", modeTransaction='" + modeTransaction + '\'' +
                ", reservedData62Responce='" + reservedData62Responce + '\'' +
                ", mti0='" + mti0 + '\'' +
                ", primaryAccNo2='" + primaryAccNo2 + '\'' +
                ", processingCode3='" + processingCode3 + '\'' +
                ", amtTransaction4='" + amtTransaction4 + '\'' +
                ", amtSattlement5='" + amtSattlement5 + '\'' +
                ", amtCardholderbilling6='" + amtCardholderbilling6 + '\'' +
                ", transmissionDateTime7='" + transmissionDateTime7 + '\'' +
                ", amtCardholderbillingfee8='" + amtCardholderbillingfee8 + '\'' +
                ", conversationRateSattlement9='" + conversationRateSattlement9 + '\'' +
                ", conversationRateCardholderbilling10='" + conversationRateCardholderbilling10 + '\'' +
                ", systemTraceAuditnumber11='" + systemTraceAuditnumber11 + '\'' +
                ", timeLocalTransaction12='" + timeLocalTransaction12 + '\'' +
                ", dateLocalTransaction13='" + dateLocalTransaction13 + '\'' +
                ", dateExpiration14='" + dateExpiration14 + '\'' +
                ", dateSattlement15='" + dateSattlement15 + '\'' +
                ", dateConversion16='" + dateConversion16 + '\'' +
                ", dateCapture17='" + dateCapture17 + '\'' +
                ", merchantType18='" + merchantType18 + '\'' +
                ", countryCodeAcquiringinstitution19='" + countryCodeAcquiringinstitution19 + '\'' +
                ", countryCodePrimaryaccountno20='" + countryCodePrimaryaccountno20 + '\'' +
                ", countryCodeForwardinginstitution21='" + countryCodeForwardinginstitution21 + '\'' +
                ", posEntrymode22='" + posEntrymode22 + '\'' +
                ", cardSequenceNumber23='" + cardSequenceNumber23 + '\'' +
                ", functioncode24='" + functioncode24 + '\'' +
                ", messageReasonCode25='" + messageReasonCode25 + '\'' +
                ", cardAcceptorBusinessCode26='" + cardAcceptorBusinessCode26 + '\'' +
                ", autidResLength27='" + autidResLength27 + '\'' +
                ", reconsilationDate28='" + reconsilationDate28 + '\'' +
                ", amtSattlementFee29='" + amtSattlementFee29 + '\'' +
                ", amtTranProcessingFee30='" + amtTranProcessingFee30 + '\'' +
                ", amtSattleProcessingFee31='" + amtSattleProcessingFee31 + '\'' +
                ", accuringInsituteIdCode32='" + accuringInsituteIdCode32 + '\'' +
                ", forwardInsituteIdCode33='" + forwardInsituteIdCode33 + '\'' +
                ", primaryAccountNumberExtended34='" + primaryAccountNumberExtended34 + '\'' +
                ", track2Data35='" + track2Data35 + '\'' +
                ", track3Data36='" + track3Data36 + '\'' +
                ", retriRefNo37='" + retriRefNo37 + '\'' +
                ", authIdResCode38='" + authIdResCode38 + '\'' +
                ", responseCode39='" + responseCode39 + '\'' +
                ", serviceRestricCode40='" + serviceRestricCode40 + '\'' +
                ", cardAcceptorTemId41='" + cardAcceptorTemId41 + '\'' +
                ", cardAcceptorIdCode42='" + cardAcceptorIdCode42 + '\'' +
                ", cardAcceptorNameLocation43='" + cardAcceptorNameLocation43 + '\'' +
                ", additionalResData44='" + additionalResData44 + '\'' +
                ", trackOneData45='" + trackOneData45 + '\'' +
                ", amountsFees46='" + amountsFees46 + '\'' +
                ", additionalDataNational47='" + additionalDataNational47 + '\'' +
                ", additionalDataPrivate48='" + additionalDataPrivate48 + '\'' +
                ", currCodeTransaction49='" + currCodeTransaction49 + '\'' +
                ", currCodeStatleMent50='" + currCodeStatleMent50 + '\'' +
                ", currencyCodeCardholderbilling51='" + currencyCodeCardholderbilling51 + '\'' +
                ", pinData52=" + Arrays.toString(pinData52) +
                ", secRelatedContInfo53='" + secRelatedContInfo53 + '\'' +
                ", addlAmt54='" + addlAmt54 + '\'' +
                ", iccCardSystemRelatedData55='" + iccCardSystemRelatedData55 + '\'' +
                ", msgReasonCode56='" + msgReasonCode56 + '\'' +
                ", authLifeCode57='" + authLifeCode57 + '\'' +
                ", authAgentIdCode58='" + authAgentIdCode58 + '\'' +
                ", echoData59='" + echoData59 + '\'' +
                ", reservedData60='" + reservedData60 + '\'' +
                ", reservedData61='" + reservedData61 + '\'' +
                ", reservedData62='" + reservedData62 + '\'' +
                ", reservedData63='" + reservedData63 + '\'' +
                ", messageAuthenticationCodeField64=" + Arrays.toString(messageAuthenticationCodeField64) +
                ", reservedData65='" + reservedData65 + '\'' +
                ", statleMentCode66='" + statleMentCode66 + '\'' +
                ", extPaymentCode67='" + extPaymentCode67 + '\'' +
                ", countryCodeReceivingInstitution68='" + countryCodeReceivingInstitution68 + '\'' +
                ", countryCodeSettlementInstitution69='" + countryCodeSettlementInstitution69 + '\'' +
                ", netMgnInfoCode70='" + netMgnInfoCode70 + '\'' +
                ", messageNumber71='" + messageNumber71 + '\'' +
                ", dataRecord72='" + dataRecord72 + '\'' +
                ", dateAction73='" + dateAction73 + '\'' +
                ", creditNo74='" + creditNo74 + '\'' +
                ", creditRevNo75='" + creditRevNo75 + '\'' +
                ", debitNo76='" + debitNo76 + '\'' +
                ", debitRevNo77='" + debitRevNo77 + '\'' +
                ", transperNo78='" + transperNo78 + '\'' +
                ", transperRevNo79='" + transperRevNo79 + '\'' +
                ", inquiriesNo80='" + inquiriesNo80 + '\'' +
                ", authNo81='" + authNo81 + '\'' +
                ", creditProcessFeeAmt82='" + creditProcessFeeAmt82 + '\'' +
                ", creditTransFeeAmt83='" + creditTransFeeAmt83 + '\'' +
                ", debitProcessFeeAmt84='" + debitProcessFeeAmt84 + '\'' +
                ", debitTransFeeAmt85='" + debitTransFeeAmt85 + '\'' +
                ", creditAmt86='" + creditAmt86 + '\'' +
                ", creditRevAmt87='" + creditRevAmt87 + '\'' +
                ", debitAmt88='" + debitAmt88 + '\'' +
                ", debitRevAmt89='" + debitRevAmt89 + '\'' +
                ", orgDataElement90='" + orgDataElement90 + '\'' +
                ", fileUpdateCode91='" + fileUpdateCode91 + '\'' +
                ", countryCodeTransactionOrig92='" + countryCodeTransactionOrig92 + '\'' +
                ", transactionDest93='" + transactionDest93 + '\'' +
                ", transactionOrig94='" + transactionOrig94 + '\'' +
                ", replaceAmt95='" + replaceAmt95 + '\'' +
                ", keyManagementData96='" + keyManagementData96 + '\'' +
                ", amtNetSattle97='" + amtNetSattle97 + '\'' +
                ", payee98='" + payee98 + '\'' +
                ", settlementInstitutionIdCode99='" + settlementInstitutionIdCode99 + '\'' +
                ", receiveInsituteIdCode100='" + receiveInsituteIdCode100 + '\'' +
                ", fileName101='" + fileName101 + '\'' +
                ", accIdentOne102='" + accIdentOne102 + '\'' +
                ", accIdentTwo103='" + accIdentTwo103 + '\'' +
                ", reservedData104='" + reservedData104 + '\'' +
                ", creditsChargebackAmount105='" + creditsChargebackAmount105 + '\'' +
                ", debitsChargebackAmount106='" + debitsChargebackAmount106 + '\'' +
                ", creditsChargebackNumber107='" + creditsChargebackNumber107 + '\'' +
                ", debitsChargebackNumber108='" + debitsChargebackNumber108 + '\'' +
                ", creditsFeeAmounts109='" + creditsFeeAmounts109 + '\'' +
                ", debitsFeAmounts110='" + debitsFeAmounts110 + '\'' +
                ", reservedData111='" + reservedData111 + '\'' +
                ", reservedData112='" + reservedData112 + '\'' +
                ", reservedData113='" + reservedData113 + '\'' +
                ", reservedData114='" + reservedData114 + '\'' +
                ", reservedData115='" + reservedData115 + '\'' +
                ", reservedData116='" + reservedData116 + '\'' +
                ", reservedData117='" + reservedData117 + '\'' +
                ", paymentNo118='" + paymentNo118 + '\'' +
                ", paymentRevNo119='" + paymentRevNo119 + '\'' +
                ", reservedData120='" + reservedData120 + '\'' +
                ", reservedData121='" + reservedData121 + '\'' +
                ", reservedData122='" + reservedData122 + '\'' +
                ", posDataCode123='" + posDataCode123 + '\'' +
                ", reservedData124='" + reservedData124 + '\'' +
                ", netMgnInfo125='" + netMgnInfo125 + '\'' +
                ", reservedData126='" + reservedData126 + '\'' +
                ", switchKey127_2='" + switchKey127_2 + '\'' +
                ", routeInfo127_3='" + routeInfo127_3 + '\'' +
                ", posData127_4='" + posData127_4 + '\'' +
                ", serStationData127_5='" + serStationData127_5 + '\'' +
                ", authProfile127_6='" + authProfile127_6 + '\'' +
                ", checkData127_7='" + checkData127_7 + '\'' +
                ", retentionData127_8='" + retentionData127_8 + '\'' +
                ", addlNodeData127_9='" + addlNodeData127_9 + '\'' +
                ", cvv2127_10='" + cvv2127_10 + '\'' +
                ", orgKey127_11='" + orgKey127_11 + '\'' +
                ", termOwner127_12='" + termOwner127_12 + '\'' +
                ", posGeograpicData127_13='" + posGeograpicData127_13 + '\'' +
                ", sponsorBank127_14='" + sponsorBank127_14 + '\'' +
                ", addressVerificData127_15='" + addressVerificData127_15 + '\'' +
                ", addressVerificResult127_16='" + addressVerificResult127_16 + '\'' +
                ", cardHolderInfo127_17='" + cardHolderInfo127_17 + '\'' +
                ", validationData127_18='" + validationData127_18 + '\'' +
                ", bankDetail127_19='" + bankDetail127_19 + '\'' +
                ", origOrAuthorizerDateStattleMent127_20='" + origOrAuthorizerDateStattleMent127_20 + '\'' +
                ", recordIdentification127_21='" + recordIdentification127_21 + '\'' +
                ", srtucreData127_22='" + srtucreData127_22 + '\'' +
                ", payeeNameAddress127_23='" + payeeNameAddress127_23 + '\'' +
                ", payerAcc127_24='" + payerAcc127_24 + '\'' +
                ", iccData127_25='" + iccData127_25 + '\'' +
                ", amtAuthData127_25_2='" + amtAuthData127_25_2 + '\'' +
                ", amtOtherData127_25_3='" + amtOtherData127_25_3 + '\'' +
                ", appIdentifierData127_25_4='" + appIdentifierData127_25_4 + '\'' +
                ", appInterChangePrifileData127_25_5='" + appInterChangePrifileData127_25_5 + '\'' +
                ", appTransCounterData127_25_6='" + appTransCounterData127_25_6 + '\'' +
                ", appUsageData127_25_7='" + appUsageData127_25_7 + '\'' +
                ", authorizationResponseCode127_25_8='" + authorizationResponseCode127_25_8 + '\'' +
                ", cardAuthenticationReliabilityIndicator127_25_9='" + cardAuthenticationReliabilityIndicator127_25_9 + '\'' +
                ", cardAuthenticationResultsCode127_25_10='" + cardAuthenticationResultsCode127_25_10 + '\'' +
                ", chipConditionCode127_25_11='" + chipConditionCode127_25_11 + '\'' +
                ", appCryptogramData127_25_12='" + appCryptogramData127_25_12 + '\'' +
                ", appCryptoGramInfoData127_25_13='" + appCryptoGramInfoData127_25_13 + '\'' +
                ", cvmList127_25_14='" + cvmList127_25_14 + '\'' +
                ", cvmResultData127_25_15='" + cvmResultData127_25_15 + '\'' +
                ", interfaceDeviceSerialNumber127_25_16='" + interfaceDeviceSerialNumber127_25_16 + '\'' +
                ", issuerAccCodeData127_25_17='" + issuerAccCodeData127_25_17 + '\'' +
                ", issueAppData127_25_18='" + issueAppData127_25_18 + '\'' +
                ", issuerScriptResults127_25_19='" + issuerScriptResults127_25_19 + '\'' +
                ", terminalApplicationVersionNumber127_25_20='" + terminalApplicationVersionNumber127_25_20 + '\'' +
                ", termCapableData127_25_21='" + termCapableData127_25_21 + '\'' +
                ", termCounCodeData127_25_22='" + termCounCodeData127_25_22 + '\'' +
                ", termTypeData127_25_23='" + termTypeData127_25_23 + '\'' +
                ", termVerResData127_25_24='" + termVerResData127_25_24 + '\'' +
                ", transactionCategoryCode127_25_25='" + transactionCategoryCode127_25_25 + '\'' +
                ", tranCurrCodeData127_25_26='" + tranCurrCodeData127_25_26 + '\'' +
                ", tranDateData127_25_27='" + tranDateData127_25_27 + '\'' +
                ", transactionSequenceCounter127_25_28='" + transactionSequenceCounter127_25_28 + '\'' +
                ", tranTypeData127_25_29='" + tranTypeData127_25_29 + '\'' +
                ", unpretictableData127_25_30='" + unpretictableData127_25_30 + '\'' +
                ", issuerAuthenticationData127_25_31='" + issuerAuthenticationData127_25_31 + '\'' +
                ", issuerScriptTemplateOne127_25_32='" + issuerScriptTemplateOne127_25_32 + '\'' +
                ", issuerScriptTemplateTwo127_25_33='" + issuerScriptTemplateTwo127_25_33 + '\'' +
                ", orgNode127_26='" + orgNode127_26 + '\'' +
                ", cardVerResult127_27='" + cardVerResult127_27 + '\'' +
                ", americanExpCardIdentifier127_28='" + americanExpCardIdentifier127_28 + '\'' +
                ", secureData127_29='" + secureData127_29 + '\'' +
                ", secureResult127_30='" + secureResult127_30 + '\'' +
                ", issuerNetId127_31='" + issuerNetId127_31 + '\'' +
                ", ucafData127_32='" + ucafData127_32 + '\'' +
                ", extentedTransType127_33='" + extentedTransType127_33 + '\'' +
                ", accTypeQualifiers127_34='" + accTypeQualifiers127_34 + '\'' +
                ", acquireNetId127_35='" + acquireNetId127_35 + '\'' +
                ", cusId127_36='" + cusId127_36 + '\'' +
                ", extResCode127_37='" + extResCode127_37 + '\'' +
                ", addlPosDataCode127_38='" + addlPosDataCode127_38 + '\'' +
                ", orgResCode127_39='" + orgResCode127_39 + '\'' +
                ", macExt128=" + Arrays.toString(macExt128) +
                ", tsi='" + tsi + '\'' +
                '}';
    }
}
