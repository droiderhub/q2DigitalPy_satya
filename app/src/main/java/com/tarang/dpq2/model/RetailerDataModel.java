package com.tarang.dpq2.model;

public class RetailerDataModel {
    String nextLoad = "";
    String reconcillationTime;
    String retailerNameInArabic;
    String retailerNumberEnglish;
    String retailerNameEnglish;
    String retailerLanguageIndicator;
    String terminalCurrencyCode;
    String terminalCountryCode;
    String transactionCurrencyExponent;
    String currencySymbolArabic;
    String currencySymbolEnglish;
    String arabicReceipt1;
    String arabicReceipt2;
    String englishReceipt1;
    String englishReceipt2;
    String retailerAddress1Arabic;
    String retailerAddress1English;
    String retailerAddress2Arabic;
    String retailerAddress2English;
    String terminalCapability;
    String additionalTerminalCapabilities;
    String downloadPhoneNumber;
    String eMVTerminalType;
    String automaticLoad;
    String sAFRetryLimit;
    String sAFDefaultMessageTransmissionNumber;

    public String getTerminalCountryCode() {
        return terminalCountryCode;
    }

    public void setTerminalCountryCode(String terminalCountryCode) {
        this.terminalCountryCode = terminalCountryCode;
    }

    public String getNextLoad() {
        return nextLoad;
    }

    public void setNextLoad(String nextLoad) {
        this.nextLoad = nextLoad;
    }

    public String getReconcillationTime() {
        return reconcillationTime;
    }

    public void setReconcillationTime(String reconcillationTime) {
        this.reconcillationTime = reconcillationTime;
    }

    public String getRetailerNameInArabic() {
        return retailerNameInArabic;
    }

    public void setRetailerNameInArabic(String retailerNameInArabic) {
        this.retailerNameInArabic = retailerNameInArabic;
    }

    public String getRetailerNumberEnglish() {
        return retailerNumberEnglish;
    }

    public void setRetailerNumberEnglish(String retailerNumberEnglish) {
        this.retailerNumberEnglish = retailerNumberEnglish;
    }

    public String getRetailerNameEnglish() {
        return retailerNameEnglish;
    }

    public void setRetailerNameEnglish(String retailerNameEnglish) {
        this.retailerNameEnglish = retailerNameEnglish;
    }

    public String getRetailerLanguageIndicator() {
        return retailerLanguageIndicator;
    }

    public void setRetailerLanguageIndicator(String retailerLanguageIndicator) {
        this.retailerLanguageIndicator = retailerLanguageIndicator;
    }

    public String getTerminalCurrencyCode() {
        return terminalCurrencyCode;
    }

    public void setTerminalCurrencyCode(String terminalCurrencyCode) {
        this.terminalCurrencyCode = terminalCurrencyCode;
    }

    public String getTransactionCurrencyExponent() {
        if(transactionCurrencyExponent.trim().length() != 0)
        return transactionCurrencyExponent;
        else
            return "0";
    }

    public void setTransactionCurrencyExponent(String transactionCurrencyExponent) {
        this.transactionCurrencyExponent = transactionCurrencyExponent;
    }

    public String getCurrencySymbolArabic() {
        return currencySymbolArabic;
    }

    public void setCurrencySymbolArabic(String currencySymbolArabic) {
        this.currencySymbolArabic = currencySymbolArabic;
    }

    public String getCurrencySymbolEnglish() {
        return currencySymbolEnglish;
    }

    public void setCurrencySymbolEnglish(String currencySymbolEnglish) {
        this.currencySymbolEnglish = currencySymbolEnglish;
    }

    public String getArabicReceipt1() {
        return arabicReceipt1;
    }

    public void setArabicReceipt1(String arabicReceipt1) {
        this.arabicReceipt1 = arabicReceipt1;
    }

    public String getArabicReceipt2() {
        return arabicReceipt2;
    }

    public void setArabicReceipt2(String arabicReceipt2) {
        this.arabicReceipt2 = arabicReceipt2;
    }

    public String getEnglishReceipt1() {
        return englishReceipt1;
    }

    public void setEnglishReceipt1(String englishReceipt1) {
        this.englishReceipt1 = englishReceipt1;
    }

    public String getEnglishReceipt2() {
        return englishReceipt2;
    }

    public void setEnglishReceipt2(String englishReceipt2) {
        this.englishReceipt2 = englishReceipt2;
    }

    public String getRetailerAddress1Arabic() {
        return retailerAddress1Arabic;
    }

    public void setRetailerAddress1Arabic(String retailerAddress1Arabic) {
        this.retailerAddress1Arabic = retailerAddress1Arabic;
    }

    public String getRetailerAddress1English() {
        return retailerAddress1English;
    }

    public void setRetailerAddress1English(String retailerAddress1English) {
        this.retailerAddress1English = retailerAddress1English;
    }

    public String getRetailerAddress2Arabic() {
        return retailerAddress2Arabic;
    }

    public void setRetailerAddress2Arabic(String retailerAddress2Arabic) {
        this.retailerAddress2Arabic = retailerAddress2Arabic;
    }

    public String getRetailerAddress2English() {
        return retailerAddress2English;
    }

    public void setRetailerAddress2English(String retailerAddress2English) {
        this.retailerAddress2English = retailerAddress2English;
    }

    public String getTerminalCapability() {
        return terminalCapability;
    }

    public void setTerminalCapability(String terminalCapability) {
        this.terminalCapability = terminalCapability;
    }

    public String getAdditionalTerminalCapabilities() {
        return additionalTerminalCapabilities;
    }

    public void setAdditionalTerminalCapabilities(String additionalTerminalCapabilities) {
        this.additionalTerminalCapabilities = additionalTerminalCapabilities;
    }

    public String getDownloadPhoneNumber() {
        return downloadPhoneNumber;
    }

    public void setDownloadPhoneNumber(String downloadPhoneNumber) {
        this.downloadPhoneNumber = downloadPhoneNumber;
    }

    public String geteMVTerminalType() {
        return eMVTerminalType;
    }

    public void seteMVTerminalType(String eMVTerminalType) {
        this.eMVTerminalType = eMVTerminalType;
    }

    public String getAutomaticLoad() {
        return automaticLoad;
    }

    public void setAutomaticLoad(String automaticLoad) {
        this.automaticLoad = automaticLoad;
    }

    public String getsAFRetryLimit() {
        return sAFRetryLimit;
    }

    public void setsAFRetryLimit(String sAFRetryLimit) {
        this.sAFRetryLimit = sAFRetryLimit;
    }

    public String getsAFDefaultMessageTransmissionNumber() {
        return sAFDefaultMessageTransmissionNumber;
    }

    public void setsAFDefaultMessageTransmissionNumber(String sAFDefaultMessageTransmissionNumber) {
        this.sAFDefaultMessageTransmissionNumber = sAFDefaultMessageTransmissionNumber;
    }

    @Override
    public String toString() {
        return "RetailerDataModel{" +
                "nextLoad='" + nextLoad + '\'' +
                ", reconcillationTime='" + reconcillationTime + '\'' +
                ", retailerNameInArabic='" + retailerNameInArabic + '\'' +
                ", retailerNumberEnglish='" + retailerNumberEnglish + '\'' +
                ", retailerNameEnglish='" + retailerNameEnglish + '\'' +
                ", retailerLanguageIndicator='" + retailerLanguageIndicator + '\'' +
                ", terminalCurrencyCode='" + terminalCurrencyCode + '\'' +
                ", terminalCountryCode='" + terminalCountryCode + '\'' +
                ", transactionCurrencyExponent='" + transactionCurrencyExponent + '\'' +
                ", currencySymbolArabic='" + currencySymbolArabic + '\'' +
                ", currencySymbolEnglish='" + currencySymbolEnglish + '\'' +
                ", arabicReceipt1='" + arabicReceipt1 + '\'' +
                ", arabicReceipt2='" + arabicReceipt2 + '\'' +
                ", englishReceipt1='" + englishReceipt1 + '\'' +
                ", englishReceipt2='" + englishReceipt2 + '\'' +
                ", retailerAddress1Arabic='" + retailerAddress1Arabic + '\'' +
                ", retailerAddress1English='" + retailerAddress1English + '\'' +
                ", retailerAddress2Arabic='" + retailerAddress2Arabic + '\'' +
                ", retailerAddress2English='" + retailerAddress2English + '\'' +
                ", terminalCapability='" + terminalCapability + '\'' +
                ", additionalTerminalCapabilities='" + additionalTerminalCapabilities + '\'' +
                ", downloadPhoneNumber='" + downloadPhoneNumber + '\'' +
                ", eMVTerminalType='" + eMVTerminalType + '\'' +
                ", automaticLoad='" + automaticLoad + '\'' +
                ", sAFRetryLimit='" + sAFRetryLimit + '\'' +
                ", sAFDefaultMessageTransmissionNumber='" + sAFDefaultMessageTransmissionNumber + '\'' +
                '}';
    }
}
