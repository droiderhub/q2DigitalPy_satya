package com.tarang.dpq2.base.room_database.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TMSMessageTextModelEntity {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "messageCode")
    private String messageCode = "";

    @ColumnInfo(name = "displayCode")
    private String displayCode = "";

    @ColumnInfo(name = "arabicMessageText")
    private String arabicMessageText = "";

    @ColumnInfo(name = "englishMessageText")
    private String englishMessageText = "";

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    public String getDisplayCode() {
        return displayCode;
    }

    public void setDisplayCode(String displayCode) {
        this.displayCode = displayCode;
    }

    public String getArabicMessageText() {
        return arabicMessageText;
    }

    public void setArabicMessageText(String arabicMessageText) {
        this.arabicMessageText = arabicMessageText;
    }

    public String getEnglishMessageText() {
        return englishMessageText;
    }

    public void setEnglishMessageText(String englishMessageText) {
        this.englishMessageText = englishMessageText;
    }

    @Override
    public String toString() {
        return "TMSMessageTextModelEntity{" +
                "uid=" + uid +
                ", messageCode='" + messageCode + '\'' +
                ", displayCode='" + displayCode + '\'' +
                ", arabicMessageText='" + arabicMessageText + '\'' +
                ", englishMessageText='" + englishMessageText + '\'' +
                '}';
    }
}
