package cn.com.venvy.common.utils;

import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * 判断是否是网站
 * @author John
 *
 */
public class VenvyStringUtil {

    public static final String EMPTY = "";

    /**
     * 校验是否是手机号
     *
     * @param phone
     * @return
     */
    public static boolean isPhone(String phone) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[0-9])|(18[0-9])|17[0-9])\\d{8}$");
        Matcher m = p.matcher(phone);
        return m.matches();
    }

    /**
     * 校验是否是邮箱
     *
     * @param
     * @return
     */
    public static boolean isEmail(String email) {
        Pattern p = Pattern.compile("^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)$");
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /***
     *
     * @param num 当前
     * @param total 总数
     * @param scale 保留几位
     * @return
     */
    public static String accuracy(double num, double total, int scale) {
        DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
        //可以设置精确几位小数  
        df.setMaximumFractionDigits(scale);
        //模式 例如四舍五入  
        df.setRoundingMode(RoundingMode.HALF_UP);
        double accuracy_num = num / total * 100;
        return df.format(accuracy_num) + "%";
    }


    /**
     * 把字符转转化为md5加密
     *
     * @param source
     * @return
     */
    public static String convertMD5(String source) {
        byte[] buf = source.getBytes();
        StringBuilder md5Str = new StringBuilder();
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(buf);
            byte[] tmp = md5.digest();
            for (byte b : tmp) {
                md5Str.append(Integer.toHexString(b & 0xff));
            }
        } catch (NoSuchAlgorithmException e) {
            return "";
        }

        return md5Str.toString();
    }

    // 比较时间的先后
    public static boolean greater(Date d1, Date d2) {
        // 如果compareTo返回0，表示两个日期相等，返回小于0的值，表示d1在d2之前，大于0表示d1在d2之后

        return d1.compareTo(d2) > 0;
    }

    // 切割过长的红包标题
    public static String spliteWalletTitle(String title) {

        if (title.contains("\n")) return title;
        StringBuilder stringBuilder = new StringBuilder();
        int length = title.length();
        if (length > 4) {
            length /= 2;
            stringBuilder.append(title.substring(0, length))
                    .append("\n")
                    .append(title.substring(length));
        } else {
            return title;
        }

        return stringBuilder.toString();
    }
}
