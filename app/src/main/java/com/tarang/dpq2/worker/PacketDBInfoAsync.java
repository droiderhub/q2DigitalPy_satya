package com.tarang.dpq2.worker;

import android.content.Context;
import android.os.AsyncTask;

import androidx.work.ListenableWorker;

import com.tarang.dpq2.R;
import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.MapperFlow;
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
import com.tarang.dpq2.viewmodel.TransactionViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.tarang.dpq2.base.terminal_sdk.AppConfig.EMV.TAG55;
import static com.tarang.dpq2.base.terminal_sdk.AppConfig.EMV.ic55Data;


public class PacketDBInfoAsync extends AsyncTask<Void, Void, Boolean> {
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
    public static boolean REFUND_OFFLINE_ENABLED ;
    public static boolean REFUND_STATUS = false;
    public static String DE_38_DB = "DE_38_DB";
    public static String DE_39_DB = "DE_39_DB";
    public static String DE_124 = "DE_124";
    public static String TRANSACTION_PENDING = "TRANSACTION_PENDING";
    public static boolean INVALID_CARD = false;
    AppDatabase database;
    Context context;
    String transactionType;
    String cardType;
    String cardNumber;
    PacketDBInfoListener packetDBInfoListener;



    public PacketDBInfoAsync(Context context,String transactionType, String cardType,String cardNumber,PacketDBInfoListener packetDBInfoListener){
        this.context = context;
        this.transactionType = transactionType;
        this.cardType = cardType;
        this.cardNumber = cardNumber;
        this.packetDBInfoListener = packetDBInfoListener;
        database = AppDatabase.getInstance(AppManager.getContext().getApplicationContext());
        Logger.v("PacketDBInfoAsync 1");

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        Logger.v("packetDbInfoResult -"+aBoolean);

        if (packetDBInfoListener != null)
            packetDBInfoListener.packetDbInfoResult(aBoolean);

        Logger.v("CARD_SCHEME----"+CARD_SCHEME);
        Logger.v("RetriRefNo37----"+RetriRefNo37);
        Logger.v("MSCARD---"+MSCARD);
        Logger.v("MANUAL_ENTRY----"+MANUAL_ENTRY);
        Logger.v("TRANSACTION_ALLOWED----"+TRANSACTION_ALLOWED);
        Logger.v("MANUAL_ENTRY_ALLOWED---"+MANUAL_ENTRY_ALLOWED);
        Logger.v("CARD_VERIFICATION----"+CARD_VERIFICATION);
        Logger.v("CARD_SCHEME_ID----"+CARD_SCHEME_ID);
        Logger.v("REFUND_OFFLINE_ENABLED----"+REFUND_OFFLINE_ENABLED);
        Logger.v("REFUND_STATUS----"+REFUND_STATUS);
        Logger.v("DE_38_DB----"+DE_38_DB);
        Logger.v("DE_39_DB----"+DE_39_DB);
        Logger.v("DE_124----"+DE_124);
        Logger.v("INVALID_CARD---"+INVALID_CARD);
        Logger.v("packetDbInfoResult_packetDbInfoResult");

        if (aBoolean) {
            Logger.v("PacketDBInfoAsync_success");
        } else {
            Logger.v("PacketDBInfoAsync_failed");

        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        Logger.v("PacketDBInfoAsync DoWork");
        Tag62RelatedTuple dataset = database.getTransactionDao().getLastTimeTransaction(1, "000","001","003","007","400");
        TransactionModelDao transactionModelDao = database.getTransactionDao();
//        String tansType = getInputData().getString(TRANSACTION_TYPE);
        String tansType = transactionType;
        Logger.v("tansType -" + tansType);
        if (tansType.equalsIgnoreCase(ConstantApp.LAST_EMV)) {
            TransactionModelEntity lastTrans = database.getTransactionDao().getLastEMVTransaction(ConstantAppValue.DIPPED, ConstantAppValue.CONTACTLESS);
            if(lastTrans != null && lastTrans.getIccCardSystemRelatedData55().trim().length() != 0) {
                DE_124= Utils.decrypt( lastTrans.getIccCardSystemRelatedData55());
                return true;
            }else
                return false;
        } else if (tansType.equalsIgnoreCase(ConstantApp.REGISTRATION) || tansType.equalsIgnoreCase(ConstantApp.FILEACTION) || tansType.equalsIgnoreCase(ConstantApp.FORMAT_FILESYS)) {
            List<TransactionModelEntity> transaction = database.getTransactionDao().getSuccessTransaction(1,"000");
            if(transaction.size() != 0)
                return true;
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
                DE_124= data124.toString();
            } else {
                DE_124= null;
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
                    CARD_SCHEME_ID= dataCard;
                    if (AppManager.getInstance().getReversalStatus(successTransaction.getUid())) {
                        Logger.v("true 1");
                        REFUND_STATUS= true;
                    }else {
                        Logger.v("false 1");
                        REFUND_STATUS= false;
                    }
                } else {
                    Logger.v("false 2");
                    REFUND_STATUS= false;
                }
            } else {
                Logger.v("false 3");
                REFUND_STATUS= false;
            }

        } else {
            TMSCardSchemeDao cardSchemeDao = database.getTMSCardSchemeDao();
            String aid = ""; //ma,p1 etc
            if(cardType != null && cardType.equalsIgnoreCase(MANUAL_ENTRY)){
                CARD_SCHEME_ID= "";
                if (dataset != null) {
                   RetriRefNo37= dataset.getRetriRefNo37();
                } else {
                    RetriRefNo37= "000000000000";
                }
                return true;
            }else if (cardType!= null && cardType.equalsIgnoreCase(MSCARD)) {
                String binRange = cardNumber;
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
                INVALID_CARD=true;
                return true;
            }
            TMSCardSchemeEntity cardData = cardSchemeDao.getCardSchemeData(aid);
            Logger.v("cardData --" + cardData.toString());
            Logger.v("CV Validation --" + cardData.getCardHolderAuth());
            Logger.v("getTransactionAllowed --" + cardData.getTransactionAllowed());
            Logger.v("getManualEntryEnabled --" + cardData.getManualEntryEnabled());
            CARD_VERIFICATION= cardData.getCardHolderAuth();
            TRANSACTION_ALLOWED= cardData.getTransactionAllowed();
            MANUAL_ENTRY_ALLOWED= cardData.getManualEntryEnabled();

            CARD_SCHEME= aid;
            Logger.v("CARD_SCHEME_aid----"+aid);
            // CARD_SCHEME = aid;
            if (tansType.equalsIgnoreCase(ConstantApp.REFUND)) {
                boolean offlineRefunEnabled = cardSchemeDao.getOfflineRefunEnabled(aid);
                REFUND_OFFLINE_ENABLED= offlineRefunEnabled;
                if (offlineRefunEnabled) {
                    DE_38_DB= transactionModelDao.getAuthIdResCode38(AppManager.getInstance().getDe37());
                    DE_39_DB= transactionModelDao.getResponseCode39(AppManager.getInstance().getDe37());
                }
            }
            String dataCard = cardSchemeDao.getCardSchemeID(aid);
            CARD_SCHEME_ID= dataCard;
        }

        if (dataset != null) {
//            data.putString(StartTimeConnection, dataset.getStartTimeConnection());
//            data.putString(StartTimeTransaction, dataset.getStartTimeTransaction());
//            data.putString(EndTimeConnection, dataset.getEndTimeConnection());
//            data.putString(EndTimeTransaction, dataset.getEndTimeTransaction());
            RetriRefNo37= dataset.getRetriRefNo37();
        } else {
//            data.putString(StartTimeConnection, "00000000");
//            data.putString(StartTimeTransaction, "00000000");
//            data.putString(EndTimeConnection, "00000000");
//            data.putString(EndTimeTransaction, "00000000");
            RetriRefNo37= "000000000000";
        }
        return true;
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

}



