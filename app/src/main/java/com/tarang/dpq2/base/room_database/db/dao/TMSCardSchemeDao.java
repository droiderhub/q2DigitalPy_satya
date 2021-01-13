package com.tarang.dpq2.base.room_database.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.tarang.dpq2.base.room_database.db.entity.TMSCardSchemeEntity;
import com.tarang.dpq2.base.room_database.db.tuple.BinRangeTuple;
import com.tarang.dpq2.base.room_database.db.tuple.FloorLimitTuple;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface TMSCardSchemeDao {

    @Insert(onConflict = REPLACE)
    void insertCardScheme(TMSCardSchemeEntity modelEntity);

    @Query("SELECT * FROM TMSCardSchemeEntity Where cardIndicator == :id")
    TMSCardSchemeEntity getCardSchemeData(String id);

    @Query("SELECT * FROM TMSCardSchemeEntity")
    List<TMSCardSchemeEntity> getCardSchemeData();

    @Query("Update TMSCardSchemeEntity SET binRanges = :cardRanges , cardPrefexSequence = :prefix Where cardIndicator == :cardIndicator")
    void updateBindRangers(String cardIndicator,String cardRanges, String prefix);

    @Update(onConflict = REPLACE)
    void updateCardScheme(TMSCardSchemeEntity modelEntity);

    @Query("SELECT cardIndicator , binRanges FROM TMSCardSchemeEntity WHERE cardIndicator != :indicator AND cardIndicator != :indicator1 AND cardIndicator != :indicator2 AND cardIndicator != :indicator3")
    List<BinRangeTuple> getAllBinRanges(String indicator,String indicator1,String indicator2,String indicator3);

    @Query("SELECT cardIndicator , binRanges FROM TMSCardSchemeEntity WHERE cardIndicator == :indicator OR cardIndicator == :indicator1")
    List<BinRangeTuple> getSpecificBinRanges(String indicator , String indicator1);

    @Query("SELECT cardSchemeID FROM TMSCardSchemeEntity WHERE cardIndicator == :id")
    String getCardSchemeID(String id);

    @Query("SELECT offlineRefundEnabled FROM TMSCardSchemeEntity WHERE cardIndicator == :card")
    boolean getOfflineRefunEnabled(String card);

    @Query("SELECT floorLimitEnabled,terminalFloorLimit,terminalFloorLimitFallback FROM TMSCardSchemeEntity Where cardIndicator == :id")
    FloorLimitTuple getFloorLimit(String id);

    @Query("DELETE FROM TMSCardSchemeEntity")
    public void nukeTable();

}
