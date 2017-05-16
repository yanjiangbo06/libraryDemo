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

    public static final String[] TABLE_NAMES = {"report_cache"};

    public static final int TABLE_REPORT = 0;

    static {
        DB_CREATE_SQL = new ArrayList<>();
        ReportDB report = new ReportDB();
        DB_CREATE_SQL.add(report.produceCreateSQL());
    }

    public static class ReportDB implements ITable {
        public static final String[] COLUMNS = {"report_id", "leavel", "create_time","message"};
        public static final int REPORT_ID = 0;// report_Id
        public static final int REPORT_LEVEL = 1;
        public static final int REPORT_CREATE_TIME = 2;
        public static final int REPORT_MESSAGE = 3;

        public String produceCreateSQL() {
            final String sql = "CREATE TABLE IF NOT EXISTS "
                    + TABLE_NAMES[TABLE_REPORT] + "(" + COLUMNS[REPORT_ID]
                    + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMNS[REPORT_LEVEL]
                    + " INTEGER NOT NULL,"
                    + COLUMNS[REPORT_CREATE_TIME]
                    + " TEXT NOT NULL,"
                    + COLUMNS[REPORT_MESSAGE]
                    + " TEXT NOT NULL)";
            VenvyLog.d(getClass().getSimpleName(), " createSQL = " + sql);
            return sql;
        }

    }
}
