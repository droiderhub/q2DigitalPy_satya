package com.tarang.dpq2.tms;

import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.room_database.db.AppDatabase;
import com.tarang.dpq2.base.room_database.db.entity.TMSAIDdataModelEntity;

import java.util.List;

public class AIDDataParser {

    public static void parseSegment1(List<String> data, AppDatabase db) {
        TMSAIDdataModelEntity model = new TMSAIDdataModelEntity();
        model.setAid(data.get(0).substring(3).trim());
        model.setAidLable(data.get(1).trim());
        model.setAidTerminalVersionNumber(data.get(2).trim());
        model.setExactOnlySelection(data.get(3).equals("1"));
        model.setSkipEMVProcess(data.get(4).equals("1"));
        model.setDefaultTDOL(data.get(5).trim());
        model.setDefaultDDOL(data.get(6).trim());
        model.setEmvAdditionalTag(data.get(7).trim());
        model.setDenialActionCode(data.get(8).trim());
        model.setOnlineActionCode(data.get(9).trim());
        model.setDefaultActionCode(data.get(10).trim());
        model.setTrsholdValue(data.get(11).trim());
        model.setTargetPercentage(data.get(12).trim());
        model.setMaxTargetPercentage(data.get(13).trim());
        AppManager.getInstance().setTerminalAIDVersionNumber(model.getAid(),model.getAidTerminalVersionNumber());
        db.getAIDDataDao().insertAIDData(model);
    }
}
