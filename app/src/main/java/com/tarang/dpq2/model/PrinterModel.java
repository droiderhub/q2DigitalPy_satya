package com.tarang.dpq2.model;

public class PrinterModel {
    private String retailerNameEnglish;
    private String retailerNameArabic;
    private String terminalStreetEnglish;
    private String terminalStreetArabic;
    private String terminaCityEnglish;
    private String terminalCityArabic;
    private String retailerTelephone;
    private String startDate;
    private String startTime = "";
    private String bId;
    private String mId;
    private String tId;
    private String mcc;
    private String stan;
    private String posSoftwareVersionNumber;
    private String rrn;
    //    private String bankLine;
//    private String storeAndCashierInfoEnglish;
//    private String storeAndCashierInfoArabic;
//    private String productsInfoEnglish;
//    private String productsInfoArabic;
    private String applicationLabelEnglish;
    private String applicationLabelArabic;
    private String transactionTypeEnglish;
    private String transactionTypeArabic;
    private String pan;
    private String cardExpry;
    private String totalAmountEnglish;
    private String totalAmountArabic;
    private String purchaseWithCashBackAmountEnglish;
    private String purchaseWithCashBackAmountArabic;
    private String purchaseAmountEnglish;
    private String purchaseAmountArabic;
    private String purchaseWithCashBackAmountStringEnglish;
    private String purchaseWithCashBackAmountStringArabic;
    private String purchaseAmountStringEnglish;
    private String purchaseAmountStringArabic;
    private String amountEnglish;
    private String amountArabic;
    private String transactionOutcomeEnglish;
    private String transactionOutcomeArabic;
    private String cardHolderVerificationOrReasonForDeclineEnglish;
    private String cardHolderVerificationOrReasonForDeclineArabic;
    //    private String signatureLineEnglish;
//    private String signatureLineArabic;
//    private String creditOrDebitAcknowledgementConfirmationEnglish;
//    private String creditOrDebitAcknowledgementConfirmationArabic;
    private String approvalCodeStringEnglish;
    private String approvalCodeStringArabic;
    private String approvalCodeEnglish;
    private String approvalCodeArabic;
    private String endDate;
    private String endTime;
    private String thankYouEnglish;
    private String thankYouArabic;
    //    private String recordOfTransctionEnglish;
//    private String recordOfTransctionArabic;
    private String receiptVersionEnglish;
    private String receiptVersionArabic;
    private String posEntryMode = "";
    private String alpharesponseCode = "";
    private String aid = "";
    private String tvr = "";
    private String tsi = "";
    private String cvr = "";
    private String applicationCryptogramInfo = "";
    private String applicationCryptogram = "";
    private String kernalId = "";
    private String data44 = "";
    //    private String quickResponseCode;
//    private String seperator;
//    private String multipleLinesOfTextEnglish;
//    private String multipleLinesOfTextArabic;
    private String pleaseRetainYourReceiptArabic;
    private String pleaseRetainYourReceiptEnglish;
    //    private String thankYouUsingMadaArabic;
//    private String thankYouUsingMadaEnglish;
    private String signBelowEnglish;
    private String signBelowArabic;
    private String underline;
    private String accountForTheAmountEnglish;
    private String accountForTheAmountArabic;
    private String qrCodeData;

    public String getData44() {
        String localData44 = "";
        if (data44 != null && data44.trim().length() != 0) {
            try {
                String tag1 = data44.substring(0, 2);
                if (tag1.equalsIgnoreCase("01")) {
                    localData44 = data44.substring(2, 31);
                    localData44 = localData44 + " " + data44.substring(33);
                } else if (tag1.equalsIgnoreCase("02")) {
                    localData44 = data44.substring(8);
                    localData44 = localData44 + " " + data44.substring(2, 6);
                }
            } catch (IndexOutOfBoundsException e) {
                return localData44;
            }
        }

        return localData44;
    }

    public void setData44(String data44) {
        this.data44 = data44;
    }

    public String getPurchaseWithCashBackAmountEnglish() {
        return purchaseWithCashBackAmountEnglish;
    }

    public void setPurchaseWithCashBackAmountEnglish(String purchaseWithCashBackAmountEnglish) {
        this.purchaseWithCashBackAmountEnglish = purchaseWithCashBackAmountEnglish;
    }

    public String getPurchaseWithCashBackAmountArabic() {
        return purchaseWithCashBackAmountArabic;
    }

    public void setPurchaseWithCashBackAmountArabic(String purchaseWithCashBackAmountArabic) {
        this.purchaseWithCashBackAmountArabic = purchaseWithCashBackAmountArabic;
    }

    public String getPurchaseAmountEnglish() {
        return purchaseAmountEnglish;
    }

    public void setPurchaseAmountEnglish(String purchaseAmountEnglish) {
        this.purchaseAmountEnglish = purchaseAmountEnglish;
    }

    public String getPurchaseAmountArabic() {
        return purchaseAmountArabic;
    }

    public void setPurchaseAmountArabic(String purchaseAmountArabic) {
        this.purchaseAmountArabic = purchaseAmountArabic;
    }

    public String getPurchaseWithCashBackAmountStringEnglish() {
        return purchaseWithCashBackAmountStringEnglish;
    }

    public void setPurchaseWithCashBackAmountStringEnglish(String purchaseWithCashBackAmountStringEnglish) {
        this.purchaseWithCashBackAmountStringEnglish = purchaseWithCashBackAmountStringEnglish;
    }

    public String getPurchaseWithCashBackAmountStringArabic() {
        return purchaseWithCashBackAmountStringArabic;
    }

    public void setPurchaseWithCashBackAmountStringArabic(String purchaseWithCashBackAmountStringArabic) {
        this.purchaseWithCashBackAmountStringArabic = purchaseWithCashBackAmountStringArabic;
    }

    public String getPurchaseAmountStringEnglish() {
        return purchaseAmountStringEnglish;
    }

    public void setPurchaseAmountStringEnglish(String purchaseAmountStringEnglish) {
        this.purchaseAmountStringEnglish = purchaseAmountStringEnglish;
    }

    public String getPurchaseAmountStringArabic() {
        return purchaseAmountStringArabic;
    }

    public void setPurchaseAmountStringArabic(String purchaseAmountStringArabic) {
        this.purchaseAmountStringArabic = purchaseAmountStringArabic;
    }

    public String getQrCodeData() {
        return qrCodeData;
    }

    public void setQrCodeData(String qrCodeData) {
        this.qrCodeData = qrCodeData;
    }

    public String getSignBelowEnglish() {
        return signBelowEnglish;
    }

    public void setSignBelowEnglish(String signBelowEnglish) {
        this.signBelowEnglish = signBelowEnglish;
    }

    public String getSignBelowArabic() {
        return signBelowArabic;
    }

    public void setSignBelowArabic(String signBelowArabic) {
        this.signBelowArabic = signBelowArabic;
    }

    public String getUnderline() {
        return underline;
    }

    public void setUnderline(String underline) {
        this.underline = underline;
    }

    public String getAccountForTheAmountEnglish() {
        return accountForTheAmountEnglish;
    }

    public void setAccountForTheAmountEnglish(String accountForTheAmountEnglish) {
        this.accountForTheAmountEnglish = accountForTheAmountEnglish;
    }

    public String getAccountForTheAmountArabic() {
        return accountForTheAmountArabic;
    }

    public void setAccountForTheAmountArabic(String accountForTheAmountArabic) {
        this.accountForTheAmountArabic = accountForTheAmountArabic;
    }

    public String getApprovalCodeStringEnglish() {
        return approvalCodeStringEnglish;
    }

    public void setApprovalCodeStringEnglish(String approvalCodeStringEnglish) {
        this.approvalCodeStringEnglish = approvalCodeStringEnglish;
    }

    public String getApprovalCodeStringArabic() {
        return approvalCodeStringArabic;
    }

    public void setApprovalCodeStringArabic(String approvalCodeStringArabic) {
        this.approvalCodeStringArabic = approvalCodeStringArabic;
    }

    public String getPleaseRetainYourReceiptArabic() {
        return pleaseRetainYourReceiptArabic;
    }

    public void setPleaseRetainYourReceiptArabic(String pleaseRetainYourReceiptArabic) {
        this.pleaseRetainYourReceiptArabic = pleaseRetainYourReceiptArabic;
    }

    public String getPleaseRetainYourReceiptEnglish() {
        return pleaseRetainYourReceiptEnglish;
    }

    public void setPleaseRetainYourReceiptEnglish(String pleaseRetainYourReceiptEnglish) {
        this.pleaseRetainYourReceiptEnglish = pleaseRetainYourReceiptEnglish;
    }

//    public String getThankYouUsingMadaArabic() {
//        return thankYouUsingMadaArabic;
//    }
//
//    public void setThankYouUsingMadaArabic(String thankYouUsingMadaArabic) {
//        this.thankYouUsingMadaArabic = thankYouUsingMadaArabic;
//    }
//
//    public String getThankYouUsingMadaEnglish() {
//        return thankYouUsingMadaEnglish;
//    }
//
//    public void setThankYouUsingMadaEnglish(String thankYouUsingMadaEnglish) {
//        this.thankYouUsingMadaEnglish = thankYouUsingMadaEnglish;
//    }

    public String getRetailerNameEnglish() {
        return retailerNameEnglish;
    }

    public void setRetailerNameEnglish(String retailerNameEnglish) {
        this.retailerNameEnglish = retailerNameEnglish;
    }

    public String getRetailerNameArabic() {
        return retailerNameArabic;
    }

    public void setRetailerNameArabic(String retailerNameArabic) {
        this.retailerNameArabic = retailerNameArabic;
    }

    public String getTerminalStreetEnglish() {
        return terminalStreetEnglish;
    }

    public void setTerminalStreetEnglish(String terminalStreetEnglish) {
        this.terminalStreetEnglish = terminalStreetEnglish;
    }

    public String getTerminalStreetArabic() {
        return terminalStreetArabic;
    }

    public void setTerminalStreetArabic(String terminalStreetArabic) {
        this.terminalStreetArabic = terminalStreetArabic;
    }

    public String getTerminaCityEnglish() {
        return terminaCityEnglish;
    }

    public void setTerminaCityEnglish(String terminaCityEnglish) {
        this.terminaCityEnglish = terminaCityEnglish;
    }

    public String getTerminalCityArabic() {
        return terminalCityArabic;
    }

    public void setTerminalCityArabic(String terminalCityArabic) {
        this.terminalCityArabic = terminalCityArabic;
    }

    public String getRetailerTelephone() {
        return retailerTelephone;
    }

    public void setRetailerTelephone(String retailerTelephone) {
        this.retailerTelephone = retailerTelephone;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String gettId() {
        return tId;
    }

    public void settId(String tId) {
        this.tId = tId;
    }

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public String getStan() {
        return stan;
    }

    public void setStan(String stan) {
        this.stan = stan;
    }

    public String getPosSoftwareVersionNumber() {
        return posSoftwareVersionNumber;
    }

    public void setPosSoftwareVersionNumber(String posSoftwareVersionNumber) {
        this.posSoftwareVersionNumber = posSoftwareVersionNumber;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getKernalId() {
        if (kernalId != null && kernalId.trim().length() != 0)
            return kernalId.toUpperCase();
        return "";
    }

    public void setKernalId(String kernalId) {
        this.kernalId = kernalId;
    }

    //    public String getBankLine() {
//        return bankLine;
//    }
//
//    public void setBankLine(String bankLine) {
//        this.bankLine = bankLine;
//    }

//    public String getStoreAndCashierInfoEnglish() {
//        return storeAndCashierInfoEnglish;
//    }
//
//    public void setStoreAndCashierInfoEnglish(String storeAndCashierInfoEnglish) {
//        this.storeAndCashierInfoEnglish = storeAndCashierInfoEnglish;
//    }
//
//    public String getStoreAndCashierInfoArabic() {
//        return storeAndCashierInfoArabic;
//    }
//
//    public void setStoreAndCashierInfoArabic(String storeAndCashierInfoArabic) {
//        this.storeAndCashierInfoArabic = storeAndCashierInfoArabic;
//    }
//
//    public String getProductsInfoEnglish() {
//        return productsInfoEnglish;
//    }
//
//    public void setProductsInfoEnglish(String productsInfoEnglish) {
//        this.productsInfoEnglish = productsInfoEnglish;
//    }
//
//    public String getProductsInfoArabic() {
//        return productsInfoArabic;
//    }
//
//    public void setProductsInfoArabic(String productsInfoArabic) {
//        this.productsInfoArabic = productsInfoArabic;
//    }

    public String getApplicationLabelEnglish() {
        return applicationLabelEnglish;
    }

    public void setApplicationLabelEnglish(String applicationLabelEnglish) {
        this.applicationLabelEnglish = applicationLabelEnglish;
    }

    public String getApplicationLabelArabic() {
        return applicationLabelArabic;
    }

    public void setApplicationLabelArabic(String applicationLabelArabic) {
        this.applicationLabelArabic = applicationLabelArabic;
    }

    public String getTransactionTypeEnglish() {
        return transactionTypeEnglish;
    }

    public void setTransactionTypeEnglish(String transactionTypeEnglish) {
        this.transactionTypeEnglish = transactionTypeEnglish;
    }

    public String getTransactionTypeArabic() {
        return transactionTypeArabic;
    }

    public void setTransactionTypeArabic(String transactionTypeArabic) {
        this.transactionTypeArabic = transactionTypeArabic;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getCardExpry() {
        return cardExpry;
    }

    public void setCardExpry(String cardExpry) {
        this.cardExpry = cardExpry;
    }

    public String getTotalAmountEnglish() {
        return totalAmountEnglish;
    }

    public void setTotalAmountEnglish(String totalAmountEnglish) {
        this.totalAmountEnglish = totalAmountEnglish;
    }

    public String getTotalAmountArabic() {
        return totalAmountArabic;
    }

    public void setTotalAmountArabic(String totalAmountArabic) {
        this.totalAmountArabic = totalAmountArabic;
    }

//    public String getCashbackAmountEnglish() {
//        return cashbackAmountEnglish;
//    }
//
//    public void setCashbackAmountEnglish(String cashbackAmountEnglish) {
//        this.cashbackAmountEnglish = cashbackAmountEnglish;
//    }
//
//    public String getCashbackAmountArabic() {
//        return cashbackAmountArabic;
//    }
//
//    public void setCashbackAmountArabic(String cashbackAmountArabic) {
//        this.cashbackAmountArabic = cashbackAmountArabic;
//    }

    public String getAmountEnglish() {
        return amountEnglish;
    }

    public void setAmountEnglish(String amountEnglish) {
        this.amountEnglish = amountEnglish;
    }

    public String getAmountArabic() {
        return amountArabic;
    }

    public void setAmountArabic(String amountArabic) {
        this.amountArabic = amountArabic;
    }

    public String getTransactionOutcomeEnglish() {
        return transactionOutcomeEnglish;
    }

    public void setTransactionOutcomeEnglish(String transactionOutcomeEnglish) {
        this.transactionOutcomeEnglish = transactionOutcomeEnglish;
    }

    public String getTransactionOutcomeArabic() {
        return transactionOutcomeArabic;
    }

    public void setTransactionOutcomeArabic(String transactionOutcomeArabic) {
        this.transactionOutcomeArabic = transactionOutcomeArabic;
    }

    public String getCardHolderVerificationOrReasonForDeclineEnglish() {
        return splitSenteneIfLong(cardHolderVerificationOrReasonForDeclineEnglish);
    }

    private String splitSenteneIfLong(String word) {
        if(word == null)
            return null;
        if (word.trim().length() < 30)
            return word;
        String[] individual = word.split(" ");
        if (individual.length == 0)
            return word;
        String newWord = "";
        String secondWord = "";
        for (int i = 0; i < individual.length; i++) {
            String tempWord = newWord + individual[i] + " ";
            if (tempWord.trim().length() < 30) {
                newWord = tempWord;
            } else {
                secondWord = secondWord + individual[i] + " ";
            }
        }
        if (secondWord.trim().length() != 0) {
            return newWord + "---" + secondWord;
        } else
            return newWord;
    }

    public void setCardHolderVerificationOrReasonForDeclineEnglish(String cardHolderVerificationOrReasonForDeclineEnglish) {
        this.cardHolderVerificationOrReasonForDeclineEnglish = cardHolderVerificationOrReasonForDeclineEnglish;
    }

    public String getCardHolderVerificationOrReasonForDeclineArabic() {
        return cardHolderVerificationOrReasonForDeclineArabic;
    }

    public void setCardHolderVerificationOrReasonForDeclineArabic(String cardHolderVerificationOrReasonForDeclineArabic) {
        this.cardHolderVerificationOrReasonForDeclineArabic = cardHolderVerificationOrReasonForDeclineArabic;
    }

    //    public String getSignatureLineEnglish() {
//        return signatureLineEnglish;
//    }
//
//    public void setSignatureLineEnglish(String signatureLineEnglish) {
//        this.signatureLineEnglish = signatureLineEnglish;
//    }
//
//    public String getSignatureLineArabic() {
//        return signatureLineArabic;
//    }
//
//    public void setSignatureLineArabic(String signatureLineArabic) {
//        this.signatureLineArabic = signatureLineArabic;
//    }
//
//    public String getCreditOrDebitAcknowledgementConfirmationEnglish() {
//        return creditOrDebitAcknowledgementConfirmationEnglish;
//    }
//
//    public void setCreditOrDebitAcknowledgementConfirmationEnglish(String creditOrDebitAcknowledgementConfirmationEnglish) {
//        this.creditOrDebitAcknowledgementConfirmationEnglish = creditOrDebitAcknowledgementConfirmationEnglish;
//    }
//
//    public String getCreditOrDebitAcknowledgementConfirmationArabic() {
//        return creditOrDebitAcknowledgementConfirmationArabic;
//    }
//
//    public void setCreditOrDebitAcknowledgementConfirmationArabic(String creditOrDebitAcknowledgementConfirmationArabic) {
//        this.creditOrDebitAcknowledgementConfirmationArabic = creditOrDebitAcknowledgementConfirmationArabic;
//    }
//
    public String getApprovalCodeEnglish() {
        return approvalCodeEnglish;
    }

    public void setApprovalCodeEnglish(String approvalCodeEnglish) {
        this.approvalCodeEnglish = approvalCodeEnglish;
    }

    public String getApprovalCodeArabic() {
        return approvalCodeArabic;
    }

    public void setApprovalCodeArabic(String approvalCodeArabic) {
        this.approvalCodeArabic = approvalCodeArabic;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getThankYouEnglish() {
        return thankYouEnglish;
    }

    public void setThankYouEnglish(String thankYouEnglish) {
        this.thankYouEnglish = thankYouEnglish;
    }

    public String getThankYouArabic() {
        return thankYouArabic;
    }

    public void setThankYouArabic(String thankYouArabic) {
        this.thankYouArabic = thankYouArabic;
    }

//    public String getRecordOfTransctionEnglish() {
//        return recordOfTransctionEnglish;
//    }
//
//    public void setRecordOfTransctionEnglish(String recordOfTransctionEnglish) {
//        this.recordOfTransctionEnglish = recordOfTransctionEnglish;
//    }

//    public String getRecordOfTransctionArabic() {
//        return recordOfTransctionArabic;
//    }
//
//    public void setRecordOfTransctionArabic(String recordOfTransctionArabic) {
//        this.recordOfTransctionArabic = recordOfTransctionArabic;
//    }

    public String getReceiptVersionEnglish() {
        return receiptVersionEnglish;
    }

    public void setReceiptVersionEnglish(String receiptVersionEnglish) {
        this.receiptVersionEnglish = receiptVersionEnglish;
    }

    public String getReceiptVersionArabic() {
        return receiptVersionArabic;
    }

    public void setReceiptVersionArabic(String receiptVersionArabic) {
        this.receiptVersionArabic = receiptVersionArabic;
    }

    public String getPosEntryMode() {
        return posEntryMode;
    }

    public void setPosEntryMode(String posEntryMode) {
        this.posEntryMode = posEntryMode;
    }

    public String getAlpharesponseCode() {
        return alpharesponseCode;
    }

    public void setAlpharesponseCode(String alpharesponseCode) {
        this.alpharesponseCode = alpharesponseCode;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getTvr() {
        return tvr;
    }

    public void setTvr(String tvr) {
        this.tvr = tvr;
    }

    public String getTsi() {
        return (tsi != null)?tsi+" " :"";
    }

    public void setTsi(String tsi) {
        this.tsi = tsi;
    }

    public String getCvr() {
        return (cvr != null)?cvr+" ":"";
    }

    public void setCvr(String cvr) {
        this.cvr = cvr;
    }

    public String getApplicationCryptogramInfo() {
        return (applicationCryptogramInfo != null)?applicationCryptogramInfo+" ":"";
    }

    public void setApplicationCryptogramInfo(String applicationCryptogramInfo) {
        this.applicationCryptogramInfo = applicationCryptogramInfo;
    }

    public String getApplicationCryptogram() {
        if (applicationCryptogram != null && applicationCryptogram.trim().length() != 0)
            return applicationCryptogram +" ";
        return "";
    }

    public void setApplicationCryptogram(String applicationCryptogram) {
        this.applicationCryptogram = applicationCryptogram;
    }

//    public String getQuickResponseCode() {
//        return quickResponseCode;
//    }
//
//    public void setQuickResponseCode(String quickResponseCode) {
//        this.quickResponseCode = quickResponseCode;
//    }

//    public String getSeperator() {
//        return seperator;
//    }
//
//    public void setSeperator(String seperator) {
//        this.seperator = seperator;
//    }
//
//    public String getMultipleLinesOfTextEnglish() {
//        return multipleLinesOfTextEnglish;
//    }
//
//    public void setMultipleLinesOfTextEnglish(String multipleLinesOfTextEnglish) {
//        this.multipleLinesOfTextEnglish = multipleLinesOfTextEnglish;
//    }
//
//    public String getMultipleLinesOfTextArabic() {
//        return multipleLinesOfTextArabic;
//    }
//
//    public void setMultipleLinesOfTextArabic(String multipleLinesOfTextArabic) {
//        this.multipleLinesOfTextArabic = multipleLinesOfTextArabic;
//    }


    @Override
    public String toString() {
        return "PrinterModel{" +
                "retailerNameEnglish='" + retailerNameEnglish + '\'' +
                ", retailerNameArabic='" + retailerNameArabic + '\'' +
                ", terminalStreetEnglish='" + terminalStreetEnglish + '\'' +
                ", terminalStreetArabic='" + terminalStreetArabic + '\'' +
                ", terminaCityEnglish='" + terminaCityEnglish + '\'' +
                ", terminalCityArabic='" + terminalCityArabic + '\'' +
                ", retailerTelephone='" + retailerTelephone + '\'' +
                ", startDate='" + startDate + '\'' +
                ", startTime='" + startTime + '\'' +
                ", bId='" + bId + '\'' +
                ", mId='" + mId + '\'' +
                ", tId='" + tId + '\'' +
                ", mcc='" + mcc + '\'' +
                ", stan='" + stan + '\'' +
                ", posSoftwareVersionNumber='" + posSoftwareVersionNumber + '\'' +
                ", rrn='" + rrn + '\'' +
                ", applicationLabelEnglish='" + applicationLabelEnglish + '\'' +
                ", applicationLabelArabic='" + applicationLabelArabic + '\'' +
                ", transactionTypeEnglish='" + transactionTypeEnglish + '\'' +
                ", transactionTypeArabic='" + transactionTypeArabic + '\'' +
                ", pan='" + pan + '\'' +
                ", cardExpry='" + cardExpry + '\'' +
                ", totalAmountEnglish='" + totalAmountEnglish + '\'' +
                ", totalAmountArabic='" + totalAmountArabic + '\'' +
                ", purchaseWithCashBackAmountEnglish='" + purchaseWithCashBackAmountEnglish + '\'' +
                ", purchaseWithCashBackAmountArabic='" + purchaseWithCashBackAmountArabic + '\'' +
                ", purchaseAmountEnglish='" + purchaseAmountEnglish + '\'' +
                ", purchaseAmountArabic='" + purchaseAmountArabic + '\'' +
                ", purchaseWithCashBackAmountStringEnglish='" + purchaseWithCashBackAmountStringEnglish + '\'' +
                ", purchaseWithCashBackAmountStringArabic='" + purchaseWithCashBackAmountStringArabic + '\'' +
                ", purchaseAmountStringEnglish='" + purchaseAmountStringEnglish + '\'' +
                ", purchaseAmountStringArabic='" + purchaseAmountStringArabic + '\'' +
                ", amountEnglish='" + amountEnglish + '\'' +
                ", amountArabic='" + amountArabic + '\'' +
                ", transactionOutcomeEnglish='" + transactionOutcomeEnglish + '\'' +
                ", transactionOutcomeArabic='" + transactionOutcomeArabic + '\'' +
                ", cardHolderVerificationOrReasonForDeclineEnglish='" + cardHolderVerificationOrReasonForDeclineEnglish + '\'' +
                ", cardHolderVerificationOrReasonForDeclineArabic='" + cardHolderVerificationOrReasonForDeclineArabic + '\'' +
                ", approvalCodeStringEnglish='" + approvalCodeStringEnglish + '\'' +
                ", approvalCodeStringArabic='" + approvalCodeStringArabic + '\'' +
                ", approvalCodeEnglish='" + approvalCodeEnglish + '\'' +
                ", approvalCodeArabic='" + approvalCodeArabic + '\'' +
                ", endDate='" + endDate + '\'' +
                ", endTime='" + endTime + '\'' +
                ", thankYouEnglish='" + thankYouEnglish + '\'' +
                ", thankYouArabic='" + thankYouArabic + '\'' +
                ", receiptVersionEnglish='" + receiptVersionEnglish + '\'' +
                ", receiptVersionArabic='" + receiptVersionArabic + '\'' +
                ", posEntryMode='" + posEntryMode + '\'' +
                ", alpharesponseCode='" + alpharesponseCode + '\'' +
                ", aid='" + aid + '\'' +
                ", tvr='" + tvr + '\'' +
                ", tsi='" + tsi + '\'' +
                ", cvr='" + cvr + '\'' +
                ", applicationCryptogramInfo='" + applicationCryptogramInfo + '\'' +
                ", applicationCryptogram='" + applicationCryptogram + '\'' +
                ", kernalId='" + kernalId + '\'' +
                ", data44='" + data44 + '\'' +
                ", pleaseRetainYourReceiptArabic='" + pleaseRetainYourReceiptArabic + '\'' +
                ", pleaseRetainYourReceiptEnglish='" + pleaseRetainYourReceiptEnglish + '\'' +
                ", signBelowEnglish='" + signBelowEnglish + '\'' +
                ", signBelowArabic='" + signBelowArabic + '\'' +
                ", underline='" + underline + '\'' +
                ", accountForTheAmountEnglish='" + accountForTheAmountEnglish + '\'' +
                ", accountForTheAmountArabic='" + accountForTheAmountArabic + '\'' +
                ", qrCodeData='" + qrCodeData + '\'' +
                '}';
    }
}
