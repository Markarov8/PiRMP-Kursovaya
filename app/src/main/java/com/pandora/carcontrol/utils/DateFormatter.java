package com.pandora.carcontrol.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatter {

    private static final SimpleDateFormat ISO_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd MMM", Locale.getDefault());
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd MMM. HH:mm", Locale.getDefault());

    public static String formatDate(String isoDateString) {
        try {
            Date date = ISO_FORMAT.parse(isoDateString);
            return date != null ? DATE_FORMAT.format(date) : "";
        } catch (ParseException e) {
            return "";
        }
    }

    public static String formatTime(String isoDateString) {
        try {
            Date date = ISO_FORMAT.parse(isoDateString);
            return date != null ? TIME_FORMAT.format(date) : "";
        } catch (ParseException e) {
            return "";
        }
    }

    public static String formatDateTime(String isoDateString) {
        try {
            Date date = ISO_FORMAT.parse(isoDateString);
            return date != null ? DATE_TIME_FORMAT.format(date) : "";
        } catch (ParseException e) {
            return "";
        }
    }
}