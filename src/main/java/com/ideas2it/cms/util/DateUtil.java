package com.ideas2it.cms.util;

import java.util.Date;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.text.ParseException; 
import java.time.Period;
import java.text.SimpleDateFormat;

import com.ideas2it.cms.helper.DateValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <p>
 * Utility class for various date-related functionalities.
 * This class provides methods to validate date strings, check if a given date is valid,
 * and calculate the difference between two dates in terms of years, months, or days.
 * </p>
 * <p>
 * The primary functionalities include:
 * <ul>
 *     <li>Validating whether a given date string conforms to a specified format.</li>
 *     <li>Calculating the difference between two dates or between a given date and the current date.</li>
 * </ul>
 * </p>
 * <p>
 * The date format expected in methods should be in one of the standard formats
 * like "dd/MM/yyyy", "yyyy/MM/dd", "MM/dd/yyyy", and the utility does not support timestamp formats.
 * </p>
 * <p>
 * The calculations are based on the Java 8 Date-Time API (java.time).
 * </p>
 */

public class DateUtil {

    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);
  /**
     * <p>
     * Checks if the given date by the user is a valid date.
     * It is achieved by using SimpleDateFormat. The date string is parsed by SimpleDateFormat.
     * If the date is in invalid format a `ParseException` occurs.
     * This is handled in try catch block by following ways,
     * If the date is parsed successfully true is returned.
     * Else the `ParseException` occurs, catch block is executed and false is returned  
     * </p>
     *
     * @param dateStr
     *        The date of birth of student was taken as `dateStr`.
     *        It is parsed to check for valid date.
     * @param dateFormat
     *        The format of the date that to be verified can be specified here.
     *        The date format can be `dd/mm/yyyy`, `yyyy/MM/dd`, `MM/dd/yyyy`
     *        SHOULD NOT be in format of Timestamp or Month name-Day-Year with no leading zeros (February 17, 2009).
     * @return boolean
     *         If the user gives the dateStr in correct format as specified, returns true
     *         Else false is returned if the given date is in the future. Example: Greater than current date.
     * 
     */
     public static DateValidationResult checkValidDateAndAge(String dateStr, String dateFormat) {
         logger.debug("Validating date");
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false); //This turn off the strict parsing of date (strict parsing checks in terms of Timestamp).
        try {
            Date date = sdf.parse(dateStr);
            if(date.after(new Date())){
                return DateValidationResult.FUTURE_DATE;
            } else if(calculateDifferenceOfTwoDates(dateStr, null, "Years") > 18) {
                return DateValidationResult.OVER_18;
            } else if(calculateDifferenceOfTwoDates(dateStr, null, "Years") < 3) {
                return DateValidationResult.UNDER_3;
            } else {
                return DateValidationResult.VALID_DATE;
            }
        } catch (ParseException e) {
            return DateValidationResult.INVALID_DATE;
	    }
     }

     /**
     * <p>
     *
     * Calculates the difference between the current date and user given date.
     * If you want to find difference between 2 different date, the second date can be passed as secont parameter.
     * In case of difference between two date the start date is inclusive, end date is exclusive-
     * (Start date , End date].
     * It is achieved by using DateTimeFormatter.
     *
     * </p>
     *
     * @param startDateInclusive
     *        Must be in format of "dd/mm/yyyy".
     *        This must be the start date if you are finding difference between two dates.
     *        This start date must be inclusive.      
     * @param endDateExclusive
     *        Must be in format of "dd/mm/yyyy".
     *        This must be the end date if you are finding difference between two dates.
     *        If you need to find the difference between startDateInclusive and current date,
     *        - give this value in parameter as null.
     *        This end date must be exclusive.
     * @param preiodPreference
     *        This parameter gets the period preference.
     *        For no of days in between "Days" or "days" can be given.
     *        For no of months in between "Months" or "months" can be given.
     *        For no of years in between "Years" or "years" can be given.
     *        Any other inputs than this will return -1 immediately.
     * @return int
     *         Returns no of days or months or years as integer.
     *         If the preiodPreference is a valid input (Years or Months or Days)
     *         - returns the value accordingly. Else returns -1;
     * 
     */
      public static int calculateDifferenceOfTwoDates(String startDateInclusive, String endDateExclusive, String preiodPreference) {
          logger.debug("Calculating difference between two intervals");
          LocalDate currentDate ;          
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

          if(endDateExclusive == null) {
              currentDate = LocalDate.now();
          } else {
              currentDate = LocalDate.parse(endDateExclusive, formatter);;
          }
          LocalDate userGivenDate = LocalDate.parse(startDateInclusive, formatter);
          Period period = Period.between(userGivenDate, currentDate);
          if(preiodPreference.equals("Months") || preiodPreference.equals("months")) {
              return period.getMonths();
          } else if(preiodPreference.equals("Days") || preiodPreference.equals("days")) {
              return period.getDays();
          } else if(preiodPreference.equals("Years") || preiodPreference.equals("years")) {
              return period.getYears();
          } else {
              return -1;
          }
      }
}