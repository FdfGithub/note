package com.qianjing.note.util;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;

import java.util.Date;

public class CronTimeUtil {

    public static String getCron(Date date) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        int week = dateTime.get(DateTimeFieldType.dayOfWeek());//周几
        int month = dateTime.get(DateTimeFieldType.monthOfYear());//月
        int day = dateTime.get(DateTimeFieldType.dayOfMonth());//日
        int hours = dateTime.get(DateTimeFieldType.hourOfDay());//时
        int minute = dateTime.get(DateTimeFieldType.minuteOfHour());//分
        int second = dateTime.get(DateTimeFieldType.secondOfMinute());//秒

        return second + StringUtils.SPACE + minute + StringUtils.SPACE + hours
                + StringUtils.SPACE + day + StringUtils.SPACE + month
                + StringUtils.SPACE + week;
    }
}
