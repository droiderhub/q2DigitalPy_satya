package com.tarang.dpq2.base;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.tarang.dpq2.R;
import com.tarang.dpq2.base.baseactivities.BaseActivity;
import com.tarang.dpq2.base.jpos_class.ConstantApp;
import com.tarang.dpq2.base.terminal_sdk.AppConfig;
import com.tarang.dpq2.base.terminal_sdk.device.SDKDevice;
import com.tarang.dpq2.base.terminal_sdk.event.SimpleTransferListener;
import com.tarang.dpq2.base.utilities.Utils;
import com.tarang.dpq2.model.MenuModel;
import com.tarang.dpq2.view.activities.ChangeLanguageActivity;
import com.tarang.dpq2.view.activities.DisplaySubMenuData;
import com.tarang.dpq2.view.activities.EnterAmountActivity;
import com.tarang.dpq2.view.activities.EnterRrnActivity;
import com.tarang.dpq2.view.activities.AdminMenuActivity;
import com.tarang.dpq2.view.activities.GPSLocationActivity;
import com.tarang.dpq2.view.activities.LandingPageActivity;
import com.tarang.dpq2.view.activities.ManualCardActivity;
import com.tarang.dpq2.view.activities.MerchantMenuActivity;
import com.tarang.dpq2.view.activities.PasswordActivity;
import com.tarang.dpq2.view.activities.PrintActivity;
import com.tarang.dpq2.view.activities.EnterRrnDateAmountActivity;
import com.tarang.dpq2.view.activities.ReconciliationViewActivity;
import com.tarang.dpq2.view.activities.SplashActivity;
import com.tarang.dpq2.view.activities.SubMenuActivity;
import com.tarang.dpq2.view.activities.SwitchConnectionActivity;
import com.tarang.dpq2.view.activities.TerminalInfoActivity;
import com.tarang.dpq2.view.activities.TransactionActivity;
import com.tarang.dpq2.view.activities.TransactionHistoryActivity;
import com.tarang.dpq2.view.activities.ValidateCardActivity;
import com.tarang.dpq2.view.dialoge.PopupDialoge;
import com.tarang.dpq2.worker.SocketConnectionWorker;


public class MapperFlow implements ConstantApp {
    private static MapperFlow instance = null;

    public MapperFlow() {
    }

    public static MapperFlow getInstance() {
        if (instance == null) {
            instance = new MapperFlow();
        }
        return instance;
    }

    public void moveToLandingPage(Context context) {
        Logger.v("LandingPage --" + 1);
        Intent i = new Intent(context, LandingPageActivity.class);
        context.startActivity(i);
        ((SplashActivity) context).finish();
    }


    public void moveToLandingPage(Context context, boolean clearBackStack, int ii) {
        Utils.setNullDialoge();
        Logger.v("LandingPage --" + ii);
        Intent intent = new Intent(context, LandingPageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        ((BaseActivity) context).finish();
    }

    public void moveToTransactionMenu(Context contex) {

        if (AppManager.getInstance().getInitializationStatus(contex)) {
           // SDKDevice.sdkDevice = null;
            AppManager.getInstance().setDe38("");
            AppManager.getInstance().setDe39("");
            SocketConnectionWorker.TRANSACTION_START_TIME = "";
            AppManager.getInstance().setResponseMTI(null);
            Intent i = new Intent(contex, SubMenuActivity.class);
            i.putExtra(MenuList, new MenuModel.MenuItemHolder(MenuModel.getInstance().getMenu(Menu_Transaction, contex), true));
            i.putExtra(TITLE, contex.getString(R.string.transaction_menu));
            contex.startActivity(i);
        }
    }

    public Utils.DialogeClick dialoge = new Utils.DialogeClick() {
        @Override
        public void onClick() {
            Utils.dismissDialoge();
        }
    };

    public void moveToMerchantMenu(Context contex) {
        Intent i = new Intent(contex, SubMenuActivity.class);
        i.putExtra(MenuList, new MenuModel.MenuItemHolder(MenuModel.getInstance().getMenu(Menu_Merchant, contex), true));
        i.putExtra(TITLE, contex.getString(R.string.merchant_menu));
        contex.startActivity(i);
    }

    public void moveToPasswrod(Context context, boolean admin) {
        Intent intent = new Intent(context, PasswordActivity.class);
        if (admin)
            intent.putExtra(TAG, "ADMIN");
        else
            intent.putExtra(TAG, "MERCHANT");
        intent.putExtra(TITLE, context.getString(R.string.merchant_menu));
        context.startActivity(intent);
    }

    public void moveToAdminMenu(Context contex) {
        Intent i = new Intent(contex, SubMenuActivity.class);
        i.putExtra(MenuList, new MenuModel.MenuItemHolder(MenuModel.getInstance().getMenu(Menu_Admin, contex), true));
        i.putExtra(TITLE, contex.getString(R.string.admin_menu));
        contex.startActivity(i);
    }

    public void moveToSubMenu(Context context, MenuModel.MenuItem tag) {
        Logger.v("moveToSubMenu_mapperflow");
        Intent i = new Intent(context, SubMenuActivity.class);
        if (tag.getMenu_name().equalsIgnoreCase(context.getString(R.string.preauth))){
            i.putExtra(MenuList, new MenuModel.MenuItemHolder(tag.getSubmenu(), true));
        }else{
            i.putExtra(MenuList, new MenuModel.MenuItemHolder(tag.getSubmenu(), false));
        }
        i.putExtra(TITLE, tag.getMenu_name());
        context.startActivity(i);
    }

    public void moveToDisplaySubMenu(Context context, MenuModel.MenuItem tag) {
        Logger.v("DisplaySubMenuData 1");
        Intent i = new Intent(context, DisplaySubMenuData.class);
        i.putExtra(MenuList, new MenuModel.MenuItemHolder(tag.getSubmenu(), false));
        i.putExtra(TITLE, tag.getMenu_name());
        context.startActivity(i);
    }

    public void moveToManualEntry(Context context) {
        Logger.v("Manual Entry --");
        Intent intent = new Intent(context, ManualCardActivity.class);
        context.startActivity(intent);
        ((BaseActivity) context).finish();
    }

    public void moveToDisplaySubMenu(Context context, String title) {
        Logger.v("DisplaySubMenuData 2");
        Intent i = new Intent(context, DisplaySubMenuData.class);
        i.putExtra(TITLE, title);
        context.startActivity(i);
    }

    public void moveToMenuClick(final Context context, MenuModel.MenuItem tag) {
        Intent intent = null;
        boolean isFinsh = false;
        switch (tag.getMenu_tag()) {
            case ConstantApp.PURCHASE:
                intent = new Intent(context, EnterAmountActivity.class);
                isFinsh = true;
                break;
            case ConstantApp.PURCHASE_NAQD:
                intent = new Intent(context, EnterAmountActivity.class);
                intent.putExtra(TAG, tag.getMenu_tag());
                isFinsh = true;
                break;
            case ConstantApp.PURCHASE_REVERSAL:
                isFinsh = true;
                AppManager.getInstance().setReversalManual(true);
                intent = new Intent(context, PrintActivity.class);
                intent.putExtra(TAG, tag.getMenu_tag());
                intent.putExtra(TAG_REVERSAL, true);
                break;
            case ConstantApp.REFUND:
                isFinsh = true;
                intent = new Intent(context, PasswordActivity.class);
                intent.putExtra(TAG, tag.getMenu_tag());
                break;
            case ConstantApp.CASH_ADVANCE:
                isFinsh = true;
                intent = new Intent(context, EnterAmountActivity.class);
                intent.putExtra(TAG, tag.getMenu_tag());
                break;
            case ConstantApp.PRE_AUTHORISATION:
                isFinsh = true;
                intent = new Intent(context, EnterAmountActivity.class);
                break;
            case ConstantApp.PRE_AUTHORISATION_VOID:
                //sub menu
                isFinsh = true;
                intent = new Intent(context, EnterRrnDateAmountActivity.class);
                intent.putExtra(TAG, tag.getMenu_tag());
                break;
            case ConstantApp.PRE_AUTHORISATION_EXTENSION:
                isFinsh = true;
                intent = new Intent(context, EnterRrnDateAmountActivity.class);
                intent.putExtra(TAG, tag.getMenu_tag());
                break;
            case ConstantApp.PURCHASE_ADVICE:
                // Sub menu
                break;
            case ConstantApp.PURCHASE_ADVICE_MANUAL:
                isFinsh = true;
                intent = new Intent(context, ManualCardActivity.class);
                intent.putExtra(TAG, tag.getMenu_tag());
                break;
            case ConstantApp.PURCHASE_ADVICE_FULL:
                isFinsh = true;
                intent = new Intent(context, EnterRrnDateAmountActivity.class);
                intent.putExtra(TAG, tag.getMenu_tag());
                break;
            case ConstantApp.PURCHASE_ADVICE_PARTIAL:
                isFinsh = true;
                intent = new Intent(context, EnterRrnDateAmountActivity.class);
                intent.putExtra(TAG, tag.getMenu_tag());
                break;

            // Admin Menu
            case ConstantApp.REGISTRATION:
                if (Utils.isInternetAvailable(context))
                    intent = new Intent(context, AdminMenuActivity.class);
                break;
            case ConstantApp.SET_PARAMATERS:
                //Sub menu
                break;
            case ConstantApp.EDIT_IP:
                new PopupDialoge(context).createEditText(MenuModel.getInstance().submenuSetParameters(context).get(9));
                break;
            case ConstantApp.FULL_DOWNLOAD:
                if (Utils.isInternetAvailable(context))
                intent = new Intent(context, AdminMenuActivity.class);
                break;
            case ConstantApp.PARTIAL_DOWNLOAD:
                if (Utils.isInternetAvailable(context))
                intent = new Intent(context, AdminMenuActivity.class);
                break;
            case ConstantApp.SAF_VIEW:
                AppManager.getInstance().setHistoryView(ConstantApp.SAF_HISTORY);
                AppManager.getInstance().setAdminSafHistoryView("ADMIN");
                intent = new Intent(context, TransactionHistoryActivity.class);
                break;
            case ConstantApp.DEL_SAF_FILE:
                break;
            case ConstantApp.TRSM_EDIT_FILE:
                new PopupDialoge(context).createEditText(MenuModel.getInstance().submenuSetParameters(context).get(6));
                break;
            case ConstantApp.SET_DATE_TIME:
                break;
            case ConstantApp.FORMAT_FILESYS:
                intent = new Intent(context, AdminMenuActivity.class);
                break;
            case ConstantApp.PRINT_TMS_DATA:
                // Sub menu
                break;
            case ConstantApp.CHANGE_PASSWORD_ADMIN:
                intent = new Intent(context, MerchantMenuActivity.class);
                break;
            case ConstantApp.RESET_PASSWORD:
                new PopupDialoge(context).createWithResetButtons(tag);
                break;
            case ConstantApp.HOST_SETTING:
                //sub menu
                break;
            case ConstantApp.MANAGER_MENU:
                //sub menu
                break;
            case ConstantApp.KEY_INJECTION:
                intent = new Intent(context, AdminMenuActivity.class);
                break;
            case ConstantApp.DEBUG_MODE:
                showDebugAleart(context);
                break;
            case ConstantApp.MERCHANT_PORTAL:
                showPortalAleart(context);
                break;

            // Merchant Menu
            case ConstantApp.DUPLICATE:
                intent = new Intent(context, MerchantMenuActivity.class);
                break;
            case ConstantApp.RECONCILIATION:
                if (Utils.isInternetAvailable(context))
                    intent = new Intent(context, MerchantMenuActivity.class);
                break;
            case ConstantApp.SANPSHOT_TOTAL:
                intent = new Intent(context, MerchantMenuActivity.class);
                break;
            case ConstantApp.RUNNING_TOTAL:
                intent = new Intent(context, MerchantMenuActivity.class);
                break;
            case ConstantApp.RECONCILE_SETUP:
                intent = new Intent(context, MerchantMenuActivity.class);
                break;
            case ConstantApp.HISTORY_VIEW:
                // Sub menu
                break;
            case ConstantApp.LAST_EMV:
                break;
            case ConstantApp.CHANGE_PASSWORD:
                intent = new Intent(context, MerchantMenuActivity.class);
                break;
            case ConstantApp.TERMINFO_MENU:
                // Sub menu
                break;
            case ConstantApp.SIM_NUMBER:
                intent = new Intent(context, MerchantMenuActivity.class);
                break;
            case ConstantApp.DE_SAF_ALL_FILE:
                intent = new Intent(context, MerchantMenuActivity.class);
                break;
            case ConstantApp.SWITCH_CONNECTION:
//                Utils.switchConnection(context, tag);
                intent = new Intent(context, SwitchConnectionActivity.class);
                break;
            case ConstantApp.SET_GPS_LOCATION:
                intent = new Intent(context, GPSLocationActivity.class);
                break;
            case ConstantApp.SELECT_LANGUAGE:
                // Utils.changeLanguage(context);
                intent = new Intent(context, ChangeLanguageActivity.class);

                break;

            // Print TMS Option from Admin Menu
            case ConstantApp.TMS_RETAILER_DATA:
                moveToDisplaySubMenu(context, tag.getMenu_name());
                break;
            case ConstantApp.TMS_CARD_DATA:
                break;
            case ConstantApp.TMS_HOST_MSG_DATA:
                break;
            case ConstantApp.TMS_PUB_FEY_DATA:
                break;
            case ConstantApp.TMS_COMM_DATA:
                break;
            case ConstantApp.TMS_AID_LIST:
                moveToDisplaySubMenu(context, tag.getMenu_name());
                break;
            case ConstantApp.TMS_AID_DATA:
                break;
            case ConstantApp.TMS_EXPORT_USB:
                break;
            case ConstantApp.TMS_DEVICE_SPEC:
                break;
            case ConstantApp.HSTNG_PRIORITY:
                Utils.alertPriorityDialoge(context);
                break;
            case ConstantApp.HSTNG_TLS:
                new PopupDialoge(context).createEditText(MenuModel.getInstance().submenuHostSettings(context).get(1));
                break;
            case ConstantApp.HSTNG_PING_SETUP:
                Utils.checkInternetPing(context);
                break;

            // submenu ManagerMenu from Admin Menu
            case ConstantApp.CHK_DIGIT_CALC:
                intent = new Intent(context, ValidateCardActivity.class);
                break;
            case ConstantApp.REMOTE_DOWNLOAD:
                break;

            // submenu Reconsilation setup from Merchant Menu
            case ConstantApp.CURRENT_STATUS:
                break;
            case ConstantApp.RECONSILATION_TIME:
                break;
            case ConstantApp.ENABLE_DISABLE_RECON:
                break;

            // TerminalMenu Submenu  setup from Merchant Menu
            case ConstantApp.TERMINAL_INFO:
                intent = new Intent(context, TerminalInfoActivity.class);
                break;
            case ConstantApp.PRINT_SYS_INFO:
                break;
            case ConstantApp.TERMINAL_VENDER_PING:
                Utils.checkInternetPing(context);
                break;

            // submenu Reconsilation setup from Merchant Menu
            case ConstantApp.TRANSACTION_VIEW:
                AppManager.getInstance().setHistoryView(ConstantApp.TRANSACTION_VIEW);
                AppManager.getInstance().setAdminSafHistoryView("TRANSACTION_HISTORY");
                intent = new Intent(context, TransactionHistoryActivity.class);
                break;
            case ConstantApp.RECONCILE_VIEW:
                intent = new Intent(context, ReconciliationViewActivity.class);
                break;
            case ConstantApp.SAF_HISTORY:
                AppManager.getInstance().setHistoryView(ConstantApp.SAF_HISTORY);
                AppManager.getInstance().setAdminSafHistoryView("MERCHANT");
                intent = new Intent(context, TransactionHistoryActivity.class);
                break;
            case ConstantApp.DELETE_HISTORY:
                if (!AppManager.getInstance().getBoolean(ConstantApp.DELETE_HISTORY)) {
                    Utils.alertDialogShow(context, context.getString(R.string.delete_history_qstn), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AppManager.getInstance().setBoolean(ConstantApp.DELETE_HISTORY, true);
                            AppManager.getInstance().setAdminSafHistoryView("DELETE_TXN_HISTORY");
                            context.startActivity(new Intent(context, TransactionHistoryActivity.class));
                            Utils.dismissDialoge();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Utils.dismissDialoge();
                        }
                    });
                } else
                    Utils.alertDialogShow(context, context.getString(R.string.no_data), false);
                break;


        }
        if (intent != null) {
            context.startActivity(intent);
        }
        if (isFinsh)
            ((BaseActivity) context).finish();

    }

    private void showDebugAleart(final Context context) {
        final boolean debug = AppManager.getInstance().isDebugEnabled();
        Utils.alertDialogShow(context, (debug) ? context.getString(R.string.disable_debug_mode) : context.getString(R.string.enable_debug_mode), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManager.getInstance().setDebugmode(!debug);
                Utils.dismissDialoge();
                ((BaseActivity) context).finish();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.dismissDialoge();
            }
        });
    }

    /*private void showPortalAleart(final Context context) {
        final boolean debug = AppManager.getInstance().isMerchantPoratalEnable();
        Utils.alertDialogShow(context, (debug) ? context.getString(R.string.merchant_portal_disable) : context.getString(R.string.merchant_portal_enable), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManager.getInstance().setMerchantPortal(!debug);
                Utils.dismissDialoge();
                ((BaseActivity) context).finish();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.dismissDialoge();
            }
        });
    }*/

    public void moveToPrintScreen(Context contex, String card) { //sampleString
        Intent i = new Intent(contex, PrintActivity.class);
        contex.startActivity(i);
        if (!(contex instanceof PrintActivity))
            ((BaseActivity) contex).finish();
    }

    public void moveToPrintScreen(Context contex, String accNo, String expDate) {
        Intent i = new Intent(contex, PrintActivity.class);
        i.putExtra(TAG_CARD_MANUAL, true);
        i.putExtra(TAG1, accNo);
        i.putExtra(TAG2, expDate);
        contex.startActivity(i);
        if (!(contex instanceof PrintActivity))
            ((BaseActivity) contex).finish();
    }

    public void moveToPrintScreen(Context contex, boolean bool) {
        Intent i = new Intent(contex, PrintActivity.class);
        i.putExtra(ConstantApp.TAG_FALLBACK, bool);
        contex.startActivity(i);
        if (!(contex instanceof PrintActivity))
            ((BaseActivity) contex).finish();
    }

    public void moveToPrintScreen(Context contex) {
        Intent i = new Intent(contex, PrintActivity.class);
        contex.startActivity(i);
        if (!(contex instanceof PrintActivity))
            ((BaseActivity) contex).finish();
    }

    public void moveToPrintScreenFallback(Context contex) {
        Intent i = new Intent(contex, PrintActivity.class);
        i.putExtra(ConstantApp.TAG_FALLBACK,true);
        contex.startActivity(i);
        if (!(contex instanceof PrintActivity))
            ((BaseActivity) contex).finish();
    }

    public void moveToPrintScreenWaveAgain(Context contex) {
        Intent i = new Intent(contex, PrintActivity.class);
        i.putExtra(ConstantApp.DO_WAVE_AGAIN,true);
        contex.startActivity(i);
        if (!(contex instanceof PrintActivity))
            ((BaseActivity) contex).finish();
    }

    public void moveEnterAmount(Context context) { //sampleString
        AppManager.getInstance().setMenuItem(MenuModel.getInstance().getTransactionMenu(context).get(0));
        Intent i = new Intent(context, EnterAmountActivity.class);
        i.putExtra(TAG_CARD, "mscard");
        context.startActivity(i);
    }

    public void moveToEnterRrnActivity(Context context, String tag) {
        Intent intent = new Intent(context, EnterRrnActivity.class);
        intent.putExtra(TAG, tag);
        context.startActivity(intent);
    }

    public void moveToReEnterAmount(Context context) {
        Intent i = new Intent(context, EnterAmountActivity.class);
        Logger.v("SimpleTransferListener.isCashBackAmountExceed --" + SimpleTransferListener.isCashBackAmountExceed);
        if (SimpleTransferListener.isCashBackAmountExceed) {
            AppConfig.EMV.amountValue = AppConfig.EMV.amountValue - AppConfig.EMV.amtCashBack;
            i.putExtra(TAG, Cashback_Amount);
            i.putExtra(Purchase_Amount, (AppConfig.EMV.amountValue));
//            AppConfig.EMV.amountValue = (amountValue);
        } else
            i.putExtra(TAG, AppManager.getInstance().getMenuItem().getMenu_tag());
        context.startActivity(i);
        ((BaseActivity) context).finish();
    }

    public void moveToEnterRrnDateAmountActivity(Context context, String accNo, String expDate, String tag) {
        Intent intent = new Intent(context, EnterRrnDateAmountActivity.class);
        intent.putExtra(TAG, tag);
        intent.putExtra(TAG1, accNo);
        intent.putExtra(TAG2, expDate);
        context.startActivity(intent);
    }

    public void moveToEnterRrnDateAmountActivity(Context context, String tag) {
        Intent intent = new Intent(context, EnterRrnDateAmountActivity.class);
        intent.putExtra(TAG, tag);
        context.startActivity(intent);
    }

    public void startOnlinePinInput(Context context, String accNo) {
//        packet.setOnlinePin(true);
//        context.startActivity(new Intent(context, EnterPinActivity.class).putExtra("accNo", accNo));
    }

    public void startMSPinInput(Context context, String accNo) {
//        packet.setOnlinePin(true);
//        context.startActivity(new Intent(context, EnterPinActivity.class).putExtra("accNo", accNo).putExtra("printer_tag", true));
    }

    public void startOfflinePinInput(Context context, byte[] modulus, byte[] exponent, String atmpt) {
//        packet.setOnlinePin(false);
//        Intent intent = new Intent(context, EnterOfflinePinActivity.class);
//        intent.putExtra("modulus", modulus);
//        intent.putExtra("exponent", exponent);
//        intent.putExtra("ATMPT", atmpt);
//        context.startActivity(intent);
    }


    public void moveToReconsilation(Context context) {
        AppManager.getInstance().setMenuItem(new MenuModel.MenuItem(context.getString(R.string.reconcilation), ConstantApp.RECONCILIATION));
        Intent intent = new Intent(context, MerchantMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        context.startActivity(intent);
        ((BaseActivity) context).finish();
    }

    public void moveToFullDownload(Context context) {
        AppManager.getInstance().setMenuItem(new MenuModel.MenuItem(context.getString(R.string.full_download), ConstantApp.FULL_DOWNLOAD, R.drawable.full_download, true));
        Intent intent = new Intent(context, AdminMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        context.startActivity(intent);
        ((BaseActivity) context).finish();
    }

    private void showPortalAleart(final Context context) {
        final boolean debug = AppManager.getInstance().isMerchantPoratalEnable();
        Utils.alertDialogShow(context, (debug) ? context.getString(R.string.merchant_portal_disable) : context.getString(R.string.merchant_portal_enable), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManager.getInstance().setMerchantPortal(!debug);
                Utils.dismissDialoge();
                ((BaseActivity) context).finish();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.dismissDialoge();
            }
        });
    }


    private static void resetPassword(final Context context) {
        Utils.alertYesDialogShow(context, "Are you sure you want to Reset Password", new View.OnClickListener() {
            @Override
            public void onClick(View dialog) {
                AppManager.getInstance().resetAdminPassword();
                Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
                Utils.dismissDialoge();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View dialog) {
//                dialog.dismiss();
                Utils.dismissDialoge();
            }
        });

    }

    public void moveToTransactionActivity(Context context) {
        Intent intent = new Intent(context, TransactionActivity.class);
        intent.putExtra("SHOW_AGAIN", true);
        context.startActivity(intent);
        ((BaseActivity) context).finish();
    }
    public void movePurchase(Context context, String amount) {
        if(!Utils.checkPrinterPaper(context)) {
            AppManager.getInstance().setDe38("");
            AppManager.getInstance().setDe39("");
            SocketConnectionWorker.TRANSACTION_START_TIME = "";
            AppManager.getInstance().setResponseMTI(null);
            AppManager.getInstance().setMenuItem(MenuModel.getInstance().getTransactionMenu(context).get(0));
            Toast toast = Toast.makeText(context, context.getString(R.string.please_enter_amount), Toast.LENGTH_SHORT);
            if (!amount.equalsIgnoreCase("") && amount.replaceAll(",", "").trim().length() != 0 && amount.replaceAll("\\.", "").trim().length() != 0) {
                Double amountValue = Double.valueOf(amount.replaceAll(",", ""));
                Logger.v("AMOUNT -VALL-" + amount);
                if (amountValue > 0) {
                    AppConfig.EMV.amtCashBack = 0;
                    AppConfig.EMV.amountValue = (amountValue);
                    Logger.v("AMOUNT -VALL-" + AppConfig.EMV.amountValue);
                    Intent intent = new Intent(context, TransactionActivity.class);
                    intent.putExtra(Total_Amount, amountValue);
                    AppConfig.EMV.amountValue = (amountValue);
                    context.startActivity(intent);
                } else {
                    toast.show();
                }
            } else {
                toast.show();
            }
        }
    }
}
