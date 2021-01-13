package com.tarang.dpq2.base.room_database.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.tarang.dpq2.base.room_database.db.entity.TMSAIDdataModelEntity;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface AIDDataDao {

    @Insert(onConflict = REPLACE)
    void insertAIDData(TMSAIDdataModelEntity modelEntity);

    @Query("SELECT * FROM TMSAIDdataModelEntity Where aid == :id")
    TMSAIDdataModelEntity getAIDData(String id);

    @Query("SELECT * FROM TMSAIDdataModelEntity")
    List<TMSAIDdataModelEntity> getAllAIDData();

    @Query("DELETE FROM TMSAIDdataModelEntity")
    public void nukeTable();

}
