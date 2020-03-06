package com.hero.libhero.utils;

import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
//                if (hour > 99)
//                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);

                Log.e("secToTime", "timeStr=" + timeStr);
            }
        }

        return timeStr;
    }
    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;

    }


    public static String yuyue_time(String Pickup_time){
        String picktime = "";
        if (DateUtils.TimeToData(Pickup_time).substring(5, 10).equals(DateUtils.getNowTime().substring(5, 10))) {
            picktime = "今天" + DateUtils.TimeToData(Pickup_time).substring(11, 16);
        } else {
            picktime = "明天" + DateUtils.TimeToData(Pickup_time).substring(11, 16);
        }
        return picktime;
    }
    public static String yuji_time(String time_add) {
        String time = "";

        if (!TextUtils.isEmpty(time_add)) {
            time = DateUtils.TimeToData(time_add);
            if (time.substring(0, 4).equals(DateUtils.getNowTime().substring(0, 4))) {
                if (time.substring(0, 10).equals(DateUtils.getNowTime().substring(0, 10))) {
                    time = time.substring(11, 16);
                } else {
                    time = time.substring(5, 16);
                }
            } else {
                time = time.substring(0, 16);
            }

        }

        return time;
    }

    public static String TimeToDatas(String times) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date(Long.parseLong(times) * 1000L));

        return date;
    }

    public static String TimeToData(String times) {
        String date = "";
        if (!TextUtils.isEmpty(times)) {


            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (times.equals(""))
                times = "0";

            if (times.equals("*****")) {
                date = times;
            } else {
                date = sdf.format(new Date(Long.parseLong(times) * 1000L));
            }
        }
        return date;

    }
    public static String IsTodayYear(String times) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = "";
        if (TextUtils.isEmpty(times)) {
            date = "";
        } else {

            if (times.equals("*****")) {
                date = times;
            } else {
                date = sdf.format(new Date(Long.parseLong(times) * 1000L));
            }
        }

        String today=DateUtils.getTodayDate();
        if(date.substring(0,4).equals(today.substring(0,4))){//同一年
            date=date.substring(5,16);
        }else {
            date=date.substring(0,16);
        }

        System.out.println(date);
        return date;

    }
    public static long TimeToLong(String times) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt2 = null;
        try {
            dt2 = sdf.parse(times);
        } catch (ParseException e) {
            e.printStackTrace();
        }
//继续转换得到秒数的long型
        long lTime = dt2.getTime() / 1000;

        Log.e("lTime", "=" + lTime);

        return lTime;
    }

    public static String GetAddTime(String time_add) {

        try {
            if (TextUtils.isEmpty(time_add)) {
                return "";
            }


            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String date = "";
            if (!GlobalUtil.isNum(time_add)) {
                return "";
            } else {
                String time = DateUtils.TimeToData(time_add);
                if (time.substring(0, 10).equals(DateUtils.getNowTime().substring(0, 10))) {

                    date = time.substring(11, 16);

                    long now = System.currentTimeMillis() / 1000;
                    long time_add2 = Long.valueOf(time_add);
                    long dd = now - time_add2;

                    Log.e("now", now + "-" + time_add2 + "=" + dd);

                    if ((dd / 60 / 60) < 24) {//小于 24小时
                        if ((dd / 60) < 60) {//小于60分钟
                            if (dd < 60) {
                                date = dd + "秒前";
                            } else {
                                date = dd / 60 + "分钟前";
                            }
                        } else {
                            date = dd / 60 / 60 + "小时前";
                        }
                    }

                } else {
                    date = time.substring(5, 16);
                }

                return date;


            }
        } catch (NullPointerException e) {
            return "";
        }

    }

    /**
     * 计算两个日期型的时间相差多少时间
     *
     * @return
     */
    public static String getDateDistance(String date) {

        if (date == null || date.equals("")) {
            return "";
        }
        long timeLong = Long.parseLong(date) - System.currentTimeMillis() / 1000;

        timeLong = timeLong / 60 / 60 / 24;
        return timeLong + "天";
    }

    public static long getdaytime(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt2 = null;
        try {
            dt2 = sdf.parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return dt2.getTime();
    }


    /**
     * 返回unix时间戳 (1970年至今的秒数)
     *
     * @return
     */
    public static long getUnixStamp() {
        long time = System.currentTimeMillis() / 1000;
        Log.e("time", time + "");
        return time;
    }

    public static String getNowTime() {
        long ld = System.currentTimeMillis();                             //取得网站日期时间
        Date date = new Date(ld);

        //转换为标准时间对象
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String times = sdf.format(date);

        return times;
    }

    /**
     * 得到昨天的日期
     *
     * @return
     */
    public static String getYestoryDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String yestoday = sdf.format(calendar.getTime());
        return yestoday;
    }

    /**
     * 得到今天的日期
     *
     * @return
     */
    public static String getTodayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
        return date;
    }

    public static String getTomorrowDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, +1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String yestoday = sdf.format(calendar.getTime());
        return yestoday;
    }

    /**
     * 时间戳转化为时间格式
     *
     * @param timeStamp
     * @return
     */
    public static String timeStampToStr(long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(timeStamp * 1000);
        return date;
    }

    /**
     * 得到日期 yyyy-MM-dd
     *
     * @param timeStamp 时间戳
     * @return
     */
    public static String formatDate(long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(timeStamp * 1000);
        return date;
    }

    /**
     * 得到时间 HH:mm:ss
     *
     * @param timeStamp 时间戳
     * @return
     */
    public static String getTime(long timeStamp) {
        String time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        String date = sdf.format(timeStamp * 1000);
        String[] split = date.split("\\s");
        if (split.length > 1) {
            time = split[1];
        }
        return date;
    }

    /**
     * 将一个时间戳转换成提示性时间字符串，如刚刚，1秒前
     *
     * @param timeStamp
     * @return
     */
    public static String convertTimeToFormat(long timeStamp) {
        long curTime = System.currentTimeMillis() / (long) 1000;
        long time = curTime - timeStamp;

        if (time < 60 && time >= 0) {
            return "刚刚";
        } else if (time >= 60 && time < 3600) {
            return time / 60 + "分钟前";
        } else if (time >= 3600 && time < 3600 * 24) {
            return time / 3600 + "小时前";
        } else if (time >= 3600 * 24 && time < 3600 * 24 * 30) {
            if ((time / 3600 / 24) == 1) {
                return "昨天";
            }

            return time / 3600 / 24 + "天前";
        } else {
            return "刚刚";
        }
    }

    /**
     * 将一个时间戳转换成提示性时间字符串，(多少分钟)
     *
     * @param timeStamp
     * @return
     */
    public static String timeStampToFormat(long timeStamp) {
        long curTime = System.currentTimeMillis() / (long) 1000;
        long time = curTime - timeStamp;
        return time / 60 + "";
    }

}
