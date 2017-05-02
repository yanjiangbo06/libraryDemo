package cn.com.venvy.common.utils;

import android.os.SystemClock;

/**
 * Created by Arthur on 2017/4/28.
 */

public class Debug {
    private long[] mHits;
    private static Debug instance;
    public static volatile boolean venvyDebug = true;

    public static Debug getInstance(){
        if(null == instance){
            instance = new Debug();
        }
        return instance;
    }
    private  Debug(){
        mHits = new long[5];
    }

    public void toggle(){
        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
        mHits[mHits.length - 1] = SystemClock.uptimeMillis();
        if (mHits[0] >= (SystemClock.uptimeMillis() - 1000)) {
            debugToggle();
        }
    }
    /**
     * 关闭log输出
     */
    public static void disableLogging() {
        venvyDebug = false;
    }

    /**
     * 打开log输出
     */
    public static void enableLogging(){
        venvyDebug = true;
    }

    /**
     * 开关debug
     */
    public static void debugToggle(){
        venvyDebug = !venvyDebug;
    }

    /**
     * 设置是否关闭debug
     * @param writeDebugLogs true :打开，false:关闭
     */
    public static void writeDebugLogs(boolean writeDebugLogs){
        venvyDebug = writeDebugLogs;
    }


}
