package cn.com.venvy.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Arthur on 2017/4/27.
 */

public class VenvyPreferenceUtils {
    private static final String PREFERENCE_NAME = "venvylivevideo++";
    public static void putString(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREFERENCE_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(Context context, String key, String defValue) {
        return context.getSharedPreferences(PREFERENCE_NAME,
                Activity.MODE_PRIVATE).getString(key, defValue);
    }
}
