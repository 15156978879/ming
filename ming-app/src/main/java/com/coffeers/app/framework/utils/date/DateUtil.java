package com.coffeers.app.framework.utils.date;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Date工具类
 */
public class DateUtil {

    /**
     * 当天的开始时间
     * @return
     */
    public static long startOfTodDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date=calendar.getTime();
        return date.getTime();
    }
    /**
     * 当天的结束时间
     * @return
     */
    public static long endOfTodDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date date=calendar.getTime();
        return date.getTime();
    }
    /**
     * 昨天的开始时间
     * @return
     */
    public static long startOfyesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DATE, -1);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date=calendar.getTime();
        return date.getTime();
    }
    /**
     * 昨天的结束时间
     * @return
     */
    public static long endOfyesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        calendar.add(Calendar.DATE, -1);
        Date date=calendar.getTime();
        return date.getTime();
    }

    /**
     * 功能：获取上周的开始时间
     */
    public static long startOfLastWeek() {// 当周开始时间
        return startOfThisWeek() - 7 * 24 * 60 * 60 * 1000;
    }

    /**
     * 功能：获取上周的结束时间
     */
    public static long endOfLastWeek() {// 当周开始时间
        return endOfThisWeek() - 7 * 24 * 60 * 60 * 1000;
    }

    /**
     * 功能：获取本周的开始时间 示例：2013-05-13 00:00:00
     */
    public static long startOfThisWeek() {// 当周开始时间
        Calendar currentDate = Calendar.getInstance();
        currentDate.setFirstDayOfWeek(Calendar.MONDAY);
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);
        currentDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date date=currentDate.getTime();
        return date.getTime();
    }

    /**
     * 功能：获取本周的结束时间 示例：2013-05-19 23:59:59
     */
    public static long endOfThisWeek() {// 当周结束时间
        Calendar currentDate = Calendar.getInstance();
        currentDate.setFirstDayOfWeek(Calendar.MONDAY);
        currentDate.set(Calendar.HOUR_OF_DAY, 23);
        currentDate.set(Calendar.MINUTE, 59);
        currentDate.set(Calendar.SECOND, 59);
        currentDate.set(Calendar.MILLISECOND, 999);
        currentDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        Date date=currentDate.getTime();
        return date.getTime();
    }

    /**
     * 功能：获取本月的开始时间
     */
    public static long startOfThisMonth() {// 当周开始时间
        Calendar currentDate = Calendar.getInstance();
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);
        currentDate.set(Calendar.DAY_OF_MONTH, 1);
        Date date=currentDate.getTime();
        return date.getTime();
    }

    /**
     * 功能：获取本月的结束时间
     */
    public static long endOfThisMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DATE, -1);
        Date date=cal.getTime();
        return date.getTime();
    }

    /**
     * 功能：获取上月的开始时间
     */
    public static long startOfLastMonth() {// 当周开始时间
        Calendar currentDate = Calendar.getInstance();
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);
        currentDate.set(Calendar.DAY_OF_MONTH, 1);
        currentDate.add(Calendar.MONTH, -1);
        Date date=currentDate.getTime();
        return date.getTime();
    }
    /**
     * 功能：获取上月的结束时间
     */
    public static long endOfLastMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        cal.add(Calendar.DATE, -1);
        Date date=cal.getTime();
        return date.getTime();
    }

    /**
     *  根据long返回year
     * @param milliseconds
     * @return
     */
    public static Object[] theYearOfTime(long milliseconds){
        Calendar cal = Calendar.getInstance();
        Date date=cal.getTime();
        int thisYear=date.getYear()+1900;
        cal.setTimeInMillis(milliseconds);
        date=cal.getTime();
        int regirsterYear=date.getYear()+1900;
        if(regirsterYear<thisYear){
            List<Integer> yearL=new ArrayList<Integer>();
            for(int i=regirsterYear;i<=thisYear;i++){
                yearL.add(i);
            }
            return yearL.toArray();
        }else{
            return new Object[]{thisYear};
        }

    }

    /**
     * 功能：获取本年的开始时间
     */
    public static long startOfTheYear(int year) {// 当周开始时间
        Calendar currentDate = Calendar.getInstance();
        currentDate.set(Calendar.YEAR, year);
        currentDate.set(Calendar.MONTH, 0);
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);
        currentDate.set(Calendar.DAY_OF_MONTH, 1);
        Date date=currentDate.getTime();
        return date.getTime();
    }
    /**
     * 功能：获取本年的结束时间
     */
    public static long endOfTheYear(int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH,11);
        cal.set(Calendar.DAY_OF_MONTH, 31);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        Date date=cal.getTime();
        return date.getTime();
    }
}