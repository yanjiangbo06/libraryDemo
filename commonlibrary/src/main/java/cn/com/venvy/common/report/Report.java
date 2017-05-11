package cn.com.venvy.common.report;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.com.venvy.common.http.HttpRequest;
import cn.com.venvy.common.http.RequestFactory;
import cn.com.venvy.common.http.base.IRequestHandler;
import cn.com.venvy.common.http.base.IResponse;
import cn.com.venvy.common.http.base.Request;
import cn.com.venvy.common.utils.VenvyAesUtil;
import cn.com.venvy.common.utils.VenvyAsyncTaskUtil;
import cn.com.venvy.common.utils.VenvyBase64;
import cn.com.venvy.common.utils.VenvyLog;
import cn.com.venvy.common.utils.VenvyUIUtil;

/**
 * Created by yanjiangbo on 2017/5/4.
 */

public class Report {

    private static final String REPORT_AES_KEY = "";
    private static final String REPORT_AES_IV = "";
    private static final String REPORT_URL = "";
    private static final String REPORT_SERVER_KEY = "info";
    private static final String KEY_ASYNC_TASK = "Report_report";

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
                reportInfo.leave = level;
                reportInfo.message = reportString;
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
        CrashReport.report(e);
    }

    private static void reportCache() {
        TrackReport.reportCache();
    }

    static void startReport(final List<ReportInfo> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        String signParams = VenvyAesUtil.encrypt(reportInfoListToString(list), REPORT_AES_KEY, REPORT_AES_IV);
        String baseRequestSign = VenvyBase64.encode(signParams.getBytes());
        params.put(REPORT_SERVER_KEY, baseRequestSign);
        Request request = HttpRequest.put(REPORT_URL, params);
        RequestFactory.initConnect(RequestFactory.HttpPlugin.OK_HTTP).connect(request, new IRequestHandler.RequestHandlerAdapter() {
            @Override
            public void requestFinish(Request request, IResponse response) {
                if (!response.isSuccess()) {
                    requestError(request, new Exception());
                } else {
                    clearCache();
                }
            }
        });
    }

    protected static void cacheReportInfo(ReportInfo info) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("leave", info.leave);
            jsonObject.put("message", info.message);
            cacheReportInfo(jsonObject);
        } catch (JSONException e) {
            VenvyLog.e("", e);
            //忽略此处的异常，如果此处添加report，可能会引起死循环
        }
    }

    static void cacheReportInfo(List<ReportInfo> infoList) {
        if (infoList == null || infoList.size() == 0) {
            return;
        }
        saveToFile(reportInfoListToString(infoList));
    }

    static List<ReportInfo> getReportInfoList() {
        String cacheString = getByFile();
        if (TextUtils.isEmpty(cacheString)) {
            return null;
        }
        List<ReportInfo> list = new ArrayList<>();
        JSONArray jsonArray = JSONArray.parseArray(cacheString);
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ReportInfo reportInfo = new ReportInfo();
                reportInfo.message = jsonObject.getString("message");
                reportInfo.leave = ReportLevel.getLevel(jsonObject.getIntValue("leave"));
                list.add(reportInfo);
            }
        }
        return list;
    }

    private static void clearCache() {

    }

    private static String reportInfoListToString(List<ReportInfo> infoList) {
        if (infoList == null || infoList.size() == 0) {
            return null;
        }
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < infoList.size(); i++) {
            ReportInfo reportInfo = infoList.get(i);
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("leave", reportInfo.leave.getValue());
                jsonObject.put("message", reportInfo.message);
                jsonArray.add(jsonObject);
            } catch (JSONException e) {
                VenvyLog.e("", e);
                //忽略此处的异常，如果此处添加report，可能会引起死循环
            }
        }
        return jsonArray.toJSONString();
    }

    private static void cacheReportInfo(JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        String cacheString = getByFile();
        try {
            JSONArray jsonArray;
            if (!TextUtils.isEmpty(cacheString)) {
                jsonArray = JSONArray.parseArray(cacheString);
            } else {
                jsonArray = new JSONArray();
            }
            jsonArray.add(jsonObject);
            saveToFile(jsonArray.toString());
        } catch (JSONException e) {
            VenvyLog.e("", e);
        }
    }

    private static void saveToFile(String s) {
        if (TextUtils.isEmpty(s)) {
            return;
        }

    }

    private static String getByFile() {
        return null;
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
}
