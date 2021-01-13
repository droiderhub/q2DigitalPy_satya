package com.tarang.dpq2.tms;

import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.room_database.db.AppDatabase;
import com.tarang.dpq2.base.room_database.db.entity.TMSCardSchemeEntity;

import java.util.List;

public class CardSchemeParser {

    public static void parseSegment1(List<String> data, AppDatabase db, boolean partialDownload) {
        TMSCardSchemeEntity model;
        if (partialDownload) {
            model = db.getTMSCardSchemeDao().getCardSchemeData(data.get(0).substring(3));
            if(model == null)
                model = new TMSCardSchemeEntity();
        }else
            model = new TMSCardSchemeEntity();
        model.setCardIndicator(data.get(0).substring(3));
        model.setCardNameArabic(data.get(1));
        model.setCardNameEnglish(data.get(2));
        model.setCardSchemeID(data.get(3));
        model.setMerchantCategoryCode(data.get(4));
        model.setMerchantID(data.get(5));
        model.setTerminalID(data.get(6));
        model.setEmvEnabled(data.get(7).equals("1"));
        model.setServiceCodeEnabled(data.get(8).equals("1"));
        model.setOfflineRefundEnabled(data.get(9).equals("1"));
        AppManager.getInstance().setRefundOfflineEnabled(model.getCardIndicator(),model.isOfflineRefundEnabled());
        AppManager.getInstance().setMerchantCode(model.getCardIndicator(),model.getMerchantCategoryCode());
        Logger.v("DE26-----"+model.getCardIndicator(),model.getMerchantCategoryCode());
        db.getTMSCardSchemeDao().insertCardScheme(model);
    }

    public static void parseSegment2(List<String> data, AppDatabase db) {
        TMSCardSchemeEntity model = db.getTMSCardSchemeDao().getCardSchemeData(data.get(0).substring(3));
        model.setTransactionAllowed(data.get(1));
        model.setCardHolderAuth(data.get(2));
        model.setSupervisorFunctions(data.get(3));
        model.setManualEntryEnabled(data.get(4));
        model.setFloorLimitEnabled(data.get(5).equals("1"));
        model.setTerminalFloorLimit(data.get(6)+"00");
       // model.setTerminalFloorLimit("800"+"00");
        //Logger.v("terminal_floor_limit_set_hardcoded_2000");
        model.setTerminalFloorLimitFallback(data.get(7)+"00");
        model.setMaximumCashback(data.get(8)+"00");
        model.setMaxTransactionAmtIndicator(data.get(9));
        model.setMaxTransactionAmt(data.get(10)+"00");
        model.setLuhnCheckEnabled(data.get(11).equals("1"));
        model.setExpiryDataPosition(data.get(12));
        model.setDelayCallSetup(data.get(13));
        Logger.v("FloorLimit -"+model.toString());
        AppManager.getInstance().setFllorLimit(model.getCardIndicator(),model.getTerminalFloorLimit());
        AppManager.getInstance().setTransactionAmountEnabled(model.getCardIndicator(),model.getMaxTransactionAmtIndicator());
        AppManager.getInstance().setMaxCashBackAmount(model.getCardIndicator(),model.getMaximumCashback());
        AppManager.getInstance().setMaxAmount(model.getCardIndicator(),model.getMaxTransactionAmt());
        AppManager.getInstance().setFllorLimitEnabled(model.getCardIndicator(),model.isFloorLimitEnabled());
        db.getTMSCardSchemeDao().updateCardScheme(model);
    }

    public static void parseSegment3(List<String> data, AppDatabase db) {
        Logger.v("BinRange --" + data.get(0).substring(3) + " -- " + data.get(1) + " -- " + data.get(2));
        db.getTMSCardSchemeDao().updateBindRangers(data.get(0).substring(3), data.get(1), data.get(2));
    }
}
