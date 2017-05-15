package cn.com.venvy.common.utils;

/**
 * Created by yanjiangbo on 2017/5/10.
 */

public class VenvyIDHelper {

    private static VenvyIDHelper sRequestIDHelper;

    private int _ID = Integer.MIN_VALUE;

    private int _reportId = Integer.MIN_VALUE;

    public synchronized static VenvyIDHelper getInstance() {
        if (sRequestIDHelper == null) {
            sRequestIDHelper = new VenvyIDHelper();
        }
        return sRequestIDHelper;
    }

    /**
     * 此处获取动态ID
     * @return
     */
    public int getId() {
        _ID = _ID++;
        if (_ID >= Integer.MAX_VALUE) {
            _ID = Integer.MIN_VALUE;
            getId();
        }
        return _ID;
    }

    /**
     * 此处获取动态report_ID
     * @return
     */
    public int getReportId() {
        _reportId = _reportId++;
        if (_reportId >= Integer.MAX_VALUE) {
            _reportId = Integer.MIN_VALUE;
            getReportId();
        }
        return _reportId;
    }
}
