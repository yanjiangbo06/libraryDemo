package cn.com.venvy.common.report;

import java.util.ArrayList;
import java.util.List;

import cn.com.venvy.common.utils.VenvyLog;

/**
 * Created by yanjiangbo on 2017/5/4.
 */

class CrashReport extends Report {

    public static void report(final ReportInfo reportInfo) {
        if (reportInfo == null) {
            return;
        }
        List<ReportInfo> reportInfoList = getReportInfoList();
        if (reportInfoList == null) {
            reportInfoList = new ArrayList<>();
        }
        reportInfoList.add(reportInfo);
        startReport(reportInfoList);
    }
}
