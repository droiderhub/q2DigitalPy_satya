package com.tarang.dpq2.base.room_database.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.tarang.dpq2.base.room_database.db.entity.SAFModelEntity;
import com.tarang.dpq2.base.room_database.db.entity.TransactionModelEntity;
import com.tarang.dpq2.base.room_database.db.tuple.TransactionHistoryTuple;

import java.util.List;

@Dao
public interface SAFModelDao {

    @Query("SELECT * FROM SAFModelEntity")
    List<TransactionModelEntity> getAll();

    @Query("SELECT * FROM SAFModelEntity WHERE (responseCode39 == :de39 OR responseCode39 == :de39_)")
    List<TransactionModelEntity> getAllSuccess(String de39,String de39_);

    @Query("SELECT * FROM SAFModelEntity WHERE additionalDataNational47 ==:code")
    List<TransactionModelEntity> getAll(String code);

    // Retrive a single Transaction
    @Query("SELECT * FROM SAFModelEntity WHERE retriRefNo37 == :id")
    SAFModelEntity getTransaction(String id);

    // Retrive a last Transaction
    @Query("SELECT * FROM SAFModelEntity ORDER BY uid DESC LIMIT 1")
    TransactionModelEntity getLastTransaction();

    // Insert a Single Transaction
    @Insert()
    void insertTransaction(SAFModelEntity modelEntity);

    @Query("UPDATE SAFModelEntity SET iccCardSystemRelatedData55_final = :status WHERE uid == :id")
    void updateData55(int id, String status);

    //Update Start Time Transaction
    @Query("UPDATE SAFModelEntity SET startTimeTransaction = :date WHERE uid == :id")
    void updateStartTime(int id, String date);

    //Update Start Time Transaction
    @Query("UPDATE SAFModelEntity SET endTimeTransaction = :date WHERE uid == :id")
    void updateEndTime(int id, String date);

    //Update Connection Start Time
    @Query("UPDATE SAFModelEntity SET startTimeConnection = :date WHERE uid == :id")
    void updateConnectionStartTime(int id, String date);

    //Update Connection Start Time
    @Query("UPDATE SAFModelEntity SET endTimeConnection = :date WHERE uid == :id")
    void updateConnectionEndTime(int id, String date);

    //Fetch Previous Transaction Time
    @Query("SELECT * FROM SAFModelEntity WHERE responseCode39 == :success ORDER BY uid DESC LIMIT 1")
    SAFModelEntity getLastSuccessTransaction(String success);

    //Fetch rrn
    @Query("SELECT authIdResCode38 FROM SAFModelEntity WHERE retriRefNo37 == :rrn")
    String getAuthIdResCode38(String rrn);

    //Fetch rrn
    @Query("SELECT responseCode39 FROM SAFModelEntity WHERE retriRefNo37 == :rrn")
    String getResponseCode39(String rrn);

    @Query("SELECT uid,iccCardSystemRelatedData55,retriRefNo37,responseCode39,transmissionDateTime7,amtTransaction4,authIdResCode38 FROM SAFModelEntity")
    List<TransactionHistoryTuple> loadAllSAFTransactions();

    // Update the existing Transaction
    @Update()
    int updateTransaction(SAFModelEntity modelEntity);

    @Delete
    void delete(SAFModelEntity SAFModelEntity);

    @Query("UPDATE SAFModelEntity SET authIdResCode38 = :data38,responseCode39 = :data39 WHERE uid == :id")
    void updateResponse(int id, String data38, String data39);

    @Query("DELETE FROM SAFModelEntity")
    public void nukeTable();

    @Query("DELETE FROM SAFModelEntity WHERE retriRefNo37 = :rrn37")
     void deleteSAFWithRRn(String rrn37);

    @Query("DELETE FROM SAFModelEntity WHERE systemTraceAuditnumber11 = :rrn37")
     void deleteSAFWithSTAN(String rrn37);

    @Query("DELETE FROM SAFModelEntity WHERE :id==uid")
    public void deleteSafItem(int id);

    @Query("UPDATE SAFModelEntity SET request_mportal = :status WHERE systemTraceAuditnumber11 = :rrn37")
    void updateSAFMerchantPortalRequest(String rrn37,String status);

    @Query("UPDATE SAFModelEntity SET status_mportal = :status, request_mportal = :req WHERE systemTraceAuditnumber11 = :rrn37")
    void updateSAFMerchantPortal(String rrn37,boolean status,String req);

    @Query("SELECT * FROM TransactionModelEntity WHERE status_mportal == :status AND request_mportal != :rrn37")
    TransactionModelEntity getMPortalRequest(String rrn37,boolean status);

}
