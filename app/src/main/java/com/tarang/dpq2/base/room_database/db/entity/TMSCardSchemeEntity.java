package com.tarang.dpq2.base.room_database.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TMSCardSchemeEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "cardIndicator")
    private String cardIndicator = "";

    @ColumnInfo(name = "cardNameArabic")
    private String cardNameArabic = "";

    @ColumnInfo(name = "cardNameEnglish")
    private String cardNameEnglish = "";

    @ColumnInfo(name = "cardSchemeID")
    private String cardSchemeID = "";

    @ColumnInfo(name = "merchantCategoryCode")
    private String merchantCategoryCode = "";

    @ColumnInfo(name = "merchantID")
    private String merchantID = "";

    @ColumnInfo(name = "terminalID")
    private String terminalID = "";

    @ColumnInfo(name = "emvEnabled")
    private boolean emvEnabled;

    @ColumnInfo(name = "serviceCodeEnabled")
    private boolean serviceCodeEnabled;

    @ColumnInfo(name = "offlineRefundEnabled")
    private boolean offlineRefundEnabled;

    @ColumnInfo(name = "transactionAllowed")
    private String transactionAllowed = "";

    @ColumnInfo(name = "cardHolderAuth")
    private String cardHolderAuth = "";

    @ColumnInfo(name = "supervisorFunctions")
    private String supervisorFunctions = "";

    @ColumnInfo(name = "manualEntryEnabled")
    private String manualEntryEnabled = "";

    @ColumnInfo(name = "floorLimitEnabled")
    private boolean floorLimitEnabled;

    @ColumnInfo(name = "terminalFloorLimit")
    private String terminalFloorLimit = "";

    @ColumnInfo(name = "terminalFloorLimitFallback")
    private String terminalFloorLimitFallback = "";

    @ColumnInfo(name = "maximumCashback")
    private String maximumCashback = "";

    @ColumnInfo(name = "maxTransactionAmtIndicator")
    private String maxTransactionAmtIndicator = "";

    @ColumnInfo(name = "maxTransactionAmt")
    private String maxTransactionAmt = "";

    @ColumnInfo(name = "luhnCheckEnabled")
    private boolean luhnCheckEnabled;

    @ColumnInfo(name = "expiryDataPosition")
    private String expiryDataPosition = "";

    @ColumnInfo(name = "delayCallSetup")
    private String delayCallSetup = "";

    @ColumnInfo(name = "binRanges")
    private String binRanges = "";

    @ColumnInfo(name = "cardPrefexSequence")
    private String cardPrefexSequence = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCardIndicator() {
        return cardIndicator;
    }

    public void setCardIndicator(String cardIndicator) {
        this.cardIndicator = cardIndicator;
    }

    public String getCardNameArabic() {
        return cardNameArabic;
    }

    public void setCardNameArabic(String cardNameArabic) {
        this.cardNameArabic = cardNameArabic;
    }

    public String getCardNameEnglish() {
        return cardNameEnglish;
    }

    public void setCardNameEnglish(String cardNameEnglish) {
        this.cardNameEnglish = cardNameEnglish;
    }

    public String getCardSchemeID() {
        return cardSchemeID;
    }

    public void setCardSchemeID(String cardSchemeID) {
        this.cardSchemeID = cardSchemeID;
    }

    public String getMerchantCategoryCode() {
        return merchantCategoryCode;
    }

    public void setMerchantCategoryCode(String merchantCategoryCode) {
        this.merchantCategoryCode = merchantCategoryCode;
    }

    public String getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(String merchantID) {
        this.merchantID = merchantID;
    }

    public String getTerminalID() {
        return terminalID;
    }

    public void setTerminalID(String terminalID) {
        this.terminalID = terminalID;
    }

    public boolean isEmvEnabled() {
        return emvEnabled;
    }

    public void setEmvEnabled(boolean emvEnabled) {
        this.emvEnabled = emvEnabled;
    }

    public boolean isServiceCodeEnabled() {
        return serviceCodeEnabled;
    }

    public void setServiceCodeEnabled(boolean serviceCodeEnabled) {
        this.serviceCodeEnabled = serviceCodeEnabled;
    }

    public boolean isOfflineRefundEnabled() {
        return offlineRefundEnabled;
    }

    public void setOfflineRefundEnabled(boolean offlineRefundEnabled) {
        this.offlineRefundEnabled = offlineRefundEnabled;
    }

    public String getTransactionAllowed() {
        return transactionAllowed;
    }

    public void setTransactionAllowed(String transactionAllowed) {
        this.transactionAllowed = transactionAllowed;
    }

    public String getCardHolderAuth() {
        return cardHolderAuth;
    }

    public void setCardHolderAuth(String cardHolderAuth) {
        this.cardHolderAuth = cardHolderAuth;
    }

    public String getSupervisorFunctions() {
        return supervisorFunctions;
    }

    public void setSupervisorFunctions(String supervisorFunctions) {
        this.supervisorFunctions = supervisorFunctions;
    }

    public String getManualEntryEnabled() {
        return manualEntryEnabled;
    }

    public void setManualEntryEnabled(String manualEntryEnabled) {
        this.manualEntryEnabled = manualEntryEnabled;
    }

    public boolean isFloorLimitEnabled() {
        return floorLimitEnabled;
    }

    public void setFloorLimitEnabled(boolean floorLimitEnabled) {
        this.floorLimitEnabled = floorLimitEnabled;
    }

    public String getTerminalFloorLimit() {
        return terminalFloorLimit;
    }

    public void setTerminalFloorLimit(String terminalFloorLimit) {
        this.terminalFloorLimit = terminalFloorLimit;
    }

    public String getTerminalFloorLimitFallback() {
        return terminalFloorLimitFallback;
    }

    public void setTerminalFloorLimitFallback(String terminalFloorLimitFallback) {
        this.terminalFloorLimitFallback = terminalFloorLimitFallback;
    }

    public String getMaximumCashback() {
        return maximumCashback;
    }

    public void setMaximumCashback(String maximumCashback) {
        this.maximumCashback = maximumCashback;
    }

    public String getMaxTransactionAmtIndicator() {
        return maxTransactionAmtIndicator;
    }

    public void setMaxTransactionAmtIndicator(String maxTransactionAmtIndicator) {
        this.maxTransactionAmtIndicator = maxTransactionAmtIndicator;
    }

    public String getMaxTransactionAmt() {
        return maxTransactionAmt;
    }

    public void setMaxTransactionAmt(String maxTransactionAmt) {
        this.maxTransactionAmt = maxTransactionAmt;
    }

    public boolean isLuhnCheckEnabled() {
        return luhnCheckEnabled;
    }

    public void setLuhnCheckEnabled(boolean luhnCheckEnabled) {
        this.luhnCheckEnabled = luhnCheckEnabled;
    }

    public String getExpiryDataPosition() {
        return expiryDataPosition;
    }

    public void setExpiryDataPosition(String expiryDataPosition) {
        this.expiryDataPosition = expiryDataPosition;
    }

    public String getDelayCallSetup() {
        return delayCallSetup;
    }

    public void setDelayCallSetup(String delayCallSetup) {
        this.delayCallSetup = delayCallSetup;
    }

    public String getBinRanges() {
        return binRanges;
    }

    public void setBinRanges(String binRanges) {
        this.binRanges = binRanges;
    }

    public String getCardPrefexSequence() {
        return cardPrefexSequence;
    }

    public void setCardPrefexSequence(String cardPrefexSequence) {
        this.cardPrefexSequence = cardPrefexSequence;
    }

    @Override
    public String toString() {
        return "TMSCardSchemeEntity{" +
                "id=" + id +
                ", cardIndicator='" + cardIndicator + '\'' +
                ", cardNameArabic='" + cardNameArabic + '\'' +
                ", cardNameEnglish='" + cardNameEnglish + '\'' +
                ", cardSchemeID='" + cardSchemeID + '\'' +
                ", merchantCategoryCode='" + merchantCategoryCode + '\'' +
                ", merchantID='" + merchantID + '\'' +
                ", terminalID='" + terminalID + '\'' +
                ", emvEnabled=" + emvEnabled +
                ", serviceCodeEnabled=" + serviceCodeEnabled +
                ", offlineRefundEnabled='" + offlineRefundEnabled + '\'' +
                ", transactionAllowed='" + transactionAllowed + '\'' +
                ", cardHolderAuth='" + cardHolderAuth + '\'' +
                ", supervisorFunctions='" + supervisorFunctions + '\'' +
                ", manualEntryEnabled='" + manualEntryEnabled + '\'' +
                ", floorLimitEnabled=" + floorLimitEnabled +
                ", terminalFloorLimit='" + terminalFloorLimit + '\'' +
                ", terminalFloorLimitFallback='" + terminalFloorLimitFallback + '\'' +
                ", maximumCashback='" + maximumCashback + '\'' +
                ", maxTransactionAmtIndicator='" + maxTransactionAmtIndicator + '\'' +
                ", maxTransactionAmt='" + maxTransactionAmt + '\'' +
                ", luhnCheckEnabled=" + luhnCheckEnabled +
                ", expiryDataPosition='" + expiryDataPosition + '\'' +
                ", delayCallSetup='" + delayCallSetup + '\'' +
                ", binRanges='" + binRanges + '\'' +
                ", cardPrefexSequence='" + cardPrefexSequence + '\'' +
                '}';
    }
}
