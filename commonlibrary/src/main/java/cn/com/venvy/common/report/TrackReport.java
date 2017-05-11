package cn.com.venvy.common.report;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanjiangbo on 2017/5/4.
 */

class TrackReport extends Report {

    public static void report(ReportInfo reportInfo) {
        List<ReportInfo> list = getReportInfoList();
        if (list == null) {
            list = new ArrayList<>();
        }
        if (reportInfo != null) {
            list.add(reportInfo);
        }
        if (list.size() >= MAX_CACHE_NUM) {
            startReport(list);
        } else {
            cacheReportInfo(list);
        }
    }

    public static void reportCache() {
        List<ReportInfo> list = getReportInfoList();
        startReport(list);
    }
}
