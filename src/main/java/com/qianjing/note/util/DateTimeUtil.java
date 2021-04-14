package com.qianjing.note.util;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * Created by geely
 */
public class DateTimeUtil {

    //joda-time

    //str->Date
    //Date->str
    public static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";


    public static Date strToDate(String dateTimeStr, String formatStr) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(formatStr);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

    public static String dateToStr(Date date, String formatStr) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(formatStr);
    }

    public static Date strToDate(String dateTimeStr) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD_FORMAT);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

    public static String dateToStr(Date date) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(STANDARD_FORMAT);
    }

    public static String getDay(String date) {
        return date.split(" ")[0].split("-")[2];
    }


    public static String getMonthAndDay(Date date) {
        String[] time = dateToStr(date).split(" ")[0].split("-");
        return time[1] + "月" + time[2] + "日";
    }


    public static String getTime12(Date date) {
        String[] time = dateToStr(date, "HH:mm").split(":");
        StringBuilder sb = new StringBuilder();
        int hours = Integer.parseInt(time[0]);
        if (hours > 12) {
            hours -= 12;
            sb.append("下午").append(StringUtils.SPACE);
        } else if (hours < 12) {
            sb.append("上午").append(StringUtils.SPACE);
        } else {
            sb.append("中午").append(StringUtils.SPACE);
        }
        time[0] = hours < 10 ? "0" + hours : "" + hours;
        sb.append(time[0]).append(":").append(time[1]);
        return sb.toString();
    }

}
