package cn.com.venvy.common.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.com.venvy.common.http.HttpRequest;
import cn.com.venvy.common.http.base.IRequestHandler;
import cn.com.venvy.common.http.base.IResponse;
import cn.com.venvy.common.http.base.Request;
import cn.com.venvy.common.utils.VenvyAesUtil;

/**
 * Created by yanjiangbo on 2017/5/4.
 */

public class CrashReport extends Report {

    @Override
    public void report(String reportString) {
        List<ReportInfo> reportInfoList = getReportInfo();
        if (reportInfoList == null) {
            reportInfoList = new ArrayList<>();
        }
        final ReportInfo info = new ReportInfo();
        info.leave = ReportLevel.e;
        info.message = reportString;
        reportInfoList.add(info);
        HashMap<String, String> params = new HashMap<>();
        params.put(REPORT_SERVER_KEY, VenvyAesUtil.encrypt(reportString, REPORT_AES_KEY, REPORT_AES_IV));
        Request request = HttpRequest.put(REPORT_URL, params);
        startReport(request, new IRequestHandler() {
            @Override
            public void requestFinish(Request request, IResponse response) {
                if (!response.isSuccess()) {
                    requestError(request, new Exception());
                }
            }

            @Override
            public void requestError(Request request, Exception e) {
                cacheReportInfo(info);
            }

            @Override
            public void startRequest(Request request) {

            }

            @Override
            public void requestProgress(Request request, int progress) {

            }
        });
    }

    public void report(Exception e) {
        report(e.fillInStackTrace().getMessage());
    }
}
