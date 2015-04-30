package hk.edu.cuhk.gosports.model;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * File: TimeBean.java </p> Copyright: iflyzunhong Copyright (c) 2012</p>
 * 
 * @author lau
 * @version 1.0
 * @since 2013年10月28日上午12:09:14
 * @modify
 * @description #time in timepickerDialog t1.wheel xx月xx日 周x xx时 xx分 t2.tv
 *              xxxx年xx月xx日 xx时xx分 #virtual time offset day time xx月xx日 xx时 xx分
 *              #排序必须需要年份+月份合体
 */
@SuppressLint("SimpleDateFormat")
public class TimeBean implements Serializable {
	private static final long serialVersionUID = 1181626058335468445L;
	private String year, month, day, hour, minute, weekday;
	private boolean isToday = false;
	private boolean isImm = false;
	private DateFormat weekFormat = new SimpleDateFormat("EEE");
	private DateFormat dayFormat = new SimpleDateFormat("dd");
	private DateFormat monthFormat = new SimpleDateFormat("MM");
	private DateFormat minuteFormat = new SimpleDateFormat("mm");
	private DateFormat hourFormat = new SimpleDateFormat("HH");

	public TimeBean(Calendar c, int offsetDay, int offsetHour, int offsetMinute) {
		c.add(Calendar.DAY_OF_YEAR, offsetDay);
		c.add(Calendar.HOUR_OF_DAY, offsetHour);
		c.add(Calendar.MINUTE, offsetMinute);
		year = String.valueOf(c.get(Calendar.YEAR));
		month = String.valueOf(monthFormat.format(c.getTime()));
		day = String.valueOf(dayFormat.format(c.getTime()));
		minute = String.valueOf(minuteFormat.format(c.getTime()));
		hour = String.valueOf(hourFormat.format(c.getTime()));
		weekday = String.valueOf(weekFormat.format(c.getTime()));
		isToday = offsetDay == 0
				&& c.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(
						Calendar.DAY_OF_YEAR);
	}

	public TimeBean(Calendar c, int offsetDay) {
		this(c, offsetDay, 0, 0);
	}

	public TimeBean() {
	}

	public boolean isImm() {
		return isImm;
	}

	public void setImm(boolean isImm) {
		this.isImm = isImm;
	}

	public boolean isToday() {
		return isToday;
	}

	public void setToday(boolean isToday) {
		this.isToday = isToday;
	}

	public String getYear() {
		return year + "-";
		// TODO Chinese year
		// return year + "年";
	}

	public String getMonth() {
		return month + "-";
		// TODO Chinese month
		// return month + "月";
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getDay() {
		return day;
		// TODO
		// return day + "日";
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getHour() {
		return hour + ":";
		// TODO
		// return hour + "时";
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public String getMinute() {
		// TODO
		// return minute + "分";
		return minute + ":";
	}

	public void setMinute(String minute) {
		this.minute = minute;
	}

	public String getWeekday() {
		return weekday;
	}

	public void setWeekday(String weekday) {
		this.weekday = weekday;
	}

	/**
	 * 目前仅仅为搜索
	 */
	@Override
	public String toString() {
		return year.replace("年", "") + month.replace("月", "")
				+ day.replace("日", "") + hour.replace("时", "")
				+ minute.replace("分", "");
	}

	public String toStringForConferenceModify() {
		return getMonth() + getDay() + "  " + hour + minute;
	}

	public String toStringForSport() {
		return getYear() + getMonth() + getDay() + " "
				+ getHour().replace("时", "") + getMinute().replace("分", "")
				+ "00";
	}

	public String toStringForConferenceListHm() {
		return hour.replace("时", "") + ":" + minute.replace("分", "");
	}

	public int compareTime(TimeBean timeBean) {
		return timeBean.toString().compareTo(this.toString());
	}
}
