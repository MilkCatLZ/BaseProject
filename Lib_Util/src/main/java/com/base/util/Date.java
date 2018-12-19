package com.base.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class Date {

	public final static String SPLIT_Date = "/";
	public final static String SPLIT_TIME = ":";

	public static SimpleDateFormat getTimeForMat() {
		SimpleDateFormat tempDate = new SimpleDateFormat(new StringBuilder().append("yyyy").append(SPLIT_Date).append("MM").append(SPLIT_Date).append("dd HH").append(SPLIT_TIME)
																			.append("mm").append(SPLIT_TIME).append("ss").toString(), Locale.US);
		return tempDate;
	}

	public static SimpleDateFormat getTimeForMatNoSec() {
		SimpleDateFormat tempDate = new SimpleDateFormat("yyyy" + SPLIT_Date + "MM"
				+ SPLIT_Date + "dd HH"+SPLIT_TIME+"mm", Locale.US);
		return tempDate;
	}

	public static SimpleDateFormat getTimeForMatNoSecSta() {
		SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd HH"+SPLIT_TIME+"mm", Locale.US);
		return tempDate;
	}

	public static SimpleDateFormat getTimeForMatNoSecMinSta() {
		SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd HH", Locale.US);
		return tempDate;
	}

	public static SimpleDateFormat getTimeForMatNoTimeSta() {
		SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		return tempDate;
	}

	public static SimpleDateFormat getTimeForMatNoTimeNoYearSta() {
		SimpleDateFormat tempDate = new SimpleDateFormat("MM-dd", Locale.US);
		return tempDate;
	}

	public static SimpleDateFormat getTimeForNoYearAndSecMat() {
		SimpleDateFormat tempDate = new SimpleDateFormat("MM" + SPLIT_Date + "dd H", Locale.US);
		return tempDate;
	}

	public static SimpleDateFormat getDateForMat() {
		SimpleDateFormat tempDate = new SimpleDateFormat("yyyy" + SPLIT_Date + "MM"
				+ SPLIT_Date + "dd", Locale.US);
		return tempDate;
	}

	/**
	 * 返回当前时间和未来的时间
	 * 
	 * @param days
	 *            未来多少天
	 * @return[0] 当前时间，[1]未来时间
	 */
	public static String getFutureDate(int days) {

		String time = Date.getDateForMat().format(
				Date.getDateBefore(new java.util.Date(), -days));
		return time;
	}

	public static SimpleDateFormat getJustTimeForMat() {
		SimpleDateFormat tempDate = new SimpleDateFormat("HH"+SPLIT_TIME+"mm"+SPLIT_TIME+"ss");
		return tempDate;
	}

	public static SimpleDateFormat getJustTimeNoSecForMat() {
		SimpleDateFormat tempDate = new SimpleDateFormat("HH"+SPLIT_TIME+"mm");
		return tempDate;
	}

	public static SimpleDateFormat getDateForMatNoYear() {
		SimpleDateFormat tempDate = new SimpleDateFormat("MM" + SPLIT_Date + "dd");
		return tempDate;
	}

	/**
	 * 判断当day1 和 day2的时间范围是否大于指定范围
	 * 
	 * @param day1
	 * @param day2
	 * @param days
	 *            指定的时间范围
	 * @return true:大于，false:小于
	 */
	public static boolean isAfterTheDay(String day1, String day2, int days) {
		boolean isOk = false;
		try {
			java.util.Date d1 = getDateForMat().parse(day1);
			java.util.Date d2 = getDateForMat().parse(day2);
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(d1);
			// int day1Count = calendar1.get(Calendar.DAY_OF_YEAR);

			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(d2);
			// int day2Count = calendar2.get(Calendar.DAY_OF_YEAR);

			int daysTmp = getDaysBetween(calendar1, calendar2);

			// if(d1.getYear() != d2.getYear()){
			// isOk = false;
			// }else{
			if (daysTmp < days) {
				isOk = true;
			}
			// }

			// Log.e("ATA", "day2Count:"+day1Count);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return isOk;
	}

	/**
	 * 指定时间的日期差
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static int getDaysBetween(Calendar d1,
			Calendar d2) {
		if (d1.after(d2)) { // swap dates so that d1 is start and d2 is end
			Calendar swap = d1;
			d1 = d2;
			d2 = swap;
		}
		int days = d2.get(Calendar.DAY_OF_YEAR)
				- d1.get(Calendar.DAY_OF_YEAR);
		int y2 = d2.get(Calendar.YEAR);
		if (d1.get(Calendar.YEAR) != y2) {
			d1 = (Calendar) d1.clone();
			do {
				days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);
				d1.add(Calendar.YEAR, 1);
			} while (d1.get(Calendar.YEAR) != y2);
		}
		return days;
	}

	public static String[] setAfterStr(int days) {
		String[] time = new String[2];
		time[0] = Date.getDate();
		time[1] = Date.getDateForMat().format(
				Date.getDateAfter(new java.util.Date(), -days));

		return time;
	}

	public static String getTime() {
		SimpleDateFormat tempDate = getJustTimeNoSecForMat();
		String dateStr = tempDate.format(new java.util.Date()).toString();
		return dateStr;
	}

	public static String getTimeNoMin() {
		SimpleDateFormat tempDate = getJustTimeForMat();
		String dateStr = tempDate.format(new java.util.Date()).toString();
		return dateStr;
	}

	public static String getDate() {

		String dateStr = getDateForMat().format(new java.util.Date())
				.toString();
		return dateStr;
	}

	public static String getDateTimeNoSec() {
		SimpleDateFormat tempDate = getTimeForMatNoSec();
		String dateStr = tempDate.format(new java.util.Date()).toString();
		return dateStr;
	}

	/**
	 * 获取当前时间
	 * yyyy/MM/dd mm:ss
	 * @return
	 */
	public static String getDateTime() {
		SimpleDateFormat tempDate = getTimeForMat();
		String dateStr = tempDate.format(new java.util.Date()).toString();
		return dateStr;
	}

	public static String getFormatTime(String pData) {
		SimpleDateFormat tempDate = new SimpleDateFormat("yy" + SPLIT_Date + "MM"
				+ SPLIT_Date + "dd HH"+SPLIT_TIME+"mm"+SPLIT_TIME+"ss");
		String dateStr = tempDate.format(pData).toString();
		return dateStr;
	}

	public static SimpleDateFormat getDateNoYearFormat() {
		SimpleDateFormat tempDate = new SimpleDateFormat("MM" + SPLIT_Date
				+ "dd HH"+SPLIT_TIME+"mm"+SPLIT_TIME+"ss");
		return tempDate;
	}

	/**
	 * yyyy-MM-dd HH:mm:ss
	 *
	 * @param date
	 * @return
	 */
	public static String getDateTimeFormatMillis(String date) {
		SimpleDateFormat tempDate = getTimeForMat();
		return tempDate.format(parseDate(date));
	}

	/**
	 * yyyy-MM-dd HH:mm
	 *
	 * @param date
	 * @return
	 */
	public static String getDateTimeFormatNoSec(String date) {
		SimpleDateFormat tempDate = getTimeForMatNoSec();
		return tempDate.format(parseDate(date));
	}

	/**
	 * yyyy-MM-dd
	 *
	 * @param date
	 * @return
	 */
	public static String getDateFormatMillis(String date) {
		SimpleDateFormat tempDate = getDateForMat();
		return tempDate.format(parseDate(date));
	}


	/**
	 * MM-dd
	 *
	 * @param date
	 * @return
	 */
	public static String getDateForMatNoYear(String date) {
		SimpleDateFormat tempDate = getDateForMatNoYear();
		return tempDate.format(parseDate(date));
	}

	/**
	 * HH:mm
	 *
	 * @param date
	 * @return
	 */
	public static String getTimeFormatNoSec(String date) {
		SimpleDateFormat tempDate = getJustTimeNoSecForMat();
		return tempDate.format(parseDate(date));
	}

	public static final long parseDate(String date) {
		return parseDate(date, Calendar.getInstance().getTimeInMillis());
	}

	public static final long parseDate(String date, long fallover) {
		if (date == null || date.length() < 10)
			return fallover;
		date = date.replace("/Date(", "");
		date = date.replace("+0800)/", "");
		return Convertor.tryLong(date);
	}

	public static final int getDayOfMoon() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new java.util.Date());

		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public static final int getDayOfWeek() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new java.util.Date());

		return cal.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 得到几天前的时间
	 *
	 * @param d
	 * @param day
	 * @return
	 */
	public static java.util.Date getDateBefore(java.util.Date date, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		return now.getTime();
	}

	/**
	 * 得到几天后的时间
	 *
	 * @param d
	 * @param day
	 * @return
	 */
	public static java.util.Date getDateAfter(java.util.Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day - 1);
		return now.getTime();
	}

	/**
	 * 判断是否 dateBef 大于 dateEnd
	 *
	 * @param dateBef
	 * @param dateEnd
	 * @return
	 */
	public static boolean isDateAfter(String dateBef, String dateEnd) {
		try {

			java.util.Date dateBefore = getDateForMat().parse(dateBef);
			java.util.Date dateAfter = getDateForMat().parse(dateEnd);
			// DateFormat df = DateFormat.getDateTimeInstance();
			// System.out.println(date1.after(df.parse(date2)));
			// Log.e("ere", "ok"+(dateBefore.after(dateAfter)));
			return dateBefore.after(dateAfter);
		} catch (ParseException e) {
			System.out.print(e.getMessage());
			return false;
		}
	}

	/**
	 * 获取时间的间隔
	 *
	 * @param dateBef
	 * @param dateEnd
	 * @return
	 */
	public static int getDateScale(String dateBef, String dateEnd) {
		try {

			java.util.Date dateBefore = getDateForMat().parse(dateBef);
			java.util.Date dateAfter = getDateForMat().parse(dateEnd);

			int scale = (int) ((dateAfter.getTime() - dateBefore.getTime()) / (24 * 60 * 60 * 1000));
			return scale;
		} catch (ParseException e) {
			System.out.print(e.getMessage());
			return 0;
		}
	}

	public static boolean isTimeAfter(String dateBef, String dateEnd) {
		try {

			java.util.Date dateBefore = getJustTimeNoSecForMat().parse(dateBef);
			java.util.Date dateAfter = getJustTimeNoSecForMat().parse(dateEnd);
			// DateFormat df = DateFormat.getDateTimeInstance();
			// System.out.println(date1.after(df.parse(date2)));
			// Log.e("ere", "ok"+(dateBefore.after(dateAfter)));
			return dateBefore.after(dateAfter);
		} catch (ParseException e) {
			System.out.print(e.getMessage());
			return false;
		}
	}

	public static String formatTime(int time) {
		String tmpStr = "";
		if (time < 10) {
			tmpStr = "0" + time;
		} else {
			tmpStr = String.valueOf(time);
		}

		return tmpStr;
	}

	/*
	 * 比较时间大小
	 */

	public static Integer getDateCompareTo(String startTime, String EndTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + SPLIT_Date + "MM"
				+ SPLIT_Date + "dd" + " HH"+SPLIT_TIME+"mm");
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		try {
			c1.setTime(sdf.parse(startTime));
			c2.setTime(sdf.parse(EndTime));
		} catch (ParseException e) {
			System.err.println("格式不正确");
		}
		int result = c1.compareTo(c2);
		return result;

	}


	/**
	 * 比较时间大小
	 * @param startTime 开始时间
	 * @param EndTime	结束时间
	 * @return
	 * result:0,startTime = EndTime；
	 * result:>0,startTime > EndTime；
	 * result:<0,startTime < EndTime；
	 */
	public static Integer getDateCompareToStr(String startTime, String EndTime) {
		int result = startTime.compareTo(EndTime);
		return result;

	}

	/**
	 * 获取指定时间的间隔后的时间
	 * @param pStartTime 开始时间
	 * @param pSpand	间隔时间（分钟）
	 * @return
	 * @throws ParseException
	 */
    public static String getSpandTime(String pStartTime, int pSpand) throws ParseException {
        String timeStr = "";

        SimpleDateFormat tempDate = new SimpleDateFormat("HH"+SPLIT_TIME+"mm");

        Calendar Cal= Calendar.getInstance();
        Cal.setTime(tempDate.parse(pStartTime));
        Cal.add(Calendar.MINUTE,pSpand);
        timeStr = tempDate.format(Cal.getTime());


        return timeStr;
    }

    public static String getSpandAllTime(String pStartTime, int pSpand) throws ParseException {
        String timeStr = "";

        String tmpDate = getDateForMat().format(new java.util.Date());
        tmpDate +=" "+pStartTime;
        Calendar Cal= Calendar.getInstance();
        Cal.setTime(getTimeForMatNoSec().parse(tmpDate));
        Cal.add(Calendar.MINUTE,pSpand);
        timeStr = getTimeForMatNoSec().format(Cal.getTime());    
        
       

        return timeStr;
    }

}
