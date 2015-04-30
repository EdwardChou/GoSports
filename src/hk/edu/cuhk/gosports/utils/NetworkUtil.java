package hk.edu.cuhk.gosports.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * check network connection
 * 
 * @author EdwardChou edwardchou_gmail_com
 * @date 2015-4-29 PM5:14:59
 * @version V1.0
 * 
 */
public class NetworkUtil {

	/**
	 * check whether network is available
	 * 
	 * @param context
	 * @return is connected
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected())
			return true;
		return false;
	}

}
