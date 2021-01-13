package com.tarang.dpq2.base.room_database.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TMSAIDdataModelEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "aid")
    private String aid = "";

    @ColumnInfo(name = "aidLable")
    private String aidLable = "";

    @ColumnInfo(name = "aidTerminalVersionNumber")
    private String aidTerminalVersionNumber = "";

    @ColumnInfo(name = "exactOnlySelection")
    private boolean exactOnlySelection;

    @ColumnInfo(name = "skipEMVProcess")
    private boolean skipEMVProcess;

    @ColumnInfo(name = "defaultTDOL")
    private String defaultTDOL = "";

    @ColumnInfo(name = "defaultDDOL")
    private String defaultDDOL = "";

    @ColumnInfo(name = "emvAdditionalTag")
    private String emvAdditionalTag = "";

    @ColumnInfo(name = "denialActionCode")
    private String denialActionCode = "";

    @ColumnInfo(name = "onlineActionCode")
    private String onlineActionCode = "";

    @ColumnInfo(name = "defaultActionCode")
    private String defaultActionCode = "";

    @ColumnInfo(name = "trsholdValue")
    private String trsholdValue = "";

    @ColumnInfo(name = "targetPercentage")
    private String targetPercentage = "";

    @ColumnInfo(name = "maxTargetPercentage")
    private String maxTargetPercentage = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getAidLable() {
        return aidLable;
    }

    public void setAidLable(String aidLable) {
        this.aidLable = aidLable;
    }

    public String getAidTerminalVersionNumber() {
        return aidTerminalVersionNumber;
    }

    public void setAidTerminalVersionNumber(String aidTerminalVersionNumber) {
        this.aidTerminalVersionNumber = aidTerminalVersionNumber;
    }

    public boolean isExactOnlySelection() {
        return exactOnlySelection;
    }

    public void setExactOnlySelection(boolean exactOnlySelection) {
        this.exactOnlySelection = exactOnlySelection;
    }

    public boolean isSkipEMVProcess() {
        return skipEMVProcess;
    }

    public void setSkipEMVProcess(boolean skipEMVProcess) {
        this.skipEMVProcess = skipEMVProcess;
    }

    public String getDefaultTDOL() {
        return defaultTDOL;
    }

    public void setDefaultTDOL(String defaultTDOL) {
        this.defaultTDOL = defaultTDOL;
    }

    public String getDefaultDDOL() {
        return defaultDDOL;
    }

    public void setDefaultDDOL(String defaultDDOL) {
        this.defaultDDOL = defaultDDOL;
    }

    public String getEmvAdditionalTag() {
        return emvAdditionalTag;
    }

    public void setEmvAdditionalTag(String emvAdditionalTag) {
        this.emvAdditionalTag = emvAdditionalTag;
    }

    public String getDenialActionCode() {
        return denialActionCode;
    }

    public void setDenialActionCode(String denialActionCode) {
        this.denialActionCode = denialActionCode;
    }

    public String getOnlineActionCode() {
        return onlineActionCode;
    }

    public void setOnlineActionCode(String onlineActionCode) {
        this.onlineActionCode = onlineActionCode;
    }

    public String getDefaultActionCode() {
        return defaultActionCode;
    }

    public void setDefaultActionCode(String defaultActionCode) {
        this.defaultActionCode = defaultActionCode;
    }

    public String getTrsholdValue() {
        if (trsholdValue.trim().length() != 0)
            return trsholdValue;
        else
            return "0";
    }

    public void setTrsholdValue(String trsholdValue) {
        this.trsholdValue = trsholdValue;
    }

    public String getTargetPercentage() {
        return targetPercentage;
    }

    public void setTargetPercentage(String targetPercentage) {
        this.targetPercentage = targetPercentage;
    }

    public String getMaxTargetPercentage() {
        return maxTargetPercentage;
    }

    public void setMaxTargetPercentage(String maxTargetPercentage) {
        this.maxTargetPercentage = maxTargetPercentage;
    }

    @Override
    public String toString() {
        return "TMSAIDdataModelEntity{" +
                "id=" + id +
                ", aid='" + aid + '\'' +
                ", aidLable='" + aidLable + '\'' +
                ", aidTerminalVersionNumber='" + aidTerminalVersionNumber + '\'' +
                ", exactOnlySelection=" + exactOnlySelection +
                ", skipEMVProcess=" + skipEMVProcess +
                ", defaultTDOL='" + defaultTDOL + '\'' +
                ", defaultDDOL='" + defaultDDOL + '\'' +
                ", emvAdditionalTag='" + emvAdditionalTag + '\'' +
                ", denialActionCode='" + denialActionCode + '\'' +
                ", onlineActionCode='" + onlineActionCode + '\'' +
                ", defaultActionCode='" + defaultActionCode + '\'' +
                ", trsholdValue='" + trsholdValue + '\'' +
                ", targetPercentage='" + targetPercentage + '\'' +
                ", maxTargetPercentage='" + maxTargetPercentage + '\'' +
                '}';
    }
}
