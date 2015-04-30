package hk.edu.cuhk.gosports.utils;


/**
 * constants of the app
 * 
 * @author EdwardChou edwardchou_gmail_com
 * @date 2015-4-22 下午5:52:20
 * @version V1.0
 * 
 */
public class GSConstants {

	/**
	 * database
	 */
	public static final String DATABASE_NAME = "gosports.db";
	public static final int DATABASE_VERSION = 1;
	// table
	public static final String TABLE_EVENTS = "events";
	public static final String TABLE_MY_EVENTS = "myevents";
	public static final String TABLE_CREDIT = "credit";
	public static final String TABLE_CLUB = "club";
	public static final String TABLE_USER = "user";
	// column
	public static final String COLUMN_EVENTS_ID = "evid";
	public static final String COLUMN_SEVER_EVENT_ID = "activity_id";
	public static final String COLUMN_CREATOR = "create_user_id";
	public static final String COLUMN_TITLE = "activity_name";
	public static final String COLUMN_LOCATION = "location_name";
	public static final String COLUMN_LATITUDE = "latitude";
	public static final String COLUMN_LONGTITUDE = "longitude";
	public static final String COLUMN_EXPECT_NUM = "expected_num";
	public static final String COLUMN_CURRENT_NUM = "current_num";
	public static final String COLUMN_SPORT_TYPE = "sport_type";
	public static final String COLUMN_START_TIME = "start_time";
	public static final String COLUMN_CREATE_TIME = "create_time";
	public static final String COLUMN_EXTRA_INFO = "extra_info";
	public static final String COLUMN_IS_CREATOR = "isCreator";
	public static final String COLUMN_IS_JOINED = "isJoined";

	public static final String COLUMN_FROM_USER_ID = "from_user_id";
	public static final String COLUMN_TO_USER_ID = "to_user_id";

	public static final String COLUMN_SEVER_USER_ID = "userid";
	public static final String COLUMN_USERNAME = "username";
	public static final String COLUMN_PASS = "password";
	public static final String COLUMN_AGE = "age";
	public static final String COLUMN_SEX = "sex";
	public static final String COLUMN_MAILBOX = "mailbox";
	public static final String COLUMN_CREDIT = "credit";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_ISLOGIN = "is_login_account";

	// sql statement
	public static final String SQL_CREATE_TABLE_EVENTS = "CREATE TABLE "
			+ TABLE_EVENTS + "(evid INTEGER PRIMARY KEY autoincrement,"
			+ COLUMN_SEVER_EVENT_ID + " INTEGER," + COLUMN_CREATOR
			+ " INTEGER," + COLUMN_TITLE + " TEXT," + COLUMN_LOCATION
			+ " TEXT," + COLUMN_LATITUDE + " REAL," + COLUMN_LONGTITUDE
			+ " REAL," + COLUMN_EXPECT_NUM + " INTEGER," + COLUMN_CURRENT_NUM
			+ " INTEGER," + COLUMN_SPORT_TYPE + " INTEGER," + COLUMN_START_TIME
			+ " TEXT," + COLUMN_CREATE_TIME + " TEXT,"
			+ COLUMN_EXTRA_INFO + " TEXT," + COLUMN_IS_CREATOR + " INTEGER,"
			+ COLUMN_IS_JOINED + " INTEGER)";
	public static final String SQL_CREATE_TABLE_MY_EVENTS = "CREATE TABLE "
			+ TABLE_MY_EVENTS + "(evid INTEGER PRIMARY KEY autoincrement,"
			+ COLUMN_SEVER_EVENT_ID + " INTEGER," + COLUMN_CREATOR
			+ " INTEGER," + COLUMN_TITLE + " TEXT," + COLUMN_LOCATION
			+ " TEXT," + COLUMN_LATITUDE + " REAL," + COLUMN_LONGTITUDE
			+ " REAL," + COLUMN_EXPECT_NUM + " INTEGER," + COLUMN_CURRENT_NUM
			+ " INTEGER," + COLUMN_SPORT_TYPE + " INTEGER," + COLUMN_START_TIME
			+ " TEXT," + COLUMN_CREATE_TIME + " TEXT,"
			+ COLUMN_EXTRA_INFO + " TEXT," + COLUMN_IS_CREATOR + " INTEGER,"
			+ COLUMN_IS_JOINED + " INTEGER)";
	public static final String SQL_CREATE_TABLE_CREDIT = "CREATE TABLE "
			+ TABLE_CREDIT + "(crid INTEGER PRIMARY KEY autoincrement,"
			+ COLUMN_EVENTS_ID + " INTEGER," + COLUMN_FROM_USER_ID
			+ " INTEGER," + COLUMN_TO_USER_ID + " INTEGER," + COLUMN_CREDIT
			+ " REAL," + COLUMN_EXTRA_INFO + " TEXT)";
	// public static final String SQL_CREATE_CLUB = "CREATE TABLE " + TABLE_CLUB
	// + "(clid INTEGER PRIMARY KEY autoincrement," + "username TEXT,"
	// + COLUMN_ADDRESS + " TEXT," + "password TEXT,"
	// + COLUMN_LASTLOGINTIME + " INTEGER)";
	public static final String SQL_CREATE_USER = "CREATE TABLE " + TABLE_USER
			+ "(uid INTEGER PRIMARY KEY autoincrement," + COLUMN_SEVER_USER_ID
			+ " INTEGER," + COLUMN_USERNAME + " TEXT," + COLUMN_PASS + " TEXT,"
			+ COLUMN_AGE + " INTEGER," + COLUMN_SEX + " INTEGER,"
			+ COLUMN_MAILBOX + " TEXT," + COLUMN_CREATE_TIME + " INTEGER,"
			+ COLUMN_CREDIT + " REAL," + COLUMN_DESCRIPTION + " TEXT,"
			+ COLUMN_ISLOGIN + " INTEGER)";

	public static final int MALE = 0;
	public static final int FEMALE = 1;

	public static final int SPORT_TYPE_ALL = 0;
	public static final int SPORT_TYPE_BADMINTON = 1;
	public static final int SPORT_TYPE_TENNIS = 2;
	public static final int SPORT_TYPE_FOOTBALL = 3;
	public static final int SPORT_TYPE_BASKETBALL = 4;
	public static final int SPORT_TYPE_BIKE = 5;
	public static final int SPORT_TYPE_TABLE_TENNIS = 6;
	public static final int SPORT_TYPE_BILLIARDS = 7;
	public static final int SPORT_TYPE_RUNNING = 8;
	public static final int SPORT_TYPE_HIKING = 9;
	public static final int SPORT_TYPE_SWIMMING = 10;
	public static final int SPORT_TYPE_GYM = 11;
	public static final int SPORT_TYPE_DANCE = 12;
	public static final int SPORT_TYPE_CAMP = 13;
	public static final int SPORT_TYPE_GOLF = 14;
	public static final int SPORT_TYPE_OTHERS = 15;

	public static final String URL_REGISTER = "http://52.68.117.95/create_user.php";
	public static final String URL_LOGIN = "http://52.68.117.95/login.php";
	public static final String URL_LOAD_SPORTS = "http://52.68.117.95/load_activity.php";
	public static final String URL_ADD_SPORTS = "http://52.68.117.95/create_activity.php";
	public static final String URL_JOIN_SPORTS = "http://52.68.117.95/join_activity.php";
	public static final String URL_QUIT_SPORTS = "http://52.68.117.95/quit_activity.php";

	public static final int STATUS_OK = 0;
	public static final int STATUS_FAIL = 1;
	public static final int STATUS_ERROR = 2;
	
	public static final int MENU_SPORT_LIST = 0;
	public static final int MENU_SPORT_MAP = 1;
	public static final int MENU_SPORT_DETAIL = 2;
	public static final int MENU_SPORT_ADD = 3;
	public static final int MENU_SPORT_SELECT_LOC = 4;
	public static final int MENU_USER = 5;
	public static final int MENU_DONE_ADD_RETURN_SPORT_LIST = 6;
	public static final int MENU_DISPLAY_SPORT_ON_MAP = 7;

}
