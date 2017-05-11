package cn.com.venvy.common.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by yanjiangbo on 2017/5/10.
 */

public class VenvyDataUtil {

    private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
    private static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd hh:mm:ss";
    /**
     * 用于生成文件
     */
    private static final String DEFAULT_FILE_PATTERN = "yyyy-MM-dd-HH-mm-ss";
    private static final double KB = 1024.0;
    private static final double MB = 1048576.0;
    private static final double GB = 1073741824.0;
    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat DATE_FORMAT_PART = new SimpleDateFormat(
            "HH:mm");

    public static String currentTimeString() {
        return DATE_FORMAT_PART.format(Calendar.getInstance().getTime());
    }

    public static char chatAt(String pinyin, int index) {
        if (pinyin != null && pinyin.length() > 0)
            return pinyin.charAt(index);
        return ' ';
    }

    /***
     * 获取时间天间隔
     *
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    @SuppressLint("SimpleDateFormat")
    public static int daysBetween(Date startDate, Date endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            startDate = sdf.parse(sdf.format(startDate));
            endDate = sdf.parse(sdf.format(endDate));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        long time1 = calendar.getTimeInMillis();

        calendar.setTime(endDate);
        long time2 = calendar.getTimeInMillis();

        long betweenDays = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(betweenDays));

    }

    /***
     * 时间间隔 分钟
     *
     * @param startDate
     * @param endDate
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static int minutesBetween(Date startDate, Date endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            startDate = sdf.parse(sdf.format(startDate));
            endDate = sdf.parse(sdf.format(endDate));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        long time1 = calendar.getTimeInMillis();

        calendar.setTime(endDate);
        long time2 = calendar.getTimeInMillis();

        long betweenDays = (time2 - time1) / (1000 * 60);
        return Integer.parseInt(String.valueOf(betweenDays));

    }

    /****
     * 获取时间天间隔
     *
     * @param smdate
     * @param bdate
     * @return
     * @throws ParseException
     * @throws ParseException
     */
    @SuppressLint("SimpleDateFormat")
    public static int stringDaysBetween(String smdate, String bdate)
            throws ParseException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }
}
