package hk.edu.cuhk.gosports.utils;

import hk.edu.cuhk.gosports.GSApplication;
import hk.edu.cuhk.gosports.database.GSProvider;
import hk.edu.cuhk.gosports.model.Messager;
import hk.edu.cuhk.gosports.model.Sport;
import hk.edu.cuhk.gosports.model.Task;
import hk.edu.cuhk.gosports.model.User;

import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

/**
 * detail operation
 * 
 * @author EdwardChou edwardchou_gmail_com
 * @date 2015-4-23 上午11:46:02
 * @version V1.0
 * 
 */
public class TaskDetailOperation {

	private static final String TAG = "TaskDetailOperation";

	/**
	 * register operation, retrieve account info from GSApplcaiton
	 * 
	 * @param context
	 * @return {@link hk.edu.cuhk.gosports.model.Messager}
	 */
	public static Messager register(Context context) {
		JSONObject para = new JSONObject();
		try {
			para.put("user_name", GSApplication.user.getUsername());
			para.put("password", GSApplication.user.getPassword());
			para.put("age", GSApplication.user.getAge() + "");
			para.put("sex", GSApplication.user.getSex() + "");
			para.put("mailbox", GSApplication.user.getMailbox());
			para.put("description", GSApplication.user.getDescription());
			String result = HttpUtil.postRequest(GSConstants.URL_REGISTER,
					para, context);
			if (result != null) {
				JSONObject object;
				object = new JSONObject(result);
				String status = object.getString("message");
				if ("OK".equals(status)) {
					GSApplication.user.setUserServerId(object.getJSONObject(
							"output").getInt("userid"));
					GSApplication.user.setLogin(true);
					GSProvider.getInstance().insertUser(GSApplication.user);
					return new Messager(Task.REGISTER, GSConstants.STATUS_OK,
							object.getString("output"));
				}
				if ("FAIL".equals(status)) {
					return new Messager(Task.REGISTER, GSConstants.STATUS_FAIL,
							object.getString("output"));
				}
				if ("ERROR".equals(status)) {
					return new Messager(Task.REGISTER,
							GSConstants.STATUS_ERROR,
							object.getString("output"));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return new Messager(Task.REGISTER, GSConstants.STATUS_FAIL,
				"Network error");
	}

	/**
	 * login
	 * 
	 * @param context
	 * @return {@link hk.edu.cuhk.gosports.model.Messager}
	 */
	public static Messager login(Context context) {
		User user = GSProvider.getInstance().queryMyUser(true);
		if (user != null)
			GSApplication.setUser(user);
		try {
			if (GSApplication.user != null) {
				JSONObject parameters = new JSONObject();
				parameters.put("user_name", GSApplication.user.getUsername());
				parameters.put("password", GSApplication.user.getPassword());
				// Log.e(TAG, "name=" + GSApplication.user.getUsername()
				// + ",pass=" + GSApplication.user.getPassword());
				String loginResult = HttpUtil.postRequest(
						GSConstants.URL_LOGIN, parameters, context);
				if (loginResult != null) {
					JSONObject loginObject;
					loginObject = new JSONObject(loginResult);
					String loginStatus = loginObject.getString("message");
					if ("OK".equals(loginStatus)) {
						GSApplication.user.setUserServerId(loginObject
								.getJSONObject("output").getInt("userid"));
						GSApplication.user.setAge(loginObject.getJSONObject(
								"output").getInt("age"));
						GSApplication.user.setCredit(loginObject.getJSONObject(
								"output").getInt("credit"));
						GSApplication.user.setDescription(loginObject
								.getJSONObject("output").getString(
										"description"));
						GSApplication.user.setLogin(true);
						GSApplication.user.setMailbox(loginObject
								.getJSONObject("output").getString("mailbox"));
						GSApplication.user.setSex(loginObject.getJSONObject(
								"output").getInt("sex"));
						GSProvider.getInstance().insertUser(GSApplication.user);
						return new Messager(Task.LOGIN, GSConstants.STATUS_OK,
								"");
					}
					if ("FAIL".equals(loginStatus)) {
						return new Messager(Task.LOGIN,
								GSConstants.STATUS_FAIL,
								loginObject.getString("output"));
					}
					if ("ERROR".equals(loginStatus)) {
						return new Messager(Task.LOGIN,
								GSConstants.STATUS_ERROR,
								loginObject.getString("output"));
					}
				} else {
					return new Messager(Task.LOGIN, GSConstants.STATUS_FAIL,
							"Network error");
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return new Messager(Task.LOGIN, GSConstants.STATUS_FAIL,
				"No user cached");
	}

	/**
	 * load sports from Internet
	 * 
	 * @param context
	 * @return {@link hk.edu.cuhk.gosports.model.Messager}
	 */
	public static Messager LoadSportsFromNet(Context context) {
		JSONObject parameters = new JSONObject();
		try {
			parameters.put("userid", GSApplication.sportRequest.getUserId()
					+ "");
			parameters.put("latitude", GSApplication.sportRequest.getLatitude()
					+ "");
			parameters.put("longitude",
					GSApplication.sportRequest.getLongitude() + "");
			parameters.put("range", GSApplication.sportRequest.getRange() + "");
			parameters.put("expect_time_start", TimeUtil
					.transferLongToDate(GSApplication.sportRequest
							.getExpectTimeStart()));
			parameters.put("expect_time_end", TimeUtil
					.transferLongToDate(GSApplication.sportRequest
							.getExpectTimeEnd()));
			parameters.put("load_sport_type",
					GSApplication.sportRequest.getLoadSportType() + "");
			Log.e(TAG,
					"LoadSportsFromNet() start time:"
							+ parameters.getString("expect_time_start")
							+ ",end time:"
							+ parameters.getString("expect_time_end"));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		Log.i(TAG, GSApplication.sportRequest.toString());
		String result = HttpUtil.postRequest(GSConstants.URL_LOAD_SPORTS,
				parameters, context);
		if (result != null) {
			JSONObject object;
			try {
				object = new JSONObject(result);
				String status = object.getString("message");
				if ("OK".equals(status)) {
					JSONArray array = object.getJSONArray("output");
					Set<Integer> cachedSportServerIds = GSProvider
							.getInstance().getCachedSportServerIdSet();
					for (int i = 0; i < array.length(); i++) {
						Sport sport = new Sport();
						JSONObject obj = (JSONObject) array.get(i);
						if (null != obj) {
							sport.setSportServerID(obj.getInt("activity_id"));
							sport.setCreateUserID(obj.getInt("user_id"));
							sport.setEventTitle(obj.getString("activity_name"));
							sport.setLocation(obj.getString("location_name"));
							sport.setLatitude(obj.getDouble("latitude"));
							sport.setLongitude(obj.getDouble("longitude"));
							sport.setExpectNum(obj.getInt("expected_num"));
							sport.setCurrentNum(obj.getInt("current_num"));
							sport.setSportType(obj.getInt("sport_type"));
							sport.setStartTime(obj.getString("start_time"));
							sport.setCreateTime(obj.getString("create_time"));
							sport.setExtraInfo(obj.getString("extra_info"));
							sport.setCreator(obj.getInt("isCreator") == 1 ? true
									: false);
							sport.setJoined(obj.getInt("isJoined") == 1 ? true
									: false);
							GSProvider.getInstance().insertSports(sport,
									cachedSportServerIds);
						}
					}
					return new Messager(Task.LOAD_NET_SPORTS,
							GSConstants.STATUS_OK, "load " + array.length()
									+ " sports");
				}
				if ("FAIL".equals(status)) {
					return new Messager(Task.LOAD_NET_SPORTS,
							GSConstants.STATUS_FAIL, object.getString("output"));
				}
				if ("ERROR".equals(status)) {
					return new Messager(Task.LOAD_NET_SPORTS,
							GSConstants.STATUS_ERROR,
							object.getString("output"));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return new Messager(Task.LOAD_NET_SPORTS, GSConstants.STATUS_FAIL,
				"Network error");
	}

	/**
	 * load sports from database
	 * 
	 * @param context
	 * @return {@link hk.edu.cuhk.gosports.model.Messager}
	 */

	public static Messager LoadSportsFromDB(Context context) {
		List<Sport> sports = GSProvider.getInstance().getSports();
		if (null != sports) {
			return new Messager(Task.LOAD_DB_SPORTS, GSConstants.STATUS_OK,
					sports);
		}
		return new Messager(Task.LOAD_DB_SPORTS, GSConstants.STATUS_FAIL,
				"No sports cached");
	}

	/**
	 * load sports from database, only support single condition search
	 * 
	 * @param context
	 * @param sportId
	 *            if no need, set to -1
	 * @param sportType
	 *            if no need, set to -1
	 * @param isCreator
	 *            if no need, set to false
	 * @param hasJoin
	 *            if no need, set to false
	 * @param longtitude
	 *            if no need, set to 0.0
	 * @param latitude
	 *            if no need, set to 0.0
	 * @param range
	 *            if no need, set to 0.0
	 * @return {@link hk.edu.cuhk.gosports.model.Messager}
	 */
	// FIXME task id
	public static Messager LoadSportsFromDB(Context context, int sportId,
			int sportType, boolean isCreator, boolean hasJoin,
			double longtitude, double latitude, double range) {
		if (sportId != -1) {
			Sport sport = GSProvider.getInstance().getSport(sportId);
			if (null != sport) {
				return new Messager(Task.LOAD_DETAIL_SPORTS,
						GSConstants.STATUS_OK, sport);
			}
		}
		if (sportType != -1) {
			List<Sport> sports = GSProvider.getInstance().getSports(
					GSConstants.COLUMN_EVENT_TYPE + "=?",
					new String[] { sportType + "" });
			if (null != sports) {
				return new Messager(Task.LOAD_DETAIL_SPORTS,
						GSConstants.STATUS_OK, sports);
			}
		}
		if (isCreator) {
			List<Sport> sports = GSProvider.getInstance().getSports(
					GSConstants.COLUMN_IS_CREATOR + "=?",
					new String[] { isCreator ? "1" : "0" });
			if (null != sports) {
				return new Messager(Task.LOAD_DETAIL_SPORTS,
						GSConstants.STATUS_OK, sports);
			}
		}
		if (hasJoin) {
			List<Sport> sports = GSProvider.getInstance().getSports(
					GSConstants.COLUMN_IS_JOINED + "=?",
					new String[] { hasJoin ? "1" : "0" });
			if (null != sports) {
				return new Messager(Task.LOAD_DETAIL_SPORTS,
						GSConstants.STATUS_OK, sports);
			}
		}
		if (range > 0.0) {
			// TODO find a better way to calculate geo value
			List<Sport> sports = GSProvider.getInstance().getSports(
					GSConstants.COLUMN_EVENT_TYPE + "=?",
					new String[] { sportType + "" });
			if (null != sports) {
				return new Messager(Task.LOAD_DETAIL_SPORTS,
						GSConstants.STATUS_OK, sports);
			}
		}
		return new Messager(Task.LOAD_DETAIL_SPORTS, GSConstants.STATUS_FAIL,
				"No sports cached");
	}

	/**
	 * add sports to server and return sportId from server and insert to local
	 * database
	 * 
	 * @param context
	 * @param sport
	 * @return {@link hk.edu.cuhk.gosports.model.Messager}
	 */
	public static Messager SubmitSports2Server(Context context, Sport sport) {
		try {
			JSONObject parameters = new JSONObject();
			parameters.put("userid", GSApplication.sportRequest.getUserId()
					+ "");
			// parameters.put("geohash", sport.getGeoHash());
			parameters.put("activity_name", sport.getEventTitle());
			parameters.put("latitude", sport.getLatitude() + "");
			parameters.put("longitude", sport.getLongitude() + "");
			parameters.put("activity_start_time", sport.getStartTime());
			parameters.put("sport_type", sport.getSportType() + "");
			parameters.put("current_num", "1");
			parameters.put("expected_num", sport.getExpectNum() + "");
			parameters.put("extra_info", sport.getExtraInfo());
			String result = HttpUtil.postRequest(GSConstants.URL_ADD_SPORTS,
					parameters, context);
			if (result != null) {
				JSONObject object;
				object = new JSONObject(result);
				String status = object.getString("message");
				if ("OK".equals(status)) {
					Set<Integer> cachedSportServerIds = GSProvider
							.getInstance().getCachedSportServerIdSet();
					sport.setSportServerID(Integer.parseInt(object
							.getJSONObject("output").getString("activity_id")));
					sport.setCurrentNum(1);
					sport.setCreator(true);
					sport.setJoined(true);
					boolean successInsert = GSProvider.getInstance()
							.insertSports(sport, cachedSportServerIds);
					return new Messager(Task.DONE_ADD_NEW_SPORT_TO_SERVER,
							GSConstants.STATUS_OK, "insert sport to db status:"
									+ successInsert);
				}
				if ("FAIL".equals(status)) {
					return new Messager(Task.DONE_ADD_NEW_SPORT_TO_SERVER,
							GSConstants.STATUS_FAIL, object.getString("output"));
				}
				if ("ERROR".equals(status)) {
					return new Messager(Task.DONE_ADD_NEW_SPORT_TO_SERVER,
							GSConstants.STATUS_ERROR,
							object.getString("output"));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return new Messager(Task.DONE_ADD_NEW_SPORT_TO_SERVER,
				GSConstants.STATUS_FAIL, "Network error");
	}

	/**
	 * join or quit events request
	 * 
	 * @param context
	 * @param sportId
	 * @param isJoin
	 * @return {@link hk.edu.cuhk.gosports.model.Messager}
	 */
	public static Messager joinOrQuitEvent(Context context, int sportId,
			boolean isJoin) {
		List<Sport> queryResult = GSProvider.getInstance().getSports(
				GSConstants.COLUMN_EVENTS_ID + "=?",
				new String[] { sportId + "" });
		if (queryResult != null) {
			Sport sport = queryResult.get(0);
			if (isJoin != sport.isJoined()) {
				ContentValues values = new ContentValues();
				values.put(GSConstants.COLUMN_IS_JOINED, isJoin ? 1 : 0);
				GSProvider.getInstance().updateRecord(GSConstants.TABLE_EVENTS,
						values, GSConstants.COLUMN_EVENTS_ID + "=?",
						new String[] { sportId + "" });
				try {
					JSONObject parameters = new JSONObject();
					parameters.put("userid",
							GSApplication.sportRequest.getUserId() + "");
					parameters.put("activity_id", sportId);
					String result = HttpUtil.postRequest(
							isJoin ? GSConstants.URL_JOIN_SPORTS
									: GSConstants.URL_QUIT_SPORTS, parameters,
							context);
					if (result != null) {
						JSONObject object;
						object = new JSONObject(result);
						String status = object.getString("message");
						if ("OK".equals(status)) {
							return new Messager(isJoin ? Task.ATTEND_EVENTS
									: Task.QUIT_EVENT, GSConstants.STATUS_OK,
									"");
						}
						if ("FAIL".equals(status)) {
							return new Messager(isJoin ? Task.ATTEND_EVENTS
									: Task.QUIT_EVENT, GSConstants.STATUS_FAIL,
									object.getString("output"));
						}
						if ("ERROR".equals(status)) {
							return new Messager(isJoin ? Task.ATTEND_EVENTS
									: Task.QUIT_EVENT,
									GSConstants.STATUS_ERROR,
									object.getString("output"));
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return new Messager(isJoin ? Task.ATTEND_EVENTS : Task.QUIT_EVENT,
				GSConstants.STATUS_FAIL, "Network error");
	}
}
