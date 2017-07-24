package cangwang.com.base.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import cangwang.com.base.greenDao.DaoMaster;
import cangwang.com.base.greenDao.DaoSession;


/**
 * Created by air on 16/10/14.
 */

public class DBManager {
    private final static String dbName = "setting_db";
    private static DBManager instance;
    private Context mContext;
    private DaoMaster.DevOpenHelper openHelper;
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    public static DBManager getInstance(Context context){
        if(instance == null)
            synchronized (DBManager.class) {
                if (instance == null)
                    instance = new DBManager(context);
            }
        return instance;
    }

    public DBManager(Context context){
        this.mContext = context;
        setDataBase();
    }

    private void setDataBase(){
        openHelper = new DaoMaster.DevOpenHelper(mContext,dbName,null);
        db = openHelper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession  = daoMaster.newSession();
    }

    public DaoSession getDaoSession(){
        return daoSession;
    }

    public SQLiteDatabase getDb(){
        return db;
    }
}
