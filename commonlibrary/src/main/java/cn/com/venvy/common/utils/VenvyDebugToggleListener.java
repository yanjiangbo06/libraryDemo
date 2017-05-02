package cn.com.venvy.common.utils;

import android.os.SystemClock;
import android.view.View;

/**
 * 连续点击多次打开或者关闭Log
 * Created by Arthur on 2017/4/28.
 */
public class VenvyDebugToggleListener implements View.OnClickListener{
    @Override
    public void onClick(View v) {
        Debug.getInstance().toggle();
    }
}
