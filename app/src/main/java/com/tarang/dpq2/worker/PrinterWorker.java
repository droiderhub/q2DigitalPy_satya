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
            int dataset = getInputData().getInt(KEY_VALUE_DATA, 0);
            Logger.v("dataset --" + dataset);
            if (dataset == 0) {
                RetailerDataModel retailerDataModel = AppManager.getInstance().getRetailerDataModel();
                modelList.add(new KeyValueModel(ConstantAppValue.doted_line));
                modelList.add(new KeyValueModel("RETAILER PARAMETER"));
                modelList.add(new KeyValueModel(ConstantAppValue.doted_line));
                modelList.add(new KeyValueModel(context.getString(R.string.reconsilation_time), retailerDataModel.getReconcillationTime()));
                modelList.add(new KeyValueModel(context.getString(R.string.retailer_name_arabic), Utils.changeArabic(retailerDataModel.getRetailerNameInArabic())));
                modelList.add(new KeyValueModel(context.getString(R.string.retiler_name_english), retailerDataModel.getRetailerNameEnglish()));
                modelList.add(new KeyValueModel(context.getString(R.string.retailer_number), AppManager.getInstance().getString(ConstantApp.SPRM_PHONE_NUMBER)));
                modelList.add(new KeyValueModel(context.getString(R.string.retailer_address), retailerDataModel.getRetailerAddress1English()));
            } else if (dataset == 1) {
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
            } else if (dataset == 2) {
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
            } else if (dataset == 3) {
                List<TMSPublicKeyModelEntity> dataList = database.getTMSPublicKeyModelDao().getCapKeys();
                modelList.add(new KeyValueModel(ConstantAppValue.doted_line));
                modelList.add(new KeyValueModel("PUB KEY DATA"));
                modelList.add(new KeyValueModel(ConstantAppValue.doted_line));
                for (int i = 0; i < dataList.size(); i++) {
                    modelList.add(new KeyValueModel(context.getString(R.string.no_) + i));
                    modelList.add(new KeyValueModel(context.getString(R.string.key_idex), "" + Integer.parseInt(dataList.get(i).getKeyIndex(), 16)));
                    modelList.add(new KeyValueModel(context.getString(R.string.rid), dataList.get(i).getRID()));
                }
            } else if (dataset == 4) {
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
            } else if (dataset == 5) {
                modelList.add(new KeyValueModel(ConstantAppValue.doted_line));
                modelList.add(new KeyValueModel("AID LIST"));
                modelList.add(new KeyValueModel(ConstantAppValue.doted_line));
                List<String> aidList1 = AppManager.getInstance().getAidList();
                for (int i = 0; i < aidList1.size(); i++) {
                    if (aidList1.get(i).trim().length() != 0) {
                        modelList.add(new KeyValueModel(context.getString(R.string.aid_) + (modelList.size()), aidList1.get(i)));
                    }
                }
            } else if (dataset == 6) {
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
            } else if (dataset == 7) {
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
            } else if (dataset == 8) {

            } else if (dataset == 9) {
                modelList.add(new KeyValueModel(context.getString(R.string.contact_application)));
                modelList.add(new KeyValueModel(context.getString(R.string.mada)));
                modelList.add(new KeyValueModel(context.getString(R.string.visa)));
                modelList.add(new KeyValueModel(context.getString(R.string.master_card)));
                modelList.add(new KeyValueModel(context.getString(R.string.contactless_application)));
                modelList.add(new KeyValueModel(context.getString(R.string.mada)));
                modelList.add(new KeyValueModel(context.getString(R.string.visa)));
                modelList.add(new KeyValueModel(context.getString(R.string.master_card)));

            }
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
