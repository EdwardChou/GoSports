package hk.edu.cuhk.gosports.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * view utility
 * 
 * @author EdwardChou edwardchou_gmail_com
 * @date 2015-4-27 下午2:52:04
 * @version V1.0
 * 
 */
public class ViewUtil {

	/**
	 * screen width
	 * 
	 * @param context
	 * @return width
	 */
	public static int getWindowWidth(Context context) {
		DisplayMetrics dm = new android.util.DisplayMetrics();
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	/**
	 * screen height
	 * 
	 * @param context
	 * @return height
	 */
	public static int getWindowHeight(Context context) {
		DisplayMetrics dm = new android.util.DisplayMetrics();
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}
	
	public static String getSportName(int sportType) {
		switch (sportType) {
		case GSConstants.SPORT_TYPE_BADMINTON:
			return "Badminton";
		case GSConstants.SPORT_TYPE_TENNIS:
			return "Tennis";
		case GSConstants.SPORT_TYPE_FOOTBALL:
			return "Football";
		case GSConstants.SPORT_TYPE_BASKETBALL:
			return "Basketball";
		case GSConstants.SPORT_TYPE_BIKE:
			return "Bike";
		case GSConstants.SPORT_TYPE_TABLE_TENNIS:
			return "Table tennis";
		case GSConstants.SPORT_TYPE_BILLIARDS:
			return "Billiards";
		case GSConstants.SPORT_TYPE_RUNNING:
			return "Running";
		case GSConstants.SPORT_TYPE_HIKING:
			return "Hiking";
		case GSConstants.SPORT_TYPE_SWIMMING:
			return "Swimming";
		case GSConstants.SPORT_TYPE_GYM:
			return "Gym";
		case GSConstants.SPORT_TYPE_DANCE:
			return "Dance";
		case GSConstants.SPORT_TYPE_CAMP:
			return "Camp";
		case GSConstants.SPORT_TYPE_GOLF:
			return "Golf";
		case GSConstants.SPORT_TYPE_OTHERS:
			return "Others";
		default:
			break;
		}
		return "";
	}
}
