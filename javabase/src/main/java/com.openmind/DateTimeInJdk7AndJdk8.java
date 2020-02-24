package com.openmind;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

/**
 * jishuzhan
 *
 * @author zhoujunwen
 * @date 2019-12-18
 * @time 16:24
 * @desc
 */
public class DateTimeInJdk7AndJdk8 {
    public static void main(String[] args) throws ParseException {
        dateTimeInjdk7();
        dateTimeInJdk8();
    }

    /**
     * Java7 地宫两个时间相关的类
     *
     * @throws ParseException
     * @see java.util.Date 和
     * @see java.util.Calendar
     */
    private static void dateTimeInjdk7() throws ParseException {
        // 1.获取时间
        // output: Wed Dec 18 16:29:39 CST 2019
        Date date = new Date();
        System.out.println(date);
        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar.getTime());

        // 2.获取时间戳
        // output: 1576657779565
        Date d = new Date();
        System.out.println(d.getTime());
        Calendar c = Calendar.getInstance();
        System.out.println(c.getTime().getTime());
        System.out.println(c.getTimeInMillis());
        System.out.println(System.currentTimeMillis());

        // 3.格式化时间格式
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sf.format(new Date()));

        // 4.时间转换
        // string转date
        String str1 = "2019-12-12 22:34:55";
        System.out.println(sf.parse(str1));
        // date转string
        String str2 = "1576658621631";
        System.out.println(sf.format(new Date(Long.parseLong(str2))));
        System.out.println(sf.format(new Timestamp(Long.parseLong(str2))));

        // 5.获取昨天此时的时间
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        System.out.println(sf.format(cal.getTime()));

        // 6. 获取本月最后一天的两种方式
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        System.out.println(ca.getTime());
    }


    /**
     * Java8提供三个日期时间相关的API
     *
     * @see java.time.LocalDate 只包含日期，不包含时间，不可变类，且线程安全
     * @see java.time.LocalTime 只包含时间，不包含日期，不可变类，且线程安全
     * @see java.time.LocalDateTime 包含日期和时间，不可变类，且线程安全
     */
    private static void dateTimeInJdk8() {
        // 1.获取时间
        // 获取日期
        LocalDate localDate = LocalDate.now();
        System.out.println(localDate);
        // 获取时间
        LocalTime localTime = LocalTime.now();
        System.out.println(localTime);
        // 获取日期时间
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime);


        // 2.获取时间戳
        long nano = Instant.now().getNano();
        long milli = Instant.now().toEpochMilli();
        long seconds = Instant.now().getEpochSecond();
        System.out.println(nano);
        System.out.println(milli);
        System.out.println(seconds);


        // 3.格式化时间格式
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dt = dtf.format(LocalDateTime.now());
        System.out.println(dt);

        String dt2 = LocalDateTime.now().format(dtf);
        System.out.println(dt2);

        // 4.时间转换
        String str = "2014-12-23 12:34:56";
        LocalDateTime localDateTime1 = LocalDateTime.parse(str, dtf);
        System.out.println(localDateTime1);

        // 5.获取昨天此时的时间
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime yesterday = today.minusDays(1);
        System.out.println(yesterday);

        // 6. 获取本月最后一天的两种方式
        LocalDate today1 = LocalDate.now();
        System.out.println(today1.with(TemporalAdjusters.lastDayOfMonth()));

        // 7.计算两个时间间隔
        LocalDateTime localDateTime2 = LocalDateTime.now();
        LocalDateTime localDateTime3 = localDateTime2.plusSeconds(120);
        Duration duration = Duration.between(localDateTime2, localDateTime3);
        System.out.println(duration.getSeconds());

        // 8.计算两个日期间隔
        LocalDate localDate1 = LocalDate.now();
        LocalDate localDate2 = localDate1.plusDays(20);
        Period period = Period.between(localDate1, localDate2);
        System.out.println(period.getDays());
    }
}
