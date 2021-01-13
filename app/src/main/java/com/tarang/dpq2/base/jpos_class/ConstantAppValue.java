package com.tarang.dpq2.base.jpos_class;

public interface ConstantAppValue {


    //checking subtarnsaction condition and set value from ConstantValue
    //setting value for MTI, field 01.

    String AUTH = "1100";      //Authorisation Request
    String AUTH_ADVISE = "1120";       //Authorisation Advice
    String AUTH_ADVISE_REPEAT = "1121";//Authorisation Advice Repeat
    String FINANCIAL = "1200";          //Financial Request
    String FINANCIAL_ADVISE = "1220";           //Financial Advice
    String FINANCIAL_ADVISE_REPEAT = "1221";    //Financial Advice Repeat
    String FILEACTION = "1304";        //File Action Request
    String FILEACTION_REPEAT = "1305"; //File Action Request Repeat
    String REVERSAL = "1420";            //Reversal Advice
    String REVERSAL_REPEAT = "1421";     //Reversal Advice Repeat
    String RECONCILIATION = "1524";//Terminal Reconciliation Advice
    String RECONCILIATION_REPEAT = "1525";//Terminal Reconciliation Advice Repeat
    String ADMIN_NOTIFICATION = "1644";//Administrative Notification
    String NETWORK_MNGMT = "1804";//Network Management Request

    // MTI Value for Responce
    String AUTH_RESPONSE = "1110";
    String AUTH_ADVISE_RESPONSE = "1130";
    String FINANCIAL_RESPONSE = "1210";
    String FINANCIAL_ADVICE_RESPONSE = "1230";
    String REVERSAL_RESPONSE = "1430";
    String FILEACTION_RESPONSE = "1314";
    String RECONSILATION_RESPONSE = "1534";
    String TERMINAL_REGISTRATION_RESPONSE = "1814";

//setting value process code, field 03

    String NOTXN = "000000";
    String PURCHASE = "000000";
    String PURCHASE_NAQD = "090000";
    String PURCHASE_REVERSAL = "000000";
    String CASH_ADVANCE = "010000";
    String REFUND = "200000";
    String PRE_AUTHORISATION = "900000";
    String PRE_AUTHORISATION_COMPLETE = "000000";
    String PURCHASE_ADVICE = "000000";
    String NO_OF_TXNS = "000000";
    String PURCHASE_ADVICE_FULL = "000000";
    String PURCHASE_ADVICE_PARTIAL = "000000";
    String PRE_AUTHORISATION_VOID = "220000";


    //for debit
    /*String NOTXN_DEBIT = "000000";
    String PURCHASE = "000000";
    String PURCHASE_CASHBACK_DEBIT = "090000";
    String PURCHASE_REVERSAL_DEBIT = "000000";
    String CASH_ADVANCE_DEBIT = "010000";
    String REFUND_DEBIT = "200000";
    String PRE_AUTHORISATION_DEBIT = "900000";
    String PRE_AUTHORISATION_COMPLET_DEBIT = "000000";
    String PURCHASE_ADVICE_DEBIT = "000000";
    String NO_OF_TXNS_DEBIT = "000000";*/
    //for credit
    String NOTXN_CREDIT = "000000";
    String PURCHASE_CREDIT = "003000";
    String PURCHASE_CASHBACK_CREDIT = "093000";
    String PURCHASE_REVERSAL_CREDIT = "000000";
    String CASH_ADVANCE_CREDIT = "013000";
    String REFUND_CREDIT = "203000";
    String PRE_AUTHORISATION_CREDIT = "903000";
    String PRE_AUTHORISATION_COMPLET_CREDIT = "003000";
    String PURCHASE_ADVICE_CREDIT = "003000";
    String NO_OF_TXNS_CREDIT = "000000";

    //setting value for function code, field 24

    //Used in 1100 messages
    String AMOUNT_ACCURATE = "100";
    String AMOUNT_ESTIMATE = "101";
    //Used in 1120, 1121 messages
    String PREAUTH_INITIAL_COMPLETION = "182";
    String AUTHORIZATION_EXTENSION = "183";
    //Used in 1200 messages and Used in 1220, 1221 messages
    String FINANCIAL_TRANSACTION = "200";
    String APPROVED_AUTH_SAME_AMOUNT = "201";
    String APPROVED_AUTH_DIFF_AMOUNT = "202";
    //Used in 1304, 1305 messages file action
    String REPLACE_FIELDS_RECORD = "302";
    String REPLACE_ENTIRE_RECORD = "304";
    String REPLACE_FILE = "306";
    //Used in 1420, 1421 messages
    String TXN_NOT_COMPLETED = "400";
    //Used in 1524, 1525 messages
    String TERMINAL_RECONCILIATION = "570";
    String FORCE_RECONCILIATION = "571";
    //Used in 1644 messages
    String UNABLE_PARSE_MSG = "650";
    String MAC_ERROR = "691";
    //Used in 1804 messages
    String TERMINAL_REGISTRATION = "814";

    //setting value for message code, seeting value for field 25

    //1000-1499 Reason for an advice/notification message rather than a request message.
    //The valid codes for an 1220 / 1221 Financial Transaction Advice generated for a chip card are as follows
    String TERMINAL_PROCESSED = "1004";  //used for: Pre-authorisation completion Voice authorised credit card advice.
    String ICC_PROCESSED = "1005";       //used for: Off-line chip card advice (approved or declined) On-line chip completion.
    String UNDER_FLOOR_LIMIT = "1006";    //not used in mada

    //1500-1999 Reason for a request message rather than an advice/notification message.
//The valid codes for an 1100 Authorisation Request generated for a chip card are as follows.String ICC_RANDOM_SEL="1502";
    //DE25
    String TER_RANDOM_SEL = "1503";
    String ONLINE_ICC = "1505";
    String ONLINE_CARD_ACCEPTOR = "1506";
    String ONLINE_TERMINAL = "1508";
    String ONLINE_CARD_ISSUER = "1509";
    String MERCHANT_SUSPICIOUS = "1511";
    String PRE_AUTHORIZATION_VOID = "1151";
    String PRE_AUTHORIZATION_EXTENSION = "1152";

    //Reason for a 1200 Financial Transaction Request for a chip card rather than an 1100 Authorisation Message.
    String FALLBACK_ICCTOMSR = "1776";
    String CLESS_TXN = "1990";
    String CLESS_TXN_ADVICE = "1490";

    //4000-4499 Reason for a reversal. The valid codes for an 1420 / 1421 Reversal Advice are as follows.
    String CUSTOMER_CANCELLATION = "4000";
    String UNSPECIFIED = "4001";
    String SUSPECTED_MALFUNC = "4002";
    String FORMAT_ERROR = "4003";
    String INCORRECT_AMOUNT = "4005";
    String RES_RECEIVED_LATE = "4006";
    String UNABLE_COMPLETE_TXN = "4007";
    String UNABLE_DELIVER_MSG = "4013";
    String INVALID_RESP = "4020";
    String TIMEOUT_WAITING_RESP = "4021";
    String MAC_FAILURE = "4351";

    //setting value for data element 48 ===> Digital signature

    //HAVE TO CONFIRM THIS VALUE FROM CLIENT TODO
    String VENDOR_PUBLIC_KEY_INDEX = "01";  //48.4
    String SAMA_KEY_INDEX = "00";  //48.5

    String RANDOM_STRING_LENGTH_SEQUENCE_INDICATOR = "000010";  //48.6 ====> 32CHAR LENGTH 16BYTES
    //String MADA_SIGNATURE_LENGTH = "000090"; //48.8
    String MADA_SIGNATURE_LENGTH = "000090"; //48.8

    //SETTING VALUE MADA SIGNATURE DATA  //48.9
    String MSD1 = "0001";  //2BYTES

    String MSD2 = "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF"; // 106 BYTES
    String MSD3 = "00"; //1 BYTES
    String MSD4 = "3021300906052B0E03021A050004"; //14 BYTES  OID
    String MSD5 = "14"; //1BYTE
//    //remote public key
//    String rsaPrivateModKey = "D853FE30D5C42C463DD4E2FED8AD91C03586727F9170F57BA0BAD0F89E9E9C9BA562E04FFBC92A05F43D5A66F47684B69AD09D245ACFD6319030ACBA6472FE4A3F50247A0FAEC00C6C3D5752D66A7309352C8F3496AC8B3E0244248889A80734B67B33902CF9BF1D65CC5EF6C8DF9D43AD2E5471F33EA818A634D8929B23C3D1";
//    //local private key
//    String rsaPrivateExponentKey = "81299549DF38A41EC46849A854033E54FF206958D5A4164E5C676EC223231CEAA9D2E18FA00C3A3517E1520ACAC7C0F1AE7A0479626759C27A0F35F28C6B447C43148D9D1346F3C3E80EB4F54C2D2D3616747F7EFF8DC35CC387B5EEC4D0CD0E95960D40168730AFC8448C50D5FCD2570303D70B7C5188BFBFC601C367E2E649";
//    //local public key
//    String rsaLocalPublicModeKey = "D853FE30D5C42C463DD4E2FED8AD91C03586727F9170F57BA0BAD0F89E9E9C9BA562E04FFBC92A05F43D5A66F47684B69AD09D245ACFD6319030ACBA6472FE4A3F50247A0FAEC00C6C3D5752D66A7309352C8F3496AC8B3E0244248889A80734B67B33902CF9BF1D65CC5EF6C8DF9D43AD2E5471F33EA818A634D8929B23C3D1";
//    //CHECK CODE FRO MSD6  //SHA CHECKSUM

    //remote public key
    String rsaPrivateModKey = "CF05A51CD120B590C69BF6021E6E21B0A0E55A3AE120D10A25C83749F436A64838496A0C8452E39DAFBB1A4C34819AEFA970A225F1AA02F02BD206AAE9414D2156D16AC75AA6DD5EDF0EFEF8B7111D61D555F118AE97E37F7F9A0A75A194B1C842331CD12DB66D734CE0247A1DAE2B41F6337C9F3065D1DA9471C1706A97C4BEFED841D4B6333F6214B51E3CDDA38C8F";
    //local private key
    String rsaPrivateExponentKey = "AA41903B14DA7BB8273ED8A181793D87CFAB0FFBE06E66BC9BB9FF8E78A40138DFE1B0AFBE9B94CA785DBE10F215ACE0714F4A6B3E467DC81BA1CD0DFE6B500D9D22F9581E7BA10E0C13D085AB87CE9F15F16637132128356B73AB580089DFB0AB4BA8FFA79EFE52D03E3224E75FBF1CD0545A08502A984252EBC0E4F4DA87DFE52B228DA6A48149C79F9DD8CDE97BF1";

    //Host public key
    //LOcal
    String rsaLocalPublicModeKey = "A14B30E2005F51AE612A6223910FAF78A36460AFED1D708585D37ABBAD2F049BF91EC43480867BD921CD0B26A3E85F9EC3EE7C5F7364A08CC213757902EB2166F1D3367392D89F4956F339A38FB49F5A995E967648D584A6F2594CD9FC29B3C985096D5C1852A801AC3A3F6CAC0FDB854B34A89E4A53267BE3D10B6F2B07F0FADD6358F22759CD6641F12CC3A58A3213";
    //Live
    String rsaLocalPublicModeKey_live = "CFE6A23919D8E15E71E1F6096321539A08470A11A4165088F2EA49AB5797467A1BE00F41AD542A178DB2C67E67F47263EAA974E199F864063CD0A9AB00CACCD558991018A5E99551EF381768F7599B6D01A22ACBD4DF7E791BEB40FCF18D98B0A314A2F4D7598BCF4477C03E6DC3B7E24FD903EBC6C8CC6EA0E8814DB04F28716C5D0BFAB073663B5B3C2A5B387F9B3D";


    //setting types of card based on AID

    // visa
    //get aid from card == Constant.aid, then get same from ConstantValue==========> TMSCardSchemeEntity db ,get cardIndicator
    String A0000000031010 = "VC";
    String A0000000032010 = "VC";
    //master
    String A0000000041010 = "MC";
    //maestro
    String A0000000043060 = "DM";
    //mada
    String A0000002281010 = "P1";
    String A0000002282010 = "P1";
    //union pay
    String A000000333010101 = "UP";
    String A000000333010102 = "UP";

    String A00000002501 = "AX";

    String A000000GN= "GN";

    String REFER_TO_CARD_ISSUER_VALUE = "107";
    String REFER_TO_000 = "000";


    String[] LIST_CARD_PRINT = {A0000002281010,A000000GN,A0000000041010,A0000000043060,A0000000031010,A00000002501,A000000333010101};
    String[] LIST_CARD_PRINT_ID = {A0000002281010,A0000000031010,A0000000041010,A000000GN,A0000000043060,A00000002501,A000000333010101};

    //check online offline

    String APP_CRYPTOGRAM_ARQC = "80";  //online
    String APP_CRYPTOGRAM_TC = "40";    //offline
    String APP_CRYPTOGRAM_AAC = "00";   //decline

    String ADMIN_NOTIFICATION_RESPONSE_CODE = "690";

    // Card Names
    String MADA= "mada";
    String VISA_CREDIT= "Visa";
    String VISA_DEBIT= "Visa";
    String MASTER_CARD= "MasterCard";
    String MAESTRO= "Maestro";
    String AMEX= "American Express";
    String UNION_PAY= "UnionPay";
    String JCB= "JCB";
    String DISCOVER= "DISCOVER";
    String GCCNET= "GCCNET";

    // Card Names
    String MADA_AR= "مدى";
    String VISA_CREDIT_AR= "فيزا";
    String VISA_DEBIT_AR= "فيزا";
//    String VISA_DEBIT_AR= "VISA DEBIT";
    String MASTER_CARD_AR= "ماستر كارد";
    String MAESTRO_AR= "مايسترو";
    String AMEX_AR= "امريكان اكسبرس";
    String UNION_PAY_AR= "يونيون باي";
    String JCB_AR= "JCB";
    String DISCOVER_AR= "DISCOVER";
    String GCCNET_AR= "الشبكة الخليجية";

    String KEYED = "KEYED";
    String SWIPED = "SWIPED";
    String DIPPED = "DIPPED";
    String CONTACTLESS = "CONTACTLESS";

    String underline = "X_____________________";
    String doted_underscore = "_ _ _ _ _ _ _ _ _ _ _ _ _ _ ";
    String doted_line = "- - - - - - -  - - - - - - - ";


    String SAF_APPROVED = "087";
    String SAF_APPROVED_UNABLE = "089";
    String SAF_REJECTED = "190";
    String SAF_DECLINED = "188";
    String SAF_REFUND = "000";
}
