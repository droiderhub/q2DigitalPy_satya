package com.tarang.dpq2.worker;

import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;

import androidx.annotation.RequiresApi;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.jpos_class.ConstantApp;
import com.tarang.dpq2.base.room_database.db.AppDatabase;
import com.tarang.dpq2.base.room_database.db.entity.SAFModelEntity;
import com.tarang.dpq2.base.room_database.db.entity.TransactionModelEntity;
import com.tarang.dpq2.base.utilities.Utils;

import static com.tarang.dpq2.base.terminal_sdk.AppConfig.EMV.reqObj;
/* Storing Transaction into Database */
public class InsertTransactionWorker extends Worker {

    AppDatabase database;
    public static String EXPIRY_DATE = "EXPIRY_DATE";
    public static String SAF_REQUEST = "SAF_REQUEST";


    public InsertTransactionWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.database = AppDatabase.getInstance(context.getApplicationContext());
    }

    @NonNull
    @Override
    public Result doWork() {
        String date = getInputData().getString(EXPIRY_DATE);
        Logger.v("Expiry Date --" + getInputData().getBoolean(SAF_REQUEST, false));
        Logger.v("Expiry Date --" + date);
        Logger.v("Expiry Date --" + date.trim().length());
        AppManager.getInstance().setLastTransaction(getInputData().getBoolean(SAF_REQUEST, false));
        AppManager.getInstance().setBoolean(ConstantApp.DELETE_HISTORY,false);

        if (getInputData().getBoolean(SAF_REQUEST, false)) {
            SAFModelEntity model = new SAFModelEntity();
            model.setNameTransactionTag(reqObj.getNameTransactionTag());
            model.setCardIndicator(reqObj.getCardIndicator());
            model.setKernalID(reqObj.getSetKernalID());
            model.setModeTransaction(reqObj.getModeTransaction());
            model.setMti0(reqObj.getMti0());
            model.setTsii(reqObj.getTsi());
            model.setPrimaryAccNo2(Utils.encrypt( reqObj.getPrimaryAccNo2()));
            model.setProcessingCode3(reqObj.getProcessingCode3());
            model.setAmtTransaction4(reqObj.getAmtTransaction4());
            model.setTransmissionDateTime7(reqObj.getTransmissionDateTime7());
            model.setSystemTraceAuditnumber11(reqObj.getSystemTraceAuditnumber11());
            model.setTimeLocalTransaction12(reqObj.getTimeLocalTransaction12());
            if (date != null) {
                if (date.trim().length() == 5) {
                    model.setDateExpiration14(Utils.encrypt( date));
                } else if (date.trim().length() == 4) {
                    model.setDateExpiration14(Utils.encrypt((getInputData().getString(EXPIRY_DATE).substring(2) + "/" + getInputData().getString(EXPIRY_DATE).substring(0, 2))));
                } else if (date.trim().length() != 0)
                    model.setDateExpiration14(Utils.encrypt((getInputData().getString(EXPIRY_DATE).substring(2, 4) + "/" + getInputData().getString(EXPIRY_DATE).substring(0, 2))));  // TODO A90saty
            }
            model.setPosEntrymode22(reqObj.getPosEntrymode22());
            model.setCardSequenceNumber23(reqObj.getCardSequenceNumber23());
            model.setFunctioncode24(reqObj.getFunctioncode24());
            model.setPosConditionCode25(reqObj.getMessageReasonCode25());
            model.setPosPinCaptureCode26(reqObj.getCardAcceptorBusinessCode26());
            model.setAmtTransFee28(reqObj.getReconsilationDate28());
            model.setAmtTranProcessingFee30(reqObj.getAmtTranProcessingFee30());
            model.setAccuringInsituteIdCode32(reqObj.getAccuringInsituteIdCode32());
            model.setTrack2Data35(Utils.encrypt( reqObj.getTrack2Data35()));
            model.setRetriRefNo37(reqObj.getRetriRefNo37());
            model.setAuthIdResCode38(reqObj.getAuthIdResCode38());
            model.setResponseCode39(reqObj.getResponseCode39());
            model.setCardAcceptorTemId41(reqObj.getCardAcceptorTemId41());
            model.setCardAcceptorIdCode42(reqObj.getCardAcceptorIdCode42());
            model.setAdditionalDataNational47(reqObj.getAdditionalDataNational47());
            model.setAdditionalDataPrivate48(reqObj.getAdditionalDataPrivate48());
            model.setCurrCodeTransaction49(reqObj.getCurrCodeTransaction49());
            model.setCurrCodeStatleMent50(reqObj.getCurrCodeStatleMent50());
            model.setSecRelatedContInfo53(reqObj.getSecRelatedContInfo53());
            model.setAddlAmt54(reqObj.getAddlAmt54());
            model.setIccCardSystemRelatedData55(Utils.encrypt( reqObj.getIccCardSystemRelatedData55()));
            model.setIccCardSystemRelatedData55_final(Utils.encrypt( reqObj.getIccCardSystemRelatedData55()));
            model.setMsgReasonCode56(reqObj.getMsgReasonCode56());
            model.setEchoData59(reqObj.getEchoData59());
            model.setReservedData62(reqObj.getReservedData62());
            model.setReservedData62Responce(reqObj.getReservedData62Responce());
            model.setMessageAuthenticationCodeField64(reqObj.getMessageAuthenticationCodeField64());
            model.setDataRecord72(reqObj.getDataRecord72());
            model.setReservedData124(reqObj.getReservedData124());
            model.setMacExt128(reqObj.getMacExt128());
            model.setStartTimeConnection(Utils.getCurrentDate());
            model.setStartTimeTransaction(Utils.getCurrentDate());
            Logger.v("SAF Insert");
            database.getSAFDao().insertTransaction(model);
            AppManager.getInstance().setDuplicateTransactionModelEntity(model);
            return Result.success();
        } else {
            TransactionModelEntity model = new TransactionModelEntity();
            model.setNameTransactionTag(reqObj.getNameTransactionTag());
            model.setCardIndicator(reqObj.getCardIndicator());
            model.setKernalID(reqObj.getSetKernalID());
            model.setModeTransaction(reqObj.getModeTransaction());
            model.setMti0(reqObj.getMti0());
            model.setTsii(reqObj.getTsi());
            model.setPrimaryAccNo2(Utils.encrypt( reqObj.getPrimaryAccNo2()));
            model.setProcessingCode3(reqObj.getProcessingCode3());
            model.setAmtTransaction4(reqObj.getAmtTransaction4());
            model.setTransmissionDateTime7(reqObj.getTransmissionDateTime7());
            model.setSystemTraceAuditnumber11(reqObj.getSystemTraceAuditnumber11());
            model.setTimeLocalTransaction12(reqObj.getTimeLocalTransaction12());
            if (date != null) {
                if (date.trim().length() == 5) {
                    model.setDateExpiration14(Utils.encrypt( date));
                } else if (date.trim().length() == 4) {
                    model.setDateExpiration14(Utils.encrypt((getInputData().getString(EXPIRY_DATE).substring(2) + "/" + getInputData().getString(EXPIRY_DATE).substring(0, 2))));
                } else if (date.trim().length() != 0)
                    model.setDateExpiration14(Utils.encrypt((getInputData().getString(EXPIRY_DATE).substring(2, 4) + "/" + getInputData().getString(EXPIRY_DATE).substring(0, 2))));
            }
            model.setPosEntrymode22(reqObj.getPosEntrymode22());
            model.setCardSequenceNumber23(reqObj.getCardSequenceNumber23());
            model.setFunctioncode24(reqObj.getFunctioncode24());
            model.setPosConditionCode25(reqObj.getMessageReasonCode25());
            model.setPosPinCaptureCode26(reqObj.getCardAcceptorBusinessCode26());
            model.setAmtTransFee28(reqObj.getReconsilationDate28());
            model.setAmtTranProcessingFee30(reqObj.getAmtTranProcessingFee30());
            model.setAccuringInsituteIdCode32(reqObj.getAccuringInsituteIdCode32());
            model.setTrack2Data35(Utils.encrypt( reqObj.getTrack2Data35()));
            model.setRetriRefNo37(reqObj.getRetriRefNo37());
            model.setAuthIdResCode38(reqObj.getAuthIdResCode38());
            model.setResponseCode39(reqObj.getResponseCode39());
            model.setCardAcceptorTemId41(reqObj.getCardAcceptorTemId41());
            model.setCardAcceptorIdCode42(reqObj.getCardAcceptorIdCode42());
            model.setAdditionalDataNational47(reqObj.getAdditionalDataNational47());
            model.setAdditionalDataPrivate48(reqObj.getAdditionalDataPrivate48());
            model.setCurrCodeTransaction49(reqObj.getCurrCodeTransaction49());
            model.setCurrCodeStatleMent50(reqObj.getCurrCodeStatleMent50());
            model.setSecRelatedContInfo53(reqObj.getSecRelatedContInfo53());
            model.setAddlAmt54(reqObj.getAddlAmt54());
            model.setIccCardSystemRelatedData55(Utils.encrypt( reqObj.getIccCardSystemRelatedData55()));
            model.setMsgReasonCode56(reqObj.getMsgReasonCode56());
            model.setEchoData59(reqObj.getEchoData59());
            model.setReservedData62(reqObj.getReservedData62());
            model.setReservedData62Responce(reqObj.getReservedData62Responce());
            model.setMessageAuthenticationCodeField64(reqObj.getMessageAuthenticationCodeField64());
            model.setDataRecord72(reqObj.getDataRecord72());
            model.setReservedData124(reqObj.getReservedData124());
            model.setMacExt128(reqObj.getMacExt128());
            model.setSaf(false);
            Logger.v("Transaction Insert");
            database.getTransactionDao().insertTransaction(model);
            return Result.success();
        }

    }
}
