/*
package com.tarang.dpq2.base;

import android.content.Context;
import android.newland.telephony.ApnEntity;
import android.newland.telephony.ApnUtils;
import android.telephony.data.ApnSetting;

import java.util.ArrayList;
import java.util.List;

public class ApnModule {

    Context context;
    ApnUtils apnUtils;

    public ApnModule(Context context) {
        this.context = context;
        apnUtils = new ApnSetting(context);
    }

    public List<ApnSetting> getAllAPIList() {
        if (AppManager.getInstance().getAPNStatus()) {
            if (addApnNode(apnUtils.getCurrentApnList().get(0))) {
                AppManager.getInstance().setAPNStatus();
                return getOnlyThree(apnUtils);
            } else {
                return getOnlyThree(apnUtils);
            }
        } else {
            return getOnlyThree(apnUtils);
        }
    }

    private List<ApnEntity> getOnlyThree(ApnUtils apnUtils) {
        List<ApnEntity> active = new ArrayList<>();
        List<String> curnt = new ArrayList<>();
        curnt.add("mobily");
        curnt.add("sky_band");
        curnt.add("stc");
        List<ApnEntity> curentList = apnUtils.getCurrentApnList();
        for (int i = 0; i < curentList.size(); i++) {
            String name = curentList.get(i).getName().toLowerCase();
            if (curnt.contains(name)) {
                curnt.remove(name);
                active.add(curentList.get(i));
            }
        }
        return active;
    }

    public void removeAllAPN() {
        List<ApnEntity> list = apnUtils.getAllApnList();
        for (ApnEntity myList : list)
            apnUtils.removeApn(myList.getId());
    }

    public boolean addApnNode(ApnEntity old) {
        ApnEntity apnEntity = new ApnEntity();
        apnEntity.setName("MOBILY");
        apnEntity.setApn("POS-M2M");
        apnEntity.setMcc(old.getMcc());
        apnEntity.setMnc(old.getMnc());
        int id = apnUtils.addNewApn(apnEntity);
        Logger.v("apnUtils -" + id);
        if (id != -1) {
            return addApnNode1(old);
        } else
            return false;
    }

    public boolean saveApnNode(String name, String apn, String mcc, String mnc) {
//        ApnEntity old = apnUtils.getAllApnList().get(0);
        ApnEntity apnEntity = new ApnEntity();
        apnEntity.setName(name);
        apnEntity.setApn(apn);
        apnEntity.setMcc(mcc);
        apnEntity.setMnc(mnc);

//        if(name.equalsIgnoreCase("MOBILY")) {
//            apnEntity.setMcc("420");
//            apnEntity.setMnc("03");
//        }else {
//            apnEntity.setMcc("420");
//            apnEntity.setMnc("01");
//        }
//        removeAllAPN();
        int id = apnUtils.addNewApn(apnEntity);
        Logger.v("apnUtils -" + id);
        if (id != -1) {
            Logger.v("apnEntity.getId(1) -"+apnEntity.getId());
            int def = apnUtils.setDefault(id);
            Logger.v("DEFF -"+def);
            if (def != -1)
                return true;
            else
                return false;
        } else
            return false;
    }

    public boolean addApnNode1(ApnEntity old) {
        ApnEntity apnEntity = new ApnEntity();
        apnEntity.setName("STC");
        apnEntity.setApn("POS.M2M");
        apnEntity.setMcc(old.getMcc());
        apnEntity.setMnc(old.getMnc());
        int id = apnUtils.addNewApn(apnEntity);
        Logger.v("apnUtils -" + id);
        if (id != -1) {
            return addApnNode2(old);
        } else
            return false;
    }

    public boolean addApnNode2(ApnEntity old) {
        ApnEntity apnEntity = new ApnEntity();
        apnEntity.setName("SKY_BAND");
        apnEntity.setApn("IBS");
        apnEntity.setMcc(old.getMcc());
        apnEntity.setMnc(old.getMnc());
        int id = apnUtils.addNewApn(apnEntity);
        Logger.v("apnUtils -" + id);
        return id != -1;
    }

    public void setSelectedAPN(int id) {
        apnUtils.setDefault(id);
        ApnEntity current = apnUtils.getPreferApn();
        Logger.v("CURREnt --" + current.getName());
    }

}
*/
