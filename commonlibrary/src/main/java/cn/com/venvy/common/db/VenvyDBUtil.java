package cn.com.venvy.common.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import cn.com.venvy.common.Exception.DBException;

/**
 * Created by yanjiangbo on 2017/5/14.
 */

public class VenvyDBUtil {

    private static DBHandler dbHandler;

    public VenvyDBUtil() throws DBException {
        init();
    }

    DBHandler init() throws DBException {
        if (dbHandler == null) {
            DBHelper dbHelper = new DBHelperImpl(DBConstants.DATABASE_NAME, DBConstants.DATABASE_VERSION, DBConstants.DB_CREATE_SQL);
            dbHandler = new DBHandler(dbHelper);
        }
        if (!dbHandler.isOpen()) {
            try {
                dbHandler.openDatabase();
            } catch (SQLiteException e) {
                throw new DBException("Database init error!", e);
            }
        }
        return this.dbHandler;
    }

    public void insert(String tableName, String columns[], String[] contents,
                       int startIndex) throws DBException {
        if (dbHandler == null || !dbHandler.isOpen()) {
            return;
        }
        ContentValues contentValues = buildContentValues(columns, startIndex,
                contents);
        long temp = dbHandler.insert(tableName, contentValues);
        if (-1 == temp) {
            throw new DBException(getClass().getSimpleName()
                    + ": insert error ");
        }
    }

    public boolean update(String tableName, String columns[], String[] contents, int targetColumnNun, String targetColumnValue, int startIndex) throws DBException {
        if (dbHandler == null || !dbHandler.isOpen()) {
            return false;
        }
        ContentValues contentValues = buildContentValues(columns, startIndex, contents);
        String whereClause = columns[targetColumnNun] + "=?";
        String[] whereArgs = {targetColumnValue};
        int temp = dbHandler.update(tableName, contentValues, whereClause, whereArgs);
        if (temp >= 1) {
            return true;
        } else {
            return false;
        }
    }

    public void delete(String tableName, String columnName, String columnValue) throws DBException {
        if (dbHandler == null || !dbHandler.isOpen()) {
            return;
        }
        String whereClause = columnName + "=?";
        String whereArgs[] = {columnValue};
        dbHandler.delete(tableName, whereClause, whereArgs);
    }

    public void delete(String tableName, String columnName, String... columnValue) throws DBException {
        if (dbHandler == null || !dbHandler.isOpen()) {
            return;
        }
        String whereClause = columnName + "=?";
        String whereArgs[] = columnValue;
        dbHandler.delete(tableName, whereClause, whereArgs);
    }

    public Cursor query(String tableName, String columnName, String... argsValue) {
        if (dbHandler == null || !dbHandler.isOpen()) {
            return null;
        }
        String querySQL = "select * from " + tableName + " where "
                + columnName + " = ?";
        String[] selectionArgs = argsValue;
        Cursor cursor = dbHandler.query(querySQL, selectionArgs);
        return cursor;
    }


    public Cursor query(String tableName) {
        if (dbHandler == null || !dbHandler.isOpen()) {
            return null;
        }
        String querySQL = "select * from " + tableName;
        Cursor cursor = dbHandler.query(querySQL);
        return cursor;
    }


    public ContentValues buildContentValues(String columns[], int startIndex,
                                            String[] contents) {
        if (columns == null || contents == null) {
            throw new IllegalArgumentException("array is null");
        }
        final int N = columns.length;
        if (startIndex < 0 || startIndex >= N || N != contents.length) {
            throw new IllegalArgumentException("array index error.");
        }
        ContentValues contentValues = new ContentValues();
        for (int i = startIndex; i < N; i++) {
            contentValues.put(columns[i], contents[i]);
        }
        return contentValues;
    }

    public boolean update(String tableName, int userIdIndex, int otherIdIndex, String columns[],
                          int startIndex, String[] contents, String otherid, String userid) throws DBException {
        if (dbHandler == null || !dbHandler.isOpen()) {
            return false;
        }
        ContentValues contentValues = buildContentValues(columns, startIndex,
                contents);
        String whereClause = columns[userIdIndex] + "=? and " + columns[otherIdIndex] + "=?";
        String[] whereArgs = {userid, otherid};
        int temp = dbHandler.update(tableName, contentValues, whereClause,
                whereArgs);
        if (temp >= 1) {
            return true;
        } else {
            return false;
        }
    }

    public void onDestroy() {
        if (dbHandler != null) {
            dbHandler.closeDatabase();
        }
    }
}
