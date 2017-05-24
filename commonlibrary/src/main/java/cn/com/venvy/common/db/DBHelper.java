package cn.com.venvy.common.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private List<String> databaseCreate;


    public DBHelper(Context context, String name, int version, List<String> databaseCreate) {
        this(context, name, version, databaseCreate, null);
    }

    public DBHelper(Context context, String name, int version, List<String> databaseCreate, CursorFactory factory) {
        super(context, name, factory, version);
        this.databaseCreate = databaseCreate;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (databaseCreate != null && db != null) {
            db.beginTransaction();
            try {
                for (String sql : databaseCreate) {
                    db.execSQL(sql);
                }
                db.setTransactionSuccessful();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                db.endTransaction();
            }
        } else {
            throw new IllegalArgumentException(getClass().getSimpleName()
                    + "\tonCreate()");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        upgradeDB(db, oldVersion, newVersion);
    }

    protected void upgradeDB(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

