package hk.edu.cuhk.gosports.service;

import hk.edu.cuhk.gosports.database.GSProvider;
import hk.edu.cuhk.gosports.model.Messager;
import hk.edu.cuhk.gosports.model.Notify;
import hk.edu.cuhk.gosports.model.Sport;
import hk.edu.cuhk.gosports.model.Task;
import hk.edu.cuhk.gosports.utils.TaskDetailOperation;

import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

/**
 * download service
 * 
 * @author EdwardChou edwardchou_gmail_com
 * @date 2015-4-21 下午10:48:37
 * @version V1.0
 * 
 */
public class MainService extends Service implements Runnable {

	private static final String TAG = "MainService";
	// service running flags
	public static boolean isRun = false;
	// notify interface list
	private static ArrayList<Notify> allActivity = new ArrayList<Notify>();
	// task configuration info
	private static ArrayList<Task> allTasks = new ArrayList<Task>();

	private final IBinder mBinder = new LocalBinder();

	public class LocalBinder extends Binder {
		public MainService getService() {
			return MainService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		GSProvider.init(getApplicationContext());
		isRun = true;
		// start new thread
		new Thread(this).start();
		Log.i(TAG, "MainService onCreate()");
	}

	@Override
	public void onDestroy() {
		isRun = false;
		super.onDestroy();
		GSProvider.closeDB();
	}

	/**
	 * add new task to new thread list
	 * 
	 * @param task
	 */
	public static void addNewTask(Task t) {
		Log.i(TAG, "newTask");
		allTasks.add(t);
	}

	/**
	 * add new task and interface to new thread list
	 * 
	 * @param task
	 * @param notify
	 */
	public static void addNewTask(Task t, Notify n) {
		Log.i(TAG, "newTask");
		allTasks.add(t);
		allActivity.add(n);
	}

	/**
	 * add new activity notify interface
	 * 
	 * @param notify
	 */
	public static void addActivity(Notify n) {
		Log.i(TAG, "addActivity");
		allActivity.add(n);
	}

	/**
	 * remove activity notify interface from the list
	 * 
	 * @param notify
	 */
	public static void removeActivity(Notify n) {
		allActivity.remove(n);
	}

	/**
	 * get activity notify interface by class name
	 * 
	 * @param className
	 * @return Notify
	 */
	public static Notify getActivityByName(String name) {
		for (Notify n : allActivity) {
			if (n.getClass().getName().indexOf(name) >= 0) {
				return n;
			}
		}
		return null;
	}

	// task opertaion end

	@Override
	public void run() {
		// if service is running
		while (isRun) {
			try {
				if (allTasks.size() > 0) {
					doTask(allTasks.get(0));
				} else {
					try {
						Thread.sleep(2000);
					} catch (Exception e) {
					}
				}
			} catch (Exception e) {
				if (allTasks.size() > 0)
					allTasks.remove(allTasks.get(0));
				Log.e(TAG, e.toString());
			}
		}
	}

	private void doTask(Task task) {
		Message msg = handler.obtainMessage();
		msg.what = task.getTaskId();

		switch (task.getTaskId()) {
		case Task.REGISTER:
			msg.obj = TaskDetailOperation.register(getApplicationContext());
			break;
		case Task.LOGIN:
			msg.obj = TaskDetailOperation.login(getApplicationContext());
			break;
		case Task.USER_INFO:
			break;
		case Task.LOAD_DETAIL_SPORTS:
			int sportIds = (Integer) task.getTaskParams().get("sportId");
			msg.obj = TaskDetailOperation.LoadSportsFromDB(
					getApplicationContext(), sportIds, -1, false, false, -0.0f,
					-0.0f, 0.0);
			break;
		case Task.LOAD_DB_SPORTS:
			boolean advance = (Boolean) task.getTaskParams().containsKey(
					"advanceSearch");
			if (advance) {
				if (task.getTaskParams().containsKey("sportId")) {
					int sportId = (Integer) task.getTaskParams().get("sportId");
					msg.obj = TaskDetailOperation.LoadSportsFromDB(
							getApplicationContext(), sportId, -1, false, false,
							-0.0f, -0.0f, 0.0);
				}
				if (task.getTaskParams().containsKey("sportType")) {
					int sportType = (Integer) task.getTaskParams().get(
							"sportType");
					msg.obj = TaskDetailOperation.LoadSportsFromDB(
							getApplicationContext(), -1, sportType, false,
							false, -0.0f, -0.0f, 0.0);
				}
				if (task.getTaskParams().containsKey("isCreator")) {
					msg.obj = TaskDetailOperation.LoadSportsFromDB(
							getApplicationContext(), -1, -1, true, false,
							-0.0f, -0.0f, 0.0);
				}
				if (task.getTaskParams().containsKey("hasJoin")) {
					msg.obj = TaskDetailOperation.LoadSportsFromDB(
							getApplicationContext(), -1, -1, false, true,
							-0.0f, -0.0f, 0.0);
				}
				if (task.getTaskParams().containsKey("longtitude")) {
					// TODO add longtitude search
				}
			} else {
				msg.obj = TaskDetailOperation
						.LoadSportsFromDB(getApplicationContext());
			}
			break;
		case Task.LOAD_NET_SPORTS:
			msg.obj = TaskDetailOperation
					.LoadSportsFromNet(getApplicationContext());
			break;
		case Task.ADD_NEW_SPORT_TO_SERVER:
			break;
		case Task.DONE_ADD_NEW_SPORT_TO_SERVER:
			msg.obj = TaskDetailOperation.SubmitSports2Server(
					getApplicationContext(),
					(Sport) task.getTaskParams().get("sport"));
			break;
		case Task.ATTEND_EVENTS:
			int sportID = (Integer) task.getTaskParams().get("sportId");
			msg.obj = TaskDetailOperation.joinOrQuitEvent(
					getApplicationContext(), sportID, true);
			break;
		case Task.QUIT_EVENT:
			int sportId = (Integer) task.getTaskParams().get("sportId");
			msg.obj = TaskDetailOperation.joinOrQuitEvent(
					getApplicationContext(), sportId, false);
			break;
		default:
			break;
		}
		allTasks.remove(task);
		handler.sendMessage(msg);
	}

	private static Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case Task.REGISTER:
				MainService.getActivityByName("RegisterActivity").refresh(
						msg.obj);
				break;
			case Task.LOGIN:
				MainService.getActivityByName("LoginActivity").refresh(msg.obj);
				break;
			case Task.LOAD_DETAIL_SPORTS:
				MainService.getActivityByName("SportsDetailFragment").refresh(
						msg.obj);
				break;
			case Task.LOAD_DB_SPORTS:
				if (msg.obj != null) {
					MainService.getActivityByName("SportsFragment").refresh(
							msg.obj);
				}
				break;
			case Task.LOAD_NET_SPORTS:
				Log.i(TAG,
						"send to login type" + ((Messager) msg.obj).getType());
				MainService.getActivityByName("SportsFragment")
						.refresh(msg.obj);
				break;
			case Task.USER_INFO:
				MainService.getActivityByName("UserFragment").refresh(msg.obj);
				break;
			case Task.ADD_NEW_SPORT_TO_SERVER:
				break;
			case Task.DONE_ADD_NEW_SPORT_TO_SERVER:
				MainService.getActivityByName("CreateSportsFragment").refresh(
						msg.obj);
				break;
			case Task.ATTEND_EVENTS:
				MainService.getActivityByName("SportsDetailFragment").refresh(
						msg.obj);
				break;
			case Task.QUIT_EVENT:
				MainService.getActivityByName("SportsDetailFragment").refresh(
						msg.obj);
				break;
			default:
				break;
			}
		}

	};

}
