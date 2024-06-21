package com.ideas2it.cms.util;

import java.util.Date;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.text.ParseException; 
import java.time.Period;
import java.text.SimpleDateFormat;
import java.time.ZoneId;


/**
 * <p>
 * Used as utils for functionalities regarding date.
 * </p>
 *
 */

public class DateUtil {

  /**
     * <p>
     * Checks if the given date by the user is a valid date.
     * It is achieved by using SimpleDateFormat. The date string is parsed by SimpleDateFomat.
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
     *         If the user gives the dateStr in corrcet format as specified, returns true
     *         Else false is returned.
     * 
     */
     public static boolean checkValidDate(String dateStr, String dateFormat) {

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false); //This turn off the strict parsing of date (strict parsing checks in terms of Timestamp).

        try {
            Date date = sdf.parse(dateStr);
            return true;
        } catch (ParseException e) { 
            return false;
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
          LocalDate currentDate ;          
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

          if(endDateExclusive == null) {
              currentDate = LocalDate.now();
          } else {
              currentDate = LocalDate.parse(endDateExclusive, formatter);;
          }
          LocalDate userGivenDate = LocalDate.parse(startDateInclusive, formatter);    
          // Calculate age using java.time.Period
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