package com.tarang.dpq2.tms;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.tarang.dpq2.base.room_database.db.AppDatabase;
import com.tarang.dpq2.base.room_database.db.dao.MessageTextModelDao;
import com.tarang.dpq2.base.room_database.db.entity.TMSMessageTextModelEntity;

import java.util.List;


public class MessageTextParser  {

    public static void insertMessageText(List<String> fsList, AppDatabase database){
        TMSMessageTextModelEntity messageTextModelEntity = new TMSMessageTextModelEntity();
        messageTextModelEntity.setMessageCode(fsList.get(0).substring(3));
        messageTextModelEntity.setDisplayCode(fsList.get(1));
        messageTextModelEntity.setArabicMessageText(fsList.get(2));
        messageTextModelEntity.setEnglishMessageText(fsList.get(3));
        database.getTMSMessageTextDao().insertMessageText(messageTextModelEntity);
//        savingToDb(context,messageTextModelEntity);

    }

    private static void savingToDb(final Context context, final TMSMessageTextModelEntity messageTextModelEntity){
        final AppDatabase db = AppDatabase.getInstance(context);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                MessageTextModelDao messageTextModelDao = db.getTMSMessageTextDao();
                Long status = messageTextModelDao.insertMessageText(messageTextModelEntity);

                Log.i("De72 status : ", status +"");

                messageTextModelDao.getPrinterMessageText("0");
                messageTextModelDao.getTerminalMessageText("100");

                Log.i("De72 retailerModel1: ", messageTextModelDao.getPrinterMessageText("0")+"");
                Log.i("De72 retailerModel2: ", messageTextModelDao.getTerminalMessageText("100")+"");
            }
        });

    }

    public static void updateMessageText(List<String> fsList, AppDatabase database){
        TMSMessageTextModelEntity messageTextModelEntity = new TMSMessageTextModelEntity();
        messageTextModelEntity.setMessageCode(fsList.get(0).substring(3));
        messageTextModelEntity.setDisplayCode(fsList.get(1));
        messageTextModelEntity.setArabicMessageText(fsList.get(2));
        messageTextModelEntity.setEnglishMessageText(fsList.get(3));
        database.getTMSMessageTextDao().updateTMSMessageText(messageTextModelEntity.getMessageCode(),
                messageTextModelEntity.getDisplayCode(),
                messageTextModelEntity.getArabicMessageText(),
                messageTextModelEntity.getEnglishMessageText());
//        updateInDb(context,messageTextModelEntity);
    }


    private static void updateInDb(final Context context, final TMSMessageTextModelEntity messageTextModelEntity){
        final AppDatabase db = AppDatabase.getInstance(context);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                MessageTextModelDao messageTextModelDao = db.getTMSMessageTextDao();
                int status = messageTextModelDao.updateTMSMessageText(messageTextModelEntity.getMessageCode(),
                        messageTextModelEntity.getDisplayCode(),
                        messageTextModelEntity.getArabicMessageText(),
                        messageTextModelEntity.getEnglishMessageText());

                Log.i("De72 status : ", status +"");
            }
        });
    }

}
