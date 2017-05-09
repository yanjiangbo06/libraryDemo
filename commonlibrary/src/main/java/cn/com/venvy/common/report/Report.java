package cn.com.venvy.common.report;

import android.content.Context;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import cn.com.venvy.common.http.RequestFactory;
import cn.com.venvy.common.http.base.IRequestConnect;
import cn.com.venvy.common.http.base.IRequestHandler;
import cn.com.venvy.common.http.base.Request;

/**
 * Created by yanjiangbo on 2017/5/4.
 */

public abstract class Report {

    protected static final String REPORT_AES_KEY = "";
    protected static final String REPORT_AES_IV = "";
    protected static final String REPORT_URL = "";
    protected static final String REPORT_SERVER_KEY = "info";

    public enum ReportLevel {
        i,
        w,
        e,
        defaultValue
    }

    private IRequestConnect connect;

    public Report() {
        RequestFactory factory = new RequestFactory();
        connect = factory.createConnect(RequestFactory.HttpPlugin.OK_HTTP);
    }

    public void startReport(Request request, IRequestHandler handler) {
        connect.connect(request, handler);
    }

    protected void cacheReportInfo(ReportInfo info) {

    }

    protected void cacheReportInfo(String s) {
        if (TextUtils.isEmpty(s)) {
            return;
        }
        String cacheString = getCacheReportString();
        try {
            if (!TextUtils.isEmpty(cacheString)) {
                JSONArray jsonArray = new JSONArray(s);
            }
        } catch (JSONException e) {

        }

    }

    protected void cacheReportInfo(List<ReportInfo> infoList) {

    }

    protected List<ReportInfo> getReportInfo() {
        return null;

    }

    private String getCacheReportString() {

        return null;
    }

    abstract void report(String reportString);
}
