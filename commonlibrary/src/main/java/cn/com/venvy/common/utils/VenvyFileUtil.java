package cn.com.venvy.common.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/***
 * 文件管理类
 *
 * @author John
 *
 */
public class VenvyFileUtil {
    /***
     * 判断结构体是否存在本地
     *
     * @param mFileName
     *            (本地文件名)
     * @return true 本地存在 false 本地丢失
     *         Environment.getExternalStorageDirectory().getAbsolutePath()
     */
    public static boolean isExistFile(Context mContext, String mFileName) {
        File mFrameworkFile = new File(mContext.getCacheDir().getAbsolutePath()
                + "/"
                + mFileName);
        // 创建对象
        if (!mFrameworkFile.exists())
            return false;
        return true;
    }

    /***
     * 读取文件数据
     *
     * @param fileName 文件名
     * @return 文件数据
     * @throws IOException
     *             mContext.getCacheDir() .getAbsolutePath()
     */
    public static String readFile(Context mContext, String fileName)
            throws IOException {
        String res = "";
        try {
            File file = new File(mContext.getCacheDir().getAbsolutePath() + "/" + fileName);
            FileInputStream fin = new FileInputStream(file);
            if (fin != null) {
                InputStreamReader inputReader = new InputStreamReader(fin);
                BufferedReader buffReader = new BufferedReader(inputReader);
                String line;
                // 分行读取
                while ((line = buffReader.readLine()) != null) {
                    res += line;
                }
                fin.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
