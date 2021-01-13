package com.tarang.dpq2.model;

public class ReconcileSetupModel {
    private boolean reconcileStatus;
    private String reconcileTime;
    private boolean reconcileToggle;

    public ReconcileSetupModel(boolean reconcileStatus, String reconcileTime, boolean reconcileToggle) {
        this.reconcileStatus = reconcileStatus;
        this.reconcileTime = reconcileTime;
        this.reconcileToggle = reconcileToggle;
    }

    public ReconcileSetupModel(){

    }


    public boolean getReconcileStatus() {
        return reconcileStatus;
    }

    public void setReconcileStatus(boolean reconcileStatus) {
        this.reconcileStatus = reconcileStatus;
    }

    public String getReconcileTime() {
        return reconcileTime;
    }

    public void setReconcileTime(String reconcileTime) {
        this.reconcileTime = reconcileTime;
    }

    public boolean getReconcileToggle() {
        return reconcileToggle;
    }

    public void setReconcileToggle(boolean reconcileToggle) {
        this.reconcileToggle = reconcileToggle;
    }
}
