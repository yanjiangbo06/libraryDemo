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

import static cn.com.venvy.common.report.Report.ReportLevel.e;

/**
 * Created by yanjiangbo on 2017/5/4.
 */

public class ReportHelper {

    private static final String REPORT_AES_KEY = "8lgK5fr5yatOfHio";
    private static final String REPORT_AES_IV = "lx7eZhVoBEnKXELF";
    private static final String REPORT_SERVER_KEY = "info";
    private static final String KEY_ASYNC_TASK = "Report_report";
    //最大缓存条数
    private static final int MAX_CACHE_NUM = 10;
    //数据库最大保存条数
    private static final int MAX_DB_CACHE_NUM = 500;
    //轮询时间间隔
    private static final int POLLING_TIME = 1000 * 60 * 5;


    private IRequestConnect connect;

    private VenvyDBController dbController;

    private boolean enable = true;

    private Platform mPlatform;

    private String mCacheTableName;

    private String getReportUrl() {
        if (VenvyDebug.getInstance().isDebug()) {
            return "http://test-log.videojj.com/api/log";
        } else {
            return "http://log.videojj.com/api/log";
        }
    }

    public ReportHelper(Platform platform) {
        mPlatform = platform;
        if (TextUtils.equals(platform.getPlatformInfo().getBuId(), "OS")) {
            mCacheTableName = DBConstants.TABLE_NAMES[DBConstants.TABLE_OS_REPORT];
        } else {
            mCacheTableName = DBConstants.TABLE_NAMES[DBConstants.TABLE_LIVE_REPORT];
        }
        connect = platform.getRequestConnect();
        startPolling();
    }

    public void report(@NonNull final ReportInfo reportInfo) {
        reportInfo.createTime = String.valueOf(System.currentTimeMillis());
        cacheReportInfo(reportInfo);
        long count = getTotalCacheNum();
        if (reportInfo.level == e || count > MAX_CACHE_NUM) {
            startReport();
        }
    }

    private void reportCache() {
        startReport();
    }

    private void startReport() {

        VenvyAsyncTaskUtil.doAsyncTask(KEY_ASYNC_TASK, new VenvyAsyncTaskUtil.IDoAsyncTask<Void, Void>() {
            @Override
            public Void doAsyncTask(Void... strings) throws Exception {
                // 避免重复发送，正在发送的时候所有的请求只入到cache而等待上次投递结果
                if (connect == null) {
                    VenvyLog.e("connect is null,do you call init method?");
                    return null;
                }
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
                    Request request = HttpRequest.put(getReportUrl(), params);
                    connect.connect(request, new IRequestHandler.RequestHandlerAdapter() {
                        @Override
                        public void requestFinish(Request request, IResponse response) {
                            if (!response.isSuccess()) {
                                requestError(request, new Exception("http error"));
                            } else {
                                Object result = response.getData();
                                if (result != null && result instanceof String) {
                                    try {
                                        String value = (String) result;
                                        JSONObject jsonObject = new JSONObject(value);
                                        String logReport = jsonObject.optString("logReport");
                                        if (!TextUtils.isEmpty(logReport)) {
                                            if (!logReport.contains("info")) {
                                                Report.ReportLevel.i.setEnable(false);
                                            }
                                            if (!logReport.contains("warning")) {
                                                Report.ReportLevel.w.setEnable(false);
                                            }
                                            if (!logReport.contains("error")) {
                                                e.setEnable(false);
                                            }
                                        }
                                    } catch (JSONException e) {
                                        VenvyLog.e("JSON error : ", e);
                                    }
                                }
                            }
                        }

                        @Override
                        public void requestError(Request request, Exception e) {
                            super.requestError(request, e);
                            cacheReportList(list);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }, null);
    }

    private void cacheReportInfo(ReportInfo info) {
        try {
            getDbController().insert(mCacheTableName, DBConstants.ReportDB.COLUMNS, getReportDbContent(info), 1);
        } catch (DBException e) {
            VenvyLog.e("DBException : ", e);
        }
    }

    private String[] getReportDbContent(ReportInfo info) {
        return new String[]{String.valueOf(info.id), String.valueOf(info.level.getValue()), info.createTime, info.tag, info.message};
    }

    private void cacheReportList(List<ReportInfo> list) {
        try {
            if (list == null || list.size() == 0) {
                return;
            }
            ArrayList<String[]> dbContents = new ArrayList<>();
            for (ReportInfo info : list) {
                dbContents.add(getReportDbContent(info));
            }
            getDbController().insert(mCacheTableName, DBConstants.ReportDB.COLUMNS, dbContents, 1);
        } catch (DBException e) {
            VenvyLog.e("DBException : ", e);
        }
    }

    private long getTotalCacheNum() {
        Cursor cursor = null;
        try {
            cursor = getDbController().queryCount(mCacheTableName);
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


    private List<ReportInfo> getReportInfoListAndDeleteDB() {
        Cursor cursor = null;
        List<ReportInfo> list = new ArrayList<>();
        try {
            cursor = getDbController().queryAll(mCacheTableName);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ReportInfo info = new ReportInfo();
                    info.id = cursor.getInt(DBConstants.ReportDB.REPORT_ID);
                    info.level = Report.ReportLevel.getLevel(cursor.getInt(DBConstants.ReportDB.REPORT_LEVEL));
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

    public void setReportEnable(boolean able) {
        this.enable = able;
    }


    private void deleteCache() {
        try {
            getDbController().deleteAll(mCacheTableName);
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
    private void startPolling() {
        if (!enable) {
            return;
        }
        reportCache();
        VenvyUIUtil.runOnUIThreadDelay(new Runnable() {
            @Override
            public void run() {
                startPolling();
                VenvyLog.i("start poll Report");
            }
        }, POLLING_TIME);
    }

    private VenvyDBController getDbController() {
        if (dbController == null || !dbController.isOpen()) {
            try {
                dbController = new VenvyDBController(mPlatform.getContext());
            } catch (DBException e) {
                //此处为了不造成死循环，不做投递操作
                VenvyLog.e("", e);
            }
        }
        return dbController;
    }

    public void onDestroy() {
        if (getDbController() != null) {
            getDbController().onDestroy();
        }
    }
}
