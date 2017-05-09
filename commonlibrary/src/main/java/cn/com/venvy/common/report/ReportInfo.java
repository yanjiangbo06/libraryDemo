package cn.com.venvy.common.report;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yanjiangbo on 2017/5/5.
 */

public class ReportInfo implements Parcelable {

    public Report.ReportLevel leave;
    public String message;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (this.leave == Report.ReportLevel.i) {
            dest.writeInt(1);
        } else if (this.leave == Report.ReportLevel.w) {
            dest.writeInt(2);
        } else if (this.leave == Report.ReportLevel.e) {
            dest.writeInt(3);
        } else {
            dest.writeInt(0);
        }
        dest.writeString(this.message);
    }

    public ReportInfo() {

    }

    protected ReportInfo(Parcel in) {
        int leaveType = in.readInt();
        switch (leaveType) {
            case 0:
                this.leave = Report.ReportLevel.defaultValue;
                break;
            case 1:
                this.leave = Report.ReportLevel.i;
                break;
            case 2:
                this.leave = Report.ReportLevel.w;
                break;
            case 3:
                this.leave = Report.ReportLevel.e;
                break;
        }
        this.message = in.readString();
    }

    public static final Parcelable.Creator<ReportInfo> CREATOR = new Parcelable.Creator<ReportInfo>() {
        @Override
        public ReportInfo createFromParcel(Parcel source) {
            return new ReportInfo(source);
        }

        @Override
        public ReportInfo[] newArray(int size) {
            return new ReportInfo[size];
        }
    };
}
