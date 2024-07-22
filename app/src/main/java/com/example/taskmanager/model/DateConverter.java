package com.example.taskmanager.model;

import androidx.room.TypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateConverter {

    private static final String DATE_FORMAT = "yyyy-MM-dd";  // Adjust format as needed
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);

    @TypeConverter
    public static Date toDate(String dateString) {
        try {
            return dateString == null ? null : dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @TypeConverter
    public static String fromDate(Date date) {
        return date == null ? null : dateFormat.format(date);
    }
//    @TypeConverter
//    public static Date toDate(Long timestamp) {
//        return timestamp == null ? null : new Date(timestamp);
//    }
//
//    @TypeConverter
//    public static Long toTimestamp(Date date) {
//        return date == null ? null : date.getTime();
//    }
}
