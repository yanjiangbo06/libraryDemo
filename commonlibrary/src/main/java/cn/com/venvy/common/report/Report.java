package cn.com.venvy.common.report;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.com.venvy.Platform;
import cn.com.venvy.common.db.DBConstants;
import cn.com.venvy.common.db.VenvyDBController;
import cn.com.venvy.common.exception.DBException;
import cn.com.venvy.common.http.HttpRequest;
import cn.com.venvy.common.http.RequestFactory;
import cn.com.venvy.common.http.base.IRequestConnect;
import cn.com.venvy.common.http.base.IRequestHandler;
import cn.com.venvy.common.http.base.IResponse;
import cn.com.venvy.common.http.base.Request;
import cn.com.venvy.common.http.base.RequestConnectStatus;
import cn.com.venvy.common.utils.VenvyAesUtil;
import cn.com.venvy.common.utils.VenvyAsyncTaskUtil;
import cn.com.venvy.common.utils.VenvyDebug;
import cn.com.venvy.common.utils.VenvyGzipUtil;
import cn.com.venvy.common.utils.VenvyLog;
import cn.com.venvy.common.utils.VenvyUIUtil;

/**
 * Created by yanjiangbo on 2017/5/4.
 */

public class Report {

    private static final String REPORT_AES_KEY = "8lgK5fr5yatOfHio";
    private static final String REPORT_AES_IV = "lx7eZhVoBEnKXELF";
    private static final String REPORT_URL;
    //private static final String REPORT_URL = "http://192.168.2.234:8080/api/log";
    private static final String REPORT_SERVER_KEY = "info";
    private static final String KEY_ASYNC_TASK = "Report_report";
    private static IRequestConnect connect;

    private static VenvyDBController dbController;
    private static boolean hasInit = false;

    private static boolean enable = true;
    //最大缓存条数
    private static final int MAX_CACHE_NUM = 10;
    //数据库最大保存条数
    private static final int MAX_DB_CACHE_NUM = 500;
    //轮询时间间隔
    private static final int POLLING_TIME = 1000 * 60 * 5;

    static {
        if (VenvyDebug.getInstance().isDebug()) {
            REPORT_URL = "http://test-log.videojj.com/api/log";
        } else {
            REPORT_URL = "http://log.videojj.com/api/log";
        }
    }

    public enum ReportLevel {

        i(1, true),
        w(2, true),
        e(3, true),
        d(0, true);

        private int mSing = -1;
        private boolean mEnable;

        ReportLevel(int sing, boolean enable) {
            mSing = sing;
            mEnable = enable;
        }

        public void setEnable(boolean enable) {
            this.mEnable = enable;
        }

        public boolean getEnable() {
            return mEnable;
        }

        public int getValue() {
            return mSing;
        }

        public static ReportLevel getLevel(int value) {
            if (value == i.getValue()) {
                return i;
            }
            if (value == w.getValue()) {
                return w;
            }
            if (value == e.getValue()) {
                return e;
            }
            return d;
        }
    }

    public static void init(Platform platform) {
        if (!hasInit) {
            connect = RequestFactory.initConnect(RequestFactory.HttpPlugin.OK_HTTP, platform);
            startPolling();
            hasInit = !hasInit;
        } else {
            VenvyLog.w("Report is inited ");
        }
    }

    public static void setReportEnable(boolean able) {
        enable = able;
    }

    public static void report(@NonNull final ReportLevel level, @NonNull final String tag, @NonNull final String reportString) {
        if (!hasInit) {
            VenvyLog.w("Report has not be init");
            return;
        }
        if (!enable) {
            VenvyLog.w("Report has closed");
            return;
        }
        if (!level.getEnable()) {
            VenvyLog.w("the level is " + level.getValue() + " of Report has closed");
            return;
        }
        ReportInfo reportInfo = new ReportInfo();
        reportInfo.level = level;
        reportInfo.message = reportString;
        reportInfo.tag = tag;
        reportInfo.createTime = String.valueOf(System.currentTimeMillis());
        report(reportInfo);
    }

    public static void report(@NonNull Exception e) {

        if (!hasInit) {
            VenvyLog.w("Report has not be init");
            return;
        }
        if (!enable) {
            VenvyLog.w("Report has closed");
            return;
        }
        if (!ReportLevel.e.getEnable()) {
            VenvyLog.w("the level is 3 of Report has closed");
            return;
        }
        StringBuilder builder = new StringBuilder();
        StackTraceElement[] element = e.getStackTrace();
        if (element != null) {
            for (StackTraceElement i : element) {
                builder.append(i.toString());
                builder.append("\n");
            }
        }
        report(ReportLevel.e, "crash", builder.toString());
    }

    public static void report(@NonNull final ReportInfo reportInfo) {
        if (!hasInit) {
            VenvyLog.w("Report has not be init");
            return;
        }
        if (!enable) {
            VenvyLog.w("Report has closed");
            return;
        }
        if (!reportInfo.level.getEnable()) {
            VenvyLog.w("the level is " + reportInfo.level.getValue() + " of Report has closed");
            return;
        }
        if (TextUtils.isEmpty(reportInfo.tag) || TextUtils.isEmpty(reportInfo.message) || reportInfo.level == null) {
            VenvyLog.e("reportInfo is not vaild");
            return;
        }
        reportInfo.createTime = String.valueOf(System.currentTimeMillis());
        cacheReportInfo(reportInfo);
        long count = getTotalCacheNum();
        if (reportInfo.level == ReportLevel.e || count > MAX_CACHE_NUM) {
            startReport();
        }
    }

    private static void reportCache() {
        startReport();
    }

    private static void startReport() {

        VenvyAsyncTaskUtil.doAsyncTask(KEY_ASYNC_TASK, new VenvyAsyncTaskUtil.IDoAsyncTask<Void, Void>() {
            @Override
            public Void doAsyncTask(Void... strings) throws Exception {
                // 避免重复发送，正在发送的时候所有的请求只入到cache而等待上次投递结果
                if (connect.getConnectStatus() == RequestConnectStatus.ACTIVE) {
                    return null;
                }
                final List<ReportInfo> list = getReportInfoListAndDeleteDB();
                try {
                    HashMap<String, String> params = new HashMap<>();
                    String reportString = reportInfoListToString(list);
                    String gzipString = VenvyGzipUtil.compress(reportString);
                    if (gzipString == null) {
                        return null;
                    }
                    String signParams = VenvyAesUtil.encrypt(REPORT_AES_KEY, REPORT_AES_IV, gzipString);
                    params.put(REPORT_SERVER_KEY, signParams);
                    Request request = HttpRequest.put(REPORT_URL, params);
                    connect.connect(request, new IRequestHandler.RequestHandlerAdapter() {
                        @Override
                        public void requestFinish(Request request, IResponse response) {
                            if (!response.isSuccess()) {
                                cacheReportList(list);
                            }
                        }

                        @Override
                        public void requestError(Request request, Exception e) {
                            super.requestError(request, e);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }, null);
    }

    private static void cacheReportInfo(ReportInfo info) {
        try {
            getDbController().insert(DBConstants.TABLE_NAMES[DBConstants.TABLE_REPORT], DBConstants.ReportDB.COLUMNS, getReportDbContent(info), 1);
        } catch (DBException e) {
            VenvyLog.e("DBException : ", e);
        }
    }

    private static String[] getReportDbContent(ReportInfo info) {
        return new String[]{String.valueOf(info.id), String.valueOf(info.level.getValue()), info.createTime, info.tag, info.message};
    }

    private static void cacheReportList(List<ReportInfo> list) {
        try {
            if (list == null || list.size() == 0) {
                return;
            }
            ArrayList<String[]> dbContents = new ArrayList<>();
            for (ReportInfo info : list) {
                dbContents.add(getReportDbContent(info));
            }
            getDbController().insert(DBConstants.TABLE_NAMES[DBConstants.TABLE_REPORT], DBConstants.ReportDB.COLUMNS, dbContents, 1);
        } catch (DBException e) {
            VenvyLog.e("DBException : ", e);
        }
    }

    private static long getTotalCacheNum() {
        Cursor cursor = null;
        try {
            cursor = getDbController().queryCount(DBConstants.TABLE_NAMES[DBConstants.TABLE_REPORT]);
            if (cursor != null) {
                cursor.moveToFirst();
                long count = cursor.getLong(0);
                cursor.close();
                return count;
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return 0;
    }


    private static List<ReportInfo> getReportInfoListAndDeleteDB() {
        Cursor cursor = null;
        List<ReportInfo> list = new ArrayList<>();
        try {
            cursor = getDbController().queryAll(DBConstants.TABLE_NAMES[DBConstants.TABLE_REPORT]);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ReportInfo info = new ReportInfo();
                    info.id = cursor.getInt(DBConstants.ReportDB.REPORT_ID);
                    info.level = ReportLevel.getLevel(cursor.getInt(DBConstants.ReportDB.REPORT_LEVEL));
                    info.message = cursor.getString(DBConstants.ReportDB.REPORT_MESSAGE);
                    info.createTime = cursor.getString(DBConstants.ReportDB.REPORT_CREATE_TIME);
                    info.tag = cursor.getString(DBConstants.ReportDB.REPORT_TAG);
                    if (info.level.getEnable()) {
                        list.add(info);
                    }
                } while (cursor.moveToNext());
                cursor.close();
            }
            if (list.size() > MAX_DB_CACHE_NUM) {
                setReportEnable(false);
            }
            deleteCache();
            return list;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }


    private static void deleteCache() {
        try {
            getDbController().deleteAll(DBConstants.TABLE_NAMES[DBConstants.TABLE_REPORT]);
        } catch (DBException e) {
            e.printStackTrace();
            VenvyLog.e("DB delete error : ", e);
        }
    }


    private static String reportInfoListToString(List<ReportInfo> infoList) {
        if (infoList == null || infoList.size() == 0) {
            return "";
        }
        JSONArray jsonArray = new JSONArray();
        try {
            for (ReportInfo reportInfo : infoList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("level", reportInfo.level.getValue());
                jsonObject.put("tag", reportInfo.tag);
                jsonObject.put("message", reportInfo.message);
                jsonObject.put("create_time", reportInfo.createTime);
                jsonArray.put(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            VenvyLog.e("JSON error : ", e);
        }
        return jsonArray.toString();
    }

    /**
     * 每隔一段时间主动上抛一次数据
     */
    private static void startPolling() {
        if (!enable) {
            return;
        }
        Report.reportCache();
        VenvyUIUtil.runOnUIThreadDelay(new Runnable() {
            @Override
            public void run() {
                startPolling();
                VenvyLog.i("start poll Report");
            }
        }, POLLING_TIME);
    }

    private static VenvyDBController getDbController() {
        if (dbController == null || !dbController.isOpen()) {
            try {
                dbController = new VenvyDBController();
            } catch (DBException e) {
                //此处为了不造成死循环，不做投递操作
                VenvyLog.e("", e);
            }
        }
        return dbController;
    }

    public static void onDestroy() {
        if (getDbController() != null) {
            getDbController().onDestroy();
        }
    }
}
