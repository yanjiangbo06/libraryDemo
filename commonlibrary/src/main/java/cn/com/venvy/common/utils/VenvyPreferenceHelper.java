package cn.com.venvy.common.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Arthur on 2017/4/27.
 */

public class VenvyPreferenceHelper {
    private static final String PREFERENCE_NAME = "venvylivevideo++";

    public static void putString(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        SharedPreferencesCompat.apply(editor);
    }


    public static String getString(Context context, String key, String defValue) {
        return context.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE).getString(key, defValue);
    }

    /**
     * 保存数据到本地
     * @param context
     * @param key
     * @param data
     */
    public static void putData(Context context, @NonNull String key, Object data){
        String type = data.getClass().getSimpleName();
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if ("Integer".equals(type)){
            editor.putInt(key, (Integer)data);
        }else if ("Boolean".equals(type)){
            editor.putBoolean(key, (Boolean)data);
        }else if ("String".equals(type)){
            editor.putString(key, (String)data);
        }else if ("Float".equals(type)){
            editor.putFloat(key, (Float)data);
        }else if ("Long".equals(type)){
            editor.putLong(key, (Long)data);
        }
        SharedPreferencesCompat.apply(editor);
    }
    /**
     * 根据默认值
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getData(Context context,@NonNull String key, Object defaultObject){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE);

        String type = defaultObject.getClass().getSimpleName();

        if ("Integer".equals(type)){
            return sharedPreferences.getInt(key, (Integer) defaultObject);
        }else if ("Boolean".equals(type)){
            return sharedPreferences.getBoolean(key, (Boolean) defaultObject);
        }else if ("String".equals(type)){
            return sharedPreferences.getString(key, (String) defaultObject);
        }else if ("Float".equals(type)){
            return sharedPreferences.getFloat(key, (Float) defaultObject);
        }else if ("Long".equals(type)){
            return sharedPreferences.getLong(key, (Long) defaultObject);
        }
        return null;
    }

    /**
     * 移除某个key值已经对应的值
     * @param context
     * @param key
     */
    public static void remove(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     * @param context
     */
    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     * @param context
     * @param key
     * @return
     */
    public static boolean contains(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        return sp.contains(key);
    }


    private static class SharedPreferencesCompat{
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         * @return
         */
        @SuppressWarnings({ "unchecked", "rawtypes" })
        private static Method findApplyMethod(){
            try
            {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e){
            }
            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor)
        {
            try
            {
                if (sApplyMethod != null)
                {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e){
            } catch (IllegalAccessException e){
            } catch (InvocationTargetException e){
            }
            editor.commit();
        }
    }
}
