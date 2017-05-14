package cn.com.venvy.common.report;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yanjiangbo on 2017/5/5.
 */

public class ReportInfo implements Parcelable {

    public int id;
    public Report.ReportLevel level;
    public String message;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.level.getValue());
        dest.writeString(this.message);
    }

    public ReportInfo() {

    }

    protected ReportInfo(Parcel in) {
        this.id = in.readInt();
        int leaveType = in.readInt();
        this.level = Report.ReportLevel.getLevel(leaveType);
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
