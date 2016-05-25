/*
 * 文件名: DateUtil.java
 * 版    权： Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 根据时间格式，获取时间字符串
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.annotation.SuppressLint;

/**
 * 根据时间格式，获取时间字符串<BR>
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-9]
 */
@SuppressLint("SimpleDateFormat")
public class DateUtil
{
    
    /**
     * 日期Format格式"yyyy-MM-dd HH:mm"
     */
    public static final SimpleDateFormat TIME_FORMAT_BASE = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm");
    
    /**
     * 日期格式是“年-月-日 时-分-秒”
     */
    public static final String FORMAT_YYYYMMDD_HHMMSS = "yyyy-MM-dd_HH:mm:ss";
    
    /**
     * 时间转换为 "yyyy-MM-dd HH:mm:ss" 格式<BR>
     * @param date
     *            date
     * @return 日期的时间串
     */
    public static String getFormatTimeStringForFriendManager(Date date)
    {
        return null == date ? TIME_FORMAT_BASE.format(new Date())
                : TIME_FORMAT_BASE.format(date);
    }
    
    /**
     * "yyyy-MM-dd HH:mm:ss"时间串转Date<BR>
     * @param friendManagerTimeString
     *            找朋友小助手时间串
     * @return 日期对象
     * @throws ParseException
     *             解析发生异常
     */
    public static Date getDateFromFriendManageTimeString(
            String friendManagerTimeString) throws ParseException
    {
        return TIME_FORMAT_BASE.parse(friendManagerTimeString);
    }
    
    /**
     * 获取时间字符串精确到毫秒<BR>
     * 获取形式如：HHmmssSSS
     * @return String 时分秒毫秒
     */
    public static String getMillisecond()
    {
        SimpleDateFormat timeStampMilliSecondDF = new SimpleDateFormat(
                "HHmmssSSS");
        return timeStampMilliSecondDF.format(new Date());
    }
    
    /**
     * 根据给定的格式与时间(Date类型的)，返回时间字符串。最常用。<BR>
     * @param date 指定的日期
     * @param format 日期格式字符串
     * @return String 指定格式的日期字符串.
     */
    public static String getFormatDateTime(Date date, String format)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
    
    /**
     * 是否是昨天<BR>
     * @param time 时间值
     * @return 是否是昨天
     */
    public static boolean isYesterday(long time)
    {
        
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);
        Date today = c.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        
        return format.format(today).equals(format.format(new Date(time)));
    }
    
    /**
     * 是否是前天<BR>
     * @param time 时间值
     * @return 是否是前天
     */
    public static boolean isDayBeforeYesterday(long time)
    {
        
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, c.get(Calendar.DATE) - 2);
        Date today = c.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        
        return format.format(today).equals(format.format(new Date(time)));
    }
    
    /**
     * 是否是今天<BR>
     * @param time 时间
     * @return 是否是今天
     */
    public static boolean isToday(long time)
    {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, c.get(Calendar.DATE));
        Date today = c.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        
        return format.format(today).equals(format.format(new Date(time)));
    }
    
    /**
     * 返回制定日期所在的周是一年中的第几个周。<br>
     * 
     * @param year 年
     * @param month 范围1-12<br>
     * @param day 天
     * @return int 第几周
     */
    public static int getWeekOfYear(String year, String month, String day)
    {
        Calendar cal = new GregorianCalendar();
        cal.clear();
        cal.set(Integer.valueOf(year).intValue(), Integer.valueOf(month)
                .intValue() - 1, Integer.valueOf(day).intValue());
        return cal.get(Calendar.WEEK_OF_YEAR);
    }
    
    /**
     * 返回制定日期所在的周是一年中的第几个周。<br>
     * 
     * @param time 时间的毫秒数
     * @return int 第几周
     */
    public static int getWeekOfYear(long time)
    {
        Date date = new Date(time);
        Calendar cal = new GregorianCalendar();
        cal.clear();
        cal.set(date.getYear(), date.getMonth() - 1, date.getDay());
        return cal.get(Calendar.WEEK_OF_YEAR);
    }
    
    /**
     * 是否同一周<BR>
     * @param time1 时间毫秒 1
     * @param time2 时间毫秒 2
     * @return 是否同一周
     */
    public static boolean isTheSameWeek(long time1, long time2)
    {
        return getWeekOfYear(time1) == getWeekOfYear(time2);
    }
    
    /**
     * 聊天时间类型<BR>
     * @author zhaozeyang
     * @version [Paitao Client V20130911, 2013-11-27]
     */
    public enum ChatTimeType
    {
        /**
         * 今天
         */
        TODAY
        {
            @Override
            public String getTimeStr(long timeStamp)
            {
                return TODAY_TIME_FORMAT.format(new Date(timeStamp));
            }
        },
        /**
         * 昨天
         */
        YESTERDAY
        {
            @Override
            public String getTimeStr(long timeStamp)
            {
                return TODAY_TIME_FORMAT.format(new Date(timeStamp));
            }
        },
        /**
         * 一周内
         */
        WEEK
        {
            @Override
            public String getTimeStr(long timeStamp)
            {
                return WEEK_TIME_FORMAT.format(new Date(timeStamp));
            }
        },
        /**
         * 默认
         */
        DEFAULT
        {
            @Override
            public String getTimeStr(long timeStamp)
            {
                return DEFAULT_TIME_FORMAT.format(new Date(timeStamp));
            }
        };
        /**
         * 获得时间字符串<BR>
         * @param timeStamp 时间毫秒值
         * @return 时间字符串
         */
        public abstract String getTimeStr(long timeStamp);
        
        /**
         * 天的格式化
         */
        private static final SimpleDateFormat TODAY_TIME_FORMAT = new SimpleDateFormat(
                "HH:mm");
        
        /**
         * 星期的格式化
         */
        private static final SimpleDateFormat WEEK_TIME_FORMAT = new SimpleDateFormat(
                "E HH:mm");
        
        /**
         * 默认时间的格式化
         */
        private static final SimpleDateFormat DEFAULT_TIME_FORMAT = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
    }
}
