package com.tarang.dpq2.isopacket;

import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import androidx.work.Data;

import com.tarang.dpq2.base.AppInit;
import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.GpsTracker;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.jpos_class.ByteConversionUtils;
import com.tarang.dpq2.base.utilities.Utils;
import com.tarang.dpq2.model.ReservedData62Model;
import com.tarang.dpq2.model.TerminalConnectionGPRSModel;
import com.tarang.dpq2.model.TerminalConnectionWifiModel;
import com.tarang.dpq2.worker.PacketDBInfoWorker;

import static com.tarang.dpq2.base.terminal_sdk.AppConfig.EMV.reqObj;

public class SupportPacket {

    private static SupportPacket request;
    public static Context context;

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static ReservedData62Model setReservedData62Model(String requestType) {
        ReservedData62Model reservedData62Model = new ReservedData62Model();
        reservedData62Model.setTerminalDailIndicator("");
        reservedData62Model.setPrinterStatus(getPrinterStatus());
        reservedData62Model.setIdleTime(ByteConversionUtils.formatTranDate("hhmmss"));
        reservedData62Model.setMagneticReaderStatus("0");
        reservedData62Model.setChipCardReaderStatus("0");
        reservedData62Model.setGpsLocationCoordinates(getLocation(context));
        reservedData62Model.setContactlessReaderStatus("0");
        if (requestType.equalsIgnoreCase("terminal_registration")) {
            reservedData62Model.setConnectionStartTime("000000000");
            reservedData62Model.setConnectionEndTime("000000000");
            reservedData62Model.setRequestSentTime("000000000");
            reservedData62Model.setResponseReceivedTime("000000000");
        } else {
            //need to get from previous transaction
            reservedData62Model.setConnectionStartTime("");
            reservedData62Model.setConnectionEndTime("");
            reservedData62Model.setRequestSentTime("");
            reservedData62Model.setResponseReceivedTime("");
        }
        reservedData62Model.setPerformanceTimersReference(String.valueOf(AppManager.getInstance().getRetriveRefNumber37()));
        reservedData62Model.setMadaEFTPOSSpecificationReleaseVersion("010000");
//        reservedData62Model.setConnectionDetails("010101" + getNetworkType());

        return reservedData62Model;
    }
//TODO online changes
    public static String getReservedData62(Context context) {
        StringBuilder stringBuilder62 = new StringBuilder();

        //Terminal Dial Indicator
        stringBuilder62.append("01");
        stringBuilder62.append("1");

        //Printer Status
        stringBuilder62.append("02");
        stringBuilder62.append(getPrinterStatus());

        //Idle Time
        stringBuilder62.append("03");
        stringBuilder62.append("000030");

        //Magnetic Reader Status
        stringBuilder62.append("04");
        stringBuilder62.append("0");

        //Chip Card Reader Status
        stringBuilder62.append("05");
        stringBuilder62.append("0");

        //GPS Location Coordinates
        stringBuilder62.append("07");
        stringBuilder62.append(AppManager.getInstance().getLocation());

        //Contactless Reader Status
        stringBuilder62.append("09");
        stringBuilder62.append("0");

        String[] time = AppManager.getInstance().getLastTransactionTime();
        //Connection Start Time
        stringBuilder62.append("10");
        stringBuilder62.append("000000000");
        //Connection End Time
        stringBuilder62.append("11");
        stringBuilder62.append("000000000");

        if(checkCondition()) {
            //Request Sent Time
            stringBuilder62.append("12");
            stringBuilder62.append("000000000");

            //Response Received Time
            stringBuilder62.append("13");
            stringBuilder62.append("000000000");

            //Performance Timers Reference
            stringBuilder62.append("14");
            stringBuilder62.append("000000000000");
        }else {
            //Request Sent Time
            stringBuilder62.append("12");
            stringBuilder62.append(time[2]);

            //Response Received Time
            stringBuilder62.append("13");
            stringBuilder62.append(time[3]);

            //Performance Timers Reference
            stringBuilder62.append("14");
            stringBuilder62.append(time[4]);
        }
        //mada EFTPOS specification release version
        stringBuilder62.append("15");
        if (AppInit.VERSION_6_0_5)
            stringBuilder62.append("060005");
        else
            stringBuilder62.append("060009");

        //Connection Details
        stringBuilder62.append("16");
        stringBuilder62.append(getNetworkServiceProvider(context));
        stringBuilder62.append(getNetworkType(context));

        Logger.v("stringBuilder62.toString() --" + stringBuilder62.toString());

        return stringBuilder62.toString();
    }

    private static boolean checkCondition() {
        if(reqObj != null){
            if(reqObj.getMti0() != null){
                if(reqObj.getMti0().equalsIgnoreCase("1220") || reqObj.getMti0().equalsIgnoreCase("1120")){
                    return true;
                }
            }
        }
        return false;
    }

    private static String  getNetworkServiceProvider(Context context) {
        String data = getPriority();
        Logger.v("Data --"+data);
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        if(tm.getNetworkOperatorName().equalsIgnoreCase("STC")){
            data = data +"0101";
        }else if(tm.getNetworkOperatorName().equalsIgnoreCase("Mobily")){
            data = data +"0202";
        }else if(tm.getNetworkOperatorName().equalsIgnoreCase("Zain")){
            data = data +"0303";
        }else if(tm.getNetworkOperatorName().equalsIgnoreCase("Sky Band")){
            data = data +"0404";
        }else if(tm.getNetworkOperatorName().equalsIgnoreCase("Geidea")){
            data = data +"0505";
        }else {
            data = data +"0101";
        }
        Logger.v("Newtwork --"+data);
        return data;
    }

    public static String getPriority() {
        boolean priority = AppManager.getInstance().getConnectonMode();
        TerminalConnectionGPRSModel terminalConnectionGPRSModel1 = AppManager.getInstance().getTerminalConnectionGPRSModel();
        TerminalConnectionWifiModel terminalConnectionWifiModel1 = AppManager.getInstance().getTerminalConnectionWifiModel();
        if (priority) {
            if (terminalConnectionGPRSModel1 != null && terminalConnectionGPRSModel1.getNetworkIpAddress() != null
                    && terminalConnectionGPRSModel1.getNetworkTcpPort() != null &&
                    terminalConnectionGPRSModel1.getNetworkIpAddress().trim().length() != 0
                    && terminalConnectionGPRSModel1.getNetworkTcpPort().trim().length() != 0) {
                Logger.v("IP --1--" + terminalConnectionGPRSModel1.getPriority());
                if(terminalConnectionGPRSModel1.getPriority().equalsIgnoreCase("2")){
                    return "02";
                }else
                    return "01";
            } else if (terminalConnectionWifiModel1 != null && terminalConnectionWifiModel1.getNetworkIpAddress() != null
                    && terminalConnectionWifiModel1.getNetworkTcpPort() != null &&
                    terminalConnectionWifiModel1.getNetworkIpAddress().trim().length() != 0
                    && terminalConnectionWifiModel1.getNetworkTcpPort().trim().length() != 0) {
                Logger.v("IP --2--" + terminalConnectionWifiModel1.getPriority());
                if(terminalConnectionWifiModel1.getPriority().equalsIgnoreCase("2")){
                    return "02";
                }else
                    return "01";

            }else {
                return "01";
            }
        } else {
            if (terminalConnectionWifiModel1 != null && terminalConnectionWifiModel1.getNetworkIpAddress() != null
                    && terminalConnectionWifiModel1.getNetworkTcpPort() != null &&
                    terminalConnectionWifiModel1.getNetworkIpAddress().trim().length() != 0
                    && terminalConnectionWifiModel1.getNetworkTcpPort().trim().length() != 0) {
                Logger.v("IP --2--" + terminalConnectionWifiModel1.getPriority());
                if(terminalConnectionWifiModel1.getPriority().equalsIgnoreCase("2")){
                    return "02";
                }else
                    return "01";
            } else if (terminalConnectionGPRSModel1 != null && terminalConnectionGPRSModel1.getNetworkIpAddress() != null
                    && terminalConnectionGPRSModel1.getNetworkTcpPort() != null &&
                    terminalConnectionGPRSModel1.getNetworkIpAddress().trim().length() != 0
                    && terminalConnectionGPRSModel1.getNetworkTcpPort().trim().length() != 0) {
                Logger.v("IP --1--" + terminalConnectionGPRSModel1.getPriority());
                if(terminalConnectionGPRSModel1.getPriority().equalsIgnoreCase("2")){
                    return "02";
                }else
                    return "01";
            } else {
                return "01";
            }
        }

    }
   /* public static String getReservedData62(Context context, Data database) {
        StringBuilder stringBuilder62 = new StringBuilder();

        //Terminal Dial Indicator
        stringBuilder62.append("01");
        stringBuilder62.append("1");

        //Printer Status
        stringBuilder62.append("02");
        stringBuilder62.append(getPrinterStatus());

        //Idle Time
        stringBuilder62.append("03");
        stringBuilder62.append(ByteConversionUtils.formatTranDate("hhmmss"));

        //Magnetic Reader Status
        stringBuilder62.append("04");
        stringBuilder62.append("0");

        //Chip Card Reader Status
        stringBuilder62.append("05");
        stringBuilder62.append("0");

        //GPS Location Coordinates
        stringBuilder62.append("07");
        stringBuilder62.append(getLocation(context));

        //Contactless Reader Status
        stringBuilder62.append("09");
        stringBuilder62.append("0");

        String[] time = AppManager.getInstance().getLastTransactionTime();
        //Connection Start Time
        stringBuilder62.append("10");
        stringBuilder62.append(time[0]);
        //Connection End Time
        stringBuilder62.append("11");
        stringBuilder62.append(time[1]);
        //Request Sent Time
        stringBuilder62.append("12");
        stringBuilder62.append(time[2]);

        //Response Received Time
        stringBuilder62.append("13");
        stringBuilder62.append(time[3]);

        //Performance Timers Reference
        stringBuilder62.append("14");
        stringBuilder62.append(database.getString(PacketDBInfoWorker.RetriRefNo37));

        //mada EFTPOS specification release version
        stringBuilder62.append("15");
        if (AppInit.VERSION_6_0_5)
            stringBuilder62.append("060005");
        else
            stringBuilder62.append("060009");

        //Connection Details
        stringBuilder62.append("16");
        stringBuilder62.append("010101");
        stringBuilder62.append(getNetworkType(context));

        Logger.v("stringBuilder62.toString() --"+stringBuilder62.toString());

        return stringBuilder62.toString();
    }*/

    public String getAdditionalDataPrivate48() {
        StringBuffer adp = new StringBuffer();
        //vendor ID
        adp.append("");
        //Vendor Terminal Type
        adp.append("");
        //Vendor Terminal Serial Number
        adp.append("");
        //Vendor Key Index
        adp.append("");
        //SAMA Key Index
        adp.append("");
        //Random string sequence length
        adp.append("0010");
        //Random string sequence
        adp.append(randomAlphaNumeric(16));
        //Vendor/mada Signature length
        adp.append("");
        //Vendor/mada Signature
        adp.append("");

        return adp.toString();
    }

    public static String getLocation(Context context) {
        String latnlong = "N000000W0000000";
        GpsTracker gpsTracker = new GpsTracker(context);
        if (gpsTracker.canGetLocation()) {
            double longitude = gpsTracker.getLongitude();
            double latitude = gpsTracker.getLatitude();
            StringBuilder builder = new StringBuilder();

            if (latitude < 0) {
                builder.append("S");
            } else {
                builder.append("N");
            }

            String latitudeDegrees = Location.convert(Math.abs(latitude), Location.FORMAT_SECONDS);
            Logger.v("latitudeDegrees --"+latitudeDegrees);
            String[] latitudeSplit = latitudeDegrees.split(":");
            builder.append(addZeroWithCordinate(latitudeSplit[0],2));
//            builder.append("°");
            builder.append(addZeroWithCordinate(latitudeSplit[1],2));
//            builder.append("'");
            builder.append(addZeroWithCordinate(latitudeSplit[2],2));
//            builder.append("\"");

//            builder.append(" ");

            if (longitude < 0) {
                builder.append("W");
            } else {
                builder.append("E");
            }

            String longitudeDegrees = Location.convert(Math.abs(longitude), Location.FORMAT_SECONDS);
            Logger.v("latitudeDegrees --"+longitudeDegrees);
            String[] longitudeSplit = longitudeDegrees.split(":");
            builder.append(addZeroWithCordinate(longitudeSplit[0],3));
//            builder.append("°");
            builder.append(addZeroWithCordinate(longitudeSplit[1],2));
//            builder.append("'");
            builder.append(addZeroWithCordinate(longitudeSplit[2],2));
//            builder.append("\"");
            Logger.v("builder -"+builder.toString());
            latnlong = builder.toString();
            return builder.toString();
        } else {
            gpsTracker.showSettingsAlert();
        }

        return latnlong.replaceAll(".","0");
    }

    private static String addZeroWithCordinate(String val,int i) {
        String s = ((int)Float.parseFloat(val))+"";
        Logger.v("val -"+s);
        if(s.trim().length() < i)
            return addZero(i - s.trim().length())+s;
        else if(s.trim().length() > i)
            return s.substring(0,i);
        else
            return s;
    }

    private static String addZero(int zero) {
        String s = "";
        for(int i=0;i<zero;i++ )
            s = s + "0";
        return s;
    }

    public static String getPrinterStatus() {
        String status = "2";
//        Printer printer = SDKDevice.getInstance(context).getPrinter();
//        PrinterStatus printerStatus = printer.getStatus();
//        if (printerStatus.toString().equalsIgnoreCase(PrinterStatus.NORMAL.toString())) {
//            status = "2";
//        } else if (printerStatus.toString().equalsIgnoreCase(PrinterStatus.OUTOF_PAPER.toString())) {
//            status = "1";
//        }
        return status;
    }

    public static String getNetworkType(Context context) {
        String networkTypeString = "";
        if (Utils.isInternetAvailable(context)) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            activeNetwork.getExtraInfo();
            if (isConnected) {
                int networkType = activeNetwork.getType();
                if (networkType == ConnectivityManager.TYPE_WIFI) {
                    networkTypeString = "06";
                } else if (networkType == ConnectivityManager.TYPE_MOBILE) {
                    networkTypeString = "02";
                } else {
                    networkTypeString = "02";
                }
            }
        }
        return networkTypeString;
    }


    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    public static String getNetworkTypeString(Context context) {
        String networkTypeString = "";
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        try {
            activeNetwork.getExtraInfo();
            if (isConnected) {
                int networkType = activeNetwork.getType();
                if (networkType == ConnectivityManager.TYPE_WIFI) {
                    networkTypeString = "WIFI";
                } else if (networkType == ConnectivityManager.TYPE_MOBILE) {
                    networkTypeString = "GPRS";
                } else {
                    networkTypeString = "Unknown";
                }
            }
        } catch (Exception e) {
            networkTypeString = "Unknown";
            Logger.v("Exception");
        }
        return networkTypeString;
    }

    public static String getNetworkServiceProviderString(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm.getNetworkOperatorName().equalsIgnoreCase("STC")) {
            return "STC";
        } else if (tm.getNetworkOperatorName().equalsIgnoreCase("Mobily")) {
            return "Mobily";
        } else if (tm.getNetworkOperatorName().equalsIgnoreCase("Zain")) {
            return "Zain";
        } else if (tm.getNetworkOperatorName().equalsIgnoreCase("Sky Band")) {
            return "Sky Band";
        } else if (tm.getNetworkOperatorName().equalsIgnoreCase("Geidea")) {
            return "Geidea";
        } else {
            return tm.getNetworkOperatorName();
        }
    }
}
