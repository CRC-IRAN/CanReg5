/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package canreg.common;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author ervikm
 */
public class DateHelper {

    public static GregorianCalendarCanReg parseDateStringToGregorianCalendarCanReg(String dateString, String dateFormatString) throws ParseException, IllegalArgumentException {

        if (dateString.length() != dateFormatString.length()) {
            return null;
        }

        boolean unknownDay = false;
        boolean unknownMonth = false;
        boolean unknownYear = false;

        int day = 99;
        int month = 99;
        int year = 9999;

        String yearString = getYear(dateString, dateFormatString);
        String monthString = getMonth(dateString, dateFormatString);
        String dayString = getDay(dateString, dateFormatString);

        if (dayString.trim().length() > 0) {
            day = Integer.parseInt(dayString);
        } else {
            dayString = "99";
        }
        if (monthString.trim().length() > 0) {
            month = Integer.parseInt(monthString);
        } else {
            monthString = "99";

        }
        if (yearString.trim().length() > 0) {
            year = Integer.parseInt(yearString);

        } else {
            yearString = "9999";
        }

        GregorianCalendarCanReg calendar = new GregorianCalendarCanReg();

        calendar.clear();
        calendar.setLenient(false);
        calendar.set(year, month - 1, day);

        boolean dateReadProperly = false;

        while (!dateReadProperly) {
            try {
                calendar.getTimeInMillis(); // This is just to trigger an error - if we have one latent...
                dateReadProperly = true;
            } catch (IllegalArgumentException iae) {
                if ("YEAR".equalsIgnoreCase(iae.getMessage())) {
                    calendar.clear(Calendar.YEAR);
                    if ("9999".equals(yearString) || "0000".equals(yearString)) {
                        unknownYear = true;
                    } else {
                        throw iae;
                    }
                } else if ("MONTH".equalsIgnoreCase(iae.getMessage())) {
                    calendar.clear(Calendar.MONTH);
                    if ("99".equals(monthString) || "00".equals(monthString)) {
                        unknownMonth = true;
                    } else {
                        throw iae;
                    }
                } else if ("DAY_OF_MONTH".equalsIgnoreCase(iae.getMessage())) {
                    calendar.clear(Calendar.DAY_OF_MONTH);
                    if ("99".equals(dayString) || "00".equals(dayString)) {
                        unknownDay = true;
                    } else {
                        throw iae;
                    }
                }
            }
        }

        calendar.setUnknownDay(unknownDay);
        calendar.setUnkownMonth(unknownMonth);
        calendar.setUnknownYear(unknownYear);

        return calendar;
    }

    public static String parseGregorianCalendarCanRegToDateString(GregorianCalendarCanReg calendar, String dateFormatString) {
        String dateString = dateFormatString;
        DecimalFormat format = new DecimalFormat();
        // NumberFormatter nf = new NumberFormatter(format);
        format.setMinimumIntegerDigits(2);
        format.setGroupingUsed(false);
        try {
            if (calendar.isUnknownYear() || !calendar.isSet(Calendar.YEAR)) {
                dateString = setYear(dateString, dateFormatString, "9999");
            } else {
                dateString = setYear(dateString, dateFormatString, format.format(calendar.get(Calendar.YEAR)));
            }
            if (calendar.isUnknownMonth() || !calendar.isSet(Calendar.MONTH)) {
                dateString = setMonth(dateString, dateFormatString, "99");
            } else {
                dateString = setMonth(dateString, dateFormatString, format.format(calendar.get(Calendar.MONTH) + 1));
            }
            if (calendar.isUnknownDay() || !calendar.isSet(Calendar.DAY_OF_MONTH)) {
                dateString = setDay(dateString, dateFormatString, "99");
            } else {
                dateString = setDay(dateString, dateFormatString, format.format(calendar.get(Calendar.DAY_OF_MONTH)));
            }
        } catch (IllegalArgumentException iae) {
            System.out.println(iae + ": " + calendar);
        }
        return dateString;
    }

    public static String getYear(String dateString, String dateFormatString) {
        return getPartOfStringBasedOnFilter(dateString, dateFormatString, 'y');
    }

    public static String getMonth(String dateString, String dateFormatString) {
        return getPartOfStringBasedOnFilter(dateString, dateFormatString, 'm');
    }

    public static String getDay(String dateString, String dateFormatString) {
        return getPartOfStringBasedOnFilter(dateString, dateFormatString, 'd');
    }

    private static String getPartOfStringBasedOnFilter(String string, String filter, char lookFor) {
        String returnString = "";

        // Case insensitive
        filter = filter.toLowerCase();

        for (int i = 0; i < string.length() && i < filter.length(); i++) {
            if (filter.charAt(i) == lookFor) {
                returnString += string.charAt(i);
            }
        }
        return returnString;
    }

    public static String setYear(String dateString, String dateFormatString, String replacementString) {
        return setPartOfStringBasedOnFilter(dateString, dateFormatString, 'y', replacementString);
    }

    public static String setMonth(String dateString, String dateFormatString, String replacementString) {
        return setPartOfStringBasedOnFilter(dateString, dateFormatString, 'm', replacementString);
    }

    public static String setDay(String dateString, String dateFormatString, String replacementString) {
        return setPartOfStringBasedOnFilter(dateString, dateFormatString, 'd', replacementString);
    }

    private static String setPartOfStringBasedOnFilter(String string, String filter, char lookFor, String replacementString) {
        // Case insensitive
        String newString = "";
        filter = filter.toLowerCase();
        int placeInReplacementString = 0;
        for (int i = 0; i < string.length() && i < filter.length(); i++) {
            if (filter.charAt(filter.length() - 1 - i) == lookFor) {
                newString = replacementString.charAt(replacementString.length() - 1 - placeInReplacementString++) + newString;
                if (placeInReplacementString >= replacementString.length()) {
                    placeInReplacementString = 0;
                }
            } else {
                newString = string.charAt(string.length() - 1 - i) + newString;
            }
        }
        return newString;
    }

    public static long daysBetween(Calendar startDate, Calendar endDate) {
        Calendar date = (Calendar) startDate.clone();
        long daysBetween = 0;
        while (date.before(endDate) || date.equals(endDate)) {
            date.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        return daysBetween - 1;
    }

    public static long yearsBetween(Calendar startDate, Calendar endDate) {
        Calendar date = (Calendar) startDate.clone();
        long yearsBetween = 0;
        while (date.before(endDate) || date.equals(endDate)) {
            date.add(Calendar.YEAR, 1);
            yearsBetween++;
        }
        return yearsBetween - 1;
    }

    public static Calendar parseTimestamp(String timestamp, String dateFormat, Locale locale) throws ParseException {
        /*
         ** we specify Locale.US since months are in english
         */
        if (locale == null){
            locale = Locale.getDefault();
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, locale);
        Date d = sdf.parse(timestamp);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        return cal;
    }
}
