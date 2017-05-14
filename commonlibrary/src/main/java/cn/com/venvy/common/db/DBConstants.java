package cn.com.venvy.common.db;

import java.util.ArrayList;
import java.util.List;

import cn.com.venvy.common.utils.VenvyLog;

public class DBConstants {
    private interface ITable {
        String produceCreateSQL();
    }

    static final String DATABASE_NAME = "venvy_video.db";
    static final int DATABASE_VERSION = 1;
    static final int ID = 0;

    static List<String> DB_CREATE_SQL;

    public static final String[] TABLE_NAMES = {"report_cache"};

    public static final int TABLE_REPORT = 0;

    static {
        DB_CREATE_SQL = new ArrayList<>();

        Report report = new Report();
        DB_CREATE_SQL.add(report.produceCreateSQL());

    }

    public static class Report implements ITable {
        public static final String[] COLUMNS = {"report_id", "leavel", "message"};
        public static final int REPORT_ID = 0;// report_Id
        public static final int REPORT_LEAVEL = 1;
        public static final int REPORT_MESSAGE = 2;// unit_Condition

        public String produceCreateSQL() {
            final String sql = "CREATE TABLE IF NOT EXISTS "
                    + TABLE_NAMES[TABLE_REPORT] + "(" + COLUMNS[REPORT_ID]
                    + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMNS[REPORT_LEAVEL]
                    + " INTEGER NOT NULL,"
                    + COLUMNS[REPORT_MESSAGE]
                    + " TEXT NOT NULL)";
            VenvyLog.d(getClass().getSimpleName(), " createSQL = " + sql);
            return sql;
        }

    }
}
