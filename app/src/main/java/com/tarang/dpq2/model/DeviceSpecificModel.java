package com.tarang.dpq2.model;

public class DeviceSpecificModel {
    private String Data;
    private String cardSchemeMada;
    private String terminalContactlessTransactionLimitMada;
    private String terminalCVMRequiredLimitMada;
    private String terminalContactlessFloorLimitMada = "0";

    private String cardScheme1;
    private String terminalContactlessTransactionLimit1;
    private String terminalCVMRequiredLimit1;
    private String terminalContactlessFloorLimit1;

    private String cardScheme2;
    private String terminalContactlessTransactionLimit2;
    private String terminalCVMRequiredLimit2;
    private String terminalContactlessFloorLimit2;

    private String maxSAFDepth;
    private String maxSAFCumulativeAmount;
    private String idleTime;
    private String maxReconciliationAmount;
    private String maxTransactionsProcessed;
    private String qRCodePrintIndicator;

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public String getCardSchemeMada() {
        return cardSchemeMada;
    }

    public void setCardSchemeMada(String cardSchemeMada) {
        this.cardSchemeMada = cardSchemeMada;
    }

    public String getTerminalContactlessTransactionLimitMada() {
        return terminalContactlessTransactionLimitMada;
    }

    public void setTerminalContactlessTransactionLimitMada(String terminalContactlessTransactionLimitMada) {
        this.terminalContactlessTransactionLimitMada = terminalContactlessTransactionLimitMada;
    }

    public String getTerminalCVMRequiredLimitMada() {
        return terminalCVMRequiredLimitMada;
    }

    public void setTerminalCVMRequiredLimitMada(String terminalCVMRequiredLimitMada) {
        this.terminalCVMRequiredLimitMada = terminalCVMRequiredLimitMada;
    }

    public String getTerminalContactlessFloorLimitMada() {
        return terminalContactlessFloorLimitMada;
    }

    public void setTerminalContactlessFloorLimitMada(String terminalContactlessFloorLimitMada) {
        this.terminalContactlessFloorLimitMada = terminalContactlessFloorLimitMada;
    }

    public String getCardScheme1() {
        return cardScheme1;
    }

    public void setCardScheme1(String cardScheme1) {
        this.cardScheme1 = cardScheme1;
    }

    public String getTerminalContactlessTransactionLimit1() {
        return terminalContactlessTransactionLimit1;
    }

    public void setTerminalContactlessTransactionLimit1(String terminalContactlessTransactionLimit1) {
        this.terminalContactlessTransactionLimit1 = terminalContactlessTransactionLimit1;
    }

    public String getTerminalCVMRequiredLimit1() {
        return terminalCVMRequiredLimit1;
    }

    public void setTerminalCVMRequiredLimit1(String terminalCVMRequiredLimit1) {
        this.terminalCVMRequiredLimit1 = terminalCVMRequiredLimit1;
    }

    public String getTerminalContactlessFloorLimit1() {
        return terminalContactlessFloorLimit1;
    }

    public void setTerminalContactlessFloorLimit1(String terminalContactlessFloorLimit1) {
        this.terminalContactlessFloorLimit1 = terminalContactlessFloorLimit1;
    }

    public String getCardScheme2() {
        return cardScheme2;
    }

    public void setCardScheme2(String cardScheme2) {
        this.cardScheme2 = cardScheme2;
    }

    public String getTerminalContactlessTransactionLimit2() {
        return terminalContactlessTransactionLimit2;
    }

    public void setTerminalContactlessTransactionLimit2(String terminalContactlessTransactionLimit2) {
        this.terminalContactlessTransactionLimit2 = terminalContactlessTransactionLimit2;
    }

    public String getTerminalCVMRequiredLimit2() {
        return terminalCVMRequiredLimit2;
    }

    public void setTerminalCVMRequiredLimit2(String terminalCVMRequiredLimit2) {
        this.terminalCVMRequiredLimit2 = terminalCVMRequiredLimit2;
    }

    public String getTerminalContactlessFloorLimit2() {
        return terminalContactlessFloorLimit2;
    }

    public void setTerminalContactlessFloorLimit2(String terminalContactlessFloorLimit2) {
        this.terminalContactlessFloorLimit2 = terminalContactlessFloorLimit2;
    }

    public String getMaxSAFDepth() {
        return maxSAFDepth;
    }

    public void setMaxSAFDepth(String maxSAFDepth) {
        this.maxSAFDepth = maxSAFDepth;
    }

    public String getMaxSAFCumulativeAmount() {
        return maxSAFCumulativeAmount;
    }

    public void setMaxSAFCumulativeAmount(String maxSAFCumulativeAmount) {
        this.maxSAFCumulativeAmount = maxSAFCumulativeAmount;
    }

    public String getIdleTime() {
        return idleTime;
    }

    public void setIdleTime(String idleTime) {
        this.idleTime = idleTime;
    }

    public String getMaxReconciliationAmount() {
        return maxReconciliationAmount;
    }

    public void setMaxReconciliationAmount(String maxReconciliationAmount) {
        this.maxReconciliationAmount = maxReconciliationAmount;
    }

    public String getMaxTransactionsProcessed() {
        return maxTransactionsProcessed;
    }

    public void setMaxTransactionsProcessed(String maxTransactionsProcessed) {
        this.maxTransactionsProcessed = maxTransactionsProcessed;
    }

    public String getqRCodePrintIndicator() {
        return qRCodePrintIndicator;
    }

    public void setqRCodePrintIndicator(String qRCodePrintIndicator) {
        this.qRCodePrintIndicator = qRCodePrintIndicator;
    }

    @Override
    public String toString() {
        return "DeviceSpecificModel{" +
                "Data='" + Data + '\'' +
                ", cardSchemeMada='" + cardSchemeMada + '\'' +
                ", terminalContactlessTransactionLimitMada='" + terminalContactlessTransactionLimitMada + '\'' +
                ", terminalCVMRequiredLimitMada='" + terminalCVMRequiredLimitMada + '\'' +
                ", terminalContactlessFloorLimitMada='" + terminalContactlessFloorLimitMada + '\'' +
                ", cardScheme1='" + cardScheme1 + '\'' +
                ", terminalContactlessTransactionLimit1='" + terminalContactlessTransactionLimit1 + '\'' +
                ", terminalCVMRequiredLimit1='" + terminalCVMRequiredLimit1 + '\'' +
                ", terminalContactlessFloorLimit1='" + terminalContactlessFloorLimit1 + '\'' +
                ", cardScheme2='" + cardScheme2 + '\'' +
                ", terminalContactlessTransactionLimit2='" + terminalContactlessTransactionLimit2 + '\'' +
                ", terminalCVMRequiredLimit2='" + terminalCVMRequiredLimit2 + '\'' +
                ", terminalContactlessFloorLimit2='" + terminalContactlessFloorLimit2 + '\'' +
                ", maxSAFDepth='" + maxSAFDepth + '\'' +
                ", maxSAFCumulativeAmount='" + maxSAFCumulativeAmount + '\'' +
                ", idleTime='" + idleTime + '\'' +
                ", maxReconciliationAmount='" + maxReconciliationAmount + '\'' +
                ", maxTransactionsProcessed='" + maxTransactionsProcessed + '\'' +
                ", qRCodePrintIndicator='" + qRCodePrintIndicator + '\'' +
                '}';
    }
}
