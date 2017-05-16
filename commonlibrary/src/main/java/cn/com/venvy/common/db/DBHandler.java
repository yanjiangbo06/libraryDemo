package cn.com.venvy.common.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import cn.com.venvy.common.exception.DBException;

public class DBHandler {
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public DBHandler(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
        this.db = dbHelper.getWritableDatabase();
    }

    public void closeDatabase() {
        dbHelper.close();
    }

    public SQLiteDatabase getWriteableDatabase()
            throws DBException {
        try {
            db = dbHelper.getWritableDatabase();
            return db;
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public void openDatabase() throws DBException {
        this.getWriteableDatabase();
    }

    public boolean isOpen() {
        if (null == db) {
            return false;
        }
        return db.isOpen();
    }

    public void beginTransaction() {
        db.beginTransaction();
    }

    public void commitTransaction() {
        db.setTransactionSuccessful();
    }

    public void endTransaction() {
        db.endTransaction();
    }

    public Cursor query(String sql, String[] selectionArgs) {
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }

    public Cursor query(String sql) {
        Cursor cursor = db.rawQuery(sql, null);
        return cursor;
    }

    public Cursor query(boolean distinct, String table, String[] columns,
                        String selection, String[] selectionArgs, String groupBy,
                        String having, String orderBy, String limit) {
        Cursor cursor = db.query(distinct, table, columns, selection, selectionArgs,
                groupBy, having, orderBy, limit);
        return cursor;
    }

    public Cursor query(String table, String[] columns, String selection,
                        String[] selectionArgs, String orderBy) {
        Cursor cursor = db.query(table, columns, selection, selectionArgs, null, null,
                orderBy, null);
        return cursor;
    }

    public void execute(String sql) throws SQLException {
        db.execSQL(sql);
    }


    public long insert(String table, ContentValues contentValues) {
        long result = db.insert(table, null, contentValues);
        return result;
    }

    public int update(String table, ContentValues contentValues,
                      String whereClause, String[] whereArgs) {
        int result = db.update(table, contentValues, whereClause, whereArgs);
        return result;
    }

    public int delete(String table, String whereClause, String[] whereArgs) {
        int result = db.delete(table, whereClause, whereArgs);
        return result;
    }
}
