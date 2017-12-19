package com.saltechdigital.iaischedule.constantandenum;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Jeanpaul Tossou on 06/11/2016.
 */

public class CommonMethods {
    private static DateFormat dateFormat = new SimpleDateFormat("d MMM yyyy", Locale.FRENCH);
    private static DateFormat timeFormat = new SimpleDateFormat("KK:mma", Locale.FRENCH);

    private static DateTimeFormatter formatterTime = DateTimeFormat.forPattern("HH:mm");
    private static DateTimeFormatter formatterDate = DateTimeFormat.forPattern("dd/MM/yyyy");
    private static DateTimeFormatter formatterDateWeb = DateTimeFormat.forPattern("yyyy-MM-dd");

    /**
     * Les deux méthodes permettent d'avoir l'heure et la date seulement que le premier groupe le ramene en format PA et AM
     *
     * @return date and hour
     */

    public static String getCurrentTime() {

        DateTime dt = new DateTime();
        return dt.toString(formatterTime);
    }

    static String getCurrentTimeEng() {

        Date today = Calendar.getInstance().getTime();
        return timeFormat.format(today);
    }

    public static String getCurrentDate() {

        DateTime dt = new DateTime();
        return dt.toString(formatterDate);
    }

    public static String getCurrentWebDate() {

        DateTime dt = new DateTime();
        return dt.toString(formatterDateWeb);
    }

    static String getCurrentDateEng() {

        Date today = Calendar.getInstance().getTime();
        return dateFormat.format(today);
    }

}
