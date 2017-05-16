package cn.com.venvy.common.report;

import android.database.Cursor;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.com.venvy.common.Exception.DBException;
import cn.com.venvy.common.db.DBConstants;
import cn.com.venvy.common.db.VenvyDBController;
import cn.com.venvy.common.http.HttpRequest;
import cn.com.venvy.common.http.RequestFactory;
import cn.com.venvy.common.http.base.IRequestConnect;
import cn.com.venvy.common.http.base.IRequestHandler;
import cn.com.venvy.common.http.base.IResponse;
import cn.com.venvy.common.http.base.Request;
import cn.com.venvy.common.http.base.RequestConnectStatus;
import cn.com.venvy.common.utils.VenvyAesUtil;
import cn.com.venvy.common.utils.VenvyAsyncTaskUtil;
import cn.com.venvy.common.utils.VenvyIDHelper;
import cn.com.venvy.common.utils.VenvyLog;
import cn.com.venvy.common.utils.VenvyUIUtil;

/**
 * Created by yanjiangbo on 2017/5/4.
 */

public class Report {

    private static final String REPORT_AES_KEY = "8lgK5fr5yatOfHio";
    private static final String REPORT_AES_IV = "lx7eZhVoBEnKXELF";
    private static final String REPORT_URL = "http://test-log.videojj.com/api/log";
    private static final String REPORT_SERVER_KEY = "info";
    private static final String KEY_ASYNC_TASK = "Report_report";

    private static final IRequestConnect connect = RequestFactory.initConnect(RequestFactory.HttpPlugin.OK_HTTP);

    private static VenvyDBController dbController;

    //最大缓存条数
    static final int MAX_CACHE_NUM = 5;

    //轮询时间间隔
    private static final int POLLING_TIME = 1000 * 60 * 5;

    public enum ReportLevel {

        i(1),
        w(2),
        e(3),
        d(0);

        private int mSing = -1;

        ReportLevel(int sing) {
            mSing = sing;
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

    public static void init() {
        startPolling();
    }

    public static void report(final ReportLevel level, final String reportString) {
        if (level == null || TextUtils.isEmpty(reportString)) {
            return;
        }
        VenvyAsyncTaskUtil.doAsyncTask(KEY_ASYNC_TASK, new VenvyAsyncTaskUtil.IDoAsyncTask<Void, Void>() {
            @Override
            public Void doAsyncTask(Void... strings) throws Exception {
                ReportInfo reportInfo = new ReportInfo();
                reportInfo.level = level;
                reportInfo.message = reportString;
                reportInfo.createTime = System.currentTimeMillis() + "|" + VenvyIDHelper.getInstance().getReportId();
                if (level == ReportLevel.e) {
                    CrashReport.report(reportInfo);
                } else {
                    TrackReport.report(reportInfo);
                }
                return null;
            }
        }, null);
    }

    public static void report(Exception e) {

        if (e == null) {
            VenvyLog.e("nullPointException for Report Exception value");
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
        report(ReportLevel.e, builder.toString());
    }

    private static void reportCache() {
        if (getDbController() != null) {
            List<ReportInfo> list = getReportInfoList();
            startReport(list);
        }
    }

    static void startReport(final List<ReportInfo> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        cacheReportInfo(list);
        // 避免重复发送，正在发送的时候所有的请求只入到cache而等待上次投递结果
        if (connect.getConnectStatus() == RequestConnectStatus.ACTIVE) {
            return;
        }
        try {
            HashMap<String, String> params = new HashMap<>();
            String signParams = VenvyAesUtil.encrypt(REPORT_AES_KEY, REPORT_AES_IV, reportInfoListToString(list));
            params.put(REPORT_SERVER_KEY, signParams);
            Request request = HttpRequest.put(REPORT_URL, params);
            connect.connect(request, new IRequestHandler.RequestHandlerAdapter() {
                @Override
                public void requestFinish(Request request, IResponse response) {
                    if (response.isSuccess()) {
                        deleteCache(list);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void cacheReportInfo(List<ReportInfo> infoList) {
        Cursor cursor = null;
        try {
            for (ReportInfo reportInfo : infoList) {
                if (getDbController() != null) {
                    if (reportInfo.id != 0) {
                        cursor = getDbController().query(DBConstants.TABLE_NAMES[DBConstants.TABLE_REPORT], DBConstants.ReportDB.COLUMNS[DBConstants.ReportDB.REPORT_ID], new String[]{String.valueOf(reportInfo.id)});
                        if (cursor != null) {
                            continue;
                        }
                    }
                    getDbController().insert(DBConstants.TABLE_NAMES[DBConstants.TABLE_REPORT], DBConstants.ReportDB.COLUMNS, new String[]{String.valueOf(reportInfo.id), String.valueOf(reportInfo.level.getValue()), reportInfo.createTime, reportInfo.message}, 1);
                    cursor = getDbController().query(DBConstants.TABLE_NAMES[DBConstants.TABLE_REPORT], DBConstants.ReportDB.COLUMNS[DBConstants.ReportDB.REPORT_CREATE_TIME], new String[]{reportInfo.createTime});
                    if (cursor != null && cursor.getCount() > 0) {
                        cursor.moveToLast();
                        int _id = cursor.getInt(DBConstants.ReportDB.REPORT_ID);
                        reportInfo.id = _id;
                    }
                }
            }
        } catch (DBException e) {
            e.printStackTrace();
            VenvyLog.e("", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    static List<ReportInfo> getReportInfoList() {

        Cursor cursor = null;
        List<ReportInfo> list = new ArrayList<>();
        if (getDbController() != null) {
            cursor = getDbController().query(DBConstants.TABLE_NAMES[DBConstants.TABLE_REPORT]);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    ReportInfo info = new ReportInfo();
                    info.id = cursor.getInt(DBConstants.ReportDB.REPORT_ID);
                    info.level = ReportLevel.getLevel(cursor.getInt(DBConstants.ReportDB.REPORT_LEVEL));
                    info.message = cursor.getColumnName(DBConstants.ReportDB.REPORT_MESSAGE);
                    info.createTime = cursor.getColumnName(DBConstants.ReportDB.REPORT_CREATE_TIME);
                    list.add(info);
                }
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    private static void deleteCache(List<ReportInfo> infoList) {
        try {
            if (infoList == null || infoList.size() == 0) {
                return;
            }
            if (getDbController() != null) {
                for (int i = 0; i < infoList.size(); i++) {
                    getDbController().delete(DBConstants.TABLE_NAMES[DBConstants.TABLE_REPORT], DBConstants.ReportDB.COLUMNS[DBConstants.ReportDB.REPORT_ID], String.valueOf(infoList.get(i).id));
                }
            }
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
                jsonObject.put("message", reportInfo.message);
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
        Report.reportCache();
        VenvyUIUtil.runOnUIThreadDelay(new Runnable() {
            @Override
            public void run() {
                startPolling();
            }
        }, POLLING_TIME);
    }

    private static VenvyDBController getDbController() {
        if (dbController == null) {
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
