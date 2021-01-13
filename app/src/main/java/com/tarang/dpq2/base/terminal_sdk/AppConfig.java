package com.tarang.dpq2.base.terminal_sdk;

import com.tarang.dpq2.base.jpos_class.MadaRequest;
import com.tarang.dpq2.base.terminal_sdk.q2.result.SwipeResult;


public class AppConfig {

    public static boolean customerCopyPrinted = false;

//    public static boolean isChipTransaction;
    public static boolean isCardRemoved = false;
    public static boolean printerDataAvailable = false;

    public static class EMV{
        /**
         * Password entered is complete
         */
        public static MadaRequest reqObj;
        public static byte[] reqObjArray;
        public static final int PIN_FINISH = 1;
        public static final int PIN_CANCEL = 2;
        public static final int SOCKET_FINISH = 3;
        public static final int SOCKET_CANCEL = 4;
        public static final int SOCKET_UNABEL_ONLINE = 12;
        public static final int START_TIMER = 5;
        public static final int START_TIMER_REVERSE = 55;
        public static final int STOP_TIMER = 9;
        public static final int PRINT_CUSTOMER_COPY = 6;
        public static final int PRINT_CUSTOMER_COPY_CANCEL = 7;
        public static final int SAF_TIMER_= 8;
        public static final int STOP_TIMER_= 10;
        public static final int SELECT_APP= 11;
//        public static BigDecimal amt = new BigDecimal("0");
        public static double amountValue;
        public static double amtCashBack;
//        public static int isECSwitch;
        public static String icCardNum;
        public static String icKernalId = "";
        public static String card_name;
        public static String icCardSerialNum;
        public static String icCardTrack2data;
        public static String icExpiredDate;
        public static String ic55Data = "";
        public static byte[] pinBlock;
        public static byte[] aid;
        public static int consumeType = -1; // Consumption type, magnetic stripe card consumption did not pass the IC55-tag data, 0-magnetic 1-IC 2-RF. Distinguish the passing of parameters
        public static SwipeResult swipResult;// Swiping result
        public static String[] TAG55 = {
                "9F26", //0
                "9F10",//1
                "9F27" ,//2
                "9F37",//3
                "9F36",//4
                "95" ,//5
                "9A",//6
                "9C",//7
                "9F02" ,//8
                "5F2A"//9
                ,"82",//10
                "9F1A",//11
                "9F03" ,//12
                "9F33",//13
                "9F34",//14
                "9F35" ,//15
                "9F1E",//16
                "84",//17
                "50" ,//18
                "4F",//19
                "9F12",//20
                "9B",//21
                "8A",//22
                "9F6C",//23
                "9F66"//24
        };


        public static boolean enableDatabaseUpdate = false;
    }
//    public static int INDEX_FRAGMENT_START = -1;
//    public static int INDEX_FRAGMENT_EMV = -1;
//    public static int INDEX_FRAGMENT_PIN = -1;

//    public static class ScanResult{
//        public static final int SCAN_FINISH = 0;
//        public static final int SCAN_RESPONSE = 1;
//        public static final int SCAN_ERROR = 2;
//        public static final int SCAN_TIMEOUT = 3;
//        public static final int SCAN_CANCEL = 4;
//    }

//    public static class ScanType {
        /**
         * Front scan
         */
//        public static final int FRONT = 1;
        /**
         * Back scan
         */
//        public static final int BACK = 0;
//    }
}

