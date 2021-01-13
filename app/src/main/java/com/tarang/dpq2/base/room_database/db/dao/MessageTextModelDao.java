package com.tarang.dpq2.base.room_database.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.tarang.dpq2.base.room_database.db.entity.TMSMessageTextModelEntity;

import java.util.List;

@Dao
public interface MessageTextModelDao {

    @Query("DELETE FROM TMSMessageTextModelEntity")
    void deleteUsers();

    @Insert
    long insertMessageText(TMSMessageTextModelEntity messageTextModelEntities);

    @Query("SELECT * FROM TMSMessageTextModelEntity WHERE messageCode == :messageCode AND " + "displayCode == 0 LIMIT 1")
    TMSMessageTextModelEntity getTerminalMessageText(String messageCode);

    @Query("SELECT * FROM TMSMessageTextModelEntity")
    List<TMSMessageTextModelEntity> getTerminalMessageList();

    @Query("SELECT * FROM TMSMessageTextModelEntity WHERE messageCode == :messageCode AND " + "displayCode == 2 LIMIT 1")
    TMSMessageTextModelEntity getPrinterMessageText(String messageCode);

    @Query("Update TMSMessageTextModelEntity SET arabicMessageText = :arabicMessageText AND englishMessageText = :englishMessageText WHERE messageCode == :messageCode AND displayCode == :displayCode")
    int updateTMSMessageText(String messageCode,String displayCode, String arabicMessageText, String englishMessageText);

    @Query("DELETE FROM TMSMessageTextModelEntity")
    public void nukeTable();

}
