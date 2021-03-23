package com.tarang.dpq2.base.room_database.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.tarang.dpq2.base.room_database.db.entity.TransactionModelEntity;
import com.tarang.dpq2.base.room_database.db.tuple.Tag62RelatedTuple;
import com.tarang.dpq2.base.room_database.db.tuple.TransactionHistoryTuple;

import java.util.List;

@Dao
public interface TransactionModelDao {

    @Query("SELECT * FROM TransactionModelEntity")
    List<TransactionModelEntity> getAll();

    @Query("SELECT * FROM TransactionModelEntity WHERE statusTransaction ==:status AND (responseCode39 ==:code OR responseCode39 ==:code1 OR responseCode39 ==:code4 OR responseCode39 ==:code3 OR responseCode39 ==:code2) AND additionalDataNational47 ==:indicator")
    List<TransactionModelEntity> getSuccessTransaction(String code1, int status, String code, String indicator, String code2, String code3, String code4);

    @Query("SELECT * FROM TransactionModelEntity WHERE statusTransaction ==:status AND (responseCode39 ==:code OR responseCode39 ==:code1 OR responseCode39 ==:code4 OR responseCode39 ==:code3 OR responseCode39 ==:code2) AND cardIndicator ==:indicator")
    List<TransactionModelEntity> getSuccessTransaction(String indicator, String code1, int status, String code, String code2, String code3, String code4);

    @Query("SELECT * FROM TransactionModelEntity WHERE statusTransaction ==:status AND (responseCode39 ==:code OR responseCode39 ==:code1 OR responseCode39 ==:code2) AND additionalDataNational47 ==:indicator")
    List<TransactionModelEntity> getSuccessTransaction(String code2, String code1, int status, String code, String indicator);

    @Query("SELECT * FROM TransactionModelEntity WHERE statusTransaction ==:status AND responseCode39 ==:code AND additionalDataNational47 ==:indicator AND safEntity == :saf")
    List<TransactionModelEntity> getSuccessTransaction(int status, String code, String indicator, boolean saf);

    @Query("SELECT * FROM TransactionModelEntity WHERE statusTransaction ==:status AND responseCode39 ==:code AND additionalDataNational47 ==:indicator AND nameTransactionTag == :transaction")
    List<TransactionModelEntity> getSuccessTransaction(int status, String code, String indicator, String transaction);

    @Query("SELECT * FROM TransactionModelEntity WHERE statusTransaction ==:status AND responseCode39 ==:code AND cardIndicator ==:indicator AND :uid < uid")
    List<TransactionModelEntity> getSuccessAfterRunningTransaction(int status, String code, String indicator, int uid);

    @Query("SELECT * FROM TransactionModelEntity WHERE statusTransaction ==:status AND responseCode39 ==:code AND additionalDataNational47 ==:indicator AND safEntity == :saf AND :uid < uid")
    List<TransactionModelEntity> getSuccessAfterRunningTransaction(int status, String code, String indicator, boolean saf, int uid);

    @Query("SELECT * FROM TransactionModelEntity WHERE statusTransaction ==:status AND responseCode39 ==:code AND additionalDataNational47 ==:indicator AND nameTransactionTag == :transaction AND :uid < uid")
    List<TransactionModelEntity> getSuccessAfterRunningTransaction(int status, String code, String indicator, String transaction, int uid);

    @Query("SELECT * FROM TransactionModelEntity WHERE uid IN (:userIds)")
    List<TransactionModelEntity> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM TransactionModelEntity WHERE mti0 LIKE :mti0 AND "
            + "primaryAccNo2 LIKE :primaryAccNo2 LIMIT 1")
    TransactionModelEntity findByName(String mti0, String primaryAccNo2);

    @Insert
    void insertAll(TransactionModelEntity... transactionHistories);


    // Retrive a single Transaction
    @Query("SELECT * FROM TransactionModelEntity WHERE retriRefNo37 == :id")
    TransactionModelEntity getTransaction(String id);

    // Retrive a single Transaction
    @Query("SELECT * FROM TransactionModelEntity WHERE uid == :uuid")
    TransactionModelEntity getTransaction(int uuid);

    // Retrive a last Transaction
    @Query("SELECT * FROM TransactionModelEntity WHERE safEntity == :bool ORDER BY uid DESC LIMIT 1")
    TransactionModelEntity getLastTransaction(boolean bool);

    // Retrive a last Transaction
    @Query("SELECT * FROM TransactionModelEntity WHERE safEntity == :bool AND mti0 != :mti ORDER BY uid DESC LIMIT 1")
    TransactionModelEntity getLastTransaction(boolean bool, String mti);

    // Retrive a last Transaction
    @Query("SELECT uid FROM TransactionModelEntity ORDER BY uid DESC LIMIT 1")
    int getLastTransactionUid();

    // Insert a Single Transaction
    @Insert
    void insertTransaction(TransactionModelEntity modelEntity);

    //Update Start Time Transaction
    @Query("UPDATE TransactionModelEntity SET startTimeTransaction = :date WHERE uid == :id")
    void updateStartTime(int id, String date);

    //Update Start Time Transaction
    @Query("UPDATE TransactionModelEntity SET endTimeTransaction = :date WHERE uid == :id")
    void updateEndTime(int id, String date);

    //Update Connection Start Time
    @Query("UPDATE TransactionModelEntity SET startTimeConnection = :date WHERE uid == :id")
    void updateConnectionStartTime(int id, String date);

    //Update Connection Start Time
    @Query("UPDATE TransactionModelEntity SET endTimeConnection = :date WHERE uid == :id")
    void updateConnectionEndTime(int id, String date);

    //Fetch Previous Transaction Time
    @Query("SELECT startTimeTransaction , endTimeTransaction , startTimeConnection, endTimeConnection , retriRefNo37 FROM TransactionModelEntity WHERE statusTransaction ==:status AND (responseCode39 == :id OR responseCode39 == :id1 OR responseCode39 == :id2 OR responseCode39 == :id3 OR responseCode39 == :id4 ) ORDER BY uid DESC LIMIT 1")
    Tag62RelatedTuple getLastTimeTransaction(int status, String id, String id1, String id2, String id3, String id4);

    //Fetch Previous Transaction
    @Query("SELECT * FROM TransactionModelEntity WHERE statusTransaction == :status AND (responseCode39 == :success1 OR responseCode39 == :success2 OR responseCode39 == :success3 OR responseCode39 == :success4 OR responseCode39 == :success5 ) AND (nameTransactionTag == :trans1 OR nameTransactionTag == :trans2 OR nameTransactionTag == :trans3 OR nameTransactionTag == :trans4 OR nameTransactionTag == :trans5 OR nameTransactionTag == :trans6 OR nameTransactionTag == :trans7 OR nameTransactionTag == :trans8 OR nameTransactionTag == :trans9) ORDER BY uid DESC LIMIT 1")
    TransactionModelEntity getLastSuccessTransaction(int status, String success1, String success2, String success3, String success4, String success5, String trans1
            , String trans2, String trans3, String trans4, String trans5, String trans6, String trans7, String trans8, String trans9);

    //Fetch rrn
    @Query("SELECT authIdResCode38 FROM TransactionModelEntity WHERE retriRefNo37 == :rrn")
    String getAuthIdResCode38(String rrn);

    //Fetch rrn
    @Query("SELECT responseCode39 FROM TransactionModelEntity WHERE retriRefNo37 == :rrn")
    String getResponseCode39(String rrn);

    // Update the existing Transaction
    @Update()
    int updateTransaction(TransactionModelEntity modelEntity);

    @Query("DELETE FROM TransactionModelEntity WHERE systemTraceAuditnumber11 = :rrn37")
    void deleteSAFWithSTAN(String rrn37);

    @Delete
    void delete(TransactionModelEntity transactionModelEntity);

    @Query("UPDATE TransactionModelEntity SET additionalDataNational47= :data47, authIdResCode38 = :data38,responseCode39 = :data39 WHERE uid == :id")
    void updateResponse(int id, String data47, String data38, String data39);

    @Query("UPDATE TransactionModelEntity SET additionalDataNational47= :data47,responseCode39 = :data39 WHERE uid == :id")
    void updateResponse(int id, String data47, String data39);

    @Query("UPDATE TransactionModelEntity SET reservedData62Responce= :data62 WHERE uid == :id")
    void update62Response(int id, String data62);

    @Query("UPDATE TransactionModelEntity SET responseData44= :data62 WHERE uid == :id")
    void update44Response(int id, String data62);

    @Query("UPDATE TransactionModelEntity SET statusTransaction = :status WHERE uid == :id")
    void updateResponse(int id, int status);

    @Query("UPDATE TransactionModelEntity SET iccCardSystemRelatedData55_final = :status WHERE uid == :id")
    void updateData55(int id, String status);

    @Query("UPDATE TransactionModelEntity SET responseCode39 = :status WHERE uid == :id")
    void updateResponse39(int id, String status);

    @Query("SELECT uid,iccCardSystemRelatedData55,retriRefNo37,responseCode39,transmissionDateTime7,amtTransaction4,authIdResCode38,startTimeTransaction FROM TransactionModelEntity WHERE nameTransactionTag != :data")
    List<TransactionHistoryTuple> loadAllTransactions(String data);

    @Query("DELETE FROM TransactionModelEntity")
    public void nukeTable();

   /* @Query("SELECT * FROM TransactionModelEntity WHERE :id < uid ")
    public List<TransactionHistoryTuple> getSnapshotData(int id);*/

    @Query("SELECT * FROM TransactionModelEntity WHERE statusTransaction ==:status AND responseCode39 ==:code")
    List<TransactionModelEntity> getSuccessTransaction(int status, String code);

    @Query("SELECT * FROM TransactionModelEntity WHERE statusTransaction ==:status AND responseCode39 ==:code AND additionalDataNational47 ==:indicator AND nameTransactionTag == :transaction AND safEntity == :saf")
    List<TransactionModelEntity> getSuccessTransaction(int status, String code, String indicator, String transaction, boolean saf);

    // Retrive a last Transaction
    @Query("SELECT * FROM TransactionModelEntity WHERE modeTransaction == :type OR modeTransaction == :type1 ORDER BY uid DESC LIMIT 1")
    TransactionModelEntity getLastEMVTransaction(String type, String type1);

    @Query("UPDATE TransactionModelEntity SET status_mportal = :status , request_mportal = :req WHERE systemTraceAuditnumber11 = :rrn37")
    void updateSAFMerchantPortal(String rrn37,boolean status,String req);

    @Query("UPDATE TransactionModelEntity SET request_mportal = :status WHERE systemTraceAuditnumber11 == :rrn37")
    void updateSAFMerchantPortalRequest(String rrn37,String status);

    @Query("SELECT * FROM TransactionModelEntity WHERE status_mportal == :status AND request_mportal != :rrn37")
    TransactionModelEntity getMPortalRequest(String rrn37,boolean status);

    @Query("SELECT * FROM TransactionModelEntity WHERE status_mportal == :status AND request_mportal != :rrn37")
    List<TransactionModelEntity> getPendingMPortalRequest(String rrn37,boolean status);

}
