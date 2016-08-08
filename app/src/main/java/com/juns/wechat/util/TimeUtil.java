package com.juns.wechat.util;

/**
 * 时间日期 处理工具
 * create by 王宗文 on 2015/8/17
 *
 * @version 1.5
 */

import android.annotation.SuppressLint;
import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@SuppressLint("SimpleDateFormat")
public class TimeUtil {
    public static String format(long timeInMillis, boolean fullTime) {
        SimpleDateFormat sdf = null;
        if (fullTime) {
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } else {
            sdf = new SimpleDateFormat("yyyy-MM-dd");
        }

        String result = sdf.format(new Date(timeInMillis));
        return result;
    }

    public static String format(Date date, boolean fullTime) {
        SimpleDateFormat sdf = null;
        if (fullTime) {
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } else {
            sdf = new SimpleDateFormat("yyyy-MM-dd");
        }

        String result = sdf.format(date);
        return result;
    }

    /**
     * 将字符串类型的日期转换成毫秒
     *
     * @param date
     * @param fullTime
     * @return
     */
    public static Date format(String date, boolean fullTime) {
        SimpleDateFormat sdf = null;
        if (fullTime) {
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } else {
            sdf = new SimpleDateFormat("yyyy-MM-dd");
        }
        try {
            Date d = sdf.parse(date);
            return d;
        } catch (ParseException e) {
        }
        return null;
    }

    /**
     *
     * @param date 时间字符串
     * @param dateFormat 将要转换为的格式
     * @return
     */
    public static String format(String date, String dateFormat) {
        try {
            Date d = new Date(date);
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            return sdf.format(d);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 將long 轉換時間字符串 MM-dd HH:mm
     *
     * @param time
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String getAsyTime(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        Date date = new Date(time);
        sdf.format(date);
        return sdf.format(date);
    }

    /**
     * 根据生日获取年龄 格式必须符合
     *
     * @param s1 生日
     * @param s2 当天的年月日
     * @return
     */
    public static int getDifftime(String s1, String s2) {
        int year = Integer.parseInt(s2.split("-")[0]) - Integer.parseInt(s1.split("-")[0]);
        int month = Integer.parseInt(s2.split("-")[1]) - Integer.parseInt(s1.split("-")[1]);
        if (month < 0) {
            month += 12;
            year += -1;
        }
        return year;
    }

    /**
     * 获取当前时间并格式化 HH:mm:ss a
     * */
    @SuppressLint("SimpleDateFormat")
    public static String getFormatMessageTime(Context context) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss a");
        return format.format(new Date());

    }

    /**
     * 获取格式化时间 HH:mm:ss
     */
    @SuppressLint("SimpleDateFormat")
    public static String getFreeCallTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(date);
    }


    @SuppressLint("SimpleDateFormat")
    public static String getRecentTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return getStandardDate(format.format(date));
    }

    /**
     * 获取今年的纪念日时间
     *
     * @param month 几月份，从1开始
     * @param day 几号
     * @return calendar
     */
    public static Calendar getMemoryDayInThisYear(int month, int day) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    /**
     * 将long 转换 时间字符串 yyyy-MM-dd HH:mm:ss 時間為毫秒數
     *
     * @param timestamp
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String getStandardMTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // millisecond
        Date date = new Date(timestamp);
        sdf.format(date);
        return sdf.format(date);
    }

    /**
     * 获取今天的日期
     *
     * @return Calendar
     */
    public static Calendar getToday() {
        Calendar now = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);
        return now;
    }

    /**
     * 将年月日字符串“yyyy-mm-dd”切割成int数组
     *
     * @param date
     * @return
     */
    public static int[] splitDateString(String date) {
        String[] values = null;
        try {
            values = date.split("-");
        } catch (Exception e) {
            values = new String[]{"0", "0", "0"};
        }
        int[] retValues = new int[3];
        if ((values != null) && (values.length == 3)) {
            try {
                for (int i = 0; i < values.length; i++) {
                    retValues[i] = Integer.parseInt(values[i]);
                }
            } catch (Exception e) {
            }
        }
        return retValues;
    }

    /**
     * 将时间戳转为代表"距现在多久之前"的字符串
     *
     * @param timeStr 时间戳
     * @return
     */
    public static String getStandardDate(String timeStr) {

        StringBuffer sb = new StringBuffer();
        long t = format(timeStr,true).getTime();
       // long t = Long.parseLong(timeStr);
        long time = System.currentTimeMillis() - t;
        long mill = (long) Math.ceil(time / 1000);//秒前

        long minute = (long) Math.ceil(time / 60 / 1000.0f);// 分钟前

        long hour = (long) Math.ceil(time / 60 / 60 / 1000.0f);// 小时

        long day = (long) Math.ceil(time / 24 / 60 / 60 / 1000.0f);// 天前

        if (day - 1 > 0) {
            sb.append(day + "天");
        } else if (hour - 1 > 0) {
            if (hour >= 24) {
                sb.append("1天");
            } else {
                sb.append(hour + "小时");
            }
        } else if (minute - 1 > 0) {
            if (minute == 60) {
                sb.append("1小时");
            } else {
                sb.append(minute + "分钟");
            }
        } else if (mill - 1 > 0) {
            if (mill == 60) {
                sb.append("1分钟");
            } else {
                sb.append(mill + "秒");
            }
        } else {
            sb.append("刚刚");
        }
        if (!sb.toString().equals("刚刚")) {
            sb.append("前");
        }
        return sb.toString();
    }

}
