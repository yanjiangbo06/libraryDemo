package cn.com.venvy.common.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import cn.com.venvy.common.permission.PermissionCheckHelper;

public class VenvyFileUtil {

    /***
     * 判断结构体是否存在本地
     *
     * @param mFileName
     */
    public static boolean isExistFile(Context mContext, String mFileName) {
        File mFrameworkFile = new File(mContext.getCacheDir().getAbsolutePath()
                + "/"
                + mFileName);
        return mFrameworkFile.exists();
    }

    /***
     * 读取文件数据
     *
     * @param fileName 文件名
     * @return 文件数据
     * @throws IOException
     *             mContext.getCacheDir() .getAbsolutePath()
     */
    public static String readFile(final Context context, @NonNull final String fileName, PermissionCheckHelper.PermissionCallbackListener callbackListener) {
        if (VenvyAPIUtil.isSupport(23) && !PermissionCheckHelper.isPermissionGranted(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            if (PermissionCheckHelper.instance().isRequesting()) {
                callbackListener.onPermissionCheckCallback(-1, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new int[]{PackageManager.PERMISSION_DENIED});
                return null;
            }
            PermissionCheckHelper.instance().requestPermissions(context, 201, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, new String[]{
                    "外部文件读取权限"
            }, callbackListener == null ? new PermissionCheckHelper.PermissionCallbackListener() {
                @Override
                public void onPermissionCheckCallback(int requestCode, String[] permissions, int[] grantResults) {

                }
            } : callbackListener);
            return null;
        }
        return readFormFile(context, fileName);
    }

    public static void saveFile(final Context context, @NonNull final String fileName, final String content, PermissionCheckHelper.PermissionCallbackListener callbackListener) {
        if (VenvyAPIUtil.isSupport(23) && PermissionCheckHelper.isPermissionGranted(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            if (PermissionCheckHelper.instance().isRequesting()) {
                callbackListener.onPermissionCheckCallback(-1, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new int[]{PackageManager.PERMISSION_DENIED});
                return;
            }
            PermissionCheckHelper.instance().requestPermissions(context, 202, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, new String[]{
                    "外部文件写入权限"
            }, callbackListener == null ? new PermissionCheckHelper.PermissionCallbackListener() {
                @Override
                public void onPermissionCheckCallback(int requestCode, String[] permissions, int[] grantResults) {

                }
            } : callbackListener);
        } else {
            writeToFile(context, fileName, content);
        }
    }

    public static void deleteFile(final Context context, @NonNull final String fileName, PermissionCheckHelper.PermissionCallbackListener callbackListener) {
        if (VenvyAPIUtil.isSupport(23) && PermissionCheckHelper.isPermissionGranted(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            if (PermissionCheckHelper.instance().isRequesting()) {
                callbackListener.onPermissionCheckCallback(-1, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new int[]{PackageManager.PERMISSION_DENIED});
                return;
            }
            PermissionCheckHelper.instance().requestPermissions(context, 202, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, new String[]{
                    "外部文件写入权限"
            }, callbackListener == null ? new PermissionCheckHelper.PermissionCallbackListener() {
                @Override
                public void onPermissionCheckCallback(int requestCode, String[] permissions, int[] grantResults) {

                }
            } : callbackListener);
        } else {
            deleteFile(context, fileName);
        }
    }


    private static String readFormFile(Context context, @NonNull String fileName) {
        StringBuilder res = new StringBuilder();
        FileInputStream fin = null;
        if (context != null) {
            fileName = context.getCacheDir().getAbsolutePath() + "/" + fileName;
        } else {
            fileName = Environment.getExternalStorageDirectory().getPath() + "/venvy/" + fileName;
        }
        try {
            File file = new File(fileName);
            if (file.exists()) {
                fin = new FileInputStream(file);
                InputStreamReader inputReader = new InputStreamReader(fin);
                BufferedReader buffReader = new BufferedReader(inputReader);
                String line;
                // 分行读取
                while ((line = buffReader.readLine()) != null) {
                    res.append(line);
                }
                fin.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fin != null) {
                    fin.close();
                }
            } catch (Exception e) {

            }
        }
        return res.toString();
    }

    private static void writeToFile(Context context, @NonNull String fileName, String content) {
        if (context != null) {
            fileName = context.getCacheDir().getAbsolutePath() + "/" + fileName;
        } else {
            fileName = Environment.getExternalStorageDirectory().getPath() + "/venvy/" + fileName;
        }
        File file = new File(fileName);
        if (!file.exists()) {
            file.mkdir();
            return;
        }
        if (!file.exists()) {
            VenvyLog.d("Error: file is not exists!");
            return;
        }
        OutputStreamWriter osw = null;
        try {
            osw = new OutputStreamWriter(new FileOutputStream(fileName), "utf-8");
            try {
                osw.write(content);
                osw.flush();
                osw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } finally {
            try {
                if (osw != null) {
                    osw.close();
                }
            } catch (Exception e) {

            }
        }
    }

    private static void deleteFile(Context context, @NonNull String fileName) {
        if (context != null) {
            fileName = context.getCacheDir().getAbsolutePath() + "/" + fileName;
        } else {
            fileName = Environment.getExternalStorageDirectory().getPath() + "/venvy/" + fileName;
        }
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
    }
}
