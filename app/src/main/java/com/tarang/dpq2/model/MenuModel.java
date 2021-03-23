package com.tarang.dpq2.model;

import android.content.Context;

import com.tarang.dpq2.R;
import com.tarang.dpq2.base.AppInit;
import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.jpos_class.ConstantApp;
import com.wizarpos.emvsample.constant.Constant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class MenuModel implements Serializable {
    private static MenuModel instance = null;


    public MenuModel() {
    }

    public List<MenuItem> getMenu(int tag, Context context) {

        switch (tag) {
            case ConstantApp.Menu_Transaction:
                return getTransactionMenu(context);
            case ConstantApp.Menu_Merchant:
                return getMerchantMenu(context);
            case ConstantApp.Menu_Admin:
                return getAdminMenu(context);
        }
        return null;
    }

    public static MenuModel getInstance() {
        if (instance == null) {
            instance = new MenuModel();
        }
        return instance;
    }

    public List<MenuItem> getTransactionMenu(Context context) {
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem(context.getString(R.string.purhase), ConstantApp.PURCHASE,R.drawable.sale_icon));
        items.add(new MenuItem(context.getString(R.string.purchase_with_naqd), ConstantApp.PURCHASE_NAQD,R.drawable.naqd_icon));
        items.add(new MenuItem(context.getString(R.string.reversal), ConstantApp.PURCHASE_REVERSAL,R.drawable.reversal));
        items.add(new MenuItem(context.getString(R.string.cash_advice), ConstantApp.CASH_ADVANCE,R.drawable.cash_adv));
        items.add(new MenuItem(context.getString(R.string.refund), ConstantApp.REFUND,R.drawable.refund_icon));
       // items.add(new MenuItem(context.getString(R.string.preauth), ConstantApp.PRE_AUTHORISATION,R.drawable.preauth));
        if(AppInit.VERSION_6_0_5){
            items.add(new MenuItem(context.getString(R.string.preauth), ConstantApp.PRE_AUTHORISATION, R.drawable.preauth));
            items.add(new MenuItem(context.getString(R.string.preauth_advice), ConstantApp.PURCHASE_ADVICE_PARTIAL, R.drawable.advice)); //TODO: ISO 6.0.5
        }else {
            items.add(new MenuItem(context.getString(R.string.preauth), ConstantApp.PRE_AUTHORISATION, submenuAutherization(context), R.drawable.preauth));
            items.add(new MenuItem(context.getString(R.string.preauth_advice), ConstantApp.PURCHASE_ADVICE, submenuPartialFull(context), R.drawable.advice));
        }
        return items;
    }

    private List<MenuItem> submenuAutherization(Context context) {
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem(context.getString(R.string.preauth), ConstantApp.PRE_AUTHORISATION, R.drawable.preauth));
        items.add(new MenuItem(context.getString(R.string.preauth_void), ConstantApp.PRE_AUTHORISATION_VOID, R.drawable.preauth_void));
        items.add(new MenuItem(context.getString(R.string.preauth_extension), ConstantApp.PRE_AUTHORISATION_EXTENSION, R.drawable.preauth_exten));
        return items;
    }

    public List<MenuItem> getMerchantMenu(Context context) {
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem(context.getString(R.string.duplicate), ConstantApp.DUPLICATE,R.drawable.duplicate,true));
        items.add(new MenuItem(context.getString(R.string.reconcilation), ConstantApp.RECONCILIATION,R.drawable.reconcilation,true));
        items.add(new MenuItem(context.getString(R.string.snapshot_total), ConstantApp.SANPSHOT_TOTAL,R.drawable.snapshot_total,true));
        items.add(new MenuItem(context.getString(R.string.running_totla), ConstantApp.RUNNING_TOTAL,R.drawable.running_total,true));
        items.add(new MenuItem(context.getString(R.string.reconcile_setup), ConstantApp.RECONCILE_SETUP,R.drawable.reconcile_setup,true));
        items.add(new MenuItem(context.getString(R.string.history_view), ConstantApp.HISTORY_VIEW,submenuHistoryView(context),R.drawable.history,true));
        items.add(new MenuItem(context.getString(R.string.last_emv), ConstantApp.LAST_EMV,true,R.drawable.preauth,true));
        items.add(new MenuItem(context.getString(R.string.change_password), ConstantApp.CHANGE_PASSWORD,R.drawable.admin_pwd));
        items.add(new MenuItem(context.getString(R.string.terminfo_menu), ConstantApp.TERMINFO_MENU,submenuTerminalMenu(context),R.drawable.terminal_info));
        items.add(new MenuItem(context.getString(R.string.sim_number), ConstantApp.SIM_NUMBER,R.drawable.d_info));
        items.add(new MenuItem(context.getString(R.string.de_saf_all_file), ConstantApp.DE_SAF_ALL_FILE,R.drawable.admin_del,true));
        items.add(new MenuItem(context.getString(R.string.switch_connection), ConstantApp.SWITCH_CONNECTION,R.drawable.admin_server_set));
        items.add(new MenuItem(true,context.getString(R.string.set_gps_location), ConstantApp.SET_GPS_LOCATION,R.drawable.admin_param));
    //    items.add(new MenuItem(context.getString(R.string.select_language), ConstantApp.SELECT_LANGUAGE,R.drawable.translation));
        if (AppManager.getInstance().isMerchantPoratalEnable())
            items.add(new MenuItem(context.getString(R.string.mprotral_batch_upload), ConstantApp.MPORTAL_BATCH_UPLOAD, R.drawable.saf_view));

        items.add(new MenuItem("User Guide \n  دليل المستخدم", ConstantApp.USER_GUIDE_, userGuide(context), R.drawable.saf_view));
        return items;
    }

    private List<MenuItem> userGuide(Context context) {
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem("Operators Transactions \n معاملات المشغلين", ConstantApp.UG_OPERATOR_TRANSACTION,getSubMenutransactionHint(context)));
        items.add(new MenuItem("Merchant \n عمليات التاجر", ConstantApp.UG_MERCHANT,true));
        items.add(new MenuItem("RRN \n الرقم المرجعي للاسترداد RRN", ConstantApp.UG_RRN,true));
        items.add(new MenuItem("Response Code \n رمز الإستجابة", ConstantApp.UG_RESPONCE_CODE,true));
        items.add(new MenuItem("Common Error Messages \n رسائل الخطأ الشائعة", ConstantApp.UG_COMMON_ERROR_MSG,true));
        items.add(new MenuItem("Paper Roll Installation \n تركيب الورق", ConstantApp.UG_PAPER_ROLL_INSTALL,true));
        items.add(new MenuItem("Power ON/OFF \n التشغيل والإيقاف", ConstantApp.UG_POWER_ON_OFF,true));
        items.add(new MenuItem("Card Reader Operation \n عمليات قارئ البطاقات", ConstantApp.UG_CARD_READER,true));
        items.add(new MenuItem("Common Troubleshooting \n  الأخطاء الشائعة", ConstantApp.UG_COMMON_TROUBLE_SHOOT,true));
        items.add(new MenuItem("Caution and Safety Insts \n  تعليمات الحذر والسلامة", ConstantApp.UG_CAUTION_SAFETY,true));
        return items;
    }

    private List<MenuItem> getSubMenutransactionHint(Context context) {
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem(context.getString(R.string.purhase), ConstantApp.UGO_PURCHASE, getSubMenuModeHint(context)));
        items.add(new MenuItem(context.getString(R.string.purchase_with_naqd), ConstantApp.UGO_PURCHASE_NAQD, true));
        items.add(new MenuItem(context.getString(R.string.reversal), ConstantApp.UGO_PURCHASE_REVERSAL, true));
        items.add(new MenuItem(context.getString(R.string.preauth_advice), ConstantApp.UGO_PURCHASE_ADVICE_PARTIAL, true));
        items.add(new MenuItem(context.getString(R.string.cash_advice), ConstantApp.UGO_CASH_ADVANCE, true));
        items.add(new MenuItem(context.getString(R.string.preauth), ConstantApp.UGO_PRE_AUTHORISATION, true));
        items.add(new MenuItem(context.getString(R.string.refund), ConstantApp.UGO_REFUND, true));
        return items;
    }

    private List<MenuItem> getSubMenuModeHint(Context context) {
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem("Chip Card \n  بطاقة الشريحة", ConstantApp.UGO_MODE_CHIP_CARD,true));
        items.add(new MenuItem("Magnetic Stripe Card \n بطاقة ممغنطة", ConstantApp.UGO_MODE_MAGNETIC_STRIPE,true));
        items.add(new MenuItem("Contactless \n عن بعد", ConstantApp.UGO_MODE_CONACTLESS,true));
        items.add(new MenuItem("Manual Entry \n ادخال يدوي", ConstantApp.UGO_MODE_MANUAL_ENTRY,true));
        return items;
    }

    public List<MenuItem> getAdminMenu(Context context) {
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem(context.getString(R.string.registration), ConstantApp.REGISTRATION,R.drawable.admin_reg));
        items.add(new MenuItem(context.getString(R.string.set_parameters), ConstantApp.SET_PARAMATERS,true,submenuSetParameters(context),R.drawable.admin_param));
        items.add(new MenuItem(context.getString(R.string.full_download), ConstantApp.FULL_DOWNLOAD,R.drawable.full_download,true));
        items.add(new MenuItem(context.getString(R.string.partial_download), ConstantApp.PARTIAL_DOWNLOAD,R.drawable.download,true));
        items.add(new MenuItem(context.getString(R.string.saf_view), ConstantApp.SAF_VIEW,R.drawable.saf_view,true));
        items.add(new MenuItem(true,context.getString(R.string.trsm_id_edit), ConstantApp.SPRM_TRSM_ID,R.drawable.edit_trsm_id));
        items.add(new MenuItem(context.getString(R.string.format_filesys), ConstantApp.FORMAT_FILESYS,R.drawable.admin_del));
        items.add(new MenuItem(context.getString(R.string.print_tms_data), ConstantApp.PRINT_TMS_DATA,submenuPrintTmsData(context),R.drawable.admin_data,true));
        items.add(new MenuItem(context.getString(R.string.change_password_admin), ConstantApp.CHANGE_PASSWORD_ADMIN,R.drawable.admin_pwd));
        items.add(new MenuItem(context.getString(R.string.reset_password), ConstantApp.RESET_PASSWORD,R.drawable.pwd_reset));
        items.add(new MenuItem(context.getString(R.string.host_setting), ConstantApp.HOST_SETTING,submenuHostSettings(context),R.drawable.admin_server_set));
        items.add(new MenuItem(context.getString(R.string.manager_menu), ConstantApp.MANAGER_MENU,submenuManagerMenu(context),R.drawable.manage_menu));
        if (!AppManager.getInstance().isDebugEnabled())
            items.add(new MenuItem(context.getString(R.string.debug_mode_enable), ConstantApp.DEBUG_MODE, R.drawable.manage_menu));
        else
            items.add(new MenuItem(context.getString(R.string.debug_mode_disable), ConstantApp.DEBUG_MODE , R.drawable.manage_menu));

        if (!AppManager.getInstance().isMerchantPoratalEnable())
            items.add(new MenuItem(context.getString(R.string.merchant_portal_enable), ConstantApp.MERCHANT_PORTAL, R.drawable.admin_server_set));
        else
            items.add(new MenuItem(context.getString(R.string.merchant_portal_disable), ConstantApp.MERCHANT_PORTAL, R.drawable.admin_server_set));
//        if(chckStatus(context))
        if (!AppInit.HITTING_LIVE_SERVER)
            items.add(new MenuItem(context.getString(R.string.inject_keys), ConstantApp.KEY_INJECTION, R.drawable.ic_vpn_key_black_24dp));
        return items;
    }

    private boolean chckStatus(Context context) {
//        SDKDevice sdk = SDKDevice.getInstance(context);
//        if(sdk.isDeviceAlive()) {
//            byte[] ksn = sdk.getK21Pininput().getDukptKsn(AppConfig.Pin.DUKPT_DES_INDEX);
//            if (ksn == null || ksn.length == 0)
//                return true;
//        }
        return false;
    }


    public List<MenuItem> submenuPartialFull(Context context) {
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem(context.getString(R.string.full), ConstantApp.PURCHASE_ADVICE_FULL));
        items.add(new MenuItem(context.getString(R.string.partial), ConstantApp.PURCHASE_ADVICE_PARTIAL));
      //  items.add(new MenuItem(context.getString(R.string.manual), ConstantApp.PURCHASE_ADVICE_MANUAL));
        return items;
    }

    public List<MenuItem> submenuPreAuthVoidPartialFull(Context context) {
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem(context.getString(R.string.full), ConstantApp.PRE_AUTHORISATION_VOID_FULL));
        items.add(new MenuItem(context.getString(R.string.partial), ConstantApp.PRE_AUTHORISATION_VOID_PARTIAL));
        return items;
    }

    public List<MenuItem> submenuSetParameters(Context context) {
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem(context.getString(R.string.phone_number), ConstantApp.SPRM_PHONE_NUMBER));
        items.add(new MenuItem(context.getString(R.string.vender_id), ConstantApp.SPRM_VENDER_ID));
        items.add(new MenuItem(context.getString(R.string.terminal_type), ConstantApp.SPRM_TERMINAL_TYPE));
//        items.add(new MenuItem(context.getString(R.string.terminal_serial_number), Constant.SPRM_TERMINAL_SERIAL_NUM));
        items.add(new MenuItem(context.getString(R.string.key_issuer_number), ConstantApp.SPRM_KEY_ISSUE_));
        items.add(new MenuItem(context.getString(R.string.vendder_key1), ConstantApp.SPRM_VENDOR_KEY1));
        items.add(new MenuItem(context.getString(R.string.sama_key1), ConstantApp.SPRM_SAMA_KEY1));
        items.add(new MenuItem(context.getString(R.string.trsm_id), ConstantApp.SPRM_TRSM_ID));
        items.add(new MenuItem(context.getString(R.string.nii_id), ConstantApp.SPRM_NII_ID));
        items.add(new MenuItem(context.getString(R.string.tls), ConstantApp.HSTNG_TLS));
        items.add(new MenuItem(context.getString(R.string.ip_config), ConstantApp.SPRM_IP_CONFIG));
        items.add(new MenuItem(context.getString(R.string.port), ConstantApp.SPRM_PORT));
        items.add(new MenuItem(context.getString(R.string.mip_config), ConstantApp.MERCHANT_IP_CONFIG));
        items.add(new MenuItem(context.getString(R.string.mport), ConstantApp.MERCHANT_PORT));
        return (items);
    }

    public List<MenuItem> submenuHostSettings(Context context) {
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem("APN"+AppManager.getInstance().getGPRSAPN1(), ConstantApp.HSTNG_GPRS_CONNECTED));
        items.add(new MenuItem(context.getString(R.string.priority), ConstantApp.HSTNG_PRIORITY));
        items.add(new MenuItem(context.getString(R.string.tls), ConstantApp.HSTNG_TLS));
//        items.add(new MenuItem(context.getString(R.string.gprs_connected_disconnect), Constant.HSTNG_GPRS_CONNECTED));
//        items.add(new MenuItem(context.getString(R.string.gprs_configuration), Constant.HSTNG_GPRS_CONFG));
//        items.add(new MenuItem(context.getString(R.string.wifi_setup), Constant.HSTNG_WIFI_SETUP));
//        items.add(new MenuItem(context.getString(R.string.gprs_operator), Constant.HSTNG_GPRS_OPERATOR));
//        items.add(new MenuItem(context.getString(R.string.debug_option), Constant.HSTNG_DEBUG_OPTION));
        items.add(new MenuItem(context.getString(R.string.ping_setup), ConstantApp.HSTNG_PING_SETUP));
        return (items);
    }

    public List<MenuItem> submenuPrintTmsData(Context context) {
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem(context.getString(R.string.retailer_data), ConstantApp.TMS_RETAILER_DATA,true));
        items.add(new MenuItem(context.getString(R.string.card_data), ConstantApp.TMS_CARD_DATA,true));
        items.add(new MenuItem(context.getString(R.string.host_msg_data), ConstantApp.TMS_HOST_MSG_DATA,true));
        items.add(new MenuItem(context.getString(R.string.pub_key_data), ConstantApp.TMS_PUB_FEY_DATA,true));
        items.add(new MenuItem(context.getString(R.string.comm_data), ConstantApp.TMS_COMM_DATA,true));
        items.add(new MenuItem(context.getString(R.string.aid_list), ConstantApp.TMS_AID_LIST,true));
        items.add(new MenuItem(context.getString(R.string.aid_data), ConstantApp.TMS_AID_DATA,true));
//        items.add(new MenuItem(context.getString(R.string.tms_export_usb), Constant.TMS_EXPORT_USB));
        items.add(new MenuItem(context.getString(R.string.device_spec), ConstantApp.TMS_DEVICE_SPEC,true));
        return (items);
    }

    public List<MenuItem> submenuManagerMenu(Context context){
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem(context.getString(R.string.chk_digit_calc), ConstantApp.CHK_DIGIT_CALC));
//        items.add(new MenuItem(context.getString(R.string.remote_download), Constant.REMOTE_DOWNLOAD));
        return (items);
    }

    public List<MenuItem> submenuReconsilSetup(Context context){
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem(context.getString(R.string.current_status), ConstantApp.CURRENT_STATUS));
        items.add(new MenuItem(context.getString(R.string.reconciliation_time), ConstantApp.RECONSILATION_TIME));
        items.add(new MenuItem(context.getString(R.string.enable_disable_recon), ConstantApp.ENABLE_DISABLE_RECON));
        return (items);
    }

    public List<MenuItem> submenuTerminalMenu(Context context){
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem(context.getString(R.string.terminal_info), ConstantApp.TERMINAL_INFO));
        items.add(new MenuItem(context.getString(R.string.print_sys_info), ConstantApp.PRINT_SYS_INFO,true));
        items.add(new MenuItem(context.getString(R.string.terminal_vendor_ping), ConstantApp.TERMINAL_VENDER_PING));
        return (items);
    }

    public List<MenuItem> submenuHistoryView(Context context){
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem(context.getString(R.string.transaction_view), ConstantApp.TRANSACTION_VIEW));
        items.add(new MenuItem(context.getString(R.string.reconcile_view), ConstantApp.RECONCILE_VIEW));
        items.add(new MenuItem(context.getString(R.string.saf_history), ConstantApp.SAF_HISTORY));
        items.add(new MenuItem(context.getString(R.string.delete_history), ConstantApp.DELETE_HISTORY));
        return (items);
    }

    public MenuItem getHostSetting(Context context){
        return new MenuItem(context.getString(R.string.host_setting), ConstantApp.HOST_SETTING, submenuHostSettings(context), R.drawable.admin_server_set);
    }

    public static class MenuItem implements Serializable {
        String menu_name;
        String menu_tag;
        int menuDrawable = 0;
        boolean displayData;
        boolean isPrint;
        boolean isEditAllowed;
        boolean registerValidation;
        boolean tmsValidate;
        List<MenuItem> submenu = null;


        public MenuItem(String menu_name, String menu_tag) {
            this.menu_name = menu_name;
            this.menu_tag = menu_tag;
            this.isPrint = false;
            this.displayData = false;
            this.isEditAllowed = false;

        }

        public MenuItem(String menu_name, String menu_tag,List<MenuItem> submenu) {
            this.isPrint = false;
            this.displayData = false;
            this.isEditAllowed = false;
            this.menu_name = menu_name;
            this.menu_tag = menu_tag;
            this.submenu = new ArrayList<>(submenu);
        }

        public MenuItem(String menu_name, String menu_tag,List<MenuItem> submenu,int menuDrawable) {
            this.isPrint = false;
            this.displayData = false;
            this.isEditAllowed = false;
            this.menu_name = menu_name;
            this.menu_tag = menu_tag;
            this.submenu = new ArrayList<>(submenu);
            this.menuDrawable = menuDrawable;
        }

        public MenuItem(String menu_name, String menu_tag,List<MenuItem> submenu,int menuDrawable,boolean validateReg) {
            this.registerValidation = validateReg;
            this.isPrint = false;
            this.displayData = false;
            this.isEditAllowed = false;
            this.menu_name = menu_name;
            this.menu_tag = menu_tag;
            this.submenu = new ArrayList<>(submenu);
            this.menuDrawable = menuDrawable;
        }

        public MenuItem(String menu_name, String menu_tag,int menuDrawable) {
            this.isPrint = false;
            this.displayData = false;
            this.menu_name = menu_name;
            this.menu_tag = menu_tag;
            this.isEditAllowed = false;
            this.menuDrawable = menuDrawable;
        }

        public MenuItem(boolean isEditAllowed,String menu_name, String menu_tag,int menuDrawable) {
            this.isPrint = false;
            this.displayData = false;
            this.menu_name = menu_name;
            this.menu_tag = menu_tag;
            this.isEditAllowed = isEditAllowed;
            this.menuDrawable = menuDrawable;
        }
        public MenuItem(String menu_name, String menu_tag,int menuDrawable,boolean validateReg) {
            this.registerValidation = validateReg;
            this.isPrint = false;
            this.displayData = false;
            this.menu_name = menu_name;
            this.menu_tag = menu_tag;
            this.isEditAllowed = false;
            this.menuDrawable = menuDrawable;
        }

        public MenuItem(String menu_name, String menu_tag,int menuDrawable,boolean validateReg,boolean tmsValidate) {
            this.registerValidation = validateReg;
            this.tmsValidate = tmsValidate;
            this.isPrint = false;
            this.displayData = false;
            this.isEditAllowed = false;
            this.menu_name = menu_name;
            this.menu_tag = menu_tag;
            this.menuDrawable = menuDrawable;
        }

        public MenuItem(String menu_name, String menu_tag, boolean displayData, List<MenuItem> submenu) {
            this.isPrint = false;
            this.menu_name = menu_name;
            this.menu_tag = menu_tag;
            this.displayData = displayData;
            this.submenu = submenu;
            this.isEditAllowed = false;
        }

        public MenuItem(String menu_name, String menu_tag, boolean displayData, List<MenuItem> submenu,int menuDrawable) {
            this.isPrint = false;
            this.menu_name = menu_name;
            this.menu_tag = menu_tag;
            this.displayData = displayData;
            this.submenu = submenu;
            this.isEditAllowed = false;
            this.menuDrawable = menuDrawable;
        }

        public MenuItem(String menu_name, String menu_tag, boolean displayData, int menuDrawable) {
            this.isPrint = false;
            this.menu_name = menu_name;
            this.menu_tag = menu_tag;
            this.isEditAllowed = false;
            this.displayData = displayData;
            this.menuDrawable = menuDrawable;
        }

        public MenuItem(String menu_name, String menu_tag, boolean displayData, int menuDrawable,boolean validateReg) {
            this.registerValidation = validateReg;
            this.isPrint = false;
            this.isEditAllowed = false;
            this.menu_name = menu_name;
            this.menu_tag = menu_tag;
            this.displayData = displayData;
            this.menuDrawable = menuDrawable;
        }

        public MenuItem(String menu_name, String menu_tag, boolean isPrint) {
            this.displayData = false;
            this.isEditAllowed = false;
            this.menu_name = menu_name;
            this.menu_tag = menu_tag;
            this.isPrint = isPrint;
        }

        public String getMenu_name() {
            return menu_name;
        }

        public String getMenu_tag() {
            return menu_tag;
        }

        public int getMenuDrawable() {
            return menuDrawable;
        }

        public List<MenuItem> getSubmenu() {
            return submenu;
        }

        public boolean isDisplayData() {
            if(checkTransaction())
                return false;
            return displayData;
        }

        public boolean isRegisteValidation(){
            return registerValidation;
        }

        public boolean isPrint() {
            if(checkTransaction())
                return false;
            return isPrint;
        }

        public boolean isEditAllowed() {
            return isEditAllowed;
        }

        private boolean checkTransaction() {
            return (menu_tag.equalsIgnoreCase(ConstantApp.PURCHASE) ||
                    menu_tag.equalsIgnoreCase(ConstantApp.PURCHASE_NAQD) ||
                    menu_tag.equalsIgnoreCase(ConstantApp.PURCHASE_REVERSAL) ||
                    menu_tag.equalsIgnoreCase(ConstantApp.CASH_ADVANCE) ||
                    menu_tag.equalsIgnoreCase(ConstantApp.REFUND) ||
                    menu_tag.equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION) ||
                    menu_tag.equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION_VOID) ||
                    menu_tag.equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION_EXTENSION) ||
                    menu_tag.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE) ||
                    menu_tag.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_MANUAL) ||
                    menu_tag.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_PARTIAL) ||
                    menu_tag.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_FULL))
                ;
        }
    }

    public static class MenuItemHolder implements Serializable {
        List<MenuItem> menu_data;
        boolean isGrid;

        public MenuItemHolder(List<MenuItem> menu_data,boolean isGrid) {
            this.menu_data = menu_data;
            this.isGrid = isGrid;
        }

        public boolean isGrid() {
            return isGrid;
        }

        public List<MenuItem> getMenu_data() {
            return menu_data;
        }

        public void setMenu_data(List<MenuItem> menu_data) {
            this.menu_data = menu_data;
        }
    }

    public static class DisplayModel implements Serializable{
        List<DisplayValue> displayValues;

        public List<DisplayValue> getDisplayValues() {
            return displayValues;
        }

        public void setDisplayValues(List<DisplayValue> displayValues) {
            this.displayValues = displayValues;
        }
    }

    public static class DisplayValue implements Serializable{
        String title;
        String value;

        public DisplayValue(String title, String value) {
            this.title = title;
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
