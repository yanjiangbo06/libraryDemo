package cn.com.venvy.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

/**
 * Created by yanjiangbo on 2017/5/18.
 */

public class VenvyGzipUtil {

    private final static String CHARSET_NAME = "UTF-8";

    // 压缩
    public static String compress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(str.getBytes());
        gzip.close();
        return VenvyBase64.encode(out.toByteArray());
    }
}
