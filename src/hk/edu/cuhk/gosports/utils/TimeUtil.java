package hk.edu.cuhk.gosports.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

	/**
	 * transform millionseconds to formated date
	 * 
	 * @param dateFormat
	 *            e.g. yyyy-MM-dd HH:mm:ss
	 * @param millSec
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String transferLongToDate(long millSec) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(millSec);
		return sdf.format(date);
	}

	@SuppressLint("SimpleDateFormat")
	public static String transferDateToLong(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dt = null;
		try {
			dt = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String lTime = dt.getTime() + "";
		return lTime;
	}

	/**
	 * date to string
	 * 
	 * @param date
	 * @return String yyyy-MM-dd HH:mm:ss
	 */
	@SuppressLint("SimpleDateFormat")
	public static String transferDateToString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

}
