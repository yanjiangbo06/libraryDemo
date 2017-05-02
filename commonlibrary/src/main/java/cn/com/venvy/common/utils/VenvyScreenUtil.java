package cn.com.venvy.common.utils;
import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.WindowManager;

public class VenvyScreenUtil {
	/**
	 * 返回当前屏幕是否为竖屏。
	 * 
	 * @param context
	 * @return 当且仅当当前屏幕为竖屏时返回true,否则返回false。
	 */
	public static boolean isScreenOriatationPortrait(@NonNull  Context context) {
		return context.getResources().getConfiguration().orientation ==
				Configuration.ORIENTATION_PORTRAIT;
	}

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(@NonNull Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
	/**
	 * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
	 */
	public static int dip2px(@NonNull Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
	/***
	 * 获取屏幕高度
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(@NonNull Context context) {
		Display display = ((WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		return display.getHeight();
	}
	/**
	 * 获取屏幕宽度
	 */
	public static int getScreenWidth(@NonNull Context context) {
		Display display = ((WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		return display.getWidth();
	}
}
