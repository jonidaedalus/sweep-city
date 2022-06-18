package kz.alash.naklei.service.utils;

import org.slf4j.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtility {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(DateUtility.class);

    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static SimpleDateFormat hourAndMinutesFormatter = new SimpleDateFormat("HH:mm");
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
    public static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    public static DateTimeFormatter cronTimeFormatter = DateTimeFormatter.ofPattern("s m h d M y");
    public static DateTimeFormatter cronTimeFormatterNoYear = DateTimeFormatter.ofPattern("0 mm HH dd MM ?");
    public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static long differenceBetween(Date date1, Date date2) {
        long diffInMillies = Math.abs(date1.getTime() - date2.getTime());
        return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public static LocalTime getNearestHourThird(LocalTime datetime) {
        int minutes = datetime.getMinute();
        int mod = minutes % 20;
        LocalTime newTime = datetime.plusMinutes(20 - mod);;
        newTime = newTime.truncatedTo(ChronoUnit.MINUTES);
        return newTime;
    }

    public static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static LocalDate convertToLocalDateViaSqlDate(Date dateToConvert) {
        return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
    }
}
