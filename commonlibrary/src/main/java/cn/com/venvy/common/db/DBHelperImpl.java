package cn.com.venvy.common.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import java.util.List;

import cn.com.venvy.Platform;
import cn.com.venvy.common.Exception.DBException;

public class DBHelperImpl extends DBHelper {


    public DBHelperImpl(String name, int version, List<String> databaseCreate) throws DBException {
        this(Platform.instance().getContext(), name, version, databaseCreate);
    }

    public DBHelperImpl(Context context, String name, int version, List<String> databaseCreate) throws DBException {
        this(context, name, version, databaseCreate, null);
    }

    public DBHelperImpl(Context context, String name, int version, List<String> databaseCreate, CursorFactory factory) throws DBException {
        super(context, name, version, databaseCreate, factory);
    }

    @Override
    protected void upgradeDB(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
