package hk.edu.cuhk.gosports;

import hk.edu.cuhk.gosports.model.SportRequest;
import hk.edu.cuhk.gosports.model.User;
import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * maintain global parameters
 * 
 * @author EdwardChou edwardchou_gmail_com
 * @date 2015-4-20 PM5:13:39
 * @version V1.0
 * 
 */
public class GSApplication extends Application {
	public static final String TAG = "GSApplication";

	private static GSApplication instance;
	public static User user;
	public static SportRequest sportRequest;
	public static boolean hasLogin = false;

	@Override
	public void onCreate() {
		// initialize the singleton
		instance = this;
		user = new User();
		sportRequest = new SportRequest();
		// initialize context before use baidu map SDK, set ApplicationContext
		// as context, remember this method should be call before
		// setContentView()
		SDKInitializer.initialize(getApplicationContext());
		super.onCreate();
	}

	/**
	 * singleton instance
	 * 
	 * @return GSApplication instance
	 */
	public static synchronized GSApplication getInstance() {
		return instance;
	}

	public static User getUser() {
		return user;
	}

	public static void setUser(User user) {
		GSApplication.user = user;
	}

	public static SportRequest getSportRequest() {
		return sportRequest;
	}

	public static void setSportRequest(SportRequest sportRequest) {
		GSApplication.sportRequest = sportRequest;
	}

}
