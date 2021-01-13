package com.tarang.dpq2.base.room_database.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.tarang.dpq2.base.room_database.db.entity.TMSPublicKeyModelEntity;

import java.util.List;

@Dao
public interface TMSPublicKeyModelDao {
    // Insert a Single Transaction
    @Insert
    void insertTMSPublicKey(TMSPublicKeyModelEntity tmsPublicKeyModelEntity);

    @Query("SELECT * FROM TMSPublicKeyModelEntity")
    List<TMSPublicKeyModelEntity> getCapKeys();

    @Query("DELETE FROM TMSPublicKeyModelEntity")
    public void nukeTable();

}
