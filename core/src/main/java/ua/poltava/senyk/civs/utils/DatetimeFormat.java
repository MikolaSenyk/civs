package ua.poltava.senyk.civs.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper class for format datetime values as String
 */
public class DatetimeFormat {
    
    private static final SimpleDateFormat FULL_DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat NO_SECOND_DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat SOLID_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
    private static final SimpleDateFormat HUMAN_DATETIME_FORMAT = new SimpleDateFormat("EEE, d MMM yyyy, 'at' h:mm aa");
    private static final SimpleDateFormat ISO8601_DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

	public static String getDateTime(Date date) {
		return FULL_DATETIME_FORMAT.format(date);
	}
    
    public static String getIsoDateTime(Date date) {
		return ISO8601_DATETIME_FORMAT.format(date);
	}
	
	public static String getDateHM(Date date) {
		return HUMAN_DATETIME_FORMAT.format(date); // before NO_SECOND_DATETIME_FORMAT
	}

	/**
	 * Get String with date in format "yyyy-MM-dd"
	 * @param date
	 * @return 
	 */
	public static String getDate(Date date) {
		return DATE_FORMAT.format(date);
	}
	
	/**
	 * Get date from String in format 2013-09-16
	 * @param yyyy_MM_dd - String with date
	 * @return Date
	 * @throws ParseException 
	 */
	public static Date getDate(String yyyy_MM_dd) throws ParseException {
		return DATE_FORMAT.parse(yyyy_MM_dd);
	}
	
	/**
	 * Get String with date in the format yyyyMMdd
	 * @param date
	 * @return String date without other symbols
	 */
	public static String getSolidDate(Date date) {
		return SOLID_DATE_FORMAT.format(date);
	}

	public static String getTime(Date date) {
		return TIME_FORMAT.format(date);
	}

    public static String getFaceBookDateHM(Date date) {
        Locale.setDefault(Locale.US);
        SimpleDateFormat fDate = new SimpleDateFormat("EEE, d MMM yyyy, 'at' h:mm");
        SimpleDateFormat fDN = new SimpleDateFormat("a");
        return fDate.format(date) + fDN.format(date).toLowerCase();
    }
    
    public static Date getDateFromISO8601(String isoDate) throws ParseException {
        isoDate = isoDate.replaceAll("\\+0([0-9]){1}\\:([0-9]){2}", "+0$1$2");
        Pattern twoDigitPattern = Pattern.compile("(\\d\\d)");
        Pattern blockPattern = Pattern.compile("(\\d{4}-\\d\\d-\\d\\d)T(.+):(.+):(.+?)([+-Z]{1})([0-9:]+)");
        Matcher blockMatcher = blockPattern.matcher(isoDate);
        if ( blockMatcher.matches() ) {
            String date = blockMatcher.group(1);
            String hours = blockMatcher.group(2);
            String mins = blockMatcher.group(3);
            String secs = blockMatcher.group(4);
            String tzSign = blockMatcher.group(5);
            String tzTime = blockMatcher.group(6);
            //System.out.println("Date chunks: " + date + "|" + hours + "|" + mins + "|" + secs + "|" + tzSign + "|" + tzTime);
            
            // check seconds
            Matcher secsMatcher = twoDigitPattern.matcher(secs);
            if ( ! secsMatcher.matches() ) secs = "00"; // TODO add milliseconds support
            
            // check time zone format
            if ( tzSign.equals("Z") ) tzTime = "";
            else {
                tzTime = tzTime.replaceAll(":", "");
            }
            
            String validDate = date + "T" + hours + ":" + mins + ":" + secs + tzSign + tzTime;
            //System.out.println("date: " + validDate);
            Date d = ISO8601_DATETIME_FORMAT.parse(validDate);
            //System.out.println("as date: " + d);
            return d;
        }
        throw new ParseException("Bad ISO8601 datetime format", 1);
    }

}
