package com.tarang.dpq2.worker;

import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.jpos_class.ConstantApp;
import com.tarang.dpq2.base.jpos_class.ConstantAppValue;
import com.tarang.dpq2.base.room_database.db.AppDatabase;
import com.tarang.dpq2.base.room_database.db.dao.TMSCardSchemeDao;
import com.tarang.dpq2.base.room_database.db.dao.TransactionModelDao;
import com.tarang.dpq2.base.room_database.db.entity.TMSCardSchemeEntity;
import com.tarang.dpq2.base.room_database.db.entity.TransactionModelEntity;
import com.tarang.dpq2.base.room_database.db.tuple.BinRangeTuple;
import com.tarang.dpq2.base.room_database.db.tuple.Tag62RelatedTuple;
import com.tarang.dpq2.base.utilities.Utils;
import com.tarang.dpq2.model.DeviceSpecificModel;
import com.wizarpos.emvsample.constant.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.tarang.dpq2.base.terminal_sdk.AppConfig.EMV.TAG55;
import static com.tarang.dpq2.base.terminal_sdk.AppConfig.EMV.ic55Data;

/* Packet validation with TMS database data before All transaction */
public class PacketDBInfoWorker extends Worker {

    public static String RetriRefNo37 = "RetriRefNo37";
    public static String CARD_TYPE = "card_type";
    public static String CARD_NUMBER = "CARD_NUMBER";
    public static String TRANSACTION_TYPE = "TRANSACTION_TYPE";
    public static String MSCARD = "MSCARD";
    public static String MANUAL_ENTRY = "MANUAL_ENTRY";
    public static String ICRFCARD = "ICRFCARD";
    public static String CARD_SCHEME = "CARD_SCHEME";
    public static String TRANSACTION_ALLOWED = "TRANSACTION_ALLOWED";
    public static String MANUAL_ENTRY_ALLOWED = "MANUAL_ENTRY_ALLOWED";
    public static String CARD_VERIFICATION = "CARD_VERIFICATION";
    public static String CARD_SCHEME_ID = "CARD_SCHEME_ID";
    public static String REFUND_OFFLINE_ENABLED = "REFUND_OFFLINE_ENABLED";
    public static String REFUND_STATUS = "REFUND_OFFLINE_ENABLED";
    public static String DE_38_DB = "DE_38_DB";
    public static String DE_39_DB = "DE_39_DB";
    public static String DE_124 = "DE_124";
    public static String TRANSACTION_PENDING = "TRANSACTION_PENDING";
    public static String INVALID_CARD = "INVALID_CARD";
    AppDatabase database;
    Data.Builder data = new Data.Builder();

    public PacketDBInfoWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        database = AppDatabase.getInstance(context.getApplicationContext());
        Logger.v("PacketDBInfoWorker 1");
    }

    @NonNull
    @Override
    public Result doWork() {
        Logger.v("PacketDBInfoWorker DoWork");
        Tag62RelatedTuple dataset = database.getTransactionDao().getLastTimeTransaction(1, "000","001","003","007","400");
        TransactionModelDao transactionModelDao = database.getTransactionDao();
        String tansType = getInputData().getString(TRANSACTION_TYPE);
        Logger.v("tansType -" + tansType);
        if (tansType.equalsIgnoreCase(ConstantApp.LAST_EMV)) {
            TransactionModelEntity lastTrans = database.getTransactionDao().getLastEMVTransaction(ConstantAppValue.DIPPED, ConstantAppValue.CONTACTLESS);
            if(lastTrans != null && lastTrans.getIccCardSystemRelatedData55().trim().length() != 0) {
                data.putString(DE_124, Utils.decrypt( lastTrans.getIccCardSystemRelatedData55()));
                return Result.success(data.build());
            }else
                return Result.failure();
        } else if (tansType.equalsIgnoreCase(ConstantApp.REGISTRATION) || tansType.equalsIgnoreCase(ConstantApp.FILEACTION) || tansType.equalsIgnoreCase(ConstantApp.FORMAT_FILESYS)) {
            List<TransactionModelEntity> transaction = database.getTransactionDao().getSuccessTransaction(1,"000");
            if(transaction.size() != 0)
                data.putBoolean(TRANSACTION_PENDING,true);
        } else if (tansType.equalsIgnoreCase(ConstantApp.RECONCILIATION)) {
            TMSCardSchemeDao cardSchemeDao = database.getTMSCardSchemeDao();
            List<TMSCardSchemeEntity> cardSchemeData = cardSchemeDao.getCardSchemeData();
            StringBuilder data124 = new StringBuilder();
            int count = 0;
            for (int i = 0; i < cardSchemeData.size(); i++) {
                String localData124 = getDebitCount(cardSchemeData.get(i).getCardSchemeID(), cardSchemeData.get(i).getCardIndicator(), database);
                if (localData124.trim().length() != 0) {
                    count = count + 1;
                    data124.append(localData124);
                }
            }
            if (count != 0) {
                data124.insert(0, String.format("%02d", count));
                Logger.v("data124 --" + data124);
                data.putString(DE_124, data124.toString());
            } else {
                data.putString(DE_124, null);
            }

        } else if (tansType.equalsIgnoreCase(ConstantApp.PURCHASE_REVERSAL)) {
            TransactionModelEntity successTransaction;
            if (AppManager.getInstance().isDebugEnabled()) {
                successTransaction = AppManager.getInstance().getDebugTransactionModelEntity();
            } else if (AppManager.getInstance().isReversalManual())
                successTransaction = database.getTransactionDao().getLastSuccessTransaction(1,"000","001","003","007","800"
                        , ConstantApp.PURCHASE, ConstantApp.PURCHASE_NAQD, ConstantApp.REFUND
                        , ConstantApp.PRE_AUTHORISATION, ConstantApp.PRE_AUTHORISATION_VOID, ConstantApp.PRE_AUTHORISATION_EXTENSION
                        , ConstantApp.PURCHASE_ADVICE_FULL, ConstantApp.PURCHASE_ADVICE_PARTIAL, ConstantApp.PURCHASE_ADVICE_MANUAL);
            else
                successTransaction = database.getTransactionDao().getLastTransaction(false);
            if (successTransaction != null) {
                Logger.v("successTransaction --" + successTransaction.toString());
                AppManager.getInstance().setTransactionModelEntity(successTransaction);
                if (Utils.getDateDifference(successTransaction.getEndTimeConnection())) {
                    TMSCardSchemeDao cardSchemeDao = database.getTMSCardSchemeDao();
                    String dataCard = cardSchemeDao.getCardSchemeID(successTransaction.getCardIndicator());
                    data.putString(CARD_SCHEME_ID, dataCard);
                    if (AppManager.getInstance().getReversalStatus(successTransaction.getUid())) {
                        Logger.v("true 1");
                        data.putBoolean(REFUND_STATUS, true);
                    }else {
                        Logger.v("false 1");
                        data.putBoolean(REFUND_STATUS, false);
                    }
                } else {
                    Logger.v("false 2");
                    data.putBoolean(REFUND_STATUS, false);
                }
            } else {
                Logger.v("false 3");
                data.putBoolean(REFUND_STATUS, false);
            }

        } else {
            TMSCardSchemeDao cardSchemeDao = database.getTMSCardSchemeDao();
            String aid = ""; //ma,p1 etc
            if(getInputData().getString(CARD_TYPE) != null && getInputData().getString(CARD_TYPE).equalsIgnoreCase(MANUAL_ENTRY)){
                data.putString(CARD_SCHEME_ID, "");
                if (dataset != null) {
                    data.putString(RetriRefNo37, dataset.getRetriRefNo37());
                } else {
                    data.putString(RetriRefNo37, "000000000000");
                }
                return Result.success(data.build());
            }else if (getInputData().getString(CARD_TYPE) != null && getInputData().getString(CARD_TYPE).equalsIgnoreCase(MSCARD)) {
                String binRange = getInputData().getString(CARD_NUMBER);
                List<BinRangeTuple> binRangesMada = cardSchemeDao.getSpecificBinRanges(ConstantAppValue.A0000002281010.toUpperCase(), ConstantAppValue.A0000002281010.toLowerCase());
                List<BinRangeTuple> commonBinCUP = new ArrayList<>(); // Common CUP bin
                List<BinRangeTuple> chinaUnionPayBin = cardSchemeDao.getSpecificBinRanges(ConstantAppValue.A000000333010101.toUpperCase(), ConstantAppValue.A000000333010101.toLowerCase());
                List<BinRangeTuple> binRanges = cardSchemeDao.getAllBinRanges(ConstantAppValue.A0000002281010.toUpperCase(), ConstantAppValue.A0000002281010.toLowerCase()
                        , ConstantAppValue.A000000333010101.toUpperCase(), ConstantAppValue.A000000333010101.toLowerCase());

                aid = Utils.getCardFromBinRange(binRange, binRangesMada);
                //
                if(aid.trim().length() == 0) {
                    aid = Utils.getCardFromBinRange(binRange, commonBinCUP);
                }
                if(aid.trim().length() == 0) {
                    aid = Utils.getCardFromBinRange(binRange, binRanges);
                }
                if(aid.trim().length() == 0) {
                    aid = Utils.getCardFromBinRange(binRange, chinaUnionPayBin);
                }
                if(aid.trim().length() == 0){
                    aid = Utils.getCardFromBinRangeF(binRange, binRangesMada);
                }
                if(aid.trim().length() == 0){
                    aid = Utils.getCardFromBinRangeF(binRange, commonBinCUP);
                }
                if(aid.trim().length() == 0){
                    aid = Utils.getCardFromBinRangeF(binRange, binRanges);
                }
                if(aid.trim().length() == 0){
                    aid = Utils.getCardFromBinRangeF(binRange, chinaUnionPayBin);
                }
                Logger.v("aid -1--" + binRange);
            } else {
                if (ic55Data != null && ic55Data.trim().length() != 0) {
                    HashMap<String, String> tag55 = Utils.getParsedTag55(ic55Data);
                    aid = tag55.get(TAG55[19]);
                    aid = Utils.fetchIndicatorFromAID(aid);
                }
            }

            Logger.v("aid ---" + aid);
            if(aid.trim().length() ==0){
                Logger.v("Invalid card");
                data.putBoolean(INVALID_CARD,true);
                return Result.failure(data.build());
            }
            TMSCardSchemeEntity cardData = cardSchemeDao.getCardSchemeData(aid);
            Logger.v("cardData --" + cardData.toString());
            Logger.v("CV Validation --" + cardData.getCardHolderAuth());
            Logger.v("getTransactionAllowed --" + cardData.getTransactionAllowed());
            Logger.v("getManualEntryEnabled --" + cardData.getManualEntryEnabled());
            data.putString(CARD_VERIFICATION, cardData.getCardHolderAuth());
            data.putString(TRANSACTION_ALLOWED, cardData.getTransactionAllowed());
            data.putString(MANUAL_ENTRY_ALLOWED, cardData.getManualEntryEnabled());

            data.putString(CARD_SCHEME, aid);
            Logger.v("CARD_SCHEME_aid----"+aid);
           // CARD_SCHEME = aid;
            if (tansType.equalsIgnoreCase(ConstantApp.REFUND)) {
                boolean offlineRefunEnabled = cardSchemeDao.getOfflineRefunEnabled(aid);
                data.putBoolean(REFUND_OFFLINE_ENABLED, offlineRefunEnabled);
                if (offlineRefunEnabled) {
                    data.putString(DE_38_DB, transactionModelDao.getAuthIdResCode38(AppManager.getInstance().getDe37()));
                    data.putString(DE_39_DB, transactionModelDao.getResponseCode39(AppManager.getInstance().getDe37()));
                }
            }
            String dataCard = cardSchemeDao.getCardSchemeID(aid);
            data.putString(CARD_SCHEME_ID, dataCard);
        }

        if (dataset != null) {
//            data.putString(StartTimeConnection, dataset.getStartTimeConnection());
//            data.putString(StartTimeTransaction, dataset.getStartTimeTransaction());
//            data.putString(EndTimeConnection, dataset.getEndTimeConnection());
//            data.putString(EndTimeTransaction, dataset.getEndTimeTransaction());
            data.putString(RetriRefNo37, dataset.getRetriRefNo37());
        } else {
//            data.putString(StartTimeConnection, "00000000");
//            data.putString(StartTimeTransaction, "00000000");
//            data.putString(EndTimeConnection, "00000000");
//            data.putString(EndTimeTransaction, "00000000");
            data.putString(RetriRefNo37, "000000000000");
        }


        return Result.success(data.build());
    }

    private String getDebitCount(String cardSchemeID, String cardIndicator, AppDatabase database) {
        String data = "";
        long credit = 0, debit = 0, creditAmount = 0, debitAmount = 0, cashBack = 0, cashAdvance = 0, authCount = 0;
        List<TransactionModelEntity> transaction = database.getTransactionDao().getSuccessTransaction("400", 1, "000", cardSchemeID + cardIndicator, "001", "003", "007");
        Logger.v("Count -1-" + transaction.size());
        for (int i = 0; i < transaction.size(); i++) {
            TransactionModelEntity trans = transaction.get(i);
            Logger.v("trans -" + trans.toString());
            if (Utils.reconsilationTag(trans.getNameTransactionTag())) {
                if (trans.getNameTransactionTag().equalsIgnoreCase(ConstantApp.REFUND)) {
                    credit = credit + 1;
                    creditAmount = creditAmount + Long.parseLong(trans.getAmtTransaction4());
                } else {
                    debit = debit + 1;
                    debitAmount = debitAmount + Long.parseLong(trans.getAmtTransaction4());
                }
            }
            if (trans.getNameTransactionTag().equalsIgnoreCase(ConstantApp.PURCHASE_REVERSAL) && trans.getPosConditionCode25().equalsIgnoreCase(ConstantAppValue.CUSTOMER_CANCELLATION)) {
                if (!(trans.getProcessingCode3().equalsIgnoreCase(ConstantAppValue.REFUND))) {
                    credit = credit + 1;
                    creditAmount = creditAmount + Long.parseLong(trans.getAmtTransaction4());
                } else {
                    debit = debit + 1;
                    debitAmount = debitAmount + Long.parseLong(trans.getAmtTransaction4());
                }
            }
            if (trans.getNameTransactionTag().equalsIgnoreCase(ConstantApp.CASH_ADVANCE)) {
                long amt = Long.parseLong(trans.getAmtTransaction4());
                cashAdvance = cashAdvance + amt;
            } else if (trans.getNameTransactionTag().equalsIgnoreCase(ConstantApp.PURCHASE_NAQD)) {
                if (trans.getAddlAmt54().trim().length() != 0) {
                    long amt = Long.parseLong(trans.getAddlAmt54().substring(8));
                    cashBack = cashBack + amt;
                }
            }

//            if (trans.getMti0().equalsIgnoreCase(ConstantValue.AUTH) || trans.getMti0().equalsIgnoreCase(ConstantValue.AUTH_ADVISE) || trans.getMti0().equalsIgnoreCase(ConstantValue.AUTH_ADVISE_REPEAT)) {
//                authCount = authCount + 1;
//            }
            if (trans.getModeTransaction().equalsIgnoreCase(ConstantAppValue.DIPPED) || trans.getModeTransaction().equalsIgnoreCase(ConstantAppValue.CONTACTLESS)) {
                if (!Utils.reconsilationAuthTag(trans.getNameTransactionTag())) {
                    if (!(trans.getModeTransaction().equalsIgnoreCase(ConstantApp.PURCHASE) && trans.getMti0().equalsIgnoreCase(ConstantAppValue.FINANCIAL_ADVISE)))
                        authCount = authCount + 1;
                }
            }
        }

        Logger.v("Credit --" + credit + "-Amount-" + creditAmount);
        Logger.v("Debit --" + debit + "-Amount-" + debitAmount);
        Logger.v("Credit --" + credit + "-Amount-" + creditAmount);
        Logger.v("Debit --" + debit + "-Amount-" + debitAmount);
        Logger.v("cashBack --" + cashBack);
        Logger.v("cashAdvance-" + cashAdvance);
        Logger.v("authCount-" + authCount);

//        List<TransactionModelEntity> safTransaction = database.getSAFDao().getAll(cardSchemeID + cardIndicator);
//        Logger.v("Count -2-" + safTransaction.size());
//        for (int i = 0; i < safTransaction.size(); i++) {
//            TransactionModelEntity trans = safTransaction.get(i);
//            if (Utils.reconsilationTag(trans.getNameTransactionTag())) {
//                if (!trans.getNameTransactionTag().equalsIgnoreCase(Constant.REFUND)) {
//                    credit = credit + 1;
//                    creditAmount = creditAmount + Integer.parseInt(trans.getAmtTransaction4());
//                } else {
//                    debit = debit + 1;
//                    debitAmount = debitAmount + Integer.parseInt(trans.getAmtTransaction4());
//                }
//            }
//            if (trans.getNameTransactionTag().equalsIgnoreCase(Constant.CASH_ADVANCE)) {
//                int amt = Integer.parseInt(trans.getAmtTransaction4());
//                cashAdvance = cashAdvance + amt;
//            } else if (trans.getNameTransactionTag().equalsIgnoreCase(Constant.PURCHASE_NAQD)) {
//                int amt = Integer.parseInt(trans.getAddlAmt54());
//                cashBack = cashBack + amt;
//            }
//
//            if (trans.getMti0().equalsIgnoreCase("1110") || trans.getMti0().equalsIgnoreCase("1120")) {
//                authCount = authCount + 1;
//            }
//
//        }
//        if (creditAmount != 0 || debitAmount != 0 || credit != 0 || debit != 0 || cashBack != 0 || cashAdvance != 0 || authCount != 0) {
        String value = String.format("%010d%015d%010d%015d%015d%015d%010d", debit, debitAmount, credit, creditAmount, cashBack, cashAdvance, authCount);
        data = data + cardIndicator + cardSchemeID + value;
        Logger.v(value);
//        }

        return data;
    }
    /*private String getDebitCount(String cardSchemeID, String cardIndicator, AppDatabase database) {
        String data = "";
        long credit = 0, debit = 0, creditAmount = 0, debitAmount = 0, cashBack = 0, cashAdvance = 0, authCount = 0;
        List<TransactionModelEntity> transaction = database.getTransactionDao().getSuccessTransaction("400", 1, "000", cardSchemeID + cardIndicator,"001","003","007");
        Logger.v("Count -1-" + transaction.size());
        for (int i = 0; i < transaction.size(); i++) {
            TransactionModelEntity trans = transaction.get(i);
            Logger.v("trans -" + trans.toString());
            if (Utils.reconsilationTag(trans.getNameTransactionTag())) {
                if (trans.getNameTransactionTag().equalsIgnoreCase(ConstantApp.REFUND)) {
                    credit = credit + 1;
                    creditAmount = creditAmount + Long.parseLong(trans.getAmtTransaction4());
                } else {
                    debit = debit + 1;
                    debitAmount = debitAmount + Long.parseLong(trans.getAmtTransaction4());
                }
            }
            if(trans.getNameTransactionTag().equalsIgnoreCase(ConstantApp.PURCHASE_REVERSAL) && trans.getPosConditionCode25().equalsIgnoreCase(ConstantAppValue.CUSTOMER_CANCELLATION)) {
                if (!(trans.getProcessingCode3().equalsIgnoreCase(ConstantAppValue.REFUND))) {
                    credit = credit + 1;
                    creditAmount = creditAmount + Long.parseLong(trans.getAmtTransaction4());
                } else {
                    debit = debit + 1;
                    debitAmount = debitAmount + Long.parseLong(trans.getAmtTransaction4());
                }
            }
            if (trans.getNameTransactionTag().equalsIgnoreCase(ConstantApp.CASH_ADVANCE)) {
                long amt = Long.parseLong(trans.getAmtTransaction4());
                cashAdvance = cashAdvance + amt;
            } else if (trans.getNameTransactionTag().equalsIgnoreCase(ConstantApp.PURCHASE_NAQD)) {
                if(trans.getAddlAmt54().trim().length() != 0) {
                    long amt = Long.parseLong(trans.getAddlAmt54().substring(8));
                    cashBack = cashBack + amt;
                }
            }

            if (trans.getMti0().equalsIgnoreCase("1110") || trans.getMti0().equalsIgnoreCase("1120")) {
                authCount = authCount + 1;
            }


        }
        Logger.v("Credit --" + credit + "-Amount-" + creditAmount);
        Logger.v("Debit --" + debit + "-Amount-" + debitAmount);

//        List<TransactionModelEntity> safTransaction = database.getSAFDao().getAll(cardSchemeID + cardIndicator);
//        Logger.v("Count -2-" + safTransaction.size());
//        for (int i = 0; i < safTransaction.size(); i++) {
//            TransactionModelEntity trans = safTransaction.get(i);
//            if (Utils.reconsilationTag(trans.getNameTransactionTag())) {
//                if (!trans.getNameTransactionTag().equalsIgnoreCase(Constant.REFUND)) {
//                    credit = credit + 1;
//                    creditAmount = creditAmount + Integer.parseInt(trans.getAmtTransaction4());
//                } else {
//                    debit = debit + 1;
//                    debitAmount = debitAmount + Integer.parseInt(trans.getAmtTransaction4());
//                }
//            }
//            if (trans.getNameTransactionTag().equalsIgnoreCase(Constant.CASH_ADVANCE)) {
//                int amt = Integer.parseInt(trans.getAmtTransaction4());
//                cashAdvance = cashAdvance + amt;
//            } else if (trans.getNameTransactionTag().equalsIgnoreCase(Constant.PURCHASE_NAQD)) {
//                int amt = Integer.parseInt(trans.getAddlAmt54());
//                cashBack = cashBack + amt;
//            }
//
//            if (trans.getMti0().equalsIgnoreCase("1110") || trans.getMti0().equalsIgnoreCase("1120")) {
//                authCount = authCount + 1;
//            }
//
//        }
        Logger.v("Credit --" + credit + "-Amount-" + creditAmount);
        Logger.v("Debit --" + debit + "-Amount-" + debitAmount);
        Logger.v("cashBack --" + cashBack);
        Logger.v("cashAdvance-" + cashAdvance);
        Logger.v("authCount-" + authCount);

        if (creditAmount != 0 || debitAmount != 0) {
            String value = String.format("%010d%015d%010d%015d%015d%015d%010d", debit, debitAmount, credit, creditAmount, cashBack, cashAdvance, authCount);
            data = data + cardIndicator + cardSchemeID + value;
            Logger.v(value);
        }
        return data;
    }*/

    public static boolean getCumilativeAmount(AppDatabase database){
        TMSCardSchemeDao cardSchemeDao = database.getTMSCardSchemeDao();
        List<TMSCardSchemeEntity> cardSchemeData = cardSchemeDao.getCardSchemeData();
        long count = 0;
        long amount = 0;
        for (int k = 0; k < cardSchemeData.size(); k++) {
            String cardSchemeID = cardSchemeData.get(k).getCardSchemeID();
            String cardIndicator = cardSchemeData.get(k).getCardIndicator();
            long credit = 0, debit = 0, creditAmount = 0, debitAmount = 0, cashBack = 0, cashAdvance = 0, authCount = 0;
            List<TransactionModelEntity> transaction = database.getTransactionDao().getSuccessTransaction("400", 1, "000", cardSchemeID + cardIndicator,"001","003","007");
            Logger.v("Count -1-" + transaction.size());
            for (int i = 0; i < transaction.size(); i++) {
                TransactionModelEntity trans = transaction.get(i);
                Logger.v("trans -" + trans.toString());
                if (Utils.reconsilationTag(trans.getNameTransactionTag())) {
                    if (trans.getNameTransactionTag().equalsIgnoreCase(ConstantApp.REFUND)) {
                        credit = credit + 1;
                        creditAmount = creditAmount + Long.parseLong(trans.getAmtTransaction4());
                    } else {
                        debit = debit + 1;
                        debitAmount = debitAmount + Long.parseLong(trans.getAmtTransaction4());
                    }
                }

                if(trans.getNameTransactionTag().equalsIgnoreCase(ConstantApp.PURCHASE_REVERSAL) && trans.getPosConditionCode25().equalsIgnoreCase(ConstantAppValue.CUSTOMER_CANCELLATION)) {
                    if (!(trans.getProcessingCode3().equalsIgnoreCase(ConstantAppValue.REFUND))) {
                        credit = credit + 1;
                        creditAmount = creditAmount + Long.parseLong(trans.getAmtTransaction4());
                    } else {
                        debit = debit + 1;
                        debitAmount = debitAmount + Long.parseLong(trans.getAmtTransaction4());
                    }
                }

                if (trans.getNameTransactionTag().equalsIgnoreCase(ConstantApp.CASH_ADVANCE)) {
                    long amt = Long.parseLong(trans.getAmtTransaction4());
                    cashAdvance = cashAdvance + amt;
                } else if (trans.getNameTransactionTag().equalsIgnoreCase(ConstantApp.PURCHASE_NAQD)) {
                    if(trans.getAddlAmt54().trim().length() != 0) {
                        long amt = Long.parseLong(trans.getAddlAmt54().substring(8));
                        cashBack = cashBack + amt;
                    }
                }

                if (trans.getMti0().equalsIgnoreCase("1110") || trans.getMti0().equalsIgnoreCase("1120")) {
                    authCount = authCount + 1;
                }


            }

            Logger.v("Credit --" + credit + "-Amount-" + creditAmount);
            Logger.v("Debit --" + debit + "-Amount-" + debitAmount);
            Logger.v("cashBack --" + cashBack);
            Logger.v("cashAdvance-" + cashAdvance);
            Logger.v("authCount-" + authCount);
            count = count + credit+debit+authCount;
            amount = amount+creditAmount+debitAmount+cashBack+cashAdvance;
        }

        List<TransactionModelEntity> safLiat = database.getSAFDao().getAllSuccess(ConstantAppValue.SAF_APPROVED, ConstantAppValue.SAF_APPROVED_UNABLE);
        for (int i=0;i<safLiat.size();i++){
            TransactionModelEntity trans = safLiat.get(i);
            count = count + 1;
            long amt = Long.parseLong(trans.getAmtTransaction4());
            amount = amount + amt;
        }

        DeviceSpecificModel deviceSpecificModel1 = AppManager.getInstance().getDeviceSpecificModel();
        if(deviceSpecificModel1 != null) {
            String maxAmount = deviceSpecificModel1.getMaxReconciliationAmount();
            String maxCount = deviceSpecificModel1.getMaxTransactionsProcessed();
            Logger.v("Max Amount --"+maxAmount);
            Logger.v("Max Amount --"+maxCount);
            if(maxAmount != null && maxAmount.trim().length() != 0 && Long.parseLong(maxAmount) != 0){
                if(Long.parseLong(maxAmount) <= amount){
                    return true;
                }
            }

            if(maxCount != null && maxCount.trim().length() != 0 && Long.parseLong(maxCount) != 0){
                if(Long.parseLong(maxCount) <= count){
                    return true;
                }
            }
        }
        return false;
    }
}
