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
import cn.com.venvy.common.utils.VenvyLog;

/**
 * Created by yanjiangbo on 2017/5/4.
 */

public class Report {

    protected static final String REPORT_AES_KEY = "";
    protected static final String REPORT_AES_IV = "";
    protected static final String REPORT_URL = "";
    protected static final String REPORT_SERVER_KEY = "info";

    protected static final int MAX_CACHE_NUM = 5;

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

    public static void report(ReportLevel level, String reportString) {
        ReportInfo reportInfo = new ReportInfo();
        reportInfo.leave = level;
        reportInfo.message = reportString;
        if (level == ReportLevel.e) {
            CrashReport.report(reportInfo);
        } else {
            TrackReport.report(reportInfo);
        }
    }

    public static void report(Exception e) {
        CrashReport.report(e);
    }

    protected static void startReport(final List<ReportInfo> list) {
        HashMap<String, String> params = new HashMap<>();
        params.put(REPORT_SERVER_KEY, VenvyAesUtil.encrypt(reportInfoListToString(list), REPORT_AES_KEY, REPORT_AES_IV));
        Request request = HttpRequest.put(REPORT_URL, params);
        RequestFactory.getRequestConnect().connect(request, new IRequestHandler() {
            @Override
            public void requestFinish(Request request, IResponse response) {
                if (!response.isSuccess()) {
                    requestError(request, new Exception());
                } else {
                    clearCache();
                }
            }

            @Override
            public void requestError(Request request, Exception e) {
                cacheReportInfo(list);
            }

            @Override
            public void startRequest(Request request) {

            }

            @Override
            public void requestProgress(Request request, int progress) {

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

    protected static void cacheReportInfo(List<ReportInfo> infoList) {
        if (infoList == null) {
            return;
        }
        if (infoList.size() == 0) {
            clearCache();
        }
        saveToFile(reportInfoListToString(infoList));
    }

    protected static List<ReportInfo> getReportInfoList() {
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

    protected static void clearCache() {

    }

    protected static String reportInfoListToString(List<ReportInfo> infoList) {
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
            JSONArray jsonArray = null;
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

}
