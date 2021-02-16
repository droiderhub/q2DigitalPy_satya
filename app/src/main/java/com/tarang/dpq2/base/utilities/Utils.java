package com.tarang.dpq2.base.utilities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfRenderer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.cloudpos.DeviceException;
import com.cloudpos.printer.PrinterDevice;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.tarang.dpq2.R;
import com.tarang.dpq2.base.AppInit;
import com.tarang.dpq2.base.AppManager;
import com.tarang.dpq2.base.EReceiptGenerator;
import com.tarang.dpq2.base.Logger;
import com.tarang.dpq2.base.MapperFlow;
import com.tarang.dpq2.base.baseactivities.BaseActivity;
import com.tarang.dpq2.base.jpos_class.ConstantApp;
import com.tarang.dpq2.base.jpos_class.ConstantAppValue;
import com.tarang.dpq2.base.terminal_sdk.AppConfig;
import com.tarang.dpq2.base.terminal_sdk.device.SDKDevice;
import com.tarang.dpq2.base.terminal_sdk.event.SimpleTransferListener;
import com.tarang.dpq2.base.terminal_sdk.utils.CheckConnection;
import com.tarang.dpq2.base.terminal_sdk.utils.CheckConnectionInterface;
import com.tarang.dpq2.base.room_database.db.tuple.BinRangeTuple;
import com.tarang.dpq2.base.utilities.multilangutils.LocalizationActivityDelegate;
import com.tarang.dpq2.model.DeviceSpecificModel;
import com.tarang.dpq2.model.KeyValueModel;
import com.tarang.dpq2.model.MenuModel;
import com.tarang.dpq2.view.activities.SplashActivity;
import com.tarang.dpq2.viewmodel.TransactionViewModel;
import com.tarang.dpq2.worker.LoadKeyWorker;
import com.tarang.dpq2.worker.SAFWorker;
import com.tarang.dpq2.worker.SocketConnectionWorker;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import static com.cloudpos.jniinterface.EMVJNIInterface.query_contact_card_presence;
import static com.tarang.dpq2.base.terminal_sdk.AppConfig.EMV.reqObj;

public class Utils {
    public static Dialog alertDialog;
    private static ProgressDialog m_Dialog;
    private static Dialog alertDialog_new;

    public Utils() {
    }
    public static String getAndroidVersion() {
        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        return "Android SDK: " + sdkVersion + " (" + release +")";
    }

    public static boolean reconsilationTag(String modeTransaction) {
        Logger.v("modeTransaction -" + modeTransaction);
        return modeTransaction.equalsIgnoreCase(ConstantApp.PURCHASE) ||
                modeTransaction.equalsIgnoreCase(ConstantApp.PURCHASE_NAQD) ||
                modeTransaction.equalsIgnoreCase(ConstantApp.REFUND) ||
                modeTransaction.equalsIgnoreCase(ConstantApp.CASH_ADVANCE) ||
                modeTransaction.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_FULL) ||
                modeTransaction.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_PARTIAL) ||
                modeTransaction.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_MANUAL);
    }

    public static boolean reconsilationAuthTag(String modeTransaction) {
        Logger.v("modeTransaction -" + modeTransaction);
        return modeTransaction.equalsIgnoreCase(ConstantApp.AUTHORIZATION_ADVICE) ||
                modeTransaction.equalsIgnoreCase(ConstantApp.FINANCIAL_ADVISE);
    }
    public static String removePaddedF(String cardNo) {
        try {
            String myCard = cardNo.trim();
            Logger.v("Card No -"+myCard);
            if (myCard.substring(myCard.length() - 1).equalsIgnoreCase("f")) {
                return removePaddedF(myCard.substring(0, myCard.length() - 1));
            } else {
                return myCard;
            }
        }catch (Exception e){
            return cardNo;
        }
    }

    public static boolean check38Tag(String modeTransaction) {
        Logger.v("modeTransaction -" + modeTransaction);
        return modeTransaction.equalsIgnoreCase(ConstantApp.REFUND) ||
                modeTransaction.equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION) ||
                modeTransaction.equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION_EXTENSION) ||
                modeTransaction.equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION_VOID) ||
                modeTransaction.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_FULL) ||
                modeTransaction.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_PARTIAL) ||
                modeTransaction.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_MANUAL);
    }

    public static boolean checkNewAuth(String transactionType) {
        if (AppInit.VERSION_6_0_5)
            return false;
        return transactionType.equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION)
                || transactionType.equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION_VOID)
                || transactionType.equalsIgnoreCase(ConstantApp.PRE_AUTHORISATION_EXTENSION)
                || transactionType.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_FULL)
                || transactionType.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_PARTIAL)
                || transactionType.equalsIgnoreCase(ConstantApp.PURCHASE_ADVICE_MANUAL);
    }


    public static Bitmap getQrCode(String qrData,Context context) {
        QRGEncoder qrgEncoder;
        Bitmap bitmap = null;
        if (qrData.length() > 0) {
            WindowManager manager = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int width = point.x;
            int height = point.y;
            int smallerDimension = width < height ? width : height;
            smallerDimension = smallerDimension * 3 / 10;

            qrgEncoder = new QRGEncoder(
                    qrData, null,
                    QRGContents.Type.TEXT,
                    smallerDimension);
            try {
                bitmap = qrgEncoder.encodeAsBitmap();
                return bitmap;
//                qr_img.setImageBitmap(bitmap);
            } catch (WriterException e) {
                return bitmap;
            }
        }
        return bitmap;
    }

    public interface DialogeClick {
        public void onClick();
    }


    public static void alertDialogShow(final Context context,  final List<String> nameList) {
        dismissDialoge();
        if (((BaseActivity) context).isFinishing())
            return;

        final Dialog alertDialog1 = new Dialog(context);
        alertDialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (alertDialog1.getWindow() != null)
        alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog1.setContentView(R.layout.list_select);

        LinearLayout ll_holder = alertDialog1.findViewById(R.id.ll_holder);
        alertDialog1.setTitle(context.getString(R.string.select_application));
        alertDialog1.setCancelable(false);
        for (int i = 0; i < nameList.size(); i++) {
            Button button = new Button(context);
            button.setText(nameList.get(i));
            final int ii = i;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Logger.v("setOnClickListener-" + ii);
                    alertDialog1.cancel();
                    alertDialog1.dismiss();
                    SimpleTransferListener.selectApplication = ii;
                    SimpleTransferListener.selectApplicationName = nameList.get(ii);
                    Message pinFinishMsg = new Message();
                    pinFinishMsg.what = AppConfig.EMV.SELECT_APP;
                    pinFinishMsg.obj = null;
                    SimpleTransferListener.getPinEventHandler().sendMessage(pinFinishMsg);
                }
            });
            ll_holder.addView(button);
        }
        Button button = new Button(context);
        button.setText(context.getString(R.string.cancel));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.v("setOnClickListener 2");
                alertDialog1.cancel();
                alertDialog1.dismiss();
                SimpleTransferListener.selectApplication = -1;
                Message pinFinishMsg = new Message();
                pinFinishMsg.what = AppConfig.EMV.SELECT_APP;
                pinFinishMsg.obj = null;
                SimpleTransferListener.getPinEventHandler().sendMessage(pinFinishMsg);
            }
        });
        ll_holder.addView(button);

        alertDialog1.setCanceledOnTouchOutside(false);

        Window window = alertDialog1.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        //wlp.gravity = Gravity.BOTTOM;
        wlp.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;
        wlp.y = 140;
//        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        alertDialog1.show();
    }

    public static void alertDialogShow(final Context context, String message) {
        Logger.v("Message --" + message);
        dismissDialoge();
        if (((BaseActivity) context).isFinishing())
            return;
        if (alertDialog == null) {
            alertDialog = new Dialog(context);
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (alertDialog.getWindow() != null)
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.setContentView(R.layout.custom_dialog_transparent);
        }

        LinearLayout button_layout = alertDialog.findViewById(R.id.button_layout);
        button_layout.setVisibility(View.GONE);
        TextView text2 = (TextView) alertDialog.findViewById(R.id.text_dialog2);
        text2.setVisibility(View.GONE);
        TextView text = (TextView) alertDialog.findViewById(R.id.text_dialog);
        text.setText(message);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        //wlp.gravity = Gravity.BOTTOM;
        wlp.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;
        wlp.y = 140;
//        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        if (alertDialog != null) {
            if (!((Activity) context).isFinishing())
                shoeDialoge();
            Logger.v("Alert Shown");
        }
    }
    public static void alertDialogShowBottom(final Context context, String message) {
        Logger.v("Message --" + message);
        dismissDialoge();
        if (((BaseActivity) context).isFinishing())
            return;
        if (alertDialog == null) {
            alertDialog = new Dialog(context);
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (alertDialog.getWindow() != null)
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.setContentView(R.layout.custom_dialog_transparent);
        }



        LinearLayout button_layout = alertDialog.findViewById(R.id.button_layout);
        button_layout.setVisibility(View.GONE);
        TextView text2 = (TextView) alertDialog.findViewById(R.id.text_dialog2);
        text2.setVisibility(View.GONE);
        TextView text = (TextView) alertDialog.findViewById(R.id.text_dialog);
        text.setText(message);
        alertDialog.setCanceledOnTouchOutside(false);

        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        //wlp.gravity = Gravity.BOTTOM;
        wlp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        wlp.y = 140;
//        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        if (alertDialog != null) {
            if (!((Activity) context).isFinishing())
                shoeDialoge();
            Logger.v("Alert Shown");
        }
    }
    public static void alertDialogShow(final Context context, String message, String msg2) {
        Logger.v("Message --" + message);
        dismissDialoge();
        if (((BaseActivity) context).isFinishing())
            return;
        if (alertDialog == null) {
            alertDialog = new Dialog(context);
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (alertDialog.getWindow() != null)
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.setContentView(R.layout.custom_dialog_transparent);
        }

        LinearLayout button_layout = alertDialog.findViewById(R.id.button_layout);
        button_layout.setVisibility(View.GONE);
        TextView text = (TextView) alertDialog.findViewById(R.id.text_dialog);
        text.setText(message);
        TextView text2 = (TextView) alertDialog.findViewById(R.id.text_dialog2);
        text2.setVisibility(View.VISIBLE);
        text2.setText(msg2);
        alertDialog.setCanceledOnTouchOutside(false);

        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        //wlp.gravity = Gravity.BOTTOM;
        wlp.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;
        wlp.y = 140;
//        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        if (alertDialog != null) {
            if (!((Activity) context).isFinishing())
                shoeDialoge();
            Logger.v("Alert Shown");
        }
    }
    public static void alertDialogShowStatus(final Context context, String message,String message1, boolean stat) {
        Logger.v("Message --" + message);
        dismissDialoge();
        if (((BaseActivity) context).isFinishing())
            return;
        alertDialog_new = new Dialog(context);
        alertDialog_new.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (alertDialog_new.getWindow() != null)
            alertDialog_new.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog_new.setContentView(R.layout.custom_dialog_transparent_1);

        TextView text = (TextView) alertDialog_new.findViewById(R.id.text_dialog);
        text.setText(message);
        TextView text2 = (TextView) alertDialog_new.findViewById(R.id.text_dialog2);
        text2.setVisibility(View.VISIBLE);
        text2.setText(message1);
        ImageView status = (ImageView) alertDialog_new.findViewById(R.id.img_status_);
        if (stat)
            status.setImageDrawable(context.getResources().getDrawable(R.drawable.status_tick));
        else
            status.setImageDrawable(context.getResources().getDrawable(R.drawable.status_wrong));
        alertDialog_new.setCanceledOnTouchOutside(false);

        Window window = alertDialog_new.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        //wlp.gravity = Gravity.BOTTOM;
        wlp.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;
        wlp.y = 140;
//        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        if (alertDialog_new != null) {
            if (!((Activity) context).isFinishing())
                alertDialog_new.show();
            Logger.v("Alert Shown");
        }
    }

    public static void alertDialogOneShow(final Context context, String message) {
        dismissDialoge();
        Logger.v("Message --" + message);
        if (alertDialog == null) {
            alertDialog = new Dialog(context);
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (alertDialog.getWindow() != null)
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.setContentView(R.layout.custom_dialog_transparent);
        }

        LinearLayout button_layout = alertDialog.findViewById(R.id.button_layout);
        button_layout.setVisibility(View.GONE);
        TextView text2 = (TextView) alertDialog.findViewById(R.id.text_dialog2);
        text2.setVisibility(View.GONE);
        TextView text = (TextView) alertDialog.findViewById(R.id.text_dialog);
        ImageView loading = (ImageView) alertDialog.findViewById(R.id.loading);
        text.setText(message);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        loading.setVisibility(View.VISIBLE);
        Window window = alertDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams wlp = window.getAttributes();
            // wlp.gravity = Gravity.BOTTOM;
            wlp.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;
            wlp.y = 140;
            //wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(wlp);
        }
        if (alertDialog != null)
//            if (!((Activity) context).isFinishing())
            shoeDialoge();
    }
    public static void alertDialogOneShowBottom(final Context context, String message) {
        Logger.v("Message --" + message);
        dismissDialoge();
        if (alertDialog == null) {
            alertDialog = new Dialog(context);
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (alertDialog.getWindow() != null)
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.setContentView(R.layout.custom_dialog_transparent);
        }

        LinearLayout button_layout = alertDialog.findViewById(R.id.button_layout);
        button_layout.setVisibility(View.GONE);
        TextView text2 = (TextView) alertDialog.findViewById(R.id.text_dialog2);
        text2.setVisibility(View.GONE);
        TextView text = (TextView) alertDialog.findViewById(R.id.text_dialog);
        ImageView loading = (ImageView) alertDialog.findViewById(R.id.loading);
        text.setText(message);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        loading.setVisibility(View.VISIBLE);
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;
        wlp.y = 140;
//        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        if (alertDialog != null)
//            if (!((Activity) context).isFinishing())
            shoeDialoge();
    }

    public static void alertDialogShow(String url, final Context context, View.OnClickListener oklistener, View.OnClickListener cancellistener, View.OnClickListener smsListner, View.OnClickListener emailListner) {
        File file = new EReceiptGenerator(context).downloadReceipt(TransactionViewModel.printerModel);
        String text = url;
        Bitmap barcode = null;
        Bitmap pdf = null;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            if(text.trim().length() != 0) {
                BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                barcode = barcodeEncoder.createBitmap(bitMatrix);
            }

            Logger.v("URLL --" + url);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ParcelFileDescriptor fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
                PdfRenderer pdfRenderer = null;
                pdfRenderer = new PdfRenderer(fileDescriptor);
                Logger.v("Page count -" + pdfRenderer.getPageCount());
                PdfRenderer.Page page = pdfRenderer.openPage(0);
                pdf = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
                page.render(pdf, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                page.close();
            }
        } catch (Exception e) {
            Logger.v("PDF Exception -" + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        dismissDialoge();
        if (((BaseActivity) context).isFinishing())
            return;
        alertDialog = new Dialog(context);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (alertDialog.getWindow() != null)
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.setContentView(R.layout.custom_dialog_transparent_2);
        alertDialog.setCanceledOnTouchOutside(false);
        Button ok = alertDialog.findViewById(R.id.btnn_print);
        Button btnn_sms = alertDialog.findViewById(R.id.btnn_sms);
        Button btnn_email = alertDialog.findViewById(R.id.btnn_email);
        ImageView cancel = alertDialog.findViewById(R.id.btnn_cancel);
        ImageView pdfView = alertDialog.findViewById(R.id.pdfView);
        LinearLayout smsAndMailLayout = alertDialog.findViewById(R.id.below_ll);
        if (AppManager.getInstance().isMerchantPoratalEnable()) {
            smsAndMailLayout.setVisibility(View.VISIBLE);
        } else {
            smsAndMailLayout.setVisibility(View.GONE);
        }
        alertDialog.setCancelable(false);
        ok.setOnClickListener(oklistener);
        cancel.setOnClickListener(cancellistener);
        btnn_sms.setOnClickListener(smsListner);
        btnn_email.setOnClickListener(emailListner);

        ImageView img_barcode = alertDialog.findViewById(R.id.img_barcode);
        Logger.v("PDF done");
        if(text.trim().length() != 0) {
            img_barcode.setImageBitmap(barcode);
        }else {
            img_barcode.setVisibility(View.GONE);
        }
        pdfView.setImageBitmap(pdf);

        if (alertDialog != null)
            if (!((Activity) context).isFinishing())
                shoeDialoge();
    }


    public static Dialog alertDialogShowCard(final Context context, String message) {
        dismissDialoge();
        Logger.v("msg--" + message);
        Dialog alertDialog1 = new Dialog(context);
        alertDialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (alertDialog1.getWindow() != null)
        alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog1.setContentView(R.layout.custom_dialog_transparent);
        LinearLayout button_layout = alertDialog1.findViewById(R.id.button_layout);
        button_layout.setVisibility(View.GONE);
        TextView text = (TextView) alertDialog1.findViewById(R.id.text_dialog);
        text.setText(message);
        alertDialog1.setCanceledOnTouchOutside(false);
        alertDialog1.findViewById(R.id.loading).setVisibility(View.GONE);
        Window window = alertDialog1.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        //wlp.gravity = Gravity.BOTTOM;
        wlp.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;
        wlp.y = 100;
//        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        Logger.v("show Message --" + message);
        alertDialog1.show();
        return alertDialog1;
    }

    public static void alertDialogShow(final Context context, String message, final DialogeClick click) {
        Logger.v("Message --" + message);
        dismissDialoge();
        if (((BaseActivity) context).isFinishing())
            return;
        if (alertDialog == null) {
            alertDialog = new Dialog(context);
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (alertDialog.getWindow() != null)
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.setContentView(R.layout.custom_dialog_transparent);
        }
        TextView text = (TextView) alertDialog.findViewById(R.id.text_dialog);
        text.setText(message);
        Button ok = alertDialog.findViewById(R.id.ok);
        Button cancel = alertDialog.findViewById(R.id.cancel);
        ImageView loading = alertDialog.findViewById(R.id.loading);
        loading.setVisibility(View.GONE);
        TextView text2 = (TextView) alertDialog.findViewById(R.id.text_dialog2);
        text2.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                click.onClick();
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);

        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;
        wlp.y = 140;
//        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        if (alertDialog != null) {
            if (!((Activity) context).isFinishing())
                shoeDialoge();
            else
                Logger.v("Show Dialoge else");
        } else
            Logger.v("Dialoge null");
    }

    public static void alertDialogShow(final Context context, String message, View.OnClickListener listener) {
        dismissDialoge();
        if (((BaseActivity) context).isFinishing())
            return;
        if (alertDialog == null) {
            alertDialog = new Dialog(context);
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (alertDialog.getWindow() != null)
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.setContentView(R.layout.custom_dialog_transparent);
        }
        TextView text = (TextView) alertDialog.findViewById(R.id.text_dialog);
        text.setText(message);
        //alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        Button ok = alertDialog.findViewById(R.id.ok);
        Button cancel = alertDialog.findViewById(R.id.cancel);
        ImageView loading = alertDialog.findViewById(R.id.loading);
        loading.setVisibility(View.GONE);
        TextView text2 = (TextView) alertDialog.findViewById(R.id.text_dialog2);
        text2.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);
        ok.setOnClickListener(listener);
        alertDialog.setCancelable(false);
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;
        wlp.y = 140;
//        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        if (alertDialog != null) {
            if (!((Activity) context).isFinishing())
                shoeDialoge();
        }
    }

    public static void alertPriorityDialoge(final Context context) {
        dismissDialoge();
        if (((Activity) context).isFinishing())
            return;
        if (alertDialog == null) {
            alertDialog = new Dialog(context);
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (alertDialog.getWindow() != null)
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.setContentView(R.layout.dialoge_priority);
        }
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        boolean isGPRS = AppManager.getInstance().getConnectionPriority();
        ((TextView) alertDialog.findViewById(R.id.txt_primary)).setText((isGPRS)?context.getString(R.string.gprs_primary) : context.getString(R.string.wifi_primary));
        ((TextView) alertDialog.findViewById(R.id.txt_secondary)).setText((!isGPRS) ? context.getString(R.string.wifi_secondary) : context.getString(R.string.gprs_secondary));
        alertDialog.findViewById(R.id.connection_cancel1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialoge();
            }
        });
        alertDialog.findViewById(R.id.connection_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialoge();
            }
        });
        alertDialog.findViewById(R.id.connection_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.findViewById(R.id.ll_edit).setVisibility(View.VISIBLE);
                alertDialog.findViewById(R.id.ll_view).setVisibility(View.GONE);
            }
        });
        alertDialog.findViewById(R.id.connection_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean rbt_wifi = ((RadioButton) alertDialog.findViewById(R.id.radioWifi)).isChecked();
                AppManager.getInstance().setPrimaryConnection(!rbt_wifi);
                dismissDialoge();
            }
        });
        if (AppManager.getInstance().getConnectionPriority())
            ((RadioButton) alertDialog.findViewById(R.id.radioGPRS)).setChecked(true);
        else
            ((RadioButton) alertDialog.findViewById(R.id.radioWifi)).setChecked(true);
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;
        wlp.y = 100;
//        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        if (alertDialog != null)
            if (!((Activity) context).isFinishing())
                shoeDialoge();
    }

    public static void alertDialogShow(final Context context, String message, View.OnClickListener oklistener, View.OnClickListener cancellistener) {
        dismissDialoge();
        if (((BaseActivity) context).isFinishing())
            return;
        if (alertDialog == null) {
            alertDialog = new Dialog(context);
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (alertDialog.getWindow() != null)
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.setContentView(R.layout.custom_dialog_transparent);
        }
        TextView text = (TextView) alertDialog.findViewById(R.id.text_dialog);
        text.setText(message);
        //alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        Button ok = alertDialog.findViewById(R.id.ok);
        Button cancel = alertDialog.findViewById(R.id.cancel);
        ImageView loading = alertDialog.findViewById(R.id.loading);
        loading.setVisibility(View.GONE);
        TextView text2 = (TextView) alertDialog.findViewById(R.id.text_dialog2);
        text2.setVisibility(View.GONE);
        alertDialog.setCancelable(false);
        ok.setOnClickListener(oklistener);

        if (cancellistener != null) {
            ok.setOnClickListener(oklistener);
            cancel.setOnClickListener(cancellistener);
        } else {
            ok.setVisibility(View.GONE);
            cancel.setOnClickListener(cancellistener);
        }

        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;
        wlp.y = 140;
//        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        if (alertDialog != null)
            if (!((Activity) context).isFinishing())
                shoeDialoge();
    }

    public static void alertYesDialogShow(final Context context, String message, View.OnClickListener yeslistener, View.OnClickListener nolistener) {
        dismissDialoge();
        if (((BaseActivity) context).isFinishing())
            return;
        if (alertDialog == null) {
            alertDialog = new Dialog(context);
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (alertDialog.getWindow() != null)
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.setContentView(R.layout.custom_dialog_transparent);
        }
        TextView text = (TextView) alertDialog.findViewById(R.id.text_dialog);
        text.setText(message);
        //alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        Button ok = alertDialog.findViewById(R.id.ok);
        Button cancel = alertDialog.findViewById(R.id.cancel);
        ok.setText(context.getString(R.string.common_yes));
        cancel.setText(context.getString(R.string.common_no));
        ImageView loading = alertDialog.findViewById(R.id.loading);
        loading.setVisibility(View.GONE);
        TextView text2 = (TextView) alertDialog.findViewById(R.id.text_dialog2);
        text2.setVisibility(View.GONE);
        ok.setOnClickListener(yeslistener);
        cancel.setOnClickListener(nolistener);
        alertDialog.setCancelable(false);
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;
        wlp.y = 140;
//        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        if (alertDialog != null)
            if (!((Activity) context).isFinishing())
                shoeDialoge();
    }

    public static Dialog alertYesDialogShow1(final Context context, String message, View.OnClickListener yeslistener, View.OnClickListener nolistener) {
        Dialog alertDialog1 = new Dialog(context);
        alertDialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (alertDialog1.getWindow() != null)
        alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog1.setContentView(R.layout.custom_dialog_transparent);

        TextView text = (TextView) alertDialog1.findViewById(R.id.text_dialog);
        text.setText(message);
        //alertDialog.setCancelable(false);
        alertDialog1.setCanceledOnTouchOutside(false);
        Button ok = alertDialog1.findViewById(R.id.ok);
        Button cancel = alertDialog1.findViewById(R.id.cancel);
        ok.setText(context.getString(R.string.common_yes));
        cancel.setText(context.getString(R.string.common_no));
        ImageView loading = alertDialog1.findViewById(R.id.loading);
        loading.setVisibility(View.GONE);
        ok.setOnClickListener(yeslistener);
        cancel.setOnClickListener(nolistener);
        alertDialog1.setCancelable(false);
        Window window = alertDialog1.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;
        wlp.y = 140;
//        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        alertDialog1.show();
        return alertDialog1;
    }

//    public static void showErrorMsg(final Context context) {
//        dismissDialoge();
//        alertDialog = new AlertDialog.Builder(context).create();
//        alertDialog.setMessage("UnSuccessful");
//        alertDialog.setCanceledOnTouchOutside(false);
//        //alertDialog.setCancelable(false);
//        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dismissDialoge();
//                ((BaseActivity) context).finish();
//            }
//        });
//        shoeDialoge();
//    }

    public static void showListnerMsg(final Context context) {
        int iccard = AppConfig.EMV.consumeType;
        dismissDialoge();
        if (((BaseActivity) context).isFinishing())
            return;
        if (alertDialog == null) {
            alertDialog = new Dialog(context);
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (alertDialog.getWindow() != null)
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.setContentView(R.layout.custom_dialog_transparent);
        }
        TextView text = (TextView) alertDialog.findViewById(R.id.text_dialog);
        if (iccard == 1) {
            text.setText(context.getString(R.string.insert_not_supported));
        } else if (iccard == 0) {
            text.setText(context.getString(R.string.swipe_not_supported));
        } else if (iccard == 2) {
            text.setText(context.getString(R.string.wave_not_supported));
        }
        Button ok = alertDialog.findViewById(R.id.ok);
        Button cancel = alertDialog.findViewById(R.id.cancel);
        ImageView loading = alertDialog.findViewById(R.id.loading);
        loading.setVisibility(View.GONE);
        TextView text2 = (TextView) alertDialog.findViewById(R.id.text_dialog2);
        text2.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);
        alertDialog.setCancelable(false);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialoge();
                MapperFlow.getInstance().moveToLandingPage(context, true, 19);
            }
        });

        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;
        wlp.y = 140;
//        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        if (!((Activity) context).isFinishing())
            shoeDialoge();
    }

    public static void tmsAlertDialogShow(final Context context, String message) {
        int pack = Integer.parseInt(message.substring(3, 5));
        int total = Integer.parseInt(message.substring(5, 7));
        if (alertDialog == null) {
            alertDialog = new Dialog(context);
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (alertDialog.getWindow() != null)
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.setContentView(R.layout.custom_dialog_transparent);
        }
        TextView text = (TextView) alertDialog.findViewById(R.id.text_dialog);

        if (message.substring(0, 3).equalsIgnoreCase("306"))
            alertDialog.setTitle(context.getString(R.string.full_download));
        else
            alertDialog.setTitle(context.getString(R.string.partial_download));
        text.setText(String.format(context.getString(R.string.downloading) + " %02d / %02d", pack, total));
        alertDialog.setCanceledOnTouchOutside(false);
        Button ok = alertDialog.findViewById(R.id.ok);
        Button cancel = alertDialog.findViewById(R.id.cancel);
        ok.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);
        Logger.v("Pack -" + pack + "--" + total);

        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;
        wlp.y = 140;
//        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        if (!alertDialog.isShowing())
            if (!((Activity) context).isFinishing())
                shoeDialoge();
    }

    public void resetDialoge() {
        alertDialog = null;
    }

    public static void dismissDialoge() {
        if (SimpleTransferListener.isScreenAvailable == 2)
            return;
        Logger.v("Dismiss Dialoge");
        hideDialoge();
        hideProgressDialoge();
    }

    public static void setNullDialoge() {
        dismissDialoge();
        alertDialog = null;
        m_Dialog = null;
    }

    public static void clearData() {
        reqObj = null;
        AppConfig.EMV.reqObjArray = null;
//        AppConfig.EMV.amountValue = 0;
        AppConfig.EMV.icCardNum = null;
//        AppConfig.EMV.emvTransInfo = null;
        AppConfig.EMV.icCardSerialNum = null;
        AppConfig.EMV.icCardTrack2data = null;
        AppConfig.EMV.icExpiredDate = null;
        AppConfig.EMV.ic55Data = null;
        AppConfig.EMV.pinBlock = null;
        SAFWorker.isReversal = false;
        SAFWorker.isSAFRepeat = false;
        SAFWorker.safTimerInitaited = false;
        SimpleTransferListener.selectApplicationName = "";
        AppConfig.EMV.icKernalId = "";
        SocketConnectionWorker.TRANSACTION_START_TIME = "";
        AppManager.getInstance().setFinancialAdviceRequired(false);
        AppManager.getInstance().setAuthorisationAdviceRequired(false);
        AppConfig.EMV.consumeType = -1;
    }

    public static void hideDialoge() {
        try {
            if (alertDialog != null) {
                alertDialog.dismiss();
                alertDialog.cancel();
                alertDialog = null;
            }
            if (alertDialog_new != null) {
                alertDialog_new.dismiss();
                alertDialog_new.cancel();
                alertDialog_new = null;
            }
        } catch (IllegalArgumentException e) {
            Logger.v("Illegal Exception");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static void hideDialoge1() {
        try {
            if (alertDialog != null) {
                alertDialog.dismiss();
                alertDialog.cancel();
                alertDialog = null;
            }
            if (alertDialog_new != null) {
                alertDialog_new.dismiss();
                alertDialog_new.cancel();
                alertDialog_new = null;
            }
        } catch (IllegalArgumentException e) {
            Logger.v("Illegal Exception");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static void hideProgressDialoge() {
        if (m_Dialog != null) {
            m_Dialog.dismiss();
            m_Dialog.cancel();
            m_Dialog = null;
        }
    }

    public static void showProgressDialog(Context context, String message) {
        hideDialoge();
        m_Dialog = new ProgressDialog(context, R.style.custom_progress_dialog);
        m_Dialog.setMessage(message);
        m_Dialog.setCancelable(false);
        m_Dialog.setCanceledOnTouchOutside(false);
        m_Dialog.show();
    }

    public static Dialog customDialogShow(Context context, String message) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_dailog);

        TextView textView = (TextView) dialog.findViewById(R.id.txt_dia);
        textView.setText(message);

        return dialog;
    }

    public static boolean getDateDifference(String endTimeConnection) {
        Logger.v("endTimeConnection" + endTimeConnection);
        Long diff = new Long(0);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            Date date = formatter.parse(endTimeConnection);
            diff = new Date().getTime() - date.getTime();
            Logger.v("diff --" + diff);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (diff < 100000);
    }

    private boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public static boolean isInternetAvailable(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

        if (activeNetworkInfo != null) { // connected to the internet
            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                Logger.v("TYPE_WIFI");
                return true;
            } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                Logger.v("TYPE_MOBILE");
                return true;
            }
        }
        ((BaseActivity) context).showToast(context.getString(R.string.please_check_internet_connection));
        Logger.v("Internet False");
        return false;
    }

    public static String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return formatter.format(date);
    }

    public static String addAccountNumber(String primaryAccNo2) {
        Logger.v("primaryAccNo2 -" + primaryAccNo2);
        String number = "";
        if (primaryAccNo2 != null) {
            number = primaryAccNo2.substring(0, 6);
            for (int i = 0; i < (primaryAccNo2.length() - 10); i++)
                number = number + "*";
            number = number + primaryAccNo2.substring(primaryAccNo2.length() - 4);
        }
        //every after 4 digit give space
        String val = "4";   // use 4 here to insert spaces every 4 characters
        String result = number.replaceAll("(.{" + val + "})", "$1 ").trim();
        System.out.println(result);

        Logger.v("primaryAccNo2 -" + number);
        return result;
    }

    public static String getTransactionNameEnglish(Context context, String tag) {
        switch (tag) {
            case ConstantApp.PURCHASE:
                return context.getString(R.string.purhase_en);
            case ConstantApp.PURCHASE_NAQD:
                return context.getString(R.string.purchase_with_naqd_en);
            case ConstantApp.PURCHASE_REVERSAL:
                return context.getString(R.string.reversal_en);
            case ConstantApp.REFUND:
                return context.getString(R.string.refund_en);
            case ConstantApp.CASH_ADVANCE:
                return context.getString(R.string.cash_advice_en);
            case ConstantApp.PRE_AUTHORISATION:
                return context.getString(R.string.preauth_en);
            case ConstantApp.PRE_AUTHORISATION_VOID:
                return context.getString(R.string.preauth_void_en);
            case ConstantApp.PRE_AUTHORISATION_EXTENSION:
                return context.getString(R.string.preauth_extension_en);
            case ConstantApp.PURCHASE_ADVICE_MANUAL:
                return context.getString(R.string.preauth_advice_en);
            case ConstantApp.PURCHASE_ADVICE_FULL:
                return context.getString(R.string.preauth_advice_en);
            case ConstantApp.PURCHASE_ADVICE_PARTIAL:
                return context.getString(R.string.preauth_advice_en);
        }
        return "";
    }

    public static String getTransactionNameArabic(Context context, String tag) {
        switch (tag) {
            case ConstantApp.PURCHASE:
                return context.getString(R.string.purhase_ar);
            case ConstantApp.PURCHASE_NAQD:
                return context.getString(R.string.purchase_with_naqd_ar);
            case ConstantApp.PURCHASE_REVERSAL:
                return context.getString(R.string.reversal_ar);
            case ConstantApp.REFUND:
                return context.getString(R.string.refund_ar);
            case ConstantApp.CASH_ADVANCE:
                return context.getString(R.string.cash_advice_ar);
            case ConstantApp.PRE_AUTHORISATION:
                return context.getString(R.string.preauth_ar);
            case ConstantApp.PRE_AUTHORISATION_VOID:
                return context.getString(R.string.preauth_void_ar);
            case ConstantApp.PRE_AUTHORISATION_EXTENSION:
                return context.getString(R.string.preauth_extension_ar);
            case ConstantApp.PURCHASE_ADVICE_MANUAL:
                return context.getString(R.string.preauth_advice_ar);
            case ConstantApp.PURCHASE_ADVICE_FULL:
                return context.getString(R.string.preauth_advice_ar);
            case ConstantApp.PURCHASE_ADVICE_PARTIAL:
                return context.getString(R.string.preauth_advice_ar);
        }
        return "";
    }

    public static String getTransactionAmountArabic(Context context, String tag) {
        switch (tag) {
            case ConstantApp.PURCHASE:
                return context.getString(R.string.purchase_amount_ar);
            case ConstantApp.NAQD_AMOUNT:
                return context.getString(R.string.naqd_amount_ar);
            case ConstantApp.PURCHASE_REVERSAL:
                return context.getString(R.string.reversal_amt_ar);
            case ConstantApp.REFUND:
                return context.getString(R.string.refund_amt_ar);
            case ConstantApp.CASH_ADVANCE:
                return context.getString(R.string.cash_advance_amt_ar);
            case ConstantApp.PRE_AUTHORISATION:
                return context.getString(R.string.auth_amount_ar);
            case ConstantApp.PRE_AUTHORISATION_VOID:
                return context.getString(R.string.preauth_void_amt_ar);
            case ConstantApp.PRE_AUTHORISATION_EXTENSION:
                return context.getString(R.string.preauth_extension_amt_ar);
            case ConstantApp.PURCHASE_ADVICE_MANUAL:
                return context.getString(R.string.purchase_advice_amount_ar);
            case ConstantApp.PURCHASE_ADVICE_FULL:
                return context.getString(R.string.purchase_advice_amount_ar);
            case ConstantApp.PURCHASE_ADVICE_PARTIAL:
                return context.getString(R.string.purchase_advice_amount_ar);
            case ConstantApp.Purchase_Amount:
                return context.getString(R.string.purchase_amount_ar);
        }
        return "";
    }

    public static String formatLanguageNumber(Activity activity, String amt) {
        if (checkArabicLanguage(activity)) {
            return getArabicNumbersPlain(amt);
        } else
            return amt;
    }

    public static String getArabicNumbers(String amount) {
        String str = changeAmountFormatWithDecimal(amount);
        char[] arabicChars = {'٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩'};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                builder.append(arabicChars[(int) (str.charAt(i)) - 48]);
            } else if (str.charAt(i) == '.') {
                builder.append(",");
            }
        }
        Logger.v("Number in English : " + str);
        Logger.v("Number In Arabic : " + builder.toString());
        return builder.toString();
    }

    public static String getArabicNumbers(double amount) {
        String str = changeAmountFormatWithDecimal(amount);
        char[] arabicChars = {'٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩'};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                builder.append(arabicChars[(int) (str.charAt(i)) - 48]);
            } else if (str.charAt(i) == '.') {
                builder.append(",");
            }
        }
        Logger.v("Number in English : " + str);
        Logger.v("Number In Arabic : " + builder.toString());
        return builder.toString();
    }
    public static String getArabicNumbers(String amount,boolean isZeroExtra) {
        String str = changeAmountFormatWithDecimal((Double.parseDouble(amount))/100);
        char[] arabicChars = {'٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩'};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                builder.append(arabicChars[(int) (str.charAt(i)) - 48]);
            } else if (str.charAt(i) == '.') {
                builder.append(",");
            }
        }
        Logger.v("Number in English : " + str);
        Logger.v("Number In Arabic : " + builder.toString());
        return builder.toString();
    }

    public static String getArabicNumbersPlain(String str) {
        char[] arabicChars = {'٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩'};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                builder.append(arabicChars[(int) (str.charAt(i)) - 48]);
            } else if (str.charAt(i) == '.') {
                builder.append(",");
            }
        }
        Logger.v("Number in English : " + str);
        Logger.v("Number In Arabic : " + builder.toString());
        return builder.toString();
    }

    public static String getArabicNumbersSimple(String str) {
        String regex = "[0-9]+";
        if (!str.matches(regex)) {
            return str;
        }
        char[] arabicChars = {'٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩'};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                builder.append(arabicChars[(int) (str.charAt(i)) - 48]);
            } else if (str.charAt(i) == '.') {
                builder.append(",");
            }
        }
        Logger.v("Number in English : " + str);
        Logger.v("Number In Arabic : " + builder.toString());
        return builder.toString();
    }

    public static String changeAmountFormatWithDecimal(String amt) {
        Logger.v("amount --1-" + amt);
        double amount = ((double) Long.parseLong(amt)) / 100;
        Logger.v("amount --1-" + amount);
        return String.format(Locale.ENGLISH, "%.2f", amount);
    }

    public static String changeAmountFormatWithDecimal(double amt) {
        Logger.v("amount --1-" + amt);
        //  double amount = ((double) Long.parseLong(amt)) / 100;
        // Logger.v("amount --1-" + amount);
        return String.format("%.2f", amt);
    }

//    public static void checkAPN(final Context context) {
//        if (checkSimStatus(context)) {
//            Utils.alertDialogGPRSShow(context, new Utils.DialogeClickWithString() {
//                @Override
//                public void onClick(String data) {
//                    AppManager.getInstance().setGPRSAPN(data);
//                    AppManager.getInstance().setConnectonMode(true);
//                    saveConnection(context);
//                }
//            });
//        } else {
//            Toast.makeText(context, context.getString(R.string.sim_not_present), Toast.LENGTH_SHORT).show();
//        }
//    }

//    public static void alertDialogGPRSShow(final Context context, final DialogeClickWithString click) {
//        dismissDialoge();
//        if (((BaseActivity) context).isFinishing())
//            return;
//
////        final ApnModule module = new ApnModule(context);
////        final List<ApnEntity> nameList = module.getAllAPIList();
////        Logger.v("module --" + nameList.size());
////        if (nameList.size() == 0) {
////            return;
////        }
//        final int[] type = new int[1];
//        final Dialog alertDialog1 = new Dialog(context);
//        alertDialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        if (alertDialog1.getWindow() != null)
//            alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        alertDialog1.setContentView(R.layout.list_select);
//
//        final LinearLayout ll_holder = alertDialog1.findViewById(R.id.ll_holder);
//        final LinearLayout save_btnnn = alertDialog1.findViewById(R.id.save_btnnn);
//        Button btn_save = alertDialog1.findViewById(R.id.btn_save);
//        final EditText edt_value = alertDialog1.findViewById(R.id.edt_value);
//        final EditText edt_value1 = alertDialog1.findViewById(R.id.edt_value1);
//        final EditText edt_value_mnc = alertDialog1.findViewById(R.id.edt_value_mnc);
//        final EditText edt_value_mcc = alertDialog1.findViewById(R.id.edt_value_mcc);
//
//        TextView txt_selected = alertDialog1.findViewById(R.id.txt_selected);
//        txt_selected.setText("Access Point Names \n نقاط الوصول");
//        final String[] nameList = AppInit.apnName;
//        for (int i = 0; i < nameList.length; i++) {
//            Button button = new Button(context);
//            button.setText(nameList[i]);
//            final int ii = i;
//            button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Logger.v("setOnClickListener-" + ii);
//                    Logger.v("setOnClickListener-" + nameList[ii]);
//                    ll_holder.setVisibility(View.GONE);
//                    save_btnnn.setVisibility(View.VISIBLE);
//                    type[0] = ii;
//                    Logger.v("type -" + type[0]);
//                    edt_value.setText(AppInit.apnName[type[0]]);
//                    edt_value1.setText(AppInit.apnList[type[0]]);
//                    edt_value_mnc.setText(AppInit.mnc[type[0]]);
//                    edt_value_mcc.setText(AppInit.mcc[type[0]]);
////                    showEditApn(ii, click, context);
//                }
//            });
//            ll_holder.addView(button);
//        }
//        btn_save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Logger.v("click save");
//                if (edt_value.getText().toString().trim().length() != 0 && edt_value1.getText().toString().trim().length() != 0
//                        && edt_value_mnc.getText().toString().trim().length() != 0 && edt_value_mcc.getText().toString().trim().length() != 0) {
//                    final ApnModule module = new ApnModule(context);
//                    Logger.v("click save APN");
//                    if (module.saveApnNode(edt_value.getText().toString().trim(), edt_value1.getText().toString().trim()
//                            ,edt_value_mcc.getText().toString().trim(), edt_value_mnc.getText().toString().trim())) {
//                        Logger.v("Created");
//                        click.onClick(AppInit.apnName[type[0]]);
//                    }else
//                        Toast.makeText(context, context.getResources().getString(R.string.encountered_error), Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(context, context.getResources().getString(R.string.please_enter), Toast.LENGTH_SHORT).show();
//                    Logger.v("Created else");
//                }
//            }
//        });
//        alertDialog1.setCanceledOnTouchOutside(false);
//
//        Window window = alertDialog1.getWindow();
//        WindowManager.LayoutParams wlp = window.getAttributes();
//        //wlp.gravity = Gravity.BOTTOM;
//        wlp.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;
//        wlp.y = 140;
////        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//        window.setAttributes(wlp);
//
//        alertDialog1.show();
//    }

    public static HashMap<String, String> getParsedTag55(String tag) {
        String[] tag55 = AppConfig.EMV.TAG55;
        HashMap<String, String> tagValue = new HashMap<>();
        if (tag == null || tag.trim().length() == 0) {
            return tagValue;
        }
        int pos = 0;
        for (int i = 0; i < tag55.length; i++) {
            int tagLength = tag55[i].length();
            if ((pos + tagLength) <= tag.length()) {
                String tagName = tag.substring(pos, pos + tagLength);
                if (tagName.equalsIgnoreCase(tag55[i])) {
                    String valLength = tag.substring(pos + tagLength, pos + tagLength + 2);
                    int value = Integer.parseInt(valLength, 16);
                    tagValue.put(tag55[i], tag.substring(pos + tagLength + 2, pos + tagLength + 2 + (value * 2)));
                    pos = pos + tagLength + 2 + (value * 2);
                }
            }
        }
        return tagValue;
    }

    public static boolean allowedMTI(String mti0) {
        switch (mti0.trim().toUpperCase()) {
            case ConstantAppValue.FINANCIAL:
            case ConstantAppValue.FINANCIAL_ADVISE:
            case ConstantAppValue.FINANCIAL_ADVISE_REPEAT:
            case ConstantAppValue.REVERSAL:
            case ConstantAppValue.REVERSAL_REPEAT:
            case ConstantAppValue.AUTH:
            case ConstantAppValue.AUTH_ADVISE:
            case ConstantAppValue.AUTH_ADVISE_REPEAT:
                return true;
        }
        return false;
    }

    public static String fetchMti(String mti) {
        String mti1 = mti.toUpperCase();
        Logger.v("MTI --" + mti1);
        switch (mti1) {
            case ConstantAppValue.FINANCIAL:
                return ConstantAppValue.FINANCIAL_RESPONSE;
            case ConstantAppValue.FINANCIAL_ADVISE:
                return ConstantAppValue.FINANCIAL_ADVICE_RESPONSE;
            case ConstantAppValue.FINANCIAL_ADVISE_REPEAT:
                return ConstantAppValue.FINANCIAL_ADVICE_RESPONSE;
            case ConstantAppValue.REVERSAL:
                return ConstantAppValue.REVERSAL_RESPONSE;
            case ConstantAppValue.REVERSAL_REPEAT:
                return ConstantAppValue.REVERSAL_RESPONSE;
            case ConstantAppValue.AUTH:
                return ConstantAppValue.AUTH_RESPONSE;
            case ConstantAppValue.AUTH_ADVISE:
                return ConstantAppValue.AUTH_ADVISE_RESPONSE;
            case ConstantAppValue.AUTH_ADVISE_REPEAT:
                return ConstantAppValue.AUTH_ADVISE_RESPONSE;
            default:
                return "1230";
        }
    }



    public static String formatAmount(Activity context, double amt) {
        String value = String.format("%.2f", amt);
        LocalizationActivityDelegate localizationDelegate = new LocalizationActivityDelegate(context);
        Logger.v("localizationDelegate----" + localizationDelegate.getLanguage(context));
        Logger.v("localizationDelegateus----" + new Locale("en", "US"));
        if (localizationDelegate.getLanguage(context).equals(new Locale("en", "US"))) {
            return " SAR " + value;
        } else {
            return (" ريال " + Utils.getArabicNumbersPlain(value));
        }
    }

    public static boolean checkArabicLanguage(Activity context) {
        LocalizationActivityDelegate localizationDelegate = new LocalizationActivityDelegate(context);
        Logger.v("localizationDelegate----" + localizationDelegate.getLanguage(context));
        Logger.v("localizationDelegateus----" + new Locale("en", "US"));
        if (localizationDelegate.getLanguage(context).equals(new Locale("en", "US"))) {
            return false;
        } else
            return true;
    }

    public static String formatAmountWithoutSAR(Activity context, double amt) {
        String value = String.format("%.2f", amt);
        LocalizationActivityDelegate localizationDelegate = new LocalizationActivityDelegate(context);
        Logger.v("localizationDelegate----" + localizationDelegate.getLanguage(context));
        Logger.v("localizationDelegateus----" + new Locale("en", "US"));
        if (localizationDelegate.getLanguage(context).equals(new Locale("en", "US"))) {
            return value;
        } else {
            return (Utils.getArabicNumbersPlain(value));
        }
    }

    public static String formatAmountWithoutSAR(double amt) {
        String value = String.format("%.2f", amt);
        return (Utils.getArabicNumbersPlain(value));
    }

    public static String getCardName(String id) {
        switch (id.toUpperCase()) {
            case "P1":
                return ConstantAppValue.MADA;
            case "VC":
                return ConstantAppValue.VISA_CREDIT;
            case "VD":
                return ConstantAppValue.VISA_DEBIT;
            case "MC":
                return ConstantAppValue.MASTER_CARD;
            case "DM":
                return ConstantAppValue.MAESTRO;
            case "AX":
                return ConstantAppValue.AMEX;
            case "UP":
                return ConstantAppValue.UNION_PAY;
            case "JC":
                return ConstantAppValue.JCB;
            case "DC":
                return ConstantAppValue.DISCOVER;
            case "GN":
                return ConstantAppValue.GCCNET;
        }
        return "";
    }

    public static String getCardNameArabic(String id) {
        switch (id.toUpperCase()) {
            case "P1":
                return ConstantAppValue.MADA_AR;
            case "VC":
                return ConstantAppValue.VISA_CREDIT_AR;
            case "VD":
                return ConstantAppValue.VISA_DEBIT_AR;
            case "MC":
                return ConstantAppValue.MASTER_CARD_AR;
            case "DM":
                return ConstantAppValue.MAESTRO_AR;
            case "AX":
                return ConstantAppValue.AMEX_AR;
            case "UP":
                return ConstantAppValue.UNION_PAY_AR;
            case "JC":
                return ConstantAppValue.JCB_AR;
            case "DC":
                return ConstantAppValue.DISCOVER_AR;
            case "GN":
                return ConstantAppValue.GCCNET_AR;
        }
        return "";
    }

    public static HashMap<String, String> getParsedTag55(byte[] tag1) {
        Logger.v(tag1);
        String tag = new String(tag1);
        Logger.v(tag);
        String[] tag55 = AppConfig.EMV.TAG55;
        HashMap<String, String> tagValue = new HashMap<>();
        int pos = 0;
        for (int i = 0; i < tag55.length; i++) {
            int tagLength = tag55[i].length();
            String tagName = tag.substring(pos, pos + tagLength);
            if (tagName.equalsIgnoreCase(tag55[i])) {
                String valLength = tag.substring(pos + tagLength, pos + tagLength + 2);
                int value = Integer.parseInt(valLength, 16);
                tagValue.put(tag55[i], tag.substring(pos + tagLength + 2, pos + tagLength + 2 + (value * 2)));
                Logger.v("tag55[" + i + "] --" + tag55[i]);
                Logger.v("tagValue[" + i + "] --" + tagValue.get(tag55[i]));
                pos = pos + tagLength + 2 + (value * 2);
            }
        }
        return tagValue;
    }

    public static boolean checkTMSValidation(String data, String transaction) {
        if (data.trim().length() == 0)
            return false;
        char[] value = data.toCharArray();
        int position = getTransactionPosition(transaction);
        if (value.length < position || position == -1)
            return false;
        return (value[position] == '1');
    }

    public static boolean checkCardValidation(String data, String transaction) {
        if (data == null)
            return false;
        char[] value = data.toCharArray();
        int position = getTransactionPosition(transaction);
        if (value.length < position || position == -1)
            return false;
        return (value[position] == '2') || (value[position] == '3');
    }

    public static String checkCardIndication(String data, String transaction) {
        if (data == null)
            return "";
        char[] value = data.toCharArray();
        int position = getTransactionPosition(transaction);
        if (value.length < position || position == -1)
            return "";
        return (value[position]) + "";
    }

    public static boolean checkCardValidationLine(String data, String transaction) {
        char[] value = data.toCharArray();
        int position = getTransactionPosition(transaction);
        if (value.length < position || position == -1)
            return false;
        return (value[position] == '1') || (value[position] == '3');
    }

    //TODO online changes
    public static int getTransactionPosition(String transaction) {
        switch (transaction) {
            case ConstantApp.PURCHASE:
                return 0;
            case ConstantApp.PURCHASE_NAQD:
                return 1;
            case ConstantApp.PURCHASE_REVERSAL:
                return 6;
            case ConstantApp.REFUND:
                return 3;
            case ConstantApp.CASH_ADVANCE:
                return 5;
            case ConstantApp.PRE_AUTHORISATION:
                return 4;
            case ConstantApp.PRE_AUTHORISATION_VOID:
                return 4;
//            case Constant.PRE_AUTHORISATION_VOID_FULL:
//                return 6;
//            case Constant.PRE_AUTHORISATION_VOID_PARTIAL:
//                return 6;
            case ConstantApp.PRE_AUTHORISATION_EXTENSION:
                return 4;
            case ConstantApp.PURCHASE_ADVICE:
                return 2;
            case ConstantApp.PURCHASE_ADVICE_FULL:
                return 2;
            case ConstantApp.PURCHASE_ADVICE_PARTIAL:
                return 2;
            case ConstantApp.PURCHASE_ADVICE_MANUAL:
                return 2;
            case ConstantApp.FALLBACK:
                return 0;
            case ConstantApp.FINANCIAL_AUTHORIZATION:
                return 0;
            case ConstantApp.FINANCIAL_ADVISE:
                return 0;
        }
        return -1;
    }

    public static String getCardFromBinRange(String bin, List<BinRangeTuple> model) {
        return checkNormalBinRange(bin, model);
    }

    public static String getCardFromBinRangeF(String bin, List<BinRangeTuple> model) {
        return checkVarientBinRange(bin, model);
    }

    private static String checkVarientBinRange(String bin, List<BinRangeTuple> model) {
        for (int i = 0; i < model.size(); i++) {
            Logger.v("getCardIndicator --" + model.get(i).getCardIndicator());
            Logger.v("Bin --" + model.get(i).getBinRanges());
            if (model.get(i).getBinRanges().trim().length() != 0) {
                Logger.v("Bin --" + model.get(i).getBinRanges());
                if (model.get(i).getBinRanges().contains("|")) {
                    String[] ranges = model.get(i).getBinRanges().split("\\|");
                    for (int j = 0; j < ranges.length; j++) {
                        Logger.v("Bin --" + ranges[j]);
                        if (ranges[j].contains("F") && checkBinBlockWithF(bin, ranges[j]))
                            return model.get(i).getCardIndicator();
                    }
                } else if (checkBinBlockWithF(bin, model.get(i).getBinRanges()))
                    return model.get(i).getCardIndicator();
            }
        }
        return "";
    }

    private static String checkNormalBinRange(String bin, List<BinRangeTuple> model) {
        for (int i = 0; i < model.size(); i++) {
            Logger.v("getCardIndicator --" + model.get(i).getCardIndicator());
            Logger.v("Bin --" + model.get(i).getBinRanges());
            if (model.get(i).getBinRanges().trim().length() != 0) {
                Logger.v("Bin --" + model.get(i).getBinRanges());
                if (model.get(i).getBinRanges().contains("|")) {
                    String[] ranges = model.get(i).getBinRanges().split("\\|");
                    for (int j = 0; j < ranges.length; j++) {
                        Logger.v("Bin --" + ranges[j]);
                        if (checkBinBlock(bin, ranges[j]))
                            return model.get(i).getCardIndicator();
                    }
                } else if (checkBinBlock(bin, model.get(i).getBinRanges()))
                    return model.get(i).getCardIndicator();
            }
        }
        return "";
    }

    public static boolean checkBinBlock(String bin, String range) {
        if (range.contains("-")) {
            return (checkBinRangers(bin, range));
        } else
            return (range.equalsIgnoreCase(bin));
    }

    public static boolean checkBinBlockWithF(String bin, String range) {
        if (range.contains("-")) {
            return (checkBinRangers(bin, range));
        } else
            return (checkIndividualBin(range, bin));
    }

    private static boolean checkIndividualBin(String range, String bin) {
        if (range.contains("F")) {
            String range1 = ((range + " ").split("F")[0]).trim();
            Logger.v("Split --" + range1);
            Logger.v("Vall --" + bin.substring(0, range1.length()));
            return bin.substring(0, range1.length()).equalsIgnoreCase(range1);
        }
        return (range.equalsIgnoreCase(bin));
    }

    public static boolean checkBinRangers(String card, String range) {
        String value[] = range.split("-");
        if (!value[0].contains("F") && !value[1].contains("F")) {
            int startValue = Integer.parseInt(value[0]);
            int endValue = Integer.parseInt(value[1]);
            int bin = Integer.parseInt(card);
            return bin >= startValue && bin <= endValue;
        }
        return false;
    }

    public static boolean checkCardPresent(Context context) {
//        final ICCardModule iCCardModule = SDKDevice.getInstance(context).getICCardModule();
//        final K21RFCardModule rfCardModule = SDKDevice.getInstance(context).getRFCardModule();
        boolean isCardRemoved = false;
//        if (AppConfig.EMV.consumeType == 2) {
//            isCardRemoved = rfCardModule.isRfcardExist();
//        } else {
//            Map<ICCardSlot, ICCardSlotState> map = new HashMap<ICCardSlot, ICCardSlotState>();
//            map = iCCardModule.checkSlotsState();
//            for (Map.Entry<ICCardSlot, ICCardSlotState> entry : map.entrySet()) {
//                if (entry.getKey() != null)
//                    Logger.v("Key --" + entry.getKey() + "Value --" + entry.getValue());
//                if (!entry.getValue().toString().equals("NO_CARD")) {
//                    isCardRemoved = true;
//                }
//            }
//        }
        Logger.v("Card presence " + query_contact_card_presence());
        if (query_contact_card_presence() == 0) {
            AppConfig.isCardRemoved = false;
        } else {
            AppConfig.isCardRemoved = true;
        }
        return AppConfig.isCardRemoved;
    }

    public static boolean checkPrinterPaper(Context context, DialogeClick click) {
//        Printer printer = SDKDevice.getInstance(context).getPrinter();
//        if (printer != null) {
//            PrinterStatus printerStatus = printer.getStatus();
//            if (printerStatus.toString().equalsIgnoreCase(PrinterStatus.OUTOF_PAPER.toString())) {
//                Utils.alertDialogShow(context, context.getString(R.string.out_of_paper), click);
//                return true;
//            }
//        }
        return false;
    }

    public static String fetchIndicatorFromAID(String aid1) {
        String aid = aid1.toUpperCase();
        Logger.v("AID --" + aid);
        if (aid.contains(ConstantApp.A0000000031010)) {
            //aid = card indicator
            aid = ConstantAppValue.A0000000031010;
        } else if ((aid.contains(ConstantApp.A0000000032010))) {
            aid = ConstantAppValue.A0000000032010;
        } else if ((aid.contains(ConstantApp.A0000000041010))) {
            aid = ConstantAppValue.A0000000041010;
        } else if ((aid.contains(ConstantApp.A0000000043060))) {
            aid = ConstantAppValue.A0000000043060;
        } else if ((aid.contains(ConstantApp.A0000002281010))) {
            aid = ConstantAppValue.A0000002281010;
        } else if ((aid.contains(ConstantApp.A0000002282010))) {
            aid = ConstantAppValue.A0000002282010;
        } else if ((aid.contains(ConstantApp.A000000333010101))) {
            aid = ConstantAppValue.A000000333010101;
        } else if ((aid.contains(ConstantApp.A000000333010102))) {
            aid = ConstantAppValue.A000000333010102;
        } else if ((aid.contains(ConstantApp.A00000002501))) {
            aid = ConstantAppValue.A00000002501;
        } else
            aid = "";
        Logger.v("AID --" + aid);
        return aid;
    }

    public static String getFloorLimit(String aid) {
        aid = fetchIndicatorFromAID(aid);
        DeviceSpecificModel deviceSpecificModel1 = AppManager.getInstance().getDeviceSpecificModel();
        if (aid.equalsIgnoreCase(ConstantAppValue.A0000002281010)) {
            Logger.v("CVM Limit" + deviceSpecificModel1.getTerminalCVMRequiredLimitMada());
            Logger.v("Transaction Limit" + deviceSpecificModel1.getTerminalContactlessTransactionLimitMada());
            return deviceSpecificModel1.getTerminalContactlessFloorLimitMada();
        }
        if (aid.equalsIgnoreCase(ConstantAppValue.A0000000032010)) {
            Logger.v("CVM Limit" + deviceSpecificModel1.getTerminalCVMRequiredLimit1());
            Logger.v("Transaction Limit" + deviceSpecificModel1.getTerminalContactlessTransactionLimit1());
            return deviceSpecificModel1.getTerminalContactlessFloorLimit1();
        }
        if (aid.equalsIgnoreCase(ConstantAppValue.A0000000041010)) {
            Logger.v("CVM Limit" + deviceSpecificModel1.getTerminalCVMRequiredLimit2());
            Logger.v("Transaction Limit" + deviceSpecificModel1.getTerminalContactlessTransactionLimit2());
            return deviceSpecificModel1.getTerminalContactlessFloorLimit2();
        }
        return "";
    }

    public static void LoadAID(LifecycleOwner owner, final Context context) {
        WorkManager mWorkManager = WorkManager.getInstance(context);
        OneTimeWorkRequest.Builder mRequest = new OneTimeWorkRequest.Builder(LoadKeyWorker.class);
        WorkRequest build = mRequest.build();
        mWorkManager.enqueue(build);
        mWorkManager.getWorkInfoByIdLiveData(build.getId()).observe(owner, new Observer<WorkInfo>() {
            @Override
            public void onChanged(@Nullable WorkInfo workInfo) {
                if (workInfo != null) {
                    WorkInfo.State state = workInfo.getState();
                    Logger.v("Starte --" + state);
                    if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
//                        Logger.v("SIZE -- " + LoadKeyWorker.aidList_sub.size());
//                        loadAid(0, context);
                    }
//                    if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
//                    if (workInfo.getOutputData().getInt(LoadKeyWorker.AID_POSITION, -1) != -1)
//                        LoadAID(workInfo.getOutputData());
//                    }

                }
            }
        });
    }


    public static void switchConnection(final Context activity, MenuModel.MenuItem tag) {
        hideProgressDialoge();
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (dialog.getWindow() != null)
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setTitle(tag.getMenu_name());
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialoge_radionbutton);
        RadioGroup radioGroup = dialog.findViewById(R.id.radioGroup);
        Button connection_submit = dialog.findViewById(R.id.connection_submit);
        boolean switchConnection = AppManager.getInstance().getConnectonMode();
        ((RadioButton) dialog.findViewById(R.id.radioGPRS)).setChecked(switchConnection);
        ((RadioButton) dialog.findViewById(R.id.radioWifi)).setChecked(!switchConnection);
        dialog.findViewById(R.id.radioWifi);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rbtGPRS = group.findViewById(R.id.radioGPRS);
                RadioButton rbtWifi = group.findViewById(R.id.radioWifi);
                if (rbtGPRS.isChecked()) {
                    if (checkSimStatus(activity)) {
                        AppManager.getInstance().setConnectonMode(true);
                    } else {
                        rbtWifi.setChecked(true);
                        Toast.makeText(activity, activity.getString(R.string.sim_not_present), Toast.LENGTH_SHORT).show();
                    }
                } else if (rbtWifi.isClickable()) {
                    AppManager.getInstance().setConnectonMode(false);
                }
            }
        });
        connection_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean switchConnection = AppManager.getInstance().getConnectonMode();
                enableWifi(activity, !switchConnection);
                enableMobileData(activity, switchConnection);
                if (!switchConnection)
                    Toast.makeText(activity, activity.getString(R.string.connection_switched_to_wifi), Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(activity, activity.getString(R.string.connection_switched_to_mobile_data), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                dialog.cancel();
            }
        });
        dialog.findViewById(R.id.connection_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialog.cancel();
            }
        });

        dialog.show();

    }

    public static void enableWifi(Context context, boolean enable) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(enable);
    }

    public static void enableMobileData(Context context, boolean mobileDataEnabled) {
        try {
            TelephonyManager telephonyService = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            Method setMobileDataEnabledMethod = telephonyService.getClass().getDeclaredMethod("setDataEnabled", boolean.class);
            setMobileDataEnabledMethod.invoke(telephonyService, mobileDataEnabled);
        } catch (Exception ex) {
            Logger.v("Exception --" + ex.getMessage());
        }

    }

    public static boolean checkSimStatus(Context context) {
        TelephonyManager telMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telMgr.getSimState();
        if (simState == TelephonyManager.SIM_STATE_READY)
            return true;
        return false;
    }

    public static String changeArabic(String arabic) {
        return new String(arabic.getBytes(Charset.forName("Cp1252")), Charset.forName("ISO-8859-6")).trim(); //TMS
    }

    public static boolean isProbablyArabic(String s) {
        for (int i = 0; i < s.length(); ) {
            int c = s.codePointAt(i);
            if (c >= 0x0600 && c <= 0x06E0)
                return true;
            i += Character.charCount(c);
        }
        return false;
    }

    // function to split a list into two sublists in Java
    public static List[] split(List<KeyValueModel> list, int evenNumber) {
        // create two empty lists
        List<KeyValueModel> first = new ArrayList<KeyValueModel>();
        List<KeyValueModel> second = new ArrayList<KeyValueModel>();

        // get size of the list
        int size = list.size();

        // First size)/2 element copy into list
        // first and rest second list
        for (int i = 0; i < size / evenNumber; i++)
            first.add(list.get(i));

        // Second size)/2 element copy into list
        // first and rest second list
        for (int i = size / evenNumber; i < size; i++)
            second.add(list.get(i));

        // return a List of array
        return new List[]{first, second};
    }


    public static void alertDialogShow(final Context context, String message, final boolean moveLandinPage) {
        dismissDialoge();
        if (((BaseActivity) context).isFinishing())
            return;
        alertDialog = new Dialog(context);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (alertDialog.getWindow() != null)
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.setContentView(R.layout.custom_dialog_transparent);
        alertDialog.findViewById(R.id.loading).setVisibility(View.GONE);
        TextView text = (TextView) alertDialog.findViewById(R.id.text_dialog);
        text.setText(message);
        Button ok = alertDialog.findViewById(R.id.ok);
        Button cancel = alertDialog.findViewById(R.id.cancel);
        cancel.setVisibility(View.GONE);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialoge();
                if (moveLandinPage)
                    MapperFlow.getInstance().moveToLandingPage(context, true, 20);
                else
                    ((BaseActivity) context).finish();
            }
        });
        //alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        if (alertDialog != null)
            if (!((Activity) context).isFinishing())
                shoeDialoge();
    }

    public static void shoeDialoge() {
        try {
            if (alertDialog != null)
                alertDialog.show();
        } catch (IllegalArgumentException e) {
            Logger.v("Illegal Exception");
            alertDialog = null;
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    public static boolean isInternetAvailable(Context context, boolean showDialoge) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

        if (activeNetworkInfo != null) { // connected to the internet
            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                Logger.v("TYPE_WIFI");
                return true;
            } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                Logger.v("TYPE_MOBILE");
                return true;
            }
        }
        if (AppManager.getInstance().isDebugEnabled() && checkMti())
            return true;
        ((BaseActivity) context).showDialoge(context.getString(R.string.please_check_internet_connection), showDialoge);
        Logger.v("Internet False");
        return false;
    }

    private static boolean checkMti() {
        if (reqObj == null)
            return true;
        return Utils.allowedMTI(reqObj.getMti0());
    }

    public static Utils.DialogeClick dialoge = new Utils.DialogeClick() {
        @Override
        public void onClick() {
            Utils.dismissDialoge();
        }
    };

    public static boolean checkPrinterPaper(Context context) {

        PrinterDevice printer = SDKDevice.getInstance(context).getPrinter();
        boolean status = false;
        try {
            printer.open();
            if ( printer.queryStatus()==1){
                Logger.v("have paper");
                printer.close();
                status = false;
                return status;
            }
            else {
                Utils.alertDialogShow(context, context.getString(R.string.out_of_paper), dialoge);
                Logger.v("out of paper");
                printer.close();
                status = true;
                return status;
            }
        } catch (DeviceException e) {
            e.printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return status;


//        Printer printer = SDKDevice.getInstance(context).getPrinter();
//        if (printer != null) {
//            PrinterStatus printerStatus = printer.getStatus();
//            if (printerStatus.toString().equalsIgnoreCase(PrinterStatus.OUTOF_PAPER.toString())) {
//                Utils.alertDialogShow(context, context.getString(R.string.out_of_paper), dialoge);
//                return true;
//            }
//        }
    }

    public static boolean checkPrinterPaper(final Context context, boolean landinPage) {

        PrinterDevice printer = SDKDevice.getInstance(context).getPrinter();
        boolean status = false;
        try {
            printer.open();
            if ( printer.queryStatus()==1){
                Logger.v("have paper");
                printer.close();
                status = false;
                return status;
            }
            else {
                Utils.alertDialogShow(context, context.getString(R.string.out_of_paper), new DialogeClick() {
                    @Override
                    public void onClick() {
                        MapperFlow.getInstance().moveToLandingPage(context, true, 22);
                    }
                });
                Logger.v("out of paper");
                printer.close();
                status = true;
                return status;
            }
        } catch (DeviceException e) {
            e.printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return status;
//        Printer printer = SDKDevice.getInstance(context).getPrinter();
//        if (printer != null) {
//            PrinterStatus printerStatus = printer.getStatus();
//            if (printerStatus.toString().equalsIgnoreCase(PrinterStatus.OUTOF_PAPER.toString())) {
//                Utils.alertDialogShow(context, context.getString(R.string.out_of_paper), new DialogeClick() {
//                    @Override
//                    public void onClick() {
//                        MapperFlow.getInstance().moveToLandingPage(context, true, 22);
//                    }
//                });
//                return true;
//            }
//        }
    }

    public static void checkInternetPing(final Context context) {
        CheckConnection task = new CheckConnection(context,
                new CheckConnectionInterface() {
                    @Override
                    public void myMethod(boolean result) {
                        if (result == true) {
                            Toast.makeText(context, context.getString(R.string.connection_succes),
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, context.getString(R.string.connection_failed), Toast.LENGTH_LONG).show();
                        }
                    }
                });
        task.execute();
    }

    public static void changeLanguage(final Context context) {
        final LocalizationActivityDelegate localizationDelegate = new LocalizationActivityDelegate((Activity) context);
//                intent = new Intent(context, SelectLanguageActivity.class);
        // custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_lang_select_layout);

        RadioButton english_rb = dialog.findViewById(R.id.english_rb);
        RadioButton arabic_rb = dialog.findViewById(R.id.arabic_rb);
        Logger.v("localizationDelegate----" + localizationDelegate.getLanguage(context));
        Logger.v("localizationDelegateus----" + new Locale("en", "US"));

        if (localizationDelegate.getLanguage(context).equals(new Locale("en", "US"))) {
            english_rb.setChecked(true);
            arabic_rb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    localizationDelegate.setLanguage(context, "ar", "EH");
                    Intent intent = new Intent(context, SplashActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    ((BaseActivity) context).finish();
                }
            });

        } else if (localizationDelegate.getLanguage(context).equals(new Locale("ar", "EH"))) {
            arabic_rb.setChecked(true);
            english_rb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    localizationDelegate.setLanguage(context, "en", "US");
                    Intent intent = new Intent(context, SplashActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    ((BaseActivity) context).finish();
                }
            });
        }
        dialog.show();
    }

    public static byte[] getSha1(byte[] source) {
        StringBuilder sb = new StringBuilder();
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("SHA-1");
            md5.update(source);
            byte[] md = md5.digest();
            return md;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static SecretKeySpec secretKey;
    private static byte[] key;

    public static void setKey(String myKey)
    {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
    public static String bcdToASCString(byte[] bytes, int offset, int len) {
        if (offset < 0) {
            offset = 0;
        }

        if (len > bytes.length - offset) {
            len = bytes.length - offset;
        }

        byte[] temp = new byte[len * 2];

        for(int i = offset; i < offset + len; ++i) {
            byte val = (byte)((bytes[i] & 240) >> 4 & 15);
            temp[i * 2] = (byte)(val > 9 ? val + 65 - 10 : val + 48);
            val = (byte)(bytes[i] & 15);
            temp[i * 2 + 1] = (byte)(val > 9 ? val + 65 - 10 : val + 48);
        }

        return new String(temp);
    }
    /************ here we are performing encryption************/
    public static String encrypt( String stringToEncrypt)
    {
        if (AppInit.ENCRYPT_DISABLE){
            return stringToEncrypt;
        }else {
           /* String secret = "tarang";
            try {
                setKey(secret);
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                return Base64.getEncoder().encodeToString(cipher.doFinal(stringToEncrypt.getBytes("UTF-8")));
            } catch (Exception e) {
                System.out.println("Error arised while encrypting: " + e.toString());
            }
            return null;*/
            return stringToEncrypt;
        }
    }
    /************ here we are performing decryption************/
    public static String decrypt( String stringToDecrypt)
    {
        if (AppInit.ENCRYPT_DISABLE){
            return stringToDecrypt;
        }else {
           /* try {
                String secret = "tarang";
                setKey(secret);
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
                cipher.init(Cipher.DECRYPT_MODE, secretKey);
                return new String(cipher.doFinal(Base64.getDecoder().decode(stringToDecrypt)));
            } catch (Exception e) {
                System.out.println("Error arised while decrypting: " + e.toString());
            }
            return null;*/
            return stringToDecrypt;
        }
    }

    public static void saveConnection(Context context) {
        enableWifi(context, false);
        enableMobileData(context, true);
//        if (!switchConnection)
//            Toast.makeText(SwitchConnectionActivity.this, getString(R.string.connection_switched_to_wifi), Toast.LENGTH_SHORT).show();
//        else
//            Toast.makeText(SwitchConnectionActivity.this, getString(R.string.connection_switched_to_mobile_data), Toast.LENGTH_SHORT).show();
    }
}
