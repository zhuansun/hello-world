package com.zspc.hw.common.util;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * @date 2021/4/19 19:47
 */

@Slf4j
public class DateUtil {
    public static final String DATE_FORMAT_STRING = "yyyyMMddHHmmss";

    public static final String DATE_FORMAT_COMMON_STRING = "yyyy-MM-dd HH:mm:ss";

    // 上海时间
    private static final ZoneId ZONE_ID = ZoneId.of("Asia/Shanghai");

    /**
     * 线程安全的日期格式对象
     */
    private static final ThreadLocal<DateFormat> DATE_FORMAT = new ThreadLocal<DateFormat>() {

        @Override
        protected DateFormat initialValue() {
            // 完整日期
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }

    };
    /**
     * 格式化完整日期
     *
     * @param date
     * @return yyyy-MM-dd HH:mm:ss格式的字符串
     */
    public static String formatDate(Date date) {
        return DATE_FORMAT.get().format(date);
    }

    public static LocalDateTime longToLocalDateTime(Long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZONE_ID);
    }

    public static LocalDate longToLocalDate(Long timestamp) {
        return DateUtil.longToLocalDateTime(timestamp).toLocalDate();
    }

    public static LocalTime longToLocalTime(Long timestamp) {
        return DateUtil.longToLocalDateTime(timestamp).toLocalTime();
    }

    public static Date longToDate(Long timestamp) {
        return new Date(timestamp);
    }

    public static Long localDateTimeToLong(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZONE_ID).toInstant().toEpochMilli();
    }

    public static Long localDateTimeToLongSec(LocalDateTime localDateTime) {
        long mills = localDateTime.atZone(ZONE_ID).toInstant().toEpochMilli();
        return Long.valueOf(String.valueOf(mills).substring(0, String.valueOf(mills).length() - 3));
    }

    public static Long dateToLongSec(Date date) {
        long mills = date.getTime();
        return Long.valueOf(String.valueOf(mills).substring(0, String.valueOf(mills).length() - 3));
    }

    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        if (Objects.isNull(localDateTime)) {
            return null;
        }
        return longToDate(localDateTimeToLong(localDateTime));
    }

    public static Long DateToLong(Date date) {
        return date.getTime();
    }
//
//    public static Date stringToDate(String dateStr, String format) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
//        try {
//            return simpleDateFormat.parse(dateStr);
//        } catch (ParseException e) {
//            throw new BusinessException("时间转化失败");
//        }
//    }
//
//    public static LocalDateTime formatCST(String dateStr) {
//        if (StringUtils.isNotBlank(dateStr)) {
//            try {
//                DateFormat cstFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyy",
//                    Locale.ENGLISH);
//                Date date = cstFormat.parse(dateStr);
//                return LocalDateTimeUtil.of(date);
//            } catch (Exception e) {
//                log.error("formatCST error");
//            }
//        }
//        return null;
//    }

    // 获得某天最大时间 2020-02-19 23:59:59
    public static Date getEndOfDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime
            .ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static String getDateStr(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    public static Date parseDateStr(String dateStr, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            return simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            log.error("getEndOfHour error date {}", dateStr);
        }
        return null;
    }

    public static Date getStartOfHour(Date date) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(simpleDateFormat.parse(simpleDateFormat.format(date)));
            calendar.add(Calendar.MINUTE, 0);
            calendar.add(Calendar.SECOND, 0);
            return calendar.getTime();
        } catch (Exception e) {
            log.error("getEndOfHour error date {}", date);
        }
        return date;
    }

    public static Date getEndOfHour(Date date) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(simpleDateFormat.parse(simpleDateFormat.format(date)));
            calendar.add(Calendar.MINUTE, 59);
            calendar.add(Calendar.SECOND, 59);
            return calendar.getTime();
        } catch (Exception e) {
            log.error("getEndOfHour error date {}", date);
        }
        return date;
    }

    // 获得某天最小时间 2020-02-17 00:00:00
    public static Date getStartOfDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime
            .ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date getBeforeMinute(Date date, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) - minute);
        return calendar.getTime();
    }

    public static Date getBeforeHours(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - hours);
        return calendar.getTime();
    }

    public static Date getAfterHours(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + hours);
        return calendar.getTime();
    }

    /**
     * 获取当前时间前 n 天的时间
     */
    public static Date getBeforeDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - days);
        return calendar.getTime();
    }

    /**
     * 获取当前时间后 n 天的时间
     */
    public static Date getAfterDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + days);
        return calendar.getTime();
    }

    /**
     * 获取当前时间前 n 天的时间
     */
    public static Date getBeforeMonths(Date date, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.add(Calendar.MONTH, -month);
        return calendar.getTime();
    }

    /**
     * 获取当前时间前 n 天的时间
     */
    public static Date getAfterMonths(Date date, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.add(Calendar.MONTH, month);
        return calendar.getTime();
    }

    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     * //大于一天，就算两天
     */
    public static int differentDays(Date date1, Date date2) {
        return new BigDecimal((date2.getTime() - date1.getTime())).divide(
            new BigDecimal((1000 * 3600 * 24)), 2, BigDecimal.ROUND_HALF_UP).intValue();
    }
    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     * //大于一天，就算两天
     */
    public static int differentDaysByDay(LocalDateTime date1, LocalDateTime date2) {
        Integer after = Integer.parseInt(getDateStr(localDateTimeToDate(date1),"yyyyMMdd"));
        Integer start = Integer.parseInt(getDateStr(localDateTimeToDate(date2),"yyyyMMdd"));
        return after-start;
    }

    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     */
    public static int differentHours(Date date1, Date date2) {
        return (int) ((date2.getTime() - date1.getTime()) / (1000 * 3600));
    }

    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     */
    public static int differentSeconds(Date date1, Date date2) {
        return (int) ((date2.getTime() - date1.getTime()) / 1000);
    }

    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     */
    public static int differentSeconds(LocalDateTime date1, LocalDateTime date2) {
        return (int) (localDateTimeToLongSec(date1) - localDateTimeToLongSec(date2));
    }

    //获取这一年的日历
    private static Calendar getCalendarFormYear(int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.YEAR, year);
        return cal;
    }

    //获取某一年的某一周的周日日期
    public static String getEndDayOfWeekNo(String weekStr) {
        String yearStr = weekStr.substring(0, 4);
        int year = Integer.parseInt(yearStr);
        int weekNo = Integer.parseInt(weekStr.replace(yearStr, ""));

        Calendar cal = getCalendarFormYear(year);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setMinimalDaysInFirstWeek(4);
        cal.set(Calendar.WEEK_OF_YEAR, weekNo);
        cal.add(Calendar.DAY_OF_WEEK, 6);

        String yyyy = cal.get(Calendar.YEAR) + "";
        String month = (cal.get(Calendar.MONTH) + 1) < 10 ?
            "0" + (cal.get(Calendar.MONTH) + 1) :
            (cal.get(Calendar.MONTH) + 1) + "";
        String day = cal.get(Calendar.DAY_OF_MONTH) < 10 ?
            "0" + cal.get(Calendar.DAY_OF_MONTH) :
            cal.get(Calendar.DAY_OF_MONTH) + "";
        return yyyy + month + day;
    }
//
//    //获取一周中的某一天,day的取值参考Calendar.SATURDAY没去
//    public static Date getDateOfWeek(Date date, int day) {
//        if (day < 1 || day > 7) {
//            throw new BusinessException("DateUtil.getDateOfWeek day取值范围再1~7之间 ");
//        }
//        Calendar calendar = Calendar.getInstance();
//        calendar.setFirstDayOfWeek(Calendar.MONDAY);
//        calendar.setTime(date);
//        calendar.set(Calendar.DAY_OF_WEEK, day);
//        return calendar.getTime();
//    }

    public static Date getStartOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    public static Date getEndOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);//日期倒数一日,既得到本月最后一天
        return getEndOfDay(calendar.getTime());
    }

    //获取某一年的某一周的周一日期
    public static String getStartDayOfWeekNo(String weekStr) {
        String yearStr = weekStr.substring(0, 4);
        int year = Integer.parseInt(yearStr);
        int weekNo = Integer.parseInt(weekStr.replace(yearStr, ""));

        Calendar cal = getCalendarFormYear(year);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setMinimalDaysInFirstWeek(4);
        cal.set(Calendar.WEEK_OF_YEAR, weekNo);

        String yyyy = cal.get(Calendar.YEAR) + "";
        String month = (cal.get(Calendar.MONTH) + 1) < 10 ?
            "0" + (cal.get(Calendar.MONTH) + 1) :
            (cal.get(Calendar.MONTH) + 1) + "";
        String day = cal.get(Calendar.DAY_OF_MONTH) < 10 ?
            "0" + cal.get(Calendar.DAY_OF_MONTH) :
            cal.get(Calendar.DAY_OF_MONTH) + "";
        return yyyy + month + day;

    }

    //获取某一年的某一月的最后一天
    public static String getEndDayOfMonthNo(String monthStr) {
        String yearStr = monthStr.substring(0, 4);
        int year = Integer.parseInt(yearStr);
        int monthNo = Integer.parseInt(monthStr.replace(yearStr, ""));

        Calendar cal = getCalendarFormYear(year);
        cal.set(Calendar.MONTH, monthNo - 1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

        String yyyy = cal.get(Calendar.YEAR) + "";
        String month = (cal.get(Calendar.MONTH) + 1) < 10 ?
            "0" + (cal.get(Calendar.MONTH) + 1) :
            (cal.get(Calendar.MONTH) + 1) + "";
        String day = cal.get(Calendar.DAY_OF_MONTH) < 10 ?
            "0" + cal.get(Calendar.DAY_OF_MONTH) :
            cal.get(Calendar.DAY_OF_MONTH) + "";
        return yyyy + month + day;
    }

    //获取某一年的某一月的第一天
    public static String getStartDayOfMonthNo(String monthStr) {
        String yearStr = monthStr.substring(0, 4);
        int year = Integer.parseInt(yearStr);
        int monthNo = Integer.parseInt(monthStr.replace(yearStr, ""));

        Calendar cal = getCalendarFormYear(year);
        cal.set(Calendar.MONTH, monthNo - 1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DATE));

        String yyyy = cal.get(Calendar.YEAR) + "";
        String month = (cal.get(Calendar.MONTH) + 1) < 10 ?
            "0" + (cal.get(Calendar.MONTH) + 1) :
            (cal.get(Calendar.MONTH) + 1) + "";
        String day = cal.get(Calendar.DAY_OF_MONTH) < 10 ?
            "0" + cal.get(Calendar.DAY_OF_MONTH) :
            cal.get(Calendar.DAY_OF_MONTH) + "";
        return yyyy + month + day;

    }

    /**
     * 获取今天周几
     */
    public static Integer getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取某一天所在周
     * 每一周是从周一 到 周日
     * 针对跨年的情况，看每周的时间，属于大于半数的那一年
     */
    public static String getWeekOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setMinimalDaysInFirstWeek(4);
        int year = calendar.get(Calendar.YEAR);
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        return year + (week < 10 ? "0" + week : "" + week);
    }

    /**
     * 获取某一天的所在月
     */
    public static String getMonthOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        return year + (month < 10 ? "0" + month : "" + month);
    }

    /**
     * 分钟格式化；按formatMinute格式化
     * 2022-09-12 12:34:03
     * 2022-09-12 12:30:00
     */
    public static Date getMinuteFormat(Date currentDate, int formatMinute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        int minute = calendar.get(Calendar.MINUTE);
        int formatMin = minute/formatMinute*formatMinute;
        calendar.set(Calendar.MINUTE,formatMin);
        calendar.set(Calendar.SECOND,0);
        return calendar.getTime();
    }

    /**
     * 获取两周之间相差几周 yyyyww 202201
     */
    public static int differentWeeks(String before, String after) throws ParseException {

        String beforeYearStr = before.substring(0, 4);
        String beforeWeekStr = before.replace(beforeYearStr, "");
        int beforeYear = Integer.parseInt(beforeYearStr);
        int beforeWeek = Integer.parseInt(beforeWeekStr);

        String afterYearStr = after.substring(0, 4);
        String afterWeekStr = after.replace(afterYearStr, "");
        int afterYear = Integer.parseInt(afterYearStr);
        int afterWeek = Integer.parseInt(afterWeekStr);

        //判断是否跨年
        int differentWeeks = 1;
        if (beforeYear != afterYear) {
            //跨年
            //判断跨几年
            int differentYears = afterYear - beforeYear;
            for (int i = 0; i <= differentYears; i++) {
                int currentForYear = beforeYear + i;
                //获取beforeYear总共有多少周（2021-12-31 和 2022-1-1 分别是属于第几周）
                int beforeTotalWeeks = getTotalWeeksOfYear(currentForYear);
                if (currentForYear == beforeYear) {
                    differentWeeks += (beforeTotalWeeks - beforeWeek);
                } else if (currentForYear == afterYear) {
                    differentWeeks += afterWeek;
                } else {
                    differentWeeks += beforeTotalWeeks;
                }
            }
        } else {
            differentWeeks = afterWeek - beforeWeek + 1;
        }
        return differentWeeks;
    }

    /**
     * 获取某一年总共有多少周
     */
    public static int getTotalWeeksOfYear(int year) throws ParseException {
        String date = year + "-12-31";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String weekOfYear = getWeekOfYear(sdf.parse(date));
        String beforeYearStr = weekOfYear.substring(0, 4);
        String beforeWeekStr = weekOfYear.replace(beforeYearStr, "");
        return Integer.parseInt(beforeWeekStr);
    }

    /**
     * 获取两月之间相差几月 yyyymm
     */
    public static int differentMonths(String before, String after) {

        String beforeYear = before.substring(0, 4);
        String beforeMonth = before.replace(beforeYear, "");

        String afterYear = after.substring(0, 4);
        String afterMonth = after.replace(afterYear, "");

        //判断是否跨年
        if (!beforeYear.equals(afterYear)) {
            //跨年
            return 12 - Integer.parseInt(beforeMonth) + Integer.parseInt(afterMonth) + 1;
        } else {
            return Integer.parseInt(afterMonth) - Integer.parseInt(beforeMonth) + 1;
        }
    }



    public static LocalDateTime getEarlyLocalDateTime(LocalDateTime one, LocalDateTime two){
        if(Objects.nonNull(one) && Objects.nonNull(two)){
            //取早的一次
            return two.isAfter(one) ? one : two;
        }
        return Objects.isNull(one) ? two : one;
    }


    public static void main(String[] args) throws ParseException {
        long a = localDateTimeToLong(LocalDateTime.now());
        Long aLong = Long.valueOf(String.valueOf(a).substring(0, String.valueOf(a).length() - 3));
        System.out.println(a);
        System.out.println(aLong);
    }

}
