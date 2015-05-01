package hk.edu.cuhk.gosports.database;

import hk.edu.cuhk.gosports.model.Sport;
import hk.edu.cuhk.gosports.model.User;
import hk.edu.cuhk.gosports.utils.EncryptionUtil;
import hk.edu.cuhk.gosports.utils.GSConstants;
import hk.edu.cuhk.gosports.utils.TimeUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Database provider for CURD operation
 * 
 * @author EdwardChou edwardchou_gmail_com
 * @date 2015-4-22 下午5:50:55
 * @version V1.0
 * 
 */
public class GSProvider {

	private final String TAG = "GSProvider";

	private static GSHelper mGSHelper;
	private SQLiteDatabase db;
	private static GSProvider instance;

	public GSProvider(Context context) {
		mGSHelper = new GSHelper(context);
	}

	/**
	 * Singleton instance
	 * 
	 * @param context
	 */
	public static synchronized void init(Context context) {
		if (instance == null) {
			instance = new GSProvider(context);
		}
	}

	public static synchronized GSProvider getInstance() {
		if (instance == null) {
			throw new IllegalStateException(GSProvider.class.getSimpleName()
					+ "is not initialized, call init(..) method first");
		}
		return instance;
	}

	public static void closeDB() {
		mGSHelper.close();
	}

	/**
	 * basic insert, delete, query, update operation line start
	 */
	public int insertRecord(String table, String nullColumnHack,
			ContentValues values) {
		if (null == db || db.isReadOnly()) {
			db = mGSHelper.getWritableDatabase();
		}
		if (null == values || table == null || "".equals(table)) {
			Log.i(TAG, "insert: table or values parameters is null !");
			return -1;
		}
		return (int) db.insert(table, nullColumnHack, values);
	}

	public boolean deleteRecord(String table, String whereClause,
			String[] whereArgs) {
		if (null == db || db.isReadOnly()) {
			db = mGSHelper.getWritableDatabase();
		}
		if (null == table || null == whereClause || null == whereArgs) {
			Log.i(TAG,
					"delete: table or whereClause or whereArgs parameters is null !");
			return false;
		}
		return 0 == db.delete(table, whereClause, whereArgs) ? false : true;
	}

	public Cursor queryRecord(String table, String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy) {
		if (null == db || !db.isReadOnly()) {
			db = mGSHelper.getReadableDatabase();
		}
		if (null == table) {
			Log.i(TAG, "query: table parameter is null !");
			return null;
		}
		return db.query(table, columns, selection, selectionArgs, groupBy,
				having, orderBy);
	}

	public boolean updateRecord(String table, ContentValues values,
			String whereClause, String[] whereArgs) {
		if (null == db || db.isReadOnly()) {
			db = mGSHelper.getWritableDatabase();
		}
		if (null == table || null == values) {
			Log.i(TAG, "update: table or values parameters is null !");
			return false;
		}
		return 0 == db.update(table, values, whereClause, whereArgs);
	}

	/**
	 * line end
	 */

	/**
	 * query user info from database
	 * 
	 * @param isLogin
	 *            account
	 * @return {@link hk.edu.cuhk.gosports.model.User}
	 */
	public User queryMyUser(boolean isLogin) {
		User user = null;
		Cursor cursor = queryRecord(GSConstants.TABLE_USER, null,
				GSConstants.COLUMN_ISLOGIN + "=?", new String[] { "1" }, null,
				null, GSConstants.COLUMN_LOGIN_TIMESATMP + " desc");
		if (null != cursor) {
			if (cursor.moveToNext()) {
				user = new User();
				user.setUserId(cursor.getInt(0));
				user.setUserServerId(cursor.getInt(1));
				user.setUsername(cursor.getString(2));
				if (isLogin) {
					user.setPassword(EncryptionUtil.decrypt(cursor.getString(3)));
				}
				user.setAge(cursor.getInt(4));
				user.setSex(cursor.getInt(5));
				user.setMailbox(cursor.getString(6));
				user.setCredit((int) cursor.getFloat(8));
				user.setDescription(cursor.getString(9));
			}
		}
		cursor.close();
		return user;
	}

	/**
	 * get sports from database
	 */
	public List<Sport> getSports() {
		return getSports(null, null);
	}

	public Sport getSport(int sportId) {
		Sport s = null;
		Cursor cursor = queryRecord(GSConstants.TABLE_EVENTS, null,
				GSConstants.COLUMN_EVENTS_ID + "=?", new String[] { sportId
						+ "" }, null, null, null);
		List<Sport> result = getSportsFromCursor(cursor);
		s = result.get(0);
		return s;
	}

	public List<Sport> getSports(String selection, String[] selectionArgs) {
		List<Sport> result = null;
		Cursor cursor = queryRecord(GSConstants.TABLE_EVENTS, null, selection,
				selectionArgs, null, null, GSConstants.COLUMN_CREATE_TIME
						+ " desc");
		result = getSportsFromCursor(cursor);
		return result;
	}

	private List<Sport> getSportsFromCursor(Cursor cursor) {
		List<Sport> result = null;
		if (null != cursor) {
			result = new ArrayList<Sport>();
			while (cursor.moveToNext()) {
				Sport sport = new Sport();
				sport.setSportID(cursor.getInt(0));
				sport.setSportServerID(cursor.getInt(1));
				sport.setCreateUserID(cursor.getInt(2));
				sport.setEventTitle(cursor.getString(3));
				sport.setLocation(cursor.getString(4));
				sport.setLatitude(cursor.getDouble(5));
				sport.setLongitude(cursor.getDouble(6));
				sport.setExpectNum(cursor.getInt(7));
				sport.setCurrentNum(cursor.getInt(8));
				sport.setSportType(cursor.getInt(9));
				sport.setStartTime(TimeUtil.transferLongToDate(cursor
						.getLong(10)));
				sport.setCreateTime(TimeUtil.transferLongToDate(cursor
						.getLong(11)));
				sport.setExtraInfo(cursor.getString(12));
				sport.setCreator(cursor.getInt(13) == 1 ? true : false);
				sport.setJoined(cursor.getInt(14) == 1 ? true : false);
				result.add(sport);
			}
		}
		cursor.close();
		return result;
	}

	public boolean insertUser(User user) {
		if (null == user) {
			Log.i(TAG, "user info is null !");
			return false;
		}
		ContentValues values = getUserContentValues(user);
		return -1 == insertRecord(GSConstants.TABLE_USER, null, values) ? false
				: true;
	}

	private ContentValues getUserContentValues(User user) {
		ContentValues values = new ContentValues();
		values.put(GSConstants.COLUMN_SEVER_USER_ID, user.getUserServerId());
		values.put(GSConstants.COLUMN_USERNAME, user.getUsername());
		values.put(GSConstants.COLUMN_PASS,
				EncryptionUtil.encrypt(user.getPassword()));
		values.put(GSConstants.COLUMN_AGE, user.getAge());
		values.put(GSConstants.COLUMN_SEX, user.getSex());
		values.put(GSConstants.COLUMN_MAILBOX, user.getMailbox());
		values.put(GSConstants.COLUMN_CREDIT, user.getCredit());
		values.put(GSConstants.COLUMN_DESCRIPTION, user.getDescription());
		values.put(GSConstants.COLUMN_ISLOGIN, user.isLogin());
		values.put(GSConstants.COLUMN_LOGIN_TIMESATMP,
				System.currentTimeMillis() + "");
		return values;
	}

	/**
	 * retrieve the cached sports serverId
	 * 
	 * @return id set
	 */
	public Set<Integer> getCachedSportServerIdSet() {
		Set<Integer> cachedSportServerId = null;
		Cursor cursor = queryRecord(GSConstants.TABLE_EVENTS,
				new String[] { GSConstants.COLUMN_SEVER_EVENT_ID }, null, null,
				null, null, null);
		if (cursor != null) {
			cachedSportServerId = new HashSet<Integer>();
			while (cursor.moveToNext()) {
				cachedSportServerId.add(cursor.getInt(0));
			}
		}
		cursor.close();
		return cachedSportServerId;
	}

	/**
	 * insert sport to database
	 * 
	 * @param sport
	 * @param cachedSportServerId
	 * @return insert status
	 */
	public boolean insertSports(Sport sport, Set<Integer> cachedSportServerId) {
		if (null == sport) {
			Log.i(TAG, "sports info is null !");
			return false;
		}
		if (cachedSportServerId != null) {
			if (cachedSportServerId.contains(sport.getSportServerID())) {
				return false;
			}
			Log.i(TAG, "insert sport to db");
			ContentValues values = getSportsContentValues(sport);
			return -1 == insertRecord(GSConstants.TABLE_EVENTS, null, values) ? false
					: true;
		}
		return false;
	}

	private ContentValues getSportsContentValues(Sport sport) {
		ContentValues values = new ContentValues();
		values.put(GSConstants.COLUMN_SEVER_EVENT_ID, sport.getSportServerID());
		values.put(GSConstants.COLUMN_CREATOR, sport.getCreateUserID());
		values.put(GSConstants.COLUMN_TITLE, sport.getEventTitle());
		values.put(GSConstants.COLUMN_LOCATION, sport.getLocation());
		values.put(GSConstants.COLUMN_LATITUDE, sport.getLatitude());
		values.put(GSConstants.COLUMN_LONGTITUDE, sport.getLongitude());
		values.put(GSConstants.COLUMN_EXPECT_NUM, sport.getExpectNum());
		values.put(GSConstants.COLUMN_CURRENT_NUM, sport.getCurrentNum());
		values.put(GSConstants.COLUMN_EVENT_TYPE, sport.getSportType());
		values.put(GSConstants.COLUMN_START_TIME,
				TimeUtil.transferDateToLong(sport.getStartTime()));
		values.put(GSConstants.COLUMN_CREATE_TIME,
				TimeUtil.transferDateToLong(sport.getCreateTime()));
		values.put(GSConstants.COLUMN_EXTRA_INFO, sport.getExtraInfo());
		values.put(GSConstants.COLUMN_IS_CREATOR, sport.isCreator() ? "1" : "0");
		values.put(GSConstants.COLUMN_IS_JOINED, sport.isJoined() ? "1" : "0");
		return values;
	}

}
