package com.tarang.dpq2.base.terminal_sdk.event;

public class SimpleTransferListenerOld {}
//
//        implements EmvFinalAppSelectListener {
//
//    public static MakeConnection connection;
//    private SoundPoolImpl spi = null;
//    private Context context;
//    //	private SdkSupport.CardRecognized reader;
//    private static int[] L_55TAGS = new int[32];
//    private static int[] L_SCRIPTTAGS = new int[21];
//    private static int[] L_REVTAGS = new int[5];
//    private int isECSwitch = 0;
//    private static WaitThreat waitPinInputThreat = new WaitThreat();
//    private static WaitThreat waitPinInputThreat_rf = new WaitThreat();
//    private static WaitThreat waitPinInputThreat_pin = new WaitThreat();
//    private static WaitThreat waitSockettThreat = new WaitThreat();
//    private static WaitThreat waitSelectApp = new WaitThreat();
//    public static byte[] pinBlock = null;
//    private String encryptAlgorithm;
//    private SDKDevice sdkDevice;
//    private EmvModule emvModule;
//    private String transType;
//    private int pinAttempt;
//    private int trackKeyIndex;
//    private int mkIndex;
//    public static byte[] selectApplication;
//    boolean selectAppCancelled = false;
//    public static boolean isCancelled;
//    public static boolean isCardInvalid;
//    public static boolean isAmountExceed;
//    public static boolean isTransactionNotAllowed;
//    public static boolean disableReversal;
//    public static boolean doReversal;
//    public static int isPinRequires = 0;
//    public static boolean isEMVCompleted;
//    public static boolean isGccNet;
//    public static boolean isSAFDecilined = false;
//    public static boolean isPinCancelled = false;
//    public static int isScreenAvailable = 0;
//
//
//    public static int DO_SOCKET_REQ = 0;
//    public static int DO_SOCKET_REQ_AGAIN = 100;
//    public static int DO_FALLBACK = 1;
//    public static int DO_FALLBACK_APP = 101;
//    public static int DO_REVERSAL = 2;
//    public static int DO_REVERSAL_CANCEL = 22;
//    public static int DO_TIMER = 3;
//    public static int DO_TIMER_REVERSAL = 33;
//    public static int DO_SAF = 4;
//    public static int DO_SAF_APPROVED = 66;
//    public static int DO_SAF_REJECTED = 6;
//    public static int DO_SAF_DECLINED = 7;
//    public static int FINAL_ERROR = 77;
//    public static int DO_ERROR = 5;
//    public static int DO_ERROR_MESSAGE = 9;
//    public static int DO_CARD_INVALID = 10;
//    public static int DO_AMOUNT_EXCEED = 11;
//    public static int DO_TRANSACTION = 12;
//    public static int DO_CANCEL_AND_MOVE = 13;
//    public static int DO_ERROR_PIN_CANCELLED = 14;
//    public static int DO_WAVE_AGAIN = 15;
//    public static int DO_BEEP_ONCE = 16;
//    public static int DO_INVALID_PIN = 17;
//
//    static {
//        L_55TAGS[0] = 0x9f26;//
//        L_55TAGS[1] = 0x9F10;//
//        L_55TAGS[2] = 0x9F27;//
//        L_55TAGS[3] = 0x9F37; //
//        L_55TAGS[4] = 0x9F36;//
//        L_55TAGS[5] = 0x95;//
//        L_55TAGS[6] = 0x9A;//
//        L_55TAGS[7] = 0x9C;//
//        L_55TAGS[8] = 0x9F02;
//        L_55TAGS[9] = 0x5F2A;//
//        L_55TAGS[10] = 0x82;
//        L_55TAGS[11] = 0x9F1A;//
//        L_55TAGS[12] = 0x9F03;
//        L_55TAGS[13] = 0x9F33;//
//        L_55TAGS[14] = 0x9F34;//
//        L_55TAGS[15] = 0x9F35;//
//        L_55TAGS[16] = 0x9F1E;//
//        L_55TAGS[17] = 0x84;//
////		L_55TAGS[18] = 0x9F09;
////		L_55TAGS[19] = 0x9F41;
////		L_55TAGS[20] = 0x8a;
////		L_55TAGS[21] = 0x9f63;
//        L_55TAGS[18] = 0x50;
//        L_55TAGS[19] = 0x4f;
//        L_55TAGS[20] = 0x9f12;
//        L_55TAGS[21] = 0xDF01;//
//        L_55TAGS[22] = 0x9B;
//        L_55TAGS[23] = 0x8a;
//        L_55TAGS[24] = 0x5A;//
//        L_55TAGS[25] = 0x57;//
//        L_55TAGS[26] = 0x9F6C;//
//        L_55TAGS[27] = 0x9F6E;//
//        L_55TAGS[28] = 0x9F24;//
//        L_55TAGS[29] = 0x9F66;//
//        L_55TAGS[30] = 0x9F19;//
//        L_55TAGS[31] = 0x9F25;//
////        L_55TAGS[32] = 0x9F66;
//
//        L_SCRIPTTAGS[0] = 0x9F33;
//        L_SCRIPTTAGS[1] = 0x9F34;
//        L_SCRIPTTAGS[2] = 0x9F35;
//        L_SCRIPTTAGS[3] = 0x95;
//        L_SCRIPTTAGS[4] = 0x9F37;
//        L_SCRIPTTAGS[5] = 0x9F1E;
//        L_SCRIPTTAGS[6] = 0x9F10;
//        L_SCRIPTTAGS[7] = 0x9F26;
//        L_SCRIPTTAGS[8] = 0x9F27;
//        L_SCRIPTTAGS[9] = 0x9F36;
//        L_SCRIPTTAGS[10] = 0x82;
//        L_SCRIPTTAGS[11] = 0xDF31;
//        L_SCRIPTTAGS[12] = 0x9F1A;
//        L_SCRIPTTAGS[13] = 0x9A;
//        L_SCRIPTTAGS[14] = 0x9C;
//        L_SCRIPTTAGS[15] = 0x9F02;
//        L_SCRIPTTAGS[16] = 0x5F2A;
//        L_SCRIPTTAGS[17] = 0x84;
//        L_SCRIPTTAGS[18] = 0x9F09;
//        L_SCRIPTTAGS[19] = 0x9F41;
//        L_SCRIPTTAGS[20] = 0x9F63;
//
//        L_REVTAGS[0] = 0x95;
//        L_REVTAGS[1] = 0x9F1E;
//        L_REVTAGS[2] = 0x9F10;
//        L_REVTAGS[3] = 0x9F36;
//        L_REVTAGS[4] = 0xDF31;
//    }
//
//    static String responseData55 = "";
//    static String responseData38 = "";
//    private boolean madeOnlineConnection;
//    private boolean isMada;
//    private boolean showPinOkay;
//    private boolean doBeepOnce;
//    private boolean showCard;
//    public static int rf_thread = 0;
//    private boolean isAppSelect = false;
//    private int errorCode = 0;
//
//
//    public SimpleTransferListenerOld(Context context, EmvModule emvModule, String transType, MakeConnection connection) {
//        this.context = context;
//        this.emvModule = emvModule;
//        this.transType = transType;
//        this.connection = connection;
//        sdkDevice = SDKDevice.getInstance(context);
//        madeOnlineConnection = false;
//        isAppSelect = false;
//        AppConfig.EMV.icKernalId = "";
//        pinAttempt = 1;
//        SimpleTransferListener.pinBlock = null;
//        isEMVCompleted = false;
//        errorCode = 0;
////		this.reader = reader;
//        mkIndex = AppConfig.Pin.MKSK_DES_INDEX_MK;
//        trackKeyIndex = AppConfig.Pin.MKSK_DES_INDEX_WK_TRACK;
//        encryptAlgorithm = MESeriesConst.TrackEncryptAlgorithm.BY_UNIONPAY_MODEL;
//    }
//
//    //after all it captures all the card details only in case of rf and ic
//
//    @Override
//    public void onEmvFinished(boolean isSuccess, EmvTransInfo emvTransInfo) throws Exception {
////        if (errorCode == 1) {
////            connection.onConnect(DO_ERROR);
////            return;
////        }
//        if ((!isAppSelect) && AppConfig.EMV.consumeType == 2) {
//            connection.onConnect(DO_WAVE_AGAIN);
//            return;
//        }
//        if (isEMVCompleted)
//            return;
//
//        Logger.ve("Listner ended");
//
//        Logger.v("selectAppCancelled --" + selectAppCancelled);
//        Logger.v("isPinCancelled --" + isPinCancelled);
//        Logger.v("isScreenAvailable --" + isScreenAvailable);
//        Logger.v("madeOnlineConnection --" + madeOnlineConnection);
//        if (selectAppCancelled || isPinCancelled || ((isScreenAvailable != 1) && !madeOnlineConnection)) {
//            connection.onConnect(DO_CANCEL_AND_MOVE);
//            return;
//        }
//        showPinOkay();
//        isEMVCompleted = true;
//        Logger.v("Listner State --onEmvFinished");
//        Logger.v("Listner State --" + emvTransInfo.toString());
//        Logger.v("Listner State --getKernelId--" + String.format("%02x", emvTransInfo.getKernelId()));
//
//
//        AppConfig.EMV.emvTransInfo = emvTransInfo;
//        AppConfig.EMV.icKernalId = String.format("%02x", emvTransInfo.getKernelId());
//        Logger.v("kernal_id_-----" + AppConfig.EMV.icKernalId);
//        if (isCardInvalid) {
//            connection.onConnect(DO_CARD_INVALID);
//            Logger.v("isCardInvalid");
//            return;
//        }
//        if (isAmountExceed) {
//            connection.onConnect(DO_AMOUNT_EXCEED);
//            Logger.v("isAmountExceed");
//            return;
//        }
//
//        if (isTransactionNotAllowed) {
//            connection.onConnect(DO_TRANSACTION);
//            Logger.v("isTransactionNotAllowed");
//            return;
//        }
//
//        // TTransaction code
//        int executeRslt = emvTransInfo.getExecuteRslt();
//        Logger.v("executeRslt----" + executeRslt);
//        String resultMsg = null;
//        switch (executeRslt) {
//            case 0:
//            case 1:
//                resultMsg = context.getString(R.string.msg_trans_acc);
//                break;
//            case 2:
//                resultMsg = context.getString(R.string.msg_trans_reject);
//                break;
//            case 3:
//                resultMsg = context.getString(R.string.msg_request_online);
//                break;
//            case -2105:
//                resultMsg = context.getString(R.string.msg_trans_exceed_limit);
//                break;
//            default:
//                resultMsg = context.getString(R.string.msg_trans_failed);
//                break;
//        }
//        showMessage(context.getString(R.string.msg_trans_result) + resultMsg + "\r\n", MessageTag.DATA, 1);
//        int[] emvTags = new int[3];
//        emvTags[0] = 0x5a;
//        emvTags[1] = 0x5F34;
//        emvTags[2] = 0x57;
//        TLVPackage tlv = ISOUtils.newTlvPackage();
//        String data = emvModule.fetchEmvData(emvTags);
//        tlv.unpack(ISOUtils.hex2byte(data));
//        String cardNo = tlv.getString(0x5a);
//        String track2 = tlv.getString(0x57); // Two track  data
//        Logger.ve("Listner ended -" + isAppSelect + "--" + track2);
//
//        if (null == cardNo && track2 != null) {
//            cardNo = track2.substring(0, track2.indexOf('D'));
//        }
//        String cardSN = tlv.getString(0x5F34);// PAN
//        String expiredDate = null;
//
//        if (track2 != null) {
//            expiredDate = track2.substring(track2.indexOf('D') + 1, track2.indexOf('D') + 5);
//        }
//        if (cardSN == null) {
//            cardSN = "000";
//        } else {
//            cardSN = ISOUtils.padleft(cardSN, 3, '0');
//        }
//        String serviceCode = "";
//        if (null != track2) {
//            serviceCode = track2.substring(track2.indexOf('D') + 5, track2.indexOf('D') + 8);
//
//        }
//        //Since the array is BCD encoded, the last digit of the card number needs to be removed if it is 'F'.
//        if (null != cardNo && cardNo.endsWith("F"))
//            cardNo = cardNo.substring(0, cardNo.length() - 1);
//        showMessage(context.getString(R.string.msg_term_cardNo) + cardNo + "\r\n", MessageTag.DATA, 3);
//        showMessage(context.getString(R.string.msg_term_cardSN) + cardSN + "\r\n", MessageTag.DATA, 4);
//        showMessage(context.getString(R.string.msg_term_expiredDate) + expiredDate + "\r\n", MessageTag.DATA, 5);
//        showMessage(context.getString(R.string.msg_term_serviceCode) + serviceCode + "\r\n", MessageTag.DATA, 6);
//        showMessage(context.getString(R.string.msg_term_track2) + track2 + "\r\n", MessageTag.DATA, 7);
//        AppConfig.EMV.icCardSerialNum = cardSN;
//        if (cardNo == null) {
//            if (AppConfig.EMV.consumeType == 2)
//                connection.onConnect(DO_WAVE_AGAIN);
//            else
//                connection.onConnect(DO_ERROR);
//            Logger.v("Case 8");
//            return;
//        }
//        doBeepOnce();
//        if (AppConfig.EMV.consumeType == 2 && !madeOnlineConnection) {
//            new LightsDisplay(this.context).showAllLights();
//            Thread.sleep(1000);
//            new LightsDisplay(this.context).hideAllLights();
//        }
//
////        if ((context instanceof PrintActivity && !isCancelled) || (isCancelled && !madeOnlineConnection && responseData38.trim().equalsIgnoreCase("5933"))) {
//        if (context instanceof PrintActivity &&(SimpleTransferListener.isScreenAvailable == 1)) {
//            ((PrintActivity) context).showStatusDiaolge(executeRslt,emvTransInfo.getErrorcode() == 0, madeOnlineConnection);
//            Thread.sleep(1000);
//        }
//        if (null != track2) {
//            K21Swiper swiper = sdkDevice.getInstance(context).getK21Swiper();
//            SwipResult swipRslt = swiper.calculateTrackData(track2, null, new WorkingKey(trackKeyIndex), SupportMSDAlgorithm.getMSDAlgorithm(encryptAlgorithm));
//            showMessage(context.getString(R.string.msg_term_track2_ciphertext) + (swipRslt.getSecondTrackData() == null ? null : ISOUtils.hexString(swipRslt.getSecondTrackData())) + "\r\n", MessageTag.DATA, 8);
//        }
//
//        String data55 = emvModule.fetchEmvData(L_55TAGS);
//        Logger.v("data55 --" + data55);
//        Logger.v("MyData55 --" + data55);
//
////        AppConfig.EMV.icApplicationCryptogram = data55.split("9F27")[1].substring(2, 4);
//        showMessage(context.getString(R.string.msg_term_55tag) + data55 + "\r\n", MessageTag.DATA, 9);
//        AppConfig.EMV.ic55Data = data55;// 55 filed data
//
//        if (emvTransInfo.getOpenCardType() == ModuleType.COMMON_RFCARDREADER) {
//            Logger.v("Inside RF --");
//            HashMap<String, String> tag55 = Utils.getParsedTag55(AppConfig.EMV.ic55Data);
//            String tagOnline = tag55.get(TAG55[14]);
//            Logger.v("TAG Online--" + tagOnline);
//            Logger.v("TAG Online--" + emvTransInfo.getCvm());
//            if (!(data55.contains("9f34") || data55.contains("9F34")))
//                AppConfig.EMV.ic55Data = data55 + "9F3403" + appendCVM(emvTransInfo.getCvm());// Trans info
//            Logger.v("AppConfig.EMV.ic55Data --" + AppConfig.EMV.ic55Data);
//        }
//        AppConfig.EMV.icCardTrack2data = track2;
//        AppConfig.EMV.icExpiredDate = expiredDate;
//        Logger.v("emvTransInfo.getCardNo() --" + cardNo);
//        AppConfig.EMV.icCardNum = cardNo;
//        showCard();
//        String dataScript = emvModule.fetchEmvData(L_SCRIPTTAGS);
//        showMessage(context.getString(R.string.msg_term_script) + dataScript + "\r\n", MessageTag.DATA, 10);
//        String revData = emvModule.fetchEmvData(L_REVTAGS);
//        showMessage(context.getString(R.string.msg_term_flushes_data) + revData + "\r\n", MessageTag.DATA, 11);
//        Logger.v("isCancelled " + isCancelled);
//        Logger.v("isPinRequires " + isPinRequires);
//        Logger.v("isPinCancelled " + isPinCancelled);
//        Logger.v("Pin " + (pinBlock != null));
////        if (!isCancelled && (!isPinRequires || (pinBlock != null)))
//        HashMap<String, String> tag55 = Utils.getParsedTag55(ic55Data);
//        String tagoffLine = tag55.get(TAG55[2]);
//        Logger.v("tagoffLine -" + tagoffLine);
//        Logger.v("tagoffLine -" + madeOnlineConnection);
//        Logger.v("AppConfig.EMV.enableDatabaseUpdate --" + AppConfig.EMV.enableDatabaseUpdate);
//        Logger.v("AppConfig.EMV.enableDatabaseUpdate --" + executeRslt);
//        if (AppConfig.EMV.enableDatabaseUpdate) {
//            AppDatabase database = AppDatabase.getInstance(context.getApplicationContext());
//            TransactionModelEntity transaction = database.getTransactionDao().getLastTransaction(false);
//            if(isScreenAvailable == 1) {
//                database.getTransactionDao().updateResponse(transaction.getUid(), (executeRslt == 0 || executeRslt == 3) ? 1 : executeRslt);
//                AppManager.getInstance().setDuplicateTransactionModelEntity(database.getTransactionDao().getTransaction(transaction.getUid()));
//            }else
//                database.getTransactionDao().updateResponse(transaction.getUid(), 0);
//            database.getTransactionDao().updateData55(transaction.getUid(),ic55Data);
//        }
//        Log.v("responce", "Listner ended 1");
//
//        String data8a = null;
//        if (errorCode != 1) {
//            //The specific reason for the error code
//            int errorCode = emvTransInfo.getErrorcode();
//            Logger.v("ErrorCode--" + errorCode);
//            if(responseData55.trim().length() != 0 ) {
//                if (responseData55.substring(0, 2).equalsIgnoreCase("8a")) {
//                    TLVPackage tlv1 = ISOUtils.newTlvPackage();
//                    tlv1.unpack(ISOUtils.hex2byte(responseData55));
//                    data8a = tlv1.getString(0x8a);
//                }else {
//                    if (AppManager.getInstance().getDe39().equalsIgnoreCase(Constant.SUCCESS_RESPONSE_000)
//                            || AppManager.getInstance().getDe39().equalsIgnoreCase(Constant.SUCCESS_RESPONSE_001)
//                            || AppManager.getInstance().getDe39().equalsIgnoreCase(Constant.SUCCESS_RESPONSE_003)
//                            || AppManager.getInstance().getDe39().equalsIgnoreCase(Constant.SUCCESS_RESPONSE_007))
//                        data8a = ("3030");// 0x8a Transaction reply code:Taken from the 39 field value of unionpay 8583 specification, this parameter is populated with the actual value of the transaction.
//                }
//            }
//            if (errorCode != 0) {
//                if (madeOnlineConnection && data8a != null && data8a.trim().equalsIgnoreCase("3030")) {
//                    Logger.v("REversal 1");
//                    if (AppConfig.EMV.enableDatabaseUpdate) {
//                        AppDatabase database = AppDatabase.getInstance(context.getApplicationContext());
//                        TransactionModelEntity transaction = database.getTransactionDao().getLastTransaction(false);
//                        database.getTransactionDao().updateResponse39(transaction.getUid(), "190");
//                        AppManager.getInstance().setDuplicateTransactionModelEntity(database.getTransactionDao().getTransaction(transaction.getUid()));
//                    }
//                    connection.onConnect(DO_REVERSAL);
//                    Logger.v("Case 9");
//                    return;
//                }
//                if (getErrorMessgae(context, errorCode) != null) {
//                    connection.onConnect(DO_ERROR_MESSAGE);
//                    Logger.v("Case 99");
//                    return;
//                } else {
//                    Logger.v("Error 1--" + madeOnlineConnection);
//                    Logger.v("Case 10");
////                connection.onConnect(DO_ERROR);
//                    showMessage(context.getString(R.string.msg_trans_error_details) + errorCode + resultMsg + "\r\n", MessageTag.DATA, 2);
//                    if (madeOnlineConnection)
//                        connection.onConnect(FINAL_ERROR);
//                    else
//                        connection.onConnect(DO_SAF_DECLINED);
//                    return;
//                }
//            } else
//                Logger.v("No Error");
//        }
//        if (isPinCancelled) {
//            Logger.v("Case 1");
//            connection.onConnect(DO_ERROR_PIN_CANCELLED);
//        } else if (isSAFDecilined && (transType.equalsIgnoreCase(Constant.REFUND) || transType.equalsIgnoreCase(Constant.PURCHASE) || transType.equalsIgnoreCase(Constant.CASH_ADVANCE))) {
//            Logger.v("Case 2");
//            CountDownResponseTimer.cancelTimer(999);
//            if ((executeRslt == 0 || executeRslt == 1))
//                connection.onConnect(DO_SAF_APPROVED);
//            else
//                connection.onConnect(DO_SAF_REJECTED);
//        } else if (tagoffLine != null && (tagoffLine.equalsIgnoreCase("00") || tagoffLine.equalsIgnoreCase("80")) && !madeOnlineConnection) {
//            Logger.v("Case 3");
//            CountDownResponseTimer.cancelTimer(888);
//            connection.onConnect(DO_SAF_DECLINED);
//        } else if ((executeRslt == 0 || executeRslt == 1) && ((transType.equalsIgnoreCase(Constant.REFUND) && !madeOnlineConnection) || (tagoffLine != null && tagoffLine.equalsIgnoreCase("40") && !madeOnlineConnection))) {
//            Logger.v("Case 4");
//            CountDownResponseTimer.cancelTimer(777);
//            connection.onConnect(DO_SAF);
//        } else if (doReversal) {
//            Logger.v("Case 5");
//            connection.onConnect(DO_REVERSAL);
//        } else if ((executeRslt != 0 && executeRslt != 1 && executeRslt != 3)
//                && data8a != null && data8a.trim().equalsIgnoreCase("3030")
//                && (AppManager.getInstance().getDe39() != null) && AppManager.getInstance().getDe39().equalsIgnoreCase("000")) {
//            Logger.v("Case 6");
//            connection.onConnect(DO_REVERSAL);
//        } else {
//            Logger.v("Case 7");
//            if ((isScreenAvailable == 2)){
//                connection.onConnect(DO_REVERSAL_CANCEL);
//            }else if((isScreenAvailable == 1))
//                MapperFlow.getInstance().moveToPrintScreen(context);
//        }
////		if(context instanceof LandingPageActivity) {
////			Logger.v("response", "context instanceof LandingPageActivity");
////			((LandingPageActivity) context).readerRFANDIC(emvTransInfo);
////		}else if(context instanceof TransactionActivity) {
////			Logger.v("response", "context instanceof TransactionActivity");
////			((TransactionActivity) context).readerRFANDIC(emvTransInfo);
////		}else
////			Logger.v("response", "context instanceof else");
//    }
//
//    private void showCard() {
//        if(showCard) {
//            if (context instanceof PrintActivity) {
//                ((PrintActivity) context).setAccountNo();
//            }
//            showCard = false;
//        }
//    }
//
//    @Override
//    public void onError(EmvTransController arg0, Exception arg1) {
//        if ((isScreenAvailable != 1))
//            connection.onConnect(DO_CANCEL_AND_MOVE);
//        Logger.v("Listner State --onError");
//        showMessage(context.getString(R.string.msg_emv_trans_failed) + arg1.getMessage() + "\r\n", MessageTag.ERROR, 12);
//        arg1.printStackTrace();
//        String de39 = AppManager.getInstance().getDe39();
//        if (de39.trim().length() != 0) {
//            if (de39.equalsIgnoreCase(Constant.SUCCESS_RESPONSE_000) || de39.equalsIgnoreCase(Constant.SUCCESS_RESPONSE_001)
//                    || de39.equalsIgnoreCase(Constant.SUCCESS_RESPONSE_003) || de39.equalsIgnoreCase(Constant.SUCCESS_RESPONSE_007)) {
//                doReversal = true;
//            }
//        }
//        errorCode = 1;
//        arg0.doEmvFinish(false);
//    }
//
//    @Override
//    public void onFallback(EmvTransInfo arg0) throws Exception {
//        Logger.ve("Listner State --onFallback--"+arg0.getErrorcode());
//        if ((isScreenAvailable != 1))
//            connection.onConnect(DO_CANCEL_AND_MOVE);
//
//        if (AppConfig.EMV.consumeType == 2) {
//            new LightsDisplay(this.context).showAllLights();
//            Thread.sleep(1000);
//            new LightsDisplay(this.context).hideAllLights();
//        }
//
//        showMessage(context.getString(R.string.msg_ic_env_notmeet_fallback) + "\r\n", MessageTag.ERROR, 121);
//        if(arg0.getErrorcode() == -5){
//            connection.onConnect(DO_FALLBACK_APP);
//        }else
//            connection.onConnect(DO_FALLBACK);
//    }
//
//    @Override
//    public void onRequestOnline(EmvTransController controller, EmvTransInfo emvTransInfo) throws Exception {
//        if ((isScreenAvailable != 1)) {
//            controller.doEmvFinish(false);
//            return;
//        }
//        Logger.ve("Listner State --onRequestOnline");
//        doBeepOnce();
//        showPinOkay();
//        if (AppConfig.EMV.consumeType == 2) {
//            new LightsDisplay(this.context).showAllLights();
//            Thread.sleep(1000);
//            new LightsDisplay(this.context).hideAllLights();
//        }
//        AppConfig.EMV.icKernalId = String.format("%02x", emvTransInfo.getKernelId());
//        Logger.v("kernal_id2-----" + AppConfig.EMV.icKernalId);
//        madeOnlineConnection = true;
//        int emvResult = emvTransInfo.getEmvrsltCode();
//        Logger.v("onRequestOnline--" + emvResult);
//        isGccNet = getCurrencyCode(emvTransInfo.getAid());
//        Logger.v("currencyCode --" + isGccNet);
//        String resultMsg = null;
//        switch (emvResult) {
//            case 3:
//                resultMsg = context.getString(R.string.msg_pboc_online);
//                break;
//            case 15:
//                resultMsg = context.getString(R.string.msg_rfqpboc_online);
//                break;
//        }
//        showMessage(context.getString(R.string.msg_request_online_result) + resultMsg + "\r\n", MessageTag.DATA, 13);
//
//        int[] emvTags = new int[5];
//        emvTags[0] = 0x5a;
//        emvTags[1] = 0x5F34;
//        emvTags[2] = 0x5f24;
//        emvTags[3] = 0x57;
//        emvTags[4] = 0x81;
//        String data = emvModule.fetchEmvData(emvTags);
//        Logger.v("onRequestOnline - " + data);
//        TLVPackage tlv = ISOUtils.newTlvPackage();
//        tlv.unpack(ISOUtils.hex2byte(data));
//        String cardNo = tlv.getString(0x5a);
//        String cardSN = tlv.getString(0x5F34);// Card serial number == context.getCardSequenceNumber()
//        String track2 = tlv.getString(0x57); // Two track data == context.getTrack_2_eqv_data()
//        showMessage("Tracke 2 Chipt" + "---" + track2, MessageTag.DATA, 28);
//
//        if (null == cardNo && track2 != null) {
//            cardNo = track2.substring(0, track2.indexOf('D'));
//        }
//        String expiredDate = null;
//        if (track2 != null) {
//            expiredDate = track2.substring(track2.indexOf('D') + 1, track2.indexOf('D') + 5);
//        }
//        if (cardSN == null) {
//            cardSN = "000";
//        } else {
//            cardSN = ISOUtils.padleft(cardSN, 3, '0');
//        }
//        String serviceCode = "";
//        if (null != track2) {
//            serviceCode = track2.substring(track2.indexOf('D') + 5, track2.indexOf('D') + 8);
//        }
//        //Since the array is BCD encoded, the last digit of the card number needs to be removed if it is 'F'.
//        if (null != cardNo && cardNo.endsWith("F"))
//            cardNo = cardNo.substring(0, cardNo.length() - 1);
//        showMessage(context.getString(R.string.msg_term_cardNo) + cardNo + "\r\n", MessageTag.DATA, 14);
//        showMessage(context.getString(R.string.msg_term_cardSN) + cardSN + "\r\n", MessageTag.DATA, 15);
//        showMessage(context.getString(R.string.msg_term_expiredDate) + expiredDate + "\r\n", MessageTag.DATA, 16);
//        showMessage(context.getString(R.string.msg_term_serviceCode) + serviceCode + "\r\n", MessageTag.DATA, 17);
//        showMessage(context.getString(R.string.msg_term_track2) + track2 + "\r\n", MessageTag.DATA, 18);
//        AppConfig.EMV.icCardNum = emvTransInfo.getCardNo();
//        Logger.v("AppConfig.EMV.icCardSerialNum11 -" + cardSN);
//        Logger.v("emvTransInfo.getCvm() -" + emvTransInfo.getCvm());
//        AppConfig.EMV.icCardSerialNum = cardSN;
//        AppConfig.EMV.icCardTrack2data = track2;
//        AppConfig.EMV.icExpiredDate = expiredDate;
//        AppConfig.EMV.emvTransInfo = emvTransInfo;//  Emv Trans Info
//        if (null != track2) {
//            K21Swiper swiper = SDKDevice.getInstance(context).getK21Swiper();
//            SwipResult swipRslt = swiper.calculateTrackData(track2, null, new WorkingKey(trackKeyIndex), SupportMSDAlgorithm.getMSDAlgorithm(encryptAlgorithm));
//            showMessage(context.getString(R.string.msg_term_track2_ciphertext) + (swipRslt.getSecondTrackData() == null ? null : ISOUtils.hexString(swipRslt.getSecondTrackData())) + "\r\n", MessageTag.DATA, 19);
//        }
//        String data55 = emvModule.fetchEmvData(L_55TAGS);
//        Logger.v("MyData55 --" + data55);
//        showMessage(context.getString(R.string.msg_term_55tag) + data55 + "\r\n", MessageTag.DATA, 20);
//        AppConfig.EMV.ic55Data = data55;// Trans info
//        String dataScript = emvModule.fetchEmvData(L_SCRIPTTAGS);
//        showMessage(context.getString(R.string.msg_term_script) + dataScript + "\r\n", MessageTag.DATA, 21);
//        String revData = emvModule.fetchEmvData(L_REVTAGS);
//        showMessage(context.getString(R.string.msg_term_flushes_data) + revData + "\r\n", MessageTag.DATA, 22);
//
//        if (emvTransInfo.getOpenCardType() == ModuleType.COMMON_RFCARDREADER) {
//            Logger.v("Inside RF --");
//            HashMap<String, String> tag55 = Utils.getParsedTag55(AppConfig.EMV.ic55Data);
//            String tagOnline = tag55.get(TAG55[14]);
//            Logger.v("TAG Online--" + tagOnline);
//            Logger.v("TAG Online--" + emvTransInfo.getCvm());
//            if (!(data55.contains("9f34") || data55.contains("9F34")))
//                AppConfig.EMV.ic55Data = data55 + "9F3403" + appendCVM(emvTransInfo.getCvm());// Trans info
//
//            boolean promtPin = ((emvTransInfo.getCvm() == EmvConst.OP_ONLINE_PIN));
//            Logger.v("TAG Online--" + promtPin);
//            if (promtPin) {
//                isPinRequires = 1;
//                rf_thread = 1;
//                if ((isScreenAvailable == 1))
//                    MapperFlow.getInstance().startOnlinePinInput(context, emvTransInfo.getCardNo());
//                waitPinInputThreat_rf.waitForRslt();
//
////                Logger.v("Pin 11" + (pinBlock != null));
////                if (pinBlock == null) {
////                    Logger.v("Pinn enter 11");
////                    isPinCancelled = true;
////                    controller.doEmvFinish(false);
////                    return;
////                }
//                showMessage(context.getString(R.string.msg_pwd) + (pinBlock == null ? "null" : ISOUtils.hexString(pinBlock)), MessageTag.DATA, 29);
//                if (pinBlock == null)
//                    isPinRequires = 0;
//
//                if (isPinCancelled) {
//                    controller.sendPinInputResult(null);
//                    controller.doEmvFinish(false);
//                    return;
//                } else
//                    controller.sendPinInputResult(pinBlock);
//            }
//        } else {
//            Logger.v("Inside RF -else-");
////            boolean promtPin = ((emvTransInfo.getCvm() == EmvConst.OP_ONLINE_PIN));
////            if (!promtPin) {
////                if (transType.equalsIgnoreCase(Constant.REFUND) || transType.equalsIgnoreCase(Constant.PRE_AUTHORISATION) ||
////                        transType.equalsIgnoreCase(Constant.PRE_AUTHORISATION_VOID) || transType.equalsIgnoreCase(Constant.PRE_AUTHORISATION_EXTENSION)
////                        || transType.equalsIgnoreCase(Constant.PURCHASE_ADVICE) || transType.equalsIgnoreCase(Constant.PURCHASE_ADVICE_FULL)
////                        || transType.equalsIgnoreCase(Constant.PURCHASE_ADVICE_PARTIAL) || transType.equalsIgnoreCase(Constant.PURCHASE_ADVICE_MANUAL)) {
////                    String aid = ByteConversionUtils.byteArrayToHexString(emvTransInfo.getAid(), emvTransInfo.getAid().length, false);
////                    if (AppManager.getInstance().getRefundOfflineEnabled(Utils.fetchIndicatorFromAID(aid))) {
////                        madeOnlineConnection = false;
////                        controller.doEmvFinish(true);
////                    }
////                }
////            }
//        }
//
//        sendOnLineRequest(controller, emvTransInfo);
//    }
//
//    private void sendOnLineRequest(EmvTransController controller, EmvTransInfo emvTransInfo) throws InterruptedException {
//        Logger.v("Chip Transaction");
//        // [step1]：get ic card data from emvTransInfo then send to host
//        // TODO Online transaction ....
//        connection.onConnect((rf_thread == 2) ? DO_SOCKET_REQ_AGAIN : DO_SOCKET_REQ);
//        waitSockettThreat.waitForRslt();
//        Logger.v("Wait ended" + doReversal);
//        Logger.v("responseData55 --" + responseData55);
//        String de39 = AppManager.getInstance().getDe39();
//        if (doReversal && !isSAFDecilined && !AppManager.getInstance().isAdminNotificationReversal()) {
//            controller.doEmvFinish(false);
//            return;
//        } else if ((AppManager.getInstance().getResponseMTI() != null) && AppManager.getInstance().getResponseMTI().equalsIgnoreCase("1230")) {
//            if (de39.equalsIgnoreCase("000"))
//                controller.doEmvFinish(true);
//            else
//                controller.doEmvFinish(false);
//            return;
//        }
//        disableReversal = true;
//        if (!(de39.equalsIgnoreCase("117") || de39.equalsIgnoreCase("121") || de39.equalsIgnoreCase("196")) || (3 <= pinAttempt)) {
//            SecondIssuanceRequest request = new SecondIssuanceRequest();
//            if (!isCancelled && responseData38.trim().length() != 0)
//                request.setAuthorisationCode(responseData38);//0x89 Authorization code
//
//            if (!isCancelled && responseData55 != null && responseData55.trim().length() != 0) {
//                Logger.v("Inside Step 2");
//                if (responseData55.substring(0, 2).equalsIgnoreCase("8a")) {
//                TLVPackage tlv1 = ISOUtils.newTlvPackage();
//                tlv1.unpack(ISOUtils.hex2byte(responseData55));
//                String data8a = tlv1.getString(0x8a);
//                String data91 = tlv1.getString(0x91);
//                String data71 = tlv1.getString(0x71);
//                String data72 = tlv1.getString(0x72);
//                Logger.v("data8a -" + data8a);
//                Logger.v("data91 -" + data91);
//                request.setAuthorisationResponseCode(IsoRequest.getStringFromHex(data8a));// 0x8a Transaction reply code:Taken from the 39 field value of unionpay 8583 specification, this parameter is populated with the actual value of the transaction.
//                request.setField55(ISOUtils.hex2byte(responseData55));// 55 filed data of 8583 message
//                if (data91 != null && data91.trim().length() != 0)
//                    request.setIssuerAuthenticationData(ISOUtils.hex2byte(data91));
//                // data 71 and 71 is for issuer certificate
//                if (data71 != null && data71.trim().length() != 0)
//                    request.setIssuerScriptTemplate1(ISOUtils.hex2byte(data71));
//                if (data72 != null && data72.trim().length() != 0)
//                    request.setIssuerScriptTemplate2(ISOUtils.hex2byte(data72));
//                Logger.v("issuer_certificate------" + data71 + "======" + data72);
//                } else {
//                    if (AppManager.getInstance().getDe39().equalsIgnoreCase(Constant.SUCCESS_RESPONSE_000)
//                            || AppManager.getInstance().getDe39().equalsIgnoreCase(Constant.SUCCESS_RESPONSE_001)
//                            || AppManager.getInstance().getDe39().equalsIgnoreCase(Constant.SUCCESS_RESPONSE_003)
//                            || AppManager.getInstance().getDe39().equalsIgnoreCase(Constant.SUCCESS_RESPONSE_007))
//                        request.setAuthorisationResponseCode(IsoRequest.getStringFromHex("3030"));// 0x8a Transaction reply code:Taken from the 39 field value of unionpay 8583 specification, this parameter is populated with the actual value of the transaction.
//                }
//            } else if (isCancelled && responseData38.trim().length() != 0) {
//                madeOnlineConnection = false;
//                request.setAuthorisationResponseCode(responseData38);//0x89 Authorization code
//                Logger.v("--" + isCancelled + "--" + responseData55);
//            }
////            controller.doEmvFinish(false);
//            //[Step2].Online transaction success or credit for load wait onemvfinished callback to end porcess after calling secondIssuance.
//            Logger.v("Second Issuer Req");
//            controller.secondIssuance(request);
//        } else {
//            pinAttempt = pinAttempt + 1;
//            connection.onConnect(DO_INVALID_PIN);
//            rf_thread = 2;
//            if ((isScreenAvailable == 1))
//                MapperFlow.getInstance().startOnlinePinInput(context, emvTransInfo.getCardNo());
//            waitPinInputThreat_pin.waitForRslt();
//
////            Logger.v("Pin 11" + (pinBlock != null));
////            if (pinBlock == null) {
////                Logger.v("Pinn enter 11");
////                isPinCancelled = true;
////                controller.doEmvFinish(false);
////                return;
////            }
//            showMessage(context.getString(R.string.msg_pwd) + (pinBlock == null ? "null" : ISOUtils.hexString(pinBlock)), MessageTag.DATA, 29);
//            if (pinBlock == null)
//                isPinRequires = 0;
//            if (isPinCancelled) {
//                controller.sendPinInputResult(null);
//                controller.doEmvFinish(false);
//                return;
//            } else
//                controller.sendPinInputResult(pinBlock);
//
//            sendOnLineRequest(controller, emvTransInfo);
//        }
//    }
//
//    private String appendCVM(byte cvm) {
//        switch (cvm) {
//            case EmvConst.OP_ONLINE_PIN:
//                return Constant.CVM_ONLINE_PIN_VALUE;
//            case EmvConst.OP_NO_CVM:
//                return Constant.CVM_NO;
//        }
//        return Constant.CVM_SIGNATURE;
//    }
//
//    private boolean getCurrencyCode(byte[] getAid) {
//        String aid = ByteConversionUtils.byteArrayToHexString(getAid, getAid.length, false);
//        Logger.v("aid--" + aid);
//        if (!isMada) {
//            if (transType.equalsIgnoreCase(Constant.REFUND) || transType.equalsIgnoreCase(Constant.PURCHASE)) {
//                int country = 0;
//                byte[] countrycode = sdkDevice.getEmvModuleType().getEmvData(0x5F28);
//                byte[] countrycode1 = sdkDevice.getEmvModuleType().getEmvData(0x9F42);
//                if (countrycode != null && countrycode.length != 0) {
//                    country = Integer.parseInt(ISOUtils.hexString(countrycode));
//                    Logger.v(ISOUtils.hexString(countrycode));
//                    Logger.v("CurrencyCode - ");
//                } else if (countrycode1 != null && (countrycode1.length != 0)) {
//                    country = Integer.parseInt(ISOUtils.hexString(countrycode1));
//                    Logger.v(ISOUtils.hexString(countrycode1));
//                } else
//                    Logger.v("Currency else");
//                if (country == 48 || country == 414 || country == 512 || country == 634 || country == 784) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//
//    //Multi-application card will callback this method for  select application
//    @Override
//    public void onRequestSelectApplication(EmvTransController arg0, EmvTransInfo arg1) throws Exception {
//        if ((isScreenAvailable != 1)) {
//            arg0.doEmvFinish(false);
//            return;
//        }
//        Logger.ve("Listner State --onRequestSelectApplication");
//        showMessage(context.getString(R.string.msg_select_app_hint) + "\r\n", MessageTag.DATA, 23);
//        Map<byte[], AIDSelect> map = arg1.getAidSelectMap();
//        List<String> nameList = new ArrayList<String>();
//        List<byte[]> aidList = new ArrayList<byte[]>();
//        boolean isMada = false;
//        for (Entry<byte[], AIDSelect> entry : map.entrySet()) {
//            nameList.add(entry.getValue().getName());
//            aidList.add(entry.getValue().getAid());
//            if (!isMada) {
//                String aidTrim = ISOUtils.hexdump(entry.getValue().getAid()).replaceAll(" ", "");
//                Logger.v("aidTrim. -" + aidTrim);
//                isMada = aidTrim.contains(Constant.A0000002281010) || aidTrim.contains(Constant.A0000002282010);
//                if (isMada)
//                    selectApplication = entry.getValue().getAid();
//            }
//            showMessage(context.getString(R.string.msg_aid_name) + entry.getValue().getName(), MessageTag.DATA, 24);
//            showMessage(context.getString(R.string.msg_aid) + ISOUtils.hexdump(entry.getValue().getAid()), MessageTag.DATA, 25);
////            arg0.selectApplication(entry.getValue().getAid());
//        }
//        if (isMada) {
//            Logger.v("Mada aid");
//            arg0.selectApplication(selectApplication);
//        } else {
//            selectApplication = aidList.get(0);
//            Logger.v("aidList.size() --" + aidList.size());
//            // Select the first application by default
//            if (1 < aidList.size()) {
//                Logger.v("aidList.size() --" + aidList.size());
//                ((PrintActivity) context).showAppSection(aidList, nameList);
//                waitSelectApp.waitForRslt();
//            }
//            if (selectApplication != null) {
//                selectAppCancelled = false;
//                arg0.selectApplication(selectApplication);
//            } else {
//                selectAppCancelled = true;
//                arg0.doEmvFinish(false);
//            }
//        }
//    }
//
//    @Override
//    public void onRequestTransferConfirm(EmvTransController controller, EmvTransInfo arg1) throws Exception {
//        Logger.ve("Listner State --onRequestTransferConfirm");
//        showMessage(context.getString(R.string.msg_trans_confirm_finish) + "\r\n", MessageTag.DATA, 26);
//        controller.transferConfirm(true);
//    }
//
//    // IM81 and N900 would call back，ME30 and ME31 would not call back.
//    @Override
//    public void onRequestPinEntry(final EmvTransController emvTransController, EmvTransInfo emvTransInfo) throws Exception {
//        if ((isScreenAvailable != 1)) {
//            emvTransController.doEmvFinish(false);
//            return;
//        }
//        Logger.ve("Listner State --onRequestPinEntry");
//        showPinOkay = false;
//        doBeepOnce();
//        if (emvTransInfo.getCardNo() != null) {
//            AppConfig.EMV.icCardNum = emvTransInfo.getCardNo();
//            AppConfig.EMV.emvTransInfo = emvTransInfo;
//            showCard();
//        } else {
//            showMessage(context.getString(R.string.msg_cardno_null) + "\r\n", MessageTag.DATA, 27);
//        }
//
//        showMessage("Tracke 2 Chipt" + "---" + new String(emvTransInfo.getTrack_2_eqv_data()), MessageTag.DATA, 28);
//        if (emvTransInfo.getCardNo() != null) {
//            showMessage(context.getString(R.string.msg_enter_6_pwd) + "\r\n", MessageTag.DATA, 28);
//            isPinCancelled = false;
//            if (emvTransInfo.getEmvPinInputType() == EmvPinInputType.OFFLINE || emvTransInfo.getEmvPinInputType() == EmvPinInputType.OFFLINE_ONLY) {
//                doPinInput(false, emvTransInfo);
//            } else {
//                doPinInput(true, emvTransInfo);
//            }
//            Logger.v("Pin " + (pinBlock != null));
//
//            waitPinInputThreat.waitForRslt();
//            Logger.v("Pin " + (pinBlock != null));
////            if (pinBlock == null) {
////                Logger.v("Pinn enter");
////                isPinCancelled = true;
////                emvTransController.doEmvFinish(false);
////                return;
////            }
//            showMessage(context.getString(R.string.msg_pwd) + (pinBlock == null ? "null" : ISOUtils.hexString(pinBlock)), MessageTag.DATA, 29);
//            if (emvTransInfo.getEmvPinInputType() == EmvPinInputType.OFFLINE || emvTransInfo.getEmvPinInputType() == EmvPinInputType.OFFLINE_ONLY) {
//                Logger.v("CVM Result --" + emvTransInfo);
//                if (context instanceof PrintActivity) {
//                    showPinOkay = true;
//                }
//            }
//            if (pinBlock == null)
//                isPinRequires = 0;
//
//            Logger.v("Pin Loading into EMV-" + isCancelled);
//            Logger.v("Pin Loading into EMV-" + isPinCancelled);
//            if (isPinCancelled) {
//                emvTransController.sendPinInputResult(null);
//                emvTransController.doEmvFinish(false);
//                return;
//            } else if (pinBlock == null)
//                emvTransController.sendPinInputResult(new byte[]{});
//            else
//                emvTransController.sendPinInputResult(pinBlock);
//
//        } else {
//            showMessage(context.getString(R.string.msg_swipresult_null) + "\r\n", MessageTag.DATA, 30);
//        }
//    }
//
//    /**
//     * input password
//     *
//     * @param isOnline     is it online pin?
//     * @param emvTransInfo emvTransInfo
//     *                     //	 * @throws Exception
//     */
//    public void doPinInput(boolean isOnline, EmvTransInfo emvTransInfo) throws Exception {
//        Logger.ve("Listner State --doPinInput");
//        Logger.v("response", "Logger -" + isOnline);
//        isPinRequires = (isOnline) ? 1 : 2;
//        if ((isScreenAvailable == 1)) {
//            if (isOnline) {
//                MapperFlow.getInstance().startOnlinePinInput(context, emvTransInfo.getCardNo());
////			if(context instanceof TransactionActivity)
////				((TransactionActivity)context).startOnlinePinInput(emvTransInfo.getCardNo());
////			else if(context instanceof LandingPageActivity)
////				((LandingPageActivity)context).startOnlinePinInput(emvTransInfo.getCardNo());
//            } else {
//                if (pinBlock != null) {
//                    byte[] tagAttempt = emvModule.getEmvData(0x9F17);
//                    if (tagAttempt != null && tagAttempt.length != 0) {
//                        String attempt = ISOUtils.hexString(tagAttempt);
//                        Logger.v("ATTEMPTS --" + attempt);
//                        MapperFlow.getInstance().startOfflinePinInput(context, emvTransInfo.getModulus(), emvTransInfo.getExponent(), attempt);
//                    } else
//                        MapperFlow.getInstance().startOfflinePinInput(context, emvTransInfo.getModulus(), emvTransInfo.getExponent(), null);
//                } else
//                    MapperFlow.getInstance().startOfflinePinInput(context, emvTransInfo.getModulus(), emvTransInfo.getExponent(), null);
////			if(context instanceof TransactionActivity)
////				((TransactionActivity)context).startOfflinePinInput(emvTransInfo.getModulus(),emvTransInfo.getExponent());
////			else if(context instanceof LandingPageActivity)
////				((LandingPageActivity)context).startOfflinePinInput(emvTransInfo.getModulus(),emvTransInfo.getExponent());
//
//            }
//        }
//    }
//
//    /**
//     * Whether to intercept acctType select event
//     *
//     * @return
//     */
//    @Override
//    public boolean isAccountTypeSelectInterceptor() {
//        return true;
//    }
//
//    /**
//     * Whether to intercept the cardHolder certificated confirmation  event
//     *
//     * @return
//     */
//    @Override
//    public boolean isCardHolderCertConfirmInterceptor() {
//        return true;
//    }
//
//    /**
//     * whether intercept electron cash confirmation event
//     *
//     * @return
//     */
//    @Override
//    public boolean isEcSwitchInterceptor() {
//        return true;
//    }
//
//    /**
//     * Whether intercept to use  external sequence number processor
//     *
//     * @return
//     */
//    @Override
//    public boolean isTransferSequenceGenerateInterceptor() {
//        return true;
//    }
//
//    /**
//     * Whether intercept to show message on LCD event
//     *
//     * @return
//     */
//    @Override
//    public boolean isLCDMsgInterceptor() {
//        return true;
//    }
//
//
//    /**
//     * account type selection
//     * <p>
//     * return to int range
//     * <p>
//     * <ol>
//     * <li>default</li>
//     * <li>savings</li>
//     * <li>Cheque/debit</li>
//     * <li>Credit</li>
//     * </ol>
//     *
//     * @return 1-4:selection range， －1：failed
//     */
//    @Override
//    public int accTypeSelect() {
//        return 1;
//    }
//
//    /**
//     * cardHolder certificated confirmation
//     * <p>
//     *
//     * @return true:confirmation succeed， false:confirmation failed
//     */
//    @Override
//    public boolean cardHolderCertConfirm(EmvCardholderCertType certType, String certno) {
//        return true;
//    }
//
//    /**
//     * Ecash /emv selection
//     * <p>
//     * transaction return：
//     * <p>
//     * <ul>
//     * <li>1:continue electronic cash transactions</li>
//     * <li>0:do not carry out electronic cash transactions</li>
//     * <li>－1:user termination</li>
//     * <li>－3:time out</li>
//     * </ul>
//     */
//    @Override
//    public int ecSwitch() {
//        Logger.ve("Listner State --ecSwitch");
////        try {
////            final WaitThreat waitThreat = new WaitThreat();
////            final Builder builder = new Builder(context);
////            builder.setMessage(context.getString(R.string.msg_is_use_ecash));
////            builder.setPositiveButton(context.getString(R.string.common_yes), new DialogInterface.OnClickListener() {
////
////                @Override
////                public void onClick(DialogInterface dialog, int which) {
////                    AppConfig.EMV.isECSwitch = 1;
////                    dialog.dismiss();
////                    isECSwitch = 1;
////                    waitThreat.notifyThread();
////                }
////            });
////            builder.setNegativeButton(context.getString(R.string.common_no), new DialogInterface.OnClickListener() {
////
////                @Override
////                public void onClick(DialogInterface dialog, int which) {
////                    AppConfig.EMV.isECSwitch = 0;
////                    dialog.dismiss();
////                    isECSwitch = 0;
////                    waitThreat.notifyThread();
////                }
////            });
//////			((MainActivity)context).runOnUiThread(new Runnable() {
//////
//////				@Override
//////				public void run() {
//////					builder.setCancelable(false);
//////					builder.show();
//////				}
//////			});
//////            waitThreat.waitForRslt();
////            // electronic cash return 1,otherwise return 0
////            return 0;
////        } catch (Exception e) {
////            e.printStackTrace();
////            return -1;
////        }
//        return -1;
//    }
//
//    /**
//     * serial number Add 1 and return
//     *
//     * @return
//     */
//    @Override
//    public int incTsc() {
//        return 0;
//    }
//
//    @Override
//    public boolean isLanguageselectInterceptor() {
//        return true;
//    }
//
//    /**
//     * display info
//     *
//     * @param title        title
//     * @param msg          message
//     * @param yesnoShowed  whether show yes no
//     * @param waittingTime waiting time
//     * @return if yesnoShow is equal to true, return 1 means confirmation.
//     * return 0 means cancel.
//     * if yesnoShow is equal to false,return value has no meaning.
//     */
//    @Override
//    public int lcdMsg(String title, String msg, boolean yesnoShowed, int waittingTime) {
//        return 1;
//    }
//
//    @Override
//    public byte[] languageSelect(byte[] language, int len) {
//        if (len >= 2) {
//            return new byte[]{language[0], language[1]};
//        }
//        return null;
//    }
//
//    // thread wait 、awake
//    public static class WaitThreat {
//        Object syncObj = new Object();
//
//        public void waitForRslt() throws InterruptedException {
//            synchronized (syncObj) {
//                syncObj.wait();
//            }
//        }
//
//        void notifyThread() {
//            synchronized (syncObj) {
//                syncObj.notify();
//            }
//        }
//    }
//
//    @Override
//    public void onRequestAmountEntry(final EmvTransController controller, EmvTransInfo emvTransInfo) {
//        Logger.ve("Listner State --onRequestAmountEntry");
//        showMessage(context.getString(R.string.msg_money_callback_request) + "\r\n", MessageTag.NORMAL, 31);
//        controller.sendAmtInputResult(new BigDecimal(AppConfig.EMV.amountValue));
////		DialogUtils.createCustomDialog(context, context.getString(R.string.msg_enter_preauto_money), null, R.layout.dialog_amtinput, new DialogUtils.CustomDialogCallback() {
////			@Override
////			public void onResult(int id, View dialogView) {
////				if(id == 0){//sure
////					Editable editable = ((EditText) dialogView.findViewById(R.id.edit_amt_input)).getText();
////					if (editable.toString().equals("") || editable.toString() == null) {
////						showMessage(context.getString(R.string.msg_preauth_money_null) + "\r\n", MessageTag.NORMAL);
////						controller.sendAmtInputResult(null);
////					} else {
////						DecimalFormat df = new DecimalFormat("#.00");
////						BigDecimal amt = new BigDecimal(editable.toString());
////						AppConfig.EMV.amt = amt;
////						showMessage(context.getString(R.string.msg_preauth_money) + df.format(amt) + "\r\n", MessageTag.NORMAL);
////						controller.sendAmtInputResult(amt);
////					}
////				}else if(id == -1){//cancel
////					showMessage(context.getString(R.string.msg_trans_cancel) + "\r\n", MessageTag.NORMAL);
////					controller.sendAmtInputResult(null); 	//When the amount of pre-authorization is empty ,it means to cancel the transaction.
////				}
////			}
////		});
//    }
//
//    @Override
//    public void onFinalAppSelect(EmvTransController emvTransController, EmvTransInfo context) throws Exception {
//        if ((isScreenAvailable != 1)) {
//            emvTransController.doEmvFinish(false);
//            return;
//        }
//        isAppSelect = true;
//        Logger.ve("Listner State --onFinalAppSelect");
//        Logger.v("Listner State --getKernelId--" + String.format("%02x", context.getKernelId()));
//        Logger.v("Listner State --getKernelId--" + String.format("%02x", context.getKernelId()));
//        doBeepOnce = true;
//        showCard = true;
//        isPinCancelled = false;
//        rf_thread = 0;
//        isTransactionNotAllowed = false;
//        isAmountExceed = false;
//        responseData55 = "";
//        responseData38 = "";
//        isPinRequires = 0;
//        SimpleTransferListener.isSAFDecilined = false;
////        emvModule.setEmvData(0x9F02, getHextBytes("000000000800"));//You can set emv kernel data in this step
//        emvModule.setEmvData(0xDF24, getHextBytes("F4C0F0E8AF8E62"));//You can set emv kernel data in this step
//        Logger.v("Listner State --onFinalAppSelect" + ByteConversionUtils.byteArrayToHexString(context.getAid(), context.getAid().length, false));
//        String aid = ByteConversionUtils.byteArrayToHexString(context.getAid(), context.getAid().length, false);
//        Logger.v("aid--" + aid);
//        isGccNet = false;
//        AppDatabase database = AppDatabase.getInstance(this.context.getApplicationContext());
//        TMSCardSchemeDao cardSchemeDao = database.getTMSCardSchemeDao();
//        TMSCardSchemeEntity cardData = cardSchemeDao.getCardSchemeData(Utils.fetchIndicatorFromAID(aid));
//        if (cardData != null) {
//            Logger.v("cardData --" + cardData.toString());
//            Logger.v("CV Validation --" + cardData.getCardHolderAuth());
//            Logger.v("getTransactionAllowed --" + cardData.getTransactionAllowed());
//            Logger.v("getManualEntryEnabled --" + cardData.getManualEntryEnabled());
//            if (!(Utils.checkTMSValidation(cardData.getTransactionAllowed(), transType))) {
//                isTransactionNotAllowed = true;
//                emvTransController.doEmvFinish(false);
//                return;
//            }
//        } else {
//            isTransactionNotAllowed = true;
//            emvTransController.doEmvFinish(false);
//            return;
//        }
//        String amtEnabled = AppManager.getInstance().getTransactionAmountEnabled(Utils.fetchIndicatorFromAID(aid));
//        if (Utils.checkTMSValidation(amtEnabled, transType)) {
//            String amt = AppManager.getInstance().getMaxAmount(Utils.fetchIndicatorFromAID(aid));
//            if (amt.trim().length() != 0) {
//                double ammt = Double.parseDouble(amt);
//                double amount = (AppConfig.EMV.amountValue);
//                Logger.v("Amount -- " + AppConfig.EMV.amountValue);
//                Logger.v("Amount -- " + amt);
//                Logger.v("Amount -- " + ammt);
//                Logger.v("Amount -- " + amount);
//                if (ammt < amount) {
//                    Logger.v("Amount exceed");
//                    isAmountExceed = true;
//                    emvTransController.doEmvFinish(false);
//                    return;
//                } else
//                    Logger.v("Amount else");
//            } else
//                Logger.v("amt else");
//        } else
//            Logger.v("amt else 1");
//
//        if(transType.equalsIgnoreCase(Constant.PURCHASE_NAQD)){
//            String amt = AppManager.getInstance().getMaxCashAmount(Utils.fetchIndicatorFromAID(aid));
//            if (amt.trim().length() != 0) {
//                double ammt = Double.parseDouble(amt);
//                double amount = (AppConfig.EMV.amtCashBack);
//                Logger.v("Amount -- " + AppConfig.EMV.amtCashBack);
//                Logger.v("Amount -- " + amt);
//                Logger.v("Amount -- " + ammt);
//                Logger.v("Amount -- " + amount);
//                if (ammt < amount) {
//                    Logger.v("Amount exceed");
//                    isAmountExceed = true;
//                    emvTransController.doEmvFinish(false);
//                    return;
//                } else
//                    Logger.v("Amount else");
//            } else
//                Logger.v("amt else");
//        }
//
//        isMada = ConstantValue.A0000002281010.equalsIgnoreCase(Utils.fetchIndicatorFromAID(aid));
//        Logger.v("IsMada --" + transType);
//        Logger.v("IsMada --" + isMada);
//        if (!(isMada) && (transType.equalsIgnoreCase(Constant.PURCHASE_NAQD) || transType.equalsIgnoreCase(Constant.PRE_AUTHORISATION_EXTENSION) || transType.equalsIgnoreCase(Constant.PRE_AUTHORISATION_VOID))) {
//            isCardInvalid = true;
//            showTransactionNotAllowedDialoge();
//            emvTransController.doEmvFinish(false);
//            return;
//        } else if (transType.equalsIgnoreCase(Constant.CASH_ADVANCE) && isMada) {
//            isCardInvalid = true;
//            showTransactionNotAllowedDialoge();
//            emvTransController.doEmvFinish(false);
//            return;
//        } else
//            isCardInvalid = false;
//        String versionNum = AppManager.getInstance().getTerminalAIDVersionNumber(aid);
//        RetailerDataModel retailerDataModel = AppManager.getInstance().getRetailerDataModel();
//        Logger.v("retailerDataModel--" + retailerDataModel.toString());
//        Logger.v("versionNum--" + versionNum);
//        Logger.v("versionNum--" + AppManager.getInstance().getMerchantCode(Utils.fetchIndicatorFromAID(aid)));
//        Logger.v("versionNum--" + Utils.getFloorLimit(aid));
//
//        if (transType.equalsIgnoreCase(Constant.REFUND) || transType.equalsIgnoreCase(Constant.PRE_AUTHORISATION) ||
//                transType.equalsIgnoreCase(Constant.PRE_AUTHORISATION_VOID) || transType.equalsIgnoreCase(Constant.PRE_AUTHORISATION_EXTENSION)
//                || transType.equalsIgnoreCase(Constant.PURCHASE_ADVICE) || transType.equalsIgnoreCase(Constant.PURCHASE_ADVICE_FULL)
//                || transType.equalsIgnoreCase(Constant.PURCHASE_ADVICE_PARTIAL) || transType.equalsIgnoreCase(Constant.PURCHASE_ADVICE_MANUAL)) {
//            if (!AppManager.getInstance().getRefundOfflineEnabled(Utils.fetchIndicatorFromAID(aid))) {
//                emvModule.setEmvData(0x9F1B, getHextBytes(String.format("%08d", 0)));
//            }
//        }
//
//        emvModule.setEmvData(0x9A, getHextBytes(ByteConversionUtils.formatTranDate("yyMMdd")));
//        emvModule.setEmvData(0x9C, getHextBytes((getCode(transType))));
//        emvModule.setEmvData(0xDF34, getHextBytes(("C400")));
////        emvModule.setEmvData(0x9C, getHextBytes(("0" + InnerProcessingCode.USING_STANDARD_PROCESSINGCODE)));
//        emvModule.setEmvData(0x9F21, getHextBytes(ByteConversionUtils.formatTranDate("hhmmss")));
//        emvModule.setEmvData(0x9F1A, getHextBytes("0" + retailerDataModel.getTerminalCountryCode()));
////        emvModule.setEmvData(0x9F2A, getHextBytes("0" + retailerDataModel.getTerminalCurrencyCode()));
//        emvModule.setEmvData(0x5F2A, getHextBytes("0" + retailerDataModel.getTerminalCurrencyCode()));
//        emvModule.setEmvData(0x9F33, getHextBytes(retailerDataModel.getTerminalCapability()));
//        emvModule.setEmvData(0x9F40, getHextBytes(retailerDataModel.getAdditionalTerminalCapabilities()));
//        emvModule.setEmvData(0x9F35, getHextBytes(retailerDataModel.geteMVTerminalType()));
//        emvModule.setEmvData(0x9F1C, getHextBytes(AppManager.getInstance().getCardAcceptorID41())); // Terminal ID
//        emvModule.setEmvData(0x9F16, getHextBytes(AppManager.getInstance().getCardAcceptorCode42()));
//        emvModule.setEmvData(0x9F15, getHextBytes(AppManager.getInstance().getMerchantCode(Utils.fetchIndicatorFromAID(aid))));
//
//        String amount = String.format("%012d", (int) (AppConfig.EMV.amountValue * 100));
//        String amount1 = String.format("%08d", (int) (AppConfig.EMV.amountValue * 100));
//        String cashBack = String.format("%012d", (int) (AppConfig.EMV.amtCashBack * 100));
//        Logger.v("ämount 1--" + amount);
//        Logger.v("ämount 1--" + amount1);
//        Logger.v("ämount 1--" + cashBack);
//        Logger.v("ämount 1--" + AppConfig.EMV.amtCashBack);
////        emvModule.setEmvData(0x9F02, getHextBytes((amount.length() %2 == 0)?amount:"0"+amount));
////        emvModule.setEmvData(0x9F03, getHextBytes((cashBack.length() %2 == 0)?amount:"0"+amount));
////        emvModule.setEmvData(0x9F02, getHextBytes(amount));
////        emvModule.setEmvData(0x81, getHextBytes(amount1));
////        emvModule.setEmvData(0x9F03, getHextBytes(cashBack));
//
//        String floorLimit = AppManager.getInstance().getFloorLimit(Utils.fetchIndicatorFromAID(aid));
//        boolean enabled = AppManager.getInstance().isFllorLimitEnabled(Utils.fetchIndicatorFromAID(aid));
//        Logger.v("ämount 1--" + floorLimit);
//        Logger.v("ämount 1--" + enabled);
//        if (transType.equalsIgnoreCase(Constant.PURCHASE_ADVICE_FULL) || transType.equalsIgnoreCase(Constant.PURCHASE_ADVICE_PARTIAL)) {
//            if (isMada) {
//                Logger.v("Offline Disabled");
//                emvModule.setEmvData(0x9F1B, getHextBytes(String.format("%08d", 0)));
//            } else
//                Logger.v("Offline Ele disabled");
//        }
////        else if (transType.equalsIgnoreCase(Constant.REFUND) || transType.equalsIgnoreCase(Constant.PRE_AUTHORISATION)
////                || transType.equalsIgnoreCase(Constant.PRE_AUTHORISATION_VOID) || transType.equalsIgnoreCase(Constant.PRE_AUTHORISATION_EXTENSION)) {
////            if (!AppManager.getInstance().getRefundOfflineEnabled(Utils.fetchIndicatorFromAID(aid))) {
////                emvModule.setEmvData(0x9F1B, getHextBytes(String.format("%08d", 0)));
////            }
////        }
//        Logger.v("Trans else");
//        Logger.v("Floor Limit --" + ISOUtils.hexString(emvModule.getEmvData(0x9F1B)));
//        Logger.v("Floor Limit --" + ISOUtils.hexString(emvModule.getEmvData(0x9F33)));
//        Logger.v("Floor Limit --" + ISOUtils.hexString(emvModule.getEmvData(0x9F40)));
//
//        emvModule.setEmvData(0x9F37, getHextBytes((CreatePacket.randomAlphaNumeric(8))));
//        emvModule.setEmvData(0x9F1E, getHextBytes(AppManager.getInstance().getVendorTerminalSerialNumber())); // Terminal Serial Num
//        emvModule.setEmvData(0x9F09, getHextBytes(versionNum));
//        Logger.v("Show card --" + Utils.fetchIndicatorFromAID(aid));
//        if (this.context instanceof PrintActivity) {
//            Logger.v("Show Card");
//            ((PrintActivity) this.context).showCard(Utils.fetchIndicatorFromAID(aid));
//        }
//        emvTransController.transferConfirm(true);
//    }
//
//    private void showTransactionNotAllowedDialoge() {
//        if (context instanceof PrintActivity) {
//            ((PrintActivity) context).showAlert(3);
//        }
//    }
//
//    private byte[] getHextBytes(String data) {
//        return ISOUtils.hex2byte(data);
//    }
//
//    private static Handler pinEventHandler = new Handler(Looper.getMainLooper()) {
//
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (isEMVCompleted && (msg.what != AppConfig.EMV.START_TIMER) && (msg.what != AppConfig.EMV.START_TIMER_REVERSE))
//                return;
//            switch (msg.what) {
//                case AppConfig.EMV.PIN_FINISH:
//                    Logger.v("Pin Finished");
//                    if (msg.obj != null) {
//                        byte[] pinEntry = (byte[]) msg.obj;
//                        if (pinEntry.length != 0)
//                            pinBlock = pinEntry;
//                        else
//                            pinBlock = null;
//                    }
//                    if (rf_thread == 1)
//                        waitPinInputThreat_rf.notifyThread();
//                    else if (rf_thread == 2)
//                        waitPinInputThreat_pin.notifyThread();
//                    else
//                        waitPinInputThreat.notifyThread();
//                    break;
//                case AppConfig.EMV.PIN_CANCEL:
//                    isCancelled = true;
//                    if (rf_thread == 1)
//                        waitPinInputThreat_rf.notifyThread();
//                    else if (rf_thread == 2)
//                        waitPinInputThreat_pin.notifyThread();
//                    else
//                        waitPinInputThreat.notifyThread();
//                    break;
//                case AppConfig.EMV.SOCKET_FINISH:
//                    String resObj = msg.obj.toString();
//                    Logger.v("ResObj --" + resObj);
//                    if (resObj.contains("__")) {
//                        String[] dataSplit = resObj.split("__");
//                        if (1 < dataSplit.length)
//                            responseData55 = dataSplit[1].trim();
//                        if (dataSplit.length != 0)
//                            responseData38 = dataSplit[0].trim();
//                    } else {
//                        responseData55 = resObj;
//                    }
//                    Logger.v("responseData55 --" + responseData55);
//                    isCancelled = false;
//                    waitSockettThreat.notifyThread();
//                    break;
//                case AppConfig.EMV.SOCKET_CANCEL:
//                    isCancelled = true;
//                    responseData55 = "";
//                    waitSockettThreat.notifyThread();
//                    break;
//                case AppConfig.EMV.SOCKET_UNABEL_ONLINE:
////                    isSAFDecilined = false; // disabling unable to go Online
//                    isCancelled = true;
//                    if (isSAFDecilined)
//                        responseData38 = "Z3";
//                    else
//                        responseData38 = "";
//                    waitSockettThreat.notifyThread();
//                    break;
//                case AppConfig.EMV.SELECT_APP:
//                    waitSelectApp.notifyThread();
//                    break;
//                case AppConfig.EMV.START_TIMER:
//                    connection.onConnect(DO_TIMER);
//                    break;
//                case AppConfig.EMV.START_TIMER_REVERSE:
//                    connection.onConnect(DO_TIMER_REVERSAL);
//                    break;
//                default:
//                    break;
//            }
//        }
//
//    };
//
//    public static Handler getPinEventHandler() {
//        return pinEventHandler;
//    }
//
//    public static void setPinEventHandler(Handler pinEventHandler) {
//        SimpleTransferListener.pinEventHandler = pinEventHandler;
//    }
//
//    private void showMessage(String msg, int messageType, int i) {
//        Logger.v("Poss---------" + i);
//        Logger.v("TAG -" + messageType + " -- Mess-" + msg);
////		((MainActivity) context).showMessage(msg, messageType);
//    }
//
//    public interface MakeConnection {
//        public void onConnect(int i);
//    }
//
//    public static String getErrorMessgae(Context context, int errorCode) {
//        String resultMsg;
//        switch (errorCode) {
//            case -6:
//                resultMsg = context.getString(R.string.msg_trans_error_no_supported_app);
//                break;
//            case -11:
//                resultMsg = context.getString(R.string.msg_trans_error_offline_data_auth_failed);
//                break;
//            case -13:
//                resultMsg = context.getString(R.string.msg_trans_error_holder_auth_failed);
//                break;
//            case -18:
//                resultMsg = context.getString(R.string.msg_trans_error_card_locked);
//                break;
//            case -1531:
//            case -2116:
//                resultMsg = context.getString(R.string.msg_trans_error_card_expired);
//                break;
//            case -1532:
//            case -2115:
//                resultMsg = context.getString(R.string.msg_trans_error_card_not_effected);
//                break;
//            case -1822:
//                resultMsg = context.getString(R.string.msg_trans_error_ecash_nsf);
//                break;
//            case -1903:
//                resultMsg = context.getString(R.string.msg_error_ec_load_exceed_limit);
//                break;
//            case -1904:
//            case -1905:
//                resultMsg = context.getString(R.string.msg_trans_error_script_execut_error);
//                break;
//            case -1901:
//                resultMsg = context.getString(R.string.msg_trans_error_script_transfinite);
//                break;
//            case -2105:
//                resultMsg = context.getString(R.string.msg_trans_error_pre_money_limit);
//                break;
//            case -2120:
//            case -1441:
//                resultMsg = context.getString(R.string.msg_trans_error_pure_ecash_cant_online);
//                break;
//            case -2121:
//                resultMsg = context.getString(R.string.msg_trans_error_card_reject);
//                break;
//            default:
//                resultMsg = null;
//                break;
//        }
//        return resultMsg;
//    }
//
//    public void showPinOkay() {
//        if (showPinOkay) {
//            ((PrintActivity) context).printOkay();
//            showPinOkay = false;
//        }
//    }
//
//    public void doBeepOnce() {
//        if (doBeepOnce) {
//            if (AppConfig.EMV.consumeType == 2) {
//                connection.onConnect(DO_BEEP_ONCE);
//            }
//            doBeepOnce = false;
//        }
//    }
//
//    private String getCode(String transactionType) {
//        String code = IsoRequest.getInstance(context).getProcessCode(transactionType).substring(0, 2);
//        Logger.v("Code --" + code);
//        return code;
//    }
//}
