package cn.com.venvy.common.report;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import cn.com.venvy.Platform;
import cn.com.venvy.common.utils.VenvyLog;

/**
 * Created by yanjiangbo on 2017/5/4.
 */

public class Report {


    private boolean enable = true;
    private ReportHelper mReportHelper;
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

    public void init(Platform platform) {
        mReportHelper = new ReportHelper(platform);
    }

    public void setReportEnable(boolean able) {
        mReportHelper.setReportEnable(able);
    }

    public void report(@NonNull final ReportLevel level, @NonNull final String tag, @NonNull final String reportString) {
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
        mReportHelper.report(reportInfo);
    }

    public void report(@NonNull Exception e) {
        report("crash", e);
    }

    public void report(String tag, @NonNull Exception e) {

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
        report(ReportLevel.e, tag, builder.toString());
    }

    public void report(@NonNull final ReportInfo reportInfo) {
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
        mReportHelper.report(reportInfo);

    }


    public void onDestroy() {
        mReportHelper.onDestroy();
    }
}
