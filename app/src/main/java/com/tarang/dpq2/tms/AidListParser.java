package com.tarang.dpq2.tms;

import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.Logger;

import java.util.ArrayList;
import java.util.List;

public class AidListParser {

    public static void toSaveAidListDetails(List<String> fsList){
        List<String> aidList = new ArrayList<>();
        String aid1 = fsList.get(0).substring(3);
        Logger.v("fsList -0-"+fsList.get(0));
        Logger.v("fsList -0-"+fsList.get(0).substring(3));

        aidList.add(aid1);
        for (int i = 1; i < fsList.size(); i++){
            if(fsList.get(i).trim().length() != 0) {
                Logger.v("fsList --" + fsList.get(i));
                aidList.add(fsList.get(i));
            }
        }
//        AIDListModel aidListModel = new AIDListModel();
//        aidListModel.setAid1(fsList.get(0).substring(3));
//        aidListModel.setAid2(fsList.get(1));
//        aidListModel.setAid3(fsList.get(2));
//        aidListModel.setAid4(fsList.get(3));
//        aidListModel.setAid5(fsList.get(4));
//        aidListModel.setAid6(fsList.get(5));
//        aidListModel.setAid7(fsList.get(6));
//        aidListModel.setAid8(fsList.get(7));
//        aidListModel.setAid9(fsList.get(8));
//        aidListModel.setAid10(fsList.get(9));

//        ArrayList<String> aidList = fsList;

        AppManager.getInstance().setAidListDetails(aidList);

        List<String> aidList1 = AppManager.getInstance().getAidList();

        Logger.v("De72 aid list: "+ aidList1.toString());

    }
}
