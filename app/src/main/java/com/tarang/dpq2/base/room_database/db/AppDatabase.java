package com.tarang.dpq2.base.room_database.db;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.tarang.dpq2.base.room_database.db.dao.AIDDataDao;
import com.tarang.dpq2.base.room_database.db.dao.SAFModelDao;
import com.tarang.dpq2.base.room_database.db.dao.TMSCardSchemeDao;
import com.tarang.dpq2.base.room_database.db.dao.MessageTextModelDao;
import com.tarang.dpq2.base.room_database.db.dao.TMSPublicKeyModelDao;
import com.tarang.dpq2.base.room_database.db.dao.TransactionModelDao;
import com.tarang.dpq2.base.room_database.db.entity.SAFModelEntity;
import com.tarang.dpq2.base.room_database.db.entity.TMSAIDdataModelEntity;
import com.tarang.dpq2.base.room_database.db.entity.TMSCardSchemeEntity;
import com.tarang.dpq2.base.room_database.db.entity.TMSMessageTextModelEntity;
import com.tarang.dpq2.base.room_database.db.entity.TMSPublicKeyModelEntity;
import com.tarang.dpq2.base.room_database.db.entity.TransactionModelEntity;

@Database(entities = {TransactionModelEntity.class, TMSMessageTextModelEntity.class, SAFModelEntity.class,
        TMSPublicKeyModelEntity.class, TMSCardSchemeEntity.class, TMSAIDdataModelEntity.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase appDatabase = null;

    /**
     * from developers android, made my own singleton
     *
     * @param context
     * @return
     */
    public static AppDatabase getInstance(Context context) {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class, "payswiff_database")
                    .allowMainThreadQueries()
                    .build();
        }
        return appDatabase;
    }

    public abstract TransactionModelDao getTransactionDao();

    public abstract SAFModelDao getSAFDao();

    public abstract TMSPublicKeyModelDao getTMSPublicKeyModelDao();

    public abstract MessageTextModelDao getTMSMessageTextDao();

    public abstract TMSCardSchemeDao getTMSCardSchemeDao();

    public abstract AIDDataDao getAIDDataDao();
}