package cn.com.venvy.common.report;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanjiangbo on 2017/5/4.
 */

class CrashReport extends Report {

    public static void report(final ReportInfo reportInfo) {
        List<ReportInfo> reportInfoList = getReportInfoList();
        if (reportInfoList == null) {
            reportInfoList = new ArrayList<>();
        }
        reportInfoList.add(reportInfo);
        startReport(reportInfoList);
    }

    public static void report(Exception e) {
        ReportInfo info = new ReportInfo();
        info.leave = ReportLevel.e;
        info.message = e.fillInStackTrace().getMessage();
        report(info);
    }
}
