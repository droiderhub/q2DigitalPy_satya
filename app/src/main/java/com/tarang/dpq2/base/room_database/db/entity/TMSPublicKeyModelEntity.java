package com.tarang.dpq2.base.room_database.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TMSPublicKeyModelEntity {

    @PrimaryKey(autoGenerate = true)
    private int uid;
    @ColumnInfo(name = "RID")
    private String RID = "";
    @ColumnInfo(name = "keyIndex")
    private String keyIndex = "";
    @ColumnInfo(name = "hashId")
    private String hashId = "";
    @ColumnInfo(name = "digitalSignatureId")
    private String digitalSignatureId = "";
    @ColumnInfo(name = "publicKey")
    private String publicKey = "";
    @ColumnInfo(name = "exponent")
    private String exponent = "";
    @ColumnInfo(name = "checkSum")
    private String checkSum = "";
    @ColumnInfo(name = "caPublicKeyLength")
    private String caPublicKeyLength = "";
    @ColumnInfo(name = "caPublicKeyExpiryDate")
    private String caPublicKeyExpiryDate = "";

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getRID() {
        return RID;
    }

    public void setRID(String RID) {
        this.RID = RID;
    }

    public String getKeyIndex() {
        return keyIndex;
    }

    public void setKeyIndex(String keyIndex) {
        this.keyIndex = keyIndex;
    }

    public String getHashId() {
        return hashId;
    }

    public void setHashId(String hashId) {
        this.hashId = hashId;
    }

    public String getDigitalSignatureId() {
        return digitalSignatureId;
    }

    public void setDigitalSignatureId(String digitalSignatureId) {
        this.digitalSignatureId = digitalSignatureId;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getExponent() {
        return exponent;
    }

    public void setExponent(String exponent) {
        this.exponent = exponent;
    }

    public String getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }

    public String getCaPublicKeyLength() {
        return caPublicKeyLength;
    }

    public void setCaPublicKeyLength(String caPublicKeyLength) {
        this.caPublicKeyLength = caPublicKeyLength;
    }

    public String getCaPublicKeyExpiryDate() {
        return caPublicKeyExpiryDate;
    }

    public void setCaPublicKeyExpiryDate(String caPublicKeyExpiryDate) {
        this.caPublicKeyExpiryDate = caPublicKeyExpiryDate;
    }

    @Override
    public String toString() {
        return "TMSPublicKeyModelEntity{" +
                "uid=" + uid +
                ", RID='" + RID + '\'' +
                ", keyIndex='" + keyIndex + '\'' +
                ", hashId='" + hashId + '\'' +
                ", digitalSignatureId='" + digitalSignatureId + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", exponent='" + exponent + '\'' +
                ", checkSum='" + checkSum + '\'' +
                ", caPublicKeyLength='" + caPublicKeyLength + '\'' +
                ", caPublicKeyExpiryDate='" + caPublicKeyExpiryDate + '\'' +
                '}';
    }
}
