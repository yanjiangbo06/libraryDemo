package cn.com.venvy.common.db;

import java.util.ArrayList;
import java.util.List;

import cn.com.venvy.common.utils.VenvyLog;

public class DBConstants {
    private interface ITable {
        String produceCreateSQL();
    }

    public static final String DATABASE_NAME = "venvy_video.db";
    public static final int DATABASE_VERSION = 1;
    static final int ID = 0;

    public static List<String> DB_CREATE_SQL;

    public static final String[] TABLE_NAMES = {"report_os_cache", "report_live_cache"};

    public static final int TABLE_OS_REPORT = 0;
    public static final int TABLE_LIVE_REPORT = 1;

    static {
        DB_CREATE_SQL = new ArrayList<>();
        ReportDB report = new ReportDB(TABLE_NAMES[TABLE_OS_REPORT]);
        DB_CREATE_SQL.add(report.produceCreateSQL());
        ReportDB report2 = new ReportDB(TABLE_NAMES[TABLE_LIVE_REPORT]);
        DB_CREATE_SQL.add(report2.produceCreateSQL());
    }

    public static class ReportDB implements ITable {
        public static final String[] COLUMNS = {"report_id", "leavel", "create_time", "tag", "message"};
        public static final int REPORT_ID = 0;// report_Id
        public static final int REPORT_LEVEL = 1;
        public static final int REPORT_CREATE_TIME = 2;
        public static final int REPORT_TAG = 3;
        public static final int REPORT_MESSAGE = 4;
        private String mTableName;

        public ReportDB(String tableName) {
            mTableName = tableName;
        }

        public String produceCreateSQL() {
            final String sql = "CREATE TABLE IF NOT EXISTS "
                    + mTableName + "(" + COLUMNS[REPORT_ID]
                    + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMNS[REPORT_LEVEL]
                    + " INTEGER NOT NULL,"
                    + COLUMNS[REPORT_CREATE_TIME]
                    + " TEXT NOT NULL,"
                    + COLUMNS[REPORT_TAG]
                    + " TEXT NOT NULL,"
                    + COLUMNS[REPORT_MESSAGE]
                    + " TEXT NOT NULL)";
            VenvyLog.d(getClass().getSimpleName(), " createSQL = " + sql);
            return sql;
        }

    }
}
