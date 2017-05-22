package cn.com.venvy.common.utils;

import android.os.SystemClock;

/**
 * Created by Arthur on 2017/4/28.
 */

public class VenvyDebug {
    private long[] mHits;
    private static VenvyDebug instance;
    private boolean venvyDebug = true;

    public static VenvyDebug getInstance() {
        if (null == instance) {
            instance = new VenvyDebug();
        }
        return instance;
    }

    public boolean isDebug() {
        return venvyDebug;
    }

    private VenvyDebug() {
        mHits = new long[5];
    }

    public void toggle() {
        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
        mHits[mHits.length - 1] = SystemClock.uptimeMillis();
        if (mHits[0] >= (SystemClock.uptimeMillis() - 1000)) {
            debugToggle();
        }
    }

    /**
     * 关闭log输出
     */
    private void disableLogging() {
        venvyDebug = false;
    }

    /**
     * 打开log输出
     */
    private void enableLogging() {
        venvyDebug = true;
    }

    /**
     * 开关debug
     */
    public void debugToggle() {
        venvyDebug = !venvyDebug;
        VenvyLog.i("Debug status has changed, isDebug == " + venvyDebug);
    }

    /**
     * 设置是否关闭debug
     *
     * @param writeDebugLogs true :打开，false:关闭
     */
    private void writeDebugLogs(boolean writeDebugLogs) {
        venvyDebug = writeDebugLogs;
    }


}
