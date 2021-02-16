package com.tarang.dpq2.worker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.cloudpos.printer.PrinterDevice;
import com.tarang.dpq2.R;
import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.jpos_class.ByteConversionUtils;
import com.tarang.dpq2.base.jpos_class.ConstantApp;
import com.tarang.dpq2.base.jpos_class.ConstantAppValue;
import com.tarang.dpq2.base.terminal_sdk.AppConfig;
import com.tarang.dpq2.base.room_database.db.AppDatabase;
import com.tarang.dpq2.base.room_database.db.entity.TMSAIDdataModelEntity;
import com.tarang.dpq2.base.room_database.db.entity.TMSCardSchemeEntity;
import com.tarang.dpq2.base.room_database.db.entity.TMSMessageTextModelEntity;
import com.tarang.dpq2.base.room_database.db.entity.TMSPublicKeyModelEntity;
import com.tarang.dpq2.base.room_database.db.entity.TransactionModelEntity;
import com.tarang.dpq2.base.terminal_sdk.device.SDKDevice;
import com.tarang.dpq2.base.terminal_sdk.event.SimpleTransferListener;
import com.tarang.dpq2.base.terminal_sdk.utils.PrinterReceipt;
import com.tarang.dpq2.base.utilities.Utils;
import com.tarang.dpq2.isopacket.IsoRequest;
import com.tarang.dpq2.model.DeviceSpecificModel;
import com.tarang.dpq2.model.KeyValueModel;
import com.tarang.dpq2.model.PrinterModel;
import com.tarang.dpq2.model.RetailerDataModel;
import com.tarang.dpq2.model.TerminalConnectionGPRSModel;
import com.tarang.dpq2.model.TerminalConnectionWifiModel;
import com.tarang.dpq2.viewmodel.TransactionViewModel;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.tarang.dpq2.base.terminal_sdk.AppConfig.EMV.TAG55;
import static com.tarang.dpq2.worker.PacketDBInfoWorker.getCumilativeAmount;

public class PrinterWorker extends Worker {

    private final Context context;
    private final String DASH = "****************************************";
    public static List<KeyValueModel> modelList = new ArrayList<>();
    public static PrinterModel printerModel = new PrinterModel();
    private final Bitmap logoBitmap;
    public int uid;
    public static String SAF_REQUEST = "SAF_REQUEST";
    public static String CANCELLED_REQUEST = "CANCELLED_REQUEST";
    public static String RECONSILATION_REQUEST = "RECONSILATION_REQUEST";
    public static String RECONSILATION_REQUEST_DUPLICATE = "RECONSILATION_REQUEST_DUPLICATE";
    public static String RUNNING_TOTAL = "RUNNING_TOTAL";
    public static String SNAPSHOT_TOTAL = "SNAPSHOT_TOTAL";
    public static String UID_LAST_TRANSACTION = "UID_LAST_TRANSACTION";
    public static String DUPLICATE_PRINT = "DUPLICATE_PRINT";
    public static String KEY_VALUE_PRINT = "KEY_VALUE_PRINT";
    public static String KEY_VALUE_DATA = "KEY_VALUE_DATA";
    public static String STATUS = "STATUS";
    public static boolean DO_RECON_AMOUNT = false;
    public static boolean DO_PARTIAL_DOWNLOAD = false;
    public static boolean RECON_PRINTED = false;
    public static boolean RECON_PRINTED_DUB = false;
    public static String printDe30;
    public static boolean DO_SAF_NOW = false;

    AppDatabase database;
    PrinterDevice devicePrinter;

    private Data.Builder data = new Data.Builder();


    public PrinterWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        DO_RECON_AMOUNT = false;
        this.context = context;
        this.database = AppDatabase.getInstance(context.getApplicationContext());
//        mPrinter = SDKDevice.getInstance(context).getPrinter();
//        arial = mPrinter.getFontsPath(context, "arial.ttf", true);
//        arialBold = mPrinter.getFontsPath(context, "ARIALBD.TTF", true);
//        arabic = mPrinter.getFontsPath(context, "cour.ttf", true);
//        arabicBold = mPrinter.getFontsPath(context, "courbd.ttf", true);
//        simsun = mPrinter.getFontsPath(context, "simsun.ttc", true);
        uid = AppManager.getInstance().getSnapshotID();
        logoBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.mada_logo_black);
        devicePrinter = SDKDevice.getInstance(context).getPrinter();
        printerModel = new PrinterModel();
    }

    @NonNull
    @Override
    public Result doWork() {

        if (getInputData().getBoolean(KEY_VALUE_PRINT, false)) {
            Logger.v("Recon");
            modelList.clear();
            modelList = new ArrayList<>();
            String dataset = getInputData().getString(KEY_VALUE_DATA);
            Logger.v("dataset --" + dataset);
            if (dataset.equalsIgnoreCase(ConstantApp.TMS_RETAILER_DATA)) {
                RetailerDataModel retailerDataModel = AppManager.getInstance().getRetailerDataModel();
                modelList.add(new KeyValueModel(ConstantAppValue.doted_line));
                modelList.add(new KeyValueModel("RETAILER PARAMETER"));
                modelList.add(new KeyValueModel(ConstantAppValue.doted_line));
                modelList.add(new KeyValueModel(context.getString(R.string.reconsilation_time), retailerDataModel.getReconcillationTime()));
                modelList.add(new KeyValueModel(context.getString(R.string.retailer_name_arabic), Utils.changeArabic(retailerDataModel.getRetailerNameInArabic())));
                modelList.add(new KeyValueModel(context.getString(R.string.retiler_name_english), retailerDataModel.getRetailerNameEnglish()));
                modelList.add(new KeyValueModel(context.getString(R.string.retailer_number), AppManager.getInstance().getString(ConstantApp.SPRM_PHONE_NUMBER)));
                modelList.add(new KeyValueModel(context.getString(R.string.retailer_address), retailerDataModel.getRetailerAddress1English()));
            } else if (dataset.equalsIgnoreCase(ConstantApp.TMS_CARD_DATA)) {
                List<TMSCardSchemeEntity> dataList = database.getTMSCardSchemeDao().getCardSchemeData();
                modelList.add(new KeyValueModel(ConstantAppValue.doted_line));
                modelList.add(new KeyValueModel("CARD DATA"));
                modelList.add(new KeyValueModel(ConstantAppValue.doted_line));
                modelList.add(new KeyValueModel(context.getString(R.string.terminal_id), AppManager.getInstance().getCardAcceptorID41()));
                modelList.add(new KeyValueModel(ConstantAppValue.doted_line));

                for (int i = 0; i < dataList.size(); i++) {
                    modelList.add(new KeyValueModel(context.getString(R.string.no_) + i));
                    modelList.add(new KeyValueModel(context.getString(R.string.card_scheme_id), dataList.get(i).getCardIndicator()));
                    modelList.add(new KeyValueModel(context.getString(R.string.card_schem_name_english), dataList.get(i).getCardNameEnglish()));
                    modelList.add(new KeyValueModel(context.getString(R.string.card_scheme_name_arabic), Utils.changeArabic(dataList.get(i).getCardNameArabic())));
                    modelList.add(new KeyValueModel(context.getString(R.string.card_scheme_acquire_id), dataList.get(i).getCardSchemeID()));
                    modelList.add(new KeyValueModel("Merchant Code", dataList.get(i).getMerchantCategoryCode()));
                    modelList.add(new KeyValueModel("Merchant Id", dataList.get(i).getMerchantID()));
                    modelList.add(new KeyValueModel("Terminal Id", dataList.get(i).getTerminalID()));
                    modelList.add(new KeyValueModel("EMV Enabled", String.valueOf(dataList.get(i).isEmvEnabled())));
                    modelList.add(new KeyValueModel("Service Code",String.valueOf(dataList.get(i).isServiceCodeEnabled())));
                    modelList.add(new KeyValueModel(context.getString(R.string.terminal_floor_limit), dataList.get(i).getTerminalFloorLimit()));
                    modelList.add(new KeyValueModel("Floor Limit Enabled",String.valueOf(dataList.get(i).isFloorLimitEnabled())));
                    modelList.add(new KeyValueModel("Forced Offline", "" + (dataList.get(i).isOfflineRefundEnabled())));
                    modelList.add(new KeyValueModel("Max Cashback Allowed",dataList.get(i).getMaximumCashback()));
                    modelList.add(new KeyValueModel("Max Amount Allowed",dataList.get(i).getMaxTransactionAmt()));
                    modelList.add(new KeyValueModel("MOD Check", "" + dataList.get(i).isLuhnCheckEnabled()));
                    modelList.add(new KeyValueModel("Delay Call setup", "" + dataList.get(i).getDelayCallSetup()));

                    modelList.add(new KeyValueModel("TRANSACTION PERMISSION"));
                    modelList.add(new KeyValueModel("-----------"));
                    modelList.add(new KeyValueModel("     ALOW PIN SIGN SPR MAN ACHK"));
                    modelList.add(new KeyValueModel("PUR    " + getValue(dataList.get(i).getTransactionAllowed(), ConstantApp.PURCHASE) + "   "
                            + getValuePin(dataList.get(i).getCardHolderAuth(), ConstantApp.PURCHASE) + "    "
                            + getValueSign(dataList.get(i).getCardHolderAuth(), ConstantApp.PURCHASE) + "   "
                            + getValue(dataList.get(i).getSupervisorFunctions(), ConstantApp.PURCHASE) + "   "
                            + getValue(dataList.get(i).getManualEntryEnabled(), ConstantApp.PURCHASE)
                            + "    " + getValue(dataList.get(i).getMaxTransactionAmtIndicator(), ConstantApp.PURCHASE) + " "));
                    modelList.add(new KeyValueModel("CSBK   " + getValue(dataList.get(i).getTransactionAllowed(), ConstantApp.PURCHASE_NAQD) + "   "
                            + getValuePin(dataList.get(i).getCardHolderAuth(), ConstantApp.PURCHASE_NAQD) + "    "
                            + getValueSign(dataList.get(i).getCardHolderAuth(), ConstantApp.PURCHASE_NAQD) + "   "
                            + getValue(dataList.get(i).getSupervisorFunctions(), ConstantApp.PURCHASE_NAQD) + "   "
                            + getValue(dataList.get(i).getManualEntryEnabled(), ConstantApp.PURCHASE_NAQD) + "    "
                            + getValue(dataList.get(i).getMaxTransactionAmtIndicator(), ConstantApp.PURCHASE_NAQD) + " "));
                    modelList.add(new KeyValueModel("ADV    " + getValue(dataList.get(i).getTransactionAllowed(), ConstantApp.CASH_ADVANCE) + "   "
                            + getValuePin(dataList.get(i).getCardHolderAuth(), ConstantApp.CASH_ADVANCE) + "    "
                            + getValueSign(dataList.get(i).getCardHolderAuth(), ConstantApp.CASH_ADVANCE) + "   "
                            + getValue(dataList.get(i).getSupervisorFunctions(), ConstantApp.CASH_ADVANCE) + "   "
                            + getValue(dataList.get(i).getManualEntryEnabled(), ConstantApp.CASH_ADVANCE) + "    "
                            + getValue(dataList.get(i).getMaxTransactionAmtIndicator(), ConstantApp.CASH_ADVANCE) + " "));
                    modelList.add(new KeyValueModel("RFND   " + getValue(dataList.get(i).getTransactionAllowed(), ConstantApp.REFUND) + "   "
                            + getValuePin(dataList.get(i).getCardHolderAuth(), ConstantApp.REFUND) + "    "
                            + getValueSign(dataList.get(i).getCardHolderAuth(), ConstantApp.REFUND) + "   "
                            + getValue(dataList.get(i).getSupervisorFunctions(), ConstantApp.REFUND) + "   "
                            + getValue(dataList.get(i).getManualEntryEnabled(), ConstantApp.REFUND) + "    "
                            + getValue(dataList.get(i).getMaxTransactionAmtIndicator(), ConstantApp.REFUND) + " "));
//                    modelList.add(new KeyValueModel("CSAD   "+getValue(dataList.get(i).getTransactionAllowed(),Constant.CASH_ADVANCE)+"   "+getValue("",Constant.CASH_ADVANCE)+"    "+getValue("",Constant.CASH_ADVANCE)+"   "+getValue("",Constant.PURCHASE)+"   "+getValue("",Constant.PURCHASE)+"    "+getValue("",Constant.PURCHASE)+" "));
                    modelList.add(new KeyValueModel("PADV   " + getValue(dataList.get(i).getTransactionAllowed(), ConstantApp.PURCHASE_ADVICE) + "   "
                            + getValuePin(dataList.get(i).getCardHolderAuth(), ConstantApp.PURCHASE_ADVICE) + "    "
                            + getValueSign(dataList.get(i).getCardHolderAuth(), ConstantApp.PURCHASE_ADVICE) + "   "
                            + getValue(dataList.get(i).getSupervisorFunctions(), ConstantApp.PURCHASE_ADVICE) + "   "
                            + getValue(dataList.get(i).getManualEntryEnabled(), ConstantApp.PURCHASE_ADVICE) + "    "
                            + getValue(dataList.get(i).getMaxTransactionAmtIndicator(), ConstantApp.PURCHASE_ADVICE) + " "));
                    modelList.add(new KeyValueModel("PAUT   " + getValue(dataList.get(i).getTransactionAllowed(), ConstantApp.PRE_AUTHORISATION) + "   "
                            + getValuePin(dataList.get(i).getCardHolderAuth(), ConstantApp.PRE_AUTHORISATION) + "    "
                            + getValueSign(dataList.get(i).getCardHolderAuth(), ConstantApp.PRE_AUTHORISATION) + "   "
                            + getValue(dataList.get(i).getSupervisorFunctions(), ConstantApp.PRE_AUTHORISATION) + "   "
                            + getValue(dataList.get(i).getManualEntryEnabled(), ConstantApp.PRE_AUTHORISATION) + "    "
                            + getValue(dataList.get(i).getMaxTransactionAmtIndicator(), ConstantApp.PRE_AUTHORISATION) + " "));
                    modelList.add(new KeyValueModel("PAUV   " + getValue(dataList.get(i).getTransactionAllowed(), ConstantApp.PRE_AUTHORISATION_VOID) + "   "
                            + getValuePin(dataList.get(i).getCardHolderAuth(), ConstantApp.PRE_AUTHORISATION_VOID) + "    "
                            + getValueSign(dataList.get(i).getCardHolderAuth(), ConstantApp.PRE_AUTHORISATION_VOID) + "   "
                            + getValue(dataList.get(i).getSupervisorFunctions(), ConstantApp.PRE_AUTHORISATION_VOID) + "   "
                            + getValue(dataList.get(i).getManualEntryEnabled(), ConstantApp.PRE_AUTHORISATION_VOID) + "    "
                            + getValue(dataList.get(i).getMaxTransactionAmtIndicator(), ConstantApp.PRE_AUTHORISATION_VOID) + " "));
                    modelList.add(new KeyValueModel("PAUE   " + getValue(dataList.get(i).getTransactionAllowed(), ConstantApp.PRE_AUTHORISATION_EXTENSION) + "   "
                            + getValuePin(dataList.get(i).getCardHolderAuth(), ConstantApp.PRE_AUTHORISATION_EXTENSION) + "    "
                            + getValueSign(dataList.get(i).getCardHolderAuth(), ConstantApp.PRE_AUTHORISATION_EXTENSION) + "   "
                            + getValue(dataList.get(i).getSupervisorFunctions(), ConstantApp.PRE_AUTHORISATION_EXTENSION) + "   "
                            + getValue(dataList.get(i).getManualEntryEnabled(), ConstantApp.PRE_AUTHORISATION_EXTENSION) + "    "
                            + getValue(dataList.get(i).getMaxTransactionAmtIndicator(), ConstantApp.PRE_AUTHORISATION_EXTENSION) + " "));
                    modelList.add(new KeyValueModel("-----------"));
                    modelList.add(new KeyValueModel(context.getString(R.string.card_bin_range), dataList.get(i).getBinRanges()));
                }
            } else if (dataset.equalsIgnoreCase(ConstantApp.TMS_HOST_MSG_DATA)) {
                List<TMSMessageTextModelEntity> dataList = database.getTMSMessageTextDao().getTerminalMessageList();
                modelList.add(new KeyValueModel(ConstantAppValue.doted_line));
                modelList.add(new KeyValueModel("HOST MSG DATA"));
                modelList.add(new KeyValueModel(ConstantAppValue.doted_line));
                modelList.add(new KeyValueModel(context.getString(R.string.terminal_id), AppManager.getInstance().getCardAcceptorID41()));
                for (int i = 0; i < dataList.size(); i++) {
                    modelList.add(new KeyValueModel(context.getString(R.string.no_) + i));
                    modelList.add(new KeyValueModel(context.getString(R.string.display_code), dataList.get(i).getDisplayCode()));
                    modelList.add(new KeyValueModel(context.getString(R.string.message_code), dataList.get(i).getMessageCode()));
                    modelList.add(new KeyValueModel(context.getString(R.string.english_text), dataList.get(i).getEnglishMessageText()));
                    modelList.add(new KeyValueModel(context.getString(R.string.arabic_text), Utils.changeArabic(dataList.get(i).getArabicMessageText())));
                }
            } else if (dataset.equalsIgnoreCase(ConstantApp.TMS_PUB_FEY_DATA)) {
                List<TMSPublicKeyModelEntity> dataList = database.getTMSPublicKeyModelDao().getCapKeys();
                modelList.add(new KeyValueModel(ConstantAppValue.doted_line));
                modelList.add(new KeyValueModel("PUB KEY DATA"));
                modelList.add(new KeyValueModel(ConstantAppValue.doted_line));
                for (int i = 0; i < dataList.size(); i++) {
                    modelList.add(new KeyValueModel(context.getString(R.string.no_) + i));
                    modelList.add(new KeyValueModel(context.getString(R.string.key_idex), "" + Integer.parseInt(dataList.get(i).getKeyIndex(), 16)));
                    modelList.add(new KeyValueModel(context.getString(R.string.rid), dataList.get(i).getRID()));
                }
            } else if (dataset.equalsIgnoreCase(ConstantApp.TMS_COMM_DATA)) {
                TerminalConnectionGPRSModel terminalConnectionGPRSModel1 = AppManager.getInstance().getTerminalConnectionGPRSModel();
                modelList.add(new KeyValueModel(ConstantAppValue.doted_line));
                modelList.add(new KeyValueModel("COMM DATA"));
                modelList.add(new KeyValueModel(ConstantAppValue.doted_line));
                modelList.add(new KeyValueModel(context.getString(R.string.terminal_id), AppManager.getInstance().getCardAcceptorID41()));
                if (terminalConnectionGPRSModel1 != null) {
                    modelList.add(new KeyValueModel(context.getString(R.string.connection_type_gprs)));
                    modelList.add(new KeyValueModel(terminalConnectionGPRSModel1.toString()));
                }
                TerminalConnectionWifiModel terminalConnectionWifiModel1 = AppManager.getInstance().getTerminalConnectionWifiModel();
                if (terminalConnectionWifiModel1 != null) {
                    modelList.add(new KeyValueModel(context.getString(R.string.connection_type_wifi)));
                    modelList.add(new KeyValueModel(terminalConnectionWifiModel1.toString()));
                }
            } else if (dataset.equalsIgnoreCase(ConstantApp.TMS_AID_LIST)) {
                modelList.add(new KeyValueModel(ConstantAppValue.doted_line));
                modelList.add(new KeyValueModel("AID LIST"));
                modelList.add(new KeyValueModel(ConstantAppValue.doted_line));
                List<String> aidList1 = AppManager.getInstance().getAidList();
                for (int i = 0; i < aidList1.size(); i++) {
                    if (aidList1.get(i).trim().length() != 0) {
                        modelList.add(new KeyValueModel(context.getString(R.string.aid_) + (modelList.size()), aidList1.get(i)));
                    }
                }
            } else if (dataset.equalsIgnoreCase(ConstantApp.TMS_AID_DATA)) {
                List<TMSAIDdataModelEntity> dataList = database.getAIDDataDao().getAllAIDData();
                modelList.add(new KeyValueModel(ConstantAppValue.doted_line));
                modelList.add(new KeyValueModel("AID DATA"));
                modelList.add(new KeyValueModel(ConstantAppValue.doted_line));
                for (int i = 0; i < dataList.size(); i++) {
                    modelList.add(new KeyValueModel(context.getString(R.string.no_) + i));
                    modelList.add(new KeyValueModel(context.getString(R.string.aid), dataList.get(i).getAid()));
                    modelList.add(new KeyValueModel(context.getString(R.string.aid_label), dataList.get(i).getAidLable()));
                    modelList.add(new KeyValueModel(context.getString(R.string.terminal_aid_version), dataList.get(i).getAidTerminalVersionNumber()));
//                    modelList.add(new KeyValueModel("Exact Only Selection", dataList.get(i).getArabicMessageText()));
//                    modelList.add(new KeyValueModel("Skip EMV Processing", dataList.get(i).get()));
                    modelList.add(new KeyValueModel(context.getString(R.string.default_tdol), dataList.get(i).getDefaultTDOL()));
                    modelList.add(new KeyValueModel(context.getString(R.string.online_action), dataList.get(i).getOnlineActionCode()));
                    modelList.add(new KeyValueModel(context.getString(R.string.default_action), dataList.get(i).getDefaultActionCode()));
                    modelList.add(new KeyValueModel(context.getString(R.string.denial_action), dataList.get(i).getDenialActionCode()));
                    modelList.add(new KeyValueModel(context.getString(R.string.ddol), dataList.get(i).getDefaultDDOL()));
                }
            } else if (dataset.equalsIgnoreCase(ConstantApp.TMS_DEVICE_SPEC)) {
                modelList.add(new KeyValueModel(ConstantAppValue.doted_line));
                modelList.add(new KeyValueModel("DEVICE SPEC"));
                modelList.add(new KeyValueModel(ConstantAppValue.doted_line));
                DeviceSpecificModel deviceSpecificModel1 = AppManager.getInstance().getDeviceSpecificModel();
                if (deviceSpecificModel1 != null) {
                    modelList.add(new KeyValueModel(context.getString(R.string.terminal_id), AppManager.getInstance().getCardAcceptorID41()));
                    modelList.add(new KeyValueModel("Terminal Type :", AppManager.getInstance().getVendorTerminalType()));
                    modelList.add(new KeyValueModel(context.getString(R.string.card_scheme_mada)));
                    modelList.add(new KeyValueModel(context.getString(R.string.txt_limit), deviceSpecificModel1.getTerminalContactlessTransactionLimitMada()));
                    modelList.add(new KeyValueModel(context.getString(R.string.cvm_limit), deviceSpecificModel1.getTerminalCVMRequiredLimitMada()));
                    modelList.add(new KeyValueModel(context.getString(R.string.floor_limit), deviceSpecificModel1.getTerminalContactlessFloorLimitMada()));
                    modelList.add(new KeyValueModel(context.getString(R.string.card_schemes_visi)));
                    modelList.add(new KeyValueModel(context.getString(R.string.txt_limit), deviceSpecificModel1.getTerminalContactlessTransactionLimit1()));
                    modelList.add(new KeyValueModel(context.getString(R.string.cvm_limit), deviceSpecificModel1.getTerminalCVMRequiredLimit1()));
                    modelList.add(new KeyValueModel(context.getString(R.string.floor_limit), deviceSpecificModel1.getTerminalContactlessFloorLimit1()));
                    modelList.add(new KeyValueModel(context.getString(R.string.card_scheme_master)));
                    modelList.add(new KeyValueModel(context.getString(R.string.txt_limit), deviceSpecificModel1.getTerminalContactlessTransactionLimit2()));
                    modelList.add(new KeyValueModel(context.getString(R.string.cvm_limit), deviceSpecificModel1.getTerminalCVMRequiredLimit2()));
                    modelList.add(new KeyValueModel(context.getString(R.string.floor_limit), deviceSpecificModel1.getTerminalContactlessFloorLimit2()));
                    modelList.add(new KeyValueModel(context.getString(R.string.max_saf_dept), deviceSpecificModel1.getMaxSAFDepth()));
                    modelList.add(new KeyValueModel(context.getString(R.string.cumulative_amount), deviceSpecificModel1.getMaxSAFCumulativeAmount()));
                    modelList.add(new KeyValueModel(context.getString(R.string.idle_time), deviceSpecificModel1.getIdleTime()));
                    modelList.add(new KeyValueModel(context.getString(R.string.max_reconsilcation_amount), deviceSpecificModel1.getMaxReconciliationAmount()));
                    modelList.add(new KeyValueModel(context.getString(R.string.max_reconsilation_count), deviceSpecificModel1.getMaxTransactionsProcessed()));
                    modelList.add(new KeyValueModel(context.getString(R.string.qr_indicator), deviceSpecificModel1.getqRCodePrintIndicator()));
                }
            } else if (dataset.equalsIgnoreCase(ConstantApp.PRINT_SYS_INFO)) {
                modelList.add(new KeyValueModel(context.getString(R.string.contact_application)));
                modelList.add(new KeyValueModel(context.getString(R.string.mada)));
                modelList.add(new KeyValueModel(context.getString(R.string.visa)));
                modelList.add(new KeyValueModel(context.getString(R.string.master_card)));
                modelList.add(new KeyValueModel(context.getString(R.string.contactless_application)));
                modelList.add(new KeyValueModel(context.getString(R.string.mada)));
                modelList.add(new KeyValueModel(context.getString(R.string.visa)));
                modelList.add(new KeyValueModel(context.getString(R.string.master_card)));

            }else if (dataset.equalsIgnoreCase(ConstantApp.UG_MERCHANT)) {
                modelList.add(new KeyValueModel("MERCHANT"));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel("1.Duplicate"));
                modelList.add(new KeyValueModel("It will print Duplicate copy of the last Success Transaction."));
                modelList.add(new KeyValueModel("2.Reconciliation"));
                modelList.add(new KeyValueModel("The POS Terminal Reconciliation process calculates the counts and amounts of the transactions processed by the terminal. The terminal then sends the figures to the mada switch to check if they match mada’s view on what is processed during the business day."));
                modelList.add(new KeyValueModel("Terminal Reconciliation usually takes place after the end of the business day but may also be triggered when the physical terminal needs servicing or replacement."));
                modelList.add(new KeyValueModel("After settlement gets processed, it will print Reconciliation receipt.\n"));
                modelList.add(new KeyValueModel("3.Snapshot Total"));
                modelList.add(new KeyValueModel("The Merchant can perform this operation at any time to print the total without resetting the total. It will print terminal Transaction total without Reset."));
                modelList.add(new KeyValueModel("4.Running Total"));
                modelList.add(new KeyValueModel("The supervisor should perform this operation at the end of each shift, so the total     will be reset and a fresh account starts for the next shift. It will Print terminal Transaction total and will do reset."));
                modelList.add(new KeyValueModel("5.Reconcile Setup"));
                modelList.add(new KeyValueModel("The merchant can view reconciliation status and adjust the automatic  reconciliationtime"));
                modelList.add(new KeyValueModel("5.1 Current status"));
                modelList.add(new KeyValueModel("To know the current status of reconciliation"));
                modelList.add(new KeyValueModel("5.2 Reconciliation time"));
                modelList.add(new KeyValueModel("Can adjust the reconciliation time here, enter the time and press enter, Terminal shows reconciliation time changed successfully"));
                modelList.add(new KeyValueModel("5.3 Change Status"));
                modelList.add(new KeyValueModel("To enable or disable automatic reconciliation"));
                modelList.add(new KeyValueModel("6.History View"));
                modelList.add(new KeyValueModel("Merchant can see all the transactions and also can search transactions by record ,amount , date etc"));
                modelList.add(new KeyValueModel("7.Last EMV"));
                modelList.add(new KeyValueModel("To get the tag details of the last EMV transaction"));
                modelList.add(new KeyValueModel("8.Change Password"));
                modelList.add(new KeyValueModel("Merchant can change default password to his/her own secret password to access merchant menu."));
                modelList.add(new KeyValueModel("Enter OLD password (Current password) using number pad,then press ENTER KEY."));
                modelList.add(new KeyValueModel("Enter NEW password using number pad. Then press ENTERkey."));
                modelList.add(new KeyValueModel("It will display on the screen “Change  OK” onceget changed."));
                modelList.add(new KeyValueModel("9.Terminal Info"));
                modelList.add(new KeyValueModel("This menu shows the basic terminal information, print system info and can also ping the vendor for connection check"));
                modelList.add(new KeyValueModel("10.SIM NUMBER"));
                modelList.add(new KeyValueModel("Enter the SIM details"));
                modelList.add(new KeyValueModel("11.DE-SAF All file"));
                modelList.add(new KeyValueModel("Offline transactions will be store in the terminal memory and will forward to the mada switch once it reached the pre-defined limit"));
                modelList.add(new KeyValueModel("12.Switch Connection"));
                modelList.add(new KeyValueModel("To swap connection between GPRS and WIFI"));
                modelList.add(new KeyValueModel("13.Set GPS Location"));
                modelList.add(new KeyValueModel("To set the GPS Location manually"));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel("التاجر"));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel("1- النسخة الإضافية"));
                modelList.add(new KeyValueModel("سيطبع الجهاز نسخة مكررة من آخر عملية ناجحة"));
                modelList.add(new KeyValueModel("2- التسوية"));
                modelList.add(new KeyValueModel("تحسب عملية التسوية النهائية لمحطة نقاط البيع نقاط ومبالغ المعاملات التي تتم معالجتها بواسطة الجهاز. ثم ترسل المحطة الأرقام إلى مفتاح مدى للتحقق مما إذا كانت تتطابق مع وجهة نظر مدى بشأن ما تم معالجته خلال يوم العمل"));
                modelList.add(new KeyValueModel("تتم تسوية المحطة عادةً بعد نهاية يوم العمل، ولكن قد يتم تشغيلها أيضًا عندما يحتاج الجهاز الفعلي إلى الخدمة أو الاستبدال"));
                modelList.add(new KeyValueModel("بعد معالجة التسوية، تتم طباعة إيصال التسوية"));
                modelList.add(new KeyValueModel("3 إجمالي اللقطات"));
                modelList.add(new KeyValueModel("يمكن للتاجر تنفيذ هذه العملية في أي وقت لطباعة الإجمالي دون إعادة تعيين الإجمالي"));
                modelList.add(new KeyValueModel("ستطبع إجمالي المعاملات النهائية بدون إعادة تعيين"));
                modelList.add(new KeyValueModel("4- إجمالي اللقطات"));
                modelList.add(new KeyValueModel("يجب أن يقوم المشرف بهذه العملية في نهاية كل نوبة، لذلك سيتم إعادة تعيين الإجمالي ويبدأ حساب جديد للوردية التالية"));
                modelList.add(new KeyValueModel("ستطبع المعاملات النهائية جميعاَ وسيتم إعادة تعيينها"));
                modelList.add(new KeyValueModel("5- إعداد التسوية"));
                modelList.add(new KeyValueModel("يمكن للتاجر عرض حالة التسوية وضبط وقت التسوية التلقائي"));
                modelList.add(new KeyValueModel("5-1- الحالة الحالية"));
                modelList.add(new KeyValueModel("لمعرفة الوضع الحالي للتسوية"));
                modelList.add(new KeyValueModel("5-2- وقت التسوية"));
                modelList.add(new KeyValueModel("يمكن ضبط وقت التسوية هنا، وذلك بإدخال الوقت والضغط على زر الإدخال، ومن ثَمَّ تُظهر المحطة تغيير وقت التسوية بنجاح"));
                modelList.add(new KeyValueModel("5-3- تغيير الوضع"));
                modelList.add(new KeyValueModel("لتمكين أو تعطيل التسوية التلقائية"));
                modelList.add(new KeyValueModel("6- عرض التاريخ"));
                modelList.add(new KeyValueModel("يمكن للتاجر رؤية جميع المعاملات ويمكنه أيضًا البحث عن المعاملات حسب السجل والمبلغ والتاريخ، إلخ"));
                modelList.add(new KeyValueModel("7 آخر معاملة يوروب ماستركارد وفيزا (EMV)"));
                modelList.add(new KeyValueModel("للحصول على تفاصيل البطاقة لآخر معاملة EMV"));
                modelList.add(new KeyValueModel("8- تغيير كلمة المرور"));
                modelList.add(new KeyValueModel("يمكن للتاجر تغيير كلمة المرور الإفتراضية إلى كلمة المرور السرية الخاصة به للوصول إلى قائمة التاجر"));
                modelList.add(new KeyValueModel("أدخل كلمة المرور القديمة (كلمة المرور الحالية) باستخدام لوحة الأرقام، ثم اضغط على مفتاح الإدخال"));
                modelList.add(new KeyValueModel("أدخل كلمة مرور جديدة باستخدام لوحة الأرقام. ثم اضغط مفتاح الإدخال"));
                modelList.add(new KeyValueModel("سيتم عرض \"تم التغيير\" على الشاشة بمجرد التغيير"));
                modelList.add(new KeyValueModel("9- معلومات المحطة الطرفية"));
                modelList.add(new KeyValueModel("تعرض هذه القائمة معلومات المحطة الطرفية الأساسية، وتطبع معلومات النظام ويمكنها أيضًا فحص اتصال البائع للتحقق من الإتصال"));
                modelList.add(new KeyValueModel("10- رقم وحدة تعريف المشترِك (SIM)"));
                modelList.add(new KeyValueModel("أدخل تفاصيل بطاقة وحدة تعريف المشترِك (SIM)"));
                modelList.add(new KeyValueModel("11.DE-SAF All file"));
                modelList.add(new KeyValueModel("في حالة عدم الإتصال يتم تخزين المعاملات في الذاكرة الطرفية وستتم إعادة توجيهها إلى مفتاح مدى بمجرد الوصول إلى الحد المحدد مسبقً"));
                modelList.add(new KeyValueModel("12- تبديل الإتصال"));
                modelList.add(new KeyValueModel("لمبادلة الإتصال بين الخدمة اللاسلكية العامة للرزم (GPRS) و الواي فاي"));
                modelList.add(new KeyValueModel("13- ضبط موقع نظام المواقع العالمي (GPS)"));
                modelList.add(new KeyValueModel("لضبط موقع نظام المواقع العالمي (GPS) يدويًا."));
            } else if (dataset.equalsIgnoreCase(ConstantApp.UG_RRN)) {
                modelList.add(new KeyValueModel("RRN"));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel("Retrieval Reference Number (RRN)", "Retrieval Reference Number or RRN is a 12 alphanumeric character referencesupplied by the system retaining the original source information and used to assist inlocating that information or a copy of it. This Data Element is mandatory for allfinancial transaction and reversal operation. The RRN should always be printed in fullon the POS receipt."));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel("الرقم المرجعي للاسترداد (RRN)"));
                modelList.add(new KeyValueModel("الرقم المرجعي للاسترداد أو RRN هو 12 حرفًا أبجديًا رقميًا يتم توفيره بواسطة النظام الذي يحتفظ بمعلومات المصدر الأصلية، ويستخدم للمساعدة في تحديد هذه المعلومات أو نسخة منها. عنصر البيانات هذا إلزامي لجميع المعاملات المالية وعمليات الإلغاء. ويجب دائمًا طباعة الرقم المرجعي للاسترداد بالكامل على إيصال نقطة البيع"));
            } else if (dataset.equalsIgnoreCase(ConstantApp.UG_RESPONCE_CODE)) {
                modelList.add(new KeyValueModel("Response Code"));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel("CODE-ACTION-DESCRIPTION "));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel("000-Approve-Approved"));
                modelList.add(new KeyValueModel("001-Approve-Honor with identification"));
                modelList.add(new KeyValueModel("003-Approve-Approved (VIP)"));
                modelList.add(new KeyValueModel("007-Approve-Approved, update ICC"));
                modelList.add(new KeyValueModel("087-Approve-Offline Approved (Chip only)"));
                modelList.add(new KeyValueModel("089-Approve-Unable to go On-line. Off-line approved (Chip only)"));
                modelList.add(new KeyValueModel("100-Decline-Do not honor"));
                modelList.add(new KeyValueModel("101-Decline-Expired card"));
                modelList.add(new KeyValueModel("102-Decline-Suspected fraud (To be used when ARQC validation fails)"));
                modelList.add(new KeyValueModel("103-Decline-Card acceptor contact acquirer"));
                modelList.add(new KeyValueModel("104-Decline-Restricted card"));
                modelList.add(new KeyValueModel("105-Decline-Card acceptor call acquirer’s security department"));
                modelList.add(new KeyValueModel("106-Decline-Allowable PIN tries exceeded"));
                modelList.add(new KeyValueModel("107-Decline-Refer to card issuer"));
                modelList.add(new KeyValueModel("108-Decline-Refer to card issuer’s special conditions"));
                modelList.add(new KeyValueModel("109-Decline-Invalid merchant"));
                modelList.add(new KeyValueModel("110-Decline-Invalid amount"));
                modelList.add(new KeyValueModel("111-Decline-Invalid card number"));
                modelList.add(new KeyValueModel("112-Decline-PIN data required"));
                modelList.add(new KeyValueModel("114-Decline-No account of type requested"));
                modelList.add(new KeyValueModel("115-Decline-Requested function not supported"));
                modelList.add(new KeyValueModel("116-Decline-Not sufficient funds"));
                modelList.add(new KeyValueModel("117-Decline-Incorrect PIN"));
                modelList.add(new KeyValueModel("118-Decline-No card record"));
                modelList.add(new KeyValueModel("119-Decline-Transaction not permitted to cardholder"));
                modelList.add(new KeyValueModel("120-Decline-Transaction not permitted to terminal"));
                modelList.add(new KeyValueModel("121-Decline-Exceeds withdrawal amount limit"));
                modelList.add(new KeyValueModel("122-Decline-Security violation"));
                modelList.add(new KeyValueModel("123-Decline-Exceeds withdrawal frequency limit"));
                modelList.add(new KeyValueModel("125-Decline-Card not effective"));
                modelList.add(new KeyValueModel("126-Decline-Invalid PIN block"));
                modelList.add(new KeyValueModel("127-Decline-PIN length error"));
                modelList.add(new KeyValueModel("128-Decline-PIN key synch error"));
                modelList.add(new KeyValueModel("129-Decline-Suspected counterfeit card"));
                modelList.add(new KeyValueModel("182-Decline-Invalid date (Visa 80)"));
                modelList.add(new KeyValueModel("183-Decline-Cryptographic error found in PIN or CVV (Visa 81)"));
                modelList.add(new KeyValueModel("184-Decline-Incorrect CVV (Visa 82)"));
                modelList.add(new KeyValueModel("185-Decline-Unable to verify PIN (Visa 83)"));
                modelList.add(new KeyValueModel("188-decline-Offline declined"));
                modelList.add(new KeyValueModel("190-Decline-Unable to go online – Offline declined "));
                modelList.add(new KeyValueModel("200-decline-Do not honor"));
                modelList.add(new KeyValueModel("201-Decline-Expired card"));
                modelList.add(new KeyValueModel("202-Decline-Suspected fraud (To be used when ARQC validation fails)"));
                modelList.add(new KeyValueModel("203-Decline-Card acceptor contact acquirer"));
                modelList.add(new KeyValueModel("204-Decline-Restricted card"));
                modelList.add(new KeyValueModel("205-Decline-Card acceptor call acquirer’s security department"));
                modelList.add(new KeyValueModel("206-Decline-Allowable PIN tries exceeded"));
                modelList.add(new KeyValueModel("207-Decline-Special conditions"));
                modelList.add(new KeyValueModel("208-Decline-Lost card"));
                modelList.add(new KeyValueModel("209-Decline-Stolen card"));
                modelList.add(new KeyValueModel("210-Decline-Suspected counterfeit card"));
                modelList.add(new KeyValueModel("400-Accepted-Accepted"));
                modelList.add(new KeyValueModel("902-Decline-Invalid transaction"));
                modelList.add(new KeyValueModel("903-Decline-Re-enter transaction"));
                modelList.add(new KeyValueModel("904-Decline-Format error"));
                modelList.add(new KeyValueModel("906-Decline-Cutover in process"));
                modelList.add(new KeyValueModel("907-Decline-Card issuer or switch inoperative"));
                modelList.add(new KeyValueModel("908-Decline-Transaction destination cannot be found for routing"));
                modelList.add(new KeyValueModel("909-Decline-System malfunction"));
                modelList.add(new KeyValueModel("910-Decline-Card issuer signed off"));
                modelList.add(new KeyValueModel("911-Decline-Card issuer timed out"));
                modelList.add(new KeyValueModel("912-Decline-Card issuer unavailable"));
                modelList.add(new KeyValueModel("913-Decline-Duplicate transmission"));
                modelList.add(new KeyValueModel("914-Decline-Not able to trace back to original transaction"));
                modelList.add(new KeyValueModel("915-Decline-Reconciliation cutover or checkpoint error"));
                modelList.add(new KeyValueModel("916-Decline-MAC incorrect (permissible in 1644)"));
                modelList.add(new KeyValueModel("917-Decline-MAC key sync"));
                modelList.add(new KeyValueModel("918-Decline-No communication keys available for use"));
                modelList.add(new KeyValueModel("919-Decline-Encryption key sync error"));
                modelList.add(new KeyValueModel("920-Decline-Security software/hardware error – try again"));
                modelList.add(new KeyValueModel("921-Decline-Security software/hardware error – no action"));
                modelList.add(new KeyValueModel("922-Decline-Message number out of sequence"));
                modelList.add(new KeyValueModel("923-Decline-Request in progress"));
                modelList.add(new KeyValueModel("940-Decline-Unknown terminal"));
                modelList.add(new KeyValueModel("942-Decline-Invalid Reconciliation Date"));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel("رمز الإستجابة"));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel("الرمز - الإجراء - الوصف "));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel("000 - مقبولة - مقبولة"));
                modelList.add(new KeyValueModel("001 - مقبولة - تُقْبَل مع التعريف"));
                modelList.add(new KeyValueModel("003 - مقبولة - مقبولة بامتياز   (VIP)"));
                modelList.add(new KeyValueModel("007 - مقبولة - يُعْتَد بها بشرط تحديث ICC"));
                modelList.add(new KeyValueModel("087 - مقبولة - مقبولة في وضعية عدم الإتصال، (الشريحة فقط)"));
                modelList.add(new KeyValueModel("089 - مقبولة - لا يمكن الإتصال، مقبولة مع العلم بأن الجهاز غير متصل (الشريحة فقط) "));
                modelList.add(new KeyValueModel("100 - مرفوضة - لا يُعْتَد بها"));
                modelList.add(new KeyValueModel("101 - مرفوضة - بطاقة منتهية الصلاحية"));
                modelList.add(new KeyValueModel("102 - مرفوضة - شبهة الإحتيال (يتم استخدامها عند فشل التحقق من صحة ARQC)"));
                modelList.add(new KeyValueModel("103 - مرفوضة - إتصال متقبِّل البطاقة بالجهة المستحوذة"));
                modelList.add(new KeyValueModel("104 - مرفوضة - البطاقة المقيدة"));
                modelList.add(new KeyValueModel("105 - مرفوضة - متقِّبل البطاقة يتصل بالقسم الأمني للمستحوذ"));
                modelList.add(new KeyValueModel("106 - مرفوضة - تم تجاوز محاولات رقم التعريف الشخصي (PIN) المسموح بها"));
                modelList.add(new KeyValueModel("107 - مرفوضة - الرجوع إلى مصدر البطاقة"));
                modelList.add(new KeyValueModel("108 - مرفوضة - راجع الشروط الخاصة لمصدر البطاقة"));
                modelList.add(new KeyValueModel("109 - مرفوضة - تاجر غير صحيح"));
                modelList.add(new KeyValueModel("110 - مرفوضة - مبلغ غير صحيح"));
                modelList.add(new KeyValueModel("111 - مرفوضة - رقم البطاقة غير صحيح"));
                modelList.add(new KeyValueModel("112 - مرفوضة - طلب بيانات رقم التعريف الشخصي (PIN)"));
                modelList.add(new KeyValueModel("114 - مرفوضة - لم يطلب حساب من هذا النوع"));
                modelList.add(new KeyValueModel("115 - مرفوضة - الوظيفة المطلوبة غير مدعومة"));
                modelList.add(new KeyValueModel("116 - مرفوضة - لا توجد أموال كافية"));
                modelList.add(new KeyValueModel("117 - مرفوضة - رقم التعريف الشخصي (PIN) غير صحيح"));
                modelList.add(new KeyValueModel("118 - مرفوضة - لا يوجد سجل للبطاقة"));
                modelList.add(new KeyValueModel("119 - مرفوضة - العملية غير مسموح بها لحامل البطاقة"));
                modelList.add(new KeyValueModel("120 - مرفوضة - المعاملة غير مسموح بها للمحطة"));
                modelList.add(new KeyValueModel("121 - مرفوضة - تجاوز حد المبلغ المسحوب"));
                modelList.add(new KeyValueModel("122 - مرفوضة - اختراق الأمن"));
                modelList.add(new KeyValueModel("123 - مرفوضة - تجاوز حد تكرار عملية السحب"));
                modelList.add(new KeyValueModel("125 - مرفوضة - البطاقة غير فعالة"));
                modelList.add(new KeyValueModel("126 - مرفوضة - رقم تعريف شخصي (PIN) غير صحيح ويحظر"));
                modelList.add(new KeyValueModel("127 - مرفوضة - خطأ في طول رقم التعريف الشخصي (PIN)"));
                modelList.add(new KeyValueModel("128 - مرفوضة - خطأ مزامنة مفتاح رقم التعريف الشخصي (PIN)"));
                modelList.add(new KeyValueModel("129 - مرفوضة - بطاقة مزورة مشتبه فيها"));
                modelList.add(new KeyValueModel("182 - مرفوضة - تاريخ غير صالح (Visa 80)"));
                modelList.add(new KeyValueModel("183 - مرفوضة - تم العثور على خطأ تشفير في رقم التعريف الشخصي PIN أو ( Visa 81) CM "));
                modelList.add(new KeyValueModel("184 - مرفوضة - رقم التحقق من البطاقة غير صحيح (Visa 82)"));
                modelList.add(new KeyValueModel("185 - مرفوضة - تعذر التحقق من رقم التعريف الشخصي PIN (Visa 83)"));
                modelList.add(new KeyValueModel("188 - مرفوضة - تم رفض وضع عدم الاتصال"));
                modelList.add(new KeyValueModel("190 - مرفوضة - تعذر الاتصال بالإنترنت - تم رفض وضع عدم الاتصال"));
                modelList.add(new KeyValueModel("200 - مرفوضة - لا يُعْتَد به"));
                modelList.add(new KeyValueModel("201 - مرفوضة - بطاقة منتهية الصلاحية"));
                modelList.add(new KeyValueModel("202 - مرفوضة - إحتيال مشتبه به يتم إستخدامه عند فشل التحقق من صحة  الكريبتوقرام ARQC))"));
                modelList.add(new KeyValueModel("203 - مرفوضة - الاتصال بجهة قبول البطاقة"));
                modelList.add(new KeyValueModel("204 - مرفوضة - البطاقة المقيدة"));
                modelList.add(new KeyValueModel("205 - مرفوضة - القسم الأمني في متقبِّل الاتصالات بشأن البطاقة"));
                modelList.add(new KeyValueModel("206 - مرفوضة - تم تجاوز محاولات إدخال رقم التعريف الشخصي (PIN) المسموح بها"));
                modelList.add(new KeyValueModel("207 - مرفوضة - شروط خاصة"));
                modelList.add(new KeyValueModel("208 - مرفوضة - البطاقة مفقودة"));
                modelList.add(new KeyValueModel("209 - مرفوضة - البطاقة مسروقة"));
                modelList.add(new KeyValueModel("210 - مرفوضة - بطاقة مزورة مشتبه بها"));
                modelList.add(new KeyValueModel("400 - مقبول - تم قبولها"));
                modelList.add(new KeyValueModel("902 - مرفوضة - المعاملة غير صالحة"));
                modelList.add(new KeyValueModel("903 - مرفوضة - أعد إدخال المعاملة"));
                modelList.add(new KeyValueModel("904 - مرفوضة - خطأ في التنسيق"));
                modelList.add(new KeyValueModel("906 - مرفوضة - جاري التغيير"));
                modelList.add(new KeyValueModel("907 - مرفوضة - مصدر البطاقة أو المفتاح (Switch) معطل"));
                modelList.add(new KeyValueModel("908 - مرفوضة - لا يمكن العثور على وجهة المعاملة للتوجيه"));
                modelList.add(new KeyValueModel("909 - مرفوضة - عطل في النظام"));
                modelList.add(new KeyValueModel("910 - مرفوضة - Cتوقيع جهة إصدار البطاقة"));
                modelList.add(new KeyValueModel("911 - مرفوضة - انتهت مهلة مُصدر البطاقة"));
                modelList.add(new KeyValueModel("912 - مرفوضة - مُصدر البطاقة غير متاح"));
                modelList.add(new KeyValueModel("913 - مرفوضة - إرسال مكرر"));
                modelList.add(new KeyValueModel("914 - مرفوضة - غير قادر على العودة إلى المعاملة الأصلية"));
                modelList.add(new KeyValueModel("915 - مرفوضة - خطأ في تغيير التسوية أو خطأ في نقطة التحقق"));
                modelList.add(new KeyValueModel("916 - مرفوضة - MAC incorrect (permissible in 1644)"));
                modelList.add(new KeyValueModel("917 - مرفوضة - MAC key sync"));
                modelList.add(new KeyValueModel("918 - مرفوضة - لا توجد مفاتيح إتصال متاحة للإستخدام"));
                modelList.add(new KeyValueModel("919 - مرفوضة - خطأ مزامنة مفتاح التشفير"));
                modelList.add(new KeyValueModel("920 - مرفوضة - خطأ في برنامج الأمان / الأجهزة - حاول مرة أخرى"));
                modelList.add(new KeyValueModel("921 - مرفوضة - خطأ في جهاز برنامج الأمان - لا يوجد إجراء"));
                modelList.add(new KeyValueModel("922 - مرفوضة - رقم الرسالة خارج التسلسل"));
                modelList.add(new KeyValueModel("923 - مرفوضة - الطلب قيد الإجراء"));
                modelList.add(new KeyValueModel("940 - مرفوضة - محطة غير معروفة"));
                modelList.add(new KeyValueModel("942 - مرفوضة - تاريخ تسوية غير صالح"));

            } else if (dataset.equalsIgnoreCase(ConstantApp.UG_COMMON_ERROR_MSG)) {
                modelList.add(new KeyValueModel("Common Error Messages"));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel("MESSAGE - DESCRIPTION"));
                modelList.add(new KeyValueModel("Out of Paper"));
                modelList.add(new KeyValueModel("Displayed when paper failure has been detected by the terminal."));
                modelList.add(new KeyValueModel("Card Error"));
                modelList.add(new KeyValueModel("Indicates Malfunction of card"));
                modelList.add(new KeyValueModel("Swipe Error"));
                modelList.add(new KeyValueModel("If the card is swiped incorrectly"));
                modelList.add(new KeyValueModel("Read Chip Error"));
                modelList.add(new KeyValueModel("If the card is not inserted correctly"));
                modelList.add(new KeyValueModel("Reconciliation Completed"));
                modelList.add(new KeyValueModel("Indicates the reconciliation at terminal is In-balance"));
                modelList.add(new KeyValueModel("Reconciliation Unsuccessful"));
                modelList.add(new KeyValueModel("Indicates the reconciliation at terminal is Out-of-balance"));
                modelList.add(new KeyValueModel("Amount Out Of Range Per Trans"));
                modelList.add(new KeyValueModel("Displayed when the merchant attempted a transaction where the amount exceeded the upper limit for card  scheme"));
                modelList.add(new KeyValueModel("Cancel"));
                modelList.add(new KeyValueModel("Transaction cancelled by merchant"));
                modelList.add(new KeyValueModel("Wrong PIN"));
                modelList.add(new KeyValueModel("Displayed when an incorrect PIN is entered"));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel("رسائل خطأ المحطة الطرفية الشائع"));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel("الرسالة - الوصف"));
                modelList.add(new KeyValueModel("نفذ الورق"));
                modelList.add(new KeyValueModel("تظهر عندما يتم إكتشاف فشل في الورق من قبل المحطة"));
                modelList.add(new KeyValueModel("خطأ في البطاقة"));
                modelList.add(new KeyValueModel("يشير إلى خلل في البطاقة"));
                modelList.add(new KeyValueModel("خطأ في التمرير"));
                modelList.add(new KeyValueModel("إذا تم تمرير البطاقة بشكل غير صحيح"));
                modelList.add(new KeyValueModel("خطأ في قراءة الشريحة"));
                modelList.add(new KeyValueModel("إذا لم يتم إدخال البطاقة بشكل صحيح"));
                modelList.add(new KeyValueModel("إكتمال التسوية"));
                modelList.add(new KeyValueModel("يشير إلى أن الرصيد يكفي للتسوية في المحطة "));
                modelList.add(new KeyValueModel("التسوية فشلت"));
                modelList.add(new KeyValueModel("يشير إلى أن التسوية في الطرفية خارج الرصيد"));
                modelList.add(new KeyValueModel("المبلغ خارج نطاق المدى للمعاملة"));
                modelList.add(new KeyValueModel("تظهر عند محاولة التاجر إجراء معاملة تجاوز مبلغ الحد الأعلى لنظام البطاقة"));
                modelList.add(new KeyValueModel("إلغاء"));
                modelList.add(new KeyValueModel("تم إلغاء المعاملة من قبل التاجر"));
                modelList.add(new KeyValueModel("خطأ في رقم التعريف الشخصي"));
                modelList.add(new KeyValueModel("تظهر عند إدخال رقم تعريف شخصي غير صحيح"));
            } else if (dataset.equalsIgnoreCase(ConstantApp.UG_PAPER_ROLL_INSTALL)) {
                modelList.add(new KeyValueModel("Paper Roll Installation"));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel("STEP 1", "Gently pull upwards on the groove located on both sides of the Printer door. "));
                modelList.add(new KeyValueModel("STEP 2", "Open the printer door. "));
                modelList.add(new KeyValueModel("STEP 3", "Place the paper roll into the compartment The correct orientation of the paper is critical. Leave a few centimeters of excess paper protruding."));
                modelList.add(new KeyValueModel("STEP 4", "Close the printer door and push gently until a slight 'click' is heard when fastened. "));
                modelList.add(new KeyValueModel("STEP 5", "Remove excess paper by tearing along the serrated edge."));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel("تركيب لفة الورق"));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel("الخطوة الأولى: إسحب برفق لأعلى على الأخدود الموجود على جانبي باب الطابعة"));
                modelList.add(new KeyValueModel("الخطوة الثانية: إفتح باب الطابعة"));
                modelList.add(new KeyValueModel("الخطوة الثالثة: ضع لفافة الورق في الحجرة مع مراعاة الإتجاه الصحيح للفة الورق. أترك بضعة"));
                modelList.add(new KeyValueModel("سنتيمترات من الورق الزائد بارزاً"));
                modelList.add(new KeyValueModel("الخطوة الرابعة: أغلق باب الطابعة واضغط برفق حتى يتم سماع \"نقرة\" طفيفة عند تثبيتها"));
                modelList.add(new KeyValueModel("الخطوة الخامسة: قم بإزالة الورق الزائد عن طريق تمزيقه بالحافة المسننة"));

            } else if (dataset.equalsIgnoreCase(ConstantApp.UG_POWER_ON_OFF)) {
                modelList.add(new KeyValueModel("Power ON/OFF"));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel("Power ON: ", "Press Power button on the POS terminal keypad for 2 seconds, until the LCD backlight is lit. Then POS terminal is powered on. "));
                modelList.add(new KeyValueModel("Power OFF: ", "Press and hold power button on the POS terminal keypad for 2 - 3 seconds and there will be a prompt \"power off. ..\" , Then POS terminal is powered off. "));
                modelList.add(new KeyValueModel("Note: ", "Please unplug the power supply and remove the battery to power off if the terminal is not responding."));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel("التشغيل والإيقاف"));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel("التشغيل:", "اضغط على زر الطاقة على لوحة مفاتيح نقطة البيع لمدة ثانيتين، حتى تضاء الإضاءة الخلفية للشاشة.  عندها تكون نقطة البيع في وضع التشغيل التشغيل"));
                modelList.add(new KeyValueModel("إيقاف التشغيل:", "Press and hold power button on the POS terminal keypad for 2 - 3 seconds and there will be a prompt \"power off. ..\" , Then POS terminal is powered off. "));
                modelList.add(new KeyValueModel("ملاحظة:", "Please unplug the power supply and remove the battery to power off if the terminal is not responding."));
            } else if (dataset.equalsIgnoreCase(ConstantApp.UG_CARD_READER)) {
                modelList.add(new KeyValueModel("Card Reader Operations"));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel("Chip Card Reader"));
                modelList.add(new KeyValueModel("It is located under the keypad."));
                modelList.add(new KeyValueModel("Please insert the Chip card into the terminal to the end with the contact surface (chip) facing upward. \n"));
                modelList.add(new KeyValueModel("Contactless Card Reader"));
                modelList.add(new KeyValueModel("It is located in the area around the display. Hold the card over the contactless area at a distance not more than 40mm. "));
                modelList.add(new KeyValueModel("Magnetic Stripe/Swipe- Card Reader "));
                modelList.add(new KeyValueModel("It is located on the right side of the terminal. Please swipe the magnetic stripe card with the magnetic stripe facing towards the display. The magnetic stripe card should be pressed against the card reader tightly and swiped through as smoothly as possible. It can be swiped through from top to bottom or from bottom to top."));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel("عمليات قارئ البطاقات"));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel("قارئ بطاقة الشريحة"));
                modelList.add(new KeyValueModel("موضعها يقع تحت لوحة المفاتيح"));
                modelList.add(new KeyValueModel("يرجى إدخال بطاقة الشريحة في المحطة بحيث يكون سطح التلامس (الشريحة) متجهًا لأعلى"));
                modelList.add(new KeyValueModel("قارئ بطاقة عدم التلامس"));
                modelList.add(new KeyValueModel("وهي تقع في المنطقة المحيطة بالشاشة. لوِح بالبطاقة فوق منطقة التلامس على مسافة لا تزيد عن 40 مم"));
                modelList.add(new KeyValueModel("قارئ بطاقة الشريط الممغنط/التمرير"));
                modelList.add(new KeyValueModel("يقع على الجانب الأيمن من المحطة. يرجى تمرير بطاقة الشريط المغنطيسي بحيث يكون الشريط المغناطيسي باتجاه الشاشة. يجب الضغط على بطاقة الشريط المغنطيسي ضد قارئ البطاقة بإحكام وتمريرها بسلاسة قدر الإمكان. يمكن تمريره من أعلى إلى أسفل أو من أسفل إلى أعلى"));
            } else if (dataset.equalsIgnoreCase(ConstantApp.UG_COMMON_TROUBLE_SHOOT)) {
                modelList.add(new KeyValueModel("Common Troubleshooting"));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel("I. Battery life of POS terminal will be significantly reduced after charging"));
                modelList.add(new KeyValueModel("1. Battery is not fully charged."));
                modelList.add(new KeyValueModel("2. Battery has a failure. Please replace the battery."));
                modelList.add(new KeyValueModel("II. POS terminal unable or inefficient to read magnetic stripe card"));
                modelList.add(new KeyValueModel("Check whether the magnetic stripe is facing towards the display when swiping the magnetic stripe card."));
                modelList.add(new KeyValueModel("III Chip card information cannot be read"));
                modelList.add(new KeyValueModel("Check whether the Chip card contact surface (chip) is facing upward when inserted."));
                modelList.add(new KeyValueModel("IV. LCD screen not illuminated"));
                modelList.add(new KeyValueModel("1. Check whether the power supply is connected to the electrical outlet correctly."));
                modelList.add(new KeyValueModel("2. Check whether the power supply connector is connected to the POS terminal correctly."));
                modelList.add(new KeyValueModel("3. Check whether the POS terminal battery is low or poorly fitted"));
                modelList.add(new KeyValueModel("V. Printer not work"));
                modelList.add(new KeyValueModel("1. Make sure the printer lid is closed correctly."));
                modelList.add(new KeyValueModel("2. Make sure appropriate paper roll is used and correctly loaded in the compartment.\n"));
                modelList.add(new KeyValueModel("VI. Receipt not printed or printed incompletely"));
                modelList.add(new KeyValueModel("1. Check whether the paper roll is loaded properly."));
                modelList.add(new KeyValueModel("2. Remove any scraps of paper remaining inside the compartment."));
                modelList.add(new KeyValueModel("3. Check proper paper feeding by using paper feed function and select reprint option if normal.\n"));
                modelList.add(new KeyValueModel("VII. Illegible printing"));
                modelList.add(new KeyValueModel("1. The sensitivity of printing paper maybe low (common after long-term storage), please replace the printing paper."));
                modelList.add(new KeyValueModel("VIII. Communication failure", "1. Check whether the SIM card is inserted or card contact is dirty."));
                modelList.add(new KeyValueModel("2. Check whether the signal of handheld device is strong enough "));
                modelList.add(new KeyValueModel("3. Try to establish communication again."));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel(" الأخطاء الشائعة"));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel("أولاً: إذا انخفض عمر بطارية جهاز نقطة البيع بشكل ملحوظ بعد الشحن،"));
                modelList.add(new KeyValueModel("1. البطارية غير مشحونة بالكامل"));
                modelList.add(new KeyValueModel("2. فشل البطارية. يرجى استبدالها."));
                modelList.add(new KeyValueModel("ثانياً: جهاز نقطة البيع غير قادر على قراءة بطاقة الشريط المغنطيسي"));
                modelList.add(new KeyValueModel("تحقق مما إذا كان الشريط المغنطيسي مواجهًا للشاشة عند تمرير بطاقة الشريط المغنطيسي"));
                modelList.add(new KeyValueModel("ثالثاً: لا يمكن قراءة معلومات بطاقة الشريحة"));
                modelList.add(new KeyValueModel("تحقق مما إذا كان سطح تلامس البطاقة (الشريحة) متجهاً لأعلى عند إدخال البطاقة"));
                modelList.add(new KeyValueModel("رابعاً: الشاشة غير مضاءة"));
                modelList.add(new KeyValueModel("1. تحقق مما إذا كان مصدر الطاقة متصلاً بمأخذ التيار الكهربائي بشكل صحيح"));
                modelList.add(new KeyValueModel("2. تحقق مما إذا كان موصل مصدر الطاقة متصلاً بطرف نقطة البيع بشكل صحيح"));
                modelList.add(new KeyValueModel("3. تحقق مما إذا كانت بطارية محطة نقطة البيع منخفضة أو ليست مركبة بالشكل الصحيح"));
                modelList.add(new KeyValueModel("خامساً: الطابعة لا تعمل"));
                modelList.add(new KeyValueModel("1. تأكد من إغلاق غطاء الطابعة بشكل صحيح"));
                modelList.add(new KeyValueModel("2. تأكد من استخدام لفة الورق المناسبة وتحميلها بشكل صحيح في الحجرة"));
                modelList.add(new KeyValueModel("سادساً: لم يتم طباعة الإيصال أو طباعته بشكل غير كامل"));
                modelList.add(new KeyValueModel("1. تحقق من تحميل لفافة الورق بشكل صحيح"));
                modelList.add(new KeyValueModel("2. قم بإزالة أي قصاصات ورق متبقية داخل الحجرة"));
                modelList.add(new KeyValueModel("3. تحقق من تغذية الورق المناسبة باستخدام وظيفة تغذية الورق وحدد خيار إعادة الطباعة إذا كان"));
                modelList.add(new KeyValueModel("ذلك طبيعيًا"));
                modelList.add(new KeyValueModel("1. سابعاً: الطباعة غير مقروءة"));
                modelList.add(new KeyValueModel("ثامناً: فشل في التواصل", "1.  تحقق مما إذا كانت بطاقة SIM مدخلة أو جهة اتصال البطاقة متسخة"));
                modelList.add(new KeyValueModel("2.  تحقق مما إذا كانت إشارة الجهاز المحمول قوية بما يكفي"));
                modelList.add(new KeyValueModel("3.  حاول إنشاء اتصال مرة أخرى"));
            } else if (dataset.equalsIgnoreCase(ConstantApp.UG_CAUTION_SAFETY)) {
                modelList.add(new KeyValueModel("Caution and Safety Instructions"));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel("• Do not attempt to disassemble, maintain or repair any part."));
                modelList.add(new KeyValueModel("• Do not use if damaged or with signs of tamper."));
                modelList.add(new KeyValueModel("• Only use the power adapter supplied by Digital Pay with an electrical outlet of the correct rating. "));
                modelList.add(new KeyValueModel("• Avoid the potential hazard of electrical shock do not use in wet environments or during an electrical storm. "));
                modelList.add(new KeyValueModel("• Do not use in proximity of potentially flammable gases or substances. "));
                modelList.add(new KeyValueModel("• Ensure cables used do not cause a trip hazard or risk the device being dropped on to a hard surface. "));
                modelList.add(new KeyValueModel("• Do not expose to excessive heat or cold. "));
                modelList.add(new KeyValueModel("• Only use a rechargeable battery supplied or specified by Digital Pay and follow caution instructions printed on it."));
                modelList.add(new KeyValueModel("• Do not immerse, use liquids, sprays or aerosol cleaners. "));
                modelList.add(new KeyValueModel("• This device is intended for hand held use only. "));
                modelList.add(new KeyValueModel("• Before entering pin ,user should be aware of his/her surrounding "));
                modelList.add(new KeyValueModel("• This is a hand-held device, do not enter pin while charging or on any fixed surface."));
                modelList.add(new KeyValueModel("\n"));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel(" تعليمات الحذر والسلامة"));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel("لا تحاول تفكيك أو صيانة أو إصلاح أي جزء."));
                modelList.add(new KeyValueModel("لا تستخدم الجهاز في حالة التلف أو مع وجود علامات عبث."));
                modelList.add(new KeyValueModel("استخدم فقط محول الطاقة المزود بواسطة Digital Pay بمأخذ كهربائي من التصنيف الصحيح."));
                modelList.add(new KeyValueModel("تجنب المخاطر المحتملة لصدمة كهربائية لا تستخدم الجهاز في البيئات الرطبة أو أثناء عاصفة كهربائية."));
                modelList.add(new KeyValueModel("لا تستخدم الجهاز بالقرب من الغازات أو المواد القابلة للاشتعال."));
                modelList.add(new KeyValueModel("تأكد من أن الكابلات المستخدمة لا تسبب خطر سقوط الجهاز على سطح صلب."));
                modelList.add(new KeyValueModel("لا تُعرض الجهاز للحرارة أو البرودة المفرطة."));
                modelList.add(new KeyValueModel("إستخدم فقط البطارية القابلة لإعادة الشحن التي يتم توفيرها أو تحديدها بواسطة Digital Pay واتباع تعليمات السلامة المطبوعة عليها."));
                modelList.add(new KeyValueModel("لا تغطس الجهاز في سائل أو تستخدم السوائل أو البخاخات أو منظفات الأيروسول."));
                modelList.add(new KeyValueModel("هذا الجهاز مخصص للاستخدام باليد فقط."));
                modelList.add(new KeyValueModel("قبل إدخال رقم التعريف الشخصي، يجب أن يكون المستخدم على دراية بمحيطه."));
                modelList.add(new KeyValueModel("هذا جهاز محمول باليد، لا تدخل دبوساً أثناء الشحن أو على أي سطح ثابت."));
            } else if (dataset.equalsIgnoreCase(ConstantApp.UGO_MODE_CHIP_CARD)) {
                modelList.add(new KeyValueModel("PURCHASE - CHIP Card"));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel("Purchase", "A data capture transaction that debits a cardholder›s account in exchange for goods  or services."));
                modelList.add(new KeyValueModel("STEP 1"));
                modelList.add(new KeyValueModel("Enter the Purchase amount. Example: SR10.00"));
                modelList.add(new KeyValueModel("Press ENTER."));
                modelList.add(new KeyValueModel("STEP 2"));
                modelList.add(new KeyValueModel("Insert customer’s card."));
                modelList.add(new KeyValueModel("STEP 3"));
                modelList.add(new KeyValueModel("Key-in customer’s Personal Identification Number or PIN."));
                modelList.add(new KeyValueModel("Then press ENTER"));
                modelList.add(new KeyValueModel("STEP 4"));
                modelList.add(new KeyValueModel("Terminal will process the transaction."));
                modelList.add(new KeyValueModel("Transaction Approved"));
                modelList.add(new KeyValueModel("STEP 5", "Printing Receipt"));
                modelList.add(new KeyValueModel("STEP 6", "Remove the card"));

                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel("شراء - بطاقة الشريحة"));

                modelList.add(new KeyValueModel("أدخل قيمة الشراء"));
                modelList.add(new KeyValueModel(" مثال: 10.00 ريال سعودي"));
                modelList.add(new KeyValueModel(" أضغط على زر الإدخال"));

                modelList.add(new KeyValueModel("أدخل بطاقة العميل"));

                modelList.add(new KeyValueModel("أدخل رقم التعريف الشخصي للعميل (PIN)"));
                modelList.add(new KeyValueModel(" ثم أضغط على زر الإدخال"));

                modelList.add(new KeyValueModel("ستقوم المحطة بمعالجة المعاملة"));
                modelList.add(new KeyValueModel(" تمت الموافقة على المعاملة"));

                modelList.add(new KeyValueModel(" طباعة الإيصال"));

                modelList.add(new KeyValueModel("أخرج البطاقة"));


            } else if (dataset.equalsIgnoreCase(ConstantApp.UGO_MODE_MAGNETIC_STRIPE)) {
                modelList.add(new KeyValueModel("PURCHASE – MAGNETIC STRIPE CARD"));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel("Purchase", "A data capture transaction that debits a cardholder›s account in exchange for goods  or services."));
                modelList.add(new KeyValueModel("STEP 1"));
                modelList.add(new KeyValueModel("Enter the Purchase amount. Example: SR10.00"));
                modelList.add(new KeyValueModel("Press ENTER."));
                modelList.add(new KeyValueModel("STEP 2"));
                modelList.add(new KeyValueModel("Insert Card in Reverse Direction"));
                modelList.add(new KeyValueModel("Terminal shows"));
                modelList.add(new KeyValueModel("Not Accepted"));
                modelList.add(new KeyValueModel("PLS Swipe"));
                modelList.add(new KeyValueModel("PLS Remove card"));
                modelList.add(new KeyValueModel("STEP 3"));
                modelList.add(new KeyValueModel("Remove card"));
                modelList.add(new KeyValueModel("Terminal shows"));
                modelList.add(new KeyValueModel("READ CHIP ERR"));
                modelList.add(new KeyValueModel("Swipe"));
                modelList.add(new KeyValueModel("Swipe the card"));
                modelList.add(new KeyValueModel("STEP 4"));
                modelList.add(new KeyValueModel("Key-in customer’s Personal Identification Number or PIN."));
                modelList.add(new KeyValueModel("Then press ENTER"));
                modelList.add(new KeyValueModel("STEP 5"));
                modelList.add(new KeyValueModel("Terminal will process the transaction."));
                modelList.add(new KeyValueModel("Transaction Approved"));
                modelList.add(new KeyValueModel("STEP 6", "Printing Receipt"));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel("أدخل قيمة الشراء"));
                modelList.add(new KeyValueModel("مثال: 10.00 ريال سعودي"));
                modelList.add(new KeyValueModel(" أضغط على زر الإدخال"));
                modelList.add(new KeyValueModel(" أدخل البطاقة في الاتجاه العكسي"));
                modelList.add(new KeyValueModel("المحطة تظهر"));
                modelList.add(new KeyValueModel("أغير مقبول"));
                modelList.add(new KeyValueModel(" يرجى التمرير"));
                modelList.add(new KeyValueModel("يرجى إزالة البطاقة"));
                modelList.add(new KeyValueModel(" أخرج البطاقة"));
                modelList.add(new KeyValueModel("المحطة تظهر"));
                modelList.add(new KeyValueModel("قراءة خطأ الشريحة"));
                modelList.add(new KeyValueModel("أمسح"));
                modelList.add(new KeyValueModel("أدخل رقم التعريف الشخصي للعميل (PIN) "));
                modelList.add(new KeyValueModel("ثم أضغط على زر الإدخال"));
                modelList.add(new KeyValueModel("ستقوم المحطة بمعالجة المعاملة"));
                modelList.add(new KeyValueModel("تمت الموافقة على المعاملة"));
                modelList.add(new KeyValueModel("طباعة الإيصال"));

            } else if (dataset.equalsIgnoreCase(ConstantApp.UGO_MODE_CONACTLESS)) {
                modelList.add(new KeyValueModel("PURCHASE – CONTACTLESS"));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel("Purchase", "A data capture transaction that debits a cardholder›s account in exchange for goods  or services."));
                modelList.add(new KeyValueModel("STEP 1"));
                modelList.add(new KeyValueModel("Enter the Purchase amount. Example: SR10.00"));
                modelList.add(new KeyValueModel("Press ENTER."));
                modelList.add(new KeyValueModel("STEP 2"));
                modelList.add(new KeyValueModel("Wave the card"));
                modelList.add(new KeyValueModel("STEP 3"));
                modelList.add(new KeyValueModel("If the amount entered is above contactless Limit , terminal prompts PIN entry"));
                modelList.add(new KeyValueModel("Key-in customer’s Personal Identification Number or PIN."));
                modelList.add(new KeyValueModel("Then press ENTER"));
                modelList.add(new KeyValueModel("STEP 4"));
                modelList.add(new KeyValueModel("Terminal will process the transaction."));
                modelList.add(new KeyValueModel("Transaction Approved"));
                modelList.add(new KeyValueModel("STEP 5", "Printing Receipt"));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel("أدخل قيمة الشراء"));
                modelList.add(new KeyValueModel("مثال: 10.00 ريال سعودي"));
                modelList.add(new KeyValueModel("أضغط على زر الإدخال"));
                modelList.add(new KeyValueModel("قم بتلويح البطاقة"));
                modelList.add(new KeyValueModel("إذا كان المبلغ الذي تم إدخاله أعلى من حد عدم "));
                modelList.add(new KeyValueModel(" التلامس المحدد،"));
                modelList.add(new KeyValueModel("تطالب المحطة الطرفية بإدخال رقم التعريف "));
                modelList.add(new KeyValueModel("الشخصي"));
                modelList.add(new KeyValueModel("أدخل رقم التعريف الشخصي (PIN)"));
                modelList.add(new KeyValueModel("ستقوم المحطة بمعالجة المعاملة"));
                modelList.add(new KeyValueModel("تمت الموافقة على المعاملة"));
                modelList.add(new KeyValueModel("طباعة الإيصال"));

            } else if (dataset.equalsIgnoreCase(ConstantApp.UGO_MODE_MANUAL_ENTRY)) {
                modelList.add(new KeyValueModel("PURCHASE – Manual Card entry"));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel("Purchase", "A data capture transaction that debits a cardholder›s account in exchange for goods  or services."));
                modelList.add(new KeyValueModel("STEP 1"));
                modelList.add(new KeyValueModel("Enter the Purchase amount. Example: SR10.00"));
                modelList.add(new KeyValueModel("Press ENTER."));
                modelList.add(new KeyValueModel("STEP 2"));
                modelList.add(new KeyValueModel("Terminal shows"));
                modelList.add(new KeyValueModel("Enter Card Details"));
                modelList.add(new KeyValueModel("STEP 3"));
                modelList.add(new KeyValueModel("Enter Card Number,Validity and PIN"));
                modelList.add(new KeyValueModel("STEP 4"));
                modelList.add(new KeyValueModel("Terminal will process the transaction."));
                modelList.add(new KeyValueModel("Transaction Approved"));
                modelList.add(new KeyValueModel("STEP 5", "Printing Receipt"));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel("أدخل قيمة الشراء"));
                modelList.add(new KeyValueModel("مثال: 10.00 ريال سعودي"));
                modelList.add(new KeyValueModel("أضغط على زر الإدخال"));
                modelList.add(new KeyValueModel("المحطة تظهر"));
                modelList.add(new KeyValueModel("أدخل/ امسح/لوح"));
                modelList.add(new KeyValueModel("أضغط زر الإدخال"));
                modelList.add(new KeyValueModel("أدخل رقم البطاقة والصلاحية"));
                modelList.add(new KeyValueModel("ورقم التعريف الشخصي (PIN)"));
                modelList.add(new KeyValueModel("ستقوم المحطة بمعالجة المعاملة"));
                modelList.add(new KeyValueModel("تمت الموافقة على المعاملة"));
                modelList.add(new KeyValueModel("طباعة الإيصال"));

            } else if (dataset.equalsIgnoreCase(ConstantApp.UGO_PURCHASE_NAQD)) {
                modelList.add(new KeyValueModel("Purchase with NAQD"));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel("Purchase with NAQD", "A Purchase transaction where the amount of the transaction represents both the value of the goods (or services) and of a Cash Amount requested by the Cardholder. The amount of the cash portion is identified in the transaction data as a separate item."));
                modelList.add(new KeyValueModel("STEP 1"));
                modelList.add(new KeyValueModel("Enter the Purchase amount. Example: SR10.00"));
                modelList.add(new KeyValueModel("Press ENTER."));
                modelList.add(new KeyValueModel("STEP 2"));
                modelList.add(new KeyValueModel("Enter NAQD Amount Example:SR5.00."));
                modelList.add(new KeyValueModel("Press ENTER."));
                modelList.add(new KeyValueModel("STEP 3"));
                modelList.add(new KeyValueModel("Insert/Swipe/Wave"));
                modelList.add(new KeyValueModel("Terminal Shows total ofPurchase and  NAQDAmount"));
                modelList.add(new KeyValueModel("Press Enter"));
                modelList.add(new KeyValueModel("STEP 4"));
                modelList.add(new KeyValueModel("Terminal will process the transaction."));
                modelList.add(new KeyValueModel("Transaction Approved"));
                modelList.add(new KeyValueModel("STEP 5"));
                modelList.add(new KeyValueModel("Printing Receipt"));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel("أدخل قيمة الشراء"));
                modelList.add(new KeyValueModel("مثال: 10.00 ريال سعودي"));
                modelList.add(new KeyValueModel("أضغط على زر الإدخال"));
                modelList.add(new KeyValueModel("أدخل قيمة \"نقد"));
                modelList.add(new KeyValueModel("مثال: 5.00 ريال سعودي"));
                modelList.add(new KeyValueModel("أضغط على زر الإدخال"));
                modelList.add(new KeyValueModel("أدخل/ امسح/ لوح"));
                modelList.add(new KeyValueModel("المحطة تظهر قيمة الشراء، مبلغ \"نقد"));
                modelList.add(new KeyValueModel("والمبلغ الكلي"));
                modelList.add(new KeyValueModel("أضغط على زر الإدخال"));
                modelList.add(new KeyValueModel("ستقوم المحطة بمعالجة المعاملة"));
                modelList.add(new KeyValueModel("تمت الموافقة على المعاملة"));
                modelList.add(new KeyValueModel("طباعة الإيصال"));

            } else if (dataset.equalsIgnoreCase(ConstantApp.UGO_PURCHASE_ADVICE_PARTIAL)) {
                modelList.add(new KeyValueModel("Purchase Advice"));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel("Purchase Advice", "Follow-up to an approved pre-authorization purchase transaction. It is initiated after the cardholder received the purchased goods or services. The amount entered in this transaction supersedes that entered in the pre-authorization purchase."));
                modelList.add(new KeyValueModel("STEP 1", "Enter Original Transaction REF Number"));
                modelList.add(new KeyValueModel("STEP 2", "Enter Original Transaction Date"));
                modelList.add(new KeyValueModel("STEP 3", "Enter Auth Code"));
                modelList.add(new KeyValueModel("STEP 4", "Purchase Advice Amount"));
                modelList.add(new KeyValueModel("STEP 5", "Insert/Swipe/Enter"));
                modelList.add(new KeyValueModel("STEP 6"));
                modelList.add(new KeyValueModel("Terminal will process the transaction."));
                modelList.add(new KeyValueModel("Transaction Approved"));
                modelList.add(new KeyValueModel("STEP 7"));
                modelList.add(new KeyValueModel("Printing Receipt"));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel("أدخل المعاملة الأصلية"));
                modelList.add(new KeyValueModel("رقم المرجع"));
                modelList.add(new KeyValueModel("أدخل المعاملة الأصلية"));
                modelList.add(new KeyValueModel("التاريخ"));
                modelList.add(new KeyValueModel("أدخل رمز المصادقة"));
                modelList.add(new KeyValueModel("مبلغ إشعار الشراء"));
                modelList.add(new KeyValueModel("أدخل/ امسح/ لوح"));
                modelList.add(new KeyValueModel("ستقوم المحطة بمعالجة المعاملة"));
                modelList.add(new KeyValueModel("تمت الموافقة على المعاملة"));
                modelList.add(new KeyValueModel("طباعة الإيصال"));

            } else if (dataset.equalsIgnoreCase(ConstantApp.UGO_CASH_ADVANCE)) {
                modelList.add(new KeyValueModel("Cash Advance"));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel("Cash Advance", "A manual cash disbursement, typically obtained at a bank’s branch. Note some publications identify all Cash transactions involving a Credit card as ‘Cash Advance’ transactions."));
                modelList.add(new KeyValueModel("STEP 1", "Enter Cash Amount"));
                modelList.add(new KeyValueModel("STEP 2", "Insert/Swipe/Enter"));
                modelList.add(new KeyValueModel("STEP 3"));
                modelList.add(new KeyValueModel("Terminal will process the transaction."));
                modelList.add(new KeyValueModel("Transaction Approved"));
                modelList.add(new KeyValueModel("STEP 4"));
                modelList.add(new KeyValueModel("Printing Receipt"));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel("أدخل قيمة المبلغ"));
                modelList.add(new KeyValueModel("أدخل / امسح/ لوِح"));
                modelList.add(new KeyValueModel("ستقوم المحطة بمعالجة المعاملة"));
                modelList.add(new KeyValueModel("تمت الموافقة على المعاملة"));
                modelList.add(new KeyValueModel("طباعة الإيصال"));


            } else if (dataset.equalsIgnoreCase(ConstantApp.UGO_PRE_AUTHORISATION)) {
                modelList.add(new KeyValueModel("Authorization"));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel("Authorization", "Online check of a cardholder's account before a purchase is made. The transaction is entered with an amount that is equal to that of the purchase or that is predetermined by the retailer. If approved, this transaction assumes a pre-authorization purchase completion will follow to finalize the purchase. The pre-authorized amount can optionally be held against the account until a pre-authorization completion occurs or the hold time expires."));
                modelList.add(new KeyValueModel("STEP 1", "Enter Authorization Amount"));
                modelList.add(new KeyValueModel("STEP 2", "Insert/Swipe/Enter"));
                modelList.add(new KeyValueModel("STEP 3"));
                modelList.add(new KeyValueModel("Terminal will process the transaction."));
                modelList.add(new KeyValueModel("Transaction Approved"));
                modelList.add(new KeyValueModel("STEP 4"));
                modelList.add(new KeyValueModel("Printing Receipt"));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel("أدخل مبلغ التفويض"));
                modelList.add(new KeyValueModel("أدخل/ امسح / لوح"));
                modelList.add(new KeyValueModel("ستقوم المحطة بمعالجة المعاملة"));
                modelList.add(new KeyValueModel("تمت الموافقة على المعاملة"));
                modelList.add(new KeyValueModel("طباعة الإيصال"));


            } else if (dataset.equalsIgnoreCase(ConstantApp.UGO_REFUND)) {
                modelList.add(new KeyValueModel("Refund"));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel("Refund", "A data capture transaction that is initiated by the Retailer to credit the cardholder for a refund of goods or services, and to debit the retailer's account accordingly. It requires the cardholder to enter the PIN and also requires the retailer's supervisor password and the retailer's signature."));
                modelList.add(new KeyValueModel("STEP 1", "Enter Merchant Password"));
                modelList.add(new KeyValueModel("STEP 2", "Enter RRN Number"));
                modelList.add(new KeyValueModel("STEP 3", "Enter Date"));
                modelList.add(new KeyValueModel("STEP 4", "Enter Refund Amount"));
                modelList.add(new KeyValueModel("STEP 5", "Insert/Swipe/Enter"));
                modelList.add(new KeyValueModel("STEP 6"));
                modelList.add(new KeyValueModel("Terminal will process the transaction."));
                modelList.add(new KeyValueModel("Transaction Approved"));
                modelList.add(new KeyValueModel("STEP 7"));
                modelList.add(new KeyValueModel("Printing Receipt"));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel("أدخل كلمة مرور التاجر"));
                modelList.add(new KeyValueModel("أدخل رقم بيانات السجل النسبي (RRN)"));
                modelList.add(new KeyValueModel("أدخل التاريخ"));
                modelList.add(new KeyValueModel("أدخل مبلغ الإسترداد"));
                modelList.add(new KeyValueModel("أدخل/ امسح / لوِح"));
                modelList.add(new KeyValueModel("ستقوم المحطة بمعالجة المعاملة"));
                modelList.add(new KeyValueModel("تمت الموافقة على المعاملة"));
                modelList.add(new KeyValueModel("طباعة الإيصال"));

            } else if (dataset.equalsIgnoreCase(ConstantApp.UGO_PURCHASE_REVERSAL)) {
                modelList.add(new KeyValueModel("Reversal"));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel("Reversals are generated:"));
                modelList.add(new KeyValueModel("a) When initiated by the Retailer as a Cancel transaction to reverse the previous purchase transaction within 60 sec"));
                modelList.add(new KeyValueModel("b) Automatically when a terminal communication error occurs."));
                modelList.add(new KeyValueModel("c) Automatically on a timeout."));
                modelList.add(new KeyValueModel(DASH));
                modelList.add(new KeyValueModel("يتم إنشاء العمليات العكسية في الحالات التالية:"));
                modelList.add(new KeyValueModel("أ) إذا قام البائع بإلغاء عملية الشراء السابقة في"));
                modelList.add(new KeyValueModel(" غضون 60 ثانية"));
                modelList.add(new KeyValueModel("ب) تلقائيًا عند حدوث خطأ في إتصال المحطة"));
                modelList.add(new KeyValueModel("ج) تلقائيًا عند انتهاء المهلة"));

            }
            modelList.add(new KeyValueModel("\n"));
            Logger.v("modelList --" + modelList.size());

//            boolean status = false;
//            try {
//                status = PrinterReceipt.printKeyValue(modelList, devicePrinter,context);
//            } catch (DeviceException e) {
//                e.printStackTrace();
//            }
//            if (status)
            return Result.success();
//            else
//                return Result.failure();

        }
        if (getInputData().getBoolean(RECONSILATION_REQUEST, false)) {
            Logger.v("Recon");
            if (PrinterWorker.RECON_PRINTED) {
                return Result.success(data.build());
            }
            boolean printstatus = PrinterReceipt.printBillReconciliation(database, devicePrinter, logoBitmap, context);
            data.putBoolean(STATUS, printstatus);
//            if (printstatus) {
            PrinterWorker.RECON_PRINTED = true;
            database.getTransactionDao().nukeTable();
//            }
            return Result.success(data.build());
        }

        if (getInputData().getBoolean(RECONSILATION_REQUEST_DUPLICATE, false)) {
            if (PrinterWorker.RECON_PRINTED_DUB) {
                return Result.success(data.build());
            }
            Logger.v("Recon");
            Logger.v("Recon Dup");
            boolean printstatus = PrinterReceipt.printBillReconciliationDuplicate(devicePrinter, logoBitmap, context);
            data.putBoolean(STATUS, printstatus);
            RECON_PRINTED_DUB = true;
            return Result.success(data.build());
        }
        if (getInputData().getBoolean(SNAPSHOT_TOTAL, false) || getInputData().getBoolean(RUNNING_TOTAL, false)) {
            int uidLat = database.getTransactionDao().getLastTransactionUid();
            Logger.v("SnapShot --" + uidLat);
            Logger.v("SnapShot --" + uid);
            if (uid < uidLat) {
                boolean printstatus = PrinterReceipt.printBillSnapShot(database, devicePrinter, uid, getInputData().getBoolean(SNAPSHOT_TOTAL, false), logoBitmap, context);
                data.putBoolean(STATUS, printstatus);
                data.putInt(UID_LAST_TRANSACTION, uidLat);
                Logger.v("Print Snapshot success");
                return Result.success(data.build());
            } else {
                return Result.failure(data.build());
            }
        }

        TransactionModelEntity lastTrans;
        boolean isSAF = getInputData().getBoolean(SAF_REQUEST, false);
        boolean isCancelled = getInputData().getBoolean(CANCELLED_REQUEST, false);
        boolean isDuplicate = getInputData().getBoolean(DUPLICATE_PRINT, false);
        Logger.v("SAF --" + isSAF);
        if (isDuplicate) {
            lastTrans = AppManager.getInstance().getDuplicateTransactionModelEntity();
        } else if (AppManager.getInstance().isDebugEnabled()) {
            lastTrans = AppManager.getInstance().getDebugTransactionModelEntity();
        } else if (isSAF)
            lastTrans = database.getSAFDao().getLastTransaction();
        else
            lastTrans = database.getTransactionDao().getLastTransaction(false);
        if (lastTrans.getNameTransactionTag().equalsIgnoreCase(ConstantApp.PURCHASE_REVERSAL) || lastTrans.getMti0().equalsIgnoreCase(ConstantAppValue.REVERSAL) || lastTrans.getMti0().equalsIgnoreCase(ConstantAppValue.REVERSAL_REPEAT)) {
            if (lastTrans.getPosConditionCode25().equalsIgnoreCase(ConstantAppValue.TIMEOUT_WAITING_RESP) || lastTrans.getPosConditionCode25().equalsIgnoreCase(ConstantAppValue.MAC_FAILURE)) {
                if (AppConfig.customerCopyPrinted)
                    lastTrans = database.getTransactionDao().getLastTransaction(false, ConstantAppValue.REVERSAL);
                else
                    return Result.success();
            }
        }

        if(lastTrans.getModeTransaction().equalsIgnoreCase(ConstantAppValue.KEYED) || lastTrans.getModeTransaction().equalsIgnoreCase(ConstantAppValue.SWIPED)){
            if(lastTrans.getEndTimeTransaction().trim().equalsIgnoreCase("dd/MM/yyyy HH:mm:ss")){
                if(lastTrans.getResponseCode39().trim().equalsIgnoreCase(ConstantAppValue.REFER_TO_CARD_ISSUER_VALUE)){
                    lastTrans.setResponseCode39("190");
                }
            }
        }

        AppManager.getInstance().setDuplicateTransactionModelEntity(lastTrans);

        Logger.v("lastTrans -1-" + lastTrans.toString());
        RetailerDataModel retailerDataModel = AppManager.getInstance().getRetailerDataModel();
        Logger.v("retailerDataModel --" + retailerDataModel.toString());
        String id = lastTrans.getAdditionalDataNational47();
        Logger.v("id --" + id);
        String indicator = lastTrans.getCardIndicator();
        Logger.v("indicator --" + indicator);
        TMSCardSchemeEntity cardData = null;
        if (indicator.trim().length() != 0) {
            cardData = database.getTMSCardSchemeDao().getCardSchemeData(indicator);
            Logger.v("cardData --" + cardData.toString());
            Logger.v("CV Validation --" + cardData.getCardHolderAuth());
            Logger.v("CV Validation --" + lastTrans.getNameTransactionTag());
        }
        HashMap<String, String> tag55test = Utils.getParsedTag55(lastTrans.getIccCardSystemRelatedData55());
        Logger.v("tag55printer------" + tag55test.get(TAG55[3]));

        DeviceSpecificModel deviceSpecificModel1 = AppManager.getInstance().getDeviceSpecificModel();
        Logger.v("getqRCodePrintIndicator --" + deviceSpecificModel1.getqRCodePrintIndicator());
        if (deviceSpecificModel1.getqRCodePrintIndicator().trim().equalsIgnoreCase("1")) //TODO qrindicator condition
            printerModel.setQrCodeData(buildQRCode(lastTrans));
        else
            printerModel.setQrCodeData(null);
        printerModel.setRetailerNameArabic(new String(retailerDataModel.getRetailerNameInArabic().getBytes(Charset.forName("Cp1252")), Charset.forName("ISO-8859-6")).trim()); //TMS
        printerModel.setRetailerNameEnglish(retailerDataModel.getRetailerNameEnglish().trim()); //TMS
        printerModel.setTerminalStreetArabic(new String(retailerDataModel.getRetailerAddress1Arabic().getBytes(Charset.forName("Cp1252")), Charset.forName("ISO-8859-6")).trim()); //TMS
        printerModel.setTerminalStreetEnglish(retailerDataModel.getRetailerAddress1English().trim());//TMS
        printerModel.setTerminalCityArabic(new String(retailerDataModel.getRetailerAddress2Arabic().getBytes(Charset.forName("Cp1252")), Charset.forName("ISO-8859-6")).trim()); //TMS
        printerModel.setRetailerTelephone(AppManager.getInstance().getString(ConstantApp.SPRM_PHONE_NUMBER).trim());//TMS
        printerModel.setTerminaCityEnglish(retailerDataModel.getRetailerAddress2English().trim());//TMS

        // AK
        if (!isSAF && !(lastTrans.getStartTimeTransaction().trim().equalsIgnoreCase("dd/MM/yyyy HH:mm:ss"))) {
            String startDate[] = lastTrans.getStartTimeTransaction().split(" ");
            printerModel.setStartDate(startDate[0]);
            printerModel.setStartTime(startDate[1]);
        } else {
            if (!(lastTrans.getStartTimeTransaction().trim().equalsIgnoreCase("dd/MM/yyyy HH:mm:ss"))) {
                String startDate[] = lastTrans.getStartTimeTransaction().split(" ");
                printerModel.setStartDate(startDate[0]);
                printerModel.setStartTime(startDate[1]);
            } else {
                String start = Utils.getCurrentDate();
                String startDate[] = start.split(" ");
                printerModel.setStartDate(startDate[0]);
                printerModel.setStartTime(startDate[1]);
                if (isSAF)
                    database.getSAFDao().updateStartTime(lastTrans.getUid(), start);
                else
                    database.getTransactionDao().updateStartTime(lastTrans.getUid(), start);
            }
        }
        if (lastTrans.getAdditionalDataNational47().trim().length() != 0) {
            printerModel.setbId(lastTrans.getAdditionalDataNational47().substring(0, 4)); //TMS
        }
        printerModel.setbId(lastTrans.getAdditionalDataNational47().substring(0, 4)); //TMS
        printerModel.setmId(lastTrans.getCardAcceptorIdCode42()); // 41 Data
        printerModel.settId(lastTrans.getCardAcceptorTemId41()); // 42
        printerModel.setMcc(lastTrans.getPosPinCaptureCode26()); //TMS
        printerModel.setStan(lastTrans.getSystemTraceAuditnumber11());
        printerModel.setPosSoftwareVersionNumber(AppManager.getDeviceVersion()); // APP
        printerModel.setRrn(lastTrans.getRetriRefNo37());

//AK
        printerModel.setTransactionTypeArabic(Utils.getTransactionNameArabic(context, lastTrans.getNameTransactionTag()));//
        printerModel.setTransactionTypeEnglish(Utils.getTransactionNameEnglish(context, lastTrans.getNameTransactionTag()).toUpperCase());
        printerModel.setPan(Utils.addAccountNumber(Utils.decrypt( lastTrans.getPrimaryAccNo2())));
        printerModel.setCardExpry(Utils.decrypt( lastTrans.getDateExpiration14()));

//AK
        if (!lastTrans.getNameTransactionTag().equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION_EXTENSION)) {
            printerModel.setTotalAmountArabic(Utils.getTransactionAmountArabic(context, lastTrans.getNameTransactionTag())); // Need to change
            printerModel.setTotalAmountEnglish(Utils.getTransactionNameEnglish(context, lastTrans.getNameTransactionTag()).toUpperCase() + " AMOUNT");
//AK
            printerModel.setAmountArabic(Utils.getArabicNumbers(lastTrans.getAmtTransaction4(), true) + " ريال ");
            printerModel.setAmountEnglish("SAR " + Utils.changeAmountFormatWithDecimal(lastTrans.getAmtTransaction4()));
        }
        //setting data for purchase with cashback
        if (lastTrans.getNameTransactionTag().equalsIgnoreCase(ConstantApp.PURCHASE_NAQD)) {
            printerModel.setPurchaseAmountStringArabic(Utils.getTransactionAmountArabic(context, ConstantApp.Purchase_Amount));
//            double total = Double.parseDouble(lastTrans.getAmtTransaction4());
            double total = AppConfig.EMV.amountValue;
//            int total =(int)(AppConfig.EMV.amountValue);
//            Logger.v("satyamount-----" + AppConfig.EMV.amountValue);
//            Logger.v("satyamount-----" + total);
//            double cashBack = Double.parseDouble(lastTrans.getAddlAmt54().substring(8));
//            int cashBack =(int) AppConfig.EMV.amtCashBack;
//            Logger.v("satyamountcb-----" + AppConfig.EMV.amtCashBack);
//            Logger.v("satyamountcb-----" + cashBack);
            printerModel.setPurchaseAmountArabic(Utils.getArabicNumbers((total - AppConfig.EMV.amtCashBack)) + " ريال ");
            printerModel.setPurchaseWithCashBackAmountStringArabic(Utils.getTransactionAmountArabic(context, ConstantApp.NAQD_AMOUNT));
            printerModel.setPurchaseWithCashBackAmountArabic(Utils.getArabicNumbers(AppConfig.EMV.amtCashBack) + " ريال ");
            printerModel.setPurchaseAmountStringEnglish("PURCHASE AMOUNT");
            printerModel.setPurchaseAmountEnglish("SAR " + Utils.changeAmountFormatWithDecimal((total - AppConfig.EMV.amtCashBack)));
//            Logger.v("purchase_amount_print-----" + (AppConfig.EMV.amtCashBack - total));
            printerModel.setPurchaseWithCashBackAmountStringEnglish("NAQD AMOUNT");
            printerModel.setPurchaseWithCashBackAmountEnglish("SAR " + Utils.changeAmountFormatWithDecimal(AppConfig.EMV.amtCashBack));
            //AK
            printerModel.setTotalAmountArabic("المبلغ الإجمالي"); // Need to change
            printerModel.setTotalAmountEnglish("TOTAL AMOUNT");
        }


        // Responce TMS - Packet 3
        Logger.v("lastTrans.getResponseCode39() --" + isCancelled);
        Logger.v("lastTrans.getResponseCode39() --" + isSAF);
        Logger.v("lastTrans.getResponseCode39() --" + lastTrans.getResponseCode39());
        printerModel.setAlpharesponseCode(lastTrans.getResponseCode39()); // Field 39 - response
        if (!isCancelled && (((isSAF || isDuplicate) && (lastTrans.getResponseCode39().equalsIgnoreCase(ConstantAppValue.SAF_APPROVED) || lastTrans.getResponseCode39().equalsIgnoreCase(ConstantAppValue.SAF_APPROVED_UNABLE) || lastTrans.getResponseCode39().equalsIgnoreCase(ConstantAppValue.SAF_REFUND))) ||
                ((lastTrans.getStatusTransaction() != 0) && (lastTrans.getResponseCode39().equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_000)
                        || lastTrans.getResponseCode39().equalsIgnoreCase(ConstantAppValue.SAF_APPROVED) || lastTrans.getResponseCode39().equalsIgnoreCase(ConstantAppValue.SAF_APPROVED_UNABLE) || lastTrans.getResponseCode39().equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_003) || lastTrans.getResponseCode39().equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_001) || lastTrans.getResponseCode39().equalsIgnoreCase(ConstantApp.SUCCESS_RESPONSE_007))))) {
            if (isSAF || lastTrans.getResponseCode39().equalsIgnoreCase(ConstantAppValue.SAF_APPROVED) || lastTrans.getResponseCode39().equalsIgnoreCase(ConstantAppValue.SAF_APPROVED_UNABLE)) {
                printerModel.setTransactionOutcomeArabic("مقبولة(ب)"); // response -
                printerModel.setTransactionOutcomeEnglish("OFFLINE APPROVED"); // response
            } else {
                printerModel.setTransactionOutcomeArabic("مقبولة"); // response -
                printerModel.setTransactionOutcomeEnglish("APPROVED"); // response
            }

            if (isDuplicate) {
                if ((lastTrans.getResponseCode39().equalsIgnoreCase(ConstantAppValue.SAF_APPROVED) || lastTrans.getResponseCode39().equalsIgnoreCase(ConstantAppValue.SAF_APPROVED_UNABLE))) {
                    printerModel.setTransactionOutcomeArabic("مقبولة(ب)"); // response -
                    printerModel.setTransactionOutcomeEnglish("OFFLINE APPROVED"); // response
                }
            }

            //        lastTrans.getAuthIdResCode38()

            if (lastTrans.getAuthIdResCode38() != null && lastTrans.getAuthIdResCode38().trim().length() != 0) {
                printerModel.setApprovalCodeEnglish(lastTrans.getAuthIdResCode38()); //response Field 38
                printerModel.setApprovalCodeArabic(Utils.getArabicNumbersSimple(lastTrans.getAuthIdResCode38())); //response Field 38
                printerModel.setApprovalCodeStringArabic("رمز الموافقة"); //Hard coded
                printerModel.setApprovalCodeStringEnglish("APPROVAL CODE"); //Hard Coded

            }
            //set signature
            Logger.v("PinVlock --" + lastTrans.getPinData52());

            if ((lastTrans.getPinData52() != null && lastTrans.getPinData52().trim().length() != 0) || lastTrans.getPosEntrymode22().substring(7, 8).equalsIgnoreCase("1")) {
                printerModel.setCardHolderVerificationOrReasonForDeclineArabic("تم التحقق من الرقم السري للعميل");
                printerModel.setCardHolderVerificationOrReasonForDeclineEnglish("CARDHOLDER PIN VERIFIED");
                Logger.v("PIN Required");
            } else {
                Logger.v("Sign Required");
                if ((lastTrans.getCardIndicator().equalsIgnoreCase(ConstantAppValue.A0000002282010) || lastTrans.getCardIndicator().equalsIgnoreCase(ConstantAppValue.A000000GN)) && lastTrans.getModeTransaction().equalsIgnoreCase(ConstantAppValue.CONTACTLESS)) {
                    Logger.v("Sign 2d Required");
                    printerModel.setCardHolderVerificationOrReasonForDeclineArabic("لا يتطلب التحقق");
                    printerModel.setCardHolderVerificationOrReasonForDeclineEnglish("NO VERIFICATION REQUIRED");

                    printerModel.setSignBelowArabic(null);
                    printerModel.setSignBelowEnglish(null);
                    printerModel.setAccountForTheAmountArabic(null);
                    printerModel.setAccountForTheAmountEnglish(null);
                    printerModel.setUnderline(null);
                } else {
                    printerModel.setCardHolderVerificationOrReasonForDeclineArabic("تم التحقق من الرقم السري للعميل");
                    printerModel.setCardHolderVerificationOrReasonForDeclineEnglish("CARDHOLDER SIGNATURE VERIFIED");
                    printerModel.setSignBelowArabic("العميل يوقع بالاسفل");
                    printerModel.setSignBelowEnglish("CUSTOMER SIGN BELOW");
                    printerModel.setAccountForTheAmountArabic("أوافق على الخصم من حسابي للمبلغ");
                    printerModel.setAccountForTheAmountEnglish("DEBIT MY ACCOUNT FOR THE AMOUNT");
                    printerModel.setUnderline(ConstantAppValue.underline);
                }

            }

            if (!printerModel.getCardHolderVerificationOrReasonForDeclineEnglish().equalsIgnoreCase("NO VERIFICATION REQUIRED")) {
                if (lastTrans.getNameTransactionTag().equalsIgnoreCase(ConstantApp.REFUND) || lastTrans.getNameTransactionTag().equalsIgnoreCase(ConstantApp.PURCHASE_NAQD)) {
                    printerModel.setSignBelowArabic("التاجر يوقع بالاسفل");
                    printerModel.setSignBelowEnglish("RETAILER SIGN BELOW");
                    printerModel.setAccountForTheAmountArabic("أوافق على إضافة المبلغ لحساب العميل");
                    printerModel.setAccountForTheAmountEnglish("CREDIT CUSTOMER ACCOUNT FOR THE AMOUNT");
                    printerModel.setUnderline(ConstantAppValue.underline);
                    if (lastTrans.getNameTransactionTag().equalsIgnoreCase(ConstantApp.PURCHASE_NAQD)) {
                        printerModel.setSignBelowArabic("العميل يوقع بالاسفل");
                        printerModel.setSignBelowEnglish("CUSTOMER SIGN BELOW");
                        printerModel.setAccountForTheAmountArabic("أوافق على الخصم من حسابي للمبلغ");
                        printerModel.setAccountForTheAmountEnglish("DEBIT MY ACCOUNT FOR THE AMOUNT");
                    }
                } else if ((lastTrans.getModeTransaction().equalsIgnoreCase(ConstantAppValue.KEYED) || lastTrans.getModeTransaction().equalsIgnoreCase(ConstantAppValue.SWIPED))) {
                    if (cardData != null && Utils.checkCardValidationLine(cardData.getCardHolderAuth(), lastTrans.getNameTransactionTag())) {
                        printerModel.setSignBelowArabic("العميل يوقع بالاسفل");
                        printerModel.setSignBelowEnglish("CUSTOMER SIGN BELOW");
                        printerModel.setAccountForTheAmountArabic("أوافق على الخصم من حسابي للمبلغ");
                        printerModel.setAccountForTheAmountEnglish("DEBIT MY ACCOUNT FOR THE AMOUNT");
                        printerModel.setUnderline(ConstantAppValue.underline);
                    }
                } else {
                    HashMap<String, String> tag55 = Utils.getParsedTag55(Utils.decrypt( lastTrans.getIccCardSystemRelatedData55()));
                    /*Logger.v("tag55printer------" + tag55.get(TAG55[3]));
                    if (tag55.get(TAG55[3]) == "") { //9f37

                        printerModel.setSignBelowArabic("العميل يوقع بالاسفل");
                        printerModel.setSignBelowEnglish("CUSTOMER SIGN BELOW");
                        printerModel.setAccountForTheAmountArabic("أوافق على الخصم من حسابي للمبلغ");
                        printerModel.setAccountForTheAmountEnglish("DEBIT MY ACCOUNT FOR THE AMOUNT");
                        printerModel.setUnderline(ConstantAppValue.underline);
                    }*/

                    Logger.v("tag55printer------" + tag55.get(TAG55[14]));
                    try {
                        String tagg = tag55.get(TAG55[14]).substring(1, 2);
                        Logger.v("tag55printer------" + tagg);
                        if (tagg.trim().equalsIgnoreCase("3") || tagg.trim().equalsIgnoreCase("5")) { //9f37
                            printerModel.setSignBelowArabic("العميل يوقع بالاسفل");
                            printerModel.setSignBelowEnglish("CUSTOMER SIGN BELOW");
                            printerModel.setAccountForTheAmountArabic("أوافق على الخصم من حسابي للمبلغ");
                            printerModel.setAccountForTheAmountEnglish("DEBIT MY ACCOUNT FOR THE AMOUNT");
                            printerModel.setUnderline(ConstantAppValue.underline);
                        }if (tagg.trim().equalsIgnoreCase("F")){
                            printerModel.setCardHolderVerificationOrReasonForDeclineArabic("لا يتطلب التحقق");
                            printerModel.setCardHolderVerificationOrReasonForDeclineEnglish("NO VERIFICATION REQUIRED");

                            printerModel.setSignBelowArabic(null);
                            printerModel.setSignBelowEnglish(null);
                            printerModel.setAccountForTheAmountArabic(null);
                            printerModel.setAccountForTheAmountEnglish(null);
                            printerModel.setUnderline(null);
                        }
                    }catch (NullPointerException e){
                        Logger.v("Exception ee121");
                    }
                }
            }

            if (lastTrans.getResponseData44().trim().length() != 0) {
                printerModel.setData44(lastTrans.getResponseData44());
                Logger.v("Sign 2d Required");
                printerModel.setCardHolderVerificationOrReasonForDeclineArabic("لا يتطلب التحقق");
                printerModel.setCardHolderVerificationOrReasonForDeclineEnglish("NO VERIFICATION REQUIRED");

                printerModel.setSignBelowArabic(null);
                printerModel.setSignBelowEnglish(null);
                printerModel.setAccountForTheAmountArabic(null);
                printerModel.setAccountForTheAmountEnglish(null);
                printerModel.setUnderline(null);
            }

        } else if (lastTrans.getResponseCode39().equalsIgnoreCase(ConstantApp.REVERSAL_RESPONSE_400)) {
            printerModel.setTransactionOutcomeArabic("مستلمة"); // response -
            printerModel.setTransactionOutcomeEnglish("ACCEPTED"); // response

//            printerModel.setCardHolderVerificationOrReasonForDeclineArabic("تم التحقق من الرقم السري للعميل"); 
//            printerModel.setCardHolderVerificationOrReasonForDeclineEnglish("CARDHOLDER PIN VERIFIED");  


            //        lastTrans.getAuthIdResCode38()

//            if (isSAF) {
//                printerModel.setApprovalCodeEnglish("000000"); //response Field 38
//                printerModel.setApprovalCodeArabic("١٢٣٤٥٦"); //response Field 38
//
//            } else
            if (lastTrans.getAuthIdResCode38() != null && lastTrans.getAuthIdResCode38().trim().length() != 0) {
                printerModel.setApprovalCodeEnglish(lastTrans.getAuthIdResCode38()); //response Field 38
                printerModel.setApprovalCodeArabic(Utils.getArabicNumbersSimple(lastTrans.getAuthIdResCode38())); //response Field 38
            }
            printerModel.setApprovalCodeStringArabic("رمز الموافقة"); //Hard coded
            printerModel.setApprovalCodeStringEnglish("APPROVAL CODE"); //Hard Coded

            //set signature
            if (cardData != null && Utils.checkCardValidationLine(cardData.getCardHolderAuth(), lastTrans.getNameTransactionTag())) {
                printerModel.setSignBelowArabic("العميل يوقع بالاسفل");
                printerModel.setSignBelowEnglish("CUSTOMER SIGN BELOW");
                printerModel.setAccountForTheAmountArabic("أوافق على الخصم من حسابي للمبلغ");
                printerModel.setAccountForTheAmountEnglish("DEBIT MY ACCOUNT FOR THE AMOUNT");
                printerModel.setUnderline(ConstantAppValue.underline);
            }


        } else if (isCancelled) {
            printerModel.setTransactionOutcomeArabic("مرفوضة"); // response -
            printerModel.setTransactionOutcomeEnglish("DECLINED"); // response
            printerModel.setCardHolderVerificationOrReasonForDeclineArabic("إلغاء"); // response -
            printerModel.setCardHolderVerificationOrReasonForDeclineEnglish("CANCELLATION"); // response
            printerModel.setAlpharesponseCode("UC");
        } else {
            if (isSAF || (lastTrans.getResponseCode39().equalsIgnoreCase(ConstantAppValue.SAF_REJECTED) || lastTrans.getResponseCode39().equalsIgnoreCase(ConstantAppValue.SAF_DECLINED)) || AppManager.getInstance().isAdminNotificationReversal()) {
                printerModel.setTransactionOutcomeArabic("مرفوضة(ب)"); // response -
                printerModel.setTransactionOutcomeEnglish("OFFLINE DECLINED"); // response
            } else {
                printerModel.setTransactionOutcomeArabic("مرفوضة"); // response -
                printerModel.setTransactionOutcomeEnglish("DECLINED"); // response
            }
            if (lastTrans.getResponseCode39().trim().length() != 0) {
                TMSMessageTextModelEntity status = database.getTMSMessageTextDao().getPrinterMessageText(lastTrans.getResponseCode39());
                if (status != null && status.getArabicMessageText().trim().length() != 0 && status.getEnglishMessageText().trim().length() != 0) {
                    printerModel.setCardHolderVerificationOrReasonForDeclineArabic(new String(status.getArabicMessageText().getBytes(Charset.forName("Cp1252")), Charset.forName("ISO-8859-6"))); //TMS
                    printerModel.setCardHolderVerificationOrReasonForDeclineEnglish(status.getEnglishMessageText().toUpperCase()); // response
                } else {
                    printerModel.setCardHolderVerificationOrReasonForDeclineArabic("مرفوضة"); // response -
                    printerModel.setCardHolderVerificationOrReasonForDeclineEnglish("DECLINED"); // response
                }
            } else {
                printerModel.setCardHolderVerificationOrReasonForDeclineArabic("مرفوضة"); // response -
                printerModel.setCardHolderVerificationOrReasonForDeclineEnglish("DECLINED"); // response
                printerModel.setAlpharesponseCode("190"); // Field 39 - response
            }
        }

//AK
        if (!isSAF && !(lastTrans.getEndTimeTransaction().trim().equalsIgnoreCase("dd/MM/yyyy HH:mm:ss"))) {
            String endDate[] = lastTrans.getEndTimeTransaction().split(" ");
            printerModel.setEndDate(endDate[0]);
            printerModel.setEndTime(endDate[1]);
        } else {
            if (!(lastTrans.getEndTimeTransaction().trim().equalsIgnoreCase("dd/MM/yyyy HH:mm:ss"))) {
                String startDate[] = lastTrans.getEndTimeTransaction().split(" ");
                printerModel.setEndDate(startDate[0]);
                printerModel.setEndTime(startDate[1]);
            } else {
                String end = Utils.getCurrentDate();
                String endDate[] = end.split(" ");
                printerModel.setEndDate(endDate[0]);
                printerModel.setEndTime(endDate[1]);
                if (isSAF)
                    database.getSAFDao().updateEndTime(lastTrans.getUid(), end);
                else
                    database.getTransactionDao().updateEndTime(lastTrans.getUid(), end);
            }
        }
        printerModel.setThankYouEnglish("THANK YOU FOR USING mada"); // Hard coded
        printerModel.setPleaseRetainYourReceiptEnglish("PLEASE RETAIN YOUR RECEIPT"); // Hard coded
        printerModel.setReceiptVersionArabic("نسخة العميل"); // Hard coded Based on Copy
        if (AppConfig.customerCopyPrinted) {
            printerModel.setReceiptVersionEnglish("*CUSTOMER COPY*"); // Hard coded
            printerModel.setReceiptVersionArabic("*نسخة العميل*"); // Hard coded Based on Copy

        } else {
            printerModel.setReceiptVersionEnglish("*RETAILER COPY*"); // Hard coded
            printerModel.setReceiptVersionArabic("*نسخة التاجر*"); // Hard coded Based on Copy

        }

        printerModel.setPosEntryMode(lastTrans.getModeTransaction()); // Field 22
        if (indicator.trim().length() != 0) {
            printerModel.setApplicationLabelEnglish(Utils.getCardName(indicator));
            printerModel.setApplicationLabelArabic(Utils.getCardNameArabic(indicator)); // Arabic Harcodes Field 55 TAG 50
        }
        if (!(lastTrans.getModeTransaction().equalsIgnoreCase(ConstantAppValue.KEYED) || lastTrans.getModeTransaction().equalsIgnoreCase(ConstantAppValue.SWIPED))) {
            HashMap<String, String> tag55 = Utils.getParsedTag55(Utils.decrypt( lastTrans.getIccCardSystemRelatedData55()));

//            TLVPackage tlv1 = ISOUtils.newTlvPackage();
//            tlv1.unpack(ISOUtils.hex2byte(lastTrans.getIccCardSystemRelatedData55()));
//            String d9f34 = tlv1.getString(0x9F34);
            String d9f34 = IsoRequest.getStringFromHex(tag55.get(TAG55[14]));
            Logger.v("cvm_d9f34----"+d9f34);
            d9f34=SimpleTransferListener.cvm9f34;
            Logger.v("cvm_d9f34----"+d9f34);
            String tag50 = IsoRequest.getStringFromHex(tag55.get(TAG55[18]));
            if (tag50.trim().length() != 0)
                printerModel.setApplicationLabelEnglish(tag50); // Field 55 TAG 50
            Logger.v("9F12 --"+tag50);
            Logger.v("9F12 --"+ SimpleTransferListener.selectApplicationName);
            if (tag50 != null && tag50.trim().length() != 0 && !(indicator.equalsIgnoreCase("GN"))){
                if(SimpleTransferListener.selectApplicationName != null && SimpleTransferListener.selectApplicationName.trim().length() != 0){
                    printerModel.setApplicationLabelEnglish(SimpleTransferListener.selectApplicationName); // Field 55 TAG 50
                }else {
//                    printerModel.setApplicationLabelEnglish(ByteConversionUtils.convertHexToString(tag50)); // Field 55 TAG 50
                    printerModel.setApplicationLabelEnglish(tag50); // Field 55 TAG 50
                }
            }

            printerModel.setAid(tag55.get(TAG55[17])); // Tag 84
//            printerModel.setTvr((tag55.get(TAG55[5]) != null && (tag55.get(TAG55[5]).trim().length() != 0)) ? tag55.get(TAG55[5]) : tag55.get(TAG55[22]) + " " + tag55.get(TAG55[23])); // Tag 95
//            printerModel.setTsi(tag55.get(TAG55[21])); // Tag 9B
            Logger.v("9f6c----"+SimpleTransferListener.s0x9f6c+"   "+"s0x9F66----"+SimpleTransferListener.s0x9F66);
            if ((SimpleTransferListener.s0x9f6c!= null) && SimpleTransferListener.s0x9f6c.length()!=0 && (SimpleTransferListener.s0x9F66 != null) && SimpleTransferListener.s0x9F66.length()!=0){
                printerModel.setTvr(SimpleTransferListener.s0x9f6c+ " "+SimpleTransferListener.s0x9F66);
            }else {
                printerModel.setTvr(SimpleTransferListener.tvr);
            }
            Logger.v("tvr----"+printerModel.getTvr());
            Logger.v("tvr----"+SimpleTransferListener.tvr);
            String tsii = SimpleTransferListener.tsi;
            Logger.v("TSII -"+SimpleTransferListener.tsi);
            Logger.v("TSII -"+lastTrans.getTsii());
            printerModel.setTsi(lastTrans.getTsii()); // Tag 9B
            printerModel.setCvr(d9f34); // Tag 9F34
            printerModel.setApplicationCryptogramInfo(tag55.get(TAG55[2])); // Tag , 9F27
            printerModel.setKernalId(lastTrans.getKernalID());
            printerModel.setApplicationCryptogram(tag55.get(TAG55[0])); // Tag 9F26
        }
        printerModel.setThankYouArabic(" شكرا لك على استخدام مدى ");
        printerModel.setPleaseRetainYourReceiptArabic(" يرجي الاحتفاظ بالايصال ");
        if (lastTrans.getResponseData44().trim().length() != 0) {
            if (printerModel.getCvr().trim().equalsIgnoreCase("640300") && printerModel.getTransactionOutcomeEnglish().trim().equalsIgnoreCase("APPROVED")) {
                Logger.v("PIN 2d Required NEW");
                printerModel.setCardHolderVerificationOrReasonForDeclineArabic("تم التحقق من هوية حامل الجهاز");
                printerModel.setCardHolderVerificationOrReasonForDeclineEnglish("DEVICE OWNER IDENTITY VERIFIED");
            }
        }

        if (isDuplicate) {
            printerModel.setReceiptVersionEnglish("*DUPLICATE COPY*"); // Hard coded
            printerModel.setReceiptVersionArabic("*نسخة إضافية*"); // Hard coded Based on Copy
        }
        fetchfromTag(lastTrans.getReservedData62Responce());
        if (!DO_RECON_AMOUNT && !DO_PARTIAL_DOWNLOAD)
            DO_RECON_AMOUNT = getCumilativeAmount(database);
        Logger.v("printerModel --" + printerModel.toString());
        printDe30 = printerModel.getAlpharesponseCode();
        //for E-receipt

        TransactionViewModel.printerModel = printerModel;
//        boolean printstatus = PrinterReceipt.printBill(printerModel, logoBitmap,devicePrinter,context);
////        boolean printstatus = PrinterReceipt.printBill(printerModel, mPrinter, arial, arialBold, arabic, arabicBold, simsun, bitmap1);
//        data.putBoolean(STATUS, printstatus);
        return Result.success(data.build());
    }

    private void fetchfromTag(String de62) {
        DO_PARTIAL_DOWNLOAD = false;
        DO_RECON_AMOUNT = false;
        if (de62 != null)
            if (de62.trim().length() != 0) {
                Logger.v("de62 --" + de62);
                String[] tag06 = de62.split("06");
                Logger.v("de62 --" + tag06.length);
                if (tag06.length != 0) {
                    String innerTag = (tag06.length == 2) ? tag06[1] : tag06[0];
                    Logger.v("tag06[0] --" + innerTag);
                    if (innerTag.length() != 0) {
                        DO_PARTIAL_DOWNLOAD = innerTag.substring(0, 1).equalsIgnoreCase("1");
                    }
                }

                String[] tag08 = de62.split("08");
                Logger.v("de62 --" + tag08.length);
                if (tag08.length != 0) {
                    String innerTag = (tag08.length == 2) ? tag08[1] : tag08[0];
                    Logger.v("tag06[0] --" + innerTag);
                    if (innerTag.length() != 0) {
                        DO_RECON_AMOUNT = innerTag.substring(0, 1).equalsIgnoreCase("1");
                    }
                }
            }
    }

    private String getValue(String s, String type) {
        if (Utils.checkTMSValidation(s, type))
            return "Y";
        else
            return "N";
    }

    private String getValuePin(String s, String type) {
        if (Utils.checkCardValidation(s, type))
            return "Y";
        else
            return "N";
    }

    private String getValueSign(String s, String type) {
        if (Utils.checkCardValidationLine(s, type))
            return "Y";
        else
            return "N";
    }

    private String buildQRCode(TransactionModelEntity lastTrans) {
        StringBuilder qrBuilder = new StringBuilder();
        qrBuilder.append("MTI " + lastTrans.getMti0());
        qrBuilder.append(" PCODE " + lastTrans.getProcessingCode3()); //aakash told
//        qrBuilder.append("00000000"+lastTrans.getAmtTransaction4());
        qrBuilder.append(" TXNAMNT " + getAmount(lastTrans.getAmtTransaction4()));
        qrBuilder.append(" STAN " + lastTrans.getSystemTraceAuditnumber11());
        qrBuilder.append(" L-DATE&TIME " + ByteConversionUtils.formatTranDate("yyyyMMddHHmmss", lastTrans.getTimeLocalTransaction12(), "yyMMddHHmmss"));
        qrBuilder.append(" BID " + lastTrans.getAdditionalDataNational47().substring(0, 4));
        String id = lastTrans.getAdditionalDataNational47();
        String indicator = id.substring(4);
        qrBuilder.append(" SCHEME " + Utils.getCardName(indicator));
        qrBuilder.append(" TID " + lastTrans.getCardAcceptorTemId41());
        qrBuilder.append(" MID " + lastTrans.getCardAcceptorIdCode42());
        qrBuilder.append(" POSCODE " + lastTrans.getPosEntrymode22());
        qrBuilder.append(" FCODE " + lastTrans.getFunctioncode24());
        qrBuilder.append(" MRCODE " + lastTrans.getPosConditionCode25());
        qrBuilder.append(" MCC " + lastTrans.getPosPinCaptureCode26());
//        qrBuilder.append("RRN "+lastTrans.getSystemTraceAuditnumber11());
        qrBuilder.append(" RRN " + lastTrans.getRetriRefNo37());
        qrBuilder.append(" APPCODE " + lastTrans.getAuthIdResCode38());
        qrBuilder.append(" ACTCODE " + lastTrans.getResponseCode39());
        return qrBuilder.toString();
    }

    private String getAmount(String de4) {
//        double amt = AppConfig.EMV.amountValue;
        long amt = Long.parseLong(de4);
        /*Logger.v("AMOUNT -VALL-" + AppConfig.EMV.amountValue);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat formatter = new DecimalFormat("####.00", symbols);
        String finalAmt = formatter.format(amt).replaceAll("\\.", "");
        Logger.v("AMTT -" + finalAmt);
        long amount = (Long.parseLong(finalAmt));
        Logger.v("getAmount --" + String.format("%12d", amount));*/
        return String.format("%012d", (amt));
    }

}
